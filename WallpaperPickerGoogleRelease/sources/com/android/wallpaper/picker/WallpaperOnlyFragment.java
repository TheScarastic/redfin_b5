package com.android.wallpaper.picker;

import com.android.wallpaper.model.CustomizationSectionController;
import java.util.List;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class WallpaperOnlyFragment extends CustomizationPickerFragment {
    public static final /* synthetic */ int $r8$clinit = 0;

    @Override // com.android.wallpaper.picker.CustomizationPickerFragment
    public List<CustomizationSectionController<?>> getAvailableSections(List<CustomizationSectionController<?>> list) {
        return super.getAvailableSections((List) list.stream().filter(WallpaperOnlyFragment$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.toList()));
    }
}
