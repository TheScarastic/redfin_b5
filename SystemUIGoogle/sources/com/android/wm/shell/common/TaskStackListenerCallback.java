package com.android.wm.shell.common;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.window.TaskSnapshot;
/* loaded from: classes2.dex */
public interface TaskStackListenerCallback {
    default void onActivityDismissingDockedStack() {
    }

    default void onActivityForcedResizable(String str, int i, int i2) {
    }

    default void onActivityLaunchOnSecondaryDisplayFailed() {
    }

    default void onActivityLaunchOnSecondaryDisplayRerouted() {
    }

    default void onActivityPinned(String str, int i, int i2, int i3) {
    }

    default void onActivityRequestedOrientationChanged(int i, int i2) {
    }

    default void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
    }

    default void onActivityRotation(int i) {
    }

    default void onActivityUnpinned() {
    }

    default void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onRecentTaskListFrozenChanged(boolean z) {
    }

    default void onRecentTaskListUpdated() {
    }

    default void onTaskCreated(int i, ComponentName componentName) {
    }

    default void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
    }

    default void onTaskDisplayChanged(int i, int i2) {
    }

    default void onTaskMovedToFront(int i) {
    }

    default void onTaskProfileLocked(int i, int i2) {
    }

    default void onTaskRemoved(int i) {
    }

    default void onTaskSnapshotChanged(int i, TaskSnapshot taskSnapshot) {
    }

    default void onTaskStackChanged() {
    }

    default void onTaskStackChangedBackground() {
    }

    default void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) {
        onTaskMovedToFront(runningTaskInfo.taskId);
    }

    default void onActivityLaunchOnSecondaryDisplayFailed(ActivityManager.RunningTaskInfo runningTaskInfo) {
        onActivityLaunchOnSecondaryDisplayFailed();
    }

    default void onActivityLaunchOnSecondaryDisplayRerouted(ActivityManager.RunningTaskInfo runningTaskInfo) {
        onActivityLaunchOnSecondaryDisplayRerouted();
    }
}
