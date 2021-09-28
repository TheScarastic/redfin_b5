package androidx.slice.widget;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.slice.ArrayUtils;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceAction;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;
import androidx.slice.widget.SliceActionView;
import com.android.systemui.shared.R;
import com.google.common.math.IntMath;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class RowView extends SliceChildView implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public final ProgressBar mActionSpinner;
    public boolean mAllowTwoLines;
    public final LinearLayout mContent;
    public Handler mHandler;
    public List<SliceAction> mHeaderActions;
    public boolean mIsHeader;
    public boolean mIsRangeSliding;
    public boolean mIsStarRating;
    public long mLastSentRangeUpdate;
    public int mMeasuredRangeHeight;
    public View mRangeBar;
    public SliceItem mRangeItem;
    public int mRangeMaxValue;
    public int mRangeMinValue;
    public boolean mRangeUpdaterRunning;
    public int mRangeValue;
    public final LinearLayout mRootView;
    public SliceActionImpl mRowAction;
    public RowContent mRowContent;
    public int mRowIndex;
    public View mSeeMoreView;
    public SliceItem mSelectionItem;
    public ArrayList<String> mSelectionOptionKeys;
    public ArrayList<CharSequence> mSelectionOptionValues;
    public Spinner mSelectionSpinner;
    public boolean mShowActionSpinner;
    public SliceItem mStartItem;
    public final ArrayMap<SliceActionImpl, SliceActionView> mToggles = new ArrayMap<>();
    public final ArrayMap<SliceActionImpl, SliceActionView> mActions = new ArrayMap<>();
    public Set<SliceItem> mLoadingActions = new HashSet();
    public Runnable mRangeUpdater = new Runnable() { // from class: androidx.slice.widget.RowView.2
        @Override // java.lang.Runnable
        public void run() {
            RowView.this.sendSliderValue();
            RowView.this.mRangeUpdaterRunning = false;
        }
    };
    public final SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() { // from class: androidx.slice.widget.RowView.3
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            RowView rowView = RowView.this;
            rowView.mRangeValue = i + rowView.mRangeMinValue;
            long currentTimeMillis = System.currentTimeMillis();
            RowView rowView2 = RowView.this;
            long j = rowView2.mLastSentRangeUpdate;
            if (j != 0 && currentTimeMillis - j > 200) {
                rowView2.mRangeUpdaterRunning = false;
                rowView2.mHandler.removeCallbacks(rowView2.mRangeUpdater);
                RowView.this.sendSliderValue();
            } else if (!rowView2.mRangeUpdaterRunning) {
                rowView2.mRangeUpdaterRunning = true;
                rowView2.mHandler.postDelayed(rowView2.mRangeUpdater, 200);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            RowView.this.mIsRangeSliding = true;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            RowView rowView = RowView.this;
            rowView.mIsRangeSliding = false;
            if (rowView.mRangeUpdaterRunning) {
                rowView.mRangeUpdaterRunning = false;
                rowView.mHandler.removeCallbacks(rowView.mRangeUpdater);
                RowView rowView2 = RowView.this;
                int progress = seekBar.getProgress();
                RowView rowView3 = RowView.this;
                rowView2.mRangeValue = progress + rowView3.mRangeMinValue;
                rowView3.sendSliderValue();
            }
        }
    };
    public final RatingBar.OnRatingBarChangeListener mRatingBarChangeListener = new RatingBar.OnRatingBarChangeListener() { // from class: androidx.slice.widget.RowView.4
        @Override // android.widget.RatingBar.OnRatingBarChangeListener
        public void onRatingChanged(RatingBar ratingBar, float f, boolean z) {
            RowView rowView = RowView.this;
            rowView.mRangeValue = Math.round(f + ((float) rowView.mRangeMinValue));
            long currentTimeMillis = System.currentTimeMillis();
            RowView rowView2 = RowView.this;
            long j = rowView2.mLastSentRangeUpdate;
            if (j != 0 && currentTimeMillis - j > 200) {
                rowView2.mRangeUpdaterRunning = false;
                rowView2.mHandler.removeCallbacks(rowView2.mRangeUpdater);
                RowView.this.sendSliderValue();
            } else if (!rowView2.mRangeUpdaterRunning) {
                rowView2.mRangeUpdaterRunning = true;
                rowView2.mHandler.postDelayed(rowView2.mRangeUpdater, 200);
            }
        }
    };
    public int mIconSize = getContext().getResources().getDimensionPixelSize(R.dimen.abc_slice_icon_size);
    public int mImageSize = getContext().getResources().getDimensionPixelSize(R.dimen.abc_slice_small_image_size);
    public final LinearLayout mStartContainer = (LinearLayout) findViewById(R.id.icon_frame);
    public final LinearLayout mSubContent = (LinearLayout) findViewById(R.id.subcontent);
    public final TextView mPrimaryText = (TextView) findViewById(16908310);
    public final TextView mSecondaryText = (TextView) findViewById(16908304);
    public final TextView mLastUpdatedText = (TextView) findViewById(R.id.last_updated);
    public final View mBottomDivider = findViewById(R.id.bottom_divider);
    public final View mActionDivider = findViewById(R.id.action_divider);
    public final LinearLayout mEndContainer = (LinearLayout) findViewById(16908312);

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
                    sliceItem.fireActionInternal(RowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    RowView rowView = RowView.this;
                    if (rowView.mObserver != null) {
                        RowView.this.mObserver.onSliceAction(new EventInfo(rowView.getMode(), 6, 7, this.mRowIndex), this.mActionItem);
                    }
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RowView", "PendingIntent for slice cannot be sent", e);
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
                    sliceItem.fireActionInternal(RowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    RowView rowView = RowView.this;
                    if (rowView.mObserver != null) {
                        RowView.this.mObserver.onSliceAction(new EventInfo(rowView.getMode(), 7, 8, this.mRowIndex), this.mActionItem);
                    }
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    public RowView(Context context) {
        super(context);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.abc_slice_small_template, (ViewGroup) this, false);
        this.mRootView = linearLayout;
        addView(linearLayout);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(16908290);
        this.mContent = linearLayout2;
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.action_sent_indicator);
        this.mActionSpinner = progressBar;
        SliceViewUtil.tintIndeterminateProgressBar(getContext(), progressBar);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        setImportantForAccessibility(2);
        linearLayout2.setImportantForAccessibility(2);
    }

    public final void addAction(SliceActionImpl sliceActionImpl, int i, ViewGroup viewGroup, boolean z) {
        SliceActionView sliceActionView = new SliceActionView(getContext(), this.mRowStyle);
        viewGroup.addView(sliceActionView);
        if (viewGroup.getVisibility() == 8) {
            viewGroup.setVisibility(0);
        }
        boolean isToggle = sliceActionImpl.isToggle();
        EventInfo eventInfo = new EventInfo(getMode(), !isToggle, isToggle != 0 ? 3 : 0, this.mRowIndex);
        if (z) {
            eventInfo.setPosition(0, 0, 1);
        }
        sliceActionView.setAction(sliceActionImpl, eventInfo, this.mObserver, i, this.mLoadingListener);
        if (this.mLoadingActions.contains(sliceActionImpl.mSliceItem)) {
            sliceActionView.setLoading(true);
        }
        if (isToggle != 0) {
            this.mToggles.put(sliceActionImpl, sliceActionView);
        } else {
            this.mActions.put(sliceActionImpl, sliceActionView);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for r6v0, resolved type: android.widget.ImageView */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00d6  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00e4  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00fe  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0109  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x010f  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0123  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0132  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean addItem(androidx.slice.SliceItem r10, int r11, boolean r12) {
        /*
        // Method dump skipped, instructions count: 365
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowView.addItem(androidx.slice.SliceItem, int, boolean):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00f7  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x014c  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x014e  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0156  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x017b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void addSubtitle(boolean r10) {
        /*
        // Method dump skipped, instructions count: 399
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowView.addSubtitle(boolean):void");
    }

    public final int getRowContentHeight() {
        int height = this.mRowContent.getHeight(this.mSliceStyle, this.mViewPolicy);
        if (this.mRangeBar != null && this.mStartItem == null) {
            height -= this.mSliceStyle.mRowRangeHeight;
        }
        return this.mSelectionSpinner != null ? height - this.mSliceStyle.mRowSelectionHeight : height;
    }

    public final void measureChildWithExactHeight(View view, int i, int i2) {
        measureChild(view, i, View.MeasureSpec.makeMeasureSpec(i2 + this.mInsetTop + this.mInsetBottom, IntMath.MAX_SIGNED_POWER_OF_TWO));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        SliceActionView sliceActionView;
        SliceActionView.SliceActionLoadingListener sliceActionLoadingListener;
        SliceActionImpl sliceActionImpl;
        SliceActionImpl sliceActionImpl2 = this.mRowAction;
        if (sliceActionImpl2 != null && sliceActionImpl2.mActionItem != null) {
            if (sliceActionImpl2.getSubtype() != null) {
                String subtype = this.mRowAction.getSubtype();
                Objects.requireNonNull(subtype);
                char c = 65535;
                switch (subtype.hashCode()) {
                    case -868304044:
                        if (subtype.equals("toggle")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 759128640:
                        if (subtype.equals("time_picker")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1250407999:
                        if (subtype.equals("date_picker")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        sliceActionView = this.mToggles.get(this.mRowAction);
                        break;
                    case 1:
                        onClickPicker(false);
                        return;
                    case 2:
                        onClickPicker(true);
                        return;
                    default:
                        sliceActionView = this.mActions.get(this.mRowAction);
                        break;
                }
            } else {
                sliceActionView = this.mActions.get(this.mRowAction);
            }
            if (sliceActionView != null && !(view instanceof SliceActionView)) {
                SliceActionImpl sliceActionImpl3 = sliceActionView.mSliceAction;
                if (sliceActionImpl3 != null) {
                    if (!sliceActionImpl3.isToggle()) {
                        sliceActionView.sendActionInternal();
                    } else if (sliceActionView.mActionView != null && (sliceActionImpl = sliceActionView.mSliceAction) != null && sliceActionImpl.isToggle()) {
                        ((Checkable) sliceActionView.mActionView).toggle();
                    }
                }
            } else if (this.mRowContent.mIsHeader) {
                performClick();
            } else {
                try {
                    this.mShowActionSpinner = this.mRowAction.mActionItem.fireActionInternal(getContext(), null);
                    if (this.mObserver != null) {
                        this.mObserver.onSliceAction(new EventInfo(getMode(), 3, 0, this.mRowIndex), this.mRowAction.mSliceItem);
                    }
                    if (this.mShowActionSpinner && (sliceActionLoadingListener = this.mLoadingListener) != null) {
                        ((SliceAdapter) sliceActionLoadingListener).onSliceActionLoading(this.mRowAction.mSliceItem, this.mRowIndex);
                        this.mLoadingActions.add(this.mRowAction.mSliceItem);
                    }
                    updateActionSpinner();
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    public final void onClickPicker(boolean z) {
        if (this.mRowAction != null) {
            Log.d("ASDF", "ASDF" + z + ":" + this.mRowAction.mSliceItem);
            SliceItem findSubtype = SliceQuery.findSubtype(this.mRowAction.mSliceItem, "long", "millis");
            if (findSubtype != null) {
                int i = this.mRowIndex;
                Calendar instance = Calendar.getInstance();
                instance.setTime(new Date(findSubtype.getLong()));
                if (z) {
                    new DatePickerDialog(getContext(), R.style.DialogTheme, new DateSetListener(this.mRowAction.mSliceItem, i), instance.get(1), instance.get(2), instance.get(5)).show();
                } else {
                    new TimePickerDialog(getContext(), R.style.DialogTheme, new TimeSetListener(this.mRowAction.mSliceItem, i), instance.get(11), instance.get(12), false).show();
                }
            }
        }
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        if (this.mSelectionItem != null && adapterView == this.mSelectionSpinner && i >= 0 && i < this.mSelectionOptionKeys.size()) {
            if (this.mObserver != null) {
                this.mObserver.onSliceAction(new EventInfo(getMode(), 5, 6, this.mRowIndex), this.mSelectionItem);
            }
            try {
                if (this.mSelectionItem.fireActionInternal(getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.SELECTION", this.mSelectionOptionKeys.get(i)))) {
                    this.mShowActionSpinner = true;
                    SliceActionView.SliceActionLoadingListener sliceActionLoadingListener = this.mLoadingListener;
                    if (sliceActionLoadingListener != null) {
                        ((SliceAdapter) sliceActionLoadingListener).onSliceActionLoading(this.mRowAction.mSliceItem, this.mRowIndex);
                        this.mLoadingActions.add(this.mRowAction.mSliceItem);
                    }
                    updateActionSpinner();
                }
            } catch (PendingIntent.CanceledException e) {
                Log.e("RowView", "PendingIntent for slice cannot be sent", e);
            }
        }
    }

    @Override // android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int paddingLeft = getPaddingLeft();
        LinearLayout linearLayout = this.mRootView;
        linearLayout.layout(paddingLeft, this.mInsetTop, linearLayout.getMeasuredWidth() + paddingLeft, getRowContentHeight() + this.mInsetTop);
        if (this.mRangeBar != null && this.mStartItem == null) {
            int rowContentHeight = getRowContentHeight() + ((this.mSliceStyle.mRowRangeHeight - this.mMeasuredRangeHeight) / 2) + this.mInsetTop;
            View view = this.mRangeBar;
            view.layout(paddingLeft, rowContentHeight, view.getMeasuredWidth() + paddingLeft, this.mMeasuredRangeHeight + rowContentHeight);
        } else if (this.mSelectionSpinner != null) {
            int rowContentHeight2 = getRowContentHeight() + this.mInsetTop;
            Spinner spinner = this.mSelectionSpinner;
            spinner.layout(paddingLeft, rowContentHeight2, spinner.getMeasuredWidth() + paddingLeft, this.mSelectionSpinner.getMeasuredHeight() + rowContentHeight2);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int i3;
        int rowContentHeight = getRowContentHeight();
        if (rowContentHeight != 0) {
            this.mRootView.setVisibility(0);
            measureChildWithExactHeight(this.mRootView, i, rowContentHeight);
            i3 = this.mRootView.getMeasuredWidth();
        } else {
            this.mRootView.setVisibility(8);
            i3 = 0;
        }
        View view = this.mRangeBar;
        if (view == null || this.mStartItem != null) {
            Spinner spinner = this.mSelectionSpinner;
            if (spinner != null) {
                measureChildWithExactHeight(spinner, i, this.mSliceStyle.mRowSelectionHeight);
                i3 = Math.max(i3, this.mSelectionSpinner.getMeasuredWidth());
            }
        } else {
            measureChildWithExactHeight(view, i, this.mSliceStyle.mRowRangeHeight);
            this.mMeasuredRangeHeight = this.mRangeBar.getMeasuredHeight();
            i3 = Math.max(i3, this.mRangeBar.getMeasuredWidth());
        }
        int max = Math.max(i3 + this.mInsetStart + this.mInsetEnd, getSuggestedMinimumWidth());
        RowContent rowContent = this.mRowContent;
        setMeasuredDimension(FrameLayout.resolveSizeAndState(max, i, 0), (rowContent != null ? rowContent.getHeight(this.mSliceStyle, this.mViewPolicy) : 0) + this.mInsetTop + this.mInsetBottom);
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    public final void populateViews(boolean z) {
        ProgressBar progressBar;
        Drawable drawable;
        IconCompat iconCompat;
        Drawable loadDrawable;
        boolean z2;
        int i;
        boolean z3 = z && this.mIsRangeSliding;
        if (!z3) {
            resetViewState();
        }
        if (this.mRowContent.getLayoutDir() != -1) {
            setLayoutDirection(this.mRowContent.getLayoutDir());
        }
        if (this.mRowContent.isDefaultSeeMore()) {
            final Button button = (Button) LayoutInflater.from(getContext()).inflate(R.layout.abc_slice_row_show_more, (ViewGroup) this, false);
            button.setOnClickListener(new View.OnClickListener() { // from class: androidx.slice.widget.RowView.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    try {
                        RowView rowView = RowView.this;
                        if (rowView.mObserver != null) {
                            int mode = rowView.getMode();
                            RowView rowView2 = RowView.this;
                            rowView2.mObserver.onSliceAction(new EventInfo(mode, 4, 0, rowView2.mRowIndex), rowView2.mRowContent.mSliceItem);
                        }
                        RowView rowView3 = RowView.this;
                        rowView3.mShowActionSpinner = rowView3.mRowContent.mSliceItem.fireActionInternal(rowView3.getContext(), null);
                        RowView rowView4 = RowView.this;
                        if (rowView4.mShowActionSpinner) {
                            SliceActionView.SliceActionLoadingListener sliceActionLoadingListener = rowView4.mLoadingListener;
                            if (sliceActionLoadingListener != null) {
                                ((SliceAdapter) sliceActionLoadingListener).onSliceActionLoading(rowView4.mRowContent.mSliceItem, rowView4.mRowIndex);
                            }
                            RowView rowView5 = RowView.this;
                            rowView5.mLoadingActions.add(rowView5.mRowContent.mSliceItem);
                            button.setVisibility(8);
                        }
                        RowView.this.updateActionSpinner();
                    } catch (PendingIntent.CanceledException e) {
                        Log.e("RowView", "PendingIntent for slice cannot be sent", e);
                    }
                }
            });
            int i2 = this.mTintColor;
            if (i2 != -1) {
                button.setTextColor(i2);
            }
            this.mSeeMoreView = button;
            this.mRootView.addView(button);
            if (this.mLoadingActions.contains(this.mRowContent.mSliceItem)) {
                this.mShowActionSpinner = true;
                button.setVisibility(8);
                updateActionSpinner();
                return;
            }
            return;
        }
        SliceItem sliceItem = this.mRowContent.mContentDescr;
        CharSequence charSequence = sliceItem != null ? (CharSequence) sliceItem.mObj : null;
        if (charSequence != null) {
            this.mContent.setContentDescription(charSequence);
        }
        RowContent rowContent = this.mRowContent;
        boolean z4 = rowContent.mIsHeader;
        SliceItem sliceItem2 = (!z4 || rowContent.mShowTitleItems) ? rowContent.mStartItem : null;
        this.mStartItem = sliceItem2;
        boolean z5 = sliceItem2 != null && (!z4 || rowContent.mShowTitleItems);
        if (z5) {
            z5 = addItem(sliceItem2, this.mTintColor, true);
        }
        this.mStartContainer.setVisibility(z5 ? 0 : 8);
        SliceItem sliceItem3 = this.mRowContent.mTitleItem;
        if (sliceItem3 != null) {
            this.mPrimaryText.setText(sliceItem3.getSanitizedText());
        }
        SliceStyle sliceStyle = this.mSliceStyle;
        if (sliceStyle != null) {
            TextView textView = this.mPrimaryText;
            if (this.mIsHeader) {
                i = sliceStyle.mHeaderTitleSize;
            } else {
                i = sliceStyle.mTitleSize;
            }
            textView.setTextSize(0, (float) i);
            this.mPrimaryText.setTextColor(this.mRowStyle.getTitleColor());
        }
        this.mPrimaryText.setVisibility(sliceItem3 != null ? 0 : 8);
        addSubtitle(sliceItem3 != null);
        this.mBottomDivider.setVisibility(this.mRowContent.mShowBottomDivider ? 0 : 8);
        SliceItem sliceItem4 = this.mRowContent.mPrimaryAction;
        if (!(sliceItem4 == null || sliceItem4 == this.mStartItem)) {
            SliceActionImpl sliceActionImpl = new SliceActionImpl(sliceItem4);
            this.mRowAction = sliceActionImpl;
            if (sliceActionImpl.getSubtype() != null) {
                String subtype = this.mRowAction.getSubtype();
                Objects.requireNonNull(subtype);
                switch (subtype.hashCode()) {
                    case -868304044:
                        if (subtype.equals("toggle")) {
                            z2 = false;
                            break;
                        }
                        z2 = true;
                        break;
                    case 759128640:
                        if (subtype.equals("time_picker")) {
                            z2 = true;
                            break;
                        }
                        z2 = true;
                        break;
                    case 1250407999:
                        if (subtype.equals("date_picker")) {
                            z2 = true;
                            break;
                        }
                        z2 = true;
                        break;
                    default:
                        z2 = true;
                        break;
                }
                switch (z2) {
                    case false:
                        addAction(this.mRowAction, this.mTintColor, this.mEndContainer, false);
                        setViewClickable(this.mRootView, true);
                        return;
                    case true:
                        setViewClickable(this.mRootView, true);
                        return;
                    case true:
                        setViewClickable(this.mRootView, true);
                        return;
                }
            }
        }
        SliceItem sliceItem5 = this.mRowContent.mRange;
        if (sliceItem5 != null) {
            if (this.mRowAction != null) {
                setViewClickable(this.mRootView, true);
            }
            this.mRangeItem = sliceItem5;
            SliceItem findSubtype = SliceQuery.findSubtype(sliceItem5, "int", "range_mode");
            if (findSubtype != null) {
                this.mIsStarRating = findSubtype.getInt() == 2;
            }
            if (!z3) {
                SliceItem findSubtype2 = SliceQuery.findSubtype(this.mRangeItem, "int", "min");
                int i3 = findSubtype2 != null ? findSubtype2.getInt() : 0;
                this.mRangeMinValue = i3;
                SliceItem findSubtype3 = SliceQuery.findSubtype(this.mRangeItem, "int", "max");
                int i4 = this.mIsStarRating ? 5 : 100;
                if (findSubtype3 != null) {
                    i4 = findSubtype3.getInt();
                }
                this.mRangeMaxValue = i4;
                SliceItem findSubtype4 = SliceQuery.findSubtype(this.mRangeItem, "int", "value");
                this.mRangeValue = findSubtype4 != null ? findSubtype4.getInt() - i3 : 0;
                if (this.mHandler == null) {
                    this.mHandler = new Handler();
                }
                if (this.mIsStarRating) {
                    RatingBar ratingBar = new RatingBar(getContext());
                    ((LayerDrawable) ratingBar.getProgressDrawable()).getDrawable(2).setColorFilter(this.mTintColor, PorterDuff.Mode.SRC_IN);
                    ratingBar.setStepSize(1.0f);
                    ratingBar.setNumStars(this.mRangeMaxValue);
                    ratingBar.setRating((float) this.mRangeValue);
                    ratingBar.setVisibility(0);
                    LinearLayout linearLayout = new LinearLayout(getContext());
                    linearLayout.setGravity(17);
                    linearLayout.setVisibility(0);
                    linearLayout.addView(ratingBar, new FrameLayout.LayoutParams(-2, -2));
                    addView(linearLayout, new FrameLayout.LayoutParams(-1, -2));
                    ratingBar.setOnRatingBarChangeListener(this.mRatingBarChangeListener);
                    this.mRangeBar = linearLayout;
                } else {
                    SliceItem findSubtype5 = SliceQuery.findSubtype(this.mRangeItem, "int", "range_mode");
                    boolean z6 = findSubtype5 != null && findSubtype5.getInt() == 1;
                    boolean equals = "action".equals(this.mRangeItem.mFormat);
                    boolean z7 = this.mStartItem == null;
                    if (!equals) {
                        if (z7) {
                            progressBar = new ProgressBar(getContext(), null, 16842872);
                        } else {
                            progressBar = (ProgressBar) LayoutInflater.from(getContext()).inflate(R.layout.abc_slice_progress_inline_view, (ViewGroup) this, false);
                            RowStyle rowStyle = this.mRowStyle;
                            if (rowStyle != null) {
                                int i5 = rowStyle.mProgressBarInlineWidth;
                                if (progressBar != null && i5 >= 0) {
                                    ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
                                    layoutParams.width = i5;
                                    progressBar.setLayoutParams(layoutParams);
                                }
                                RowStyle rowStyle2 = this.mRowStyle;
                                setViewSidePaddings(progressBar, rowStyle2.mProgressBarStartPadding, rowStyle2.mProgressBarEndPadding);
                            }
                        }
                        if (z6) {
                            progressBar.setIndeterminate(true);
                        }
                    } else if (z7) {
                        progressBar = new SeekBar(getContext());
                    } else {
                        progressBar = (SeekBar) LayoutInflater.from(getContext()).inflate(R.layout.abc_slice_seekbar_view, (ViewGroup) this, false);
                        RowStyle rowStyle3 = this.mRowStyle;
                        if (rowStyle3 != null) {
                            int i6 = rowStyle3.mSeekBarInlineWidth;
                            if (progressBar != null && i6 >= 0) {
                                ViewGroup.LayoutParams layoutParams2 = progressBar.getLayoutParams();
                                layoutParams2.width = i6;
                                progressBar.setLayoutParams(layoutParams2);
                            }
                        }
                    }
                    if (z6) {
                        drawable = progressBar.getIndeterminateDrawable();
                    } else {
                        drawable = progressBar.getProgressDrawable();
                    }
                    int i7 = this.mTintColor;
                    if (!(i7 == -1 || drawable == null)) {
                        drawable.setTint(i7);
                        if (z6) {
                            progressBar.setIndeterminateDrawable(drawable);
                        } else {
                            progressBar.setProgressDrawable(drawable);
                        }
                    }
                    progressBar.setMax(this.mRangeMaxValue - this.mRangeMinValue);
                    progressBar.setProgress(this.mRangeValue);
                    progressBar.setVisibility(0);
                    if (this.mStartItem == null) {
                        addView(progressBar, new FrameLayout.LayoutParams(-1, -2));
                    } else {
                        this.mSubContent.setVisibility(8);
                        this.mContent.addView(progressBar, 1);
                    }
                    this.mRangeBar = progressBar;
                    if (equals) {
                        SliceItem inputRangeThumb = this.mRowContent.getInputRangeThumb();
                        SeekBar seekBar = (SeekBar) this.mRangeBar;
                        if (!(inputRangeThumb == null || (iconCompat = (IconCompat) inputRangeThumb.mObj) == null || (loadDrawable = iconCompat.loadDrawable(getContext())) == null)) {
                            seekBar.setThumb(loadDrawable);
                        }
                        Drawable thumb = seekBar.getThumb();
                        int i8 = this.mTintColor;
                        if (!(i8 == -1 || thumb == null)) {
                            thumb.setTint(i8);
                            seekBar.setThumb(thumb);
                        }
                        seekBar.setOnSeekBarChangeListener(this.mSeekBarChangeListener);
                    }
                }
            }
            if (this.mStartItem == null) {
                return;
            }
        }
        SliceItem sliceItem6 = this.mRowContent.mSelection;
        if (sliceItem6 != null) {
            this.mSelectionItem = sliceItem6;
            if (this.mHandler == null) {
                this.mHandler = new Handler();
            }
            this.mSelectionOptionKeys = new ArrayList<>();
            this.mSelectionOptionValues = new ArrayList<>();
            List<SliceItem> items = sliceItem6.getSlice().getItems();
            for (int i9 = 0; i9 < items.size(); i9++) {
                SliceItem sliceItem7 = items.get(i9);
                if (ArrayUtils.contains(sliceItem7.mHints, "selection_option")) {
                    SliceItem findSubtype6 = SliceQuery.findSubtype(sliceItem7, "text", "selection_option_key");
                    SliceItem findSubtype7 = SliceQuery.findSubtype(sliceItem7, "text", "selection_option_value");
                    if (!(findSubtype6 == null || findSubtype7 == null)) {
                        this.mSelectionOptionKeys.add(((CharSequence) findSubtype6.mObj).toString());
                        this.mSelectionOptionValues.add(findSubtype7.getSanitizedText());
                    }
                }
            }
            this.mSelectionSpinner = (Spinner) LayoutInflater.from(getContext()).inflate(R.layout.abc_slice_row_selection, (ViewGroup) this, false);
            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), (int) R.layout.abc_slice_row_selection_text, this.mSelectionOptionValues);
            arrayAdapter.setDropDownViewResource(R.layout.abc_slice_row_selection_dropdown_text);
            this.mSelectionSpinner.setAdapter((SpinnerAdapter) arrayAdapter);
            addView(this.mSelectionSpinner);
            this.mSelectionSpinner.setOnItemSelectedListener(this);
            return;
        }
        updateEndItems();
        updateActionSpinner();
    }

    @Override // androidx.slice.widget.SliceChildView
    public void resetView() {
        this.mRowContent = null;
        this.mLoadingActions.clear();
        resetViewState();
    }

    public final void resetViewState() {
        this.mRootView.setVisibility(0);
        setLayoutDirection(2);
        setViewClickable(this.mRootView, false);
        setViewClickable(this.mContent, false);
        this.mStartContainer.removeAllViews();
        this.mEndContainer.removeAllViews();
        this.mEndContainer.setVisibility(8);
        this.mPrimaryText.setText((CharSequence) null);
        this.mSecondaryText.setText((CharSequence) null);
        this.mLastUpdatedText.setText((CharSequence) null);
        this.mLastUpdatedText.setVisibility(8);
        this.mToggles.clear();
        this.mActions.clear();
        this.mRowAction = null;
        this.mBottomDivider.setVisibility(8);
        this.mActionDivider.setVisibility(8);
        View view = this.mSeeMoreView;
        if (view != null) {
            this.mRootView.removeView(view);
            this.mSeeMoreView = null;
        }
        this.mIsRangeSliding = false;
        this.mRangeItem = null;
        this.mRangeMinValue = 0;
        this.mRangeMaxValue = 0;
        this.mRangeValue = 0;
        this.mLastSentRangeUpdate = 0;
        this.mHandler = null;
        View view2 = this.mRangeBar;
        if (view2 != null) {
            if (this.mStartItem == null) {
                removeView(view2);
            } else {
                this.mContent.removeView(view2);
            }
            this.mRangeBar = null;
        }
        this.mSubContent.setVisibility(0);
        this.mStartItem = null;
        this.mActionSpinner.setVisibility(8);
        Spinner spinner = this.mSelectionSpinner;
        if (spinner != null) {
            removeView(spinner);
            this.mSelectionSpinner = null;
        }
        this.mSelectionItem = null;
    }

    public void sendSliderValue() {
        if (this.mRangeItem != null) {
            try {
                this.mLastSentRangeUpdate = System.currentTimeMillis();
                this.mRangeItem.fireActionInternal(getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", this.mRangeValue));
                if (this.mObserver != null) {
                    EventInfo eventInfo = new EventInfo(getMode(), 2, 4, this.mRowIndex);
                    eventInfo.state = this.mRangeValue;
                    this.mObserver.onSliceAction(eventInfo, this.mRangeItem);
                }
            } catch (PendingIntent.CanceledException e) {
                Log.e("RowView", "PendingIntent for slice cannot be sent", e);
            }
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setAllowTwoLines(boolean z) {
        this.mAllowTwoLines = z;
        if (this.mRowContent != null) {
            populateViews(true);
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setInsets(int i, int i2, int i3, int i4) {
        this.mInsetStart = i;
        this.mInsetTop = i2;
        this.mInsetEnd = i3;
        this.mInsetBottom = i4;
        setPadding(i, i2, i3, i4);
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setLastUpdated(long j) {
        this.mLastUpdated = j;
        RowContent rowContent = this.mRowContent;
        if (rowContent != null) {
            SliceItem sliceItem = rowContent.mTitleItem;
            addSubtitle(sliceItem != null && TextUtils.isEmpty(sliceItem.getSanitizedText()));
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setLoadingActions(Set<SliceItem> set) {
        if (set == null) {
            this.mLoadingActions.clear();
            this.mShowActionSpinner = false;
        } else {
            this.mLoadingActions = set;
        }
        updateEndItems();
        updateActionSpinner();
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setShowLastUpdated(boolean z) {
        this.mShowLastUpdated = z;
        if (this.mRowContent != null) {
            populateViews(true);
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setSliceActions(List<SliceAction> list) {
        this.mHeaderActions = list;
        if (this.mRowContent != null) {
            updateEndItems();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0044, code lost:
        if (r2 != false) goto L_0x0048;
     */
    @Override // androidx.slice.widget.SliceChildView
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setSliceItem(androidx.slice.widget.SliceContent r4, boolean r5, int r6, int r7, androidx.slice.widget.SliceView.OnSliceActionListener r8) {
        /*
            r3 = this;
            r3.mObserver = r8
            androidx.slice.widget.RowContent r7 = r3.mRowContent
            r8 = 0
            if (r7 == 0) goto L_0x0047
            boolean r7 = r7.isValid()
            if (r7 == 0) goto L_0x0047
            androidx.slice.widget.RowContent r7 = r3.mRowContent
            if (r7 == 0) goto L_0x0019
            androidx.slice.SliceStructure r0 = new androidx.slice.SliceStructure
            androidx.slice.SliceItem r7 = r7.mSliceItem
            r0.<init>(r7)
            goto L_0x001a
        L_0x0019:
            r0 = 0
        L_0x001a:
            androidx.slice.SliceStructure r7 = new androidx.slice.SliceStructure
            androidx.slice.SliceItem r1 = r4.mSliceItem
            androidx.slice.Slice r1 = r1.getSlice()
            r7.<init>(r1)
            r1 = 1
            if (r0 == 0) goto L_0x0030
            boolean r2 = r0.equals(r7)
            if (r2 == 0) goto L_0x0030
            r2 = r1
            goto L_0x0031
        L_0x0030:
            r2 = r8
        L_0x0031:
            if (r0 == 0) goto L_0x0041
            android.net.Uri r0 = r0.mUri
            if (r0 == 0) goto L_0x0041
            android.net.Uri r7 = r7.mUri
            boolean r7 = r0.equals(r7)
            if (r7 == 0) goto L_0x0041
            r7 = r1
            goto L_0x0042
        L_0x0041:
            r7 = r8
        L_0x0042:
            if (r7 == 0) goto L_0x0047
            if (r2 == 0) goto L_0x0047
            goto L_0x0048
        L_0x0047:
            r1 = r8
        L_0x0048:
            r3.mShowActionSpinner = r8
            r3.mIsHeader = r5
            androidx.slice.widget.RowContent r4 = (androidx.slice.widget.RowContent) r4
            r3.mRowContent = r4
            r3.mRowIndex = r6
            r3.populateViews(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowView.setSliceItem(androidx.slice.widget.SliceContent, boolean, int, int, androidx.slice.widget.SliceView$OnSliceActionListener):void");
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setStyle(SliceStyle sliceStyle, RowStyle rowStyle) {
        this.mSliceStyle = sliceStyle;
        this.mRowStyle = rowStyle;
        if (sliceStyle != null) {
            setViewSidePaddings(this.mStartContainer, rowStyle.mTitleItemStartPadding, rowStyle.mTitleItemEndPadding);
            LinearLayout linearLayout = this.mContent;
            RowStyle rowStyle2 = this.mRowStyle;
            setViewSidePaddings(linearLayout, rowStyle2.mContentStartPadding, rowStyle2.mContentEndPadding);
            TextView textView = this.mPrimaryText;
            RowStyle rowStyle3 = this.mRowStyle;
            setViewSidePaddings(textView, rowStyle3.mTitleStartPadding, rowStyle3.mTitleEndPadding);
            LinearLayout linearLayout2 = this.mSubContent;
            RowStyle rowStyle4 = this.mRowStyle;
            setViewSidePaddings(linearLayout2, rowStyle4.mSubContentStartPadding, rowStyle4.mSubContentEndPadding);
            LinearLayout linearLayout3 = this.mEndContainer;
            RowStyle rowStyle5 = this.mRowStyle;
            setViewSidePaddings(linearLayout3, rowStyle5.mEndItemStartPadding, rowStyle5.mEndItemEndPadding);
            View view = this.mBottomDivider;
            RowStyle rowStyle6 = this.mRowStyle;
            int i = rowStyle6.mBottomDividerStartPadding;
            int i2 = rowStyle6.mBottomDividerEndPadding;
            boolean z = i < 0 && i2 < 0;
            if (view != null && !z) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                if (i >= 0) {
                    marginLayoutParams.setMarginStart(i);
                }
                if (i2 >= 0) {
                    marginLayoutParams.setMarginEnd(i2);
                }
                view.setLayoutParams(marginLayoutParams);
            }
            View view2 = this.mActionDivider;
            int i3 = this.mRowStyle.mActionDividerHeight;
            if (view2 != null && i3 >= 0) {
                ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
                layoutParams.height = i3;
                view2.setLayoutParams(layoutParams);
            }
            if (this.mRowStyle.getTintColor() != -1) {
                setTint(this.mRowStyle.getTintColor());
            }
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public void setTint(int i) {
        this.mTintColor = i;
        if (this.mRowContent != null) {
            populateViews(true);
        }
    }

    public final void setViewClickable(View view, boolean z) {
        Drawable drawable = null;
        view.setOnClickListener(z ? this : null);
        if (z) {
            drawable = SliceViewUtil.getDrawable(getContext(), 16843534);
        }
        view.setBackground(drawable);
        view.setClickable(z);
    }

    public final void setViewSidePaddings(View view, int i, int i2) {
        boolean z = i < 0 && i2 < 0;
        if (view != null && !z) {
            if (i < 0) {
                i = view.getPaddingStart();
            }
            int paddingTop = view.getPaddingTop();
            if (i2 < 0) {
                i2 = view.getPaddingEnd();
            }
            view.setPaddingRelative(i, paddingTop, i2, view.getPaddingBottom());
        }
    }

    public void updateActionSpinner() {
        this.mActionSpinner.setVisibility(this.mShowActionSpinner ? 0 : 8);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX DEBUG: Type inference failed for r2v0. Raw type applied. Possible types: java.util.List<androidx.slice.core.SliceAction> */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x0139  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateEndItems() {
        /*
        // Method dump skipped, instructions count: 320
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowView.updateEndItems():void");
    }
}
