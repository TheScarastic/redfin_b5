package com.android.systemui.screenshot;

import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DeleteScreenshotReceiver_Factory implements Factory<DeleteScreenshotReceiver> {
    private final Provider<Executor> backgroundExecutorProvider;
    private final Provider<ScreenshotSmartActions> screenshotSmartActionsProvider;

    public DeleteScreenshotReceiver_Factory(Provider<ScreenshotSmartActions> provider, Provider<Executor> provider2) {
        this.screenshotSmartActionsProvider = provider;
        this.backgroundExecutorProvider = provider2;
    }

    @Override // javax.inject.Provider
    public DeleteScreenshotReceiver get() {
        return newInstance(this.screenshotSmartActionsProvider.get(), this.backgroundExecutorProvider.get());
    }

    public static DeleteScreenshotReceiver_Factory create(Provider<ScreenshotSmartActions> provider, Provider<Executor> provider2) {
        return new DeleteScreenshotReceiver_Factory(provider, provider2);
    }

    public static DeleteScreenshotReceiver newInstance(ScreenshotSmartActions screenshotSmartActions, Executor executor) {
        return new DeleteScreenshotReceiver(screenshotSmartActions, executor);
    }
}
