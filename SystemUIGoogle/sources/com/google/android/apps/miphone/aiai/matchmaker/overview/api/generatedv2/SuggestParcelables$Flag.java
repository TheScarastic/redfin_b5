package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class SuggestParcelables$Flag {
    private boolean hasName;
    private boolean hasValue;
    @Nullable
    private String name;
    @Nullable
    private String value;

    private SuggestParcelables$Flag(Bundle bundle) {
        readFromBundle(bundle);
    }

    private SuggestParcelables$Flag() {
    }

    public static SuggestParcelables$Flag create(Bundle bundle) {
        return new SuggestParcelables$Flag(bundle);
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("name", this.name);
        bundle.putString("value", this.value);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("name")) {
            this.hasName = false;
        } else {
            this.hasName = true;
            this.name = bundle.getString("name");
        }
        if (!bundle.containsKey("value")) {
            this.hasValue = false;
            return;
        }
        this.hasValue = true;
        this.value = bundle.getString("value");
    }
}
