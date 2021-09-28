package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes2.dex */
public enum SuggestParcelables$ContentType {
    CONTENT_TYPE_UNKNOWN(0),
    CONTENT_TYPE_TEXT(1),
    CONTENT_TYPE_IMAGE(2);
    
    public final int value;

    SuggestParcelables$ContentType(int i) {
        this.value = i;
    }

    public static SuggestParcelables$ContentType create(Bundle bundle) {
        return create(bundle.getInt("value"));
    }

    public static SuggestParcelables$ContentType create(int i) {
        if (i == 0) {
            return CONTENT_TYPE_UNKNOWN;
        }
        if (i == 1) {
            return CONTENT_TYPE_TEXT;
        }
        if (i == 2) {
            return CONTENT_TYPE_IMAGE;
        }
        return null;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("value", this.value);
        return bundle;
    }
}
