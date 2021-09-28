package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$ScreenshotActionFeedback {
    @Nullable
    private String actionType;
    private boolean hasActionType;
    private boolean hasIsSmartActions;
    private boolean hasQuickShareInfo;
    private boolean isSmartActions;
    @Nullable
    private FeedbackParcelables$QuickShareInfo quickShareInfo;

    private FeedbackParcelables$ScreenshotActionFeedback() {
    }

    public static FeedbackParcelables$ScreenshotActionFeedback create() {
        return new FeedbackParcelables$ScreenshotActionFeedback();
    }

    public void setActionType(@Nullable String str) {
        this.actionType = str;
        this.hasActionType = str != null;
    }

    public void setIsSmartActions(boolean z) {
        this.isSmartActions = z;
        this.hasIsSmartActions = true;
    }

    public void setQuickShareInfo(@Nullable FeedbackParcelables$QuickShareInfo feedbackParcelables$QuickShareInfo) {
        this.quickShareInfo = feedbackParcelables$QuickShareInfo;
        this.hasQuickShareInfo = feedbackParcelables$QuickShareInfo != null;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("actionType", this.actionType);
        bundle.putBoolean("isSmartActions", this.isSmartActions);
        FeedbackParcelables$QuickShareInfo feedbackParcelables$QuickShareInfo = this.quickShareInfo;
        if (feedbackParcelables$QuickShareInfo == null) {
            bundle.putBundle("quickShareInfo", null);
        } else {
            bundle.putBundle("quickShareInfo", feedbackParcelables$QuickShareInfo.writeToBundle());
        }
        return bundle;
    }
}
