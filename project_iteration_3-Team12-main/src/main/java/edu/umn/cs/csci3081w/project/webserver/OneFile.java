package edu.umn.cs.csci3081w.project.webserver;

import java.io.FileWriter;
import java.io.IOException;

public final class OneFile {
  private static OneFile filey;
  private FileWriter file2;

  private OneFile() {
    String pathVar = "output.csv";
    try {
      FileWriter file1 = new FileWriter(pathVar, false);
      file1.write("");
      file1.close();

      this.file2 = new FileWriter(pathVar, true);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * GetInstance is a static function.
   * @return instance of class
   */
  public static OneFile getInstance() {
    if (filey == null) {
      filey = new OneFile();
    }
    return filey;
  }

  public FileWriter getFile2() {
    return file2;
  }
}
