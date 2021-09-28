package com.android.customization.model.mode;

import android.app.UiModeManager;
import android.content.ContentValues;
import com.android.customization.model.CustomizationManager;
import com.android.customization.model.color.ColorCustomizationManager;
import com.android.customization.model.themedicon.ThemedIconSectionController$$ExternalSyntheticLambda0;
import com.android.customization.model.themedicon.ThemedIconSwitchProvider;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
/* loaded from: classes.dex */
public final /* synthetic */ class DarkModeSectionController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId = 0;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ DarkModeSectionController$$ExternalSyntheticLambda1(UiModeManager uiModeManager, boolean z) {
        this.f$0 = uiModeManager;
        this.f$1 = z;
    }

    public /* synthetic */ DarkModeSectionController$$ExternalSyntheticLambda1(ThemedIconSwitchProvider.FetchThemedIconEnabledCallback fetchThemedIconEnabledCallback, boolean z) {
        this.f$0 = fetchThemedIconEnabledCallback;
        this.f$1 = z;
    }

    public /* synthetic */ DarkModeSectionController$$ExternalSyntheticLambda1(ThemedIconSwitchProvider themedIconSwitchProvider, boolean z) {
        this.f$0 = themedIconSwitchProvider;
        this.f$1 = z;
    }

    public /* synthetic */ DarkModeSectionController$$ExternalSyntheticLambda1(boolean z, CustomizationManager.Callback callback) {
        this.f$1 = z;
        this.f$0 = callback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                boolean z = this.f$1;
                ExecutorService executorService = DarkModeSectionController.sExecutorService;
                ((UiModeManager) this.f$0).setNightModeActivated(z);
                return;
            case 1:
                boolean z2 = this.f$1;
                CustomizationManager.Callback callback = (CustomizationManager.Callback) this.f$0;
                ExecutorService executorService2 = ColorCustomizationManager.sExecutorService;
                if (z2) {
                    callback.onSuccess();
                    return;
                } else {
                    callback.onError(null);
                    return;
                }
            case 2:
                ThemedIconSwitchProvider themedIconSwitchProvider = (ThemedIconSwitchProvider) this.f$0;
                boolean z3 = this.f$1;
                Objects.requireNonNull(themedIconSwitchProvider);
                ContentValues contentValues = new ContentValues();
                contentValues.put("boolean_value", Boolean.valueOf(z3));
                if (themedIconSwitchProvider.mContentResolver.update(themedIconSwitchProvider.mThemedIconUtils.getUriForPath("icon_themed"), contentValues, null, null) == 1) {
                    themedIconSwitchProvider.mCustomizationPreferences.setThemedIconEnabled(z3);
                    return;
                }
                return;
            default:
                ((ThemedIconSectionController$$ExternalSyntheticLambda0) ((ThemedIconSwitchProvider.FetchThemedIconEnabledCallback) this.f$0)).f$0.mThemedIconSectionView.mSwitchView.setChecked(this.f$1);
                return;
        }
    }
}
