package com.android.keyguard;

import android.text.Layout;
/* compiled from: TextInterpolator.kt */
/* loaded from: classes.dex */
public final class TextInterpolatorKt {
    /* access modifiers changed from: private */
    public static final float getDrawOrigin(Layout layout, int i) {
        if (layout.getParagraphDirection(i) == 1) {
            return layout.getLineLeft(i);
        }
        return layout.getLineRight(i);
    }
}
