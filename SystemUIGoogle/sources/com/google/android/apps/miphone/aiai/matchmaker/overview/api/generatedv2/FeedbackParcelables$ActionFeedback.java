package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$SelectionFeedback;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$ActionFeedback {
    @Nullable
    private List<FeedbackParcelables$ActionMenuItem> actionMenuItems;
    private int actionPresentationMode;
    @Nullable
    private List<SuggestParcelables$Action> actionShown;
    @Nullable
    private String interactionSessionId;
    private SuggestParcelables$InteractionType interactionType;
    @Nullable
    private SuggestParcelables$Action invokedAction;
    @Nullable
    private FeedbackParcelables$ActionMenuItem invokedActionMenuItem;
    @Nullable
    private String overviewSessionId;
    @Nullable
    private SuggestParcelables$Entity selectedEntity;
    @Nullable
    private String selectionSessionId;
    private FeedbackParcelables$SelectionFeedback.SelectionType selectionType;
    @Nullable
    private String taskSnapshotSessionId;
    private ActionInteraction userInteraction;
    @Nullable
    private String verticalTypeName;

    /* loaded from: classes2.dex */
    public enum ActionInteraction {
        ACTION_UNKNOWN(0),
        ACTION_SHOWN(1),
        ACTION_INVOKED(2),
        ACTION_DISMISSED(3),
        ACTION_MENU_SHOWN(4);
        
        public final int value;

        ActionInteraction(int i) {
            this.value = i;
        }

        public Bundle writeToBundle() {
            Bundle bundle = new Bundle();
            bundle.putInt("value", this.value);
            return bundle;
        }
    }

    private FeedbackParcelables$ActionFeedback() {
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        FeedbackParcelables$SelectionFeedback.SelectionType selectionType = this.selectionType;
        if (selectionType == null) {
            bundle.putBundle("selectionType", null);
        } else {
            bundle.putBundle("selectionType", selectionType.writeToBundle());
        }
        SuggestParcelables$Entity suggestParcelables$Entity = this.selectedEntity;
        if (suggestParcelables$Entity == null) {
            bundle.putBundle("selectedEntity", null);
        } else {
            bundle.putBundle("selectedEntity", suggestParcelables$Entity.writeToBundle());
        }
        if (this.actionShown == null) {
            bundle.putParcelableArrayList("actionShown", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.actionShown.size());
            for (SuggestParcelables$Action suggestParcelables$Action : this.actionShown) {
                if (suggestParcelables$Action == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(suggestParcelables$Action.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("actionShown", arrayList);
        }
        SuggestParcelables$Action suggestParcelables$Action2 = this.invokedAction;
        if (suggestParcelables$Action2 == null) {
            bundle.putBundle("invokedAction", null);
        } else {
            bundle.putBundle("invokedAction", suggestParcelables$Action2.writeToBundle());
        }
        ActionInteraction actionInteraction = this.userInteraction;
        if (actionInteraction == null) {
            bundle.putBundle("userInteraction", null);
        } else {
            bundle.putBundle("userInteraction", actionInteraction.writeToBundle());
        }
        bundle.putInt("actionPresentationMode", this.actionPresentationMode);
        bundle.putBundle("selection", null);
        bundle.putString("overviewSessionId", this.overviewSessionId);
        bundle.putString("taskSnapshotSessionId", this.taskSnapshotSessionId);
        bundle.putString("interactionSessionId", this.interactionSessionId);
        bundle.putString("selectionSessionId", this.selectionSessionId);
        bundle.putString("verticalTypeName", this.verticalTypeName);
        if (this.actionMenuItems == null) {
            bundle.putParcelableArrayList("actionMenuItems", null);
        } else {
            ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>(this.actionMenuItems.size());
            for (FeedbackParcelables$ActionMenuItem feedbackParcelables$ActionMenuItem : this.actionMenuItems) {
                if (feedbackParcelables$ActionMenuItem == null) {
                    arrayList2.add(null);
                } else {
                    arrayList2.add(feedbackParcelables$ActionMenuItem.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("actionMenuItems", arrayList2);
        }
        FeedbackParcelables$ActionMenuItem feedbackParcelables$ActionMenuItem2 = this.invokedActionMenuItem;
        if (feedbackParcelables$ActionMenuItem2 == null) {
            bundle.putBundle("invokedActionMenuItem", null);
        } else {
            bundle.putBundle("invokedActionMenuItem", feedbackParcelables$ActionMenuItem2.writeToBundle());
        }
        SuggestParcelables$InteractionType suggestParcelables$InteractionType = this.interactionType;
        if (suggestParcelables$InteractionType == null) {
            bundle.putBundle("interactionType", null);
        } else {
            bundle.putBundle("interactionType", suggestParcelables$InteractionType.writeToBundle());
        }
        return bundle;
    }
}
