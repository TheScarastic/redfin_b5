package com.android.customization.model;

import android.view.View;
import com.android.customization.model.CustomizationOption;
/* loaded from: classes.dex */
public interface CustomizationOption<T extends CustomizationOption> {
    void bindThumbnailTile(View view);

    int getLayoutResId();

    String getTitle();

    boolean isActive(CustomizationManager<T> customizationManager);
}
