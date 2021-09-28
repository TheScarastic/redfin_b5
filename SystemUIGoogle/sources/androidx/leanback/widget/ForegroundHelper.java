package androidx.leanback.widget;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
/* loaded from: classes.dex */
final class ForegroundHelper {
    /* access modifiers changed from: package-private */
    public static void setForeground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 23) {
            view.setForeground(drawable);
        }
    }
}
