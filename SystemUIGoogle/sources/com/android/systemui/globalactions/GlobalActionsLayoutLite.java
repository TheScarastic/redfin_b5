package com.android.systemui.globalactions;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.helper.widget.Flow;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.HardwareBgDrawable;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
/* loaded from: classes.dex */
public class GlobalActionsLayoutLite extends GlobalActionsLayout {
    private final int mMaxColumns = getResources().getInteger(R$integer.power_menu_lite_max_columns);
    private final int mMaxRows = getResources().getInteger(R$integer.power_menu_lite_max_rows);

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0(View view) {
    }

    @Override // com.android.systemui.globalactions.GlobalActionsLayout
    protected HardwareBgDrawable getBackgroundDrawable(int i) {
        return null;
    }

    @Override // com.android.systemui.globalactions.GlobalActionsLayout
    @VisibleForTesting
    protected boolean shouldReverseListItems() {
        return false;
    }

    public GlobalActionsLayoutLite(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnClickListener(GlobalActionsLayoutLite$$ExternalSyntheticLambda0.INSTANCE);
    }

    @Override // com.android.systemui.globalactions.GlobalActionsLayout, com.android.systemui.MultiListLayout
    public void onUpdateList() {
        super.onUpdateList();
        int i = getCurrentRotation() == 0 ? this.mMaxColumns : this.mMaxRows;
        int childCount = getListView().getChildCount() - 1;
        if (getCurrentRotation() != 0 && childCount > this.mMaxRows) {
            i--;
        }
        ((Flow) findViewById(R$id.list_flow)).setMaxElementsWrap(i);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.globalactions.GlobalActionsLayout
    public void addToListView(View view, boolean z) {
        super.addToListView(view, z);
        ((Flow) findViewById(R$id.list_flow)).addView(view);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.MultiListLayout
    public void removeAllListViews() {
        View findViewById = findViewById(R$id.list_flow);
        super.removeAllListViews();
        super.addToListView(findViewById, false);
    }

    @Override // android.widget.LinearLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        ViewGroup listView = getListView();
        boolean z2 = false;
        for (int i5 = 0; i5 < listView.getChildCount(); i5++) {
            View childAt = listView.getChildAt(i5);
            if (childAt instanceof GlobalActionsItem) {
                z2 = z2 || ((GlobalActionsItem) childAt).isTruncated();
            }
        }
        if (z2) {
            for (int i6 = 0; i6 < listView.getChildCount(); i6++) {
                View childAt2 = listView.getChildAt(i6);
                if (childAt2 instanceof GlobalActionsItem) {
                    ((GlobalActionsItem) childAt2).setMarquee(true);
                }
            }
        }
    }

    @VisibleForTesting
    protected float getGridItemSize() {
        return getContext().getResources().getDimension(R$dimen.global_actions_grid_item_height);
    }

    @VisibleForTesting
    protected float getAnimationDistance() {
        return getGridItemSize() / 2.0f;
    }

    @Override // com.android.systemui.MultiListLayout
    public float getAnimationOffsetX() {
        return getAnimationDistance();
    }
}
