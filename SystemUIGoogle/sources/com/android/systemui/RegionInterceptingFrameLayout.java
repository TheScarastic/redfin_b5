package com.android.systemui;

import android.content.Context;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
public class RegionInterceptingFrameLayout extends FrameLayout {
    private final ViewTreeObserver.OnComputeInternalInsetsListener mInsetsListener = new RegionInterceptingFrameLayout$$ExternalSyntheticLambda0(this);

    /* loaded from: classes.dex */
    public interface RegionInterceptableView {
        Region getInterceptRegion();

        default boolean shouldInterceptTouch() {
            return false;
        }
    }

    public RegionInterceptingFrameLayout(Context context) {
        super(context);
    }

    public RegionInterceptingFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RegionInterceptingFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public RegionInterceptingFrameLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnComputeInternalInsetsListener(this.mInsetsListener);
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mInsetsListener);
    }

    public /* synthetic */ void lambda$new$0(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        Region interceptRegion;
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.setEmpty();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof RegionInterceptableView) {
                RegionInterceptableView regionInterceptableView = (RegionInterceptableView) childAt;
                if (regionInterceptableView.shouldInterceptTouch() && (interceptRegion = regionInterceptableView.getInterceptRegion()) != null) {
                    internalInsetsInfo.touchableRegion.op(interceptRegion, Region.Op.UNION);
                }
            }
        }
    }
}
