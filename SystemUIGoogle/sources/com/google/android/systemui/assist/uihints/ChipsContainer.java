package com.google.android.systemui.assist.uihints;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.R$layout;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class ChipsContainer extends LinearLayout implements TranscriptionController.TranscriptionSpaceView {
    private final int CHIP_MARGIN;
    private final int START_DELTA;
    private ValueAnimator mAnimator;
    private int mAvailableWidth;
    private List<ChipView> mChipViews;
    private List<Bundle> mChips;
    private boolean mDarkBackground;

    public ChipsContainer(Context context) {
        this(context, null);
    }

    public ChipsContainer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ChipsContainer(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ChipsContainer(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mChips = new ArrayList();
        this.mChipViews = new ArrayList();
        this.mAnimator = new ValueAnimator();
        this.CHIP_MARGIN = (int) getResources().getDimension(R$dimen.assist_chip_horizontal_margin);
        this.START_DELTA = (int) getResources().getDimension(R$dimen.assist_greeting_start_delta);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int rotatedWidth = DisplayUtils.getRotatedWidth(getContext());
        if (rotatedWidth != this.mAvailableWidth) {
            this.mAvailableWidth = rotatedWidth;
            setChipsInternal();
        }
        super.onMeasure(i, i2);
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public ListenableFuture<Void> hide(boolean z) {
        if (this.mAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        removeAllViews();
        setVisibility(8);
        setTranslationY(0.0f);
        return Futures.immediateFuture(null);
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public void setHasDarkBackground(boolean z) {
        if (this.mDarkBackground != z) {
            this.mDarkBackground = z;
            for (int i = 0; i < getChildCount(); i++) {
                ((ChipView) getChildAt(i)).setHasDarkBackground(z);
            }
        }
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public void onFontSizeChanged() {
        float dimension = ((LinearLayout) this).mContext.getResources().getDimension(R$dimen.assist_chip_text_size);
        for (ChipView chipView : this.mChipViews) {
            chipView.updateTextSize(dimension);
        }
        requestLayout();
    }

    /* access modifiers changed from: package-private */
    public void setChips(List<Bundle> list) {
        this.mChips = list;
        setChipsInternal();
        setVisibility(0);
    }

    /* access modifiers changed from: package-private */
    public void setChipsAnimatedBounce(List<Bundle> list, float f) {
        this.mChips = list;
        setChipsInternal();
        bounceAnimate(f);
    }

    /* access modifiers changed from: package-private */
    public void setChipsAnimatedZoom(List<Bundle> list) {
        this.mChips = list;
        setChipsInternal();
        zoomAnimate();
    }

    private void setChipsInternal() {
        ChipView chipView;
        int i = this.mAvailableWidth;
        int i2 = 0;
        for (Bundle bundle : this.mChips) {
            if (i2 < this.mChipViews.size()) {
                chipView = this.mChipViews.get(i2);
            } else {
                chipView = (ChipView) LayoutInflater.from(getContext()).inflate(R$layout.assist_chip, (ViewGroup) this, false);
                this.mChipViews.add(chipView);
            }
            if (chipView.setChip(bundle)) {
                chipView.setHasDarkBackground(this.mDarkBackground);
                chipView.measure(0, 0);
                int measuredWidth = chipView.getMeasuredWidth() + (this.CHIP_MARGIN * 2);
                if (measuredWidth < i) {
                    if (chipView.getParent() == null) {
                        chipView.setVisibility(0);
                        addView(chipView);
                    }
                    i -= measuredWidth;
                    i2++;
                }
            }
        }
        if (i2 < this.mChipViews.size()) {
            while (i2 < this.mChipViews.size()) {
                this.mChipViews.get(i2).setVisibility(8);
                i2++;
            }
        }
        requestLayout();
    }

    private void bounceAnimate(float f) {
        if (this.mAnimator.isRunning()) {
            Log.w("ChipsContainer", "Already animating in chips view; ignoring");
            return;
        }
        this.mAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mAnimator.setInterpolator(new OvershootInterpolator(Math.min(10.0f, (f / 1.2f) + 3.0f)));
        this.mAnimator.setDuration(400L);
        this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.systemui.assist.uihints.ChipsContainer$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ChipsContainer.$r8$lambda$U7UhXfXjdVrk2B6JqT4r9b8GbgM(ChipsContainer.this, valueAnimator);
            }
        });
        setVisibility(0);
        this.mAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bounceAnimate$0(ValueAnimator valueAnimator) {
        setTranslationY(((float) this.START_DELTA) * (1.0f - ((Float) valueAnimator.getAnimatedValue()).floatValue()));
    }

    private void zoomAnimate() {
        if (this.mAnimator.isRunning()) {
            Log.w("ChipsContainer", "Already animating in chips view; ignoring");
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ObjectAnimator.ofFloat(this, View.SCALE_X, 0.8f, 1.0f)).with(ObjectAnimator.ofFloat(this, View.SCALE_Y, 0.8f, 1.0f)).with(ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f, 1.0f));
        animatorSet.setDuration(200L);
        setVisibility(0);
        animatorSet.start();
    }
}
