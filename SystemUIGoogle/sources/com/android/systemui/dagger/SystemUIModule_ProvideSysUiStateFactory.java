package com.android.systemui.dagger;

import com.android.systemui.model.SysUiState;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class SystemUIModule_ProvideSysUiStateFactory implements Factory<SysUiState> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final SystemUIModule_ProvideSysUiStateFactory INSTANCE = new SystemUIModule_ProvideSysUiStateFactory();
    }

    @Override // javax.inject.Provider
    public SysUiState get() {
        return provideSysUiState();
    }

    public static SystemUIModule_ProvideSysUiStateFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SysUiState provideSysUiState() {
        return (SysUiState) Preconditions.checkNotNullFromProvides(SystemUIModule.provideSysUiState());
    }
}
