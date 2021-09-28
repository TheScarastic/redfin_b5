package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.sizecompatui.SizeCompatUIController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideSizeCompatUIControllerFactory implements Factory<SizeCompatUIController> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<DisplayImeController> imeControllerProvider;
    private final Provider<SyncTransactionQueue> syncQueueProvider;

    public WMShellBaseModule_ProvideSizeCompatUIControllerFactory(Provider<Context> provider, Provider<DisplayController> provider2, Provider<DisplayImeController> provider3, Provider<SyncTransactionQueue> provider4) {
        this.contextProvider = provider;
        this.displayControllerProvider = provider2;
        this.imeControllerProvider = provider3;
        this.syncQueueProvider = provider4;
    }

    @Override // javax.inject.Provider
    public SizeCompatUIController get() {
        return provideSizeCompatUIController(this.contextProvider.get(), this.displayControllerProvider.get(), this.imeControllerProvider.get(), this.syncQueueProvider.get());
    }

    public static WMShellBaseModule_ProvideSizeCompatUIControllerFactory create(Provider<Context> provider, Provider<DisplayController> provider2, Provider<DisplayImeController> provider3, Provider<SyncTransactionQueue> provider4) {
        return new WMShellBaseModule_ProvideSizeCompatUIControllerFactory(provider, provider2, provider3, provider4);
    }

    public static SizeCompatUIController provideSizeCompatUIController(Context context, DisplayController displayController, DisplayImeController displayImeController, SyncTransactionQueue syncTransactionQueue) {
        return (SizeCompatUIController) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideSizeCompatUIController(context, displayController, displayImeController, syncTransactionQueue));
    }
}
