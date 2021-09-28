package com.android.systemui.media;

import com.android.systemui.util.animation.DisappearParameters;
import com.android.systemui.util.animation.MeasurementInput;
/* compiled from: MediaHost.kt */
/* loaded from: classes.dex */
public interface MediaHostState {
    MediaHostState copy();

    DisappearParameters getDisappearParameters();

    float getExpansion();

    boolean getFalsingProtectionNeeded();

    MeasurementInput getMeasurementInput();

    boolean getShowsOnlyActiveMedia();

    boolean getVisible();

    void setExpansion(float f);
}
