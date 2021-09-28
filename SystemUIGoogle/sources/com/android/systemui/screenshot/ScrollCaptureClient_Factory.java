package com.android.systemui.screenshot;

import android.content.Context;
import android.view.IWindowManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScrollCaptureClient_Factory implements Factory<ScrollCaptureClient> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<Context> contextProvider;
    private final Provider<IWindowManager> windowManagerServiceProvider;

    public ScrollCaptureClient_Factory(Provider<IWindowManager> provider, Provider<Executor> provider2, Provider<Context> provider3) {
        this.windowManagerServiceProvider = provider;
        this.bgExecutorProvider = provider2;
        this.contextProvider = provider3;
    }

    @Override // javax.inject.Provider
    public ScrollCaptureClient get() {
        return newInstance(this.windowManagerServiceProvider.get(), this.bgExecutorProvider.get(), this.contextProvider.get());
    }

    public static ScrollCaptureClient_Factory create(Provider<IWindowManager> provider, Provider<Executor> provider2, Provider<Context> provider3) {
        return new ScrollCaptureClient_Factory(provider, provider2, provider3);
    }

    public static ScrollCaptureClient newInstance(IWindowManager iWindowManager, Executor executor, Context context) {
        return new ScrollCaptureClient(iWindowManager, executor, context);
    }
}
