package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.Position;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.RouteData;
import edu.umn.cs.csci3081w.project.model.StopData;
import edu.umn.cs.csci3081w.project.model.TestUtils;
import java.util.ArrayList;
import java.util.List;
import javax.websocket.Session;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;


public class GetRoutesCommandTest {
  /**
   * Test execute function.
   */
  @Test
  public void testExecute() {
    Route testRoute = TestUtils.createRoute();
    MyWebServer wsb = new MyWebServer();
    Position pos = new Position();
    MyWebServerSessionState mwsss = mock(MyWebServerSessionState.class);
    List<RouteData> rest = new ArrayList<RouteData>();
    StopData stop = new StopData("testStopData", pos, 3);
    List<StopData> stopData = new ArrayList<>(); //for route
    stopData.add(stop);
    RouteData rd = new RouteData();
    rd.setId("routeTestData");
    rd.setStops(stopData);
    rest.add(rd);
    wsb.routes = rest;
    GetRoutesCommand grc = new GetRoutesCommand(wsb);
    MyWebServerSession myWebServerSessionSpy = spy(MyWebServerSession.class);
    doNothing().when(myWebServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    Session sessionDummy = mock(Session.class);
    myWebServerSessionSpy.onOpen(sessionDummy);
    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "getRoutes");
    grc.execute(myWebServerSessionSpy, commandFromClient, mwsss);
    ArgumentCaptor<JsonObject> messageCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(myWebServerSessionSpy).sendJson(messageCaptor.capture());
    JsonObject commandToClient = messageCaptor.getValue();
    assertTrue(commandToClient != null);
  }
}
