package com.android.systemui.wmshell;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TvPipModule_ProvidesTvPipMenuControllerFactory implements Factory<TvPipMenuController> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<PipBoundsState> pipBoundsStateProvider;
    private final Provider<PipMediaController> pipMediaControllerProvider;
    private final Provider<SystemWindows> systemWindowsProvider;

    public TvPipModule_ProvidesTvPipMenuControllerFactory(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<SystemWindows> provider3, Provider<PipMediaController> provider4, Provider<Handler> provider5) {
        this.contextProvider = provider;
        this.pipBoundsStateProvider = provider2;
        this.systemWindowsProvider = provider3;
        this.pipMediaControllerProvider = provider4;
        this.mainHandlerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public TvPipMenuController get() {
        return providesTvPipMenuController(this.contextProvider.get(), this.pipBoundsStateProvider.get(), this.systemWindowsProvider.get(), this.pipMediaControllerProvider.get(), this.mainHandlerProvider.get());
    }

    public static TvPipModule_ProvidesTvPipMenuControllerFactory create(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<SystemWindows> provider3, Provider<PipMediaController> provider4, Provider<Handler> provider5) {
        return new TvPipModule_ProvidesTvPipMenuControllerFactory(provider, provider2, provider3, provider4, provider5);
    }

    public static TvPipMenuController providesTvPipMenuController(Context context, PipBoundsState pipBoundsState, SystemWindows systemWindows, PipMediaController pipMediaController, Handler handler) {
        return (TvPipMenuController) Preconditions.checkNotNullFromProvides(TvPipModule.providesTvPipMenuController(context, pipBoundsState, systemWindows, pipMediaController, handler));
    }
}
