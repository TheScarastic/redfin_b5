package com.android.systemui.media.dialog;

import android.content.Context;
import android.media.session.MediaSessionManager;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.phone.ShadeController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaOutputDialogFactory_Factory implements Factory<MediaOutputDialogFactory> {
    private final Provider<Context> contextProvider;
    private final Provider<LocalBluetoothManager> lbmProvider;
    private final Provider<MediaSessionManager> mediaSessionManagerProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<ShadeController> shadeControllerProvider;
    private final Provider<ActivityStarter> starterProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public MediaOutputDialogFactory_Factory(Provider<Context> provider, Provider<MediaSessionManager> provider2, Provider<LocalBluetoothManager> provider3, Provider<ShadeController> provider4, Provider<ActivityStarter> provider5, Provider<NotificationEntryManager> provider6, Provider<UiEventLogger> provider7) {
        this.contextProvider = provider;
        this.mediaSessionManagerProvider = provider2;
        this.lbmProvider = provider3;
        this.shadeControllerProvider = provider4;
        this.starterProvider = provider5;
        this.notificationEntryManagerProvider = provider6;
        this.uiEventLoggerProvider = provider7;
    }

    @Override // javax.inject.Provider
    public MediaOutputDialogFactory get() {
        return newInstance(this.contextProvider.get(), this.mediaSessionManagerProvider.get(), this.lbmProvider.get(), this.shadeControllerProvider.get(), this.starterProvider.get(), this.notificationEntryManagerProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static MediaOutputDialogFactory_Factory create(Provider<Context> provider, Provider<MediaSessionManager> provider2, Provider<LocalBluetoothManager> provider3, Provider<ShadeController> provider4, Provider<ActivityStarter> provider5, Provider<NotificationEntryManager> provider6, Provider<UiEventLogger> provider7) {
        return new MediaOutputDialogFactory_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static MediaOutputDialogFactory newInstance(Context context, MediaSessionManager mediaSessionManager, LocalBluetoothManager localBluetoothManager, ShadeController shadeController, ActivityStarter activityStarter, NotificationEntryManager notificationEntryManager, UiEventLogger uiEventLogger) {
        return new MediaOutputDialogFactory(context, mediaSessionManager, localBluetoothManager, shadeController, activityStarter, notificationEntryManager, uiEventLogger);
    }
}
