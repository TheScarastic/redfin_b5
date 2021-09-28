package com.google.android.systemui.qs.dagger;

import android.content.Context;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import com.android.systemui.qs.AutoAddTracker;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.WalletController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class QSModuleGoogle_ProvideAutoTileManagerFactory implements Factory<AutoTileManager> {
    private final Provider<AutoAddTracker.Builder> autoAddTrackerBuilderProvider;
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<CastController> castControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DataSaverController> dataSaverControllerProvider;
    private final Provider<DeviceControlsController> deviceControlsControllerProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<QSTileHost> hostProvider;
    private final Provider<HotspotController> hotspotControllerProvider;
    private final Provider<Boolean> isReduceBrightColorsAvailableProvider;
    private final Provider<ManagedProfileController> managedProfileControllerProvider;
    private final Provider<NightDisplayListener> nightDisplayListenerProvider;
    private final Provider<ReduceBrightColorsController> reduceBrightColorsControllerProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<WalletController> walletControllerProvider;

    public QSModuleGoogle_ProvideAutoTileManagerFactory(Provider<Context> provider, Provider<AutoAddTracker.Builder> provider2, Provider<QSTileHost> provider3, Provider<Handler> provider4, Provider<SecureSettings> provider5, Provider<HotspotController> provider6, Provider<DataSaverController> provider7, Provider<ManagedProfileController> provider8, Provider<NightDisplayListener> provider9, Provider<CastController> provider10, Provider<BatteryController> provider11, Provider<ReduceBrightColorsController> provider12, Provider<DeviceControlsController> provider13, Provider<WalletController> provider14, Provider<Boolean> provider15) {
        this.contextProvider = provider;
        this.autoAddTrackerBuilderProvider = provider2;
        this.hostProvider = provider3;
        this.handlerProvider = provider4;
        this.secureSettingsProvider = provider5;
        this.hotspotControllerProvider = provider6;
        this.dataSaverControllerProvider = provider7;
        this.managedProfileControllerProvider = provider8;
        this.nightDisplayListenerProvider = provider9;
        this.castControllerProvider = provider10;
        this.batteryControllerProvider = provider11;
        this.reduceBrightColorsControllerProvider = provider12;
        this.deviceControlsControllerProvider = provider13;
        this.walletControllerProvider = provider14;
        this.isReduceBrightColorsAvailableProvider = provider15;
    }

    @Override // javax.inject.Provider
    public AutoTileManager get() {
        return provideAutoTileManager(this.contextProvider.get(), this.autoAddTrackerBuilderProvider.get(), this.hostProvider.get(), this.handlerProvider.get(), this.secureSettingsProvider.get(), this.hotspotControllerProvider.get(), this.dataSaverControllerProvider.get(), this.managedProfileControllerProvider.get(), this.nightDisplayListenerProvider.get(), this.castControllerProvider.get(), this.batteryControllerProvider.get(), this.reduceBrightColorsControllerProvider.get(), this.deviceControlsControllerProvider.get(), this.walletControllerProvider.get(), this.isReduceBrightColorsAvailableProvider.get().booleanValue());
    }

    public static QSModuleGoogle_ProvideAutoTileManagerFactory create(Provider<Context> provider, Provider<AutoAddTracker.Builder> provider2, Provider<QSTileHost> provider3, Provider<Handler> provider4, Provider<SecureSettings> provider5, Provider<HotspotController> provider6, Provider<DataSaverController> provider7, Provider<ManagedProfileController> provider8, Provider<NightDisplayListener> provider9, Provider<CastController> provider10, Provider<BatteryController> provider11, Provider<ReduceBrightColorsController> provider12, Provider<DeviceControlsController> provider13, Provider<WalletController> provider14, Provider<Boolean> provider15) {
        return new QSModuleGoogle_ProvideAutoTileManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static AutoTileManager provideAutoTileManager(Context context, AutoAddTracker.Builder builder, QSTileHost qSTileHost, Handler handler, SecureSettings secureSettings, HotspotController hotspotController, DataSaverController dataSaverController, ManagedProfileController managedProfileController, NightDisplayListener nightDisplayListener, CastController castController, BatteryController batteryController, ReduceBrightColorsController reduceBrightColorsController, DeviceControlsController deviceControlsController, WalletController walletController, boolean z) {
        return (AutoTileManager) Preconditions.checkNotNullFromProvides(QSModuleGoogle.provideAutoTileManager(context, builder, qSTileHost, handler, secureSettings, hotspotController, dataSaverController, managedProfileController, nightDisplayListener, castController, batteryController, reduceBrightColorsController, deviceControlsController, walletController, z));
    }
}
