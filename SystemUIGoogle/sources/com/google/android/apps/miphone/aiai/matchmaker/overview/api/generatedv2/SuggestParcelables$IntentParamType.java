package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes2.dex */
public enum SuggestParcelables$IntentParamType {
    INTENT_PARAM_TYPE_UNKNOWN(0),
    INTENT_PARAM_TYPE_STRING(1),
    INTENT_PARAM_TYPE_INT(2),
    INTENT_PARAM_TYPE_FLOAT(3),
    INTENT_PARAM_TYPE_LONG(4),
    INTENT_PARAM_TYPE_INTENT(5),
    INTENT_PARAM_TYPE_CONTENT_URI(6),
    INTENT_PARAM_TYPE_BOOL(7);
    
    public final int value;

    SuggestParcelables$IntentParamType(int i) {
        this.value = i;
    }

    public static SuggestParcelables$IntentParamType create(Bundle bundle) {
        return create(bundle.getInt("value"));
    }

    public static SuggestParcelables$IntentParamType create(int i) {
        if (i == 0) {
            return INTENT_PARAM_TYPE_UNKNOWN;
        }
        if (i == 1) {
            return INTENT_PARAM_TYPE_STRING;
        }
        if (i == 2) {
            return INTENT_PARAM_TYPE_INT;
        }
        if (i == 3) {
            return INTENT_PARAM_TYPE_FLOAT;
        }
        if (i == 4) {
            return INTENT_PARAM_TYPE_LONG;
        }
        if (i == 5) {
            return INTENT_PARAM_TYPE_INTENT;
        }
        if (i == 6) {
            return INTENT_PARAM_TYPE_CONTENT_URI;
        }
        if (i == 7) {
            return INTENT_PARAM_TYPE_BOOL;
        }
        return null;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("value", this.value);
        return bundle;
    }
}
