package edu.umn.cs.csci3081w.project.model;

import java.io.PrintStream;

public class GoldBusDecorator extends BusDecorator {
  /**
   * Constructor for the class.
   * @param bus to set color to
   */
  public GoldBusDecorator(Bus bus) {
    super(bus);
    bus.setColor(255, 215, 0, 255);

  }

  @Override
  public Bus getBus() {
    return bus;
  }

  @Override
  public void report(PrintStream out) {
    bus.report(out);
  }

  @Override
  public boolean isTripComplete() {
    return bus.isTripComplete();
  }

  @Override
  public boolean loadPassenger(Passenger newPassenger) {
    return bus.loadPassenger(newPassenger);
  }

  @Override
  public boolean move() {
    return bus.move();
  }

  @Override
  public void update() {
    bus.update();
  }

  @Override
  public void updateBusData() {
    bus.updateBusData();
  }

  @Override
  public void displayInfo() {
    bus.displayInfo();
  }

  @Override
  public BusData getBusData() {
    return bus.getBusData();
  }

  @Override
  public String getName() {
    return bus.getName();
  }

  @Override
  public Stop getNextStop() {
    return bus.getNextStop();
  }

  @Override
  public long getNumPassengers() {
    return bus.getNumPassengers();
  }

  @Override
  public int getCapacity() {
    return bus.getCapacity();
  }

  @Override
  public Route getOutgoingRoute() {
    return bus.getOutgoingRoute();
  }

  @Override
  public Route getIncomingRoute() {
    return bus.getIncomingRoute();
  }

  @Override
  public double getSpeed() {
    return bus.getSpeed();
  }

  @Override
  public void setConcreteBusSubject(ConcreteBusSubject concreteBusSubject) {
    bus.setConcreteBusSubject(concreteBusSubject);
  }
}
