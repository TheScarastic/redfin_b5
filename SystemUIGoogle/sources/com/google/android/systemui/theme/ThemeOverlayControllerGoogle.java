package com.google.android.systemui.theme;

import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.om.FabricatedOverlay;
import android.os.Handler;
import android.os.UserManager;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.theme.ThemeOverlayController;
import com.android.systemui.util.settings.SecureSettings;
import com.google.material.monet.ColorScheme;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ThemeOverlayControllerGoogle.kt */
/* loaded from: classes2.dex */
public final class ThemeOverlayControllerGoogle extends ThemeOverlayController {
    private ColorScheme colorScheme;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ThemeOverlayControllerGoogle(Context context, BroadcastDispatcher broadcastDispatcher, Handler handler, Executor executor, Executor executor2, ThemeOverlayApplier themeOverlayApplier, SecureSettings secureSettings, WallpaperManager wallpaperManager, UserManager userManager, DumpManager dumpManager, DeviceProvisionedController deviceProvisionedController, UserTracker userTracker, FeatureFlags featureFlags, WakefulnessLifecycle wakefulnessLifecycle) {
        super(context, broadcastDispatcher, handler, executor, executor2, themeOverlayApplier, secureSettings, wallpaperManager, userManager, deviceProvisionedController, userTracker, dumpManager, featureFlags, wakefulnessLifecycle);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(handler, "bgHandler");
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        Intrinsics.checkNotNullParameter(executor2, "bgExecutor");
        Intrinsics.checkNotNullParameter(themeOverlayApplier, "themeOverlayApplier");
        Intrinsics.checkNotNullParameter(secureSettings, "secureSettings");
        Intrinsics.checkNotNullParameter(wallpaperManager, "wallpaperManager");
        Intrinsics.checkNotNullParameter(userManager, "userManager");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(deviceProvisionedController, "deviceProvisionedController");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle, "wakefulnessLifecycle");
    }

    @Override // com.android.systemui.theme.ThemeOverlayController
    protected int getNeutralColor(WallpaperColors wallpaperColors) {
        Intrinsics.checkNotNullParameter(wallpaperColors, "wallpaperColors");
        return ColorScheme.Companion.getSeedColor(wallpaperColors);
    }

    private final boolean inDarkMode() {
        int i = this.mContext.getResources().getConfiguration().uiMode & 48;
        return (i == 0 || i == 16 || i != 32) ? false : true;
    }

    @Override // com.android.systemui.theme.ThemeOverlayController
    protected int getAccentColor(WallpaperColors wallpaperColors) {
        Intrinsics.checkNotNullParameter(wallpaperColors, "wallpaperColors");
        return ColorScheme.Companion.getSeedColor(wallpaperColors);
    }

    @Override // com.android.systemui.theme.ThemeOverlayController
    protected FabricatedOverlay getOverlay(int i, int i2) {
        List<Integer> list;
        String str;
        String str2;
        ColorScheme colorScheme = new ColorScheme(i, inDarkMode());
        this.colorScheme = colorScheme;
        if (i2 == 1) {
            Intrinsics.checkNotNull(colorScheme);
            list = colorScheme.getAllAccentColors();
            str = "accent";
        } else {
            Intrinsics.checkNotNull(colorScheme);
            list = colorScheme.getAllNeutralColors();
            str = "neutral";
        }
        ColorScheme colorScheme2 = this.colorScheme;
        Intrinsics.checkNotNull(colorScheme2);
        int size = colorScheme2.getAccent1().size();
        FabricatedOverlay.Builder builder = new FabricatedOverlay.Builder("com.android.systemui", str, "android");
        int i3 = 0;
        int size2 = list.size() - 1;
        if (size2 >= 0) {
            while (true) {
                int i4 = i3 + 1;
                int i5 = i3 % size;
                int i6 = (i3 / size) + 1;
                if (i5 == 0) {
                    str2 = "android:color/system_" + str + i6 + "_10";
                } else if (i5 != 1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("android:color/system_");
                    sb.append(str);
                    sb.append(i6);
                    sb.append('_');
                    sb.append(i5 - 1);
                    sb.append("00");
                    str2 = sb.toString();
                } else {
                    str2 = "android:color/system_" + str + i6 + "_50";
                }
                builder.setResourceValue(str2, 28, ColorUtils.setAlphaComponent(list.get(i3).intValue(), 255));
                if (i4 > size2) {
                    break;
                }
                i3 = i4;
            }
        }
        return builder.build();
    }

    @Override // com.android.systemui.theme.ThemeOverlayController, com.android.systemui.SystemUI, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        super.dump(fileDescriptor, printWriter, strArr);
        printWriter.println(Intrinsics.stringPlus("colorScheme: ", this.colorScheme));
        printWriter.println("ThemeOverlayControllerGoogle: yes");
    }
}
