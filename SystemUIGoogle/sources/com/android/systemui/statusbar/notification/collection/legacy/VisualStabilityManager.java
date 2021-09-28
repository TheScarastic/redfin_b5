package com.android.systemui.statusbar.notification.collection.legacy;

import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import androidx.collection.ArraySet;
import com.android.systemui.Dumpable;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.VisibilityLocationProvider;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class VisualStabilityManager implements OnHeadsUpChangedListener, Dumpable {
    private boolean mGroupChangedAllowed;
    private final Handler mHandler;
    private boolean mIsTemporaryReorderingAllowed;
    private boolean mPanelExpanded;
    private boolean mPulsing;
    private boolean mReorderingAllowed;
    private boolean mScreenOn;
    private long mTemporaryReorderingStart;
    private VisibilityLocationProvider mVisibilityLocationProvider;
    final WakefulnessLifecycle.Observer mWakefulnessObserver;
    private final ArrayList<Callback> mReorderingAllowedCallbacks = new ArrayList<>();
    private final ArraySet<Callback> mPersistentReorderingCallbacks = new ArraySet<>();
    private final ArrayList<Callback> mGroupChangesAllowedCallbacks = new ArrayList<>();
    private final ArraySet<Callback> mPersistentGroupCallbacks = new ArraySet<>();
    private ArraySet<View> mAllowedReorderViews = new ArraySet<>();
    private ArraySet<NotificationEntry> mLowPriorityReorderingViews = new ArraySet<>();
    private ArraySet<View> mAddedChildren = new ArraySet<>();
    private final Runnable mOnTemporaryReorderingExpired = new Runnable() { // from class: com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            VisualStabilityManager.this.lambda$new$0();
        }
    };

    /* loaded from: classes.dex */
    public interface Callback {
        void onChangeAllowed();
    }

    public VisualStabilityManager(NotificationEntryManager notificationEntryManager, Handler handler, StatusBarStateController statusBarStateController, WakefulnessLifecycle wakefulnessLifecycle) {
        AnonymousClass3 r0 = new WakefulnessLifecycle.Observer() { // from class: com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager.3
            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onFinishedGoingToSleep() {
                VisualStabilityManager.this.setScreenOn(false);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onStartedWakingUp() {
                VisualStabilityManager.this.setScreenOn(true);
            }
        };
        this.mWakefulnessObserver = r0;
        this.mHandler = handler;
        if (notificationEntryManager != null) {
            notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager.1
                @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                public void onPreEntryUpdated(NotificationEntry notificationEntry) {
                    if (notificationEntry.isAmbient() != notificationEntry.getRow().isLowPriority()) {
                        VisualStabilityManager.this.mLowPriorityReorderingViews.add(notificationEntry);
                    }
                }
            });
        }
        if (statusBarStateController != null) {
            setPulsing(statusBarStateController.isPulsing());
            statusBarStateController.addCallback(new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager.2
                @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
                public void onPulsingChanged(boolean z) {
                    VisualStabilityManager.this.setPulsing(z);
                }

                @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
                public void onExpandedChanged(boolean z) {
                    VisualStabilityManager.this.setPanelExpanded(z);
                }
            });
        }
        if (wakefulnessLifecycle != null) {
            wakefulnessLifecycle.addObserver(r0);
        }
    }

    public void addReorderingAllowedCallback(Callback callback, boolean z) {
        if (z) {
            this.mPersistentReorderingCallbacks.add(callback);
        }
        if (!this.mReorderingAllowedCallbacks.contains(callback)) {
            this.mReorderingAllowedCallbacks.add(callback);
        }
    }

    public void addGroupChangesAllowedCallback(Callback callback, boolean z) {
        if (z) {
            this.mPersistentGroupCallbacks.add(callback);
        }
        if (!this.mGroupChangesAllowedCallbacks.contains(callback)) {
            this.mGroupChangesAllowedCallbacks.add(callback);
        }
    }

    /* access modifiers changed from: private */
    public void setScreenOn(boolean z) {
        this.mScreenOn = z;
        updateAllowedStates();
    }

    /* access modifiers changed from: private */
    public void setPanelExpanded(boolean z) {
        this.mPanelExpanded = z;
        updateAllowedStates();
    }

    /* access modifiers changed from: private */
    public void setPulsing(boolean z) {
        if (this.mPulsing != z) {
            this.mPulsing = z;
            updateAllowedStates();
        }
    }

    private void updateAllowedStates() {
        boolean z = true;
        boolean z2 = (!this.mScreenOn || !this.mPanelExpanded || this.mIsTemporaryReorderingAllowed) && !this.mPulsing;
        boolean z3 = z2 && !this.mReorderingAllowed;
        this.mReorderingAllowed = z2;
        if (z3) {
            notifyChangeAllowed(this.mReorderingAllowedCallbacks, this.mPersistentReorderingCallbacks);
        }
        boolean z4 = (!this.mScreenOn || !this.mPanelExpanded) && !this.mPulsing;
        if (!z4 || this.mGroupChangedAllowed) {
            z = false;
        }
        this.mGroupChangedAllowed = z4;
        if (z) {
            notifyChangeAllowed(this.mGroupChangesAllowedCallbacks, this.mPersistentGroupCallbacks);
        }
    }

    private void notifyChangeAllowed(ArrayList<Callback> arrayList, ArraySet<Callback> arraySet) {
        int i = 0;
        while (i < arrayList.size()) {
            Callback callback = arrayList.get(i);
            callback.onChangeAllowed();
            if (!arraySet.contains(callback)) {
                arrayList.remove(callback);
                i--;
            }
            i++;
        }
    }

    public boolean isReorderingAllowed() {
        return this.mReorderingAllowed;
    }

    public boolean areGroupChangesAllowed() {
        return this.mGroupChangedAllowed;
    }

    public boolean canReorderNotification(ExpandableNotificationRow expandableNotificationRow) {
        if (this.mReorderingAllowed || this.mAddedChildren.contains(expandableNotificationRow) || this.mLowPriorityReorderingViews.contains(expandableNotificationRow.getEntry())) {
            return true;
        }
        if (!this.mAllowedReorderViews.contains(expandableNotificationRow) || this.mVisibilityLocationProvider.isInVisibleLocation(expandableNotificationRow.getEntry())) {
            return false;
        }
        return true;
    }

    public void setVisibilityLocationProvider(VisibilityLocationProvider visibilityLocationProvider) {
        this.mVisibilityLocationProvider = visibilityLocationProvider;
    }

    public void onReorderingFinished() {
        this.mAllowedReorderViews.clear();
        this.mAddedChildren.clear();
        this.mLowPriorityReorderingViews.clear();
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        if (z) {
            this.mAllowedReorderViews.add(notificationEntry.getRow());
        }
    }

    public void temporarilyAllowReordering() {
        this.mHandler.removeCallbacks(this.mOnTemporaryReorderingExpired);
        this.mHandler.postDelayed(this.mOnTemporaryReorderingExpired, 1000);
        if (!this.mIsTemporaryReorderingAllowed) {
            this.mTemporaryReorderingStart = SystemClock.elapsedRealtime();
        }
        this.mIsTemporaryReorderingAllowed = true;
        updateAllowedStates();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mIsTemporaryReorderingAllowed = false;
        updateAllowedStates();
    }

    public void notifyViewAddition(View view) {
        this.mAddedChildren.add(view);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("VisualStabilityManager state:");
        printWriter.print("  mIsTemporaryReorderingAllowed=");
        printWriter.println(this.mIsTemporaryReorderingAllowed);
        printWriter.print("  mTemporaryReorderingStart=");
        printWriter.println(this.mTemporaryReorderingStart);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        printWriter.print("    Temporary reordering window has been open for ");
        printWriter.print(elapsedRealtime - (this.mIsTemporaryReorderingAllowed ? this.mTemporaryReorderingStart : elapsedRealtime));
        printWriter.println("ms");
        printWriter.println();
    }
}
