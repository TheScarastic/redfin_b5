package com.android.customization.model.color;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import com.android.systemui.shared.R;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public class ColorBundle extends ColorOption {
    public final PreviewInfo mPreviewInfo;

    /* loaded from: classes.dex */
    public static class PreviewInfo {
        public final List<Drawable> icons;
        public final int secondaryColorDark;
        public final int secondaryColorLight;

        public PreviewInfo(int i, int i2, int i3, int i4, List list, Drawable drawable, int i5, AnonymousClass1 r8) {
            this.secondaryColorLight = i;
            this.secondaryColorDark = i2;
            this.icons = list;
        }
    }

    public ColorBundle(String str, Map<String, String> map, boolean z, int i, PreviewInfo previewInfo) {
        super(str, map, z, i);
        this.mPreviewInfo = previewInfo;
    }

    @Override // com.android.customization.model.CustomizationOption
    public void bindThumbnailTile(View view) {
        Resources resources = view.getContext().getResources();
        PreviewInfo previewInfo = this.mPreviewInfo;
        Objects.requireNonNull(previewInfo);
        int i = (resources.getConfiguration().uiMode & 48) == 32 ? previewInfo.secondaryColorDark : previewInfo.secondaryColorLight;
        GradientDrawable gradientDrawable = (GradientDrawable) view.getResources().getDrawable(R.drawable.color_chip_medium_filled, ((ImageView) view.findViewById(R.id.color_preview_icon)).getContext().getTheme());
        if (i != 0) {
            gradientDrawable.setTintList(ColorStateList.valueOf(i));
        } else {
            gradientDrawable.setTintList(ColorStateList.valueOf(resources.getColor(17170495)));
        }
        ((ImageView) view.findViewById(R.id.color_preview_icon)).setImageDrawable(gradientDrawable);
        Context context = view.getContext();
        if (this.mContentDescription == null) {
            String string = context.getString(R.string.default_theme_title);
            if (this.mIsDefault) {
                this.mContentDescription = string;
            } else {
                this.mContentDescription = this.mTitle;
            }
        }
        view.setContentDescription(this.mContentDescription);
    }

    @Override // com.android.customization.model.CustomizationOption
    public int getLayoutResId() {
        return R.layout.color_option;
    }

    @Override // com.android.customization.model.color.ColorOption
    public String getSource() {
        return "preset";
    }
}
