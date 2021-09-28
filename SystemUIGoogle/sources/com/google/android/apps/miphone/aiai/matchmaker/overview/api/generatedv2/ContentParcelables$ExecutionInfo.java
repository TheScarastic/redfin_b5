package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class ContentParcelables$ExecutionInfo {
    @Nullable
    private String deeplinkUri;
    private boolean hasDeeplinkUri;

    private ContentParcelables$ExecutionInfo(Bundle bundle) {
        readFromBundle(bundle);
    }

    public static ContentParcelables$ExecutionInfo create(Bundle bundle) {
        return new ContentParcelables$ExecutionInfo(bundle);
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("deeplinkUri", this.deeplinkUri);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("deeplinkUri")) {
            this.hasDeeplinkUri = false;
            return;
        }
        this.hasDeeplinkUri = true;
        this.deeplinkUri = bundle.getString("deeplinkUri");
    }
}
