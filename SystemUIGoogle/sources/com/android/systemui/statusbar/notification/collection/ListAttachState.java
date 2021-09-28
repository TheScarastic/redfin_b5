package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ListAttachState.kt */
/* loaded from: classes.dex */
public final class ListAttachState {
    public static final Companion Companion = new Companion(null);
    private NotifFilter excludingFilter;
    private GroupEntry parent;
    private NotifPromoter promoter;
    private NotifSection section;
    private SuppressedAttachState suppressedChanges;

    public /* synthetic */ ListAttachState(GroupEntry groupEntry, NotifSection notifSection, NotifFilter notifFilter, NotifPromoter notifPromoter, SuppressedAttachState suppressedAttachState, DefaultConstructorMarker defaultConstructorMarker) {
        this(groupEntry, notifSection, notifFilter, notifPromoter, suppressedAttachState);
    }

    public static final ListAttachState create() {
        return Companion.create();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ListAttachState)) {
            return false;
        }
        ListAttachState listAttachState = (ListAttachState) obj;
        return Intrinsics.areEqual(this.parent, listAttachState.parent) && Intrinsics.areEqual(this.section, listAttachState.section) && Intrinsics.areEqual(this.excludingFilter, listAttachState.excludingFilter) && Intrinsics.areEqual(this.promoter, listAttachState.promoter) && Intrinsics.areEqual(this.suppressedChanges, listAttachState.suppressedChanges);
    }

    public int hashCode() {
        GroupEntry groupEntry = this.parent;
        int i = 0;
        int hashCode = (groupEntry == null ? 0 : groupEntry.hashCode()) * 31;
        NotifSection notifSection = this.section;
        int hashCode2 = (hashCode + (notifSection == null ? 0 : notifSection.hashCode())) * 31;
        NotifFilter notifFilter = this.excludingFilter;
        int hashCode3 = (hashCode2 + (notifFilter == null ? 0 : notifFilter.hashCode())) * 31;
        NotifPromoter notifPromoter = this.promoter;
        if (notifPromoter != null) {
            i = notifPromoter.hashCode();
        }
        return ((hashCode3 + i) * 31) + this.suppressedChanges.hashCode();
    }

    public String toString() {
        return "ListAttachState(parent=" + this.parent + ", section=" + this.section + ", excludingFilter=" + this.excludingFilter + ", promoter=" + this.promoter + ", suppressedChanges=" + this.suppressedChanges + ')';
    }

    private ListAttachState(GroupEntry groupEntry, NotifSection notifSection, NotifFilter notifFilter, NotifPromoter notifPromoter, SuppressedAttachState suppressedAttachState) {
        this.parent = groupEntry;
        this.section = notifSection;
        this.excludingFilter = notifFilter;
        this.promoter = notifPromoter;
        this.suppressedChanges = suppressedAttachState;
    }

    public final GroupEntry getParent() {
        return this.parent;
    }

    public final void setParent(GroupEntry groupEntry) {
        this.parent = groupEntry;
    }

    public final NotifSection getSection() {
        return this.section;
    }

    public final void setSection(NotifSection notifSection) {
        this.section = notifSection;
    }

    public final NotifFilter getExcludingFilter() {
        return this.excludingFilter;
    }

    public final void setExcludingFilter(NotifFilter notifFilter) {
        this.excludingFilter = notifFilter;
    }

    public final NotifPromoter getPromoter() {
        return this.promoter;
    }

    public final void setPromoter(NotifPromoter notifPromoter) {
        this.promoter = notifPromoter;
    }

    public final SuppressedAttachState getSuppressedChanges() {
        return this.suppressedChanges;
    }

    public final void clone(ListAttachState listAttachState) {
        Intrinsics.checkNotNullParameter(listAttachState, "other");
        this.parent = listAttachState.parent;
        this.section = listAttachState.section;
        this.excludingFilter = listAttachState.excludingFilter;
        this.promoter = listAttachState.promoter;
        this.suppressedChanges.clone(listAttachState.suppressedChanges);
    }

    public final void reset() {
        this.parent = null;
        this.section = null;
        this.excludingFilter = null;
        this.promoter = null;
        this.suppressedChanges.reset();
    }

    /* compiled from: ListAttachState.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final ListAttachState create() {
            return new ListAttachState(null, null, null, null, SuppressedAttachState.Companion.create(), null);
        }
    }
}
