package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$ScreenshotOpFeedback {
    private long durationMs;
    private boolean hasDurationMs;
    private boolean hasOp;
    private boolean hasStatus;
    private FeedbackParcelables$ScreenshotOp op;
    private FeedbackParcelables$ScreenshotOpStatus status;

    private FeedbackParcelables$ScreenshotOpFeedback() {
    }

    public static FeedbackParcelables$ScreenshotOpFeedback create() {
        return new FeedbackParcelables$ScreenshotOpFeedback();
    }

    public void setOp(FeedbackParcelables$ScreenshotOp feedbackParcelables$ScreenshotOp) {
        this.op = feedbackParcelables$ScreenshotOp;
        this.hasOp = feedbackParcelables$ScreenshotOp != null;
    }

    public void setStatus(FeedbackParcelables$ScreenshotOpStatus feedbackParcelables$ScreenshotOpStatus) {
        this.status = feedbackParcelables$ScreenshotOpStatus;
        this.hasStatus = feedbackParcelables$ScreenshotOpStatus != null;
    }

    public void setDurationMs(long j) {
        this.durationMs = j;
        this.hasDurationMs = true;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        FeedbackParcelables$ScreenshotOp feedbackParcelables$ScreenshotOp = this.op;
        if (feedbackParcelables$ScreenshotOp == null) {
            bundle.putBundle("op", null);
        } else {
            bundle.putBundle("op", feedbackParcelables$ScreenshotOp.writeToBundle());
        }
        FeedbackParcelables$ScreenshotOpStatus feedbackParcelables$ScreenshotOpStatus = this.status;
        if (feedbackParcelables$ScreenshotOpStatus == null) {
            bundle.putBundle("status", null);
        } else {
            bundle.putBundle("status", feedbackParcelables$ScreenshotOpStatus.writeToBundle());
        }
        bundle.putLong("durationMs", this.durationMs);
        return bundle;
    }
}
