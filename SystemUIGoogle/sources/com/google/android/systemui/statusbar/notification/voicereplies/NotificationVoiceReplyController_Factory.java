package com.google.android.systemui.statusbar.notification.voicereplies;

import android.content.Context;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController_Factory implements Factory<NotificationVoiceReplyController> {
    private final Provider<Context> contextProvider;
    private final Provider<Integer> ctaContainerIdProvider;
    private final Provider<Integer> ctaIconIdProvider;
    private final Provider<Integer> ctaLayoutProvider;
    private final Provider<Integer> ctaTextIdProvider;
    private final Provider<HeadsUpManager> headsUpManagerProvider;
    private final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    private final Provider<NotificationVoiceReplyLogger> loggerProvider;
    private final Provider<NotificationShadeWindowController> notifShadeWindowControllerProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<NotificationRemoteInputManager> notificationRemoteInputManagerProvider;
    private final Provider<LockscreenShadeTransitionController> shadeTransitionControllerProvider;
    private final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    private final Provider<StatusBar> statusBarProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;

    public NotificationVoiceReplyController_Factory(Provider<NotificationEntryManager> provider, Provider<NotificationLockscreenUserManager> provider2, Provider<NotificationRemoteInputManager> provider3, Provider<Integer> provider4, Provider<Integer> provider5, Provider<Integer> provider6, Provider<Integer> provider7, Provider<LockscreenShadeTransitionController> provider8, Provider<NotificationShadeWindowController> provider9, Provider<StatusBarKeyguardViewManager> provider10, Provider<StatusBar> provider11, Provider<SysuiStatusBarStateController> provider12, Provider<HeadsUpManager> provider13, Provider<Context> provider14, Provider<NotificationVoiceReplyLogger> provider15) {
        this.notificationEntryManagerProvider = provider;
        this.lockscreenUserManagerProvider = provider2;
        this.notificationRemoteInputManagerProvider = provider3;
        this.ctaLayoutProvider = provider4;
        this.ctaContainerIdProvider = provider5;
        this.ctaTextIdProvider = provider6;
        this.ctaIconIdProvider = provider7;
        this.shadeTransitionControllerProvider = provider8;
        this.notifShadeWindowControllerProvider = provider9;
        this.statusBarKeyguardViewManagerProvider = provider10;
        this.statusBarProvider = provider11;
        this.statusBarStateControllerProvider = provider12;
        this.headsUpManagerProvider = provider13;
        this.contextProvider = provider14;
        this.loggerProvider = provider15;
    }

    @Override // javax.inject.Provider
    public NotificationVoiceReplyController get() {
        return newInstance(this.notificationEntryManagerProvider.get(), this.lockscreenUserManagerProvider.get(), this.notificationRemoteInputManagerProvider.get(), this.ctaLayoutProvider.get().intValue(), this.ctaContainerIdProvider.get().intValue(), this.ctaTextIdProvider.get().intValue(), this.ctaIconIdProvider.get().intValue(), this.shadeTransitionControllerProvider.get(), this.notifShadeWindowControllerProvider.get(), this.statusBarKeyguardViewManagerProvider.get(), this.statusBarProvider.get(), this.statusBarStateControllerProvider.get(), this.headsUpManagerProvider.get(), this.contextProvider.get(), this.loggerProvider.get());
    }

    public static NotificationVoiceReplyController_Factory create(Provider<NotificationEntryManager> provider, Provider<NotificationLockscreenUserManager> provider2, Provider<NotificationRemoteInputManager> provider3, Provider<Integer> provider4, Provider<Integer> provider5, Provider<Integer> provider6, Provider<Integer> provider7, Provider<LockscreenShadeTransitionController> provider8, Provider<NotificationShadeWindowController> provider9, Provider<StatusBarKeyguardViewManager> provider10, Provider<StatusBar> provider11, Provider<SysuiStatusBarStateController> provider12, Provider<HeadsUpManager> provider13, Provider<Context> provider14, Provider<NotificationVoiceReplyLogger> provider15) {
        return new NotificationVoiceReplyController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static NotificationVoiceReplyController newInstance(NotificationEntryManager notificationEntryManager, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationRemoteInputManager notificationRemoteInputManager, int i, int i2, int i3, int i4, LockscreenShadeTransitionController lockscreenShadeTransitionController, NotificationShadeWindowController notificationShadeWindowController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, StatusBar statusBar, SysuiStatusBarStateController sysuiStatusBarStateController, HeadsUpManager headsUpManager, Context context, NotificationVoiceReplyLogger notificationVoiceReplyLogger) {
        return new NotificationVoiceReplyController(notificationEntryManager, notificationLockscreenUserManager, notificationRemoteInputManager, i, i2, i3, i4, lockscreenShadeTransitionController, notificationShadeWindowController, statusBarKeyguardViewManager, statusBar, sysuiStatusBarStateController, headsUpManager, context, notificationVoiceReplyLogger);
    }
}
