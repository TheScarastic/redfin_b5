package com.android.systemui.statusbar.notification.stack;

import android.util.Property;
import android.view.View;
import androidx.collection.ArraySet;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class AnimationFilter {
    boolean animateAlpha;
    boolean animateDimmed;
    boolean animateHeight;
    boolean animateHideSensitive;
    boolean animateTopInset;
    boolean animateX;
    boolean animateY;
    boolean animateZ;
    long customDelay;
    boolean hasDelays;
    boolean hasGoToFullShadeEvent;
    ArraySet<View> animateYViews = new ArraySet<>();
    private ArraySet<Property> mAnimatedProperties = new ArraySet<>();

    public AnimationFilter animateAlpha() {
        this.animateAlpha = true;
        return this;
    }

    public AnimationFilter animateScale() {
        animate(View.SCALE_X);
        animate(View.SCALE_Y);
        return this;
    }

    public AnimationFilter animateX() {
        this.animateX = true;
        return this;
    }

    public AnimationFilter animateY() {
        this.animateY = true;
        return this;
    }

    public AnimationFilter hasDelays() {
        this.hasDelays = true;
        return this;
    }

    public AnimationFilter animateZ() {
        this.animateZ = true;
        return this;
    }

    public AnimationFilter animateHeight() {
        this.animateHeight = true;
        return this;
    }

    public AnimationFilter animateTopInset() {
        this.animateTopInset = true;
        return this;
    }

    public AnimationFilter animateDimmed() {
        this.animateDimmed = true;
        return this;
    }

    public AnimationFilter animateHideSensitive() {
        this.animateHideSensitive = true;
        return this;
    }

    public boolean shouldAnimateY(View view) {
        return this.animateY || this.animateYViews.contains(view);
    }

    public void applyCombination(ArrayList<NotificationStackScrollLayout.AnimationEvent> arrayList) {
        reset();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            combineFilter(arrayList.get(i).filter);
            if (arrayList.get(i).animationType == 7) {
                this.hasGoToFullShadeEvent = true;
            }
        }
    }

    public void combineFilter(AnimationFilter animationFilter) {
        this.animateAlpha |= animationFilter.animateAlpha;
        this.animateX |= animationFilter.animateX;
        this.animateY |= animationFilter.animateY;
        this.animateYViews.addAll((ArraySet<? extends View>) animationFilter.animateYViews);
        this.animateZ |= animationFilter.animateZ;
        this.animateHeight |= animationFilter.animateHeight;
        this.animateTopInset |= animationFilter.animateTopInset;
        this.animateDimmed |= animationFilter.animateDimmed;
        this.animateHideSensitive |= animationFilter.animateHideSensitive;
        this.hasDelays |= animationFilter.hasDelays;
        this.mAnimatedProperties.addAll((ArraySet<? extends Property>) animationFilter.mAnimatedProperties);
    }

    public void reset() {
        this.animateAlpha = false;
        this.animateX = false;
        this.animateY = false;
        this.animateYViews.clear();
        this.animateZ = false;
        this.animateHeight = false;
        this.animateTopInset = false;
        this.animateDimmed = false;
        this.animateHideSensitive = false;
        this.hasDelays = false;
        this.hasGoToFullShadeEvent = false;
        this.customDelay = -1;
        this.mAnimatedProperties.clear();
    }

    public AnimationFilter animate(Property property) {
        this.mAnimatedProperties.add(property);
        return this;
    }

    public boolean shouldAnimateProperty(Property property) {
        return this.mAnimatedProperties.contains(property);
    }
}
