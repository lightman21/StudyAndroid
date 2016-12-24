package org.android.study.ith.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by tanghao on 12/23/16.
 */

public class TouchEventStudyView extends TextView {

  public static final String TAG = "StudyTouch";

  public TouchEventStudyView(Context context) {
    super(context);
  }

  public TouchEventStudyView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public TouchEventStudyView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    boolean dresult = super.onTouchEvent(event);

    Log.d(TAG, "View onTouchEvent: " + dresult + "___" + event.getAction());

    return !dresult;
  }
}
