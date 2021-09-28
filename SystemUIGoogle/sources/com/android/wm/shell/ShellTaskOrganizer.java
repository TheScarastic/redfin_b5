package com.android.wm.shell;

import android.app.ActivityManager;
import android.app.TaskInfo;
import android.content.Context;
import android.content.LocusId;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceControl;
import android.window.ITaskOrganizerController;
import android.window.StartingWindowInfo;
import android.window.TaskAppearedInfo;
import android.window.TaskOrganizer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.common.ScreenshotUtils;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.sizecompatui.SizeCompatUIController;
import com.android.wm.shell.startingsurface.StartingWindowController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class ShellTaskOrganizer extends TaskOrganizer implements SizeCompatUIController.SizeCompatUICallback {
    private final ArrayMap<IBinder, TaskListener> mLaunchCookieToListener;
    private final Object mLock;
    private final ArraySet<LocusIdListener> mLocusIdListeners;
    private final SizeCompatUIController mSizeCompatUI;
    private StartingWindowController mStartingWindow;
    private final SparseArray<TaskListener> mTaskListeners;
    private final SparseArray<TaskAppearedInfo> mTasks;
    private final SparseArray<LocusId> mVisibleTasksWithLocusId;

    /* loaded from: classes2.dex */
    public interface LocusIdListener {
        void onVisibilityChanged(int i, LocusId locusId, boolean z);
    }

    /* loaded from: classes2.dex */
    public @interface TaskListenerType {
    }

    /* loaded from: classes2.dex */
    public interface TaskListener {
        default void dump(PrintWriter printWriter, String str) {
        }

        default void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        default void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        }

        default void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        default void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        default boolean supportSizeCompatUI() {
            return true;
        }

        default void attachChildSurfaceToTask(int i, SurfaceControl.Builder builder) {
            throw new IllegalStateException("This task listener doesn't support child surface attachment.");
        }
    }

    public ShellTaskOrganizer(ShellExecutor shellExecutor, Context context, SizeCompatUIController sizeCompatUIController) {
        this(null, shellExecutor, context, sizeCompatUIController);
    }

    @VisibleForTesting
    ShellTaskOrganizer(ITaskOrganizerController iTaskOrganizerController, ShellExecutor shellExecutor, Context context, SizeCompatUIController sizeCompatUIController) {
        super(iTaskOrganizerController, shellExecutor);
        this.mTaskListeners = new SparseArray<>();
        this.mTasks = new SparseArray<>();
        this.mLaunchCookieToListener = new ArrayMap<>();
        this.mVisibleTasksWithLocusId = new SparseArray<>();
        this.mLocusIdListeners = new ArraySet<>();
        this.mLock = new Object();
        this.mSizeCompatUI = sizeCompatUIController;
        if (sizeCompatUIController != null) {
            sizeCompatUIController.setSizeCompatUICallback(this);
        }
    }

    public List<TaskAppearedInfo> registerOrganizer() {
        List<TaskAppearedInfo> registerOrganizer;
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 580605218, 0, null, null);
            }
            registerOrganizer = ShellTaskOrganizer.super.registerOrganizer();
            for (int i = 0; i < registerOrganizer.size(); i++) {
                TaskAppearedInfo taskAppearedInfo = registerOrganizer.get(i);
                if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1683614271, 1, null, Long.valueOf((long) taskAppearedInfo.getTaskInfo().taskId), String.valueOf(taskAppearedInfo.getTaskInfo().baseIntent));
                }
                onTaskAppeared(taskAppearedInfo);
            }
        }
        return registerOrganizer;
    }

    public void createRootTask(int i, int i2, TaskListener taskListener) {
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1312360667, 5, null, Long.valueOf((long) i), Long.valueOf((long) i2), String.valueOf(taskListener.toString()));
        }
        Binder binder = new Binder();
        setPendingLaunchCookieListener(binder, taskListener);
        ShellTaskOrganizer.super.createRootTask(i, i2, binder);
    }

    public void initStartingWindow(StartingWindowController startingWindowController) {
        this.mStartingWindow = startingWindowController;
    }

    public void addListenerForType(TaskListener taskListener, @TaskListenerType int... iArr) {
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 1990759023, 0, null, String.valueOf(Arrays.toString(iArr)), String.valueOf(taskListener));
            }
            for (int i : iArr) {
                if (this.mTaskListeners.get(i) == null) {
                    this.mTaskListeners.put(i, taskListener);
                    for (int size = this.mTasks.size() - 1; size >= 0; size--) {
                        TaskAppearedInfo valueAt = this.mTasks.valueAt(size);
                        if (getTaskListener(valueAt.getTaskInfo()) == taskListener) {
                            taskListener.onTaskAppeared(valueAt.getTaskInfo(), valueAt.getLeash());
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Listener for listenerType=" + i + " already exists");
                }
            }
        }
    }

    public void removeListener(TaskListener taskListener) {
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1340279385, 0, null, String.valueOf(taskListener));
            }
            int indexOfValue = this.mTaskListeners.indexOfValue(taskListener);
            if (indexOfValue == -1) {
                Log.w("ShellTaskOrganizer", "No registered listener found");
                return;
            }
            ArrayList arrayList = new ArrayList();
            for (int size = this.mTasks.size() - 1; size >= 0; size--) {
                TaskAppearedInfo valueAt = this.mTasks.valueAt(size);
                if (getTaskListener(valueAt.getTaskInfo()) == taskListener) {
                    arrayList.add(valueAt);
                }
            }
            this.mTaskListeners.removeAt(indexOfValue);
            for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                TaskAppearedInfo taskAppearedInfo = (TaskAppearedInfo) arrayList.get(size2);
                updateTaskListenerIfNeeded(taskAppearedInfo.getTaskInfo(), taskAppearedInfo.getLeash(), null, getTaskListener(taskAppearedInfo.getTaskInfo()));
            }
        }
    }

    public void setPendingLaunchCookieListener(IBinder iBinder, TaskListener taskListener) {
        synchronized (this.mLock) {
            this.mLaunchCookieToListener.put(iBinder, taskListener);
        }
    }

    public void addLocusIdListener(LocusIdListener locusIdListener) {
        synchronized (this.mLock) {
            this.mLocusIdListeners.add(locusIdListener);
            for (int i = 0; i < this.mVisibleTasksWithLocusId.size(); i++) {
                locusIdListener.onVisibilityChanged(this.mVisibleTasksWithLocusId.keyAt(i), this.mVisibleTasksWithLocusId.valueAt(i), true);
            }
        }
    }

    public void addStartingWindow(StartingWindowInfo startingWindowInfo, IBinder iBinder) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.addStartingWindow(startingWindowInfo, iBinder);
        }
    }

    public void removeStartingWindow(int i, SurfaceControl surfaceControl, Rect rect, boolean z) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.removeStartingWindow(i, surfaceControl, rect, z);
        }
    }

    public void copySplashScreenView(int i) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.copySplashScreenView(i);
        }
    }

    public void onAppSplashScreenViewRemoved(int i) {
        StartingWindowController startingWindowController = this.mStartingWindow;
        if (startingWindowController != null) {
            startingWindowController.onAppSplashScreenViewRemoved(i);
        }
    }

    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        synchronized (this.mLock) {
            onTaskAppeared(new TaskAppearedInfo(runningTaskInfo, surfaceControl));
        }
    }

    private void onTaskAppeared(TaskAppearedInfo taskAppearedInfo) {
        int i = taskAppearedInfo.getTaskInfo().taskId;
        this.mTasks.put(i, taskAppearedInfo);
        TaskListener taskListener = getTaskListener(taskAppearedInfo.getTaskInfo(), true);
        if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            long j = (long) i;
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -1325223370, 1, null, Long.valueOf(j), String.valueOf(taskListener));
        }
        if (taskListener != null) {
            taskListener.onTaskAppeared(taskAppearedInfo.getTaskInfo(), taskAppearedInfo.getLeash());
        }
        notifyLocusVisibilityIfNeeded(taskAppearedInfo.getTaskInfo());
        notifySizeCompatUI(taskAppearedInfo.getTaskInfo(), taskListener);
    }

    public void screenshotTask(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, Consumer<SurfaceControl.ScreenshotHardwareBuffer> consumer) {
        TaskAppearedInfo taskAppearedInfo = this.mTasks.get(runningTaskInfo.taskId);
        if (taskAppearedInfo != null) {
            ScreenshotUtils.captureLayer(taskAppearedInfo.getLeash(), rect, consumer);
        }
    }

    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 157713005, 1, null, Long.valueOf((long) runningTaskInfo.taskId));
            }
            TaskAppearedInfo taskAppearedInfo = this.mTasks.get(runningTaskInfo.taskId);
            TaskListener taskListener = getTaskListener(taskAppearedInfo.getTaskInfo());
            TaskListener taskListener2 = getTaskListener(runningTaskInfo);
            this.mTasks.put(runningTaskInfo.taskId, new TaskAppearedInfo(runningTaskInfo, taskAppearedInfo.getLeash()));
            boolean updateTaskListenerIfNeeded = updateTaskListenerIfNeeded(runningTaskInfo, taskAppearedInfo.getLeash(), taskListener, taskListener2);
            if (!updateTaskListenerIfNeeded && taskListener2 != null) {
                taskListener2.onTaskInfoChanged(runningTaskInfo);
            }
            notifyLocusVisibilityIfNeeded(runningTaskInfo);
            if (updateTaskListenerIfNeeded || !runningTaskInfo.equalsForSizeCompat(taskAppearedInfo.getTaskInfo())) {
                notifySizeCompatUI(runningTaskInfo, taskListener2);
            }
        }
    }

    public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, 980952660, 1, null, Long.valueOf((long) runningTaskInfo.taskId));
            }
            TaskListener taskListener = getTaskListener(runningTaskInfo);
            if (taskListener != null) {
                taskListener.onBackPressedOnTaskRoot(runningTaskInfo);
            }
        }
    }

    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        synchronized (this.mLock) {
            if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -880817403, 1, null, Long.valueOf((long) runningTaskInfo.taskId));
            }
            int i = runningTaskInfo.taskId;
            TaskListener taskListener = getTaskListener(this.mTasks.get(i).getTaskInfo());
            this.mTasks.remove(i);
            if (taskListener != null) {
                taskListener.onTaskVanished(runningTaskInfo);
            }
            notifyLocusVisibilityIfNeeded(runningTaskInfo);
            notifySizeCompatUI(runningTaskInfo, null);
        }
    }

    public ActivityManager.RunningTaskInfo getRunningTaskInfo(int i) {
        ActivityManager.RunningTaskInfo taskInfo;
        synchronized (this.mLock) {
            TaskAppearedInfo taskAppearedInfo = this.mTasks.get(i);
            taskInfo = taskAppearedInfo != null ? taskAppearedInfo.getTaskInfo() : null;
        }
        return taskInfo;
    }

    public void setSurfaceMetadata(int i, int i2, int i3) {
        synchronized (this.mLock) {
            TaskAppearedInfo taskAppearedInfo = this.mTasks.get(i);
            if (!(taskAppearedInfo == null || taskAppearedInfo.getLeash() == null)) {
                SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                transaction.setMetadata(taskAppearedInfo.getLeash(), i2, i3);
                transaction.apply();
            }
        }
    }

    private boolean updateTaskListenerIfNeeded(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl, TaskListener taskListener, TaskListener taskListener2) {
        if (taskListener == taskListener2) {
            return false;
        }
        if (taskListener != null) {
            taskListener.onTaskVanished(runningTaskInfo);
        }
        if (taskListener2 == null) {
            return true;
        }
        taskListener2.onTaskAppeared(runningTaskInfo, surfaceControl);
        return true;
    }

    private void notifyLocusVisibilityIfNeeded(TaskInfo taskInfo) {
        int i = taskInfo.taskId;
        LocusId locusId = this.mVisibleTasksWithLocusId.get(i);
        boolean equals = Objects.equals(locusId, taskInfo.mTopActivityLocusId);
        if (locusId == null) {
            LocusId locusId2 = taskInfo.mTopActivityLocusId;
            if (locusId2 != null && taskInfo.isVisible) {
                this.mVisibleTasksWithLocusId.put(i, locusId2);
                notifyLocusIdChange(i, taskInfo.mTopActivityLocusId, true);
            }
        } else if (equals && !taskInfo.isVisible) {
            this.mVisibleTasksWithLocusId.remove(i);
            notifyLocusIdChange(i, taskInfo.mTopActivityLocusId, false);
        } else if (equals) {
        } else {
            if (taskInfo.isVisible) {
                this.mVisibleTasksWithLocusId.put(i, taskInfo.mTopActivityLocusId);
                notifyLocusIdChange(i, locusId, false);
                notifyLocusIdChange(i, taskInfo.mTopActivityLocusId, true);
                return;
            }
            this.mVisibleTasksWithLocusId.remove(taskInfo.taskId);
            notifyLocusIdChange(i, locusId, false);
        }
    }

    private void notifyLocusIdChange(int i, LocusId locusId, boolean z) {
        for (int i2 = 0; i2 < this.mLocusIdListeners.size(); i2++) {
            this.mLocusIdListeners.valueAt(i2).onVisibilityChanged(i, locusId, z);
        }
    }

    @Override // com.android.wm.shell.sizecompatui.SizeCompatUIController.SizeCompatUICallback
    public void onSizeCompatRestartButtonClicked(int i) {
        TaskAppearedInfo taskAppearedInfo;
        synchronized (this.mLock) {
            taskAppearedInfo = this.mTasks.get(i);
        }
        if (taskAppearedInfo != null) {
            restartTaskTopActivityProcessIfVisible(taskAppearedInfo.getTaskInfo().token);
        }
    }

    private void notifySizeCompatUI(ActivityManager.RunningTaskInfo runningTaskInfo, TaskListener taskListener) {
        if (this.mSizeCompatUI != null) {
            if (taskListener == null || !taskListener.supportSizeCompatUI() || !runningTaskInfo.topActivityInSizeCompat || !runningTaskInfo.isVisible) {
                this.mSizeCompatUI.onSizeCompatInfoChanged(runningTaskInfo.displayId, runningTaskInfo.taskId, null, null);
            } else {
                this.mSizeCompatUI.onSizeCompatInfoChanged(runningTaskInfo.displayId, runningTaskInfo.taskId, runningTaskInfo.configuration, taskListener);
            }
        }
    }

    private TaskListener getTaskListener(ActivityManager.RunningTaskInfo runningTaskInfo) {
        return getTaskListener(runningTaskInfo, false);
    }

    private TaskListener getTaskListener(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z) {
        TaskListener taskListener;
        int i = runningTaskInfo.taskId;
        ArrayList arrayList = runningTaskInfo.launchCookies;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            IBinder iBinder = (IBinder) arrayList.get(size);
            TaskListener taskListener2 = this.mLaunchCookieToListener.get(iBinder);
            if (taskListener2 != null) {
                if (z) {
                    this.mLaunchCookieToListener.remove(iBinder);
                    this.mTaskListeners.put(i, taskListener2);
                }
                return taskListener2;
            }
        }
        TaskListener taskListener3 = this.mTaskListeners.get(i);
        if (taskListener3 != null) {
            return taskListener3;
        }
        if (!runningTaskInfo.hasParentTask() || (taskListener = this.mTaskListeners.get(runningTaskInfo.parentTaskId)) == null) {
            return this.mTaskListeners.get(taskInfoToTaskListenerType(runningTaskInfo));
        }
        return taskListener;
    }

    @TaskListenerType
    @VisibleForTesting
    static int taskInfoToTaskListenerType(ActivityManager.RunningTaskInfo runningTaskInfo) {
        int windowingMode = runningTaskInfo.getWindowingMode();
        if (windowingMode == 1) {
            return -2;
        }
        if (windowingMode != 2) {
            return windowingMode != 6 ? -1 : -3;
        }
        return -4;
    }

    public static String taskListenerTypeToString(@TaskListenerType int i) {
        if (i == -4) {
            return "TASK_LISTENER_TYPE_PIP";
        }
        if (i == -3) {
            return "TASK_LISTENER_TYPE_MULTI_WINDOW";
        }
        if (i == -2) {
            return "TASK_LISTENER_TYPE_FULLSCREEN";
        }
        if (i == -1) {
            return "TASK_LISTENER_TYPE_UNDEFINED";
        }
        return "taskId#" + i;
    }

    public void dump(PrintWriter printWriter, String str) {
        synchronized (this.mLock) {
            String str2 = str + "  ";
            String str3 = str2 + "  ";
            printWriter.println(str + "ShellTaskOrganizer");
            printWriter.println(str2 + this.mTaskListeners.size() + " Listeners");
            for (int size = this.mTaskListeners.size() + -1; size >= 0; size += -1) {
                int keyAt = this.mTaskListeners.keyAt(size);
                printWriter.println(str2 + "#" + size + " " + taskListenerTypeToString(keyAt));
                this.mTaskListeners.valueAt(size).dump(printWriter, str3);
            }
            printWriter.println();
            printWriter.println(str2 + this.mTasks.size() + " Tasks");
            for (int size2 = this.mTasks.size() + -1; size2 >= 0; size2 += -1) {
                printWriter.println(str2 + "#" + size2 + " task=" + this.mTasks.keyAt(size2) + " listener=" + getTaskListener(this.mTasks.valueAt(size2).getTaskInfo()));
            }
            printWriter.println();
            printWriter.println(str2 + this.mLaunchCookieToListener.size() + " Launch Cookies");
            for (int size3 = this.mLaunchCookieToListener.size() + -1; size3 >= 0; size3 += -1) {
                printWriter.println(str2 + "#" + size3 + " cookie=" + this.mLaunchCookieToListener.keyAt(size3) + " listener=" + this.mLaunchCookieToListener.valueAt(size3));
            }
        }
    }
}
