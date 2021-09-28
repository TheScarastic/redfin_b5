package com.android.systemui.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.provider.Settings;
import android.view.ContextThemeWrapper;
import com.android.systemui.R$bool;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.FeatureFlags;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class Utils {
    public static <T> void safeForeach(List<T> list, Consumer<T> consumer) {
        for (int size = list.size() - 1; size >= 0; size--) {
            T t = list.get(size);
            if (t != null) {
                consumer.accept(t);
            }
        }
    }

    public static boolean isHeadlessRemoteDisplayProvider(PackageManager packageManager, String str) {
        if (packageManager.checkPermission("android.permission.REMOTE_DISPLAY_PROVIDER", str) != 0) {
            return false;
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setPackage(str);
        return packageManager.queryIntentActivities(intent, 0).isEmpty();
    }

    public static boolean isGesturalModeOnDefaultDisplay(Context context, int i) {
        return context.getDisplayId() == 0 && QuickStepContract.isGesturalMode(i);
    }

    public static boolean useQsMediaPlayer(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "qs_media_controls", 1) > 0;
    }

    public static boolean useMediaResumption(Context context) {
        int i = Settings.Secure.getInt(context.getContentResolver(), "qs_media_resumption", 1);
        if (!useQsMediaPlayer(context) || i <= 0) {
            return false;
        }
        return true;
    }

    public static boolean allowMediaRecommendations(Context context) {
        int i = Settings.Secure.getInt(context.getContentResolver(), "qs_media_recommend", 1);
        if (!useQsMediaPlayer(context) || i <= 0) {
            return false;
        }
        return true;
    }

    public static boolean shouldUseSplitNotificationShade(FeatureFlags featureFlags, Resources resources) {
        return featureFlags.isTwoColumnNotificationShadeEnabled() && resources.getBoolean(R$bool.config_use_split_notification_shade);
    }

    public static int getPrivateAttrColorIfUnset(ContextThemeWrapper contextThemeWrapper, TypedArray typedArray, int i, int i2, int i3) {
        if (typedArray.hasValue(i)) {
            return typedArray.getColor(i, i2);
        }
        TypedArray obtainStyledAttributes = contextThemeWrapper.obtainStyledAttributes(new int[]{i3});
        int color = obtainStyledAttributes.getColor(0, i2);
        obtainStyledAttributes.recycle();
        return color;
    }
}
