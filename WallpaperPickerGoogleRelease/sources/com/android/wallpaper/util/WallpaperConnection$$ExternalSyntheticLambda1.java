package com.android.wallpaper.util;

import android.app.Activity;
import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.util.Log;
import com.android.customization.model.grid.GridOption;
import com.android.wallpaper.util.WallpaperConnection;
import com.google.android.apps.wallpaper.module.ClearcutUserEventLogger;
import com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle;
import com.google.wireless.android.apps.wallpaper.WallpaperLogProto$WallpaperEvent;
import java.util.Objects;
/* loaded from: classes.dex */
public final /* synthetic */ class WallpaperConnection$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId = 3;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ WallpaperConnection$$ExternalSyntheticLambda1(int i, Activity activity, WallpaperManager wallpaperManager) {
        this.f$2 = i;
        this.f$0 = activity;
        this.f$1 = wallpaperManager;
    }

    public /* synthetic */ WallpaperConnection$$ExternalSyntheticLambda1(WallpaperConnection wallpaperConnection, WallpaperColors wallpaperColors, int i) {
        this.f$0 = wallpaperConnection;
        this.f$1 = wallpaperColors;
        this.f$2 = i;
    }

    public /* synthetic */ WallpaperConnection$$ExternalSyntheticLambda1(ClearcutUserEventLogger clearcutUserEventLogger, int i, GridOption gridOption) {
        this.f$0 = clearcutUserEventLogger;
        this.f$2 = i;
        this.f$1 = gridOption;
    }

    public /* synthetic */ WallpaperConnection$$ExternalSyntheticLambda1(ClearcutUserEventLogger clearcutUserEventLogger, int i, String str) {
        this.f$0 = clearcutUserEventLogger;
        this.f$2 = i;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                WallpaperColors wallpaperColors = (WallpaperColors) this.f$1;
                int i = this.f$2;
                WallpaperConnection.WallpaperConnectionListener wallpaperConnectionListener = ((WallpaperConnection) this.f$0).mListener;
                if (wallpaperConnectionListener != null) {
                    wallpaperConnectionListener.onWallpaperColorsChanged(wallpaperColors, i);
                    return;
                }
                return;
            case 1:
                ClearcutUserEventLogger clearcutUserEventLogger = (ClearcutUserEventLogger) this.f$0;
                int i2 = this.f$2;
                String str = (String) this.f$1;
                boolean z = ClearcutUserEventLogger.IS_VERBOSE;
                Objects.requireNonNull(clearcutUserEventLogger);
                WallpaperLogProto$WallpaperEvent.Builder newBuilder = WallpaperLogProto$WallpaperEvent.newBuilder();
                newBuilder.setType(WallpaperLogProto$WallpaperEvent.Type.forNumber(i2));
                if (str != null) {
                    newBuilder.copyOnWrite();
                    WallpaperLogProto$WallpaperEvent.access$800((WallpaperLogProto$WallpaperEvent) newBuilder.instance, str);
                }
                clearcutUserEventLogger.log(newBuilder.build());
                return;
            case 2:
                ClearcutUserEventLogger clearcutUserEventLogger2 = (ClearcutUserEventLogger) this.f$0;
                int i3 = this.f$2;
                boolean z2 = ClearcutUserEventLogger.IS_VERBOSE;
                Objects.requireNonNull(clearcutUserEventLogger2);
                WallpaperLogProto$WallpaperEvent.Builder newBuilder2 = WallpaperLogProto$WallpaperEvent.newBuilder();
                newBuilder2.setType(WallpaperLogProto$WallpaperEvent.Type.forNumber(i3));
                int hashCode = ((GridOption) this.f$1).mTitle.hashCode();
                newBuilder2.copyOnWrite();
                WallpaperLogProto$WallpaperEvent.access$2400((WallpaperLogProto$WallpaperEvent) newBuilder2.instance, hashCode);
                clearcutUserEventLogger2.log(newBuilder2.build());
                return;
            default:
                int i4 = this.f$2;
                Activity activity = (Activity) this.f$0;
                WallpaperManager wallpaperManager = (WallpaperManager) this.f$1;
                int i5 = MicropaperPreviewFragmentGoogle.$r8$clinit;
                boolean z3 = true;
                if (i4 == 1) {
                    Log.e("MicropaperPreviewFragmentGoogle", "Can not set micropaper on lock screen only.");
                }
                if (i4 != 2) {
                    z3 = false;
                }
                MicropaperPreviewFragmentGoogle.setMicropaperComponentAndReturn(activity, wallpaperManager, z3);
                return;
        }
    }
}
