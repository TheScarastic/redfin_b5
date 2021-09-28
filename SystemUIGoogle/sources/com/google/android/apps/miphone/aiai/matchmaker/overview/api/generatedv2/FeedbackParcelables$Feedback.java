package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$Feedback {
    @Nullable
    private Object feedback;
    private boolean hasFeedback;
    @Nullable
    private String id;
    @Nullable
    private InteractionContextParcelables$InteractionContext interactionContext;
    private FeedbackParcelables$SuggestionAction suggestionAction;
    private long timestampMs;

    private FeedbackParcelables$Feedback() {
    }

    public static FeedbackParcelables$Feedback create() {
        return new FeedbackParcelables$Feedback();
    }

    public void setFeedback(@Nullable Object obj) {
        this.feedback = obj;
        this.hasFeedback = obj != null;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        if (this.feedback instanceof FeedbackParcelables$OverviewFeedback) {
            bundle.putInt("feedback#tag", 6);
            FeedbackParcelables$OverviewFeedback feedbackParcelables$OverviewFeedback = (FeedbackParcelables$OverviewFeedback) this.feedback;
            if (feedbackParcelables$OverviewFeedback == null) {
                bundle.putBundle("feedback", null);
            } else {
                bundle.putBundle("feedback", feedbackParcelables$OverviewFeedback.writeToBundle());
            }
        }
        if (this.feedback instanceof FeedbackParcelables$SelectionFeedback) {
            bundle.putInt("feedback#tag", 7);
            FeedbackParcelables$SelectionFeedback feedbackParcelables$SelectionFeedback = (FeedbackParcelables$SelectionFeedback) this.feedback;
            if (feedbackParcelables$SelectionFeedback == null) {
                bundle.putBundle("feedback", null);
            } else {
                bundle.putBundle("feedback", feedbackParcelables$SelectionFeedback.writeToBundle());
            }
        }
        if (this.feedback instanceof FeedbackParcelables$ActionFeedback) {
            bundle.putInt("feedback#tag", 8);
            FeedbackParcelables$ActionFeedback feedbackParcelables$ActionFeedback = (FeedbackParcelables$ActionFeedback) this.feedback;
            if (feedbackParcelables$ActionFeedback == null) {
                bundle.putBundle("feedback", null);
            } else {
                bundle.putBundle("feedback", feedbackParcelables$ActionFeedback.writeToBundle());
            }
        }
        if (this.feedback instanceof FeedbackParcelables$ActionGroupFeedback) {
            bundle.putInt("feedback#tag", 9);
            FeedbackParcelables$ActionGroupFeedback feedbackParcelables$ActionGroupFeedback = (FeedbackParcelables$ActionGroupFeedback) this.feedback;
            if (feedbackParcelables$ActionGroupFeedback == null) {
                bundle.putBundle("feedback", null);
            } else {
                bundle.putBundle("feedback", feedbackParcelables$ActionGroupFeedback.writeToBundle());
            }
        }
        if (this.feedback instanceof FeedbackParcelables$TaskSnapshotFeedback) {
            bundle.putInt("feedback#tag", 10);
            FeedbackParcelables$TaskSnapshotFeedback feedbackParcelables$TaskSnapshotFeedback = (FeedbackParcelables$TaskSnapshotFeedback) this.feedback;
            if (feedbackParcelables$TaskSnapshotFeedback == null) {
                bundle.putBundle("feedback", null);
            } else {
                bundle.putBundle("feedback", feedbackParcelables$TaskSnapshotFeedback.writeToBundle());
            }
        }
        if (this.feedback instanceof FeedbackParcelables$ScreenshotFeedback) {
            bundle.putInt("feedback#tag", 11);
            FeedbackParcelables$ScreenshotFeedback feedbackParcelables$ScreenshotFeedback = (FeedbackParcelables$ScreenshotFeedback) this.feedback;
            if (feedbackParcelables$ScreenshotFeedback == null) {
                bundle.putBundle("feedback", null);
            } else {
                bundle.putBundle("feedback", feedbackParcelables$ScreenshotFeedback.writeToBundle());
            }
        }
        if (this.feedback instanceof FeedbackParcelables$ComposeFeedback) {
            bundle.putInt("feedback#tag", 12);
            FeedbackParcelables$ComposeFeedback feedbackParcelables$ComposeFeedback = (FeedbackParcelables$ComposeFeedback) this.feedback;
            if (feedbackParcelables$ComposeFeedback == null) {
                bundle.putBundle("feedback", null);
            } else {
                bundle.putBundle("feedback", feedbackParcelables$ComposeFeedback.writeToBundle());
            }
        }
        bundle.putString("id", this.id);
        bundle.putLong("timestampMs", this.timestampMs);
        FeedbackParcelables$SuggestionAction feedbackParcelables$SuggestionAction = this.suggestionAction;
        if (feedbackParcelables$SuggestionAction == null) {
            bundle.putBundle("suggestionAction", null);
        } else {
            bundle.putBundle("suggestionAction", feedbackParcelables$SuggestionAction.writeToBundle());
        }
        InteractionContextParcelables$InteractionContext interactionContextParcelables$InteractionContext = this.interactionContext;
        if (interactionContextParcelables$InteractionContext == null) {
            bundle.putBundle("interactionContext", null);
        } else {
            bundle.putBundle("interactionContext", interactionContextParcelables$InteractionContext.writeToBundle());
        }
        return bundle;
    }
}
