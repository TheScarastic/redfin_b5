package com.android.customization.model.themedicon;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.android.customization.model.mode.DarkModeSectionController$$ExternalSyntheticLambda1;
import com.android.customization.model.themedicon.ThemedIconSwitchProvider;
import com.android.wallpaper.picker.SectionView;
import java.util.Objects;
/* loaded from: classes.dex */
public final /* synthetic */ class ThemedIconSectionController$$ExternalSyntheticLambda0 implements SectionView.SectionViewListener, ThemedIconSwitchProvider.FetchThemedIconEnabledCallback {
    public final /* synthetic */ ThemedIconSectionController f$0;

    public /* synthetic */ ThemedIconSectionController$$ExternalSyntheticLambda0(ThemedIconSectionController themedIconSectionController, int i) {
        this.f$0 = themedIconSectionController;
    }

    @Override // com.android.wallpaper.picker.SectionView.SectionViewListener
    public void onViewActivated(Context context, boolean z) {
        ThemedIconSectionController themedIconSectionController = this.f$0;
        Objects.requireNonNull(themedIconSectionController);
        if (context != null) {
            ThemedIconSwitchProvider themedIconSwitchProvider = themedIconSectionController.mThemedIconOptionsProvider;
            themedIconSwitchProvider.mExecutorService.submit(new DarkModeSectionController$$ExternalSyntheticLambda1(themedIconSwitchProvider, z));
            ((MutableLiveData) themedIconSectionController.mWorkspaceViewModel.updateWorkspace$delegate.getValue()).setValue(Boolean.valueOf(z));
        }
    }
}
