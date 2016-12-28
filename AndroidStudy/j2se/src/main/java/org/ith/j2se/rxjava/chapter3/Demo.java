package org.ith.j2se.rxjava.chapter3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.tuple.Pair;
import org.ith.j2se.util.TUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by tanghao on 12/27/16.
 */

public class Demo {
  public static void main(String[] args) {
    Observable.just("this is a day never to be forgotten".split(" "));

    String quote = "this is a day never to be forgotten";
    speak(quote,100)
        .subscribe(System.out::println);

    TUtils.sleep();
  }

  public static Observable<String> speak(String quote, long millisPerChar) {
    String[] tokes = quote.replaceAll("[:,]", "").split(" ");
    Observable<String> words = Observable.from(tokes);
    Observable<Long> absoluteDelay = words
        .map(String::length)
        .map(len -> len * millisPerChar)
        .scan((total, current) -> total + current);

    return words
        .zipWith(absoluteDelay.startWith(0L), Pair::of)
        .flatMap(pair -> Observable.just(pair.getLeft()).delay(pair.getRight(), TimeUnit.MILLISECONDS)
        );
  }

  public static void slice() {
    Observable<Integer> rand = Observable.create(new Observable.OnSubscribe<Integer>() {
      @Override
      public void call(Subscriber<? super Integer> subscriber) {
        while (!subscriber.isUnsubscribed()) {
          int d = new Random().nextInt(15);
          System.out.println(Thread.currentThread().getName() + " generate ----------" + d);
          subscriber.onNext(d);
        }

        System.out.println("Now subscribe uuuuuunsubscribe");
      }
    }).subscribeOn(Schedulers.computation());

    rand.takeWhile(it -> it > 1)
        .subscribe(d -> System.out.println("Received " + d));

    System.out.println("run After takeWile");

//    TUtils.sleep();
  }

  public static void distinct() {
    Observable<Integer> randInt = Observable.create(new Observable.OnSubscribe<Integer>() {
      @Override
      public void call(Subscriber<? super Integer> subscriber) {
        while (!subscriber.isUnsubscribed())
          subscriber.onNext(new Random().nextInt(15));
      }
    });

    //caution: the stream will never completed
    randInt.distinct().take(5).subscribe(d -> System.out.println(Thread.currentThread().getName() + ", " + d),
        err -> System.out.println("onError " + err),
        () -> System.out.println("onCompleted!!!!"));

    System.out.println("================\n\n");

//    randInt.distinctUntilChanged().subscribe(System.out::println);

    Observable.just(1, 1, 1, 2).distinctUntilChanged().subscribe(new Observer<Integer>() {
      @Override
      public void onCompleted() {
        System.out.println("onCompleted");
      }

      @Override
      public void onError(Throwable e) {

        System.out.println("onError");
      }

      @Override
      public void onNext(Integer integer) {

        System.out.println("onNext " + integer);
      }
    });


    System.out.println("\n===========distinct===========\n");

    Observable.just(1, 1, 1, 2)
        .distinct()
        .subscribe(new Observer<Integer>() {
          @Override
          public void onCompleted() {
            System.out.println("onCompleted");
          }

          @Override
          public void onError(Throwable e) {

            System.out.println("onError");
          }

          @Override
          public void onNext(Integer integer) {

            System.out.println("onNext " + integer);
          }
        });
  }

  public static void single() {
    Observable.just(1, 1, 1, 1)
        .single()
        .subscribe(System.out::println);
  }


  public static void reduction() {
    Observable<List<Integer>> all = Observable
        .range(10, 20)
        .reduce(new ArrayList<>(), (list, item) -> {
          list.add(item);
          return list;
        });

    //a handy way
    Observable<List<Integer>> handyAll = Observable
        .range(10, 20)
        .collect(ArrayList::new, List::add);

    all.forEach(System.out::print);

    System.out.println("\n\n=========handyAll\n");

    handyAll.forEach(System.out::print);

    System.out.println("\n\n=========toList\n");

    Observable.range(10, 20).toList().forEach(System.out::print);

    System.out.println("\n\n=========collect used in StringBuffer\n");

    Observable
        .range(1, 10)
        .collect(StringBuilder::new,
            (sb, x) -> sb.append(x).append(", "))
        .map(StringBuilder::toString)
        .subscribe(d -> System.out.println(d));


    System.out.println("\n\n=========Single\n");

//    handyAll.single().subscribe(d->System.out.print(d + ",,," + System.currentTimeMillis()));

    Observable.range(0, 10).single().subscribe(System.out::println, err -> System.out.println("onError cause = " + err.getCause()), () -> System.out.println("onComplete"));

  }

  private static void seeIfGenericWorks(List<Object> list) {
    for (Object obj : list) {
      System.out.println(obj);
      list.add(obj);
    }

    list.add("string");
    list.add(110);
  }

  private void test() {
    List<String> stringList = new ArrayList<>();
    stringList.add("Hello");
//    seeIfGenericWorks(stringList);


  }
}
