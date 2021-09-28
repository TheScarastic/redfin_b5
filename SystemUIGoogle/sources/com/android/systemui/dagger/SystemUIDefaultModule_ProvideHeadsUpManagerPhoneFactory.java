package com.android.systemui.dagger;

import android.content.Context;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemUIDefaultModule_ProvideHeadsUpManagerPhoneFactory implements Factory<HeadsUpManagerPhone> {
    private final Provider<KeyguardBypassController> bypassControllerProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<GroupMembershipManager> groupManagerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public SystemUIDefaultModule_ProvideHeadsUpManagerPhoneFactory(Provider<Context> provider, Provider<StatusBarStateController> provider2, Provider<KeyguardBypassController> provider3, Provider<GroupMembershipManager> provider4, Provider<ConfigurationController> provider5) {
        this.contextProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.bypassControllerProvider = provider3;
        this.groupManagerProvider = provider4;
        this.configurationControllerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public HeadsUpManagerPhone get() {
        return provideHeadsUpManagerPhone(this.contextProvider.get(), this.statusBarStateControllerProvider.get(), this.bypassControllerProvider.get(), this.groupManagerProvider.get(), this.configurationControllerProvider.get());
    }

    public static SystemUIDefaultModule_ProvideHeadsUpManagerPhoneFactory create(Provider<Context> provider, Provider<StatusBarStateController> provider2, Provider<KeyguardBypassController> provider3, Provider<GroupMembershipManager> provider4, Provider<ConfigurationController> provider5) {
        return new SystemUIDefaultModule_ProvideHeadsUpManagerPhoneFactory(provider, provider2, provider3, provider4, provider5);
    }

    public static HeadsUpManagerPhone provideHeadsUpManagerPhone(Context context, StatusBarStateController statusBarStateController, KeyguardBypassController keyguardBypassController, GroupMembershipManager groupMembershipManager, ConfigurationController configurationController) {
        return (HeadsUpManagerPhone) Preconditions.checkNotNullFromProvides(SystemUIDefaultModule.provideHeadsUpManagerPhone(context, statusBarStateController, keyguardBypassController, groupMembershipManager, configurationController));
    }
}
