package com.android.settingslib.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.LinearLayout;
/* loaded from: classes.dex */
public class BannerMessageView extends LinearLayout {
    private Rect mTouchTargetForDismissButton;

    public BannerMessageView(Context context) {
        super(context);
    }

    public BannerMessageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BannerMessageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public BannerMessageView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // android.widget.LinearLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setupIncreaseTouchTargetForDismissButton();
    }

    private void setupIncreaseTouchTargetForDismissButton() {
        if (this.mTouchTargetForDismissButton == null) {
            View findViewById = findViewById(R$id.top_row);
            View findViewById2 = findViewById(R$id.banner_dismiss_btn);
            if (findViewById != null && findViewById2 != null && findViewById2.getVisibility() == 0) {
                int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.settingslib_preferred_minimum_touch_target);
                int width = findViewById2.getWidth();
                int height = findViewById2.getHeight();
                int i = 0;
                int i2 = width < dimensionPixelSize ? dimensionPixelSize - width : 0;
                if (height < dimensionPixelSize) {
                    i = dimensionPixelSize - height;
                }
                Rect rect = new Rect();
                findViewById2.getHitRect(rect);
                Rect rect2 = new Rect();
                findViewById.getHitRect(rect2);
                Rect rect3 = new Rect();
                this.mTouchTargetForDismissButton = rect3;
                int i3 = rect2.left + rect.left;
                rect3.left = i3;
                int i4 = rect2.left + rect.right;
                rect3.right = i4;
                int i5 = rect2.top + rect.top;
                rect3.top = i5;
                int i6 = rect2.top + rect.bottom;
                rect3.bottom = i6;
                rect3.left = i3 - (i2 % 2 == 1 ? (i2 / 2) + 1 : i2 / 2);
                rect3.top = i5 - (i % 2 == 1 ? (i / 2) + 1 : i / 2);
                rect3.right = i4 + (i2 / 2);
                rect3.bottom = i6 + (i / 2);
                setTouchDelegate(new TouchDelegate(this.mTouchTargetForDismissButton, findViewById2));
            }
        }
    }
}
