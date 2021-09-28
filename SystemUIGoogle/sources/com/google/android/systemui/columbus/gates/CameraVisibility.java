package com.google.android.systemui.columbus.gates;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.systemui.columbus.actions.Action;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsJVMKt;
/* compiled from: CameraVisibility.kt */
/* loaded from: classes2.dex */
public final class CameraVisibility extends Gate {
    public static final Companion Companion = new Companion(null);
    private final IActivityManager activityManager;
    private boolean cameraShowing;
    private boolean exceptionActive;
    private final List<Action> exceptions;
    private final KeyguardVisibility keyguardGate;
    private final PackageManager packageManager;
    private final PowerState powerState;
    private final Handler updateHandler;
    private final CameraVisibility$taskStackListener$1 taskStackListener = new CameraVisibility$taskStackListener$1(this);
    private final CameraVisibility$gateListener$1 gateListener = new CameraVisibility$gateListener$1(this);
    private final CameraVisibility$actionListener$1 actionListener = new CameraVisibility$actionListener$1(this);

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public CameraVisibility(Context context, List<Action> list, KeyguardVisibility keyguardVisibility, PowerState powerState, IActivityManager iActivityManager, Handler handler) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(list, "exceptions");
        Intrinsics.checkNotNullParameter(keyguardVisibility, "keyguardGate");
        Intrinsics.checkNotNullParameter(powerState, "powerState");
        Intrinsics.checkNotNullParameter(iActivityManager, "activityManager");
        Intrinsics.checkNotNullParameter(handler, "updateHandler");
        this.exceptions = list;
        this.keyguardGate = keyguardVisibility;
        this.powerState = powerState;
        this.activityManager = iActivityManager;
        this.updateHandler = handler;
        this.packageManager = context.getPackageManager();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onActivate() {
        this.exceptionActive = false;
        for (Action action : this.exceptions) {
            action.registerListener(this.actionListener);
            this.exceptionActive = action.isAvailable() | this.exceptionActive;
        }
        this.cameraShowing = isCameraShowing();
        this.keyguardGate.registerListener(this.gateListener);
        this.powerState.registerListener(this.gateListener);
        try {
            this.activityManager.registerTaskStackListener(this.taskStackListener);
        } catch (RemoteException e) {
            Log.e("Columbus/CameraVis", "Could not register task stack listener", e);
        }
        updateBlocking();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onDeactivate() {
        this.keyguardGate.unregisterListener(this.gateListener);
        this.powerState.unregisterListener(this.gateListener);
        for (Action action : this.exceptions) {
            action.unregisterListener(this.actionListener);
        }
        try {
            this.activityManager.unregisterTaskStackListener(this.taskStackListener);
        } catch (RemoteException e) {
            Log.e("Columbus/CameraVis", "Could not unregister task stack listener", e);
        }
    }

    /* access modifiers changed from: private */
    public final void updateBlocking() {
        setBlocking(!this.exceptionActive && this.cameraShowing);
    }

    /* access modifiers changed from: private */
    public final void updateCameraIsShowing() {
        this.cameraShowing = isCameraShowing();
        updateBlocking();
    }

    public final boolean isCameraShowing() {
        return isCameraTopActivity() && isCameraInForeground() && !this.powerState.isBlocking();
    }

    private final boolean isCameraTopActivity() {
        String str;
        try {
            List tasks = this.activityManager.getTasks(1);
            if (tasks.isEmpty()) {
                return false;
            }
            ComponentName componentName = ((ActivityManager.RunningTaskInfo) tasks.get(0)).topActivity;
            if (componentName == null) {
                str = null;
            } else {
                str = componentName.getPackageName();
            }
            return StringsKt__StringsJVMKt.equals(str, "com.google.android.GoogleCamera", true);
        } catch (RemoteException e) {
            Log.e("Columbus/CameraVis", "unable to check task stack", e);
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0053  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x005e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean isCameraInForeground() {
        /*
            r8 = this;
            java.lang.String r0 = "com.google.android.GoogleCamera"
            r1 = 1
            r2 = 0
            r3 = 0
            android.content.pm.PackageManager r4 = r8.packageManager     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            android.app.IActivityManager r5 = r8.activityManager     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            android.content.pm.UserInfo r5 = r5.getCurrentUser()     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            int r5 = r5.id     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            android.content.pm.ApplicationInfo r4 = r4.getApplicationInfoAsUser(r0, r2, r5)     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            int r4 = r4.uid     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            android.app.IActivityManager r8 = r8.activityManager     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            java.util.List r8 = r8.getRunningAppProcesses()     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            java.lang.String r5 = "activityManager.runningAppProcesses"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r8, r5)     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            java.util.Iterator r8 = r8.iterator()     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
        L_0x0024:
            boolean r5 = r8.hasNext()     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            if (r5 == 0) goto L_0x0043
            java.lang.Object r5 = r8.next()     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            r6 = r5
            android.app.ActivityManager$RunningAppProcessInfo r6 = (android.app.ActivityManager.RunningAppProcessInfo) r6     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            int r7 = r6.uid     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            if (r7 != r4) goto L_0x003f
            java.lang.String r6 = r6.processName     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            boolean r6 = kotlin.text.StringsKt.equals(r6, r0, r1)     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            if (r6 == 0) goto L_0x003f
            r6 = r1
            goto L_0x0040
        L_0x003f:
            r6 = r2
        L_0x0040:
            if (r6 == 0) goto L_0x0024
            goto L_0x0044
        L_0x0043:
            r5 = r3
        L_0x0044:
            android.app.ActivityManager$RunningAppProcessInfo r5 = (android.app.ActivityManager.RunningAppProcessInfo) r5     // Catch: NameNotFoundException -> 0x004f, RemoteException -> 0x0047
            goto L_0x0050
        L_0x0047:
            r8 = move-exception
            java.lang.String r0 = "Columbus/CameraVis"
            java.lang.String r4 = "Could not check camera foreground status"
            android.util.Log.e(r0, r4, r8)
        L_0x004f:
            r5 = r3
        L_0x0050:
            if (r5 != 0) goto L_0x0053
            goto L_0x0059
        L_0x0053:
            int r8 = r5.importance
            java.lang.Integer r3 = java.lang.Integer.valueOf(r8)
        L_0x0059:
            r8 = 100
            if (r3 != 0) goto L_0x005e
            goto L_0x0065
        L_0x005e:
            int r0 = r3.intValue()
            if (r0 != r8) goto L_0x0065
            goto L_0x0066
        L_0x0065:
            r1 = r2
        L_0x0066:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.columbus.gates.CameraVisibility.isCameraInForeground():boolean");
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public String toString() {
        return super.toString() + " [cameraShowing -> " + this.cameraShowing + "; exceptionActive -> " + this.exceptionActive + ']';
    }

    /* compiled from: CameraVisibility.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
