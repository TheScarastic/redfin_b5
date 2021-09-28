package com.android.systemui.statusbar.policy;

import android.graphics.drawable.Drawable;
/* loaded from: classes.dex */
public interface UserInfoController extends CallbackController<OnUserInfoChangedListener> {

    /* loaded from: classes.dex */
    public interface OnUserInfoChangedListener {
        void onUserInfoChanged(String str, Drawable drawable, String str2);
    }

    void reloadUserInfo();
}
