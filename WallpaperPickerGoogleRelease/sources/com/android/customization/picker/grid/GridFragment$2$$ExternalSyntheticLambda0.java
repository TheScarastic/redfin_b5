package com.android.customization.picker.grid;

import android.os.Handler;
import com.android.customization.model.CustomizationOption;
import com.android.customization.model.color.ColorOption;
import com.android.customization.model.color.ColorSectionController;
import com.android.customization.picker.grid.GridFragment;
import com.android.customization.widget.OptionSelectorController;
import com.android.wallpaper.util.DiskBasedLogger$$ExternalSyntheticLambda1;
import java.util.Objects;
/* loaded from: classes.dex */
public final /* synthetic */ class GridFragment$2$$ExternalSyntheticLambda0 implements OptionSelectorController.OptionSelectedListener {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ GridFragment$2$$ExternalSyntheticLambda0(ColorSectionController colorSectionController) {
        this.f$0 = colorSectionController;
    }

    public /* synthetic */ GridFragment$2$$ExternalSyntheticLambda0(GridFragment.AnonymousClass2 r2) {
        this.f$0 = r2;
    }

    @Override // com.android.customization.widget.OptionSelectorController.OptionSelectedListener
    public final void onOptionSelected(CustomizationOption customizationOption) {
        switch (this.$r8$classId) {
            case 0:
                GridFragment.AnonymousClass2 r3 = (GridFragment.AnonymousClass2) this.f$0;
                GridFragment.access$800(GridFragment.this, customizationOption);
                GridFragment.this.mBottomActionBar.setVisibility(0);
                return;
            default:
                ColorSectionController colorSectionController = (ColorSectionController) this.f$0;
                Objects.requireNonNull(colorSectionController);
                ColorOption colorOption = (ColorOption) customizationOption;
                if (!colorSectionController.mSelectedColor.equals(colorOption)) {
                    colorSectionController.mSelectedColor = colorOption;
                    new Handler().postDelayed(new DiskBasedLogger$$ExternalSyntheticLambda1(colorSectionController), 100);
                    return;
                }
                return;
        }
    }
}
