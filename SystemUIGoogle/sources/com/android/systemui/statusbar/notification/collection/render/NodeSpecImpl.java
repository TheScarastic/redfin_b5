package com.android.systemui.statusbar.notification.collection.render;

import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NodeController.kt */
/* loaded from: classes.dex */
public final class NodeSpecImpl implements NodeSpec {
    private final List<NodeSpec> children = new ArrayList();
    private final NodeController controller;
    private final NodeSpec parent;

    public NodeSpecImpl(NodeSpec nodeSpec, NodeController nodeController) {
        Intrinsics.checkNotNullParameter(nodeController, "controller");
        this.parent = nodeSpec;
        this.controller = nodeController;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeSpec
    public NodeSpec getParent() {
        return this.parent;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeSpec
    public NodeController getController() {
        return this.controller;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeSpec
    public List<NodeSpec> getChildren() {
        return this.children;
    }
}
