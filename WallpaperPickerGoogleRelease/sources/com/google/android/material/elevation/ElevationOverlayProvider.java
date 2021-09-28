package com.google.android.material.elevation;

import android.content.Context;
import android.graphics.Color;
import androidx.core.graphics.ColorUtils;
import androidx.lifecycle.runtime.R$id;
import com.android.systemui.shared.R;
import com.google.android.material.resources.MaterialAttributes;
/* loaded from: classes.dex */
public class ElevationOverlayProvider {
    public final int colorSurface;
    public final float displayDensity;
    public final int elevationOverlayColor;
    public final boolean elevationOverlayEnabled;

    public ElevationOverlayProvider(Context context) {
        this.elevationOverlayEnabled = MaterialAttributes.resolveBoolean(context, R.attr.elevationOverlayEnabled, false);
        this.elevationOverlayColor = R$id.getColor(context, R.attr.elevationOverlayColor, 0);
        this.colorSurface = R$id.getColor(context, R.attr.colorSurface, 0);
        this.displayDensity = context.getResources().getDisplayMetrics().density;
    }

    public int compositeOverlayIfNeeded(int i, float f) {
        if (this.elevationOverlayEnabled) {
            if (ColorUtils.setAlphaComponent(i, 255) == this.colorSurface) {
                float f2 = this.displayDensity;
                float f3 = 0.0f;
                if (f2 > 0.0f && f > 0.0f) {
                    f3 = Math.min(((((float) Math.log1p((double) (f / f2))) * 4.5f) + 2.0f) / 100.0f, 1.0f);
                }
                return ColorUtils.setAlphaComponent(R$id.layer(ColorUtils.setAlphaComponent(i, 255), this.elevationOverlayColor, f3), Color.alpha(i));
            }
        }
        return i;
    }
}
