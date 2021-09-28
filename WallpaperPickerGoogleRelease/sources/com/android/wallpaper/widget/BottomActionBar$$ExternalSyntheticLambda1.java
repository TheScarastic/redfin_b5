package com.android.wallpaper.widget;

import android.content.Context;
import android.view.View;
import com.android.customization.model.CustomizationOption;
import com.android.customization.model.grid.GridSectionController;
import com.android.customization.picker.grid.GridFragment;
import com.android.customization.widget.OptionSelectorController;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.CustomizationSectionController;
import com.android.wallpaper.picker.AppbarFragment;
import com.android.wallpaper.picker.CustomizationPickerFragment;
import com.android.wallpaper.widget.BottomActionBar;
/* loaded from: classes.dex */
public final /* synthetic */ class BottomActionBar$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ BottomActionBar$$ExternalSyntheticLambda1(GridSectionController gridSectionController, Context context) {
        this.f$0 = gridSectionController;
        this.f$1 = context;
    }

    public /* synthetic */ BottomActionBar$$ExternalSyntheticLambda1(OptionSelectorController.AnonymousClass1 r2, CustomizationOption customizationOption) {
        this.f$0 = r2;
        this.f$1 = customizationOption;
    }

    public /* synthetic */ BottomActionBar$$ExternalSyntheticLambda1(BottomActionBar bottomActionBar, BottomActionBar.BottomAction bottomAction) {
        this.f$0 = bottomActionBar;
        this.f$1 = bottomAction;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                BottomActionBar bottomActionBar = (BottomActionBar) this.f$0;
                BottomActionBar.BottomAction bottomAction = (BottomActionBar.BottomAction) this.f$1;
                if (bottomActionBar.mBottomSheetBehavior.state == 4) {
                    bottomActionBar.mContentViewMap.forEach(new BottomActionBar$$ExternalSyntheticLambda3(bottomAction));
                }
                bottomActionBar.mBottomSheetView.setAccessibilityTraversalAfter(view.getId());
                return;
            case 1:
                CustomizationSectionController.CustomizationSectionNavigationController customizationSectionNavigationController = ((GridSectionController) this.f$0).mSectionNavigationController;
                String string = ((Context) this.f$1).getString(R.string.grid_title);
                GridFragment gridFragment = new GridFragment();
                gridFragment.setArguments(AppbarFragment.createArguments(string));
                ((CustomizationPickerFragment) customizationSectionNavigationController).navigateTo(gridFragment);
                return;
            default:
                OptionSelectorController.this.setSelectedOption((CustomizationOption) this.f$1);
                return;
        }
    }
}
