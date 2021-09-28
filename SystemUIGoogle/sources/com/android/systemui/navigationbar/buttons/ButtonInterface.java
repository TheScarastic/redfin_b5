package com.android.systemui.navigationbar.buttons;

import android.graphics.drawable.Drawable;
/* loaded from: classes.dex */
public interface ButtonInterface {
    void abortCurrentGesture();

    void setDarkIntensity(float f);

    void setDelayTouchFeedback(boolean z);

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    void setImageDrawable(Drawable drawable);

    void setVertical(boolean z);
}
