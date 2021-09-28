package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.google.android.systemui.columbus.actions.Action;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SetupWizard_Factory implements Factory<SetupWizard> {
    private final Provider<Context> contextProvider;
    private final Provider<Set<Action>> exceptionsProvider;
    private final Provider<DeviceProvisionedController> provisionedControllerProvider;

    public SetupWizard_Factory(Provider<Context> provider, Provider<Set<Action>> provider2, Provider<DeviceProvisionedController> provider3) {
        this.contextProvider = provider;
        this.exceptionsProvider = provider2;
        this.provisionedControllerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public SetupWizard get() {
        return newInstance(this.contextProvider.get(), this.exceptionsProvider.get(), DoubleCheck.lazy(this.provisionedControllerProvider));
    }

    public static SetupWizard_Factory create(Provider<Context> provider, Provider<Set<Action>> provider2, Provider<DeviceProvisionedController> provider3) {
        return new SetupWizard_Factory(provider, provider2, provider3);
    }

    public static SetupWizard newInstance(Context context, Set<Action> set, Lazy<DeviceProvisionedController> lazy) {
        return new SetupWizard(context, set, lazy);
    }
}
