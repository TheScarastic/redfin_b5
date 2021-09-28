package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.android.keyguard.AlphaOptimizedLinearLayout;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import com.android.systemui.animation.Interpolators;
/* loaded from: classes.dex */
public class KeyguardUserSwitcherListView extends AlphaOptimizedLinearLayout {
    private boolean mAnimating;
    private final AppearAnimationUtils mAppearAnimationUtils;
    private final DisappearAnimationUtils mDisappearAnimationUtils;

    public KeyguardUserSwitcherListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAppearAnimationUtils = new AppearAnimationUtils(context, 220, -0.5f, 0.5f, Interpolators.FAST_OUT_SLOW_IN);
        this.mDisappearAnimationUtils = new DisappearAnimationUtils(context, 220, 0.2f, 0.2f, Interpolators.FAST_OUT_SLOW_IN_REVERSE);
    }

    public void setDarkAmount(float f) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof KeyguardUserDetailItemView) {
                ((KeyguardUserDetailItemView) childAt).setDarkAmount(f);
            }
        }
    }

    public boolean isAnimating() {
        return this.mAnimating;
    }

    public void updateVisibilities(boolean z, boolean z2) {
        this.mAnimating = false;
        int childCount = getChildCount();
        KeyguardUserDetailItemView[] keyguardUserDetailItemViewArr = new KeyguardUserDetailItemView[childCount];
        for (int i = 0; i < childCount; i++) {
            keyguardUserDetailItemViewArr[i] = (KeyguardUserDetailItemView) getChildAt(i);
            keyguardUserDetailItemViewArr[i].clearAnimation();
            if (i == 0) {
                keyguardUserDetailItemViewArr[i].updateVisibilities(true, z, z2);
                keyguardUserDetailItemViewArr[i].setClickable(true);
            } else {
                keyguardUserDetailItemViewArr[i].setClickable(z);
                keyguardUserDetailItemViewArr[i].updateVisibilities(z2 || z, true, false);
            }
        }
        if (z2) {
            keyguardUserDetailItemViewArr[0] = null;
            setClipChildren(false);
            setClipToPadding(false);
            this.mAnimating = true;
            (z ? this.mAppearAnimationUtils : this.mDisappearAnimationUtils).startAnimation(keyguardUserDetailItemViewArr, new Runnable() { // from class: com.android.systemui.statusbar.policy.KeyguardUserSwitcherListView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardUserSwitcherListView.m373$r8$lambda$TmINzvQcbZlD7JNH54Pil_GAJ4(KeyguardUserSwitcherListView.this);
                }
            });
        }
    }

    public /* synthetic */ void lambda$updateVisibilities$0() {
        setClipChildren(true);
        setClipToPadding(true);
        this.mAnimating = false;
    }

    public void replaceView(KeyguardUserDetailItemView keyguardUserDetailItemView, int i) {
        removeViewAt(i);
        addView(keyguardUserDetailItemView, i);
    }

    public void removeLastView() {
        removeViewAt(getChildCount() - 1);
    }
}
