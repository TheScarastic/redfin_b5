package com.android.wallpaper.picker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
/* loaded from: classes.dex */
public abstract class SectionView extends LinearLayout {
    public SectionViewListener mSectionViewListener;

    /* loaded from: classes.dex */
    public interface SectionViewListener {
        void onViewActivated(Context context, boolean z);
    }

    public SectionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
