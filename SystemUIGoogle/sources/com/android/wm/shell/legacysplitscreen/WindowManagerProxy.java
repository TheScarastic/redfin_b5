package com.android.wm.shell.legacysplitscreen;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.WindowManagerGlobal;
import android.window.TaskOrganizer;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
/* loaded from: classes2.dex */
public class WindowManagerProxy {
    private final SyncTransactionQueue mSyncTransactionQueue;
    private final TaskOrganizer mTaskOrganizer;
    private static final int[] HOME_AND_RECENTS = {2, 3};
    private static final int[] CONTROLLED_ACTIVITY_TYPES = {1, 2, 3, 0};
    private static final int[] CONTROLLED_WINDOWING_MODES = {1, 4, 0};
    @GuardedBy({"mDockedRect"})
    private final Rect mDockedRect = new Rect();
    private final Rect mTmpRect1 = new Rect();
    @GuardedBy({"mDockedRect"})
    private final Rect mTouchableRegion = new Rect();

    public WindowManagerProxy(SyncTransactionQueue syncTransactionQueue, TaskOrganizer taskOrganizer) {
        this.mSyncTransactionQueue = syncTransactionQueue;
        this.mTaskOrganizer = taskOrganizer;
    }

    public void dismissOrMaximizeDocked(LegacySplitScreenTaskListener legacySplitScreenTaskListener, LegacySplitDisplayLayout legacySplitDisplayLayout, boolean z) {
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            legacySplitScreenTaskListener.mSplitScreenController.startDismissSplit(!z, true);
        } else {
            applyDismissSplit(legacySplitScreenTaskListener, legacySplitDisplayLayout, z);
        }
    }

    public void setResizing(boolean z) {
        try {
            ActivityTaskManager.getService().setSplitScreenResizing(z);
        } catch (RemoteException e) {
            Log.w("WindowManagerProxy", "Error calling setDockedStackResizing: " + e);
        }
    }

    public void setTouchRegion(Rect rect) {
        try {
            synchronized (this.mDockedRect) {
                this.mTouchableRegion.set(rect);
            }
            WindowManagerGlobal.getWindowManagerService().setDockedTaskDividerTouchRegion(this.mTouchableRegion);
        } catch (RemoteException e) {
            Log.w("WindowManagerProxy", "Failed to set touchable region: " + e);
        }
    }

    public void applyResizeSplits(int i, LegacySplitDisplayLayout legacySplitDisplayLayout) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        legacySplitDisplayLayout.resizeSplits(i, windowContainerTransaction);
        applySyncTransaction(windowContainerTransaction);
    }

    public boolean getHomeAndRecentsTasks(List<ActivityManager.RunningTaskInfo> list, WindowContainerToken windowContainerToken) {
        List list2;
        if (windowContainerToken == null) {
            list2 = this.mTaskOrganizer.getRootTasks(0, HOME_AND_RECENTS);
        } else {
            list2 = this.mTaskOrganizer.getChildTasks(windowContainerToken, HOME_AND_RECENTS);
        }
        int size = list2.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) list2.get(i);
            list.add(runningTaskInfo);
            if (runningTaskInfo.topActivityType == 2) {
                z = runningTaskInfo.isResizeable;
            }
        }
        return z;
    }

    public boolean applyHomeTasksMinimized(LegacySplitDisplayLayout legacySplitDisplayLayout, WindowContainerToken windowContainerToken, WindowContainerTransaction windowContainerTransaction) {
        Rect rect;
        ArrayList arrayList = new ArrayList();
        boolean homeAndRecentsTasks = getHomeAndRecentsTasks(arrayList, windowContainerToken);
        if (homeAndRecentsTasks) {
            rect = legacySplitDisplayLayout.calcResizableMinimizedHomeStackBounds();
        } else {
            boolean z = false;
            rect = new Rect(0, 0, 0, 0);
            int size = arrayList.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                } else if (((ActivityManager.RunningTaskInfo) arrayList.get(size)).topActivityType == 2) {
                    int i = ((ActivityManager.RunningTaskInfo) arrayList.get(size)).configuration.orientation;
                    boolean isLandscape = legacySplitDisplayLayout.mDisplayLayout.isLandscape();
                    if (i == 2 || (i == 0 && isLandscape)) {
                        z = true;
                    }
                    rect.right = z == isLandscape ? legacySplitDisplayLayout.mDisplayLayout.width() : legacySplitDisplayLayout.mDisplayLayout.height();
                    rect.bottom = z == isLandscape ? legacySplitDisplayLayout.mDisplayLayout.height() : legacySplitDisplayLayout.mDisplayLayout.width();
                } else {
                    size--;
                }
            }
        }
        for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
            if (!homeAndRecentsTasks) {
                if (((ActivityManager.RunningTaskInfo) arrayList.get(size2)).topActivityType != 3) {
                    windowContainerTransaction.setWindowingMode(((ActivityManager.RunningTaskInfo) arrayList.get(size2)).token, 1);
                }
            }
            windowContainerTransaction.setBounds(((ActivityManager.RunningTaskInfo) arrayList.get(size2)).token, rect);
        }
        legacySplitDisplayLayout.mTiles.mHomeBounds.set(rect);
        return homeAndRecentsTasks;
    }

    public boolean applyEnterSplit(LegacySplitScreenTaskListener legacySplitScreenTaskListener, LegacySplitDisplayLayout legacySplitDisplayLayout) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.setLaunchRoot(legacySplitScreenTaskListener.mSecondary.token, CONTROLLED_WINDOWING_MODES, CONTROLLED_ACTIVITY_TYPES);
        boolean buildEnterSplit = buildEnterSplit(windowContainerTransaction, legacySplitScreenTaskListener, legacySplitDisplayLayout);
        applySyncTransaction(windowContainerTransaction);
        return buildEnterSplit;
    }

    public boolean buildEnterSplit(WindowContainerTransaction windowContainerTransaction, LegacySplitScreenTaskListener legacySplitScreenTaskListener, LegacySplitDisplayLayout legacySplitDisplayLayout) {
        List rootTasks = this.mTaskOrganizer.getRootTasks(0, (int[]) null);
        if (rootTasks.isEmpty()) {
            return false;
        }
        ActivityManager.RunningTaskInfo runningTaskInfo = null;
        for (int size = rootTasks.size() - 1; size >= 0; size--) {
            ActivityManager.RunningTaskInfo runningTaskInfo2 = (ActivityManager.RunningTaskInfo) rootTasks.get(size);
            if (runningTaskInfo2.supportsMultiWindow || runningTaskInfo2.topActivityType == 2) {
                int windowingMode = runningTaskInfo2.getWindowingMode();
                if (ArrayUtils.contains(CONTROLLED_WINDOWING_MODES, windowingMode) && ArrayUtils.contains(CONTROLLED_ACTIVITY_TYPES, runningTaskInfo2.getActivityType()) && windowingMode != 4) {
                    runningTaskInfo = isHomeOrRecentTask(runningTaskInfo2) ? runningTaskInfo2 : null;
                    windowContainerTransaction.reparent(runningTaskInfo2.token, legacySplitScreenTaskListener.mSecondary.token, true);
                }
            }
        }
        windowContainerTransaction.reorder(legacySplitScreenTaskListener.mSecondary.token, true);
        boolean applyHomeTasksMinimized = applyHomeTasksMinimized(legacySplitDisplayLayout, null, windowContainerTransaction);
        if (runningTaskInfo != null && !Transitions.ENABLE_SHELL_TRANSITIONS) {
            windowContainerTransaction.setBoundsChangeTransaction(runningTaskInfo.token, legacySplitScreenTaskListener.mHomeBounds);
        }
        return applyHomeTasksMinimized;
    }

    static boolean isHomeOrRecentTask(ActivityManager.RunningTaskInfo runningTaskInfo) {
        int activityType = runningTaskInfo.getActivityType();
        return activityType == 2 || activityType == 3;
    }

    public void applyDismissSplit(LegacySplitScreenTaskListener legacySplitScreenTaskListener, LegacySplitDisplayLayout legacySplitDisplayLayout, boolean z) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.setLaunchRoot(legacySplitScreenTaskListener.mSecondary.token, (int[]) null, (int[]) null);
        buildDismissSplit(windowContainerTransaction, legacySplitScreenTaskListener, legacySplitDisplayLayout, z);
        applySyncTransaction(windowContainerTransaction);
    }

    public static void buildDismissSplit(WindowContainerTransaction windowContainerTransaction, LegacySplitScreenTaskListener legacySplitScreenTaskListener, LegacySplitDisplayLayout legacySplitDisplayLayout, boolean z) {
        int i;
        int i2;
        TaskOrganizer taskOrganizer = legacySplitScreenTaskListener.getTaskOrganizer();
        List childTasks = taskOrganizer.getChildTasks(legacySplitScreenTaskListener.mPrimary.token, (int[]) null);
        List childTasks2 = taskOrganizer.getChildTasks(legacySplitScreenTaskListener.mSecondary.token, (int[]) null);
        List rootTasks = taskOrganizer.getRootTasks(0, HOME_AND_RECENTS);
        rootTasks.removeIf(new Predicate() { // from class: com.android.wm.shell.legacysplitscreen.WindowManagerProxy$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return WindowManagerProxy.$r8$lambda$47w38wy_iigsOkn3kFhRTExSL3k(LegacySplitScreenTaskListener.this, (ActivityManager.RunningTaskInfo) obj);
            }
        });
        if (!childTasks.isEmpty() || !childTasks2.isEmpty() || !rootTasks.isEmpty()) {
            if (z) {
                for (int size = childTasks.size() - 1; size >= 0; size--) {
                    windowContainerTransaction.reparent(((ActivityManager.RunningTaskInfo) childTasks.get(size)).token, (WindowContainerToken) null, true);
                }
                boolean z2 = false;
                for (int size2 = childTasks2.size() - 1; size2 >= 0; size2--) {
                    ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) childTasks2.get(size2);
                    windowContainerTransaction.reparent(runningTaskInfo.token, (WindowContainerToken) null, true);
                    if (isHomeOrRecentTask(runningTaskInfo)) {
                        windowContainerTransaction.setBounds(runningTaskInfo.token, (Rect) null);
                        windowContainerTransaction.setWindowingMode(runningTaskInfo.token, 0);
                        if (size2 == 0) {
                            z2 = true;
                        }
                    }
                }
                if (z2 && !Transitions.ENABLE_SHELL_TRANSITIONS) {
                    boolean isLandscape = legacySplitDisplayLayout.mDisplayLayout.isLandscape();
                    if (isLandscape) {
                        i = legacySplitDisplayLayout.mSecondary.left - legacySplitScreenTaskListener.mHomeBounds.left;
                    } else {
                        i = legacySplitDisplayLayout.mSecondary.left;
                    }
                    if (isLandscape) {
                        i2 = legacySplitDisplayLayout.mSecondary.top;
                    } else {
                        i2 = legacySplitDisplayLayout.mSecondary.top - legacySplitScreenTaskListener.mHomeBounds.top;
                    }
                    SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                    transaction.setPosition(legacySplitScreenTaskListener.mSecondarySurface, (float) i, (float) i2);
                    Rect rect = new Rect(0, 0, legacySplitDisplayLayout.mDisplayLayout.width(), legacySplitDisplayLayout.mDisplayLayout.height());
                    rect.offset(-i, -i2);
                    transaction.setWindowCrop(legacySplitScreenTaskListener.mSecondarySurface, rect);
                    windowContainerTransaction.setBoundsChangeTransaction(legacySplitScreenTaskListener.mSecondary.token, transaction);
                }
            } else {
                for (int size3 = childTasks2.size() - 1; size3 >= 0; size3--) {
                    if (!isHomeOrRecentTask((ActivityManager.RunningTaskInfo) childTasks2.get(size3))) {
                        windowContainerTransaction.reparent(((ActivityManager.RunningTaskInfo) childTasks2.get(size3)).token, (WindowContainerToken) null, true);
                    }
                }
                for (int size4 = childTasks2.size() - 1; size4 >= 0; size4--) {
                    ActivityManager.RunningTaskInfo runningTaskInfo2 = (ActivityManager.RunningTaskInfo) childTasks2.get(size4);
                    if (isHomeOrRecentTask(runningTaskInfo2)) {
                        windowContainerTransaction.reparent(runningTaskInfo2.token, (WindowContainerToken) null, true);
                        windowContainerTransaction.setBounds(runningTaskInfo2.token, (Rect) null);
                        windowContainerTransaction.setWindowingMode(runningTaskInfo2.token, 0);
                    }
                }
                for (int size5 = childTasks.size() - 1; size5 >= 0; size5--) {
                    windowContainerTransaction.reparent(((ActivityManager.RunningTaskInfo) childTasks.get(size5)).token, (WindowContainerToken) null, true);
                }
            }
            for (int size6 = rootTasks.size() - 1; size6 >= 0; size6--) {
                windowContainerTransaction.setBounds(((ActivityManager.RunningTaskInfo) rootTasks.get(size6)).token, (Rect) null);
                windowContainerTransaction.setWindowingMode(((ActivityManager.RunningTaskInfo) rootTasks.get(size6)).token, 0);
            }
            windowContainerTransaction.setFocusable(legacySplitScreenTaskListener.mPrimary.token, true);
        }
    }

    public static /* synthetic */ boolean lambda$buildDismissSplit$0(LegacySplitScreenTaskListener legacySplitScreenTaskListener, ActivityManager.RunningTaskInfo runningTaskInfo) {
        return runningTaskInfo.token.equals(legacySplitScreenTaskListener.mSecondary.token) || runningTaskInfo.token.equals(legacySplitScreenTaskListener.mPrimary.token);
    }

    public void applySyncTransaction(WindowContainerTransaction windowContainerTransaction) {
        this.mSyncTransactionQueue.queue(windowContainerTransaction);
    }

    public boolean queueSyncTransactionIfWaiting(WindowContainerTransaction windowContainerTransaction) {
        return this.mSyncTransactionQueue.queueIfWaiting(windowContainerTransaction);
    }

    public void runInSync(SyncTransactionQueue.TransactionRunnable transactionRunnable) {
        this.mSyncTransactionQueue.runInSync(transactionRunnable);
    }
}
