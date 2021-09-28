package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$ComposeFeedback {
    @Nullable
    private List<FeedbackParcelables$ComposeActionFeedback> actionFeedback;
    private ComposeFeedbackType feedbackType;
    private int itemId;
    @Nullable
    private String text;

    /* loaded from: classes2.dex */
    public enum ComposeFeedbackType {
        UNKNOWN_TYPE(0),
        ITEM_SAVED(1),
        ITEM_DELETED(2),
        ACTION_CLICKED(3),
        ACTION_PINNED(4),
        DATABASE_CLEARED(5);
        
        public final int value;

        ComposeFeedbackType(int i) {
            this.value = i;
        }

        public Bundle writeToBundle() {
            Bundle bundle = new Bundle();
            bundle.putInt("value", this.value);
            return bundle;
        }
    }

    private FeedbackParcelables$ComposeFeedback() {
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        ComposeFeedbackType composeFeedbackType = this.feedbackType;
        if (composeFeedbackType == null) {
            bundle.putBundle("feedbackType", null);
        } else {
            bundle.putBundle("feedbackType", composeFeedbackType.writeToBundle());
        }
        bundle.putInt("itemId", this.itemId);
        bundle.putString("text", this.text);
        if (this.actionFeedback == null) {
            bundle.putParcelableArrayList("actionFeedback", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.actionFeedback.size());
            for (FeedbackParcelables$ComposeActionFeedback feedbackParcelables$ComposeActionFeedback : this.actionFeedback) {
                if (feedbackParcelables$ComposeActionFeedback == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(feedbackParcelables$ComposeActionFeedback.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("actionFeedback", arrayList);
        }
        return bundle;
    }
}
