package com.google.android.systemui.assist;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.util.ArraySet;
import android.view.RenderNodeAnimator;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.android.internal.app.AssistUtils;
import com.android.systemui.R$dimen;
/* loaded from: classes2.dex */
public final class OpaUtils {
    static final Interpolator INTERPOLATOR_40_40 = new PathInterpolator(0.4f, 0.0f, 0.6f, 1.0f);
    static final Interpolator INTERPOLATOR_40_OUT = new PathInterpolator(0.4f, 0.0f, 1.0f, 1.0f);

    /* access modifiers changed from: package-private */
    public static float getDeltaDiamondPositionBottomX() {
        return 0.0f;
    }

    /* access modifiers changed from: package-private */
    public static float getDeltaDiamondPositionLeftY() {
        return 0.0f;
    }

    /* access modifiers changed from: package-private */
    public static float getDeltaDiamondPositionRightY() {
        return 0.0f;
    }

    /* access modifiers changed from: package-private */
    public static float getDeltaDiamondPositionTopX() {
        return 0.0f;
    }

    /* access modifiers changed from: package-private */
    public static Animator getTranslationAnimatorX(View view, Interpolator interpolator, int i) {
        RenderNodeAnimator renderNodeAnimator = new RenderNodeAnimator(0, 0.0f);
        renderNodeAnimator.setTarget(view);
        renderNodeAnimator.setInterpolator(interpolator);
        renderNodeAnimator.setDuration((long) i);
        return renderNodeAnimator;
    }

    /* access modifiers changed from: package-private */
    public static ObjectAnimator getAlphaObjectAnimator(View view, float f, int i, int i2, Interpolator interpolator) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.ALPHA, f);
        ofFloat.setInterpolator(interpolator);
        ofFloat.setDuration((long) i);
        ofFloat.setStartDelay((long) i2);
        return ofFloat;
    }

    /* access modifiers changed from: package-private */
    public static Animator getLongestAnim(ArraySet<Animator> arraySet) {
        long j = Long.MIN_VALUE;
        Animator animator = null;
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            Animator valueAt = arraySet.valueAt(size);
            if (valueAt.getTotalDuration() > j) {
                j = valueAt.getTotalDuration();
                animator = valueAt;
            }
        }
        return animator;
    }

    /* access modifiers changed from: package-private */
    public static ObjectAnimator getScaleObjectAnimator(View view, float f, int i, Interpolator interpolator) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(View.SCALE_X, f), PropertyValuesHolder.ofFloat(View.SCALE_Y, f));
        ofPropertyValuesHolder.setDuration((long) i);
        ofPropertyValuesHolder.setInterpolator(interpolator);
        return ofPropertyValuesHolder;
    }

    /* access modifiers changed from: package-private */
    public static ObjectAnimator getTranslationObjectAnimatorY(View view, Interpolator interpolator, float f, float f2, int i) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.Y, f2, f2 + f);
        ofFloat.setInterpolator(interpolator);
        ofFloat.setDuration((long) i);
        return ofFloat;
    }

    /* access modifiers changed from: package-private */
    public static ObjectAnimator getTranslationObjectAnimatorX(View view, Interpolator interpolator, float f, float f2, int i) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.X, f2, f2 + f);
        ofFloat.setInterpolator(interpolator);
        ofFloat.setDuration((long) i);
        return ofFloat;
    }

    /* access modifiers changed from: package-private */
    public static float getPxVal(Resources resources, int i) {
        return (float) resources.getDimensionPixelOffset(i);
    }

    /* access modifiers changed from: package-private */
    public static boolean isAGSACurrentAssistant(Context context) {
        ComponentName assistComponentForUser = new AssistUtils(context).getAssistComponentForUser(-2);
        return assistComponentForUser != null && "com.google.android.googlequicksearchbox/com.google.android.voiceinteraction.GsaVoiceInteractionService".equals(assistComponentForUser.flattenToString());
    }

    /* access modifiers changed from: package-private */
    public static float getDeltaDiamondPositionTopY(Resources resources) {
        return -getPxVal(resources, R$dimen.opa_diamond_translation);
    }

    /* access modifiers changed from: package-private */
    public static float getDeltaDiamondPositionLeftX(Resources resources) {
        return -getPxVal(resources, R$dimen.opa_diamond_translation);
    }

    /* access modifiers changed from: package-private */
    public static float getDeltaDiamondPositionRightX(Resources resources) {
        return getPxVal(resources, R$dimen.opa_diamond_translation);
    }

    /* access modifiers changed from: package-private */
    public static float getDeltaDiamondPositionBottomY(Resources resources) {
        return getPxVal(resources, R$dimen.opa_diamond_translation);
    }
}
