package org.android.study.ith.entities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by tanghao on 12/9/16.
 */

public class AppInfo implements Comparable {
  private long lastUpdateTime;
  private String name;
  private Bitmap icon;
  private int iconResId;
  private Drawable iconDrawable;

  public Drawable getIconDrawable() {
    return iconDrawable;
  }

  public void setIconDrawable(Drawable iconDrawable) {
    this.iconDrawable = iconDrawable;
  }

  public int getIconResId() {
    return iconResId;
  }

  public void setIconResId(int iconResId) {
    this.iconResId = iconResId;
  }

  public AppInfo(long lastUpdateTime, String name, Bitmap icon) {
    this.lastUpdateTime = lastUpdateTime;
    this.name = name;
    this.icon = icon;
  }

  @Override
  public int compareTo(Object o) {
    AppInfo f = (AppInfo) o;
    return getName().compareTo(f.getName());
  }

  public long getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(long lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Bitmap getIcon() {
    return icon;
  }

  public void setIcon(Bitmap icon) {
    this.icon = icon;
  }
}
