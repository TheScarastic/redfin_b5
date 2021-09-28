package com.android.systemui.doze;

import android.hardware.display.AmbientDisplayConfiguration;
import android.os.Trace;
import android.util.Log;
import com.android.internal.util.Preconditions;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.wakelock.WakeLock;
import java.io.PrintWriter;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class DozeMachine {
    static final boolean DEBUG = DozeService.DEBUG;
    private final BatteryController mBatteryController;
    private final AmbientDisplayConfiguration mConfig;
    private DockManager mDockManager;
    private final DozeHost mDozeHost;
    private final DozeLog mDozeLog;
    private final Service mDozeService;
    private Part[] mParts;
    private int mPulseReason;
    private final WakeLock mWakeLock;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    private final ArrayList<State> mQueuedRequests = new ArrayList<>();
    private State mState = State.UNINITIALIZED;
    private boolean mWakeLockHeldForCurrentState = false;

    /* loaded from: classes.dex */
    public interface Part {
        default void destroy() {
        }

        default void dump(PrintWriter printWriter) {
        }

        default void onScreenState(int i) {
        }

        default void setDozeMachine(DozeMachine dozeMachine) {
        }

        void transitionTo(State state, State state2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.doze.DozeMachine$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        static {
            int[] iArr = new int[State.values().length];
            $SwitchMap$com$android$systemui$doze$DozeMachine$State = iArr;
            try {
                iArr[State.DOZE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[State.DOZE_AOD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[State.DOZE_AOD_PAUSED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[State.DOZE_AOD_PAUSING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[State.DOZE_AOD_DOCKED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[State.DOZE_REQUEST_PULSE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[State.DOZE_PULSING.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[State.DOZE_PULSING_BRIGHT.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[State.UNINITIALIZED.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[State.INITIALIZED.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[State.FINISH.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[State.DOZE_PULSE_DONE.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
        }
    }

    /* loaded from: classes.dex */
    public enum State {
        UNINITIALIZED,
        INITIALIZED,
        DOZE,
        DOZE_AOD,
        DOZE_REQUEST_PULSE,
        DOZE_PULSING,
        DOZE_PULSING_BRIGHT,
        DOZE_PULSE_DONE,
        FINISH,
        DOZE_AOD_PAUSED,
        DOZE_AOD_PAUSING,
        DOZE_AOD_DOCKED;

        boolean canPulse() {
            int i = AnonymousClass1.$SwitchMap$com$android$systemui$doze$DozeMachine$State[ordinal()];
            return i == 1 || i == 2 || i == 3 || i == 4 || i == 5;
        }

        boolean staysAwake() {
            int i = AnonymousClass1.$SwitchMap$com$android$systemui$doze$DozeMachine$State[ordinal()];
            return i == 5 || i == 6 || i == 7 || i == 8;
        }

        /* access modifiers changed from: package-private */
        public boolean isAlwaysOn() {
            return this == DOZE_AOD || this == DOZE_AOD_DOCKED;
        }

        /* access modifiers changed from: package-private */
        public int screenState(DozeParameters dozeParameters) {
            switch (AnonymousClass1.$SwitchMap$com$android$systemui$doze$DozeMachine$State[ordinal()]) {
                case 1:
                case 3:
                    return 1;
                case 2:
                case 4:
                    return 4;
                case 5:
                case 7:
                case 8:
                    return 2;
                case 6:
                case 9:
                case 10:
                    return dozeParameters.shouldControlScreenOff() ? 2 : 1;
                default:
                    return 0;
            }
        }
    }

    public DozeMachine(Service service, AmbientDisplayConfiguration ambientDisplayConfiguration, WakeLock wakeLock, WakefulnessLifecycle wakefulnessLifecycle, BatteryController batteryController, DozeLog dozeLog, DockManager dockManager, DozeHost dozeHost, Part[] partArr) {
        this.mDozeService = service;
        this.mConfig = ambientDisplayConfiguration;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mWakeLock = wakeLock;
        this.mBatteryController = batteryController;
        this.mDozeLog = dozeLog;
        this.mDockManager = dockManager;
        this.mDozeHost = dozeHost;
        this.mParts = partArr;
        for (Part part : partArr) {
            part.setDozeMachine(this);
        }
    }

    public void destroy() {
        for (Part part : this.mParts) {
            part.destroy();
        }
    }

    public void requestState(State state) {
        Preconditions.checkArgument(state != State.DOZE_REQUEST_PULSE);
        requestState(state, -1);
    }

    public void requestPulse(int i) {
        Preconditions.checkState(!isExecutingTransition());
        requestState(State.DOZE_REQUEST_PULSE, i);
    }

    /* access modifiers changed from: package-private */
    public void onScreenState(int i) {
        this.mDozeLog.traceDisplayState(i);
        for (Part part : this.mParts) {
            part.onScreenState(i);
        }
    }

    private void requestState(State state, int i) {
        Assert.isMainThread();
        if (DEBUG) {
            Log.i("DozeMachine", "request: current=" + this.mState + " req=" + state, new Throwable("here"));
        }
        boolean z = !isExecutingTransition();
        this.mQueuedRequests.add(state);
        if (z) {
            this.mWakeLock.acquire("DozeMachine#requestState");
            for (int i2 = 0; i2 < this.mQueuedRequests.size(); i2++) {
                transitionTo(this.mQueuedRequests.get(i2), i);
            }
            this.mQueuedRequests.clear();
            this.mWakeLock.release("DozeMachine#requestState");
        }
    }

    public State getState() {
        Assert.isMainThread();
        if (!isExecutingTransition()) {
            return this.mState;
        }
        throw new IllegalStateException("Cannot get state because there were pending transitions: " + this.mQueuedRequests.toString());
    }

    public int getPulseReason() {
        Assert.isMainThread();
        State state = this.mState;
        boolean z = state == State.DOZE_REQUEST_PULSE || state == State.DOZE_PULSING || state == State.DOZE_PULSING_BRIGHT || state == State.DOZE_PULSE_DONE;
        Preconditions.checkState(z, "must be in pulsing state, but is " + this.mState);
        return this.mPulseReason;
    }

    public void wakeUp() {
        this.mDozeService.requestWakeUp();
    }

    public boolean isExecutingTransition() {
        return !this.mQueuedRequests.isEmpty();
    }

    private void transitionTo(State state, int i) {
        State transitionPolicy = transitionPolicy(state);
        if (DEBUG) {
            Log.i("DozeMachine", "transition: old=" + this.mState + " req=" + state + " new=" + transitionPolicy);
        }
        if (transitionPolicy != this.mState) {
            validateTransition(transitionPolicy);
            State state2 = this.mState;
            this.mState = transitionPolicy;
            this.mDozeLog.traceState(transitionPolicy);
            Trace.traceCounter(4096, "doze_machine_state", transitionPolicy.ordinal());
            updatePulseReason(transitionPolicy, state2, i);
            performTransitionOnComponents(state2, transitionPolicy);
            updateWakeLockState(transitionPolicy);
            resolveIntermediateState(transitionPolicy);
        }
    }

    private void updatePulseReason(State state, State state2, int i) {
        if (state == State.DOZE_REQUEST_PULSE) {
            this.mPulseReason = i;
        } else if (state2 == State.DOZE_PULSE_DONE) {
            this.mPulseReason = -1;
        }
    }

    private void performTransitionOnComponents(State state, State state2) {
        for (Part part : this.mParts) {
            part.transitionTo(state, state2);
        }
        this.mDozeLog.traceDozeStateSendComplete(state2);
        if (AnonymousClass1.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()] == 11) {
            this.mDozeService.finish();
        }
    }

    private void validateTransition(State state) {
        try {
            int[] iArr = AnonymousClass1.$SwitchMap$com$android$systemui$doze$DozeMachine$State;
            int i = iArr[this.mState.ordinal()];
            boolean z = true;
            if (i == 9) {
                Preconditions.checkState(state == State.INITIALIZED);
            } else if (i == 11) {
                Preconditions.checkState(state == State.FINISH);
            }
            int i2 = iArr[state.ordinal()];
            if (i2 == 7) {
                if (this.mState != State.DOZE_REQUEST_PULSE) {
                    z = false;
                }
                Preconditions.checkState(z);
            } else if (i2 == 12) {
                State state2 = this.mState;
                if (!(state2 == State.DOZE_REQUEST_PULSE || state2 == State.DOZE_PULSING || state2 == State.DOZE_PULSING_BRIGHT)) {
                    z = false;
                }
                Preconditions.checkState(z);
            } else if (i2 == 9) {
                throw new IllegalArgumentException("can't transition to UNINITIALIZED");
            } else if (i2 == 10) {
                if (this.mState != State.UNINITIALIZED) {
                    z = false;
                }
                Preconditions.checkState(z);
            }
        } catch (RuntimeException e) {
            throw new IllegalStateException("Illegal Transition: " + this.mState + " -> " + state, e);
        }
    }

    private State transitionPolicy(State state) {
        State state2 = this.mState;
        State state3 = State.FINISH;
        if (state2 == state3) {
            return state3;
        }
        if (!this.mDozeHost.isDozeSuppressed() || !state.isAlwaysOn()) {
            State state4 = this.mState;
            if ((state4 == State.DOZE_AOD_PAUSED || state4 == State.DOZE_AOD_PAUSING || state4 == State.DOZE_AOD || state4 == State.DOZE || state4 == State.DOZE_AOD_DOCKED) && state == State.DOZE_PULSE_DONE) {
                Log.i("DozeMachine", "Dropping pulse done because current state is already done: " + this.mState);
                return this.mState;
            } else if (state != State.DOZE_REQUEST_PULSE || state4.canPulse()) {
                return state;
            } else {
                Log.i("DozeMachine", "Dropping pulse request because current state can't pulse: " + this.mState);
                return this.mState;
            }
        } else {
            Log.i("DozeMachine", "Doze is suppressed. Suppressing state: " + state);
            this.mDozeLog.traceDozeSuppressed(state);
            return State.DOZE;
        }
    }

    private void updateWakeLockState(State state) {
        boolean staysAwake = state.staysAwake();
        boolean z = this.mWakeLockHeldForCurrentState;
        if (z && !staysAwake) {
            this.mWakeLock.release("DozeMachine#heldForState");
            this.mWakeLockHeldForCurrentState = false;
        } else if (!z && staysAwake) {
            this.mWakeLock.acquire("DozeMachine#heldForState");
            this.mWakeLockHeldForCurrentState = true;
        }
    }

    private void resolveIntermediateState(State state) {
        State state2;
        int i = AnonymousClass1.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state.ordinal()];
        if (i == 10 || i == 12) {
            int wakefulness = this.mWakefulnessLifecycle.getWakefulness();
            if (state != State.INITIALIZED && (wakefulness == 2 || wakefulness == 1)) {
                state2 = State.FINISH;
            } else if (this.mDockManager.isDocked()) {
                state2 = this.mDockManager.isHidden() ? State.DOZE : State.DOZE_AOD_DOCKED;
            } else if (this.mConfig.alwaysOnEnabled(-2)) {
                state2 = State.DOZE_AOD;
            } else {
                state2 = State.DOZE;
            }
            transitionTo(state2, -1);
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.print(" state=");
        printWriter.println(this.mState);
        printWriter.print(" wakeLockHeldForCurrentState=");
        printWriter.println(this.mWakeLockHeldForCurrentState);
        printWriter.print(" wakeLock=");
        printWriter.println(this.mWakeLock);
        printWriter.print(" isDozeSuppressed=");
        printWriter.println(this.mDozeHost.isDozeSuppressed());
        printWriter.println("Parts:");
        for (Part part : this.mParts) {
            part.dump(printWriter);
        }
    }

    /* loaded from: classes.dex */
    public interface Service {
        void finish();

        void requestWakeUp();

        void setDozeScreenBrightness(int i);

        void setDozeScreenState(int i);

        /* loaded from: classes.dex */
        public static class Delegate implements Service {
            private final Service mDelegate;

            public Delegate(Service service) {
                this.mDelegate = service;
            }

            @Override // com.android.systemui.doze.DozeMachine.Service
            public void finish() {
                this.mDelegate.finish();
            }

            @Override // com.android.systemui.doze.DozeMachine.Service
            public void setDozeScreenState(int i) {
                this.mDelegate.setDozeScreenState(i);
            }

            @Override // com.android.systemui.doze.DozeMachine.Service
            public void requestWakeUp() {
                this.mDelegate.requestWakeUp();
            }

            @Override // com.android.systemui.doze.DozeMachine.Service
            public void setDozeScreenBrightness(int i) {
                this.mDelegate.setDozeScreenBrightness(i);
            }
        }
    }
}
