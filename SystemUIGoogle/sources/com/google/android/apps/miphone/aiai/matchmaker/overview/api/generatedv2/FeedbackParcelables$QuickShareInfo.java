package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$QuickShareInfo {
    @Nullable
    private String contentUri;
    private boolean hasContentUri;
    private boolean hasTargetClassName;
    private boolean hasTargetPackageName;
    private boolean hasTargetShortcutId;
    @Nullable
    private String targetClassName;
    @Nullable
    private String targetPackageName;
    @Nullable
    private String targetShortcutId;

    private FeedbackParcelables$QuickShareInfo() {
    }

    public static FeedbackParcelables$QuickShareInfo create() {
        return new FeedbackParcelables$QuickShareInfo();
    }

    public void setContentUri(@Nullable String str) {
        this.contentUri = str;
        this.hasContentUri = str != null;
    }

    public void setTargetPackageName(@Nullable String str) {
        this.targetPackageName = str;
        this.hasTargetPackageName = str != null;
    }

    public void setTargetClassName(@Nullable String str) {
        this.targetClassName = str;
        this.hasTargetClassName = str != null;
    }

    public void setTargetShortcutId(@Nullable String str) {
        this.targetShortcutId = str;
        this.hasTargetShortcutId = str != null;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("contentUri", this.contentUri);
        bundle.putString("targetPackageName", this.targetPackageName);
        bundle.putString("targetClassName", this.targetClassName);
        bundle.putString("targetShortcutId", this.targetShortcutId);
        return bundle;
    }
}
