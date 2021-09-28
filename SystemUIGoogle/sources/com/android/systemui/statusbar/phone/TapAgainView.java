package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.wm.shell.animation.Interpolators;
/* loaded from: classes.dex */
public class TapAgainView extends TextView {
    public TapAgainView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        updateColor();
    }

    /* access modifiers changed from: package-private */
    public void updateColor() {
        setTextColor(getResources().getColor(R$color.notif_pill_text, ((TextView) this).mContext.getTheme()));
        setBackground(getResources().getDrawable(R$drawable.rounded_bg_full, ((TextView) this).mContext.getTheme()));
    }

    public void animateIn() {
        int dimensionPixelSize = ((TextView) this).mContext.getResources().getDimensionPixelSize(R$dimen.keyguard_indication_y_translation);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, View.ALPHA, 1.0f);
        ofFloat.setStartDelay(150);
        ofFloat.setDuration(317L);
        ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, (float) dimensionPixelSize, 0.0f);
        ofFloat2.setDuration(600L);
        ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.TapAgainView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                TapAgainView.this.setTranslationY(0.0f);
            }
        });
        animatorSet.playTogether(ofFloat2, ofFloat);
        animatorSet.start();
        setVisibility(0);
    }

    public void animateOut() {
        int dimensionPixelSize = ((TextView) this).mContext.getResources().getDimensionPixelSize(R$dimen.keyguard_indication_y_translation);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f);
        ofFloat.setDuration(167L);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, 0.0f, (float) (-dimensionPixelSize));
        ofFloat2.setDuration(167L);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.TapAgainView.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                TapAgainView.this.setVisibility(8);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                TapAgainView.this.setVisibility(8);
            }
        });
        animatorSet.playTogether(ofFloat2, ofFloat);
        animatorSet.start();
    }
}
