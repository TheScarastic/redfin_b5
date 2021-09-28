package com.google.android.systemui.columbus.gates;

import android.app.IActivityManager;
import android.content.Context;
import android.os.Handler;
import com.google.android.systemui.columbus.actions.Action;
import dagger.internal.Factory;
import java.util.List;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class CameraVisibility_Factory implements Factory<CameraVisibility> {
    private final Provider<IActivityManager> activityManagerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<List<Action>> exceptionsProvider;
    private final Provider<KeyguardVisibility> keyguardGateProvider;
    private final Provider<PowerState> powerStateProvider;
    private final Provider<Handler> updateHandlerProvider;

    public CameraVisibility_Factory(Provider<Context> provider, Provider<List<Action>> provider2, Provider<KeyguardVisibility> provider3, Provider<PowerState> provider4, Provider<IActivityManager> provider5, Provider<Handler> provider6) {
        this.contextProvider = provider;
        this.exceptionsProvider = provider2;
        this.keyguardGateProvider = provider3;
        this.powerStateProvider = provider4;
        this.activityManagerProvider = provider5;
        this.updateHandlerProvider = provider6;
    }

    @Override // javax.inject.Provider
    public CameraVisibility get() {
        return newInstance(this.contextProvider.get(), this.exceptionsProvider.get(), this.keyguardGateProvider.get(), this.powerStateProvider.get(), this.activityManagerProvider.get(), this.updateHandlerProvider.get());
    }

    public static CameraVisibility_Factory create(Provider<Context> provider, Provider<List<Action>> provider2, Provider<KeyguardVisibility> provider3, Provider<PowerState> provider4, Provider<IActivityManager> provider5, Provider<Handler> provider6) {
        return new CameraVisibility_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static CameraVisibility newInstance(Context context, List<Action> list, KeyguardVisibility keyguardVisibility, PowerState powerState, IActivityManager iActivityManager, Handler handler) {
        return new CameraVisibility(context, list, keyguardVisibility, powerState, iActivityManager, handler);
    }
}
