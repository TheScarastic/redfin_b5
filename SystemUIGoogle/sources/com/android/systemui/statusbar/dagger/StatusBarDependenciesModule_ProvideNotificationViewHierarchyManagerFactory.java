package com.android.systemui.statusbar.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.DynamicChildBindController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.inflation.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarDependenciesModule_ProvideNotificationViewHierarchyManagerFactory implements Factory<NotificationViewHierarchyManager> {
    private final Provider<AssistantFeedbackController> assistantFeedbackControllerProvider;
    private final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    private final Provider<KeyguardBypassController> bypassControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DynamicChildBindController> dynamicChildBindControllerProvider;
    private final Provider<ForegroundServiceSectionController> fgsSectionControllerProvider;
    private final Provider<NotificationGroupManagerLegacy> groupManagerProvider;
    private final Provider<LowPriorityInflationHelper> lowPriorityInflationHelperProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<NotificationLockscreenUserManager> notificationLockscreenUserManagerProvider;
    private final Provider<DynamicPrivacyController> privacyControllerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<VisualStabilityManager> visualStabilityManagerProvider;

    public StatusBarDependenciesModule_ProvideNotificationViewHierarchyManagerFactory(Provider<Context> provider, Provider<Handler> provider2, Provider<NotificationLockscreenUserManager> provider3, Provider<NotificationGroupManagerLegacy> provider4, Provider<VisualStabilityManager> provider5, Provider<StatusBarStateController> provider6, Provider<NotificationEntryManager> provider7, Provider<KeyguardBypassController> provider8, Provider<Optional<Bubbles>> provider9, Provider<DynamicPrivacyController> provider10, Provider<ForegroundServiceSectionController> provider11, Provider<DynamicChildBindController> provider12, Provider<LowPriorityInflationHelper> provider13, Provider<AssistantFeedbackController> provider14) {
        this.contextProvider = provider;
        this.mainHandlerProvider = provider2;
        this.notificationLockscreenUserManagerProvider = provider3;
        this.groupManagerProvider = provider4;
        this.visualStabilityManagerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.notificationEntryManagerProvider = provider7;
        this.bypassControllerProvider = provider8;
        this.bubblesOptionalProvider = provider9;
        this.privacyControllerProvider = provider10;
        this.fgsSectionControllerProvider = provider11;
        this.dynamicChildBindControllerProvider = provider12;
        this.lowPriorityInflationHelperProvider = provider13;
        this.assistantFeedbackControllerProvider = provider14;
    }

    @Override // javax.inject.Provider
    public NotificationViewHierarchyManager get() {
        return provideNotificationViewHierarchyManager(this.contextProvider.get(), this.mainHandlerProvider.get(), this.notificationLockscreenUserManagerProvider.get(), this.groupManagerProvider.get(), this.visualStabilityManagerProvider.get(), this.statusBarStateControllerProvider.get(), this.notificationEntryManagerProvider.get(), this.bypassControllerProvider.get(), this.bubblesOptionalProvider.get(), this.privacyControllerProvider.get(), this.fgsSectionControllerProvider.get(), this.dynamicChildBindControllerProvider.get(), this.lowPriorityInflationHelperProvider.get(), this.assistantFeedbackControllerProvider.get());
    }

    public static StatusBarDependenciesModule_ProvideNotificationViewHierarchyManagerFactory create(Provider<Context> provider, Provider<Handler> provider2, Provider<NotificationLockscreenUserManager> provider3, Provider<NotificationGroupManagerLegacy> provider4, Provider<VisualStabilityManager> provider5, Provider<StatusBarStateController> provider6, Provider<NotificationEntryManager> provider7, Provider<KeyguardBypassController> provider8, Provider<Optional<Bubbles>> provider9, Provider<DynamicPrivacyController> provider10, Provider<ForegroundServiceSectionController> provider11, Provider<DynamicChildBindController> provider12, Provider<LowPriorityInflationHelper> provider13, Provider<AssistantFeedbackController> provider14) {
        return new StatusBarDependenciesModule_ProvideNotificationViewHierarchyManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static NotificationViewHierarchyManager provideNotificationViewHierarchyManager(Context context, Handler handler, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, VisualStabilityManager visualStabilityManager, StatusBarStateController statusBarStateController, NotificationEntryManager notificationEntryManager, KeyguardBypassController keyguardBypassController, Optional<Bubbles> optional, DynamicPrivacyController dynamicPrivacyController, ForegroundServiceSectionController foregroundServiceSectionController, DynamicChildBindController dynamicChildBindController, LowPriorityInflationHelper lowPriorityInflationHelper, AssistantFeedbackController assistantFeedbackController) {
        return (NotificationViewHierarchyManager) Preconditions.checkNotNullFromProvides(StatusBarDependenciesModule.provideNotificationViewHierarchyManager(context, handler, notificationLockscreenUserManager, notificationGroupManagerLegacy, visualStabilityManager, statusBarStateController, notificationEntryManager, keyguardBypassController, optional, dynamicPrivacyController, foregroundServiceSectionController, dynamicChildBindController, lowPriorityInflationHelper, assistantFeedbackController));
    }
}
