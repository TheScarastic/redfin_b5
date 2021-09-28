package com.android.systemui.assist.ui;

import android.util.Log;
/* loaded from: classes.dex */
public final class EdgeLight {
    private int mColor;
    private float mLength;
    private float mStart;

    public static EdgeLight[] copy(EdgeLight[] edgeLightArr) {
        EdgeLight[] edgeLightArr2 = new EdgeLight[edgeLightArr.length];
        for (int i = 0; i < edgeLightArr.length; i++) {
            edgeLightArr2[i] = new EdgeLight(edgeLightArr[i]);
        }
        return edgeLightArr2;
    }

    public EdgeLight(int i, float f, float f2) {
        this.mColor = i;
        this.mStart = f;
        this.mLength = f2;
    }

    public EdgeLight(EdgeLight edgeLight) {
        this.mColor = edgeLight.getColor();
        this.mStart = edgeLight.getStart();
        this.mLength = edgeLight.getLength();
    }

    public int getColor() {
        return this.mColor;
    }

    public boolean setColor(int i) {
        boolean z = this.mColor != i;
        this.mColor = i;
        return z;
    }

    public float getLength() {
        return this.mLength;
    }

    public void setLength(float f) {
        this.mLength = f;
    }

    public void setEndpoints(float f, float f2) {
        if (f > f2) {
            Log.e("EdgeLight", String.format("Endpoint must be >= start (add 1 if necessary). Got [%f, %f]", Float.valueOf(f), Float.valueOf(f2)));
            return;
        }
        this.mStart = f;
        this.mLength = f2 - f;
    }

    public float getStart() {
        return this.mStart;
    }

    public void setStart(float f) {
        this.mStart = f;
    }

    public float getEnd() {
        return this.mStart + this.mLength;
    }
}
