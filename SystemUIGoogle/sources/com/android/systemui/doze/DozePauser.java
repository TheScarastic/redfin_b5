package com.android.systemui.doze;

import android.app.AlarmManager;
import android.os.Handler;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.util.AlarmTimeout;
/* loaded from: classes.dex */
public class DozePauser implements DozeMachine.Part {
    public static final String TAG = "DozePauser";
    private DozeMachine mMachine;
    private final AlarmTimeout mPauseTimeout;
    private final AlwaysOnDisplayPolicy mPolicy;

    public DozePauser(Handler handler, AlarmManager alarmManager, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy) {
        this.mPauseTimeout = new AlarmTimeout(alarmManager, new AlarmManager.OnAlarmListener() { // from class: com.android.systemui.doze.DozePauser$$ExternalSyntheticLambda0
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                DozePauser.$r8$lambda$FVkmNuZ6KH2bD4WjyZqi1lKUmUE(DozePauser.this);
            }
        }, TAG, handler);
        this.mPolicy = alwaysOnDisplayPolicy;
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }

    /* renamed from: com.android.systemui.doze.DozePauser$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        static {
            int[] iArr = new int[DozeMachine.State.values().length];
            $SwitchMap$com$android$systemui$doze$DozeMachine$State = iArr;
            try {
                iArr[DozeMachine.State.DOZE_AOD_PAUSING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        if (AnonymousClass1.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()] != 1) {
            this.mPauseTimeout.cancel();
        } else {
            this.mPauseTimeout.schedule(this.mPolicy.proxScreenOffDelayMs, 1);
        }
    }

    /* access modifiers changed from: private */
    public void onTimeout() {
        this.mMachine.requestState(DozeMachine.State.DOZE_AOD_PAUSED);
    }
}
