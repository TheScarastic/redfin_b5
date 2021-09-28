package com.android.wm.shell.splitscreen;

import android.app.ActivityManager;
import android.graphics.Rect;
import android.view.SurfaceSession;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.splitscreen.StageTaskListener;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class MainStage extends StageTaskListener {
    private boolean mIsActive = false;

    /* access modifiers changed from: package-private */
    public MainStage(ShellTaskOrganizer shellTaskOrganizer, int i, StageTaskListener.StageListenerCallbacks stageListenerCallbacks, SyncTransactionQueue syncTransactionQueue, SurfaceSession surfaceSession) {
        super(shellTaskOrganizer, i, stageListenerCallbacks, syncTransactionQueue, surfaceSession);
    }

    /* access modifiers changed from: package-private */
    public boolean isActive() {
        return this.mIsActive;
    }

    /* access modifiers changed from: package-private */
    public void activate(Rect rect, WindowContainerTransaction windowContainerTransaction) {
        if (!this.mIsActive) {
            WindowContainerToken windowContainerToken = this.mRootTaskInfo.token;
            WindowContainerTransaction windowingMode = windowContainerTransaction.setBounds(windowContainerToken, rect).setWindowingMode(windowContainerToken, 6);
            int[] iArr = StageTaskListener.CONTROLLED_WINDOWING_MODES;
            int[] iArr2 = StageTaskListener.CONTROLLED_ACTIVITY_TYPES;
            windowingMode.setLaunchRoot(windowContainerToken, iArr, iArr2).reparentTasks((WindowContainerToken) null, windowContainerToken, iArr, iArr2, true).reorder(windowContainerToken, true);
            this.mIsActive = true;
        }
    }

    /* access modifiers changed from: package-private */
    public void deactivate(WindowContainerTransaction windowContainerTransaction) {
        deactivate(windowContainerTransaction, false);
    }

    /* access modifiers changed from: package-private */
    public void deactivate(WindowContainerTransaction windowContainerTransaction, boolean z) {
        if (this.mIsActive) {
            this.mIsActive = false;
            ActivityManager.RunningTaskInfo runningTaskInfo = this.mRootTaskInfo;
            if (runningTaskInfo != null) {
                WindowContainerToken windowContainerToken = runningTaskInfo.token;
                windowContainerTransaction.setLaunchRoot(windowContainerToken, (int[]) null, (int[]) null).reparentTasks(windowContainerToken, (WindowContainerToken) null, StageTaskListener.CONTROLLED_WINDOWING_MODES_WHEN_ACTIVE, StageTaskListener.CONTROLLED_ACTIVITY_TYPES, z).reorder(windowContainerToken, false);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateConfiguration(int i, Rect rect, WindowContainerTransaction windowContainerTransaction) {
        windowContainerTransaction.setBounds(this.mRootTaskInfo.token, rect).setWindowingMode(this.mRootTaskInfo.token, i);
    }
}
