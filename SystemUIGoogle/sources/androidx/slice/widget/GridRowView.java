package androidx.slice.widget;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.core.content.ContextCompat;
import androidx.slice.CornerDrawable;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceQuery;
import androidx.slice.view.R$dimen;
import androidx.slice.view.R$drawable;
import androidx.slice.view.R$id;
import androidx.slice.view.R$layout;
import androidx.slice.view.R$string;
import androidx.slice.view.R$style;
import androidx.slice.widget.GridContent;
import androidx.slice.widget.SliceView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
/* loaded from: classes.dex */
public class GridRowView extends SliceChildView implements View.OnClickListener, View.OnTouchListener {
    private static final int TEXT_LAYOUT = R$layout.abc_slice_secondary_text;
    protected final View mForeground;
    protected GridContent mGridContent;
    private final int mGutter;
    private int mHiddenItemCount;
    protected final int mIconSize;
    protected final int mLargeImageHeight;
    private final int[] mLoc;
    boolean mMaxCellUpdateScheduled;
    protected int mMaxCells;
    private final ViewTreeObserver.OnPreDrawListener mMaxCellsUpdater;
    protected int mRowCount;
    protected int mRowIndex;
    protected final int mSmallImageMinWidth;
    protected final int mSmallImageSize;
    private final int mTextPadding;
    protected final LinearLayout mViewContainer;

    public GridRowView(Context context) {
        this(context, null);
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
        this.mIconSize = resources.getDimensionPixelSize(R$dimen.abc_slice_icon_size);
        this.mSmallImageSize = resources.getDimensionPixelSize(R$dimen.abc_slice_small_image_size);
        this.mLargeImageHeight = resources.getDimensionPixelSize(R$dimen.abc_slice_grid_image_only_height);
        this.mSmallImageMinWidth = resources.getDimensionPixelSize(R$dimen.abc_slice_grid_image_min_width);
        this.mGutter = resources.getDimensionPixelSize(R$dimen.abc_slice_grid_gutter);
        this.mTextPadding = resources.getDimensionPixelSize(R$dimen.abc_slice_grid_text_padding);
        View view = new View(getContext());
        this.mForeground = view;
        addView(view, new FrameLayout.LayoutParams(-1, -1));
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setInsets(int i, int i2, int i3, int i4) {
        super.setInsets(i, i2, i3, i4);
        this.mViewContainer.setPadding(i, i2 + getExtraTopPadding(), i3, i4 + getExtraBottomPadding());
    }

    protected int getTitleTextLayout() {
        return R$layout.abc_slice_title;
    }

    protected int getExtraTopPadding() {
        SliceStyle sliceStyle;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isAllImages() || this.mRowIndex != 0 || (sliceStyle = this.mSliceStyle) == null) {
            return 0;
        }
        return sliceStyle.getGridTopPadding();
    }

    protected int getExtraBottomPadding() {
        SliceStyle sliceStyle;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isAllImages()) {
            return 0;
        }
        if ((this.mRowIndex == this.mRowCount - 1 || getMode() == 1) && (sliceStyle = this.mSliceStyle) != null) {
            return sliceStyle.getGridBottomPadding();
        }
        return 0;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int height = this.mGridContent.getHeight(this.mSliceStyle, this.mViewPolicy) + this.mInsetTop + this.mInsetBottom;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, 1073741824);
        this.mViewContainer.getLayoutParams().height = height;
        super.onMeasure(i, makeMeasureSpec);
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setTint(int i) {
        super.setTint(i);
        if (this.mGridContent != null) {
            resetView();
            populateViews();
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setSliceItem(SliceContent sliceContent, boolean z, int i, int i2, SliceView.OnSliceActionListener onSliceActionListener) {
        resetView();
        setSliceActionListener(onSliceActionListener);
        this.mRowIndex = i;
        this.mRowCount = i2;
        this.mGridContent = (GridContent) sliceContent;
        if (!scheduleMaxCellsUpdate()) {
            populateViews();
        }
        this.mViewContainer.setPadding(this.mInsetStart, this.mInsetTop + getExtraTopPadding(), this.mInsetEnd, this.mInsetBottom + getExtraBottomPadding());
    }

    protected boolean scheduleMaxCellsUpdate() {
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

    protected int getMaxCells() {
        int i;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isValid() || getWidth() == 0) {
            return -1;
        }
        if (this.mGridContent.getGridContent().size() <= 1) {
            return 1;
        }
        int largestImageMode = this.mGridContent.getLargestImageMode();
        if (largestImageMode == 2) {
            i = this.mLargeImageHeight;
        } else if (largestImageMode != 4) {
            i = this.mSmallImageMinWidth;
        } else {
            i = this.mGridContent.getFirstImageSize(getContext()).x;
        }
        return getWidth() / (i + this.mGutter);
    }

    protected void populateViews() {
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isValid()) {
            resetView();
        } else if (!scheduleMaxCellsUpdate()) {
            if (this.mGridContent.getLayoutDir() != -1) {
                setLayoutDirection(this.mGridContent.getLayoutDir());
            }
            boolean z = true;
            if (this.mGridContent.getContentIntent() != null) {
                this.mViewContainer.setTag(new Pair(this.mGridContent.getContentIntent(), new EventInfo(getMode(), 3, 1, this.mRowIndex)));
                makeEntireGridClickable(true);
            }
            CharSequence contentDescription = this.mGridContent.getContentDescription();
            if (contentDescription != null) {
                this.mViewContainer.setContentDescription(contentDescription);
            }
            ArrayList<GridContent.CellContent> gridContent2 = this.mGridContent.getGridContent();
            if (this.mGridContent.getLargestImageMode() == 2 || this.mGridContent.getLargestImageMode() == 4) {
                this.mViewContainer.setGravity(48);
            } else {
                this.mViewContainer.setGravity(16);
            }
            int i = this.mMaxCells;
            if (this.mGridContent.getSeeMoreItem() == null) {
                z = false;
            }
            this.mHiddenItemCount = 0;
            for (int i2 = 0; i2 < gridContent2.size(); i2++) {
                if (this.mViewContainer.getChildCount() >= i) {
                    int size = gridContent2.size() - i;
                    this.mHiddenItemCount = size;
                    if (z) {
                        addSeeMoreCount(size);
                        return;
                    }
                    return;
                }
                addCell(gridContent2.get(i2), i2, Math.min(gridContent2.size(), i));
            }
        }
    }

    private void addSeeMoreCount(int i) {
        ViewGroup viewGroup;
        TextView textView;
        LinearLayout linearLayout = this.mViewContainer;
        View childAt = linearLayout.getChildAt(linearLayout.getChildCount() - 1);
        this.mViewContainer.removeView(childAt);
        SliceItem seeMoreItem = this.mGridContent.getSeeMoreItem();
        int childCount = this.mViewContainer.getChildCount();
        int i2 = this.mMaxCells;
        if (("slice".equals(seeMoreItem.getFormat()) || "action".equals(seeMoreItem.getFormat())) && seeMoreItem.getSlice().getItems().size() > 0) {
            addCell(new GridContent.CellContent(seeMoreItem), childCount, i2);
            return;
        }
        LayoutInflater from = LayoutInflater.from(getContext());
        if (this.mGridContent.isAllImages()) {
            viewGroup = (FrameLayout) from.inflate(R$layout.abc_slice_grid_see_more_overlay, (ViewGroup) this.mViewContainer, false);
            viewGroup.addView(childAt, 0, new FrameLayout.LayoutParams(-1, -1));
            textView = (TextView) viewGroup.findViewById(R$id.text_see_more_count);
            viewGroup.findViewById(R$id.overlay_see_more).setBackground(new CornerDrawable(SliceViewUtil.getDrawable(getContext(), 16842800), this.mSliceStyle.getImageCornerRadius()));
        } else {
            viewGroup = (LinearLayout) from.inflate(R$layout.abc_slice_grid_see_more, (ViewGroup) this.mViewContainer, false);
            textView = (TextView) viewGroup.findViewById(R$id.text_see_more_count);
            TextView textView2 = (TextView) viewGroup.findViewById(R$id.text_see_more);
            SliceStyle sliceStyle = this.mSliceStyle;
            if (!(sliceStyle == null || this.mRowStyle == null)) {
                textView2.setTextSize(0, (float) sliceStyle.getGridTitleSize());
                textView2.setTextColor(this.mRowStyle.getTitleColor());
            }
        }
        this.mViewContainer.addView(viewGroup, new LinearLayout.LayoutParams(0, -1, 1.0f));
        textView.setText(getResources().getString(R$string.abc_slice_more_content, Integer.valueOf(i)));
        EventInfo eventInfo = new EventInfo(getMode(), 4, 1, this.mRowIndex);
        eventInfo.setPosition(2, childCount, i2);
        viewGroup.setTag(new Pair(seeMoreItem, eventInfo));
        makeClickable(viewGroup, true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00c8, code lost:
        if ("long".equals(r4) != false) goto L_0x00cd;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addCell(androidx.slice.widget.GridContent.CellContent r29, int r30, int r31) {
        /*
        // Method dump skipped, instructions count: 516
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.GridRowView.addCell(androidx.slice.widget.GridContent$CellContent, int, int):void");
    }

    private boolean addTextItem(SliceItem sliceItem, ViewGroup viewGroup, int i) {
        CharSequence charSequence;
        String format = sliceItem.getFormat();
        if (!"text".equals(format) && !"long".equals(format)) {
            return false;
        }
        boolean hasAnyHints = SliceQuery.hasAnyHints(sliceItem, "large", "title");
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(hasAnyHints ? getTitleTextLayout() : TEXT_LAYOUT, (ViewGroup) null);
        SliceStyle sliceStyle = this.mSliceStyle;
        if (!(sliceStyle == null || this.mRowStyle == null)) {
            textView.setTextSize(0, (float) (hasAnyHints ? sliceStyle.getGridTitleSize() : sliceStyle.getGridSubtitleSize()));
            textView.setTextColor(hasAnyHints ? this.mRowStyle.getTitleColor() : this.mRowStyle.getSubtitleColor());
        }
        if ("long".equals(format)) {
            charSequence = SliceViewUtil.getTimestampString(getContext(), sliceItem.getLong());
        } else {
            charSequence = sliceItem.getSanitizedText();
        }
        textView.setText(charSequence);
        viewGroup.addView(textView);
        textView.setPadding(0, i, 0, 0);
        return true;
    }

    protected boolean addImageItem(SliceItem sliceItem, SliceItem sliceItem2, int i, ViewGroup viewGroup, boolean z) {
        Drawable loadDrawable;
        ViewGroup.LayoutParams layoutParams;
        int i2;
        String format = sliceItem.getFormat();
        SliceStyle sliceStyle = this.mSliceStyle;
        boolean z2 = sliceStyle != null && sliceStyle.getApplyCornerRadiusToLargeImages();
        if (!"image".equals(format) || sliceItem.getIcon() == null || (loadDrawable = sliceItem.getIcon().loadDrawable(getContext())) == null) {
            return false;
        }
        ImageView imageView = new ImageView(getContext());
        if (z2) {
            imageView.setImageDrawable(new CornerDrawable(loadDrawable, this.mSliceStyle.getImageCornerRadius()));
        } else {
            imageView.setImageDrawable(loadDrawable);
        }
        if (sliceItem.hasHint("raw")) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            layoutParams = new LinearLayout.LayoutParams(this.mGridContent.getFirstImageSize(getContext()).x, this.mGridContent.getFirstImageSize(getContext()).y);
        } else if (sliceItem.hasHint("large")) {
            imageView.setScaleType(z2 ? ImageView.ScaleType.FIT_XY : ImageView.ScaleType.CENTER_CROP);
            if (z) {
                i2 = -1;
            } else {
                i2 = this.mLargeImageHeight;
            }
            layoutParams = new LinearLayout.LayoutParams(-1, i2);
        } else {
            boolean z3 = !sliceItem.hasHint("no_tint");
            int i3 = !z3 ? this.mSmallImageSize : this.mIconSize;
            imageView.setScaleType(z3 ? ImageView.ScaleType.CENTER_INSIDE : ImageView.ScaleType.CENTER_CROP);
            layoutParams = new LinearLayout.LayoutParams(i3, i3);
        }
        if (i != -1 && !sliceItem.hasHint("no_tint")) {
            imageView.setColorFilter(i);
        }
        if (sliceItem2 == null || this.mViewContainer.getChildCount() == this.mMaxCells - 1) {
            viewGroup.addView(imageView, layoutParams);
            return true;
        }
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(getContext()).inflate(R$layout.abc_slice_grid_text_overlay_image, viewGroup, false);
        frameLayout.addView(imageView, 0, new FrameLayout.LayoutParams(-2, -2));
        ((TextView) frameLayout.findViewById(R$id.text_overlay)).setText(sliceItem2.getText());
        frameLayout.findViewById(R$id.tint_overlay).setBackground(new CornerDrawable(ContextCompat.getDrawable(getContext(), R$drawable.abc_slice_gradient), this.mSliceStyle.getImageCornerRadius()));
        viewGroup.addView(frameLayout, layoutParams);
        return true;
    }

    private boolean addPickerItem(final SliceItem sliceItem, ViewGroup viewGroup, int i, final boolean z) {
        SliceItem findSubtype = SliceQuery.findSubtype(sliceItem, "long", "millis");
        if (findSubtype == null) {
            return false;
        }
        long j = findSubtype.getLong();
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(getTitleTextLayout(), (ViewGroup) null);
        SliceStyle sliceStyle = this.mSliceStyle;
        if (sliceStyle != null) {
            textView.setTextSize(0, (float) sliceStyle.getGridTitleSize());
            textView.setTextColor(this.mSliceStyle.getTitleColor());
        }
        final Date date = new Date(j);
        SliceItem find = SliceQuery.find(sliceItem, "text", "title", (String) null);
        if (find != null) {
            textView.setText(find.getText());
        }
        final int i2 = this.mRowIndex;
        viewGroup.setOnClickListener(new View.OnClickListener() { // from class: androidx.slice.widget.GridRowView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(date);
                if (z) {
                    new DatePickerDialog(GridRowView.this.getContext(), R$style.DialogTheme, new DateSetListener(sliceItem, i2), instance.get(1), instance.get(2), instance.get(5)).show();
                } else {
                    new TimePickerDialog(GridRowView.this.getContext(), R$style.DialogTheme, new TimeSetListener(sliceItem, i2), instance.get(11), instance.get(12), false).show();
                }
            }
        });
        viewGroup.setClickable(true);
        int i3 = 16843534;
        if (Build.VERSION.SDK_INT >= 21) {
            i3 = 16843868;
        }
        viewGroup.setBackground(SliceViewUtil.getDrawable(getContext(), i3));
        viewGroup.addView(textView);
        textView.setPadding(0, i, 0, 0);
        return true;
    }

    /* loaded from: classes.dex */
    private class DateSetListener implements DatePickerDialog.OnDateSetListener {
        private final SliceItem mActionItem;
        private final int mRowIndex;

        DateSetListener(SliceItem sliceItem, int i) {
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
                    sliceItem.fireAction(GridRowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
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
    private class TimeSetListener implements TimePickerDialog.OnTimeSetListener {
        private final SliceItem mActionItem;
        private final int mRowIndex;

        TimeSetListener(SliceItem sliceItem, int i) {
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
                    sliceItem.fireAction(GridRowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
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

    private int determinePadding(SliceItem sliceItem) {
        SliceStyle sliceStyle;
        if (sliceItem == null) {
            return 0;
        }
        if ("image".equals(sliceItem.getFormat())) {
            return this.mTextPadding;
        }
        if (("text".equals(sliceItem.getFormat()) || "long".equals(sliceItem.getFormat())) && (sliceStyle = this.mSliceStyle) != null) {
            return sliceStyle.getVerticalGridTextPadding();
        }
        return 0;
    }

    private void makeEntireGridClickable(boolean z) {
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

    private void makeClickable(View view, boolean z) {
        Drawable drawable = null;
        view.setOnClickListener(z ? this : null);
        int i = 16843534;
        if (Build.VERSION.SDK_INT >= 21) {
            i = 16843868;
        }
        if (z) {
            drawable = SliceViewUtil.getDrawable(getContext(), i);
        }
        view.setBackground(drawable);
        view.setClickable(z);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        SliceItem find;
        Pair pair = (Pair) view.getTag();
        SliceItem sliceItem = (SliceItem) pair.first;
        EventInfo eventInfo = (EventInfo) pair.second;
        if (sliceItem != null && (find = SliceQuery.find(sliceItem, "action", (String) null, (String) null)) != null) {
            try {
                find.fireAction(null, null);
                SliceView.OnSliceActionListener onSliceActionListener = this.mObserver;
                if (onSliceActionListener != null) {
                    onSliceActionListener.onSliceAction(eventInfo, find);
                }
            } catch (PendingIntent.CanceledException e) {
                Log.e("GridRowView", "PendingIntent for slice cannot be sent", e);
            }
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        onForegroundActivated(motionEvent);
        return false;
    }

    private void onForegroundActivated(MotionEvent motionEvent) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mForeground.getLocationOnScreen(this.mLoc);
            this.mForeground.getBackground().setHotspot((float) ((int) (motionEvent.getRawX() - ((float) this.mLoc[0]))), (float) ((int) (motionEvent.getRawY() - ((float) this.mLoc[1]))));
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mForeground.setPressed(true);
        } else if (actionMasked == 3 || actionMasked == 1 || actionMasked == 2) {
            this.mForeground.setPressed(false);
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
}
