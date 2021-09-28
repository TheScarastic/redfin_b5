package com.android.systemui.statusbar.policy;
/* loaded from: classes.dex */
public interface LocationController extends CallbackController<LocationChangeCallback> {

    /* loaded from: classes.dex */
    public interface LocationChangeCallback {
        default void onLocationActiveChanged(boolean z) {
        }

        default void onLocationSettingsChanged(boolean z) {
        }
    }

    boolean isLocationActive();

    boolean isLocationEnabled();

    boolean setLocationEnabled(boolean z);
}
