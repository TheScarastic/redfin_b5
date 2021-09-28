package androidx.core.app;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import androidx.core.content.ContextCompat;
/* loaded from: classes.dex */
public class ActivityCompat extends ContextCompat {
    public static void finishAffinity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 16) {
            activity.finishAffinity();
        } else {
            activity.finish();
        }
    }

    public static void recreate(final Activity activity) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 28) {
            activity.recreate();
        } else if (i <= 23) {
            new Handler(activity.getMainLooper()).post(new Runnable() { // from class: androidx.core.app.ActivityCompat.2
                @Override // java.lang.Runnable
                public void run() {
                    if (!activity.isFinishing() && !ActivityRecreator.recreate(activity)) {
                        activity.recreate();
                    }
                }
            });
        } else if (!ActivityRecreator.recreate(activity)) {
            activity.recreate();
        }
    }
}
