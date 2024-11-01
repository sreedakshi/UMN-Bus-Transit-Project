package edu.umn.cs.csci3081w.project.webserver;

import edu.umn.cs.csci3081w.project.model.Bus;
import edu.umn.cs.csci3081w.project.model.BusCreator;
import edu.umn.cs.csci3081w.project.model.BusSubject;
import edu.umn.cs.csci3081w.project.model.ConcreteBusSubject;
import edu.umn.cs.csci3081w.project.model.ConcreteStopSubject;
import edu.umn.cs.csci3081w.project.model.GoldBusDecorator;
import edu.umn.cs.csci3081w.project.model.MaroonDecorator;
import edu.umn.cs.csci3081w.project.model.OverallConcreteBusCreator;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Stop;
import edu.umn.cs.csci3081w.project.model.StopSubject;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VisualizationSimulator {

  private WebInterface webInterface;
  private ConfigManager configManager;
  private List<Integer> busStartTimings;
  private List<Integer> timeSinceLastBus;
  private int numTimeSteps = 0;
  private int simulationTimeElapsed = 0;
  private List<Route> prototypeRoutes;
  private List<Bus> busses;
  private int busId = 1000;
  private boolean paused = false;
  private Random rand;
  private BusSubject busSubject;
  private StopSubject stopSubject;
  private BusCreator simulationConcreteBusCreator;

  /**
   * Constructor for Simulation.
   *
   * @param webI    MWS object
   * @param configM config object
   * @param session session object
   */
  public VisualizationSimulator(MyWebServer webI, ConfigManager configM,
                                MyWebServerSession session) {
    this.webInterface = webI;
    this.configManager = configM;
    //initialize these lists so that we do not get a null pointer
    this.busStartTimings = new ArrayList<Integer>();
    this.prototypeRoutes = new ArrayList<Route>();
    this.busses = new ArrayList<Bus>();
    this.timeSinceLastBus = new ArrayList<Integer>();
    this.rand = new Random();
    this.busSubject = new ConcreteBusSubject(session);
    this.stopSubject = new ConcreteStopSubject(session);
  }

  /**
   * Sets the bus creator for the simulation.
   *
   * @param currentSimulationTime time when the simulation was started.
   */
  public void setConcreteBusCreator(LocalDateTime currentSimulationTime) {
    int day = currentSimulationTime.getDayOfMonth();
    int time = currentSimulationTime.getHour();
    OverallConcreteBusCreator overallBusCreator = new OverallConcreteBusCreator();
    overallBusCreator.setCurrentConcreteBusCreator(day, time);
    this.simulationConcreteBusCreator = overallBusCreator;
  }

  /**
   * Starts the simulation.
   *
   * @param busStartTimingsParam start timings of bus
   * @param numTimeStepsParam    number of time steps
   */
  public void start(List<Integer> busStartTimingsParam, int numTimeStepsParam) {
    this.busStartTimings = busStartTimingsParam;
    this.numTimeSteps = numTimeStepsParam;
    for (int i = 0; i < busStartTimings.size(); i++) {
      this.timeSinceLastBus.add(i, 0);
    }
    simulationTimeElapsed = 0;
    prototypeRoutes = configManager.getRoutes();
    for (int i = 0; i < prototypeRoutes.size(); i++) {
      prototypeRoutes.get(i).report(System.out);
      prototypeRoutes.get(i).updateRouteData();
      webInterface.updateRoute(prototypeRoutes.get(i).getRouteData(), false);
    }
  }

  /**
   * Getter for numTimeSteps.
   * @return int of getNumTimeSteps.
   */
  public int getNumTimeSteps() {
    return numTimeSteps;
  }

  /**
   * Getter for simulationTimeElapsed.
   * @return int simulationTimeElapsed.
   */
  public int getSimulationTimeElapsed() {
    return simulationTimeElapsed;
  }

  /**
   * Getter for prototypeRoutes.
   * @return list of routes.
   */
  public List<Route> getPrototypeRoutes() {
    return prototypeRoutes;
  }

  /**
   * Setter for prototypeRoutes.
   * @param routes a list of routes.
   */
  public void setPrototypeRoutes(List<Route> routes) {
    prototypeRoutes = routes;
  }

  /**
   * Setter for simulationConcreteBusCreator.
   * @param sim is a BusCreator instance.
   */
  public void setSimulationConcreteBusCreator(BusCreator sim) {
    this.simulationConcreteBusCreator = sim;
  }

  /**
   * Setter for busses.
   * @param busses a list of busses.
   */
  public void setBusses(List<Bus> busses) {
    this.busses = busses;
  }

  /**
   * Getter for timeSinceLastBus.
   * @return List of times since the last bus.
   */
  public List<Integer> getTimeSinceLastBus() {
    return timeSinceLastBus;
  }

  /**
   * Setter for timeSinceLastBus.
   * @param timeS a list of times since the last bus.
   */
  public void setTimeSinceLastBus(List<Integer> timeS) {
    this.timeSinceLastBus = timeS;
  }

  /**
   * Getter for busStartTimings.
   * @return List of busstartTimings.
   */
  public List<Integer> getBusStartTimings() {
    return busStartTimings;
  }

  /**
   * Setter for busStartTimings.
   * @param busTimings list of busTimings.
   */
  public void setBusStartTimings(List<Integer> busTimings) {
    this.busStartTimings = busTimings;
  }

  /**
   * Getter for stopSubject.
   * @return StopSubject
   */
  public StopSubject getStopSubject() {
    return stopSubject;
  }

  /**
   * Getter for busSubject.
   * @return BusSubject
   */
  public BusSubject getBusSubject() {
    return busSubject;
  }

  /**
   * Toggles the pause state of the simulation.
   */
  public void togglePause() {
    paused = !paused;
  }

  /**
   * Getter for paused variable.
   * @return boolean value of paused
   */
  public boolean getPauseStatus() {
    return paused;
  }

  /**
   * Updates the simulation at each step.
   */
  public void update() {
    if (!paused) {
      simulationTimeElapsed++;
      System.out.println("~~~~The simulation time is now"
          + "at time step "
          + simulationTimeElapsed + "~~~~");
      // Check if we need to generate new busses
      for (int i = 0; i < timeSinceLastBus.size(); i++) {
        // Check if we need to make a new bus
        if (timeSinceLastBus.get(i) <= 0) {
          Route outbound = prototypeRoutes.get(2 * i);
          Route inbound = prototypeRoutes.get(2 * i + 1);

          busses
              .add(new MaroonDecorator(this.simulationConcreteBusCreator
                  .createBus(String.valueOf(busId),
                  outbound.shallowCopy(), inbound.shallowCopy(), 1)));
          busId++;
          timeSinceLastBus.set(i, busStartTimings.get(i));
          timeSinceLastBus.set(i, timeSinceLastBus.get(i) - 1);
        } else {
          timeSinceLastBus.set(i, timeSinceLastBus.get(i) - 1);
        }
      }
      // Update busses
      for (int i = busses.size() - 1; i >= 0; i--) {
        busses.get(i).update();
        if (busses.get(i).isTripComplete()) {
          webInterface.updateBus(busses.get(i).getBusData(), true);
          busses.remove(i);
          continue;
        }
        if (busses.get(i).getOutgoingRoute().isAtEnd()) {
          busses.set(i, new GoldBusDecorator(busses.get(i).getBus()));
        }
        webInterface.updateBus(busses.get(i).getBusData(), false);
        busses.get(i).report(System.out);
      }
      // Update routes
      for (int i = 0; i < prototypeRoutes.size(); i++) {
        prototypeRoutes.get(i).update();
        webInterface.updateRoute(prototypeRoutes.get(i).getRouteData(), false);
        prototypeRoutes.get(i).report(System.out);
      }
      busSubject.notifyBusObservers();
      stopSubject.notifyStopObservers();

      OneFile oneFile = OneFile.getInstance();
      FileWriter obj1 = oneFile.getFile2();

      String stringy = "";
      for (int k = 0; k < busses.size(); k++) {
        Bus boose = busses.get(k);
        stringy = "";
        stringy += "BUS";
        stringy += ", " + String.valueOf(simulationTimeElapsed);
        stringy += ", " + String.valueOf(boose.getBusData().getId());
        stringy += ", " + String.valueOf(boose.getBusData().getPosition().getXcoordLoc());
        stringy += ", " + String.valueOf(boose.getBusData().getPosition().getYcoordLoc());
        stringy += ", " + String.valueOf(boose.getNumPassengers());
        stringy += ", " + String.valueOf(boose.getCapacity()) + "\n";
        try {
          obj1.append(stringy);
          obj1.flush();
        } catch (IOException e) {
          e.printStackTrace();
        }

        //Print to file here.
      }
      int num = 1;
      List<Stop> stops12 = new ArrayList<Stop>();
      List<Integer> ids = new ArrayList<Integer>();
      ids.add(-1);
      for (Route route : prototypeRoutes) {
        List<Stop> stops1 = route.getStops();
        for (int s = 0; s < stops1.size(); s++) {
          Stop ob1 = stops1.get(s);
          int id1 = ob1.getId();
          for (int t = 0; t < ids.size(); t++) {
            if (id1 == ids.get(t)) {
              break;
            } else if (t == ids.size() - 1) {
              stops12.add(ob1);
              ids.add(ob1.getId());
            }
          }
        }
      }


      for (int p = 0; p < stops12.size(); p++) {
        Stop ar1 = stops12.get(p);
        stringy = "";
        stringy += "STOP";
        stringy += ", " + String.valueOf(simulationTimeElapsed);
        stringy += ", " + String.valueOf(ar1.getId());
        stringy += ", " + String.valueOf(ar1.getLongitude());
        stringy += ", " + String.valueOf(ar1.getLatitude());
        stringy += ", " + String.valueOf(ar1.getNumPassengersPresent()) + "\n";
        try {
          obj1.append(stringy);
          obj1.flush();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }


    }

  }

  /**
   * Identifies the bus that should be an observer and notifies the subject.
   *
   * @param id identifier of the bus that is an observer of the simulation
   */
  public void addBusObserver(String id) {
    for (Bus bus : busses) {
      if (bus.getName().equals(id)) {
        busSubject.registerBusObserver(bus);
      }
    }
  }

  /**
   * Identifies the stop that should be an observer and notifies the subject.
   *
   * @param id identifier of the stop that is an observer of the simulation
   */
  public void addStopObserver(String id) {
    for (Route route : prototypeRoutes) {
      List<Stop> stops = route.getStops();
      for (Stop stop : stops) {
        String stopId = stop.getId() + "";
        if (stopId.equals(id)) {
          stopSubject.registerStopObserver(stop);
        }
      }
    }
  }

}
