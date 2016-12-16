package org.android.study.ith;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import org.android.study.ith.activity.PasswordEditTextAct;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    enableStrictMode();
  }

  @Override
  protected void onResume() {
    super.onResume();
    avoidBlockingIO();
  }

  @SuppressWarnings("unused")
  public void goPass() {
    startActivity(new Intent(this, PasswordEditTextAct.class));
  }

  public void enableStrictMode() {
    StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .penaltyDialog()
        .build();

    StrictMode.setThreadPolicy(threadPolicy);
  }

  public void mockBlockingIO() {
    FileOutputStream fos = null;
    try {
      File mockfile = new File(getCacheDir() + File.separator + "mock_blocking_io");
      if (!mockfile.exists())
        mockfile.createNewFile();

      fos = new FileOutputStream(mockfile);
      for (int i = 0; i < 80; i++) {
        fos.write((int) Math.random());
        SystemClock.sleep(10);
      }
      fos.flush();
    } catch (Exception e) {
      //ignore
    } finally {
      try {
        fos.close();
      } catch (Exception e) {
        //ignore
      }
    }
  }

  public void avoidBlockingIO()
  {
    Schedulers.io().createWorker().schedule(()->mockBlockingIO());
  }
}
