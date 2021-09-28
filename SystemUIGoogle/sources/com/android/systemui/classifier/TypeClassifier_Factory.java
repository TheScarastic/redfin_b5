package com.android.systemui.classifier;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TypeClassifier_Factory implements Factory<TypeClassifier> {
    private final Provider<FalsingDataProvider> dataProvider;

    public TypeClassifier_Factory(Provider<FalsingDataProvider> provider) {
        this.dataProvider = provider;
    }

    @Override // javax.inject.Provider
    public TypeClassifier get() {
        return newInstance(this.dataProvider.get());
    }

    public static TypeClassifier_Factory create(Provider<FalsingDataProvider> provider) {
        return new TypeClassifier_Factory(provider);
    }

    public static TypeClassifier newInstance(FalsingDataProvider falsingDataProvider) {
        return new TypeClassifier(falsingDataProvider);
    }
}
