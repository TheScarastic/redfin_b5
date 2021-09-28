package com.android.systemui.qs.tileimpl;

import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.tiles.AirplaneModeTile;
import com.android.systemui.qs.tiles.AlarmTile;
import com.android.systemui.qs.tiles.BatterySaverTile;
import com.android.systemui.qs.tiles.BluetoothTile;
import com.android.systemui.qs.tiles.CameraToggleTile;
import com.android.systemui.qs.tiles.CastTile;
import com.android.systemui.qs.tiles.CellularTile;
import com.android.systemui.qs.tiles.ColorInversionTile;
import com.android.systemui.qs.tiles.DataSaverTile;
import com.android.systemui.qs.tiles.DeviceControlsTile;
import com.android.systemui.qs.tiles.DndTile;
import com.android.systemui.qs.tiles.FlashlightTile;
import com.android.systemui.qs.tiles.HotspotTile;
import com.android.systemui.qs.tiles.InternetTile;
import com.android.systemui.qs.tiles.LocationTile;
import com.android.systemui.qs.tiles.MicrophoneToggleTile;
import com.android.systemui.qs.tiles.NfcTile;
import com.android.systemui.qs.tiles.NightDisplayTile;
import com.android.systemui.qs.tiles.QuickAccessWalletTile;
import com.android.systemui.qs.tiles.ReduceBrightColorsTile;
import com.android.systemui.qs.tiles.RotationLockTile;
import com.android.systemui.qs.tiles.ScreenRecordTile;
import com.android.systemui.qs.tiles.UiModeNightTile;
import com.android.systemui.qs.tiles.UserTile;
import com.android.systemui.qs.tiles.WifiTile;
import com.android.systemui.qs.tiles.WorkModeTile;
import com.android.systemui.util.leak.GarbageMonitor;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFactoryImpl_Factory implements Factory<QSFactoryImpl> {
    private final Provider<AirplaneModeTile> airplaneModeTileProvider;
    private final Provider<AlarmTile> alarmTileProvider;
    private final Provider<BatterySaverTile> batterySaverTileProvider;
    private final Provider<BluetoothTile> bluetoothTileProvider;
    private final Provider<CameraToggleTile> cameraToggleTileProvider;
    private final Provider<CastTile> castTileProvider;
    private final Provider<CellularTile> cellularTileProvider;
    private final Provider<ColorInversionTile> colorInversionTileProvider;
    private final Provider<CustomTile.Builder> customTileBuilderProvider;
    private final Provider<DataSaverTile> dataSaverTileProvider;
    private final Provider<DeviceControlsTile> deviceControlsTileProvider;
    private final Provider<DndTile> dndTileProvider;
    private final Provider<FlashlightTile> flashlightTileProvider;
    private final Provider<HotspotTile> hotspotTileProvider;
    private final Provider<InternetTile> internetTileProvider;
    private final Provider<LocationTile> locationTileProvider;
    private final Provider<GarbageMonitor.MemoryTile> memoryTileProvider;
    private final Provider<MicrophoneToggleTile> microphoneToggleTileProvider;
    private final Provider<NfcTile> nfcTileProvider;
    private final Provider<NightDisplayTile> nightDisplayTileProvider;
    private final Provider<QSHost> qsHostLazyProvider;
    private final Provider<QuickAccessWalletTile> quickAccessWalletTileProvider;
    private final Provider<ReduceBrightColorsTile> reduceBrightColorsTileProvider;
    private final Provider<RotationLockTile> rotationLockTileProvider;
    private final Provider<ScreenRecordTile> screenRecordTileProvider;
    private final Provider<UiModeNightTile> uiModeNightTileProvider;
    private final Provider<UserTile> userTileProvider;
    private final Provider<WifiTile> wifiTileProvider;
    private final Provider<WorkModeTile> workModeTileProvider;

    public QSFactoryImpl_Factory(Provider<QSHost> provider, Provider<CustomTile.Builder> provider2, Provider<WifiTile> provider3, Provider<InternetTile> provider4, Provider<BluetoothTile> provider5, Provider<CellularTile> provider6, Provider<DndTile> provider7, Provider<ColorInversionTile> provider8, Provider<AirplaneModeTile> provider9, Provider<WorkModeTile> provider10, Provider<RotationLockTile> provider11, Provider<FlashlightTile> provider12, Provider<LocationTile> provider13, Provider<CastTile> provider14, Provider<HotspotTile> provider15, Provider<UserTile> provider16, Provider<BatterySaverTile> provider17, Provider<DataSaverTile> provider18, Provider<NightDisplayTile> provider19, Provider<NfcTile> provider20, Provider<GarbageMonitor.MemoryTile> provider21, Provider<UiModeNightTile> provider22, Provider<ScreenRecordTile> provider23, Provider<ReduceBrightColorsTile> provider24, Provider<CameraToggleTile> provider25, Provider<MicrophoneToggleTile> provider26, Provider<DeviceControlsTile> provider27, Provider<AlarmTile> provider28, Provider<QuickAccessWalletTile> provider29) {
        this.qsHostLazyProvider = provider;
        this.customTileBuilderProvider = provider2;
        this.wifiTileProvider = provider3;
        this.internetTileProvider = provider4;
        this.bluetoothTileProvider = provider5;
        this.cellularTileProvider = provider6;
        this.dndTileProvider = provider7;
        this.colorInversionTileProvider = provider8;
        this.airplaneModeTileProvider = provider9;
        this.workModeTileProvider = provider10;
        this.rotationLockTileProvider = provider11;
        this.flashlightTileProvider = provider12;
        this.locationTileProvider = provider13;
        this.castTileProvider = provider14;
        this.hotspotTileProvider = provider15;
        this.userTileProvider = provider16;
        this.batterySaverTileProvider = provider17;
        this.dataSaverTileProvider = provider18;
        this.nightDisplayTileProvider = provider19;
        this.nfcTileProvider = provider20;
        this.memoryTileProvider = provider21;
        this.uiModeNightTileProvider = provider22;
        this.screenRecordTileProvider = provider23;
        this.reduceBrightColorsTileProvider = provider24;
        this.cameraToggleTileProvider = provider25;
        this.microphoneToggleTileProvider = provider26;
        this.deviceControlsTileProvider = provider27;
        this.alarmTileProvider = provider28;
        this.quickAccessWalletTileProvider = provider29;
    }

    @Override // javax.inject.Provider
    public QSFactoryImpl get() {
        return newInstance(DoubleCheck.lazy(this.qsHostLazyProvider), this.customTileBuilderProvider, this.wifiTileProvider, this.internetTileProvider, this.bluetoothTileProvider, this.cellularTileProvider, this.dndTileProvider, this.colorInversionTileProvider, this.airplaneModeTileProvider, this.workModeTileProvider, this.rotationLockTileProvider, this.flashlightTileProvider, this.locationTileProvider, this.castTileProvider, this.hotspotTileProvider, this.userTileProvider, this.batterySaverTileProvider, this.dataSaverTileProvider, this.nightDisplayTileProvider, this.nfcTileProvider, this.memoryTileProvider, this.uiModeNightTileProvider, this.screenRecordTileProvider, this.reduceBrightColorsTileProvider, this.cameraToggleTileProvider, this.microphoneToggleTileProvider, this.deviceControlsTileProvider, this.alarmTileProvider, this.quickAccessWalletTileProvider);
    }

    public static QSFactoryImpl_Factory create(Provider<QSHost> provider, Provider<CustomTile.Builder> provider2, Provider<WifiTile> provider3, Provider<InternetTile> provider4, Provider<BluetoothTile> provider5, Provider<CellularTile> provider6, Provider<DndTile> provider7, Provider<ColorInversionTile> provider8, Provider<AirplaneModeTile> provider9, Provider<WorkModeTile> provider10, Provider<RotationLockTile> provider11, Provider<FlashlightTile> provider12, Provider<LocationTile> provider13, Provider<CastTile> provider14, Provider<HotspotTile> provider15, Provider<UserTile> provider16, Provider<BatterySaverTile> provider17, Provider<DataSaverTile> provider18, Provider<NightDisplayTile> provider19, Provider<NfcTile> provider20, Provider<GarbageMonitor.MemoryTile> provider21, Provider<UiModeNightTile> provider22, Provider<ScreenRecordTile> provider23, Provider<ReduceBrightColorsTile> provider24, Provider<CameraToggleTile> provider25, Provider<MicrophoneToggleTile> provider26, Provider<DeviceControlsTile> provider27, Provider<AlarmTile> provider28, Provider<QuickAccessWalletTile> provider29) {
        return new QSFactoryImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29);
    }

    public static QSFactoryImpl newInstance(Lazy<QSHost> lazy, Provider<CustomTile.Builder> provider, Provider<WifiTile> provider2, Provider<InternetTile> provider3, Provider<BluetoothTile> provider4, Provider<CellularTile> provider5, Provider<DndTile> provider6, Provider<ColorInversionTile> provider7, Provider<AirplaneModeTile> provider8, Provider<WorkModeTile> provider9, Provider<RotationLockTile> provider10, Provider<FlashlightTile> provider11, Provider<LocationTile> provider12, Provider<CastTile> provider13, Provider<HotspotTile> provider14, Provider<UserTile> provider15, Provider<BatterySaverTile> provider16, Provider<DataSaverTile> provider17, Provider<NightDisplayTile> provider18, Provider<NfcTile> provider19, Provider<GarbageMonitor.MemoryTile> provider20, Provider<UiModeNightTile> provider21, Provider<ScreenRecordTile> provider22, Provider<ReduceBrightColorsTile> provider23, Provider<CameraToggleTile> provider24, Provider<MicrophoneToggleTile> provider25, Provider<DeviceControlsTile> provider26, Provider<AlarmTile> provider27, Provider<QuickAccessWalletTile> provider28) {
        return new QSFactoryImpl(lazy, provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28);
    }
}
