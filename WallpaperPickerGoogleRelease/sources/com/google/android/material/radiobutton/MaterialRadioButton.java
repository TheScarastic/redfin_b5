package com.google.android.material.radiobutton;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.lifecycle.runtime.R$id;
import com.android.systemui.shared.R;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
/* loaded from: classes.dex */
public class MaterialRadioButton extends AppCompatRadioButton {
    public static final int[][] ENABLED_CHECKED_STATES = {new int[]{16842910, 16842912}, new int[]{16842910, -16842912}, new int[]{-16842910, 16842912}, new int[]{-16842910, -16842912}};
    public ColorStateList materialThemeColorsTintList;
    public boolean useMaterialThemeColors;

    public MaterialRadioButton(Context context, AttributeSet attributeSet) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, R.attr.radioButtonStyle, 2131886817), attributeSet, R.attr.radioButtonStyle);
        Context context2 = getContext();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.MaterialRadioButton, R.attr.radioButtonStyle, 2131886817, new int[0]);
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
                int color = R$id.getColor(this, R.attr.colorControlActivated);
                int color2 = R$id.getColor(this, R.attr.colorOnSurface);
                int color3 = R$id.getColor(this, R.attr.colorSurface);
                int[][] iArr = ENABLED_CHECKED_STATES;
                int[] iArr2 = new int[iArr.length];
                iArr2[0] = R$id.layer(color3, color, 1.0f);
                iArr2[1] = R$id.layer(color3, color2, 0.54f);
                iArr2[2] = R$id.layer(color3, color2, 0.38f);
                iArr2[3] = R$id.layer(color3, color2, 0.38f);
                this.materialThemeColorsTintList = new ColorStateList(iArr, iArr2);
            }
            setButtonTintList(this.materialThemeColorsTintList);
        }
    }
}
