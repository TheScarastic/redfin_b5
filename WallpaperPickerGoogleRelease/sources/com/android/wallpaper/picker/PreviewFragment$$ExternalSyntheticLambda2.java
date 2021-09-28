package com.android.wallpaper.picker;

import com.android.customization.model.color.ColorSectionController;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final /* synthetic */ class PreviewFragment$$ExternalSyntheticLambda2 implements Supplier {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PreviewFragment$$ExternalSyntheticLambda2(ColorSectionController colorSectionController) {
        this.f$0 = colorSectionController;
    }

    public /* synthetic */ PreviewFragment$$ExternalSyntheticLambda2(PreviewFragment previewFragment) {
        this.f$0 = previewFragment;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        switch (this.$r8$classId) {
            case 0:
                return Integer.valueOf(!((PreviewFragment) this.f$0).mViewAsHome ? 1 : 0);
            default:
                return Integer.valueOf("preset".equals(((ColorSectionController) this.f$0).mColorManager.getCurrentColorSource()) ? 1 : 0);
        }
    }
}
