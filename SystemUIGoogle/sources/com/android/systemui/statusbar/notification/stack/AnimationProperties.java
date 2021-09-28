package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.ArrayMap;
import android.util.Property;
import android.view.View;
import android.view.animation.Interpolator;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class AnimationProperties {
    public long delay;
    public long duration;
    private Consumer<Property> mAnimationEndAction;
    private ArrayMap<Property, Interpolator> mInterpolatorMap;

    public boolean wasAdded(View view) {
        return false;
    }

    public AnimationFilter getAnimationFilter() {
        return new AnimationFilter() { // from class: com.android.systemui.statusbar.notification.stack.AnimationProperties.1
            @Override // com.android.systemui.statusbar.notification.stack.AnimationFilter
            public boolean shouldAnimateProperty(Property property) {
                return true;
            }
        };
    }

    public AnimatorListenerAdapter getAnimationFinishListener(final Property property) {
        final Consumer<Property> consumer = this.mAnimationEndAction;
        if (consumer == null) {
            return null;
        }
        return new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.stack.AnimationProperties.2
            private boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (!this.mCancelled) {
                    consumer.accept(property);
                }
            }
        };
    }

    public AnimationProperties setAnimationEndAction(Consumer<Property> consumer) {
        this.mAnimationEndAction = consumer;
        return this;
    }

    public Interpolator getCustomInterpolator(View view, Property property) {
        ArrayMap<Property, Interpolator> arrayMap = this.mInterpolatorMap;
        if (arrayMap != null) {
            return arrayMap.get(property);
        }
        return null;
    }

    public void combineCustomInterpolators(AnimationProperties animationProperties) {
        ArrayMap<Property, Interpolator> arrayMap = animationProperties.mInterpolatorMap;
        if (arrayMap != null) {
            if (this.mInterpolatorMap == null) {
                this.mInterpolatorMap = new ArrayMap<>();
            }
            this.mInterpolatorMap.putAll((ArrayMap<? extends Property, ? extends Interpolator>) arrayMap);
        }
    }

    public AnimationProperties setCustomInterpolator(Property property, Interpolator interpolator) {
        if (this.mInterpolatorMap == null) {
            this.mInterpolatorMap = new ArrayMap<>();
        }
        this.mInterpolatorMap.put(property, interpolator);
        return this;
    }

    public AnimationProperties setDuration(long j) {
        this.duration = j;
        return this;
    }

    public AnimationProperties setDelay(long j) {
        this.delay = j;
        return this;
    }

    public AnimationProperties resetCustomInterpolators() {
        this.mInterpolatorMap = null;
        return this;
    }
}
