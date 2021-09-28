package androidx.slice;

import android.content.Context;
import android.os.Bundle;
import androidx.slice.core.SliceAction;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;
import androidx.slice.widget.ListContent;
import androidx.slice.widget.RowContent;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class SliceMetadata {
    public Context mContext;
    public long mExpiry;
    public RowContent mHeaderContent;
    public long mLastUpdated;
    public ListContent mListContent;
    public SliceAction mPrimaryAction;
    public Slice mSlice;
    public List<SliceAction> mSliceActions;

    public SliceMetadata(Context context, Slice slice) {
        List<SliceAction> list;
        RowContent rowContent;
        this.mSlice = slice;
        this.mContext = context;
        SliceItem find = SliceQuery.find(slice, "long", "ttl", (String) null);
        if (find != null) {
            this.mExpiry = find.getLong();
        }
        SliceItem find2 = SliceQuery.find(slice, "long", "last_updated", (String) null);
        if (find2 != null) {
            this.mLastUpdated = find2.getLong();
        }
        SliceItem findSubtype = SliceQuery.findSubtype(slice, "bundle", "host_extras");
        if (findSubtype != null) {
            Object obj = findSubtype.mObj;
            if (obj instanceof Bundle) {
                Bundle bundle = (Bundle) obj;
                ListContent listContent = new ListContent(slice);
                this.mListContent = listContent;
                RowContent rowContent2 = listContent.mHeaderContent;
                this.mHeaderContent = rowContent2;
                ListContent.getRowType(rowContent2, true, listContent.mSliceActions);
                this.mPrimaryAction = this.mListContent.getShortcut(this.mContext);
                list = this.mListContent.mSliceActions;
                this.mSliceActions = list;
                if (list == null && (rowContent = this.mHeaderContent) != null && SliceQuery.hasHints(rowContent.mSliceItem, "list_item")) {
                    ArrayList<SliceItem> arrayList = this.mHeaderContent.mEndItems;
                    ArrayList arrayList2 = new ArrayList();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (SliceQuery.find(arrayList.get(i), "action") != null) {
                            arrayList2.add(new SliceActionImpl(arrayList.get(i)));
                        }
                    }
                    if (arrayList2.size() > 0) {
                        this.mSliceActions = arrayList2;
                        return;
                    }
                    return;
                }
                return;
            }
        }
        Bundle bundle2 = Bundle.EMPTY;
        ListContent listContent = new ListContent(slice);
        this.mListContent = listContent;
        RowContent rowContent2 = listContent.mHeaderContent;
        this.mHeaderContent = rowContent2;
        ListContent.getRowType(rowContent2, true, listContent.mSliceActions);
        this.mPrimaryAction = this.mListContent.getShortcut(this.mContext);
        list = this.mListContent.mSliceActions;
        this.mSliceActions = list;
        if (list == null) {
        }
    }

    public int getLoadingState() {
        boolean z = SliceQuery.find(this.mSlice, (String) null, "partial", (String) null) != null;
        if (!this.mListContent.isValid()) {
            return 0;
        }
        if (z) {
            return 1;
        }
        return 2;
    }

    public boolean isExpired() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.mExpiry;
        return (j == 0 || j == -1 || currentTimeMillis <= j) ? false : true;
    }
}
