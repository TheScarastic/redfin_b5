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
public class SideStage extends StageTaskListener {
    /* access modifiers changed from: package-private */
    public SideStage(ShellTaskOrganizer shellTaskOrganizer, int i, StageTaskListener.StageListenerCallbacks stageListenerCallbacks, SyncTransactionQueue syncTransactionQueue, SurfaceSession surfaceSession) {
        super(shellTaskOrganizer, i, stageListenerCallbacks, syncTransactionQueue, surfaceSession);
    }

    /* access modifiers changed from: package-private */
    public void addTask(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, WindowContainerTransaction windowContainerTransaction) {
        WindowContainerToken windowContainerToken = this.mRootTaskInfo.token;
        windowContainerTransaction.setBounds(windowContainerToken, rect).reparent(runningTaskInfo.token, windowContainerToken, true).reorder(windowContainerToken, true);
    }

    /* access modifiers changed from: package-private */
    public boolean removeAllTasks(WindowContainerTransaction windowContainerTransaction, boolean z) {
        windowContainerTransaction.reorder(this.mRootTaskInfo.token, false);
        if (this.mChildrenTaskInfo.size() == 0) {
            return false;
        }
        windowContainerTransaction.reparentTasks(this.mRootTaskInfo.token, (WindowContainerToken) null, StageTaskListener.CONTROLLED_WINDOWING_MODES_WHEN_ACTIVE, StageTaskListener.CONTROLLED_ACTIVITY_TYPES, z);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean removeTask(int i, WindowContainerToken windowContainerToken, WindowContainerTransaction windowContainerTransaction) {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mChildrenTaskInfo.get(i);
        if (runningTaskInfo == null) {
            return false;
        }
        windowContainerTransaction.reparent(runningTaskInfo.token, windowContainerToken, false);
        return true;
    }
}
