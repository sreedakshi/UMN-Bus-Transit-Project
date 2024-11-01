package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.webserver.MyWebServerSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;


public class BusTest {

  /**
   * Setup deterministic operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Testing state after using constructor.
   */
  @Test
  public void testConstructorNormal() {
    Stop stop1 = new Stop(0, 44.972392, -93.243774);
    Stop stop2 = new Stop(1, 44.973580, -93.235071);
    Stop stop3 = new Stop(2, 44.975392, -93.226632);
    List<Stop> stopsIn = new ArrayList<Stop>();
    stopsIn.add(stop1);
    stopsIn.add(stop2);
    stopsIn.add(stop3);
    List<Double> distancesIn = new ArrayList<Double>();
    distancesIn.add(0.008784);
    distancesIn.add(0.008631);
    List<Double> probabilitiesIn = new ArrayList<Double>();
    probabilitiesIn.add(.15);
    probabilitiesIn.add(0.3);
    probabilitiesIn.add(.025);
    PassengerGenerator generatorIn = new RandomPassengerGenerator(probabilitiesIn, stopsIn);
    Route testRouteIn = new Route("testRouteIn", stopsIn, distancesIn, 3, generatorIn);
    List<Stop> stopsOut = new ArrayList<>();
    stopsOut.add(stop3);
    stopsOut.add(stop2);
    stopsOut.add(stop1);
    List<Double> distancesOut = new ArrayList<>();
    distancesOut.add(0.008631);
    distancesOut.add(0.008784);
    List<Double> probabilitiesOut = new ArrayList<>();
    probabilitiesOut.add(.025);
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.15);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(probabilitiesOut, stopsOut);
    Route testRouteOut = new Route("testRouteIn", stopsOut, distancesOut, 3, generatorOut);
    Bus bus = new Bus("TestBus", testRouteOut, testRouteIn, 5, 1);
    assertEquals("TestBus", bus.getName());
    assertEquals(5, bus.getCapacity());
    assertEquals(1.0, bus.getSpeed());
  }

  /**
   * Test report with null stream.
   */
  @Test
  public void testBusReportNullStream() {
    Exception exception = assertThrows(NullPointerException.class, () -> {
      Bus testBus = TestUtils.createBus();
      testBus.report(null);
    });
  }

  /**
   * Test report with no passenger.
   */
  @Test
  public void testBusReportNoPassengers() {
    try {
      Bus testBus = TestUtils.createBus();
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      testBus.report(testStream);
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Bus Info Start####" + System.lineSeparator()
              + "Name: TestBus" + System.lineSeparator()
              + "Speed: 1.0" + System.lineSeparator()
              + "Distance to next stop: 0.0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num of passengers: 0" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Bus Info End####" + System.lineSeparator();
      assertEquals(strToCompare, data);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test report with passenger.
   */
  @Test
  public void testBusReportWithPassengers() {
    try {
      Bus testBus = TestUtils.createBus();
      Passenger passenger = new Passenger(1, "Goldy");
      testBus.loadPassenger(passenger);
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      testBus.report(testStream);
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Bus Info Start####" + System.lineSeparator()
              + "Name: TestBus" + System.lineSeparator()
              + "Speed: 1.0" + System.lineSeparator()
              + "Distance to next stop: 0.0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num of passengers: 1" + System.lineSeparator()
              + "####Passenger Info Start####" + System.lineSeparator()
              + "Name: Goldy" + System.lineSeparator()
              + "Destination: 1" + System.lineSeparator()
              + "Total wait: 1" + System.lineSeparator()
              + "Wait at stop: 0" + System.lineSeparator()
              + "Time on bus: 1" + System.lineSeparator()
              + "####Passenger Info End####" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Bus Info End####" + System.lineSeparator();
      assertEquals(strToCompare, data);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test trip not complete when bus just created.
   */
  @Test
  public void testIsTripCompleteWhenBusJustCreated() {
    Bus testBus = TestUtils.createBus();
    boolean testBusAtEnd = testBus.isTripComplete();
    assertFalse(testBusAtEnd);
  }

  /**
   * Test trip complete when bus at end.
   */
  @Test
  public void testIsTripCompleteWhenBusAtEnd() {
    Bus testBus = TestUtils.createBus();
    testBus.move();
    testBus.move();
    testBus.move();
    testBus.move();
    testBus.move();
    testBus.move();
    boolean testBusAtEnd = testBus.isTripComplete();
    assertTrue(testBusAtEnd);
  }

  /**
   * Test load passenger with a full bus.
   */
  @Test
  public void testloadPassengerFullBus() {
    Bus testBus = TestUtils.createBus();
    Passenger passenger = new Passenger(1, "Goldy");
    Passenger passenger2 = new Passenger(1, "Cookie");
    Passenger passenger3 = new Passenger(1, "Potato");
    Passenger passenger4 = new Passenger(1, "Biscuit");
    Passenger passenger5 = new Passenger(1, "Peanut");
    Passenger passenger6 = new Passenger(1, "Chickpea");
    testBus.loadPassenger(passenger);
    testBus.loadPassenger(passenger2);
    testBus.loadPassenger(passenger3);
    testBus.loadPassenger(passenger4);
    testBus.loadPassenger(passenger5);
    boolean passengersLoaded = testBus.loadPassenger(passenger6);
    assertFalse(passengersLoaded);
  }

  /**
   * Test load passenger on empty bus.
   */
  @Test
  public void testLoadPassengerEmptyBus() {
    Bus testBus = TestUtils.createBus();
    Passenger passenger = new Passenger(1, "Goldy");
    boolean passengersLoaded = testBus.loadPassenger(passenger);
    assertTrue(passengersLoaded);
  }

  /**
   * Test move passenger.
   */
  @Test
  public void testMoveNoPassengersOnBus() {
    Bus testBus = TestUtils.createBus();
    Passenger passenger = new Passenger(0, "Goldy");
    boolean testBusMoved = testBus.move();
    assertEquals(1, testBus.getNextStop().getId());
    assertTrue(testBusMoved);
  }

  /**
   * Test move passenger with passengers on bus.
   */
  @Test
  public void testMoveWithPassengersOnBus() {
    Bus testBus = TestUtils.createBus();
    Passenger passenger = new Passenger(2, "Goldy");
    testBus.loadPassenger(passenger);
    boolean testBusMoved = testBus.move();
    assertEquals(1, testBus.getNextStop().getId());
    assertTrue(testBusMoved);
  }

  /**
   * Test update on outgoing route.
   */
  @Test
  public void testOutgoingRouteUpdate() {
    try {
      Bus testBus = TestUtils.createBus();
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      testBus.report(testStream);
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Bus Info Start####" + System.lineSeparator()
              + "Name: TestBus" + System.lineSeparator()
              + "Speed: 1.0" + System.lineSeparator()
              + "Distance to next stop: 0.0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num of passengers: 0" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Bus Info End####" + System.lineSeparator();
      assertEquals(strToCompare, data);
      testBus.update();
      final Charset charset2 = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
      PrintStream testStream2 = new PrintStream(outputStream2, true, charset2.name());
      testBus.report(testStream2);
      String data2 = new String(outputStream2.toByteArray(), charset2);
      testStream2.close();
      outputStream2.close();
      String strToCompare2 =
          "####Bus Info Start####" + System.lineSeparator()
              + "Name: TestBus" + System.lineSeparator()
              + "Speed: 1.0" + System.lineSeparator()
              + "Distance to next stop: 0.9712663713083954" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num of passengers: 0" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Bus Info End####" + System.lineSeparator();
      assertEquals(strToCompare2, data2);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test update bus data without moving the bus.
   */
  @Test
  public void testRouteUpdateBusDataNoMove() {
    try {
      Bus testBus = TestUtils.createBus();
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      testBus.report(testStream);
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Bus Info Start####" + System.lineSeparator()
              + "Name: TestBus" + System.lineSeparator()
              + "Speed: 1.0" + System.lineSeparator()
              + "Distance to next stop: 0.0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num of passengers: 0" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Bus Info End####" + System.lineSeparator();
      assertEquals(strToCompare, data);
      testBus.updateBusData();
      final Charset charset2 = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
      PrintStream testStream2 = new PrintStream(outputStream2, true, charset2.name());
      testBus.report(testStream2);
      String data2 = new String(outputStream2.toByteArray(), charset2);
      testStream2.close();
      outputStream2.close();
      String strToCompare2 =
          "####Bus Info Start####" + System.lineSeparator()
              + "Name: TestBus" + System.lineSeparator()
              + "Speed: 1.0" + System.lineSeparator()
              + "Distance to next stop: 0.0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num of passengers: 0" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Bus Info End####" + System.lineSeparator();
      assertEquals(strToCompare2, data2);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test update bus data while bus in incoming route.
   */
  @Test
  public void testRouteUpdateBusDataMoving() {
    try {
      Bus testBus = TestUtils.createBus();
      testBus.move();
      testBus.move();
      testBus.move();
      testBus.move();
      testBus.move();
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      testBus.report(testStream);
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Bus Info Start####" + System.lineSeparator()
              + "Name: TestBus" + System.lineSeparator()
              + "Speed: 1.0" + System.lineSeparator()
              + "Distance to next stop: 0.9712663713083954" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num of passengers: 0" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Bus Info End####" + System.lineSeparator();
      assertEquals(strToCompare, data);
      testBus.updateBusData();
      final Charset charset2 = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
      PrintStream testStream2 = new PrintStream(outputStream2, true, charset2.name());
      testBus.report(testStream2);
      String data2 = new String(outputStream2.toByteArray(), charset2);
      testStream2.close();
      outputStream2.close();
      String strToCompare2 =
          "####Bus Info Start####" + System.lineSeparator()
              + "Name: TestBus" + System.lineSeparator()
              + "Speed: 1.0" + System.lineSeparator()
              + "Distance to next stop: 0.9712663713083954" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num of passengers: 0" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Bus Info End####" + System.lineSeparator();
      assertEquals(strToCompare2, data2);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test update bus data after bus reaches end.
   */
  @Test
  public void testRouteUpdateBusDataEnd() {
    try {
      Bus testBus = TestUtils.createBus();
      testBus.move();
      testBus.move();
      testBus.move();
      testBus.move();
      testBus.move();
      testBus.move();
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      testBus.report(testStream);
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Bus Info Start####" + System.lineSeparator()
              + "Name: TestBus" + System.lineSeparator()
              + "Speed: 1.0" + System.lineSeparator()
              + "Distance to next stop: 999.0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num of passengers: 0" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Bus Info End####" + System.lineSeparator();
      assertEquals(strToCompare, data);
      testBus.updateBusData();
      final Charset charset2 = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
      PrintStream testStream2 = new PrintStream(outputStream2, true, charset2.name());
      testBus.report(testStream2);
      String data2 = new String(outputStream2.toByteArray(), charset2);
      testStream2.close();
      outputStream2.close();
      String strToCompare2 =
          "####Bus Info Start####" + System.lineSeparator()
              + "Name: TestBus" + System.lineSeparator()
              + "Speed: 1.0" + System.lineSeparator()
              + "Distance to next stop: 999.0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num of passengers: 0" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Bus Info End####" + System.lineSeparator();
      assertEquals(strToCompare2, data2);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test number of passengers on bus.
   */
  @Test
  public void testGetNumPassengersOnePass() {
    Bus testBus = TestUtils.createBus();
    Passenger passenger = new Passenger(1, "Goldy");
    testBus.loadPassenger(passenger);
    long passNum = testBus.getNumPassengers();
    assertEquals(1, passNum);
  }

  /**
   * Test getter for outgoing route.
   */
  @Test
  public void testGetOutgoingRoute() {
    Bus testBus = TestUtils.createBus();
    Route outgoingRoute = testBus.getOutgoingRoute();
    assertTrue(outgoingRoute instanceof Route);
  }

  /**
   * Testing getter for incoming route.
   */
  @Test
  public void testGetIncomingRoute() {
    Bus testBus = TestUtils.createBus();
    Route incomingRoute = testBus.getIncomingRoute();
    assertTrue(incomingRoute instanceof Route);
  }

  /*
  /**
   * Testing getter for concrete bus subject.
   */
  /*
  @Test
  public void testGetConcreteBusSubject() {
    Bus testBus = TestUtils.createBus();
    ConcreteBusSubject cb = testBus.getConcreteBusSubject();
    assertTrue(cb instanceof ConcreteBusSubject);
  }
   */

  /**
   * Testing display info.
   */
  @Test
  public void testDisplayInfo() {
    Bus testBus = TestUtils.createBus();
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    ConcreteBusSubject cs = new ConcreteBusSubject(myWebServerSessionSpy);
    testBus.setConcreteBusSubject(cs);
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    testBus.displayInfo();
    verify(myWebServerSessionSpy).sendJson(messageCaptor.capture());
    JsonObject output = messageCaptor.getValue();
    String command = output.get("command").getAsString();
    assertEquals("observeBus", command);
    String text = output.get("text").getAsString();
    String strToCompare =
        "Bus TestBus" + System.lineSeparator()
            + "-----------------------------" + System.lineSeparator()
            + "  * Position: (0.0,0.0)" + System.lineSeparator()
            + "  * Passengers: 0" + System.lineSeparator()
            + "  * Capacity: 0" + System.lineSeparator();
    assertEquals(strToCompare, text);
  }
}
