package org.android.study.ith.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by tanghao on 12/23/16.
 */

public class TouchEventStudyVp extends LinearLayout {

  public static final String TAG = "StudyTouch";

  public TouchEventStudyVp(Context context) {
    super(context);
  }

  public TouchEventStudyVp(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public TouchEventStudyVp(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    boolean dresult = super.onTouchEvent(event);
    Log.d(TAG, "ViewGroup onTouchEvent: " + "___" + event.getAction());
    return dresult;
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    boolean dresult = super.dispatchTouchEvent(ev);
    Log.d(TAG, "ViewGroup dispatchTouchEvent: " + dresult + "___" + ev.getAction());
    return dresult;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    boolean dresult = super.onInterceptTouchEvent(ev);
    Log.d(TAG, "ViewGroup onInterceptTouchEvent: " + dresult + "___" + ev.getAction());
    return dresult;
  }
}
