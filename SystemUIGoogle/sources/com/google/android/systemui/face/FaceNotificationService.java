package com.google.android.systemui.face;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import com.android.systemui.R$string;
/* loaded from: classes2.dex */
public class FaceNotificationService {
    private FaceNotificationBroadcastReceiver mBroadcastReceiver;
    private Context mContext;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.google.android.systemui.face.FaceNotificationService.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricError(int i, String str, BiometricSourceType biometricSourceType) {
            if (i == 1004) {
                FaceNotificationSettings.updateReenrollSetting(FaceNotificationService.this.mContext, 3);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricHelp(int i, String str, BiometricSourceType biometricSourceType) {
            if (i == 13) {
                FaceNotificationSettings.updateReenrollSetting(FaceNotificationService.this.mContext, 1);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserUnlocked() {
            if (FaceNotificationService.this.mNotificationQueued) {
                Log.d("FaceNotificationService", "Not showing notification; already queued.");
            } else if (FaceNotificationSettings.isReenrollRequired(FaceNotificationService.this.mContext)) {
                FaceNotificationService.this.queueReenrollNotification();
            }
        }
    };
    private boolean mNotificationQueued;

    public FaceNotificationService(Context context) {
        this.mContext = context;
        start();
    }

    private void start() {
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mBroadcastReceiver = new FaceNotificationBroadcastReceiver(this.mContext);
    }

    /* access modifiers changed from: private */
    public void queueReenrollNotification() {
        this.mNotificationQueued = true;
        this.mHandler.postDelayed(new Runnable(this.mContext.getString(R$string.face_reenroll_notification_title), this.mContext.getString(R$string.face_reenroll_notification_content)) { // from class: com.google.android.systemui.face.FaceNotificationService$$ExternalSyntheticLambda0
            public final /* synthetic */ String f$1;
            public final /* synthetic */ String f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                FaceNotificationService.this.lambda$queueReenrollNotification$0(this.f$1, this.f$2);
            }
        }, 10000);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$queueReenrollNotification$0(String str, String str2) {
        showNotification("face_action_show_reenroll_dialog", str, str2);
    }

    private void showNotification(String str, CharSequence charSequence, CharSequence charSequence2) {
        this.mNotificationQueued = false;
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        if (notificationManager == null) {
            Log.e("FaceNotificationService", "Failed to show notification " + str + ". Notification manager is null!");
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(str);
        intentFilter.addAction("face_action_notification_dismissed");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
        PendingIntent broadcastAsUser = PendingIntent.getBroadcastAsUser(this.mContext, 0, new Intent(str), 0, UserHandle.CURRENT);
        PendingIntent broadcastAsUser2 = PendingIntent.getBroadcastAsUser(this.mContext, 0, new Intent("face_action_notification_dismissed"), 0, UserHandle.CURRENT);
        String string = this.mContext.getString(R$string.face_notification_name);
        NotificationChannel notificationChannel = new NotificationChannel("FaceHiPriNotificationChannel", string, 4);
        Notification build = new Notification.Builder(this.mContext, "FaceHiPriNotificationChannel").setCategory("sys").setSmallIcon(17302479).setContentTitle(charSequence).setContentText(charSequence2).setSubText(string).setContentIntent(broadcastAsUser).setDeleteIntent(broadcastAsUser2).setAutoCancel(true).setLocalOnly(true).setOnlyAlertOnce(true).setVisibility(-1).build();
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notifyAsUser("FaceNotificationService", 1, build, UserHandle.CURRENT);
    }
}
