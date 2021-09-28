package com.android.systemui.qs;

import android.view.View;
/* loaded from: classes.dex */
public interface QSFooter {
    default void disable(int i, int i2, boolean z) {
    }

    void setExpandClickListener(View.OnClickListener onClickListener);

    void setExpanded(boolean z);

    void setExpansion(float f);

    void setKeyguardShowing(boolean z);

    void setListening(boolean z);

    void setVisibility(int i);
}
