package com.android.systemui.statusbar.notification.collection.render;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.stack.SectionHeaderView;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SectionHeaderController.kt */
/* loaded from: classes.dex */
public final class SectionHeaderNodeControllerImpl implements NodeController, SectionHeaderController {
    private SectionHeaderView _view;
    private final ActivityStarter activityStarter;
    private View.OnClickListener clearAllClickListener;
    private final String clickIntentAction;
    private final int headerTextResId;
    private final LayoutInflater layoutInflater;
    private final String nodeLabel;
    private final View.OnClickListener onHeaderClickListener = new View.OnClickListener(this) { // from class: com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl$onHeaderClickListener$1
        final /* synthetic */ SectionHeaderNodeControllerImpl this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            this.this$0.activityStarter.startActivity(new Intent(this.this$0.clickIntentAction), true, true, 536870912);
        }
    };

    public SectionHeaderNodeControllerImpl(String str, LayoutInflater layoutInflater, int i, ActivityStarter activityStarter, String str2) {
        Intrinsics.checkNotNullParameter(str, "nodeLabel");
        Intrinsics.checkNotNullParameter(layoutInflater, "layoutInflater");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(str2, "clickIntentAction");
        this.nodeLabel = str;
        this.layoutInflater = layoutInflater;
        this.headerTextResId = i;
        this.activityStarter = activityStarter;
        this.clickIntentAction = str2;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void addChildAt(NodeController nodeController, int i) {
        NodeController.DefaultImpls.addChildAt(this, nodeController, i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public View getChildAt(int i) {
        return NodeController.DefaultImpls.getChildAt(this, i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public int getChildCount() {
        return NodeController.DefaultImpls.getChildCount(this);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void moveChildTo(NodeController nodeController, int i) {
        NodeController.DefaultImpls.moveChildTo(this, nodeController, i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void removeChild(NodeController nodeController, boolean z) {
        NodeController.DefaultImpls.removeChild(this, nodeController, z);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public String getNodeLabel() {
        return this.nodeLabel;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0047  */
    @Override // com.android.systemui.statusbar.notification.collection.render.SectionHeaderController
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void reinflateView(android.view.ViewGroup r6) {
        /*
            r5 = this;
            java.lang.String r0 = "parent"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r0 = r5._view
            r1 = -1
            if (r0 != 0) goto L_0x000c
        L_0x000a:
            r2 = r1
            goto L_0x0023
        L_0x000c:
            android.view.ViewGroup r2 = r0.getTransientContainer()
            if (r2 != 0) goto L_0x0013
            goto L_0x0016
        L_0x0013:
            r2.removeView(r0)
        L_0x0016:
            android.view.ViewParent r2 = r0.getParent()
            if (r2 != r6) goto L_0x000a
            int r2 = r6.indexOfChild(r0)
            r6.removeView(r0)
        L_0x0023:
            android.view.LayoutInflater r0 = r5.layoutInflater
            int r3 = com.android.systemui.R$layout.status_bar_notification_section_header
            r4 = 0
            android.view.View r0 = r0.inflate(r3, r6, r4)
            java.lang.String r3 = "null cannot be cast to non-null type com.android.systemui.statusbar.notification.stack.SectionHeaderView"
            java.util.Objects.requireNonNull(r0, r3)
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r0 = (com.android.systemui.statusbar.notification.stack.SectionHeaderView) r0
            int r3 = r5.headerTextResId
            r0.setHeaderText(r3)
            android.view.View$OnClickListener r3 = r5.onHeaderClickListener
            r0.setOnHeaderClickListener(r3)
            android.view.View$OnClickListener r3 = r5.clearAllClickListener
            if (r3 != 0) goto L_0x0042
            goto L_0x0045
        L_0x0042:
            r0.setOnClearAllClickListener(r3)
        L_0x0045:
            if (r2 == r1) goto L_0x004a
            r6.addView(r0, r2)
        L_0x004a:
            r5._view = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl.reinflateView(android.view.ViewGroup):void");
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.SectionHeaderController
    public SectionHeaderView getHeaderView() {
        return this._view;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.SectionHeaderController
    public void setOnClearAllClickListener(View.OnClickListener onClickListener) {
        Intrinsics.checkNotNullParameter(onClickListener, "listener");
        this.clearAllClickListener = onClickListener;
        SectionHeaderView sectionHeaderView = this._view;
        if (sectionHeaderView != null) {
            sectionHeaderView.setOnClearAllClickListener(onClickListener);
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public View getView() {
        SectionHeaderView sectionHeaderView = this._view;
        Intrinsics.checkNotNull(sectionHeaderView);
        return sectionHeaderView;
    }
}
