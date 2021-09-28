package com.android.wallpaper.picker;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import com.android.wallpaper.model.WallpaperSectionController;
import com.android.wallpaper.picker.CategorySelectorFragment;
import java.util.Objects;
/* loaded from: classes.dex */
public final /* synthetic */ class CategoryFragment$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ CategoryFragment$$ExternalSyntheticLambda0(WallpaperSectionController wallpaperSectionController) {
        this.f$0 = wallpaperSectionController;
    }

    public /* synthetic */ CategoryFragment$$ExternalSyntheticLambda0(CategoryFragment categoryFragment) {
        this.f$0 = categoryFragment;
    }

    public /* synthetic */ CategoryFragment$$ExternalSyntheticLambda0(CategorySelectorFragment.CategoryAdapter categoryAdapter) {
        this.f$0 = categoryAdapter;
    }

    public /* synthetic */ CategoryFragment$$ExternalSyntheticLambda0(LivePreviewFragment livePreviewFragment) {
        this.f$0 = livePreviewFragment;
    }

    public /* synthetic */ CategoryFragment$$ExternalSyntheticLambda0(StartRotationDialogFragment startRotationDialogFragment) {
        this.f$0 = startRotationDialogFragment;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        switch (this.$r8$classId) {
            case 0:
                CategoryFragment categoryFragment = (CategoryFragment) this.f$0;
                int i2 = CategoryFragment.$r8$clinit;
                Objects.requireNonNull(categoryFragment);
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", categoryFragment.getActivity().getPackageName(), null));
                categoryFragment.startActivityForResult(intent, 1);
                return;
            case 1:
                WallpaperSectionController wallpaperSectionController = (WallpaperSectionController) this.f$0;
                Objects.requireNonNull(wallpaperSectionController);
                Intent intent2 = new Intent();
                intent2.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent2.setData(Uri.fromParts("package", wallpaperSectionController.mAppContext.getPackageName(), null));
                wallpaperSectionController.mActivity.startActivityForResult(intent2, 1);
                return;
            case 2:
                CategorySelectorFragment.CategoryAdapter categoryAdapter = (CategorySelectorFragment.CategoryAdapter) this.f$0;
                Objects.requireNonNull(categoryAdapter);
                Intent intent3 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent3.setData(Uri.fromParts("package", CategorySelectorFragment.this.getActivity().getPackageName(), null));
                CategorySelectorFragment.this.startActivityForResult(intent3, 1);
                return;
            case 3:
                LivePreviewFragment livePreviewFragment = (LivePreviewFragment) this.f$0;
                if (livePreviewFragment.mDeleteIntent != null) {
                    livePreviewFragment.requireContext().startService(livePreviewFragment.mDeleteIntent);
                    livePreviewFragment.finishActivity(false);
                    return;
                }
                return;
            default:
                StartRotationDialogFragment startRotationDialogFragment = (StartRotationDialogFragment) this.f$0;
                int i3 = StartRotationDialogFragment.$r8$clinit;
                ((RotationStarter) startRotationDialogFragment.getTargetFragment()).startRotation(startRotationDialogFragment.mIsWifiOnlyChecked ? 1 : 0);
                return;
        }
    }
}
