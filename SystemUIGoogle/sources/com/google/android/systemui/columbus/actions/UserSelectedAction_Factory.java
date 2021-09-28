package com.google.android.systemui.columbus.actions;

import android.content.Context;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.google.android.systemui.columbus.ColumbusSettings;
import com.google.android.systemui.columbus.PowerManagerWrapper;
import dagger.internal.Factory;
import java.util.Map;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class UserSelectedAction_Factory implements Factory<UserSelectedAction> {
    private final Provider<ColumbusSettings> columbusSettingsProvider;
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<PowerManagerWrapper> powerManagerProvider;
    private final Provider<TakeScreenshot> takeScreenshotProvider;
    private final Provider<Map<String, UserAction>> userSelectedActionsProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public UserSelectedAction_Factory(Provider<Context> provider, Provider<ColumbusSettings> provider2, Provider<Map<String, UserAction>> provider3, Provider<TakeScreenshot> provider4, Provider<KeyguardStateController> provider5, Provider<PowerManagerWrapper> provider6, Provider<WakefulnessLifecycle> provider7) {
        this.contextProvider = provider;
        this.columbusSettingsProvider = provider2;
        this.userSelectedActionsProvider = provider3;
        this.takeScreenshotProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.powerManagerProvider = provider6;
        this.wakefulnessLifecycleProvider = provider7;
    }

    @Override // javax.inject.Provider
    public UserSelectedAction get() {
        return newInstance(this.contextProvider.get(), this.columbusSettingsProvider.get(), this.userSelectedActionsProvider.get(), this.takeScreenshotProvider.get(), this.keyguardStateControllerProvider.get(), this.powerManagerProvider.get(), this.wakefulnessLifecycleProvider.get());
    }

    public static UserSelectedAction_Factory create(Provider<Context> provider, Provider<ColumbusSettings> provider2, Provider<Map<String, UserAction>> provider3, Provider<TakeScreenshot> provider4, Provider<KeyguardStateController> provider5, Provider<PowerManagerWrapper> provider6, Provider<WakefulnessLifecycle> provider7) {
        return new UserSelectedAction_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static UserSelectedAction newInstance(Context context, ColumbusSettings columbusSettings, Map<String, UserAction> map, TakeScreenshot takeScreenshot, KeyguardStateController keyguardStateController, PowerManagerWrapper powerManagerWrapper, WakefulnessLifecycle wakefulnessLifecycle) {
        return new UserSelectedAction(context, columbusSettings, map, takeScreenshot, keyguardStateController, powerManagerWrapper, wakefulnessLifecycle);
    }
}
