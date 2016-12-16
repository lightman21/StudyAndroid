package org.android.study.ith.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.android.study.ith.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by tanghao on 12/16/16.
 */

public class DownloadVideoAct extends AppCompatActivity {

  @BindView(R.id.pb_download)
  ProgressBar mProgress;

  @BindView(R.id.btn_download)
  Button mDownloadBtn;
  private PublishSubject<Integer> mDownloadProgress = PublishSubject.create();

  private String savePath = "/sdcard/thlearn.mp4";
  private String destUrl = "http://23.106.150.49:9999/th/atOn.mp4";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_downloadvideo);
    ButterKnife.bind(this);

    mDownloadBtn.setOnClickListener(v ->
        rxDownload(destUrl, savePath)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(it -> System.out.println(it), err -> System.out.println(err))
    );
  }

  private Observable<Boolean> rxDownload(String source, String destination) {
    return Observable.create(subscriber -> {
      try {
        boolean result = downloadFile(source, destination);
        if (result) {
          subscriber.onNext(true);
          subscriber.onCompleted();
        } else {
          subscriber.onError(new Throwable("Download  failed."));
        }
      } catch (Exception e) {
        subscriber.onError(e);
        e.printStackTrace();
      }
    });
  }

  private boolean downloadFile(String source, String destination) {
    boolean result = false;
    InputStream input = null;
    OutputStream output = null;
    HttpURLConnection connection = null;
    try {
      URL url = new URL(source);
      connection = (HttpURLConnection) url.openConnection();
      connection.connect();

      if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
        return false;
      }

      int fileLength = connection.getContentLength();

      input = connection.getInputStream();
      output = new FileOutputStream(destination);

      byte data[] = new byte[1024];
      long total = 0;
      int count;
      while ((count = input.read(data)) != -1) {
        total += count;

        if (fileLength > 0) {
          int percentage = (int) (total * 100 / fileLength);
          mDownloadProgress.onNext(percentage);
        }
        output.write(data, 0, count);
      }
      mDownloadProgress.onCompleted();
      result = true;
    } catch (Exception e) {
      mDownloadProgress.onError(e);
    } finally {
      try {
        if (output != null) {
          output.close();
        }
        if (input != null) {
          input.close();
        }
      } catch (IOException e) {
        mDownloadProgress.onError(e);
      }

      if (connection != null) {
        connection.disconnect();
        mDownloadProgress.onCompleted();
      }
    }
    return result;
  }

}
