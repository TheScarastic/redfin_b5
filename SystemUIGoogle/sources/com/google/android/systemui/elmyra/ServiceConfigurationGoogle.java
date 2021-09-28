package com.google.android.systemui.elmyra;

import android.content.Context;
import com.google.android.systemui.elmyra.actions.Action;
import com.google.android.systemui.elmyra.actions.CameraAction;
import com.google.android.systemui.elmyra.actions.DismissTimer;
import com.google.android.systemui.elmyra.actions.LaunchOpa;
import com.google.android.systemui.elmyra.actions.SettingsAction;
import com.google.android.systemui.elmyra.actions.SetupWizardAction;
import com.google.android.systemui.elmyra.actions.SilenceCall;
import com.google.android.systemui.elmyra.actions.SnoozeAlarm;
import com.google.android.systemui.elmyra.actions.UnpinNotifications;
import com.google.android.systemui.elmyra.feedback.AssistInvocationEffect;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.feedback.HapticClick;
import com.google.android.systemui.elmyra.feedback.NavUndimEffect;
import com.google.android.systemui.elmyra.feedback.SquishyNavigationButtons;
import com.google.android.systemui.elmyra.feedback.UserActivity;
import com.google.android.systemui.elmyra.gates.CameraVisibility;
import com.google.android.systemui.elmyra.gates.ChargingState;
import com.google.android.systemui.elmyra.gates.Gate;
import com.google.android.systemui.elmyra.gates.KeyguardDeferredSetup;
import com.google.android.systemui.elmyra.gates.KeyguardProximity;
import com.google.android.systemui.elmyra.gates.LockTask;
import com.google.android.systemui.elmyra.gates.NavigationBarVisibility;
import com.google.android.systemui.elmyra.gates.PowerSaveState;
import com.google.android.systemui.elmyra.gates.SetupWizard;
import com.google.android.systemui.elmyra.gates.SystemKeyPress;
import com.google.android.systemui.elmyra.gates.TelephonyActivity;
import com.google.android.systemui.elmyra.gates.UsbState;
import com.google.android.systemui.elmyra.gates.VrMode;
import com.google.android.systemui.elmyra.gates.WakeMode;
import com.google.android.systemui.elmyra.sensors.CHREGestureSensor;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import com.google.android.systemui.elmyra.sensors.JNIGestureSensor;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import com.google.android.systemui.elmyra.sensors.config.ScreenStateAdjustment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes2.dex */
public class ServiceConfigurationGoogle implements ServiceConfiguration {
    private final List<Action> mActions;
    private final List<FeedbackEffect> mFeedbackEffects;
    private final List<Gate> mGates;
    private final GestureSensor mGestureSensor;

    public ServiceConfigurationGoogle(Context context, AssistInvocationEffect assistInvocationEffect, LaunchOpa.Builder builder, SettingsAction.Builder builder2, CameraAction.Builder builder3, SetupWizardAction.Builder builder4, SquishyNavigationButtons squishyNavigationButtons, UnpinNotifications unpinNotifications, SilenceCall silenceCall, TelephonyActivity telephonyActivity) {
        LaunchOpa build = builder.addFeedbackEffect(assistInvocationEffect).build();
        SettingsAction build2 = builder2.setLaunchOpa(build).build();
        List asList = Arrays.asList(new DismissTimer(context), new SnoozeAlarm(context), silenceCall, build2);
        CameraAction build3 = builder3.addFeedbackEffect(assistInvocationEffect).build();
        ArrayList arrayList = new ArrayList();
        this.mActions = arrayList;
        arrayList.addAll(asList);
        arrayList.add(unpinNotifications);
        arrayList.add(build3);
        arrayList.add(builder4.setSettingsAction(build2).setLaunchOpa(build).build());
        arrayList.add(build);
        ArrayList arrayList2 = new ArrayList();
        this.mFeedbackEffects = arrayList2;
        arrayList2.add(new HapticClick(context));
        arrayList2.add(squishyNavigationButtons);
        arrayList2.add(new NavUndimEffect());
        arrayList2.add(new UserActivity(context));
        ArrayList arrayList3 = new ArrayList();
        this.mGates = arrayList3;
        arrayList3.add(new WakeMode(context));
        arrayList3.add(new ChargingState(context));
        arrayList3.add(new UsbState(context));
        arrayList3.add(new KeyguardProximity(context));
        arrayList3.add(new SetupWizard(context, Arrays.asList(build2)));
        arrayList3.add(new NavigationBarVisibility(context, asList));
        arrayList3.add(new SystemKeyPress(context));
        arrayList3.add(telephonyActivity);
        arrayList3.add(new VrMode(context));
        arrayList3.add(new KeyguardDeferredSetup(context, asList));
        arrayList3.add(new CameraVisibility(context, build3, asList));
        arrayList3.add(new PowerSaveState(context));
        arrayList3.add(new LockTask(context));
        ArrayList arrayList4 = new ArrayList();
        arrayList4.add(new ScreenStateAdjustment(context));
        GestureConfiguration gestureConfiguration = new GestureConfiguration(context, arrayList4);
        if (JNIGestureSensor.isAvailable(context)) {
            this.mGestureSensor = new JNIGestureSensor(context, gestureConfiguration);
        } else {
            this.mGestureSensor = new CHREGestureSensor(context, gestureConfiguration, new SnapshotConfiguration(context));
        }
    }

    @Override // com.google.android.systemui.elmyra.ServiceConfiguration
    public List<Action> getActions() {
        return this.mActions;
    }

    @Override // com.google.android.systemui.elmyra.ServiceConfiguration
    public List<FeedbackEffect> getFeedbackEffects() {
        return this.mFeedbackEffects;
    }

    @Override // com.google.android.systemui.elmyra.ServiceConfiguration
    public List<Gate> getGates() {
        return this.mGates;
    }

    @Override // com.google.android.systemui.elmyra.ServiceConfiguration
    public GestureSensor getGestureSensor() {
        return this.mGestureSensor;
    }
}
