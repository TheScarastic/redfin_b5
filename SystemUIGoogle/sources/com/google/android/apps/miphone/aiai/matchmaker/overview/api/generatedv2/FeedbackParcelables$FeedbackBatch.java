package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$FeedbackBatch {
    @Nullable
    private List<FeedbackParcelables$Feedback> feedback;
    private boolean hasFeedback;
    private boolean hasOverviewSessionId;
    private boolean hasScreenSessionId;
    @Nullable
    private String overviewSessionId;
    private long screenSessionId;

    private FeedbackParcelables$FeedbackBatch() {
    }

    public static FeedbackParcelables$FeedbackBatch create() {
        return new FeedbackParcelables$FeedbackBatch();
    }

    public void setFeedback(@Nullable List<FeedbackParcelables$Feedback> list) {
        this.feedback = list;
        this.hasFeedback = list != null;
    }

    @Nullable
    public List<FeedbackParcelables$Feedback> getFeedback() {
        return this.feedback;
    }

    public void setScreenSessionId(long j) {
        this.screenSessionId = j;
        this.hasScreenSessionId = true;
    }

    public void setOverviewSessionId(@Nullable String str) {
        this.overviewSessionId = str;
        this.hasOverviewSessionId = str != null;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        if (this.feedback == null) {
            bundle.putParcelableArrayList("feedback", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.feedback.size());
            for (FeedbackParcelables$Feedback feedbackParcelables$Feedback : this.feedback) {
                if (feedbackParcelables$Feedback == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(feedbackParcelables$Feedback.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("feedback", arrayList);
        }
        bundle.putLong("screenSessionId", this.screenSessionId);
        bundle.putString("overviewSessionId", this.overviewSessionId);
        return bundle;
    }
}
