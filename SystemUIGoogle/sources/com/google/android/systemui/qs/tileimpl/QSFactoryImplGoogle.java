package com.google.android.systemui.qs.tileimpl;

import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.tileimpl.QSFactoryImpl;
import com.android.systemui.qs.tileimpl.QSTileImpl;
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
import com.google.android.systemui.qs.tiles.BatterySaverTileGoogle;
import com.google.android.systemui.qs.tiles.OverlayToggleTile;
import com.google.android.systemui.qs.tiles.ReverseChargingTile;
import dagger.Lazy;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public class QSFactoryImplGoogle extends QSFactoryImpl {
    private final Provider<BatterySaverTileGoogle> mBatterySaverTileGoogleProvider;
    private final Provider<OverlayToggleTile> mOverlayToggleTileProvider;
    private final Provider<ReverseChargingTile> mReverseChargingTileProvider;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public QSFactoryImplGoogle(Lazy<QSHost> lazy, Provider<CustomTile.Builder> provider, Provider<WifiTile> provider2, Provider<InternetTile> provider3, Provider<BluetoothTile> provider4, Provider<CellularTile> provider5, Provider<DndTile> provider6, Provider<ColorInversionTile> provider7, Provider<AirplaneModeTile> provider8, Provider<WorkModeTile> provider9, Provider<RotationLockTile> provider10, Provider<FlashlightTile> provider11, Provider<LocationTile> provider12, Provider<CastTile> provider13, Provider<HotspotTile> provider14, Provider<UserTile> provider15, Provider<BatterySaverTileGoogle> provider16, Provider<DataSaverTile> provider17, Provider<NightDisplayTile> provider18, Provider<NfcTile> provider19, Provider<GarbageMonitor.MemoryTile> provider20, Provider<UiModeNightTile> provider21, Provider<ScreenRecordTile> provider22, Provider<ReverseChargingTile> provider23, Provider<ReduceBrightColorsTile> provider24, Provider<CameraToggleTile> provider25, Provider<MicrophoneToggleTile> provider26, Provider<DeviceControlsTile> provider27, Provider<AlarmTile> provider28, Provider<OverlayToggleTile> provider29, Provider<QuickAccessWalletTile> provider30) {
        super(lazy, provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, new Provider() { // from class: com.google.android.systemui.qs.tileimpl.QSFactoryImplGoogle$$ExternalSyntheticLambda0
            @Override // javax.inject.Provider
            public final Object get() {
                return (BatterySaverTile) Provider.this.get();
            }
        }, provider17, provider18, provider19, provider20, provider21, provider22, provider24, provider25, provider26, provider27, provider28, provider30);
        Objects.requireNonNull(provider16);
        this.mReverseChargingTileProvider = provider23;
        this.mBatterySaverTileGoogleProvider = provider16;
        this.mOverlayToggleTileProvider = provider29;
    }

    @Override // com.android.systemui.qs.tileimpl.QSFactoryImpl, com.android.systemui.plugins.qs.QSFactory
    public QSTile createTile(String str) {
        QSTileImpl createTileInternal = createTileInternal(str);
        if (createTileInternal != null) {
            return createTileInternal;
        }
        return super.createTile(str);
    }

    private QSTileImpl createTileInternal(String str) {
        str.hashCode();
        if (str.equals("ott")) {
            return this.mOverlayToggleTileProvider.get();
        }
        if (!str.equals("reverse")) {
            return null;
        }
        return this.mReverseChargingTileProvider.get();
    }
}
