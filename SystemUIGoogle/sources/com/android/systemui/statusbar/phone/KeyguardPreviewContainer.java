package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
public class KeyguardPreviewContainer extends FrameLayout {
    private Drawable mBlackBarDrawable;

    public KeyguardPreviewContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        AnonymousClass1 r1 = new Drawable() { // from class: com.android.systemui.statusbar.phone.KeyguardPreviewContainer.1
            @Override // android.graphics.drawable.Drawable
            public int getOpacity() {
                return -1;
            }

            @Override // android.graphics.drawable.Drawable
            public void setAlpha(int i) {
            }

            @Override // android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }

            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                canvas.save();
                canvas.clipRect(0, KeyguardPreviewContainer.this.getHeight() - KeyguardPreviewContainer.this.getPaddingBottom(), KeyguardPreviewContainer.this.getWidth(), KeyguardPreviewContainer.this.getHeight());
                canvas.drawColor(-16777216);
                canvas.restore();
            }
        };
        this.mBlackBarDrawable = r1;
        setBackground(r1);
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        setPadding(0, 0, 0, windowInsets.getStableInsetBottom());
        return super.onApplyWindowInsets(windowInsets);
    }
}
