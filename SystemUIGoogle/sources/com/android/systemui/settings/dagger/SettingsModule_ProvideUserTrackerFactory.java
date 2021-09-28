package com.android.systemui.settings.dagger;

import android.content.Context;
import android.os.Handler;
import android.os.UserManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SettingsModule_ProvideUserTrackerFactory implements Factory<UserTracker> {
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<UserManager> userManagerProvider;

    public SettingsModule_ProvideUserTrackerFactory(Provider<Context> provider, Provider<UserManager> provider2, Provider<DumpManager> provider3, Provider<Handler> provider4) {
        this.contextProvider = provider;
        this.userManagerProvider = provider2;
        this.dumpManagerProvider = provider3;
        this.handlerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public UserTracker get() {
        return provideUserTracker(this.contextProvider.get(), this.userManagerProvider.get(), this.dumpManagerProvider.get(), this.handlerProvider.get());
    }

    public static SettingsModule_ProvideUserTrackerFactory create(Provider<Context> provider, Provider<UserManager> provider2, Provider<DumpManager> provider3, Provider<Handler> provider4) {
        return new SettingsModule_ProvideUserTrackerFactory(provider, provider2, provider3, provider4);
    }

    public static UserTracker provideUserTracker(Context context, UserManager userManager, DumpManager dumpManager, Handler handler) {
        return (UserTracker) Preconditions.checkNotNullFromProvides(SettingsModule.provideUserTracker(context, userManager, dumpManager, handler));
    }
}
