package org.android.study.ith.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import org.android.study.ith.R;

/**
 * Created by tanghao on 12/5/16.
 *
 * learn by : http://stackoverflow.com/questions/2150078/how-to-check-visibility-of-software-keyboard-in-android
 *
 *
 *
 * Some improvements to avoid wrongly detect the visibility of soft keyboard on high density
 * devices:
 *
 * Threshold of height difference should be defined as 128 dp, not 128 pixels. Refer to Google
 * design doc about Metrics and Grid
 *
 * 48 dp is comfortable size for touch object and 32 dp is minimum for buttons.
 *
 *
 * Generic soft keyboard should include 4 rows of key buttons , so minimum keyboard height should
 * be: 32 dp * 4 = 128 dp,
 *
 * that means threshold size should transfer to pixels by multiply device density. For xxxhdpi
 * devices (density 4), the soft keyboard height threshold should be 128 * 4 = 512 pixels. Height
 * difference between root view and its visible area: root view height - status bar height - visible
 * frame height = root view bottom - visible frame bottom, since status bar height equal to the top
 * of root view visible frame.
 */
public class SoftKeyBoardActivity extends Activity {

  public static final String TAG = SoftKeyBoardActivity.class.getSimpleName();

  /* 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard */
  private final float SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128f;
  private View mDecorView;
  private Rect mRect = new Rect();
  private int lastVisibleH;
  private float minKeyBoardH;
  private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener =
      new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          mDecorView.getWindowVisibleDisplayFrame(mRect);
          int currentH = mRect.height();
          int diff = Math.abs(lastVisibleH - currentH);

          if (lastVisibleH != -1 && diff > minKeyBoardH) {

            if (lastVisibleH > currentH) {
              //now softkeyboard show
              //now diff is the keyboard's height
              // TODO: 12/5/16 do sth

              Log.d(TAG, "onGlobalLayout: softkeyboard show");
            } else {
              //now softkeyboard hide
              // TODO: 12/5/16 do sth

              Log.d(TAG, "onGlobalLayout: softkeyboard hide");
            }
          }

          lastVisibleH = currentH;
        }
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    dealWithSoftKeyboard();
  }

  private void dealWithSoftKeyboard() {
    minKeyBoardH = TypedValue
        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD,
            getResources().getDisplayMetrics());
    mDecorView = getWindow().getDecorView();
    mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    removeGlobalListener();
  }


  private void animateScroll(final View v, final int distance) {
    //300 ms  distance
    //refresh 10 times
    final int c[] = new int[1];
    v.post(new Runnable() {
      @Override
      public void run() {
        if (c[0] >= 10) {
          v.removeCallbacks(null);
        } else {
          v.scrollBy(0, distance / 10);
          c[0]++;
          v.postDelayed(this, 150 / 10);
        }
      }
    });
  }

  @SuppressWarnings("deprecated")
  @TargetApi(16)
  private void removeGlobalListener() {
    if (Build.VERSION.SDK_INT < 16) {
      mDecorView.getViewTreeObserver().removeGlobalOnLayoutListener(mGlobalLayoutListener);
    } else {
      mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
    }
  }
}
