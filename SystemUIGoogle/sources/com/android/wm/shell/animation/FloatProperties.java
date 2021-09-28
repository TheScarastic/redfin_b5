package com.android.wm.shell.animation;

import android.graphics.Rect;
import android.graphics.RectF;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: FloatProperties.kt */
/* loaded from: classes2.dex */
public final class FloatProperties {
    public static final Companion Companion = new Companion(null);
    public static final FloatPropertyCompat<Rect> RECT_X = new FloatPropertyCompat<Rect>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECT_X$1
        public void setValue(Rect rect, float f) {
            if (rect != null) {
                rect.offsetTo((int) f, rect.top);
            }
        }

        public float getValue(Rect rect) {
            Integer valueOf = rect == null ? null : Integer.valueOf(rect.left);
            if (valueOf == null) {
                return -3.4028235E38f;
            }
            return (float) valueOf.intValue();
        }
    };
    public static final FloatPropertyCompat<Rect> RECT_Y = new FloatPropertyCompat<Rect>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECT_Y$1
        public void setValue(Rect rect, float f) {
            if (rect != null) {
                rect.offsetTo(rect.left, (int) f);
            }
        }

        public float getValue(Rect rect) {
            Integer valueOf = rect == null ? null : Integer.valueOf(rect.top);
            if (valueOf == null) {
                return -3.4028235E38f;
            }
            return (float) valueOf.intValue();
        }
    };
    public static final FloatPropertyCompat<Rect> RECT_WIDTH = new FloatPropertyCompat<Rect>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECT_WIDTH$1
        public float getValue(Rect rect) {
            Intrinsics.checkNotNullParameter(rect, "rect");
            return (float) rect.width();
        }

        public void setValue(Rect rect, float f) {
            Intrinsics.checkNotNullParameter(rect, "rect");
            rect.right = rect.left + ((int) f);
        }
    };
    public static final FloatPropertyCompat<Rect> RECT_HEIGHT = new FloatPropertyCompat<Rect>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECT_HEIGHT$1
        public float getValue(Rect rect) {
            Intrinsics.checkNotNullParameter(rect, "rect");
            return (float) rect.height();
        }

        public void setValue(Rect rect, float f) {
            Intrinsics.checkNotNullParameter(rect, "rect");
            rect.bottom = rect.top + ((int) f);
        }
    };
    public static final FloatPropertyCompat<RectF> RECTF_X = new FloatPropertyCompat<RectF>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECTF_X$1
        public void setValue(RectF rectF, float f) {
            if (rectF != null) {
                rectF.offsetTo(f, rectF.top);
            }
        }

        public float getValue(RectF rectF) {
            if (rectF == null) {
                return -3.4028235E38f;
            }
            return rectF.left;
        }
    };
    public static final FloatPropertyCompat<RectF> RECTF_Y = new FloatPropertyCompat<RectF>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECTF_Y$1
        public void setValue(RectF rectF, float f) {
            if (rectF != null) {
                rectF.offsetTo(rectF.left, f);
            }
        }

        public float getValue(RectF rectF) {
            if (rectF == null) {
                return -3.4028235E38f;
            }
            return rectF.top;
        }
    };

    /* compiled from: FloatProperties.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
