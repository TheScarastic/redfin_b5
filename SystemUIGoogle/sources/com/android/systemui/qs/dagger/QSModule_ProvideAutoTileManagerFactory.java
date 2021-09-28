package com.android.systemui.qs.dagger;

import android.content.Context;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import com.android.systemui.qs.AutoAddTracker;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.WalletController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSModule_ProvideAutoTileManagerFactory implements Factory<AutoTileManager> {
    private final Provider<AutoAddTracker.Builder> autoAddTrackerBuilderProvider;
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

    public QSModule_ProvideAutoTileManagerFactory(Provider<Context> provider, Provider<AutoAddTracker.Builder> provider2, Provider<QSTileHost> provider3, Provider<Handler> provider4, Provider<SecureSettings> provider5, Provider<HotspotController> provider6, Provider<DataSaverController> provider7, Provider<ManagedProfileController> provider8, Provider<NightDisplayListener> provider9, Provider<CastController> provider10, Provider<ReduceBrightColorsController> provider11, Provider<DeviceControlsController> provider12, Provider<WalletController> provider13, Provider<Boolean> provider14) {
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
        this.reduceBrightColorsControllerProvider = provider11;
        this.deviceControlsControllerProvider = provider12;
        this.walletControllerProvider = provider13;
        this.isReduceBrightColorsAvailableProvider = provider14;
    }

    @Override // javax.inject.Provider
    public AutoTileManager get() {
        return provideAutoTileManager(this.contextProvider.get(), this.autoAddTrackerBuilderProvider.get(), this.hostProvider.get(), this.handlerProvider.get(), this.secureSettingsProvider.get(), this.hotspotControllerProvider.get(), this.dataSaverControllerProvider.get(), this.managedProfileControllerProvider.get(), this.nightDisplayListenerProvider.get(), this.castControllerProvider.get(), this.reduceBrightColorsControllerProvider.get(), this.deviceControlsControllerProvider.get(), this.walletControllerProvider.get(), this.isReduceBrightColorsAvailableProvider.get().booleanValue());
    }

    public static QSModule_ProvideAutoTileManagerFactory create(Provider<Context> provider, Provider<AutoAddTracker.Builder> provider2, Provider<QSTileHost> provider3, Provider<Handler> provider4, Provider<SecureSettings> provider5, Provider<HotspotController> provider6, Provider<DataSaverController> provider7, Provider<ManagedProfileController> provider8, Provider<NightDisplayListener> provider9, Provider<CastController> provider10, Provider<ReduceBrightColorsController> provider11, Provider<DeviceControlsController> provider12, Provider<WalletController> provider13, Provider<Boolean> provider14) {
        return new QSModule_ProvideAutoTileManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static AutoTileManager provideAutoTileManager(Context context, AutoAddTracker.Builder builder, QSTileHost qSTileHost, Handler handler, SecureSettings secureSettings, HotspotController hotspotController, DataSaverController dataSaverController, ManagedProfileController managedProfileController, NightDisplayListener nightDisplayListener, CastController castController, ReduceBrightColorsController reduceBrightColorsController, DeviceControlsController deviceControlsController, WalletController walletController, boolean z) {
        return (AutoTileManager) Preconditions.checkNotNullFromProvides(QSModule.provideAutoTileManager(context, builder, qSTileHost, handler, secureSettings, hotspotController, dataSaverController, managedProfileController, nightDisplayListener, castController, reduceBrightColorsController, deviceControlsController, walletController, z));
    }
}
