package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
/* loaded from: classes.dex */
public class ButtonLinearLayout extends LinearLayout {
    public ButtonLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.widget.LinearLayout, android.view.View, android.view.ViewGroup
    public CharSequence getAccessibilityClassName() {
        return Button.class.getName();
    }
}
