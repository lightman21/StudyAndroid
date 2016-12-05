package org.ith.j2se.rxjava.chapter3.entities;

import java.time.LocalDate;
import rx.Observable;

/**
 * Created by tanghao on 12/6/16.
 */

public class Vacation {
  private final City where;
  private final LocalDate when;

  public Vacation(City where, LocalDate when) {
    this.where = where;
    this.when = when;
  }

  public Observable<Weather> weather()
  {
    return Observable.empty();
  }

  public Observable<Flight> cheapFlightFrom(City from)
  {
    return Observable.empty();
  }

  public Observable<Hotel>cheapHotel()
  {
    return Observable.empty();
  }


}
