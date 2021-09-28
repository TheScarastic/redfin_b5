package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.util.Log;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.Optional;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UnpinNotifications.kt */
/* loaded from: classes2.dex */
public final class UnpinNotifications extends Action {
    public static final Companion Companion = new Companion(null);
    private final UnpinNotifications$gateListener$1 gateListener;
    private boolean hasPinnedHeadsUp;
    private final HeadsUpManager headsUpManager;
    private final SilenceAlertsDisabled silenceAlertsDisabled;
    private final String tag = "Columbus/UnpinNotif";
    private final UnpinNotifications$headsUpChangedListener$1 headsUpChangedListener = new UnpinNotifications$headsUpChangedListener$1(this);

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public UnpinNotifications(Context context, SilenceAlertsDisabled silenceAlertsDisabled, Optional<HeadsUpManager> optional) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(silenceAlertsDisabled, "silenceAlertsDisabled");
        Intrinsics.checkNotNullParameter(optional, "headsUpManagerOptional");
        this.silenceAlertsDisabled = silenceAlertsDisabled;
        HeadsUpManager orElse = optional.orElse(null);
        this.headsUpManager = orElse;
        UnpinNotifications$gateListener$1 unpinNotifications$gateListener$1 = new Gate.Listener(this) { // from class: com.google.android.systemui.columbus.actions.UnpinNotifications$gateListener$1
            final /* synthetic */ UnpinNotifications this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.google.android.systemui.columbus.gates.Gate.Listener
            public void onGateChanged(Gate gate) {
                Intrinsics.checkNotNullParameter(gate, "gate");
                if (this.this$0.silenceAlertsDisabled.isBlocking()) {
                    this.this$0.onSilenceAlertsDisabled();
                } else {
                    this.this$0.onSilenceAlertsEnabled();
                }
            }
        };
        this.gateListener = unpinNotifications$gateListener$1;
        if (orElse == null) {
            Log.w("Columbus/UnpinNotif", "No HeadsUpManager");
        } else {
            silenceAlertsDisabled.registerListener(unpinNotifications$gateListener$1);
        }
        updateAvailable();
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }

    /* access modifiers changed from: private */
    public final void onSilenceAlertsEnabled() {
        HeadsUpManager headsUpManager = this.headsUpManager;
        if (headsUpManager != null) {
            headsUpManager.addListener(this.headsUpChangedListener);
        }
        HeadsUpManager headsUpManager2 = this.headsUpManager;
        this.hasPinnedHeadsUp = headsUpManager2 == null ? false : headsUpManager2.hasPinnedHeadsUp();
    }

    /* access modifiers changed from: private */
    public final void onSilenceAlertsDisabled() {
        HeadsUpManager headsUpManager = this.headsUpManager;
        if (headsUpManager != null) {
            headsUpManager.removeListener(this.headsUpChangedListener);
        }
    }

    /* access modifiers changed from: private */
    public final void updateAvailable() {
        setAvailable(!this.silenceAlertsDisabled.isBlocking() && this.hasPinnedHeadsUp);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        HeadsUpManager headsUpManager = this.headsUpManager;
        if (headsUpManager != null) {
            headsUpManager.unpinAll(true);
        }
    }

    /* compiled from: UnpinNotifications.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
