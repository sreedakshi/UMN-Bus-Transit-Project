package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.Bus;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Stop;
import javax.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class MyWebServerSessionTest {

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
   * Test command for initializing the simulation.
   */
  @Test
  public void testSimulationInitialization() {
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "initRoutes");
    myWebServerSessionSpy.onMessage(commandFromClient.toString());
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(myWebServerSessionSpy).sendJson(messageCaptor.capture());
    JsonObject commandToClient = messageCaptor.getValue();
    assertEquals("4", commandToClient.get("numRoutes").getAsString());
  }

  /**
   * Test the setter for my web server session state.
   */
  @Test
  public void testsetMyWebServerSessionState() {
    MyWebServerSession session = new MyWebServerSession();
    MyWebServerSessionState state = new MyWebServerSessionState();
    session.setmyWebServerSessionState(state);
    assertTrue(session.getMyWebServerSessionState() instanceof MyWebServerSessionState);
  }

  /**
   * Test the setter for my web server.
   */
  @Test
  public void testsetMyWebServer() {
    MyWebServerSession session = new MyWebServerSession();
    MyWebServer myWS = new MyWebServer();
    session.setMyWebServer(myWS);
    assertTrue(session.getMyWebServer() instanceof MyWebServer);
  }

  /**
   * Test the setter for config manager.
   */
  @Test
  public void testsetConfigManager() {
    MyWebServerSession session = new MyWebServerSession();
    ConfigManager cm = new ConfigManager();
    session.setConfigManager(cm);
    assertTrue(session.getConfigManager() instanceof ConfigManager);
  }

  /**
   * Test the setter for visualization simulator.
   */
  @Test
  public void testsetVisualizationSimulator() {
    MyWebServerSession session = new MyWebServerSession();
    MyWebServer myWS = new MyWebServer();
    ConfigManager cm = new ConfigManager();
    VisualizationSimulator vs = new VisualizationSimulator(myWS, cm, session);
    session.setVisualizationSimulator(vs);
    assertTrue(session.getVisualizationSimulator() instanceof VisualizationSimulator);
  }
}
