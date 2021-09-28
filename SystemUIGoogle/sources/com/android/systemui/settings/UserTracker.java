package com.android.systemui.settings;

import android.content.Context;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UserTracker.kt */
/* loaded from: classes.dex */
public interface UserTracker extends UserContentResolverProvider, UserContextProvider {

    /* compiled from: UserTracker.kt */
    /* loaded from: classes.dex */
    public interface Callback {
        default void onProfilesChanged(List<? extends UserInfo> list) {
            Intrinsics.checkNotNullParameter(list, "profiles");
        }

        default void onUserChanged(int i, Context context) {
            Intrinsics.checkNotNullParameter(context, "userContext");
        }
    }

    void addCallback(Callback callback, Executor executor);

    UserHandle getUserHandle();

    int getUserId();

    UserInfo getUserInfo();

    List<UserInfo> getUserProfiles();

    void removeCallback(Callback callback);
}
