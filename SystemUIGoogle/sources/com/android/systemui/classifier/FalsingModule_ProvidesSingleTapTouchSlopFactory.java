package com.android.systemui.classifier;

import android.view.ViewConfiguration;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FalsingModule_ProvidesSingleTapTouchSlopFactory implements Factory<Float> {
    private final Provider<ViewConfiguration> viewConfigurationProvider;

    public FalsingModule_ProvidesSingleTapTouchSlopFactory(Provider<ViewConfiguration> provider) {
        this.viewConfigurationProvider = provider;
    }

    @Override // javax.inject.Provider
    public Float get() {
        return Float.valueOf(providesSingleTapTouchSlop(this.viewConfigurationProvider.get()));
    }

    public static FalsingModule_ProvidesSingleTapTouchSlopFactory create(Provider<ViewConfiguration> provider) {
        return new FalsingModule_ProvidesSingleTapTouchSlopFactory(provider);
    }

    public static float providesSingleTapTouchSlop(ViewConfiguration viewConfiguration) {
        return FalsingModule.providesSingleTapTouchSlop(viewConfiguration);
    }
}
