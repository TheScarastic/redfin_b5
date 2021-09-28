package com.google.android.systemui.elmyra.gates;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.TaskStackListener;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.android.systemui.R$string;
import com.google.android.systemui.elmyra.actions.Action;
import com.google.android.systemui.elmyra.actions.CameraAction;
import com.google.android.systemui.elmyra.gates.Gate;
import java.util.List;
/* loaded from: classes2.dex */
public class CameraVisibility extends Gate {
    private final CameraAction mCameraAction;
    private final String mCameraPackageName;
    private boolean mCameraShowing;
    private final List<Action> mExceptions;
    private final Gate.Listener mGateListener;
    private final KeyguardVisibility mKeyguardGate;
    private final PackageManager mPackageManager;
    private final PowerState mPowerState;
    private final Handler mUpdateHandler;
    private final TaskStackListener mTaskStackListener = new TaskStackListener() { // from class: com.google.android.systemui.elmyra.gates.CameraVisibility.1
        public void onTaskStackChanged() {
            CameraVisibility.this.mUpdateHandler.post(new CameraVisibility$1$$ExternalSyntheticLambda0(CameraVisibility.this));
        }
    };
    private final IActivityManager mActivityManager = ActivityManager.getService();

    public CameraVisibility(Context context, CameraAction cameraAction, List<Action> list) {
        super(context);
        AnonymousClass2 r0 = new Gate.Listener() { // from class: com.google.android.systemui.elmyra.gates.CameraVisibility.2
            @Override // com.google.android.systemui.elmyra.gates.Gate.Listener
            public void onGateChanged(Gate gate) {
                CameraVisibility.this.mUpdateHandler.post(new CameraVisibility$2$$ExternalSyntheticLambda0(CameraVisibility.this));
            }
        };
        this.mGateListener = r0;
        this.mCameraAction = cameraAction;
        this.mExceptions = list;
        this.mPackageManager = context.getPackageManager();
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        KeyguardVisibility keyguardVisibility = new KeyguardVisibility(context);
        this.mKeyguardGate = keyguardVisibility;
        PowerState powerState = new PowerState(context);
        this.mPowerState = powerState;
        keyguardVisibility.setListener(r0);
        powerState.setListener(r0);
        this.mCameraPackageName = context.getResources().getString(R$string.google_camera_app_package_name);
        this.mUpdateHandler = new Handler(context.getMainLooper());
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onActivate() {
        this.mKeyguardGate.activate();
        this.mPowerState.activate();
        this.mCameraShowing = isCameraShowing();
        try {
            this.mActivityManager.registerTaskStackListener(this.mTaskStackListener);
        } catch (RemoteException e) {
            Log.e("Elmyra/CameraVisibility", "Could not register task stack listener", e);
        }
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onDeactivate() {
        this.mKeyguardGate.deactivate();
        this.mPowerState.deactivate();
        try {
            this.mActivityManager.unregisterTaskStackListener(this.mTaskStackListener);
        } catch (RemoteException e) {
            Log.e("Elmyra/CameraVisibility", "Could not unregister task stack listener", e);
        }
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected boolean isBlocked() {
        for (int i = 0; i < this.mExceptions.size(); i++) {
            if (this.mExceptions.get(i).isAvailable()) {
                return false;
            }
        }
        if (!this.mCameraShowing || this.mCameraAction.isAvailable()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void updateCameraIsShowing() {
        boolean isCameraShowing = isCameraShowing();
        if (this.mCameraShowing != isCameraShowing) {
            this.mCameraShowing = isCameraShowing;
            notifyListener();
        }
    }

    public boolean isCameraShowing() {
        return isCameraTopActivity() && isCameraInForeground() && !this.mPowerState.isBlocking();
    }

    private boolean isCameraTopActivity() {
        try {
            List tasks = ActivityManager.getService().getTasks(1);
            if (tasks.isEmpty()) {
                return false;
            }
            return ((ActivityManager.RunningTaskInfo) tasks.get(0)).topActivity.getPackageName().equalsIgnoreCase(this.mCameraPackageName);
        } catch (RemoteException e) {
            Log.e("Elmyra/CameraVisibility", "unable to check task stack", e);
            return false;
        }
    }

    private boolean isCameraInForeground() {
        try {
            UserInfo currentUser = this.mActivityManager.getCurrentUser();
            int i = this.mPackageManager.getApplicationInfoAsUser(this.mCameraPackageName, 0, currentUser != null ? currentUser.id : 0).uid;
            List runningAppProcesses = this.mActivityManager.getRunningAppProcesses();
            for (int i2 = 0; i2 < runningAppProcesses.size(); i2++) {
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) runningAppProcesses.get(i2);
                if (runningAppProcessInfo.uid == i && runningAppProcessInfo.processName.equalsIgnoreCase(this.mCameraPackageName)) {
                    if (runningAppProcessInfo.importance == 100) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException unused) {
        } catch (RemoteException e) {
            Log.e("Elmyra/CameraVisibility", "Could not check camera foreground status", e);
        }
        return false;
    }
}
