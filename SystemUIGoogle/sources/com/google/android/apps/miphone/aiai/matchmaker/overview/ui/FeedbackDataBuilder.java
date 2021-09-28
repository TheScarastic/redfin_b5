package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$Feedback;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$FeedbackBatch;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$QuickShareInfo;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotActionFeedback;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotFeedback;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotOp;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotOpFeedback;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotOpStatus;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$InteractionType;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.utils.Utils;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class FeedbackDataBuilder {
    FeedbackParcelables$FeedbackBatch feedbackBatch;
    final String overviewSessionId;
    final List<FeedbackParcelables$Feedback> feedbacks = new ArrayList();
    final int screenSessionId = 0;

    public FeedbackParcelables$FeedbackBatch build() {
        FeedbackParcelables$FeedbackBatch create = FeedbackParcelables$FeedbackBatch.create();
        create.setScreenSessionId((long) this.screenSessionId);
        create.setOverviewSessionId(this.overviewSessionId);
        create.setFeedback((List) Utils.checkNotNull(this.feedbacks));
        this.feedbackBatch = create;
        return create;
    }

    /* access modifiers changed from: package-private */
    public static FeedbackDataBuilder newBuilder(String str) {
        return new FeedbackDataBuilder(str);
    }

    /* access modifiers changed from: package-private */
    public FeedbackDataBuilder addScreenshotActionFeedback(String str, String str2, boolean z, @Nullable Intent intent) {
        FeedbackParcelables$ScreenshotActionFeedback create = FeedbackParcelables$ScreenshotActionFeedback.create();
        create.setActionType(str2);
        create.setIsSmartActions(z);
        addQuickShareInfo(create, str2, intent);
        FeedbackParcelables$ScreenshotFeedback create2 = FeedbackParcelables$ScreenshotFeedback.create();
        create2.setScreenshotId(str);
        create2.setScreenshotFeedback(create);
        addFeedback().setFeedback(create2);
        return this;
    }

    /* access modifiers changed from: package-private */
    public FeedbackDataBuilder addScreenshotOpFeedback(String str, FeedbackParcelables$ScreenshotOp feedbackParcelables$ScreenshotOp, FeedbackParcelables$ScreenshotOpStatus feedbackParcelables$ScreenshotOpStatus, long j) {
        FeedbackParcelables$ScreenshotOpFeedback create = FeedbackParcelables$ScreenshotOpFeedback.create();
        create.setDurationMs(j);
        create.setOp(feedbackParcelables$ScreenshotOp);
        create.setStatus(feedbackParcelables$ScreenshotOpStatus);
        FeedbackParcelables$ScreenshotFeedback create2 = FeedbackParcelables$ScreenshotFeedback.create();
        create2.setScreenshotId(str);
        create2.setScreenshotFeedback(create);
        addFeedback().setFeedback(create2);
        return this;
    }

    private static void addQuickShareInfo(FeedbackParcelables$ScreenshotActionFeedback feedbackParcelables$ScreenshotActionFeedback, String str, @Nullable Intent intent) {
        if (SuggestParcelables$InteractionType.QUICK_SHARE.name().equals(str) && intent != null && intent.getComponent() != null) {
            FeedbackParcelables$QuickShareInfo create = FeedbackParcelables$QuickShareInfo.create();
            Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.STREAM");
            if (uri != null) {
                create.setContentUri(uri.toString());
            }
            create.setTargetPackageName(((ComponentName) Utils.checkNotNull(intent.getComponent())).getPackageName());
            create.setTargetClassName(((ComponentName) Utils.checkNotNull(intent.getComponent())).getClassName());
            create.setTargetShortcutId(intent.getStringExtra("android.intent.extra.shortcut.ID"));
            feedbackParcelables$ScreenshotActionFeedback.setQuickShareInfo(create);
        }
    }

    private FeedbackParcelables$Feedback addFeedback() {
        FeedbackParcelables$Feedback create = FeedbackParcelables$Feedback.create();
        this.feedbacks.add(create);
        return create;
    }

    private FeedbackDataBuilder(String str) {
        this.overviewSessionId = str;
    }
}
