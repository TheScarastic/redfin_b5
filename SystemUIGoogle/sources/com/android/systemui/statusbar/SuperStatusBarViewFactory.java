package com.android.systemui.statusbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.systemui.R$layout;
import com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.StatusBarWindowView;
import com.android.systemui.util.InjectionInflationController;
/* loaded from: classes.dex */
public class SuperStatusBarViewFactory {
    private final Context mContext;
    private final InjectionInflationController mInjectionInflationController;
    private NotificationShadeWindowView mNotificationShadeWindowView;
    private final NotificationShelfComponent.Builder mNotificationShelfComponentBuilder;
    private NotificationShelfController mNotificationShelfController;
    private StatusBarWindowView mStatusBarWindowView;

    public SuperStatusBarViewFactory(Context context, InjectionInflationController injectionInflationController, NotificationShelfComponent.Builder builder) {
        this.mContext = context;
        this.mInjectionInflationController = injectionInflationController;
        this.mNotificationShelfComponentBuilder = builder;
    }

    public NotificationShadeWindowView getNotificationShadeWindowView() {
        NotificationShadeWindowView notificationShadeWindowView = this.mNotificationShadeWindowView;
        if (notificationShadeWindowView != null) {
            return notificationShadeWindowView;
        }
        NotificationShadeWindowView notificationShadeWindowView2 = (NotificationShadeWindowView) this.mInjectionInflationController.injectable(LayoutInflater.from(this.mContext)).inflate(R$layout.super_notification_shade, (ViewGroup) null);
        this.mNotificationShadeWindowView = notificationShadeWindowView2;
        if (notificationShadeWindowView2 != null) {
            return notificationShadeWindowView2;
        }
        throw new IllegalStateException("R.layout.super_notification_shade could not be properly inflated");
    }

    public StatusBarWindowView getStatusBarWindowView() {
        StatusBarWindowView statusBarWindowView = this.mStatusBarWindowView;
        if (statusBarWindowView != null) {
            return statusBarWindowView;
        }
        StatusBarWindowView statusBarWindowView2 = (StatusBarWindowView) this.mInjectionInflationController.injectable(LayoutInflater.from(this.mContext)).inflate(R$layout.super_status_bar, (ViewGroup) null);
        this.mStatusBarWindowView = statusBarWindowView2;
        if (statusBarWindowView2 != null) {
            return statusBarWindowView2;
        }
        throw new IllegalStateException("R.layout.super_status_bar could not be properly inflated");
    }

    public NotificationShelfController getNotificationShelfController(ViewGroup viewGroup) {
        NotificationShelfController notificationShelfController = this.mNotificationShelfController;
        if (notificationShelfController != null) {
            return notificationShelfController;
        }
        NotificationShelf notificationShelf = (NotificationShelf) LayoutInflater.from(this.mContext).inflate(R$layout.status_bar_notification_shelf, viewGroup, false);
        if (notificationShelf != null) {
            NotificationShelfController notificationShelfController2 = this.mNotificationShelfComponentBuilder.notificationShelf(notificationShelf).build().getNotificationShelfController();
            this.mNotificationShelfController = notificationShelfController2;
            notificationShelfController2.init();
            return this.mNotificationShelfController;
        }
        throw new IllegalStateException("R.layout.status_bar_notification_shelf could not be properly inflated");
    }
}
