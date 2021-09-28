package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: RootNodeController.kt */
/* loaded from: classes.dex */
public final class RootNodeController implements NodeController {
    private final NotificationListContainer listContainer;
    private final String nodeLabel = "<root>";
    private final View view;

    public RootNodeController(NotificationListContainer notificationListContainer, View view) {
        Intrinsics.checkNotNullParameter(notificationListContainer, "listContainer");
        Intrinsics.checkNotNullParameter(view, "view");
        this.listContainer = notificationListContainer;
        this.view = view;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public View getView() {
        return this.view;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public String getNodeLabel() {
        return this.nodeLabel;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public View getChildAt(int i) {
        return this.listContainer.getContainerChildAt(i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public int getChildCount() {
        return this.listContainer.getContainerChildCount();
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void addChildAt(NodeController nodeController, int i) {
        Intrinsics.checkNotNullParameter(nodeController, "child");
        this.listContainer.addContainerViewAt(nodeController.getView(), i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void moveChildTo(NodeController nodeController, int i) {
        Intrinsics.checkNotNullParameter(nodeController, "child");
        this.listContainer.changeViewPosition((ExpandableView) nodeController.getView(), i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void removeChild(NodeController nodeController, boolean z) {
        Intrinsics.checkNotNullParameter(nodeController, "child");
        if (z) {
            this.listContainer.setChildTransferInProgress(true);
        }
        this.listContainer.removeContainerView(nodeController.getView());
        if (z) {
            this.listContainer.setChildTransferInProgress(false);
        }
    }
}
