package com.android.systemui.wmshell;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.tv.TvPipNotificationController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TvPipModule_ProvideTvPipNotificationControllerFactory implements Factory<TvPipNotificationController> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<PipMediaController> pipMediaControllerProvider;

    public TvPipModule_ProvideTvPipNotificationControllerFactory(Provider<Context> provider, Provider<PipMediaController> provider2, Provider<Handler> provider3) {
        this.contextProvider = provider;
        this.pipMediaControllerProvider = provider2;
        this.mainHandlerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public TvPipNotificationController get() {
        return provideTvPipNotificationController(this.contextProvider.get(), this.pipMediaControllerProvider.get(), this.mainHandlerProvider.get());
    }

    public static TvPipModule_ProvideTvPipNotificationControllerFactory create(Provider<Context> provider, Provider<PipMediaController> provider2, Provider<Handler> provider3) {
        return new TvPipModule_ProvideTvPipNotificationControllerFactory(provider, provider2, provider3);
    }

    public static TvPipNotificationController provideTvPipNotificationController(Context context, PipMediaController pipMediaController, Handler handler) {
        return (TvPipNotificationController) Preconditions.checkNotNullFromProvides(TvPipModule.provideTvPipNotificationController(context, pipMediaController, handler));
    }
}
