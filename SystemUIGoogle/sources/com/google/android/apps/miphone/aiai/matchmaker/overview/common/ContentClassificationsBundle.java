package com.google.android.apps.miphone.aiai.matchmaker.overview.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ContentParcelables$Contents;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.InteractionContextParcelables$InteractionContext;
/* loaded from: classes2.dex */
public final class ContentClassificationsBundle {
    public final String activityName;
    @Nullable
    public final Bundle assistBundle;
    public final int bundleVersion;
    public final long captureTimestampMs;
    public final ContentParcelables$Contents contents;
    @Nullable
    public final InteractionContextParcelables$InteractionContext interactionContext;
    public final String packageName;
    public final int taskId;

    public static ContentClassificationsBundle create(String str, String str2, int i, long j, @Nullable Bundle bundle, ContentParcelables$Contents contentParcelables$Contents, @Nullable InteractionContextParcelables$InteractionContext interactionContextParcelables$InteractionContext, int i2) {
        return new ContentClassificationsBundle(str, str2, i, j, bundle, contentParcelables$Contents, interactionContextParcelables$InteractionContext, i2);
    }

    public Bundle createBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("PackageName", this.packageName);
        bundle.putString("ActivityName", this.activityName);
        bundle.putInt("TaskId", this.taskId);
        bundle.putLong("CaptureTimestampMs", this.captureTimestampMs);
        bundle.putBundle("AssistBundle", this.assistBundle);
        ContentParcelables$Contents contentParcelables$Contents = this.contents;
        if (contentParcelables$Contents == null) {
            bundle.putBundle("Contents", null);
        } else {
            bundle.putBundle("Contents", contentParcelables$Contents.writeToBundle());
        }
        InteractionContextParcelables$InteractionContext interactionContextParcelables$InteractionContext = this.interactionContext;
        if (interactionContextParcelables$InteractionContext == null) {
            bundle.putBundle("InteractionContext", null);
        } else {
            bundle.putBundle("InteractionContext", interactionContextParcelables$InteractionContext.writeToBundle());
        }
        bundle.putInt("Version", this.bundleVersion);
        bundle.putInt("BundleTypedVersion", 3);
        return bundle;
    }

    private ContentClassificationsBundle(String str, String str2, int i, long j, @Nullable Bundle bundle, ContentParcelables$Contents contentParcelables$Contents, @Nullable InteractionContextParcelables$InteractionContext interactionContextParcelables$InteractionContext, int i2) {
        this.packageName = str;
        this.activityName = str2;
        this.taskId = i;
        this.captureTimestampMs = j;
        this.assistBundle = bundle;
        this.contents = contentParcelables$Contents;
        this.interactionContext = interactionContextParcelables$InteractionContext;
        this.bundleVersion = i2;
    }
}
