package com.android.systemui.qs;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import com.android.systemui.qs.tileimpl.SlashImageView;
/* loaded from: classes.dex */
public class AlphaControlledSignalTileView extends SignalTileView {
    public AlphaControlledSignalTileView(Context context) {
        super(context);
    }

    @Override // com.android.systemui.qs.SignalTileView
    protected SlashImageView createSlashImageView(Context context) {
        return new AlphaControlledSlashImageView(context);
    }

    /* loaded from: classes.dex */
    public static class AlphaControlledSlashImageView extends SlashImageView {
        public AlphaControlledSlashImageView(Context context) {
            super(context);
        }

        public void setFinalImageTintList(ColorStateList colorStateList) {
            super.setImageTintList(colorStateList);
            SlashDrawable slash = getSlash();
            if (slash != null) {
                ((AlphaControlledSlashDrawable) slash).setFinalTintList(colorStateList);
            }
        }

        @Override // com.android.systemui.qs.tileimpl.SlashImageView
        protected void ensureSlashDrawable() {
            if (getSlash() == null) {
                AlphaControlledSlashDrawable alphaControlledSlashDrawable = new AlphaControlledSlashDrawable(getDrawable());
                setSlash(alphaControlledSlashDrawable);
                alphaControlledSlashDrawable.setAnimationEnabled(getAnimationEnabled());
                setImageViewDrawable(alphaControlledSlashDrawable);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class AlphaControlledSlashDrawable extends SlashDrawable {
        /* access modifiers changed from: protected */
        @Override // com.android.systemui.qs.SlashDrawable
        public void setDrawableTintList(ColorStateList colorStateList) {
        }

        AlphaControlledSlashDrawable(Drawable drawable) {
            super(drawable);
        }

        public void setFinalTintList(ColorStateList colorStateList) {
            super.setDrawableTintList(colorStateList);
        }
    }
}
