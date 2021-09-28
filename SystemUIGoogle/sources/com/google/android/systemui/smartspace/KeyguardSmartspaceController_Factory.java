package com.google.android.systemui.smartspace;

import android.content.Context;
import com.android.systemui.statusbar.FeatureFlags;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class KeyguardSmartspaceController_Factory implements Factory<KeyguardSmartspaceController> {
    private final Provider<Context> contextProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<KeyguardMediaViewController> mediaControllerProvider;
    private final Provider<KeyguardZenAlarmViewController> zenControllerProvider;

    public KeyguardSmartspaceController_Factory(Provider<Context> provider, Provider<FeatureFlags> provider2, Provider<KeyguardZenAlarmViewController> provider3, Provider<KeyguardMediaViewController> provider4) {
        this.contextProvider = provider;
        this.featureFlagsProvider = provider2;
        this.zenControllerProvider = provider3;
        this.mediaControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public KeyguardSmartspaceController get() {
        return newInstance(this.contextProvider.get(), this.featureFlagsProvider.get(), this.zenControllerProvider.get(), this.mediaControllerProvider.get());
    }

    public static KeyguardSmartspaceController_Factory create(Provider<Context> provider, Provider<FeatureFlags> provider2, Provider<KeyguardZenAlarmViewController> provider3, Provider<KeyguardMediaViewController> provider4) {
        return new KeyguardSmartspaceController_Factory(provider, provider2, provider3, provider4);
    }

    public static KeyguardSmartspaceController newInstance(Context context, FeatureFlags featureFlags, KeyguardZenAlarmViewController keyguardZenAlarmViewController, KeyguardMediaViewController keyguardMediaViewController) {
        return new KeyguardSmartspaceController(context, featureFlags, keyguardZenAlarmViewController, keyguardMediaViewController);
    }
}
