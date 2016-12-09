package org.android.study.ith.base;

import android.app.Application;

/**
 * Created by tanghao on 12/9/16.
 */

public class TBaseApplication extends Application {

  private static Application mApp;

  public static Application getAppContext() {
    return mApp;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    mApp = this;
  }
}
