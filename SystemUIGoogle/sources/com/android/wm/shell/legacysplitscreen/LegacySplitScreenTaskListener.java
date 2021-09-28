package com.android.wm.shell.legacysplitscreen;

import android.app.ActivityManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.TaskOrganizer;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.SurfaceUtils;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
import java.util.ArrayList;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class LegacySplitScreenTaskListener implements ShellTaskOrganizer.TaskListener {
    private static final String TAG = "LegacySplitScreenTaskListener";
    ActivityManager.RunningTaskInfo mPrimary;
    SurfaceControl mPrimaryDim;
    SurfaceControl mPrimarySurface;
    ActivityManager.RunningTaskInfo mSecondary;
    SurfaceControl mSecondaryDim;
    SurfaceControl mSecondarySurface;
    final LegacySplitScreenController mSplitScreenController;
    private final LegacySplitScreenTransitions mSplitTransitions;
    private final SyncTransactionQueue mSyncQueue;
    private final ShellTaskOrganizer mTaskOrganizer;
    private final SparseArray<SurfaceControl> mLeashByTaskId = new SparseArray<>();
    private final SparseArray<Point> mPositionByTaskId = new SparseArray<>();
    Rect mHomeBounds = new Rect();
    private boolean mSplitScreenSupported = false;
    final SurfaceSession mSurfaceSession = new SurfaceSession();

    /* access modifiers changed from: package-private */
    public LegacySplitScreenTaskListener(LegacySplitScreenController legacySplitScreenController, ShellTaskOrganizer shellTaskOrganizer, Transitions transitions, SyncTransactionQueue syncTransactionQueue) {
        this.mSplitScreenController = legacySplitScreenController;
        this.mTaskOrganizer = shellTaskOrganizer;
        LegacySplitScreenTransitions legacySplitScreenTransitions = new LegacySplitScreenTransitions(legacySplitScreenController.mTransactionPool, transitions, legacySplitScreenController, this);
        this.mSplitTransitions = legacySplitScreenTransitions;
        transitions.addHandler(legacySplitScreenTransitions);
        this.mSyncQueue = syncTransactionQueue;
    }

    /* access modifiers changed from: package-private */
    public void init() {
        try {
            synchronized (this) {
                try {
                    this.mTaskOrganizer.createRootTask(0, 3, this);
                    this.mTaskOrganizer.createRootTask(0, 4, this);
                } catch (Exception e) {
                    this.mTaskOrganizer.removeListener(this);
                    throw e;
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isSplitScreenSupported() {
        return this.mSplitScreenSupported;
    }

    /* access modifiers changed from: package-private */
    public SurfaceControl.Transaction getTransaction() {
        return this.mSplitScreenController.mTransactionPool.acquire();
    }

    /* access modifiers changed from: package-private */
    public void releaseTransaction(SurfaceControl.Transaction transaction) {
        this.mSplitScreenController.mTransactionPool.release(transaction);
    }

    /* access modifiers changed from: package-private */
    public TaskOrganizer getTaskOrganizer() {
        return this.mTaskOrganizer;
    }

    /* access modifiers changed from: package-private */
    public LegacySplitScreenTransitions getSplitTransitions() {
        return this.mSplitTransitions;
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        synchronized (this) {
            if (runningTaskInfo.hasParentTask()) {
                handleChildTaskAppeared(runningTaskInfo, surfaceControl);
                return;
            }
            int windowingMode = runningTaskInfo.getWindowingMode();
            if (windowingMode == 3) {
                if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1362429294, 4, null, String.valueOf(TAG), Long.valueOf((long) runningTaskInfo.taskId));
                }
                this.mPrimary = runningTaskInfo;
                this.mPrimarySurface = surfaceControl;
            } else if (windowingMode == 4) {
                if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 982027396, 4, null, String.valueOf(TAG), Long.valueOf((long) runningTaskInfo.taskId));
                }
                this.mSecondary = runningTaskInfo;
                this.mSecondarySurface = surfaceControl;
            } else if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -298656957, 20, null, String.valueOf(TAG), Long.valueOf((long) runningTaskInfo.taskId), Long.valueOf((long) windowingMode));
            }
            if (!(this.mSplitScreenSupported || this.mPrimarySurface == null || this.mSecondarySurface == null)) {
                this.mSplitScreenSupported = true;
                this.mSplitScreenController.onSplitScreenSupported();
                if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 473543554, 0, null, String.valueOf(TAG));
                }
                SurfaceControl.Transaction transaction = getTransaction();
                this.mPrimaryDim = SurfaceUtils.makeDimLayer(transaction, this.mPrimarySurface, "Primary Divider Dim", this.mSurfaceSession);
                this.mSecondaryDim = SurfaceUtils.makeDimLayer(transaction, this.mSecondarySurface, "Secondary Divider Dim", this.mSurfaceSession);
                transaction.apply();
                releaseTransaction(transaction);
            }
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        synchronized (this) {
            this.mPositionByTaskId.remove(runningTaskInfo.taskId);
            if (runningTaskInfo.hasParentTask()) {
                this.mLeashByTaskId.remove(runningTaskInfo.taskId);
                return;
            }
            ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mPrimary;
            boolean z = true;
            boolean z2 = runningTaskInfo2 != null && runningTaskInfo.token.equals(runningTaskInfo2.token);
            ActivityManager.RunningTaskInfo runningTaskInfo3 = this.mSecondary;
            if (runningTaskInfo3 == null || !runningTaskInfo.token.equals(runningTaskInfo3.token)) {
                z = false;
            }
            if (this.mSplitScreenSupported && (z2 || z)) {
                this.mSplitScreenSupported = false;
                SurfaceControl.Transaction transaction = getTransaction();
                transaction.remove(this.mPrimaryDim);
                transaction.remove(this.mSecondaryDim);
                transaction.remove(this.mPrimarySurface);
                transaction.remove(this.mSecondarySurface);
                transaction.apply();
                releaseTransaction(transaction);
                this.mSplitScreenController.onTaskVanished();
            }
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (runningTaskInfo.displayId == 0) {
            synchronized (this) {
                if (!runningTaskInfo.supportsMultiWindow) {
                    if (this.mSplitScreenController.isDividerVisible()) {
                        int i = runningTaskInfo.taskId;
                        int i2 = this.mPrimary.taskId;
                        if (!(i == i2 || runningTaskInfo.parentTaskId == i2)) {
                            this.mSplitScreenController.startDismissSplit(!runningTaskInfo.isFocused);
                        }
                        this.mSplitScreenController.startDismissSplit(runningTaskInfo.isFocused);
                    }
                    return;
                }
                if (!runningTaskInfo.hasParentTask()) {
                    handleTaskInfoChanged(runningTaskInfo);
                } else if (!runningTaskInfo.positionInParent.equals(this.mPositionByTaskId.get(runningTaskInfo.taskId))) {
                    handleChildTaskChanged(runningTaskInfo);
                } else {
                    return;
                }
                this.mPositionByTaskId.put(runningTaskInfo.taskId, new Point(runningTaskInfo.positionInParent));
            }
        }
    }

    private void handleChildTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        this.mLeashByTaskId.put(runningTaskInfo.taskId, surfaceControl);
        this.mPositionByTaskId.put(runningTaskInfo.taskId, new Point(runningTaskInfo.positionInParent));
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            updateChildTaskSurface(runningTaskInfo, surfaceControl, true);
        }
    }

    private void handleChildTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            updateChildTaskSurface(runningTaskInfo, this.mLeashByTaskId.get(runningTaskInfo.taskId), false);
        }
    }

    private void updateChildTaskSurface(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, boolean z) {
        this.mSyncQueue.runInSync(new SyncTransactionQueue.TransactionRunnable(surfaceControl, runningTaskInfo.positionInParent, z) { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTaskListener$$ExternalSyntheticLambda0
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
                LegacySplitScreenTaskListener.lambda$updateChildTaskSurface$0(this.f$0, this.f$1, this.f$2, transaction);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateChildTaskSurface$0(SurfaceControl surfaceControl, Point point, boolean z, SurfaceControl.Transaction transaction) {
        transaction.setWindowCrop(surfaceControl, null);
        transaction.setPosition(surfaceControl, (float) point.x, (float) point.y);
        if (z && !Transitions.ENABLE_SHELL_TRANSITIONS) {
            transaction.setAlpha(surfaceControl, 1.0f);
            transaction.setMatrix(surfaceControl, 1.0f, 0.0f, 0.0f, 1.0f);
            transaction.show(surfaceControl);
        }
    }

    private void handleTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (!this.mSplitScreenSupported) {
            Log.e(TAG, "Got handleTaskInfoChanged when not initialized: " + runningTaskInfo);
            return;
        }
        int i = this.mSecondary.topActivityType;
        boolean z = true;
        boolean z2 = i == 2 || (i == 3 && this.mSplitScreenController.isHomeStackResizable());
        boolean z3 = this.mPrimary.topActivityType == 0;
        boolean z4 = this.mSecondary.topActivityType == 0;
        if (runningTaskInfo.token.asBinder() == this.mPrimary.token.asBinder()) {
            this.mPrimary = runningTaskInfo;
        } else if (runningTaskInfo.token.asBinder() == this.mSecondary.token.asBinder()) {
            this.mSecondary = runningTaskInfo;
        }
        if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
            boolean z5 = this.mPrimary.topActivityType == 0;
            int i2 = this.mSecondary.topActivityType;
            boolean z6 = i2 == 0;
            if (i2 != 2 && (i2 != 3 || !this.mSplitScreenController.isHomeStackResizable())) {
                z = false;
            }
            if (z5 != z3 || z4 != z6 || z2 != z) {
                if (z5 || z6) {
                    if (this.mSplitScreenController.isDividerVisible()) {
                        this.mSplitScreenController.startDismissSplit(false);
                    } else if (!z5 && z3 && z4) {
                        this.mSplitScreenController.startEnterSplit();
                    }
                } else if (z) {
                    ArrayList arrayList = new ArrayList();
                    this.mSplitScreenController.getWmProxy().getHomeAndRecentsTasks(arrayList, this.mSplitScreenController.getSecondaryRoot());
                    for (int i3 = 0; i3 < arrayList.size(); i3++) {
                        ActivityManager.RunningTaskInfo runningTaskInfo2 = (ActivityManager.RunningTaskInfo) arrayList.get(i3);
                        SurfaceControl surfaceControl = this.mLeashByTaskId.get(runningTaskInfo2.taskId);
                        if (surfaceControl != null) {
                            updateChildTaskSurface(runningTaskInfo2, surfaceControl, false);
                        }
                    }
                    this.mSplitScreenController.ensureMinimizedSplit();
                } else {
                    this.mSplitScreenController.ensureNormalSplit();
                }
            }
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
        if (this.mLeashByTaskId.contains(i)) {
            builder.setParent(this.mLeashByTaskId.get(i));
            return;
        }
        throw new IllegalArgumentException("There is no surface for taskId=" + i);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + this);
        printWriter.println(str2 + "mSplitScreenSupported=" + this.mSplitScreenSupported);
        if (this.mPrimary != null) {
            printWriter.println(str2 + "mPrimary.taskId=" + this.mPrimary.taskId);
        }
        if (this.mSecondary != null) {
            printWriter.println(str2 + "mSecondary.taskId=" + this.mSecondary.taskId);
        }
    }

    public String toString() {
        return TAG;
    }
}
