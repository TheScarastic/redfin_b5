package com.android.systemui.globalactions;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
/* loaded from: classes.dex */
public class MinHeightScrollView extends ScrollView {
    public MinHeightScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        View childAt = getChildAt(0);
        if (childAt != null) {
            childAt.setMinimumHeight(View.MeasureSpec.getSize(i2));
        }
        super.onMeasure(i, i2);
    }
}
