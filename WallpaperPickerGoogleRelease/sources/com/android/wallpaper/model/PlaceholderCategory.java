package com.android.wallpaper.model;

import android.app.Activity;
import android.content.Context;
import com.android.wallpaper.asset.Asset;
/* loaded from: classes.dex */
public class PlaceholderCategory extends Category {
    public PlaceholderCategory(String str, String str2, int i) {
        super(str, str2, i);
    }

    @Override // com.android.wallpaper.model.Category
    public Asset getThumbnail(Context context) {
        return null;
    }

    @Override // com.android.wallpaper.model.Category
    public void show(Activity activity, PickerIntentFactory pickerIntentFactory, int i) {
    }
}
