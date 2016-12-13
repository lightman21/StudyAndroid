package org.android.study.ith.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.concurrent.TimeUnit;
import org.android.study.ith.R;
import org.android.study.ith.util.TUtil;
import rx.Observable;

/**
 * Created by tanghao on 12/8/16.
 */
public class PasswordEditTextAct extends AppCompatActivity {
  private Button btnDebounce;
  public static final String TAG = "thDebounce";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_password_edittext);
    btnDebounce = (Button) findViewById(R.id.btn_debounce);

    btnDebounce.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Observable.just(v).debounce(3000, TimeUnit.MILLISECONDS)
            .subscribe(it -> {
              Log.d(TAG, "onClick: " + TUtil.now());
            });
      }
    });
  }
}
