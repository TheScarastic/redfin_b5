package com.android.keyguard;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.UserSwitchObserver;
import android.app.admin.DevicePolicyManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.biometrics.CryptoObject;
import android.hardware.biometrics.IBiometricEnabledOnKeyguardCallback;
import android.hardware.face.FaceManager;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.service.dreams.IDreamManager;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import androidx.lifecycle.Observer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.LockPatternUtils;
import com.android.settingslib.WirelessUtils;
import com.android.settingslib.fuelgauge.BatteryStatus;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.R$string;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.Assert;
import com.android.systemui.util.RingerModeTracker;
import com.google.android.collect.Lists;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class KeyguardUpdateMonitor implements TrustManager.TrustListener, Dumpable {
    public static final boolean CORE_APPS_ONLY;
    private static final boolean DEBUG_FACE;
    private static final boolean DEBUG_FINGERPRINT;
    private static final ComponentName FALLBACK_HOME_COMPONENT = new ComponentName("com.android.settings", "com.android.settings.FallbackHome");
    private static int sCurrentUser;
    private boolean mAssistantVisible;
    private final AuthController mAuthController;
    private boolean mAuthInterruptActive;
    private final Executor mBackgroundExecutor;
    @VisibleForTesting
    BatteryStatus mBatteryStatus;
    private BiometricManager mBiometricManager;
    private boolean mBouncer;
    @VisibleForTesting
    protected final BroadcastReceiver mBroadcastAllReceiver;
    private final BroadcastDispatcher mBroadcastDispatcher;
    @VisibleForTesting
    protected final BroadcastReceiver mBroadcastReceiver;
    private final Context mContext;
    private boolean mCredentialAttempted;
    private boolean mDeviceInteractive;
    private final DevicePolicyManager mDevicePolicyManager;
    private ContentObserver mDeviceProvisionedObserver;
    private final IDreamManager mDreamManager;
    private CancellationSignal mFaceCancelSignal;
    private boolean mFaceLockedOutPermanent;
    private FaceManager mFaceManager;
    private List<FaceSensorPropertiesInternal> mFaceSensorProperties;
    private CancellationSignal mFingerprintCancelSignal;
    private boolean mFingerprintLockedOut;
    private boolean mFingerprintLockedOutPermanent;
    private FingerprintManager mFpm;
    private boolean mGoingToSleep;
    private final Handler mHandler;
    private boolean mHasLockscreenWallpaper;
    private final boolean mIsAutomotive;
    private boolean mIsDreaming;
    private boolean mIsFaceAuthUserRequested;
    private boolean mIsFaceEnrolled;
    private final boolean mIsPrimaryUser;
    private boolean mIsUdfpsEnrolled;
    private KeyguardBypassController mKeyguardBypassController;
    private boolean mKeyguardGoingAway;
    private boolean mKeyguardIsVisible;
    private boolean mKeyguardOccluded;
    private boolean mLockIconPressed;
    private LockPatternUtils mLockPatternUtils;
    private int mLockScreenMode;
    private boolean mLogoutEnabled;
    private boolean mNeedsSlowUnlockTransition;
    private boolean mOccludingAppRequestingFace;
    private boolean mOccludingAppRequestingFp;
    private int mPhoneState;
    private int mRingMode;
    private RingerModeTracker mRingerModeTracker;
    private boolean mScreenOn;
    private boolean mSecureCameraLaunched;
    private int mStatusBarState;
    private final StatusBarStateController mStatusBarStateController;
    private final StatusBarStateController.StateListener mStatusBarStateControllerListener;
    private StrongAuthTracker mStrongAuthTracker;
    private List<SubscriptionInfo> mSubscriptionInfo;
    private SubscriptionManager mSubscriptionManager;
    private boolean mSwitchingUser;
    @VisibleForTesting
    protected boolean mTelephonyCapable;
    private final TelephonyListenerManager mTelephonyListenerManager;
    private TelephonyManager mTelephonyManager;
    private ContentObserver mTimeFormatChangeObserver;
    private TrustManager mTrustManager;
    private UserManager mUserManager;
    private final UserSwitchObserver mUserSwitchObserver;
    private final Vibrator mVibrator;
    HashMap<Integer, SimData> mSimDatas = new HashMap<>();
    HashMap<Integer, ServiceState> mServiceStates = new HashMap<>();
    private final boolean mAcquiredHapticEnabled = false;
    private final ArrayList<WeakReference<KeyguardUpdateMonitorCallback>> mCallbacks = Lists.newArrayList();
    private int mFingerprintRunningState = 0;
    private int mFaceRunningState = 0;
    private int mActiveMobileDataSubscription = -1;
    private int mHardwareFingerprintUnavailableRetryCount = 0;
    private int mHardwareFaceUnavailableRetryCount = 0;
    private final Runnable mFpCancelNotReceived = new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda7
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardUpdateMonitor.this.lambda$new$0();
        }
    };
    private final Runnable mFaceCancelNotReceived = new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardUpdateMonitor.this.lambda$new$1();
        }
    };
    private final Observer<Integer> mRingerModeObserver = new Observer<Integer>() { // from class: com.android.keyguard.KeyguardUpdateMonitor.2
        public void onChanged(Integer num) {
            KeyguardUpdateMonitor.this.mHandler.obtainMessage(305, num.intValue(), 0).sendToTarget();
        }
    };
    private SparseBooleanArray mBiometricEnabledForUser = new SparseBooleanArray();
    private IBiometricEnabledOnKeyguardCallback mBiometricEnabledCallback = new IBiometricEnabledOnKeyguardCallback.Stub() { // from class: com.android.keyguard.KeyguardUpdateMonitor.3
        public void onChanged(boolean z, int i) throws RemoteException {
            KeyguardUpdateMonitor.this.mHandler.post(new KeyguardUpdateMonitor$3$$ExternalSyntheticLambda0(this, i, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onChanged$0(int i, boolean z) {
            KeyguardUpdateMonitor.this.mBiometricEnabledForUser.put(i, z);
            KeyguardUpdateMonitor.this.updateBiometricListeningState();
        }
    };
    @VisibleForTesting
    public TelephonyCallback.ActiveDataSubscriptionIdListener mPhoneStateListener = new TelephonyCallback.ActiveDataSubscriptionIdListener() { // from class: com.android.keyguard.KeyguardUpdateMonitor.4
        public void onActiveDataSubscriptionIdChanged(int i) {
            KeyguardUpdateMonitor.this.mActiveMobileDataSubscription = i;
            KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(328);
        }
    };
    private SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.keyguard.KeyguardUpdateMonitor.5
        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(328);
        }
    };
    private SparseBooleanArray mUserIsUnlocked = new SparseBooleanArray();
    private SparseBooleanArray mUserHasTrust = new SparseBooleanArray();
    private SparseBooleanArray mUserTrustIsManaged = new SparseBooleanArray();
    private SparseBooleanArray mUserTrustIsUsuallyManaged = new SparseBooleanArray();
    private SparseBooleanArray mUserFaceUnlockRunning = new SparseBooleanArray();
    private Map<Integer, Intent> mSecondaryLockscreenRequirement = new HashMap();
    @VisibleForTesting
    SparseArray<BiometricAuthenticated> mUserFingerprintAuthenticated = new SparseArray<>();
    @VisibleForTesting
    SparseArray<BiometricAuthenticated> mUserFaceAuthenticated = new SparseArray<>();
    private final KeyguardListenQueue mListenModels = new KeyguardListenQueue();
    private Runnable mUpdateBiometricListeningState = new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda6
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardUpdateMonitor.this.updateBiometricListeningState();
        }
    };
    private Runnable mRetryFingerprintAuthentication = new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor.7
        @Override // java.lang.Runnable
        public void run() {
            Log.w("KeyguardUpdateMonitor", "Retrying fingerprint after HW unavailable, attempt " + KeyguardUpdateMonitor.this.mHardwareFingerprintUnavailableRetryCount);
            if (KeyguardUpdateMonitor.this.mFpm.isHardwareDetected()) {
                KeyguardUpdateMonitor.this.updateFingerprintListeningState();
            } else if (KeyguardUpdateMonitor.this.mHardwareFingerprintUnavailableRetryCount < 20) {
                KeyguardUpdateMonitor.access$708(KeyguardUpdateMonitor.this);
                KeyguardUpdateMonitor.this.mHandler.postDelayed(KeyguardUpdateMonitor.this.mRetryFingerprintAuthentication, 500);
            }
        }
    };
    private Runnable mRetryFaceAuthentication = new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor.8
        @Override // java.lang.Runnable
        public void run() {
            Log.w("KeyguardUpdateMonitor", "Retrying face after HW unavailable, attempt " + KeyguardUpdateMonitor.this.mHardwareFaceUnavailableRetryCount);
            KeyguardUpdateMonitor.this.updateFaceListeningState();
        }
    };
    private DisplayClientState mDisplayClientState = new DisplayClientState();
    private final FingerprintManager.LockoutResetCallback mFingerprintLockoutResetCallback = new FingerprintManager.LockoutResetCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor.11
        public void onLockoutReset(int i) {
            KeyguardUpdateMonitor.this.handleFingerprintLockoutReset();
        }
    };
    private final FaceManager.LockoutResetCallback mFaceLockoutResetCallback = new FaceManager.LockoutResetCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor.12
        public void onLockoutReset(int i) {
            KeyguardUpdateMonitor.this.handleFaceLockoutReset();
        }
    };
    private final FingerprintManager.FingerprintDetectionCallback mFingerprintDetectionCallback = new FingerprintManager.FingerprintDetectionCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda1
        public final void onFingerprintDetected(int i, int i2, boolean z) {
            KeyguardUpdateMonitor.this.lambda$new$3(i, i2, z);
        }
    };
    @VisibleForTesting
    final FingerprintManager.AuthenticationCallback mFingerprintAuthenticationCallback = new FingerprintManager.AuthenticationCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor.13
        private boolean mPlayedAcquiredHaptic;

        @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
        public void onAuthenticationFailed() {
            KeyguardUpdateMonitor.this.handleFingerprintAuthFailed();
        }

        @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult authenticationResult) {
            Trace.beginSection("KeyguardUpdateMonitor#onAuthenticationSucceeded");
            KeyguardUpdateMonitor.this.handleFingerprintAuthenticated(authenticationResult.getUserId(), authenticationResult.isStrongBiometric());
            Trace.endSection();
            if (!this.mPlayedAcquiredHaptic && KeyguardUpdateMonitor.this.isUdfpsEnrolled()) {
                KeyguardUpdateMonitor.this.playAcquiredHaptic();
            }
        }

        @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
        public void onAuthenticationHelp(int i, CharSequence charSequence) {
            KeyguardUpdateMonitor.this.handleFingerprintHelp(i, charSequence.toString());
        }

        @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
        public void onAuthenticationError(int i, CharSequence charSequence) {
            KeyguardUpdateMonitor.this.handleFingerprintError(i, charSequence.toString());
        }

        public void onAuthenticationAcquired(int i) {
            KeyguardUpdateMonitor.this.handleFingerprintAcquired(i);
            if (i == 0 && KeyguardUpdateMonitor.this.isUdfpsEnrolled()) {
                this.mPlayedAcquiredHaptic = true;
                KeyguardUpdateMonitor.this.playAcquiredHaptic();
            }
        }

        public void onUdfpsPointerDown(int i) {
            Log.d("KeyguardUpdateMonitor", "onUdfpsPointerDown, sensorId: " + i);
            this.mPlayedAcquiredHaptic = false;
        }

        public void onUdfpsPointerUp(int i) {
            Log.d("KeyguardUpdateMonitor", "onUdfpsPointerUp, sensorId: " + i);
        }
    };
    private final FaceManager.FaceDetectionCallback mFaceDetectionCallback = new FaceManager.FaceDetectionCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda0
        public final void onFaceDetected(int i, int i2, boolean z) {
            KeyguardUpdateMonitor.this.lambda$new$4(i, i2, z);
        }
    };
    @VisibleForTesting
    final FaceManager.AuthenticationCallback mFaceAuthenticationCallback = new FaceManager.AuthenticationCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor.14
        public void onAuthenticationFailed() {
            KeyguardUpdateMonitor.this.handleFaceAuthFailed();
            if (KeyguardUpdateMonitor.this.mKeyguardBypassController != null) {
                KeyguardUpdateMonitor.this.mKeyguardBypassController.setUserHasDeviceEntryIntent(false);
            }
        }

        public void onAuthenticationSucceeded(FaceManager.AuthenticationResult authenticationResult) {
            Trace.beginSection("KeyguardUpdateMonitor#onAuthenticationSucceeded");
            KeyguardUpdateMonitor.this.handleFaceAuthenticated(authenticationResult.getUserId(), authenticationResult.isStrongBiometric());
            Trace.endSection();
            if (KeyguardUpdateMonitor.this.mKeyguardBypassController != null) {
                KeyguardUpdateMonitor.this.mKeyguardBypassController.setUserHasDeviceEntryIntent(false);
            }
        }

        public void onAuthenticationHelp(int i, CharSequence charSequence) {
            KeyguardUpdateMonitor.this.handleFaceHelp(i, charSequence.toString());
        }

        public void onAuthenticationError(int i, CharSequence charSequence) {
            KeyguardUpdateMonitor.this.handleFaceError(i, charSequence.toString());
            if (KeyguardUpdateMonitor.this.mKeyguardBypassController != null) {
                KeyguardUpdateMonitor.this.mKeyguardBypassController.setUserHasDeviceEntryIntent(false);
            }
        }

        public void onAuthenticationAcquired(int i) {
            KeyguardUpdateMonitor.this.handleFaceAcquired(i);
        }
    };
    private final TaskStackChangeListener mTaskStackListener = new TaskStackChangeListener() { // from class: com.android.keyguard.KeyguardUpdateMonitor.19
        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public void onTaskStackChangedBackground() {
            try {
                ActivityTaskManager.RootTaskInfo rootTaskInfo = ActivityTaskManager.getService().getRootTaskInfo(0, 4);
                if (rootTaskInfo != null) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(335, Boolean.valueOf(rootTaskInfo.visible)));
                }
            } catch (RemoteException e) {
                Log.e("KeyguardUpdateMonitor", "unable to check task stack", e);
            }
        }
    };
    private boolean mDeviceProvisioned = isDeviceProvisionedInSettingsDb();

    private boolean containsFlag(int i, int i2) {
        return (i & i2) != 0;
    }

    public static boolean isSimPinSecure(int i) {
        return i == 2 || i == 3 || i == 7;
    }

    @VisibleForTesting
    public void playAcquiredHaptic() {
    }

    static /* synthetic */ int access$708(KeyguardUpdateMonitor keyguardUpdateMonitor) {
        int i = keyguardUpdateMonitor.mHardwareFingerprintUnavailableRetryCount;
        keyguardUpdateMonitor.mHardwareFingerprintUnavailableRetryCount = i + 1;
        return i;
    }

    static {
        boolean z = Build.IS_DEBUGGABLE;
        DEBUG_FACE = z;
        DEBUG_FINGERPRINT = z;
        try {
            CORE_APPS_ONLY = IPackageManager.Stub.asInterface(ServiceManager.getService("package")).isOnlyCoreApps();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        Log.e("KeyguardUpdateMonitor", "Fp cancellation not received, transitioning to STOPPED");
        this.mFingerprintRunningState = 0;
        updateFingerprintListeningState();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        Log.e("KeyguardUpdateMonitor", "Face cancellation not received, transitioning to STOPPED");
        this.mFaceRunningState = 0;
        updateFaceListeningState();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class BiometricAuthenticated {
        private final boolean mAuthenticated;
        private final boolean mIsStrongBiometric;

        BiometricAuthenticated(boolean z, boolean z2) {
            this.mAuthenticated = z;
            this.mIsStrongBiometric = z2;
        }
    }

    public static synchronized void setCurrentUser(int i) {
        synchronized (KeyguardUpdateMonitor.class) {
            sCurrentUser = i;
        }
    }

    public static synchronized int getCurrentUser() {
        int i;
        synchronized (KeyguardUpdateMonitor.class) {
            i = sCurrentUser;
        }
        return i;
    }

    public void onTrustChanged(boolean z, int i, int i2) {
        Assert.isMainThread();
        this.mUserHasTrust.put(i, z);
        updateBiometricListeningState();
        for (int i3 = 0; i3 < this.mCallbacks.size(); i3++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i3).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTrustChanged(i);
                if (z && i2 != 0) {
                    keyguardUpdateMonitorCallback.onTrustGrantedWithFlags(i2, i);
                }
            }
        }
    }

    public void onTrustError(CharSequence charSequence) {
        dispatchErrorMessage(charSequence);
    }

    /* access modifiers changed from: private */
    public void handleSimSubscriptionInfoChanged() {
        Assert.isMainThread();
        Log.v("KeyguardUpdateMonitor", "onSubscriptionInfoChanged()");
        List<SubscriptionInfo> completeActiveSubscriptionInfoList = this.mSubscriptionManager.getCompleteActiveSubscriptionInfoList();
        if (completeActiveSubscriptionInfoList != null) {
            Iterator<SubscriptionInfo> it = completeActiveSubscriptionInfoList.iterator();
            while (it.hasNext()) {
                Log.v("KeyguardUpdateMonitor", "SubInfo:" + it.next());
            }
        } else {
            Log.v("KeyguardUpdateMonitor", "onSubscriptionInfoChanged: list is null");
        }
        List<SubscriptionInfo> subscriptionInfo = getSubscriptionInfo(true);
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        for (int i = 0; i < subscriptionInfo.size(); i++) {
            SubscriptionInfo subscriptionInfo2 = subscriptionInfo.get(i);
            hashSet.add(Integer.valueOf(subscriptionInfo2.getSubscriptionId()));
            if (refreshSimState(subscriptionInfo2.getSubscriptionId(), subscriptionInfo2.getSimSlotIndex())) {
                arrayList.add(subscriptionInfo2);
            }
        }
        Iterator<Map.Entry<Integer, SimData>> it2 = this.mSimDatas.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<Integer, SimData> next = it2.next();
            if (!hashSet.contains(next.getKey())) {
                Log.i("KeyguardUpdateMonitor", "Previously active sub id " + next.getKey() + " is now invalid, will remove");
                it2.remove();
                SimData value = next.getValue();
                for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
                    KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
                    if (keyguardUpdateMonitorCallback != null) {
                        keyguardUpdateMonitorCallback.onSimStateChanged(value.subId, value.slotId, value.simState);
                    }
                }
            }
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            SimData simData = this.mSimDatas.get(Integer.valueOf(((SubscriptionInfo) arrayList.get(i3)).getSubscriptionId()));
            for (int i4 = 0; i4 < this.mCallbacks.size(); i4++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback2 = this.mCallbacks.get(i4).get();
                if (keyguardUpdateMonitorCallback2 != null) {
                    keyguardUpdateMonitorCallback2.onSimStateChanged(simData.subId, simData.slotId, simData.simState);
                }
            }
        }
        callbacksRefreshCarrierInfo();
    }

    /* access modifiers changed from: private */
    public void handleAirplaneModeChanged() {
        callbacksRefreshCarrierInfo();
    }

    private void callbacksRefreshCarrierInfo() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onRefreshCarrierInfo();
            }
        }
    }

    public List<SubscriptionInfo> getSubscriptionInfo(boolean z) {
        List<SubscriptionInfo> list = this.mSubscriptionInfo;
        if (list == null || z) {
            list = this.mSubscriptionManager.getCompleteActiveSubscriptionInfoList();
        }
        if (list == null) {
            this.mSubscriptionInfo = new ArrayList();
        } else {
            this.mSubscriptionInfo = list;
        }
        return new ArrayList(this.mSubscriptionInfo);
    }

    public List<SubscriptionInfo> getFilteredSubscriptionInfo(boolean z) {
        List<SubscriptionInfo> subscriptionInfo = getSubscriptionInfo(false);
        if (subscriptionInfo.size() == 2) {
            SubscriptionInfo subscriptionInfo2 = subscriptionInfo.get(0);
            SubscriptionInfo subscriptionInfo3 = subscriptionInfo.get(1);
            if (subscriptionInfo2.getGroupUuid() != null && subscriptionInfo2.getGroupUuid().equals(subscriptionInfo3.getGroupUuid())) {
                if (!subscriptionInfo2.isOpportunistic() && !subscriptionInfo3.isOpportunistic()) {
                    return subscriptionInfo;
                }
                if (CarrierConfigManager.getDefaultConfig().getBoolean("always_show_primary_signal_bar_in_opportunistic_network_boolean")) {
                    if (!subscriptionInfo2.isOpportunistic()) {
                        subscriptionInfo2 = subscriptionInfo3;
                    }
                    subscriptionInfo.remove(subscriptionInfo2);
                } else {
                    if (subscriptionInfo2.getSubscriptionId() == this.mActiveMobileDataSubscription) {
                        subscriptionInfo2 = subscriptionInfo3;
                    }
                    subscriptionInfo.remove(subscriptionInfo2);
                }
            }
        }
        return subscriptionInfo;
    }

    public void onTrustManagedChanged(boolean z, int i) {
        Assert.isMainThread();
        this.mUserTrustIsManaged.put(i, z);
        this.mUserTrustIsUsuallyManaged.put(i, this.mTrustManager.isTrustUsuallyManaged(i));
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTrustManagedChanged(i);
            }
        }
    }

    public void setCredentialAttempted() {
        this.mCredentialAttempted = true;
        updateBiometricListeningState();
    }

    public void setKeyguardGoingAway(boolean z) {
        this.mKeyguardGoingAway = z;
        updateBiometricListeningState();
    }

    public void setKeyguardOccluded(boolean z) {
        this.mKeyguardOccluded = z;
        updateBiometricListeningState();
    }

    public void requestFaceAuthOnOccludingApp(boolean z) {
        this.mOccludingAppRequestingFace = z;
        updateFaceListeningState();
    }

    public void requestFingerprintAuthOnOccludingApp(boolean z) {
        this.mOccludingAppRequestingFp = z;
        updateFingerprintListeningState();
    }

    public void onCameraLaunched() {
        this.mSecureCameraLaunched = true;
        updateBiometricListeningState();
    }

    public boolean isDreaming() {
        return this.mIsDreaming;
    }

    public void awakenFromDream() {
        IDreamManager iDreamManager;
        if (this.mIsDreaming && (iDreamManager = this.mDreamManager) != null) {
            try {
                iDreamManager.awaken();
            } catch (RemoteException unused) {
                Log.e("KeyguardUpdateMonitor", "Unable to awaken from dream");
            }
        }
    }

    @VisibleForTesting
    protected void onFingerprintAuthenticated(int i, boolean z) {
        Assert.isMainThread();
        Trace.beginSection("KeyGuardUpdateMonitor#onFingerPrintAuthenticated");
        this.mUserFingerprintAuthenticated.put(i, new BiometricAuthenticated(true, z));
        if (getUserCanSkipBouncer(i)) {
            this.mTrustManager.unlockedByBiometricForUser(i, BiometricSourceType.FINGERPRINT);
        }
        this.mFingerprintCancelSignal = null;
        updateBiometricListeningState();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthenticated(i, BiometricSourceType.FINGERPRINT, z);
            }
        }
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(336), 500);
        this.mAssistantVisible = false;
        reportSuccessfulBiometricUnlock(z, i);
        Trace.endSection();
    }

    private void reportSuccessfulBiometricUnlock(final boolean z, final int i) {
        this.mBackgroundExecutor.execute(new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor.6
            @Override // java.lang.Runnable
            public void run() {
                KeyguardUpdateMonitor.this.mLockPatternUtils.reportSuccessfulBiometricUnlock(z, i);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleFingerprintAuthFailed() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthFailed(BiometricSourceType.FINGERPRINT);
            }
        }
        handleFingerprintHelp(-1, this.mContext.getString(R$string.kg_fingerprint_not_recognized));
    }

    /* access modifiers changed from: private */
    public void handleFingerprintAcquired(int i) {
        Assert.isMainThread();
        if (i == 0) {
            for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onBiometricAcquired(BiometricSourceType.FINGERPRINT);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFingerprintAuthenticated(int i, boolean z) {
        try {
            Trace.beginSection("KeyGuardUpdateMonitor#handlerFingerPrintAuthenticated");
            int i2 = ActivityManager.getService().getCurrentUser().id;
            if (i2 != i) {
                Log.d("KeyguardUpdateMonitor", "Fingerprint authenticated for wrong user: " + i);
            } else if (isFingerprintDisabled(i2)) {
                Log.d("KeyguardUpdateMonitor", "Fingerprint disabled by DPM for userId: " + i2);
            } else {
                onFingerprintAuthenticated(i2, z);
                setFingerprintRunningState(0);
                Trace.endSection();
            }
        } catch (RemoteException e) {
            Log.e("KeyguardUpdateMonitor", "Failed to get current user id: ", e);
        } finally {
            setFingerprintRunningState(0);
        }
    }

    /* access modifiers changed from: private */
    public void handleFingerprintHelp(int i, String str) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricHelp(i, str, BiometricSourceType.FINGERPRINT);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFingerprintError(int i, String str) {
        Assert.isMainThread();
        if (this.mHandler.hasCallbacks(this.mFpCancelNotReceived)) {
            this.mHandler.removeCallbacks(this.mFpCancelNotReceived);
        }
        this.mFingerprintCancelSignal = null;
        if (i == 5 && this.mFingerprintRunningState == 3) {
            setFingerprintRunningState(0);
            updateFingerprintListeningState();
        } else {
            setFingerprintRunningState(0);
        }
        if (i == 1) {
            this.mHandler.postDelayed(this.mRetryFingerprintAuthentication, 500);
        }
        if (i == 9) {
            this.mFingerprintLockedOutPermanent = true;
            requireStrongAuthIfAllLockedOut();
        }
        if (i == 7 || i == 9) {
            this.mFingerprintLockedOut = true;
            if (isUdfpsEnrolled()) {
                updateFingerprintListeningState();
            }
        }
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricError(i, str, BiometricSourceType.FINGERPRINT);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFingerprintLockoutReset() {
        this.mFingerprintLockedOut = false;
        this.mFingerprintLockedOutPermanent = false;
        if (isUdfpsEnrolled()) {
            this.mHandler.postDelayed(new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardUpdateMonitor.this.updateFingerprintListeningState();
                }
            }, 600);
        } else {
            updateFingerprintListeningState();
        }
    }

    private void setFingerprintRunningState(int i) {
        boolean z = false;
        boolean z2 = this.mFingerprintRunningState == 1;
        if (i == 1) {
            z = true;
        }
        this.mFingerprintRunningState = i;
        Log.d("KeyguardUpdateMonitor", "fingerprintRunningState: " + this.mFingerprintRunningState);
        if (z2 != z) {
            notifyFingerprintRunningStateChanged();
        }
    }

    private void notifyFingerprintRunningStateChanged() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricRunningStateChanged(isFingerprintDetectionRunning(), BiometricSourceType.FINGERPRINT);
            }
        }
    }

    @VisibleForTesting
    protected void onFaceAuthenticated(int i, boolean z) {
        Trace.beginSection("KeyGuardUpdateMonitor#onFaceAuthenticated");
        Assert.isMainThread();
        this.mUserFaceAuthenticated.put(i, new BiometricAuthenticated(true, z));
        if (getUserCanSkipBouncer(i)) {
            this.mTrustManager.unlockedByBiometricForUser(i, BiometricSourceType.FACE);
        }
        this.mFaceCancelSignal = null;
        updateBiometricListeningState();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthenticated(i, BiometricSourceType.FACE, z);
            }
        }
        this.mAssistantVisible = false;
        reportSuccessfulBiometricUnlock(z, i);
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public void handleFaceAuthFailed() {
        Assert.isMainThread();
        this.mFaceCancelSignal = null;
        setFaceRunningState(0);
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthFailed(BiometricSourceType.FACE);
            }
        }
        handleFaceHelp(-2, this.mContext.getString(R$string.kg_face_not_recognized));
    }

    /* access modifiers changed from: private */
    public void handleFaceAcquired(int i) {
        Assert.isMainThread();
        if (i == 0) {
            if (DEBUG_FACE) {
                Log.d("KeyguardUpdateMonitor", "Face acquired");
            }
            for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onBiometricAcquired(BiometricSourceType.FACE);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFaceAuthenticated(int i, boolean z) {
        Trace.beginSection("KeyGuardUpdateMonitor#handlerFaceAuthenticated");
        try {
            if (this.mGoingToSleep) {
                Log.d("KeyguardUpdateMonitor", "Aborted successful auth because device is going to sleep.");
                return;
            }
            int i2 = ActivityManager.getService().getCurrentUser().id;
            if (i2 != i) {
                Log.d("KeyguardUpdateMonitor", "Face authenticated for wrong user: " + i);
            } else if (isFaceDisabled(i2)) {
                Log.d("KeyguardUpdateMonitor", "Face authentication disabled by DPM for userId: " + i2);
            } else {
                if (DEBUG_FACE) {
                    Log.d("KeyguardUpdateMonitor", "Face auth succeeded for user " + i2);
                }
                onFaceAuthenticated(i2, z);
                setFaceRunningState(0);
                Trace.endSection();
            }
        } catch (RemoteException e) {
            Log.e("KeyguardUpdateMonitor", "Failed to get current user id: ", e);
        } finally {
            setFaceRunningState(0);
        }
    }

    /* access modifiers changed from: private */
    public void handleFaceHelp(int i, String str) {
        Assert.isMainThread();
        if (DEBUG_FACE) {
            Log.d("KeyguardUpdateMonitor", "Face help received: " + str);
        }
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricHelp(i, str, BiometricSourceType.FACE);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFaceError(int i, String str) {
        int i2;
        Assert.isMainThread();
        if (DEBUG_FACE) {
            Log.d("KeyguardUpdateMonitor", "Face error received: " + str);
        }
        if (this.mHandler.hasCallbacks(this.mFaceCancelNotReceived)) {
            this.mHandler.removeCallbacks(this.mFaceCancelNotReceived);
        }
        this.mFaceCancelSignal = null;
        if (i == 5 && this.mFaceRunningState == 3) {
            setFaceRunningState(0);
            updateFaceListeningState();
        } else {
            setFaceRunningState(0);
        }
        if ((i == 1 || i == 2) && (i2 = this.mHardwareFaceUnavailableRetryCount) < 20) {
            this.mHardwareFaceUnavailableRetryCount = i2 + 1;
            this.mHandler.removeCallbacks(this.mRetryFaceAuthentication);
            this.mHandler.postDelayed(this.mRetryFaceAuthentication, 500);
        }
        if (i == 9) {
            this.mFaceLockedOutPermanent = true;
            requireStrongAuthIfAllLockedOut();
        }
        for (int i3 = 0; i3 < this.mCallbacks.size(); i3++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i3).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricError(i, str, BiometricSourceType.FACE);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFaceLockoutReset() {
        this.mFaceLockedOutPermanent = false;
        updateFaceListeningState();
    }

    private void setFaceRunningState(int i) {
        boolean z = false;
        boolean z2 = this.mFaceRunningState == 1;
        if (i == 1) {
            z = true;
        }
        this.mFaceRunningState = i;
        Log.d("KeyguardUpdateMonitor", "faceRunningState: " + this.mFaceRunningState);
        if (z2 != z) {
            notifyFaceRunningStateChanged();
        }
    }

    private void notifyFaceRunningStateChanged() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricRunningStateChanged(isFaceDetectionRunning(), BiometricSourceType.FACE);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFaceUnlockStateChanged(boolean z, int i) {
        Assert.isMainThread();
        this.mUserFaceUnlockRunning.put(i, z);
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onFaceUnlockStateChanged(z, i);
            }
        }
    }

    public boolean isFingerprintDetectionRunning() {
        return this.mFingerprintRunningState == 1;
    }

    public boolean isFaceDetectionRunning() {
        return this.mFaceRunningState == 1;
    }

    private boolean isTrustDisabled(int i) {
        return isSimPinSecure();
    }

    private boolean isFingerprintDisabled(int i) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
        return !(devicePolicyManager == null || (devicePolicyManager.getKeyguardDisabledFeatures(null, i) & 32) == 0) || isSimPinSecure();
    }

    private boolean isFaceDisabled(int i) {
        return ((Boolean) DejankUtils.whitelistIpcs(new Supplier((DevicePolicyManager) this.mContext.getSystemService("device_policy"), i) { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda11
            public final /* synthetic */ DevicePolicyManager f$1;
            public final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Supplier
            public final Object get() {
                return KeyguardUpdateMonitor.this.lambda$isFaceDisabled$2(this.f$1, this.f$2);
            }
        })).booleanValue();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isFaceDisabled$2(DevicePolicyManager devicePolicyManager, int i) {
        return Boolean.valueOf(!(devicePolicyManager == null || (devicePolicyManager.getKeyguardDisabledFeatures(null, i) & 128) == 0) || isSimPinSecure());
    }

    private boolean getIsFaceAuthenticated() {
        BiometricAuthenticated biometricAuthenticated = this.mUserFaceAuthenticated.get(getCurrentUser());
        if (biometricAuthenticated != null) {
            return biometricAuthenticated.mAuthenticated;
        }
        return false;
    }

    private void requireStrongAuthIfAllLockedOut() {
        boolean z = true;
        boolean z2 = (this.mFaceLockedOutPermanent || !shouldListenForFace()) && !getIsFaceAuthenticated();
        if (!this.mFingerprintLockedOutPermanent && shouldListenForFingerprint(isUdfpsEnrolled())) {
            z = false;
        }
        if (z2 && z) {
            Log.d("KeyguardUpdateMonitor", "All biometrics locked out - requiring strong auth");
            this.mLockPatternUtils.requireStrongAuth(8, getCurrentUser());
        }
    }

    public boolean getUserCanSkipBouncer(int i) {
        return getUserHasTrust(i) || getUserUnlockedWithBiometric(i);
    }

    public boolean getUserHasTrust(int i) {
        return !isTrustDisabled(i) && this.mUserHasTrust.get(i);
    }

    public boolean getUserUnlockedWithBiometric(int i) {
        BiometricAuthenticated biometricAuthenticated = this.mUserFingerprintAuthenticated.get(i);
        BiometricAuthenticated biometricAuthenticated2 = this.mUserFaceAuthenticated.get(i);
        return (biometricAuthenticated != null && biometricAuthenticated.mAuthenticated && isUnlockingWithBiometricAllowed(biometricAuthenticated.mIsStrongBiometric)) || (biometricAuthenticated2 != null && biometricAuthenticated2.mAuthenticated && isUnlockingWithBiometricAllowed(biometricAuthenticated2.mIsStrongBiometric));
    }

    public boolean getUserTrustIsManaged(int i) {
        return this.mUserTrustIsManaged.get(i) && !isTrustDisabled(i);
    }

    private void updateSecondaryLockscreenRequirement(int i) {
        Intent intent = this.mSecondaryLockscreenRequirement.get(Integer.valueOf(i));
        boolean isSecondaryLockscreenEnabled = this.mDevicePolicyManager.isSecondaryLockscreenEnabled(UserHandle.of(i));
        boolean z = true;
        if (!isSecondaryLockscreenEnabled || intent != null) {
            if (!isSecondaryLockscreenEnabled && intent != null) {
                this.mSecondaryLockscreenRequirement.put(Integer.valueOf(i), null);
            }
            z = false;
        } else {
            ComponentName profileOwnerOrDeviceOwnerSupervisionComponent = this.mDevicePolicyManager.getProfileOwnerOrDeviceOwnerSupervisionComponent(UserHandle.of(i));
            if (profileOwnerOrDeviceOwnerSupervisionComponent == null) {
                Log.e("KeyguardUpdateMonitor", "No Profile Owner or Device Owner supervision app found for User " + i);
            } else {
                ResolveInfo resolveService = this.mContext.getPackageManager().resolveService(new Intent("android.app.action.BIND_SECONDARY_LOCKSCREEN_SERVICE").setPackage(profileOwnerOrDeviceOwnerSupervisionComponent.getPackageName()), 0);
                if (!(resolveService == null || resolveService.serviceInfo == null)) {
                    this.mSecondaryLockscreenRequirement.put(Integer.valueOf(i), new Intent().setComponent(resolveService.serviceInfo.getComponentName()));
                }
            }
            z = false;
        }
        if (z) {
            for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onSecondaryLockscreenRequirementChanged(i);
                }
            }
        }
    }

    public Intent getSecondaryLockscreenRequirement(int i) {
        return this.mSecondaryLockscreenRequirement.get(Integer.valueOf(i));
    }

    public boolean isTrustUsuallyManaged(int i) {
        Assert.isMainThread();
        return this.mUserTrustIsUsuallyManaged.get(i);
    }

    public boolean isUnlockingWithBiometricAllowed(boolean z) {
        return this.mStrongAuthTracker.isUnlockingWithBiometricAllowed(z);
    }

    public boolean isUserInLockdown(int i) {
        return containsFlag(this.mStrongAuthTracker.getStrongAuthForUser(i), 32);
    }

    private boolean isEncryptedOrLockdown(int i) {
        int strongAuthForUser = this.mStrongAuthTracker.getStrongAuthForUser(i);
        return containsFlag(strongAuthForUser, 1) || (containsFlag(strongAuthForUser, 2) || containsFlag(strongAuthForUser, 32));
    }

    public boolean userNeedsStrongAuth() {
        return this.mStrongAuthTracker.getStrongAuthForUser(getCurrentUser()) != 0;
    }

    public boolean needsSlowUnlockTransition() {
        return this.mNeedsSlowUnlockTransition;
    }

    public StrongAuthTracker getStrongAuthTracker() {
        return this.mStrongAuthTracker;
    }

    /* access modifiers changed from: private */
    public void notifyStrongAuthStateChanged(int i) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onStrongAuthStateChanged(i);
            }
        }
    }

    public boolean isScreenOn() {
        return this.mScreenOn;
    }

    private void dispatchErrorMessage(CharSequence charSequence) {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTrustAgentErrorMessage(charSequence);
            }
        }
    }

    @VisibleForTesting
    void setAssistantVisible(boolean z) {
        this.mAssistantVisible = z;
        updateBiometricListeningState();
    }

    /* loaded from: classes.dex */
    static class DisplayClientState {
        DisplayClientState() {
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(int i, int i2, boolean z) {
        handleFingerprintAuthenticated(i2, z);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(int i, int i2, boolean z) {
        handleFaceAuthenticated(i2, z);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SimData {
        public int simState;
        public int slotId;
        public int subId;

        SimData(int i, int i2, int i3) {
            this.simState = i;
            this.slotId = i2;
            this.subId = i3;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:31:0x008a, code lost:
            if ("IMSI".equals(r0) == false) goto L_0x008d;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static com.android.keyguard.KeyguardUpdateMonitor.SimData fromIntent(android.content.Intent r7) {
            /*
                java.lang.String r0 = r7.getAction()
                java.lang.String r1 = "android.intent.action.SIM_STATE_CHANGED"
                boolean r0 = r1.equals(r0)
                if (r0 == 0) goto L_0x0093
                java.lang.String r0 = "ss"
                java.lang.String r0 = r7.getStringExtra(r0)
                java.lang.String r1 = "android.telephony.extra.SLOT_INDEX"
                r2 = 0
                int r1 = r7.getIntExtra(r1, r2)
                r3 = -1
                java.lang.String r4 = "android.telephony.extra.SUBSCRIPTION_INDEX"
                int r3 = r7.getIntExtra(r4, r3)
                java.lang.String r4 = "ABSENT"
                boolean r4 = r4.equals(r0)
                r5 = 5
                java.lang.String r6 = "reason"
                if (r4 == 0) goto L_0x003d
                java.lang.String r7 = r7.getStringExtra(r6)
                java.lang.String r0 = "PERM_DISABLED"
                boolean r7 = r0.equals(r7)
                if (r7 == 0) goto L_0x003a
                r7 = 7
                goto L_0x003b
            L_0x003a:
                r7 = 1
            L_0x003b:
                r2 = r7
                goto L_0x008d
            L_0x003d:
                java.lang.String r4 = "READY"
                boolean r4 = r4.equals(r0)
                if (r4 == 0) goto L_0x0047
            L_0x0045:
                r2 = r5
                goto L_0x008d
            L_0x0047:
                java.lang.String r4 = "LOCKED"
                boolean r4 = r4.equals(r0)
                if (r4 == 0) goto L_0x0067
                java.lang.String r7 = r7.getStringExtra(r6)
                java.lang.String r0 = "PIN"
                boolean r0 = r0.equals(r7)
                if (r0 == 0) goto L_0x005d
                r2 = 2
                goto L_0x008d
            L_0x005d:
                java.lang.String r0 = "PUK"
                boolean r7 = r0.equals(r7)
                if (r7 == 0) goto L_0x008d
                r2 = 3
                goto L_0x008d
            L_0x0067:
                java.lang.String r7 = "NETWORK"
                boolean r7 = r7.equals(r0)
                if (r7 == 0) goto L_0x0071
                r2 = 4
                goto L_0x008d
            L_0x0071:
                java.lang.String r7 = "CARD_IO_ERROR"
                boolean r7 = r7.equals(r0)
                if (r7 == 0) goto L_0x007c
                r2 = 8
                goto L_0x008d
            L_0x007c:
                java.lang.String r7 = "LOADED"
                boolean r7 = r7.equals(r0)
                if (r7 != 0) goto L_0x0045
                java.lang.String r7 = "IMSI"
                boolean r7 = r7.equals(r0)
                if (r7 == 0) goto L_0x008d
                goto L_0x0045
            L_0x008d:
                com.android.keyguard.KeyguardUpdateMonitor$SimData r7 = new com.android.keyguard.KeyguardUpdateMonitor$SimData
                r7.<init>(r2, r1, r3)
                return r7
            L_0x0093:
                java.lang.IllegalArgumentException r7 = new java.lang.IllegalArgumentException
                java.lang.String r0 = "only handles intent ACTION_SIM_STATE_CHANGED"
                r7.<init>(r0)
                throw r7
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardUpdateMonitor.SimData.fromIntent(android.content.Intent):com.android.keyguard.KeyguardUpdateMonitor$SimData");
        }

        public String toString() {
            return "SimData{state=" + this.simState + ",slotId=" + this.slotId + ",subId=" + this.subId + "}";
        }
    }

    /* loaded from: classes.dex */
    public static class StrongAuthTracker extends LockPatternUtils.StrongAuthTracker {
        private final Consumer<Integer> mStrongAuthRequiredChangedCallback;

        public StrongAuthTracker(Context context, Consumer<Integer> consumer) {
            super(context);
            this.mStrongAuthRequiredChangedCallback = consumer;
        }

        public boolean isUnlockingWithBiometricAllowed(boolean z) {
            return isBiometricAllowedForUser(z, KeyguardUpdateMonitor.getCurrentUser());
        }

        public boolean hasUserAuthenticatedSinceBoot() {
            return (getStrongAuthForUser(KeyguardUpdateMonitor.getCurrentUser()) & 1) == 0;
        }

        public void onStrongAuthRequiredChanged(int i) {
            this.mStrongAuthRequiredChangedCallback.accept(Integer.valueOf(i));
        }
    }

    protected void handleStartedWakingUp() {
        Trace.beginSection("KeyguardUpdateMonitor#handleStartedWakingUp");
        Assert.isMainThread();
        updateBiometricListeningState();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onStartedWakingUp();
            }
        }
        Trace.endSection();
    }

    protected void handleStartedGoingToSleep(int i) {
        Assert.isMainThread();
        this.mLockIconPressed = false;
        clearBiometricRecognized();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onStartedGoingToSleep(i);
            }
        }
        this.mGoingToSleep = true;
        updateBiometricListeningState();
    }

    protected void handleFinishedGoingToSleep(int i) {
        Assert.isMainThread();
        this.mGoingToSleep = false;
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onFinishedGoingToSleep(i);
            }
        }
        updateBiometricListeningState();
    }

    /* access modifiers changed from: private */
    public void handleScreenTurnedOn() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onScreenTurnedOn();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleScreenTurnedOff() {
        DejankUtils.startDetectingBlockingIpcs("KeyguardUpdateMonitor#handleScreenTurnedOff");
        Assert.isMainThread();
        this.mHardwareFingerprintUnavailableRetryCount = 0;
        this.mHardwareFaceUnavailableRetryCount = 0;
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onScreenTurnedOff();
            }
        }
        DejankUtils.stopDetectingBlockingIpcs("KeyguardUpdateMonitor#handleScreenTurnedOff");
    }

    /* access modifiers changed from: private */
    public void handleDreamingStateChanged(int i) {
        Assert.isMainThread();
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        this.mIsDreaming = z;
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onDreamingStateChanged(this.mIsDreaming);
            }
        }
        updateBiometricListeningState();
    }

    /* access modifiers changed from: private */
    public void handleUserInfoChanged(int i) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserInfoChanged(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleUserUnlocked(int i) {
        Assert.isMainThread();
        this.mUserIsUnlocked.put(i, true);
        this.mNeedsSlowUnlockTransition = resolveNeedsSlowUnlockTransition();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserUnlocked();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleUserStopped(int i) {
        Assert.isMainThread();
        this.mUserIsUnlocked.put(i, this.mUserManager.isUserUnlocked(i));
    }

    @VisibleForTesting
    void handleUserRemoved(int i) {
        Assert.isMainThread();
        this.mUserIsUnlocked.delete(i);
        this.mUserTrustIsUsuallyManaged.delete(i);
    }

    /* access modifiers changed from: private */
    public void handleKeyguardGoingAway(boolean z) {
        Assert.isMainThread();
        setKeyguardGoingAway(z);
    }

    @VisibleForTesting
    protected void setStrongAuthTracker(StrongAuthTracker strongAuthTracker) {
        StrongAuthTracker strongAuthTracker2 = this.mStrongAuthTracker;
        if (strongAuthTracker2 != null) {
            this.mLockPatternUtils.unregisterStrongAuthTracker(strongAuthTracker2);
        }
        this.mStrongAuthTracker = strongAuthTracker;
        this.mLockPatternUtils.registerStrongAuthTracker(strongAuthTracker);
    }

    @VisibleForTesting
    void resetBiometricListeningState() {
        this.mFingerprintRunningState = 0;
        this.mFaceRunningState = 0;
    }

    /* access modifiers changed from: private */
    public void registerRingerTracker() {
        this.mRingerModeTracker.getRingerMode().observeForever(this.mRingerModeObserver);
    }

    /* access modifiers changed from: protected */
    @VisibleForTesting
    public KeyguardUpdateMonitor(Context context, Looper looper, BroadcastDispatcher broadcastDispatcher, DumpManager dumpManager, RingerModeTracker ringerModeTracker, Executor executor, StatusBarStateController statusBarStateController, LockPatternUtils lockPatternUtils, AuthController authController, TelephonyListenerManager telephonyListenerManager, FeatureFlags featureFlags, Vibrator vibrator) {
        AnonymousClass1 r5 = new StatusBarStateController.StateListener() { // from class: com.android.keyguard.KeyguardUpdateMonitor.1
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onStateChanged(int i) {
                KeyguardUpdateMonitor.this.mStatusBarState = i;
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onExpandedChanged(boolean z) {
                for (int i = 0; i < KeyguardUpdateMonitor.this.mCallbacks.size(); i++) {
                    KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) ((WeakReference) KeyguardUpdateMonitor.this.mCallbacks.get(i)).get();
                    if (keyguardUpdateMonitorCallback != null) {
                        keyguardUpdateMonitorCallback.onShadeExpandedChanged(z);
                    }
                }
            }
        };
        this.mStatusBarStateControllerListener = r5;
        AnonymousClass9 r8 = new BroadcastReceiver() { // from class: com.android.keyguard.KeyguardUpdateMonitor.9
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if ("android.intent.action.TIME_TICK".equals(action) || "android.intent.action.TIME_SET".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(301);
                } else if ("android.intent.action.TIMEZONE_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(339, intent.getStringExtra("time-zone")));
                } else if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(302, new BatteryStatus(intent)));
                } else if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    SimData fromIntent = SimData.fromIntent(intent);
                    if (!intent.getBooleanExtra("rebroadcastOnUnlock", false)) {
                        Log.v("KeyguardUpdateMonitor", "action " + action + " state: " + intent.getStringExtra("ss") + " slotId: " + fromIntent.slotId + " subid: " + fromIntent.subId);
                        KeyguardUpdateMonitor.this.mHandler.obtainMessage(304, fromIntent.subId, fromIntent.slotId, Integer.valueOf(fromIntent.simState)).sendToTarget();
                    } else if (fromIntent.simState == 1) {
                        KeyguardUpdateMonitor.this.mHandler.obtainMessage(338, Boolean.TRUE).sendToTarget();
                    }
                } else if ("android.intent.action.PHONE_STATE".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(306, intent.getStringExtra("state")));
                } else if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(329);
                } else if ("android.intent.action.SERVICE_STATE".equals(action)) {
                    ServiceState newFromBundle = ServiceState.newFromBundle(intent.getExtras());
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(330, intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1), 0, newFromBundle));
                } else if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(328);
                } else if ("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(337);
                }
            }
        };
        this.mBroadcastReceiver = r8;
        AnonymousClass10 r9 = new BroadcastReceiver() { // from class: com.android.keyguard.KeyguardUpdateMonitor.10
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if ("android.app.action.NEXT_ALARM_CLOCK_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(301);
                } else if ("android.intent.action.USER_INFO_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(317, intent.getIntExtra("android.intent.extra.user_handle", getSendingUserId()), 0));
                } else if ("com.android.facelock.FACE_UNLOCK_STARTED".equals(action)) {
                    Trace.beginSection("KeyguardUpdateMonitor.mBroadcastAllReceiver#onReceive ACTION_FACE_UNLOCK_STARTED");
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(327, 1, getSendingUserId()));
                    Trace.endSection();
                } else if ("com.android.facelock.FACE_UNLOCK_STOPPED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(327, 0, getSendingUserId()));
                } else if ("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(309, Integer.valueOf(getSendingUserId())));
                } else if ("android.intent.action.USER_UNLOCKED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(334, getSendingUserId(), 0));
                } else if ("android.intent.action.USER_STOPPED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(340, intent.getIntExtra("android.intent.extra.user_handle", -1), 0));
                } else if ("android.intent.action.USER_REMOVED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(341, intent.getIntExtra("android.intent.extra.user_handle", -1), 0));
                } else if ("android.nfc.action.REQUIRE_UNLOCK_FOR_NFC".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(345);
                }
            }
        };
        this.mBroadcastAllReceiver = r9;
        AnonymousClass17 r10 = new UserSwitchObserver() { // from class: com.android.keyguard.KeyguardUpdateMonitor.17
            public void onUserSwitching(int i, IRemoteCallback iRemoteCallback) {
                KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(310, i, 0, iRemoteCallback));
            }

            public void onUserSwitchComplete(int i) throws RemoteException {
                KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(314, i, 0));
            }
        };
        this.mUserSwitchObserver = r10;
        this.mContext = context;
        this.mSubscriptionManager = SubscriptionManager.from(context);
        this.mTelephonyListenerManager = telephonyListenerManager;
        this.mStrongAuthTracker = new StrongAuthTracker(context, new Consumer() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KeyguardUpdateMonitor.this.notifyStrongAuthStateChanged(((Integer) obj).intValue());
            }
        });
        this.mBackgroundExecutor = executor;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mRingerModeTracker = ringerModeTracker;
        this.mStatusBarStateController = statusBarStateController;
        statusBarStateController.addCallback(r5);
        this.mStatusBarState = statusBarStateController.getState();
        this.mLockPatternUtils = lockPatternUtils;
        this.mAuthController = authController;
        dumpManager.registerDumpable(KeyguardUpdateMonitor.class.getName(), this);
        this.mVibrator = vibrator;
        AnonymousClass15 r4 = new Handler(looper) { // from class: com.android.keyguard.KeyguardUpdateMonitor.15
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 301:
                        KeyguardUpdateMonitor.this.handleTimeUpdate();
                        return;
                    case 302:
                        KeyguardUpdateMonitor.this.handleBatteryUpdate((BatteryStatus) message.obj);
                        return;
                    case 303:
                    case 307:
                    case 311:
                    case 313:
                    case 315:
                    case 316:
                    case 323:
                    case 324:
                    case 325:
                    case 326:
                    default:
                        super.handleMessage(message);
                        return;
                    case 304:
                        KeyguardUpdateMonitor.this.handleSimStateChange(message.arg1, message.arg2, ((Integer) message.obj).intValue());
                        return;
                    case 305:
                        KeyguardUpdateMonitor.this.handleRingerModeChange(message.arg1);
                        return;
                    case 306:
                        KeyguardUpdateMonitor.this.handlePhoneStateChanged((String) message.obj);
                        return;
                    case 308:
                        KeyguardUpdateMonitor.this.handleDeviceProvisioned();
                        return;
                    case 309:
                        KeyguardUpdateMonitor.this.handleDevicePolicyManagerStateChanged(message.arg1);
                        return;
                    case 310:
                        KeyguardUpdateMonitor.this.handleUserSwitching(message.arg1, (IRemoteCallback) message.obj);
                        return;
                    case 312:
                        KeyguardUpdateMonitor.this.handleKeyguardReset();
                        return;
                    case 314:
                        KeyguardUpdateMonitor.this.handleUserSwitchComplete(message.arg1);
                        return;
                    case 317:
                        KeyguardUpdateMonitor.this.handleUserInfoChanged(message.arg1);
                        return;
                    case 318:
                        KeyguardUpdateMonitor.this.handleReportEmergencyCallAction();
                        return;
                    case 319:
                        Trace.beginSection("KeyguardUpdateMonitor#handler MSG_STARTED_WAKING_UP");
                        KeyguardUpdateMonitor.this.handleStartedWakingUp();
                        Trace.endSection();
                        return;
                    case 320:
                        KeyguardUpdateMonitor.this.handleFinishedGoingToSleep(message.arg1);
                        return;
                    case 321:
                        KeyguardUpdateMonitor.this.handleStartedGoingToSleep(message.arg1);
                        return;
                    case 322:
                        KeyguardUpdateMonitor.this.handleKeyguardBouncerChanged(message.arg1);
                        return;
                    case 327:
                        Trace.beginSection("KeyguardUpdateMonitor#handler MSG_FACE_UNLOCK_STATE_CHANGED");
                        KeyguardUpdateMonitor.this.handleFaceUnlockStateChanged(message.arg1 != 0, message.arg2);
                        Trace.endSection();
                        return;
                    case 328:
                        KeyguardUpdateMonitor.this.handleSimSubscriptionInfoChanged();
                        return;
                    case 329:
                        KeyguardUpdateMonitor.this.handleAirplaneModeChanged();
                        return;
                    case 330:
                        KeyguardUpdateMonitor.this.handleServiceStateChange(message.arg1, (ServiceState) message.obj);
                        return;
                    case 331:
                        KeyguardUpdateMonitor.this.handleScreenTurnedOn();
                        return;
                    case 332:
                        Trace.beginSection("KeyguardUpdateMonitor#handler MSG_SCREEN_TURNED_ON");
                        KeyguardUpdateMonitor.this.handleScreenTurnedOff();
                        Trace.endSection();
                        return;
                    case 333:
                        KeyguardUpdateMonitor.this.handleDreamingStateChanged(message.arg1);
                        return;
                    case 334:
                        KeyguardUpdateMonitor.this.handleUserUnlocked(message.arg1);
                        return;
                    case 335:
                        KeyguardUpdateMonitor.this.setAssistantVisible(((Boolean) message.obj).booleanValue());
                        return;
                    case 336:
                        KeyguardUpdateMonitor.this.updateBiometricListeningState();
                        return;
                    case 337:
                        KeyguardUpdateMonitor.this.updateLogoutEnabled();
                        return;
                    case 338:
                        KeyguardUpdateMonitor.this.updateTelephonyCapable(((Boolean) message.obj).booleanValue());
                        return;
                    case 339:
                        KeyguardUpdateMonitor.this.handleTimeZoneUpdate((String) message.obj);
                        return;
                    case 340:
                        KeyguardUpdateMonitor.this.handleUserStopped(message.arg1);
                        return;
                    case 341:
                        KeyguardUpdateMonitor.this.handleUserRemoved(message.arg1);
                        return;
                    case 342:
                        KeyguardUpdateMonitor.this.handleKeyguardGoingAway(((Boolean) message.obj).booleanValue());
                        return;
                    case 343:
                        KeyguardUpdateMonitor.this.handleLockScreenMode();
                        return;
                    case 344:
                        KeyguardUpdateMonitor.this.handleTimeFormatUpdate((String) message.obj);
                        return;
                    case 345:
                        KeyguardUpdateMonitor.this.handleRequireUnlockForNfc();
                        return;
                }
            }
        };
        this.mHandler = r4;
        if (!this.mDeviceProvisioned) {
            watchForDeviceProvisioning();
        }
        this.mBatteryStatus = new BatteryStatus(1, 100, 0, 0, 0, true);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_TICK");
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.SERVICE_STATE");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
        broadcastDispatcher.registerReceiverWithHandler(r8, intentFilter, r4);
        executor.execute(new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardUpdateMonitor.this.lambda$new$5();
            }
        });
        r4.post(new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardUpdateMonitor.this.registerRingerTracker();
            }
        });
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.USER_INFO_CHANGED");
        intentFilter2.addAction("android.app.action.NEXT_ALARM_CLOCK_CHANGED");
        intentFilter2.addAction("com.android.facelock.FACE_UNLOCK_STARTED");
        intentFilter2.addAction("com.android.facelock.FACE_UNLOCK_STOPPED");
        intentFilter2.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
        intentFilter2.addAction("android.intent.action.USER_UNLOCKED");
        intentFilter2.addAction("android.intent.action.USER_STOPPED");
        intentFilter2.addAction("android.intent.action.USER_REMOVED");
        intentFilter2.addAction("android.nfc.action.REQUIRE_UNLOCK_FOR_NFC");
        broadcastDispatcher.registerReceiverWithHandler(r9, intentFilter2, r4, UserHandle.ALL);
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mSubscriptionListener);
        try {
            ActivityManager.getService().registerUserSwitchObserver(r10, "KeyguardUpdateMonitor");
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
        TrustManager trustManager = (TrustManager) context.getSystemService(TrustManager.class);
        this.mTrustManager = trustManager;
        trustManager.registerTrustListener(this);
        setStrongAuthTracker(this.mStrongAuthTracker);
        this.mDreamManager = IDreamManager.Stub.asInterface(ServiceManager.getService("dreams"));
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) {
            this.mFpm = (FingerprintManager) context.getSystemService("fingerprint");
        }
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.biometrics.face")) {
            FaceManager faceManager = (FaceManager) context.getSystemService("face");
            this.mFaceManager = faceManager;
            this.mFaceSensorProperties = faceManager.getSensorPropertiesInternal();
        }
        if (!(this.mFpm == null && this.mFaceManager == null)) {
            BiometricManager biometricManager = (BiometricManager) context.getSystemService(BiometricManager.class);
            this.mBiometricManager = biometricManager;
            biometricManager.registerEnabledOnKeyguardCallback(this.mBiometricEnabledCallback);
        }
        updateBiometricListeningState();
        FingerprintManager fingerprintManager = this.mFpm;
        if (fingerprintManager != null) {
            fingerprintManager.addLockoutResetCallback(this.mFingerprintLockoutResetCallback);
        }
        FaceManager faceManager2 = this.mFaceManager;
        if (faceManager2 != null) {
            faceManager2.addLockoutResetCallback(this.mFaceLockoutResetCallback);
        }
        this.mIsAutomotive = isAutomotive();
        TaskStackChangeListeners.getInstance().registerTaskStackListener(this.mTaskStackListener);
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        this.mUserManager = userManager;
        this.mIsPrimaryUser = userManager.isPrimaryUser();
        int currentUser = ActivityManager.getCurrentUser();
        this.mUserIsUnlocked.put(currentUser, this.mUserManager.isUserUnlocked(currentUser));
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        this.mDevicePolicyManager = devicePolicyManager;
        this.mLogoutEnabled = devicePolicyManager.isLogoutEnabled();
        updateSecondaryLockscreenRequirement(currentUser);
        for (UserInfo userInfo : this.mUserManager.getUsers()) {
            SparseBooleanArray sparseBooleanArray = this.mUserTrustIsUsuallyManaged;
            int i = userInfo.id;
            sparseBooleanArray.put(i, this.mTrustManager.isTrustUsuallyManaged(i));
        }
        updateAirplaneModeState();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.mTelephonyManager = telephonyManager;
        if (telephonyManager != null) {
            this.mTelephonyListenerManager.addActiveDataSubscriptionIdListener(this.mPhoneStateListener);
            for (int i2 = 0; i2 < this.mTelephonyManager.getActiveModemCount(); i2++) {
                int simState = this.mTelephonyManager.getSimState(i2);
                int[] subscriptionIds = this.mSubscriptionManager.getSubscriptionIds(i2);
                if (subscriptionIds != null) {
                    for (int i3 : subscriptionIds) {
                        this.mHandler.obtainMessage(304, i3, i2, Integer.valueOf(simState)).sendToTarget();
                    }
                }
            }
        }
        updateLockScreenMode(featureFlags.isKeyguardLayoutEnabled());
        this.mTimeFormatChangeObserver = new ContentObserver(this.mHandler) { // from class: com.android.keyguard.KeyguardUpdateMonitor.16
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(344, Settings.System.getString(KeyguardUpdateMonitor.this.mContext.getContentResolver(), "time_12_24")));
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("time_12_24"), false, this.mTimeFormatChangeObserver, -1);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5() {
        Intent registerReceiver;
        int defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
        ServiceState serviceStateForSubscriber = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).getServiceStateForSubscriber(defaultSubscriptionId);
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(330, defaultSubscriptionId, 0, serviceStateForSubscriber));
        if (this.mBatteryStatus == null && (registerReceiver = this.mContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"))) != null && this.mBatteryStatus == null) {
            this.mBroadcastReceiver.onReceive(this.mContext, registerReceiver);
        }
    }

    private void updateLockScreenMode(boolean z) {
        if (z != this.mLockScreenMode) {
            this.mLockScreenMode = z ? 1 : 0;
            this.mHandler.sendEmptyMessage(343);
        }
    }

    private void updateUdfpsEnrolled(int i) {
        this.mIsUdfpsEnrolled = this.mAuthController.isUdfpsEnrolled(i);
    }

    private void updateFaceEnrolled(int i) {
        this.mIsFaceEnrolled = ((Boolean) DejankUtils.whitelistIpcs(new Supplier(i) { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda10
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.function.Supplier
            public final Object get() {
                return KeyguardUpdateMonitor.this.lambda$updateFaceEnrolled$6(this.f$1);
            }
        })).booleanValue();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$updateFaceEnrolled$6(int i) {
        FaceManager faceManager = this.mFaceManager;
        return Boolean.valueOf(faceManager != null && faceManager.isHardwareDetected() && this.mFaceManager.hasEnrolledTemplates(i) && this.mBiometricEnabledForUser.get(i));
    }

    public boolean isUdfpsEnrolled() {
        return this.mIsUdfpsEnrolled;
    }

    public boolean isUdfpsAvailable() {
        return this.mAuthController.getUdfpsProps() != null && !this.mAuthController.getUdfpsProps().isEmpty();
    }

    public boolean isFaceEnrolled() {
        return this.mIsFaceEnrolled;
    }

    private void updateAirplaneModeState() {
        if (WirelessUtils.isAirplaneModeOn(this.mContext) && !this.mHandler.hasMessages(329)) {
            this.mHandler.sendEmptyMessage(329);
        }
    }

    /* access modifiers changed from: private */
    public void updateBiometricListeningState() {
        updateFingerprintListeningState();
        updateFaceListeningState();
    }

    /* access modifiers changed from: private */
    public void updateFingerprintListeningState() {
        if (!this.mHandler.hasMessages(336)) {
            updateUdfpsEnrolled(getCurrentUser());
            boolean shouldListenForFingerprint = shouldListenForFingerprint(isUdfpsEnrolled());
            int i = this.mFingerprintRunningState;
            boolean z = true;
            if (!(i == 1 || i == 3)) {
                z = false;
            }
            if (z && !shouldListenForFingerprint) {
                stopListeningForFingerprint();
            } else if (!z && shouldListenForFingerprint) {
                startListeningForFingerprint();
            }
        }
    }

    public boolean isUserUnlocked(int i) {
        return this.mUserIsUnlocked.get(i);
    }

    public void onAuthInterruptDetected(boolean z) {
        if (this.mAuthInterruptActive != z) {
            this.mAuthInterruptActive = z;
            updateFaceListeningState();
        }
    }

    public void requestFaceAuth(boolean z) {
        this.mIsFaceAuthUserRequested = z | this.mIsFaceAuthUserRequested;
        updateFaceListeningState();
    }

    public void cancelFaceAuth() {
        stopListeningForFace();
    }

    /* access modifiers changed from: private */
    public void updateFaceListeningState() {
        if (!this.mHandler.hasMessages(336)) {
            this.mHandler.removeCallbacks(this.mRetryFaceAuthentication);
            boolean shouldListenForFace = shouldListenForFace();
            int i = this.mFaceRunningState;
            if (i == 1 && !shouldListenForFace) {
                this.mIsFaceAuthUserRequested = false;
                stopListeningForFace();
            } else if (i != 1 && shouldListenForFace) {
                startListeningForFace();
            }
        }
    }

    private boolean shouldListenForFingerprintAssistant() {
        BiometricAuthenticated biometricAuthenticated = this.mUserFingerprintAuthenticated.get(getCurrentUser());
        if (!this.mAssistantVisible || !this.mKeyguardOccluded) {
            return false;
        }
        if ((biometricAuthenticated == null || !biometricAuthenticated.mAuthenticated) && !this.mUserHasTrust.get(getCurrentUser(), false)) {
            return true;
        }
        return false;
    }

    private boolean shouldListenForFaceAssistant() {
        BiometricAuthenticated biometricAuthenticated = this.mUserFaceAuthenticated.get(getCurrentUser());
        if (!this.mAssistantVisible || !this.mKeyguardOccluded) {
            return false;
        }
        if ((biometricAuthenticated == null || !biometricAuthenticated.mAuthenticated) && !this.mUserHasTrust.get(getCurrentUser(), false)) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    protected boolean shouldListenForFingerprint(boolean z) {
        boolean z2;
        int currentUser = getCurrentUser();
        boolean z3 = !getUserHasTrust(currentUser);
        boolean shouldListenForFingerprintAssistant = shouldListenForFingerprintAssistant();
        boolean z4 = this.mKeyguardIsVisible || !this.mDeviceInteractive || (this.mBouncer && !this.mKeyguardGoingAway) || this.mGoingToSleep || shouldListenForFingerprintAssistant || (((z2 = this.mKeyguardOccluded) && this.mIsDreaming) || (z2 && z3 && (this.mOccludingAppRequestingFp || z)));
        boolean z5 = this.mBiometricEnabledForUser.get(currentUser);
        boolean userCanSkipBouncer = getUserCanSkipBouncer(currentUser);
        boolean isFingerprintDisabled = isFingerprintDisabled(currentUser);
        boolean z6 = !this.mSwitchingUser && !isFingerprintDisabled && (!this.mKeyguardGoingAway || !this.mDeviceInteractive) && this.mIsPrimaryUser && z5;
        boolean z7 = !this.mFingerprintLockedOut || !this.mBouncer || !this.mCredentialAttempted;
        boolean isEncryptedOrLockdown = isEncryptedOrLockdown(currentUser);
        boolean userNeedsStrongAuth = userNeedsStrongAuth();
        boolean z8 = z4 && z6 && z7 && (!z || (!userCanSkipBouncer && !isEncryptedOrLockdown && !userNeedsStrongAuth && z3 && !this.mFingerprintLockedOut));
        if (DEBUG_FINGERPRINT) {
            maybeLogListenerModelData(new KeyguardFingerprintListenModel(System.currentTimeMillis(), currentUser, z8, z5, this.mBouncer, userCanSkipBouncer, this.mCredentialAttempted, this.mDeviceInteractive, this.mIsDreaming, isEncryptedOrLockdown, isFingerprintDisabled, this.mFingerprintLockedOut, this.mGoingToSleep, this.mKeyguardGoingAway, this.mKeyguardIsVisible, this.mKeyguardOccluded, this.mOccludingAppRequestingFp, this.mIsPrimaryUser, shouldListenForFingerprintAssistant, this.mSwitchingUser, z, z3, userNeedsStrongAuth));
        }
        return z8;
    }

    public boolean shouldListenForFace() {
        boolean z = false;
        if (this.mFaceManager == null) {
            return false;
        }
        boolean z2 = this.mKeyguardIsVisible && this.mDeviceInteractive && !this.mGoingToSleep && !(this.mStatusBarState == 2);
        int currentUser = getCurrentUser();
        int strongAuthForUser = this.mStrongAuthTracker.getStrongAuthForUser(currentUser);
        boolean z3 = containsFlag(strongAuthForUser, 2) || containsFlag(strongAuthForUser, 32);
        boolean z4 = containsFlag(strongAuthForUser, 1) || containsFlag(strongAuthForUser, 16);
        KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
        boolean z5 = keyguardBypassController != null && keyguardBypassController.canBypass();
        boolean z6 = !getUserCanSkipBouncer(currentUser) || z5;
        boolean z7 = (!z3 || (!this.mFaceSensorProperties.isEmpty() && this.mFaceSensorProperties.get(0).supportsFaceDetection)) ? !z4 || (z5 && !this.mBouncer) : false;
        boolean isFaceAuthenticated = getIsFaceAuthenticated();
        boolean isFaceDisabled = isFaceDisabled(currentUser);
        boolean z8 = this.mBiometricEnabledForUser.get(currentUser);
        boolean shouldListenForFaceAssistant = shouldListenForFaceAssistant();
        if ((this.mBouncer || this.mAuthInterruptActive || this.mOccludingAppRequestingFace || z2 || shouldListenForFaceAssistant) && !this.mSwitchingUser && !isFaceDisabled && z6 && !this.mKeyguardGoingAway && z8 && !this.mLockIconPressed && z7 && this.mIsPrimaryUser && ((!this.mSecureCameraLaunched || this.mOccludingAppRequestingFace) && !isFaceAuthenticated)) {
            z = true;
        }
        if (DEBUG_FACE) {
            maybeLogListenerModelData(new KeyguardFaceListenModel(System.currentTimeMillis(), currentUser, z, this.mAuthInterruptActive, z6, z8, this.mBouncer, isFaceAuthenticated, isFaceDisabled, z2, this.mKeyguardGoingAway, shouldListenForFaceAssistant, this.mLockIconPressed, this.mOccludingAppRequestingFace, this.mIsPrimaryUser, z7, this.mSecureCameraLaunched, this.mSwitchingUser));
        }
        return z;
    }

    private void maybeLogListenerModelData(KeyguardListenModel keyguardListenModel) {
        boolean z = true;
        if ((!DEBUG_FACE || !(keyguardListenModel instanceof KeyguardFaceListenModel) || this.mFaceRunningState == 1) && (!DEBUG_FINGERPRINT || !(keyguardListenModel instanceof KeyguardFingerprintListenModel) || this.mFingerprintRunningState == 1)) {
            z = false;
        }
        if (z && keyguardListenModel.getListening()) {
            this.mListenModels.add(keyguardListenModel);
        }
    }

    private void startListeningForFingerprint() {
        int currentUser = getCurrentUser();
        boolean isUnlockWithFingerprintPossible = isUnlockWithFingerprintPossible(currentUser);
        if (this.mFingerprintCancelSignal != null) {
            Log.e("KeyguardUpdateMonitor", "Cancellation signal is not null, high chance of bug in fp auth lifecycle management. FP state: " + this.mFingerprintRunningState + ", unlockPossible: " + isUnlockWithFingerprintPossible);
        }
        int i = this.mFingerprintRunningState;
        if (i == 2) {
            setFingerprintRunningState(3);
        } else if (i != 3 && isUnlockWithFingerprintPossible) {
            this.mFingerprintCancelSignal = new CancellationSignal();
            if (isEncryptedOrLockdown(currentUser)) {
                this.mFpm.detectFingerprint(this.mFingerprintCancelSignal, this.mFingerprintDetectionCallback, currentUser);
            } else {
                this.mFpm.authenticate(null, this.mFingerprintCancelSignal, this.mFingerprintAuthenticationCallback, null, -1, currentUser);
            }
            setFingerprintRunningState(1);
        }
    }

    private void startListeningForFace() {
        int currentUser = getCurrentUser();
        boolean isUnlockWithFacePossible = isUnlockWithFacePossible(currentUser);
        if (this.mFaceCancelSignal != null) {
            Log.e("KeyguardUpdateMonitor", "Cancellation signal is not null, high chance of bug in face auth lifecycle management. Face state: " + this.mFaceRunningState + ", unlockPossible: " + isUnlockWithFacePossible);
        }
        int i = this.mFaceRunningState;
        if (i == 2) {
            setFaceRunningState(3);
        } else if (i != 3 && isUnlockWithFacePossible) {
            this.mFaceCancelSignal = new CancellationSignal();
            boolean z = !this.mFaceSensorProperties.isEmpty() && this.mFaceSensorProperties.get(0).supportsFaceDetection;
            if (!isEncryptedOrLockdown(currentUser) || !z) {
                KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
                this.mFaceManager.authenticate((CryptoObject) null, this.mFaceCancelSignal, this.mFaceAuthenticationCallback, (Handler) null, currentUser, keyguardBypassController != null && keyguardBypassController.isBypassEnabled());
            } else {
                this.mFaceManager.detectFace(this.mFaceCancelSignal, this.mFaceDetectionCallback, currentUser);
            }
            setFaceRunningState(1);
        }
    }

    public boolean isUnlockingWithBiometricsPossible(int i) {
        return isUnlockWithFacePossible(i) || isUnlockWithFingerprintPossible(i);
    }

    private boolean isUnlockWithFingerprintPossible(int i) {
        FingerprintManager fingerprintManager = this.mFpm;
        return fingerprintManager != null && fingerprintManager.isHardwareDetected() && !isFingerprintDisabled(i) && this.mFpm.hasEnrolledTemplates(i);
    }

    private boolean isUnlockWithFacePossible(int i) {
        return isFaceAuthEnabledForUser(i) && !isFaceDisabled(i);
    }

    public boolean isFaceAuthEnabledForUser(int i) {
        updateFaceEnrolled(i);
        return this.mIsFaceEnrolled;
    }

    private void stopListeningForFingerprint() {
        if (this.mFingerprintRunningState == 1) {
            CancellationSignal cancellationSignal = this.mFingerprintCancelSignal;
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
                this.mFingerprintCancelSignal = null;
                this.mHandler.removeCallbacks(this.mFpCancelNotReceived);
                this.mHandler.postDelayed(this.mFpCancelNotReceived, 3000);
            }
            setFingerprintRunningState(2);
        }
        if (this.mFingerprintRunningState == 3) {
            setFingerprintRunningState(2);
        }
    }

    private void stopListeningForFace() {
        if (this.mFaceRunningState == 1) {
            CancellationSignal cancellationSignal = this.mFaceCancelSignal;
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
                this.mFaceCancelSignal = null;
                this.mHandler.removeCallbacks(this.mFaceCancelNotReceived);
                this.mHandler.postDelayed(this.mFaceCancelNotReceived, 3000);
            }
            setFaceRunningState(2);
        }
        if (this.mFaceRunningState == 3) {
            setFaceRunningState(2);
        }
    }

    /* access modifiers changed from: private */
    public boolean isDeviceProvisionedInSettingsDb() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0;
    }

    private void watchForDeviceProvisioning() {
        this.mDeviceProvisionedObserver = new ContentObserver(this.mHandler) { // from class: com.android.keyguard.KeyguardUpdateMonitor.18
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                super.onChange(z);
                KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
                keyguardUpdateMonitor.mDeviceProvisioned = keyguardUpdateMonitor.isDeviceProvisionedInSettingsDb();
                if (KeyguardUpdateMonitor.this.mDeviceProvisioned) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(308);
                }
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("device_provisioned"), false, this.mDeviceProvisionedObserver);
        boolean isDeviceProvisionedInSettingsDb = isDeviceProvisionedInSettingsDb();
        if (isDeviceProvisionedInSettingsDb != this.mDeviceProvisioned) {
            this.mDeviceProvisioned = isDeviceProvisionedInSettingsDb;
            if (isDeviceProvisionedInSettingsDb) {
                this.mHandler.sendEmptyMessage(308);
            }
        }
    }

    public void setHasLockscreenWallpaper(boolean z) {
        Assert.isMainThread();
        if (z != this.mHasLockscreenWallpaper) {
            this.mHasLockscreenWallpaper = z;
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onHasLockscreenWallpaperChanged(z);
                }
            }
        }
    }

    public boolean hasLockscreenWallpaper() {
        return this.mHasLockscreenWallpaper;
    }

    /* access modifiers changed from: private */
    public void handleDevicePolicyManagerStateChanged(int i) {
        Assert.isMainThread();
        updateFingerprintListeningState();
        updateSecondaryLockscreenRequirement(i);
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onDevicePolicyManagerStateChanged();
            }
        }
    }

    @VisibleForTesting
    void handleUserSwitching(int i, IRemoteCallback iRemoteCallback) {
        Assert.isMainThread();
        clearBiometricRecognized();
        this.mUserTrustIsUsuallyManaged.put(i, this.mTrustManager.isTrustUsuallyManaged(i));
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserSwitching(i);
            }
        }
        try {
            iRemoteCallback.sendResult((Bundle) null);
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void handleUserSwitchComplete(int i) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserSwitchComplete(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleDeviceProvisioned() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onDeviceProvisioned();
            }
        }
        if (this.mDeviceProvisionedObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mDeviceProvisionedObserver);
            this.mDeviceProvisionedObserver = null;
        }
    }

    /* access modifiers changed from: private */
    public void handlePhoneStateChanged(String str) {
        Assert.isMainThread();
        if (TelephonyManager.EXTRA_STATE_IDLE.equals(str)) {
            this.mPhoneState = 0;
        } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(str)) {
            this.mPhoneState = 2;
        } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(str)) {
            this.mPhoneState = 1;
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onPhoneStateChanged(this.mPhoneState);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleRingerModeChange(int i) {
        Assert.isMainThread();
        this.mRingMode = i;
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onRingerModeChanged(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleTimeUpdate() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTimeChanged();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleLockScreenMode() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onLockScreenModeChanged(this.mLockScreenMode);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleTimeZoneUpdate(String str) {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTimeZoneChanged(TimeZone.getTimeZone(str));
                keyguardUpdateMonitorCallback.onTimeChanged();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleTimeFormatUpdate(String str) {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTimeFormatChanged(str);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleBatteryUpdate(BatteryStatus batteryStatus) {
        Assert.isMainThread();
        boolean isBatteryUpdateInteresting = isBatteryUpdateInteresting(this.mBatteryStatus, batteryStatus);
        this.mBatteryStatus = batteryStatus;
        if (isBatteryUpdateInteresting) {
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onRefreshBatteryInfo(batteryStatus);
                }
            }
        }
    }

    @VisibleForTesting
    void updateTelephonyCapable(boolean z) {
        Assert.isMainThread();
        if (z != this.mTelephonyCapable) {
            this.mTelephonyCapable = z;
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onTelephonyCapable(this.mTelephonyCapable);
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0077  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a8  */
    @com.android.internal.annotations.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void handleSimStateChange(int r7, int r8, int r9) {
        /*
            r6 = this;
            com.android.systemui.util.Assert.isMainThread()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "handleSimStateChange(subId="
            r0.append(r1)
            r0.append(r7)
            java.lang.String r1 = ", slotId="
            r0.append(r1)
            r0.append(r8)
            java.lang.String r1 = ", state="
            r0.append(r1)
            r0.append(r9)
            java.lang.String r1 = ")"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "KeyguardUpdateMonitor"
            android.util.Log.d(r1, r0)
            boolean r0 = android.telephony.SubscriptionManager.isValidSubscriptionId(r7)
            r2 = 0
            r3 = 1
            if (r0 != 0) goto L_0x0068
            java.lang.String r0 = "invalid subId in handleSimStateChange()"
            android.util.Log.w(r1, r0)
            if (r9 != r3) goto L_0x005f
            r6.updateTelephonyCapable(r3)
            java.util.HashMap<java.lang.Integer, com.android.keyguard.KeyguardUpdateMonitor$SimData> r0 = r6.mSimDatas
            java.util.Collection r0 = r0.values()
            java.util.Iterator r0 = r0.iterator()
        L_0x004a:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x005d
            java.lang.Object r1 = r0.next()
            com.android.keyguard.KeyguardUpdateMonitor$SimData r1 = (com.android.keyguard.KeyguardUpdateMonitor.SimData) r1
            int r4 = r1.slotId
            if (r4 != r8) goto L_0x004a
            r1.simState = r3
            goto L_0x004a
        L_0x005d:
            r0 = r3
            goto L_0x0069
        L_0x005f:
            r0 = 8
            if (r9 != r0) goto L_0x0067
            r6.updateTelephonyCapable(r3)
            goto L_0x0068
        L_0x0067:
            return
        L_0x0068:
            r0 = r2
        L_0x0069:
            java.util.HashMap<java.lang.Integer, com.android.keyguard.KeyguardUpdateMonitor$SimData> r1 = r6.mSimDatas
            java.lang.Integer r4 = java.lang.Integer.valueOf(r7)
            java.lang.Object r1 = r1.get(r4)
            com.android.keyguard.KeyguardUpdateMonitor$SimData r1 = (com.android.keyguard.KeyguardUpdateMonitor.SimData) r1
            if (r1 != 0) goto L_0x0086
            com.android.keyguard.KeyguardUpdateMonitor$SimData r1 = new com.android.keyguard.KeyguardUpdateMonitor$SimData
            r1.<init>(r9, r8, r7)
            java.util.HashMap<java.lang.Integer, com.android.keyguard.KeyguardUpdateMonitor$SimData> r4 = r6.mSimDatas
            java.lang.Integer r5 = java.lang.Integer.valueOf(r7)
            r4.put(r5, r1)
            goto L_0x009a
        L_0x0086:
            int r4 = r1.simState
            if (r4 != r9) goto L_0x0094
            int r4 = r1.subId
            if (r4 != r7) goto L_0x0094
            int r4 = r1.slotId
            if (r4 == r8) goto L_0x0093
            goto L_0x0094
        L_0x0093:
            r3 = r2
        L_0x0094:
            r1.simState = r9
            r1.subId = r7
            r1.slotId = r8
        L_0x009a:
            if (r3 != 0) goto L_0x009e
            if (r0 == 0) goto L_0x00be
        L_0x009e:
            if (r9 == 0) goto L_0x00be
        L_0x00a0:
            java.util.ArrayList<java.lang.ref.WeakReference<com.android.keyguard.KeyguardUpdateMonitorCallback>> r0 = r6.mCallbacks
            int r0 = r0.size()
            if (r2 >= r0) goto L_0x00be
            java.util.ArrayList<java.lang.ref.WeakReference<com.android.keyguard.KeyguardUpdateMonitorCallback>> r0 = r6.mCallbacks
            java.lang.Object r0 = r0.get(r2)
            java.lang.ref.WeakReference r0 = (java.lang.ref.WeakReference) r0
            java.lang.Object r0 = r0.get()
            com.android.keyguard.KeyguardUpdateMonitorCallback r0 = (com.android.keyguard.KeyguardUpdateMonitorCallback) r0
            if (r0 == 0) goto L_0x00bb
            r0.onSimStateChanged(r7, r8, r9)
        L_0x00bb:
            int r2 = r2 + 1
            goto L_0x00a0
        L_0x00be:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardUpdateMonitor.handleSimStateChange(int, int, int):void");
    }

    @VisibleForTesting
    void handleServiceStateChange(int i, ServiceState serviceState) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            Log.w("KeyguardUpdateMonitor", "invalid subId in handleServiceStateChange()");
            return;
        }
        updateTelephonyCapable(true);
        this.mServiceStates.put(Integer.valueOf(i), serviceState);
        callbacksRefreshCarrierInfo();
    }

    public boolean isKeyguardVisible() {
        return this.mKeyguardIsVisible;
    }

    public void onKeyguardVisibilityChanged(boolean z) {
        Assert.isMainThread();
        Log.d("KeyguardUpdateMonitor", "onKeyguardVisibilityChanged(" + z + ")");
        this.mKeyguardIsVisible = z;
        if (z) {
            this.mSecureCameraLaunched = false;
        }
        KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
        if (keyguardBypassController != null) {
            keyguardBypassController.setUserHasDeviceEntryIntent(false);
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onKeyguardVisibilityChangedRaw(z);
            }
        }
        updateBiometricListeningState();
    }

    /* access modifiers changed from: private */
    public void handleKeyguardReset() {
        updateBiometricListeningState();
        this.mNeedsSlowUnlockTransition = resolveNeedsSlowUnlockTransition();
    }

    private boolean resolveNeedsSlowUnlockTransition() {
        if (isUserUnlocked(getCurrentUser())) {
            return false;
        }
        ResolveInfo resolveActivityAsUser = this.mContext.getPackageManager().resolveActivityAsUser(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME"), 0, getCurrentUser());
        if (resolveActivityAsUser != null) {
            return FALLBACK_HOME_COMPONENT.equals(resolveActivityAsUser.getComponentInfo().getComponentName());
        }
        Log.w("KeyguardUpdateMonitor", "resolveNeedsSlowUnlockTransition: returning false since activity could not be resolved.");
        return false;
    }

    /* access modifiers changed from: private */
    public void handleKeyguardBouncerChanged(int i) {
        Assert.isMainThread();
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        this.mBouncer = z;
        if (z) {
            this.mSecureCameraLaunched = false;
        } else {
            this.mCredentialAttempted = false;
        }
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onKeyguardBouncerChanged(this.mBouncer);
            }
        }
        updateBiometricListeningState();
    }

    /* access modifiers changed from: private */
    public void handleRequireUnlockForNfc() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onRequireUnlockForNfc();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleReportEmergencyCallAction() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onEmergencyCallAction();
            }
        }
    }

    private boolean isBatteryUpdateInteresting(BatteryStatus batteryStatus, BatteryStatus batteryStatus2) {
        boolean isPluggedIn = batteryStatus2.isPluggedIn();
        boolean isPluggedIn2 = batteryStatus.isPluggedIn();
        boolean z = isPluggedIn2 && isPluggedIn && batteryStatus.status != batteryStatus2.status;
        boolean z2 = batteryStatus2.present;
        boolean z3 = batteryStatus.present;
        if (isPluggedIn2 == isPluggedIn && !z && batteryStatus.level == batteryStatus2.level) {
            return ((!isPluggedIn || batteryStatus2.maxChargingWattage == batteryStatus.maxChargingWattage) && z3 == z2 && batteryStatus2.health == batteryStatus.health) ? false : true;
        }
        return true;
    }

    private boolean isAutomotive() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive");
    }

    public void removeCallback(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback) {
        Assert.isMainThread();
        this.mCallbacks.removeIf(new Predicate() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return KeyguardUpdateMonitor.lambda$removeCallback$7(KeyguardUpdateMonitorCallback.this, (WeakReference) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeCallback$7(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback, WeakReference weakReference) {
        return weakReference.get() == keyguardUpdateMonitorCallback;
    }

    public void registerCallback(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback) {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            if (this.mCallbacks.get(i).get() == keyguardUpdateMonitorCallback) {
                return;
            }
        }
        this.mCallbacks.add(new WeakReference<>(keyguardUpdateMonitorCallback));
        removeCallback(null);
        sendUpdates(keyguardUpdateMonitorCallback);
    }

    public void setKeyguardBypassController(KeyguardBypassController keyguardBypassController) {
        this.mKeyguardBypassController = keyguardBypassController;
    }

    public boolean isSwitchingUser() {
        return this.mSwitchingUser;
    }

    public void setSwitchingUser(boolean z) {
        this.mSwitchingUser = z;
        this.mHandler.post(this.mUpdateBiometricListeningState);
    }

    private void sendUpdates(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback) {
        keyguardUpdateMonitorCallback.onRefreshBatteryInfo(this.mBatteryStatus);
        keyguardUpdateMonitorCallback.onTimeChanged();
        keyguardUpdateMonitorCallback.onRingerModeChanged(this.mRingMode);
        keyguardUpdateMonitorCallback.onPhoneStateChanged(this.mPhoneState);
        keyguardUpdateMonitorCallback.onRefreshCarrierInfo();
        keyguardUpdateMonitorCallback.onClockVisibilityChanged();
        keyguardUpdateMonitorCallback.onKeyguardVisibilityChangedRaw(this.mKeyguardIsVisible);
        keyguardUpdateMonitorCallback.onTelephonyCapable(this.mTelephonyCapable);
        keyguardUpdateMonitorCallback.onLockScreenModeChanged(this.mLockScreenMode);
        for (Map.Entry<Integer, SimData> entry : this.mSimDatas.entrySet()) {
            SimData value = entry.getValue();
            keyguardUpdateMonitorCallback.onSimStateChanged(value.subId, value.slotId, value.simState);
        }
    }

    public void sendKeyguardReset() {
        this.mHandler.obtainMessage(312).sendToTarget();
    }

    public void sendKeyguardBouncerChanged(boolean z) {
        Message obtainMessage = this.mHandler.obtainMessage(322);
        obtainMessage.arg1 = z ? 1 : 0;
        obtainMessage.sendToTarget();
    }

    public void reportSimUnlocked(int i) {
        Log.v("KeyguardUpdateMonitor", "reportSimUnlocked(subId=" + i + ")");
        handleSimStateChange(i, getSlotId(i), 5);
    }

    public void reportEmergencyCallAction(boolean z) {
        if (!z) {
            this.mHandler.obtainMessage(318).sendToTarget();
            return;
        }
        Assert.isMainThread();
        handleReportEmergencyCallAction();
    }

    public boolean isDeviceProvisioned() {
        return this.mDeviceProvisioned;
    }

    public ServiceState getServiceState(int i) {
        return this.mServiceStates.get(Integer.valueOf(i));
    }

    public void clearBiometricRecognized() {
        Assert.isMainThread();
        this.mUserFingerprintAuthenticated.clear();
        this.mUserFaceAuthenticated.clear();
        this.mTrustManager.clearAllBiometricRecognized(BiometricSourceType.FINGERPRINT);
        this.mTrustManager.clearAllBiometricRecognized(BiometricSourceType.FACE);
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricsCleared();
            }
        }
    }

    public boolean isSimPinVoiceSecure() {
        return isSimPinSecure();
    }

    public boolean isSimPinSecure() {
        for (SubscriptionInfo subscriptionInfo : getSubscriptionInfo(false)) {
            if (isSimPinSecure(getSimState(subscriptionInfo.getSubscriptionId()))) {
                return true;
            }
        }
        return false;
    }

    public int getSimState(int i) {
        if (this.mSimDatas.containsKey(Integer.valueOf(i))) {
            return this.mSimDatas.get(Integer.valueOf(i)).simState;
        }
        return 0;
    }

    private int getSlotId(int i) {
        if (!this.mSimDatas.containsKey(Integer.valueOf(i))) {
            refreshSimState(i, SubscriptionManager.getSlotIndex(i));
        }
        return this.mSimDatas.get(Integer.valueOf(i)).slotId;
    }

    private boolean refreshSimState(int i, int i2) {
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        boolean z = false;
        int simState = telephonyManager != null ? telephonyManager.getSimState(i2) : 0;
        SimData simData = this.mSimDatas.get(Integer.valueOf(i));
        if (simData == null) {
            this.mSimDatas.put(Integer.valueOf(i), new SimData(simState, i2, i));
            return true;
        }
        if (simData.simState != simState) {
            z = true;
        }
        simData.simState = simState;
        return z;
    }

    public void dispatchStartedWakingUp() {
        synchronized (this) {
            this.mDeviceInteractive = true;
        }
        this.mHandler.sendEmptyMessage(319);
    }

    public void dispatchStartedGoingToSleep(int i) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(321, i, 0));
    }

    public void dispatchFinishedGoingToSleep(int i) {
        synchronized (this) {
            this.mDeviceInteractive = false;
        }
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(320, i, 0));
    }

    public void dispatchScreenTurnedOn() {
        synchronized (this) {
            this.mScreenOn = true;
        }
        this.mHandler.sendEmptyMessage(331);
    }

    public void dispatchScreenTurnedOff() {
        synchronized (this) {
            this.mScreenOn = false;
        }
        this.mHandler.sendEmptyMessage(332);
    }

    public void dispatchDreamingStarted() {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(333, 1, 0));
    }

    public void dispatchDreamingStopped() {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(333, 0, 0));
    }

    public void dispatchKeyguardGoingAway(boolean z) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(342, Boolean.valueOf(z)));
    }

    public boolean isDeviceInteractive() {
        return this.mDeviceInteractive;
    }

    public boolean isGoingToSleep() {
        return this.mGoingToSleep;
    }

    public int getNextSubIdForState(int i) {
        List<SubscriptionInfo> subscriptionInfo = getSubscriptionInfo(false);
        int i2 = -1;
        int i3 = Integer.MAX_VALUE;
        for (int i4 = 0; i4 < subscriptionInfo.size(); i4++) {
            int subscriptionId = subscriptionInfo.get(i4).getSubscriptionId();
            int slotId = getSlotId(subscriptionId);
            if (i == getSimState(subscriptionId) && i3 > slotId) {
                i2 = subscriptionId;
                i3 = slotId;
            }
        }
        return i2;
    }

    public SubscriptionInfo getSubscriptionInfoForSubId(int i) {
        List<SubscriptionInfo> subscriptionInfo = getSubscriptionInfo(false);
        for (int i2 = 0; i2 < subscriptionInfo.size(); i2++) {
            SubscriptionInfo subscriptionInfo2 = subscriptionInfo.get(i2);
            if (i == subscriptionInfo2.getSubscriptionId()) {
                return subscriptionInfo2;
            }
        }
        return null;
    }

    public boolean isLogoutEnabled() {
        return this.mLogoutEnabled;
    }

    /* access modifiers changed from: private */
    public void updateLogoutEnabled() {
        Assert.isMainThread();
        boolean isLogoutEnabled = this.mDevicePolicyManager.isLogoutEnabled();
        if (this.mLogoutEnabled != isLogoutEnabled) {
            this.mLogoutEnabled = isLogoutEnabled;
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onLogoutEnabledChanged();
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x03e4  */
    /* JADX WARNING: Removed duplicated region for block: B:60:? A[RETURN, SYNTHETIC] */
    @Override // com.android.systemui.Dumpable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dump(java.io.FileDescriptor r18, java.io.PrintWriter r19, java.lang.String[] r20) {
        /*
        // Method dump skipped, instructions count: 1002
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardUpdateMonitor.dump(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
    }
}
