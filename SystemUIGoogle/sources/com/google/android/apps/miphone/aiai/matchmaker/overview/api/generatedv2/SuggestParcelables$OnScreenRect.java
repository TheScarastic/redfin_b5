package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes2.dex */
public final class SuggestParcelables$OnScreenRect {
    private boolean hasHeight;
    private boolean hasLeft;
    private boolean hasTop;
    private boolean hasWidth;
    private float height;
    private float left;
    private float top;
    private float width;

    private SuggestParcelables$OnScreenRect(Bundle bundle) {
        readFromBundle(bundle);
    }

    public static SuggestParcelables$OnScreenRect create(Bundle bundle) {
        return new SuggestParcelables$OnScreenRect(bundle);
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putFloat("left", this.left);
        bundle.putFloat("top", this.top);
        bundle.putFloat("width", this.width);
        bundle.putFloat("height", this.height);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("left")) {
            this.hasLeft = false;
        } else {
            this.hasLeft = true;
            this.left = bundle.getFloat("left");
        }
        if (!bundle.containsKey("top")) {
            this.hasTop = false;
        } else {
            this.hasTop = true;
            this.top = bundle.getFloat("top");
        }
        if (!bundle.containsKey("width")) {
            this.hasWidth = false;
        } else {
            this.hasWidth = true;
            this.width = bundle.getFloat("width");
        }
        if (!bundle.containsKey("height")) {
            this.hasHeight = false;
            return;
        }
        this.hasHeight = true;
        this.height = bundle.getFloat("height");
    }
}
