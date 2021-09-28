package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes2.dex */
public final class SuggestParcelables$ExtrasInfo {
    private boolean containsBitmaps;
    private boolean containsPendingIntents;
    private boolean hasContainsBitmaps;
    private boolean hasContainsPendingIntents;

    private SuggestParcelables$ExtrasInfo(Bundle bundle) {
        readFromBundle(bundle);
    }

    public static SuggestParcelables$ExtrasInfo create(Bundle bundle) {
        return new SuggestParcelables$ExtrasInfo(bundle);
    }

    public boolean getContainsPendingIntents() {
        return this.containsPendingIntents;
    }

    public boolean getContainsBitmaps() {
        return this.containsBitmaps;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("containsPendingIntents", this.containsPendingIntents);
        bundle.putBoolean("containsBitmaps", this.containsBitmaps);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("containsPendingIntents")) {
            this.hasContainsPendingIntents = false;
        } else {
            this.hasContainsPendingIntents = true;
            this.containsPendingIntents = bundle.getBoolean("containsPendingIntents");
        }
        if (!bundle.containsKey("containsBitmaps")) {
            this.hasContainsBitmaps = false;
            return;
        }
        this.hasContainsBitmaps = true;
        this.containsBitmaps = bundle.getBoolean("containsBitmaps");
    }
}
