package com.android.systemui.statusbar.notification.collection.render;

import java.util.List;
/* compiled from: NodeController.kt */
/* loaded from: classes.dex */
public interface NodeSpec {
    List<NodeSpec> getChildren();

    NodeController getController();

    NodeSpec getParent();
}
