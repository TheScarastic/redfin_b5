package com.android.systemui.statusbar.notification.collection.inflation;

import android.content.Context;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.icon.IconManager;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.notification.row.RowInflaterTask;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationRowBinderImpl_Factory implements Factory<NotificationRowBinderImpl> {
    private final Provider<Context> contextProvider;
    private final Provider<ExpandableNotificationRowComponent.Builder> expandableNotificationRowComponentBuilderProvider;
    private final Provider<IconManager> iconManagerProvider;
    private final Provider<LowPriorityInflationHelper> lowPriorityInflationHelperProvider;
    private final Provider<NotifBindPipeline> notifBindPipelineProvider;
    private final Provider<NotificationLockscreenUserManager> notificationLockscreenUserManagerProvider;
    private final Provider<NotificationMessagingUtil> notificationMessagingUtilProvider;
    private final Provider<NotificationRemoteInputManager> notificationRemoteInputManagerProvider;
    private final Provider<RowContentBindStage> rowContentBindStageProvider;
    private final Provider<RowInflaterTask> rowInflaterTaskProvider;

    public NotificationRowBinderImpl_Factory(Provider<Context> provider, Provider<NotificationMessagingUtil> provider2, Provider<NotificationRemoteInputManager> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<NotifBindPipeline> provider5, Provider<RowContentBindStage> provider6, Provider<RowInflaterTask> provider7, Provider<ExpandableNotificationRowComponent.Builder> provider8, Provider<IconManager> provider9, Provider<LowPriorityInflationHelper> provider10) {
        this.contextProvider = provider;
        this.notificationMessagingUtilProvider = provider2;
        this.notificationRemoteInputManagerProvider = provider3;
        this.notificationLockscreenUserManagerProvider = provider4;
        this.notifBindPipelineProvider = provider5;
        this.rowContentBindStageProvider = provider6;
        this.rowInflaterTaskProvider = provider7;
        this.expandableNotificationRowComponentBuilderProvider = provider8;
        this.iconManagerProvider = provider9;
        this.lowPriorityInflationHelperProvider = provider10;
    }

    @Override // javax.inject.Provider
    public NotificationRowBinderImpl get() {
        return newInstance(this.contextProvider.get(), this.notificationMessagingUtilProvider.get(), this.notificationRemoteInputManagerProvider.get(), this.notificationLockscreenUserManagerProvider.get(), this.notifBindPipelineProvider.get(), this.rowContentBindStageProvider.get(), this.rowInflaterTaskProvider, this.expandableNotificationRowComponentBuilderProvider.get(), this.iconManagerProvider.get(), this.lowPriorityInflationHelperProvider.get());
    }

    public static NotificationRowBinderImpl_Factory create(Provider<Context> provider, Provider<NotificationMessagingUtil> provider2, Provider<NotificationRemoteInputManager> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<NotifBindPipeline> provider5, Provider<RowContentBindStage> provider6, Provider<RowInflaterTask> provider7, Provider<ExpandableNotificationRowComponent.Builder> provider8, Provider<IconManager> provider9, Provider<LowPriorityInflationHelper> provider10) {
        return new NotificationRowBinderImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static NotificationRowBinderImpl newInstance(Context context, NotificationMessagingUtil notificationMessagingUtil, NotificationRemoteInputManager notificationRemoteInputManager, NotificationLockscreenUserManager notificationLockscreenUserManager, NotifBindPipeline notifBindPipeline, RowContentBindStage rowContentBindStage, Provider<RowInflaterTask> provider, ExpandableNotificationRowComponent.Builder builder, IconManager iconManager, LowPriorityInflationHelper lowPriorityInflationHelper) {
        return new NotificationRowBinderImpl(context, notificationMessagingUtil, notificationRemoteInputManager, notificationLockscreenUserManager, notifBindPipeline, rowContentBindStage, provider, builder, iconManager, lowPriorityInflationHelper);
    }
}
