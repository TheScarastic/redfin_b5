package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class InteractionContextParcelables$InteractionEvent {
    private int action;
    private int actionButton;
    private int actionIndex;
    private int actionMasked;
    @Nullable
    private SuggestParcelables$OnScreenRect bitmapCropRect;
    private float bitmapScaleX;
    private float bitmapScaleY;
    private int buttonState;
    private int deviceId;
    private long downTimeMs;
    private int edgeFlags;
    private long eventTimeMs;
    private int motionEventFlags;
    private float orientation;
    private float rawX;
    private float rawY;
    private int source;
    private float toolMajor;
    private float toolMinor;
    private float x;
    private float xPrecision;
    private float y;
    private float yPrecision;

    private InteractionContextParcelables$InteractionEvent() {
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("action", this.action);
        bundle.putInt("actionButton", this.actionButton);
        bundle.putInt("actionIndex", this.actionIndex);
        bundle.putInt("actionMasked", this.actionMasked);
        bundle.putInt("buttonState", this.buttonState);
        bundle.putInt("deviceId", this.deviceId);
        bundle.putLong("downTimeMs", this.downTimeMs);
        bundle.putInt("edgeFlags", this.edgeFlags);
        bundle.putInt("motionEventFlags", this.motionEventFlags);
        bundle.putFloat("orientation", this.orientation);
        bundle.putFloat("rawX", this.rawX);
        bundle.putFloat("rawY", this.rawY);
        bundle.putInt("source", this.source);
        bundle.putFloat("toolMajor", this.toolMajor);
        bundle.putFloat("toolMinor", this.toolMinor);
        bundle.putFloat("x", this.x);
        bundle.putFloat("y", this.y);
        bundle.putFloat("xPrecision", this.xPrecision);
        bundle.putFloat("yPrecision", this.yPrecision);
        SuggestParcelables$OnScreenRect suggestParcelables$OnScreenRect = this.bitmapCropRect;
        if (suggestParcelables$OnScreenRect == null) {
            bundle.putBundle("bitmapCropRect", null);
        } else {
            bundle.putBundle("bitmapCropRect", suggestParcelables$OnScreenRect.writeToBundle());
        }
        bundle.putFloat("bitmapScaleX", this.bitmapScaleX);
        bundle.putFloat("bitmapScaleY", this.bitmapScaleY);
        bundle.putLong("eventTimeMs", this.eventTimeMs);
        return bundle;
    }
}
