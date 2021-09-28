package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes2.dex */
public enum FeedbackParcelables$ScreenshotOp {
    OP_UNKNOWN(0),
    RETRIEVE_SMART_ACTIONS(1),
    REQUEST_SMART_ACTIONS(2),
    WAIT_FOR_SMART_ACTIONS(3);
    
    public final int value;

    FeedbackParcelables$ScreenshotOp(int i) {
        this.value = i;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("value", this.value);
        return bundle;
    }
}
