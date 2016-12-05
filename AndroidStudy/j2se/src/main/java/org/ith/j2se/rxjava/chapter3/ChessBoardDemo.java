package org.ith.j2se.rxjava.chapter3;

import rx.Observable;

/**
 * Created by tanghao on 12/6/16.
 */
public class ChessBoardDemo {

  /**
   * one with chessboard's row(ranks,1 to 8) and one with columns(files,a to h).
   *
   * we wourld like to find all possible 64 squares on a chessboard.
   */
  public static void main(String[] args) {

    Observable<Integer> oneToEight = Observable.range(1, 8);

    Observable<String> ranks = oneToEight.map(Object::toString);

    Observable<String> files = oneToEight.map(x -> 'A' + x - 1)
        .map(ascii -> (char) ascii.intValue())
        .map(ch -> Character.toString(ch));

    Observable<String> squares = files.flatMap(file -> ranks.map(rank -> file + rank));
    squares.subscribe(System.out::println);
  }
}
