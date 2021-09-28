package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$OverviewFeedback {
    private int numSelectionsInitialized;
    private int numSelectionsSuggested;
    private int overviewPresentationMode;
    @Nullable
    private String overviewSessionId;
    @Nullable
    private String primaryTaskAppComponentName;
    @Nullable
    private List<String> taskAppComponentNameList;
    @Nullable
    private String taskSnapshotSessionId;
    private OverviewInteraction userInteraction;

    /* loaded from: classes2.dex */
    public enum OverviewInteraction {
        UNKNOWN_OVERVIEW_ACTION(0),
        OVERVIEW_SCREEN_STARTED(1),
        OVERVIEW_SCREEN_DISMISSED(2),
        OVERVIEW_SCREEN_QUICK_DISMISSED(3),
        OVERVIEW_SCREEN_SWITCHED(4),
        OVERVIEW_TASK_SNAPSHOT_DISPLAY(5),
        OVERVIEW_SCREEN_APP_CLOSED(6),
        OVERVIEW_SCREEN_EXIT_APP_ENTERED(7),
        OVERVIEW_SCREEN_EXIT_BACK_BUTTON(8),
        OVERVIEW_SCREEN_EXIT_HOME_BUTTON(9),
        OVERVIEW_SCREEN_EXIT_POWER_BUTTON(10),
        OVERVIEW_SCREEN_ENTER_ALL_APPS(11);
        
        public final int value;

        OverviewInteraction(int i) {
            this.value = i;
        }

        public Bundle writeToBundle() {
            Bundle bundle = new Bundle();
            bundle.putInt("value", this.value);
            return bundle;
        }
    }

    private FeedbackParcelables$OverviewFeedback() {
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        OverviewInteraction overviewInteraction = this.userInteraction;
        if (overviewInteraction == null) {
            bundle.putBundle("userInteraction", null);
        } else {
            bundle.putBundle("userInteraction", overviewInteraction.writeToBundle());
        }
        bundle.putInt("overviewPresentationMode", this.overviewPresentationMode);
        bundle.putInt("numSelectionsSuggested", this.numSelectionsSuggested);
        bundle.putInt("numSelectionsInitialized", this.numSelectionsInitialized);
        bundle.putString("overviewSessionId", this.overviewSessionId);
        bundle.putString("taskSnapshotSessionId", this.taskSnapshotSessionId);
        bundle.putString("primaryTaskAppComponentName", this.primaryTaskAppComponentName);
        if (this.taskAppComponentNameList == null) {
            bundle.putStringArrayList("taskAppComponentNameList", null);
        } else {
            bundle.putStringArrayList("taskAppComponentNameList", new ArrayList<>(this.taskAppComponentNameList));
        }
        return bundle;
    }
}
