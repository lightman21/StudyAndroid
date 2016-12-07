package org.ith.j2se.rxjava.chapter3;

import java.util.concurrent.TimeUnit;
import rx.Observable;

/**
 * Created by tanghao on 12/7/16.
 *
 * http://stackoverflow.com/questions/37652952/how-to-use-rxjava-interval-operator
 *
 * Observable.interval operates by default on Schedulers.computation()
 *
 * so your stream is being run on a thread other than the main thread.
 */
public class InternalOperatorDemo {
  public static void main(String[] args) throws Exception {

    Observable.interval(0, 100, TimeUnit.MILLISECONDS)
        .map(x -> x + x)
        .subscribe(System.out::println);

    Thread.sleep(10 * 1000);
    System.out.println("-----------now main thread dead--------");
  }
}
