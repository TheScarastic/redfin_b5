package com.google.android.systemui.columbus;

import android.content.Context;
import android.util.Log;
import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.actions.DismissTimer;
import com.google.android.systemui.columbus.actions.LaunchApp;
import com.google.android.systemui.columbus.actions.LaunchOpa;
import com.google.android.systemui.columbus.actions.LaunchOverview;
import com.google.android.systemui.columbus.actions.ManageMedia;
import com.google.android.systemui.columbus.actions.OpenNotificationShade;
import com.google.android.systemui.columbus.actions.SettingsAction;
import com.google.android.systemui.columbus.actions.SilenceCall;
import com.google.android.systemui.columbus.actions.SnoozeAlarm;
import com.google.android.systemui.columbus.actions.TakeScreenshot;
import com.google.android.systemui.columbus.actions.UnpinNotifications;
import com.google.android.systemui.columbus.actions.UserAction;
import com.google.android.systemui.columbus.actions.UserSelectedAction;
import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import com.google.android.systemui.columbus.feedback.HapticClick;
import com.google.android.systemui.columbus.feedback.UserActivity;
import com.google.android.systemui.columbus.gates.CameraVisibility;
import com.google.android.systemui.columbus.gates.ChargingState;
import com.google.android.systemui.columbus.gates.FlagEnabled;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.gates.KeyguardProximity;
import com.google.android.systemui.columbus.gates.PowerSaveState;
import com.google.android.systemui.columbus.gates.PowerState;
import com.google.android.systemui.columbus.gates.ScreenTouch;
import com.google.android.systemui.columbus.gates.SetupWizard;
import com.google.android.systemui.columbus.gates.SystemKeyPress;
import com.google.android.systemui.columbus.gates.TelephonyActivity;
import com.google.android.systemui.columbus.gates.UsbState;
import com.google.android.systemui.columbus.gates.VrMode;
import com.google.android.systemui.columbus.sensors.CHREGestureSensor;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import com.google.android.systemui.columbus.sensors.GestureSensorImpl;
import com.google.android.systemui.columbus.sensors.config.Adjustment;
import com.google.android.systemui.columbus.sensors.config.LowSensitivitySettingAdjustment;
import dagger.Lazy;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SpreadBuilder;
/* compiled from: ColumbusModule.kt */
/* loaded from: classes2.dex */
public abstract class ColumbusModule {
    public static final Companion Companion = new Companion(null);

    public static final Set<Integer> provideBlockingSystemKeys() {
        return Companion.provideBlockingSystemKeys();
    }

    public static final List<Action> provideColumbusActions(List<Action> list, UnpinNotifications unpinNotifications, UserSelectedAction userSelectedAction) {
        return Companion.provideColumbusActions(list, unpinNotifications, userSelectedAction);
    }

    public static final Set<FeedbackEffect> provideColumbusEffects(HapticClick hapticClick, UserActivity userActivity) {
        return Companion.provideColumbusEffects(hapticClick, userActivity);
    }

    public static final Set<Gate> provideColumbusGates(FlagEnabled flagEnabled, ChargingState chargingState, UsbState usbState, KeyguardProximity keyguardProximity, SetupWizard setupWizard, SystemKeyPress systemKeyPress, TelephonyActivity telephonyActivity, VrMode vrMode, CameraVisibility cameraVisibility, PowerSaveState powerSaveState, PowerState powerState, ScreenTouch screenTouch) {
        return Companion.provideColumbusGates(flagEnabled, chargingState, usbState, keyguardProximity, setupWizard, systemKeyPress, telephonyActivity, vrMode, cameraVisibility, powerSaveState, powerState, screenTouch);
    }

    public static final List<Action> provideFullscreenActions(DismissTimer dismissTimer, SnoozeAlarm snoozeAlarm, SilenceCall silenceCall, SettingsAction settingsAction) {
        return Companion.provideFullscreenActions(dismissTimer, snoozeAlarm, silenceCall, settingsAction);
    }

    public static final List<Adjustment> provideGestureAdjustments(LowSensitivitySettingAdjustment lowSensitivitySettingAdjustment) {
        return Companion.provideGestureAdjustments(lowSensitivitySettingAdjustment);
    }

    public static final GestureSensor provideGestureSensor(Context context, ColumbusSettings columbusSettings, Lazy<CHREGestureSensor> lazy, Lazy<GestureSensorImpl> lazy2) {
        return Companion.provideGestureSensor(context, columbusSettings, lazy, lazy2);
    }

    public static final long provideTransientGateDuration() {
        return Companion.provideTransientGateDuration();
    }

    public static final Map<String, UserAction> provideUserSelectedActions(LaunchOpa launchOpa, ManageMedia manageMedia, TakeScreenshot takeScreenshot, LaunchOverview launchOverview, OpenNotificationShade openNotificationShade, LaunchApp launchApp) {
        return Companion.provideUserSelectedActions(launchOpa, manageMedia, takeScreenshot, launchOverview, openNotificationShade, launchApp);
    }

    /* compiled from: ColumbusModule.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final long provideTransientGateDuration() {
            return 500;
        }

        private Companion() {
        }

        public final Set<Gate> provideColumbusGates(FlagEnabled flagEnabled, ChargingState chargingState, UsbState usbState, KeyguardProximity keyguardProximity, SetupWizard setupWizard, SystemKeyPress systemKeyPress, TelephonyActivity telephonyActivity, VrMode vrMode, CameraVisibility cameraVisibility, PowerSaveState powerSaveState, PowerState powerState, ScreenTouch screenTouch) {
            Intrinsics.checkNotNullParameter(flagEnabled, "flagEnabled");
            Intrinsics.checkNotNullParameter(chargingState, "chargingState");
            Intrinsics.checkNotNullParameter(usbState, "usbState");
            Intrinsics.checkNotNullParameter(keyguardProximity, "keyguardProximity");
            Intrinsics.checkNotNullParameter(setupWizard, "setupWizard");
            Intrinsics.checkNotNullParameter(systemKeyPress, "systemKeyPress");
            Intrinsics.checkNotNullParameter(telephonyActivity, "telephonyActivity");
            Intrinsics.checkNotNullParameter(vrMode, "vrMode");
            Intrinsics.checkNotNullParameter(cameraVisibility, "cameraVisibility");
            Intrinsics.checkNotNullParameter(powerSaveState, "powerSaveState");
            Intrinsics.checkNotNullParameter(powerState, "powerState");
            Intrinsics.checkNotNullParameter(screenTouch, "screenTouch");
            return SetsKt__SetsKt.setOf((Object[]) new Gate[]{flagEnabled, chargingState, usbState, keyguardProximity, setupWizard, systemKeyPress, telephonyActivity, vrMode, cameraVisibility, powerSaveState, powerState, screenTouch});
        }

        public final List<Action> provideColumbusActions(List<Action> list, UnpinNotifications unpinNotifications, UserSelectedAction userSelectedAction) {
            Intrinsics.checkNotNullParameter(list, "fullscreenActions");
            Intrinsics.checkNotNullParameter(unpinNotifications, "unpinNotifications");
            Intrinsics.checkNotNullParameter(userSelectedAction, "userSelectedAction");
            SpreadBuilder spreadBuilder = new SpreadBuilder(3);
            Object[] array = list.toArray(new Action[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T>");
            spreadBuilder.addSpread(array);
            spreadBuilder.add(unpinNotifications);
            spreadBuilder.add(userSelectedAction);
            return CollectionsKt__CollectionsKt.listOf((Object[]) spreadBuilder.toArray(new Action[spreadBuilder.size()]));
        }

        public final List<Action> provideFullscreenActions(DismissTimer dismissTimer, SnoozeAlarm snoozeAlarm, SilenceCall silenceCall, SettingsAction settingsAction) {
            Intrinsics.checkNotNullParameter(dismissTimer, "dismissTimer");
            Intrinsics.checkNotNullParameter(snoozeAlarm, "snoozeAlarm");
            Intrinsics.checkNotNullParameter(silenceCall, "silenceCall");
            Intrinsics.checkNotNullParameter(settingsAction, "settingsAction");
            return CollectionsKt__CollectionsKt.listOf((Object[]) new Action[]{dismissTimer, snoozeAlarm, silenceCall, settingsAction});
        }

        public final Set<FeedbackEffect> provideColumbusEffects(HapticClick hapticClick, UserActivity userActivity) {
            Intrinsics.checkNotNullParameter(hapticClick, "hapticClick");
            Intrinsics.checkNotNullParameter(userActivity, "userActivity");
            return SetsKt__SetsKt.setOf((Object[]) new FeedbackEffect[]{hapticClick, userActivity});
        }

        public final List<Adjustment> provideGestureAdjustments(LowSensitivitySettingAdjustment lowSensitivitySettingAdjustment) {
            Intrinsics.checkNotNullParameter(lowSensitivitySettingAdjustment, "lowSensitivitySettingAdjustment");
            return CollectionsKt__CollectionsJVMKt.listOf(lowSensitivitySettingAdjustment);
        }

        public final Set<Integer> provideBlockingSystemKeys() {
            return SetsKt__SetsKt.setOf((Object[]) new Integer[]{24, 25, 26});
        }

        public final Map<String, UserAction> provideUserSelectedActions(LaunchOpa launchOpa, ManageMedia manageMedia, TakeScreenshot takeScreenshot, LaunchOverview launchOverview, OpenNotificationShade openNotificationShade, LaunchApp launchApp) {
            Intrinsics.checkNotNullParameter(launchOpa, "launchOpa");
            Intrinsics.checkNotNullParameter(manageMedia, "manageMedia");
            Intrinsics.checkNotNullParameter(takeScreenshot, "takeScreenshot");
            Intrinsics.checkNotNullParameter(launchOverview, "launchOverview");
            Intrinsics.checkNotNullParameter(openNotificationShade, "openNotificationShade");
            Intrinsics.checkNotNullParameter(launchApp, "launchApp");
            return MapsKt__MapsKt.mapOf(new Pair("assistant", launchOpa), new Pair("media", manageMedia), new Pair("screenshot", takeScreenshot), new Pair("overview", launchOverview), new Pair("notifications", openNotificationShade), new Pair("launch", launchApp));
        }

        public final GestureSensor provideGestureSensor(Context context, ColumbusSettings columbusSettings, Lazy<CHREGestureSensor> lazy, Lazy<GestureSensorImpl> lazy2) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(columbusSettings, "columbusSettings");
            Intrinsics.checkNotNullParameter(lazy, "chreGestureSensor");
            Intrinsics.checkNotNullParameter(lazy2, "apGestureSensor");
            if (columbusSettings.useApSensor() || !context.getPackageManager().hasSystemFeature("android.hardware.context_hub")) {
                Log.i("Columbus/Module", "Creating AP sensor");
                GestureSensorImpl gestureSensorImpl = lazy2.get();
                Intrinsics.checkNotNullExpressionValue(gestureSensorImpl, "{\n                Log.i(TAG, \"Creating AP sensor\")\n                apGestureSensor.get()\n            }");
                return gestureSensorImpl;
            }
            Log.i("Columbus/Module", "Creating CHRE sensor");
            CHREGestureSensor cHREGestureSensor = lazy.get();
            Intrinsics.checkNotNullExpressionValue(cHREGestureSensor, "{\n                Log.i(TAG, \"Creating CHRE sensor\")\n                chreGestureSensor.get()\n            }");
            return cHREGestureSensor;
        }
    }
}
