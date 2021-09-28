package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class VisualStabilityCoordinator implements Coordinator {
    protected static final long ALLOW_SECTION_CHANGE_TIMEOUT = 500;
    private final DelayableExecutor mDelayableExecutor;
    private final HeadsUpManager mHeadsUpManager;
    private boolean mPanelExpanded;
    private boolean mPulsing;
    private boolean mReorderingAllowed;
    private boolean mScreenOn;
    private final StatusBarStateController mStatusBarStateController;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    private boolean mIsSuppressingGroupChange = false;
    private final Set<String> mEntriesWithSuppressedSectionChange = new HashSet();
    private Map<String, Runnable> mEntriesThatCanChangeSection = new HashMap();
    private final NotifStabilityManager mNotifStabilityManager = new NotifStabilityManager("VisualStabilityCoordinator") { // from class: com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator.1
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager
        public void onBeginRun() {
            VisualStabilityCoordinator.this.mIsSuppressingGroupChange = false;
            VisualStabilityCoordinator.this.mEntriesWithSuppressedSectionChange.clear();
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager
        public boolean isGroupChangeAllowed(NotificationEntry notificationEntry) {
            boolean z = VisualStabilityCoordinator.this.mReorderingAllowed || VisualStabilityCoordinator.this.mHeadsUpManager.isAlerting(notificationEntry.getKey());
            VisualStabilityCoordinator.access$076(VisualStabilityCoordinator.this, !z ? 1 : 0);
            return z;
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager
        public boolean isSectionChangeAllowed(NotificationEntry notificationEntry) {
            boolean z = VisualStabilityCoordinator.this.mReorderingAllowed || VisualStabilityCoordinator.this.mHeadsUpManager.isAlerting(notificationEntry.getKey()) || VisualStabilityCoordinator.this.mEntriesThatCanChangeSection.containsKey(notificationEntry.getKey());
            if (z) {
                VisualStabilityCoordinator.this.mEntriesWithSuppressedSectionChange.add(notificationEntry.getKey());
            }
            return z;
        }
    };
    final StatusBarStateController.StateListener mStatusBarStateControllerListener = new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator.2
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onPulsingChanged(boolean z) {
            VisualStabilityCoordinator.this.mPulsing = z;
            VisualStabilityCoordinator.this.updateAllowedStates();
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onExpandedChanged(boolean z) {
            VisualStabilityCoordinator.this.mPanelExpanded = z;
            VisualStabilityCoordinator.this.updateAllowedStates();
        }
    };
    final WakefulnessLifecycle.Observer mWakefulnessObserver = new WakefulnessLifecycle.Observer() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator.3
        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public void onFinishedGoingToSleep() {
            VisualStabilityCoordinator.this.mScreenOn = false;
            VisualStabilityCoordinator.this.updateAllowedStates();
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public void onStartedWakingUp() {
            VisualStabilityCoordinator.this.mScreenOn = true;
            VisualStabilityCoordinator.this.updateAllowedStates();
        }
    };

    /* JADX WARN: Type inference failed for: r2v2, types: [byte, boolean] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ boolean access$076(com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator r1, int r2) {
        /*
            boolean r0 = r1.mIsSuppressingGroupChange
            r2 = r2 | r0
            byte r2 = (byte) r2
            r1.mIsSuppressingGroupChange = r2
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator.access$076(com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator, int):boolean");
    }

    public VisualStabilityCoordinator(HeadsUpManager headsUpManager, WakefulnessLifecycle wakefulnessLifecycle, StatusBarStateController statusBarStateController, DelayableExecutor delayableExecutor) {
        this.mHeadsUpManager = headsUpManager;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mStatusBarStateController = statusBarStateController;
        this.mDelayableExecutor = delayableExecutor;
    }

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public void attach(NotifPipeline notifPipeline) {
        this.mWakefulnessLifecycle.addObserver(this.mWakefulnessObserver);
        boolean z = true;
        if (!(this.mWakefulnessLifecycle.getWakefulness() == 2 || this.mWakefulnessLifecycle.getWakefulness() == 1)) {
            z = false;
        }
        this.mScreenOn = z;
        this.mStatusBarStateController.addCallback(this.mStatusBarStateControllerListener);
        this.mPulsing = this.mStatusBarStateController.isPulsing();
        notifPipeline.setVisualStabilityManager(this.mNotifStabilityManager);
    }

    /* access modifiers changed from: private */
    public void updateAllowedStates() {
        boolean isReorderingAllowed = isReorderingAllowed();
        this.mReorderingAllowed = isReorderingAllowed;
        if (!isReorderingAllowed) {
            return;
        }
        if (this.mIsSuppressingGroupChange || isSuppressingSectionChange()) {
            this.mNotifStabilityManager.invalidateList();
        }
    }

    private boolean isSuppressingSectionChange() {
        return !this.mEntriesWithSuppressedSectionChange.isEmpty();
    }

    private boolean isReorderingAllowed() {
        return (!this.mScreenOn || !this.mPanelExpanded) && !this.mPulsing;
    }

    public void temporarilyAllowSectionChanges(NotificationEntry notificationEntry, long j) {
        String key = notificationEntry.getKey();
        boolean isSectionChangeAllowed = this.mNotifStabilityManager.isSectionChangeAllowed(notificationEntry);
        if (this.mEntriesThatCanChangeSection.containsKey(key)) {
            this.mEntriesThatCanChangeSection.get(key).run();
        }
        this.mEntriesThatCanChangeSection.put(key, this.mDelayableExecutor.executeAtTime(new Runnable(key) { // from class: com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator$$ExternalSyntheticLambda0
            public final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                VisualStabilityCoordinator.this.lambda$temporarilyAllowSectionChanges$0(this.f$1);
            }
        }, j + ALLOW_SECTION_CHANGE_TIMEOUT));
        if (!isSectionChangeAllowed) {
            this.mNotifStabilityManager.invalidateList();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$temporarilyAllowSectionChanges$0(String str) {
        this.mEntriesThatCanChangeSection.remove(str);
    }
}
