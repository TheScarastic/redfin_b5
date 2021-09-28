package com.android.wm.shell;

import android.app.ActivityManager;
import android.graphics.Point;
import android.util.Slog;
import android.util.SparseArray;
import android.view.SurfaceControl;
import com.android.wm.shell.FullscreenTaskListener;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class FullscreenTaskListener implements ShellTaskOrganizer.TaskListener {
    private final SparseArray<TaskData> mDataByTaskId = new SparseArray<>();
    private final SyncTransactionQueue mSyncQueue;

    public FullscreenTaskListener(SyncTransactionQueue syncTransactionQueue) {
        this.mSyncQueue = syncTransactionQueue;
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (this.mDataByTaskId.get(runningTaskInfo.taskId) == null) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1501874464, 1, null, Long.valueOf((long) runningTaskInfo.taskId));
            }
            Point point = runningTaskInfo.positionInParent;
            this.mDataByTaskId.put(runningTaskInfo.taskId, new TaskData(surfaceControl, point));
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable(surfaceControl, point) { // from class: com.android.wm.shell.FullscreenTaskListener$$ExternalSyntheticLambda0
                    public final /* synthetic */ SurfaceControl f$0;
                    public final /* synthetic */ Point f$1;

                    {
                        this.f$0 = r1;
                        this.f$1 = r2;
                    }

                    @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                        FullscreenTaskListener.lambda$onTaskAppeared$0(this.f$0, this.f$1, transaction);
                    }
                });
                return;
            }
            return;
        }
        throw new IllegalStateException("Task appeared more than once: #" + runningTaskInfo.taskId);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onTaskAppeared$0(SurfaceControl surfaceControl, Point point, SurfaceControl.Transaction transaction) {
        transaction.setWindowCrop(surfaceControl, null);
        transaction.setPosition(surfaceControl, (float) point.x, (float) point.y);
        transaction.setAlpha(surfaceControl, 1.0f);
        transaction.setMatrix(surfaceControl, 1.0f, 0.0f, 0.0f, 1.0f);
        transaction.show(surfaceControl);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            TaskData taskData = this.mDataByTaskId.get(runningTaskInfo.taskId);
            Point point = runningTaskInfo.positionInParent;
            if (!point.equals(taskData.positionInParent)) {
                taskData.positionInParent.set(point.x, point.y);
                this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable(point) { // from class: com.android.wm.shell.FullscreenTaskListener$$ExternalSyntheticLambda1
                    public final /* synthetic */ Point f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                        FullscreenTaskListener.lambda$onTaskInfoChanged$1(FullscreenTaskListener.TaskData.this, this.f$1, transaction);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onTaskInfoChanged$1(TaskData taskData, Point point, SurfaceControl.Transaction transaction) {
        transaction.setPosition(taskData.surface, (float) point.x, (float) point.y);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (this.mDataByTaskId.get(runningTaskInfo.taskId) == null) {
            Slog.e("FullscreenTaskListener", "Task already vanished: #" + runningTaskInfo.taskId);
            return;
        }
        this.mDataByTaskId.remove(runningTaskInfo.taskId);
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 564235578, 1, null, Long.valueOf((long) runningTaskInfo.taskId));
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        if (this.mDataByTaskId.contains(i)) {
            builder.setParent(this.mDataByTaskId.get(i).surface);
            return;
        }
        throw new IllegalArgumentException("There is no surface for taskId=" + i);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + this);
        printWriter.println((str + "  ") + this.mDataByTaskId.size() + " Tasks");
    }

    public String toString() {
        return "FullscreenTaskListener:" + ShellTaskOrganizer.taskListenerTypeToString(-2);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class TaskData {
        public final Point positionInParent;
        public final SurfaceControl surface;

        public TaskData(SurfaceControl surfaceControl, Point point) {
            this.surface = surfaceControl;
            this.positionInParent = point;
        }
    }
}
