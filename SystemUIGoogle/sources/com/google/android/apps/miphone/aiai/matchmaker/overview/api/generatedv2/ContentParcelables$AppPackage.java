package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class ContentParcelables$AppPackage {
    private boolean hasPackageName;
    @Nullable
    private String packageName;

    private ContentParcelables$AppPackage(Bundle bundle) {
        readFromBundle(bundle);
    }

    public static ContentParcelables$AppPackage create(Bundle bundle) {
        return new ContentParcelables$AppPackage(bundle);
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("packageName", this.packageName);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("packageName")) {
            this.hasPackageName = false;
            return;
        }
        this.hasPackageName = true;
        this.packageName = bundle.getString("packageName");
    }
}
