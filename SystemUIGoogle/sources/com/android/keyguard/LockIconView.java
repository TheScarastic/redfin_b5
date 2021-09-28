package com.android.keyguard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.settingslib.Utils;
import com.android.systemui.Dumpable;
import com.android.systemui.R$attr;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class LockIconView extends FrameLayout implements Dumpable {
    private ImageView mLockIcon;
    private int mLockIconColor;
    private int mRadius;
    private ImageView mUnlockBgView;
    private PointF mLockIconCenter = new PointF(0.0f, 0.0f);
    private final RectF mSensorRect = new RectF();

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public LockIconView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mLockIcon = (ImageView) findViewById(R$id.lock_icon);
        this.mUnlockBgView = (ImageView) findViewById(R$id.lock_icon_bg);
    }

    /* access modifiers changed from: package-private */
    public void updateColorAndBackgroundVisibility(boolean z) {
        if (z) {
            this.mLockIconColor = Utils.getColorAttrDefaultColor(getContext(), 16842806);
            this.mUnlockBgView.setBackground(getContext().getDrawable(R$drawable.fingerprint_bg));
            this.mUnlockBgView.setVisibility(0);
        } else {
            this.mLockIconColor = Utils.getColorAttrDefaultColor(getContext(), R$attr.wallpaperTextColorAccent);
            this.mUnlockBgView.setVisibility(8);
        }
        this.mLockIcon.setImageTintList(ColorStateList.valueOf(this.mLockIconColor));
    }

    /* access modifiers changed from: package-private */
    public void setImageDrawable(Drawable drawable) {
        this.mLockIcon.setImageDrawable(drawable);
    }

    /* access modifiers changed from: package-private */
    public void setCenterLocation(PointF pointF, int i) {
        this.mLockIconCenter = pointF;
        this.mRadius = i;
        RectF rectF = this.mSensorRect;
        float f = pointF.x;
        float f2 = pointF.y;
        rectF.set(f - ((float) i), f2 - ((float) i), f + ((float) i), f2 + ((float) i));
        setX(this.mSensorRect.left);
        setY(this.mSensorRect.top);
        RectF rectF2 = this.mSensorRect;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) (rectF2.right - rectF2.left), (int) (rectF2.bottom - rectF2.top));
        layoutParams.gravity = 17;
        setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: package-private */
    public float getLocationTop() {
        return this.mLockIconCenter.y - ((float) this.mRadius);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("Center in px (x, y)= (" + this.mLockIconCenter.x + ", " + this.mLockIconCenter.y + ")");
        StringBuilder sb = new StringBuilder();
        sb.append("Radius in pixels: ");
        sb.append(this.mRadius);
        printWriter.println(sb.toString());
    }
}
