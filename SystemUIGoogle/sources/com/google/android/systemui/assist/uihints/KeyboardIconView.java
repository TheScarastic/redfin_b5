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
public class KeyboardIconView extends FrameLayout {
    private final int COLOR_DARK_BACKGROUND;
    private final int COLOR_LIGHT_BACKGROUND;
    private ImageView mKeyboardIcon;

    public KeyboardIconView(Context context) {
        this(context, null);
    }

    public KeyboardIconView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyboardIconView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public KeyboardIconView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.COLOR_DARK_BACKGROUND = getResources().getColor(R$color.transcription_icon_dark);
        this.COLOR_LIGHT_BACKGROUND = getResources().getColor(R$color.transcription_icon_light);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        this.mKeyboardIcon = (ImageView) findViewById(R$id.keyboard_icon_image);
    }

    public void setHasDarkBackground(boolean z) {
        this.mKeyboardIcon.setImageTintList(ColorStateList.valueOf(z ? this.COLOR_DARK_BACKGROUND : this.COLOR_LIGHT_BACKGROUND));
    }

    /* access modifiers changed from: package-private */
    public void onDensityChanged() {
        this.mKeyboardIcon.setImageDrawable(getContext().getDrawable(R$drawable.ic_keyboard));
    }
}
