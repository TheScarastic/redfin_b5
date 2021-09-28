package com.android.systemui.classifier;

import android.content.res.Resources;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FalsingModule_ProvidesDoubleTapTouchSlopFactory implements Factory<Float> {
    private final Provider<Resources> resourcesProvider;

    public FalsingModule_ProvidesDoubleTapTouchSlopFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    @Override // javax.inject.Provider
    public Float get() {
        return Float.valueOf(providesDoubleTapTouchSlop(this.resourcesProvider.get()));
    }

    public static FalsingModule_ProvidesDoubleTapTouchSlopFactory create(Provider<Resources> provider) {
        return new FalsingModule_ProvidesDoubleTapTouchSlopFactory(provider);
    }

    public static float providesDoubleTapTouchSlop(Resources resources) {
        return FalsingModule.providesDoubleTapTouchSlop(resources);
    }
}
