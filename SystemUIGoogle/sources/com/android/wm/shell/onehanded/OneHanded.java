package com.android.wm.shell.onehanded;

import android.content.res.Configuration;
/* loaded from: classes2.dex */
public interface OneHanded {
    default IOneHanded createExternalInterface() {
        return null;
    }

    void onConfigChanged(Configuration configuration);

    void onKeyguardVisibilityChanged(boolean z);

    void onUserSwitch(int i);

    void registerEventCallback(OneHandedEventCallback oneHandedEventCallback);

    void registerTransitionCallback(OneHandedTransitionCallback oneHandedTransitionCallback);

    void setLockedDisabled(boolean z, boolean z2);

    void stopOneHanded();

    void stopOneHanded(int i);
}
