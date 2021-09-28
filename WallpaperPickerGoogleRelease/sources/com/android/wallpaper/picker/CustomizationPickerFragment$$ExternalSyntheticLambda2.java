package com.android.wallpaper.picker;

import com.android.wallpaper.model.CustomizationSectionController;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class CustomizationPickerFragment$$ExternalSyntheticLambda2 implements Consumer {
    public static final /* synthetic */ CustomizationPickerFragment$$ExternalSyntheticLambda2 INSTANCE = new CustomizationPickerFragment$$ExternalSyntheticLambda2();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((CustomizationSectionController) obj).release();
    }
}
