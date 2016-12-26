package org.ith.j2se.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tanghao on 12/12/16.
 */

public class TUtils {
  public static String now() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");
    return sdf.format(new Date());
  }

  public static void sleep() {
    try {
      Thread.sleep(3 * 1000);
    } catch (Exception e) {

    }
  }


  public static void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (Exception e) {

    }
  }
}
