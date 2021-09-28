package com.android.systemui.statusbar.phone;

import android.os.UserManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QSDetailDisplayer;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MultiUserSwitchController_Factory implements Factory<MultiUserSwitchController> {
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<QSDetailDisplayer> qsDetailDisplayerProvider;
    private final Provider<UserManager> userManagerProvider;
    private final Provider<UserSwitcherController> userSwitcherControllerProvider;
    private final Provider<MultiUserSwitch> viewProvider;

    public MultiUserSwitchController_Factory(Provider<MultiUserSwitch> provider, Provider<UserManager> provider2, Provider<UserSwitcherController> provider3, Provider<QSDetailDisplayer> provider4, Provider<FalsingManager> provider5) {
        this.viewProvider = provider;
        this.userManagerProvider = provider2;
        this.userSwitcherControllerProvider = provider3;
        this.qsDetailDisplayerProvider = provider4;
        this.falsingManagerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public MultiUserSwitchController get() {
        return newInstance(this.viewProvider.get(), this.userManagerProvider.get(), this.userSwitcherControllerProvider.get(), this.qsDetailDisplayerProvider.get(), this.falsingManagerProvider.get());
    }

    public static MultiUserSwitchController_Factory create(Provider<MultiUserSwitch> provider, Provider<UserManager> provider2, Provider<UserSwitcherController> provider3, Provider<QSDetailDisplayer> provider4, Provider<FalsingManager> provider5) {
        return new MultiUserSwitchController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static MultiUserSwitchController newInstance(MultiUserSwitch multiUserSwitch, UserManager userManager, UserSwitcherController userSwitcherController, QSDetailDisplayer qSDetailDisplayer, FalsingManager falsingManager) {
        return new MultiUserSwitchController(multiUserSwitch, userManager, userSwitcherController, qSDetailDisplayer, falsingManager);
    }
}
