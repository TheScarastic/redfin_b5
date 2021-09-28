package com.android.systemui.navigationbar.gestural;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import com.android.systemui.R$dimen;
/* loaded from: classes.dex */
public class QuickswitchOrientedNavHandle extends NavigationHandle {
    private int mDeltaRotation;
    private final RectF mTmpBoundsRectF = new RectF();
    private final int mWidth;

    public QuickswitchOrientedNavHandle(Context context) {
        super(context);
        this.mWidth = context.getResources().getDimensionPixelSize(R$dimen.navigation_home_handle_width);
    }

    public void setDeltaRotation(int i) {
        this.mDeltaRotation = i;
    }

    @Override // com.android.systemui.navigationbar.gestural.NavigationHandle, android.view.View
    protected void onDraw(Canvas canvas) {
        RectF computeHomeHandleBounds = computeHomeHandleBounds();
        int i = this.mRadius;
        canvas.drawRoundRect(computeHomeHandleBounds, (float) i, (float) i, this.mPaint);
    }

    public RectF computeHomeHandleBounds() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5 = this.mRadius * 2;
        int i6 = getLocationOnScreen()[1];
        int i7 = this.mDeltaRotation;
        if (i7 == 1) {
            int i8 = this.mBottom;
            i = i8 + i5;
            int i9 = this.mWidth;
            int height = ((getHeight() / 2) - (i9 / 2)) - (i6 / 2);
            i4 = height + i9;
            i2 = height;
            i3 = i8;
        } else if (i7 != 3) {
            int i10 = this.mRadius * 2;
            i3 = (getWidth() / 2) - (this.mWidth / 2);
            i2 = (getHeight() - this.mBottom) - i10;
            i = (getWidth() / 2) + (this.mWidth / 2);
            i4 = i10 + i2;
        } else {
            i = getWidth() - this.mBottom;
            int i11 = i - i5;
            int i12 = this.mWidth;
            i2 = ((getHeight() / 2) - (i12 / 2)) - (i6 / 2);
            i4 = i2 + i12;
            i3 = i11;
        }
        this.mTmpBoundsRectF.set((float) i3, (float) i2, (float) i, (float) i4);
        return this.mTmpBoundsRectF;
    }
}
