package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$FeedbackBatch;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
/* loaded from: classes2.dex */
public abstract class ContentSuggestionsServiceWrapper {
    protected final Executor asyncExecutor;
    protected final Executor callbackExecutor;
    protected final Executor loggingExecutor;
    protected final Executor uiExecutor;

    /* loaded from: classes2.dex */
    public interface BundleCallback {
        void onResult(Bundle bundle);
    }

    public abstract void classifyContentSelections(Bundle bundle, BundleCallback bundleCallback);

    public abstract void notifyInteractionAsync(String str, Supplier<Bundle> supplier, @Nullable SuggestListener suggestListener, @Nullable FeedbackParcelables$FeedbackBatch feedbackParcelables$FeedbackBatch);

    public abstract void processContextImage(int i, @Nullable Bitmap bitmap, Bundle bundle);

    public abstract void runOnServiceConnection(Runnable runnable);

    public abstract void suggestContentSelections(int i, Bundle bundle, BundleCallback bundleCallback);

    /* access modifiers changed from: protected */
    public ContentSuggestionsServiceWrapper(Executor executor, Executor executor2, Executor executor3, Executor executor4) {
        this.uiExecutor = executor;
        this.asyncExecutor = executor2;
        this.loggingExecutor = executor3;
        this.callbackExecutor = executor4;
    }

    public void connectAndRunAsync(Runnable runnable) {
        this.asyncExecutor.execute(runnable);
    }
}
