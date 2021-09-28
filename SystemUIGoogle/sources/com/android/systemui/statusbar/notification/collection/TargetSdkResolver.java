package com.android.systemui.statusbar.notification.collection;

import android.content.Context;
import android.content.pm.PackageManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.phone.StatusBar;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TargetSdkResolver.kt */
/* loaded from: classes.dex */
public final class TargetSdkResolver {
    private final String TAG = "TargetSdkResolver";
    private final Context context;

    public TargetSdkResolver(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    public final void initialize(CommonNotifCollection commonNotifCollection) {
        Intrinsics.checkNotNullParameter(commonNotifCollection, "collection");
        commonNotifCollection.addCollectionListener(new NotifCollectionListener(this) { // from class: com.android.systemui.statusbar.notification.collection.TargetSdkResolver$initialize$1
            final /* synthetic */ TargetSdkResolver this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryBind(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
                Intrinsics.checkNotNullParameter(notificationEntry, "entry");
                Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
                notificationEntry.targetSdk = this.this$0.resolveNotificationSdk(statusBarNotification);
            }
        });
    }

    /* access modifiers changed from: private */
    public final int resolveNotificationSdk(StatusBarNotification statusBarNotification) {
        try {
            return StatusBar.getPackageManagerForUser(this.context, statusBarNotification.getUser().getIdentifier()).getApplicationInfo(statusBarNotification.getPackageName(), 0).targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(this.TAG, Intrinsics.stringPlus("Failed looking up ApplicationInfo for ", statusBarNotification.getPackageName()), e);
            return 0;
        }
    }
}
