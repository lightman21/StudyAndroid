package org.ith.j2se.rxjava.chapter3.entities;

/**
 * Created by tanghao on 12/5/16.
 */

public class Weather {
  private Temperature temperature;
  private Wind wind;

  public Weather(Temperature temperature, Wind wind) {
    this.temperature = temperature;
    this.wind = wind;
  }
}
