package androidx.slice.widget;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.ArrayUtils;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class GridContent extends SliceContent {
    public boolean mAllImages;
    public boolean mIsLastIndex;
    public int mMaxCellLineCount;
    public SliceItem mPrimaryAction;
    public SliceItem mSeeMoreItem;
    public SliceItem mTitleItem;
    public final ArrayList<CellContent> mGridContent = new ArrayList<>();
    public int mLargestImageMode = 5;
    public IconCompat mFirstImage = null;
    public Point mFirstImageSize = null;

    /* loaded from: classes.dex */
    public static class CellContent {
        public final ArrayList<SliceItem> mCellItems;
        public SliceItem mContentDescr;
        public SliceItem mContentIntent;
        public IconCompat mImage;
        public int mImageCount;
        public int mImageMode = -1;
        public SliceItem mOverlayItem;
        public SliceItem mPicker;
        public int mTextCount;
        public SliceItem mTitleItem;
        public SliceItem mToggleItem;

        /* JADX WARNING: Removed duplicated region for block: B:52:0x00f3  */
        /* JADX WARNING: Removed duplicated region for block: B:64:0x0112  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public CellContent(androidx.slice.SliceItem r17) {
            /*
            // Method dump skipped, instructions count: 446
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.GridContent.CellContent.<init>(androidx.slice.SliceItem):void");
        }

        public final void fillCellItems(List<SliceItem> list) {
            for (int i = 0; i < list.size(); i++) {
                SliceItem sliceItem = list.get(i);
                String str = sliceItem.mFormat;
                if (this.mPicker == null && ("date_picker".equals(sliceItem.mSubType) || "time_picker".equals(sliceItem.mSubType))) {
                    this.mPicker = sliceItem;
                } else if ("content_description".equals(sliceItem.mSubType)) {
                    this.mContentDescr = sliceItem;
                } else if (this.mTextCount < 2 && ("text".equals(str) || "long".equals(str))) {
                    SliceItem sliceItem2 = this.mTitleItem;
                    if (sliceItem2 == null || (!ArrayUtils.contains(sliceItem2.mHints, "title") && ArrayUtils.contains(sliceItem.mHints, "title"))) {
                        this.mTitleItem = sliceItem;
                    }
                    if (!ArrayUtils.contains(sliceItem.mHints, "overlay")) {
                        this.mTextCount++;
                        this.mCellItems.add(sliceItem);
                    } else if (this.mOverlayItem == null) {
                        this.mOverlayItem = sliceItem;
                    }
                } else if (this.mImageCount < 1 && "image".equals(sliceItem.mFormat)) {
                    this.mImageMode = SliceActionImpl.parseImageMode(sliceItem);
                    this.mImageCount++;
                    this.mImage = (IconCompat) sliceItem.mObj;
                    this.mCellItems.add(sliceItem);
                }
            }
        }

        public boolean isValid() {
            return this.mPicker != null || (this.mCellItems.size() > 0 && this.mCellItems.size() <= 3);
        }
    }

    public GridContent(SliceItem sliceItem, int i) {
        super(sliceItem, i);
        List<SliceItem> items;
        SliceItem find = SliceQuery.find(sliceItem, (String) null, "see_more", (String) null);
        this.mSeeMoreItem = find;
        if (find != null && "slice".equals(find.mFormat) && (items = this.mSeeMoreItem.getSlice().getItems()) != null && items.size() > 0) {
            this.mSeeMoreItem = items.get(0);
        }
        this.mPrimaryAction = SliceQuery.find(sliceItem, "slice", new String[]{"shortcut", "title"}, new String[]{"actions"});
        this.mAllImages = true;
        if ("slice".equals(sliceItem.mFormat)) {
            List<SliceItem> items2 = sliceItem.getSlice().getItems();
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < items2.size(); i2++) {
                SliceItem sliceItem2 = items2.get(i2);
                boolean z = (SliceQuery.find(sliceItem2, (String) null, "see_more", (String) null) != null) || sliceItem2.hasAnyHints("shortcut", "see_more", "keywords", "ttl", "last_updated", "overlay");
                if ("content_description".equals(sliceItem2.mSubType)) {
                    this.mContentDescr = sliceItem2;
                } else if (!z) {
                    arrayList.add(sliceItem2);
                }
            }
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                SliceItem sliceItem3 = (SliceItem) arrayList.get(i3);
                if (!"content_description".equals(sliceItem3.mSubType)) {
                    processContent(new CellContent(sliceItem3));
                }
            }
        } else {
            processContent(new CellContent(sliceItem));
        }
        isValid();
    }

    public Point getFirstImageSize(Context context) {
        IconCompat iconCompat = this.mFirstImage;
        if (iconCompat == null) {
            return new Point(-1, -1);
        }
        if (this.mFirstImageSize == null) {
            iconCompat.checkResource(context);
            Drawable loadDrawable = iconCompat.toIcon(context).loadDrawable(context);
            this.mFirstImageSize = new Point(loadDrawable.getIntrinsicWidth(), loadDrawable.getIntrinsicHeight());
        }
        return this.mFirstImageSize;
    }

    @Override // androidx.slice.widget.SliceContent
    public int getHeight(SliceStyle sliceStyle, SliceViewPolicy sliceViewPolicy) {
        int i;
        Objects.requireNonNull(sliceStyle);
        int i2 = 0;
        int i3 = 1;
        boolean z = sliceViewPolicy.mMode == 1;
        if (!isValid()) {
            return 0;
        }
        int i4 = this.mLargestImageMode;
        if (!this.mAllImages) {
            boolean z2 = this.mMaxCellLineCount > 1;
            boolean z3 = this.mFirstImage != null;
            boolean z4 = i4 == 0 || i4 == 5;
            if (i4 == 4) {
                int i5 = getFirstImageSize(sliceStyle.mContext).y;
                if (z2) {
                    i3 = 2;
                }
                i = i5 + (i3 * sliceStyle.mGridRawImageTextHeight);
            } else if (!z2 || z) {
                if (z4) {
                    i = sliceStyle.mGridMinHeight;
                } else {
                    i = sliceStyle.mGridImageTextHeight;
                }
            } else if (z3) {
                i = sliceStyle.mGridMaxHeight;
            } else {
                i = sliceStyle.mGridMinHeight;
            }
        } else if (this.mGridContent.size() == 1) {
            if (z) {
                i = sliceStyle.mGridBigPicMinHeight;
            } else {
                i = sliceStyle.mGridBigPicMaxHeight;
            }
        } else if (i4 == 0) {
            i = sliceStyle.mGridMinHeight;
        } else if (i4 == 4) {
            i = getFirstImageSize(sliceStyle.mContext).y;
        } else {
            i = sliceStyle.mGridAllImagesHeight;
        }
        boolean z5 = this.mAllImages;
        int i6 = (!z5 || this.mRowIndex != 0) ? 0 : sliceStyle.mGridTopPadding;
        if (z5 && this.mIsLastIndex) {
            i2 = sliceStyle.mGridBottomPadding;
        }
        return i2 + i + i6;
    }

    @Override // androidx.slice.widget.SliceContent
    public boolean isValid() {
        return super.isValid() && this.mGridContent.size() > 0;
    }

    public final void processContent(CellContent cellContent) {
        int i;
        SliceItem sliceItem;
        if (cellContent.isValid()) {
            if (this.mTitleItem == null && (sliceItem = cellContent.mTitleItem) != null) {
                this.mTitleItem = sliceItem;
            }
            this.mGridContent.add(cellContent);
            boolean z = true;
            if (!(cellContent.mCellItems.size() == 1 && "image".equals(cellContent.mCellItems.get(0).mFormat))) {
                this.mAllImages = false;
            }
            this.mMaxCellLineCount = Math.max(this.mMaxCellLineCount, cellContent.mTextCount);
            if (this.mFirstImage == null) {
                IconCompat iconCompat = cellContent.mImage;
                if (iconCompat == null) {
                    z = false;
                }
                if (z) {
                    this.mFirstImage = iconCompat;
                }
            }
            int i2 = this.mLargestImageMode;
            if (i2 == 5) {
                i = cellContent.mImageMode;
            } else {
                i = Math.max(i2, cellContent.mImageMode);
            }
            this.mLargestImageMode = i;
        }
    }
}
