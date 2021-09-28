package com.android.systemui.controls.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$id;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsUiControllerImpl.kt */
/* loaded from: classes.dex */
final class ItemAdapter extends ArrayAdapter<SelectionItem> {
    private final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
    private final Context parentContext;
    private final int resource;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ItemAdapter(Context context, int i) {
        super(context, i);
        Intrinsics.checkNotNullParameter(context, "parentContext");
        this.parentContext = context;
        this.resource = i;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        SelectionItem item = getItem(i);
        if (view == null) {
            view = this.layoutInflater.inflate(this.resource, viewGroup, false);
        }
        ((TextView) view.requireViewById(R$id.controls_spinner_item)).setText(item.getTitle());
        ((ImageView) view.requireViewById(R$id.app_icon)).setImageDrawable(item.getIcon());
        Intrinsics.checkNotNullExpressionValue(view, "view");
        return view;
    }
}
