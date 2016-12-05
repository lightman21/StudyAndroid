package org.ith.j2se.rxjava.chapter3;

import org.ith.j2se.rxjava.chapter3.entities.Temperature;
import org.ith.j2se.rxjava.chapter3.entities.Weather;
import org.ith.j2se.rxjava.chapter3.entities.Wind;
import org.ith.j2se.rxjava.chapter3.interfaces.WeatherStation;
import rx.Observable;

/**
 * Created by tanghao on 12/5/16 07:59
 *
 * Pairwise Compposing using zip() and zipWith()
 *
 * when two indenpendent streams emit values, but only combining them has business meaning.
 */
public class ZipDemo {
  public static void main(String[] args) {

    WeatherStation station = obtainStation();

    //when a new Temperature event occurs,zipWith() waits(obviously  without blocking without blocking without blocking)
    //for Wind,and vice versa.

    Observable<Temperature> temperatureObservable = station.temperature();
    Observable<Wind> windObservable = station.wind();

    temperatureObservable.zipWith(windObservable, (temperature, wind) -> new Weather(temperature, wind));
  }

  public static WeatherStation obtainStation() {
    return new WeatherStation() {
      @Override
      public Observable<Temperature> temperature() {
        return Observable.just(new Temperature());
      }

      @Override
      public Observable<Wind> wind() {
        return Observable.just(new Wind());
      }
    };
  }

}
