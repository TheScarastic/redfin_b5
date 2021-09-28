package com.android.systemui.statusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
/* loaded from: classes.dex */
public class AlphaOptimizedTextView extends TextView {
    @Override // android.widget.TextView, android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public AlphaOptimizedTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
