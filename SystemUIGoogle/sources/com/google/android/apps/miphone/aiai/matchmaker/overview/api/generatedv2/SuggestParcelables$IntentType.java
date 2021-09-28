package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes2.dex */
public enum SuggestParcelables$IntentType {
    DEFAULT(0),
    COPY_TEXT(1),
    SHARE_IMAGE(2),
    LENS(3),
    SAVE(4),
    COPY_IMAGE(5),
    SMART_REC(6);
    
    public final int value;

    SuggestParcelables$IntentType(int i) {
        this.value = i;
    }

    public static SuggestParcelables$IntentType create(Bundle bundle) {
        return create(bundle.getInt("value"));
    }

    public static SuggestParcelables$IntentType create(int i) {
        if (i == 0) {
            return DEFAULT;
        }
        if (i == 1) {
            return COPY_TEXT;
        }
        if (i == 2) {
            return SHARE_IMAGE;
        }
        if (i == 3) {
            return LENS;
        }
        if (i == 4) {
            return SAVE;
        }
        if (i == 5) {
            return COPY_IMAGE;
        }
        if (i == 6) {
            return SMART_REC;
        }
        return null;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("value", this.value);
        return bundle;
    }
}
