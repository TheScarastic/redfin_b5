package androidx.leanback.widget;

import android.os.Build;
/* loaded from: classes.dex */
final class ShadowHelper {
    /* access modifiers changed from: package-private */
    public static boolean supportsDynamicShadow() {
        return Build.VERSION.SDK_INT >= 21;
    }
}
