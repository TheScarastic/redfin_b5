package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.biometrics.BiometricSourceType;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Trace;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.util.LatencyTracker;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.Dumpable;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public class BiometricUnlockController extends KeyguardUpdateMonitorCallback implements Dumpable {
    private static final UiEventLogger UI_EVENT_LOGGER = new UiEventLoggerImpl();
    private final AuthController mAuthController;
    private BiometricModeListener mBiometricModeListener;
    private BiometricSourceType mBiometricType;
    private final Context mContext;
    private final DozeParameters mDozeParameters;
    private DozeScrimController mDozeScrimController;
    private boolean mFadedAwayAfterWakeAndUnlock;
    private final Handler mHandler;
    private boolean mHasScreenTurnedOnSinceAuthenticating;
    private final KeyguardBypassController mKeyguardBypassController;
    private final KeyguardStateController mKeyguardStateController;
    private KeyguardViewController mKeyguardViewController;
    private KeyguardViewMediator mKeyguardViewMediator;
    private final NotificationMediaManager mMediaManager;
    private final MetricsLogger mMetricsLogger;
    private int mMode;
    private final NotificationShadeWindowController mNotificationShadeWindowController;
    private boolean mPendingShowBouncer;
    private final PowerManager mPowerManager;
    private final ScreenLifecycle.Observer mScreenObserver;
    private ScrimController mScrimController;
    private final ShadeController mShadeController;
    private final KeyguardUpdateMonitor mUpdateMonitor;
    private PowerManager.WakeLock mWakeLock;
    private final int mWakeUpDelay;
    @VisibleForTesting
    final WakefulnessLifecycle.Observer mWakefulnessObserver;
    private PendingAuthenticated mPendingAuthenticated = null;
    private final Runnable mReleaseBiometricWakeLockRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.BiometricUnlockController.1
        @Override // java.lang.Runnable
        public void run() {
            Log.i("BiometricUnlockCtrl", "biometric wakelock: TIMEOUT!!");
            BiometricUnlockController.this.releaseBiometricWakeLock();
        }
    };

    /* loaded from: classes.dex */
    public interface BiometricModeListener {
        void notifyBiometricAuthModeChanged();

        void onModeChanged(int i);

        void onResetMode();
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class PendingAuthenticated {
        public final BiometricSourceType biometricSourceType;
        public final boolean isStrongBiometric;
        public final int userId;

        PendingAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            this.userId = i;
            this.biometricSourceType = biometricSourceType;
            this.isStrongBiometric = z;
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum BiometricUiEvent implements UiEventLogger.UiEventEnum {
        BIOMETRIC_FINGERPRINT_SUCCESS(396),
        BIOMETRIC_FINGERPRINT_FAILURE(397),
        BIOMETRIC_FINGERPRINT_ERROR(398),
        BIOMETRIC_FACE_SUCCESS(399),
        BIOMETRIC_FACE_FAILURE(400),
        BIOMETRIC_FACE_ERROR(401),
        BIOMETRIC_IRIS_SUCCESS(402),
        BIOMETRIC_IRIS_FAILURE(403),
        BIOMETRIC_IRIS_ERROR(404);
        
        static final Map<BiometricSourceType, BiometricUiEvent> ERROR_EVENT_BY_SOURCE_TYPE;
        static final Map<BiometricSourceType, BiometricUiEvent> FAILURE_EVENT_BY_SOURCE_TYPE;
        static final Map<BiometricSourceType, BiometricUiEvent> SUCCESS_EVENT_BY_SOURCE_TYPE;
        private final int mId;

        static {
            BiometricUiEvent biometricUiEvent = BIOMETRIC_FINGERPRINT_SUCCESS;
            BiometricUiEvent biometricUiEvent2 = BIOMETRIC_FINGERPRINT_FAILURE;
            BiometricUiEvent biometricUiEvent3 = BIOMETRIC_FINGERPRINT_ERROR;
            BiometricUiEvent biometricUiEvent4 = BIOMETRIC_FACE_SUCCESS;
            BiometricUiEvent biometricUiEvent5 = BIOMETRIC_FACE_FAILURE;
            BiometricUiEvent biometricUiEvent6 = BIOMETRIC_FACE_ERROR;
            BiometricUiEvent biometricUiEvent7 = BIOMETRIC_IRIS_SUCCESS;
            BiometricUiEvent biometricUiEvent8 = BIOMETRIC_IRIS_FAILURE;
            ERROR_EVENT_BY_SOURCE_TYPE = Map.of(BiometricSourceType.FINGERPRINT, biometricUiEvent3, BiometricSourceType.FACE, biometricUiEvent6, BiometricSourceType.IRIS, BIOMETRIC_IRIS_ERROR);
            SUCCESS_EVENT_BY_SOURCE_TYPE = Map.of(BiometricSourceType.FINGERPRINT, biometricUiEvent, BiometricSourceType.FACE, biometricUiEvent4, BiometricSourceType.IRIS, biometricUiEvent7);
            FAILURE_EVENT_BY_SOURCE_TYPE = Map.of(BiometricSourceType.FINGERPRINT, biometricUiEvent2, BiometricSourceType.FACE, biometricUiEvent5, BiometricSourceType.IRIS, biometricUiEvent8);
        }

        BiometricUiEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public BiometricUnlockController(Context context, DozeScrimController dozeScrimController, KeyguardViewMediator keyguardViewMediator, ScrimController scrimController, ShadeController shadeController, NotificationShadeWindowController notificationShadeWindowController, KeyguardStateController keyguardStateController, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor, Resources resources, KeyguardBypassController keyguardBypassController, DozeParameters dozeParameters, MetricsLogger metricsLogger, DumpManager dumpManager, PowerManager powerManager, NotificationMediaManager notificationMediaManager, WakefulnessLifecycle wakefulnessLifecycle, ScreenLifecycle screenLifecycle, AuthController authController) {
        AnonymousClass3 r3 = new WakefulnessLifecycle.Observer() { // from class: com.android.systemui.statusbar.phone.BiometricUnlockController.3
            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onFinishedWakingUp() {
                if (BiometricUnlockController.this.mPendingShowBouncer) {
                    BiometricUnlockController.this.showBouncer();
                }
            }
        };
        this.mWakefulnessObserver = r3;
        AnonymousClass4 r4 = new ScreenLifecycle.Observer() { // from class: com.android.systemui.statusbar.phone.BiometricUnlockController.4
            @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
            public void onScreenTurnedOn() {
                BiometricUnlockController.this.mHasScreenTurnedOnSinceAuthenticating = true;
            }
        };
        this.mScreenObserver = r4;
        this.mContext = context;
        this.mPowerManager = powerManager;
        this.mShadeController = shadeController;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mDozeParameters = dozeParameters;
        keyguardUpdateMonitor.registerCallback(this);
        this.mMediaManager = notificationMediaManager;
        wakefulnessLifecycle.addObserver(r3);
        screenLifecycle.addObserver(r4);
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mDozeScrimController = dozeScrimController;
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mScrimController = scrimController;
        this.mKeyguardStateController = keyguardStateController;
        this.mHandler = handler;
        this.mWakeUpDelay = resources.getInteger(17694942);
        this.mKeyguardBypassController = keyguardBypassController;
        keyguardBypassController.setUnlockController(this);
        this.mMetricsLogger = metricsLogger;
        this.mAuthController = authController;
        dumpManager.registerDumpable(BiometricUnlockController.class.getName(), this);
    }

    public void setKeyguardViewController(KeyguardViewController keyguardViewController) {
        this.mKeyguardViewController = keyguardViewController;
    }

    public void setBiometricModeListener(BiometricModeListener biometricModeListener) {
        this.mBiometricModeListener = biometricModeListener;
    }

    /* access modifiers changed from: private */
    public void releaseBiometricWakeLock() {
        if (this.mWakeLock != null) {
            this.mHandler.removeCallbacks(this.mReleaseBiometricWakeLockRunnable);
            Log.i("BiometricUnlockCtrl", "releasing biometric wakelock");
            this.mWakeLock.release();
            this.mWakeLock = null;
        }
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public void onBiometricAcquired(BiometricSourceType biometricSourceType) {
        Trace.beginSection("BiometricUnlockController#onBiometricAcquired");
        releaseBiometricWakeLock();
        if (!this.mUpdateMonitor.isDeviceInteractive()) {
            if (LatencyTracker.isEnabled(this.mContext)) {
                int i = 2;
                if (biometricSourceType == BiometricSourceType.FACE) {
                    i = 7;
                }
                LatencyTracker.getInstance(this.mContext).onActionStart(i);
            }
            this.mWakeLock = this.mPowerManager.newWakeLock(1, "wake-and-unlock:wakelock");
            Trace.beginSection("acquiring wake-and-unlock");
            this.mWakeLock.acquire();
            Trace.endSection();
            Log.i("BiometricUnlockCtrl", "biometric acquired, grabbing biometric wakelock");
            this.mHandler.postDelayed(this.mReleaseBiometricWakeLockRunnable, 15000);
        }
        Trace.endSection();
    }

    private boolean pulsingOrAod() {
        ScrimState state = this.mScrimController.getState();
        return state == ScrimState.AOD || state == ScrimState.PULSING;
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
        Trace.beginSection("BiometricUnlockController#onBiometricAuthenticated");
        if (this.mUpdateMonitor.isGoingToSleep()) {
            this.mPendingAuthenticated = new PendingAuthenticated(i, biometricSourceType, z);
            Trace.endSection();
            return;
        }
        this.mBiometricType = biometricSourceType;
        this.mMetricsLogger.write(new LogMaker(1697).setType(10).setSubtype(toSubtype(biometricSourceType)));
        Optional ofNullable = Optional.ofNullable(BiometricUiEvent.SUCCESS_EVENT_BY_SOURCE_TYPE.get(biometricSourceType));
        UiEventLogger uiEventLogger = UI_EVENT_LOGGER;
        Objects.requireNonNull(uiEventLogger);
        ofNullable.ifPresent(new BiometricUnlockController$$ExternalSyntheticLambda2(uiEventLogger));
        if (this.mKeyguardStateController.isOccluded() || this.mKeyguardBypassController.onBiometricAuthenticated(biometricSourceType, z)) {
            this.mKeyguardViewMediator.userActivity();
            startWakeAndUnlock(biometricSourceType, z);
            return;
        }
        Log.d("BiometricUnlockCtrl", "onBiometricAuthenticated aborted by bypass controller");
    }

    public void startWakeAndUnlock(BiometricSourceType biometricSourceType, boolean z) {
        startWakeAndUnlock(calculateMode(biometricSourceType, z));
    }

    public void startWakeAndUnlock(int i) {
        Log.v("BiometricUnlockCtrl", "startWakeAndUnlock(" + i + ")");
        boolean isDeviceInteractive = this.mUpdateMonitor.isDeviceInteractive();
        this.mMode = i;
        this.mHasScreenTurnedOnSinceAuthenticating = false;
        if (i == 2 && pulsingOrAod()) {
            this.mNotificationShadeWindowController.setForceDozeBrightness(true);
        }
        boolean z = i == 1 && this.mDozeParameters.getAlwaysOn() && this.mWakeUpDelay > 0;
        BiometricUnlockController$$ExternalSyntheticLambda1 biometricUnlockController$$ExternalSyntheticLambda1 = new Runnable(isDeviceInteractive, z) { // from class: com.android.systemui.statusbar.phone.BiometricUnlockController$$ExternalSyntheticLambda1
            public final /* synthetic */ boolean f$1;
            public final /* synthetic */ boolean f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                BiometricUnlockController.this.lambda$startWakeAndUnlock$0(this.f$1, this.f$2);
            }
        };
        if (!z && this.mMode != 0) {
            biometricUnlockController$$ExternalSyntheticLambda1.run();
        }
        int i2 = this.mMode;
        switch (i2) {
            case 1:
            case 2:
            case 6:
                if (i2 == 2) {
                    Trace.beginSection("MODE_WAKE_AND_UNLOCK_PULSING");
                    this.mMediaManager.updateMediaMetaData(false, true);
                } else if (i2 == 1) {
                    Trace.beginSection("MODE_WAKE_AND_UNLOCK");
                } else {
                    Trace.beginSection("MODE_WAKE_AND_UNLOCK_FROM_DREAM");
                    this.mUpdateMonitor.awakenFromDream();
                }
                this.mNotificationShadeWindowController.setNotificationShadeFocusable(false);
                if (z) {
                    this.mHandler.postDelayed(biometricUnlockController$$ExternalSyntheticLambda1, (long) this.mWakeUpDelay);
                } else {
                    this.mKeyguardViewMediator.onWakeAndUnlocking();
                }
                Trace.endSection();
                break;
            case 3:
                Trace.beginSection("MODE_SHOW_BOUNCER");
                if (!isDeviceInteractive) {
                    this.mPendingShowBouncer = true;
                } else {
                    showBouncer();
                }
                Trace.endSection();
                break;
            case 5:
                Trace.beginSection("MODE_UNLOCK_COLLAPSING");
                if (!isDeviceInteractive) {
                    this.mPendingShowBouncer = true;
                } else {
                    this.mPendingShowBouncer = false;
                    this.mKeyguardViewController.notifyKeyguardAuthenticated(false);
                }
                Trace.endSection();
                break;
            case 7:
            case 8:
                Trace.beginSection("MODE_DISMISS_BOUNCER or MODE_UNLOCK_FADING");
                this.mKeyguardViewController.notifyKeyguardAuthenticated(false);
                Trace.endSection();
                break;
        }
        onModeChanged(this.mMode);
        BiometricModeListener biometricModeListener = this.mBiometricModeListener;
        if (biometricModeListener != null) {
            biometricModeListener.notifyBiometricAuthModeChanged();
        }
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startWakeAndUnlock$0(boolean z, boolean z2) {
        if (!z) {
            Log.i("BiometricUnlockCtrl", "bio wakelock: Authenticated, waking up...");
            this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), 4, "android.policy:BIOMETRIC");
        }
        if (z2) {
            this.mKeyguardViewMediator.onWakeAndUnlocking();
        }
        Trace.beginSection("release wake-and-unlock");
        releaseBiometricWakeLock();
        Trace.endSection();
    }

    private void onModeChanged(int i) {
        BiometricModeListener biometricModeListener = this.mBiometricModeListener;
        if (biometricModeListener != null) {
            biometricModeListener.onModeChanged(i);
        }
    }

    /* access modifiers changed from: private */
    public void showBouncer() {
        if (this.mMode == 3) {
            this.mKeyguardViewController.showBouncer(false);
        }
        this.mShadeController.animateCollapsePanels(0, true, false, 1.1f);
        this.mPendingShowBouncer = false;
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public void onStartedGoingToSleep(int i) {
        resetMode();
        this.mFadedAwayAfterWakeAndUnlock = false;
        this.mPendingAuthenticated = null;
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public void onFinishedGoingToSleep(int i) {
        Trace.beginSection("BiometricUnlockController#onFinishedGoingToSleep");
        PendingAuthenticated pendingAuthenticated = this.mPendingAuthenticated;
        if (pendingAuthenticated != null) {
            this.mHandler.post(new Runnable(pendingAuthenticated) { // from class: com.android.systemui.statusbar.phone.BiometricUnlockController$$ExternalSyntheticLambda0
                public final /* synthetic */ BiometricUnlockController.PendingAuthenticated f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    BiometricUnlockController.this.lambda$onFinishedGoingToSleep$1(this.f$1);
                }
            });
            this.mPendingAuthenticated = null;
        }
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishedGoingToSleep$1(PendingAuthenticated pendingAuthenticated) {
        onBiometricAuthenticated(pendingAuthenticated.userId, pendingAuthenticated.biometricSourceType, pendingAuthenticated.isStrongBiometric);
    }

    public boolean hasPendingAuthentication() {
        PendingAuthenticated pendingAuthenticated = this.mPendingAuthenticated;
        return pendingAuthenticated != null && this.mUpdateMonitor.isUnlockingWithBiometricAllowed(pendingAuthenticated.isStrongBiometric) && this.mPendingAuthenticated.userId == KeyguardUpdateMonitor.getCurrentUser();
    }

    public int getMode() {
        return this.mMode;
    }

    private int calculateMode(BiometricSourceType biometricSourceType, boolean z) {
        if (biometricSourceType == BiometricSourceType.FACE || biometricSourceType == BiometricSourceType.IRIS) {
            return calculateModeForPassiveAuth(z);
        }
        return calculateModeForFingerprint(z);
    }

    private int calculateModeForFingerprint(boolean z) {
        boolean isUnlockingWithBiometricAllowed = this.mUpdateMonitor.isUnlockingWithBiometricAllowed(z);
        boolean isDreaming = this.mUpdateMonitor.isDreaming();
        if (!this.mUpdateMonitor.isDeviceInteractive()) {
            if (!this.mKeyguardViewController.isShowing()) {
                return 4;
            }
            if (this.mDozeScrimController.isPulsing() && isUnlockingWithBiometricAllowed) {
                return 2;
            }
            if (isUnlockingWithBiometricAllowed || !this.mKeyguardStateController.isMethodSecure()) {
                return 1;
            }
            return 3;
        } else if (isUnlockingWithBiometricAllowed && isDreaming) {
            return 6;
        } else {
            if (!this.mKeyguardViewController.isShowing()) {
                return 0;
            }
            if (this.mKeyguardViewController.bouncerIsOrWillBeShowing() && isUnlockingWithBiometricAllowed) {
                return 8;
            }
            if (isUnlockingWithBiometricAllowed) {
                return 5;
            }
            if (!this.mKeyguardViewController.isBouncerShowing()) {
                return 3;
            }
            return 0;
        }
    }

    private int calculateModeForPassiveAuth(boolean z) {
        boolean isUnlockingWithBiometricAllowed = this.mUpdateMonitor.isUnlockingWithBiometricAllowed(z);
        boolean isDreaming = this.mUpdateMonitor.isDreaming();
        boolean z2 = this.mKeyguardBypassController.getBypassEnabled() || this.mKeyguardBypassController.getUserHasDeviceEntryIntent();
        if (!this.mUpdateMonitor.isDeviceInteractive()) {
            if (!this.mKeyguardViewController.isShowing()) {
                if (z2) {
                    return 1;
                }
                return 4;
            } else if (!isUnlockingWithBiometricAllowed) {
                if (z2) {
                    return 3;
                }
                return 0;
            } else if (this.mDozeScrimController.isPulsing()) {
                if (z2) {
                    return 2;
                }
                return 4;
            } else if (z2) {
                return 2;
            } else {
                return 4;
            }
        } else if (!isUnlockingWithBiometricAllowed || !isDreaming) {
            if (isUnlockingWithBiometricAllowed && this.mKeyguardStateController.isOccluded()) {
                return 5;
            }
            if (!this.mKeyguardViewController.isShowing()) {
                return 0;
            }
            if ((this.mKeyguardViewController.bouncerIsOrWillBeShowing() || this.mKeyguardBypassController.getAltBouncerShowing()) && isUnlockingWithBiometricAllowed) {
                if (!z2 || !this.mKeyguardBypassController.canPlaySubtleWindowAnimations()) {
                    return 8;
                }
                return 7;
            } else if (isUnlockingWithBiometricAllowed) {
                if (z2 || this.mAuthController.isUdfpsFingerDown()) {
                    return 7;
                }
                return 0;
            } else if (z2) {
                return 3;
            } else {
                return 0;
            }
        } else if (z2) {
            return 6;
        } else {
            return 4;
        }
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
        this.mMetricsLogger.write(new LogMaker(1697).setType(11).setSubtype(toSubtype(biometricSourceType)));
        Optional ofNullable = Optional.ofNullable(BiometricUiEvent.FAILURE_EVENT_BY_SOURCE_TYPE.get(biometricSourceType));
        UiEventLogger uiEventLogger = UI_EVENT_LOGGER;
        Objects.requireNonNull(uiEventLogger);
        ofNullable.ifPresent(new BiometricUnlockController$$ExternalSyntheticLambda2(uiEventLogger));
        cleanup();
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public void onBiometricError(int i, String str, BiometricSourceType biometricSourceType) {
        this.mMetricsLogger.write(new LogMaker(1697).setType(15).setSubtype(toSubtype(biometricSourceType)).addTaggedData(1741, Integer.valueOf(i)));
        Optional ofNullable = Optional.ofNullable(BiometricUiEvent.ERROR_EVENT_BY_SOURCE_TYPE.get(biometricSourceType));
        UiEventLogger uiEventLogger = UI_EVENT_LOGGER;
        Objects.requireNonNull(uiEventLogger);
        ofNullable.ifPresent(new BiometricUnlockController$$ExternalSyntheticLambda2(uiEventLogger));
        cleanup();
    }

    private void cleanup() {
        releaseBiometricWakeLock();
    }

    public void startKeyguardFadingAway() {
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.statusbar.phone.BiometricUnlockController.2
            @Override // java.lang.Runnable
            public void run() {
                BiometricUnlockController.this.mNotificationShadeWindowController.setForceDozeBrightness(false);
            }
        }, 96);
    }

    public void finishKeyguardFadingAway() {
        if (isWakeAndUnlock()) {
            this.mFadedAwayAfterWakeAndUnlock = true;
        }
        resetMode();
    }

    private void resetMode() {
        this.mMode = 0;
        this.mBiometricType = null;
        this.mNotificationShadeWindowController.setForceDozeBrightness(false);
        BiometricModeListener biometricModeListener = this.mBiometricModeListener;
        if (biometricModeListener != null) {
            biometricModeListener.onResetMode();
            this.mBiometricModeListener.notifyBiometricAuthModeChanged();
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(" BiometricUnlockController:");
        printWriter.print("   mMode=");
        printWriter.println(this.mMode);
        printWriter.print("   mWakeLock=");
        printWriter.println(this.mWakeLock);
    }

    public boolean isWakeAndUnlock() {
        int i = this.mMode;
        return i == 1 || i == 2 || i == 6;
    }

    public boolean unlockedByWakeAndUnlock() {
        return isWakeAndUnlock() || this.mFadedAwayAfterWakeAndUnlock;
    }

    public boolean isBiometricUnlock() {
        int i;
        return isWakeAndUnlock() || (i = this.mMode) == 5 || i == 7;
    }

    public BiometricSourceType getBiometricType() {
        return this.mBiometricType;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.BiometricUnlockController$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$android$hardware$biometrics$BiometricSourceType;

        static {
            int[] iArr = new int[BiometricSourceType.values().length];
            $SwitchMap$android$hardware$biometrics$BiometricSourceType = iArr;
            try {
                iArr[BiometricSourceType.FINGERPRINT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$hardware$biometrics$BiometricSourceType[BiometricSourceType.FACE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$hardware$biometrics$BiometricSourceType[BiometricSourceType.IRIS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    private int toSubtype(BiometricSourceType biometricSourceType) {
        int i = AnonymousClass5.$SwitchMap$android$hardware$biometrics$BiometricSourceType[biometricSourceType.ordinal()];
        if (i == 1) {
            return 0;
        }
        if (i != 2) {
            return i != 3 ? 3 : 2;
        }
        return 1;
    }
}
