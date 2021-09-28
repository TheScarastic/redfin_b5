package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.util.Log;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.google.android.systemui.columbus.ColumbusSettings;
import com.google.android.systemui.columbus.PowerManagerWrapper;
import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UserSelectedAction.kt */
/* loaded from: classes2.dex */
public final class UserSelectedAction extends Action {
    public static final Companion Companion = new Companion(null);
    private UserAction currentAction;
    private final KeyguardStateController keyguardStateController;
    private final PowerManagerWrapper powerManager;
    private final UserSelectedAction$settingsChangeListener$1 settingsChangeListener;
    private final TakeScreenshot takeScreenshot;
    private final Map<String, UserAction> userSelectedActions;
    private final UserSelectedAction$keyguardMonitorCallback$1 keyguardMonitorCallback = new KeyguardStateController.Callback(this) { // from class: com.google.android.systemui.columbus.actions.UserSelectedAction$keyguardMonitorCallback$1
        final /* synthetic */ UserSelectedAction this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public void onKeyguardShowingChanged() {
            this.this$0.updateAvailable();
        }
    };
    private final UserSelectedAction$wakefulnessLifecycleObserver$1 wakefulnessLifecycleObserver = new WakefulnessLifecycle.Observer(this) { // from class: com.google.android.systemui.columbus.actions.UserSelectedAction$wakefulnessLifecycleObserver$1
        final /* synthetic */ UserSelectedAction this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public void onFinishedGoingToSleep() {
            this.this$0.updateAvailable();
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public void onStartedWakingUp() {
            this.this$0.updateAvailable();
        }
    };

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public UserSelectedAction(Context context, ColumbusSettings columbusSettings, Map<String, UserAction> map, TakeScreenshot takeScreenshot, KeyguardStateController keyguardStateController, PowerManagerWrapper powerManagerWrapper, WakefulnessLifecycle wakefulnessLifecycle) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(columbusSettings, "columbusSettings");
        Intrinsics.checkNotNullParameter(map, "userSelectedActions");
        Intrinsics.checkNotNullParameter(takeScreenshot, "takeScreenshot");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(powerManagerWrapper, "powerManager");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle, "wakefulnessLifecycle");
        this.userSelectedActions = map;
        this.takeScreenshot = takeScreenshot;
        this.keyguardStateController = keyguardStateController;
        this.powerManager = powerManagerWrapper;
        UserSelectedAction$settingsChangeListener$1 userSelectedAction$settingsChangeListener$1 = new ColumbusSettings.ColumbusSettingsChangeListener(this) { // from class: com.google.android.systemui.columbus.actions.UserSelectedAction$settingsChangeListener$1
            final /* synthetic */ UserSelectedAction this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public void onAlertSilenceEnabledChange(boolean z) {
                ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onAlertSilenceEnabledChange(this, z);
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public void onColumbusEnabledChange(boolean z) {
                ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onColumbusEnabledChange(this, z);
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public void onLowSensitivityChange(boolean z) {
                ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onLowSensitivityChange(this, z);
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public void onSelectedAppChange(String str) {
                ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onSelectedAppChange(this, str);
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public void onSelectedAppShortcutChange(String str) {
                ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onSelectedAppShortcutChange(this, str);
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public void onUseApSensorChange(boolean z) {
                ColumbusSettings.ColumbusSettingsChangeListener.DefaultImpls.onUseApSensorChange(this, z);
            }

            @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
            public void onSelectedActionChange(String str) {
                Intrinsics.checkNotNullParameter(str, "selectedAction");
                UserAction userAction = this.this$0.getActionFromSetting(str);
                if (!Intrinsics.areEqual(userAction, this.this$0.currentAction)) {
                    this.this$0.currentAction.onGestureDetected(0, null);
                    this.this$0.currentAction = userAction;
                    Log.i("Columbus/SelectedAction", Intrinsics.stringPlus("User Action selected: ", this.this$0.currentAction));
                    this.this$0.updateAvailable();
                }
            }
        };
        this.settingsChangeListener = userSelectedAction$settingsChangeListener$1;
        UserAction actionFromSetting = getActionFromSetting(columbusSettings.selectedAction());
        this.currentAction = actionFromSetting;
        Log.i("Columbus/SelectedAction", Intrinsics.stringPlus("User Action selected: ", actionFromSetting));
        columbusSettings.registerColumbusSettingsChangeListener(userSelectedAction$settingsChangeListener$1);
        UserSelectedAction$sublistener$1 userSelectedAction$sublistener$1 = new Action.Listener(this) { // from class: com.google.android.systemui.columbus.actions.UserSelectedAction$sublistener$1
            final /* synthetic */ UserSelectedAction this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.google.android.systemui.columbus.actions.Action.Listener
            public void onActionAvailabilityChanged(Action action) {
                Intrinsics.checkNotNullParameter(action, "action");
                if (Intrinsics.areEqual(this.this$0.currentAction, action)) {
                    this.this$0.updateAvailable();
                }
            }
        };
        for (UserAction userAction : map.values()) {
            userAction.registerListener(userSelectedAction$sublistener$1);
        }
        this.keyguardStateController.addCallback(this.keyguardMonitorCallback);
        wakefulnessLifecycle.addObserver(this.wakefulnessLifecycleObserver);
        updateAvailable();
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.currentAction.getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig();
    }

    /* access modifiers changed from: private */
    public final void updateAvailable() {
        if (!this.currentAction.isAvailable()) {
            setAvailable(false);
        } else if (!this.currentAction.availableOnScreenOff() && !Intrinsics.areEqual(this.powerManager.isInteractive(), Boolean.TRUE)) {
            setAvailable(false);
        } else if (this.currentAction.availableOnLockscreen() || !this.keyguardStateController.isShowing()) {
            setAvailable(true);
        } else {
            setAvailable(false);
        }
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public void onGestureDetected(int i, GestureSensor.DetectionProperties detectionProperties) {
        this.currentAction.onGestureDetected(i, detectionProperties);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public void updateFeedbackEffects(int i, GestureSensor.DetectionProperties detectionProperties) {
        this.currentAction.updateFeedbackEffects(i, detectionProperties);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.currentAction.onTrigger(detectionProperties);
    }

    /* access modifiers changed from: private */
    public final UserAction getActionFromSetting(String str) {
        return this.userSelectedActions.getOrDefault(str, this.takeScreenshot);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public String toString() {
        return super.toString() + " [currentAction -> " + this.currentAction + ']';
    }

    /* compiled from: UserSelectedAction.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
