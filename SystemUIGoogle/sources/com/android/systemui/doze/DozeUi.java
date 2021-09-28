package com.android.systemui.doze;

import android.app.AlarmManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.AlarmTimeout;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.Lazy;
import java.util.Calendar;
import java.util.Objects;
/* loaded from: classes.dex */
public class DozeUi implements DozeMachine.Part, TunerService.Tunable, ConfigurationController.ConfigurationListener {
    private final boolean mCanAnimateTransition;
    private final ConfigurationController mConfigurationController;
    private final Context mContext;
    private final DozeLog mDozeLog;
    private final DozeParameters mDozeParameters;
    private final Handler mHandler;
    private final DozeHost mHost;
    private boolean mKeyguardShowing;
    private final KeyguardUpdateMonitorCallback mKeyguardVisibilityCallback;
    private long mLastTimeTickElapsed = 0;
    private DozeMachine mMachine;
    private final Lazy<StatusBarStateController> mStatusBarStateController;
    private final AlarmTimeout mTimeTicker;
    private final TunerService mTunerService;
    private final WakeLock mWakeLock;

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onTimeTick$0() {
    }

    public DozeUi(Context context, AlarmManager alarmManager, WakeLock wakeLock, DozeHost dozeHost, Handler handler, DozeParameters dozeParameters, KeyguardUpdateMonitor keyguardUpdateMonitor, DozeLog dozeLog, TunerService tunerService, Lazy<StatusBarStateController> lazy, ConfigurationController configurationController) {
        AnonymousClass1 r0 = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.doze.DozeUi.1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onTimeChanged() {
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onKeyguardVisibilityChanged(boolean z) {
                DozeUi.this.mKeyguardShowing = z;
                DozeUi.this.updateAnimateScreenOff();
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onShadeExpandedChanged(boolean z) {
                DozeUi.this.updateAnimateScreenOff();
            }
        };
        this.mKeyguardVisibilityCallback = r0;
        this.mContext = context;
        this.mWakeLock = wakeLock;
        this.mHost = dozeHost;
        this.mHandler = handler;
        this.mCanAnimateTransition = !dozeParameters.getDisplayNeedsBlanking();
        this.mDozeParameters = dozeParameters;
        this.mTimeTicker = new AlarmTimeout(alarmManager, new AlarmManager.OnAlarmListener() { // from class: com.android.systemui.doze.DozeUi$$ExternalSyntheticLambda0
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                DozeUi.m95$r8$lambda$2oXvcafRfs9agk1A4kbfyyGBok(DozeUi.this);
            }
        }, "doze_time_tick", handler);
        keyguardUpdateMonitor.registerCallback(r0);
        this.mDozeLog = dozeLog;
        this.mTunerService = tunerService;
        this.mStatusBarStateController = lazy;
        tunerService.addTunable(this, "doze_always_on");
        this.mConfigurationController = configurationController;
        configurationController.addCallback(this);
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void destroy() {
        this.mTunerService.removeTunable(this);
        this.mConfigurationController.removeCallback(this);
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }

    /* access modifiers changed from: private */
    public void updateAnimateScreenOff() {
        if (this.mCanAnimateTransition) {
            boolean z = this.mDozeParameters.getAlwaysOn() && (this.mKeyguardShowing || this.mDozeParameters.shouldControlUnlockedScreenOff()) && !this.mHost.isPowerSaveActive();
            this.mDozeParameters.setControlScreenOffAnimation(z);
            this.mHost.setAnimateScreenOff(z);
        }
    }

    private void pulseWhileDozing(final int i) {
        this.mHost.pulseWhileDozing(new DozeHost.PulseCallback() { // from class: com.android.systemui.doze.DozeUi.2
            @Override // com.android.systemui.doze.DozeHost.PulseCallback
            public void onPulseStarted() {
                DozeMachine.State state;
                try {
                    DozeMachine dozeMachine = DozeUi.this.mMachine;
                    if (i == 8) {
                        state = DozeMachine.State.DOZE_PULSING_BRIGHT;
                    } else {
                        state = DozeMachine.State.DOZE_PULSING;
                    }
                    dozeMachine.requestState(state);
                } catch (IllegalStateException unused) {
                }
            }

            @Override // com.android.systemui.doze.DozeHost.PulseCallback
            public void onPulseFinished() {
                DozeUi.this.mMachine.requestState(DozeMachine.State.DOZE_PULSE_DONE);
            }
        }, i);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.doze.DozeUi$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        static {
            int[] iArr = new int[DozeMachine.State.values().length];
            $SwitchMap$com$android$systemui$doze$DozeMachine$State = iArr;
            try {
                iArr[DozeMachine.State.DOZE_AOD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD_DOCKED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD_PAUSING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD_PAUSED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_REQUEST_PULSE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.INITIALIZED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.FINISH.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_PULSING.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_PULSING_BRIGHT.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_PULSE_DONE.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        switch (AnonymousClass3.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()]) {
            case 1:
            case 2:
                if (state == DozeMachine.State.DOZE_AOD_PAUSED || state == DozeMachine.State.DOZE) {
                    this.mHost.dozeTimeTick();
                    Handler handler = this.mHandler;
                    WakeLock wakeLock = this.mWakeLock;
                    DozeHost dozeHost = this.mHost;
                    Objects.requireNonNull(dozeHost);
                    handler.postDelayed(wakeLock.wrap(new Runnable() { // from class: com.android.systemui.doze.DozeUi$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            DozeHost.this.dozeTimeTick();
                        }
                    }), 500);
                }
                scheduleTimeTick();
                break;
            case 3:
                scheduleTimeTick();
                break;
            case 4:
            case 5:
                unscheduleTimeTick();
                break;
            case 6:
                scheduleTimeTick();
                pulseWhileDozing(this.mMachine.getPulseReason());
                break;
            case 7:
                this.mHost.startDozing();
                break;
            case 8:
                this.mHost.stopDozing();
                unscheduleTimeTick();
                break;
        }
        updateAnimateWakeup(state2);
    }

    private void updateAnimateWakeup(DozeMachine.State state) {
        boolean z = true;
        switch (AnonymousClass3.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state.ordinal()]) {
            case 6:
            case 9:
            case 10:
            case 11:
                this.mHost.setAnimateWakeup(true);
                return;
            case 7:
            default:
                DozeHost dozeHost = this.mHost;
                if (!this.mCanAnimateTransition || !this.mDozeParameters.getAlwaysOn()) {
                    z = false;
                }
                dozeHost.setAnimateWakeup(z);
                return;
            case 8:
                return;
        }
    }

    private void scheduleTimeTick() {
        if (!this.mTimeTicker.isScheduled()) {
            long currentTimeMillis = System.currentTimeMillis();
            long roundToNextMinute = roundToNextMinute(currentTimeMillis) - System.currentTimeMillis();
            if (this.mTimeTicker.schedule(roundToNextMinute, 1)) {
                this.mDozeLog.traceTimeTickScheduled(currentTimeMillis, roundToNextMinute + currentTimeMillis);
            }
            this.mLastTimeTickElapsed = SystemClock.elapsedRealtime();
        }
    }

    private void unscheduleTimeTick() {
        if (this.mTimeTicker.isScheduled()) {
            verifyLastTimeTick();
            this.mTimeTicker.cancel();
        }
    }

    private void verifyLastTimeTick() {
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.mLastTimeTickElapsed;
        if (elapsedRealtime > 90000) {
            String formatShortElapsedTime = Formatter.formatShortElapsedTime(this.mContext, elapsedRealtime);
            this.mDozeLog.traceMissedTick(formatShortElapsedTime);
            Log.e("DozeMachine", "Missed AOD time tick by " + formatShortElapsedTime);
        }
    }

    private long roundToNextMinute(long j) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        instance.set(14, 0);
        instance.set(13, 0);
        instance.add(12, 1);
        return instance.getTimeInMillis();
    }

    /* access modifiers changed from: private */
    public void onTimeTick() {
        verifyLastTimeTick();
        this.mHost.dozeTimeTick();
        this.mHandler.post(this.mWakeLock.wrap(DozeUi$$ExternalSyntheticLambda2.INSTANCE));
        scheduleTimeTick();
    }

    @VisibleForTesting
    KeyguardUpdateMonitorCallback getKeyguardCallback() {
        return this.mKeyguardVisibilityCallback;
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        if (str.equals("doze_always_on")) {
            updateAnimateScreenOff();
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onConfigChanged(Configuration configuration) {
        updateAnimateScreenOff();
    }
}
