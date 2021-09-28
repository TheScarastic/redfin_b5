package com.android.systemui.statusbar.events;

import android.content.Context;
import com.android.systemui.R$string;
import com.android.systemui.privacy.PrivacyChipBuilder;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.time.SystemClock;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SystemEventCoordinator.kt */
/* loaded from: classes.dex */
public final class SystemEventCoordinator {
    private final BatteryController batteryController;
    private final Context context;
    private final PrivacyItemController privacyController;
    private SystemStatusAnimationScheduler scheduler;
    private final SystemClock systemClock;
    private final SystemEventCoordinator$batteryStateListener$1 batteryStateListener = new BatteryController.BatteryStateChangeCallback(this) { // from class: com.android.systemui.statusbar.events.SystemEventCoordinator$batteryStateListener$1
        private boolean plugged;
        private boolean stateKnown;
        final /* synthetic */ SystemEventCoordinator this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            if (!this.stateKnown) {
                this.stateKnown = true;
                this.plugged = z;
                notifyListeners();
            } else if (this.plugged != z) {
                this.plugged = z;
                notifyListeners();
            }
        }

        private final void notifyListeners() {
            if (this.plugged) {
                this.this$0.notifyPluggedIn();
            }
        }
    };
    private final SystemEventCoordinator$privacyStateListener$1 privacyStateListener = new SystemEventCoordinator$privacyStateListener$1(this);

    public SystemEventCoordinator(SystemClock systemClock, BatteryController batteryController, PrivacyItemController privacyItemController, Context context) {
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(batteryController, "batteryController");
        Intrinsics.checkNotNullParameter(privacyItemController, "privacyController");
        Intrinsics.checkNotNullParameter(context, "context");
        this.systemClock = systemClock;
        this.batteryController = batteryController;
        this.privacyController = privacyItemController;
        this.context = context;
    }

    public final void startObserving() {
        this.privacyController.addCallback(this.privacyStateListener);
    }

    public final void stopObserving() {
        this.privacyController.removeCallback(this.privacyStateListener);
    }

    public final void attachScheduler(SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        Intrinsics.checkNotNullParameter(systemStatusAnimationScheduler, "s");
        this.scheduler = systemStatusAnimationScheduler;
    }

    public final void notifyPluggedIn() {
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.scheduler;
        if (systemStatusAnimationScheduler != null) {
            systemStatusAnimationScheduler.onStatusEvent(new BatteryEvent());
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("scheduler");
            throw null;
        }
    }

    public final void notifyPrivacyItemsEmpty() {
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.scheduler;
        if (systemStatusAnimationScheduler != null) {
            systemStatusAnimationScheduler.setShouldShowPersistentPrivacyIndicator(false);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("scheduler");
            throw null;
        }
    }

    public final void notifyPrivacyItemsChanged(boolean z) {
        PrivacyEvent privacyEvent = new PrivacyEvent(z);
        privacyEvent.setPrivacyItems(this.privacyStateListener.getCurrentPrivacyItems());
        privacyEvent.setContentDescription((String) new Function0<String>(this, privacyEvent) { // from class: com.android.systemui.statusbar.events.SystemEventCoordinator$notifyPrivacyItemsChanged$1
            final /* synthetic */ PrivacyEvent $event;
            final /* synthetic */ SystemEventCoordinator this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$event = r2;
            }

            @Override // kotlin.jvm.functions.Function0
            public final String invoke() {
                return this.this$0.context.getString(R$string.ongoing_privacy_chip_content_multiple_apps, new PrivacyChipBuilder(this.this$0.context, this.$event.getPrivacyItems()).joinTypes());
            }
        }.invoke());
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.scheduler;
        if (systemStatusAnimationScheduler != null) {
            systemStatusAnimationScheduler.onStatusEvent(privacyEvent);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("scheduler");
            throw null;
        }
    }
}
