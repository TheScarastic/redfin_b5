package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$SelectionFeedback;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$ActionGroupFeedback {
    @Nullable
    private SuggestParcelables$ActionGroup actionGroup;
    private int actionGroupPresentationMode;
    @Nullable
    private List<SuggestParcelables$ActionGroup> actionGroupShown;
    @Nullable
    private SuggestParcelables$Entity selectedEntity;
    private FeedbackParcelables$SelectionFeedback.SelectionType selectionType;
    private ActionGroupInteraction userInteraction;

    /* loaded from: classes2.dex */
    public enum ActionGroupInteraction {
        ACTION_GROUP_ACTION_UNKNOWN(0),
        ACTION_GROUP_SHOWN(1),
        ACTION_GROUP_DISMISSED(2),
        ACTION_GROUP_EXPANDED(3);
        
        public final int value;

        ActionGroupInteraction(int i) {
            this.value = i;
        }

        public Bundle writeToBundle() {
            Bundle bundle = new Bundle();
            bundle.putInt("value", this.value);
            return bundle;
        }
    }

    private FeedbackParcelables$ActionGroupFeedback() {
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
        if (this.actionGroupShown == null) {
            bundle.putParcelableArrayList("actionGroupShown", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.actionGroupShown.size());
            for (SuggestParcelables$ActionGroup suggestParcelables$ActionGroup : this.actionGroupShown) {
                if (suggestParcelables$ActionGroup == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(suggestParcelables$ActionGroup.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("actionGroupShown", arrayList);
        }
        SuggestParcelables$ActionGroup suggestParcelables$ActionGroup2 = this.actionGroup;
        if (suggestParcelables$ActionGroup2 == null) {
            bundle.putBundle("actionGroup", null);
        } else {
            bundle.putBundle("actionGroup", suggestParcelables$ActionGroup2.writeToBundle());
        }
        ActionGroupInteraction actionGroupInteraction = this.userInteraction;
        if (actionGroupInteraction == null) {
            bundle.putBundle("userInteraction", null);
        } else {
            bundle.putBundle("userInteraction", actionGroupInteraction.writeToBundle());
        }
        bundle.putInt("actionGroupPresentationMode", this.actionGroupPresentationMode);
        bundle.putBundle("selection", null);
        return bundle;
    }
}
