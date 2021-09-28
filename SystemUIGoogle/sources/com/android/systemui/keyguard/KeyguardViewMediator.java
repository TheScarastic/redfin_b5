package com.android.systemui.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.StatusBarManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.hardware.biometrics.BiometricSourceType;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.EventLog;
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IKeyguardDrawnCallback;
import com.android.internal.policy.IKeyguardExitCallback;
import com.android.internal.policy.IKeyguardStateCallback;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardDisplayManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardViewController;
import com.android.keyguard.ViewMediatorCallback;
import com.android.systemui.DejankUtils;
import com.android.systemui.SystemUI;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.DeviceConfigProxy;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class KeyguardViewMediator extends SystemUI implements StatusBarStateController.StateListener {
    private static final Intent USER_PRESENT_INTENT = new Intent("android.intent.action.USER_PRESENT").addFlags(606076928);
    private AlarmManager mAlarmManager;
    private boolean mAnimatingScreenOff;
    private boolean mAodShowing;
    private AudioManager mAudioManager;
    private boolean mBootCompleted;
    private boolean mBootSendUserPresent;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private CharSequence mCustomMessage;
    private int mDelayedProfileShowingSequence;
    private int mDelayedShowingSequence;
    private DeviceConfigProxy mDeviceConfig;
    private boolean mDeviceInteractive;
    private final DismissCallbackRegistry mDismissCallbackRegistry;
    private DozeParameters mDozeParameters;
    private boolean mDozing;
    private IKeyguardDrawnCallback mDrawnCallback;
    private IKeyguardExitCallback mExitSecureCallback;
    private final FalsingCollector mFalsingCollector;
    private boolean mGoingToSleep;
    private Animation mHideAnimation;
    private boolean mHiding;
    private boolean mInGestureNavigationMode;
    private boolean mInputRestricted;
    private final KeyguardDisplayManager mKeyguardDisplayManager;
    private IRemoteAnimationRunner mKeyguardExitAnimationRunner;
    private final KeyguardStateController mKeyguardStateController;
    private final Lazy<KeyguardUnlockAnimationController> mKeyguardUnlockAnimationControllerLazy;
    private final Lazy<KeyguardViewController> mKeyguardViewControllerLazy;
    private boolean mLockLater;
    private final LockPatternUtils mLockPatternUtils;
    private int mLockSoundId;
    private int mLockSoundStreamId;
    private float mLockSoundVolume;
    private SoundPool mLockSounds;
    private final Lazy<NotificationShadeDepthController> mNotificationShadeDepthController;
    private final DeviceConfig.OnPropertiesChangedListener mOnPropertiesChangedListener;
    private final PowerManager mPM;
    private boolean mPendingLock;
    private boolean mPendingReset;
    private boolean mPulsing;
    private boolean mShowHomeOverLockscreen;
    private PowerManager.WakeLock mShowKeyguardWakeLock;
    private boolean mShowing;
    private boolean mShuttingDown;
    private StatusBarManager mStatusBarManager;
    private final SysuiStatusBarStateController mStatusBarStateController;
    private IRemoteAnimationFinishedCallback mSurfaceBehindRemoteAnimationFinishedCallback;
    private boolean mSurfaceBehindRemoteAnimationRunning;
    private boolean mSystemReady;
    private final TrustManager mTrustManager;
    private int mTrustedSoundId;
    private final Executor mUiBgExecutor;
    private int mUiSoundsStreamType;
    private int mUnlockSoundId;
    private final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    private final KeyguardUpdateMonitor mUpdateMonitor;
    private final UserSwitcherController mUserSwitcherController;
    private boolean mWakeAndUnlocking;
    private WorkLockActivityController mWorkLockController;
    private boolean mExternallyEnabled = true;
    private boolean mNeedToReshowWhenReenabled = false;
    private boolean mOccluded = false;
    private final SparseIntArray mLastSimStates = new SparseIntArray();
    private final SparseBooleanArray mSimWasLocked = new SparseBooleanArray();
    private String mPhoneState = TelephonyManager.EXTRA_STATE_IDLE;
    private boolean mWaitingUntilKeyguardVisible = false;
    private boolean mKeyguardDonePending = false;
    private boolean mHideAnimationRun = false;
    private boolean mHideAnimationRunning = false;
    private final ArrayList<IKeyguardStateCallback> mKeyguardStateCallbacks = new ArrayList<>();
    private boolean mPendingPinLock = false;
    private boolean mPowerGestureIntercepted = false;
    private boolean mSurfaceBehindRemoteAnimationRequested = false;
    KeyguardUpdateMonitorCallback mUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.2
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserInfoChanged(int i) {
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onKeyguardVisibilityChanged(boolean z) {
            synchronized (KeyguardViewMediator.this) {
                if (!z) {
                    if (KeyguardViewMediator.this.mPendingPinLock) {
                        Log.i("KeyguardViewMediator", "PIN lock requested, starting keyguard");
                        KeyguardViewMediator.this.mPendingPinLock = false;
                        KeyguardViewMediator.this.doKeyguardLocked(null);
                    }
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserSwitching(int i) {
            synchronized (KeyguardViewMediator.this) {
                KeyguardViewMediator.this.resetKeyguardDonePendingLocked();
                if (KeyguardViewMediator.this.mLockPatternUtils.isLockScreenDisabled(i)) {
                    KeyguardViewMediator.this.dismiss(null, null);
                } else {
                    KeyguardViewMediator.this.resetStateLocked();
                }
                KeyguardViewMediator.this.adjustStatusBarLocked();
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserSwitchComplete(int i) {
            UserInfo userInfo;
            if (i != 0 && (userInfo = UserManager.get(((SystemUI) KeyguardViewMediator.this).mContext).getUserInfo(i)) != null && !KeyguardViewMediator.this.mLockPatternUtils.isSecure(i)) {
                if (userInfo.isGuest() || userInfo.isDemo()) {
                    KeyguardViewMediator.this.dismiss(null, null);
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onClockVisibilityChanged() {
            KeyguardViewMediator.this.adjustStatusBarLocked();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onDeviceProvisioned() {
            KeyguardViewMediator.this.sendUserPresentBroadcast();
            synchronized (KeyguardViewMediator.this) {
                if (KeyguardViewMediator.this.mustNotUnlockCurrentUser()) {
                    KeyguardViewMediator.this.doKeyguardLocked(null);
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onSimStateChanged(int i, int i2, int i3) {
            boolean z;
            Log.d("KeyguardViewMediator", "onSimStateChanged(subId=" + i + ", slotId=" + i2 + ",state=" + i3 + ")");
            int size = KeyguardViewMediator.this.mKeyguardStateCallbacks.size();
            boolean isSimPinSecure = KeyguardViewMediator.this.mUpdateMonitor.isSimPinSecure();
            for (int i4 = size - 1; i4 >= 0; i4--) {
                try {
                    ((IKeyguardStateCallback) KeyguardViewMediator.this.mKeyguardStateCallbacks.get(i4)).onSimSecureStateChanged(isSimPinSecure);
                } catch (RemoteException e) {
                    Slog.w("KeyguardViewMediator", "Failed to call onSimSecureStateChanged", e);
                    if (e instanceof DeadObjectException) {
                        KeyguardViewMediator.this.mKeyguardStateCallbacks.remove(i4);
                    }
                }
            }
            synchronized (KeyguardViewMediator.this) {
                int i5 = KeyguardViewMediator.this.mLastSimStates.get(i2);
                if (!(i5 == 2 || i5 == 3)) {
                    z = false;
                    KeyguardViewMediator.this.mLastSimStates.append(i2, i3);
                }
                z = true;
                KeyguardViewMediator.this.mLastSimStates.append(i2, i3);
            }
            if (i3 != 1) {
                if (i3 == 2 || i3 == 3) {
                    synchronized (KeyguardViewMediator.this) {
                        KeyguardViewMediator.this.mSimWasLocked.append(i2, true);
                        if (!KeyguardViewMediator.this.mShowing) {
                            Log.d("KeyguardViewMediator", "INTENT_VALUE_ICC_LOCKED and keygaurd isn't showing; need to show keyguard so user can enter sim pin");
                            KeyguardViewMediator.this.doKeyguardLocked(null);
                        } else {
                            KeyguardViewMediator.this.mPendingPinLock = true;
                            KeyguardViewMediator.this.resetStateLocked();
                        }
                    }
                    return;
                } else if (i3 == 5) {
                    synchronized (KeyguardViewMediator.this) {
                        Log.d("KeyguardViewMediator", "READY, reset state? " + KeyguardViewMediator.this.mShowing);
                        if (KeyguardViewMediator.this.mShowing && KeyguardViewMediator.this.mSimWasLocked.get(i2, false)) {
                            Log.d("KeyguardViewMediator", "SIM moved to READY when the previously was locked. Reset the state.");
                            KeyguardViewMediator.this.mSimWasLocked.append(i2, false);
                            KeyguardViewMediator.this.resetStateLocked();
                        }
                    }
                    return;
                } else if (i3 != 6) {
                    if (i3 != 7) {
                        Log.v("KeyguardViewMediator", "Unspecific state: " + i3);
                        return;
                    }
                    synchronized (KeyguardViewMediator.this) {
                        if (!KeyguardViewMediator.this.mShowing) {
                            Log.d("KeyguardViewMediator", "PERM_DISABLED and keygaurd isn't showing.");
                            KeyguardViewMediator.this.doKeyguardLocked(null);
                        } else {
                            Log.d("KeyguardViewMediator", "PERM_DISABLED, resetStateLocked toshow permanently disabled message in lockscreen.");
                            KeyguardViewMediator.this.resetStateLocked();
                        }
                    }
                    return;
                }
            }
            synchronized (KeyguardViewMediator.this) {
                if (KeyguardViewMediator.this.shouldWaitForProvisioning()) {
                    if (!KeyguardViewMediator.this.mShowing) {
                        Log.d("KeyguardViewMediator", "ICC_ABSENT isn't showing, we need to show the keyguard since the device isn't provisioned yet.");
                        KeyguardViewMediator.this.doKeyguardLocked(null);
                    } else {
                        KeyguardViewMediator.this.resetStateLocked();
                    }
                }
                if (i3 == 1) {
                    if (z) {
                        Log.d("KeyguardViewMediator", "SIM moved to ABSENT when the previous state was locked. Reset the state.");
                        KeyguardViewMediator.this.resetStateLocked();
                    }
                    KeyguardViewMediator.this.mSimWasLocked.append(i2, false);
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
            int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            if (KeyguardViewMediator.this.mLockPatternUtils.isSecure(currentUser)) {
                KeyguardViewMediator.this.mLockPatternUtils.getDevicePolicyManager().reportFailedBiometricAttempt(currentUser);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            if (KeyguardViewMediator.this.mLockPatternUtils.isSecure(i)) {
                KeyguardViewMediator.this.mLockPatternUtils.getDevicePolicyManager().reportSuccessfulBiometricAttempt(i);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onTrustChanged(int i) {
            if (i == KeyguardUpdateMonitor.getCurrentUser()) {
                synchronized (KeyguardViewMediator.this) {
                    KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                    keyguardViewMediator.notifyTrustedChangedLocked(keyguardViewMediator.mUpdateMonitor.getUserHasTrust(i));
                }
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onHasLockscreenWallpaperChanged(boolean z) {
            synchronized (KeyguardViewMediator.this) {
                KeyguardViewMediator.this.notifyHasLockscreenWallpaperChanged(z);
            }
        }
    };
    ViewMediatorCallback mViewMediatorCallback = new ViewMediatorCallback() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.3
        @Override // com.android.keyguard.ViewMediatorCallback
        public void userActivity() {
            KeyguardViewMediator.this.userActivity();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void keyguardDone(boolean z, int i) {
            if (i == ActivityManager.getCurrentUser()) {
                KeyguardViewMediator.this.tryKeyguardDone();
            }
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void keyguardDoneDrawing() {
            Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardDoneDrawing");
            KeyguardViewMediator.this.mHandler.sendEmptyMessage(8);
            Trace.endSection();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void setNeedsInput(boolean z) {
            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).setNeedsInput(z);
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void keyguardDonePending(boolean z, int i) {
            Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardDonePending");
            if (i != ActivityManager.getCurrentUser()) {
                Trace.endSection();
                return;
            }
            KeyguardViewMediator.this.mKeyguardDonePending = true;
            KeyguardViewMediator.this.mHideAnimationRun = true;
            KeyguardViewMediator.this.mHideAnimationRunning = true;
            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).startPreHideAnimation(KeyguardViewMediator.this.mHideAnimationFinishedRunnable);
            KeyguardViewMediator.this.mHandler.sendEmptyMessageDelayed(13, 3000);
            Trace.endSection();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void keyguardGone() {
            Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#keyguardGone");
            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).setKeyguardGoingAwayState(false);
            KeyguardViewMediator.this.mKeyguardDisplayManager.hide();
            Trace.endSection();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void readyForKeyguardDone() {
            Trace.beginSection("KeyguardViewMediator.mViewMediatorCallback#readyForKeyguardDone");
            if (KeyguardViewMediator.this.mKeyguardDonePending) {
                KeyguardViewMediator.this.mKeyguardDonePending = false;
                KeyguardViewMediator.this.tryKeyguardDone();
            }
            Trace.endSection();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void resetKeyguard() {
            KeyguardViewMediator.this.resetStateLocked();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void onCancelClicked() {
            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).onCancelClicked();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void onBouncerVisiblityChanged(boolean z) {
            synchronized (KeyguardViewMediator.this) {
                if (z) {
                    KeyguardViewMediator.this.mPendingPinLock = false;
                }
                KeyguardViewMediator.this.adjustStatusBarLocked(z, false);
            }
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public void playTrustedSound() {
            KeyguardViewMediator.this.playTrustedSound();
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public boolean isScreenOn() {
            return KeyguardViewMediator.this.mDeviceInteractive;
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public int getBouncerPromptReason() {
            int currentUser = KeyguardUpdateMonitor.getCurrentUser();
            boolean isTrustUsuallyManaged = KeyguardViewMediator.this.mUpdateMonitor.isTrustUsuallyManaged(currentUser);
            boolean z = isTrustUsuallyManaged || KeyguardViewMediator.this.mUpdateMonitor.isUnlockingWithBiometricsPossible(currentUser);
            KeyguardUpdateMonitor.StrongAuthTracker strongAuthTracker = KeyguardViewMediator.this.mUpdateMonitor.getStrongAuthTracker();
            int strongAuthForUser = strongAuthTracker.getStrongAuthForUser(currentUser);
            if (z && !strongAuthTracker.hasUserAuthenticatedSinceBoot()) {
                return 1;
            }
            if (z && (strongAuthForUser & 16) != 0) {
                return 2;
            }
            if (z && (strongAuthForUser & 2) != 0) {
                return 3;
            }
            if (isTrustUsuallyManaged && (strongAuthForUser & 4) != 0) {
                return 4;
            }
            if (z && (strongAuthForUser & 8) != 0) {
                return 5;
            }
            if (z && (strongAuthForUser & 64) != 0) {
                return 6;
            }
            if (!z || (strongAuthForUser & 128) == 0) {
                return 0;
            }
            return 7;
        }

        @Override // com.android.keyguard.ViewMediatorCallback
        public CharSequence consumeCustomMessage() {
            CharSequence charSequence = KeyguardViewMediator.this.mCustomMessage;
            KeyguardViewMediator.this.mCustomMessage = null;
            return charSequence;
        }
    };
    private final BroadcastReceiver mDelayedLockBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD".equals(intent.getAction())) {
                int intExtra = intent.getIntExtra("seq", 0);
                synchronized (KeyguardViewMediator.this) {
                    if (KeyguardViewMediator.this.mDelayedShowingSequence == intExtra) {
                        KeyguardViewMediator.this.doKeyguardLocked(null);
                    }
                }
            } else if ("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_LOCK".equals(intent.getAction())) {
                int intExtra2 = intent.getIntExtra("seq", 0);
                int intExtra3 = intent.getIntExtra("android.intent.extra.USER_ID", 0);
                if (intExtra3 != 0) {
                    synchronized (KeyguardViewMediator.this) {
                        if (KeyguardViewMediator.this.mDelayedProfileShowingSequence == intExtra2) {
                            KeyguardViewMediator.this.lockProfile(intExtra3);
                        }
                    }
                }
            }
        }
    };
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
                synchronized (KeyguardViewMediator.this) {
                    KeyguardViewMediator.this.mShuttingDown = true;
                }
            }
        }
    };
    private Handler mHandler = new Handler(Looper.myLooper(), null, true) { // from class: com.android.systemui.keyguard.KeyguardViewMediator.6
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    KeyguardViewMediator.this.handleShow((Bundle) message.obj);
                    return;
                case 2:
                    KeyguardViewMediator.this.handleHide();
                    return;
                case 3:
                    KeyguardViewMediator.this.handleReset();
                    return;
                case 4:
                    Trace.beginSection("KeyguardViewMediator#handleMessage VERIFY_UNLOCK");
                    KeyguardViewMediator.this.handleVerifyUnlock();
                    Trace.endSection();
                    return;
                case 5:
                    KeyguardViewMediator.this.handleNotifyFinishedGoingToSleep();
                    return;
                case 6:
                    Trace.beginSection("KeyguardViewMediator#handleMessage NOTIFY_SCREEN_TURNING_ON");
                    KeyguardViewMediator.this.handleNotifyScreenTurningOn((IKeyguardDrawnCallback) message.obj);
                    Trace.endSection();
                    return;
                case 7:
                    Trace.beginSection("KeyguardViewMediator#handleMessage KEYGUARD_DONE");
                    KeyguardViewMediator.this.handleKeyguardDone();
                    Trace.endSection();
                    return;
                case 8:
                    Trace.beginSection("KeyguardViewMediator#handleMessage KEYGUARD_DONE_DRAWING");
                    KeyguardViewMediator.this.handleKeyguardDoneDrawing();
                    Trace.endSection();
                    return;
                case 9:
                    Trace.beginSection("KeyguardViewMediator#handleMessage SET_OCCLUDED");
                    KeyguardViewMediator keyguardViewMediator = KeyguardViewMediator.this;
                    boolean z = true;
                    boolean z2 = message.arg1 != 0;
                    if (message.arg2 == 0) {
                        z = false;
                    }
                    keyguardViewMediator.handleSetOccluded(z2, z);
                    Trace.endSection();
                    return;
                case 10:
                    synchronized (KeyguardViewMediator.this) {
                        KeyguardViewMediator.this.doKeyguardLocked((Bundle) message.obj);
                    }
                    return;
                case 11:
                    DismissMessage dismissMessage = (DismissMessage) message.obj;
                    KeyguardViewMediator.this.handleDismiss(dismissMessage.getCallback(), dismissMessage.getMessage());
                    return;
                case 12:
                    Trace.beginSection("KeyguardViewMediator#handleMessage START_KEYGUARD_EXIT_ANIM");
                    StartKeyguardExitAnimParams startKeyguardExitAnimParams = (StartKeyguardExitAnimParams) message.obj;
                    KeyguardViewMediator.this.handleStartKeyguardExitAnimation(startKeyguardExitAnimParams.startTime, startKeyguardExitAnimParams.fadeoutDuration, startKeyguardExitAnimParams.mApps, startKeyguardExitAnimParams.mWallpapers, startKeyguardExitAnimParams.mNonApps, startKeyguardExitAnimParams.mFinishedCallback);
                    KeyguardViewMediator.this.mFalsingCollector.onSuccessfulUnlock();
                    Trace.endSection();
                    return;
                case 13:
                    Trace.beginSection("KeyguardViewMediator#handleMessage KEYGUARD_DONE_PENDING_TIMEOUT");
                    Log.w("KeyguardViewMediator", "Timeout while waiting for activity drawn!");
                    Trace.endSection();
                    return;
                case 14:
                    Trace.beginSection("KeyguardViewMediator#handleMessage NOTIFY_STARTED_WAKING_UP");
                    KeyguardViewMediator.this.handleNotifyStartedWakingUp();
                    Trace.endSection();
                    return;
                case 15:
                    Trace.beginSection("KeyguardViewMediator#handleMessage NOTIFY_SCREEN_TURNED_ON");
                    KeyguardViewMediator.this.handleNotifyScreenTurnedOn();
                    Trace.endSection();
                    return;
                case 16:
                    KeyguardViewMediator.this.handleNotifyScreenTurnedOff();
                    return;
                case 17:
                    KeyguardViewMediator.this.handleNotifyStartedGoingToSleep();
                    return;
                case 18:
                    KeyguardViewMediator.this.handleSystemReady();
                    return;
                case 19:
                    Trace.beginSection("KeyguardViewMediator#handleMessage CANCEL_KEYGUARD_EXIT_ANIM");
                    KeyguardViewMediator.this.handleCancelKeyguardExitAnimation();
                    Trace.endSection();
                    return;
                default:
                    return;
            }
        }
    };
    private final Runnable mKeyguardGoingAwayRunnable = new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.7
        @Override // java.lang.Runnable
        public void run() {
            Trace.beginSection("KeyguardViewMediator.mKeyGuardGoingAwayRunnable");
            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).keyguardGoingAway();
            int i = (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).shouldDisableWindowAnimationsForUnlock() || (KeyguardViewMediator.this.mWakeAndUnlocking && !KeyguardViewMediator.this.mPulsing) || KeyguardViewMediator.this.isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe()) ? 2 : 0;
            if (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).isGoingToNotificationShade() || (KeyguardViewMediator.this.mWakeAndUnlocking && KeyguardViewMediator.this.mPulsing)) {
                i |= 1;
            }
            if (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).isUnlockWithWallpaper()) {
                i |= 4;
            }
            if (((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).shouldSubtleWindowAnimationsForUnlock()) {
                i |= 8;
            }
            KeyguardViewMediator.this.mUpdateMonitor.setKeyguardGoingAway(true);
            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).setKeyguardGoingAwayState(true);
            KeyguardViewMediator.this.mUiBgExecutor.execute(new KeyguardViewMediator$7$$ExternalSyntheticLambda0(i));
            Trace.endSection();
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ void lambda$run$0(int i) {
            try {
                ActivityTaskManager.getService().keyguardGoingAway(i);
            } catch (RemoteException e) {
                Log.e("KeyguardViewMediator", "Error while calling WindowManager", e);
            }
        }
    };
    private final Runnable mHideAnimationFinishedRunnable = new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardViewMediator.this.lambda$new$5();
        }
    };

    public void onShortPowerPressedGoHome() {
    }

    public KeyguardViewMediator(Context context, FalsingCollector falsingCollector, LockPatternUtils lockPatternUtils, BroadcastDispatcher broadcastDispatcher, Lazy<KeyguardViewController> lazy, DismissCallbackRegistry dismissCallbackRegistry, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, Executor executor, PowerManager powerManager, TrustManager trustManager, UserSwitcherController userSwitcherController, DeviceConfigProxy deviceConfigProxy, NavigationModeController navigationModeController, KeyguardDisplayManager keyguardDisplayManager, DozeParameters dozeParameters, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardStateController keyguardStateController, Lazy<KeyguardUnlockAnimationController> lazy2, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, Lazy<NotificationShadeDepthController> lazy3) {
        super(context);
        DeviceConfig.OnPropertiesChangedListener r4 = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.1
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (properties.getKeyset().contains("nav_bar_handle_show_over_lockscreen")) {
                    KeyguardViewMediator.this.mShowHomeOverLockscreen = properties.getBoolean("nav_bar_handle_show_over_lockscreen", true);
                }
            }
        };
        this.mOnPropertiesChangedListener = r4;
        this.mFalsingCollector = falsingCollector;
        this.mLockPatternUtils = lockPatternUtils;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mKeyguardViewControllerLazy = lazy;
        this.mDismissCallbackRegistry = dismissCallbackRegistry;
        this.mNotificationShadeDepthController = lazy3;
        this.mUiBgExecutor = executor;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mPM = powerManager;
        this.mTrustManager = trustManager;
        this.mUserSwitcherController = userSwitcherController;
        this.mKeyguardDisplayManager = keyguardDisplayManager;
        dumpManager.registerDumpable(getClass().getName(), this);
        this.mDeviceConfig = deviceConfigProxy;
        this.mShowHomeOverLockscreen = deviceConfigProxy.getBoolean("systemui", "nav_bar_handle_show_over_lockscreen", true);
        DeviceConfigProxy deviceConfigProxy2 = this.mDeviceConfig;
        Handler handler = this.mHandler;
        Objects.requireNonNull(handler);
        deviceConfigProxy2.addOnPropertiesChangedListener("systemui", new MediaRoute2Provider$$ExternalSyntheticLambda0(handler), r4);
        this.mInGestureNavigationMode = QuickStepContract.isGesturalMode(navigationModeController.addListener(new NavigationModeController.ModeChangedListener() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda1
            @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
            public final void onNavigationModeChanged(int i) {
                KeyguardViewMediator.this.lambda$new$0(i);
            }
        }));
        this.mDozeParameters = dozeParameters;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        sysuiStatusBarStateController.addCallback(this);
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardUnlockAnimationControllerLazy = lazy2;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i) {
        this.mInGestureNavigationMode = QuickStepContract.isGesturalMode(i);
    }

    public void userActivity() {
        this.mPM.userActivity(SystemClock.uptimeMillis(), false);
    }

    boolean mustNotUnlockCurrentUser() {
        return UserManager.isSplitSystemUser() && KeyguardUpdateMonitor.getCurrentUser() == 0;
    }

    private void setupLocked() {
        PowerManager.WakeLock newWakeLock = this.mPM.newWakeLock(1, "show keyguard");
        this.mShowKeyguardWakeLock = newWakeLock;
        boolean z = false;
        newWakeLock.setReferenceCounted(false);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD");
        intentFilter2.addAction("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_LOCK");
        this.mContext.registerReceiver(this.mDelayedLockBroadcastReceiver, intentFilter2, "com.android.systemui.permission.SELF", null);
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        KeyguardUpdateMonitor.setCurrentUser(ActivityManager.getCurrentUser());
        if (isKeyguardServiceEnabled()) {
            if (!shouldWaitForProvisioning() && !this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser())) {
                z = true;
            }
            setShowingLocked(z, true);
        } else {
            setShowingLocked(false, true);
        }
        ContentResolver contentResolver = this.mContext.getContentResolver();
        this.mDeviceInteractive = this.mPM.isInteractive();
        this.mLockSounds = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(new AudioAttributes.Builder().setUsage(13).setContentType(4).build()).build();
        String string = Settings.Global.getString(contentResolver, "lock_sound");
        if (string != null) {
            this.mLockSoundId = this.mLockSounds.load(string, 1);
        }
        if (string == null || this.mLockSoundId == 0) {
            Log.w("KeyguardViewMediator", "failed to load lock sound from " + string);
        }
        String string2 = Settings.Global.getString(contentResolver, "unlock_sound");
        if (string2 != null) {
            this.mUnlockSoundId = this.mLockSounds.load(string2, 1);
        }
        if (string2 == null || this.mUnlockSoundId == 0) {
            Log.w("KeyguardViewMediator", "failed to load unlock sound from " + string2);
        }
        String string3 = Settings.Global.getString(contentResolver, "trusted_sound");
        if (string3 != null) {
            this.mTrustedSoundId = this.mLockSounds.load(string3, 1);
        }
        if (string3 == null || this.mTrustedSoundId == 0) {
            Log.w("KeyguardViewMediator", "failed to load trusted sound from " + string3);
        }
        this.mLockSoundVolume = (float) Math.pow(10.0d, (double) (((float) this.mContext.getResources().getInteger(17694836)) / 20.0f));
        this.mHideAnimation = AnimationUtils.loadAnimation(this.mContext, 17432683);
        this.mWorkLockController = new WorkLockActivityController(this.mContext);
    }

    @Override // com.android.systemui.SystemUI
    public void start() {
        synchronized (this) {
            setupLocked();
        }
    }

    public void onSystemReady() {
        this.mHandler.obtainMessage(18).sendToTarget();
    }

    /* access modifiers changed from: private */
    public void handleSystemReady() {
        synchronized (this) {
            this.mSystemReady = true;
            doKeyguardLocked(null);
            this.mUpdateMonitor.registerCallback(this.mUpdateCallback);
        }
        maybeSendUserPresentBroadcast();
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0046 A[Catch: all -> 0x0081, TryCatch #0 {, blocks: (B:4:0x0002, B:6:0x0015, B:11:0x0021, B:13:0x002f, B:15:0x0034, B:16:0x003b, B:18:0x0042, B:19:0x0046, B:21:0x004a, B:29:0x005b, B:30:0x0061, B:32:0x0069, B:33:0x006b, B:35:0x006f, B:36:0x0072), top: B:42:0x0002, inners: #1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x006f A[Catch: all -> 0x0081, TryCatch #0 {, blocks: (B:4:0x0002, B:6:0x0015, B:11:0x0021, B:13:0x002f, B:15:0x0034, B:16:0x003b, B:18:0x0042, B:19:0x0046, B:21:0x004a, B:29:0x005b, B:30:0x0061, B:32:0x0069, B:33:0x006b, B:35:0x006f, B:36:0x0072), top: B:42:0x0002, inners: #1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x002f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onStartedGoingToSleep(int r9) {
        /*
            r8 = this;
            monitor-enter(r8)
            r0 = 0
            r8.mDeviceInteractive = r0     // Catch: all -> 0x0081
            r8.mPowerGestureIntercepted = r0     // Catch: all -> 0x0081
            r1 = 1
            r8.mGoingToSleep = r1     // Catch: all -> 0x0081
            int r2 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()     // Catch: all -> 0x0081
            com.android.internal.widget.LockPatternUtils r3 = r8.mLockPatternUtils     // Catch: all -> 0x0081
            boolean r3 = r3.getPowerButtonInstantlyLocks(r2)     // Catch: all -> 0x0081
            if (r3 != 0) goto L_0x0020
            com.android.internal.widget.LockPatternUtils r3 = r8.mLockPatternUtils     // Catch: all -> 0x0081
            boolean r3 = r3.isSecure(r2)     // Catch: all -> 0x0081
            if (r3 != 0) goto L_0x001e
            goto L_0x0020
        L_0x001e:
            r3 = r0
            goto L_0x0021
        L_0x0020:
            r3 = r1
        L_0x0021:
            int r4 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()     // Catch: all -> 0x0081
            long r4 = r8.getLockTimeout(r4)     // Catch: all -> 0x0081
            r8.mLockLater = r0     // Catch: all -> 0x0081
            com.android.internal.policy.IKeyguardExitCallback r6 = r8.mExitSecureCallback     // Catch: all -> 0x0081
            if (r6 == 0) goto L_0x0046
            r6.onKeyguardExitResult(r0)     // Catch: RemoteException -> 0x0033, all -> 0x0081
            goto L_0x003b
        L_0x0033:
            r2 = move-exception
            java.lang.String r3 = "KeyguardViewMediator"
            java.lang.String r4 = "Failed to call onKeyguardExitResult(false)"
            android.util.Slog.w(r3, r4, r2)     // Catch: all -> 0x0081
        L_0x003b:
            r2 = 0
            r8.mExitSecureCallback = r2     // Catch: all -> 0x0081
            boolean r2 = r8.mExternallyEnabled     // Catch: all -> 0x0081
            if (r2 != 0) goto L_0x006b
            r8.hideLocked()     // Catch: all -> 0x0081
            goto L_0x006b
        L_0x0046:
            boolean r6 = r8.mShowing     // Catch: all -> 0x0081
            if (r6 == 0) goto L_0x004d
            r8.mPendingReset = r1     // Catch: all -> 0x0081
            goto L_0x006b
        L_0x004d:
            r6 = 3
            if (r9 != r6) goto L_0x0056
            r6 = 0
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 > 0) goto L_0x005b
        L_0x0056:
            r6 = 2
            if (r9 != r6) goto L_0x0061
            if (r3 != 0) goto L_0x0061
        L_0x005b:
            r8.doKeyguardLaterLocked(r4)     // Catch: all -> 0x0081
            r8.mLockLater = r1     // Catch: all -> 0x0081
            goto L_0x006b
        L_0x0061:
            com.android.internal.widget.LockPatternUtils r3 = r8.mLockPatternUtils     // Catch: all -> 0x0081
            boolean r2 = r3.isLockScreenDisabled(r2)     // Catch: all -> 0x0081
            if (r2 != 0) goto L_0x006b
            r8.mPendingLock = r1     // Catch: all -> 0x0081
        L_0x006b:
            boolean r2 = r8.mPendingLock     // Catch: all -> 0x0081
            if (r2 == 0) goto L_0x0072
            r8.playSounds(r1)     // Catch: all -> 0x0081
        L_0x0072:
            monitor-exit(r8)     // Catch: all -> 0x0081
            com.android.keyguard.KeyguardUpdateMonitor r1 = r8.mUpdateMonitor
            r1.dispatchStartedGoingToSleep(r9)
            com.android.keyguard.KeyguardUpdateMonitor r9 = r8.mUpdateMonitor
            r9.dispatchKeyguardGoingAway(r0)
            r8.notifyStartedGoingToSleep()
            return
        L_0x0081:
            r9 = move-exception
            monitor-exit(r8)     // Catch: all -> 0x0081
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardViewMediator.onStartedGoingToSleep(int):void");
    }

    public void onFinishedGoingToSleep(int i, boolean z) {
        synchronized (this) {
            this.mDeviceInteractive = false;
            this.mGoingToSleep = false;
            this.mWakeAndUnlocking = false;
            this.mAnimatingScreenOff = this.mDozeParameters.shouldControlUnlockedScreenOff();
            resetKeyguardDonePendingLocked();
            this.mHideAnimationRun = false;
            notifyFinishedGoingToSleep();
            if (z) {
                ((PowerManager) this.mContext.getSystemService(PowerManager.class)).wakeUp(SystemClock.uptimeMillis(), 5, "com.android.systemui:CAMERA_GESTURE_PREVENT_LOCK");
                this.mPendingLock = false;
                this.mPendingReset = false;
            }
            if (this.mPendingReset) {
                resetStateLocked();
                this.mPendingReset = false;
            }
            maybeHandlePendingLock();
            if (!this.mLockLater && !z) {
                doKeyguardForChildProfilesLocked();
            }
        }
        this.mUpdateMonitor.dispatchFinishedGoingToSleep(i);
    }

    public void maybeHandlePendingLock() {
        if (this.mPendingLock && !this.mUnlockedScreenOffAnimationController.isScreenOffAnimationPlaying()) {
            doKeyguardLocked(null);
            this.mPendingLock = false;
        }
    }

    private boolean isKeyguardServiceEnabled() {
        try {
            return this.mContext.getPackageManager().getServiceInfo(new ComponentName(this.mContext, KeyguardService.class), 0).isEnabled();
        } catch (PackageManager.NameNotFoundException unused) {
            return true;
        }
    }

    private long getLockTimeout(int i) {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        long j = (long) Settings.Secure.getInt(contentResolver, "lock_screen_lock_after_timeout", 5000);
        long maximumTimeToLock = this.mLockPatternUtils.getDevicePolicyManager().getMaximumTimeToLock(null, i);
        return maximumTimeToLock <= 0 ? j : Math.max(Math.min(maximumTimeToLock - Math.max((long) Settings.System.getInt(contentResolver, "screen_off_timeout", 30000), 0L), j), 0L);
    }

    private void doKeyguardLaterLocked() {
        long lockTimeout = getLockTimeout(KeyguardUpdateMonitor.getCurrentUser());
        if (lockTimeout == 0) {
            doKeyguardLocked(null);
        } else {
            doKeyguardLaterLocked(lockTimeout);
        }
    }

    private void doKeyguardLaterLocked(long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime() + j;
        Intent intent = new Intent("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_KEYGUARD");
        intent.putExtra("seq", this.mDelayedShowingSequence);
        intent.addFlags(268435456);
        this.mAlarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime, PendingIntent.getBroadcast(this.mContext, 0, intent, 335544320));
        doKeyguardLaterForChildProfilesLocked();
    }

    private void doKeyguardLaterForChildProfilesLocked() {
        int[] enabledProfileIds = UserManager.get(this.mContext).getEnabledProfileIds(UserHandle.myUserId());
        for (int i : enabledProfileIds) {
            if (this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i)) {
                long lockTimeout = getLockTimeout(i);
                if (lockTimeout == 0) {
                    doKeyguardForChildProfilesLocked();
                } else {
                    long elapsedRealtime = SystemClock.elapsedRealtime() + lockTimeout;
                    Intent intent = new Intent("com.android.internal.policy.impl.PhoneWindowManager.DELAYED_LOCK");
                    intent.putExtra("seq", this.mDelayedProfileShowingSequence);
                    intent.putExtra("android.intent.extra.USER_ID", i);
                    intent.addFlags(268435456);
                    this.mAlarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime, PendingIntent.getBroadcast(this.mContext, 0, intent, 301989888));
                }
            }
        }
    }

    private void doKeyguardForChildProfilesLocked() {
        int[] enabledProfileIds = UserManager.get(this.mContext).getEnabledProfileIds(UserHandle.myUserId());
        for (int i : enabledProfileIds) {
            if (this.mLockPatternUtils.isSeparateProfileChallengeEnabled(i)) {
                lockProfile(i);
            }
        }
    }

    private void cancelDoKeyguardLaterLocked() {
        this.mDelayedShowingSequence++;
    }

    private void cancelDoKeyguardForChildProfilesLocked() {
        this.mDelayedProfileShowingSequence++;
    }

    public void onStartedWakingUp(boolean z) {
        Trace.beginSection("KeyguardViewMediator#onStartedWakingUp");
        synchronized (this) {
            this.mDeviceInteractive = true;
            if (this.mPendingLock && !z) {
                doKeyguardLocked(null);
            }
            this.mAnimatingScreenOff = false;
            cancelDoKeyguardLaterLocked();
            cancelDoKeyguardForChildProfilesLocked();
            notifyStartedWakingUp();
        }
        this.mUpdateMonitor.dispatchStartedWakingUp();
        maybeSendUserPresentBroadcast();
        Trace.endSection();
    }

    public void onScreenTurningOn(IKeyguardDrawnCallback iKeyguardDrawnCallback) {
        Trace.beginSection("KeyguardViewMediator#onScreenTurningOn");
        notifyScreenOn(iKeyguardDrawnCallback);
        Trace.endSection();
    }

    public void onScreenTurnedOn() {
        Trace.beginSection("KeyguardViewMediator#onScreenTurnedOn");
        notifyScreenTurnedOn();
        this.mUpdateMonitor.dispatchScreenTurnedOn();
        Trace.endSection();
    }

    public void onScreenTurnedOff() {
        notifyScreenTurnedOff();
        this.mUpdateMonitor.dispatchScreenTurnedOff();
    }

    private void maybeSendUserPresentBroadcast() {
        if (this.mSystemReady && this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser())) {
            sendUserPresentBroadcast();
        } else if (this.mSystemReady && shouldWaitForProvisioning()) {
            getLockPatternUtils().userPresent(KeyguardUpdateMonitor.getCurrentUser());
        }
    }

    public void onDreamingStarted() {
        this.mUpdateMonitor.dispatchDreamingStarted();
        synchronized (this) {
            if (this.mDeviceInteractive && this.mLockPatternUtils.isSecure(KeyguardUpdateMonitor.getCurrentUser())) {
                doKeyguardLaterLocked();
            }
        }
    }

    public void onDreamingStopped() {
        this.mUpdateMonitor.dispatchDreamingStopped();
        synchronized (this) {
            if (this.mDeviceInteractive) {
                cancelDoKeyguardLaterLocked();
            }
        }
    }

    public void setKeyguardEnabled(boolean z) {
        synchronized (this) {
            this.mExternallyEnabled = z;
            if (z || !this.mShowing) {
                if (z && this.mNeedToReshowWhenReenabled) {
                    this.mNeedToReshowWhenReenabled = false;
                    updateInputRestrictedLocked();
                    IKeyguardExitCallback iKeyguardExitCallback = this.mExitSecureCallback;
                    if (iKeyguardExitCallback != null) {
                        try {
                            iKeyguardExitCallback.onKeyguardExitResult(false);
                        } catch (RemoteException e) {
                            Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e);
                        }
                        this.mExitSecureCallback = null;
                        resetStateLocked();
                    } else {
                        showLocked(null);
                        this.mWaitingUntilKeyguardVisible = true;
                        this.mHandler.sendEmptyMessageDelayed(8, 2000);
                        while (this.mWaitingUntilKeyguardVisible) {
                            try {
                                wait();
                            } catch (InterruptedException unused) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                }
            } else if (this.mExitSecureCallback == null) {
                this.mNeedToReshowWhenReenabled = true;
                updateInputRestrictedLocked();
                hideLocked();
            }
        }
    }

    public void verifyUnlock(IKeyguardExitCallback iKeyguardExitCallback) {
        Trace.beginSection("KeyguardViewMediator#verifyUnlock");
        synchronized (this) {
            if (shouldWaitForProvisioning()) {
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e);
                }
            } else if (this.mExternallyEnabled) {
                Log.w("KeyguardViewMediator", "verifyUnlock called when not externally disabled");
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e2) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e2);
                }
            } else if (this.mExitSecureCallback != null) {
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e3) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e3);
                }
            } else if (!isSecure()) {
                this.mExternallyEnabled = true;
                this.mNeedToReshowWhenReenabled = false;
                updateInputRestricted();
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(true);
                } catch (RemoteException e4) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e4);
                }
            } else {
                try {
                    iKeyguardExitCallback.onKeyguardExitResult(false);
                } catch (RemoteException e5) {
                    Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult(false)", e5);
                }
            }
        }
        Trace.endSection();
    }

    public boolean isShowingAndNotOccluded() {
        return this.mShowing && !this.mOccluded;
    }

    public void setOccluded(boolean z, boolean z2) {
        Trace.beginSection("KeyguardViewMediator#setOccluded");
        this.mHandler.removeMessages(9);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(9, z ? 1 : 0, z2 ? 1 : 0));
        Trace.endSection();
    }

    public boolean isHiding() {
        return this.mHiding;
    }

    /* access modifiers changed from: private */
    public void handleSetOccluded(boolean z, boolean z2) {
        Trace.beginSection("KeyguardViewMediator#handleSetOccluded");
        synchronized (this) {
            if (this.mHiding && z) {
                startKeyguardExitAnimation(0, 0);
            }
            if (this.mOccluded != z) {
                this.mOccluded = z;
                this.mUpdateMonitor.setKeyguardOccluded(z);
                this.mKeyguardViewControllerLazy.get().setOccluded(z, z2 && this.mDeviceInteractive);
                adjustStatusBarLocked();
            }
        }
        Trace.endSection();
    }

    public void doKeyguardTimeout(Bundle bundle) {
        this.mHandler.removeMessages(10);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(10, bundle));
    }

    public boolean isInputRestricted() {
        return this.mShowing || this.mNeedToReshowWhenReenabled;
    }

    private void updateInputRestricted() {
        synchronized (this) {
            updateInputRestrictedLocked();
        }
    }

    private void updateInputRestrictedLocked() {
        boolean isInputRestricted = isInputRestricted();
        if (this.mInputRestricted != isInputRestricted) {
            this.mInputRestricted = isInputRestricted;
            for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
                IKeyguardStateCallback iKeyguardStateCallback = this.mKeyguardStateCallbacks.get(size);
                try {
                    iKeyguardStateCallback.onInputRestrictedStateChanged(isInputRestricted);
                } catch (RemoteException e) {
                    Slog.w("KeyguardViewMediator", "Failed to call onDeviceProvisioned", e);
                    if (e instanceof DeadObjectException) {
                        this.mKeyguardStateCallbacks.remove(iKeyguardStateCallback);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void doKeyguardLocked(Bundle bundle) {
        if (!KeyguardUpdateMonitor.CORE_APPS_ONLY) {
            boolean z = true;
            if (!this.mExternallyEnabled) {
                this.mNeedToReshowWhenReenabled = true;
            } else if (!this.mShowing || !this.mKeyguardViewControllerLazy.get().isShowing()) {
                if (!mustNotUnlockCurrentUser() || !this.mUpdateMonitor.isDeviceProvisioned()) {
                    boolean z2 = this.mUpdateMonitor.isSimPinSecure() || ((SubscriptionManager.isValidSubscriptionId(this.mUpdateMonitor.getNextSubIdForState(1)) || SubscriptionManager.isValidSubscriptionId(this.mUpdateMonitor.getNextSubIdForState(7))) && (SystemProperties.getBoolean("keyguard.no_require_sim", false) ^ true));
                    if (z2 || !shouldWaitForProvisioning()) {
                        if (bundle == null || !bundle.getBoolean("force_show", false)) {
                            z = false;
                        }
                        if (this.mLockPatternUtils.isLockScreenDisabled(KeyguardUpdateMonitor.getCurrentUser()) && !z2 && !z) {
                            return;
                        }
                        if (this.mLockPatternUtils.checkVoldPassword(KeyguardUpdateMonitor.getCurrentUser())) {
                            setShowingLocked(false);
                            hideLocked();
                            return;
                        }
                    } else {
                        return;
                    }
                }
                showLocked(bundle);
            } else {
                resetStateLocked();
            }
        }
    }

    /* access modifiers changed from: private */
    public void lockProfile(int i) {
        this.mTrustManager.setDeviceLockedForUser(i, true);
    }

    /* access modifiers changed from: private */
    public boolean shouldWaitForProvisioning() {
        return !this.mUpdateMonitor.isDeviceProvisioned() && !isSecure();
    }

    /* access modifiers changed from: private */
    public void handleDismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
        if (this.mShowing) {
            if (iKeyguardDismissCallback != null) {
                this.mDismissCallbackRegistry.addCallback(iKeyguardDismissCallback);
            }
            this.mCustomMessage = charSequence;
            this.mKeyguardViewControllerLazy.get().dismissAndCollapse();
        } else if (iKeyguardDismissCallback != null) {
            new DismissCallbackWrapper(iKeyguardDismissCallback).notifyDismissError();
        }
    }

    public void dismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
        this.mHandler.obtainMessage(11, new DismissMessage(iKeyguardDismissCallback, charSequence)).sendToTarget();
    }

    /* access modifiers changed from: private */
    public void resetStateLocked() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3));
    }

    private void notifyStartedGoingToSleep() {
        this.mHandler.sendEmptyMessage(17);
    }

    private void notifyFinishedGoingToSleep() {
        this.mHandler.sendEmptyMessage(5);
    }

    private void notifyStartedWakingUp() {
        this.mHandler.sendEmptyMessage(14);
    }

    private void notifyScreenOn(IKeyguardDrawnCallback iKeyguardDrawnCallback) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(6, iKeyguardDrawnCallback));
    }

    private void notifyScreenTurnedOn() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(15));
    }

    private void notifyScreenTurnedOff() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(16));
    }

    private void showLocked(Bundle bundle) {
        Trace.beginSection("KeyguardViewMediator#showLocked aqcuiring mShowKeyguardWakeLock");
        this.mShowKeyguardWakeLock.acquire();
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, bundle));
        Trace.endSection();
    }

    private void hideLocked() {
        Trace.beginSection("KeyguardViewMediator#hideLocked");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(2));
        Trace.endSection();
    }

    public void hideWithAnimation(IRemoteAnimationRunner iRemoteAnimationRunner) {
        if (this.mKeyguardDonePending) {
            this.mKeyguardExitAnimationRunner = iRemoteAnimationRunner;
            this.mViewMediatorCallback.readyForKeyguardDone();
        }
    }

    public void setBlursDisabledForAppLaunch(boolean z) {
        this.mNotificationShadeDepthController.get().setBlursDisabledForAppLaunch(z);
    }

    public boolean isSecure() {
        return isSecure(KeyguardUpdateMonitor.getCurrentUser());
    }

    public boolean isSecure(int i) {
        return this.mLockPatternUtils.isSecure(i) || this.mUpdateMonitor.isSimPinSecure();
    }

    public void setSwitchingUser(boolean z) {
        this.mUpdateMonitor.setSwitchingUser(z);
    }

    public void setCurrentUser(int i) {
        KeyguardUpdateMonitor.setCurrentUser(i);
        synchronized (this) {
            notifyTrustedChangedLocked(this.mUpdateMonitor.getUserHasTrust(i));
        }
    }

    public void keyguardDone() {
        Trace.beginSection("KeyguardViewMediator#keyguardDone");
        userActivity();
        EventLog.writeEvent(70000, 2);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(7));
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public void tryKeyguardDone() {
        if (!this.mKeyguardDonePending && this.mHideAnimationRun && !this.mHideAnimationRunning) {
            handleKeyguardDone();
        } else if (!this.mHideAnimationRun) {
            this.mHideAnimationRun = true;
            this.mHideAnimationRunning = true;
            this.mKeyguardViewControllerLazy.get().startPreHideAnimation(this.mHideAnimationFinishedRunnable);
        }
    }

    /* access modifiers changed from: private */
    public void handleKeyguardDone() {
        Trace.beginSection("KeyguardViewMediator#handleKeyguardDone");
        this.mUiBgExecutor.execute(new Runnable(KeyguardUpdateMonitor.getCurrentUser()) { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda4
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                KeyguardViewMediator.this.lambda$handleKeyguardDone$1(this.f$1);
            }
        });
        synchronized (this) {
            resetKeyguardDonePendingLocked();
        }
        if (this.mGoingToSleep) {
            this.mUpdateMonitor.clearBiometricRecognized();
            Log.i("KeyguardViewMediator", "Device is going to sleep, aborting keyguardDone");
            return;
        }
        IKeyguardExitCallback iKeyguardExitCallback = this.mExitSecureCallback;
        if (iKeyguardExitCallback != null) {
            try {
                iKeyguardExitCallback.onKeyguardExitResult(true);
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call onKeyguardExitResult()", e);
            }
            this.mExitSecureCallback = null;
            this.mExternallyEnabled = true;
            this.mNeedToReshowWhenReenabled = false;
            updateInputRestricted();
        }
        handleHide();
        this.mUpdateMonitor.clearBiometricRecognized();
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleKeyguardDone$1(int i) {
        if (this.mLockPatternUtils.isSecure(i)) {
            this.mLockPatternUtils.getDevicePolicyManager().reportKeyguardDismissed(i);
        }
    }

    /* access modifiers changed from: private */
    public void sendUserPresentBroadcast() {
        synchronized (this) {
            if (this.mBootCompleted) {
                int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                this.mUiBgExecutor.execute(new Runnable((UserManager) this.mContext.getSystemService("user"), new UserHandle(currentUser), currentUser) { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda6
                    public final /* synthetic */ UserManager f$1;
                    public final /* synthetic */ UserHandle f$2;
                    public final /* synthetic */ int f$3;

                    {
                        this.f$1 = r2;
                        this.f$2 = r3;
                        this.f$3 = r4;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        KeyguardViewMediator.this.lambda$sendUserPresentBroadcast$2(this.f$1, this.f$2, this.f$3);
                    }
                });
            } else {
                this.mBootSendUserPresent = true;
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$sendUserPresentBroadcast$2(UserManager userManager, UserHandle userHandle, int i) {
        for (int i2 : userManager.getProfileIdsWithDisabled(userHandle.getIdentifier())) {
            this.mContext.sendBroadcastAsUser(USER_PRESENT_INTENT, UserHandle.of(i2));
        }
        getLockPatternUtils().userPresent(i);
    }

    /* access modifiers changed from: private */
    public void handleKeyguardDoneDrawing() {
        Trace.beginSection("KeyguardViewMediator#handleKeyguardDoneDrawing");
        synchronized (this) {
            if (this.mWaitingUntilKeyguardVisible) {
                this.mWaitingUntilKeyguardVisible = false;
                notifyAll();
                this.mHandler.removeMessages(8);
            }
        }
        Trace.endSection();
    }

    private void playSounds(boolean z) {
        playSound(z ? this.mLockSoundId : this.mUnlockSoundId);
    }

    private void playSound(int i) {
        if (i != 0 && Settings.System.getInt(this.mContext.getContentResolver(), "lockscreen_sounds_enabled", 1) == 1) {
            this.mLockSounds.stop(this.mLockSoundStreamId);
            if (this.mAudioManager == null) {
                AudioManager audioManager = (AudioManager) this.mContext.getSystemService("audio");
                this.mAudioManager = audioManager;
                if (audioManager != null) {
                    this.mUiSoundsStreamType = audioManager.getUiSoundsStreamType();
                } else {
                    return;
                }
            }
            this.mUiBgExecutor.execute(new Runnable(i) { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda5
                public final /* synthetic */ int f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardViewMediator.this.lambda$playSound$3(this.f$1);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$playSound$3(int i) {
        if (!this.mAudioManager.isStreamMute(this.mUiSoundsStreamType)) {
            SoundPool soundPool = this.mLockSounds;
            float f = this.mLockSoundVolume;
            int play = soundPool.play(i, f, f, 1, 0, 1.0f);
            synchronized (this) {
                this.mLockSoundStreamId = play;
            }
        }
    }

    /* access modifiers changed from: private */
    public void playTrustedSound() {
        playSound(this.mTrustedSoundId);
    }

    private void updateActivityLockScreenState(boolean z, boolean z2) {
        this.mUiBgExecutor.execute(new Runnable(z, z2) { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda9
            public final /* synthetic */ boolean f$0;
            public final /* synthetic */ boolean f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                KeyguardViewMediator.lambda$updateActivityLockScreenState$4(this.f$0, this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateActivityLockScreenState$4(boolean z, boolean z2) {
        try {
            ActivityTaskManager.getService().setLockScreenShown(z, z2);
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void handleShow(Bundle bundle) {
        Trace.beginSection("KeyguardViewMediator#handleShow");
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        if (this.mLockPatternUtils.isSecure(currentUser)) {
            this.mLockPatternUtils.getDevicePolicyManager().reportKeyguardSecured(currentUser);
        }
        synchronized (this) {
            if (this.mSystemReady) {
                this.mHiding = false;
                this.mKeyguardExitAnimationRunner = null;
                this.mWakeAndUnlocking = false;
                this.mPendingLock = false;
                setShowingLocked(true);
                this.mKeyguardViewControllerLazy.get().show(bundle);
                resetKeyguardDonePendingLocked();
                this.mHideAnimationRun = false;
                adjustStatusBarLocked();
                userActivity();
                this.mUpdateMonitor.setKeyguardGoingAway(false);
                this.mKeyguardViewControllerLazy.get().setKeyguardGoingAwayState(false);
                this.mShowKeyguardWakeLock.release();
                this.mKeyguardDisplayManager.show();
                this.mLockPatternUtils.scheduleNonStrongBiometricIdleTimeout(KeyguardUpdateMonitor.getCurrentUser());
                Trace.endSection();
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5() {
        Log.e("KeyguardViewMediator", "mHideAnimationFinishedRunnable#run");
        this.mHideAnimationRunning = false;
        tryKeyguardDone();
    }

    /* access modifiers changed from: private */
    public void handleHide() {
        Trace.beginSection("KeyguardViewMediator#handleHide");
        if (this.mAodShowing) {
            ((PowerManager) this.mContext.getSystemService(PowerManager.class)).wakeUp(SystemClock.uptimeMillis(), 4, "com.android.systemui:BOUNCER_DOZING");
        }
        synchronized (this) {
            if (mustNotUnlockCurrentUser()) {
                this.mKeyguardExitAnimationRunner = null;
                return;
            }
            this.mHiding = true;
            if (!this.mShowing || this.mOccluded) {
                handleStartKeyguardExitAnimation(SystemClock.uptimeMillis() + this.mHideAnimation.getStartOffset(), this.mHideAnimation.getDuration(), null, null, null, null);
            } else {
                this.mKeyguardGoingAwayRunnable.run();
            }
            Trace.endSection();
        }
    }

    /* access modifiers changed from: private */
    public void handleStartKeyguardExitAnimation(long j, long j2, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        Trace.beginSection("KeyguardViewMediator#handleStartKeyguardExitAnimation");
        synchronized (this) {
            if (this.mHiding || this.mSurfaceBehindRemoteAnimationRequested || this.mKeyguardStateController.isFlingingToDismissKeyguardDuringSwipeGesture()) {
                this.mHiding = false;
                IRemoteAnimationRunner iRemoteAnimationRunner = this.mKeyguardExitAnimationRunner;
                this.mKeyguardExitAnimationRunner = null;
                if (this.mWakeAndUnlocking && this.mDrawnCallback != null) {
                    this.mKeyguardViewControllerLazy.get().getViewRootImpl().setReportNextDraw();
                    notifyDrawn(this.mDrawnCallback);
                    this.mDrawnCallback = null;
                }
                LatencyTracker.getInstance(this.mContext).onActionEnd(11);
                boolean z = KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation;
                if (z && iRemoteAnimationRunner != null && iRemoteAnimationFinishedCallback != null) {
                    AnonymousClass8 r8 = new IRemoteAnimationFinishedCallback() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.8
                        public void onAnimationFinished() throws RemoteException {
                            try {
                                iRemoteAnimationFinishedCallback.onAnimationFinished();
                            } catch (RemoteException e) {
                                Slog.w("KeyguardViewMediator", "Failed to call onAnimationFinished", e);
                            }
                            KeyguardViewMediator.this.onKeyguardExitFinished();
                            ((KeyguardViewController) KeyguardViewMediator.this.mKeyguardViewControllerLazy.get()).hide(0, 0);
                            InteractionJankMonitor.getInstance().end(29);
                        }

                        public IBinder asBinder() {
                            return iRemoteAnimationFinishedCallback.asBinder();
                        }
                    };
                    try {
                        InteractionJankMonitor.getInstance().begin(createInteractionJankMonitorConf("RunRemoteAnimation"));
                        iRemoteAnimationRunner.onAnimationStart(7, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, r8);
                    } catch (RemoteException e) {
                        Slog.w("KeyguardViewMediator", "Failed to call onAnimationStart", e);
                    }
                } else if (!z || this.mStatusBarStateController.leaveOpenOnKeyguardHide() || remoteAnimationTargetArr == null || remoteAnimationTargetArr.length <= 0) {
                    InteractionJankMonitor.getInstance().begin(createInteractionJankMonitorConf("RemoteAnimationDisabled"));
                    this.mKeyguardViewControllerLazy.get().hide(j, j2);
                    this.mContext.getMainExecutor().execute(new Runnable(iRemoteAnimationFinishedCallback, remoteAnimationTargetArr) { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda7
                        public final /* synthetic */ IRemoteAnimationFinishedCallback f$1;
                        public final /* synthetic */ RemoteAnimationTarget[] f$2;

                        {
                            this.f$1 = r2;
                            this.f$2 = r3;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            KeyguardViewMediator.this.lambda$handleStartKeyguardExitAnimation$7(this.f$1, this.f$2);
                        }
                    });
                    onKeyguardExitFinished();
                } else {
                    this.mSurfaceBehindRemoteAnimationFinishedCallback = iRemoteAnimationFinishedCallback;
                    this.mSurfaceBehindRemoteAnimationRunning = true;
                    InteractionJankMonitor.getInstance().begin(createInteractionJankMonitorConf("DismissPanel"));
                    this.mKeyguardUnlockAnimationControllerLazy.get().notifyStartKeyguardExitAnimation(remoteAnimationTargetArr[0], j, this.mSurfaceBehindRemoteAnimationRequested);
                }
                Trace.endSection();
                return;
            }
            if (iRemoteAnimationFinishedCallback != null) {
                try {
                    iRemoteAnimationFinishedCallback.onAnimationFinished();
                } catch (RemoteException e2) {
                    Slog.w("KeyguardViewMediator", "Failed to call onAnimationFinished", e2);
                }
            }
            setShowingLocked(this.mShowing, true);
            return;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleStartKeyguardExitAnimation$7(final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback, RemoteAnimationTarget[] remoteAnimationTargetArr) {
        if (iRemoteAnimationFinishedCallback == null) {
            InteractionJankMonitor.getInstance().end(29);
            return;
        }
        SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier = new SyncRtSurfaceTransactionApplier(this.mKeyguardViewControllerLazy.get().getViewRootImpl().getView());
        RemoteAnimationTarget remoteAnimationTarget = remoteAnimationTargetArr[0];
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(400L);
        ofFloat.setInterpolator(Interpolators.LINEAR);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(remoteAnimationTarget, syncRtSurfaceTransactionApplier) { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda0
            public final /* synthetic */ RemoteAnimationTarget f$0;
            public final /* synthetic */ SyncRtSurfaceTransactionApplier f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                KeyguardViewMediator.lambda$handleStartKeyguardExitAnimation$6(this.f$0, this.f$1, valueAnimator);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.9
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                try {
                    try {
                        iRemoteAnimationFinishedCallback.onAnimationFinished();
                    } catch (RemoteException unused) {
                        Slog.e("KeyguardViewMediator", "RemoteException");
                    }
                } finally {
                    InteractionJankMonitor.getInstance().end(29);
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                try {
                    try {
                        iRemoteAnimationFinishedCallback.onAnimationFinished();
                    } catch (RemoteException unused) {
                        Slog.e("KeyguardViewMediator", "RemoteException");
                    }
                } finally {
                    InteractionJankMonitor.getInstance().cancel(29);
                }
            }
        });
        ofFloat.start();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleStartKeyguardExitAnimation$6(RemoteAnimationTarget remoteAnimationTarget, SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier, ValueAnimator valueAnimator) {
        syncRtSurfaceTransactionApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget.leash).withAlpha(valueAnimator.getAnimatedFraction()).build()});
    }

    /* access modifiers changed from: private */
    public void onKeyguardExitFinished() {
        if (TelephonyManager.EXTRA_STATE_IDLE.equals(this.mPhoneState)) {
            playSounds(false);
        }
        setShowingLocked(false);
        this.mWakeAndUnlocking = false;
        this.mDismissCallbackRegistry.notifyDismissSucceeded();
        resetKeyguardDonePendingLocked();
        this.mHideAnimationRun = false;
        adjustStatusBarLocked();
        sendUserPresentBroadcast();
    }

    private InteractionJankMonitor.Configuration.Builder createInteractionJankMonitorConf(String str) {
        return new InteractionJankMonitor.Configuration.Builder(29).setView(this.mKeyguardViewControllerLazy.get().getViewRootImpl().getView()).setTag(str);
    }

    public boolean isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe() {
        return this.mSurfaceBehindRemoteAnimationRunning || this.mKeyguardStateController.isFlingingToDismissKeyguard();
    }

    /* access modifiers changed from: private */
    public void handleCancelKeyguardExitAnimation() {
        showSurfaceBehindKeyguard();
        onKeyguardExitRemoteAnimationFinished(true);
    }

    public void onKeyguardExitRemoteAnimationFinished(boolean z) {
        if (this.mSurfaceBehindRemoteAnimationRunning || this.mSurfaceBehindRemoteAnimationRequested) {
            this.mKeyguardViewControllerLazy.get().blockPanelExpansionFromCurrentTouch();
            boolean z2 = this.mShowing;
            onKeyguardExitFinished();
            if (this.mKeyguardStateController.isDismissingFromSwipe() || !z2) {
                this.mKeyguardUnlockAnimationControllerLazy.get().hideKeyguardViewAfterRemoteAnimation();
            }
            finishSurfaceBehindRemoteAnimation(z);
            this.mSurfaceBehindRemoteAnimationRequested = false;
            this.mKeyguardUnlockAnimationControllerLazy.get().notifyFinishedKeyguardExitAnimation();
            InteractionJankMonitor.getInstance().end(29);
        }
    }

    public void showSurfaceBehindKeyguard() {
        this.mSurfaceBehindRemoteAnimationRequested = true;
        try {
            ActivityTaskManager.getService().keyguardGoingAway(6);
        } catch (RemoteException e) {
            this.mSurfaceBehindRemoteAnimationRequested = false;
            e.printStackTrace();
        }
    }

    public void hideSurfaceBehindKeyguard() {
        this.mSurfaceBehindRemoteAnimationRequested = false;
        if (this.mShowing) {
            setShowingLocked(true, true);
        }
    }

    public boolean requestedShowSurfaceBehindKeyguard() {
        return this.mSurfaceBehindRemoteAnimationRequested;
    }

    public void finishSurfaceBehindRemoteAnimation(boolean z) {
        this.mSurfaceBehindRemoteAnimationRunning = false;
        IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback = this.mSurfaceBehindRemoteAnimationFinishedCallback;
        if (iRemoteAnimationFinishedCallback != null) {
            if (!z) {
                try {
                    iRemoteAnimationFinishedCallback.onAnimationFinished();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
            }
            this.mSurfaceBehindRemoteAnimationFinishedCallback = null;
        }
    }

    /* access modifiers changed from: private */
    public void adjustStatusBarLocked() {
        adjustStatusBarLocked(false, false);
    }

    /* access modifiers changed from: private */
    public void adjustStatusBarLocked(boolean z, boolean z2) {
        if (this.mStatusBarManager == null) {
            this.mStatusBarManager = (StatusBarManager) this.mContext.getSystemService("statusbar");
        }
        StatusBarManager statusBarManager = this.mStatusBarManager;
        if (statusBarManager == null) {
            Log.w("KeyguardViewMediator", "Could not get status bar manager");
            return;
        }
        int i = 0;
        if (z2) {
            statusBarManager.disable(0);
        }
        if (z || isShowingAndNotOccluded()) {
            if (!this.mShowHomeOverLockscreen || !this.mInGestureNavigationMode) {
                i = 2097152;
            }
            i |= 16777216;
        }
        this.mStatusBarManager.disable(i);
    }

    /* access modifiers changed from: private */
    public void handleReset() {
        synchronized (this) {
            this.mKeyguardViewControllerLazy.get().reset(true);
        }
    }

    /* access modifiers changed from: private */
    public void handleVerifyUnlock() {
        Trace.beginSection("KeyguardViewMediator#handleVerifyUnlock");
        synchronized (this) {
            setShowingLocked(true);
            this.mKeyguardViewControllerLazy.get().dismissAndCollapse();
        }
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public void handleNotifyStartedGoingToSleep() {
        synchronized (this) {
            this.mKeyguardViewControllerLazy.get().onStartedGoingToSleep();
        }
    }

    /* access modifiers changed from: private */
    public void handleNotifyFinishedGoingToSleep() {
        synchronized (this) {
            this.mKeyguardViewControllerLazy.get().onFinishedGoingToSleep();
        }
    }

    /* access modifiers changed from: private */
    public void handleNotifyStartedWakingUp() {
        Trace.beginSection("KeyguardViewMediator#handleMotifyStartedWakingUp");
        synchronized (this) {
            this.mKeyguardViewControllerLazy.get().onStartedWakingUp();
        }
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public void handleNotifyScreenTurningOn(IKeyguardDrawnCallback iKeyguardDrawnCallback) {
        Trace.beginSection("KeyguardViewMediator#handleNotifyScreenTurningOn");
        synchronized (this) {
            this.mKeyguardViewControllerLazy.get().onScreenTurningOn();
            if (iKeyguardDrawnCallback != null) {
                if (this.mWakeAndUnlocking) {
                    this.mDrawnCallback = iKeyguardDrawnCallback;
                } else {
                    notifyDrawn(iKeyguardDrawnCallback);
                }
            }
        }
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public void handleNotifyScreenTurnedOn() {
        Trace.beginSection("KeyguardViewMediator#handleNotifyScreenTurnedOn");
        if (LatencyTracker.isEnabled(this.mContext)) {
            LatencyTracker.getInstance(this.mContext).onActionEnd(5);
        }
        synchronized (this) {
            this.mKeyguardViewControllerLazy.get().onScreenTurnedOn();
        }
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public void handleNotifyScreenTurnedOff() {
        synchronized (this) {
            this.mDrawnCallback = null;
        }
    }

    private void notifyDrawn(IKeyguardDrawnCallback iKeyguardDrawnCallback) {
        Trace.beginSection("KeyguardViewMediator#notifyDrawn");
        try {
            iKeyguardDrawnCallback.onDrawn();
        } catch (RemoteException e) {
            Slog.w("KeyguardViewMediator", "Exception calling onDrawn():", e);
        }
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public void resetKeyguardDonePendingLocked() {
        this.mKeyguardDonePending = false;
        this.mHandler.removeMessages(13);
    }

    @Override // com.android.systemui.SystemUI
    public void onBootCompleted() {
        synchronized (this) {
            if (this.mContext.getResources().getBoolean(17891567)) {
                this.mUserSwitcherController.schedulePostBootGuestCreation();
            }
            this.mBootCompleted = true;
            adjustStatusBarLocked(false, true);
            if (this.mBootSendUserPresent) {
                sendUserPresentBroadcast();
            }
        }
    }

    public void onWakeAndUnlocking() {
        Trace.beginSection("KeyguardViewMediator#onWakeAndUnlocking");
        this.mWakeAndUnlocking = true;
        keyguardDone();
        Trace.endSection();
    }

    @Deprecated
    public void startKeyguardExitAnimation(long j, long j2) {
        startKeyguardExitAnimation(0, j, j2, null, null, null, null);
    }

    public void startKeyguardExitAnimation(int i, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        startKeyguardExitAnimation(i, 0, 0, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
    }

    private void startKeyguardExitAnimation(int i, long j, long j2, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
        Trace.beginSection("KeyguardViewMediator#startKeyguardExitAnimation");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(12, new StartKeyguardExitAnimParams(i, j, j2, remoteAnimationTargetArr, remoteAnimationTargetArr2, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback)));
        Trace.endSection();
    }

    public void cancelKeyguardExitAnimation() {
        Trace.beginSection("KeyguardViewMediator#cancelKeyguardExitAnimation");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(19));
        Trace.endSection();
    }

    public ViewMediatorCallback getViewMediatorCallback() {
        return this.mViewMediatorCallback;
    }

    public LockPatternUtils getLockPatternUtils() {
        return this.mLockPatternUtils;
    }

    @Override // com.android.systemui.SystemUI, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print("  mSystemReady: ");
        printWriter.println(this.mSystemReady);
        printWriter.print("  mBootCompleted: ");
        printWriter.println(this.mBootCompleted);
        printWriter.print("  mBootSendUserPresent: ");
        printWriter.println(this.mBootSendUserPresent);
        printWriter.print("  mExternallyEnabled: ");
        printWriter.println(this.mExternallyEnabled);
        printWriter.print("  mShuttingDown: ");
        printWriter.println(this.mShuttingDown);
        printWriter.print("  mNeedToReshowWhenReenabled: ");
        printWriter.println(this.mNeedToReshowWhenReenabled);
        printWriter.print("  mShowing: ");
        printWriter.println(this.mShowing);
        printWriter.print("  mInputRestricted: ");
        printWriter.println(this.mInputRestricted);
        printWriter.print("  mOccluded: ");
        printWriter.println(this.mOccluded);
        printWriter.print("  mDelayedShowingSequence: ");
        printWriter.println(this.mDelayedShowingSequence);
        printWriter.print("  mExitSecureCallback: ");
        printWriter.println(this.mExitSecureCallback);
        printWriter.print("  mDeviceInteractive: ");
        printWriter.println(this.mDeviceInteractive);
        printWriter.print("  mGoingToSleep: ");
        printWriter.println(this.mGoingToSleep);
        printWriter.print("  mHiding: ");
        printWriter.println(this.mHiding);
        printWriter.print("  mDozing: ");
        printWriter.println(this.mDozing);
        printWriter.print("  mAodShowing: ");
        printWriter.println(this.mAodShowing);
        printWriter.print("  mWaitingUntilKeyguardVisible: ");
        printWriter.println(this.mWaitingUntilKeyguardVisible);
        printWriter.print("  mKeyguardDonePending: ");
        printWriter.println(this.mKeyguardDonePending);
        printWriter.print("  mHideAnimationRun: ");
        printWriter.println(this.mHideAnimationRun);
        printWriter.print("  mPendingReset: ");
        printWriter.println(this.mPendingReset);
        printWriter.print("  mPendingLock: ");
        printWriter.println(this.mPendingLock);
        printWriter.print("  mWakeAndUnlocking: ");
        printWriter.println(this.mWakeAndUnlocking);
        printWriter.print("  mDrawnCallback: ");
        printWriter.println(this.mDrawnCallback);
    }

    public void setDozing(boolean z) {
        if (z != this.mDozing) {
            this.mDozing = z;
            if (!z) {
                this.mAnimatingScreenOff = false;
            }
            if (this.mShowing || !this.mPendingLock || !this.mDozeParameters.canControlUnlockedScreenOff()) {
                setShowingLocked(this.mShowing);
            }
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozeAmountChanged(float f, float f2) {
        if (this.mAnimatingScreenOff && this.mDozing && f == 1.0f) {
            this.mAnimatingScreenOff = false;
            setShowingLocked(this.mShowing, true);
        }
    }

    public void setPulsing(boolean z) {
        this.mPulsing = z;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class StartKeyguardExitAnimParams {
        long fadeoutDuration;
        RemoteAnimationTarget[] mApps;
        IRemoteAnimationFinishedCallback mFinishedCallback;
        RemoteAnimationTarget[] mNonApps;
        int mTransit;
        RemoteAnimationTarget[] mWallpapers;
        long startTime;

        private StartKeyguardExitAnimParams(int i, long j, long j2, RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, RemoteAnimationTarget[] remoteAnimationTargetArr3, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            this.mTransit = i;
            this.startTime = j;
            this.fadeoutDuration = j2;
            this.mApps = remoteAnimationTargetArr;
            this.mWallpapers = remoteAnimationTargetArr2;
            this.mNonApps = remoteAnimationTargetArr3;
            this.mFinishedCallback = iRemoteAnimationFinishedCallback;
        }
    }

    void setShowingLocked(boolean z) {
        setShowingLocked(z, false);
    }

    private void setShowingLocked(boolean z, boolean z2) {
        boolean z3 = true;
        boolean z4 = this.mDozing && !this.mWakeAndUnlocking;
        if (z == this.mShowing && z4 == this.mAodShowing && !z2) {
            z3 = false;
        }
        this.mShowing = z;
        this.mAodShowing = z4;
        if (z3) {
            notifyDefaultDisplayCallbacks(z);
            updateActivityLockScreenState(z, z4);
        }
    }

    private void notifyDefaultDisplayCallbacks(boolean z) {
        DejankUtils.whitelistIpcs(new Runnable(z) { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda8
            public final /* synthetic */ boolean f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                KeyguardViewMediator.this.lambda$notifyDefaultDisplayCallbacks$8(this.f$1);
            }
        });
        updateInputRestrictedLocked();
        this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardViewMediator.this.lambda$notifyDefaultDisplayCallbacks$9();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyDefaultDisplayCallbacks$8(boolean z) {
        for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
            IKeyguardStateCallback iKeyguardStateCallback = this.mKeyguardStateCallbacks.get(size);
            try {
                iKeyguardStateCallback.onShowingStateChanged(z);
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call onShowingStateChanged", e);
                if (e instanceof DeadObjectException) {
                    this.mKeyguardStateCallbacks.remove(iKeyguardStateCallback);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyDefaultDisplayCallbacks$9() {
        this.mTrustManager.reportKeyguardShowingChanged();
    }

    /* access modifiers changed from: private */
    public void notifyTrustedChangedLocked(boolean z) {
        for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
            try {
                this.mKeyguardStateCallbacks.get(size).onTrustedChanged(z);
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call notifyTrustedChangedLocked", e);
                if (e instanceof DeadObjectException) {
                    this.mKeyguardStateCallbacks.remove(size);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void notifyHasLockscreenWallpaperChanged(boolean z) {
        for (int size = this.mKeyguardStateCallbacks.size() - 1; size >= 0; size--) {
            try {
                this.mKeyguardStateCallbacks.get(size).onHasLockscreenWallpaperChanged(z);
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call onHasLockscreenWallpaperChanged", e);
                if (e instanceof DeadObjectException) {
                    this.mKeyguardStateCallbacks.remove(size);
                }
            }
        }
    }

    public void addStateMonitorCallback(IKeyguardStateCallback iKeyguardStateCallback) {
        synchronized (this) {
            this.mKeyguardStateCallbacks.add(iKeyguardStateCallback);
            try {
                iKeyguardStateCallback.onSimSecureStateChanged(this.mUpdateMonitor.isSimPinSecure());
                iKeyguardStateCallback.onShowingStateChanged(this.mShowing);
                iKeyguardStateCallback.onInputRestrictedStateChanged(this.mInputRestricted);
                iKeyguardStateCallback.onTrustedChanged(this.mUpdateMonitor.getUserHasTrust(KeyguardUpdateMonitor.getCurrentUser()));
                iKeyguardStateCallback.onHasLockscreenWallpaperChanged(this.mUpdateMonitor.hasLockscreenWallpaper());
            } catch (RemoteException e) {
                Slog.w("KeyguardViewMediator", "Failed to call to IKeyguardStateCallback", e);
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DismissMessage {
        private final IKeyguardDismissCallback mCallback;
        private final CharSequence mMessage;

        DismissMessage(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) {
            this.mCallback = iKeyguardDismissCallback;
            this.mMessage = charSequence;
        }

        public IKeyguardDismissCallback getCallback() {
            return this.mCallback;
        }

        public CharSequence getMessage() {
            return this.mMessage;
        }
    }
}
