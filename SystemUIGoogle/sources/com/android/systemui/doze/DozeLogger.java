package com.android.systemui.doze;

import android.view.Display;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
public final class DozeLogger {
    private final LogBuffer buffer;

    public DozeLogger(LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logPickupWakeup(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        DozeLogger$logPickupWakeup$2 dozeLogger$logPickupWakeup$2 = DozeLogger$logPickupWakeup$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logPickupWakeup$2);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logPulseStart(int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logPulseStart$2 dozeLogger$logPulseStart$2 = DozeLogger$logPulseStart$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logPulseStart$2);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logPulseFinish() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logPulseFinish$2 dozeLogger$logPulseFinish$2 = DozeLogger$logPulseFinish$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            logBuffer.push(logBuffer.obtain("DozeLog", logLevel, dozeLogger$logPulseFinish$2));
        }
    }

    public final void logNotificationPulse() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logNotificationPulse$2 dozeLogger$logNotificationPulse$2 = DozeLogger$logNotificationPulse$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            logBuffer.push(logBuffer.obtain("DozeLog", logLevel, dozeLogger$logNotificationPulse$2));
        }
    }

    public final void logDozing(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logDozing$2 dozeLogger$logDozing$2 = DozeLogger$logDozing$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logDozing$2);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logDozingSuppressed(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logDozingSuppressed$2 dozeLogger$logDozingSuppressed$2 = DozeLogger$logDozingSuppressed$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logDozingSuppressed$2);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logFling(boolean z, boolean z2, boolean z3, boolean z4) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        DozeLogger$logFling$2 dozeLogger$logFling$2 = DozeLogger$logFling$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logFling$2);
            obtain.setBool1(z);
            obtain.setBool2(z2);
            obtain.setBool3(z3);
            obtain.setBool4(z4);
            logBuffer.push(obtain);
        }
    }

    public final void logEmergencyCall() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logEmergencyCall$2 dozeLogger$logEmergencyCall$2 = DozeLogger$logEmergencyCall$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            logBuffer.push(logBuffer.obtain("DozeLog", logLevel, dozeLogger$logEmergencyCall$2));
        }
    }

    public final void logKeyguardBouncerChanged(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logKeyguardBouncerChanged$2 dozeLogger$logKeyguardBouncerChanged$2 = DozeLogger$logKeyguardBouncerChanged$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logKeyguardBouncerChanged$2);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logScreenOn(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logScreenOn$2 dozeLogger$logScreenOn$2 = DozeLogger$logScreenOn$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logScreenOn$2);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logScreenOff(int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logScreenOff$2 dozeLogger$logScreenOff$2 = DozeLogger$logScreenOff$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logScreenOff$2);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logMissedTick(String str) {
        Intrinsics.checkNotNullParameter(str, "delay");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.ERROR;
        DozeLogger$logMissedTick$2 dozeLogger$logMissedTick$2 = DozeLogger$logMissedTick$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logMissedTick$2);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logTimeTickScheduled(long j, long j2) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        DozeLogger$logTimeTickScheduled$2 dozeLogger$logTimeTickScheduled$2 = DozeLogger$logTimeTickScheduled$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logTimeTickScheduled$2);
            obtain.setLong1(j);
            obtain.setLong2(j2);
            logBuffer.push(obtain);
        }
    }

    public final void logKeyguardVisibilityChange(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logKeyguardVisibilityChange$2 dozeLogger$logKeyguardVisibilityChange$2 = DozeLogger$logKeyguardVisibilityChange$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logKeyguardVisibilityChange$2);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logDozeStateChanged(DozeMachine.State state) {
        Intrinsics.checkNotNullParameter(state, "state");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logDozeStateChanged$2 dozeLogger$logDozeStateChanged$2 = DozeLogger$logDozeStateChanged$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logDozeStateChanged$2);
            obtain.setStr1(state.name());
            logBuffer.push(obtain);
        }
    }

    public final void logStateChangedSent(DozeMachine.State state) {
        Intrinsics.checkNotNullParameter(state, "state");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logStateChangedSent$2 dozeLogger$logStateChangedSent$2 = DozeLogger$logStateChangedSent$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logStateChangedSent$2);
            obtain.setStr1(state.name());
            logBuffer.push(obtain);
        }
    }

    public final void logDisplayStateChanged(int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logDisplayStateChanged$2 dozeLogger$logDisplayStateChanged$2 = DozeLogger$logDisplayStateChanged$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logDisplayStateChanged$2);
            obtain.setStr1(Display.stateToString(i));
            logBuffer.push(obtain);
        }
    }

    public final void logWakeDisplay(boolean z, int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        DozeLogger$logWakeDisplay$2 dozeLogger$logWakeDisplay$2 = DozeLogger$logWakeDisplay$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logWakeDisplay$2);
            obtain.setBool1(z);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logProximityResult(boolean z, long j, int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        DozeLogger$logProximityResult$2 dozeLogger$logProximityResult$2 = DozeLogger$logProximityResult$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logProximityResult$2);
            obtain.setBool1(z);
            obtain.setLong1(j);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logPulseDropped(boolean z, DozeMachine.State state, boolean z2) {
        Intrinsics.checkNotNullParameter(state, "state");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logPulseDropped$2 dozeLogger$logPulseDropped$2 = DozeLogger$logPulseDropped$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logPulseDropped$2);
            obtain.setBool1(z);
            obtain.setStr1(state.name());
            obtain.setBool2(z2);
            logBuffer.push(obtain);
        }
    }

    public final void logPulseDropped(String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logPulseDropped$4 dozeLogger$logPulseDropped$4 = DozeLogger$logPulseDropped$4.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logPulseDropped$4);
            obtain.setStr1(str);
            logBuffer.push(obtain);
        }
    }

    public final void logPulseTouchDisabledByProx(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        DozeLogger$logPulseTouchDisabledByProx$2 dozeLogger$logPulseTouchDisabledByProx$2 = DozeLogger$logPulseTouchDisabledByProx$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logPulseTouchDisabledByProx$2);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logSensorTriggered(int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        DozeLogger$logSensorTriggered$2 dozeLogger$logSensorTriggered$2 = DozeLogger$logSensorTriggered$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logSensorTriggered$2);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logDozeSuppressed(DozeMachine.State state) {
        Intrinsics.checkNotNullParameter(state, "state");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logDozeSuppressed$2 dozeLogger$logDozeSuppressed$2 = DozeLogger$logDozeSuppressed$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logDozeSuppressed$2);
            obtain.setStr1(state.name());
            logBuffer.push(obtain);
        }
    }

    public final void logDozeScreenBrightness(int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logDozeScreenBrightness$2 dozeLogger$logDozeScreenBrightness$2 = DozeLogger$logDozeScreenBrightness$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logDozeScreenBrightness$2);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logSetAodDimmingScrim(long j) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logSetAodDimmingScrim$2 dozeLogger$logSetAodDimmingScrim$2 = DozeLogger$logSetAodDimmingScrim$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logSetAodDimmingScrim$2);
            obtain.setLong1(j);
            logBuffer.push(obtain);
        }
    }
}
