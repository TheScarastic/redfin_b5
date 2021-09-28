package com.android.systemui.qs;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.SecurityController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSSecurityFooter_Factory implements Factory<QSSecurityFooter> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Looper> bgLooperProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<View> rootViewProvider;
    private final Provider<SecurityController> securityControllerProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public QSSecurityFooter_Factory(Provider<View> provider, Provider<UserTracker> provider2, Provider<Handler> provider3, Provider<ActivityStarter> provider4, Provider<SecurityController> provider5, Provider<Looper> provider6) {
        this.rootViewProvider = provider;
        this.userTrackerProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.activityStarterProvider = provider4;
        this.securityControllerProvider = provider5;
        this.bgLooperProvider = provider6;
    }

    @Override // javax.inject.Provider
    public QSSecurityFooter get() {
        return newInstance(this.rootViewProvider.get(), this.userTrackerProvider.get(), this.mainHandlerProvider.get(), this.activityStarterProvider.get(), this.securityControllerProvider.get(), this.bgLooperProvider.get());
    }

    public static QSSecurityFooter_Factory create(Provider<View> provider, Provider<UserTracker> provider2, Provider<Handler> provider3, Provider<ActivityStarter> provider4, Provider<SecurityController> provider5, Provider<Looper> provider6) {
        return new QSSecurityFooter_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static QSSecurityFooter newInstance(View view, UserTracker userTracker, Handler handler, ActivityStarter activityStarter, SecurityController securityController, Looper looper) {
        return new QSSecurityFooter(view, userTracker, handler, activityStarter, securityController, looper);
    }
}
