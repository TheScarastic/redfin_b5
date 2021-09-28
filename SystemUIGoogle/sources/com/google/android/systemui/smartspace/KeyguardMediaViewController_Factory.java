package com.google.android.systemui.smartspace;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class KeyguardMediaViewController_Factory implements Factory<KeyguardMediaViewController> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<NotificationMediaManager> mediaManagerProvider;
    private final Provider<BcSmartspaceDataPlugin> pluginProvider;
    private final Provider<DelayableExecutor> uiExecutorProvider;

    public KeyguardMediaViewController_Factory(Provider<Context> provider, Provider<BcSmartspaceDataPlugin> provider2, Provider<DelayableExecutor> provider3, Provider<NotificationMediaManager> provider4, Provider<BroadcastDispatcher> provider5) {
        this.contextProvider = provider;
        this.pluginProvider = provider2;
        this.uiExecutorProvider = provider3;
        this.mediaManagerProvider = provider4;
        this.broadcastDispatcherProvider = provider5;
    }

    @Override // javax.inject.Provider
    public KeyguardMediaViewController get() {
        return newInstance(this.contextProvider.get(), this.pluginProvider.get(), this.uiExecutorProvider.get(), this.mediaManagerProvider.get(), this.broadcastDispatcherProvider.get());
    }

    public static KeyguardMediaViewController_Factory create(Provider<Context> provider, Provider<BcSmartspaceDataPlugin> provider2, Provider<DelayableExecutor> provider3, Provider<NotificationMediaManager> provider4, Provider<BroadcastDispatcher> provider5) {
        return new KeyguardMediaViewController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static KeyguardMediaViewController newInstance(Context context, BcSmartspaceDataPlugin bcSmartspaceDataPlugin, DelayableExecutor delayableExecutor, NotificationMediaManager notificationMediaManager, BroadcastDispatcher broadcastDispatcher) {
        return new KeyguardMediaViewController(context, bcSmartspaceDataPlugin, delayableExecutor, notificationMediaManager, broadcastDispatcher);
    }
}
