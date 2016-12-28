package org.ith.j2se.rxjava.chapter3;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.tuple.Pair;
import org.ith.j2se.util.TUtils;
import rx.Observable;

/**
 * Created by tanghao on 12/28/16.
 */

public class ConcatMergeSwitchOnNextDemo {

  public static void main(String[] args) {


  }

  public static void switchOnNext() {
    Observable<String> alice = speak("To be, or not to be: that is the question", 110);
    Observable<String> bob = speak("Though this be madness, yet there is method in't", 90);
    Observable<String> jane = speak(
        "There are more things in Heaven and Earth, " +
            "Horatio, than are dreamt of in your philosophy", 100
    );

    Random rnd = new Random();
    Observable<Observable<String>> quotes = Observable.just(
        alice.map(w -> "Alice: " + w),
        bob.map(w -> "Bob:   " + w),
        jane.map(w -> "Jane:  " + w));

  }

  public static void mergeOrConcat() {

    Observable<String> alice = speak("To be, or not to be: that is the question", 110);
    Observable<String> bob = speak("Though this be madness, yet there is method in't", 90);
    Observable<String> jane = speak(
        "There are more things in Heaven and Earth, " +
            "Horatio, than are dreamt of in your philosophy", 100
    );

    //    concat(alice,bob,jane);
    merge(alice, bob, jane);
    TUtils.sleep(10 * 1000);
  }

  public static void merge(Observable<String> alice, Observable<String> bob, Observable<String> jane) {
    Observable.merge(
        alice.map(w -> String.format("%-10s", "Alice: ") + w),
        bob.map(w -> String.format("%-10s", "Bob: ") + w),
        jane.map(w -> String.format("%-10s", "Jane: ") + w)
    ).subscribe(System.out::println);
  }


  public static void concat(Observable<String> alice, Observable<String> bob, Observable<String> jane) {
    Observable.concat(
        alice.map(w -> "Alice: " + w),
        bob.map(w -> "Bob: " + w),
        jane.map(w -> "Jane: " + w)
    ).subscribe(System.out::println);
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
}
