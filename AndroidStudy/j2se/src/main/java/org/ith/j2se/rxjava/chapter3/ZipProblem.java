package org.ith.j2se.rxjava.chapter3;

import java.util.concurrent.TimeUnit;
import rx.Observable;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static rx.Observable.interval;

/**
 * Created by tanghao on 12/6/16.
 *
 * 1.problem: when zip two Observables has different event frequency
 *
 * 1.solution:combineLatest() operator -----> solveZipDiffFrequency()
 *
 * the Observable trigger each other which means when slower event appears, it will find the lasted
 * faster event. and when faster appears.it  also find the lasted slower event
 *
 *
 * 2.problem: but what if you just want A Observable trigger B Observable and get the lastest event
 * but not vice versa.
 *
 * 2.solutioin: withLatestFrom() -----> solveTrigerEachOther()
 *
 * Rx 1.0.1 --> 1.2.3
 *
 *
 *
 * 3.problem: but when you use withlastestFrom. slow event appeare before the fast event so there
 * are some events from slow event will be droped. because at beginning only slow event emitted and
 * there are no fast event emitted. so the slow event will be droped.
 *
 * 3.solution: startswith() -----> solveDropedEvent()
 *
 * startswith()  generate some dummy event
 */
public class ZipProblem {
  public static void main(String[] args) {

    solveZipDiffFrequency();
    solveTrigerEachOther();
    solveDropedEvent();

  }

  public static void solveZipDiffFrequency() {
    Observable.combineLatest(
        interval(17, MILLISECONDS).map(x -> "S" + x),
        interval(10, MILLISECONDS).map(x -> "F" + x),
        (s, f) -> f + ":" + s
    ).forEach(System.out::println);
    /***
     *
     * well the output quickly becomes out-of-sync.
     *
     * but at least values are consumed in real time
     *
     * and the faster stream does not need to wait for the slower one
     *
     output:

     the Observable trigger each other
     which means when slower event appears, it will find the lasted faster event.
     and when faster appears.it will also find the lasted slower event

     F0:S0
     F1:S0
     F2:S0
     F2:S1
     F3:S1
     F4:S1
     F4:S2
     F5:S2
     F5:S3
     ...
     F998:S586
     F998:S587
     F999:S587
     F1000:S587
     F1000:S588
     F1001:S588
     */
  }

  public static void solveTrigerEachOther() {
    Observable<String> fast = interval(10, MILLISECONDS).map(x -> "F" + x);
    Observable<String> slow = interval(17, MILLISECONDS).map(x -> "S" + x);
    slow.withLatestFrom(fast, (s, f) -> s + ":" + f)
        .forEach(System.out::println);


    /**
     * Output:
     *
     * as you see,something missed in the faster Observable
     * but the slower Observable goes ok.
     *
     S0:F1
     S1:F2
     S2:F4
     S3:F5
     S4:F7
     S5:F9
     S6:F11
     *
     */
  }

  public static void solveDropedEvent() {
    Observable<String> fast = interval(10, MILLISECONDS).map(x -> "F" + x)
        .delay(100, TimeUnit.MILLISECONDS).startWith("FX");

    Observable<String> slow = interval(17, MILLISECONDS).map(x -> "S" + x);

    slow.withLatestFrom(fast, (s, f) -> s + ":" + f)
        .forEach(System.out::println);

    /**
     * output:

     S0:FX
     S1:FX
     S2:FX
     S3:FX
     S4:FX
     S5:FX
     S6:F1
     S7:F3
     S8:F4
     S9:F6
     */
  }


  public static void base(long leftFrequency, int rightFrequency) {
    Observable<Long> red = interval(leftFrequency, MILLISECONDS);
    Observable<Long> green = interval(rightFrequency, MILLISECONDS);

    Observable.zip(
        red.timestamp(),
        green.timestamp(),
        (r, g) -> r.getTimestampMillis() - g.getTimestampMillis()
    ).forEach(System.out::println);
  }

  public static void normal() {
    base(100, 100);
  }

  /**
   * when zip two Observables,the event emitted frequency may be not same.
   *
   * this may cause Problem.
   *
   * Over time this difference piles up and can lead to stale data or even memory leak (see “Memory
   * Consumption and Leaks”).
   *
   * In practice zip() must be used carefully.
   */
  public static void notSameFrequency() {
    base(100, 30);
  }


}
