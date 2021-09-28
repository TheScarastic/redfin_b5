package com.android.systemui.statusbar.phone.ongoingcall;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Chronometer;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
/* compiled from: OngoingCallChronometer.kt */
/* loaded from: classes.dex */
public final class OngoingCallChronometer extends Chronometer {
    private int minimumTextWidth;
    private boolean shouldHideText;

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public OngoingCallChronometer(Context context) {
        this(context, null, 0, 6, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public OngoingCallChronometer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ OngoingCallChronometer(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public OngoingCallChronometer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    @Override // android.widget.Chronometer
    public void setBase(long j) {
        this.minimumTextWidth = 0;
        this.shouldHideText = false;
        setVisibility(0);
        super.setBase(j);
    }

    public final void setShouldHideText(boolean z) {
        this.shouldHideText = z;
        requestLayout();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.shouldHideText) {
            setMeasuredDimension(0, 0);
            return;
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(0, 0), i2);
        int measuredWidth = getMeasuredWidth();
        if (measuredWidth > Chronometer.resolveSize(measuredWidth, i)) {
            this.shouldHideText = true;
            setVisibility(8);
            setMeasuredDimension(0, 0);
            return;
        }
        int i3 = RangesKt___RangesKt.coerceAtLeast(measuredWidth, this.minimumTextWidth);
        this.minimumTextWidth = i3;
        setMeasuredDimension(i3, View.MeasureSpec.getSize(i2));
    }
}
