package com.android.customization.widget;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: classes.dex */
public class HorizontalSpacerItemDecoration extends RecyclerView.ItemDecoration {
    public final int mOffset;

    public HorizontalSpacerItemDecoration(int i) {
        this.mOffset = i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        int i = this.mOffset;
        if (childAdapterPosition == 0) {
            i *= 2;
        }
        int itemCount = recyclerView.getAdapter().getItemCount() - 1;
        int i2 = this.mOffset;
        if (childAdapterPosition == itemCount) {
            i2 *= 2;
        }
        rect.set(i, 0, i2, 0);
    }
}
