package com.android.systemui.dagger;

import android.view.CrossWindowBlurListeners;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory implements Factory<CrossWindowBlurListeners> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory INSTANCE = new FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory();
    }

    @Override // javax.inject.Provider
    public CrossWindowBlurListeners get() {
        return provideCrossWindowBlurListeners();
    }

    public static FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static CrossWindowBlurListeners provideCrossWindowBlurListeners() {
        return (CrossWindowBlurListeners) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideCrossWindowBlurListeners());
    }
}
