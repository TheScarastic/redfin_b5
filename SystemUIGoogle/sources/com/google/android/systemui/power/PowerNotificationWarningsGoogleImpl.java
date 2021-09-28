package com.google.android.systemui.power;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IHwBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.power.PowerNotificationWarnings;
import com.android.systemui.util.NotificationChannels;
import java.util.NoSuchElementException;
import vendor.google.google_battery.V1_1.IGoogleBattery;
/* loaded from: classes2.dex */
public final class PowerNotificationWarningsGoogleImpl extends PowerNotificationWarnings {
    @VisibleForTesting
    static final String ACTION_RESUME_CHARGING = "PNW.defenderResumeCharging";
    @VisibleForTesting
    static final int NOTIFICATION_ID = R$string.defender_notify_title;
    @VisibleForTesting
    static final String TAG_DEFENDER = "battery_defender";
    @VisibleForTesting
    final BroadcastReceiver mBroadcastReceiver;
    private final Context mContext;
    @VisibleForTesting
    boolean mDefenderEnabled;
    @VisibleForTesting
    NotificationManager mNotificationManager;
    private final UiEventLogger mUiEventLogger;

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public enum BatteryDefenderEvent implements UiEventLogger.UiEventEnum {
        BATTERY_DEFENDER_NOTIFICATION(876),
        BATTERY_DEFENDER_BYPASS_LIMIT(877);
        
        private final int mId;

        BatteryDefenderEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public PowerNotificationWarningsGoogleImpl(Context context, ActivityStarter activityStarter, UiEventLogger uiEventLogger) {
        super(context, activityStarter);
        AnonymousClass1 r8 = new BroadcastReceiver() { // from class: com.google.android.systemui.power.PowerNotificationWarningsGoogleImpl.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                    boolean z = false;
                    boolean z2 = intent.getIntExtra("plugged", 0) != 0;
                    if (intent.getIntExtra("health", 1) == 3) {
                        z = true;
                    }
                    Log.d("PowerNotificationWarningsGoogleImpl", "isPlugged: " + z2 + " | isOverheated: " + z + " | defenderEnabled: " + PowerNotificationWarningsGoogleImpl.this.mDefenderEnabled);
                    if (z2) {
                        if (z) {
                            PowerNotificationWarningsGoogleImpl powerNotificationWarningsGoogleImpl = PowerNotificationWarningsGoogleImpl.this;
                            if (!powerNotificationWarningsGoogleImpl.mDefenderEnabled) {
                                powerNotificationWarningsGoogleImpl.mDefenderEnabled = true;
                                powerNotificationWarningsGoogleImpl.sendNotification();
                                return;
                            }
                        }
                        if (!z) {
                            PowerNotificationWarningsGoogleImpl powerNotificationWarningsGoogleImpl2 = PowerNotificationWarningsGoogleImpl.this;
                            if (powerNotificationWarningsGoogleImpl2.mDefenderEnabled) {
                                powerNotificationWarningsGoogleImpl2.cancelNotification();
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    PowerNotificationWarningsGoogleImpl powerNotificationWarningsGoogleImpl3 = PowerNotificationWarningsGoogleImpl.this;
                    if (powerNotificationWarningsGoogleImpl3.mDefenderEnabled) {
                        powerNotificationWarningsGoogleImpl3.cancelNotification();
                    }
                } else if (PowerNotificationWarningsGoogleImpl.ACTION_RESUME_CHARGING.equals(action)) {
                    if (PowerNotificationWarningsGoogleImpl.this.mUiEventLogger != null) {
                        PowerNotificationWarningsGoogleImpl.this.mUiEventLogger.log(BatteryDefenderEvent.BATTERY_DEFENDER_BYPASS_LIMIT);
                    }
                    PowerNotificationWarningsGoogleImpl.this.executeBypassActionWithAsync();
                    PowerNotificationWarningsGoogleImpl.this.mNotificationManager.cancelAsUser(PowerNotificationWarningsGoogleImpl.TAG_DEFENDER, PowerNotificationWarningsGoogleImpl.NOTIFICATION_ID, UserHandle.ALL);
                }
            }
        };
        this.mBroadcastReceiver = r8;
        this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mContext = context;
        this.mUiEventLogger = uiEventLogger;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction(ACTION_RESUME_CHARGING);
        context.registerReceiverAsUser(r8, UserHandle.ALL, intentFilter, null, null);
        Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver != null) {
            r8.onReceive(context, registerReceiver);
        }
    }

    /* access modifiers changed from: private */
    public void sendNotification() {
        this.mNotificationManager.notifyAsUser(TAG_DEFENDER, NOTIFICATION_ID, new NotificationCompat.Builder(this.mContext, NotificationChannels.BATTERY).setSmallIcon(17303566).setContentTitle(this.mContext.getString(R$string.defender_notify_title)).setContentText(this.mContext.getString(R$string.defender_notify_des)).setOngoing(true).addAction(0, this.mContext.getString(R$string.defender_notify_learn_more), createHelpArticlePendingIntent()).addAction(0, this.mContext.getString(R$string.defender_notify_resume_charge), createResumeChargingPendingIntent()).build(), UserHandle.ALL);
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        if (uiEventLogger != null) {
            uiEventLogger.log(BatteryDefenderEvent.BATTERY_DEFENDER_NOTIFICATION);
        }
    }

    /* access modifiers changed from: private */
    public void executeBypassActionWithAsync() {
        AsyncTask.execute(new Runnable() { // from class: com.google.android.systemui.power.PowerNotificationWarningsGoogleImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PowerNotificationWarningsGoogleImpl.this.lambda$executeBypassActionWithAsync$1();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$executeBypassActionWithAsync$1() {
        PowerNotificationWarningsGoogleImpl$$ExternalSyntheticLambda0 powerNotificationWarningsGoogleImpl$$ExternalSyntheticLambda0 = PowerNotificationWarningsGoogleImpl$$ExternalSyntheticLambda0.INSTANCE;
        IGoogleBattery initHalInterface = initHalInterface(powerNotificationWarningsGoogleImpl$$ExternalSyntheticLambda0);
        if (initHalInterface == null) {
            Log.d("PowerNotificationWarningsGoogleImpl", "Can not init hal interface");
        }
        try {
            initHalInterface.setProperty(1, 17, 1);
            initHalInterface.setProperty(2, 17, 1);
            initHalInterface.setProperty(3, 17, 1);
            destroyHalInterface(initHalInterface, powerNotificationWarningsGoogleImpl$$ExternalSyntheticLambda0);
        } catch (RemoteException e) {
            Log.e("PowerNotificationWarningsGoogleImpl", "setProperty error: " + e);
            destroyHalInterface(initHalInterface, powerNotificationWarningsGoogleImpl$$ExternalSyntheticLambda0);
        }
    }

    private IGoogleBattery initHalInterface(IHwBinder.DeathRecipient deathRecipient) {
        try {
            IGoogleBattery service = IGoogleBattery.getService();
            if (!(service == null || deathRecipient == null)) {
                service.linkToDeath(deathRecipient, 0);
            }
            return service;
        } catch (RemoteException | NoSuchElementException e) {
            Log.e("PowerNotificationWarningsGoogleImpl", "failed to get Google Battery HAL: ", e);
            return null;
        }
    }

    private void destroyHalInterface(IGoogleBattery iGoogleBattery, IHwBinder.DeathRecipient deathRecipient) {
        if (deathRecipient != null) {
            try {
                iGoogleBattery.unlinkToDeath(deathRecipient);
            } catch (RemoteException e) {
                Log.e("PowerNotificationWarningsGoogleImpl", "unlinkToDeath failed: ", e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void cancelNotification() {
        this.mDefenderEnabled = false;
        this.mNotificationManager.cancelAsUser(TAG_DEFENDER, NOTIFICATION_ID, UserHandle.ALL);
    }

    private PendingIntent createHelpArticlePendingIntent() {
        return PendingIntent.getActivity(this.mContext, 0, new Intent("android.intent.action.VIEW", Uri.parse(this.mContext.getString(R$string.defender_notify_help_url))), 67108864);
    }

    private PendingIntent createResumeChargingPendingIntent() {
        return PendingIntent.getBroadcastAsUser(this.mContext, 0, new Intent(ACTION_RESUME_CHARGING).setPackage(this.mContext.getPackageName()).setFlags(268435456), 67108864, UserHandle.CURRENT);
    }
}
