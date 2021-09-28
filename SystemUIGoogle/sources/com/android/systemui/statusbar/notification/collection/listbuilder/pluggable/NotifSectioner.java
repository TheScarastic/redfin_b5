package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
/* loaded from: classes.dex */
public abstract class NotifSectioner extends Pluggable<NotifSectioner> {
    public NodeController getHeaderNodeController() {
        return null;
    }

    public abstract boolean isInSection(ListEntry listEntry);

    /* access modifiers changed from: protected */
    public NotifSectioner(String str) {
        super(str);
    }
}
