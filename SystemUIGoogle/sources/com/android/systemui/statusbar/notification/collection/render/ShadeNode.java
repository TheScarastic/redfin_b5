package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ShadeViewDiffer.kt */
/* loaded from: classes.dex */
final class ShadeNode {
    private final NodeController controller;
    private ShadeNode parent;
    private final View view;

    public ShadeNode(NodeController nodeController) {
        Intrinsics.checkNotNullParameter(nodeController, "controller");
        this.controller = nodeController;
        this.view = nodeController.getView();
    }

    public final NodeController getController() {
        return this.controller;
    }

    public final View getView() {
        return this.view;
    }

    public final ShadeNode getParent() {
        return this.parent;
    }

    public final void setParent(ShadeNode shadeNode) {
        this.parent = shadeNode;
    }

    public final String getLabel() {
        return this.controller.getNodeLabel();
    }

    public final View getChildAt(int i) {
        return this.controller.getChildAt(i);
    }

    public final int getChildCount() {
        return this.controller.getChildCount();
    }

    public final void addChildAt(ShadeNode shadeNode, int i) {
        Intrinsics.checkNotNullParameter(shadeNode, "child");
        this.controller.addChildAt(shadeNode.controller, i);
    }

    public final void moveChildTo(ShadeNode shadeNode, int i) {
        Intrinsics.checkNotNullParameter(shadeNode, "child");
        this.controller.moveChildTo(shadeNode.controller, i);
    }

    public final void removeChild(ShadeNode shadeNode, boolean z) {
        Intrinsics.checkNotNullParameter(shadeNode, "child");
        this.controller.removeChild(shadeNode.controller, z);
    }
}
