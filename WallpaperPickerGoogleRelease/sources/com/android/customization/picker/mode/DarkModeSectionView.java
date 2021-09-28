package com.android.customization.picker.mode;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.android.systemui.shared.R;
import com.android.wallpaper.picker.AppbarFragment$$ExternalSyntheticLambda0;
import com.android.wallpaper.picker.SectionView;
/* loaded from: classes.dex */
public final class DarkModeSectionView extends SectionView {
    public boolean mIsDarkModeActivated;

    public DarkModeSectionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context.getString(R.string.mode_title);
        this.mIsDarkModeActivated = (context.getResources().getConfiguration().uiMode & 32) != 0;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        Switch r0 = (Switch) findViewById(R.id.dark_mode_toggle);
        r0.setChecked(this.mIsDarkModeActivated);
        r0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(r0) { // from class: com.android.customization.picker.mode.DarkModeSectionView$$ExternalSyntheticLambda0
            public final /* synthetic */ Switch f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                this.f$1.setChecked(DarkModeSectionView.this.mIsDarkModeActivated);
            }
        });
        setOnClickListener(new AppbarFragment$$ExternalSyntheticLambda0(this));
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setEnabled(z);
        }
    }
}
