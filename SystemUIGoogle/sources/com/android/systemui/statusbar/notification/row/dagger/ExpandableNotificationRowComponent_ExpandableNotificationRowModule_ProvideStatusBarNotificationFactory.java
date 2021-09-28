package com.android.systemui.statusbar.notification.row.dagger;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory implements Factory<StatusBarNotification> {
    private final Provider<NotificationEntry> notificationEntryProvider;

    public ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory(Provider<NotificationEntry> provider) {
        this.notificationEntryProvider = provider;
    }

    @Override // javax.inject.Provider
    public StatusBarNotification get() {
        return provideStatusBarNotification(this.notificationEntryProvider.get());
    }

    public static ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory create(Provider<NotificationEntry> provider) {
        return new ExpandableNotificationRowComponent_ExpandableNotificationRowModule_ProvideStatusBarNotificationFactory(provider);
    }

    public static StatusBarNotification provideStatusBarNotification(NotificationEntry notificationEntry) {
        return (StatusBarNotification) Preconditions.checkNotNullFromProvides(ExpandableNotificationRowComponent.ExpandableNotificationRowModule.provideStatusBarNotification(notificationEntry));
    }
}
