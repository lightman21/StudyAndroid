package org.ith.j2se.rxjava;

import static rx.Observable.empty;
import static rx.Observable.just;

public class Chatper3 {
  public static void main(String[] args) {

    test0();
  }

  /**
   * flatmap replace filter or map
   */
  public static void test1() {
    //----------filter --> flatmap------------//
    just(10)
        .filter(x -> x != 10);
    //equivalent to
    just(10)
        .flatMap(x -> x != 10 ? just(x) : empty());
    //----------filter --> flatmap------------//


    //----------map --> flatmap------------//
    just(10)
        .map(x -> x * 2);
    //equivalent
    just(10).flatMap(x -> just(x * 2));
    //----------map --> flatmap------------//
  }

  /**
   * things goes
   *
   * filter map filter map ...
   *
   * NOT
   *
   * filter filter filter map map map ...
   */
  public static void test0() {
    just(8, 9, 10)
        .filter(i -> i % 3 > 0)
        .doOnNext(it -> System.out.println("filter --> " + it))
        .map(i -> "#" + i * 10)
        .doOnNext(it -> System.out.println("map #  --> " + it))
        .filter(s -> s.length() < 4)
        .forEach(System.out::println);
  }
}
