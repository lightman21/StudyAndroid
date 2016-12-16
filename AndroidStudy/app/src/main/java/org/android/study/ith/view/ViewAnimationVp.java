package org.android.study.ith.view;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;

/**
 * Created by tanghao on 12/16/16.
 */

public class ViewAnimationVp extends ViewGroup {
  public ViewAnimationVp(Context context) {
    super(context);
  }

  public ViewAnimationVp(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ViewAnimationVp(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    RotateAnimation rotate = null;
    if(rotate != null)
       rotate.start();
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {

  }

  private class MyAnimation extends Animation
  {
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
      super.applyTransformation(interpolatedTime, t);
      Matrix matrix = t.getMatrix();
//      matrix.postRotate()
    }
  }
}
