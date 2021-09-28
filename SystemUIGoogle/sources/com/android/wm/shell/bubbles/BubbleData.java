package com.android.wm.shell.bubbles;

import android.app.PendingIntent;
import android.content.Context;
import android.content.LocusId;
import android.content.pm.ShortcutInfo;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.R;
import com.android.wm.shell.bubbles.BubbleLogger;
import com.android.wm.shell.bubbles.Bubbles;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Predicate;
/* loaded from: classes2.dex */
public class BubbleData {
    private static final Comparator<Bubble> BUBBLES_BY_SORT_KEY_DESCENDING = Comparator.comparing(BubbleData$$ExternalSyntheticLambda6.INSTANCE).reversed();
    private final List<Bubble> mBubbles;
    private Bubbles.PendingIntentCanceledListener mCancelledListener;
    private final Context mContext;
    private int mCurrentUserId;
    private boolean mExpanded;
    private Listener mListener;
    private BubbleLogger mLogger;
    private final Executor mMainExecutor;
    private int mMaxBubbles;
    private int mMaxOverflowBubbles;
    private boolean mNeedsTrimming;
    private final BubbleOverflow mOverflow;
    private final List<Bubble> mOverflowBubbles;
    private final BubblePositioner mPositioner;
    private BubbleViewProvider mSelectedBubble;
    private boolean mShowingOverflow;
    private Update mStateChange;
    private Bubbles.SuppressionChangedListener mSuppressionListener;
    private final ArrayMap<LocusId, Bubble> mSuppressedBubbles = new ArrayMap<>();
    private final ArraySet<LocusId> mVisibleLocusIds = new ArraySet<>();
    private TimeSource mTimeSource = BubbleData$$ExternalSyntheticLambda0.INSTANCE;
    private HashMap<String, String> mSuppressedGroupKeys = new HashMap<>();
    private final HashMap<String, Bubble> mPendingBubbles = new HashMap<>();

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface Listener {
        void applyUpdate(Update update);
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface TimeSource {
        long currentTimeMillis();
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class Update {
        Bubble addedBubble;
        Bubble addedOverflowBubble;
        final List<Bubble> bubbles;
        boolean expanded;
        boolean expandedChanged;
        boolean orderChanged;
        final List<Bubble> overflowBubbles;
        final List<Pair<Bubble, Integer>> removedBubbles;
        Bubble removedOverflowBubble;
        BubbleViewProvider selectedBubble;
        boolean selectionChanged;
        Bubble suppressedBubble;
        boolean suppressedSummaryChanged;
        String suppressedSummaryGroup;
        Bubble unsuppressedBubble;
        Bubble updatedBubble;

        private Update(List<Bubble> list, List<Bubble> list2) {
            this.removedBubbles = new ArrayList();
            this.bubbles = Collections.unmodifiableList(list);
            this.overflowBubbles = Collections.unmodifiableList(list2);
        }

        boolean anythingChanged() {
            return (!this.expandedChanged && !this.selectionChanged && this.addedBubble == null && this.updatedBubble == null && this.removedBubbles.isEmpty() && this.addedOverflowBubble == null && this.removedOverflowBubble == null && !this.orderChanged && this.suppressedBubble == null && this.unsuppressedBubble == null && !this.suppressedSummaryChanged && this.suppressedSummaryGroup == null) ? false : true;
        }

        void bubbleRemoved(Bubble bubble, int i) {
            this.removedBubbles.add(new Pair<>(bubble, Integer.valueOf(i)));
        }
    }

    public BubbleData(Context context, BubbleLogger bubbleLogger, BubblePositioner bubblePositioner, Executor executor) {
        this.mContext = context;
        this.mLogger = bubbleLogger;
        this.mPositioner = bubblePositioner;
        this.mMainExecutor = executor;
        this.mOverflow = new BubbleOverflow(context, bubblePositioner);
        ArrayList arrayList = new ArrayList();
        this.mBubbles = arrayList;
        ArrayList arrayList2 = new ArrayList();
        this.mOverflowBubbles = arrayList2;
        this.mStateChange = new Update(arrayList, arrayList2);
        this.mMaxBubbles = bubblePositioner.getMaxBubbles();
        this.mMaxOverflowBubbles = context.getResources().getInteger(R.integer.bubbles_max_overflow);
    }

    public void setSuppressionChangedListener(Bubbles.SuppressionChangedListener suppressionChangedListener) {
        this.mSuppressionListener = suppressionChangedListener;
    }

    public void setPendingIntentCancelledListener(Bubbles.PendingIntentCanceledListener pendingIntentCanceledListener) {
        this.mCancelledListener = pendingIntentCanceledListener;
    }

    public void onMaxBubblesChanged() {
        this.mMaxBubbles = this.mPositioner.getMaxBubbles();
        if (!this.mExpanded) {
            trim();
            dispatchPendingChanges();
            return;
        }
        this.mNeedsTrimming = true;
    }

    public boolean hasBubbles() {
        return !this.mBubbles.isEmpty();
    }

    public boolean hasOverflowBubbles() {
        return !this.mOverflowBubbles.isEmpty();
    }

    public boolean isExpanded() {
        return this.mExpanded;
    }

    public boolean hasAnyBubbleWithKey(String str) {
        return hasBubbleInStackWithKey(str) || hasOverflowBubbleWithKey(str);
    }

    public boolean hasBubbleInStackWithKey(String str) {
        return getBubbleInStackWithKey(str) != null;
    }

    public boolean hasOverflowBubbleWithKey(String str) {
        return getOverflowBubbleWithKey(str) != null;
    }

    public BubbleViewProvider getSelectedBubble() {
        return this.mSelectedBubble;
    }

    public BubbleOverflow getOverflow() {
        return this.mOverflow;
    }

    public List<Bubble> getActiveBubbles() {
        return Collections.unmodifiableList(this.mBubbles);
    }

    public void setExpanded(boolean z) {
        setExpandedInternal(z);
        dispatchPendingChanges();
    }

    public void setSelectedBubble(BubbleViewProvider bubbleViewProvider) {
        setSelectedBubbleInternal(bubbleViewProvider);
        dispatchPendingChanges();
    }

    /* access modifiers changed from: package-private */
    public void setShowingOverflow(boolean z) {
        this.mShowingOverflow = z;
    }

    /* access modifiers changed from: package-private */
    public boolean isShowingOverflow() {
        return this.mShowingOverflow && (isExpanded() || this.mPositioner.showingInTaskbar());
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.wm.shell.bubbles.Bubble getOrCreateBubble(com.android.wm.shell.bubbles.BubbleEntry r5, com.android.wm.shell.bubbles.Bubble r6) {
        /*
            r4 = this;
            if (r6 == 0) goto L_0x0007
            java.lang.String r0 = r6.getKey()
            goto L_0x000b
        L_0x0007:
            java.lang.String r0 = r5.getKey()
        L_0x000b:
            com.android.wm.shell.bubbles.Bubble r1 = r4.getBubbleInStackWithKey(r0)
            if (r1 != 0) goto L_0x003c
            com.android.wm.shell.bubbles.Bubble r1 = r4.getOverflowBubbleWithKey(r0)
            if (r1 == 0) goto L_0x001d
            java.util.List<com.android.wm.shell.bubbles.Bubble> r6 = r4.mOverflowBubbles
            r6.remove(r1)
            goto L_0x003c
        L_0x001d:
            java.util.HashMap<java.lang.String, com.android.wm.shell.bubbles.Bubble> r1 = r4.mPendingBubbles
            boolean r1 = r1.containsKey(r0)
            if (r1 == 0) goto L_0x002e
            java.util.HashMap<java.lang.String, com.android.wm.shell.bubbles.Bubble> r6 = r4.mPendingBubbles
            java.lang.Object r6 = r6.get(r0)
            com.android.wm.shell.bubbles.Bubble r6 = (com.android.wm.shell.bubbles.Bubble) r6
            goto L_0x003d
        L_0x002e:
            if (r5 == 0) goto L_0x003d
            com.android.wm.shell.bubbles.Bubble r6 = new com.android.wm.shell.bubbles.Bubble
            com.android.wm.shell.bubbles.Bubbles$SuppressionChangedListener r1 = r4.mSuppressionListener
            com.android.wm.shell.bubbles.Bubbles$PendingIntentCanceledListener r2 = r4.mCancelledListener
            java.util.concurrent.Executor r3 = r4.mMainExecutor
            r6.<init>(r5, r1, r2, r3)
            goto L_0x003d
        L_0x003c:
            r6 = r1
        L_0x003d:
            if (r5 == 0) goto L_0x0042
            r6.setEntry(r5)
        L_0x0042:
            java.util.HashMap<java.lang.String, com.android.wm.shell.bubbles.Bubble> r4 = r4.mPendingBubbles
            r4.put(r0, r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleData.getOrCreateBubble(com.android.wm.shell.bubbles.BubbleEntry, com.android.wm.shell.bubbles.Bubble):com.android.wm.shell.bubbles.Bubble");
    }

    /* access modifiers changed from: package-private */
    public void notificationEntryUpdated(Bubble bubble, boolean z, boolean z2) {
        this.mPendingBubbles.remove(bubble.getKey());
        Bubble bubbleInStackWithKey = getBubbleInStackWithKey(bubble.getKey());
        boolean z3 = z | (!bubble.isVisuallyInterruptive());
        if (bubbleInStackWithKey == null) {
            bubble.setSuppressFlyout(z3);
            doAdd(bubble);
            trim();
        } else {
            bubble.setSuppressFlyout(z3);
            doUpdate(bubble, !z3);
        }
        boolean z4 = false;
        if (bubble.shouldAutoExpand()) {
            bubble.setShouldAutoExpand(false);
            setSelectedBubbleInternal(bubble);
            if (!this.mExpanded) {
                setExpandedInternal(true);
            }
        }
        boolean z5 = this.mExpanded && this.mSelectedBubble == bubble;
        if (z5 || !z2 || !bubble.showInShade()) {
            z4 = true;
        }
        bubble.setSuppressNotification(z4);
        bubble.setShowDot(!z5);
        LocusId locusId = bubble.getLocusId();
        if (locusId != null) {
            boolean containsKey = this.mSuppressedBubbles.containsKey(locusId);
            if (containsKey && (!bubble.isSuppressed() || !bubble.isSuppressable())) {
                this.mSuppressedBubbles.remove(locusId);
                this.mStateChange.unsuppressedBubble = bubble;
            } else if (!containsKey && (bubble.isSuppressed() || (bubble.isSuppressable() && this.mVisibleLocusIds.contains(locusId)))) {
                this.mSuppressedBubbles.put(locusId, bubble);
                this.mStateChange.suppressedBubble = bubble;
            }
        }
        dispatchPendingChanges();
    }

    public void dismissBubbleWithKey(String str, int i) {
        doRemove(str, i);
        dispatchPendingChanges();
    }

    /* access modifiers changed from: package-private */
    public void addSummaryToSuppress(String str, String str2) {
        this.mSuppressedGroupKeys.put(str, str2);
        Update update = this.mStateChange;
        update.suppressedSummaryChanged = true;
        update.suppressedSummaryGroup = str;
        dispatchPendingChanges();
    }

    /* access modifiers changed from: package-private */
    public String getSummaryKey(String str) {
        return this.mSuppressedGroupKeys.get(str);
    }

    /* access modifiers changed from: package-private */
    public void removeSuppressedSummary(String str) {
        this.mSuppressedGroupKeys.remove(str);
        Update update = this.mStateChange;
        update.suppressedSummaryChanged = true;
        update.suppressedSummaryGroup = str;
        dispatchPendingChanges();
    }

    @VisibleForTesting
    public boolean isSummarySuppressed(String str) {
        return this.mSuppressedGroupKeys.containsKey(str);
    }

    public void removeBubblesWithInvalidShortcuts(String str, List<ShortcutInfo> list, int i) {
        HashSet hashSet = new HashSet();
        for (ShortcutInfo shortcutInfo : list) {
            hashSet.add(shortcutInfo.getId());
        }
        BubbleData$$ExternalSyntheticLambda9 bubbleData$$ExternalSyntheticLambda9 = new Predicate(str, hashSet) { // from class: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda9
            public final /* synthetic */ String f$0;
            public final /* synthetic */ Set f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return BubbleData.lambda$removeBubblesWithInvalidShortcuts$0(this.f$0, this.f$1, (Bubble) obj);
            }
        };
        BubbleData$$ExternalSyntheticLambda2 bubbleData$$ExternalSyntheticLambda2 = new Consumer(i) { // from class: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda2
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BubbleData.this.lambda$removeBubblesWithInvalidShortcuts$1(this.f$1, (Bubble) obj);
            }
        };
        performActionOnBubblesMatching(getBubbles(), bubbleData$$ExternalSyntheticLambda9, bubbleData$$ExternalSyntheticLambda2);
        performActionOnBubblesMatching(getOverflowBubbles(), bubbleData$$ExternalSyntheticLambda9, bubbleData$$ExternalSyntheticLambda2);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeBubblesWithInvalidShortcuts$0(String str, Set set, Bubble bubble) {
        boolean equals = str.equals(bubble.getPackageName());
        boolean hasMetadataShortcutId = bubble.hasMetadataShortcutId();
        if (!equals || !hasMetadataShortcutId) {
            return false;
        }
        boolean z = bubble.hasMetadataShortcutId() && bubble.getShortcutInfo() != null && bubble.getShortcutInfo().isEnabled() && set.contains(bubble.getShortcutInfo().getId());
        if (!equals || z) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$removeBubblesWithInvalidShortcuts$1(int i, Bubble bubble) {
        dismissBubbleWithKey(bubble.getKey(), i);
    }

    public void removeBubblesWithPackageName(String str, int i) {
        BubbleData$$ExternalSyntheticLambda8 bubbleData$$ExternalSyntheticLambda8 = new Predicate(str) { // from class: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda8
            public final /* synthetic */ String f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return BubbleData.lambda$removeBubblesWithPackageName$2(this.f$0, (Bubble) obj);
            }
        };
        BubbleData$$ExternalSyntheticLambda3 bubbleData$$ExternalSyntheticLambda3 = new Consumer(i) { // from class: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda3
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BubbleData.this.lambda$removeBubblesWithPackageName$3(this.f$1, (Bubble) obj);
            }
        };
        performActionOnBubblesMatching(getBubbles(), bubbleData$$ExternalSyntheticLambda8, bubbleData$$ExternalSyntheticLambda3);
        performActionOnBubblesMatching(getOverflowBubbles(), bubbleData$$ExternalSyntheticLambda8, bubbleData$$ExternalSyntheticLambda3);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeBubblesWithPackageName$2(String str, Bubble bubble) {
        return bubble.getPackageName().equals(str);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$removeBubblesWithPackageName$3(int i, Bubble bubble) {
        dismissBubbleWithKey(bubble.getKey(), i);
    }

    private void doAdd(Bubble bubble) {
        this.mBubbles.add(0, bubble);
        Update update = this.mStateChange;
        update.addedBubble = bubble;
        boolean z = true;
        if (this.mBubbles.size() <= 1) {
            z = false;
        }
        update.orderChanged = z;
        if (!isExpanded()) {
            setSelectedBubbleInternal(this.mBubbles.get(0));
        }
    }

    private void trim() {
        if (this.mBubbles.size() > this.mMaxBubbles) {
            int size = this.mBubbles.size() - this.mMaxBubbles;
            ArrayList arrayList = new ArrayList();
            this.mBubbles.stream().sorted(Comparator.comparingLong(BubbleData$$ExternalSyntheticLambda10.INSTANCE)).filter(new Predicate() { // from class: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda7
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return BubbleData.this.lambda$trim$4((Bubble) obj);
                }
            }).forEachOrdered(new Consumer(arrayList, size) { // from class: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda4
                public final /* synthetic */ ArrayList f$0;
                public final /* synthetic */ int f$1;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    BubbleData.lambda$trim$5(this.f$0, this.f$1, (Bubble) obj);
                }
            });
            arrayList.forEach(new Consumer() { // from class: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    BubbleData.this.lambda$trim$6((Bubble) obj);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$trim$4(Bubble bubble) {
        return !bubble.equals(this.mSelectedBubble);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$trim$5(ArrayList arrayList, int i, Bubble bubble) {
        if (arrayList.size() < i) {
            arrayList.add(bubble);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$trim$6(Bubble bubble) {
        doRemove(bubble.getKey(), 2);
    }

    private void doUpdate(Bubble bubble, boolean z) {
        this.mStateChange.updatedBubble = bubble;
        if (!isExpanded() && z) {
            int indexOf = this.mBubbles.indexOf(bubble);
            this.mBubbles.remove(bubble);
            this.mBubbles.add(0, bubble);
            this.mStateChange.orderChanged = indexOf != 0;
            setSelectedBubbleInternal(this.mBubbles.get(0));
        }
    }

    private void performActionOnBubblesMatching(List<Bubble> list, Predicate<Bubble> predicate, Consumer<Bubble> consumer) {
        ArrayList<Bubble> arrayList = new ArrayList();
        for (Bubble bubble : list) {
            if (predicate.test(bubble)) {
                arrayList.add(bubble);
            }
        }
        for (Bubble bubble2 : arrayList) {
            consumer.accept(bubble2);
        }
    }

    private void doRemove(String str, int i) {
        if (this.mPendingBubbles.containsKey(str)) {
            this.mPendingBubbles.remove(str);
        }
        int indexForKey = indexForKey(str);
        if (indexForKey != -1) {
            Bubble bubble = this.mBubbles.get(indexForKey);
            bubble.stopInflation();
            if (this.mBubbles.size() == 1) {
                if (!hasOverflowBubbles() || (!this.mPositioner.showingInTaskbar() && !isExpanded())) {
                    setExpandedInternal(false);
                    this.mSelectedBubble = null;
                } else {
                    setShowingOverflow(true);
                    setSelectedBubbleInternal(this.mOverflow);
                }
            }
            if (indexForKey < this.mBubbles.size() - 1) {
                this.mStateChange.orderChanged = true;
            }
            this.mBubbles.remove(indexForKey);
            this.mStateChange.bubbleRemoved(bubble, i);
            if (!isExpanded()) {
                this.mStateChange.orderChanged |= repackAll();
            }
            overflowBubble(i, bubble);
            if (Objects.equals(this.mSelectedBubble, bubble)) {
                setSelectedBubbleInternal(this.mBubbles.get(Math.min(indexForKey, this.mBubbles.size() - 1)));
            }
            maybeSendDeleteIntent(i, bubble);
        } else if (!hasOverflowBubbleWithKey(str)) {
        } else {
            if (i == 5 || i == 9 || i == 7 || i == 4 || i == 12 || i == 13 || i == 8) {
                Bubble overflowBubbleWithKey = getOverflowBubbleWithKey(str);
                if (overflowBubbleWithKey != null) {
                    overflowBubbleWithKey.stopInflation();
                }
                this.mLogger.logOverflowRemove(overflowBubbleWithKey, i);
                this.mOverflowBubbles.remove(overflowBubbleWithKey);
                this.mStateChange.bubbleRemoved(overflowBubbleWithKey, i);
                this.mStateChange.removedOverflowBubble = overflowBubbleWithKey;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void overflowBubble(int i, Bubble bubble) {
        if (bubble.getPendingIntentCanceled()) {
            return;
        }
        if (i == 2 || i == 1 || i == 15) {
            this.mLogger.logOverflowAdd(bubble, i);
            this.mOverflowBubbles.remove(bubble);
            this.mOverflowBubbles.add(0, bubble);
            this.mStateChange.addedOverflowBubble = bubble;
            bubble.stopInflation();
            if (this.mOverflowBubbles.size() == this.mMaxOverflowBubbles + 1) {
                List<Bubble> list = this.mOverflowBubbles;
                Bubble bubble2 = list.get(list.size() - 1);
                this.mStateChange.bubbleRemoved(bubble2, 11);
                this.mLogger.log(bubble, BubbleLogger.Event.BUBBLE_OVERFLOW_REMOVE_MAX_REACHED);
                this.mOverflowBubbles.remove(bubble2);
                this.mStateChange.removedOverflowBubble = bubble2;
            }
        }
    }

    public void dismissAll(int i) {
        if (!this.mBubbles.isEmpty()) {
            setExpandedInternal(false);
            setSelectedBubbleInternal(null);
            while (!this.mBubbles.isEmpty()) {
                doRemove(this.mBubbles.get(0).getKey(), i);
            }
            dispatchPendingChanges();
        }
    }

    public void onLocusVisibilityChanged(int i, LocusId locusId, boolean z) {
        Bubble bubbleInStackWithLocusId = getBubbleInStackWithLocusId(locusId);
        if (!z || (bubbleInStackWithLocusId != null && bubbleInStackWithLocusId.getTaskId() == i)) {
            this.mVisibleLocusIds.remove(locusId);
        } else {
            this.mVisibleLocusIds.add(locusId);
        }
        if (bubbleInStackWithLocusId != null) {
            boolean z2 = this.mSuppressedBubbles.get(locusId) != null;
            if (z && !z2 && bubbleInStackWithLocusId.isSuppressable() && i != bubbleInStackWithLocusId.getTaskId()) {
                this.mSuppressedBubbles.put(locusId, bubbleInStackWithLocusId);
                bubbleInStackWithLocusId.setSuppressBubble(true);
                this.mStateChange.suppressedBubble = bubbleInStackWithLocusId;
                dispatchPendingChanges();
            } else if (!z) {
                Bubble remove = this.mSuppressedBubbles.remove(locusId);
                if (remove != null) {
                    remove.setSuppressBubble(false);
                    this.mStateChange.unsuppressedBubble = remove;
                }
                dispatchPendingChanges();
            }
        }
    }

    public void clearOverflow() {
        while (!this.mOverflowBubbles.isEmpty()) {
            doRemove(this.mOverflowBubbles.get(0).getKey(), 8);
        }
        dispatchPendingChanges();
    }

    private void dispatchPendingChanges() {
        if (this.mListener != null && this.mStateChange.anythingChanged()) {
            this.mListener.applyUpdate(this.mStateChange);
        }
        this.mStateChange = new Update(this.mBubbles, this.mOverflowBubbles);
    }

    private void setSelectedBubbleInternal(BubbleViewProvider bubbleViewProvider) {
        if (this.mShowingOverflow || !Objects.equals(bubbleViewProvider, this.mSelectedBubble)) {
            boolean z = bubbleViewProvider != null && "Overflow".equals(bubbleViewProvider.getKey());
            if (bubbleViewProvider == null || this.mBubbles.contains(bubbleViewProvider) || this.mOverflowBubbles.contains(bubbleViewProvider) || z) {
                if (this.mExpanded && bubbleViewProvider != null && !z) {
                    ((Bubble) bubbleViewProvider).markAsAccessedAt(this.mTimeSource.currentTimeMillis());
                }
                this.mSelectedBubble = bubbleViewProvider;
                Update update = this.mStateChange;
                update.selectedBubble = bubbleViewProvider;
                update.selectionChanged = true;
                return;
            }
            Log.e("Bubbles", "Cannot select bubble which doesn't exist! (" + bubbleViewProvider + ") bubbles=" + this.mBubbles);
        }
    }

    /* access modifiers changed from: package-private */
    public void setCurrentUserId(int i) {
        this.mCurrentUserId = i;
    }

    /* access modifiers changed from: package-private */
    public void logBubbleEvent(BubbleViewProvider bubbleViewProvider, int i, String str, int i2, int i3, float f, float f2) {
        if (bubbleViewProvider == null) {
            this.mLogger.logStackUiChanged(str, i, i2, f, f2);
        } else if (!bubbleViewProvider.getKey().equals("Overflow")) {
            this.mLogger.logBubbleUiChanged((Bubble) bubbleViewProvider, str, i, i2, f, f2, i3);
        } else if (i == 3) {
            this.mLogger.logShowOverflow(str, this.mCurrentUserId);
        }
    }

    private void setExpandedInternal(boolean z) {
        if (this.mExpanded != z) {
            if (z) {
                if (!this.mBubbles.isEmpty() || this.mShowingOverflow) {
                    BubbleViewProvider bubbleViewProvider = this.mSelectedBubble;
                    if (bubbleViewProvider == null) {
                        Log.e("Bubbles", "Attempt to expand stack without selected bubble!");
                        return;
                    }
                    if (bubbleViewProvider instanceof Bubble) {
                        ((Bubble) bubbleViewProvider).markAsAccessedAt(this.mTimeSource.currentTimeMillis());
                    }
                    this.mStateChange.orderChanged |= repackAll();
                } else {
                    Log.e("Bubbles", "Attempt to expand stack when empty!");
                    return;
                }
            } else if (!this.mBubbles.isEmpty()) {
                this.mStateChange.orderChanged |= repackAll();
                if (this.mShowingOverflow) {
                    if (!this.mSelectedBubble.getKey().equals(this.mOverflow.getKey())) {
                        setSelectedBubbleInternal(this.mSelectedBubble);
                    } else {
                        setSelectedBubbleInternal(this.mBubbles.get(0));
                    }
                }
                if (this.mBubbles.indexOf(this.mSelectedBubble) > 0 && this.mBubbles.indexOf(this.mSelectedBubble) != 0) {
                    this.mBubbles.remove((Bubble) this.mSelectedBubble);
                    this.mBubbles.add(0, (Bubble) this.mSelectedBubble);
                    this.mStateChange.orderChanged = true;
                }
            }
            if (this.mNeedsTrimming) {
                this.mNeedsTrimming = false;
                trim();
            }
            this.mExpanded = z;
            Update update = this.mStateChange;
            update.expanded = z;
            update.expandedChanged = true;
        }
    }

    /* access modifiers changed from: private */
    public static long sortKey(Bubble bubble) {
        return bubble.getLastActivity();
    }

    private boolean repackAll() {
        if (this.mBubbles.isEmpty()) {
            return false;
        }
        ArrayList arrayList = new ArrayList(this.mBubbles.size());
        this.mBubbles.stream().sorted(BUBBLES_BY_SORT_KEY_DESCENDING).forEachOrdered(new Consumer(arrayList) { // from class: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda5
            public final /* synthetic */ List f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.add((Bubble) obj);
            }
        });
        if (arrayList.equals(this.mBubbles)) {
            return false;
        }
        this.mBubbles.clear();
        this.mBubbles.addAll(arrayList);
        return true;
    }

    private void maybeSendDeleteIntent(int i, Bubble bubble) {
        PendingIntent deleteIntent;
        if (i == 1 && (deleteIntent = bubble.getDeleteIntent()) != null) {
            try {
                deleteIntent.send();
            } catch (PendingIntent.CanceledException unused) {
                Log.w("Bubbles", "Failed to send delete intent for bubble with key: " + bubble.getKey());
            }
        }
    }

    private int indexForKey(String str) {
        for (int i = 0; i < this.mBubbles.size(); i++) {
            if (this.mBubbles.get(i).getKey().equals(str)) {
                return i;
            }
        }
        return -1;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public List<Bubble> getBubbles() {
        return Collections.unmodifiableList(this.mBubbles);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public List<Bubble> getOverflowBubbles() {
        return Collections.unmodifiableList(this.mOverflowBubbles);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Bubble getAnyBubbleWithkey(String str) {
        Bubble bubbleInStackWithKey = getBubbleInStackWithKey(str);
        return bubbleInStackWithKey == null ? getOverflowBubbleWithKey(str) : bubbleInStackWithKey;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Bubble getBubbleInStackWithKey(String str) {
        for (int i = 0; i < this.mBubbles.size(); i++) {
            Bubble bubble = this.mBubbles.get(i);
            if (bubble.getKey().equals(str)) {
                return bubble;
            }
        }
        return null;
    }

    private Bubble getBubbleInStackWithLocusId(LocusId locusId) {
        if (locusId == null) {
            return null;
        }
        for (int i = 0; i < this.mBubbles.size(); i++) {
            Bubble bubble = this.mBubbles.get(i);
            if (locusId.equals(bubble.getLocusId())) {
                return bubble;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public Bubble getBubbleWithView(View view) {
        for (int i = 0; i < this.mBubbles.size(); i++) {
            Bubble bubble = this.mBubbles.get(i);
            if (bubble.getIconView() != null && bubble.getIconView().equals(view)) {
                return bubble;
            }
        }
        return null;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Bubble getOverflowBubbleWithKey(String str) {
        for (int i = 0; i < this.mOverflowBubbles.size(); i++) {
            Bubble bubble = this.mOverflowBubbles.get(i);
            if (bubble.getKey().equals(str)) {
                return bubble;
            }
        }
        return null;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    void setTimeSource(TimeSource timeSource) {
        this.mTimeSource = timeSource;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    @VisibleForTesting
    public void setMaxOverflowBubbles(int i) {
        this.mMaxOverflowBubbles = i;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print("selected: ");
        BubbleViewProvider bubbleViewProvider = this.mSelectedBubble;
        printWriter.println(bubbleViewProvider != null ? bubbleViewProvider.getKey() : "null");
        printWriter.print("expanded: ");
        printWriter.println(this.mExpanded);
        printWriter.print("stack bubble count:    ");
        printWriter.println(this.mBubbles.size());
        for (Bubble bubble : this.mBubbles) {
            bubble.dump(fileDescriptor, printWriter, strArr);
        }
        printWriter.print("overflow bubble count:    ");
        printWriter.println(this.mOverflowBubbles.size());
        for (Bubble bubble2 : this.mOverflowBubbles) {
            bubble2.dump(fileDescriptor, printWriter, strArr);
        }
        printWriter.print("summaryKeys: ");
        printWriter.println(this.mSuppressedGroupKeys.size());
        Iterator<String> it = this.mSuppressedGroupKeys.keySet().iterator();
        while (it.hasNext()) {
            printWriter.println("   suppressing: " + it.next());
        }
    }
}
