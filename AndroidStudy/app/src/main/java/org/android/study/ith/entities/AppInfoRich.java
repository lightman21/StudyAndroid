package org.android.study.ith.entities;

import android.app.Activity;
import android.content.pm.ResolveInfo;

/**
 * Created by tanghao on 12/9/16.
 */

public class AppInfoRich {
  private Activity activity;
  private ResolveInfo info;

  public AppInfoRich(Activity activity, ResolveInfo info) {
    this.activity = activity;
    this.info = info;
  }

  public Activity getActivity() {
    return activity;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  public ResolveInfo getInfo() {
    return info;
  }

  public void setInfo(ResolveInfo info) {
    this.info = info;
  }
}
