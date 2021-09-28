package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import java.util.Comparator;
/* loaded from: classes.dex */
public abstract class NotifComparator extends Pluggable<NotifComparator> implements Comparator<ListEntry> {
    public abstract int compare(ListEntry listEntry, ListEntry listEntry2);
}
