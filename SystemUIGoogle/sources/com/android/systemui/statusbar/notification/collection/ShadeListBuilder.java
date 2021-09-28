package com.android.systemui.statusbar.notification.collection;

import android.util.ArrayMap;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationInteractionTracker;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeSortListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeTransformGroupsListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.PipelineState;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable;
import com.android.systemui.statusbar.notification.collection.notifcollection.CollectionReadyForBuildListener;
import com.android.systemui.util.Assert;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public class ShadeListBuilder implements Dumpable {
    private final NotificationInteractionTracker mInteractionTracker;
    private final ShadeListBuilderLogger mLogger;
    private NotifStabilityManager mNotifStabilityManager;
    private OnRenderListListener mOnRenderListListener;
    private final SystemClock mSystemClock;
    private static final Comparator<NotificationEntry> sChildComparator = ShadeListBuilder$$ExternalSyntheticLambda6.INSTANCE;
    private static final NotifSectioner DEFAULT_SECTIONER = new NotifSectioner("UnknownSection") { // from class: com.android.systemui.statusbar.notification.collection.ShadeListBuilder.2
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public boolean isInSection(ListEntry listEntry) {
            return true;
        }
    };
    private List<ListEntry> mNotifList = new ArrayList();
    private List<ListEntry> mNewNotifList = new ArrayList();
    private final PipelineState mPipelineState = new PipelineState();
    private final Map<String, GroupEntry> mGroups = new ArrayMap();
    private Collection<NotificationEntry> mAllEntries = Collections.emptyList();
    private int mIterationCount = 0;
    private final List<NotifFilter> mNotifPreGroupFilters = new ArrayList();
    private final List<NotifPromoter> mNotifPromoters = new ArrayList();
    private final List<NotifFilter> mNotifFinalizeFilters = new ArrayList();
    private final List<NotifComparator> mNotifComparators = new ArrayList();
    private final List<NotifSection> mNotifSections = new ArrayList();
    private final List<OnBeforeTransformGroupsListener> mOnBeforeTransformGroupsListeners = new ArrayList();
    private final List<OnBeforeSortListener> mOnBeforeSortListeners = new ArrayList();
    private final List<OnBeforeFinalizeFilterListener> mOnBeforeFinalizeFilterListeners = new ArrayList();
    private final List<OnBeforeRenderListListener> mOnBeforeRenderListListeners = new ArrayList();
    private List<ListEntry> mReadOnlyNotifList = Collections.unmodifiableList(this.mNotifList);
    private List<ListEntry> mReadOnlyNewNotifList = Collections.unmodifiableList(this.mNewNotifList);
    private final CollectionReadyForBuildListener mReadyForBuildListener = new CollectionReadyForBuildListener() { // from class: com.android.systemui.statusbar.notification.collection.ShadeListBuilder.1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.CollectionReadyForBuildListener
        public void onBuildList(Collection<NotificationEntry> collection) {
            Assert.isMainThread();
            ShadeListBuilder.this.mPipelineState.requireIsBefore(1);
            ShadeListBuilder.this.mLogger.logOnBuildList();
            ShadeListBuilder.this.mAllEntries = collection;
            ShadeListBuilder.this.buildList();
        }
    };
    private final Comparator<ListEntry> mTopLevelComparator = new Comparator() { // from class: com.android.systemui.statusbar.notification.collection.ShadeListBuilder$$ExternalSyntheticLambda5
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            return ShadeListBuilder.this.lambda$new$2((ListEntry) obj, (ListEntry) obj2);
        }
    };

    /* loaded from: classes.dex */
    public interface OnRenderListListener {
        void onRenderList(List<ListEntry> list);
    }

    public ShadeListBuilder(SystemClock systemClock, ShadeListBuilderLogger shadeListBuilderLogger, DumpManager dumpManager, NotificationInteractionTracker notificationInteractionTracker) {
        Assert.isMainThread();
        this.mSystemClock = systemClock;
        this.mLogger = shadeListBuilderLogger;
        this.mInteractionTracker = notificationInteractionTracker;
        dumpManager.registerDumpable("ShadeListBuilder", this);
        setSectioners(Collections.emptyList());
    }

    public void attach(NotifCollection notifCollection) {
        Assert.isMainThread();
        notifCollection.setBuildListener(this.mReadyForBuildListener);
    }

    public void setOnRenderListListener(OnRenderListListener onRenderListListener) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mOnRenderListListener = onRenderListListener;
    }

    /* access modifiers changed from: package-private */
    public void addOnBeforeFinalizeFilterListener(OnBeforeFinalizeFilterListener onBeforeFinalizeFilterListener) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mOnBeforeFinalizeFilterListeners.add(onBeforeFinalizeFilterListener);
    }

    /* access modifiers changed from: package-private */
    public void addOnBeforeRenderListListener(OnBeforeRenderListListener onBeforeRenderListListener) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mOnBeforeRenderListListeners.add(onBeforeRenderListListener);
    }

    /* access modifiers changed from: package-private */
    public void addPreGroupFilter(NotifFilter notifFilter) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mNotifPreGroupFilters.add(notifFilter);
        notifFilter.setInvalidationListener(new Pluggable.PluggableListener() { // from class: com.android.systemui.statusbar.notification.collection.ShadeListBuilder$$ExternalSyntheticLambda1
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable.PluggableListener
            public final void onPluggableInvalidated(Object obj) {
                ShadeListBuilder.this.onPreGroupFilterInvalidated((NotifFilter) obj);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void addFinalizeFilter(NotifFilter notifFilter) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mNotifFinalizeFilters.add(notifFilter);
        notifFilter.setInvalidationListener(new Pluggable.PluggableListener() { // from class: com.android.systemui.statusbar.notification.collection.ShadeListBuilder$$ExternalSyntheticLambda0
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable.PluggableListener
            public final void onPluggableInvalidated(Object obj) {
                ShadeListBuilder.this.onFinalizeFilterInvalidated((NotifFilter) obj);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void addPromoter(NotifPromoter notifPromoter) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mNotifPromoters.add(notifPromoter);
        notifPromoter.setInvalidationListener(new Pluggable.PluggableListener() { // from class: com.android.systemui.statusbar.notification.collection.ShadeListBuilder$$ExternalSyntheticLambda2
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable.PluggableListener
            public final void onPluggableInvalidated(Object obj) {
                ShadeListBuilder.this.onPromoterInvalidated((NotifPromoter) obj);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void setSectioners(List<NotifSectioner> list) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mNotifSections.clear();
        for (NotifSectioner notifSectioner : list) {
            List<NotifSection> list2 = this.mNotifSections;
            list2.add(new NotifSection(notifSectioner, list2.size()));
            notifSectioner.setInvalidationListener(new Pluggable.PluggableListener() { // from class: com.android.systemui.statusbar.notification.collection.ShadeListBuilder$$ExternalSyntheticLambda3
                @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable.PluggableListener
                public final void onPluggableInvalidated(Object obj) {
                    ShadeListBuilder.this.onNotifSectionInvalidated((NotifSectioner) obj);
                }
            });
        }
        List<NotifSection> list3 = this.mNotifSections;
        list3.add(new NotifSection(DEFAULT_SECTIONER, list3.size()));
    }

    /* access modifiers changed from: package-private */
    public void setNotifStabilityManager(NotifStabilityManager notifStabilityManager) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        if (this.mNotifStabilityManager == null) {
            this.mNotifStabilityManager = notifStabilityManager;
            notifStabilityManager.setInvalidationListener(new Pluggable.PluggableListener() { // from class: com.android.systemui.statusbar.notification.collection.ShadeListBuilder$$ExternalSyntheticLambda4
                @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable.PluggableListener
                public final void onPluggableInvalidated(Object obj) {
                    ShadeListBuilder.this.onReorderingAllowedInvalidated((NotifStabilityManager) obj);
                }
            });
            return;
        }
        throw new IllegalStateException("Attempting to set the NotifStabilityManager more than once. There should only be one visual stability manager. Manager is being set by " + this.mNotifStabilityManager.getName() + " and " + notifStabilityManager.getName());
    }

    /* access modifiers changed from: package-private */
    public List<ListEntry> getShadeList() {
        Assert.isMainThread();
        return this.mReadOnlyNotifList;
    }

    /* access modifiers changed from: private */
    public void onPreGroupFilterInvalidated(NotifFilter notifFilter) {
        Assert.isMainThread();
        this.mLogger.logPreGroupFilterInvalidated(notifFilter.getName(), this.mPipelineState.getState());
        rebuildListIfBefore(3);
    }

    /* access modifiers changed from: private */
    public void onReorderingAllowedInvalidated(NotifStabilityManager notifStabilityManager) {
        Assert.isMainThread();
        this.mLogger.logReorderingAllowedInvalidated(notifStabilityManager.getName(), this.mPipelineState.getState());
        rebuildListIfBefore(4);
    }

    /* access modifiers changed from: private */
    public void onPromoterInvalidated(NotifPromoter notifPromoter) {
        Assert.isMainThread();
        this.mLogger.logPromoterInvalidated(notifPromoter.getName(), this.mPipelineState.getState());
        rebuildListIfBefore(5);
    }

    /* access modifiers changed from: private */
    public void onNotifSectionInvalidated(NotifSectioner notifSectioner) {
        Assert.isMainThread();
        this.mLogger.logNotifSectionInvalidated(notifSectioner.getName(), this.mPipelineState.getState());
        rebuildListIfBefore(7);
    }

    /* access modifiers changed from: private */
    public void onFinalizeFilterInvalidated(NotifFilter notifFilter) {
        Assert.isMainThread();
        this.mLogger.logFinalizeFilterInvalidated(notifFilter.getName(), this.mPipelineState.getState());
        rebuildListIfBefore(8);
    }

    /* access modifiers changed from: private */
    public void buildList() {
        this.mPipelineState.requireIsBefore(1);
        this.mPipelineState.setState(1);
        this.mPipelineState.incrementTo(2);
        resetNotifs();
        onBeginRun();
        this.mPipelineState.incrementTo(3);
        filterNotifs(this.mAllEntries, this.mNotifList, this.mNotifPreGroupFilters);
        this.mPipelineState.incrementTo(4);
        groupNotifs(this.mNotifList, this.mNewNotifList);
        applyNewNotifList();
        pruneIncompleteGroups(this.mNotifList);
        dispatchOnBeforeTransformGroups(this.mReadOnlyNotifList);
        this.mPipelineState.incrementTo(5);
        promoteNotifs(this.mNotifList);
        pruneIncompleteGroups(this.mNotifList);
        this.mPipelineState.incrementTo(6);
        stabilizeGroupingNotifs(this.mNotifList);
        dispatchOnBeforeSort(this.mReadOnlyNotifList);
        this.mPipelineState.incrementTo(7);
        sortList();
        dispatchOnBeforeFinalizeFilter(this.mReadOnlyNotifList);
        this.mPipelineState.incrementTo(8);
        filterNotifs(this.mNotifList, this.mNewNotifList, this.mNotifFinalizeFilters);
        applyNewNotifList();
        pruneIncompleteGroups(this.mNotifList);
        this.mPipelineState.incrementTo(9);
        logChanges();
        freeEmptyGroups();
        cleanupPluggables();
        dispatchOnBeforeRenderList(this.mReadOnlyNotifList);
        OnRenderListListener onRenderListListener = this.mOnRenderListListener;
        if (onRenderListListener != null) {
            onRenderListListener.onRenderList(this.mReadOnlyNotifList);
        }
        this.mLogger.logEndBuildList(this.mIterationCount, this.mReadOnlyNotifList.size(), countChildren(this.mReadOnlyNotifList));
        if (this.mIterationCount % 10 == 0) {
            this.mLogger.logFinalList(this.mNotifList);
        }
        this.mPipelineState.setState(0);
        this.mIterationCount++;
    }

    private void applyNewNotifList() {
        this.mNotifList.clear();
        List<ListEntry> list = this.mNotifList;
        this.mNotifList = this.mNewNotifList;
        this.mNewNotifList = list;
        List<ListEntry> list2 = this.mReadOnlyNotifList;
        this.mReadOnlyNotifList = this.mReadOnlyNewNotifList;
        this.mReadOnlyNewNotifList = list2;
    }

    private void resetNotifs() {
        for (GroupEntry groupEntry : this.mGroups.values()) {
            groupEntry.beginNewAttachState();
            groupEntry.clearChildren();
            groupEntry.setSummary(null);
        }
        for (NotificationEntry notificationEntry : this.mAllEntries) {
            notificationEntry.beginNewAttachState();
            if (notificationEntry.mFirstAddedIteration == -1) {
                notificationEntry.mFirstAddedIteration = this.mIterationCount;
            }
        }
        this.mNotifList.clear();
    }

    private void filterNotifs(Collection<? extends ListEntry> collection, List<ListEntry> list, List<NotifFilter> list2) {
        long uptimeMillis = this.mSystemClock.uptimeMillis();
        for (ListEntry listEntry : collection) {
            if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                NotificationEntry representativeEntry = groupEntry.getRepresentativeEntry();
                if (applyFilters(representativeEntry, uptimeMillis, list2)) {
                    groupEntry.setSummary(null);
                    annulAddition(representativeEntry);
                }
                List<NotificationEntry> rawChildren = groupEntry.getRawChildren();
                for (int size = rawChildren.size() - 1; size >= 0; size--) {
                    NotificationEntry notificationEntry = rawChildren.get(size);
                    if (applyFilters(notificationEntry, uptimeMillis, list2)) {
                        rawChildren.remove(notificationEntry);
                        annulAddition(notificationEntry);
                    }
                }
                list.add(groupEntry);
            } else if (applyFilters((NotificationEntry) listEntry, uptimeMillis, list2)) {
                annulAddition(listEntry);
            } else {
                list.add(listEntry);
            }
        }
    }

    private void groupNotifs(List<ListEntry> list, List<ListEntry> list2) {
        Iterator<ListEntry> it = list.iterator();
        while (it.hasNext()) {
            NotificationEntry notificationEntry = (NotificationEntry) it.next();
            if (notificationEntry.getSbn().isGroup()) {
                String groupKey = notificationEntry.getSbn().getGroupKey();
                GroupEntry groupEntry = this.mGroups.get(groupKey);
                if (groupEntry == null) {
                    groupEntry = new GroupEntry(groupKey, this.mSystemClock.uptimeMillis());
                    groupEntry.mFirstAddedIteration = this.mIterationCount;
                    this.mGroups.put(groupKey, groupEntry);
                }
                if (groupEntry.getParent() == null) {
                    groupEntry.setParent(GroupEntry.ROOT_ENTRY);
                    list2.add(groupEntry);
                }
                notificationEntry.setParent(groupEntry);
                if (notificationEntry.getSbn().getNotification().isGroupSummary()) {
                    NotificationEntry summary = groupEntry.getSummary();
                    if (summary == null) {
                        groupEntry.setSummary(notificationEntry);
                    } else {
                        this.mLogger.logDuplicateSummary(this.mIterationCount, groupEntry.getKey(), summary.getKey(), notificationEntry.getKey());
                        if (notificationEntry.getSbn().getPostTime() > summary.getSbn().getPostTime()) {
                            groupEntry.setSummary(notificationEntry);
                            annulAddition(summary, list2);
                        } else {
                            annulAddition(notificationEntry, list2);
                        }
                    }
                } else {
                    groupEntry.addChild(notificationEntry);
                }
            } else {
                String key = notificationEntry.getKey();
                if (this.mGroups.containsKey(key)) {
                    this.mLogger.logDuplicateTopLevelKey(this.mIterationCount, key);
                } else {
                    notificationEntry.setParent(GroupEntry.ROOT_ENTRY);
                    list2.add(notificationEntry);
                }
            }
        }
    }

    private void stabilizeGroupingNotifs(List<ListEntry> list) {
        if (this.mNotifStabilityManager != null) {
            int i = 0;
            while (i < list.size()) {
                ListEntry listEntry = list.get(i);
                if (listEntry instanceof GroupEntry) {
                    GroupEntry groupEntry = (GroupEntry) listEntry;
                    List<NotificationEntry> rawChildren = groupEntry.getRawChildren();
                    int i2 = 0;
                    while (i2 < groupEntry.getChildren().size()) {
                        if (maybeSuppressGroupChange(rawChildren.get(i2), list)) {
                            rawChildren.remove(i2);
                            i2--;
                        }
                        i2++;
                    }
                } else if (maybeSuppressGroupChange(listEntry.getRepresentativeEntry(), list)) {
                    list.remove(i);
                    i--;
                }
                i++;
            }
        }
    }

    private boolean maybeSuppressGroupChange(NotificationEntry notificationEntry, List<ListEntry> list) {
        GroupEntry parent;
        GroupEntry parent2;
        if (!notificationEntry.wasAttachedInPreviousPass() || (parent = notificationEntry.getPreviousAttachState().getParent()) == (parent2 = notificationEntry.getParent()) || this.mNotifStabilityManager.isGroupChangeAllowed(notificationEntry.getRepresentativeEntry())) {
            return false;
        }
        notificationEntry.getAttachState().getSuppressedChanges().setParent(parent2);
        notificationEntry.setParent(parent);
        if (parent == GroupEntry.ROOT_ENTRY) {
            list.add(notificationEntry);
            return true;
        } else if (parent == null) {
            return true;
        } else {
            parent.addChild(notificationEntry);
            if (this.mGroups.containsKey(parent.getKey())) {
                return true;
            }
            this.mGroups.put(parent.getKey(), parent);
            return true;
        }
    }

    private void promoteNotifs(List<ListEntry> list) {
        for (int i = 0; i < list.size(); i++) {
            ListEntry listEntry = list.get(i);
            if (listEntry instanceof GroupEntry) {
                ((GroupEntry) listEntry).getRawChildren().removeIf(new Predicate(list) { // from class: com.android.systemui.statusbar.notification.collection.ShadeListBuilder$$ExternalSyntheticLambda7
                    public final /* synthetic */ List f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return ShadeListBuilder.this.lambda$promoteNotifs$0(this.f$1, (NotificationEntry) obj);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$promoteNotifs$0(List list, NotificationEntry notificationEntry) {
        boolean applyTopLevelPromoters = applyTopLevelPromoters(notificationEntry);
        if (applyTopLevelPromoters) {
            notificationEntry.setParent(GroupEntry.ROOT_ENTRY);
            list.add(notificationEntry);
        }
        return applyTopLevelPromoters;
    }

    private void pruneIncompleteGroups(List<ListEntry> list) {
        NotifStabilityManager notifStabilityManager;
        int i = 0;
        while (i < list.size()) {
            ListEntry listEntry = list.get(i);
            if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                List<NotificationEntry> rawChildren = groupEntry.getRawChildren();
                if (groupEntry.getSummary() != null && rawChildren.size() == 0) {
                    list.remove(i);
                    i--;
                    NotificationEntry summary = groupEntry.getSummary();
                    summary.setParent(GroupEntry.ROOT_ENTRY);
                    list.add(summary);
                    groupEntry.setSummary(null);
                    annulAddition(groupEntry, list);
                } else if (groupEntry.getSummary() == null || rawChildren.size() < 2) {
                    if (groupEntry.getSummary() == null || !groupEntry.wasAttachedInPreviousPass() || (notifStabilityManager = this.mNotifStabilityManager) == null || notifStabilityManager.isGroupChangeAllowed(groupEntry.getSummary())) {
                        list.remove(i);
                        i--;
                        if (groupEntry.getSummary() != null) {
                            ListEntry summary2 = groupEntry.getSummary();
                            groupEntry.setSummary(null);
                            annulAddition(summary2, list);
                        }
                        for (int i2 = 0; i2 < rawChildren.size(); i2++) {
                            NotificationEntry notificationEntry = rawChildren.get(i2);
                            notificationEntry.setParent(GroupEntry.ROOT_ENTRY);
                            list.add(notificationEntry);
                        }
                        rawChildren.clear();
                        annulAddition(groupEntry, list);
                    } else {
                        groupEntry.getAttachState().getSuppressedChanges().setWasPruneSuppressed(true);
                    }
                }
            }
            i++;
        }
    }

    private void annulAddition(ListEntry listEntry, List<ListEntry> list) {
        if (listEntry.getParent() == null || listEntry.mFirstAddedIteration == -1) {
            throw new IllegalStateException("Cannot nullify addition of " + listEntry.getKey() + ": no such addition. (" + listEntry.getParent() + " " + listEntry.mFirstAddedIteration + ")");
        } else if (listEntry.getParent() != GroupEntry.ROOT_ENTRY || !list.contains(listEntry)) {
            if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                if (groupEntry.getSummary() != null) {
                    throw new IllegalStateException("Cannot nullify group " + groupEntry.getKey() + ": summary is not null");
                } else if (!groupEntry.getChildren().isEmpty()) {
                    throw new IllegalStateException("Cannot nullify group " + groupEntry.getKey() + ": still has children");
                }
            } else if ((listEntry instanceof NotificationEntry) && (listEntry == listEntry.getParent().getSummary() || listEntry.getParent().getChildren().contains(listEntry))) {
                throw new IllegalStateException("Cannot nullify addition of child " + listEntry.getKey() + ": it's still attached to its parent.");
            }
            annulAddition(listEntry);
        } else {
            throw new IllegalStateException("Cannot nullify addition of " + listEntry.getKey() + ": it's still in the shade list.");
        }
    }

    private void annulAddition(ListEntry listEntry) {
        listEntry.setParent(null);
        listEntry.getAttachState().setSection(null);
        listEntry.getAttachState().setPromoter(null);
        if (listEntry.mFirstAddedIteration == this.mIterationCount) {
            listEntry.mFirstAddedIteration = -1;
        }
    }

    private void sortList() {
        for (ListEntry listEntry : this.mNotifList) {
            NotifSection applySections = applySections(listEntry);
            if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                for (NotificationEntry notificationEntry : groupEntry.getChildren()) {
                    notificationEntry.getAttachState().setSection(applySections);
                }
                groupEntry.sortChildren(sChildComparator);
            }
        }
        this.mNotifList.sort(this.mTopLevelComparator);
    }

    private void freeEmptyGroups() {
        this.mGroups.values().removeIf(ShadeListBuilder$$ExternalSyntheticLambda8.INSTANCE);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$freeEmptyGroups$1(GroupEntry groupEntry) {
        return groupEntry.getSummary() == null && groupEntry.getChildren().isEmpty();
    }

    private void logChanges() {
        for (NotificationEntry notificationEntry : this.mAllEntries) {
            logAttachStateChanges(notificationEntry);
        }
        for (GroupEntry groupEntry : this.mGroups.values()) {
            logAttachStateChanges(groupEntry);
        }
    }

    private void logAttachStateChanges(ListEntry listEntry) {
        ListAttachState attachState = listEntry.getAttachState();
        ListAttachState previousAttachState = listEntry.getPreviousAttachState();
        if (!Objects.equals(attachState, previousAttachState)) {
            this.mLogger.logEntryAttachStateChanged(this.mIterationCount, listEntry.getKey(), previousAttachState.getParent(), attachState.getParent());
            if (attachState.getParent() != previousAttachState.getParent()) {
                this.mLogger.logParentChanged(this.mIterationCount, previousAttachState.getParent(), attachState.getParent());
            }
            if (attachState.getSuppressedChanges().getParent() != null) {
                this.mLogger.logParentChangeSuppressed(this.mIterationCount, attachState.getSuppressedChanges().getParent(), attachState.getParent());
            }
            if (attachState.getSuppressedChanges().getWasPruneSuppressed()) {
                this.mLogger.logGroupPruningSuppressed(this.mIterationCount, attachState.getParent());
            }
            if (attachState.getExcludingFilter() != previousAttachState.getExcludingFilter()) {
                this.mLogger.logFilterChanged(this.mIterationCount, previousAttachState.getExcludingFilter(), attachState.getExcludingFilter());
            }
            boolean z = attachState.getParent() == null && previousAttachState.getParent() != null;
            if (!z && attachState.getPromoter() != previousAttachState.getPromoter()) {
                this.mLogger.logPromoterChanged(this.mIterationCount, previousAttachState.getPromoter(), attachState.getPromoter());
            }
            if (!z && attachState.getSection() != previousAttachState.getSection()) {
                this.mLogger.logSectionChanged(this.mIterationCount, previousAttachState.getSection(), attachState.getSection());
            }
            if (attachState.getSuppressedChanges().getSection() != null) {
                this.mLogger.logSectionChangeSuppressed(this.mIterationCount, attachState.getSuppressedChanges().getSection(), attachState.getSection());
            }
        }
    }

    private void onBeginRun() {
        NotifStabilityManager notifStabilityManager = this.mNotifStabilityManager;
        if (notifStabilityManager != null) {
            notifStabilityManager.onBeginRun();
        }
    }

    private void cleanupPluggables() {
        callOnCleanup(this.mNotifPreGroupFilters);
        callOnCleanup(this.mNotifPromoters);
        callOnCleanup(this.mNotifFinalizeFilters);
        callOnCleanup(this.mNotifComparators);
        for (int i = 0; i < this.mNotifSections.size(); i++) {
            this.mNotifSections.get(i).getSectioner().onCleanup();
        }
        NotifStabilityManager notifStabilityManager = this.mNotifStabilityManager;
        if (notifStabilityManager != null) {
            callOnCleanup(List.of(notifStabilityManager));
        }
    }

    private void callOnCleanup(List<? extends Pluggable<?>> list) {
        for (int i = 0; i < list.size(); i++) {
            ((Pluggable) list.get(i)).onCleanup();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ int lambda$new$2(ListEntry listEntry, ListEntry listEntry2) {
        NotifSection section = listEntry.getSection();
        Objects.requireNonNull(section);
        int index = section.getIndex();
        NotifSection section2 = listEntry2.getSection();
        Objects.requireNonNull(section2);
        int compare = Integer.compare(index, section2.getIndex());
        if (compare == 0) {
            int i = 0;
            while (i < this.mNotifComparators.size() && (compare = this.mNotifComparators.get(i).compare(listEntry, listEntry2)) == 0) {
                i++;
            }
        }
        NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
        NotificationEntry representativeEntry2 = listEntry2.getRepresentativeEntry();
        if (compare == 0) {
            compare = representativeEntry.getRanking().getRank() - representativeEntry2.getRanking().getRank();
        }
        return compare == 0 ? Long.compare(representativeEntry2.getSbn().getNotification().when, representativeEntry.getSbn().getNotification().when) : compare;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ int lambda$static$3(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        int rank = notificationEntry.getRanking().getRank() - notificationEntry2.getRanking().getRank();
        return rank == 0 ? Long.compare(notificationEntry2.getSbn().getNotification().when, notificationEntry.getSbn().getNotification().when) : rank;
    }

    private boolean applyFilters(NotificationEntry notificationEntry, long j, List<NotifFilter> list) {
        NotifFilter findRejectingFilter = findRejectingFilter(notificationEntry, j, list);
        notificationEntry.getAttachState().setExcludingFilter(findRejectingFilter);
        if (findRejectingFilter != null) {
            notificationEntry.resetInitializationTime();
        }
        return findRejectingFilter != null;
    }

    private static NotifFilter findRejectingFilter(NotificationEntry notificationEntry, long j, List<NotifFilter> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            NotifFilter notifFilter = list.get(i);
            if (notifFilter.shouldFilterOut(notificationEntry, j)) {
                return notifFilter;
            }
        }
        return null;
    }

    private boolean applyTopLevelPromoters(NotificationEntry notificationEntry) {
        NotifPromoter findPromoter = findPromoter(notificationEntry);
        notificationEntry.getAttachState().setPromoter(findPromoter);
        return findPromoter != null;
    }

    private NotifPromoter findPromoter(NotificationEntry notificationEntry) {
        for (int i = 0; i < this.mNotifPromoters.size(); i++) {
            NotifPromoter notifPromoter = this.mNotifPromoters.get(i);
            if (notifPromoter.shouldPromoteToTopLevel(notificationEntry)) {
                return notifPromoter;
            }
        }
        return null;
    }

    private NotifSection applySections(ListEntry listEntry) {
        NotifSection findSection = findSection(listEntry);
        ListAttachState previousAttachState = listEntry.getPreviousAttachState();
        if (this.mNotifStabilityManager != null && listEntry.wasAttachedInPreviousPass() && findSection != previousAttachState.getSection() && !this.mNotifStabilityManager.isSectionChangeAllowed(listEntry.getRepresentativeEntry())) {
            listEntry.getAttachState().getSuppressedChanges().setSection(findSection);
            findSection = previousAttachState.getSection();
        }
        listEntry.getAttachState().setSection(findSection);
        return findSection;
    }

    private NotifSection findSection(ListEntry listEntry) {
        for (int i = 0; i < this.mNotifSections.size(); i++) {
            NotifSection notifSection = this.mNotifSections.get(i);
            if (notifSection.getSectioner().isInSection(listEntry)) {
                return notifSection;
            }
        }
        throw new RuntimeException("Missing default sectioner!");
    }

    private void rebuildListIfBefore(int i) {
        this.mPipelineState.requireIsBefore(i);
        if (this.mPipelineState.is(0)) {
            buildList();
        }
    }

    private static int countChildren(List<ListEntry> list) {
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            ListEntry listEntry = list.get(i2);
            if (listEntry instanceof GroupEntry) {
                i += ((GroupEntry) listEntry).getChildren().size();
            }
        }
        return i;
    }

    private void dispatchOnBeforeTransformGroups(List<ListEntry> list) {
        for (int i = 0; i < this.mOnBeforeTransformGroupsListeners.size(); i++) {
            this.mOnBeforeTransformGroupsListeners.get(i).onBeforeTransformGroups(list);
        }
    }

    private void dispatchOnBeforeSort(List<ListEntry> list) {
        for (int i = 0; i < this.mOnBeforeSortListeners.size(); i++) {
            this.mOnBeforeSortListeners.get(i).onBeforeSort(list);
        }
    }

    private void dispatchOnBeforeFinalizeFilter(List<ListEntry> list) {
        for (int i = 0; i < this.mOnBeforeFinalizeFilterListeners.size(); i++) {
            this.mOnBeforeFinalizeFilterListeners.get(i).onBeforeFinalizeFilter(list);
        }
    }

    private void dispatchOnBeforeRenderList(List<ListEntry> list) {
        for (int i = 0; i < this.mOnBeforeRenderListListeners.size(); i++) {
            this.mOnBeforeRenderListListeners.get(i).onBeforeRenderList(list);
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("\tShadeListBuilder shade notifications:");
        if (getShadeList().size() == 0) {
            printWriter.println("\t\t None");
        }
        printWriter.println(ListDumper.dumpTree(getShadeList(), this.mInteractionTracker, true, "\t\t"));
    }
}
