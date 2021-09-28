package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes2.dex */
public enum SuggestParcelables$ErrorCode {
    ERROR_CODE_SUCCESS(0),
    ERROR_CODE_UNKNOWN_ERROR(1),
    ERROR_CODE_TIMEOUT(2),
    ERROR_CODE_NO_SCREEN_CONTENT(3),
    ERROR_CODE_NO_SUPPORTED_LOCALES(4);
    
    public final int value;

    SuggestParcelables$ErrorCode(int i) {
        this.value = i;
    }

    public static SuggestParcelables$ErrorCode create(Bundle bundle) {
        return create(bundle.getInt("value"));
    }

    public static SuggestParcelables$ErrorCode create(int i) {
        if (i == 0) {
            return ERROR_CODE_SUCCESS;
        }
        if (i == 1) {
            return ERROR_CODE_UNKNOWN_ERROR;
        }
        if (i == 2) {
            return ERROR_CODE_TIMEOUT;
        }
        if (i == 3) {
            return ERROR_CODE_NO_SCREEN_CONTENT;
        }
        if (i == 4) {
            return ERROR_CODE_NO_SUPPORTED_LOCALES;
        }
        return null;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("value", this.value);
        return bundle;
    }
}
