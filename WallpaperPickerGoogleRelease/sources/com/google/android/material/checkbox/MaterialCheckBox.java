package com.google.android.material.checkbox;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.lifecycle.runtime.R$id;
import com.android.systemui.shared.R;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
/* loaded from: classes.dex */
public class MaterialCheckBox extends AppCompatCheckBox {
    public static final int[][] ENABLED_CHECKED_STATES = {new int[]{16842910, 16842912}, new int[]{16842910, -16842912}, new int[]{-16842910, 16842912}, new int[]{-16842910, -16842912}};
    public ColorStateList materialThemeColorsTintList;
    public boolean useMaterialThemeColors;

    public MaterialCheckBox(Context context, AttributeSet attributeSet) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, R.attr.checkboxStyle, 2131886816), attributeSet, R.attr.checkboxStyle);
        Context context2 = getContext();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.MaterialCheckBox, R.attr.checkboxStyle, 2131886816, new int[0]);
        if (obtainStyledAttributes.hasValue(0)) {
            setButtonTintList(MaterialResources.getColorStateList(context2, obtainStyledAttributes, 0));
        }
        this.useMaterialThemeColors = obtainStyledAttributes.getBoolean(1, false);
        obtainStyledAttributes.recycle();
    }

    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.useMaterialThemeColors && getButtonTintList() == null) {
            this.useMaterialThemeColors = true;
            if (this.materialThemeColorsTintList == null) {
                int[][] iArr = ENABLED_CHECKED_STATES;
                int[] iArr2 = new int[iArr.length];
                int color = R$id.getColor(this, R.attr.colorControlActivated);
                int color2 = R$id.getColor(this, R.attr.colorSurface);
                int color3 = R$id.getColor(this, R.attr.colorOnSurface);
                iArr2[0] = R$id.layer(color2, color, 1.0f);
                iArr2[1] = R$id.layer(color2, color3, 0.54f);
                iArr2[2] = R$id.layer(color2, color3, 0.38f);
                iArr2[3] = R$id.layer(color2, color3, 0.38f);
                this.materialThemeColorsTintList = new ColorStateList(iArr, iArr2);
            }
            setButtonTintList(this.materialThemeColorsTintList);
        }
    }
}
