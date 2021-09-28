package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.NotificationInteractionTracker;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class ListDumper {
    public static String dumpTree(List<ListEntry> list, NotificationInteractionTracker notificationInteractionTracker, boolean z, String str) {
        StringBuilder sb = new StringBuilder();
        String str2 = str + "  ";
        for (int i = 0; i < list.size(); i++) {
            ListEntry listEntry = list.get(i);
            dumpEntry(listEntry, Integer.toString(i), str, sb, true, z, notificationInteractionTracker.hasUserInteractedWith(listEntry.getKey()));
            if (listEntry instanceof GroupEntry) {
                List<NotificationEntry> children = ((GroupEntry) listEntry).getChildren();
                for (int i2 = 0; i2 < children.size(); i2++) {
                    dumpEntry(children.get(i2), i + "." + i2, str2, sb, true, z, notificationInteractionTracker.hasUserInteractedWith(listEntry.getKey()));
                }
            }
        }
        return sb.toString();
    }

    public static String dumpList(List<NotificationEntry> list, boolean z, String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            dumpEntry(list.get(i), Integer.toString(i), str, sb, false, z, false);
        }
        return sb.toString();
    }

    private static void dumpEntry(ListEntry listEntry, String str, String str2, StringBuilder sb, boolean z, boolean z2, boolean z3) {
        sb.append(str2);
        sb.append("[");
        sb.append(str);
        sb.append("] ");
        sb.append(listEntry.getKey());
        if (z) {
            sb.append(" (parent=");
            sb.append(listEntry.getParent() != null ? listEntry.getParent().getKey() : null);
            sb.append(")");
        }
        if (listEntry.getSection() != null) {
            sb.append(" section=");
            sb.append(listEntry.getSection().getLabel());
        }
        if (z2) {
            NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
            Objects.requireNonNull(representativeEntry);
            NotificationEntry notificationEntry = representativeEntry;
            StringBuilder sb2 = new StringBuilder();
            if (!notificationEntry.mLifetimeExtenders.isEmpty()) {
                int size = notificationEntry.mLifetimeExtenders.size();
                String[] strArr = new String[size];
                for (int i = 0; i < size; i++) {
                    strArr[i] = notificationEntry.mLifetimeExtenders.get(i).getName();
                }
                sb2.append("lifetimeExtenders=");
                sb2.append(Arrays.toString(strArr));
                sb2.append(" ");
            }
            if (!notificationEntry.mDismissInterceptors.isEmpty()) {
                int size2 = notificationEntry.mDismissInterceptors.size();
                String[] strArr2 = new String[size2];
                for (int i2 = 0; i2 < size2; i2++) {
                    strArr2[i2] = notificationEntry.mDismissInterceptors.get(i2).getName();
                }
                sb2.append("dismissInterceptors=");
                sb2.append(Arrays.toString(strArr2));
                sb2.append(" ");
            }
            if (notificationEntry.getExcludingFilter() != null) {
                sb2.append("filter=");
                sb2.append(notificationEntry.getExcludingFilter().getName());
                sb2.append(" ");
            }
            if (notificationEntry.getNotifPromoter() != null) {
                sb2.append("promoter=");
                sb2.append(notificationEntry.getNotifPromoter().getName());
                sb2.append(" ");
            }
            if (notificationEntry.mCancellationReason != -1) {
                sb2.append("cancellationReason=");
                sb2.append(notificationEntry.mCancellationReason);
                sb2.append(" ");
            }
            if (notificationEntry.getDismissState() != NotificationEntry.DismissState.NOT_DISMISSED) {
                sb2.append("dismissState=");
                sb2.append(notificationEntry.getDismissState());
                sb2.append(" ");
            }
            if (notificationEntry.getAttachState().getSuppressedChanges().getParent() != null) {
                sb2.append("suppressedParent=");
                sb2.append(notificationEntry.getAttachState().getSuppressedChanges().getParent().getKey());
                sb2.append(" ");
            }
            if (notificationEntry.getAttachState().getSuppressedChanges().getSection() != null) {
                sb2.append("suppressedSection=");
                sb2.append(notificationEntry.getAttachState().getSuppressedChanges().getSection());
                sb2.append(" ");
            }
            if (z3) {
                sb2.append("interacted=yes ");
            }
            String sb3 = sb2.toString();
            if (!sb3.isEmpty()) {
                sb.append("\n\t");
                sb.append(str2);
                sb.append(sb3);
            }
        }
        sb.append("\n");
    }
}
