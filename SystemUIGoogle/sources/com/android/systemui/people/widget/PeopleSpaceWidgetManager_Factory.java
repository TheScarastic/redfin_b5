package com.android.systemui.people.widget;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.os.UserManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PeopleSpaceWidgetManager_Factory implements Factory<PeopleSpaceWidgetManager> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    private final Provider<Context> contextProvider;
    private final Provider<LauncherApps> launcherAppsProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<NotificationManager> notificationManagerProvider;
    private final Provider<PackageManager> packageManagerProvider;
    private final Provider<UserManager> userManagerProvider;

    public PeopleSpaceWidgetManager_Factory(Provider<Context> provider, Provider<LauncherApps> provider2, Provider<NotificationEntryManager> provider3, Provider<PackageManager> provider4, Provider<Optional<Bubbles>> provider5, Provider<UserManager> provider6, Provider<NotificationManager> provider7, Provider<BroadcastDispatcher> provider8, Provider<Executor> provider9) {
        this.contextProvider = provider;
        this.launcherAppsProvider = provider2;
        this.notificationEntryManagerProvider = provider3;
        this.packageManagerProvider = provider4;
        this.bubblesOptionalProvider = provider5;
        this.userManagerProvider = provider6;
        this.notificationManagerProvider = provider7;
        this.broadcastDispatcherProvider = provider8;
        this.bgExecutorProvider = provider9;
    }

    @Override // javax.inject.Provider
    public PeopleSpaceWidgetManager get() {
        return newInstance(this.contextProvider.get(), this.launcherAppsProvider.get(), this.notificationEntryManagerProvider.get(), this.packageManagerProvider.get(), this.bubblesOptionalProvider.get(), this.userManagerProvider.get(), this.notificationManagerProvider.get(), this.broadcastDispatcherProvider.get(), this.bgExecutorProvider.get());
    }

    public static PeopleSpaceWidgetManager_Factory create(Provider<Context> provider, Provider<LauncherApps> provider2, Provider<NotificationEntryManager> provider3, Provider<PackageManager> provider4, Provider<Optional<Bubbles>> provider5, Provider<UserManager> provider6, Provider<NotificationManager> provider7, Provider<BroadcastDispatcher> provider8, Provider<Executor> provider9) {
        return new PeopleSpaceWidgetManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static PeopleSpaceWidgetManager newInstance(Context context, LauncherApps launcherApps, NotificationEntryManager notificationEntryManager, PackageManager packageManager, Optional<Bubbles> optional, UserManager userManager, NotificationManager notificationManager, BroadcastDispatcher broadcastDispatcher, Executor executor) {
        return new PeopleSpaceWidgetManager(context, launcherApps, notificationEntryManager, packageManager, optional, userManager, notificationManager, broadcastDispatcher, executor);
    }
}
