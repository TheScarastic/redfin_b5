package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes2.dex */
public final class SuggestParcelables$Stats {
    private long endTimestampMs;
    private long entityExtractionMs;
    private boolean hasEndTimestampMs;
    private boolean hasEntityExtractionMs;
    private boolean hasOcrDetectionMs;
    private boolean hasOcrMs;
    private boolean hasStartTimestampMs;
    private long ocrDetectionMs;
    private long ocrMs;
    private long startTimestampMs;

    private SuggestParcelables$Stats(Bundle bundle) {
        readFromBundle(bundle);
    }

    public static SuggestParcelables$Stats create(Bundle bundle) {
        return new SuggestParcelables$Stats(bundle);
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong("startTimestampMs", this.startTimestampMs);
        bundle.putLong("endTimestampMs", this.endTimestampMs);
        bundle.putLong("ocrMs", this.ocrMs);
        bundle.putLong("ocrDetectionMs", this.ocrDetectionMs);
        bundle.putLong("entityExtractionMs", this.entityExtractionMs);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("startTimestampMs")) {
            this.hasStartTimestampMs = false;
        } else {
            this.hasStartTimestampMs = true;
            this.startTimestampMs = bundle.getLong("startTimestampMs");
        }
        if (!bundle.containsKey("endTimestampMs")) {
            this.hasEndTimestampMs = false;
        } else {
            this.hasEndTimestampMs = true;
            this.endTimestampMs = bundle.getLong("endTimestampMs");
        }
        if (!bundle.containsKey("ocrMs")) {
            this.hasOcrMs = false;
        } else {
            this.hasOcrMs = true;
            this.ocrMs = bundle.getLong("ocrMs");
        }
        if (!bundle.containsKey("ocrDetectionMs")) {
            this.hasOcrDetectionMs = false;
        } else {
            this.hasOcrDetectionMs = true;
            this.ocrDetectionMs = bundle.getLong("ocrDetectionMs");
        }
        if (!bundle.containsKey("entityExtractionMs")) {
            this.hasEntityExtractionMs = false;
            return;
        }
        this.hasEntityExtractionMs = true;
        this.entityExtractionMs = bundle.getLong("entityExtractionMs");
    }
}
