package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.android.systemui.R$layout;
/* loaded from: classes2.dex */
public class WidgetContainer extends LinearLayout {
    private int mWidgetCount = 0;

    public WidgetContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public int addWidget(WidgetView widgetView) {
        if (this.mWidgetCount > 0) {
            LayoutInflater.from(getContext()).inflate(R$layout.game_menu_widget_spacing, this);
        }
        super.addView(widgetView);
        int i = this.mWidgetCount + 1;
        this.mWidgetCount = i;
        return i;
    }

    public int getWidgetCount() {
        return this.mWidgetCount;
    }
}
