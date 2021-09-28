package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifSection.kt */
/* loaded from: classes.dex */
public final class NotifSection {
    private final int index;
    private final NotifSectioner sectioner;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NotifSection)) {
            return false;
        }
        NotifSection notifSection = (NotifSection) obj;
        return Intrinsics.areEqual(this.sectioner, notifSection.sectioner) && this.index == notifSection.index;
    }

    public int hashCode() {
        return (this.sectioner.hashCode() * 31) + Integer.hashCode(this.index);
    }

    public String toString() {
        return "NotifSection(sectioner=" + this.sectioner + ", index=" + this.index + ')';
    }

    public NotifSection(NotifSectioner notifSectioner, int i) {
        Intrinsics.checkNotNullParameter(notifSectioner, "sectioner");
        this.sectioner = notifSectioner;
        this.index = i;
    }

    public final NotifSectioner getSectioner() {
        return this.sectioner;
    }

    public final int getIndex() {
        return this.index;
    }

    public final String getLabel() {
        return "Section(" + this.index + ", \"" + ((Object) this.sectioner.getName()) + "\")";
    }

    public final NodeController getHeaderController() {
        return this.sectioner.getHeaderNodeController();
    }
}
