package org.ith.j2se.rxjava.chapter2;

import rx.Observable;

/**
 * Created by tanghao on 12/19/16.
 */

public class Chapter2 {
  public static void main(String[] args) {
    cacheDemo();
  }

  public static void propagateError(int id) {
    Observable.create(subscriber -> {
      try {

        subscriber.onNext(id);
        subscriber.onCompleted();
      } catch (Exception e) {
        subscriber.onError(e);
      }
    });


    System.out.println("Rx Build-in way");

    Observable.fromCallable(() -> id);

  }

  /***
   * demonstrage cache()
   */
  public static void cacheDemo() {
    Observable<Integer> into = Observable.create(subscriber -> {
      System.out.println("Create");
      subscriber.onNext(42);
      subscriber.onCompleted();
    });

    System.out.println("start--------------");

    into.subscribe(ma -> System.out.println("MA " + ma));

    into.subscribe(mb -> System.out.println("MB " + mb));

    System.out.println("finish--------------");


    System.out.println("\ncache\n");

    into = into.cache();

    into.subscribe(ca -> System.out.println("Cache A " + ca));
    into.subscribe(cb -> System.out.println("Cache B  " + cb));
  }

}
