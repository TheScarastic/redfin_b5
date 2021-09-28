package com.android.wm.shell.common;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.IActivityTaskManager;
import android.app.TaskStackListener;
import android.content.ComponentName;
import android.os.Handler;
import android.os.Message;
import android.os.Trace;
import android.util.Log;
import android.window.TaskSnapshot;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class TaskStackListenerImpl extends TaskStackListener implements Handler.Callback {
    private static final String TAG = TaskStackListenerImpl.class.getSimpleName();
    private final IActivityTaskManager mActivityTaskManager;
    private Handler mMainHandler;
    private final List<TaskStackListenerCallback> mTaskStackListeners;
    private final List<TaskStackListenerCallback> mTmpListeners;

    public TaskStackListenerImpl(Handler handler) {
        this.mTaskStackListeners = new ArrayList();
        this.mTmpListeners = new ArrayList();
        this.mActivityTaskManager = ActivityTaskManager.getService();
        this.mMainHandler = new Handler(handler.getLooper(), this);
    }

    @VisibleForTesting
    TaskStackListenerImpl(IActivityTaskManager iActivityTaskManager) {
        this.mTaskStackListeners = new ArrayList();
        this.mTmpListeners = new ArrayList();
        this.mActivityTaskManager = iActivityTaskManager;
    }

    @VisibleForTesting
    void setHandler(Handler handler) {
        this.mMainHandler = handler;
    }

    public void addListener(TaskStackListenerCallback taskStackListenerCallback) {
        boolean isEmpty;
        synchronized (this.mTaskStackListeners) {
            isEmpty = this.mTaskStackListeners.isEmpty();
            this.mTaskStackListeners.add(taskStackListenerCallback);
        }
        if (isEmpty) {
            try {
                this.mActivityTaskManager.registerTaskStackListener(this);
            } catch (Exception e) {
                Log.w(TAG, "Failed to call registerTaskStackListener", e);
            }
        }
    }

    public void removeListener(TaskStackListenerCallback taskStackListenerCallback) {
        boolean isEmpty;
        boolean isEmpty2;
        synchronized (this.mTaskStackListeners) {
            isEmpty = this.mTaskStackListeners.isEmpty();
            this.mTaskStackListeners.remove(taskStackListenerCallback);
            isEmpty2 = this.mTaskStackListeners.isEmpty();
        }
        if (!isEmpty && isEmpty2) {
            try {
                this.mActivityTaskManager.unregisterTaskStackListener(this);
            } catch (Exception e) {
                Log.w(TAG, "Failed to call unregisterTaskStackListener", e);
            }
        }
    }

    public void onRecentTaskListUpdated() {
        this.mMainHandler.obtainMessage(17).sendToTarget();
    }

    public void onRecentTaskListFrozenChanged(boolean z) {
        this.mMainHandler.obtainMessage(18, z ? 1 : 0, 0).sendToTarget();
    }

    public void onTaskStackChanged() {
        synchronized (this.mTaskStackListeners) {
            this.mTmpListeners.addAll(this.mTaskStackListeners);
        }
        for (int size = this.mTmpListeners.size() - 1; size >= 0; size--) {
            this.mTmpListeners.get(size).onTaskStackChangedBackground();
        }
        this.mTmpListeners.clear();
        this.mMainHandler.removeMessages(1);
        this.mMainHandler.sendEmptyMessage(1);
    }

    public void onTaskProfileLocked(int i, int i2) {
        this.mMainHandler.obtainMessage(7, i, i2).sendToTarget();
    }

    public void onTaskDisplayChanged(int i, int i2) {
        this.mMainHandler.obtainMessage(16, i, i2).sendToTarget();
    }

    public void onTaskCreated(int i, ComponentName componentName) {
        this.mMainHandler.obtainMessage(10, i, 0, componentName).sendToTarget();
    }

    public void onTaskRemoved(int i) {
        this.mMainHandler.obtainMessage(11, i, 0).sendToTarget();
    }

    public void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.mMainHandler.obtainMessage(12, runningTaskInfo).sendToTarget();
    }

    public void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.mMainHandler.obtainMessage(19, runningTaskInfo).sendToTarget();
    }

    public void onTaskSnapshotChanged(int i, TaskSnapshot taskSnapshot) {
        this.mMainHandler.obtainMessage(2, i, 0, taskSnapshot).sendToTarget();
    }

    public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.mMainHandler.obtainMessage(15, runningTaskInfo).sendToTarget();
    }

    public void onActivityPinned(String str, int i, int i2, int i3) {
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = str;
        obtain.argi1 = i;
        obtain.argi2 = i2;
        obtain.argi3 = i3;
        this.mMainHandler.removeMessages(3);
        this.mMainHandler.obtainMessage(3, obtain).sendToTarget();
    }

    public void onActivityUnpinned() {
        this.mMainHandler.removeMessages(8);
        this.mMainHandler.sendEmptyMessage(8);
    }

    public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = runningTaskInfo;
        obtain.argi1 = z ? 1 : 0;
        obtain.argi2 = z2 ? 1 : 0;
        obtain.argi3 = z3 ? 1 : 0;
        this.mMainHandler.removeMessages(4);
        this.mMainHandler.obtainMessage(4, obtain).sendToTarget();
    }

    public void onActivityForcedResizable(String str, int i, int i2) {
        this.mMainHandler.obtainMessage(5, i, i2, str).sendToTarget();
    }

    public void onActivityDismissingDockedTask() {
        this.mMainHandler.sendEmptyMessage(6);
    }

    public void onActivityLaunchOnSecondaryDisplayFailed(ActivityManager.RunningTaskInfo runningTaskInfo, int i) {
        this.mMainHandler.obtainMessage(9, i, 0, runningTaskInfo).sendToTarget();
    }

    public void onActivityLaunchOnSecondaryDisplayRerouted(ActivityManager.RunningTaskInfo runningTaskInfo, int i) {
        this.mMainHandler.obtainMessage(14, i, 0, runningTaskInfo).sendToTarget();
    }

    public void onActivityRequestedOrientationChanged(int i, int i2) {
        this.mMainHandler.obtainMessage(13, i, i2).sendToTarget();
    }

    public void onActivityRotation(int i) {
        this.mMainHandler.obtainMessage(20, i, 0).sendToTarget();
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        synchronized (this.mTaskStackListeners) {
            boolean z = false;
            switch (message.what) {
                case 1:
                    Trace.beginSection("onTaskStackChanged");
                    for (int size = this.mTaskStackListeners.size() - 1; size >= 0; size--) {
                        this.mTaskStackListeners.get(size).onTaskStackChanged();
                    }
                    Trace.endSection();
                    break;
                case 2:
                    Trace.beginSection("onTaskSnapshotChanged");
                    for (int size2 = this.mTaskStackListeners.size() - 1; size2 >= 0; size2--) {
                        this.mTaskStackListeners.get(size2).onTaskSnapshotChanged(message.arg1, (TaskSnapshot) message.obj);
                    }
                    Trace.endSection();
                    break;
                case 3:
                    SomeArgs someArgs = (SomeArgs) message.obj;
                    for (int size3 = this.mTaskStackListeners.size() - 1; size3 >= 0; size3--) {
                        this.mTaskStackListeners.get(size3).onActivityPinned((String) someArgs.arg1, someArgs.argi1, someArgs.argi2, someArgs.argi3);
                    }
                    break;
                case 4:
                    SomeArgs someArgs2 = (SomeArgs) message.obj;
                    ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) someArgs2.arg1;
                    boolean z2 = someArgs2.argi1 != 0;
                    boolean z3 = someArgs2.argi2 != 0;
                    if (someArgs2.argi3 != 0) {
                        z = true;
                    }
                    for (int size4 = this.mTaskStackListeners.size() - 1; size4 >= 0; size4--) {
                        this.mTaskStackListeners.get(size4).onActivityRestartAttempt(runningTaskInfo, z2, z3, z);
                    }
                    break;
                case 5:
                    for (int size5 = this.mTaskStackListeners.size() - 1; size5 >= 0; size5--) {
                        this.mTaskStackListeners.get(size5).onActivityForcedResizable((String) message.obj, message.arg1, message.arg2);
                    }
                    break;
                case 6:
                    for (int size6 = this.mTaskStackListeners.size() - 1; size6 >= 0; size6--) {
                        this.mTaskStackListeners.get(size6).onActivityDismissingDockedStack();
                    }
                    break;
                case 7:
                    for (int size7 = this.mTaskStackListeners.size() - 1; size7 >= 0; size7--) {
                        this.mTaskStackListeners.get(size7).onTaskProfileLocked(message.arg1, message.arg2);
                    }
                    break;
                case 8:
                    for (int size8 = this.mTaskStackListeners.size() - 1; size8 >= 0; size8--) {
                        this.mTaskStackListeners.get(size8).onActivityUnpinned();
                    }
                    break;
                case 9:
                    ActivityManager.RunningTaskInfo runningTaskInfo2 = (ActivityManager.RunningTaskInfo) message.obj;
                    for (int size9 = this.mTaskStackListeners.size() - 1; size9 >= 0; size9--) {
                        this.mTaskStackListeners.get(size9).onActivityLaunchOnSecondaryDisplayFailed(runningTaskInfo2);
                    }
                    break;
                case 10:
                    for (int size10 = this.mTaskStackListeners.size() - 1; size10 >= 0; size10--) {
                        this.mTaskStackListeners.get(size10).onTaskCreated(message.arg1, (ComponentName) message.obj);
                    }
                    break;
                case 11:
                    for (int size11 = this.mTaskStackListeners.size() - 1; size11 >= 0; size11--) {
                        this.mTaskStackListeners.get(size11).onTaskRemoved(message.arg1);
                    }
                    break;
                case 12:
                    ActivityManager.RunningTaskInfo runningTaskInfo3 = (ActivityManager.RunningTaskInfo) message.obj;
                    for (int size12 = this.mTaskStackListeners.size() - 1; size12 >= 0; size12--) {
                        this.mTaskStackListeners.get(size12).onTaskMovedToFront(runningTaskInfo3);
                    }
                    break;
                case 13:
                    for (int size13 = this.mTaskStackListeners.size() - 1; size13 >= 0; size13--) {
                        this.mTaskStackListeners.get(size13).onActivityRequestedOrientationChanged(message.arg1, message.arg2);
                    }
                    break;
                case 14:
                    ActivityManager.RunningTaskInfo runningTaskInfo4 = (ActivityManager.RunningTaskInfo) message.obj;
                    for (int size14 = this.mTaskStackListeners.size() - 1; size14 >= 0; size14--) {
                        this.mTaskStackListeners.get(size14).onActivityLaunchOnSecondaryDisplayRerouted(runningTaskInfo4);
                    }
                    break;
                case 15:
                    for (int size15 = this.mTaskStackListeners.size() - 1; size15 >= 0; size15--) {
                        this.mTaskStackListeners.get(size15).onBackPressedOnTaskRoot((ActivityManager.RunningTaskInfo) message.obj);
                    }
                    break;
                case 16:
                    for (int size16 = this.mTaskStackListeners.size() - 1; size16 >= 0; size16--) {
                        this.mTaskStackListeners.get(size16).onTaskDisplayChanged(message.arg1, message.arg2);
                    }
                    break;
                case 17:
                    for (int size17 = this.mTaskStackListeners.size() - 1; size17 >= 0; size17--) {
                        this.mTaskStackListeners.get(size17).onRecentTaskListUpdated();
                    }
                    break;
                case 18:
                    for (int size18 = this.mTaskStackListeners.size() - 1; size18 >= 0; size18--) {
                        this.mTaskStackListeners.get(size18).onRecentTaskListFrozenChanged(message.arg1 != 0);
                    }
                    break;
                case 19:
                    ActivityManager.RunningTaskInfo runningTaskInfo5 = (ActivityManager.RunningTaskInfo) message.obj;
                    for (int size19 = this.mTaskStackListeners.size() - 1; size19 >= 0; size19--) {
                        this.mTaskStackListeners.get(size19).onTaskDescriptionChanged(runningTaskInfo5);
                    }
                    break;
                case 20:
                    for (int size20 = this.mTaskStackListeners.size() - 1; size20 >= 0; size20--) {
                        this.mTaskStackListeners.get(size20).onActivityRotation(message.arg1);
                    }
                    break;
            }
        }
        Object obj = message.obj;
        if (obj instanceof SomeArgs) {
            ((SomeArgs) obj).recycle();
        }
        return true;
    }
}
