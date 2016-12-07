package org.ith.j2se.rxjava.chapter3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rx.Observable;

/**
 * Created by tanghao on 12/7/16.
 */
public class CollectDemo {

  public static void main(String[] args) {
    scan();
  }


  public static void scan() {
    Observable result = Observable.just("aaa", "bbbb", "c")
        .filter(it -> it.length() > 2).collect(ArrayList::new, List::add).toList();


  }

  public static void first() {
    Observable.range(10, 20)
        .reduce(new ArrayList<>(), (list, item) -> {
          list.add(item);
          return list;
        });
  }

  //overcome that verboseness
  public static void second() {
    Observable.range(10, 20)
        .collect(ArrayList::new, List::add);
  }

  public static void dupicate() {
    Observable<Integer> randomInts = Observable.create(sub -> {
      Random random = new Random();
      while (!sub.isUnsubscribed()) {
        sub.onNext(random.nextInt(1000));
      }
    });

    Observable<Integer> uniqueRandomInts = randomInts.distinct().take(10);
    uniqueRandomInts.subscribe(System.out::println);

  }
}
