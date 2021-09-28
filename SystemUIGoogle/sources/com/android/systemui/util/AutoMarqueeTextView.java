package com.android.systemui.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
/* loaded from: classes2.dex */
public class AutoMarqueeTextView extends TextView {
    private boolean mAggregatedVisible = false;

    public AutoMarqueeTextView(Context context) {
        super(context);
    }

    public AutoMarqueeTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AutoMarqueeTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public AutoMarqueeTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        onVisibilityAggregated(isVisibleToUser());
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setSelected(true);
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setSelected(false);
    }

    @Override // android.view.View
    public void onVisibilityAggregated(boolean z) {
        super.onVisibilityAggregated(z);
        if (z != this.mAggregatedVisible) {
            this.mAggregatedVisible = z;
            if (z) {
                setEllipsize(TextUtils.TruncateAt.MARQUEE);
            } else {
                setEllipsize(TextUtils.TruncateAt.END);
            }
        }
    }
}
