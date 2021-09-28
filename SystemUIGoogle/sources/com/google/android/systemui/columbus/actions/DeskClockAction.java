package com.google.android.systemui.columbus.actions;

import android.app.ActivityOptions;
import android.app.IActivityManager;
import android.app.SynchronousUserSwitchObserver;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DeskClockAction.kt */
/* loaded from: classes2.dex */
public abstract class DeskClockAction extends Action {
    public static final Companion Companion = new Companion(null);
    private boolean alertFiring;
    private final DeskClockAction$alertReceiver$1 alertReceiver = new DeskClockAction$alertReceiver$1(this);
    private final DeskClockAction$gateListener$1 gateListener;
    private boolean receiverRegistered;
    private final SilenceAlertsDisabled silenceAlertsDisabled;
    private final DeskClockAction$userSwitchCallback$1 userSwitchCallback;

    protected abstract Intent createDismissIntent();

    /* access modifiers changed from: protected */
    public abstract String getAlertAction();

    /* access modifiers changed from: protected */
    public abstract String getDoneAction();

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public DeskClockAction(Context context, SilenceAlertsDisabled silenceAlertsDisabled, IActivityManager iActivityManager) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(silenceAlertsDisabled, "silenceAlertsDisabled");
        Intrinsics.checkNotNullParameter(iActivityManager, "activityManagerService");
        this.silenceAlertsDisabled = silenceAlertsDisabled;
        DeskClockAction$gateListener$1 deskClockAction$gateListener$1 = new Gate.Listener(this) { // from class: com.google.android.systemui.columbus.actions.DeskClockAction$gateListener$1
            final /* synthetic */ DeskClockAction this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.google.android.systemui.columbus.gates.Gate.Listener
            public void onGateChanged(Gate gate) {
                Intrinsics.checkNotNullParameter(gate, "gate");
                this.this$0.updateBroadcastReceiver();
            }
        };
        this.gateListener = deskClockAction$gateListener$1;
        DeskClockAction$userSwitchCallback$1 deskClockAction$userSwitchCallback$1 = new SynchronousUserSwitchObserver(this) { // from class: com.google.android.systemui.columbus.actions.DeskClockAction$userSwitchCallback$1
            final /* synthetic */ DeskClockAction this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            public void onUserSwitching(int i) throws RemoteException {
                this.this$0.updateBroadcastReceiver();
            }
        };
        this.userSwitchCallback = deskClockAction$userSwitchCallback$1;
        silenceAlertsDisabled.registerListener(deskClockAction$gateListener$1);
        try {
            iActivityManager.registerUserSwitchObserver(deskClockAction$userSwitchCallback$1, "Columbus/DeskClockAct");
        } catch (RemoteException e) {
            Log.e("Columbus/DeskClockAct", "Failed to register user switch observer", e);
        }
        updateBroadcastReceiver();
    }

    /* access modifiers changed from: private */
    public final void updateBroadcastReceiver() {
        this.alertFiring = false;
        if (this.receiverRegistered) {
            getContext().unregisterReceiver(this.alertReceiver);
            this.receiverRegistered = false;
        }
        if (!this.silenceAlertsDisabled.isBlocking()) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(getAlertAction());
            intentFilter.addAction(getDoneAction());
            getContext().registerReceiverAsUser(this.alertReceiver, UserHandle.CURRENT, intentFilter, "com.android.systemui.permission.SEND_ALERT_BROADCASTS", null);
            this.receiverRegistered = true;
        }
        updateAvailable();
    }

    /* access modifiers changed from: private */
    public final void updateAvailable() {
        setAvailable(this.alertFiring);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        Intent createDismissIntent = createDismissIntent();
        ActivityOptions makeBasic = ActivityOptions.makeBasic();
        makeBasic.setDisallowEnterPictureInPictureWhileLaunching(true);
        createDismissIntent.setFlags(268435456);
        createDismissIntent.putExtra("android.intent.extra.REFERRER", Uri.parse(Intrinsics.stringPlus("android-app://", getContext().getPackageName())));
        try {
            getContext().startActivityAsUser(createDismissIntent, makeBasic.toBundle(), UserHandle.CURRENT);
        } catch (ActivityNotFoundException e) {
            Log.e("Columbus/DeskClockAct", "Failed to dismiss alert", e);
        }
        this.alertFiring = false;
        updateAvailable();
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public String toString() {
        return super.toString() + " [receiverRegistered -> " + this.receiverRegistered + ']';
    }

    /* compiled from: DeskClockAction.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
