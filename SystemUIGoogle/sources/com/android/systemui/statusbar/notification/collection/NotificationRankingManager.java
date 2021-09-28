package com.android.systemui.statusbar.notification.collection;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.Lazy;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import kotlin.LazyKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: NotificationRankingManager.kt */
/* loaded from: classes.dex */
public class NotificationRankingManager implements LegacyNotificationRanker {
    private final NotificationGroupManagerLegacy groupManager;
    private final HeadsUpManager headsUpManager;
    private final HighPriorityProvider highPriorityProvider;
    private final NotificationEntryManager.KeyguardEnvironment keyguardEnvironment;
    private final NotificationEntryManagerLogger logger;
    private final Lazy<NotificationMediaManager> mediaManagerLazy;
    private final NotificationFilter notifFilter;
    private final PeopleNotificationIdentifier peopleNotificationIdentifier;
    private NotificationListenerService.RankingMap rankingMap;
    private final NotificationSectionsFeatureManager sectionsFeatureManager;
    private final kotlin.Lazy mediaManager$delegate = LazyKt.lazy(new Function0<NotificationMediaManager>(this) { // from class: com.android.systemui.statusbar.notification.collection.NotificationRankingManager$mediaManager$2
        final /* synthetic */ NotificationRankingManager this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // kotlin.jvm.functions.Function0
        public final NotificationMediaManager invoke() {
            return (NotificationMediaManager) this.this$0.mediaManagerLazy.get();
        }
    });
    private final Comparator<NotificationEntry> rankingComparator = new Comparator<NotificationEntry>(this) { // from class: com.android.systemui.statusbar.notification.collection.NotificationRankingManager$rankingComparator$1
        final /* synthetic */ NotificationRankingManager this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        public final int compare(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
            StatusBarNotification sbn = notificationEntry.getSbn();
            Intrinsics.checkNotNullExpressionValue(sbn, "a.sbn");
            StatusBarNotification sbn2 = notificationEntry2.getSbn();
            Intrinsics.checkNotNullExpressionValue(sbn2, "b.sbn");
            int rank = notificationEntry.getRanking().getRank();
            int rank2 = notificationEntry2.getRanking().getRank();
            Intrinsics.checkNotNullExpressionValue(notificationEntry, "a");
            boolean isColorizedForegroundService = NotificationRankingManagerKt.isColorizedForegroundService(notificationEntry);
            Intrinsics.checkNotNullExpressionValue(notificationEntry2, "b");
            boolean isColorizedForegroundService2 = NotificationRankingManagerKt.isColorizedForegroundService(notificationEntry2);
            boolean isImportantCall = NotificationRankingManagerKt.isImportantCall(notificationEntry);
            boolean isImportantCall2 = NotificationRankingManagerKt.isImportantCall(notificationEntry2);
            int i = this.this$0.getPeopleNotificationType(notificationEntry);
            int i2 = this.this$0.getPeopleNotificationType(notificationEntry2);
            boolean z = this.this$0.isImportantMedia(notificationEntry);
            boolean z2 = this.this$0.isImportantMedia(notificationEntry2);
            boolean isSystemMax = NotificationRankingManagerKt.isSystemMax(notificationEntry);
            boolean isSystemMax2 = NotificationRankingManagerKt.isSystemMax(notificationEntry2);
            boolean isRowHeadsUp = notificationEntry.isRowHeadsUp();
            boolean isRowHeadsUp2 = notificationEntry2.isRowHeadsUp();
            boolean z3 = this.this$0.isHighPriority(notificationEntry);
            boolean z4 = this.this$0.isHighPriority(notificationEntry2);
            if (isRowHeadsUp != isRowHeadsUp2) {
                if (!isRowHeadsUp) {
                    return 1;
                }
            } else if (isRowHeadsUp) {
                return this.this$0.headsUpManager.compare(notificationEntry, notificationEntry2);
            } else {
                if (isColorizedForegroundService != isColorizedForegroundService2) {
                    if (!isColorizedForegroundService) {
                        return 1;
                    }
                } else if (isImportantCall != isImportantCall2) {
                    if (!isImportantCall) {
                        return 1;
                    }
                } else if ((this.this$0.getUsePeopleFiltering()) && i != i2) {
                    return this.this$0.peopleNotificationIdentifier.compareTo(i, i2);
                } else {
                    if (z != z2) {
                        if (!z) {
                            return 1;
                        }
                    } else if (isSystemMax != isSystemMax2) {
                        if (!isSystemMax) {
                            return 1;
                        }
                    } else if (z3 != z4) {
                        return Intrinsics.compare(z3 ? 1 : 0, z4 ? 1 : 0) * -1;
                    } else {
                        if (rank != rank2) {
                            return rank - rank2;
                        }
                        return Intrinsics.compare(sbn2.getNotification().when, sbn.getNotification().when);
                    }
                }
            }
            return -1;
        }
    };

    public NotificationRankingManager(Lazy<NotificationMediaManager> lazy, NotificationGroupManagerLegacy notificationGroupManagerLegacy, HeadsUpManager headsUpManager, NotificationFilter notificationFilter, NotificationEntryManagerLogger notificationEntryManagerLogger, NotificationSectionsFeatureManager notificationSectionsFeatureManager, PeopleNotificationIdentifier peopleNotificationIdentifier, HighPriorityProvider highPriorityProvider, NotificationEntryManager.KeyguardEnvironment keyguardEnvironment) {
        Intrinsics.checkNotNullParameter(lazy, "mediaManagerLazy");
        Intrinsics.checkNotNullParameter(notificationGroupManagerLegacy, "groupManager");
        Intrinsics.checkNotNullParameter(headsUpManager, "headsUpManager");
        Intrinsics.checkNotNullParameter(notificationFilter, "notifFilter");
        Intrinsics.checkNotNullParameter(notificationEntryManagerLogger, "logger");
        Intrinsics.checkNotNullParameter(notificationSectionsFeatureManager, "sectionsFeatureManager");
        Intrinsics.checkNotNullParameter(peopleNotificationIdentifier, "peopleNotificationIdentifier");
        Intrinsics.checkNotNullParameter(highPriorityProvider, "highPriorityProvider");
        Intrinsics.checkNotNullParameter(keyguardEnvironment, "keyguardEnvironment");
        this.mediaManagerLazy = lazy;
        this.groupManager = notificationGroupManagerLegacy;
        this.headsUpManager = headsUpManager;
        this.notifFilter = notificationFilter;
        this.logger = notificationEntryManagerLogger;
        this.sectionsFeatureManager = notificationSectionsFeatureManager;
        this.peopleNotificationIdentifier = peopleNotificationIdentifier;
        this.highPriorityProvider = highPriorityProvider;
        this.keyguardEnvironment = keyguardEnvironment;
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    public NotificationListenerService.RankingMap getRankingMap() {
        return this.rankingMap;
    }

    protected void setRankingMap(NotificationListenerService.RankingMap rankingMap) {
        this.rankingMap = rankingMap;
    }

    private final NotificationMediaManager getMediaManager() {
        return (NotificationMediaManager) this.mediaManager$delegate.getValue();
    }

    /* access modifiers changed from: private */
    public final boolean getUsePeopleFiltering() {
        return this.sectionsFeatureManager.isFilteringEnabled();
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    public List<NotificationEntry> updateRanking(NotificationListenerService.RankingMap rankingMap, Collection<NotificationEntry> collection, String str) {
        List<NotificationEntry> filterAndSortLocked;
        Intrinsics.checkNotNullParameter(collection, "entries");
        Intrinsics.checkNotNullParameter(str, "reason");
        if (rankingMap != null) {
            setRankingMap(rankingMap);
            updateRankingForEntries(collection);
        }
        synchronized (this) {
            filterAndSortLocked = filterAndSortLocked(collection, str);
        }
        return filterAndSortLocked;
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    public boolean isNotificationForCurrentProfiles(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        return this.keyguardEnvironment.isNotificationForCurrentProfiles(notificationEntry.getSbn());
    }

    private final List<NotificationEntry> filterAndSortLocked(Collection<NotificationEntry> collection, String str) {
        this.logger.logFilterAndSort(str);
        List<NotificationEntry> list = SequencesKt___SequencesKt.toList(SequencesKt___SequencesKt.sortedWith(SequencesKt___SequencesKt.filterNot(CollectionsKt___CollectionsKt.asSequence(collection), new Function1<NotificationEntry, Boolean>(this) { // from class: com.android.systemui.statusbar.notification.collection.NotificationRankingManager$filterAndSortLocked$filtered$1
            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Boolean invoke(NotificationEntry notificationEntry) {
                return Boolean.valueOf(invoke(notificationEntry));
            }

            public final boolean invoke(NotificationEntry notificationEntry) {
                Intrinsics.checkNotNullParameter(notificationEntry, "p0");
                return ((NotificationRankingManager) this.receiver).filter(notificationEntry);
            }
        }), this.rankingComparator));
        for (NotificationEntry notificationEntry : collection) {
            notificationEntry.setBucket(getBucketForEntry(notificationEntry));
        }
        return list;
    }

    /* access modifiers changed from: private */
    public final boolean filter(NotificationEntry notificationEntry) {
        boolean shouldFilterOut = this.notifFilter.shouldFilterOut(notificationEntry);
        if (shouldFilterOut) {
            notificationEntry.resetInitializationTime();
        }
        return shouldFilterOut;
    }

    private final int getBucketForEntry(NotificationEntry notificationEntry) {
        boolean access$isImportantCall = NotificationRankingManagerKt.access$isImportantCall(notificationEntry);
        boolean isRowHeadsUp = notificationEntry.isRowHeadsUp();
        boolean isImportantMedia = isImportantMedia(notificationEntry);
        boolean access$isSystemMax = NotificationRankingManagerKt.access$isSystemMax(notificationEntry);
        if (NotificationRankingManagerKt.access$isColorizedForegroundService(notificationEntry) || access$isImportantCall) {
            return 3;
        }
        if (!getUsePeopleFiltering() || !isConversation(notificationEntry)) {
            return (isRowHeadsUp || isImportantMedia || access$isSystemMax || isHighPriority(notificationEntry)) ? 5 : 6;
        }
        return 4;
    }

    private final void updateRankingForEntries(Iterable<NotificationEntry> iterable) {
        NotificationListenerService.RankingMap rankingMap = getRankingMap();
        if (rankingMap != null) {
            synchronized (iterable) {
                for (NotificationEntry notificationEntry : iterable) {
                    NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                    if (rankingMap.getRanking(notificationEntry.getKey(), ranking)) {
                        notificationEntry.setRanking(ranking);
                        String overrideGroupKey = ranking.getOverrideGroupKey();
                        if (!Objects.equals(notificationEntry.getSbn().getOverrideGroupKey(), overrideGroupKey)) {
                            String groupKey = notificationEntry.getSbn().getGroupKey();
                            boolean isGroup = notificationEntry.getSbn().isGroup();
                            boolean isGroupSummary = notificationEntry.getSbn().getNotification().isGroupSummary();
                            notificationEntry.getSbn().setOverrideGroupKey(overrideGroupKey);
                            this.groupManager.onEntryUpdated(notificationEntry, groupKey, isGroup, isGroupSummary);
                        }
                    }
                }
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    /* access modifiers changed from: private */
    public final boolean isImportantMedia(NotificationEntry notificationEntry) {
        return Intrinsics.areEqual(notificationEntry.getKey(), getMediaManager().getMediaNotificationKey()) && notificationEntry.getImportance() > 1;
    }

    private final boolean isConversation(NotificationEntry notificationEntry) {
        return getPeopleNotificationType(notificationEntry) != 0;
    }

    /* access modifiers changed from: private */
    public final int getPeopleNotificationType(NotificationEntry notificationEntry) {
        return this.peopleNotificationIdentifier.getPeopleNotificationType(notificationEntry);
    }

    /* access modifiers changed from: private */
    public final boolean isHighPriority(NotificationEntry notificationEntry) {
        return this.highPriorityProvider.isHighPriority(notificationEntry);
    }
}
