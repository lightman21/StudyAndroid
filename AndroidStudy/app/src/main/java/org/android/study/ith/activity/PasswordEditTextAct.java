package org.android.study.ith.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.concurrent.TimeUnit;
import org.android.study.ith.R;
import org.android.study.ith.util.TUtil;
import org.android.study.ith.view.TouchEventStudyVp;
import rx.Observable;

/**
 * Created by tanghao on 12/8/16.
 */
public class PasswordEditTextAct extends AppCompatActivity {
  public static final String TAG = "thDebounce";
  private Button btnDebounce;
  private ViewGroup vp;
  private View touchView;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_password_edittext);
    btnDebounce = (Button) findViewById(R.id.btn_debounce);
    vp = (ViewGroup) findViewById(R.id.id_touch_vp);
    touchView = findViewById(R.id.id_touch_view);

    btnDebounce.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Observable.just(v).debounce(3000, TimeUnit.MILLISECONDS)
            .subscribe(it -> {
              Log.d(TAG, "onClick: " + TUtil.now());
            });
      }
    });


    touchView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        Log.d(TouchEventStudyVp.TAG, "touchView's onTouch: " + "___" + event.getAction());
        return false;
      }
    });
  }

  private void show() {
    ObjectAnimator.ofInt(1)
        .start();
  }
}
