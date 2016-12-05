package org.ith.j2se.rxjava.chapter3.interfaces;

import org.ith.j2se.rxjava.chapter3.entities.Temperature;
import org.ith.j2se.rxjava.chapter3.entities.Wind;
import rx.Observable;

/**
 * Created by tanghao on 12/5/16.
 */

public interface WeatherStation {
  Observable<Temperature> temperature();

  Observable<Wind> wind();
}
