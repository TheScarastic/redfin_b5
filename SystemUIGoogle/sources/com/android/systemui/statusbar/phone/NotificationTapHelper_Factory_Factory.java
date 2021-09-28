package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationTapHelper_Factory_Factory implements Factory<NotificationTapHelper.Factory> {
    private final Provider<DelayableExecutor> delayableExecutorProvider;
    private final Provider<FalsingManager> falsingManagerProvider;

    public NotificationTapHelper_Factory_Factory(Provider<FalsingManager> provider, Provider<DelayableExecutor> provider2) {
        this.falsingManagerProvider = provider;
        this.delayableExecutorProvider = provider2;
    }

    @Override // javax.inject.Provider
    public NotificationTapHelper.Factory get() {
        return newInstance(this.falsingManagerProvider.get(), this.delayableExecutorProvider.get());
    }

    public static NotificationTapHelper_Factory_Factory create(Provider<FalsingManager> provider, Provider<DelayableExecutor> provider2) {
        return new NotificationTapHelper_Factory_Factory(provider, provider2);
    }

    public static NotificationTapHelper.Factory newInstance(FalsingManager falsingManager, DelayableExecutor delayableExecutor) {
        return new NotificationTapHelper.Factory(falsingManager, delayableExecutor);
    }
}
