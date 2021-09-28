package androidx.slice.widget;

import java.util.List;
/* loaded from: classes.dex */
public class DisplayedListItems {
    public final List<SliceContent> mDisplayedItems;
    public final int mHiddenItemCount;

    public DisplayedListItems(List<SliceContent> list, int i) {
        this.mDisplayedItems = list;
        this.mHiddenItemCount = i;
    }
}
