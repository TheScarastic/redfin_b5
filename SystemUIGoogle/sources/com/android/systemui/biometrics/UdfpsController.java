package com.android.systemui.biometrics;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.hardware.fingerprint.IUdfpsOverlayControllerCallback;
import android.media.AudioAttributes;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.R$layout;
import com.android.systemui.doze.DozeReceiver;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import java.util.Optional;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* loaded from: classes.dex */
public class UdfpsController implements DozeReceiver {
    private final AccessibilityManager mAccessibilityManager;
    private Runnable mAodInterruptRunnable;
    private boolean mAttemptedToDismissKeyguard;
    private final BroadcastReceiver mBroadcastReceiver;
    private Runnable mCancelAodTimeoutAction;
    private final ConfigurationController mConfigurationController;
    private final Context mContext;
    private final WindowManager.LayoutParams mCoreLayoutParams;
    private final DumpManager mDumpManager;
    private final Execution mExecution;
    private final FalsingManager mFalsingManager;
    private final DelayableExecutor mFgExecutor;
    private final FingerprintManager mFingerprintManager;
    private boolean mGoodCaptureReceived;
    private final UdfpsHbmProvider mHbmProvider;
    private final LayoutInflater mInflater;
    private boolean mIsAodInterruptActive;
    private final KeyguardBypassController mKeyguardBypassController;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final StatusBarKeyguardViewManager mKeyguardViewManager;
    private final KeyguardViewMediator mKeyguardViewMediator;
    private final LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    private boolean mOnFingerDown;
    @VisibleForTesting
    final BiometricOrientationEventListener mOrientationListener;
    private final PowerManager mPowerManager;
    private final ScreenLifecycle.Observer mScreenObserver;
    private boolean mScreenOn;
    @VisibleForTesting
    final FingerprintSensorPropertiesInternal mSensorProps;
    ServerRequest mServerRequest;
    private final StatusBar mStatusBar;
    private final StatusBarStateController mStatusBarStateController;
    private long mTouchLogTime;
    private VelocityTracker mVelocityTracker;
    private final Vibrator mVibrator;
    private UdfpsView mView;
    private final WindowManager mWindowManager;
    @VisibleForTesting
    public static final AudioAttributes VIBRATION_SONIFICATION_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    public static final VibrationEffect EFFECT_CLICK = VibrationEffect.get(0);
    private int mActivePointerId = -1;
    @SuppressLint({"ClickableViewAccessibility"})
    private final View.OnTouchListener mOnTouchListener = new View.OnTouchListener() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda1
        @Override // android.view.View.OnTouchListener
        public final boolean onTouch(View view, MotionEvent motionEvent) {
            return UdfpsController.this.lambda$new$0(view, motionEvent);
        }
    };
    @SuppressLint({"ClickableViewAccessibility"})
    private final View.OnHoverListener mOnHoverListener = new View.OnHoverListener() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda0
        @Override // android.view.View.OnHoverListener
        public final boolean onHover(View view, MotionEvent motionEvent) {
            return UdfpsController.this.lambda$new$1(view, motionEvent);
        }
    };
    private final AccessibilityManager.TouchExplorationStateChangeListener mTouchExplorationStateChangeListener = new AccessibilityManager.TouchExplorationStateChangeListener() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda2
        @Override // android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener
        public final void onTouchExplorationStateChanged(boolean z) {
            UdfpsController.this.lambda$new$2(z);
        }
    };
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());

    private int getCoreLayoutParamFlags() {
        return 16777512;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ServerRequest {
        final IUdfpsOverlayControllerCallback mCallback;
        final UdfpsEnrollHelper mEnrollHelper;
        final int mRequestReason;

        ServerRequest(int i, IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback, UdfpsEnrollHelper udfpsEnrollHelper) {
            this.mRequestReason = i;
            this.mCallback = iUdfpsOverlayControllerCallback;
            this.mEnrollHelper = udfpsEnrollHelper;
        }

        void onEnrollmentProgress(int i) {
            UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
            if (udfpsEnrollHelper != null) {
                udfpsEnrollHelper.onEnrollmentProgress(i);
            }
        }

        void onAcquiredGood() {
            UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
            if (udfpsEnrollHelper != null) {
                udfpsEnrollHelper.animateIfLastStep();
            }
        }

        void onEnrollmentHelp() {
            UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
            if (udfpsEnrollHelper != null) {
                udfpsEnrollHelper.onEnrollmentHelp();
            }
        }

        void onUserCanceled() {
            try {
                this.mCallback.onUserCanceled();
            } catch (RemoteException e) {
                Log.e("UdfpsController", "Remote exception", e);
            }
        }
    }

    /* loaded from: classes.dex */
    public class UdfpsOverlayController extends IUdfpsOverlayController.Stub {
        public UdfpsOverlayController() {
        }

        public void showUdfpsOverlay(int i, int i2, IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4(this, i2, iUdfpsOverlayControllerCallback));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$showUdfpsOverlay$0(int i, IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback) {
            UdfpsController.this.mServerRequest = new ServerRequest(i, iUdfpsOverlayControllerCallback, (i == 1 || i == 2) ? new UdfpsEnrollHelper(UdfpsController.this.mContext, i) : null);
            UdfpsController.this.updateOverlay();
        }

        public void hideUdfpsOverlay(int i) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$hideUdfpsOverlay$1() {
            UdfpsController udfpsController = UdfpsController.this;
            udfpsController.mServerRequest = null;
            udfpsController.updateOverlay();
        }

        public void onAcquiredGood(int i) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda2(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAcquiredGood$2(int i) {
            if (UdfpsController.this.mView == null) {
                Log.e("UdfpsController", "Null view when onAcquiredGood for sensorId: " + i);
                return;
            }
            UdfpsController.this.mGoodCaptureReceived = true;
            UdfpsController.this.mView.stopIllumination();
            ServerRequest serverRequest = UdfpsController.this.mServerRequest;
            if (serverRequest != null) {
                serverRequest.onAcquiredGood();
            } else {
                Log.e("UdfpsController", "Null serverRequest when onAcquiredGood");
            }
        }

        public void onEnrollmentProgress(int i, int i2) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda3(this, i2));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onEnrollmentProgress$3(int i) {
            ServerRequest serverRequest = UdfpsController.this.mServerRequest;
            if (serverRequest == null) {
                Log.e("UdfpsController", "onEnrollProgress received but serverRequest is null");
            } else {
                serverRequest.onEnrollmentProgress(i);
            }
        }

        public void onEnrollmentHelp(int i) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onEnrollmentHelp$4() {
            ServerRequest serverRequest = UdfpsController.this.mServerRequest;
            if (serverRequest == null) {
                Log.e("UdfpsController", "onEnrollmentHelp received but serverRequest is null");
            } else {
                serverRequest.onEnrollmentHelp();
            }
        }

        public void setDebugMessage(int i, String str) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda5(this, str));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setDebugMessage$5(String str) {
            if (UdfpsController.this.mView != null) {
                UdfpsController.this.mView.setDebugMessage(str);
            }
        }
    }

    private static float computePointerSpeed(VelocityTracker velocityTracker, int i) {
        return (float) Math.sqrt(Math.pow((double) velocityTracker.getXVelocity(i), 2.0d) + Math.pow((double) velocityTracker.getYVelocity(i), 2.0d));
    }

    public boolean onTouch(MotionEvent motionEvent) {
        UdfpsView udfpsView = this.mView;
        if (udfpsView == null) {
            return false;
        }
        return onTouch(udfpsView, motionEvent, false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(View view, MotionEvent motionEvent) {
        return onTouch(view, motionEvent, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1(View view, MotionEvent motionEvent) {
        return onTouch(view, motionEvent, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(boolean z) {
        updateTouchListener();
    }

    private boolean isWithinSensorArea(UdfpsView udfpsView, float f, float f2, boolean z) {
        if (z) {
            return udfpsView.isWithinSensorArea(f, f2);
        }
        UdfpsView udfpsView2 = this.mView;
        if (udfpsView2 == null || udfpsView2.getAnimationViewController() == null || this.mView.getAnimationViewController().shouldPauseAuth() || !getSensorLocation().contains(f, f2)) {
            return false;
        }
        return true;
    }

    private boolean onTouch(View view, MotionEvent motionEvent, boolean z) {
        boolean z2;
        int i;
        UdfpsView udfpsView = (UdfpsView) view;
        boolean isIlluminationRequested = udfpsView.isIlluminationRequested();
        int actionMasked = motionEvent.getActionMasked();
        boolean z3 = true;
        boolean z4 = false;
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked == 4) {
                            udfpsView.onTouchOutsideView();
                            return true;
                        } else if (actionMasked != 7) {
                            if (actionMasked != 9) {
                                if (actionMasked != 10) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                Trace.beginSection("UdfpsController.onTouch.ACTION_MOVE");
                int i2 = this.mActivePointerId;
                if (i2 == -1) {
                    i = motionEvent.getPointerId(0);
                } else {
                    i = motionEvent.findPointerIndex(i2);
                }
                if (i == motionEvent.getActionIndex()) {
                    boolean isWithinSensorArea = isWithinSensorArea(udfpsView, motionEvent.getX(i), motionEvent.getY(i), z);
                    if ((z || isWithinSensorArea) && shouldTryToDismissKeyguard()) {
                        Log.v("UdfpsController", "onTouch | dismiss keyguard ACTION_MOVE");
                        if (!this.mOnFingerDown) {
                            playStartHaptic();
                        }
                        this.mKeyguardViewManager.notifyKeyguardAuthenticated(false);
                        this.mAttemptedToDismissKeyguard = true;
                        return false;
                    } else if (isWithinSensorArea) {
                        if (this.mVelocityTracker == null) {
                            this.mVelocityTracker = VelocityTracker.obtain();
                        }
                        this.mVelocityTracker.addMovement(motionEvent);
                        this.mVelocityTracker.computeCurrentVelocity(1000);
                        float computePointerSpeed = computePointerSpeed(this.mVelocityTracker, this.mActivePointerId);
                        float touchMinor = motionEvent.getTouchMinor(i);
                        float touchMajor = motionEvent.getTouchMajor(i);
                        boolean z5 = computePointerSpeed > 750.0f;
                        String format = String.format("minor: %.1f, major: %.1f, v: %.1f, exceedsVelocityThreshold: %b", Float.valueOf(touchMinor), Float.valueOf(touchMajor), Float.valueOf(computePointerSpeed), Boolean.valueOf(z5));
                        long elapsedRealtime = SystemClock.elapsedRealtime() - this.mTouchLogTime;
                        if (isIlluminationRequested || this.mGoodCaptureReceived || z5) {
                            if (elapsedRealtime >= 50) {
                                Log.v("UdfpsController", "onTouch | finger move: " + format);
                                this.mTouchLogTime = SystemClock.elapsedRealtime();
                            }
                            z3 = false;
                        } else {
                            onFingerDown((int) motionEvent.getRawX(), (int) motionEvent.getRawY(), touchMinor, touchMajor);
                            Log.v("UdfpsController", "onTouch | finger down: " + format);
                            this.mTouchLogTime = SystemClock.elapsedRealtime();
                            this.mPowerManager.userActivity(SystemClock.uptimeMillis(), 2, 0);
                        }
                        z4 = z3;
                    } else {
                        Log.v("UdfpsController", "onTouch | finger outside");
                        onFingerUp();
                    }
                }
                Trace.endSection();
                return z4;
            }
            Trace.beginSection("UdfpsController.onTouch.ACTION_UP");
            this.mActivePointerId = -1;
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.mVelocityTracker = null;
            }
            Log.v("UdfpsController", "onTouch | finger up");
            this.mAttemptedToDismissKeyguard = false;
            onFingerUp();
            this.mFalsingManager.isFalseTouch(13);
            Trace.endSection();
            return false;
        }
        Trace.beginSection("UdfpsController.onTouch.ACTION_DOWN");
        VelocityTracker velocityTracker2 = this.mVelocityTracker;
        if (velocityTracker2 == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker2.clear();
        }
        boolean isWithinSensorArea2 = isWithinSensorArea(udfpsView, motionEvent.getX(), motionEvent.getY(), z);
        if (isWithinSensorArea2) {
            Trace.beginAsyncSection("UdfpsController.e2e.onPointerDown", 0);
            Log.v("UdfpsController", "onTouch | action down");
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.mVelocityTracker.addMovement(motionEvent);
            z2 = true;
        } else {
            z2 = false;
        }
        if ((isWithinSensorArea2 || z) && shouldTryToDismissKeyguard()) {
            Log.v("UdfpsController", "onTouch | dismiss keyguard ACTION_DOWN");
            if (!this.mOnFingerDown) {
                playStartHaptic();
            }
            this.mKeyguardViewManager.notifyKeyguardAuthenticated(false);
            this.mAttemptedToDismissKeyguard = true;
        }
        Trace.endSection();
        return z2;
    }

    private boolean shouldTryToDismissKeyguard() {
        return this.mView.getAnimationViewController() != null && (this.mView.getAnimationViewController() instanceof UdfpsKeyguardViewController) && this.mKeyguardStateController.canDismissLockScreen() && !this.mAttemptedToDismissKeyguard;
    }

    public UdfpsController(Context context, Execution execution, LayoutInflater layoutInflater, FingerprintManager fingerprintManager, WindowManager windowManager, StatusBarStateController statusBarStateController, DelayableExecutor delayableExecutor, StatusBar statusBar, StatusBarKeyguardViewManager statusBarKeyguardViewManager, DumpManager dumpManager, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardViewMediator keyguardViewMediator, FalsingManager falsingManager, PowerManager powerManager, AccessibilityManager accessibilityManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, ScreenLifecycle screenLifecycle, Vibrator vibrator, UdfpsHapticsSimulator udfpsHapticsSimulator, Optional<UdfpsHbmProvider> optional, KeyguardStateController keyguardStateController, KeyguardBypassController keyguardBypassController, DisplayManager displayManager, Handler handler, ConfigurationController configurationController) {
        AnonymousClass1 r2 = new ScreenLifecycle.Observer() { // from class: com.android.systemui.biometrics.UdfpsController.1
            @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
            public void onScreenTurnedOn() {
                UdfpsController.this.mScreenOn = true;
                if (UdfpsController.this.mAodInterruptRunnable != null) {
                    UdfpsController.this.mAodInterruptRunnable.run();
                    UdfpsController.this.mAodInterruptRunnable = null;
                }
            }

            @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
            public void onScreenTurnedOff() {
                UdfpsController.this.mScreenOn = false;
            }
        };
        this.mScreenObserver = r2;
        AnonymousClass2 r3 = new BroadcastReceiver() { // from class: com.android.systemui.biometrics.UdfpsController.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                ServerRequest serverRequest = UdfpsController.this.mServerRequest;
                if (serverRequest != null && serverRequest.mRequestReason != 4 && "android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                    Log.d("UdfpsController", "ACTION_CLOSE_SYSTEM_DIALOGS received, mRequestReason: " + UdfpsController.this.mServerRequest.mRequestReason);
                    UdfpsController.this.mServerRequest.onUserCanceled();
                    UdfpsController udfpsController = UdfpsController.this;
                    udfpsController.mServerRequest = null;
                    udfpsController.updateOverlay();
                }
            }
        };
        this.mBroadcastReceiver = r3;
        this.mContext = context;
        this.mExecution = execution;
        this.mVibrator = vibrator;
        this.mInflater = layoutInflater;
        FingerprintManager fingerprintManager2 = (FingerprintManager) Preconditions.checkNotNull(fingerprintManager);
        this.mFingerprintManager = fingerprintManager2;
        this.mWindowManager = windowManager;
        this.mFgExecutor = delayableExecutor;
        this.mStatusBar = statusBar;
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardViewManager = statusBarKeyguardViewManager;
        this.mDumpManager = dumpManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mFalsingManager = falsingManager;
        this.mPowerManager = powerManager;
        this.mAccessibilityManager = accessibilityManager;
        this.mLockscreenShadeTransitionController = lockscreenShadeTransitionController;
        this.mHbmProvider = optional.orElse(null);
        screenLifecycle.addObserver(r2);
        boolean z = true;
        this.mScreenOn = screenLifecycle.getScreenState() == 2;
        this.mOrientationListener = new BiometricOrientationEventListener(context, new Function0() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return UdfpsController.this.lambda$new$3();
            }
        }, displayManager, handler);
        this.mKeyguardBypassController = keyguardBypassController;
        this.mConfigurationController = configurationController;
        FingerprintSensorPropertiesInternal findFirstUdfps = findFirstUdfps();
        this.mSensorProps = findFirstUdfps;
        Preconditions.checkArgument(findFirstUdfps == null ? false : z);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(2009, getCoreLayoutParamFlags(), -3);
        this.mCoreLayoutParams = layoutParams;
        layoutParams.setTitle("UdfpsController");
        layoutParams.setFitInsetsTypes(0);
        layoutParams.gravity = 51;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.privateFlags = 536870912;
        fingerprintManager2.setUdfpsOverlayController(new UdfpsOverlayController());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        context.registerReceiver(r3, intentFilter);
        udfpsHapticsSimulator.setUdfpsController(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$new$3() {
        onOrientationChanged();
        return Unit.INSTANCE;
    }

    @VisibleForTesting
    public void playStartHaptic() {
        Vibrator vibrator = this.mVibrator;
        if (vibrator != null) {
            vibrator.vibrate(Process.myUid(), this.mContext.getOpPackageName(), EFFECT_CLICK, "udfps-onStart", VIBRATION_SONIFICATION_ATTRIBUTES);
        }
    }

    private FingerprintSensorPropertiesInternal findFirstUdfps() {
        for (FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal : this.mFingerprintManager.getSensorPropertiesInternal()) {
            if (fingerprintSensorPropertiesInternal.isAnyUdfpsType()) {
                return fingerprintSensorPropertiesInternal;
            }
        }
        return null;
    }

    @Override // com.android.systemui.doze.DozeReceiver
    public void dozeTimeTick() {
        UdfpsView udfpsView = this.mView;
        if (udfpsView != null) {
            udfpsView.dozeTimeTick();
        }
    }

    public RectF getSensorLocation() {
        FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal = this.mSensorProps;
        int i = fingerprintSensorPropertiesInternal.sensorLocationX;
        int i2 = fingerprintSensorPropertiesInternal.sensorRadius;
        int i3 = fingerprintSensorPropertiesInternal.sensorLocationY;
        return new RectF((float) (i - i2), (float) (i3 - i2), (float) (i + i2), (float) (i3 + i2));
    }

    /* access modifiers changed from: private */
    public void updateOverlay() {
        this.mExecution.assertIsMainThread();
        ServerRequest serverRequest = this.mServerRequest;
        if (serverRequest != null) {
            showUdfpsOverlay(serverRequest);
        } else {
            hideUdfpsOverlay();
        }
    }

    private WindowManager.LayoutParams computeLayoutParams(UdfpsAnimationViewController udfpsAnimationViewController) {
        int i = 0;
        int paddingX = udfpsAnimationViewController != null ? udfpsAnimationViewController.getPaddingX() : 0;
        if (udfpsAnimationViewController != null) {
            i = udfpsAnimationViewController.getPaddingY();
        }
        this.mCoreLayoutParams.flags = getCoreLayoutParamFlags();
        if (udfpsAnimationViewController != null && udfpsAnimationViewController.listenForTouchesOutsideView()) {
            this.mCoreLayoutParams.flags |= 262144;
        }
        WindowManager.LayoutParams layoutParams = this.mCoreLayoutParams;
        FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal = this.mSensorProps;
        int i2 = fingerprintSensorPropertiesInternal.sensorLocationX;
        int i3 = fingerprintSensorPropertiesInternal.sensorRadius;
        layoutParams.x = (i2 - i3) - paddingX;
        layoutParams.y = (fingerprintSensorPropertiesInternal.sensorLocationY - i3) - i;
        layoutParams.height = (i3 * 2) + (paddingX * 2);
        layoutParams.width = (i3 * 2) + (i * 2);
        Point point = new Point();
        this.mContext.getDisplay().getRealSize(point);
        int rotation = this.mContext.getDisplay().getRotation();
        if (rotation != 1) {
            if (rotation == 3 && (!(udfpsAnimationViewController instanceof UdfpsKeyguardViewController) || !this.mKeyguardUpdateMonitor.isGoingToSleep())) {
                WindowManager.LayoutParams layoutParams2 = this.mCoreLayoutParams;
                int i4 = point.x;
                FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal2 = this.mSensorProps;
                int i5 = i4 - fingerprintSensorPropertiesInternal2.sensorLocationY;
                int i6 = fingerprintSensorPropertiesInternal2.sensorRadius;
                layoutParams2.x = (i5 - i6) - paddingX;
                layoutParams2.y = (fingerprintSensorPropertiesInternal2.sensorLocationX - i6) - i;
            }
        } else if (!(udfpsAnimationViewController instanceof UdfpsKeyguardViewController) || !this.mKeyguardUpdateMonitor.isGoingToSleep()) {
            WindowManager.LayoutParams layoutParams3 = this.mCoreLayoutParams;
            FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal3 = this.mSensorProps;
            int i7 = fingerprintSensorPropertiesInternal3.sensorLocationY;
            int i8 = fingerprintSensorPropertiesInternal3.sensorRadius;
            layoutParams3.x = (i7 - i8) - paddingX;
            layoutParams3.y = ((point.y - fingerprintSensorPropertiesInternal3.sensorLocationX) - i8) - i;
        }
        WindowManager.LayoutParams layoutParams4 = this.mCoreLayoutParams;
        layoutParams4.accessibilityTitle = " ";
        return layoutParams4;
    }

    private void onOrientationChanged() {
        hideUdfpsOverlay();
        updateOverlay();
    }

    private void showUdfpsOverlay(ServerRequest serverRequest) {
        this.mExecution.assertIsMainThread();
        int i = serverRequest.mRequestReason;
        if (this.mView == null) {
            try {
                Log.v("UdfpsController", "showUdfpsOverlay | adding window reason=" + i);
                UdfpsView udfpsView = (UdfpsView) this.mInflater.inflate(R$layout.udfps_view, (ViewGroup) null, false);
                this.mView = udfpsView;
                this.mOnFingerDown = false;
                udfpsView.setSensorProperties(this.mSensorProps);
                this.mView.setHbmProvider(this.mHbmProvider);
                UdfpsAnimationViewController inflateUdfpsAnimation = inflateUdfpsAnimation(i);
                this.mAttemptedToDismissKeyguard = false;
                inflateUdfpsAnimation.init();
                this.mView.setAnimationViewController(inflateUdfpsAnimation);
                this.mOrientationListener.enable();
                if (i == 1 || i == 2 || i == 3) {
                    this.mView.setImportantForAccessibility(2);
                }
                this.mWindowManager.addView(this.mView, computeLayoutParams(inflateUdfpsAnimation));
                this.mAccessibilityManager.addTouchExplorationStateChangeListener(this.mTouchExplorationStateChangeListener);
                updateTouchListener();
            } catch (RuntimeException e) {
                Log.e("UdfpsController", "showUdfpsOverlay | failed to add window", e);
            }
        } else {
            Log.v("UdfpsController", "showUdfpsOverlay | the overlay is already showing");
        }
    }

    private UdfpsAnimationViewController inflateUdfpsAnimation(int i) {
        if (i == 1 || i == 2) {
            UdfpsEnrollView udfpsEnrollView = (UdfpsEnrollView) this.mInflater.inflate(R$layout.udfps_enroll_view, (ViewGroup) null);
            this.mView.addView(udfpsEnrollView);
            return new UdfpsEnrollViewController(udfpsEnrollView, this.mServerRequest.mEnrollHelper, this.mStatusBarStateController, this.mStatusBar, this.mDumpManager);
        } else if (i == 3) {
            UdfpsBpView udfpsBpView = (UdfpsBpView) this.mInflater.inflate(R$layout.udfps_bp_view, (ViewGroup) null);
            this.mView.addView(udfpsBpView);
            return new UdfpsBpViewController(udfpsBpView, this.mStatusBarStateController, this.mStatusBar, this.mDumpManager);
        } else if (i == 4) {
            UdfpsKeyguardView udfpsKeyguardView = (UdfpsKeyguardView) this.mInflater.inflate(R$layout.udfps_keyguard_view, (ViewGroup) null);
            this.mView.addView(udfpsKeyguardView);
            return new UdfpsKeyguardViewController(udfpsKeyguardView, this.mStatusBarStateController, this.mStatusBar, this.mKeyguardViewManager, this.mKeyguardUpdateMonitor, this.mFgExecutor, this.mDumpManager, this.mKeyguardViewMediator, this.mLockscreenShadeTransitionController, this.mConfigurationController, this);
        } else if (i != 5) {
            Log.d("UdfpsController", "Animation for reason " + i + " not supported yet");
            return null;
        } else {
            UdfpsFpmOtherView udfpsFpmOtherView = (UdfpsFpmOtherView) this.mInflater.inflate(R$layout.udfps_fpm_other_view, (ViewGroup) null);
            this.mView.addView(udfpsFpmOtherView);
            return new UdfpsFpmOtherViewController(udfpsFpmOtherView, this.mStatusBarStateController, this.mStatusBar, this.mDumpManager);
        }
    }

    private void hideUdfpsOverlay() {
        this.mExecution.assertIsMainThread();
        if (this.mView != null) {
            Log.v("UdfpsController", "hideUdfpsOverlay | removing window");
            onFingerUp();
            this.mWindowManager.removeView(this.mView);
            this.mView.setOnTouchListener(null);
            this.mView.setOnHoverListener(null);
            this.mView.setAnimationViewController(null);
            this.mAccessibilityManager.removeTouchExplorationStateChangeListener(this.mTouchExplorationStateChangeListener);
            this.mView = null;
        } else {
            Log.v("UdfpsController", "hideUdfpsOverlay | the overlay is already hidden");
        }
        this.mOrientationListener.disable();
    }

    /* access modifiers changed from: package-private */
    public void onAodInterrupt(int i, int i2, float f, float f2) {
        if (!this.mIsAodInterruptActive) {
            UdfpsController$$ExternalSyntheticLambda5 udfpsController$$ExternalSyntheticLambda5 = new Runnable(i, i2, f2, f) { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda5
                public final /* synthetic */ int f$1;
                public final /* synthetic */ int f$2;
                public final /* synthetic */ float f$3;
                public final /* synthetic */ float f$4;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsController.this.lambda$onAodInterrupt$4(this.f$1, this.f$2, this.f$3, this.f$4);
                }
            };
            this.mAodInterruptRunnable = udfpsController$$ExternalSyntheticLambda5;
            if (this.mScreenOn) {
                udfpsController$$ExternalSyntheticLambda5.run();
                this.mAodInterruptRunnable = null;
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onAodInterrupt$4(int i, int i2, float f, float f2) {
        this.mIsAodInterruptActive = true;
        this.mCancelAodTimeoutAction = this.mFgExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                UdfpsController.this.onCancelUdfps();
            }
        }, 1000);
        onFingerDown(i, i2, f, f2);
    }

    /* access modifiers changed from: package-private */
    public void onCancelUdfps() {
        onFingerUp();
        if (this.mIsAodInterruptActive) {
            Runnable runnable = this.mCancelAodTimeoutAction;
            if (runnable != null) {
                runnable.run();
                this.mCancelAodTimeoutAction = null;
            }
            this.mIsAodInterruptActive = false;
        }
    }

    public boolean isFingerDown() {
        return this.mOnFingerDown;
    }

    private void onFingerDown(int i, int i2, float f, float f2) {
        this.mExecution.assertIsMainThread();
        UdfpsView udfpsView = this.mView;
        if (udfpsView == null) {
            Log.w("UdfpsController", "Null view in onFingerDown");
            return;
        }
        if ((udfpsView.getAnimationViewController() instanceof UdfpsKeyguardViewController) && !this.mStatusBarStateController.isDozing()) {
            this.mKeyguardBypassController.setUserHasDeviceEntryIntent(true);
        }
        if (!this.mOnFingerDown) {
            playStartHaptic();
            if (!this.mKeyguardUpdateMonitor.isFaceDetectionRunning()) {
                this.mKeyguardUpdateMonitor.requestFaceAuth(false);
            }
        }
        this.mOnFingerDown = true;
        this.mFingerprintManager.onPointerDown(this.mSensorProps.sensorId, i, i2, f, f2);
        Trace.endAsyncSection("UdfpsController.e2e.onPointerDown", 0);
        Trace.beginAsyncSection("UdfpsController.e2e.startIllumination", 0);
        this.mView.startIllumination(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                UdfpsController.this.lambda$onFingerDown$5();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFingerDown$5() {
        this.mFingerprintManager.onUiReady(this.mSensorProps.sensorId);
        Trace.endAsyncSection("UdfpsController.e2e.startIllumination", 0);
    }

    private void onFingerUp() {
        this.mExecution.assertIsMainThread();
        this.mActivePointerId = -1;
        this.mGoodCaptureReceived = false;
        if (this.mView == null) {
            Log.w("UdfpsController", "Null view in onFingerUp");
            return;
        }
        if (this.mOnFingerDown) {
            this.mFingerprintManager.onPointerUp(this.mSensorProps.sensorId);
        }
        this.mOnFingerDown = false;
        if (this.mView.isIlluminationRequested()) {
            this.mView.stopIllumination();
        }
    }

    private void updateTouchListener() {
        if (this.mView != null) {
            if (this.mAccessibilityManager.isTouchExplorationEnabled()) {
                this.mView.setOnHoverListener(this.mOnHoverListener);
                this.mView.setOnTouchListener(null);
                return;
            }
            this.mView.setOnHoverListener(null);
            this.mView.setOnTouchListener(this.mOnTouchListener);
        }
    }
}
