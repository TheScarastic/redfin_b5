package com.android.settingslib;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import com.android.settingslib.RestrictedLockUtils;
import java.util.List;
/* loaded from: classes.dex */
public class RestrictedLockUtilsInternal extends RestrictedLockUtils {
    static Proxy sProxy = new Proxy();

    public static RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced(Context context, String str, int i) {
        if (((DevicePolicyManager) context.getSystemService("device_policy")) == null) {
            return null;
        }
        UserManager userManager = UserManager.get(context);
        List userRestrictionSources = userManager.getUserRestrictionSources(str, UserHandle.of(i));
        if (userRestrictionSources.isEmpty()) {
            return null;
        }
        if (userRestrictionSources.size() > 1) {
            return RestrictedLockUtils.EnforcedAdmin.createDefaultEnforcedAdminWithRestriction(str);
        }
        int userRestrictionSource = ((UserManager.EnforcingUser) userRestrictionSources.get(0)).getUserRestrictionSource();
        int identifier = ((UserManager.EnforcingUser) userRestrictionSources.get(0)).getUserHandle().getIdentifier();
        if (userRestrictionSource == 4) {
            if (identifier == i) {
                return getProfileOwner(context, str, identifier);
            }
            UserInfo profileParent = userManager.getProfileParent(identifier);
            if (profileParent == null || profileParent.id != i) {
                return RestrictedLockUtils.EnforcedAdmin.createDefaultEnforcedAdminWithRestriction(str);
            }
            return getProfileOwner(context, str, identifier);
        } else if (userRestrictionSource != 2) {
            return null;
        } else {
            if (identifier == i) {
                return getDeviceOwner(context, str);
            }
            return RestrictedLockUtils.EnforcedAdmin.createDefaultEnforcedAdminWithRestriction(str);
        }
    }

    public static boolean hasBaseUserRestriction(Context context, String str, int i) {
        return ((UserManager) context.getSystemService("user")).hasBaseUserRestriction(str, UserHandle.of(i));
    }

    private static UserHandle getUserHandleOf(int i) {
        if (i == -10000) {
            return null;
        }
        return UserHandle.of(i);
    }

    private static RestrictedLockUtils.EnforcedAdmin getDeviceOwner(Context context, String str) {
        ComponentName deviceOwnerComponentOnAnyUser;
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        if (devicePolicyManager == null || (deviceOwnerComponentOnAnyUser = devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == null) {
            return null;
        }
        return new RestrictedLockUtils.EnforcedAdmin(deviceOwnerComponentOnAnyUser, str, devicePolicyManager.getDeviceOwnerUser());
    }

    private static RestrictedLockUtils.EnforcedAdmin getProfileOwner(Context context, String str, int i) {
        DevicePolicyManager devicePolicyManager;
        ComponentName profileOwnerAsUser;
        if (i == -10000 || (devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy")) == null || (profileOwnerAsUser = devicePolicyManager.getProfileOwnerAsUser(i)) == null) {
            return null;
        }
        return new RestrictedLockUtils.EnforcedAdmin(profileOwnerAsUser, str, getUserHandleOf(i));
    }

    /* loaded from: classes.dex */
    static class Proxy {
        Proxy() {
        }
    }
}
