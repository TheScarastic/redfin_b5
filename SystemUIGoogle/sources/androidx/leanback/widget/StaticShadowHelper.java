package androidx.leanback.widget;

import android.os.Build;
/* loaded from: classes.dex */
final class StaticShadowHelper {
    /* access modifiers changed from: package-private */
    public static boolean supportsShadow() {
        return Build.VERSION.SDK_INT >= 21;
    }
}
