package com.google.android.apps.miphone.aiai.matchmaker.overview.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.InteractionContextParcelables$InteractionContext;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ParserParcelables$ParsedViewHierarchy;
/* loaded from: classes2.dex */
public final class ContentSelectionBundle {
    public final String activityName;
    @Nullable
    public final Bundle assistBundle;
    public final int bundleVersion;
    public final long captureTimestampMs;
    @Nullable
    public final InteractionContextParcelables$InteractionContext interactionContext;
    public final String packageName;
    @Nullable
    public final ParserParcelables$ParsedViewHierarchy parsedViewHierarchy;
    public final int taskId;

    public static ContentSelectionBundle create(String str, String str2, int i, long j, @Nullable InteractionContextParcelables$InteractionContext interactionContextParcelables$InteractionContext, @Nullable Bundle bundle, @Nullable ParserParcelables$ParsedViewHierarchy parserParcelables$ParsedViewHierarchy, int i2) {
        return new ContentSelectionBundle(str, str2, i, j, interactionContextParcelables$InteractionContext, bundle, parserParcelables$ParsedViewHierarchy, i2);
    }

    public Bundle createBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("PackageName", this.packageName);
        bundle.putString("ActivityName", this.activityName);
        bundle.putInt("TaskId", this.taskId);
        bundle.putLong("CaptureTimestampMs", this.captureTimestampMs);
        InteractionContextParcelables$InteractionContext interactionContextParcelables$InteractionContext = this.interactionContext;
        if (interactionContextParcelables$InteractionContext == null) {
            bundle.putBundle("InteractionContext", null);
        } else {
            bundle.putBundle("InteractionContext", interactionContextParcelables$InteractionContext.writeToBundle());
        }
        bundle.putBundle("AssistBundle", this.assistBundle);
        ParserParcelables$ParsedViewHierarchy parserParcelables$ParsedViewHierarchy = this.parsedViewHierarchy;
        if (parserParcelables$ParsedViewHierarchy == null) {
            bundle.putBundle("ParsedViewHierarchy", null);
        } else {
            bundle.putBundle("ParsedViewHierarchy", parserParcelables$ParsedViewHierarchy.writeToBundle());
        }
        bundle.putInt("Version", this.bundleVersion);
        bundle.putInt("BundleTypedVersion", 3);
        return bundle;
    }

    private ContentSelectionBundle(String str, String str2, int i, long j, @Nullable InteractionContextParcelables$InteractionContext interactionContextParcelables$InteractionContext, @Nullable Bundle bundle, @Nullable ParserParcelables$ParsedViewHierarchy parserParcelables$ParsedViewHierarchy, int i2) {
        this.packageName = str;
        this.activityName = str2;
        this.taskId = i;
        this.captureTimestampMs = j;
        this.interactionContext = interactionContextParcelables$InteractionContext;
        this.assistBundle = bundle;
        this.parsedViewHierarchy = parserParcelables$ParsedViewHierarchy;
        this.bundleVersion = i2;
    }
}
