package com.android.systemui.media;

import android.app.Notification;
import android.service.notification.StatusBarNotification;
import com.android.internal.annotations.VisibleForTesting;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaDataManager.kt */
/* loaded from: classes.dex */
public final class MediaDataManagerKt {
    private static final String[] ART_URIS = {"android.media.metadata.ALBUM_ART_URI", "android.media.metadata.ART_URI", "android.media.metadata.DISPLAY_ICON_URI"};
    private static final MediaData LOADING = new MediaData(-1, false, 0, null, null, null, null, null, CollectionsKt__CollectionsKt.emptyList(), CollectionsKt__CollectionsKt.emptyList(), "INVALID", null, null, null, true, null, false, false, null, false, null, false, 0, 8323072, null);
    private static final SmartspaceMediaData EMPTY_SMARTSPACE_MEDIA_DATA = new SmartspaceMediaData("INVALID", false, false, "INVALID", null, CollectionsKt__CollectionsKt.emptyList(), 0);

    @VisibleForTesting
    public static /* synthetic */ void getEMPTY_SMARTSPACE_MEDIA_DATA$annotations() {
    }

    public static final SmartspaceMediaData getEMPTY_SMARTSPACE_MEDIA_DATA() {
        return EMPTY_SMARTSPACE_MEDIA_DATA;
    }

    public static final boolean isMediaNotification(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        if (!statusBarNotification.getNotification().hasMediaSession()) {
            return false;
        }
        Class notificationStyle = statusBarNotification.getNotification().getNotificationStyle();
        if (Notification.DecoratedMediaCustomViewStyle.class.equals(notificationStyle) || Notification.MediaStyle.class.equals(notificationStyle)) {
            return true;
        }
        return false;
    }
}
