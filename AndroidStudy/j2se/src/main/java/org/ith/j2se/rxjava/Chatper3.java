package org.ith.j2se.rxjava;

import java.util.concurrent.TimeUnit;
import rx.Observable;

import static org.ith.j2se.rxjava.Chatper3.Sound.DAH;
import static org.ith.j2se.rxjava.Chatper3.Sound.DI;
import static rx.Observable.empty;
import static rx.Observable.just;
import static rx.Observable.timer;

/**
 * 2016-12-04 22:08:50
 *
 * what flatmap essentially does is take a master sequence(Observable) of values appearing over
 * time(event), and replaces each of the events with an independent independent independent
 * subsequence.
 */
public class Chatper3 {
  public static void main(String[] args) {
    test4();
  }

  public static void test4() {
    Observable.just(1, 2, 3)
        .delay(10, TimeUnit.SECONDS)
        .subscribe(i -> System.out.println("delay --> " + i + ",,," + Thread.currentThread().getName()))
    ;
    //equivalent to
    Observable.timer(10, TimeUnit.SECONDS)
        .flatMap(i -> just(1, 2, 3))
        .subscribe(i -> System.out.println("timer =============== " + i + ",,," + Thread.currentThread().getName()))
    ;

//    if not sleep and just let the test4 method done,you will see nothing
//    try {
//      Thread.sleep(10 * 1000);
//      System.out.println("now sleep completed!");
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }

    //----------------------why flatmap has no order?
    Observable.just("Lorem", "sit", "amet",
        "consectetur", "adipiscing")
        .delay(w -> timer(w.length(), TimeUnit.MILLISECONDS))
        .subscribe(w -> System.out.println("delay---> " + w + ",,," + w.length()));


    Observable.just("Lorem", "sit", "amet",
        "consectetur", "adipiscing")
        .flatMap(w -> {
          System.out.println("flatmap inner -> " + w + ",,," + w.length());
          return just(w).timer(w.length(), TimeUnit.MILLISECONDS);
        })
        .map(x -> x)
        .subscribe();
    //----------------------why flatmap has no order?

//    Observable.just("Lorem", "ipsum", "dolor", "sit", "amet",
//        "consectetur", "adipiscing", "elit")
//        .flatMap(w -> timer(w.length(), TimeUnit.MILLISECONDS)
//            .map(x -> x)
//        )
//        .subscribe(x -> System.out.println("subscribe " + x))
//    ;

    //    if not sleep and just let the test4 method done,you will see nothing
    try {
      Thread.sleep(1 * 1000);
      System.out.println("now sleep completed!");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


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
