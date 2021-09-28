package com.android.systemui.statusbar.notification;

import android.content.Context;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.Utils;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationSectionsFeatureManager.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsFeatureManager {
    private final Context context;
    private final DeviceConfigProxy proxy;

    public NotificationSectionsFeatureManager(DeviceConfigProxy deviceConfigProxy, Context context) {
        Intrinsics.checkNotNullParameter(deviceConfigProxy, "proxy");
        Intrinsics.checkNotNullParameter(context, "context");
        this.proxy = deviceConfigProxy;
        this.context = context;
    }

    public final boolean isFilteringEnabled() {
        return NotificationSectionsFeatureManagerKt.access$usePeopleFiltering(this.proxy);
    }

    public final boolean isMediaControlsEnabled() {
        return Utils.useQsMediaPlayer(this.context);
    }

    public final int[] getNotificationBuckets() {
        if (isFilteringEnabled() && isMediaControlsEnabled()) {
            return new int[]{2, 3, 1, 4, 5, 6};
        }
        if (!isFilteringEnabled() && isMediaControlsEnabled()) {
            return new int[]{2, 3, 1, 5, 6};
        }
        if (!isFilteringEnabled() || isMediaControlsEnabled()) {
            return new int[]{5, 6};
        }
        return new int[]{2, 3, 4, 5, 6};
    }

    public final int getNumberOfBuckets() {
        return getNotificationBuckets().length;
    }

    @VisibleForTesting
    public final void clearCache() {
        NotificationSectionsFeatureManagerKt.access$setSUsePeopleFiltering$p(null);
    }
}
