package com.android.systemui.user;

import android.app.IActivityManager;
import com.android.settingslib.users.EditUserInfoController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class CreateUserActivity_Factory implements Factory<CreateUserActivity> {
    private final Provider<IActivityManager> activityManagerProvider;
    private final Provider<EditUserInfoController> editUserInfoControllerProvider;
    private final Provider<UserCreator> userCreatorProvider;

    public CreateUserActivity_Factory(Provider<UserCreator> provider, Provider<EditUserInfoController> provider2, Provider<IActivityManager> provider3) {
        this.userCreatorProvider = provider;
        this.editUserInfoControllerProvider = provider2;
        this.activityManagerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public CreateUserActivity get() {
        return newInstance(this.userCreatorProvider.get(), this.editUserInfoControllerProvider.get(), this.activityManagerProvider.get());
    }

    public static CreateUserActivity_Factory create(Provider<UserCreator> provider, Provider<EditUserInfoController> provider2, Provider<IActivityManager> provider3) {
        return new CreateUserActivity_Factory(provider, provider2, provider3);
    }

    public static CreateUserActivity newInstance(UserCreator userCreator, EditUserInfoController editUserInfoController, IActivityManager iActivityManager) {
        return new CreateUserActivity(userCreator, editUserInfoController, iActivityManager);
    }
}
