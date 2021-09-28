package com.android.systemui.statusbar.notification.collection.render;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: NodeController.kt */
/* loaded from: classes.dex */
public final class NodeControllerKt {
    public static final String treeSpecToStr(NodeSpec nodeSpec) {
        Intrinsics.checkNotNullParameter(nodeSpec, "tree");
        StringBuilder sb = new StringBuilder();
        treeSpecToStrHelper(nodeSpec, sb, "");
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "StringBuilder().also { treeSpecToStrHelper(tree, it, \"\") }.toString()");
        return sb2;
    }

    private static final void treeSpecToStrHelper(NodeSpec nodeSpec, StringBuilder sb, String str) {
        sb.append(str + "ns{" + nodeSpec.getController().getNodeLabel());
        if (!nodeSpec.getChildren().isEmpty()) {
            String stringPlus = Intrinsics.stringPlus(str, "  ");
            for (NodeSpec nodeSpec2 : nodeSpec.getChildren()) {
                treeSpecToStrHelper(nodeSpec2, sb, stringPlus);
            }
        }
    }
}
