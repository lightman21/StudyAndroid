package org.ith.j2se.rxjava;

import rx.Observable;


public class Chatper3 {
  public static void main(String[] args) {

    Observable.just(8, 9, 10)
        .filter(i -> i % 3 > 0)
        .doOnNext(it -> System.out.println("filter --> " + it))
        .map(i -> "#" + i * 10)
        .doOnNext(it -> System.out.println("map #  --> " + it))
        .filter(s -> s.length() < 4)
        .forEach(System.out::println);

  }
}
