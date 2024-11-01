package edu.umn.cs.csci3081w.project.model;

import java.awt.Color;

public class BusData {
  private String id;
  private Position position;
  private int numPassengers;
  private int capacity;
  private Color color;

  /**
   * Stores details of a bus.
   *
   * @param id   bus id
   * @param pos  position of the bus
   * @param pass number of passengers
   * @param cap  capacity of bus to be created
   */
  public BusData(String id, Position pos, int pass, int cap) {
    this.id = id;
    this.position = pos;
    this.numPassengers = pass;
    this.capacity = cap;
  }

  public BusData() {
    this.position = new Position();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public int getNumPassengers() {
    return numPassengers;
  }

  public void setNumPassengers(int numPassengers) {
    this.numPassengers = numPassengers;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(int r, int g, int b, int a) {
    this.color = new Color(r, g, b, a);
  }
}
