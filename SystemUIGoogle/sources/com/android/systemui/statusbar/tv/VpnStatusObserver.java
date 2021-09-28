package com.android.systemui.statusbar.tv;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import com.android.internal.net.VpnConfig;
import com.android.systemui.Dependency;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.SystemUI;
import com.android.systemui.statusbar.policy.SecurityController;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: VpnStatusObserver.kt */
/* loaded from: classes.dex */
public final class VpnStatusObserver extends SystemUI implements SecurityController.SecurityControllerCallback {
    public static final Companion Companion = new Companion(null);
    private static final String NOTIFICATION_TAG;
    private final NotificationManager notificationManager;
    private final SecurityController securityController;
    private boolean vpnConnected;
    private final NotificationChannel notificationChannel = createNotificationChannel();
    private final Notification.Builder vpnConnectedNotificationBuilder = createVpnConnectedNotificationBuilder();
    private final Notification vpnDisconnectedNotification = createVpnDisconnectedNotification();

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public VpnStatusObserver(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Object obj = Dependency.get(SecurityController.class);
        Intrinsics.checkNotNullExpressionValue(obj, "get(SecurityController::class.java)");
        this.securityController = (SecurityController) obj;
        this.notificationManager = NotificationManager.from(context);
    }

    private final int getVpnIconId() {
        if (this.securityController.isVpnBranded()) {
            return R$drawable.stat_sys_branded_vpn;
        }
        return R$drawable.stat_sys_vpn_ic;
    }

    private final String getVpnName() {
        String primaryVpnName = this.securityController.getPrimaryVpnName();
        return primaryVpnName == null ? this.securityController.getWorkProfileVpnName() : primaryVpnName;
    }

    @Override // com.android.systemui.SystemUI
    public void start() {
        this.securityController.addCallback(this);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController.SecurityControllerCallback
    public void onStateChanged() {
        boolean isVpnEnabled = this.securityController.isVpnEnabled();
        if (this.vpnConnected != isVpnEnabled) {
            if (isVpnEnabled) {
                notifyVpnConnected();
            } else {
                notifyVpnDisconnected();
            }
            this.vpnConnected = isVpnEnabled;
        }
    }

    private final void notifyVpnConnected() {
        this.notificationManager.notify(Companion.getNOTIFICATION_TAG(), 20, createVpnConnectedNotification());
    }

    private final void notifyVpnDisconnected() {
        NotificationManager notificationManager = this.notificationManager;
        Companion companion = Companion;
        notificationManager.cancel(companion.getNOTIFICATION_TAG(), 20);
        notificationManager.notify(companion.getNOTIFICATION_TAG(), 17, this.vpnDisconnectedNotification);
    }

    private final NotificationChannel createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel("VPN Status", "VPN Status", 4);
        this.notificationManager.createNotificationChannel(notificationChannel);
        return notificationChannel;
    }

    private final Notification createVpnConnectedNotification() {
        Notification.Builder builder = this.vpnConnectedNotificationBuilder;
        String vpnName = getVpnName();
        if (vpnName != null) {
            builder.setContentText(this.mContext.getString(R$string.notification_disclosure_vpn_text, vpnName));
        }
        return builder.build();
    }

    private final Notification.Builder createVpnConnectedNotificationBuilder() {
        return new Notification.Builder(this.mContext, "VPN Status").setSmallIcon(getVpnIconId()).setVisibility(1).setCategory("sys").extend(new Notification.TvExtender()).setOngoing(true).setContentTitle(this.mContext.getString(R$string.notification_vpn_connected)).setContentIntent(VpnConfig.getIntentForStatusPanel(this.mContext));
    }

    private final Notification createVpnDisconnectedNotification() {
        return new Notification.Builder(this.mContext, "VPN Status").setSmallIcon(getVpnIconId()).setVisibility(1).setCategory("sys").extend(new Notification.TvExtender()).setTimeoutAfter(5000).setContentTitle(this.mContext.getString(R$string.notification_vpn_disconnected)).build();
    }

    /* compiled from: VpnStatusObserver.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final String getNOTIFICATION_TAG() {
            return VpnStatusObserver.NOTIFICATION_TAG;
        }
    }

    static {
        String simpleName = VpnStatusObserver.class.getSimpleName();
        Intrinsics.checkNotNullExpressionValue(simpleName, "VpnStatusObserver::class.java.simpleName");
        NOTIFICATION_TAG = simpleName;
    }
}
