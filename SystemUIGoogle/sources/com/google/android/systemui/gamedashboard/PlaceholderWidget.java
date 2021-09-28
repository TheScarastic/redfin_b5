package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.systemui.R$layout;
/* loaded from: classes2.dex */
public class PlaceholderWidget {
    private final Context mContext;
    private final WidgetView mWidgetView;

    public static PlaceholderWidget create(Context context, ViewGroup viewGroup) {
        return new PlaceholderWidget(context, (WidgetView) LayoutInflater.from(context).inflate(R$layout.game_menu_widget, viewGroup, false));
    }

    public PlaceholderWidget(Context context, WidgetView widgetView) {
        this.mContext = context;
        this.mWidgetView = widgetView;
        widgetView.setVisibility(4);
    }

    public WidgetView getView() {
        return this.mWidgetView;
    }
}
