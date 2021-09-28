package com.android.systemui.user;

import com.android.settingslib.users.EditUserInfoController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class UserModule_ProvideEditUserInfoControllerFactory implements Factory<EditUserInfoController> {
    private final UserModule module;

    public UserModule_ProvideEditUserInfoControllerFactory(UserModule userModule) {
        this.module = userModule;
    }

    @Override // javax.inject.Provider
    public EditUserInfoController get() {
        return provideEditUserInfoController(this.module);
    }

    public static UserModule_ProvideEditUserInfoControllerFactory create(UserModule userModule) {
        return new UserModule_ProvideEditUserInfoControllerFactory(userModule);
    }

    public static EditUserInfoController provideEditUserInfoController(UserModule userModule) {
        return (EditUserInfoController) Preconditions.checkNotNullFromProvides(userModule.provideEditUserInfoController());
    }
}
