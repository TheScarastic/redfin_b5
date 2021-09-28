package com.google.android.apps.wallpaper.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.android.customization.model.color.ColorUtils;
import com.android.wallpaper.compat.BuildCompat;
import com.android.wallpaper.module.GoogleWallpapersInjector;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.util.DiskBasedLogger;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.JobKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class ThemeSanitizerReceiver extends BroadcastReceiver {
    @NotNull
    public final CoroutineScope scope = CoroutineScopeKt.CoroutineScope(Dispatchers.Default.plus(JobKt.SupervisorJob$default(null, 1)));

    public ThemeSanitizerReceiver() {
        Dispatchers dispatchers = Dispatchers.INSTANCE;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (intent.getAction() != null && (Intrinsics.areEqual(intent.getAction(), "android.intent.action.MY_PACKAGE_REPLACED") || Intrinsics.areEqual(intent.getAction(), "android.intent.action.BOOT_COMPLETED"))) {
            if (BuildCompat.sSdk >= 31 || "S".equals(Build.VERSION.CODENAME)) {
                if (!ColorUtils.isMonetEnabled(context)) {
                    Log.i("ThemeSanitizerReceiver", "Skipping theme sanitizer");
                    return;
                }
                Injector injector = InjectorProvider.getInjector();
                Objects.requireNonNull(injector, "null cannot be cast to non-null type com.android.wallpaper.module.GoogleWallpapersInjector");
                GoogleWallpaperPreferences googlePreferences = ((GoogleWallpapersInjector) injector).getGooglePreferences(context);
                if (googlePreferences.isThemesSanitized()) {
                    Log.d("ThemeSanitizerReceiver", "Already sanitized, skipping theme sanitizer");
                    return;
                } else {
                    BuildersKt.launch$default(this.scope, null, null, new ThemeSanitizerReceiver$onReceive$1(this, context, googlePreferences, goAsync(), null), 3, null);
                    return;
                }
            }
        }
        DiskBasedLogger.e("ThemeSanitizerReceiver", "Unexpected action or Android version!", context);
        throw new IllegalStateException("Unexpected broadcast action or unsupported Android version");
    }
}
