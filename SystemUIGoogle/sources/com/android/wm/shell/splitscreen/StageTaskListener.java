package com.android.wm.shell.splitscreen;

import android.app.ActivityManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.SurfaceUtils;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class StageTaskListener implements ShellTaskOrganizer.TaskListener {
    protected static final int[] CONTROLLED_ACTIVITY_TYPES = {1};
    protected static final int[] CONTROLLED_WINDOWING_MODES = {1, 0};
    protected static final int[] CONTROLLED_WINDOWING_MODES_WHEN_ACTIVE = {1, 0, 6};
    private final StageListenerCallbacks mCallbacks;
    protected SurfaceControl mDimLayer;
    protected SurfaceControl mRootLeash;
    protected ActivityManager.RunningTaskInfo mRootTaskInfo;
    private final SurfaceSession mSurfaceSession;
    private final SyncTransactionQueue mSyncQueue;
    protected SparseArray<ActivityManager.RunningTaskInfo> mChildrenTaskInfo = new SparseArray<>();
    private final SparseArray<SurfaceControl> mChildrenLeashes = new SparseArray<>();

    /* loaded from: classes2.dex */
    public interface StageListenerCallbacks {
        void onChildTaskStatusChanged(int i, boolean z, boolean z2);

        void onNoLongerSupportMultiWindow();

        void onRootTaskAppeared();

        void onRootTaskVanished();

        void onStatusChanged(boolean z, boolean z2);
    }

    /* access modifiers changed from: package-private */
    public StageTaskListener(ShellTaskOrganizer shellTaskOrganizer, int i, StageListenerCallbacks stageListenerCallbacks, SyncTransactionQueue syncTransactionQueue, SurfaceSession surfaceSession) {
        this.mCallbacks = stageListenerCallbacks;
        this.mSyncQueue = syncTransactionQueue;
        this.mSurfaceSession = surfaceSession;
        shellTaskOrganizer.createRootTask(i, 6, this);
    }

    /* access modifiers changed from: package-private */
    public int getChildCount() {
        return this.mChildrenTaskInfo.size();
    }

    /* access modifiers changed from: package-private */
    public boolean containsTask(int i) {
        return this.mChildrenTaskInfo.contains(i);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        if (this.mRootTaskInfo == null && !runningTaskInfo.hasParentTask()) {
            this.mRootLeash = surfaceControl;
            this.mRootTaskInfo = runningTaskInfo;
            this.mCallbacks.onRootTaskAppeared();
            sendStatusChanged();
            this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.splitscreen.StageTaskListener$$ExternalSyntheticLambda2
                @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                    StageTaskListener.this.lambda$onTaskAppeared$0(transaction);
                }
            });
        } else if (runningTaskInfo.parentTaskId == this.mRootTaskInfo.taskId) {
            int i = runningTaskInfo.taskId;
            this.mChildrenLeashes.put(i, surfaceControl);
            this.mChildrenTaskInfo.put(i, runningTaskInfo);
            updateChildTaskSurface(runningTaskInfo, surfaceControl, true);
            this.mCallbacks.onChildTaskStatusChanged(i, true, runningTaskInfo.isVisible);
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                sendStatusChanged();
            }
        } else {
            throw new IllegalArgumentException(this + "\n Unknown task: " + runningTaskInfo + "\n mRootTaskInfo: " + this.mRootTaskInfo);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskAppeared$0(SurfaceControl.Transaction transaction) {
        this.mDimLayer = SurfaceUtils.makeDimLayer(transaction, this.mRootLeash, "Dim layer", this.mSurfaceSession);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (!runningTaskInfo.supportsMultiWindow) {
            this.mCallbacks.onNoLongerSupportMultiWindow();
            return;
        }
        int i = this.mRootTaskInfo.taskId;
        int i2 = runningTaskInfo.taskId;
        if (i == i2) {
            this.mRootTaskInfo = runningTaskInfo;
        } else if (runningTaskInfo.parentTaskId == i) {
            this.mChildrenTaskInfo.put(i2, runningTaskInfo);
            this.mCallbacks.onChildTaskStatusChanged(runningTaskInfo.taskId, true, runningTaskInfo.isVisible);
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                updateChildTaskSurface(runningTaskInfo, this.mChildrenLeashes.get(runningTaskInfo.taskId), false);
            }
        } else {
            throw new IllegalArgumentException(this + "\n Unknown task: " + runningTaskInfo + "\n mRootTaskInfo: " + this.mRootTaskInfo);
        }
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            sendStatusChanged();
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        int i = runningTaskInfo.taskId;
        if (this.mRootTaskInfo.taskId == i) {
            this.mCallbacks.onRootTaskVanished();
            this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.splitscreen.StageTaskListener$$ExternalSyntheticLambda1
                @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                    StageTaskListener.this.lambda$onTaskVanished$1(transaction);
                }
            });
            this.mRootTaskInfo = null;
        } else if (this.mChildrenTaskInfo.contains(i)) {
            this.mChildrenTaskInfo.remove(i);
            this.mChildrenLeashes.remove(i);
            this.mCallbacks.onChildTaskStatusChanged(i, false, runningTaskInfo.isVisible);
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                sendStatusChanged();
            }
        } else {
            throw new IllegalArgumentException(this + "\n Unknown task: " + runningTaskInfo + "\n mRootTaskInfo: " + this.mRootTaskInfo);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskVanished$1(SurfaceControl.Transaction transaction) {
        transaction.remove(this.mDimLayer);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        if (this.mRootTaskInfo.taskId == i) {
            builder.setParent(this.mRootLeash);
        } else if (this.mChildrenLeashes.contains(i)) {
            builder.setParent(this.mChildrenLeashes.get(i));
        } else {
            throw new IllegalArgumentException("There is no surface for taskId=" + i);
        }
    }

    /* access modifiers changed from: package-private */
    public void setBounds(Rect rect, WindowContainerTransaction windowContainerTransaction) {
        windowContainerTransaction.setBounds(this.mRootTaskInfo.token, rect);
    }

    /* access modifiers changed from: package-private */
    public void setVisibility(boolean z, WindowContainerTransaction windowContainerTransaction) {
        windowContainerTransaction.reorder(this.mRootTaskInfo.token, z);
    }

    /* access modifiers changed from: package-private */
    public void onSplitScreenListenerRegistered(SplitScreen.SplitScreenListener splitScreenListener, int i) {
        for (int size = this.mChildrenTaskInfo.size() - 1; size >= 0; size--) {
            int keyAt = this.mChildrenTaskInfo.keyAt(size);
            splitScreenListener.onTaskStageChanged(keyAt, i, this.mChildrenTaskInfo.get(keyAt).isVisible);
        }
    }

    private void updateChildTaskSurface(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, boolean z) {
        this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable(surfaceControl, runningTaskInfo.positionInParent, z) { // from class: com.android.wm.shell.splitscreen.StageTaskListener$$ExternalSyntheticLambda0
            public final /* synthetic */ SurfaceControl f$0;
            public final /* synthetic */ Point f$1;
            public final /* synthetic */ boolean f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
            public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                StageTaskListener.lambda$updateChildTaskSurface$2(this.f$0, this.f$1, this.f$2, transaction);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateChildTaskSurface$2(SurfaceControl surfaceControl, Point point, boolean z, SurfaceControl.Transaction transaction) {
        transaction.setWindowCrop(surfaceControl, null);
        transaction.setPosition(surfaceControl, (float) point.x, (float) point.y);
        if (z && !Transitions.ENABLE_SHELL_TRANSITIONS) {
            transaction.setAlpha(surfaceControl, 1.0f);
            transaction.setMatrix(surfaceControl, 1.0f, 0.0f, 0.0f, 1.0f);
            transaction.show(surfaceControl);
        }
    }

    private void sendStatusChanged() {
        this.mCallbacks.onStatusChanged(this.mRootTaskInfo.isVisible, this.mChildrenTaskInfo.size() > 0);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void dump(PrintWriter printWriter, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("  ");
        printWriter.println(str + this);
    }
}
