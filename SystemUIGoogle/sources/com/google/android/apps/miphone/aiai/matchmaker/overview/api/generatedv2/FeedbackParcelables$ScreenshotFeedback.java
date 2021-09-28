package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$ScreenshotFeedback {
    private boolean hasScreenshotFeedback;
    private boolean hasScreenshotId;
    @Nullable
    private Object screenshotFeedback;
    @Nullable
    private String screenshotId;

    private FeedbackParcelables$ScreenshotFeedback() {
    }

    public static FeedbackParcelables$ScreenshotFeedback create() {
        return new FeedbackParcelables$ScreenshotFeedback();
    }

    public void setScreenshotFeedback(@Nullable Object obj) {
        this.screenshotFeedback = obj;
        this.hasScreenshotFeedback = obj != null;
    }

    public void setScreenshotId(@Nullable String str) {
        this.screenshotId = str;
        this.hasScreenshotId = str != null;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        if (this.screenshotFeedback instanceof FeedbackParcelables$ScreenshotOpFeedback) {
            bundle.putInt("screenshotFeedback#tag", 2);
            FeedbackParcelables$ScreenshotOpFeedback feedbackParcelables$ScreenshotOpFeedback = (FeedbackParcelables$ScreenshotOpFeedback) this.screenshotFeedback;
            if (feedbackParcelables$ScreenshotOpFeedback == null) {
                bundle.putBundle("screenshotFeedback", null);
            } else {
                bundle.putBundle("screenshotFeedback", feedbackParcelables$ScreenshotOpFeedback.writeToBundle());
            }
        }
        if (this.screenshotFeedback instanceof FeedbackParcelables$ScreenshotActionFeedback) {
            bundle.putInt("screenshotFeedback#tag", 3);
            FeedbackParcelables$ScreenshotActionFeedback feedbackParcelables$ScreenshotActionFeedback = (FeedbackParcelables$ScreenshotActionFeedback) this.screenshotFeedback;
            if (feedbackParcelables$ScreenshotActionFeedback == null) {
                bundle.putBundle("screenshotFeedback", null);
            } else {
                bundle.putBundle("screenshotFeedback", feedbackParcelables$ScreenshotActionFeedback.writeToBundle());
            }
        }
        bundle.putString("screenshotId", this.screenshotId);
        return bundle;
    }
}
