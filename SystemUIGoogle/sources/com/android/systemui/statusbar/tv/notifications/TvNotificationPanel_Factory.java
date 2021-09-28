package com.android.systemui.statusbar.tv.notifications;

import android.content.Context;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TvNotificationPanel_Factory implements Factory<TvNotificationPanel> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;

    public TvNotificationPanel_Factory(Provider<Context> provider, Provider<CommandQueue> provider2) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
    }

    @Override // javax.inject.Provider
    public TvNotificationPanel get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get());
    }

    public static TvNotificationPanel_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2) {
        return new TvNotificationPanel_Factory(provider, provider2);
    }

    public static TvNotificationPanel newInstance(Context context, CommandQueue commandQueue) {
        return new TvNotificationPanel(context, commandQueue);
    }
}
