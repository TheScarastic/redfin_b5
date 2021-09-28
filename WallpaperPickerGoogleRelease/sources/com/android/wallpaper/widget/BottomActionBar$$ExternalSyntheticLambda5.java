package com.android.wallpaper.widget;

import android.view.ViewGroup;
import com.android.wallpaper.model.CustomizationSectionController;
import com.android.wallpaper.picker.CustomizationPickerFragment;
import com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda1;
import com.android.wallpaper.widget.BottomActionBar;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class BottomActionBar$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ BottomActionBar$$ExternalSyntheticLambda5(CustomizationPickerFragment customizationPickerFragment, ViewGroup viewGroup) {
        this.f$0 = customizationPickerFragment;
        this.f$1 = viewGroup;
    }

    public /* synthetic */ BottomActionBar$$ExternalSyntheticLambda5(BottomActionBar bottomActionBar, Set set) {
        this.f$0 = bottomActionBar;
        this.f$1 = set;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                BottomActionBar bottomActionBar = (BottomActionBar) this.f$0;
                BottomActionBar.BottomAction bottomAction = (BottomActionBar.BottomAction) obj;
                int i = BottomActionBar.$r8$clinit;
                Objects.requireNonNull(bottomActionBar);
                if (((Set) this.f$1).contains(bottomAction)) {
                    bottomActionBar.showActions(bottomAction);
                    return;
                } else {
                    bottomActionBar.hideActions(bottomAction);
                    return;
                }
            default:
                CustomizationPickerFragment customizationPickerFragment = (CustomizationPickerFragment) this.f$0;
                customizationPickerFragment.mNestedScrollView.post(new PreviewUtils$$ExternalSyntheticLambda1(customizationPickerFragment, (ViewGroup) this.f$1, (CustomizationSectionController) obj));
                return;
        }
    }
}
