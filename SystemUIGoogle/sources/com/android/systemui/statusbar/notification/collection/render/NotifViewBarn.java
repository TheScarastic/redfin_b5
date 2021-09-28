package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifViewBarn.kt */
/* loaded from: classes.dex */
public final class NotifViewBarn {
    private final Map<String, ExpandableNotificationRowController> rowMap = new LinkedHashMap();

    public final ExpandableNotificationRowController requireView(ListEntry listEntry) {
        Intrinsics.checkNotNullParameter(listEntry, "forEntry");
        ExpandableNotificationRowController expandableNotificationRowController = this.rowMap.get(listEntry.getKey());
        if (expandableNotificationRowController != null) {
            return expandableNotificationRowController;
        }
        throw new IllegalStateException(Intrinsics.stringPlus("No view has been registered for entry: ", listEntry));
    }

    public final void registerViewForEntry(ListEntry listEntry, ExpandableNotificationRowController expandableNotificationRowController) {
        Intrinsics.checkNotNullParameter(listEntry, "entry");
        Intrinsics.checkNotNullParameter(expandableNotificationRowController, "controller");
        Map<String, ExpandableNotificationRowController> map = this.rowMap;
        String key = listEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        map.put(key, expandableNotificationRowController);
    }

    public final void removeViewForEntry(ListEntry listEntry) {
        Intrinsics.checkNotNullParameter(listEntry, "entry");
        this.rowMap.remove(listEntry.getKey());
    }
}
