package com.android.systemui.statusbar.notification;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.view.View;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.internal.widget.ConversationLayout;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.collections.MapsKt___MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationManager {
    public static final Companion Companion = new Companion(null);
    private final Context context;
    private final Handler mainHandler;
    private final NotificationEntryManager notificationEntryManager;
    private final NotificationGroupManagerLegacy notificationGroupManager;
    private final ConcurrentHashMap<String, ConversationState> states = new ConcurrentHashMap<>();
    private boolean notifPanelCollapsed = true;

    public ConversationNotificationManager(NotificationEntryManager notificationEntryManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, Context context, Handler handler) {
        Intrinsics.checkNotNullParameter(notificationEntryManager, "notificationEntryManager");
        Intrinsics.checkNotNullParameter(notificationGroupManagerLegacy, "notificationGroupManager");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(handler, "mainHandler");
        this.notificationEntryManager = notificationEntryManager;
        this.notificationGroupManager = notificationGroupManagerLegacy;
        this.context = context;
        this.mainHandler = handler;
        notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener(this) { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager.1
            final /* synthetic */ ConversationNotificationManager this$0;

            {
                this.this$0 = r1;
            }

            /* access modifiers changed from: private */
            public static final Sequence<View> onNotificationRankingUpdated$getLayouts(NotificationContentView notificationContentView) {
                return SequencesKt__SequencesKt.sequenceOf(notificationContentView.getContractedChild(), notificationContentView.getExpandedChild(), notificationContentView.getHeadsUpChild());
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onNotificationRankingUpdated(NotificationListenerService.RankingMap rankingMap) {
                Sequence<ConversationLayout> sequence;
                Sequence sequence2;
                NotificationContentView[] layouts;
                Intrinsics.checkNotNullParameter(rankingMap, "rankingMap");
                NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                Set keySet = this.this$0.states.keySet();
                Intrinsics.checkNotNullExpressionValue(keySet, "states.keys");
                for (NotificationEntry notificationEntry : SequencesKt___SequencesKt.mapNotNull(CollectionsKt___CollectionsKt.asSequence(keySet), new ConversationNotificationManager$1$onNotificationRankingUpdated$activeConversationEntries$1(this.this$0))) {
                    if (rankingMap.getRanking(notificationEntry.getSbn().getKey(), ranking) && ranking.isConversation()) {
                        boolean isImportantConversation = ranking.getChannel().isImportantConversation();
                        ExpandableNotificationRow row = notificationEntry.getRow();
                        Sequence sequence3 = null;
                        Sequence sequence4 = (row == null || (layouts = row.getLayouts()) == null) ? null : ArraysKt___ArraysKt.asSequence(layouts);
                        if (!(sequence4 == null || (sequence2 = SequencesKt___SequencesKt.flatMap(sequence4, ConversationNotificationManager$1$onNotificationRankingUpdated$1.INSTANCE)) == null)) {
                            sequence3 = SequencesKt___SequencesKt.mapNotNull(sequence2, ConversationNotificationManager$1$onNotificationRankingUpdated$2.INSTANCE);
                        }
                        boolean z = false;
                        if (!(sequence3 == null || (sequence = SequencesKt___SequencesKt.filterNot(sequence3, new ConversationNotificationManager$1$onNotificationRankingUpdated$3(isImportantConversation))) == null)) {
                            ConversationNotificationManager conversationNotificationManager = this.this$0;
                            boolean z2 = false;
                            for (ConversationLayout conversationLayout : sequence) {
                                if (!isImportantConversation || !notificationEntry.isMarkedForUserTriggeredMovement()) {
                                    conversationLayout.setIsImportantConversation(isImportantConversation, false);
                                } else {
                                    conversationNotificationManager.mainHandler.postDelayed(new ConversationNotificationManager$1$onNotificationRankingUpdated$4$1(conversationLayout, isImportantConversation), 960);
                                }
                                z2 = true;
                            }
                            z = z2;
                        }
                        if (z) {
                            this.this$0.notificationGroupManager.updateIsolation(notificationEntry);
                        }
                    }
                }
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryInflated(NotificationEntry notificationEntry) {
                Intrinsics.checkNotNullParameter(notificationEntry, "entry");
                if (notificationEntry.getRanking().isConversation()) {
                    ExpandableNotificationRow row = notificationEntry.getRow();
                    if (row != null) {
                        row.setOnExpansionChangedListener(new ConversationNotificationManager$1$onEntryInflated$1(notificationEntry, this.this$0));
                    }
                    ConversationNotificationManager conversationNotificationManager = this.this$0;
                    ExpandableNotificationRow row2 = notificationEntry.getRow();
                    onEntryInflated$updateCount(conversationNotificationManager, notificationEntry, Intrinsics.areEqual(row2 == null ? null : Boolean.valueOf(row2.isExpanded()), Boolean.TRUE));
                }
            }

            /* access modifiers changed from: private */
            public static final void onEntryInflated$updateCount(ConversationNotificationManager conversationNotificationManager, NotificationEntry notificationEntry, boolean z) {
                if (!z) {
                    return;
                }
                if (!conversationNotificationManager.notifPanelCollapsed || notificationEntry.isPinnedAndExpanded()) {
                    String key = notificationEntry.getKey();
                    Intrinsics.checkNotNullExpressionValue(key, "entry.key");
                    conversationNotificationManager.resetCount(key);
                    ExpandableNotificationRow row = notificationEntry.getRow();
                    if (row != null) {
                        conversationNotificationManager.resetBadgeUi(row);
                    }
                }
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryReinflated(NotificationEntry notificationEntry) {
                Intrinsics.checkNotNullParameter(notificationEntry, "entry");
                onEntryInflated(notificationEntry);
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
                Intrinsics.checkNotNullParameter(notificationEntry, "entry");
                this.this$0.removeTrackedEntry(notificationEntry);
            }
        });
    }

    /* access modifiers changed from: private */
    public final boolean shouldIncrementUnread(ConversationState conversationState, Notification.Builder builder) {
        if ((conversationState.getNotification().flags & 8) != 0) {
            return false;
        }
        return Notification.areStyledNotificationsVisiblyDifferent(Notification.Builder.recoverBuilder(this.context, conversationState.getNotification()), builder);
    }

    public final int getUnreadCount(NotificationEntry notificationEntry, Notification.Builder builder) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(builder, "recoveredBuilder");
        ConversationState compute = this.states.compute(notificationEntry.getKey(), new BiFunction<String, ConversationState, ConversationState>(notificationEntry, this, builder) { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$getUnreadCount$1
            final /* synthetic */ NotificationEntry $entry;
            final /* synthetic */ Notification.Builder $recoveredBuilder;
            final /* synthetic */ ConversationNotificationManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.$entry = r1;
                this.this$0 = r2;
                this.$recoveredBuilder = r3;
            }

            public final ConversationNotificationManager.ConversationState apply(String str, ConversationNotificationManager.ConversationState conversationState) {
                Intrinsics.checkNotNullParameter(str, "$noName_0");
                int i = 1;
                if (conversationState != null) {
                    i = this.this$0.shouldIncrementUnread(conversationState, this.$recoveredBuilder) ? conversationState.getUnreadCount() + 1 : conversationState.getUnreadCount();
                }
                Notification notification = this.$entry.getSbn().getNotification();
                Intrinsics.checkNotNullExpressionValue(notification, "entry.sbn.notification");
                return new ConversationNotificationManager.ConversationState(i, notification);
            }
        });
        Intrinsics.checkNotNull(compute);
        return compute.getUnreadCount();
    }

    public final void onNotificationPanelExpandStateChanged(boolean z) {
        this.notifPanelCollapsed = z;
        if (!z) {
            Map map = MapsKt__MapsKt.toMap(SequencesKt___SequencesKt.mapNotNull(MapsKt___MapsKt.asSequence(this.states), new Function1<Map.Entry<? extends String, ? extends ConversationState>, Pair<? extends String, ? extends NotificationEntry>>(this) { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$onNotificationPanelExpandStateChanged$expanded$1
                final /* synthetic */ ConversationNotificationManager this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                /* Return type fixed from 'java.lang.Object' to match base method */
                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Pair<? extends String, ? extends NotificationEntry> invoke(Map.Entry<? extends String, ? extends ConversationNotificationManager.ConversationState> entry) {
                    return invoke((Map.Entry<String, ConversationNotificationManager.ConversationState>) entry);
                }

                public final Pair<String, NotificationEntry> invoke(Map.Entry<String, ConversationNotificationManager.ConversationState> entry) {
                    Intrinsics.checkNotNullParameter(entry, "$dstr$key$_u24__u24");
                    String key = entry.getKey();
                    NotificationEntry activeNotificationUnfiltered = this.this$0.notificationEntryManager.getActiveNotificationUnfiltered(key);
                    if (activeNotificationUnfiltered == null) {
                        return null;
                    }
                    ExpandableNotificationRow row = activeNotificationUnfiltered.getRow();
                    if (Intrinsics.areEqual(row == null ? null : Boolean.valueOf(row.isExpanded()), Boolean.TRUE)) {
                        return TuplesKt.to(key, activeNotificationUnfiltered);
                    }
                    return null;
                }
            }));
            this.states.replaceAll(new BiFunction<String, ConversationState, ConversationState>(map) { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$onNotificationPanelExpandStateChanged$1
                final /* synthetic */ Map<String, NotificationEntry> $expanded;

                /* access modifiers changed from: package-private */
                {
                    this.$expanded = r1;
                }

                public final ConversationNotificationManager.ConversationState apply(String str, ConversationNotificationManager.ConversationState conversationState) {
                    Intrinsics.checkNotNullParameter(str, "key");
                    Intrinsics.checkNotNullParameter(conversationState, "state");
                    return this.$expanded.containsKey(str) ? ConversationNotificationManager.ConversationState.copy$default(conversationState, 0, null, 2, null) : conversationState;
                }
            });
            for (ExpandableNotificationRow expandableNotificationRow : SequencesKt___SequencesKt.mapNotNull(CollectionsKt___CollectionsKt.asSequence(map.values()), ConversationNotificationManager$onNotificationPanelExpandStateChanged$2.INSTANCE)) {
                resetBadgeUi(expandableNotificationRow);
            }
        }
    }

    /* access modifiers changed from: private */
    public final void resetCount(String str) {
        this.states.compute(str, ConversationNotificationManager$resetCount$1.INSTANCE);
    }

    /* access modifiers changed from: private */
    public final void removeTrackedEntry(NotificationEntry notificationEntry) {
        this.states.remove(notificationEntry.getKey());
    }

    /* access modifiers changed from: private */
    public final void resetBadgeUi(ExpandableNotificationRow expandableNotificationRow) {
        NotificationContentView[] layouts = expandableNotificationRow.getLayouts();
        Sequence sequence = layouts == null ? null : ArraysKt___ArraysKt.asSequence(layouts);
        if (sequence == null) {
            sequence = SequencesKt__SequencesKt.emptySequence();
        }
        for (ConversationLayout conversationLayout : SequencesKt___SequencesKt.mapNotNull(SequencesKt___SequencesKt.flatMap(sequence, ConversationNotificationManager$resetBadgeUi$1.INSTANCE), ConversationNotificationManager$resetBadgeUi$2.INSTANCE)) {
            conversationLayout.setUnreadCount(0);
        }
    }

    /* compiled from: ConversationNotifications.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ConversationState {
        private final Notification notification;
        private final int unreadCount;

        public static /* synthetic */ ConversationState copy$default(ConversationState conversationState, int i, Notification notification, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = conversationState.unreadCount;
            }
            if ((i2 & 2) != 0) {
                notification = conversationState.notification;
            }
            return conversationState.copy(i, notification);
        }

        public final ConversationState copy(int i, Notification notification) {
            Intrinsics.checkNotNullParameter(notification, "notification");
            return new ConversationState(i, notification);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ConversationState)) {
                return false;
            }
            ConversationState conversationState = (ConversationState) obj;
            return this.unreadCount == conversationState.unreadCount && Intrinsics.areEqual(this.notification, conversationState.notification);
        }

        public int hashCode() {
            return (Integer.hashCode(this.unreadCount) * 31) + this.notification.hashCode();
        }

        public String toString() {
            return "ConversationState(unreadCount=" + this.unreadCount + ", notification=" + this.notification + ')';
        }

        public ConversationState(int i, Notification notification) {
            Intrinsics.checkNotNullParameter(notification, "notification");
            this.unreadCount = i;
            this.notification = notification;
        }

        public final Notification getNotification() {
            return this.notification;
        }

        public final int getUnreadCount() {
            return this.unreadCount;
        }
    }

    /* compiled from: ConversationNotifications.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
