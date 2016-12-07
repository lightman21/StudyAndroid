package org.ith.j2se.rxjava.chapter3;

import rx.Observable;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by tanghao on 12/7/16.
 *
 * problem: when you have two Observable.and they have different event emit frequency. and you just
 * wanna one Observable who emmit event fast.
 *
 * solution: amb() operator
 *
 * also introduce doOnSubscribe() and doOnUnsubscribe() operators
 */
public class AmbDemo {

  public static void main(String[] args) {
    justDropSlower();
  }

  public static void justDropSlower() {
    Observable.amb(
        stream(100, 17, "S"),
        stream(200, 10, "F")
    ).subscribe(System.out::println);
  }

  public static Observable<String> stream(int initialDelay, int interval, String name) {
    return Observable
        .interval(initialDelay, interval, MILLISECONDS)
        .map(x -> name + x)
        .doOnSubscribe(() -> System.out.println("Subscribe to " + name))
        .doOnUnsubscribe(() -> System.out.println("UUUUUUnsubscribe from " + name));
  }
}
