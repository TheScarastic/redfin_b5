package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ShadeViewDiffer.kt */
/* loaded from: classes.dex */
public final class ShadeViewDiffer {
    private final ShadeViewDifferLogger logger;
    private final Map<NodeController, ShadeNode> nodes;
    private final ShadeNode rootNode;
    private final Map<View, ShadeNode> views = new LinkedHashMap();

    public ShadeViewDiffer(NodeController nodeController, ShadeViewDifferLogger shadeViewDifferLogger) {
        Intrinsics.checkNotNullParameter(nodeController, "rootController");
        Intrinsics.checkNotNullParameter(shadeViewDifferLogger, "logger");
        this.logger = shadeViewDifferLogger;
        ShadeNode shadeNode = new ShadeNode(nodeController);
        this.rootNode = shadeNode;
        this.nodes = MapsKt__MapsKt.mutableMapOf(TuplesKt.to(nodeController, shadeNode));
    }

    public final void applySpec(NodeSpec nodeSpec) {
        Intrinsics.checkNotNullParameter(nodeSpec, "spec");
        Map<NodeController, NodeSpec> treeToMap = treeToMap(nodeSpec);
        if (Intrinsics.areEqual(nodeSpec.getController(), this.rootNode.getController())) {
            detachChildren(this.rootNode, treeToMap);
            attachChildren(this.rootNode, treeToMap);
            return;
        }
        throw new IllegalArgumentException("Tree root " + nodeSpec.getController().getNodeLabel() + " does not match own root at " + this.rootNode.getLabel());
    }

    private final void detachChildren(ShadeNode shadeNode, Map<NodeController, ? extends NodeSpec> map) {
        NodeSpec nodeSpec = (NodeSpec) map.get(shadeNode.getController());
        int childCount = shadeNode.getChildCount() - 1;
        if (childCount >= 0) {
            while (true) {
                int i = childCount - 1;
                ShadeNode shadeNode2 = this.views.get(shadeNode.getChildAt(childCount));
                if (shadeNode2 != null) {
                    maybeDetachChild(shadeNode, nodeSpec, shadeNode2, (NodeSpec) map.get(shadeNode2.getController()));
                    if (shadeNode2.getController().getChildCount() > 0) {
                        detachChildren(shadeNode2, map);
                    }
                }
                if (i >= 0) {
                    childCount = i;
                } else {
                    return;
                }
            }
        }
    }

    private final void maybeDetachChild(ShadeNode shadeNode, NodeSpec nodeSpec, ShadeNode shadeNode2, NodeSpec nodeSpec2) {
        String str;
        NodeSpec parent = nodeSpec2 == null ? null : nodeSpec2.getParent();
        ShadeNode node = parent == null ? null : getNode(parent);
        if (!Intrinsics.areEqual(node, shadeNode)) {
            boolean z = true;
            if (node != null) {
                z = false;
            }
            if (z) {
                this.nodes.remove(shadeNode2.getController());
                this.views.remove(shadeNode2.getController().getView());
            }
            if (!z || nodeSpec != null) {
                ShadeViewDifferLogger shadeViewDifferLogger = this.logger;
                String label = shadeNode2.getLabel();
                boolean z2 = !z;
                String label2 = shadeNode.getLabel();
                if (node == null) {
                    str = null;
                } else {
                    str = node.getLabel();
                }
                shadeViewDifferLogger.logDetachingChild(label, z2, label2, str);
                shadeNode.removeChild(shadeNode2, !z);
                shadeNode2.setParent(null);
                return;
            }
            this.logger.logSkippingDetach(shadeNode2.getLabel(), shadeNode.getLabel());
        }
    }

    private final void attachChildren(ShadeNode shadeNode, Map<NodeController, ? extends NodeSpec> map) {
        NodeSpec nodeSpec = (NodeSpec) map.get(shadeNode.getController());
        if (nodeSpec != null) {
            int i = 0;
            for (NodeSpec nodeSpec2 : nodeSpec.getChildren()) {
                int i2 = i + 1;
                View childAt = shadeNode.getChildAt(i);
                ShadeNode node = getNode(nodeSpec2);
                if (!Intrinsics.areEqual(node.getView(), childAt)) {
                    ShadeNode parent = node.getParent();
                    if (parent == null) {
                        this.logger.logAttachingChild(node.getLabel(), shadeNode.getLabel());
                        shadeNode.addChildAt(node, i);
                        node.setParent(shadeNode);
                    } else if (Intrinsics.areEqual(parent, shadeNode)) {
                        this.logger.logMovingChild(node.getLabel(), shadeNode.getLabel(), i);
                        shadeNode.moveChildTo(node, i);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Child ");
                        sb.append(node.getLabel());
                        sb.append(" should have parent ");
                        sb.append(shadeNode.getLabel());
                        sb.append(" but is actually ");
                        ShadeNode parent2 = node.getParent();
                        sb.append((Object) (parent2 == null ? null : parent2.getLabel()));
                        throw new IllegalStateException(sb.toString());
                    }
                }
                if (!nodeSpec2.getChildren().isEmpty()) {
                    attachChildren(node, map);
                }
                i = i2;
            }
            return;
        }
        throw new IllegalStateException("Required value was null.".toString());
    }

    private final ShadeNode getNode(NodeSpec nodeSpec) {
        ShadeNode shadeNode = this.nodes.get(nodeSpec.getController());
        if (shadeNode != null) {
            return shadeNode;
        }
        ShadeNode shadeNode2 = new ShadeNode(nodeSpec.getController());
        this.nodes.put(shadeNode2.getController(), shadeNode2);
        this.views.put(shadeNode2.getView(), shadeNode2);
        return shadeNode2;
    }

    private final Map<NodeController, NodeSpec> treeToMap(NodeSpec nodeSpec) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        try {
            registerNodes(nodeSpec, linkedHashMap);
            return linkedHashMap;
        } catch (DuplicateNodeException e) {
            this.logger.logDuplicateNodeInTree(nodeSpec, e);
            throw e;
        }
    }

    private final void registerNodes(NodeSpec nodeSpec, Map<NodeController, NodeSpec> map) {
        if (!map.containsKey(nodeSpec.getController())) {
            map.put(nodeSpec.getController(), nodeSpec);
            if (!nodeSpec.getChildren().isEmpty()) {
                for (NodeSpec nodeSpec2 : nodeSpec.getChildren()) {
                    registerNodes(nodeSpec2, map);
                }
                return;
            }
            return;
        }
        throw new DuplicateNodeException("Node " + nodeSpec.getController().getNodeLabel() + " appears more than once");
    }
}
