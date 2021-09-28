package androidx.slice.widget;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.slice.CornerDrawable;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceQuery;
import androidx.slice.widget.GridContent;
import androidx.slice.widget.SliceView;
import com.android.systemui.shared.R;
import com.google.common.math.IntMath;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
/* loaded from: classes.dex */
public class GridRowView extends SliceChildView implements View.OnClickListener, View.OnTouchListener {
    public final View mForeground;
    public GridContent mGridContent;
    public final int mGutter;
    public final int mIconSize;
    public final int mLargeImageHeight;
    public final int[] mLoc;
    public boolean mMaxCellUpdateScheduled;
    public int mMaxCells;
    public final ViewTreeObserver.OnPreDrawListener mMaxCellsUpdater;
    public int mRowCount;
    public int mRowIndex;
    public final int mSmallImageMinWidth;
    public final int mSmallImageSize;
    public final int mTextPadding;
    public final LinearLayout mViewContainer;

    /* loaded from: classes.dex */
    public class DateSetListener implements DatePickerDialog.OnDateSetListener {
        public final SliceItem mActionItem;
        public final int mRowIndex;

        public DateSetListener(SliceItem sliceItem, int i) {
            this.mActionItem = sliceItem;
            this.mRowIndex = i;
        }

        @Override // android.app.DatePickerDialog.OnDateSetListener
        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            Calendar instance = Calendar.getInstance();
            instance.set(i, i2, i3);
            Date time = instance.getTime();
            SliceItem sliceItem = this.mActionItem;
            if (sliceItem != null) {
                try {
                    sliceItem.fireActionInternal(GridRowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    GridRowView gridRowView = GridRowView.this;
                    if (gridRowView.mObserver != null) {
                        GridRowView.this.mObserver.onSliceAction(new EventInfo(gridRowView.getMode(), 6, 7, this.mRowIndex), this.mActionItem);
                    }
                } catch (PendingIntent.CanceledException e) {
                    Log.e("GridRowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class TimeSetListener implements TimePickerDialog.OnTimeSetListener {
        public final SliceItem mActionItem;
        public final int mRowIndex;

        public TimeSetListener(SliceItem sliceItem, int i) {
            this.mActionItem = sliceItem;
            this.mRowIndex = i;
        }

        @Override // android.app.TimePickerDialog.OnTimeSetListener
        public void onTimeSet(TimePicker timePicker, int i, int i2) {
            Date time = Calendar.getInstance().getTime();
            time.setHours(i);
            time.setMinutes(i2);
            SliceItem sliceItem = this.mActionItem;
            if (sliceItem != null) {
                try {
                    sliceItem.fireActionInternal(GridRowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    GridRowView gridRowView = GridRowView.this;
                    if (gridRowView.mObserver != null) {
                        GridRowView.this.mObserver.onSliceAction(new EventInfo(gridRowView.getMode(), 7, 8, this.mRowIndex), this.mActionItem);
                    }
                } catch (PendingIntent.CanceledException e) {
                    Log.e("GridRowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    public GridRowView(Context context) {
        this(context, null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:135:0x02c4  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x02e6  */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x0315  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:150:0x0326  */
    /* JADX WARNING: Removed duplicated region for block: B:152:0x032b  */
    /* JADX WARNING: Removed duplicated region for block: B:175:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00ad  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void addCell(androidx.slice.widget.GridContent.CellContent r29, int r30, int r31) {
        /*
        // Method dump skipped, instructions count: 931
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.GridRowView.addCell(androidx.slice.widget.GridContent$CellContent, int, int):void");
    }

    public final boolean addPickerItem(final SliceItem sliceItem, ViewGroup viewGroup, int i, final boolean z) {
        SliceItem findSubtype = SliceQuery.findSubtype(sliceItem, "long", "millis");
        if (findSubtype == null) {
            return false;
        }
        long j = findSubtype.getLong();
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.abc_slice_title, (ViewGroup) null);
        SliceStyle sliceStyle = this.mSliceStyle;
        if (sliceStyle != null) {
            textView.setTextSize(0, (float) sliceStyle.mGridTitleSize);
            textView.setTextColor(this.mSliceStyle.mTitleColor);
        }
        final Date date = new Date(j);
        SliceItem find = SliceQuery.find(sliceItem, "text", "title", (String) null);
        if (find != null) {
            textView.setText((CharSequence) find.mObj);
        }
        final int i2 = this.mRowIndex;
        viewGroup.setOnClickListener(new View.OnClickListener() { // from class: androidx.slice.widget.GridRowView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(date);
                if (z) {
                    new DatePickerDialog(GridRowView.this.getContext(), R.style.DialogTheme, new DateSetListener(sliceItem, i2), instance.get(1), instance.get(2), instance.get(5)).show();
                } else {
                    new TimePickerDialog(GridRowView.this.getContext(), R.style.DialogTheme, new TimeSetListener(sliceItem, i2), instance.get(11), instance.get(12), false).show();
                }
            }
        });
        viewGroup.setClickable(true);
        viewGroup.setBackground(SliceViewUtil.getDrawable(getContext(), 16843868));
        viewGroup.addView(textView);
        textView.setPadding(0, i, 0, 0);
        return true;
    }

    public final int determinePadding(SliceItem sliceItem) {
        SliceStyle sliceStyle;
        if (sliceItem == null) {
            return 0;
        }
        if ("image".equals(sliceItem.mFormat)) {
            return this.mTextPadding;
        }
        if (("text".equals(sliceItem.mFormat) || "long".equals(sliceItem.mFormat)) && (sliceStyle = this.mSliceStyle) != null) {
            return sliceStyle.mVerticalGridTextPadding;
        }
        return 0;
    }

    public int getExtraBottomPadding() {
        SliceStyle sliceStyle;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.mAllImages) {
            return 0;
        }
        if ((this.mRowIndex == this.mRowCount - 1 || getMode() == 1) && (sliceStyle = this.mSliceStyle) != null) {
            return sliceStyle.mGridBottomPadding;
        }
        return 0;
    }

    public int getExtraTopPadding() {
        SliceStyle sliceStyle;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.mAllImages || this.mRowIndex != 0 || (sliceStyle = this.mSliceStyle) == null) {
            return 0;
        }
        return sliceStyle.mGridTopPadding;
    }

    public int getMaxCells() {
        int i;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isValid() || getWidth() == 0) {
            return -1;
        }
        if (this.mGridContent.mGridContent.size() <= 1) {
            return 1;
        }
        GridContent gridContent2 = this.mGridContent;
        int i2 = gridContent2.mLargestImageMode;
        if (i2 == 2) {
            i = this.mLargeImageHeight;
        } else if (i2 != 4) {
            i = this.mSmallImageMinWidth;
        } else {
            i = gridContent2.getFirstImageSize(getContext()).x;
        }
        return getWidth() / (i + this.mGutter);
    }

    public final void makeClickable(View view, boolean z) {
        Drawable drawable = null;
        view.setOnClickListener(z ? this : null);
        if (z) {
            drawable = SliceViewUtil.getDrawable(getContext(), 16843868);
        }
        view.setBackground(drawable);
        view.setClickable(z);
    }

    public final void makeEntireGridClickable(boolean z) {
        Drawable drawable = null;
        this.mViewContainer.setOnTouchListener(z ? this : null);
        this.mViewContainer.setOnClickListener(z ? this : null);
        View view = this.mForeground;
        if (z) {
            drawable = SliceViewUtil.getDrawable(getContext(), 16843534);
        }
        view.setBackground(drawable);
        this.mViewContainer.setClickable(z);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        SliceItem find;
        Pair pair = (Pair) view.getTag();
        SliceItem sliceItem = (SliceItem) pair.first;
        EventInfo eventInfo = (EventInfo) pair.second;
        if (sliceItem != null && (find = SliceQuery.find(sliceItem, "action", (String) null, (String) null)) != null) {
            try {
                find.fireActionInternal(null, null);
                SliceView.OnSliceActionListener onSliceActionListener = this.mObserver;
                if (onSliceActionListener != null) {
                    onSliceActionListener.onSliceAction(eventInfo, find);
                }
            } catch (PendingIntent.CanceledException e) {
                Log.e("GridRowView", "PendingIntent for slice cannot be sent", e);
            }
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int height = this.mGridContent.getHeight(this.mSliceStyle, this.mViewPolicy) + this.mInsetTop + this.mInsetBottom;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, IntMath.MAX_SIGNED_POWER_OF_TWO);
        this.mViewContainer.getLayoutParams().height = height;
        super.onMeasure(i, makeMeasureSpec);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.mForeground.getLocationOnScreen(this.mLoc);
        this.mForeground.getBackground().setHotspot((float) ((int) (motionEvent.getRawX() - ((float) this.mLoc[0]))), (float) ((int) (motionEvent.getRawY() - ((float) this.mLoc[1]))));
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mForeground.setPressed(true);
        } else if (actionMasked == 3 || actionMasked == 1 || actionMasked == 2) {
            this.mForeground.setPressed(false);
        }
        return false;
    }

    public void populateViews() {
        ViewGroup viewGroup;
        TextView textView;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isValid()) {
            resetView();
        } else if (!scheduleMaxCellsUpdate()) {
            if (this.mGridContent.getLayoutDir() != -1) {
                setLayoutDirection(this.mGridContent.getLayoutDir());
            }
            if (this.mGridContent.mPrimaryAction != null) {
                this.mViewContainer.setTag(new Pair(this.mGridContent.mPrimaryAction, new EventInfo(getMode(), 3, 1, this.mRowIndex)));
                makeEntireGridClickable(true);
            }
            SliceItem sliceItem = this.mGridContent.mContentDescr;
            CharSequence charSequence = sliceItem != null ? (CharSequence) sliceItem.mObj : null;
            if (charSequence != null) {
                this.mViewContainer.setContentDescription(charSequence);
            }
            GridContent gridContent2 = this.mGridContent;
            ArrayList<GridContent.CellContent> arrayList = gridContent2.mGridContent;
            int i = gridContent2.mLargestImageMode;
            if (i == 2 || i == 4) {
                this.mViewContainer.setGravity(48);
            } else {
                this.mViewContainer.setGravity(16);
            }
            int i2 = this.mMaxCells;
            boolean z = this.mGridContent.mSeeMoreItem != null;
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                if (this.mViewContainer.getChildCount() >= i2) {
                    int size = arrayList.size() - i2;
                    if (z) {
                        LinearLayout linearLayout = this.mViewContainer;
                        View childAt = linearLayout.getChildAt(linearLayout.getChildCount() - 1);
                        this.mViewContainer.removeView(childAt);
                        SliceItem sliceItem2 = this.mGridContent.mSeeMoreItem;
                        int childCount = this.mViewContainer.getChildCount();
                        int i4 = this.mMaxCells;
                        if (("slice".equals(sliceItem2.mFormat) || "action".equals(sliceItem2.mFormat)) && sliceItem2.getSlice().getItems().size() > 0) {
                            addCell(new GridContent.CellContent(sliceItem2), childCount, i4);
                            return;
                        }
                        LayoutInflater from = LayoutInflater.from(getContext());
                        if (this.mGridContent.mAllImages) {
                            viewGroup = (FrameLayout) from.inflate(R.layout.abc_slice_grid_see_more_overlay, (ViewGroup) this.mViewContainer, false);
                            viewGroup.addView(childAt, 0, new FrameLayout.LayoutParams(-1, -1));
                            textView = (TextView) viewGroup.findViewById(R.id.text_see_more_count);
                            viewGroup.findViewById(R.id.overlay_see_more).setBackground(new CornerDrawable(SliceViewUtil.getDrawable(getContext(), 16842800), this.mSliceStyle.mImageCornerRadius));
                        } else {
                            viewGroup = (LinearLayout) from.inflate(R.layout.abc_slice_grid_see_more, (ViewGroup) this.mViewContainer, false);
                            textView = (TextView) viewGroup.findViewById(R.id.text_see_more_count);
                            TextView textView2 = (TextView) viewGroup.findViewById(R.id.text_see_more);
                            SliceStyle sliceStyle = this.mSliceStyle;
                            if (!(sliceStyle == null || this.mRowStyle == null)) {
                                textView2.setTextSize(0, (float) sliceStyle.mGridTitleSize);
                                textView2.setTextColor(this.mRowStyle.getTitleColor());
                            }
                        }
                        this.mViewContainer.addView(viewGroup, new LinearLayout.LayoutParams(0, -1, 1.0f));
                        textView.setText(getResources().getString(R.string.abc_slice_more_content, Integer.valueOf(size)));
                        EventInfo eventInfo = new EventInfo(getMode(), 4, 1, this.mRowIndex);
                        eventInfo.setPosition(2, childCount, i4);
                        viewGroup.setTag(new Pair(sliceItem2, eventInfo));
                        makeClickable(viewGroup, true);
                        return;
                    }
                    return;
                }
                addCell(arrayList.get(i3), i3, Math.min(arrayList.size(), i2));
            }
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public void resetView() {
        if (this.mMaxCellUpdateScheduled) {
            this.mMaxCellUpdateScheduled = false;
            getViewTreeObserver().removeOnPreDrawListener(this.mMaxCellsUpdater);
        }
        this.mViewContainer.removeAllViews();
        setLayoutDirection(2);
        makeEntireGridClickable(false);
    }

    public boolean scheduleMaxCellsUpdate() {
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isValid()) {
            return true;
        }
        if (getWidth() == 0) {
            this.mMaxCellUpdateScheduled = true;
            getViewTreeObserver().addOnPreDrawListener(this.mMaxCellsUpdater);
            return true;
        }
        this.mMaxCells = getMaxCells();
        return false;
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setInsets(int i, int i2, int i3, int i4) {
        this.mInsetStart = i;
        this.mInsetTop = i2;
        this.mInsetEnd = i3;
        this.mInsetBottom = i4;
        this.mViewContainer.setPadding(i, getExtraTopPadding() + i2, i3, getExtraBottomPadding() + i4);
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setSliceItem(SliceContent sliceContent, boolean z, int i, int i2, SliceView.OnSliceActionListener onSliceActionListener) {
        resetView();
        this.mObserver = onSliceActionListener;
        this.mRowIndex = i;
        this.mRowCount = i2;
        this.mGridContent = (GridContent) sliceContent;
        if (!scheduleMaxCellsUpdate()) {
            populateViews();
        }
        this.mViewContainer.setPadding(this.mInsetStart, getExtraTopPadding() + this.mInsetTop, this.mInsetEnd, getExtraBottomPadding() + this.mInsetBottom);
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setTint(int i) {
        this.mTintColor = i;
        if (this.mGridContent != null) {
            resetView();
            populateViews();
        }
    }

    public GridRowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLoc = new int[2];
        this.mMaxCells = -1;
        this.mMaxCellsUpdater = new ViewTreeObserver.OnPreDrawListener() { // from class: androidx.slice.widget.GridRowView.2
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                GridRowView gridRowView = GridRowView.this;
                gridRowView.mMaxCells = gridRowView.getMaxCells();
                GridRowView.this.populateViews();
                GridRowView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                GridRowView.this.mMaxCellUpdateScheduled = false;
                return true;
            }
        };
        Resources resources = getContext().getResources();
        LinearLayout linearLayout = new LinearLayout(getContext());
        this.mViewContainer = linearLayout;
        linearLayout.setOrientation(0);
        addView(linearLayout, new FrameLayout.LayoutParams(-1, -1));
        linearLayout.setGravity(16);
        this.mIconSize = resources.getDimensionPixelSize(R.dimen.abc_slice_icon_size);
        this.mSmallImageSize = resources.getDimensionPixelSize(R.dimen.abc_slice_small_image_size);
        this.mLargeImageHeight = resources.getDimensionPixelSize(R.dimen.abc_slice_grid_image_only_height);
        this.mSmallImageMinWidth = resources.getDimensionPixelSize(R.dimen.abc_slice_grid_image_min_width);
        this.mGutter = resources.getDimensionPixelSize(R.dimen.abc_slice_grid_gutter);
        this.mTextPadding = resources.getDimensionPixelSize(R.dimen.abc_slice_grid_text_padding);
        View view = new View(getContext());
        this.mForeground = view;
        addView(view, new FrameLayout.LayoutParams(-1, -1));
    }
}
