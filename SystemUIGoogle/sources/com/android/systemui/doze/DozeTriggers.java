package com.android.systemui.doze;

import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.util.IndentingPrintWriter;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.DozeSensors;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class DozeTriggers implements DozeMachine.Part {
    private static final boolean DEBUG = DozeService.DEBUG;
    private static boolean sWakeDisplaySensorState;
    private Runnable mAodInterruptRunnable;
    private final AuthController mAuthController;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final AmbientDisplayConfiguration mConfig;
    private final Context mContext;
    private final DockManager mDockManager;
    private final DozeHost mDozeHost;
    private final DozeLog mDozeLog;
    private final DozeParameters mDozeParameters;
    private final DozeSensors mDozeSensors;
    private DozeMachine mMachine;
    private final DelayableExecutor mMainExecutor;
    private long mNotificationPulseTime;
    private final ProximitySensor.ProximityCheck mProxCheck;
    private boolean mPulsePending;
    private final AsyncSensorManager mSensorManager;
    private final UiEventLogger mUiEventLogger;
    private final UiModeManager mUiModeManager;
    private final WakeLock mWakeLock;
    private boolean mWantProxSensor;
    private boolean mWantSensors;
    private boolean mWantTouchScreenSensors;
    private final TriggerReceiver mBroadcastReceiver = new TriggerReceiver();
    private final DockEventListener mDockEventListener = new DockEventListener();
    private DozeHost.Callback mHostCallback = new DozeHost.Callback() { // from class: com.android.systemui.doze.DozeTriggers.1
        @Override // com.android.systemui.doze.DozeHost.Callback
        public void onNotificationAlerted(Runnable runnable) {
            DozeTriggers.this.onNotification(runnable);
        }

        @Override // com.android.systemui.doze.DozeHost.Callback
        public void onPowerSaveChanged(boolean z) {
            if (DozeTriggers.this.mDozeHost.isPowerSaveActive()) {
                DozeTriggers.this.mMachine.requestState(DozeMachine.State.DOZE);
            } else if (DozeTriggers.this.mMachine.getState() == DozeMachine.State.DOZE && DozeTriggers.this.mConfig.alwaysOnEnabled(-2)) {
                DozeTriggers.this.mMachine.requestState(DozeMachine.State.DOZE_AOD);
            }
        }

        @Override // com.android.systemui.doze.DozeHost.Callback
        public void onDozeSuppressedChanged(boolean z) {
            DozeMachine.State state;
            if (!DozeTriggers.this.mConfig.alwaysOnEnabled(-2) || z) {
                state = DozeMachine.State.DOZE;
            } else {
                state = DozeMachine.State.DOZE_AOD;
            }
            DozeTriggers.this.mMachine.requestState(state);
        }
    };
    private final boolean mAllowPulseTriggers = true;

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum DozingUpdateUiEvent implements UiEventLogger.UiEventEnum {
        DOZING_UPDATE_NOTIFICATION(433),
        DOZING_UPDATE_SIGMOTION(434),
        DOZING_UPDATE_SENSOR_PICKUP(435),
        DOZING_UPDATE_SENSOR_DOUBLE_TAP(436),
        DOZING_UPDATE_SENSOR_LONG_SQUEEZE(437),
        DOZING_UPDATE_DOCKING(438),
        DOZING_UPDATE_SENSOR_WAKEUP(439),
        DOZING_UPDATE_SENSOR_WAKE_LOCKSCREEN(440),
        DOZING_UPDATE_SENSOR_TAP(441),
        DOZING_UPDATE_AUTH_TRIGGERED(657),
        DOZING_UPDATE_QUICK_PICKUP(708),
        DOZING_UPDATE_WAKE_TIMEOUT(794);
        
        private final int mId;

        DozingUpdateUiEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        static DozingUpdateUiEvent fromReason(int i) {
            switch (i) {
                case 1:
                    return DOZING_UPDATE_NOTIFICATION;
                case 2:
                    return DOZING_UPDATE_SIGMOTION;
                case 3:
                    return DOZING_UPDATE_SENSOR_PICKUP;
                case 4:
                    return DOZING_UPDATE_SENSOR_DOUBLE_TAP;
                case 5:
                    return DOZING_UPDATE_SENSOR_LONG_SQUEEZE;
                case 6:
                    return DOZING_UPDATE_DOCKING;
                case 7:
                    return DOZING_UPDATE_SENSOR_WAKEUP;
                case 8:
                    return DOZING_UPDATE_SENSOR_WAKE_LOCKSCREEN;
                case 9:
                    return DOZING_UPDATE_SENSOR_TAP;
                case 10:
                    return DOZING_UPDATE_AUTH_TRIGGERED;
                case 11:
                    return DOZING_UPDATE_QUICK_PICKUP;
                default:
                    return null;
            }
        }
    }

    public DozeTriggers(Context context, DozeHost dozeHost, AmbientDisplayConfiguration ambientDisplayConfiguration, DozeParameters dozeParameters, AsyncSensorManager asyncSensorManager, WakeLock wakeLock, DockManager dockManager, ProximitySensor proximitySensor, ProximitySensor.ProximityCheck proximityCheck, DozeLog dozeLog, BroadcastDispatcher broadcastDispatcher, SecureSettings secureSettings, AuthController authController, DelayableExecutor delayableExecutor, UiEventLogger uiEventLogger) {
        this.mContext = context;
        this.mDozeHost = dozeHost;
        this.mConfig = ambientDisplayConfiguration;
        this.mDozeParameters = dozeParameters;
        this.mSensorManager = asyncSensorManager;
        this.mWakeLock = wakeLock;
        this.mDozeSensors = new DozeSensors(context, asyncSensorManager, dozeParameters, ambientDisplayConfiguration, wakeLock, new DozeSensors.Callback() { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda0
            @Override // com.android.systemui.doze.DozeSensors.Callback
            public final void onSensorPulse(int i, float f, float f2, float[] fArr) {
                DozeTriggers.this.onSensor(i, f, f2, fArr);
            }
        }, new Consumer() { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DozeTriggers.$r8$lambda$tYVQpUBU5w_IkmuBvGNlrH1O8EI(DozeTriggers.this, ((Boolean) obj).booleanValue());
            }
        }, dozeLog, proximitySensor, secureSettings, authController);
        this.mUiModeManager = (UiModeManager) context.getSystemService(UiModeManager.class);
        this.mDockManager = dockManager;
        this.mProxCheck = proximityCheck;
        this.mDozeLog = dozeLog;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mAuthController = authController;
        this.mMainExecutor = delayableExecutor;
        this.mUiEventLogger = uiEventLogger;
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void destroy() {
        this.mDozeSensors.destroy();
    }

    public void onNotification(Runnable runnable) {
        if (DozeMachine.DEBUG) {
            Log.d("DozeTriggers", "requestNotificationPulse");
        }
        if (!sWakeDisplaySensorState) {
            Log.d("DozeTriggers", "Wake display false. Pulse denied.");
            runIfNotNull(runnable);
            this.mDozeLog.tracePulseDropped("wakeDisplaySensor");
            return;
        }
        this.mNotificationPulseTime = SystemClock.elapsedRealtime();
        if (!this.mConfig.pulseOnNotificationEnabled(-2)) {
            runIfNotNull(runnable);
            this.mDozeLog.tracePulseDropped("pulseOnNotificationsDisabled");
        } else if (this.mDozeHost.isDozeSuppressed()) {
            runIfNotNull(runnable);
            this.mDozeLog.tracePulseDropped("dozeSuppressed");
        } else {
            requestPulse(1, false, runnable);
            this.mDozeLog.traceNotificationPulse();
        }
    }

    private static void runIfNotNull(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    private void proximityCheckThenCall(Consumer<Boolean> consumer, boolean z, int i) {
        Boolean isProximityCurrentlyNear = this.mDozeSensors.isProximityCurrentlyNear();
        if (z) {
            consumer.accept(null);
        } else if (isProximityCurrentlyNear != null) {
            consumer.accept(isProximityCurrentlyNear);
        } else {
            this.mProxCheck.check(500, new Consumer(SystemClock.uptimeMillis(), i, consumer) { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda4
                public final /* synthetic */ long f$1;
                public final /* synthetic */ int f$2;
                public final /* synthetic */ Consumer f$3;

                {
                    this.f$1 = r2;
                    this.f$2 = r4;
                    this.f$3 = r5;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DozeTriggers.m92$r8$lambda$LLFaZptufirxDFdxqwlSJNi7g(DozeTriggers.this, this.f$1, this.f$2, this.f$3, (Boolean) obj);
                }
            });
            this.mWakeLock.acquire("DozeTriggers");
        }
    }

    public /* synthetic */ void lambda$proximityCheckThenCall$0(long j, int i, Consumer consumer, Boolean bool) {
        boolean z;
        long uptimeMillis = SystemClock.uptimeMillis();
        DozeLog dozeLog = this.mDozeLog;
        if (bool == null) {
            z = false;
        } else {
            z = bool.booleanValue();
        }
        dozeLog.traceProximityResult(z, uptimeMillis - j, i);
        consumer.accept(bool);
        this.mWakeLock.release("DozeTriggers");
    }

    @VisibleForTesting
    public void onSensor(int i, float f, float f2, float[] fArr) {
        boolean z = false;
        boolean z2 = i == 4;
        boolean z3 = i == 9;
        boolean z4 = i == 3;
        boolean z5 = i == 5;
        boolean z6 = i == 7;
        boolean z7 = i == 8;
        boolean z8 = i == 10;
        boolean z9 = i == 11;
        boolean z10 = z9 || ((z6 || z7) && fArr != null && fArr.length > 0 && fArr[0] != 0.0f);
        DozeMachine.State state = null;
        if (z6) {
            if (!this.mMachine.isExecutingTransition()) {
                state = this.mMachine.getState();
            }
            onWakeScreen(z10, state, i);
        } else if (z5) {
            requestPulse(i, true, null);
        } else if (!z7 && !z9) {
            proximityCheckThenCall(new Consumer(z2, z3, f, f2, i, z4, z8, fArr) { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda7
                public final /* synthetic */ boolean f$1;
                public final /* synthetic */ boolean f$2;
                public final /* synthetic */ float f$3;
                public final /* synthetic */ float f$4;
                public final /* synthetic */ int f$5;
                public final /* synthetic */ boolean f$6;
                public final /* synthetic */ boolean f$7;
                public final /* synthetic */ float[] f$8;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                    this.f$5 = r6;
                    this.f$6 = r7;
                    this.f$7 = r8;
                    this.f$8 = r9;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DozeTriggers.m94$r8$lambda$wx5gWG8wArrvrEM976h5B6wqSc(DozeTriggers.this, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, (Boolean) obj);
                }
            }, true, i);
        } else if (z10) {
            requestPulse(i, true, null);
        }
        if (z4) {
            if (SystemClock.elapsedRealtime() - this.mNotificationPulseTime < ((long) this.mDozeParameters.getPickupVibrationThreshold())) {
                z = true;
            }
            this.mDozeLog.tracePickupWakeUp(z);
        }
    }

    public /* synthetic */ void lambda$onSensor$2(boolean z, boolean z2, float f, float f2, int i, boolean z3, boolean z4, float[] fArr, Boolean bool) {
        if (bool != null && bool.booleanValue()) {
            return;
        }
        if (z || z2) {
            if (!(f == -1.0f || f2 == -1.0f)) {
                this.mDozeHost.onSlpiTap(f, f2);
            }
            gentleWakeUp(i);
        } else if (z3) {
            gentleWakeUp(i);
        } else if (z4) {
            DozeMachine.State state = this.mMachine.getState();
            if (state == DozeMachine.State.DOZE_AOD || state == DozeMachine.State.DOZE) {
                this.mAodInterruptRunnable = new Runnable(f, f2, fArr) { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda1
                    public final /* synthetic */ float f$1;
                    public final /* synthetic */ float f$2;
                    public final /* synthetic */ float[] f$3;

                    {
                        this.f$1 = r2;
                        this.f$2 = r3;
                        this.f$3 = r4;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        DozeTriggers.$r8$lambda$7x5GD8oRADMJNF_wwAFPsufPGw4(DozeTriggers.this, this.f$1, this.f$2, this.f$3);
                    }
                };
            }
            requestPulse(10, true, null);
        } else {
            this.mDozeHost.extendPulse(i);
        }
    }

    public /* synthetic */ void lambda$onSensor$1(float f, float f2, float[] fArr) {
        this.mAuthController.onAodInterrupt((int) f, (int) f2, fArr[3], fArr[4]);
    }

    private void gentleWakeUp(int i) {
        Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        Objects.requireNonNull(uiEventLogger);
        ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda2(uiEventLogger));
        if (this.mDozeParameters.getDisplayNeedsBlanking()) {
            this.mDozeHost.setAodDimmingScrim(1.0f);
        }
        this.mMachine.wakeUp();
    }

    public void onProximityFar(boolean z) {
        if (this.mMachine.isExecutingTransition()) {
            Log.w("DozeTriggers", "onProximityFar called during transition. Ignoring sensor response.");
            return;
        }
        boolean z2 = !z;
        DozeMachine.State state = this.mMachine.getState();
        boolean z3 = false;
        boolean z4 = state == DozeMachine.State.DOZE_AOD_PAUSED;
        DozeMachine.State state2 = DozeMachine.State.DOZE_AOD_PAUSING;
        boolean z5 = state == state2;
        DozeMachine.State state3 = DozeMachine.State.DOZE_AOD;
        if (state == state3) {
            z3 = true;
        }
        if (state == DozeMachine.State.DOZE_PULSING || state == DozeMachine.State.DOZE_PULSING_BRIGHT) {
            if (DEBUG) {
                Log.i("DozeTriggers", "Prox changed, ignore touch = " + z2);
            }
            this.mDozeHost.onIgnoreTouchWhilePulsing(z2);
        }
        if (z && (z4 || z5)) {
            if (DEBUG) {
                Log.i("DozeTriggers", "Prox FAR, unpausing AOD");
            }
            this.mMachine.requestState(state3);
        } else if (z2 && z3) {
            if (DEBUG) {
                Log.i("DozeTriggers", "Prox NEAR, pausing AOD");
            }
            this.mMachine.requestState(state2);
        }
    }

    private void onWakeScreen(boolean z, DozeMachine.State state, int i) {
        this.mDozeLog.traceWakeDisplay(z, i);
        sWakeDisplaySensorState = z;
        boolean z2 = false;
        if (z) {
            proximityCheckThenCall(new Consumer(state, i) { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda5
                public final /* synthetic */ DozeMachine.State f$1;
                public final /* synthetic */ int f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DozeTriggers.$r8$lambda$jz60iq9LsCQtyJihC9Q_K0Itjuo(DozeTriggers.this, this.f$1, this.f$2, (Boolean) obj);
                }
            }, false, i);
            return;
        }
        boolean z3 = state == DozeMachine.State.DOZE_AOD_PAUSED;
        if (state == DozeMachine.State.DOZE_AOD_PAUSING) {
            z2 = true;
        }
        if (!z2 && !z3) {
            this.mMachine.requestState(DozeMachine.State.DOZE);
            this.mUiEventLogger.log(DozingUpdateUiEvent.DOZING_UPDATE_WAKE_TIMEOUT);
        }
    }

    public /* synthetic */ void lambda$onWakeScreen$3(DozeMachine.State state, int i, Boolean bool) {
        if ((bool == null || !bool.booleanValue()) && state == DozeMachine.State.DOZE) {
            this.mMachine.requestState(DozeMachine.State.DOZE_AOD);
            Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
            UiEventLogger uiEventLogger = this.mUiEventLogger;
            Objects.requireNonNull(uiEventLogger);
            ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda2(uiEventLogger));
        }
    }

    /* renamed from: com.android.systemui.doze.DozeTriggers$2 */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        static {
            int[] iArr = new int[DozeMachine.State.values().length];
            $SwitchMap$com$android$systemui$doze$DozeMachine$State = iArr;
            try {
                iArr[DozeMachine.State.INITIALIZED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD_PAUSED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD_PAUSING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_PULSING.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_PULSING_BRIGHT.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD_DOCKED.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_PULSE_DONE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.FINISH.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        switch (AnonymousClass2.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()]) {
            case 1:
                this.mAodInterruptRunnable = null;
                sWakeDisplaySensorState = true;
                this.mBroadcastReceiver.register(this.mBroadcastDispatcher);
                this.mDozeHost.addCallback(this.mHostCallback);
                this.mDockManager.addListener(this.mDockEventListener);
                this.mDozeSensors.requestTemporaryDisable();
                checkTriggersAtInit();
                break;
            case 2:
            case 3:
                this.mAodInterruptRunnable = null;
                this.mWantProxSensor = state2 != DozeMachine.State.DOZE;
                this.mWantSensors = true;
                this.mWantTouchScreenSensors = true;
                if (state2 == DozeMachine.State.DOZE_AOD && !sWakeDisplaySensorState) {
                    onWakeScreen(false, state2, 7);
                    break;
                }
                break;
            case 4:
            case 5:
                this.mWantProxSensor = true;
                break;
            case 6:
            case 7:
                this.mWantProxSensor = true;
                this.mWantTouchScreenSensors = false;
                break;
            case 8:
                this.mWantProxSensor = false;
                this.mWantTouchScreenSensors = false;
                break;
            case 9:
                this.mDozeSensors.requestTemporaryDisable();
                break;
            case 10:
                this.mBroadcastReceiver.unregister(this.mBroadcastDispatcher);
                this.mDozeHost.removeCallback(this.mHostCallback);
                this.mDockManager.removeListener(this.mDockEventListener);
                this.mDozeSensors.setListening(false, false);
                this.mDozeSensors.setProxListening(false);
                this.mWantSensors = false;
                this.mWantProxSensor = false;
                this.mWantTouchScreenSensors = false;
                break;
        }
        this.mDozeSensors.setListening(this.mWantSensors, this.mWantTouchScreenSensors);
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void onScreenState(int i) {
        this.mDozeSensors.onScreenState(i);
        boolean z = false;
        boolean z2 = i == 3 || i == 4 || i == 1;
        DozeSensors dozeSensors = this.mDozeSensors;
        if (this.mWantProxSensor && z2) {
            z = true;
        }
        dozeSensors.setProxListening(z);
        this.mDozeSensors.setListening(this.mWantSensors, this.mWantTouchScreenSensors, z2);
        Runnable runnable = this.mAodInterruptRunnable;
        if (runnable != null && i == 2) {
            runnable.run();
            this.mAodInterruptRunnable = null;
        }
    }

    private void checkTriggersAtInit() {
        if (this.mUiModeManager.getCurrentModeType() == 3 || this.mDozeHost.isBlockingDoze() || !this.mDozeHost.isProvisioned()) {
            this.mMachine.requestState(DozeMachine.State.FINISH);
        }
    }

    public void requestPulse(int i, boolean z, Runnable runnable) {
        Assert.isMainThread();
        this.mDozeHost.extendPulse(i);
        DozeMachine.State state = this.mMachine.isExecutingTransition() ? null : this.mMachine.getState();
        if (state == DozeMachine.State.DOZE_PULSING && i == 8) {
            this.mMachine.requestState(DozeMachine.State.DOZE_PULSING_BRIGHT);
        } else if (this.mPulsePending || !this.mAllowPulseTriggers || !canPulse()) {
            if (this.mAllowPulseTriggers) {
                this.mDozeLog.tracePulseDropped(this.mPulsePending, state, this.mDozeHost.isPulsingBlocked());
            }
            runIfNotNull(runnable);
        } else {
            boolean z2 = true;
            this.mPulsePending = true;
            DozeTriggers$$ExternalSyntheticLambda6 dozeTriggers$$ExternalSyntheticLambda6 = new Consumer(runnable, i) { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda6
                public final /* synthetic */ Runnable f$1;
                public final /* synthetic */ int f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DozeTriggers.m93$r8$lambda$RNc1uTsZUg37fmYd0Fst46ytKQ(DozeTriggers.this, this.f$1, this.f$2, (Boolean) obj);
                }
            };
            if (this.mDozeParameters.getProxCheckBeforePulse() && !z) {
                z2 = false;
            }
            proximityCheckThenCall(dozeTriggers$$ExternalSyntheticLambda6, z2, i);
            Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
            UiEventLogger uiEventLogger = this.mUiEventLogger;
            Objects.requireNonNull(uiEventLogger);
            ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda2(uiEventLogger));
        }
    }

    public /* synthetic */ void lambda$requestPulse$4(Runnable runnable, int i, Boolean bool) {
        if (bool == null || !bool.booleanValue()) {
            continuePulseRequest(i);
            return;
        }
        this.mDozeLog.tracePulseDropped("inPocket");
        this.mPulsePending = false;
        runIfNotNull(runnable);
    }

    private boolean canPulse() {
        return this.mMachine.getState() == DozeMachine.State.DOZE || this.mMachine.getState() == DozeMachine.State.DOZE_AOD || this.mMachine.getState() == DozeMachine.State.DOZE_AOD_DOCKED;
    }

    private void continuePulseRequest(int i) {
        this.mPulsePending = false;
        if (this.mDozeHost.isPulsingBlocked() || !canPulse()) {
            this.mDozeLog.tracePulseDropped(this.mPulsePending, this.mMachine.getState(), this.mDozeHost.isPulsingBlocked());
        } else {
            this.mMachine.requestPulse(i);
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void dump(PrintWriter printWriter) {
        printWriter.println(" mAodInterruptRunnable=" + this.mAodInterruptRunnable);
        printWriter.print(" notificationPulseTime=");
        printWriter.println(Formatter.formatShortElapsedTime(this.mContext, this.mNotificationPulseTime));
        printWriter.println(" pulsePending=" + this.mPulsePending);
        printWriter.println("DozeSensors:");
        PrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        this.mDozeSensors.dump(indentingPrintWriter);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class TriggerReceiver extends BroadcastReceiver {
        private boolean mRegistered;

        private TriggerReceiver() {
            DozeTriggers.this = r1;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("com.android.systemui.doze.pulse".equals(intent.getAction())) {
                if (DozeMachine.DEBUG) {
                    Log.d("DozeTriggers", "Received pulse intent");
                }
                DozeTriggers.this.requestPulse(0, false, null);
            }
            if (UiModeManager.ACTION_ENTER_CAR_MODE.equals(intent.getAction())) {
                DozeTriggers.this.mMachine.requestState(DozeMachine.State.FINISH);
            }
            if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                DozeTriggers.this.mDozeSensors.onUserSwitched();
            }
        }

        public void register(BroadcastDispatcher broadcastDispatcher) {
            if (!this.mRegistered) {
                IntentFilter intentFilter = new IntentFilter("com.android.systemui.doze.pulse");
                intentFilter.addAction(UiModeManager.ACTION_ENTER_CAR_MODE);
                intentFilter.addAction("android.intent.action.USER_SWITCHED");
                broadcastDispatcher.registerReceiver(this, intentFilter);
                this.mRegistered = true;
            }
        }

        public void unregister(BroadcastDispatcher broadcastDispatcher) {
            if (this.mRegistered) {
                broadcastDispatcher.unregisterReceiver(this);
                this.mRegistered = false;
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DockEventListener implements DockManager.DockEventListener {
        private DockEventListener() {
            DozeTriggers.this = r1;
        }

        @Override // com.android.systemui.dock.DockManager.DockEventListener
        public void onEvent(int i) {
            if (DozeTriggers.DEBUG) {
                Log.d("DozeTriggers", "dock event = " + i);
            }
            if (i == 0) {
                DozeTriggers.this.mDozeSensors.ignoreTouchScreenSensorsSettingInterferingWithDocking(false);
            } else if (i == 1 || i == 2) {
                DozeTriggers.this.mDozeSensors.ignoreTouchScreenSensorsSettingInterferingWithDocking(true);
            }
        }
    }
}
