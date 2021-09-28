package com.android.wallpaper.picker.individual;

import com.android.wallpaper.model.WallpaperInfo;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class IndividualPickerFragment$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ IndividualPickerFragment$$ExternalSyntheticLambda1 INSTANCE = new IndividualPickerFragment$$ExternalSyntheticLambda1();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        int i = IndividualPickerFragment.$r8$clinit;
        return ((WallpaperInfo) obj).getWallpaperId() != null;
    }
}
