package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.policy.CallbackController;
/* loaded from: classes.dex */
public interface ManagedProfileController extends CallbackController<Callback> {

    /* loaded from: classes.dex */
    public interface Callback {
        void onManagedProfileChanged();

        void onManagedProfileRemoved();
    }

    boolean hasActiveProfile();

    boolean isWorkModeEnabled();

    void setWorkModeEnabled(boolean z);
}
