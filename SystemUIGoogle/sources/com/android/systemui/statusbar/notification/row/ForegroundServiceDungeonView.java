package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.android.systemui.R$id;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ForegroundServiceDungeonView.kt */
/* loaded from: classes.dex */
public final class ForegroundServiceDungeonView extends StackScrollerDecorView {
    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    protected View findSecondaryView() {
        return null;
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    public void setVisible(boolean z, boolean z2) {
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ForegroundServiceDungeonView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    protected View findContentView() {
        return findViewById(R$id.foreground_service_dungeon);
    }
}
