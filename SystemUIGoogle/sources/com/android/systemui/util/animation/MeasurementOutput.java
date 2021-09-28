package com.android.systemui.util.animation;
/* compiled from: MeasurementInput.kt */
/* loaded from: classes2.dex */
public final class MeasurementOutput {
    private int measuredHeight;
    private int measuredWidth;

    public final int component1() {
        return this.measuredWidth;
    }

    public final int component2() {
        return this.measuredHeight;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MeasurementOutput)) {
            return false;
        }
        MeasurementOutput measurementOutput = (MeasurementOutput) obj;
        return this.measuredWidth == measurementOutput.measuredWidth && this.measuredHeight == measurementOutput.measuredHeight;
    }

    public int hashCode() {
        return (Integer.hashCode(this.measuredWidth) * 31) + Integer.hashCode(this.measuredHeight);
    }

    public String toString() {
        return "MeasurementOutput(measuredWidth=" + this.measuredWidth + ", measuredHeight=" + this.measuredHeight + ')';
    }

    public MeasurementOutput(int i, int i2) {
        this.measuredWidth = i;
        this.measuredHeight = i2;
    }

    public final int getMeasuredWidth() {
        return this.measuredWidth;
    }

    public final void setMeasuredWidth(int i) {
        this.measuredWidth = i;
    }

    public final int getMeasuredHeight() {
        return this.measuredHeight;
    }

    public final void setMeasuredHeight(int i) {
        this.measuredHeight = i;
    }
}
