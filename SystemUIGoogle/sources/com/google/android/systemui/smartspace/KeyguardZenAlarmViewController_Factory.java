package com.google.android.systemui.smartspace;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Handler;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.ZenModeController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class KeyguardZenAlarmViewController_Factory implements Factory<KeyguardZenAlarmViewController> {
    private final Provider<AlarmManager> alarmManagerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<NextAlarmController> nextAlarmControllerProvider;
    private final Provider<BcSmartspaceDataPlugin> pluginProvider;
    private final Provider<ZenModeController> zenModeControllerProvider;

    public KeyguardZenAlarmViewController_Factory(Provider<Context> provider, Provider<BcSmartspaceDataPlugin> provider2, Provider<ZenModeController> provider3, Provider<AlarmManager> provider4, Provider<NextAlarmController> provider5, Provider<Handler> provider6) {
        this.contextProvider = provider;
        this.pluginProvider = provider2;
        this.zenModeControllerProvider = provider3;
        this.alarmManagerProvider = provider4;
        this.nextAlarmControllerProvider = provider5;
        this.handlerProvider = provider6;
    }

    @Override // javax.inject.Provider
    public KeyguardZenAlarmViewController get() {
        return newInstance(this.contextProvider.get(), this.pluginProvider.get(), this.zenModeControllerProvider.get(), this.alarmManagerProvider.get(), this.nextAlarmControllerProvider.get(), this.handlerProvider.get());
    }

    public static KeyguardZenAlarmViewController_Factory create(Provider<Context> provider, Provider<BcSmartspaceDataPlugin> provider2, Provider<ZenModeController> provider3, Provider<AlarmManager> provider4, Provider<NextAlarmController> provider5, Provider<Handler> provider6) {
        return new KeyguardZenAlarmViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static KeyguardZenAlarmViewController newInstance(Context context, BcSmartspaceDataPlugin bcSmartspaceDataPlugin, ZenModeController zenModeController, AlarmManager alarmManager, NextAlarmController nextAlarmController, Handler handler) {
        return new KeyguardZenAlarmViewController(context, bcSmartspaceDataPlugin, zenModeController, alarmManager, nextAlarmController, handler);
    }
}
