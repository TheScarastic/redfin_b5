package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes2.dex */
public final class ParserParcelables$WindowNode {
    private int displayId;
    private int height;
    private int left;
    private int top;
    private int width;

    private ParserParcelables$WindowNode() {
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("displayId", this.displayId);
        bundle.putBundle("rootViewNode", null);
        bundle.putInt("left", this.left);
        bundle.putInt("top", this.top);
        bundle.putInt("width", this.width);
        bundle.putInt("height", this.height);
        return bundle;
    }
}
