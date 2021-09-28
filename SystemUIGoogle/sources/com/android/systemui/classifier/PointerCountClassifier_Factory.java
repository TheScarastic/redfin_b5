package com.android.systemui.classifier;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PointerCountClassifier_Factory implements Factory<PointerCountClassifier> {
    private final Provider<FalsingDataProvider> dataProvider;

    public PointerCountClassifier_Factory(Provider<FalsingDataProvider> provider) {
        this.dataProvider = provider;
    }

    @Override // javax.inject.Provider
    public PointerCountClassifier get() {
        return newInstance(this.dataProvider.get());
    }

    public static PointerCountClassifier_Factory create(Provider<FalsingDataProvider> provider) {
        return new PointerCountClassifier_Factory(provider);
    }

    public static PointerCountClassifier newInstance(FalsingDataProvider falsingDataProvider) {
        return new PointerCountClassifier(falsingDataProvider);
    }
}
