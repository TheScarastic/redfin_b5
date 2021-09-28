package androidx.slice.widget;

import java.util.List;
/* loaded from: classes.dex */
class DisplayedListItems {
    private final List<SliceContent> mDisplayedItems;
    private final int mHiddenItemCount;

    /* access modifiers changed from: package-private */
    public DisplayedListItems(List<SliceContent> list, int i) {
        this.mDisplayedItems = list;
        this.mHiddenItemCount = i;
    }

    /* access modifiers changed from: package-private */
    public List<SliceContent> getDisplayedItems() {
        return this.mDisplayedItems;
    }

    /* access modifiers changed from: package-private */
    public int getHiddenItemCount() {
        return this.mHiddenItemCount;
    }
}
