package org.ith.j2se.rxjava;

import java.io.Serializable;
import rx.Observable;
import rx.Observer;
import rx.functions.Func2;

/**
 * Created by tanghao on 12/14/16.
 */

public class Chapter6 {
  public static void main(String[] args) {

    combineLatest();

  }

  public static void combineLatest() {

    Observable<Integer> into = Observable.range(0, 9);
    Observable<String> stro = Observable.just("A", "B", "C");

    Observable.combineLatest(into, stro, new Func2<Integer, String, Object>() {
      @Override
      public Object call(Integer integer, String s) {
        return integer + s;
      }
    })
        .forEach(System.out::println);

    System.out.println("=================");

    Observable.combineLatest(stro, into, new Func2<String, Integer, Object>() {
      @Override
      public Object call(String s, Integer integer) {
        return integer + s;
      }
    })
        .forEach(System.out::println);

  }

  public static void zip() {
    Observable<Integer> into = Observable.range(0, 9);
    Observable<String> stro = Observable.just("A", "B", "C");

    Observable.zip(into, stro, new Func2<Integer, String, Object>() {
      @Override
      public Object call(Integer integer, String s) {

        return s + integer;
      }
    }).forEach(System.out::print);

    System.out.println("\n=======stro into =========");


    Observable.zip(stro, into, new Func2<String, Integer, Object>() {
      @Override
      public Object call(String s, Integer integer) {

        return s + integer;
      }
    }).forEach(System.out::print);
  }

  public static void merge() {
    Observable<Integer> into = Observable.just(1, 2, 3, 4, 5);
    Observable<String> stro = Observable.just("A", "B", "C", "D");

    Observable.merge(into, stro)
        .subscribe(System.out::print);

    System.out.println("\nmergeWhenError");

    into = into.filter(it -> it > 3)
        .map(it -> it / (it - 3));

    Observable.mergeDelayError(into, stro)
        .subscribe(new Observer<Serializable>() {
          @Override
          public void onCompleted() {
            System.out.println("onCompleted");
          }

          @Override
          public void onError(Throwable e) {

            System.out.println("onError");
          }

          @Override
          public void onNext(Serializable serializable) {
            System.out.println(serializable);
          }
        });
  }
}
