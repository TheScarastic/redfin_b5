package com.android.systemui.statusbar.phone;

import android.app.AlarmManager;
import android.app.IActivityManager;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.UserManager;
import android.telecom.TelecomManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.time.DateFormatUtil;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PhoneStatusBarPolicy_Factory implements Factory<PhoneStatusBarPolicy> {
    private final Provider<AlarmManager> alarmManagerProvider;
    private final Provider<BluetoothController> bluetoothControllerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<CastController> castControllerProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<DataSaverController> dataSaverControllerProvider;
    private final Provider<DateFormatUtil> dateFormatUtilProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<Integer> displayIdProvider;
    private final Provider<HotspotController> hotspotControllerProvider;
    private final Provider<IActivityManager> iActivityManagerProvider;
    private final Provider<StatusBarIconController> iconControllerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<LocationController> locationControllerProvider;
    private final Provider<NextAlarmController> nextAlarmControllerProvider;
    private final Provider<PrivacyItemController> privacyItemControllerProvider;
    private final Provider<PrivacyLogger> privacyLoggerProvider;
    private final Provider<RecordingController> recordingControllerProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<RingerModeTracker> ringerModeTrackerProvider;
    private final Provider<RotationLockController> rotationLockControllerProvider;
    private final Provider<SensorPrivacyController> sensorPrivacyControllerProvider;
    private final Provider<SharedPreferences> sharedPreferencesProvider;
    private final Provider<TelecomManager> telecomManagerProvider;
    private final Provider<Executor> uiBgExecutorProvider;
    private final Provider<UserInfoController> userInfoControllerProvider;
    private final Provider<UserManager> userManagerProvider;
    private final Provider<ZenModeController> zenModeControllerProvider;

    public PhoneStatusBarPolicy_Factory(Provider<StatusBarIconController> provider, Provider<CommandQueue> provider2, Provider<BroadcastDispatcher> provider3, Provider<Executor> provider4, Provider<Resources> provider5, Provider<CastController> provider6, Provider<HotspotController> provider7, Provider<BluetoothController> provider8, Provider<NextAlarmController> provider9, Provider<UserInfoController> provider10, Provider<RotationLockController> provider11, Provider<DataSaverController> provider12, Provider<ZenModeController> provider13, Provider<DeviceProvisionedController> provider14, Provider<KeyguardStateController> provider15, Provider<LocationController> provider16, Provider<SensorPrivacyController> provider17, Provider<IActivityManager> provider18, Provider<AlarmManager> provider19, Provider<UserManager> provider20, Provider<RecordingController> provider21, Provider<TelecomManager> provider22, Provider<Integer> provider23, Provider<SharedPreferences> provider24, Provider<DateFormatUtil> provider25, Provider<RingerModeTracker> provider26, Provider<PrivacyItemController> provider27, Provider<PrivacyLogger> provider28) {
        this.iconControllerProvider = provider;
        this.commandQueueProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.uiBgExecutorProvider = provider4;
        this.resourcesProvider = provider5;
        this.castControllerProvider = provider6;
        this.hotspotControllerProvider = provider7;
        this.bluetoothControllerProvider = provider8;
        this.nextAlarmControllerProvider = provider9;
        this.userInfoControllerProvider = provider10;
        this.rotationLockControllerProvider = provider11;
        this.dataSaverControllerProvider = provider12;
        this.zenModeControllerProvider = provider13;
        this.deviceProvisionedControllerProvider = provider14;
        this.keyguardStateControllerProvider = provider15;
        this.locationControllerProvider = provider16;
        this.sensorPrivacyControllerProvider = provider17;
        this.iActivityManagerProvider = provider18;
        this.alarmManagerProvider = provider19;
        this.userManagerProvider = provider20;
        this.recordingControllerProvider = provider21;
        this.telecomManagerProvider = provider22;
        this.displayIdProvider = provider23;
        this.sharedPreferencesProvider = provider24;
        this.dateFormatUtilProvider = provider25;
        this.ringerModeTrackerProvider = provider26;
        this.privacyItemControllerProvider = provider27;
        this.privacyLoggerProvider = provider28;
    }

    @Override // javax.inject.Provider
    public PhoneStatusBarPolicy get() {
        return newInstance(this.iconControllerProvider.get(), this.commandQueueProvider.get(), this.broadcastDispatcherProvider.get(), this.uiBgExecutorProvider.get(), this.resourcesProvider.get(), this.castControllerProvider.get(), this.hotspotControllerProvider.get(), this.bluetoothControllerProvider.get(), this.nextAlarmControllerProvider.get(), this.userInfoControllerProvider.get(), this.rotationLockControllerProvider.get(), this.dataSaverControllerProvider.get(), this.zenModeControllerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.locationControllerProvider.get(), this.sensorPrivacyControllerProvider.get(), this.iActivityManagerProvider.get(), this.alarmManagerProvider.get(), this.userManagerProvider.get(), this.recordingControllerProvider.get(), this.telecomManagerProvider.get(), this.displayIdProvider.get().intValue(), this.sharedPreferencesProvider.get(), this.dateFormatUtilProvider.get(), this.ringerModeTrackerProvider.get(), this.privacyItemControllerProvider.get(), this.privacyLoggerProvider.get());
    }

    public static PhoneStatusBarPolicy_Factory create(Provider<StatusBarIconController> provider, Provider<CommandQueue> provider2, Provider<BroadcastDispatcher> provider3, Provider<Executor> provider4, Provider<Resources> provider5, Provider<CastController> provider6, Provider<HotspotController> provider7, Provider<BluetoothController> provider8, Provider<NextAlarmController> provider9, Provider<UserInfoController> provider10, Provider<RotationLockController> provider11, Provider<DataSaverController> provider12, Provider<ZenModeController> provider13, Provider<DeviceProvisionedController> provider14, Provider<KeyguardStateController> provider15, Provider<LocationController> provider16, Provider<SensorPrivacyController> provider17, Provider<IActivityManager> provider18, Provider<AlarmManager> provider19, Provider<UserManager> provider20, Provider<RecordingController> provider21, Provider<TelecomManager> provider22, Provider<Integer> provider23, Provider<SharedPreferences> provider24, Provider<DateFormatUtil> provider25, Provider<RingerModeTracker> provider26, Provider<PrivacyItemController> provider27, Provider<PrivacyLogger> provider28) {
        return new PhoneStatusBarPolicy_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28);
    }

    public static PhoneStatusBarPolicy newInstance(StatusBarIconController statusBarIconController, CommandQueue commandQueue, BroadcastDispatcher broadcastDispatcher, Executor executor, Resources resources, CastController castController, HotspotController hotspotController, BluetoothController bluetoothController, NextAlarmController nextAlarmController, UserInfoController userInfoController, RotationLockController rotationLockController, DataSaverController dataSaverController, ZenModeController zenModeController, DeviceProvisionedController deviceProvisionedController, KeyguardStateController keyguardStateController, LocationController locationController, SensorPrivacyController sensorPrivacyController, IActivityManager iActivityManager, AlarmManager alarmManager, UserManager userManager, RecordingController recordingController, TelecomManager telecomManager, int i, SharedPreferences sharedPreferences, DateFormatUtil dateFormatUtil, RingerModeTracker ringerModeTracker, PrivacyItemController privacyItemController, PrivacyLogger privacyLogger) {
        return new PhoneStatusBarPolicy(statusBarIconController, commandQueue, broadcastDispatcher, executor, resources, castController, hotspotController, bluetoothController, nextAlarmController, userInfoController, rotationLockController, dataSaverController, zenModeController, deviceProvisionedController, keyguardStateController, locationController, sensorPrivacyController, iActivityManager, alarmManager, userManager, recordingController, telecomManager, i, sharedPreferences, dateFormatUtil, ringerModeTracker, privacyItemController, privacyLogger);
    }
}
