package com.android.systemui.keyguard;

import android.app.IWallpaperManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.Trace;
import android.util.DisplayMetrics;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class WakefulnessLifecycle extends Lifecycle<Observer> implements Dumpable {
    private final Context mContext;
    private final DisplayMetrics mDisplayMetrics;
    private final IWallpaperManager mWallpaperManagerService;
    private int mWakefulness = 2;
    private int mLastWakeReason = 0;
    private Point mLastWakeOriginLocation = null;
    private int mLastSleepReason = 0;
    private Point mLastSleepOriginLocation = null;

    /* loaded from: classes.dex */
    public interface Observer {
        default void onFinishedGoingToSleep() {
        }

        default void onFinishedWakingUp() {
        }

        default void onStartedGoingToSleep() {
        }

        default void onStartedWakingUp() {
        }
    }

    public WakefulnessLifecycle(Context context, IWallpaperManager iWallpaperManager) {
        this.mContext = context;
        this.mDisplayMetrics = context.getResources().getDisplayMetrics();
        this.mWallpaperManagerService = iWallpaperManager;
    }

    public int getWakefulness() {
        return this.mWakefulness;
    }

    public int getLastWakeReason() {
        return this.mLastWakeReason;
    }

    public int getLastSleepReason() {
        return this.mLastSleepReason;
    }

    public void dispatchStartedWakingUp(int i) {
        if (getWakefulness() != 1) {
            setWakefulness(1);
            this.mLastWakeReason = i;
            updateLastWakeOriginLocation();
            IWallpaperManager iWallpaperManager = this.mWallpaperManagerService;
            if (iWallpaperManager != null) {
                try {
                    Point point = this.mLastWakeOriginLocation;
                    iWallpaperManager.notifyWakingUp(point.x, point.y, new Bundle());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            dispatch(WakefulnessLifecycle$$ExternalSyntheticLambda3.INSTANCE);
        }
    }

    public void dispatchFinishedWakingUp() {
        if (getWakefulness() != 2) {
            setWakefulness(2);
            dispatch(WakefulnessLifecycle$$ExternalSyntheticLambda1.INSTANCE);
        }
    }

    public void dispatchStartedGoingToSleep(int i) {
        if (getWakefulness() != 3) {
            setWakefulness(3);
            this.mLastSleepReason = i;
            updateLastSleepOriginLocation();
            IWallpaperManager iWallpaperManager = this.mWallpaperManagerService;
            if (iWallpaperManager != null) {
                try {
                    Point point = this.mLastSleepOriginLocation;
                    iWallpaperManager.notifyGoingToSleep(point.x, point.y, new Bundle());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            dispatch(WakefulnessLifecycle$$ExternalSyntheticLambda2.INSTANCE);
        }
    }

    public void dispatchFinishedGoingToSleep() {
        if (getWakefulness() != 0) {
            setWakefulness(0);
            dispatch(WakefulnessLifecycle$$ExternalSyntheticLambda0.INSTANCE);
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("WakefulnessLifecycle:");
        printWriter.println("  mWakefulness=" + this.mWakefulness);
    }

    private void setWakefulness(int i) {
        this.mWakefulness = i;
        Trace.traceCounter(4096, "wakefulness", i);
    }

    private void updateLastWakeOriginLocation() {
        this.mLastWakeOriginLocation = null;
        if (this.mLastWakeReason != 1) {
            this.mLastWakeOriginLocation = getDefaultWakeSleepOrigin();
        } else {
            this.mLastWakeOriginLocation = getPowerButtonOrigin();
        }
    }

    private void updateLastSleepOriginLocation() {
        this.mLastSleepOriginLocation = null;
        if (this.mLastSleepReason != 4) {
            this.mLastSleepOriginLocation = getDefaultWakeSleepOrigin();
        } else {
            this.mLastSleepOriginLocation = getPowerButtonOrigin();
        }
    }

    private Point getPowerButtonOrigin() {
        boolean z = true;
        if (this.mContext.getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        if (z) {
            return new Point(this.mDisplayMetrics.widthPixels, this.mContext.getResources().getDimensionPixelSize(R$dimen.physical_power_button_center_screen_location_y));
        }
        return new Point(this.mContext.getResources().getDimensionPixelSize(R$dimen.physical_power_button_center_screen_location_y), this.mDisplayMetrics.heightPixels);
    }

    private Point getDefaultWakeSleepOrigin() {
        DisplayMetrics displayMetrics = this.mDisplayMetrics;
        return new Point(displayMetrics.widthPixels / 2, displayMetrics.heightPixels);
    }
}
