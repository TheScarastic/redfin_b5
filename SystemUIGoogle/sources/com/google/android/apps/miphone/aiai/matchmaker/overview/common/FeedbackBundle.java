package com.google.android.apps.miphone.aiai.matchmaker.overview.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$FeedbackBatch;
/* loaded from: classes2.dex */
public final class FeedbackBundle {
    public final int bundleVersion;
    @Nullable
    public final FeedbackParcelables$FeedbackBatch feedbackBatch;

    public static FeedbackBundle create(@Nullable FeedbackParcelables$FeedbackBatch feedbackParcelables$FeedbackBatch, int i) {
        return new FeedbackBundle(feedbackParcelables$FeedbackBatch, i);
    }

    private static Bundle createBundle(Bundle bundle, int i) {
        bundle.putInt("Version", i);
        bundle.putInt("BundleTypedVersion", 6);
        return bundle;
    }

    public Bundle createBundle() {
        Bundle bundle = new Bundle();
        FeedbackParcelables$FeedbackBatch feedbackParcelables$FeedbackBatch = this.feedbackBatch;
        if (feedbackParcelables$FeedbackBatch == null) {
            bundle.putBundle("FeedbackBatch", null);
        } else {
            bundle.putBundle("FeedbackBatch", feedbackParcelables$FeedbackBatch.writeToBundle());
        }
        return createBundle(bundle, this.bundleVersion);
    }

    private FeedbackBundle(@Nullable FeedbackParcelables$FeedbackBatch feedbackParcelables$FeedbackBatch, int i) {
        this.feedbackBatch = feedbackParcelables$FeedbackBatch;
        this.bundleVersion = i;
    }
}
