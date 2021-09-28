package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SuppressedAttachState.kt */
/* loaded from: classes.dex */
public final class SuppressedAttachState {
    public static final Companion Companion = new Companion(null);
    private GroupEntry parent;
    private NotifSection section;
    private boolean wasPruneSuppressed;

    public /* synthetic */ SuppressedAttachState(NotifSection notifSection, GroupEntry groupEntry, boolean z, DefaultConstructorMarker defaultConstructorMarker) {
        this(notifSection, groupEntry, z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SuppressedAttachState)) {
            return false;
        }
        SuppressedAttachState suppressedAttachState = (SuppressedAttachState) obj;
        return Intrinsics.areEqual(this.section, suppressedAttachState.section) && Intrinsics.areEqual(this.parent, suppressedAttachState.parent) && this.wasPruneSuppressed == suppressedAttachState.wasPruneSuppressed;
    }

    public int hashCode() {
        NotifSection notifSection = this.section;
        int i = 0;
        int hashCode = (notifSection == null ? 0 : notifSection.hashCode()) * 31;
        GroupEntry groupEntry = this.parent;
        if (groupEntry != null) {
            i = groupEntry.hashCode();
        }
        int i2 = (hashCode + i) * 31;
        boolean z = this.wasPruneSuppressed;
        if (z) {
            z = true;
        }
        int i3 = z ? 1 : 0;
        int i4 = z ? 1 : 0;
        int i5 = z ? 1 : 0;
        return i2 + i3;
    }

    public String toString() {
        return "SuppressedAttachState(section=" + this.section + ", parent=" + this.parent + ", wasPruneSuppressed=" + this.wasPruneSuppressed + ')';
    }

    private SuppressedAttachState(NotifSection notifSection, GroupEntry groupEntry, boolean z) {
        this.section = notifSection;
        this.parent = groupEntry;
        this.wasPruneSuppressed = z;
    }

    public final NotifSection getSection() {
        return this.section;
    }

    public final void setSection(NotifSection notifSection) {
        this.section = notifSection;
    }

    public final GroupEntry getParent() {
        return this.parent;
    }

    public final void setParent(GroupEntry groupEntry) {
        this.parent = groupEntry;
    }

    public final boolean getWasPruneSuppressed() {
        return this.wasPruneSuppressed;
    }

    public final void setWasPruneSuppressed(boolean z) {
        this.wasPruneSuppressed = z;
    }

    public final void clone(SuppressedAttachState suppressedAttachState) {
        Intrinsics.checkNotNullParameter(suppressedAttachState, "other");
        this.parent = suppressedAttachState.parent;
        this.section = suppressedAttachState.section;
        this.wasPruneSuppressed = suppressedAttachState.wasPruneSuppressed;
    }

    public final void reset() {
        this.parent = null;
        this.section = null;
        this.wasPruneSuppressed = false;
    }

    /* compiled from: SuppressedAttachState.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final SuppressedAttachState create() {
            return new SuppressedAttachState(null, null, false, null);
        }
    }
}
