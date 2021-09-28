package com.android.customization.picker.grid;

import android.content.Context;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.Log;
import com.android.customization.model.grid.GridOption;
import com.android.customization.model.grid.GridSectionController;
import com.android.wallpaper.model.CustomizationSectionController;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.picker.CustomizationPickerFragment;
import com.android.wallpaper.picker.individual.IndividualPickerFragment;
import com.android.wallpaper.picker.individual.IndividualPickerUnlockableFragment;
import java.util.Objects;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class GridFragment$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ int $r8$classId = 5;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ GridFragment$$ExternalSyntheticLambda1(Context context) {
        this.f$0 = context;
    }

    public /* synthetic */ GridFragment$$ExternalSyntheticLambda1(DeviceConfig.Properties properties) {
        this.f$0 = properties;
    }

    public /* synthetic */ GridFragment$$ExternalSyntheticLambda1(GridOption gridOption) {
        this.f$0 = gridOption;
    }

    public /* synthetic */ GridFragment$$ExternalSyntheticLambda1(GridSectionController gridSectionController) {
        this.f$0 = gridSectionController;
    }

    public /* synthetic */ GridFragment$$ExternalSyntheticLambda1(GridFragment gridFragment) {
        this.f$0 = gridFragment;
    }

    public /* synthetic */ GridFragment$$ExternalSyntheticLambda1(CustomizationPickerFragment customizationPickerFragment) {
        this.f$0 = customizationPickerFragment;
    }

    public /* synthetic */ GridFragment$$ExternalSyntheticLambda1(String str) {
        this.f$0 = str;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                GridOption gridOption = (GridOption) obj;
                int i = GridFragment.$r8$clinit;
                return gridOption.equals((GridOption) this.f$0);
            case 1:
                Objects.requireNonNull((GridSectionController) this.f$0);
                return ((GridOption) obj).mIsCurrent;
            case 2:
                int i2 = GridFragment.$r8$clinit;
                Objects.requireNonNull((GridFragment) this.f$0);
                return ((GridOption) obj).mIsCurrent;
            case 3:
                CustomizationSectionController customizationSectionController = (CustomizationSectionController) obj;
                int i3 = CustomizationPickerFragment.$r8$clinit;
                if (customizationSectionController.isAvailable(((CustomizationPickerFragment) this.f$0).getContext())) {
                    return true;
                }
                customizationSectionController.release();
                Log.d("CustomizationPickerFragment", "Section is not available: " + customizationSectionController);
                return false;
            case 4:
                int i4 = IndividualPickerFragment.$r8$clinit;
                return ((WallpaperInfo) obj).getWallpaperId().equals((String) this.f$0);
            case 5:
                WallpaperInfo wallpaperInfo = (WallpaperInfo) obj;
                int i5 = IndividualPickerUnlockableFragment.$r8$clinit;
                return !TextUtils.equals("unlock_additionals_header", wallpaperInfo.getCollectionId((Context) this.f$0));
            default:
                return ((DeviceConfig.Properties) this.f$0).getBoolean((String) obj, false);
        }
    }
}
