package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes2.dex */
public enum ContentParcelables$AppIconType {
    UNDEFINED(0),
    URI(1),
    DRAWABLE(2);
    
    public final int value;

    ContentParcelables$AppIconType(int i) {
        this.value = i;
    }

    public static ContentParcelables$AppIconType create(Bundle bundle) {
        return create(bundle.getInt("value"));
    }

    public static ContentParcelables$AppIconType create(int i) {
        if (i == 0) {
            return UNDEFINED;
        }
        if (i == 1) {
            return URI;
        }
        if (i == 2) {
            return DRAWABLE;
        }
        return null;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("value", this.value);
        return bundle;
    }
}
