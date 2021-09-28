package com.android.systemui.statusbar.notification.dagger;

import android.app.INotificationManager;
import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutManager;
import android.os.Handler;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.wmshell.BubblesManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsModule_ProvideNotificationGutsManagerFactory implements Factory<NotificationGutsManager> {
    private final Provider<AccessibilityManager> accessibilityManagerProvider;
    private final Provider<AssistantFeedbackController> assistantFeedbackControllerProvider;
    private final Provider<Handler> bgHandlerProvider;
    private final Provider<Optional<BubblesManager>> bubblesManagerOptionalProvider;
    private final Provider<ChannelEditorDialogController> channelEditorDialogControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<UserContextProvider> contextTrackerProvider;
    private final Provider<HighPriorityProvider> highPriorityProvider;
    private final Provider<LauncherApps> launcherAppsProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<INotificationManager> notificationManagerProvider;
    private final Provider<OnUserInteractionCallback> onUserInteractionCallbackProvider;
    private final Provider<PeopleSpaceWidgetManager> peopleSpaceWidgetManagerProvider;
    private final Provider<ShadeController> shadeControllerProvider;
    private final Provider<ShortcutManager> shortcutManagerProvider;
    private final Provider<StatusBar> statusBarLazyProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public NotificationsModule_ProvideNotificationGutsManagerFactory(Provider<Context> provider, Provider<StatusBar> provider2, Provider<Handler> provider3, Provider<Handler> provider4, Provider<AccessibilityManager> provider5, Provider<HighPriorityProvider> provider6, Provider<INotificationManager> provider7, Provider<NotificationEntryManager> provider8, Provider<PeopleSpaceWidgetManager> provider9, Provider<LauncherApps> provider10, Provider<ShortcutManager> provider11, Provider<ChannelEditorDialogController> provider12, Provider<UserContextProvider> provider13, Provider<AssistantFeedbackController> provider14, Provider<Optional<BubblesManager>> provider15, Provider<UiEventLogger> provider16, Provider<OnUserInteractionCallback> provider17, Provider<ShadeController> provider18) {
        this.contextProvider = provider;
        this.statusBarLazyProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.bgHandlerProvider = provider4;
        this.accessibilityManagerProvider = provider5;
        this.highPriorityProvider = provider6;
        this.notificationManagerProvider = provider7;
        this.notificationEntryManagerProvider = provider8;
        this.peopleSpaceWidgetManagerProvider = provider9;
        this.launcherAppsProvider = provider10;
        this.shortcutManagerProvider = provider11;
        this.channelEditorDialogControllerProvider = provider12;
        this.contextTrackerProvider = provider13;
        this.assistantFeedbackControllerProvider = provider14;
        this.bubblesManagerOptionalProvider = provider15;
        this.uiEventLoggerProvider = provider16;
        this.onUserInteractionCallbackProvider = provider17;
        this.shadeControllerProvider = provider18;
    }

    @Override // javax.inject.Provider
    public NotificationGutsManager get() {
        return provideNotificationGutsManager(this.contextProvider.get(), DoubleCheck.lazy(this.statusBarLazyProvider), this.mainHandlerProvider.get(), this.bgHandlerProvider.get(), this.accessibilityManagerProvider.get(), this.highPriorityProvider.get(), this.notificationManagerProvider.get(), this.notificationEntryManagerProvider.get(), this.peopleSpaceWidgetManagerProvider.get(), this.launcherAppsProvider.get(), this.shortcutManagerProvider.get(), this.channelEditorDialogControllerProvider.get(), this.contextTrackerProvider.get(), this.assistantFeedbackControllerProvider.get(), this.bubblesManagerOptionalProvider.get(), this.uiEventLoggerProvider.get(), this.onUserInteractionCallbackProvider.get(), this.shadeControllerProvider.get());
    }

    public static NotificationsModule_ProvideNotificationGutsManagerFactory create(Provider<Context> provider, Provider<StatusBar> provider2, Provider<Handler> provider3, Provider<Handler> provider4, Provider<AccessibilityManager> provider5, Provider<HighPriorityProvider> provider6, Provider<INotificationManager> provider7, Provider<NotificationEntryManager> provider8, Provider<PeopleSpaceWidgetManager> provider9, Provider<LauncherApps> provider10, Provider<ShortcutManager> provider11, Provider<ChannelEditorDialogController> provider12, Provider<UserContextProvider> provider13, Provider<AssistantFeedbackController> provider14, Provider<Optional<BubblesManager>> provider15, Provider<UiEventLogger> provider16, Provider<OnUserInteractionCallback> provider17, Provider<ShadeController> provider18) {
        return new NotificationsModule_ProvideNotificationGutsManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18);
    }

    public static NotificationGutsManager provideNotificationGutsManager(Context context, Lazy<StatusBar> lazy, Handler handler, Handler handler2, AccessibilityManager accessibilityManager, HighPriorityProvider highPriorityProvider, INotificationManager iNotificationManager, NotificationEntryManager notificationEntryManager, PeopleSpaceWidgetManager peopleSpaceWidgetManager, LauncherApps launcherApps, ShortcutManager shortcutManager, ChannelEditorDialogController channelEditorDialogController, UserContextProvider userContextProvider, AssistantFeedbackController assistantFeedbackController, Optional<BubblesManager> optional, UiEventLogger uiEventLogger, OnUserInteractionCallback onUserInteractionCallback, ShadeController shadeController) {
        return (NotificationGutsManager) Preconditions.checkNotNullFromProvides(NotificationsModule.provideNotificationGutsManager(context, lazy, handler, handler2, accessibilityManager, highPriorityProvider, iNotificationManager, notificationEntryManager, peopleSpaceWidgetManager, launcherApps, shortcutManager, channelEditorDialogController, userContextProvider, assistantFeedbackController, optional, uiEventLogger, onUserInteractionCallback, shadeController));
    }
}
