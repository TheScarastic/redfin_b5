package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$ComposeActionFeedback {
    @Nullable
    private String actionToken;
    private boolean isClicked;
    private boolean isOverflow;
    private boolean isPinned;
    private boolean isSelectedText;
    private int rank;

    private FeedbackParcelables$ComposeActionFeedback() {
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("actionToken", this.actionToken);
        bundle.putInt("rank", this.rank);
        bundle.putBoolean("isClicked", this.isClicked);
        bundle.putBoolean("isPinned", this.isPinned);
        bundle.putBoolean("isOverflow", this.isOverflow);
        bundle.putBoolean("isSelectedText", this.isSelectedText);
        return bundle;
    }
}
