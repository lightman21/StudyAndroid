package org.ith.j2se.rxjava;

import rx.Observable;

import static org.ith.j2se.rxjava.Chatper3.Sound.DAH;
import static org.ith.j2se.rxjava.Chatper3.Sound.DI;
import static rx.Observable.empty;
import static rx.Observable.just;

public class Chatper3 {
  public static void main(String[] args) {

    test3();
  }

//-----------------------test3------------------------------//

  /***
   * as a rule of thumb,you use flatMap for the following situations:
   *
   * 1.The Result of transformation in map must be an Observable.
   *
   * 2.You need a one-to-many transformation.
   *
   * a single event is expanded into multtiple sub-events.
   */
  public static void test3() {
    Observable.just('A', 'B', 'C')
        .map(Character::toLowerCase)
        .flatMap(it -> toMorseCode(it));
  }

  public static Observable<Sound> toMorseCode(char sh) {
    switch (sh) {
      case 'A':
        return just(DI, DAH);
      case 'B':
        return just(DAH, DI, DI, DI);
      case 'C':
        return just(DAH, DI, DAH, DI);
      //...
      case 'P':
        return just(DI, DAH, DAH, DI);
      case 'R':
        return just(DI, DAH, DI);
      case 'S':
        return just(DI, DI, DI);
      case 'T':
        return just(DAH);

      default:
        return empty();
    }
  }

  /**
   * flatmap replace filter or map
   *
   * in practice,we do not replace map() and filter() with flatMap()
   *
   * due to the clarity of code and performance
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

//-----------------------test3------------------------------//

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

  enum Sound {DI, DAH}

}
