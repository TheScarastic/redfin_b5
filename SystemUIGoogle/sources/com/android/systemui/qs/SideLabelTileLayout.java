package com.android.systemui.qs;

import android.content.Context;
import android.util.AttributeSet;
import com.android.systemui.R$integer;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SideLabelTileLayout.kt */
/* loaded from: classes.dex */
public class SideLabelTileLayout extends TileLayout {
    @Override // com.android.systemui.qs.TileLayout
    protected boolean useSidePadding() {
        return false;
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public SideLabelTileLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    @Override // com.android.systemui.qs.TileLayout, com.android.systemui.qs.QSPanel.QSTileLayout
    public boolean updateResources() {
        boolean updateResources = super.updateResources();
        this.mMaxAllowedRows = getContext().getResources().getInteger(R$integer.quick_settings_max_rows);
        return updateResources;
    }

    public final int getPhantomTopPosition(int i) {
        return getRowTop(i / this.mColumns);
    }

    @Override // com.android.systemui.qs.TileLayout
    public boolean updateMaxRows(int i, int i2) {
        int i3 = this.mRows;
        int i4 = this.mMaxAllowedRows;
        this.mRows = i4;
        int i5 = this.mColumns;
        if (i4 > ((i2 + i5) - 1) / i5) {
            this.mRows = ((i2 + i5) - 1) / i5;
        }
        if (i3 != this.mRows) {
            return true;
        }
        return false;
    }
}
