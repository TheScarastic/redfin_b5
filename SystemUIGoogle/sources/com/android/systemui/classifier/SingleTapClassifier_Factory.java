package com.android.systemui.classifier;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SingleTapClassifier_Factory implements Factory<SingleTapClassifier> {
    private final Provider<FalsingDataProvider> dataProvider;
    private final Provider<Float> touchSlopProvider;

    public SingleTapClassifier_Factory(Provider<FalsingDataProvider> provider, Provider<Float> provider2) {
        this.dataProvider = provider;
        this.touchSlopProvider = provider2;
    }

    @Override // javax.inject.Provider
    public SingleTapClassifier get() {
        return newInstance(this.dataProvider.get(), this.touchSlopProvider.get().floatValue());
    }

    public static SingleTapClassifier_Factory create(Provider<FalsingDataProvider> provider, Provider<Float> provider2) {
        return new SingleTapClassifier_Factory(provider, provider2);
    }

    public static SingleTapClassifier newInstance(FalsingDataProvider falsingDataProvider, float f) {
        return new SingleTapClassifier(falsingDataProvider, f);
    }
}
