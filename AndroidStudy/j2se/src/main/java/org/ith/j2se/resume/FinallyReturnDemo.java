package org.ith.j2se.resume;

/**
 * Created by tanghao on 12/9/16.
 */

public class FinallyReturnDemo {
  public static void main(String[] args) {

    System.out.println(getThings());
  }

  public static int getThings() {
    int result = -1;
    try {
      int t = 1 / 0;
      System.out.println("after try ");
      return result;
    } catch (Exception e) {
    } finally {
      System.out.println("finnaly  ");
      result = 20;
    }
    System.out.println("outter  ");
    return result;
  }
}
