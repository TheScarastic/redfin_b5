package com.android.systemui.statusbar.notification.people;

import android.app.NotificationChannel;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: PeopleNotificationIdentifier.kt */
/* loaded from: classes.dex */
public final class PeopleNotificationIdentifierImpl implements PeopleNotificationIdentifier {
    private final GroupMembershipManager groupManager;
    private final NotificationPersonExtractor personExtractor;

    public PeopleNotificationIdentifierImpl(NotificationPersonExtractor notificationPersonExtractor, GroupMembershipManager groupMembershipManager) {
        Intrinsics.checkNotNullParameter(notificationPersonExtractor, "personExtractor");
        Intrinsics.checkNotNullParameter(groupMembershipManager, "groupManager");
        this.personExtractor = notificationPersonExtractor;
        this.groupManager = groupMembershipManager;
    }

    @Override // com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier
    public int getPeopleNotificationType(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        NotificationListenerService.Ranking ranking = notificationEntry.getRanking();
        Intrinsics.checkNotNullExpressionValue(ranking, "entry.ranking");
        int personTypeInfo = getPersonTypeInfo(ranking);
        if (personTypeInfo == 3) {
            return 3;
        }
        StatusBarNotification sbn = notificationEntry.getSbn();
        Intrinsics.checkNotNullExpressionValue(sbn, "entry.sbn");
        int upperBound = upperBound(personTypeInfo, extractPersonTypeInfo(sbn));
        if (upperBound == 3) {
            return 3;
        }
        return upperBound(upperBound, getPeopleTypeOfSummary(notificationEntry));
    }

    @Override // com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier
    public int compareTo(int i, int i2) {
        return Intrinsics.compare(i2, i);
    }

    private final int upperBound(int i, int i2) {
        return Math.max(i, i2);
    }

    private final int getPersonTypeInfo(NotificationListenerService.Ranking ranking) {
        if (!ranking.isConversation()) {
            return 0;
        }
        if (ranking.getConversationShortcutInfo() == null) {
            return 1;
        }
        NotificationChannel channel = ranking.getChannel();
        return Intrinsics.areEqual(channel == null ? null : Boolean.valueOf(channel.isImportantConversation()), Boolean.TRUE) ? 3 : 2;
    }

    private final int extractPersonTypeInfo(StatusBarNotification statusBarNotification) {
        return this.personExtractor.isPersonNotification(statusBarNotification) ? 1 : 0;
    }

    private final int getPeopleTypeOfSummary(NotificationEntry notificationEntry) {
        Sequence sequence;
        int i = 0;
        if (!this.groupManager.isGroupSummary(notificationEntry)) {
            return 0;
        }
        List<NotificationEntry> children = this.groupManager.getChildren(notificationEntry);
        Sequence sequence2 = null;
        if (!(children == null || (sequence = CollectionsKt___CollectionsKt.asSequence(children)) == null)) {
            sequence2 = SequencesKt___SequencesKt.map(sequence, new Function1<NotificationEntry, Integer>(this) { // from class: com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl$getPeopleTypeOfSummary$childTypes$1
                final /* synthetic */ PeopleNotificationIdentifierImpl this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                public final int invoke(NotificationEntry notificationEntry2) {
                    PeopleNotificationIdentifierImpl peopleNotificationIdentifierImpl = this.this$0;
                    Intrinsics.checkNotNullExpressionValue(notificationEntry2, "it");
                    return peopleNotificationIdentifierImpl.getPeopleNotificationType(notificationEntry2);
                }

                /* Return type fixed from 'java.lang.Object' to match base method */
                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Integer invoke(NotificationEntry notificationEntry2) {
                    return Integer.valueOf(invoke(notificationEntry2));
                }
            });
        }
        if (sequence2 == null) {
            return 0;
        }
        Iterator it = sequence2.iterator();
        while (it.hasNext() && (i = upperBound(i, ((Number) it.next()).intValue())) != 3) {
        }
        return i;
    }
}
