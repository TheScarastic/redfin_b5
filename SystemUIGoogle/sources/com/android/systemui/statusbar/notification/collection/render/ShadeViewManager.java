package com.android.systemui.statusbar.notification.collection.render;

import android.content.Context;
import android.view.View;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import java.util.List;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: ShadeViewManager.kt */
/* loaded from: classes.dex */
public final class ShadeViewManager {
    private final NotificationIconAreaController notificationIconAreaController;
    private final RootNodeController rootController;
    private final NotifViewBarn viewBarn;
    private final ShadeViewDiffer viewDiffer;

    public ShadeViewManager(Context context, NotificationListContainer notificationListContainer, ShadeViewDifferLogger shadeViewDifferLogger, NotifViewBarn notifViewBarn, NotificationIconAreaController notificationIconAreaController) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(notificationListContainer, "listContainer");
        Intrinsics.checkNotNullParameter(shadeViewDifferLogger, "logger");
        Intrinsics.checkNotNullParameter(notifViewBarn, "viewBarn");
        Intrinsics.checkNotNullParameter(notificationIconAreaController, "notificationIconAreaController");
        this.viewBarn = notifViewBarn;
        this.notificationIconAreaController = notificationIconAreaController;
        RootNodeController rootNodeController = new RootNodeController(notificationListContainer, new View(context));
        this.rootController = rootNodeController;
        this.viewDiffer = new ShadeViewDiffer(rootNodeController, shadeViewDifferLogger);
    }

    public final void attach(ShadeListBuilder shadeListBuilder) {
        Intrinsics.checkNotNullParameter(shadeListBuilder, "listBuilder");
        shadeListBuilder.setOnRenderListListener(new ShadeListBuilder.OnRenderListListener() { // from class: com.android.systemui.statusbar.notification.collection.render.ShadeViewManager$attach$1
            @Override // com.android.systemui.statusbar.notification.collection.ShadeListBuilder.OnRenderListListener
            public final void onRenderList(List<? extends ListEntry> list) {
                Intrinsics.checkNotNullParameter(list, "p0");
                ShadeViewManager.this.onNewNotifTree(list);
            }
        });
    }

    /* access modifiers changed from: private */
    public final void onNewNotifTree(List<? extends ListEntry> list) {
        this.viewDiffer.applySpec(buildTree(list));
    }

    private final NodeSpec buildTree(List<? extends ListEntry> list) {
        NodeController headerController;
        NodeController headerController2;
        NodeSpecImpl nodeSpecImpl = new NodeSpecImpl(null, this.rootController);
        ListEntry listEntry = (ListEntry) CollectionsKt.firstOrNull(list);
        NotifSection section = listEntry == null ? null : listEntry.getSection();
        if (!(section == null || (headerController2 = section.getHeaderController()) == null)) {
            nodeSpecImpl.getChildren().add(new NodeSpecImpl(nodeSpecImpl, headerController2));
        }
        for (Pair pair : SequencesKt___SequencesKt.zipWithNext(CollectionsKt___CollectionsKt.asSequence(list))) {
            ListEntry listEntry2 = (ListEntry) pair.component2();
            NotifSection section2 = listEntry2.getSection();
            if (!(!Intrinsics.areEqual(section2, ((ListEntry) pair.component1()).getSection()))) {
                section2 = null;
            }
            if (!(section2 == null || (headerController = section2.getHeaderController()) == null)) {
                nodeSpecImpl.getChildren().add(new NodeSpecImpl(nodeSpecImpl, headerController));
            }
            nodeSpecImpl.getChildren().add(buildNotifNode(listEntry2, nodeSpecImpl));
        }
        this.notificationIconAreaController.updateNotificationIcons(list);
        return nodeSpecImpl;
    }

    private final NodeSpec buildNotifNode(ListEntry listEntry, NodeSpec nodeSpec) {
        if (listEntry instanceof NotificationEntry) {
            return new NodeSpecImpl(nodeSpec, this.viewBarn.requireView(listEntry));
        }
        if (listEntry instanceof GroupEntry) {
            NotifViewBarn notifViewBarn = this.viewBarn;
            GroupEntry groupEntry = (GroupEntry) listEntry;
            NotificationEntry summary = groupEntry.getSummary();
            if (summary != null) {
                NodeSpecImpl nodeSpecImpl = new NodeSpecImpl(nodeSpec, notifViewBarn.requireView(summary));
                List<NotificationEntry> children = groupEntry.getChildren();
                Intrinsics.checkNotNullExpressionValue(children, "entry.children");
                for (ListEntry listEntry2 : children) {
                    List<NodeSpec> children2 = nodeSpecImpl.getChildren();
                    Intrinsics.checkNotNullExpressionValue(listEntry2, "it");
                    children2.add(buildNotifNode(listEntry2, nodeSpecImpl));
                }
                return nodeSpecImpl;
            }
            throw new IllegalStateException("Required value was null.".toString());
        }
        throw new RuntimeException(Intrinsics.stringPlus("Unexpected entry: ", listEntry));
    }
}
