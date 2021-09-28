package com.android.systemui.screenshot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.UserHandle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.SystemUI;
import com.android.systemui.util.NotificationChannels;
/* loaded from: classes.dex */
public class ScreenshotNotificationsController {
    private final Context mContext;
    private final NotificationManager mNotificationManager;
    private final Resources mResources;

    /* access modifiers changed from: package-private */
    public ScreenshotNotificationsController(Context context, WindowManager windowManager) {
        this.mContext = context;
        this.mResources = context.getResources();
        this.mNotificationManager = (NotificationManager) context.getSystemService("notification");
        windowManager.getDefaultDisplay().getRealMetrics(new DisplayMetrics());
    }

    public void notifyScreenshotError(int i) {
        Resources resources = this.mContext.getResources();
        String string = resources.getString(i);
        Notification.Builder builder = new Notification.Builder(this.mContext, NotificationChannels.ALERTS);
        int i2 = R$string.screenshot_failed_title;
        Notification.Builder color = builder.setTicker(resources.getString(i2)).setContentTitle(resources.getString(i2)).setContentText(string).setSmallIcon(R$drawable.stat_notify_image_error).setWhen(System.currentTimeMillis()).setVisibility(1).setCategory("err").setAutoCancel(true).setColor(this.mContext.getColor(17170460));
        Intent createAdminSupportIntent = ((DevicePolicyManager) this.mContext.getSystemService("device_policy")).createAdminSupportIntent("policy_disable_screen_capture");
        if (createAdminSupportIntent != null) {
            color.setContentIntent(PendingIntent.getActivityAsUser(this.mContext, 0, createAdminSupportIntent, 67108864, null, UserHandle.CURRENT));
        }
        SystemUI.overrideNotificationAppName(this.mContext, color, true);
        this.mNotificationManager.notify(1, new Notification.BigTextStyle(color).bigText(string).build());
    }
}
