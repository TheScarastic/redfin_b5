package com.android.systemui.toast;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import com.android.systemui.R$id;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ToastDefaultAnimation.kt */
/* loaded from: classes2.dex */
public final class ToastDefaultAnimation {
    public static final Companion Companion = new Companion(null);

    /* compiled from: ToastDefaultAnimation.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final AnimatorSet toastIn(View view) {
            Intrinsics.checkNotNullParameter(view, "view");
            View findViewById = view.findViewById(R$id.icon);
            View findViewById2 = view.findViewById(R$id.text);
            if (findViewById == null || findViewById2 == null) {
                return null;
            }
            LinearInterpolator linearInterpolator = new LinearInterpolator();
            PathInterpolator pathInterpolator = new PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f);
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1.0f);
            ofFloat.setInterpolator(pathInterpolator);
            ofFloat.setDuration(333L);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1.0f);
            ofFloat2.setInterpolator(pathInterpolator);
            ofFloat2.setDuration(333L);
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
            ofFloat3.setInterpolator(linearInterpolator);
            ofFloat3.setDuration(66L);
            findViewById2.setAlpha(0.0f);
            ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(findViewById2, "alpha", 0.0f, 1.0f);
            ofFloat4.setInterpolator(linearInterpolator);
            ofFloat4.setDuration(283L);
            ofFloat4.setStartDelay(50);
            findViewById.setAlpha(0.0f);
            ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(findViewById, "alpha", 0.0f, 1.0f);
            ofFloat5.setInterpolator(linearInterpolator);
            ofFloat5.setDuration(283L);
            ofFloat5.setStartDelay(50);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ofFloat, ofFloat2, ofFloat3, ofFloat4, ofFloat5);
            return animatorSet;
        }

        public final AnimatorSet toastOut(View view) {
            Intrinsics.checkNotNullParameter(view, "view");
            View findViewById = view.findViewById(R$id.icon);
            View findViewById2 = view.findViewById(R$id.text);
            if (findViewById == null || findViewById2 == null) {
                return null;
            }
            LinearInterpolator linearInterpolator = new LinearInterpolator();
            PathInterpolator pathInterpolator = new PathInterpolator(0.3f, 0.0f, 1.0f, 1.0f);
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.9f);
            ofFloat.setInterpolator(pathInterpolator);
            ofFloat.setDuration(250L);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.9f);
            ofFloat2.setInterpolator(pathInterpolator);
            ofFloat2.setDuration(250L);
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(view, "elevation", view.getElevation(), 0.0f);
            ofFloat3.setInterpolator(linearInterpolator);
            ofFloat3.setDuration(40L);
            ofFloat3.setStartDelay(150);
            ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
            ofFloat4.setInterpolator(linearInterpolator);
            ofFloat4.setDuration(100L);
            ofFloat4.setStartDelay(150);
            ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(findViewById2, "alpha", 1.0f, 0.0f);
            ofFloat5.setInterpolator(linearInterpolator);
            ofFloat5.setDuration(166L);
            ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(findViewById, "alpha", 1.0f, 0.0f);
            ofFloat6.setInterpolator(linearInterpolator);
            ofFloat6.setDuration(166L);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ofFloat, ofFloat2, ofFloat3, ofFloat4, ofFloat5, ofFloat6);
            return animatorSet;
        }
    }
}
