package com.android.settingslib.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
/* loaded from: classes.dex */
public class AdaptiveIcon extends LayerDrawable {
    private AdaptiveConstantState mAdaptiveConstantState;
    int mBackgroundColor;

    public AdaptiveIcon(Context context, Drawable drawable) {
        this(context, drawable, R$dimen.dashboard_tile_foreground_image_inset);
    }

    public AdaptiveIcon(Context context, Drawable drawable, int i) {
        super(new Drawable[]{new AdaptiveIconShapeDrawable(context.getResources()), drawable});
        this.mBackgroundColor = -1;
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(i);
        setLayerInset(1, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        this.mAdaptiveConstantState = new AdaptiveConstantState(context, drawable);
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
        getDrawable(0).setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
        Log.d("AdaptiveHomepageIcon", "Setting background color " + this.mBackgroundColor);
        this.mAdaptiveConstantState.mColor = i;
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.mAdaptiveConstantState;
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class AdaptiveConstantState extends Drawable.ConstantState {
        int mColor;
        Context mContext;
        Drawable mDrawable;

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return 0;
        }

        AdaptiveConstantState(Context context, Drawable drawable) {
            this.mContext = context;
            this.mDrawable = drawable;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            AdaptiveIcon adaptiveIcon = new AdaptiveIcon(this.mContext, this.mDrawable);
            adaptiveIcon.setBackgroundColor(this.mColor);
            return adaptiveIcon;
        }
    }
}
