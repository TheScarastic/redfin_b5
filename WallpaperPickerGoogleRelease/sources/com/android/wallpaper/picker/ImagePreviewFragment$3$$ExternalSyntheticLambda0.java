package com.android.wallpaper.picker;

import android.app.WallpaperColors;
import android.os.Bundle;
import com.android.customization.model.themedicon.ThemedIconSectionController$$ExternalSyntheticLambda0;
import com.android.customization.model.themedicon.ThemedIconSwitchProvider;
import com.android.wallpaper.picker.ImagePreviewFragment;
import com.google.android.apps.wallpaper.module.ClearcutUserEventLogger;
import com.google.android.gms.clearcut.Counters;
/* loaded from: classes.dex */
public final /* synthetic */ class ImagePreviewFragment$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ImagePreviewFragment$3$$ExternalSyntheticLambda0(ThemedIconSwitchProvider themedIconSwitchProvider, ThemedIconSwitchProvider.FetchThemedIconEnabledCallback fetchThemedIconEnabledCallback) {
        this.f$0 = themedIconSwitchProvider;
        this.f$1 = fetchThemedIconEnabledCallback;
    }

    public /* synthetic */ ImagePreviewFragment$3$$ExternalSyntheticLambda0(CustomizationPickerFragment customizationPickerFragment, Bundle bundle) {
        this.f$0 = customizationPickerFragment;
        this.f$1 = bundle;
    }

    public /* synthetic */ ImagePreviewFragment$3$$ExternalSyntheticLambda0(ImagePreviewFragment.AnonymousClass3 r2, WallpaperColors wallpaperColors) {
        this.f$0 = r2;
        this.f$1 = wallpaperColors;
    }

    public /* synthetic */ ImagePreviewFragment$3$$ExternalSyntheticLambda0(ClearcutUserEventLogger clearcutUserEventLogger, String str) {
        this.f$0 = clearcutUserEventLogger;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ImagePreviewFragment.this.onWallpaperColorsChanged((WallpaperColors) this.f$1);
                return;
            case 1:
                ((ThemedIconSectionController$$ExternalSyntheticLambda0) ((ThemedIconSwitchProvider.FetchThemedIconEnabledCallback) this.f$1)).f$0.mThemedIconSectionView.mSwitchView.setChecked(((ThemedIconSwitchProvider) this.f$0).mCustomizationPreferences.getThemedIconEnabled());
                return;
            case 2:
                ((CustomizationPickerFragment) this.f$0).mNestedScrollView.setScrollY(((Bundle) this.f$1).getInt("SCROLL_POSITION_Y"));
                return;
            default:
                ClearcutUserEventLogger clearcutUserEventLogger = (ClearcutUserEventLogger) this.f$0;
                clearcutUserEventLogger.mCounters.getCounter((String) this.f$1).incrementBase(0, 1);
                Counters counters = clearcutUserEventLogger.mCounters;
                counters.logAllAsync(counters.zzf);
                return;
        }
    }
}
