package com.google.android.apps.miphone.aiai.matchmaker.overview.common;

import android.app.Notification;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ContentParcelables$Contents;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.EntitiesData;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$FeedbackBatch;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.InteractionContextParcelables$InteractionContext;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ParserParcelables$ParsedViewHierarchy;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public final class BundleUtils {
    private final int bundleVersion;

    public static <T extends Parcelable> T extractParcelable(Bundle bundle, String str, Class<T> cls) {
        bundle.setClassLoader(cls.getClassLoader());
        return (T) bundle.getParcelable(str);
    }

    public static BundleUtils createWithBackwardsCompatVersion() {
        return new BundleUtils(4);
    }

    public Bundle obtainContextImageBundle(boolean z, String str, String str2, long j) {
        Bundle bundle = new Bundle();
        bundle.putInt("CONTEXT_IMAGE_BUNDLE_VERSION_KEY", 1);
        bundle.putBoolean("CONTEXT_IMAGE_PRIMARY_TASK_KEY", z);
        bundle.putString("CONTEXT_IMAGE_PACKAGE_NAME_KEY", str);
        bundle.putString("CONTEXT_IMAGE_ACTIVITY_NAME_KEY", str2);
        bundle.putLong("CONTEXT_IMAGE_CAPTURE_TIME_MS_KEY", j);
        return bundle;
    }

    public Bundle obtainScreenshotContextImageBundle(boolean z, Uri uri, String str, String str2, long j) {
        Bundle obtainContextImageBundle = obtainContextImageBundle(z, str, str2, j);
        obtainContextImageBundle.putParcelable("CONTEXT_IMAGE_BITMAP_URI_KEY", uri);
        return obtainContextImageBundle;
    }

    public Bundle createScreenshotActionsResponse(ArrayList<Notification.Action> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ScreenshotNotificationActions", arrayList);
        return bundle;
    }

    public ContentParcelables$Contents extractContents(Bundle bundle) {
        Bundle bundle2 = bundle.getBundle("Contents");
        return bundle2 == null ? ContentParcelables$Contents.create() : ContentParcelables$Contents.create(bundle2);
    }

    public EntitiesData extractEntitiesParcelable(Bundle bundle) {
        return (EntitiesData) extractParcelable(bundle, "EntitiesData", EntitiesData.class);
    }

    public Bundle createSelectionsRequest(String str, String str2, int i, long j, @Nullable InteractionContextParcelables$InteractionContext interactionContextParcelables$InteractionContext, @Nullable Bundle bundle, @Nullable ParserParcelables$ParsedViewHierarchy parserParcelables$ParsedViewHierarchy) {
        return ContentSelectionBundle.create(str, str2, i, j, interactionContextParcelables$InteractionContext, bundle, parserParcelables$ParsedViewHierarchy, this.bundleVersion).createBundle();
    }

    public Bundle createFeedbackRequest(@Nullable FeedbackParcelables$FeedbackBatch feedbackParcelables$FeedbackBatch) {
        return FeedbackBundle.create(feedbackParcelables$FeedbackBatch, this.bundleVersion).createBundle();
    }

    public Bundle createClassificationsRequest(String str, String str2, int i, long j, @Nullable Bundle bundle, @Nullable InteractionContextParcelables$InteractionContext interactionContextParcelables$InteractionContext, ContentParcelables$Contents contentParcelables$Contents) {
        return ContentClassificationsBundle.create(str, str2, i, j, bundle, contentParcelables$Contents, interactionContextParcelables$InteractionContext, this.bundleVersion).createBundle();
    }

    private BundleUtils(int i) {
        this.bundleVersion = i;
    }
}
