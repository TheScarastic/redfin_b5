package com.android.systemui.util;

import android.os.UserHandle;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UserAwareController.kt */
/* loaded from: classes2.dex */
public interface UserAwareController {
    default void changeUser(UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(userHandle, "newUser");
    }

    int getCurrentUserId();
}
