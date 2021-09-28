package com.google.android.systemui.assist.uihints;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.systemui.R$color;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
/* loaded from: classes2.dex */
public class ZeroStateIconView extends FrameLayout {
    private final int COLOR_DARK_BACKGROUND;
    private final int COLOR_LIGHT_BACKGROUND;
    private ImageView mZeroStateIcon;

    public ZeroStateIconView(Context context) {
        this(context, null);
    }

    public ZeroStateIconView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ZeroStateIconView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ZeroStateIconView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.COLOR_DARK_BACKGROUND = getResources().getColor(R$color.transcription_icon_dark);
        this.COLOR_LIGHT_BACKGROUND = getResources().getColor(R$color.transcription_icon_light);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        this.mZeroStateIcon = (ImageView) findViewById(R$id.zerostate_icon_image);
    }

    public void setHasDarkBackground(boolean z) {
        this.mZeroStateIcon.setImageTintList(ColorStateList.valueOf(z ? this.COLOR_DARK_BACKGROUND : this.COLOR_LIGHT_BACKGROUND));
    }

    /* access modifiers changed from: package-private */
    public void onDensityChanged() {
        this.mZeroStateIcon.setImageDrawable(getContext().getDrawable(R$drawable.ic_explore));
    }
}
