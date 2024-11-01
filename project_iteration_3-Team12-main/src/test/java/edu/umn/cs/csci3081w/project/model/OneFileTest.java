package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.umn.cs.csci3081w.project.webserver.OneFile;
import java.io.FileWriter;
import org.junit.jupiter.api.Test;

public class OneFileTest {
  @Test
  public void testOneFile() {
    OneFile oneFile = OneFile.getInstance();
    FileWriter obj1 = oneFile.getFile2();
    assertNotNull(obj1);
    assertNotNull(oneFile);
  }
}
