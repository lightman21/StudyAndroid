package org.ith.j2se.rxjava.chapter5;

import java.util.ArrayList;
import java.util.List;
import rx.Notification;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

/**
 * Created by tanghao on 12/13/16.
 */

public class Practise {
  public static void main(String[] args) {
    demo();
  }

  public static void demo() {
    List<String> results = new ArrayList<>();

    Observable.create(new OnSubscribe<Integer>() {
      @Override
      public void call(Subscriber<? super Integer> sub) {
        sub.onNext(1);
        sub.onNext(2);
        sub.onNext(1);
        sub.onNext(2);
        sub.onCompleted();
      }
    }).groupBy(new Func1<Integer, Integer>() {
      @Override
      public Integer call(Integer t) {
        return t;
      }
    }).flatMap(new Func1<GroupedObservable<Integer, Integer>, Observable<String>>() {
      @Override
      public Observable<String> call(final GroupedObservable<Integer, Integer> group) {
        return group.subscribeOn(Schedulers.newThread()).map(new Func1<Integer, String>() {
          @Override
          public String call(Integer t1) {
            System.out.println("Received: " + t1 + " on group : " + group.getKey());
            return "first groups: " + t1;
          }
        });
      }
    }).doOnEach(new Action1<Notification<? super String>>() {
      @Override
      public void call(Notification<? super String> t1) {
        System.out.println("notification => " + t1);
      }
    }).toBlocking().forEach(new Action1<String>() {
      @Override
      public void call(String s) {
        results.add(s);
      }
    });

    System.out.println("Results: " + results);

    //assertEquals(4, results.size());
  }

  public static void groupBy() {
    //group the Observable items via length if > 1
    Observable.range(0, 20)
        .groupBy(new Func1<Integer, Object>() {
          @Override
          public Object call(Integer integer) {
            return integer % 3;
          }
        }).subscribe(action -> {

      GroupedObservable gt = (GroupedObservable) action;
      //System.out.println(action.getClass().getSimpleName() + " " + gt.toList());
      gt.subscribe(System.out::println);
    });

  }

  public static void complex() {
    data().switchMap(new Func1<Integer, Observable<?>>() {
      @Override
      public Observable<?> call(Integer integer) {
        return Observable.just(integer).map(it -> it + ", ");
      }
    }).forEach(it -> System.out.println());
  }

  public static void scan() {
    //use it like a accumulator
    Observable.just(1, 2, 3, 4, 5)
        .scan(100, new Func2<Integer, Integer, Integer>() {
          @Override
          public Integer call(Integer integer, Integer integer2) {
            return integer + integer2;
          }
        }).subscribe(new Subscriber<Integer>() {
      @Override
      public void onCompleted() {

        System.out.println("onCompleted");
      }

      @Override
      public void onError(Throwable e) {

        System.out.println("onError" + e);
      }

      @Override
      public void onNext(Integer integer) {

        System.out.println("onNext " + integer);
      }
    });
  }

  public static void map() {
    System.out.println("map");

    data().map(it -> "map " + it + ", ").subscribe(System.out::print);

    System.out.println("\n\nflatmap");

    // flatMap can't maintain order and when error one item .
    // whole event done
    data().flatMap(new Func1<Integer, Observable<?>>() {
      @Override
      public Observable<?> call(Integer integer) {
        if (integer.intValue() > 6) {
          int t = integer / 0;
          return Observable.just(t);
        }

        return Observable.just(integer).map(it -> "flatmap " + it + " ");

      }
    }).subscribe(System.out::print, err -> System.out.println("error occur " + err), () -> System.out.println("onCompleted"));

    System.out.println("\nconcatMap will maintain order: ");

    data().concatMap(new Func1<Integer, Observable<?>>() {
      @Override
      public Observable<?> call(Integer integer) {
        return Observable.just("concatMap " + integer + " ");
      }
    }).subscribe(onNext -> System.out.print(onNext), err -> System.out.println("error occur " + err));


    System.out.println("\n\nFlatMapIterable: ");

    data().flatMapIterable(new Func1<Integer, Iterable<?>>() {
      @Override
      public Iterable<?> call(Integer integer) {
        //
        List<String> list = new ArrayList<>();
        Observable.just(integer).map(it -> "item " + it + ", ").forEach(it -> list.add(it));
        return list;
      }
    }).subscribe(System.out::print);
  }

  public static Observable<Integer> data() {
    return Observable.range(0, 10);
  }
}
