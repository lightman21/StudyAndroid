package org.ith.j2se.resume;

import java.util.Random;

/**
 * Created by tanghao on 12/29/16.
 */

public class ThreadLocalThings {
  private static ThreadLocal<Integer> mThLocal = new ThreadLocal<>();

  public static void main(String[] args) {
    Thread mLastThread;

    Random random = new Random();
    for (int i = 0; i < 3; i++) {
      mLastThread = new Thread() {
        public void run() {
          mThLocal.set(random.nextInt());
        }
      };
      mLastThread.start();
    }

    System.out.println(mThLocal.get());
  }
}
