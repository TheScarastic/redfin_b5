package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.util.AttributeSet;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
public class SidefpsView extends FrameLayout {
    private final Context mContext;
    private int mOrientation;
    private final Paint mPointerText;
    private FingerprintSensorPropertiesInternal mSensorProps;
    private final RectF mSensorRect = new RectF();
    private final Paint mSensorRectPaint;

    public SidefpsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        super.setWillNotDraw(false);
        this.mContext = context;
        Paint paint = new Paint(0);
        this.mPointerText = paint;
        paint.setAntiAlias(true);
        paint.setColor(-1);
        paint.setTextSize(50.0f);
        Paint paint2 = new Paint(0);
        this.mSensorRectPaint = paint2;
        paint2.setAntiAlias(true);
        paint2.setColor(-16776961);
        paint2.setStyle(Paint.Style.FILL);
    }

    /* access modifiers changed from: package-private */
    public void setSensorProperties(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
        this.mSensorProps = fingerprintSensorPropertiesInternal;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        int i2;
        super.onDraw(canvas);
        canvas.drawRoundRect(this.mSensorRect, 15.0f, 15.0f, this.mSensorRectPaint);
        int i3 = this.mOrientation;
        if (i3 == 1 || i3 == 3) {
            i2 = this.mSensorProps.sensorRadius + 10;
            i = 40;
        } else {
            i2 = 15;
            i = this.mSensorProps.sensorRadius + 30;
        }
        canvas.drawText(">", (float) i2, (float) i, this.mPointerText);
    }

    @Override // android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        super.onLayout(z, i, i2, i3, i4);
        int rotation = this.mContext.getDisplay().getRotation();
        this.mOrientation = rotation;
        int i6 = 50;
        if (rotation == 1 || rotation == 3) {
            i6 = this.mSensorProps.sensorRadius * 2;
            i5 = 50;
        } else {
            i5 = this.mSensorProps.sensorRadius * 2;
        }
        this.mSensorRect.set(0.0f, 0.0f, (float) i6, (float) i5);
    }
}
