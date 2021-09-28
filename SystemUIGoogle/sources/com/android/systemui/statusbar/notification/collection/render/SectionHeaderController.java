package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.statusbar.notification.stack.SectionHeaderView;
/* compiled from: SectionHeaderController.kt */
/* loaded from: classes.dex */
public interface SectionHeaderController {
    SectionHeaderView getHeaderView();

    void reinflateView(ViewGroup viewGroup);

    void setOnClearAllClickListener(View.OnClickListener onClickListener);
}
