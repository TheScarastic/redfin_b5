package com.android.systemui.shared.recents.model;

import android.app.ActivityManager;
import android.app.TaskInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.view.ViewDebug;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public class Task {
    public static final String TAG = "Task";
    @ViewDebug.ExportedProperty(category = "recents")
    public int colorBackground;
    @ViewDebug.ExportedProperty(category = "recents")
    public int colorPrimary;
    public Drawable icon;
    @ViewDebug.ExportedProperty(category = "recents")
    public boolean isDockable;
    @ViewDebug.ExportedProperty(category = "recents")
    public boolean isLocked;
    @ViewDebug.ExportedProperty(deepExport = true, prefix = "key_")
    public TaskKey key;
    public ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData lastSnapshotData;
    public ActivityManager.TaskDescription taskDescription;
    public ThumbnailData thumbnail;
    @ViewDebug.ExportedProperty(category = "recents")
    @Deprecated
    public String title;
    @ViewDebug.ExportedProperty(category = "recents")
    public String titleDescription;
    @ViewDebug.ExportedProperty(category = "recents")
    public ComponentName topActivity;

    public Task() {
        this.lastSnapshotData = new ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData();
    }

    public static Task from(TaskKey taskKey, TaskInfo taskInfo, boolean z) {
        ActivityManager.TaskDescription taskDescription = taskInfo.taskDescription;
        int i = 0;
        int primaryColor = taskDescription != null ? taskDescription.getPrimaryColor() : 0;
        if (taskDescription != null) {
            i = taskDescription.getBackgroundColor();
        }
        return new Task(taskKey, primaryColor, i, taskInfo.supportsSplitScreenMultiWindow, z, taskDescription, taskInfo.topActivity);
    }

    public void dump(String str, PrintWriter printWriter) {
        printWriter.print(str);
        printWriter.print(this.key);
        if (!this.isDockable) {
            printWriter.print(" dockable=N");
        }
        if (this.isLocked) {
            printWriter.print(" locked=Y");
        }
        printWriter.print(" ");
        printWriter.print(this.title);
        printWriter.println();
    }

    public boolean equals(Object obj) {
        return this.key.equals(((Task) obj).key);
    }

    public ComponentName getTopComponent() {
        ComponentName componentName = this.topActivity;
        return componentName != null ? componentName : this.key.baseIntent.getComponent();
    }

    public float getVisibleThumbnailRatio(boolean z) {
        Rect rect;
        ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData persistedTaskSnapshotData = this.lastSnapshotData;
        Point point = persistedTaskSnapshotData.taskSize;
        if (point == null || (rect = persistedTaskSnapshotData.contentInsets) == null) {
            return 0.0f;
        }
        float f = (float) point.x;
        float f2 = (float) point.y;
        if (z) {
            f -= (float) (rect.left + rect.right);
            f2 -= (float) (rect.top + rect.bottom);
        }
        return f / f2;
    }

    public void setLastSnapshotData(ActivityManager.RecentTaskInfo recentTaskInfo) {
        this.lastSnapshotData.set(recentTaskInfo.lastSnapshotData);
    }

    public String toString() {
        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("[");
        m.append(this.key.toString());
        m.append("] ");
        m.append(this.title);
        return m.toString();
    }

    public Task(TaskKey taskKey) {
        this.lastSnapshotData = new ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData();
        this.key = taskKey;
        this.taskDescription = new ActivityManager.TaskDescription();
    }

    public Task(Task task) {
        this(task.key, task.colorPrimary, task.colorBackground, task.isDockable, task.isLocked, task.taskDescription, task.topActivity);
        this.lastSnapshotData.set(task.lastSnapshotData);
    }

    @Deprecated
    public Task(TaskKey taskKey, int i, int i2, boolean z, boolean z2, ActivityManager.TaskDescription taskDescription, ComponentName componentName) {
        this.lastSnapshotData = new ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData();
        this.key = taskKey;
        this.colorPrimary = i;
        this.colorBackground = i2;
        this.taskDescription = taskDescription;
        this.isDockable = z;
        this.isLocked = z2;
        this.topActivity = componentName;
    }

    /* loaded from: classes.dex */
    public static class TaskKey implements Parcelable {
        public static final Parcelable.Creator<TaskKey> CREATOR = new Parcelable.Creator<TaskKey>() { // from class: com.android.systemui.shared.recents.model.Task.TaskKey.1
            @Override // android.os.Parcelable.Creator
            public TaskKey createFromParcel(Parcel parcel) {
                return TaskKey.readFromParcel(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public TaskKey[] newArray(int i) {
                return new TaskKey[i];
            }
        };
        @ViewDebug.ExportedProperty(category = "recents")
        public final Intent baseIntent;
        @ViewDebug.ExportedProperty(category = "recents")
        public final int displayId;
        @ViewDebug.ExportedProperty(category = "recents")
        public final int id;
        @ViewDebug.ExportedProperty(category = "recents")
        public long lastActiveTime;
        private int mHashCode;
        public final ComponentName sourceComponent;
        @ViewDebug.ExportedProperty(category = "recents")
        public final int userId;
        @ViewDebug.ExportedProperty(category = "recents")
        public int windowingMode;

        public TaskKey(TaskInfo taskInfo) {
            ComponentName componentName = taskInfo.origActivity;
            componentName = componentName == null ? taskInfo.realActivity : componentName;
            this.id = taskInfo.taskId;
            this.windowingMode = taskInfo.configuration.windowConfiguration.getWindowingMode();
            this.baseIntent = taskInfo.baseIntent;
            this.sourceComponent = componentName;
            this.userId = taskInfo.userId;
            this.lastActiveTime = taskInfo.lastActiveTime;
            this.displayId = taskInfo.displayId;
            updateHashCode();
        }

        /* access modifiers changed from: private */
        public static TaskKey readFromParcel(Parcel parcel) {
            return new TaskKey(parcel.readInt(), parcel.readInt(), (Intent) parcel.readTypedObject(Intent.CREATOR), (ComponentName) parcel.readTypedObject(ComponentName.CREATOR), parcel.readInt(), parcel.readLong(), parcel.readInt());
        }

        private void updateHashCode() {
            this.mHashCode = Objects.hash(Integer.valueOf(this.id), Integer.valueOf(this.windowingMode), Integer.valueOf(this.userId));
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // java.lang.Object
        public boolean equals(Object obj) {
            if (!(obj instanceof TaskKey)) {
                return false;
            }
            TaskKey taskKey = (TaskKey) obj;
            if (this.id == taskKey.id && this.windowingMode == taskKey.windowingMode && this.userId == taskKey.userId) {
                return true;
            }
            return false;
        }

        public ComponentName getComponent() {
            return this.baseIntent.getComponent();
        }

        public String getPackageName() {
            if (this.baseIntent.getComponent() != null) {
                return this.baseIntent.getComponent().getPackageName();
            }
            return this.baseIntent.getPackage();
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.mHashCode;
        }

        public void setWindowingMode(int i) {
            this.windowingMode = i;
            updateHashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("id=");
            m.append(this.id);
            m.append(" windowingMode=");
            m.append(this.windowingMode);
            m.append(" user=");
            m.append(this.userId);
            m.append(" lastActiveTime=");
            m.append(this.lastActiveTime);
            return m.toString();
        }

        @Override // android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.id);
            parcel.writeInt(this.windowingMode);
            parcel.writeTypedObject(this.baseIntent, i);
            parcel.writeInt(this.userId);
            parcel.writeLong(this.lastActiveTime);
            parcel.writeInt(this.displayId);
            parcel.writeTypedObject(this.sourceComponent, i);
        }

        public TaskKey(int i, int i2, Intent intent, ComponentName componentName, int i3, long j) {
            this.id = i;
            this.windowingMode = i2;
            this.baseIntent = intent;
            this.sourceComponent = componentName;
            this.userId = i3;
            this.lastActiveTime = j;
            this.displayId = 0;
            updateHashCode();
        }

        public TaskKey(int i, int i2, Intent intent, ComponentName componentName, int i3, long j, int i4) {
            this.id = i;
            this.windowingMode = i2;
            this.baseIntent = intent;
            this.sourceComponent = componentName;
            this.userId = i3;
            this.lastActiveTime = j;
            this.displayId = i4;
            updateHashCode();
        }
    }
}
