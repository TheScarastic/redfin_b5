package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$TaskSnapshotFeedback {
    @Nullable
    private String interactionSessionId;
    @Nullable
    private String overviewSessionId;
    @Nullable
    private String taskAppComponentName;
    @Nullable
    private String taskSnapshotSessionId;
    private TaskSnapshotInteraction userInteraction;

    /* loaded from: classes2.dex */
    public enum TaskSnapshotInteraction {
        UNKNOWN_TASK_SNAPSHOT_ACTION(0),
        TASK_SNAPSHOT_CREATED(1),
        TASK_SNAPSHOT_SUGGEST_VIEW_DISPLAYED(2),
        TASK_SNAPSHOT_PROACTIVE_HINTS_DISPLAYED(3),
        TASK_SNAPSHOT_GLEAMS_DISPLAYED(4),
        TASK_SNAPSHOT_LONG_PRESSED(5),
        TASK_SNAPSHOT_DISMISSED(6);
        
        public final int value;

        TaskSnapshotInteraction(int i) {
            this.value = i;
        }

        public Bundle writeToBundle() {
            Bundle bundle = new Bundle();
            bundle.putInt("value", this.value);
            return bundle;
        }
    }

    private FeedbackParcelables$TaskSnapshotFeedback() {
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        TaskSnapshotInteraction taskSnapshotInteraction = this.userInteraction;
        if (taskSnapshotInteraction == null) {
            bundle.putBundle("userInteraction", null);
        } else {
            bundle.putBundle("userInteraction", taskSnapshotInteraction.writeToBundle());
        }
        bundle.putString("overviewSessionId", this.overviewSessionId);
        bundle.putString("taskSnapshotSessionId", this.taskSnapshotSessionId);
        bundle.putString("taskAppComponentName", this.taskAppComponentName);
        bundle.putString("interactionSessionId", this.interactionSessionId);
        return bundle;
    }
}
