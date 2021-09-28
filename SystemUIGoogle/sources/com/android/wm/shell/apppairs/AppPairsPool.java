package com.android.wm.shell.apppairs;

import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.io.PrintWriter;
import java.util.ArrayList;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class AppPairsPool {
    private static final String TAG = "AppPairsPool";
    @VisibleForTesting
    final AppPairsController mController;
    private final ArrayList<AppPair> mPool = new ArrayList<>();

    /* access modifiers changed from: package-private */
    public AppPairsPool(AppPairsController appPairsController) {
        this.mController = appPairsController;
        incrementPool();
    }

    /* access modifiers changed from: package-private */
    public AppPair acquire() {
        ArrayList<AppPair> arrayList = this.mPool;
        AppPair remove = arrayList.remove(arrayList.size() - 1);
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 2006473416, 16, null, String.valueOf(remove.getRootTaskId()), String.valueOf(remove), Long.valueOf((long) this.mPool.size()));
        }
        if (this.mPool.size() == 0) {
            incrementPool();
        }
        return remove;
    }

    /* access modifiers changed from: package-private */
    public void release(AppPair appPair) {
        this.mPool.add(appPair);
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 1891981945, 16, null, String.valueOf(appPair.getRootTaskId()), String.valueOf(appPair), Long.valueOf((long) this.mPool.size()));
        }
    }

    @VisibleForTesting
    void incrementPool() {
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 1079041527, 1, null, Long.valueOf((long) this.mPool.size()));
        }
        AppPair appPair = new AppPair(this.mController);
        this.mController.getTaskOrganizer().createRootTask(0, 1, appPair);
        this.mPool.add(appPair);
    }

    @VisibleForTesting
    int poolSize() {
        return this.mPool.size();
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = (str + "  ") + "  ";
        printWriter.println(str + this);
        for (int size = this.mPool.size() + -1; size >= 0; size--) {
            this.mPool.get(size).dump(printWriter, str2);
        }
    }

    public String toString() {
        return TAG + "#" + this.mPool.size();
    }
}
