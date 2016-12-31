package org.ith.j2se.rxjava.chapter3.customOperator;

import org.apache.commons.lang3.tuple.Pair;
import org.ith.j2se.util.TUtils;
import rx.Observable;

import static rx.Observable.empty;
import static rx.Observable.just;

/**
 * Created by tanghao on 12/31/16.
 */

public class Demo {
  public static void main(String[] args) {
    useCompose();
  }

  private static <T> Observable.Transformer<T, T> odd() {
    Observable<Boolean> trueFalse = just(true, false).repeat();
    return upstream -> upstream
        .zipWith(trueFalse, Pair::of)
        .filter(Pair::getRight)
        .map(Pair::getLeft);
  }

  private static void useCompose() {
    Observable<Character> alphabet =
        Observable
            .range(0, 'Z' - 'A' + 1)
            .map(c -> (char) ('A' + c));

    alphabet
        .compose(odd())
        .forEach(System.out::println);
  }


  public static void pairThings() {
    Observable<Boolean> trueFalse = just(true, false).repeat();
    Observable<String> upstream = just("tang", "hao", "da", "wang");
    Observable<String> downstream = upstream
        .zipWith(trueFalse, Pair::of)
        .filter(Pair::getRight)
        .map(Pair::getLeft);

    downstream.subscribe(System.out::println);

    TUtils.sleep();
  }

  public static void nonThirdparty() {
    Observable<Boolean> trueFalse = just(false, true).repeat();
    Observable<String> upstream = just("tang", "hao", "da", "wang");
    upstream.zipWith(trueFalse, (t, bool) ->
        bool ? just(t) : empty())
        .flatMap(obs -> obs) //Note flatMap here functionality
        .subscribe(System.out::println);

    TUtils.sleep();
  }
}
