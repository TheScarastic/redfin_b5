package com.google.android.systemui.coversheet;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
/* loaded from: classes2.dex */
public class CoversheetService {
    private static final boolean DEBUG = Log.isLoggable("Coversheet", 3);
    private final Context mContext;
    private boolean mKeyguardShowing;
    private boolean mUserUnlocked;
    private final KeyguardUpdateMonitorCallback mCallback = new KeyguardUpdateMonitorCallback() { // from class: com.google.android.systemui.coversheet.CoversheetService.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onKeyguardVisibilityChanged(boolean z) {
            if (CoversheetService.DEBUG) {
                Log.d("Coversheet", "onKeyguardVisibilityChanged");
            }
            CoversheetService.this.mKeyguardShowing = z;
            CoversheetService.this.startCoversheetIfNeeded();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserUnlocked() {
            if (CoversheetService.DEBUG) {
                Log.d("Coversheet", "onUserUnlocked");
            }
            CoversheetService.this.mUserUnlocked = true;
            CoversheetService.this.startCoversheetIfNeeded();
        }
    };
    private final String mBuildId = Build.ID.split("\\.")[0];

    public CoversheetService(Context context) {
        this.mContext = context;
        start();
    }

    /* access modifiers changed from: private */
    public void startCoversheetIfNeeded() {
        boolean z = DEBUG;
        if (z) {
            Log.d("Coversheet", "mKeyguardShowing: " + this.mKeyguardShowing + ", mUserUnlocked: " + this.mUserUnlocked);
        }
        if (!this.mKeyguardShowing && this.mUserUnlocked) {
            ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.getInstance().getRunningTask();
            if (runningTask == null) {
                Log.w("Coversheet", "Not able to get any running task");
                return;
            }
            ActivityManagerWrapper.getInstance();
            boolean isHomeTask = ActivityManagerWrapper.isHomeTask(runningTask);
            if (z) {
                Log.d("Coversheet", "Going to home now? " + isHomeTask);
            }
            if (isHomeTask) {
                Intent intent = new Intent("com.google.android.apps.tips.action.COVERSHEET");
                intent.setPackage("com.google.android.apps.tips");
                intent.setFlags(335544320);
                try {
                    this.mContext.startActivity(intent);
                } catch (ActivityNotFoundException unused) {
                    Log.w("Coversheet", "Coversheet was not found");
                }
                Settings.System.putString(this.mContext.getContentResolver(), "coversheet_id", this.mBuildId);
                ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).removeCallback(this.mCallback);
            }
        }
    }

    private void start() {
        if (!((DeviceProvisionedController) Dependency.get(DeviceProvisionedController.class)).isDeviceProvisioned()) {
            if (DEBUG) {
                Log.d("Coversheet", "Store initial ID: " + this.mBuildId);
            }
            Settings.System.putString(this.mContext.getContentResolver(), "coversheet_id", this.mBuildId);
            return;
        }
        if (!TextUtils.equals(this.mBuildId, Settings.System.getString(this.mContext.getContentResolver(), "coversheet_id"))) {
            if (DEBUG) {
                Log.d("Coversheet", "Register callback.");
            }
            ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(this.mCallback);
        }
    }
}
