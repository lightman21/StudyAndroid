package org.android.study.ith.util;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.android.study.ith.base.TBaseApplication;
import org.android.study.ith.entities.AppInfo;
import rx.Observable;

/**
 * Created by tanghao on 12/9/16.
 */
public class TUtil {

    public static String now() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");
    return sdf.format(new Date());
  }


  public static Observable<AppInfo> getApps(Activity activity) {
    return Observable.create(subscriber -> {
          final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
          mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

          List<ResolveInfo> infos = activity.getPackageManager().queryIntentActivities(mainIntent, 0);
          Collections.sort(infos, (ResolveInfo o1, ResolveInfo o2) -> {
            return o1.priority - o2.priority;
          });

          for (ResolveInfo info : infos) {

            if (subscriber.isUnsubscribed()) return;

            if ((info.activityInfo.flags & ApplicationInfo.FLAG_SYSTEM ) == 1){
              continue;
            }

            subscriber.onNext(toAppInfo(info));

          }

          if (!subscriber.isUnsubscribed()) subscriber.onCompleted();

        }

    );

  }

  public static Bitmap toBitmap(int drawableId) {
    return BitmapFactory.decodeResource(appContext().getResources(), drawableId);
  }

  public static AppInfo toAppInfo(ResolveInfo info) {
    AppInfo api = new AppInfo(0L, info.loadLabel(pkManager()).toString(), toBitmap(info.getIconResource()));
    api.setIconResId(info.getIconResource());
    Drawable d = info.loadIcon(pkManager());
    api.setIconDrawable(d);
    return api;
  }

  public static PackageManager pkManager() {
    return TBaseApplication.getAppContext().getPackageManager();
  }

  public static Context appContext() {
    return TBaseApplication.getAppContext();
  }

  public static void animate(View view)
  {
    ObjectAnimator.ofInt(view,"x",1000).start();
  }

}
