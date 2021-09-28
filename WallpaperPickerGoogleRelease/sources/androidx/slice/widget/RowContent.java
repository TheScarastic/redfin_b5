package androidx.slice.widget;

import android.text.TextUtils;
import android.util.Log;
import androidx.slice.ArrayUtils;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceAction;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class RowContent extends SliceContent {
    public boolean mIsHeader;
    public SliceItem mPrimaryAction;
    public SliceItem mRange;
    public SliceItem mSelection;
    public boolean mShowActionDivider;
    public boolean mShowBottomDivider;
    public boolean mShowTitleItems;
    public SliceItem mStartItem;
    public SliceItem mSubtitleItem;
    public SliceItem mSummaryItem;
    public SliceItem mTitleItem;
    public final ArrayList<SliceItem> mEndItems = new ArrayList<>();
    public final ArrayList<SliceAction> mToggleItems = new ArrayList<>();
    public int mLineCount = 0;

    public RowContent(SliceItem sliceItem, int i) {
        super(sliceItem, i);
        boolean z;
        boolean z2 = i == 0;
        if (ArrayUtils.contains(sliceItem.mHints, "end_of_section")) {
            this.mShowBottomDivider = true;
        }
        this.mIsHeader = z2;
        if (!isValidRow(sliceItem)) {
            Log.w("RowContent", "Provided SliceItem is invalid for RowContent");
            return;
        }
        ArrayList arrayList = (ArrayList) SliceQuery.findAll(sliceItem, null, new String[]{"title"}, new String[]{null});
        if (arrayList.size() > 0) {
            String str = ((SliceItem) arrayList.get(0)).mFormat;
            if (("action".equals(str) && SliceQuery.find((SliceItem) arrayList.get(0), "image") != null) || "slice".equals(str) || "long".equals(str) || "image".equals(str)) {
                this.mStartItem = (SliceItem) arrayList.get(0);
            }
        }
        String[] strArr = {"shortcut", "title"};
        ArrayList arrayList2 = (ArrayList) SliceQuery.findAll(sliceItem, "slice", strArr, null);
        arrayList2.addAll(SliceQuery.findAll(sliceItem, "action", strArr, null));
        if (arrayList2.isEmpty() && "action".equals(sliceItem.mFormat) && sliceItem.getSlice().getItems().size() == 1) {
            this.mPrimaryAction = sliceItem;
        } else if (this.mStartItem != null && arrayList2.size() > 1 && arrayList2.get(0) == this.mStartItem) {
            this.mPrimaryAction = (SliceItem) arrayList2.get(1);
        } else if (arrayList2.size() > 0) {
            this.mPrimaryAction = (SliceItem) arrayList2.get(0);
        }
        ArrayList<SliceItem> filterInvalidItems = filterInvalidItems(sliceItem);
        if (filterInvalidItems.size() != 1 || ((!"action".equals(filterInvalidItems.get(0).mFormat) && !"slice".equals(filterInvalidItems.get(0).mFormat)) || filterInvalidItems.get(0).hasAnyHints("shortcut", "title") || !isValidRow(filterInvalidItems.get(0)))) {
            z = false;
        } else {
            sliceItem = filterInvalidItems.get(0);
            filterInvalidItems = filterInvalidItems(sliceItem);
            z = true;
        }
        if ("range".equals(sliceItem.mSubType)) {
            if (SliceQuery.findSubtype(sliceItem, "action", "range") == null || z) {
                this.mRange = sliceItem;
            } else {
                filterInvalidItems.remove(this.mStartItem);
                if (filterInvalidItems.size() != 1) {
                    SliceItem findSubtype = SliceQuery.findSubtype(sliceItem, "action", "range");
                    this.mRange = findSubtype;
                    ArrayList<SliceItem> filterInvalidItems2 = filterInvalidItems(findSubtype);
                    filterInvalidItems2.remove(getInputRangeThumb());
                    filterInvalidItems.remove(this.mRange);
                    filterInvalidItems.addAll(filterInvalidItems2);
                } else if (isValidRow(filterInvalidItems.get(0))) {
                    sliceItem = filterInvalidItems.get(0);
                    filterInvalidItems = filterInvalidItems(sliceItem);
                    this.mRange = sliceItem;
                    filterInvalidItems.remove(getInputRangeThumb());
                }
            }
        }
        if ("selection".equals(sliceItem.mSubType)) {
            this.mSelection = sliceItem;
        }
        if (filterInvalidItems.size() > 0) {
            SliceItem sliceItem2 = this.mStartItem;
            if (sliceItem2 != null) {
                filterInvalidItems.remove(sliceItem2);
            }
            SliceItem sliceItem3 = this.mPrimaryAction;
            if (sliceItem3 != null) {
                filterInvalidItems.remove(sliceItem3);
            }
            ArrayList arrayList3 = new ArrayList();
            for (int i2 = 0; i2 < filterInvalidItems.size(); i2++) {
                SliceItem sliceItem4 = filterInvalidItems.get(i2);
                if ("text".equals(sliceItem4.mFormat)) {
                    SliceItem sliceItem5 = this.mTitleItem;
                    if ((sliceItem5 == null || !ArrayUtils.contains(sliceItem5.mHints, "title")) && ArrayUtils.contains(sliceItem4.mHints, "title") && !ArrayUtils.contains(sliceItem4.mHints, "summary")) {
                        this.mTitleItem = sliceItem4;
                    } else if (this.mSubtitleItem == null && !ArrayUtils.contains(sliceItem4.mHints, "summary")) {
                        this.mSubtitleItem = sliceItem4;
                    } else if (this.mSummaryItem == null && ArrayUtils.contains(sliceItem4.mHints, "summary")) {
                        this.mSummaryItem = sliceItem4;
                    }
                } else {
                    arrayList3.add(sliceItem4);
                }
            }
            if (hasText(this.mTitleItem)) {
                this.mLineCount++;
            }
            if (hasText(this.mSubtitleItem)) {
                this.mLineCount++;
            }
            SliceItem sliceItem6 = this.mStartItem;
            boolean z3 = sliceItem6 != null && "long".equals(sliceItem6.mFormat);
            for (int i3 = 0; i3 < arrayList3.size(); i3++) {
                SliceItem sliceItem7 = (SliceItem) arrayList3.get(i3);
                boolean z4 = SliceQuery.find(sliceItem7, "action") != null;
                if (!"long".equals(sliceItem7.mFormat)) {
                    if (z4) {
                        SliceActionImpl sliceActionImpl = new SliceActionImpl(sliceItem7);
                        if (sliceActionImpl.isToggle()) {
                            this.mToggleItems.add(sliceActionImpl);
                        }
                    }
                    this.mEndItems.add(sliceItem7);
                } else if (!z3) {
                    this.mEndItems.add(sliceItem7);
                    z3 = true;
                }
            }
        }
        isValid();
    }

    public static ArrayList<SliceItem> filterInvalidItems(SliceItem sliceItem) {
        ArrayList<SliceItem> arrayList = new ArrayList<>();
        for (SliceItem sliceItem2 : sliceItem.getSlice().getItems()) {
            if (isValidRowContent(sliceItem, sliceItem2)) {
                arrayList.add(sliceItem2);
            }
        }
        return arrayList;
    }

    public static boolean hasText(SliceItem sliceItem) {
        return sliceItem != null && (ArrayUtils.contains(sliceItem.mHints, "partial") || !TextUtils.isEmpty((CharSequence) sliceItem.mObj));
    }

    public static boolean isValidRow(SliceItem sliceItem) {
        if (sliceItem == null) {
            return false;
        }
        if ("slice".equals(sliceItem.mFormat) || "action".equals(sliceItem.mFormat)) {
            List<SliceItem> items = sliceItem.getSlice().getItems();
            if (ArrayUtils.contains(sliceItem.mHints, "see_more") && items.isEmpty()) {
                return true;
            }
            for (int i = 0; i < items.size(); i++) {
                if (isValidRowContent(sliceItem, items.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isValidRowContent(SliceItem sliceItem, SliceItem sliceItem2) {
        if (!sliceItem2.hasAnyHints("keywords", "ttl", "last_updated", "horizontal") && !"content_description".equals(sliceItem2.mSubType) && !"selection_option_key".equals(sliceItem2.mSubType) && !"selection_option_value".equals(sliceItem2.mSubType)) {
            String str = sliceItem2.mFormat;
            if ("image".equals(str) || "text".equals(str) || "long".equals(str) || "action".equals(str) || "input".equals(str) || "slice".equals(str) || ("int".equals(str) && "range".equals(sliceItem.mSubType))) {
                return true;
            }
        }
        return false;
    }

    @Override // androidx.slice.widget.SliceContent
    public int getHeight(SliceStyle sliceStyle, SliceViewPolicy sliceViewPolicy) {
        int i;
        int i2;
        Objects.requireNonNull(sliceStyle);
        int i3 = sliceViewPolicy.mMaxSmallHeight;
        if (i3 <= 0) {
            i3 = sliceStyle.mRowMaxHeight;
        }
        SliceItem sliceItem = this.mRange;
        if (sliceItem == null && this.mSelection == null && sliceViewPolicy.mMode != 2) {
            return i3;
        }
        if (sliceItem != null) {
            if (((!this.mIsHeader || this.mShowTitleItems) ? this.mStartItem : null) != null) {
                return sliceStyle.mRowInlineRangeHeight;
            }
            int i4 = this.mLineCount;
            if (i4 == 0) {
                i2 = 0;
            } else if (i4 > 1) {
                i2 = sliceStyle.mRowTextWithRangeHeight;
            } else {
                i2 = sliceStyle.mRowSingleTextWithRangeHeight;
            }
            i = sliceStyle.mRowRangeHeight;
        } else if (this.mSelection == null) {
            return (this.mLineCount > 1 || this.mIsHeader) ? i3 : sliceStyle.mRowMinHeight;
        } else {
            if (this.mLineCount > 1) {
                i2 = sliceStyle.mRowTextWithSelectionHeight;
            } else {
                i2 = sliceStyle.mRowSingleTextWithSelectionHeight;
            }
            i = sliceStyle.mRowSelectionHeight;
        }
        return i2 + i;
    }

    public SliceItem getInputRangeThumb() {
        SliceItem sliceItem = this.mRange;
        if (sliceItem == null) {
            return null;
        }
        List<SliceItem> items = sliceItem.getSlice().getItems();
        for (int i = 0; i < items.size(); i++) {
            if ("image".equals(items.get(i).mFormat)) {
                return items.get(i);
            }
        }
        return null;
    }

    public boolean isDefaultSeeMore() {
        return "action".equals(this.mSliceItem.mFormat) && ArrayUtils.contains(this.mSliceItem.getSlice().mHints, "see_more") && this.mSliceItem.getSlice().getItems().isEmpty();
    }

    @Override // androidx.slice.widget.SliceContent
    public boolean isValid() {
        return super.isValid() && !(this.mStartItem == null && this.mPrimaryAction == null && this.mTitleItem == null && this.mSubtitleItem == null && this.mEndItems.size() <= 0 && this.mRange == null && this.mSelection == null && !isDefaultSeeMore());
    }
}
