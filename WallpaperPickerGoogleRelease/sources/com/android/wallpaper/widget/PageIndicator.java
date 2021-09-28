package com.android.wallpaper.widget;

import android.content.Context;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.systemui.shared.R;
import com.google.common.math.IntMath;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class PageIndicator extends ViewGroup {
    public static Method sMethodForceAnimationOnUI;
    public boolean mAnimating;
    public final int mPageDotWidth;
    public final int mPageIndicatorHeight;
    public final int mPageIndicatorWidth;
    public final ArrayList<Integer> mQueuedPositions = new ArrayList<>();
    public int mPosition = -1;
    public final Animatable2.AnimationCallback mAnimationCallback = new Animatable2.AnimationCallback() { // from class: com.android.wallpaper.widget.PageIndicator.1
        @Override // android.graphics.drawable.Animatable2.AnimationCallback
        public void onAnimationEnd(Drawable drawable) {
            super.onAnimationEnd(drawable);
            if (drawable instanceof AnimatedVectorDrawable) {
                ((AnimatedVectorDrawable) drawable).unregisterAnimationCallback(PageIndicator.this.mAnimationCallback);
            }
            PageIndicator pageIndicator = PageIndicator.this;
            pageIndicator.mAnimating = false;
            if (pageIndicator.mQueuedPositions.size() != 0) {
                PageIndicator pageIndicator2 = PageIndicator.this;
                pageIndicator2.setPosition(pageIndicator2.mQueuedPositions.remove(0).intValue());
            }
        }
    };

    public PageIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int dimension = (int) context.getResources().getDimension(R.dimen.preview_indicator_width);
        this.mPageIndicatorWidth = dimension;
        this.mPageIndicatorHeight = (int) context.getResources().getDimension(R.dimen.preview_indicator_height);
        this.mPageDotWidth = (int) (((float) dimension) * 0.4f);
    }

    public final void forceAnimationOnUI(AnimatedVectorDrawable animatedVectorDrawable) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (sMethodForceAnimationOnUI == null) {
            sMethodForceAnimationOnUI = AnimatedVectorDrawable.class.getMethod("forceAnimationOnUI", new Class[0]);
        }
        Method method = sMethodForceAnimationOnUI;
        if (method != null) {
            method.invoke(animatedVectorDrawable, new Object[0]);
        }
    }

    public final float getAlpha(boolean z) {
        return z ? 1.0f : 0.42f;
    }

    public final int getTransition(boolean z, boolean z2, boolean z3) {
        return z3 ? z ? z2 ? R.drawable.major_b_a_animation : R.drawable.major_b_c_animation : z2 ? R.drawable.major_a_b_animation : R.drawable.major_c_b_animation : z ? z2 ? R.drawable.minor_b_c_animation : R.drawable.minor_b_a_animation : z2 ? R.drawable.minor_c_b_animation : R.drawable.minor_a_b_animation;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        if (childCount != 0) {
            for (int i5 = 0; i5 < childCount; i5++) {
                int i6 = (this.mPageIndicatorWidth - this.mPageDotWidth) * i5;
                getChildAt(i5).layout(i6, 0, this.mPageIndicatorWidth + i6, this.mPageIndicatorHeight);
            }
        }
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int childCount = getChildCount();
        if (childCount == 0) {
            super.onMeasure(i, i2);
            return;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.mPageIndicatorWidth, IntMath.MAX_SIGNED_POWER_OF_TWO);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(this.mPageIndicatorHeight, IntMath.MAX_SIGNED_POWER_OF_TWO);
        for (int i3 = 0; i3 < childCount; i3++) {
            getChildAt(i3).measure(makeMeasureSpec, makeMeasureSpec2);
        }
        int i4 = this.mPageIndicatorWidth;
        int i5 = this.mPageDotWidth;
        setMeasuredDimension(((childCount - 1) * (i4 - i5)) + i5, this.mPageIndicatorHeight);
    }

    public final void playAnimation(ImageView imageView, int i) {
        Drawable drawable = getContext().getDrawable(i);
        if (drawable instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
            imageView.setImageDrawable(animatedVectorDrawable);
            try {
                forceAnimationOnUI(animatedVectorDrawable);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                Log.e("PageIndicator", "Catch an exception in playAnimation", e);
            }
            animatedVectorDrawable.registerAnimationCallback(this.mAnimationCallback);
            animatedVectorDrawable.start();
        }
    }

    public final void setIndex(int i) {
        int childCount = getChildCount();
        int i2 = 0;
        while (i2 < childCount) {
            ImageView imageView = (ImageView) getChildAt(i2);
            imageView.setTranslationX(0.0f);
            imageView.setImageResource(R.drawable.major_a_b);
            imageView.setAlpha(getAlpha(i2 == i));
            i2++;
        }
    }

    public void setLocation(float f) {
        int i = (int) f;
        int i2 = 0;
        setContentDescription(getContext().getString(R.string.accessibility_preview_pager, Integer.valueOf(i + 1), Integer.valueOf(getChildCount())));
        int i3 = i << 1;
        if (f != ((float) i)) {
            i2 = 1;
        }
        int i4 = i3 | i2;
        int i5 = this.mPosition;
        if (this.mQueuedPositions.size() != 0) {
            ArrayList<Integer> arrayList = this.mQueuedPositions;
            i5 = arrayList.get(arrayList.size() - 1).intValue();
        }
        if (i4 != i5) {
            if (this.mAnimating) {
                this.mQueuedPositions.add(Integer.valueOf(i4));
            } else {
                setPosition(i4);
            }
        }
    }

    public final void setPosition(int i) {
        int i2 = this.mPosition;
        if (i2 < 0 || Math.abs(i2 - i) != 1) {
            setIndex(i >> 1);
        } else {
            int i3 = this.mPosition;
            int i4 = i3 >> 1;
            int i5 = i >> 1;
            setIndex(i4);
            boolean z = (i3 & 1) != 0;
            boolean z2 = !z ? i3 < i : i3 > i;
            int min = Math.min(i4, i5);
            int max = Math.max(i4, i5);
            if (max == min) {
                max++;
            }
            ImageView imageView = (ImageView) getChildAt(min);
            ImageView imageView2 = (ImageView) getChildAt(max);
            if (!(imageView == null || imageView2 == null)) {
                imageView2.setTranslationX(imageView.getX() - imageView2.getX());
                playAnimation(imageView, getTransition(z, z2, false));
                imageView.setAlpha(getAlpha(false));
                playAnimation(imageView2, getTransition(z, z2, true));
                imageView2.setAlpha(getAlpha(true));
                this.mAnimating = true;
            }
        }
        this.mPosition = i;
    }
}
