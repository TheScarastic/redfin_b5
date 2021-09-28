package com.android.systemui.statusbar.notification.row;

import android.app.INotificationManager;
import android.content.Context;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialog;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ChannelEditorDialogController_Factory implements Factory<ChannelEditorDialogController> {
    private final Provider<Context> cProvider;
    private final Provider<ChannelEditorDialog.Builder> dialogBuilderProvider;
    private final Provider<INotificationManager> noManProvider;

    public ChannelEditorDialogController_Factory(Provider<Context> provider, Provider<INotificationManager> provider2, Provider<ChannelEditorDialog.Builder> provider3) {
        this.cProvider = provider;
        this.noManProvider = provider2;
        this.dialogBuilderProvider = provider3;
    }

    @Override // javax.inject.Provider
    public ChannelEditorDialogController get() {
        return newInstance(this.cProvider.get(), this.noManProvider.get(), this.dialogBuilderProvider.get());
    }

    public static ChannelEditorDialogController_Factory create(Provider<Context> provider, Provider<INotificationManager> provider2, Provider<ChannelEditorDialog.Builder> provider3) {
        return new ChannelEditorDialogController_Factory(provider, provider2, provider3);
    }

    public static ChannelEditorDialogController newInstance(Context context, INotificationManager iNotificationManager, ChannelEditorDialog.Builder builder) {
        return new ChannelEditorDialogController(context, iNotificationManager, builder);
    }
}
