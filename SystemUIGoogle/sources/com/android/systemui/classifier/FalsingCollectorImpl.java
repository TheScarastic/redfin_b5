package com.android.systemui.classifier;

import android.hardware.biometrics.BiometricSourceType;
import android.view.MotionEvent;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.dock.DockManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.StatusBarState;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.time.SystemClock;
import java.util.Collections;
import java.util.Objects;
/* loaded from: classes.dex */
public class FalsingCollectorImpl implements FalsingCollector {
    private boolean mAvoidGesture;
    private final BatteryController mBatteryController;
    private final BatteryController.BatteryStateChangeCallback mBatteryListener;
    private final DockManager.DockEventListener mDockEventListener;
    private final DockManager mDockManager;
    private final FalsingDataProvider mFalsingDataProvider;
    private final FalsingManager mFalsingManager;
    private final HistoryTracker mHistoryTracker;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateCallback;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final DelayableExecutor mMainExecutor;
    private MotionEvent mPendingDownEvent;
    private final ProximitySensor mProximitySensor;
    private boolean mScreenOn;
    private final ThresholdSensor.Listener mSensorEventListener = new ThresholdSensor.Listener() { // from class: com.android.systemui.classifier.FalsingCollectorImpl$$ExternalSyntheticLambda0
        @Override // com.android.systemui.util.sensors.ThresholdSensor.Listener
        public final void onThresholdCrossed(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
            FalsingCollectorImpl.$r8$lambda$GHD5NqS_UivQZRtWSaZkM3kaw9Y(FalsingCollectorImpl.this, thresholdSensorEvent);
        }
    };
    private boolean mSessionStarted;
    private boolean mShowingAod;
    private int mState;
    private final StatusBarStateController mStatusBarStateController;
    private final StatusBarStateController.StateListener mStatusBarStateListener;
    private final SystemClock mSystemClock;

    static void logDebug(String str, Throwable th) {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public boolean isReportingEnabled() {
        return false;
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onAffordanceSwipingAborted() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onAffordanceSwipingStarted(boolean z) {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onCameraHintStarted() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onCameraOn() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onExpansionFromPulseStopped() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onLeftAffordanceHintStarted() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onLeftAffordanceOn() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onNotificationActive() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onNotificationDismissed() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onNotificationStartDismissing() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onNotificationStartDraggingDown() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onNotificationStopDismissing() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onNotificationStopDraggingDown() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onQsDown() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onStartExpandingFromPulse() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onTrackingStarted(boolean z) {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onTrackingStopped() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onUnlockHintStarted() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void setNotificationExpanded() {
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public boolean shouldEnforceBouncer() {
        return false;
    }

    public FalsingCollectorImpl(FalsingDataProvider falsingDataProvider, FalsingManager falsingManager, KeyguardUpdateMonitor keyguardUpdateMonitor, HistoryTracker historyTracker, ProximitySensor proximitySensor, StatusBarStateController statusBarStateController, KeyguardStateController keyguardStateController, BatteryController batteryController, DockManager dockManager, DelayableExecutor delayableExecutor, SystemClock systemClock) {
        AnonymousClass1 r0 = new StatusBarStateController.StateListener() { // from class: com.android.systemui.classifier.FalsingCollectorImpl.1
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onStateChanged(int i) {
                FalsingCollectorImpl.logDebug("StatusBarState=" + StatusBarState.toShortString(i));
                FalsingCollectorImpl.this.mState = i;
                FalsingCollectorImpl.this.updateSessionActive();
            }
        };
        this.mStatusBarStateListener = r0;
        AnonymousClass2 r1 = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.classifier.FalsingCollectorImpl.2
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
                if (i == KeyguardUpdateMonitor.getCurrentUser() && biometricSourceType == BiometricSourceType.FACE) {
                    FalsingCollectorImpl.this.mFalsingDataProvider.setJustUnlockedWithFace(true);
                }
            }
        };
        this.mKeyguardUpdateCallback = r1;
        AnonymousClass3 r2 = new BatteryController.BatteryStateChangeCallback() { // from class: com.android.systemui.classifier.FalsingCollectorImpl.3
            @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
            public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            }

            @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
            public void onWirelessChargingChanged(boolean z) {
                if (z || FalsingCollectorImpl.this.mDockManager.isDocked()) {
                    FalsingCollectorImpl.this.mProximitySensor.pause();
                } else {
                    FalsingCollectorImpl.this.mProximitySensor.resume();
                }
            }
        };
        this.mBatteryListener = r2;
        AnonymousClass4 r3 = new DockManager.DockEventListener() { // from class: com.android.systemui.classifier.FalsingCollectorImpl.4
            @Override // com.android.systemui.dock.DockManager.DockEventListener
            public void onEvent(int i) {
                if (i != 0 || FalsingCollectorImpl.this.mBatteryController.isWirelessCharging()) {
                    FalsingCollectorImpl.this.mProximitySensor.pause();
                } else {
                    FalsingCollectorImpl.this.mProximitySensor.resume();
                }
            }
        };
        this.mDockEventListener = r3;
        this.mFalsingDataProvider = falsingDataProvider;
        this.mFalsingManager = falsingManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mHistoryTracker = historyTracker;
        this.mProximitySensor = proximitySensor;
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardStateController = keyguardStateController;
        this.mBatteryController = batteryController;
        this.mDockManager = dockManager;
        this.mMainExecutor = delayableExecutor;
        this.mSystemClock = systemClock;
        proximitySensor.setTag("FalsingManager");
        proximitySensor.setDelay(1);
        statusBarStateController.addCallback(r0);
        this.mState = statusBarStateController.getState();
        keyguardUpdateMonitor.registerCallback(r1);
        batteryController.addCallback(r2);
        dockManager.addListener(r3);
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onSuccessfulUnlock() {
        this.mFalsingManager.onSuccessfulUnlock();
        sessionEnd();
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void setShowingAod(boolean z) {
        this.mShowingAod = z;
        updateSessionActive();
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void setQsExpanded(boolean z) {
        if (z) {
            unregisterSensors();
        } else if (this.mSessionStarted) {
            registerSensors();
        }
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onScreenOnFromTouch() {
        onScreenTurningOn();
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onScreenTurningOn() {
        this.mScreenOn = true;
        updateSessionActive();
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onScreenOff() {
        this.mScreenOn = false;
        updateSessionActive();
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onBouncerShown() {
        unregisterSensors();
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onBouncerHidden() {
        if (this.mSessionStarted) {
            registerSensors();
        }
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onTouchEvent(MotionEvent motionEvent) {
        if (!this.mKeyguardStateController.isShowing() || (this.mStatusBarStateController.isDozing() && !this.mStatusBarStateController.isPulsing())) {
            avoidGesture();
        } else if (motionEvent.getActionMasked() == 0) {
            this.mPendingDownEvent = MotionEvent.obtain(motionEvent);
            this.mAvoidGesture = false;
        } else if (!this.mAvoidGesture) {
            MotionEvent motionEvent2 = this.mPendingDownEvent;
            if (motionEvent2 != null) {
                this.mFalsingDataProvider.onMotionEvent(motionEvent2);
                this.mPendingDownEvent.recycle();
                this.mPendingDownEvent = null;
            }
            this.mFalsingDataProvider.onMotionEvent(motionEvent);
        }
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void onMotionEventComplete() {
        DelayableExecutor delayableExecutor = this.mMainExecutor;
        FalsingDataProvider falsingDataProvider = this.mFalsingDataProvider;
        Objects.requireNonNull(falsingDataProvider);
        delayableExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.classifier.FalsingCollectorImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                FalsingDataProvider.this.onMotionEventComplete();
            }
        }, 100);
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void avoidGesture() {
        this.mAvoidGesture = true;
        MotionEvent motionEvent = this.mPendingDownEvent;
        if (motionEvent != null) {
            motionEvent.recycle();
            this.mPendingDownEvent = null;
        }
    }

    @Override // com.android.systemui.classifier.FalsingCollector
    public void updateFalseConfidence(FalsingClassifier.Result result) {
        this.mHistoryTracker.addResults(Collections.singleton(result), this.mSystemClock.uptimeMillis());
    }

    private boolean shouldSessionBeActive() {
        return this.mScreenOn && this.mState == 1 && !this.mShowingAod;
    }

    public void updateSessionActive() {
        if (shouldSessionBeActive()) {
            sessionStart();
        } else {
            sessionEnd();
        }
    }

    private void sessionStart() {
        if (!this.mSessionStarted && shouldSessionBeActive()) {
            logDebug("Starting Session");
            this.mSessionStarted = true;
            this.mFalsingDataProvider.setJustUnlockedWithFace(false);
            registerSensors();
            this.mFalsingDataProvider.onSessionStarted();
        }
    }

    private void sessionEnd() {
        if (this.mSessionStarted) {
            logDebug("Ending Session");
            this.mSessionStarted = false;
            unregisterSensors();
            this.mFalsingDataProvider.onSessionEnd();
        }
    }

    private void registerSensors() {
        this.mProximitySensor.register(this.mSensorEventListener);
    }

    private void unregisterSensors() {
        this.mProximitySensor.unregister(this.mSensorEventListener);
    }

    public void onProximityEvent(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
        this.mFalsingManager.onProximityEvent(new ProximityEventImpl(thresholdSensorEvent));
    }

    static void logDebug(String str) {
        logDebug(str, null);
    }

    /* loaded from: classes.dex */
    public static class ProximityEventImpl implements FalsingManager.ProximityEvent {
        private ThresholdSensor.ThresholdSensorEvent mThresholdSensorEvent;

        ProximityEventImpl(ThresholdSensor.ThresholdSensorEvent thresholdSensorEvent) {
            this.mThresholdSensorEvent = thresholdSensorEvent;
        }

        @Override // com.android.systemui.plugins.FalsingManager.ProximityEvent
        public boolean getCovered() {
            return this.mThresholdSensorEvent.getBelow();
        }

        @Override // com.android.systemui.plugins.FalsingManager.ProximityEvent
        public long getTimestampNs() {
            return this.mThresholdSensorEvent.getTimestampNs();
        }
    }
}
