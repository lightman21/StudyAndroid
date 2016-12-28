package org.ith.j2se.util;

import java.lang.reflect.Method;

/**
 * Created by tanghao on 12/27/16.
 */

public class ReflectionDemo {
  public static void main(String[] args) {

    Method[] methods = ReflectionDemo.class.getMethods();
    for (Method method : methods) {
      System.out.println(method.getName() + "  " + method.getDeclaringClass());
    }
  }
}
