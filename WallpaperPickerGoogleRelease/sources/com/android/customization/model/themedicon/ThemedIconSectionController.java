package com.android.customization.model.themedicon;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.customization.picker.themedicon.ThemedIconSectionView;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.CustomizationSectionController;
import com.android.wallpaper.model.WorkspaceViewModel;
import com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda0;
/* loaded from: classes.dex */
public class ThemedIconSectionController implements CustomizationSectionController<ThemedIconSectionView> {
    public boolean mSavedThemedIconEnabled;
    public final ThemedIconSwitchProvider mThemedIconOptionsProvider;
    public ThemedIconSectionView mThemedIconSectionView;
    public final WorkspaceViewModel mWorkspaceViewModel;

    public ThemedIconSectionController(ThemedIconSwitchProvider themedIconSwitchProvider, WorkspaceViewModel workspaceViewModel, Bundle bundle) {
        this.mSavedThemedIconEnabled = false;
        this.mThemedIconOptionsProvider = themedIconSwitchProvider;
        this.mWorkspaceViewModel = workspaceViewModel;
        if (bundle != null) {
            this.mSavedThemedIconEnabled = bundle.getBoolean("SAVED_THEMED_ICON_ENABLED", false);
        }
    }

    /* Return type fixed from 'com.android.wallpaper.picker.SectionView' to match base method */
    @Override // com.android.wallpaper.model.CustomizationSectionController
    public ThemedIconSectionView createView(Context context) {
        ThemedIconSectionView themedIconSectionView = (ThemedIconSectionView) LayoutInflater.from(context).inflate(R.layout.themed_icon_section_view, (ViewGroup) null);
        this.mThemedIconSectionView = themedIconSectionView;
        themedIconSectionView.mSectionViewListener = new ThemedIconSectionController$$ExternalSyntheticLambda0(this, 0);
        themedIconSectionView.mSwitchView.setChecked(this.mSavedThemedIconEnabled);
        ThemedIconSwitchProvider themedIconSwitchProvider = this.mThemedIconOptionsProvider;
        themedIconSwitchProvider.mExecutorService.submit(new PreviewUtils$$ExternalSyntheticLambda0(themedIconSwitchProvider, new ThemedIconSectionController$$ExternalSyntheticLambda0(this, 1)));
        return this.mThemedIconSectionView;
    }

    @Override // com.android.wallpaper.model.CustomizationSectionController
    public boolean isAvailable(Context context) {
        if (context != null) {
            if (this.mThemedIconOptionsProvider.mThemedIconUtils.mProviderInfo != null) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.wallpaper.model.CustomizationSectionController
    public void onSaveInstanceState(Bundle bundle) {
        ThemedIconSectionView themedIconSectionView = this.mThemedIconSectionView;
        if (themedIconSectionView != null) {
            bundle.putBoolean("SAVED_THEMED_ICON_ENABLED", themedIconSectionView.mSwitchView.isChecked());
        }
    }
}
