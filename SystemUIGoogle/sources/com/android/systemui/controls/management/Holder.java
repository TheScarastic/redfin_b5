package com.android.systemui.controls.management;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public abstract class Holder extends RecyclerView.ViewHolder {
    public /* synthetic */ Holder(View view, DefaultConstructorMarker defaultConstructorMarker) {
        this(view);
    }

    public abstract void bindData(ElementWrapper elementWrapper);

    public void updateFavorite(boolean z) {
    }

    private Holder(View view) {
        super(view);
    }
}
