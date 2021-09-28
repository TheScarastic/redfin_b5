package com.android.systemui.keyguard;

import android.os.Trace;
import com.android.systemui.Dumpable;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class ScreenLifecycle extends Lifecycle<Observer> implements Dumpable {
    private int mScreenState = 0;

    /* loaded from: classes.dex */
    public interface Observer {
        default void onScreenTurnedOff() {
        }

        default void onScreenTurnedOn() {
        }

        default void onScreenTurningOff() {
        }

        default void onScreenTurningOn() {
        }
    }

    public int getScreenState() {
        return this.mScreenState;
    }

    public void dispatchScreenTurningOn() {
        setScreenState(1);
        dispatch(ScreenLifecycle$$ExternalSyntheticLambda3.INSTANCE);
    }

    public void dispatchScreenTurnedOn() {
        setScreenState(2);
        dispatch(ScreenLifecycle$$ExternalSyntheticLambda1.INSTANCE);
    }

    public void dispatchScreenTurningOff() {
        setScreenState(3);
        dispatch(ScreenLifecycle$$ExternalSyntheticLambda2.INSTANCE);
    }

    public void dispatchScreenTurnedOff() {
        setScreenState(0);
        dispatch(ScreenLifecycle$$ExternalSyntheticLambda0.INSTANCE);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("ScreenLifecycle:");
        printWriter.println("  mScreenState=" + this.mScreenState);
    }

    private void setScreenState(int i) {
        this.mScreenState = i;
        Trace.traceCounter(4096, "screenState", i);
    }
}
