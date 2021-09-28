package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$SelectionFeedback {
    @Nullable
    private String interactionSessionId;
    private SuggestParcelables$InteractionType interactionType;
    @Nullable
    private String overviewSessionId;
    @Nullable
    private ContentParcelables$Contents screenContents;
    @Nullable
    private SuggestParcelables$Entity selectedEntity;
    @Nullable
    private String selectionId;
    private int selectionPresentationMode;
    @Nullable
    private String selectionSessionId;
    @Nullable
    private String taskSnapshotSessionId;
    private SelectionType type;
    private SelectionInteraction userInteraction;

    /* loaded from: classes2.dex */
    public enum SelectionInteraction {
        SELECTION_ACTION_UNKNOWN(0),
        SELECTION_INITIATED(1),
        SELECTION_DISMISSED(2),
        SELECTION_ADJUSTED(3),
        SELECTION_CONFIRMED(4),
        SELECTION_SUGGESTED(5),
        SELECTION_SUGGESTION_VERIFIED(6),
        SELECTION_SHOWN(7);
        
        public final int value;

        SelectionInteraction(int i) {
            this.value = i;
        }

        public Bundle writeToBundle() {
            Bundle bundle = new Bundle();
            bundle.putInt("value", this.value);
            return bundle;
        }
    }

    /* loaded from: classes2.dex */
    public enum SelectionType {
        SELECTION_TYPE_UNKNOWN(0),
        TEXT(1),
        IMAGE(2);
        
        public final int value;

        SelectionType(int i) {
            this.value = i;
        }

        public Bundle writeToBundle() {
            Bundle bundle = new Bundle();
            bundle.putInt("value", this.value);
            return bundle;
        }
    }

    private FeedbackParcelables$SelectionFeedback() {
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        SelectionType selectionType = this.type;
        if (selectionType == null) {
            bundle.putBundle("type", null);
        } else {
            bundle.putBundle("type", selectionType.writeToBundle());
        }
        SuggestParcelables$Entity suggestParcelables$Entity = this.selectedEntity;
        if (suggestParcelables$Entity == null) {
            bundle.putBundle("selectedEntity", null);
        } else {
            bundle.putBundle("selectedEntity", suggestParcelables$Entity.writeToBundle());
        }
        bundle.putBundle("selection", null);
        SelectionInteraction selectionInteraction = this.userInteraction;
        if (selectionInteraction == null) {
            bundle.putBundle("userInteraction", null);
        } else {
            bundle.putBundle("userInteraction", selectionInteraction.writeToBundle());
        }
        bundle.putInt("selectionPresentationMode", this.selectionPresentationMode);
        bundle.putString("overviewSessionId", this.overviewSessionId);
        bundle.putString("taskSnapshotSessionId", this.taskSnapshotSessionId);
        bundle.putString("interactionSessionId", this.interactionSessionId);
        bundle.putString("selectionSessionId", this.selectionSessionId);
        ContentParcelables$Contents contentParcelables$Contents = this.screenContents;
        if (contentParcelables$Contents == null) {
            bundle.putBundle("screenContents", null);
        } else {
            bundle.putBundle("screenContents", contentParcelables$Contents.writeToBundle());
        }
        SuggestParcelables$InteractionType suggestParcelables$InteractionType = this.interactionType;
        if (suggestParcelables$InteractionType == null) {
            bundle.putBundle("interactionType", null);
        } else {
            bundle.putBundle("interactionType", suggestParcelables$InteractionType.writeToBundle());
        }
        bundle.putString("selectionId", this.selectionId);
        return bundle;
    }
}
