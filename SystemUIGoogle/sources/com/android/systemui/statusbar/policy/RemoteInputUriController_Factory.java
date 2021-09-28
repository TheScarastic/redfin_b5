package com.android.systemui.statusbar.policy;

import com.android.internal.statusbar.IStatusBarService;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RemoteInputUriController_Factory implements Factory<RemoteInputUriController> {
    private final Provider<IStatusBarService> statusBarServiceProvider;

    public RemoteInputUriController_Factory(Provider<IStatusBarService> provider) {
        this.statusBarServiceProvider = provider;
    }

    @Override // javax.inject.Provider
    public RemoteInputUriController get() {
        return newInstance(this.statusBarServiceProvider.get());
    }

    public static RemoteInputUriController_Factory create(Provider<IStatusBarService> provider) {
        return new RemoteInputUriController_Factory(provider);
    }

    public static RemoteInputUriController newInstance(IStatusBarService iStatusBarService) {
        return new RemoteInputUriController(iStatusBarService);
    }
}
