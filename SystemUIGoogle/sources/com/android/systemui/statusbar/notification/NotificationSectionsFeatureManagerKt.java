package com.android.systemui.statusbar.notification;

import com.android.systemui.util.DeviceConfigProxy;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationSectionsFeatureManager.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsFeatureManagerKt {
    private static Boolean sUsePeopleFiltering;

    public static final boolean usePeopleFiltering(DeviceConfigProxy deviceConfigProxy) {
        if (sUsePeopleFiltering == null) {
            sUsePeopleFiltering = Boolean.valueOf(deviceConfigProxy.getBoolean("systemui", "notifications_use_people_filtering", true));
        }
        Boolean bool = sUsePeopleFiltering;
        Intrinsics.checkNotNull(bool);
        return bool.booleanValue();
    }
}
