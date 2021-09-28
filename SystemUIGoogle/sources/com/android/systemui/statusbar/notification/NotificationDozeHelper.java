package com.android.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.ImageView;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class NotificationDozeHelper {
    private static final int DOZE_ANIMATOR_TAG = R$id.doze_intensity_tag;
    private final ColorMatrix mGrayscaleColorMatrix = new ColorMatrix();

    public void updateGrayscale(ImageView imageView, float f) {
        if (f > 0.0f) {
            updateGrayscaleMatrix(f);
            imageView.setColorFilter(new ColorMatrixColorFilter(this.mGrayscaleColorMatrix));
            return;
        }
        imageView.setColorFilter((ColorFilter) null);
    }

    public void startIntensityAnimation(ValueAnimator.AnimatorUpdateListener animatorUpdateListener, boolean z, long j, Animator.AnimatorListener animatorListener) {
        float f = 0.0f;
        float f2 = z ? 0.0f : 1.0f;
        if (z) {
            f = 1.0f;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(f2, f);
        ofFloat.addUpdateListener(animatorUpdateListener);
        ofFloat.setDuration(500L);
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ofFloat.setStartDelay(j);
        if (animatorListener != null) {
            ofFloat.addListener(animatorListener);
        }
        ofFloat.start();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$setDozing$0(Consumer consumer, ValueAnimator valueAnimator) {
        consumer.accept((Float) valueAnimator.getAnimatedValue());
    }

    public void setDozing(Consumer<Float> consumer, boolean z, boolean z2, long j, final View view) {
        if (z2) {
            startIntensityAnimation(new ValueAnimator.AnimatorUpdateListener(consumer) { // from class: com.android.systemui.statusbar.notification.NotificationDozeHelper$$ExternalSyntheticLambda0
                public final /* synthetic */ Consumer f$0;

                {
                    this.f$0 = r1;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    NotificationDozeHelper.lambda$setDozing$0(this.f$0, valueAnimator);
                }
            }, z, j, new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.NotificationDozeHelper.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    view.setTag(NotificationDozeHelper.DOZE_ANIMATOR_TAG, null);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    view.setTag(NotificationDozeHelper.DOZE_ANIMATOR_TAG, animator);
                }
            });
            return;
        }
        Animator animator = (Animator) view.getTag(DOZE_ANIMATOR_TAG);
        if (animator != null) {
            animator.cancel();
        }
        consumer.accept(Float.valueOf(z ? 1.0f : 0.0f));
    }

    public void updateGrayscaleMatrix(float f) {
        this.mGrayscaleColorMatrix.setSaturation(1.0f - f);
    }
}
