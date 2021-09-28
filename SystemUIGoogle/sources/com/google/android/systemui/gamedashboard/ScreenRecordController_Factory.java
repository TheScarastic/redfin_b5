package com.google.android.systemui.gamedashboard;

import android.os.Handler;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ScreenRecordController_Factory implements Factory<ScreenRecordController> {
    private final Provider<RecordingController> controllerProvider;
    private final Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<ToastController> toastProvider;
    private final Provider<UserContextProvider> userContextProvider;

    public ScreenRecordController_Factory(Provider<RecordingController> provider, Provider<Handler> provider2, Provider<KeyguardDismissUtil> provider3, Provider<UserContextProvider> provider4, Provider<ToastController> provider5) {
        this.controllerProvider = provider;
        this.mainHandlerProvider = provider2;
        this.keyguardDismissUtilProvider = provider3;
        this.userContextProvider = provider4;
        this.toastProvider = provider5;
    }

    @Override // javax.inject.Provider
    public ScreenRecordController get() {
        return newInstance(this.controllerProvider.get(), this.mainHandlerProvider.get(), this.keyguardDismissUtilProvider.get(), this.userContextProvider.get(), this.toastProvider.get());
    }

    public static ScreenRecordController_Factory create(Provider<RecordingController> provider, Provider<Handler> provider2, Provider<KeyguardDismissUtil> provider3, Provider<UserContextProvider> provider4, Provider<ToastController> provider5) {
        return new ScreenRecordController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ScreenRecordController newInstance(RecordingController recordingController, Handler handler, KeyguardDismissUtil keyguardDismissUtil, UserContextProvider userContextProvider, ToastController toastController) {
        return new ScreenRecordController(recordingController, handler, keyguardDismissUtil, userContextProvider, toastController);
    }
}
