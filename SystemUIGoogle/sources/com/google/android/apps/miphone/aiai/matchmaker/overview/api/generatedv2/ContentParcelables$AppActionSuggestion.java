package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class ContentParcelables$AppActionSuggestion {
    @Nullable
    private String displayText;
    private boolean hasDisplayText;
    private boolean hasSubtitle;
    @Nullable
    private String subtitle;

    private ContentParcelables$AppActionSuggestion(Bundle bundle) {
        readFromBundle(bundle);
    }

    public static ContentParcelables$AppActionSuggestion create(Bundle bundle) {
        return new ContentParcelables$AppActionSuggestion(bundle);
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("displayText", this.displayText);
        bundle.putString("subtitle", this.subtitle);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("displayText")) {
            this.hasDisplayText = false;
        } else {
            this.hasDisplayText = true;
            this.displayText = bundle.getString("displayText");
        }
        if (!bundle.containsKey("subtitle")) {
            this.hasSubtitle = false;
            return;
        }
        this.hasSubtitle = true;
        this.subtitle = bundle.getString("subtitle");
    }
}
