package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.umn.cs.csci3081w.project.model.Bus;
import edu.umn.cs.csci3081w.project.model.BusCreator;
import edu.umn.cs.csci3081w.project.model.RandomConcreteBusCreator;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Stop;
import edu.umn.cs.csci3081w.project.model.TestUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class VisualizationSimulatorTest {
  /**
   * Testing the toggle pause functionality.
   */
  @Test
  public void testTogglePause() {
    ConfigManager cm = new ConfigManager();
    MyWebServerSession session = new MyWebServerSession();
    MyWebServer ws = new MyWebServer();
    VisualizationSimulator vs = new VisualizationSimulator(ws, cm, session);
    vs.togglePause();
    assertTrue(vs.getPauseStatus());
  }

  /**
   * Testing the other branch of toggle pause functionality.
   */
  @Test
  public void testTogglePauseFalse() {
    ConfigManager cm = new ConfigManager();
    MyWebServerSession session = new MyWebServerSession();
    MyWebServer ws = new MyWebServer();
    VisualizationSimulator vs = new VisualizationSimulator(ws, cm, session);
    vs.togglePause();
    vs.togglePause();
    assertFalse(vs.getPauseStatus());
  }

  /**
   * Tests update function.
   */
  @Test
  public void testUpdate() {
    ConfigManager cm = new ConfigManager();
    MyWebServerSession session = new MyWebServerSession();
    MyWebServer ws = new MyWebServer();
    VisualizationSimulator vs = new VisualizationSimulator(ws, cm, session);
    Route testRoute = TestUtils.createRoute();
    Route testRoute1 = TestUtils.createRoute();
    List<Route> listRoutes = new ArrayList<Route>();
    listRoutes.add(testRoute);
    listRoutes.add(testRoute1);
    vs.setPrototypeRoutes(listRoutes);
    List<Integer> poohbear = new ArrayList<Integer>();
    poohbear.add(0, 0);
    vs.setTimeSinceLastBus(poohbear);
    BusCreator sim = new RandomConcreteBusCreator();
    vs.setSimulationConcreteBusCreator(sim);
    List<Integer> busTimings = new ArrayList<Integer>();
    busTimings.add(0, 0);
    busTimings.add(1, 1);
    vs.setBusStartTimings(busTimings);
    vs.update();
    assertEquals(1, vs.getSimulationTimeElapsed());
  }


  /**
   * Testing start.
   */
  @Test
  public void testStart() {
    ConfigManager cm = new ConfigManager();
    Route testRoute = TestUtils.createRoute();
    cm.getRoutes().add(testRoute);
    MyWebServerSession session = new MyWebServerSession();
    MyWebServer ws = new MyWebServer();
    VisualizationSimulator vs = new VisualizationSimulator(ws, cm, session);
    List<Integer> busStartTimings = new ArrayList<Integer>();
    busStartTimings.add(0, 0);
    List<Integer> tlist = new ArrayList<Integer>();
    tlist.add(0, 0);
    vs.start(busStartTimings, 1);
    assertEquals(busStartTimings, vs.getBusStartTimings());
    assertEquals(1, vs.getNumTimeSteps());
    assertEquals(tlist, vs.getTimeSinceLastBus());
    assertEquals(0, vs.getSimulationTimeElapsed());
    assertEquals(cm.getRoutes(), vs.getPrototypeRoutes());
  }

  /**
   * Testing addStopObserver.
   */
  @Test
  public void testStopObserver() {
    List<Route> listRoute = new ArrayList<Route>();
    Route testRoute = TestUtils.createRoute();
    listRoute.add(testRoute);
    List<Stop> stops = testRoute.getStops();
    ConfigManager cm = new ConfigManager();
    MyWebServerSession session = new MyWebServerSession();
    MyWebServer ws = new MyWebServer();
    VisualizationSimulator vs = new VisualizationSimulator(ws, cm, session);
    vs.setPrototypeRoutes(listRoute);
    vs.addStopObserver("1");
    assertNotNull(vs.getStopSubject());
  }

  /**
   * Testing addBusObserver.
   */
  @Test
  public void testBusObserver() {
    Route testRoute = TestUtils.createRoute();
    List<Stop> stops = testRoute.getStops();
    ConfigManager cm = new ConfigManager();
    MyWebServerSession session = new MyWebServerSession();
    MyWebServer ws = new MyWebServer();
    VisualizationSimulator vs = new VisualizationSimulator(ws, cm, session);
    List<Bus> listBus = new ArrayList<Bus>();
    Bus testBus = TestUtils.createBus();
    listBus.add(testBus);
    vs.setBusses(listBus);
    vs.addBusObserver("TestBus");
    assertNotNull(vs.getBusSubject());
  }
}
