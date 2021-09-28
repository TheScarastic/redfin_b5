package com.android.systemui.classifier;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FalsingModule_ProvidesBrightLineGestureClassifiersFactory implements Factory<Set<FalsingClassifier>> {
    private final Provider<DiagonalClassifier> diagonalClassifierProvider;
    private final Provider<DistanceClassifier> distanceClassifierProvider;
    private final Provider<PointerCountClassifier> pointerCountClassifierProvider;
    private final Provider<ProximityClassifier> proximityClassifierProvider;
    private final Provider<TypeClassifier> typeClassifierProvider;
    private final Provider<ZigZagClassifier> zigZagClassifierProvider;

    public FalsingModule_ProvidesBrightLineGestureClassifiersFactory(Provider<DistanceClassifier> provider, Provider<ProximityClassifier> provider2, Provider<PointerCountClassifier> provider3, Provider<TypeClassifier> provider4, Provider<DiagonalClassifier> provider5, Provider<ZigZagClassifier> provider6) {
        this.distanceClassifierProvider = provider;
        this.proximityClassifierProvider = provider2;
        this.pointerCountClassifierProvider = provider3;
        this.typeClassifierProvider = provider4;
        this.diagonalClassifierProvider = provider5;
        this.zigZagClassifierProvider = provider6;
    }

    @Override // javax.inject.Provider
    public Set<FalsingClassifier> get() {
        return providesBrightLineGestureClassifiers(this.distanceClassifierProvider.get(), this.proximityClassifierProvider.get(), this.pointerCountClassifierProvider.get(), this.typeClassifierProvider.get(), this.diagonalClassifierProvider.get(), this.zigZagClassifierProvider.get());
    }

    public static FalsingModule_ProvidesBrightLineGestureClassifiersFactory create(Provider<DistanceClassifier> provider, Provider<ProximityClassifier> provider2, Provider<PointerCountClassifier> provider3, Provider<TypeClassifier> provider4, Provider<DiagonalClassifier> provider5, Provider<ZigZagClassifier> provider6) {
        return new FalsingModule_ProvidesBrightLineGestureClassifiersFactory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static Set<FalsingClassifier> providesBrightLineGestureClassifiers(Object obj, Object obj2, Object obj3, TypeClassifier typeClassifier, Object obj4, Object obj5) {
        return (Set) Preconditions.checkNotNullFromProvides(FalsingModule.providesBrightLineGestureClassifiers((DistanceClassifier) obj, (ProximityClassifier) obj2, (PointerCountClassifier) obj3, typeClassifier, (DiagonalClassifier) obj4, (ZigZagClassifier) obj5));
    }
}
