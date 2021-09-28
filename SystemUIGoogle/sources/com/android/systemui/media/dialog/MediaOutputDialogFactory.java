package com.android.systemui.media.dialog;

import android.content.Context;
import android.media.session.MediaSessionManager;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.phone.ShadeController;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaOutputDialogFactory.kt */
/* loaded from: classes.dex */
public final class MediaOutputDialogFactory {
    public static final Companion Companion = new Companion(null);
    private static MediaOutputDialog mediaOutputDialog;
    private final Context context;
    private final LocalBluetoothManager lbm;
    private final MediaSessionManager mediaSessionManager;
    private final NotificationEntryManager notificationEntryManager;
    private final ShadeController shadeController;
    private final ActivityStarter starter;
    private final UiEventLogger uiEventLogger;

    public MediaOutputDialogFactory(Context context, MediaSessionManager mediaSessionManager, LocalBluetoothManager localBluetoothManager, ShadeController shadeController, ActivityStarter activityStarter, NotificationEntryManager notificationEntryManager, UiEventLogger uiEventLogger) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(mediaSessionManager, "mediaSessionManager");
        Intrinsics.checkNotNullParameter(shadeController, "shadeController");
        Intrinsics.checkNotNullParameter(activityStarter, "starter");
        Intrinsics.checkNotNullParameter(notificationEntryManager, "notificationEntryManager");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        this.context = context;
        this.mediaSessionManager = mediaSessionManager;
        this.lbm = localBluetoothManager;
        this.shadeController = shadeController;
        this.starter = activityStarter;
        this.notificationEntryManager = notificationEntryManager;
        this.uiEventLogger = uiEventLogger;
    }

    /* compiled from: MediaOutputDialogFactory.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final MediaOutputDialog getMediaOutputDialog() {
            return MediaOutputDialogFactory.mediaOutputDialog;
        }

        public final void setMediaOutputDialog(MediaOutputDialog mediaOutputDialog) {
            MediaOutputDialogFactory.mediaOutputDialog = mediaOutputDialog;
        }
    }

    public final void create(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "packageName");
        Companion companion = Companion;
        MediaOutputDialog mediaOutputDialog2 = companion.getMediaOutputDialog();
        if (mediaOutputDialog2 != null) {
            mediaOutputDialog2.dismiss();
        }
        companion.setMediaOutputDialog(new MediaOutputDialog(this.context, z, new MediaOutputController(this.context, str, z, this.mediaSessionManager, this.lbm, this.shadeController, this.starter, this.notificationEntryManager, this.uiEventLogger), this.uiEventLogger));
    }

    public final void dismiss() {
        Companion companion = Companion;
        MediaOutputDialog mediaOutputDialog2 = companion.getMediaOutputDialog();
        if (mediaOutputDialog2 != null) {
            mediaOutputDialog2.dismiss();
        }
        companion.setMediaOutputDialog(null);
    }
}
