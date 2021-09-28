package com.google.android.systemui.assist;

import android.app.ActivityManager;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.widget.ILockSettings;
import com.android.systemui.util.Assert;
/* loaded from: classes2.dex */
public class OpaEnabledSettings {
    private final Context mContext;
    private final ILockSettings mLockSettings = ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings"));

    public OpaEnabledSettings(Context context) {
        this.mContext = context;
    }

    public boolean isOpaEligible() {
        Assert.isNotMainThread();
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "systemui.google.opa_enabled", 0, ActivityManager.getCurrentUser()) != 0) {
            return true;
        }
        return false;
    }

    public boolean isOpaEnabled() {
        Assert.isNotMainThread();
        try {
            return this.mLockSettings.getBoolean("systemui.google.opa_user_enabled", false, ActivityManager.getCurrentUser());
        } catch (RemoteException e) {
            Log.e("OpaEnabledSettings", "isOpaEnabled RemoteException", e);
            return false;
        }
    }

    public boolean isAgsaAssistant() {
        Assert.isNotMainThread();
        return OpaUtils.isAGSACurrentAssistant(this.mContext);
    }

    public boolean isLongPressHomeEnabled() {
        Assert.isNotMainThread();
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "assist_long_press_home_enabled", this.mContext.getResources().getBoolean(17891367) ? 1 : 0) != 0;
    }

    public void setOpaEligible(boolean z) {
        Assert.isNotMainThread();
        Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "systemui.google.opa_enabled", z ? 1 : 0, ActivityManager.getCurrentUser());
    }

    public void setOpaEnabled(boolean z) {
        Assert.isNotMainThread();
        try {
            this.mLockSettings.setBoolean("systemui.google.opa_user_enabled", z, ActivityManager.getCurrentUser());
        } catch (RemoteException e) {
            Log.e("OpaEnabledSettings", "RemoteException on OPA_USER_ENABLED", e);
        }
    }
}
