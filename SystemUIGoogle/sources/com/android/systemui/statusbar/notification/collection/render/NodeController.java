package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NodeController.kt */
/* loaded from: classes.dex */
public interface NodeController {
    void addChildAt(NodeController nodeController, int i);

    View getChildAt(int i);

    int getChildCount();

    String getNodeLabel();

    View getView();

    void moveChildTo(NodeController nodeController, int i);

    void removeChild(NodeController nodeController, boolean z);

    /* compiled from: NodeController.kt */
    /* loaded from: classes.dex */
    public static final class DefaultImpls {
        public static int getChildCount(NodeController nodeController) {
            Intrinsics.checkNotNullParameter(nodeController, "this");
            return 0;
        }

        public static View getChildAt(NodeController nodeController, int i) {
            Intrinsics.checkNotNullParameter(nodeController, "this");
            throw new RuntimeException("Not supported");
        }

        public static void addChildAt(NodeController nodeController, NodeController nodeController2, int i) {
            Intrinsics.checkNotNullParameter(nodeController, "this");
            Intrinsics.checkNotNullParameter(nodeController2, "child");
            throw new RuntimeException("Not supported");
        }

        public static void moveChildTo(NodeController nodeController, NodeController nodeController2, int i) {
            Intrinsics.checkNotNullParameter(nodeController, "this");
            Intrinsics.checkNotNullParameter(nodeController2, "child");
            throw new RuntimeException("Not supported");
        }

        public static void removeChild(NodeController nodeController, NodeController nodeController2, boolean z) {
            Intrinsics.checkNotNullParameter(nodeController, "this");
            Intrinsics.checkNotNullParameter(nodeController2, "child");
            throw new RuntimeException("Not supported");
        }
    }
}
