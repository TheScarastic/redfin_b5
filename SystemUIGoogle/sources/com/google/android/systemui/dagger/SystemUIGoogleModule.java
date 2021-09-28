package com.google.android.systemui.dagger;

import android.content.Context;
import android.hardware.SensorPrivacyManager;
import android.hardware.display.DisplayManager;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.IThermalService;
import android.os.PowerManager;
import android.os.ServiceManager;
import com.android.systemui.R$bool;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsHbmProvider;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dock.DockManager;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.recents.Recents;
import com.android.systemui.recents.RecentsImplementation;
import com.android.systemui.settings.UserContentResolverProvider;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyControllerImpl;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.SensorPrivacyControllerImpl;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.google.android.systemui.dreamliner.DockObserver;
import com.google.android.systemui.dreamliner.DreamlinerUtils;
import com.google.android.systemui.fingerprint.UdfpsGhbmProvider;
import com.google.android.systemui.fingerprint.UdfpsHbmController;
import com.google.android.systemui.fingerprint.UdfpsLhbmProvider;
import com.google.android.systemui.reversecharging.ReverseChargingController;
import com.google.android.systemui.reversecharging.ReverseWirelessCharger;
import com.google.android.systemui.smartspace.BcSmartspaceDataProvider;
import com.google.android.systemui.statusbar.policy.BatteryControllerImplGoogle;
import java.util.Optional;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
abstract class SystemUIGoogleModule {
    /* access modifiers changed from: package-private */
    public static boolean provideAllowNotificationLongPress() {
        return true;
    }

    /* access modifiers changed from: package-private */
    public static String provideLeakReportEmail() {
        return "buganizer-system+187317@google.com";
    }

    /* access modifiers changed from: package-private */
    public static BatteryController provideBatteryController(Context context, EnhancedEstimates enhancedEstimates, PowerManager powerManager, BroadcastDispatcher broadcastDispatcher, DemoModeController demoModeController, Handler handler, Handler handler2, UserContentResolverProvider userContentResolverProvider, ReverseChargingController reverseChargingController) {
        BatteryControllerImplGoogle batteryControllerImplGoogle = new BatteryControllerImplGoogle(context, enhancedEstimates, powerManager, broadcastDispatcher, demoModeController, handler, handler2, userContentResolverProvider, reverseChargingController);
        batteryControllerImplGoogle.init();
        return batteryControllerImplGoogle;
    }

    /* access modifiers changed from: package-private */
    public static SensorPrivacyController provideSensorPrivacyController(SensorPrivacyManager sensorPrivacyManager) {
        SensorPrivacyControllerImpl sensorPrivacyControllerImpl = new SensorPrivacyControllerImpl(sensorPrivacyManager);
        sensorPrivacyControllerImpl.init();
        return sensorPrivacyControllerImpl;
    }

    /* access modifiers changed from: package-private */
    public static IndividualSensorPrivacyController provideIndividualSensorPrivacyController(SensorPrivacyManager sensorPrivacyManager) {
        IndividualSensorPrivacyControllerImpl individualSensorPrivacyControllerImpl = new IndividualSensorPrivacyControllerImpl(sensorPrivacyManager);
        individualSensorPrivacyControllerImpl.init();
        return individualSensorPrivacyControllerImpl;
    }

    /* access modifiers changed from: package-private */
    public static DockManager provideDockManager(Context context, BroadcastDispatcher broadcastDispatcher, StatusBarStateController statusBarStateController, NotificationInterruptStateProvider notificationInterruptStateProvider, ConfigurationController configurationController, DelayableExecutor delayableExecutor) {
        return new DockObserver(context, DreamlinerUtils.getInstance(context), broadcastDispatcher, statusBarStateController, notificationInterruptStateProvider, configurationController, delayableExecutor);
    }

    /* access modifiers changed from: package-private */
    public static Optional<ReverseWirelessCharger> provideReverseWirelessCharger(Context context) {
        return context.getResources().getBoolean(R$bool.config_wlc_support_enabled) ? Optional.of(new ReverseWirelessCharger(context)) : Optional.empty();
    }

    /* access modifiers changed from: package-private */
    public static Optional<UsbManager> provideUsbManager(Context context) {
        return Optional.ofNullable((UsbManager) context.getSystemService(UsbManager.class));
    }

    /* access modifiers changed from: package-private */
    public static BcSmartspaceDataPlugin provideBcSmartspaceDataPlugin() {
        return new BcSmartspaceDataProvider();
    }

    /* access modifiers changed from: package-private */
    public static IThermalService provideIThermalService() {
        return IThermalService.Stub.asInterface(ServiceManager.getService("thermalservice"));
    }

    /* access modifiers changed from: package-private */
    public static HeadsUpManagerPhone provideHeadsUpManagerPhone(Context context, StatusBarStateController statusBarStateController, KeyguardBypassController keyguardBypassController, GroupMembershipManager groupMembershipManager, ConfigurationController configurationController) {
        return new HeadsUpManagerPhone(context, statusBarStateController, keyguardBypassController, groupMembershipManager, configurationController);
    }

    /* access modifiers changed from: package-private */
    public static Recents provideRecents(Context context, RecentsImplementation recentsImplementation, CommandQueue commandQueue) {
        return new Recents(context, recentsImplementation, commandQueue);
    }

    /* access modifiers changed from: package-private */
    public static UdfpsHbmProvider provideUdfpsHbmProvider(Context context, Handler handler, Executor executor, UdfpsGhbmProvider udfpsGhbmProvider, UdfpsLhbmProvider udfpsLhbmProvider, AuthController authController, DisplayManager displayManager) {
        return new UdfpsHbmController(context, handler, executor, udfpsGhbmProvider, udfpsLhbmProvider, authController, displayManager);
    }

    /* access modifiers changed from: package-private */
    public static int provideVoiceReplyCtaLayout() {
        return R$layout.assist_voice_reply_cta;
    }

    /* access modifiers changed from: package-private */
    public static int provideVoiceReplyCtaContainerId() {
        return R$id.voice_reply_cta_container;
    }

    /* access modifiers changed from: package-private */
    public static int provideVoiceReplyCtaTextId() {
        return R$id.voice_reply_cta_text;
    }

    /* access modifiers changed from: package-private */
    public static int provideVoiceReplyCtaIconId() {
        return R$id.voice_reply_cta_icon;
    }

    public static LogBuffer provideNotifVoiceReplyLogBuffer(LogBufferFactory logBufferFactory) {
        return logBufferFactory.create("NotifVoiceReplyLog", 500);
    }
}
