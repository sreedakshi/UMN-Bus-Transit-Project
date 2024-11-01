package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.umn.cs.csci3081w.project.model.BusData;
import edu.umn.cs.csci3081w.project.model.Position;
import edu.umn.cs.csci3081w.project.model.RouteData;
import edu.umn.cs.csci3081w.project.model.StopData;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class MyWebServerTest {
  /**
   * Testing update bus function with empty busses.
   */
  @Test
  public void testUpdateBusEmpty() {
    MyWebServer ws = new MyWebServer();
    BusData bd = new BusData();
    Boolean bool = false;
    ws.updateBus(bd, bool);
    assertFalse(ws.busses.isEmpty());
  }

  /**
   * Testing update bus function with non-empty busses and
   * deleting bus.
   */
  @Test
  public void testUpdateBusDelete() {
    MyWebServer ws = new MyWebServer();
    Position pos = new Position();
    BusData bd = new BusData("testBusData", pos, 3, 5);
    BusData td = new BusData("testBusData", pos, 3, 5);
    ws.busses.add(td);
    Boolean bool = true;
    ws.updateBus(bd, bool);
    assertTrue(ws.busses.isEmpty());
  }

  /**
   * Testing update bus function with non-empty busses and
   * not deleting bus.
   */
  @Test
  public void testUpdateBusNoDelete() {
    MyWebServer ws = new MyWebServer();
    Position pos = new Position();
    BusData bd = new BusData("testBusData", pos, 3, 5);
    BusData td = new BusData("testBusData", pos, 3, 5);
    ws.busses.add(bd);
    Boolean bool = false;
    ws.updateBus(td, bool);
    assertEquals(ws.busses.get(0).getId(), td.getId());
    assertEquals(ws.busses.get(0).getPosition(), td.getPosition());
    assertEquals(ws.busses.get(0).getNumPassengers(), td.getNumPassengers());
    assertEquals(ws.busses.get(0).getCapacity(), td.getCapacity());
  }

  /**
   * Testing update route function with empty routes.
   */
  @Test
  public void testUpdateRouteEmpty() {
    MyWebServer ws = new MyWebServer();
    RouteData rd = new RouteData();
    Boolean bool = false;
    ws.updateRoute(rd, bool);
    assertFalse(ws.routes.isEmpty());
  }

  /**
   * Testing update route function with non-empty routes and
   * deleting route.
   */
  @Test
  public void testUpdateRouteDelete() {
    MyWebServer ws = new MyWebServer();
    Position pos = new Position();
    StopData stop = new StopData("testStopData", pos, 3);
    List<StopData> stopData = new ArrayList<>(); //for route
    stopData.add(stop);
    RouteData rd = new RouteData();
    rd.setId("routeTestData");
    rd.setStops(stopData);
    RouteData td = new RouteData();
    td.setId("routeTestData");
    td.setStops(stopData);
    ws.routes.add(rd);
    Boolean bool = true;
    ws.updateRoute(td, bool);
    assertTrue(ws.routes.isEmpty());
  }

  /**
   * Testing update route function with non-empty route and
   * not deleting route.
   */
  @Test
  public void testUpdateRouteNoDelete() {
    MyWebServer ws = new MyWebServer();
    Position pos = new Position();
    StopData stop = new StopData("testStopData", pos, 3);
    List<StopData> stopData = new ArrayList<>(); //for route
    stopData.add(stop);
    RouteData rd = new RouteData();
    rd.setId("routeTestData");
    rd.setStops(stopData);
    RouteData td = new RouteData();
    td.setId("routeTestData");
    td.setStops(stopData);
    ws.routes.add(rd);
    Boolean bool = false;
    ws.updateRoute(td, bool);
    assertEquals(ws.routes.get(0).getId(), td.getId());
    assertEquals(ws.routes.get(0).getStops(), td.getStops());
  }
}
