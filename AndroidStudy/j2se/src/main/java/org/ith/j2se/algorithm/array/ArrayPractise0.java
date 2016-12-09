package org.ith.j2se.algorithm.array;

/**
 * Created by tanghao on 12/9/16.
 *
 * Given a sorted array,remove the duplicates in place such that each element appear only once and
 * return the new length.
 *
 * Do not allocate extra space for another array,you must do this in place with constant memory.
 *
 * For example, Given input array A = [1,1,2]
 *
 * You function should return length = 2, and A is now [1,2]
 */
public class ArrayPractise0 {

  public static void main(String[] args) {
    int[] arr = {1, 3, 4, 4, 5, 5, 5, 6, 8};
    int t = removeDuplicate(arr);
    System.out.println(t);
  }

  /**
   * 1,2,3,3,4,5,6,6,8 第一次比较0,1 第二次比较1,2 第三次比较2,3 ...
   */
  public static int removeDuplicate(int[] arr) {

    if (arr.length == 0 || arr.length == 1) return arr.length;

    // sorted sorted sorted
    int len = 1;

    for (int i = 1; i < arr.length; i++) {
      if (arr[i] != arr[len - 1]) {
        arr[len++] = arr[i];
      }
    }

    return len;
  }
}
