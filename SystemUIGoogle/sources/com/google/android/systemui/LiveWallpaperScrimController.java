package com.google.android.systemui;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IWallpaperManager;
import android.app.WallpaperInfo;
import android.content.ComponentName;
import android.os.Handler;
import android.os.RemoteException;
import android.util.ArraySet;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.DejankUtils;
import com.android.systemui.dock.DockManager;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LockscreenWallpaper;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ScrimState;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.google.android.collect.Sets;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
/* loaded from: classes2.dex */
public class LiveWallpaperScrimController extends ScrimController {
    private static ArraySet<ComponentName> REDUCED_SCRIM_WALLPAPERS = Sets.newArraySet(new ComponentName[]{new ComponentName("com.breel.geswallpapers", "com.breel.geswallpapers.wallpapers.EarthWallpaperService"), new ComponentName("com.breel.wallpapers18", "com.breel.wallpapers18.delight.wallpapers.DelightWallpaperV1"), new ComponentName("com.breel.wallpapers18", "com.breel.wallpapers18.delight.wallpapers.DelightWallpaperV2"), new ComponentName("com.breel.wallpapers18", "com.breel.wallpapers18.delight.wallpapers.DelightWallpaperV3"), new ComponentName("com.breel.wallpapers18", "com.breel.wallpapers18.surfandturf.wallpapers.variations.SurfAndTurfWallpaperV2"), new ComponentName("com.breel.wallpapers18", "com.breel.wallpapers18.cities.wallpapers.variations.SanFranciscoWallpaper"), new ComponentName("com.breel.wallpapers18", "com.breel.wallpapers18.cities.wallpapers.variations.NewYorkWallpaper")});
    private int mCurrentUser = ActivityManager.getCurrentUser();
    private final LockscreenWallpaper mLockscreenWallpaper;
    private final IWallpaperManager mWallpaperManager;

    public LiveWallpaperScrimController(LightBarController lightBarController, DozeParameters dozeParameters, AlarmManager alarmManager, KeyguardStateController keyguardStateController, DelayedWakeLock.Builder builder, Handler handler, IWallpaperManager iWallpaperManager, LockscreenWallpaper lockscreenWallpaper, KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController, DockManager dockManager, Executor executor, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        super(lightBarController, dozeParameters, alarmManager, keyguardStateController, builder, handler, keyguardUpdateMonitor, dockManager, configurationController, executor, unlockedScreenOffAnimationController);
        this.mWallpaperManager = iWallpaperManager;
        this.mLockscreenWallpaper = lockscreenWallpaper;
    }

    @Override // com.android.systemui.statusbar.phone.ScrimController
    public void transitionTo(ScrimState scrimState) {
        if (scrimState == ScrimState.KEYGUARD) {
            updateScrimValues();
        }
        super.transitionTo(scrimState);
    }

    private void updateScrimValues() {
        if (isReducedScrimWallpaperSet()) {
            setScrimBehindValues(0.25f);
        } else {
            setScrimBehindValues(0.2f);
        }
    }

    @Override // com.android.systemui.statusbar.phone.ScrimController
    public void setCurrentUser(int i) {
        this.mCurrentUser = i;
        updateScrimValues();
    }

    private boolean isReducedScrimWallpaperSet() {
        return ((Boolean) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.google.android.systemui.LiveWallpaperScrimController$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return LiveWallpaperScrimController.m609$r8$lambda$B7g8AZwVmAAO1vc8SP3KAir8mY(LiveWallpaperScrimController.this);
            }
        })).booleanValue();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isReducedScrimWallpaperSet$0() {
        try {
            WallpaperInfo wallpaperInfo = this.mWallpaperManager.getWallpaperInfo(this.mCurrentUser);
            if (wallpaperInfo != null && REDUCED_SCRIM_WALLPAPERS.contains(wallpaperInfo.getComponent())) {
                return Boolean.valueOf(this.mLockscreenWallpaper.getBitmap() == null);
            }
        } catch (RemoteException unused) {
        }
        return Boolean.FALSE;
    }
}
