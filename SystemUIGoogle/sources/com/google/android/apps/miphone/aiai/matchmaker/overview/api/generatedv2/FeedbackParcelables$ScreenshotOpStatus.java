package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes2.dex */
public enum FeedbackParcelables$ScreenshotOpStatus {
    OP_STATUS_UNKNOWN(0),
    SUCCESS(1),
    ERROR(2),
    TIMEOUT(3);
    
    public final int value;

    FeedbackParcelables$ScreenshotOpStatus(int i) {
        this.value = i;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("value", this.value);
        return bundle;
    }
}
