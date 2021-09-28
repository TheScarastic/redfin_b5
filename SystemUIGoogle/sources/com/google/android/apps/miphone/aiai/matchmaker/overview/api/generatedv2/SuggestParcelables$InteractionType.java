package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes2.dex */
public enum SuggestParcelables$InteractionType {
    UNKNOWN(0),
    LONG_PRESS(1),
    GLEAM(2),
    CHIP(3),
    GLEAM_CHIP(4),
    SCREENSHOT_NOTIFICATION(5),
    SELECT_MODE(6),
    SETUP(7),
    COMPOSE(8),
    OCR_SMART_REPLY(9),
    PIXEL_SEARCH(10),
    OVERVIEW_SHARING(11),
    QUICK_SHARE(12);
    
    public final int value;

    SuggestParcelables$InteractionType(int i) {
        this.value = i;
    }

    public static SuggestParcelables$InteractionType create(Bundle bundle) {
        return create(bundle.getInt("value"));
    }

    public static SuggestParcelables$InteractionType create(int i) {
        if (i == 0) {
            return UNKNOWN;
        }
        if (i == 1) {
            return LONG_PRESS;
        }
        if (i == 2) {
            return GLEAM;
        }
        if (i == 3) {
            return CHIP;
        }
        if (i == 4) {
            return GLEAM_CHIP;
        }
        if (i == 5) {
            return SCREENSHOT_NOTIFICATION;
        }
        if (i == 6) {
            return SELECT_MODE;
        }
        if (i == 7) {
            return SETUP;
        }
        if (i == 8) {
            return COMPOSE;
        }
        if (i == 9) {
            return OCR_SMART_REPLY;
        }
        if (i == 10) {
            return PIXEL_SEARCH;
        }
        if (i == 11) {
            return OVERVIEW_SHARING;
        }
        if (i == 12) {
            return QUICK_SHARE;
        }
        return null;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("value", this.value);
        return bundle;
    }
}
