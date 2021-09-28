package com.android.wm.shell.apppairs;

import android.app.ActivityManager;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class AppPairsController {
    private static final String TAG = "AppPairsController";
    private final DisplayController mDisplayController;
    private final DisplayImeController mDisplayImeController;
    private final ShellExecutor mMainExecutor;
    private AppPairsPool mPairsPool;
    private final SyncTransactionQueue mSyncQueue;
    private final ShellTaskOrganizer mTaskOrganizer;
    private final AppPairsImpl mImpl = new AppPairsImpl();
    private final SparseArray<AppPair> mActiveAppPairs = new SparseArray<>();

    public AppPairsController(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, DisplayController displayController, ShellExecutor shellExecutor, DisplayImeController displayImeController) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mSyncQueue = syncTransactionQueue;
        this.mDisplayController = displayController;
        this.mDisplayImeController = displayImeController;
        this.mMainExecutor = shellExecutor;
    }

    public AppPairs asAppPairs() {
        return this.mImpl;
    }

    public void onOrganizerRegistered() {
        if (this.mPairsPool == null) {
            setPairsPool(new AppPairsPool(this));
        }
    }

    @VisibleForTesting
    public void setPairsPool(AppPairsPool appPairsPool) {
        this.mPairsPool = appPairsPool;
    }

    public boolean pair(int i, int i2) {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskOrganizer.getRunningTaskInfo(i);
        ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mTaskOrganizer.getRunningTaskInfo(i2);
        if (runningTaskInfo == null || runningTaskInfo2 == null) {
            return false;
        }
        return pair(runningTaskInfo, runningTaskInfo2);
    }

    public boolean pair(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        return pairInner(runningTaskInfo, runningTaskInfo2) != null;
    }

    @VisibleForTesting
    public AppPair pairInner(ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        AppPair acquire = this.mPairsPool.acquire();
        if (!acquire.pair(runningTaskInfo, runningTaskInfo2)) {
            this.mPairsPool.release(acquire);
            return null;
        }
        this.mActiveAppPairs.put(acquire.getRootTaskId(), acquire);
        return acquire;
    }

    public void unpair(int i) {
        unpair(i, true);
    }

    public void unpair(int i, boolean z) {
        AppPair appPair = this.mActiveAppPairs.get(i);
        if (appPair == null) {
            int size = this.mActiveAppPairs.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                AppPair valueAt = this.mActiveAppPairs.valueAt(size);
                if (valueAt.contains(i)) {
                    appPair = valueAt;
                    break;
                }
                size--;
            }
        }
        if (appPair != null) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -234284913, 1, null, Long.valueOf((long) i), String.valueOf(appPair));
            }
            this.mActiveAppPairs.remove(appPair.getRootTaskId());
            appPair.unpair();
            if (z) {
                this.mPairsPool.release(appPair);
            }
        } else if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 950299522, 1, null, Long.valueOf((long) i));
        }
    }

    /* access modifiers changed from: package-private */
    public ShellTaskOrganizer getTaskOrganizer() {
        return this.mTaskOrganizer;
    }

    /* access modifiers changed from: package-private */
    public SyncTransactionQueue getSyncTransactionQueue() {
        return this.mSyncQueue;
    }

    /* access modifiers changed from: package-private */
    public DisplayController getDisplayController() {
        return this.mDisplayController;
    }

    /* access modifiers changed from: package-private */
    public DisplayImeController getDisplayImeController() {
        return this.mDisplayImeController;
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = (str + "  ") + "  ";
        printWriter.println(str + this);
        for (int size = this.mActiveAppPairs.size() + -1; size >= 0; size--) {
            this.mActiveAppPairs.valueAt(size).dump(printWriter, str2);
        }
        AppPairsPool appPairsPool = this.mPairsPool;
        if (appPairsPool != null) {
            appPairsPool.dump(printWriter, str);
        }
    }

    public String toString() {
        return TAG + "#" + this.mActiveAppPairs.size();
    }

    /* loaded from: classes2.dex */
    private class AppPairsImpl implements AppPairs {
        private AppPairsImpl() {
        }
    }
}
