package org.ith.j2se.rxjava.chapter3;

import java.util.concurrent.TimeUnit;
import org.ith.j2se.util.TUtils;
import rx.Observable;

/**
 * Created by tanghao on 12/25/16.
 */

public class UnSynchronizedZip {
  public static void main(String[] args) {

    notSameFrequency();
  }

  public static void sameFrequncy() {
    Observable<String> fast = Observable.interval(100, TimeUnit.MILLISECONDS).map(x -> "F" + x);

    Observable<String> slow = Observable.interval(100, TimeUnit.MILLISECONDS).map(x -> "S" + x);

    Observable.zip(fast, slow, (f, s) -> f + "   " + s)
        .subscribe(System.out::println);

    TUtils.sleep(3 * 1000);
  }


  public static void notSameFrequency() {
    Observable<String> fast = Observable.interval(10, TimeUnit.MILLISECONDS).map(x -> "F" + x);

    Observable<String> slow = Observable.interval(30, TimeUnit.MILLISECONDS).map(x -> "S" + x);

    slow
        .withLatestFrom(fast, (s, f) -> s + ":" + f)
        .forEach(System.out::println);

    TUtils.sleep();
  }
}
