package com.android.systemui.qs.tileimpl;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: HeightOverrideable.kt */
/* loaded from: classes.dex */
public interface HeightOverrideable {
    void resetOverride();

    void setHeightOverride(int i);

    /* compiled from: HeightOverrideable.kt */
    /* loaded from: classes.dex */
    public static final class DefaultImpls {
        public static void resetOverride(HeightOverrideable heightOverrideable) {
            Intrinsics.checkNotNullParameter(heightOverrideable, "this");
            heightOverrideable.setHeightOverride(-1);
        }
    }
}
