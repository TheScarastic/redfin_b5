package com.android.systemui.statusbar.policy;

import android.app.admin.DeviceAdminInfo;
import android.content.ComponentName;
import android.graphics.drawable.Drawable;
import com.android.systemui.Dumpable;
/* loaded from: classes.dex */
public interface SecurityController extends CallbackController<SecurityControllerCallback>, Dumpable {

    /* loaded from: classes.dex */
    public interface SecurityControllerCallback {
        void onStateChanged();
    }

    DeviceAdminInfo getDeviceAdminInfo();

    ComponentName getDeviceOwnerComponentOnAnyUser();

    CharSequence getDeviceOwnerOrganizationName();

    int getDeviceOwnerType(ComponentName componentName);

    Drawable getIcon(DeviceAdminInfo deviceAdminInfo);

    CharSequence getLabel(DeviceAdminInfo deviceAdminInfo);

    String getPrimaryVpnName();

    CharSequence getWorkProfileOrganizationName();

    String getWorkProfileVpnName();

    boolean hasCACertInCurrentUser();

    boolean hasCACertInWorkProfile();

    boolean hasWorkProfile();

    boolean isDeviceManaged();

    boolean isNetworkLoggingEnabled();

    boolean isParentalControlsEnabled();

    boolean isProfileOwnerOfOrganizationOwnedDevice();

    boolean isVpnBranded();

    boolean isVpnEnabled();

    boolean isWorkProfileOn();
}
