package com.android.systemui.shared.system;

import android.app.ActivityManager;
import android.content.ComponentName;
import com.android.systemui.shared.recents.model.ThumbnailData;
/* loaded from: classes.dex */
public abstract class TaskStackChangeListener {
    public void onActivityDismissingDockedStack() {
    }

    public void onActivityForcedResizable(String str, int i, int i2) {
    }

    public void onActivityLaunchOnSecondaryDisplayFailed() {
    }

    public void onActivityLaunchOnSecondaryDisplayFailed(ActivityManager.RunningTaskInfo runningTaskInfo) {
        onActivityLaunchOnSecondaryDisplayFailed();
    }

    public void onActivityLaunchOnSecondaryDisplayRerouted() {
    }

    public void onActivityLaunchOnSecondaryDisplayRerouted(ActivityManager.RunningTaskInfo runningTaskInfo) {
        onActivityLaunchOnSecondaryDisplayRerouted();
    }

    public void onActivityPinned(String str, int i, int i2, int i3) {
    }

    public void onActivityRequestedOrientationChanged(int i, int i2) {
    }

    public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
    }

    public void onActivityRotation(int i) {
    }

    public void onActivityUnpinned() {
    }

    public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    public void onLockTaskModeChanged(int i) {
    }

    public void onRecentTaskListFrozenChanged(boolean z) {
    }

    public void onRecentTaskListUpdated() {
    }

    public void onTaskCreated(int i, ComponentName componentName) {
    }

    public void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    public void onTaskDisplayChanged(int i, int i2) {
    }

    public void onTaskMovedToFront(int i) {
    }

    public void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) {
        onTaskMovedToFront(runningTaskInfo.taskId);
    }

    public void onTaskProfileLocked(int i, int i2) {
    }

    public void onTaskRemoved(int i) {
    }

    public void onTaskSnapshotChanged(int i, ThumbnailData thumbnailData) {
    }

    public void onTaskStackChanged() {
    }

    public void onTaskStackChangedBackground() {
    }
}
