package com.android.systemui.accessibility.floatingmenu;

import android.text.TextUtils;
/* loaded from: classes.dex */
public class Position {
    private static final TextUtils.SimpleStringSplitter sStringCommaSplitter = new TextUtils.SimpleStringSplitter(',');
    private float mPercentageX;
    private float mPercentageY;

    public static Position fromString(String str) {
        TextUtils.SimpleStringSplitter simpleStringSplitter = sStringCommaSplitter;
        simpleStringSplitter.setString(str);
        if (simpleStringSplitter.hasNext()) {
            return new Position(Float.parseFloat(simpleStringSplitter.next()), Float.parseFloat(simpleStringSplitter.next()));
        }
        throw new IllegalArgumentException("Invalid Position string: " + str);
    }

    /* access modifiers changed from: package-private */
    public Position(float f, float f2) {
        update(f, f2);
    }

    public String toString() {
        return this.mPercentageX + ", " + this.mPercentageY;
    }

    public void update(float f, float f2) {
        this.mPercentageX = f;
        this.mPercentageY = f2;
    }

    public float getPercentageX() {
        return this.mPercentageX;
    }

    public float getPercentageY() {
        return this.mPercentageY;
    }
}
