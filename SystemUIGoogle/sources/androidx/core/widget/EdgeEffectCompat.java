package androidx.core.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EdgeEffect;
import androidx.core.os.BuildCompat;
/* loaded from: classes.dex */
public final class EdgeEffectCompat {
    public static EdgeEffect create(Context context, AttributeSet attributeSet) {
        if (BuildCompat.isAtLeastS()) {
            return Api31Impl.create(context, attributeSet);
        }
        return new EdgeEffect(context);
    }

    public static float getDistance(EdgeEffect edgeEffect) {
        if (BuildCompat.isAtLeastS()) {
            return Api31Impl.getDistance(edgeEffect);
        }
        return 0.0f;
    }

    public static void onPull(EdgeEffect edgeEffect, float f, float f2) {
        if (Build.VERSION.SDK_INT >= 21) {
            edgeEffect.onPull(f, f2);
        } else {
            edgeEffect.onPull(f);
        }
    }

    public static float onPullDistance(EdgeEffect edgeEffect, float f, float f2) {
        if (BuildCompat.isAtLeastS()) {
            return Api31Impl.onPullDistance(edgeEffect, f, f2);
        }
        onPull(edgeEffect, f, f2);
        return f;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Api31Impl {
        public static EdgeEffect create(Context context, AttributeSet attributeSet) {
            try {
                return new EdgeEffect(context, attributeSet);
            } catch (Throwable unused) {
                return new EdgeEffect(context);
            }
        }

        public static float onPullDistance(EdgeEffect edgeEffect, float f, float f2) {
            try {
                return edgeEffect.onPullDistance(f, f2);
            } catch (Throwable unused) {
                edgeEffect.onPull(f, f2);
                return 0.0f;
            }
        }

        public static float getDistance(EdgeEffect edgeEffect) {
            try {
                return edgeEffect.getDistance();
            } catch (Throwable unused) {
                return 0.0f;
            }
        }
    }
}
