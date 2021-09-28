package com.google.android.systemui.gamedashboard;

import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class FpsController_Factory implements Factory<FpsController> {
    private final Provider<Executor> executorProvider;

    public FpsController_Factory(Provider<Executor> provider) {
        this.executorProvider = provider;
    }

    @Override // javax.inject.Provider
    public FpsController get() {
        return newInstance(this.executorProvider.get());
    }

    public static FpsController_Factory create(Provider<Executor> provider) {
        return new FpsController_Factory(provider);
    }

    public static FpsController newInstance(Executor executor) {
        return new FpsController(executor);
    }
}
