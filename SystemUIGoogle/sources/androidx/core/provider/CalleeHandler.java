package androidx.core.provider;

import android.os.Handler;
import android.os.Looper;
/* loaded from: classes.dex */
class CalleeHandler {
    /* access modifiers changed from: package-private */
    public static Handler create() {
        if (Looper.myLooper() == null) {
            return new Handler(Looper.getMainLooper());
        }
        return new Handler();
    }
}
