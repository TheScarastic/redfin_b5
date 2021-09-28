package com.android.systemui.screenshot;

import android.content.Context;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScrollCaptureController_Factory implements Factory<ScrollCaptureController> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<ScrollCaptureClient> clientProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ImageTileSet> imageTileSetProvider;

    public ScrollCaptureController_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<ScrollCaptureClient> provider3, Provider<ImageTileSet> provider4) {
        this.contextProvider = provider;
        this.bgExecutorProvider = provider2;
        this.clientProvider = provider3;
        this.imageTileSetProvider = provider4;
    }

    @Override // javax.inject.Provider
    public ScrollCaptureController get() {
        return newInstance(this.contextProvider.get(), this.bgExecutorProvider.get(), this.clientProvider.get(), this.imageTileSetProvider.get());
    }

    public static ScrollCaptureController_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<ScrollCaptureClient> provider3, Provider<ImageTileSet> provider4) {
        return new ScrollCaptureController_Factory(provider, provider2, provider3, provider4);
    }

    public static ScrollCaptureController newInstance(Context context, Executor executor, ScrollCaptureClient scrollCaptureClient, Object obj) {
        return new ScrollCaptureController(context, executor, scrollCaptureClient, (ImageTileSet) obj);
    }
}
