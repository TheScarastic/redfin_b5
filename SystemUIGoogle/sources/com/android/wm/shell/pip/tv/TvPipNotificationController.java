package com.android.wm.shell.pip.tv;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import com.android.wm.shell.R;
import com.android.wm.shell.pip.PipMediaController;
import java.util.Objects;
/* loaded from: classes2.dex */
public class TvPipNotificationController {
    private final ActionBroadcastReceiver mActionBroadcastReceiver = new ActionBroadcastReceiver();
    private Bitmap mArt;
    private final Context mContext;
    private String mDefaultTitle;
    private Delegate mDelegate;
    private final Handler mMainHandler;
    private String mMediaTitle;
    private final Notification.Builder mNotificationBuilder;
    private final NotificationManager mNotificationManager;
    private boolean mNotified;
    private final PackageManager mPackageManager;
    private String mPackageName;

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface Delegate {
        void closePip();

        void showPictureInPictureMenu();
    }

    public TvPipNotificationController(Context context, PipMediaController pipMediaController, Handler handler) {
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mMainHandler = handler;
        this.mNotificationBuilder = new Notification.Builder(context, "TVPIP").setLocalOnly(true).setOngoing(false).setCategory("sys").setShowWhen(true).setSmallIcon(R.drawable.pip_icon).extend(new Notification.TvExtender().setContentIntent(createPendingIntent(context, "com.android.wm.shell.pip.tv.notification.action.SHOW_PIP_MENU")).setDeleteIntent(createPendingIntent(context, "com.android.wm.shell.pip.tv.notification.action.CLOSE_PIP")));
        pipMediaController.addMetadataListener(new PipMediaController.MetadataListener() { // from class: com.android.wm.shell.pip.tv.TvPipNotificationController$$ExternalSyntheticLambda0
            @Override // com.android.wm.shell.pip.PipMediaController.MetadataListener
            public final void onMediaMetadataChanged(MediaMetadata mediaMetadata) {
                TvPipNotificationController.this.onMediaMetadataChanged(mediaMetadata);
            }
        });
        onConfigurationChanged(context);
    }

    /* access modifiers changed from: package-private */
    public void setDelegate(Delegate delegate) {
        Log.d("TvPipNotification", "setDelegate(), delegate=" + delegate);
        if (this.mDelegate != null) {
            throw new IllegalStateException("The delegate has already been set and should not change.");
        } else if (delegate != null) {
            this.mDelegate = delegate;
        } else {
            throw new IllegalArgumentException("The delegate must not be null.");
        }
    }

    /* access modifiers changed from: package-private */
    public void show(String str) {
        if (this.mDelegate != null) {
            this.mPackageName = str;
            update();
            this.mActionBroadcastReceiver.register();
            return;
        }
        throw new IllegalStateException("Delegate is not set.");
    }

    /* access modifiers changed from: package-private */
    public void dismiss() {
        this.mNotificationManager.cancel("TvPip", 1100);
        this.mNotified = false;
        this.mPackageName = null;
        this.mActionBroadcastReceiver.unregister();
    }

    /* access modifiers changed from: private */
    public void onMediaMetadataChanged(MediaMetadata mediaMetadata) {
        if (updateMediaControllerMetadata(mediaMetadata) && this.mNotified) {
            update();
        }
    }

    /* access modifiers changed from: package-private */
    public void onConfigurationChanged(Context context) {
        this.mDefaultTitle = context.getResources().getString(R.string.pip_notification_unknown_title);
        if (this.mNotified) {
            update();
        }
    }

    private void update() {
        this.mNotified = true;
        this.mNotificationBuilder.setWhen(System.currentTimeMillis()).setContentTitle(getNotificationTitle());
        if (this.mArt != null) {
            this.mNotificationBuilder.setStyle(new Notification.BigPictureStyle().bigPicture(this.mArt));
        } else {
            this.mNotificationBuilder.setStyle(null);
        }
        this.mNotificationManager.notify("TvPip", 1100, this.mNotificationBuilder.build());
    }

    private boolean updateMediaControllerMetadata(MediaMetadata mediaMetadata) {
        Bitmap bitmap;
        String str = null;
        if (mediaMetadata != null) {
            str = mediaMetadata.getString("android.media.metadata.DISPLAY_TITLE");
            if (TextUtils.isEmpty(str)) {
                str = mediaMetadata.getString("android.media.metadata.TITLE");
            }
            Bitmap bitmap2 = mediaMetadata.getBitmap("android.media.metadata.ALBUM_ART");
            bitmap = bitmap2 == null ? mediaMetadata.getBitmap("android.media.metadata.ART") : bitmap2;
        } else {
            bitmap = null;
        }
        if (TextUtils.equals(str, this.mMediaTitle) && Objects.equals(bitmap, this.mArt)) {
            return false;
        }
        this.mMediaTitle = str;
        this.mArt = bitmap;
        return true;
    }

    private String getNotificationTitle() {
        if (!TextUtils.isEmpty(this.mMediaTitle)) {
            return this.mMediaTitle;
        }
        String applicationLabel = getApplicationLabel(this.mPackageName);
        if (!TextUtils.isEmpty(applicationLabel)) {
            return applicationLabel;
        }
        return this.mDefaultTitle;
    }

    private String getApplicationLabel(String str) {
        try {
            return this.mPackageManager.getApplicationLabel(this.mPackageManager.getApplicationInfo(str, 0)).toString();
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    private static PendingIntent createPendingIntent(Context context, String str) {
        return PendingIntent.getBroadcast(context, 0, new Intent(str).setPackage(context.getPackageName()), 335544320);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class ActionBroadcastReceiver extends BroadcastReceiver {
        final IntentFilter mIntentFilter;
        boolean mRegistered;

        private ActionBroadcastReceiver() {
            IntentFilter intentFilter = new IntentFilter();
            this.mIntentFilter = intentFilter;
            intentFilter.addAction("com.android.wm.shell.pip.tv.notification.action.CLOSE_PIP");
            intentFilter.addAction("com.android.wm.shell.pip.tv.notification.action.SHOW_PIP_MENU");
            this.mRegistered = false;
        }

        void register() {
            if (!this.mRegistered) {
                TvPipNotificationController.this.mContext.registerReceiverForAllUsers(this, this.mIntentFilter, "com.android.systemui.permission.SELF", TvPipNotificationController.this.mMainHandler);
                this.mRegistered = true;
            }
        }

        void unregister() {
            if (this.mRegistered) {
                TvPipNotificationController.this.mContext.unregisterReceiver(this);
                this.mRegistered = false;
            }
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("TvPipNotification", "on(Broadcast)Receive(), action=" + action);
            if ("com.android.wm.shell.pip.tv.notification.action.SHOW_PIP_MENU".equals(action)) {
                TvPipNotificationController.this.mDelegate.showPictureInPictureMenu();
            } else if ("com.android.wm.shell.pip.tv.notification.action.CLOSE_PIP".equals(action)) {
                TvPipNotificationController.this.mDelegate.closePip();
            }
        }
    }
}
