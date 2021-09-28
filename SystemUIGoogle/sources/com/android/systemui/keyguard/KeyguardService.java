package com.android.systemui.keyguard;

import android.app.ActivityTaskManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.util.Slog;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationDefinition;
import android.view.RemoteAnimationTarget;
import android.view.WindowManagerPolicyConstants;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IKeyguardDrawnCallback;
import com.android.internal.policy.IKeyguardExitCallback;
import com.android.internal.policy.IKeyguardService;
import com.android.internal.policy.IKeyguardStateCallback;
import com.android.systemui.SystemUIApplication;
import com.android.wm.shell.transition.Transitions;
/* loaded from: classes.dex */
public class KeyguardService extends Service {
    private static final int sEnableRemoteKeyguardAnimation;
    public static boolean sEnableRemoteKeyguardGoingAwayAnimation;
    public static boolean sEnableRemoteKeyguardOccludeAnimation;
    private final IKeyguardService.Stub mBinder = new IKeyguardService.Stub() { // from class: com.android.systemui.keyguard.KeyguardService.3
        public void addStateMonitorCallback(IKeyguardStateCallback iKeyguardStateCallback) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.addStateMonitorCallback(iKeyguardStateCallback);
        }

        public void verifyUnlock(IKeyguardExitCallback iKeyguardExitCallback) {
            Trace.beginSection("KeyguardService.mBinder#verifyUnlock");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.verifyUnlock(iKeyguardExitCallback);
            Trace.endSection();
        }

        public void setOccluded(boolean z, boolean z2) {
            Trace.beginSection("KeyguardService.mBinder#setOccluded");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.setOccluded(z, z2);
            Trace.endSection();
        }

        public void dismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.dismiss(iKeyguardDismissCallback, charSequence);
        }

        public void onDreamingStarted() {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onDreamingStarted();
        }

        public void onDreamingStopped() {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onDreamingStopped();
        }

        public void onStartedGoingToSleep(int i) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onStartedGoingToSleep(WindowManagerPolicyConstants.translateSleepReasonToOffReason(i));
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(6, i);
        }

        public void onFinishedGoingToSleep(int i, boolean z) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onFinishedGoingToSleep(WindowManagerPolicyConstants.translateSleepReasonToOffReason(i), z);
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(7);
        }

        public void onStartedWakingUp(int i, boolean z) {
            Trace.beginSection("KeyguardService.mBinder#onStartedWakingUp");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onStartedWakingUp(z);
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(4, i);
            Trace.endSection();
        }

        public void onFinishedWakingUp() {
            Trace.beginSection("KeyguardService.mBinder#onFinishedWakingUp");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(5);
            Trace.endSection();
        }

        public void onScreenTurningOn(IKeyguardDrawnCallback iKeyguardDrawnCallback) {
            Trace.beginSection("KeyguardService.mBinder#onScreenTurningOn");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onScreenTurningOn(iKeyguardDrawnCallback);
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(0);
            Trace.endSection();
        }

        public void onScreenTurnedOn() {
            Trace.beginSection("KeyguardService.mBinder#onScreenTurnedOn");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onScreenTurnedOn();
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(1);
            Trace.endSection();
        }

        public void onScreenTurningOff() {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(2);
        }

        public void onScreenTurnedOff() {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onScreenTurnedOff();
            KeyguardService.this.mKeyguardLifecyclesDispatcher.dispatch(3);
        }

        public void setKeyguardEnabled(boolean z) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.setKeyguardEnabled(z);
        }

        public void onSystemReady() {
            Trace.beginSection("KeyguardService.mBinder#onSystemReady");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onSystemReady();
            Trace.endSection();
        }

        public void doKeyguardTimeout(Bundle bundle) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.doKeyguardTimeout(bundle);
        }

        public void setSwitchingUser(boolean z) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.setSwitchingUser(z);
        }

        public void setCurrentUser(int i) {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.setCurrentUser(i);
        }

        public void onBootCompleted() {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onBootCompleted();
        }

        @Deprecated
        public void startKeyguardExitAnimation(long j, long j2) {
            Trace.beginSection("KeyguardService.mBinder#startKeyguardExitAnimation");
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.startKeyguardExitAnimation(j, j2);
            Trace.endSection();
        }

        public void onShortPowerPressedGoHome() {
            KeyguardService.this.checkPermission();
            KeyguardService.this.mKeyguardViewMediator.onShortPowerPressedGoHome();
        }
    };
    private final IRemoteAnimationRunner.Stub mExitAnimationRunner;
    private final KeyguardLifecyclesDispatcher mKeyguardLifecyclesDispatcher;
    private final KeyguardViewMediator mKeyguardViewMediator;
    private final IRemoteAnimationRunner.Stub mOccludeAnimationRunner;

    static {
        boolean z = false;
        int i = SystemProperties.getInt("persist.wm.enable_remote_keyguard_animation", 0);
        sEnableRemoteKeyguardAnimation = i;
        boolean z2 = Transitions.ENABLE_SHELL_TRANSITIONS;
        sEnableRemoteKeyguardGoingAwayAnimation = !z2 && i >= 1;
        if (!z2 && i >= 2) {
            z = true;
        }
        sEnableRemoteKeyguardOccludeAnimation = z;
    }

    public KeyguardService(KeyguardViewMediator keyguardViewMediator, KeyguardLifecyclesDispatcher keyguardLifecyclesDispatcher) {
        AnonymousClass1 r1 = new IRemoteAnimationRunner.Stub() { // from class: com.android.systemui.keyguard.KeyguardService.1
            public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
                Trace.beginSection("KeyguardService.mBinder#startKeyguardExitAnimation");
                KeyguardService.this.checkPermission();
                KeyguardService.this.mKeyguardViewMediator.startKeyguardExitAnimation(i, remoteAnimationTargetArr, remoteAnimationTargetArr2, null, iRemoteAnimationFinishedCallback);
                Trace.endSection();
            }

            public void onAnimationCancelled() {
                KeyguardService.this.mKeyguardViewMediator.cancelKeyguardExitAnimation();
            }
        };
        this.mExitAnimationRunner = r1;
        AnonymousClass2 r6 = new IRemoteAnimationRunner.Stub() { // from class: com.android.systemui.keyguard.KeyguardService.2
            public void onAnimationCancelled() {
            }

            public void onAnimationStart(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
                try {
                    if (i == 22) {
                        KeyguardService.this.mBinder.setOccluded(true, true);
                    } else if (i == 23) {
                        KeyguardService.this.mBinder.setOccluded(false, true);
                    }
                    iRemoteAnimationFinishedCallback.onAnimationFinished();
                } catch (RemoteException unused) {
                    Slog.e("KeyguardService", "RemoteException");
                }
            }
        };
        this.mOccludeAnimationRunner = r6;
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mKeyguardLifecyclesDispatcher = keyguardLifecyclesDispatcher;
        RemoteAnimationDefinition remoteAnimationDefinition = new RemoteAnimationDefinition();
        if (sEnableRemoteKeyguardGoingAwayAnimation) {
            RemoteAnimationAdapter remoteAnimationAdapter = new RemoteAnimationAdapter(r1, 0, 0);
            remoteAnimationDefinition.addRemoteAnimation(20, remoteAnimationAdapter);
            remoteAnimationDefinition.addRemoteAnimation(21, remoteAnimationAdapter);
        }
        if (sEnableRemoteKeyguardOccludeAnimation) {
            RemoteAnimationAdapter remoteAnimationAdapter2 = new RemoteAnimationAdapter(r6, 0, 0);
            remoteAnimationDefinition.addRemoteAnimation(22, remoteAnimationAdapter2);
            remoteAnimationDefinition.addRemoteAnimation(23, remoteAnimationAdapter2);
        }
        ActivityTaskManager.getInstance().registerRemoteAnimationsForDisplay(0, remoteAnimationDefinition);
    }

    @Override // android.app.Service
    public void onCreate() {
        ((SystemUIApplication) getApplication()).startServicesIfNeeded();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    void checkPermission() {
        if (Binder.getCallingUid() != 1000 && getBaseContext().checkCallingOrSelfPermission("android.permission.CONTROL_KEYGUARD") != 0) {
            Log.w("KeyguardService", "Caller needs permission 'android.permission.CONTROL_KEYGUARD' to call " + Debug.getCaller());
            throw new SecurityException("Access denied to process: " + Binder.getCallingPid() + ", must have permission android.permission.CONTROL_KEYGUARD");
        }
    }
}
