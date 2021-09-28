package androidx.slice.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.slice.view.R$styleable;
import com.android.systemui.shared.R;
/* loaded from: classes.dex */
public class RowStyle {
    public int mActionDividerHeight;
    public int mBottomDividerEndPadding;
    public int mBottomDividerStartPadding;
    public int mContentEndPadding;
    public int mContentStartPadding;
    public boolean mDisableRecyclerViewItemAnimator;
    public int mEndItemEndPadding;
    public int mEndItemStartPadding;
    public int mIconSize;
    public int mImageSize;
    public int mProgressBarEndPadding;
    public int mProgressBarInlineWidth;
    public int mProgressBarStartPadding;
    public int mSeekBarInlineWidth;
    public final SliceStyle mSliceStyle;
    public int mSubContentEndPadding;
    public int mSubContentStartPadding;
    public Integer mSubtitleColor;
    public int mTextActionPadding;
    public Integer mTintColor;
    public Integer mTitleColor;
    public int mTitleEndPadding;
    public int mTitleItemEndPadding;
    public int mTitleItemStartPadding;
    public int mTitleStartPadding;

    public RowStyle(Context context, SliceStyle sliceStyle) {
        this.mTitleItemStartPadding = -1;
        this.mTitleItemEndPadding = -1;
        this.mContentStartPadding = -1;
        this.mContentEndPadding = -1;
        this.mTitleStartPadding = -1;
        this.mTitleEndPadding = -1;
        this.mSubContentStartPadding = -1;
        this.mSubContentEndPadding = -1;
        this.mEndItemStartPadding = -1;
        this.mEndItemEndPadding = -1;
        this.mBottomDividerStartPadding = -1;
        this.mBottomDividerEndPadding = -1;
        this.mActionDividerHeight = -1;
        this.mSeekBarInlineWidth = -1;
        this.mProgressBarInlineWidth = -1;
        this.mProgressBarStartPadding = -1;
        this.mProgressBarEndPadding = -1;
        this.mTextActionPadding = -1;
        this.mIconSize = -1;
        this.mDisableRecyclerViewItemAnimator = false;
        this.mSliceStyle = sliceStyle;
        this.mImageSize = context.getResources().getDimensionPixelSize(R.dimen.abc_slice_small_image_size);
    }

    public static Integer getOptionalColor(TypedArray typedArray, int i) {
        if (typedArray.hasValue(i)) {
            return Integer.valueOf(typedArray.getColor(i, 0));
        }
        return null;
    }

    public int getSubtitleColor() {
        Integer num = this.mSubtitleColor;
        if (num != null) {
            return num.intValue();
        }
        return this.mSliceStyle.mSubtitleColor;
    }

    public int getTintColor() {
        Integer num = this.mTintColor;
        if (num != null) {
            return num.intValue();
        }
        return this.mSliceStyle.mTintColor;
    }

    public int getTitleColor() {
        Integer num = this.mTitleColor;
        if (num != null) {
            return num.intValue();
        }
        return this.mSliceStyle.mTitleColor;
    }

    public RowStyle(Context context, int i, SliceStyle sliceStyle) {
        this.mTitleItemStartPadding = -1;
        this.mTitleItemEndPadding = -1;
        this.mContentStartPadding = -1;
        this.mContentEndPadding = -1;
        this.mTitleStartPadding = -1;
        this.mTitleEndPadding = -1;
        this.mSubContentStartPadding = -1;
        this.mSubContentEndPadding = -1;
        this.mEndItemStartPadding = -1;
        this.mEndItemEndPadding = -1;
        this.mBottomDividerStartPadding = -1;
        this.mBottomDividerEndPadding = -1;
        this.mActionDividerHeight = -1;
        this.mSeekBarInlineWidth = -1;
        this.mProgressBarInlineWidth = -1;
        this.mProgressBarStartPadding = -1;
        this.mProgressBarEndPadding = -1;
        this.mTextActionPadding = -1;
        this.mIconSize = -1;
        this.mDisableRecyclerViewItemAnimator = false;
        this.mSliceStyle = sliceStyle;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(i, R$styleable.RowStyle);
        try {
            this.mTitleItemStartPadding = (int) obtainStyledAttributes.getDimension(22, -1.0f);
            this.mTitleItemEndPadding = (int) obtainStyledAttributes.getDimension(21, -1.0f);
            this.mContentStartPadding = (int) obtainStyledAttributes.getDimension(4, -1.0f);
            this.mContentEndPadding = (int) obtainStyledAttributes.getDimension(3, -1.0f);
            this.mTitleStartPadding = (int) obtainStyledAttributes.getDimension(23, -1.0f);
            this.mTitleEndPadding = (int) obtainStyledAttributes.getDimension(20, -1.0f);
            this.mSubContentStartPadding = (int) obtainStyledAttributes.getDimension(15, -1.0f);
            this.mSubContentEndPadding = (int) obtainStyledAttributes.getDimension(14, -1.0f);
            this.mEndItemStartPadding = (int) obtainStyledAttributes.getDimension(7, -1.0f);
            this.mEndItemEndPadding = (int) obtainStyledAttributes.getDimension(6, -1.0f);
            this.mBottomDividerStartPadding = (int) obtainStyledAttributes.getDimension(2, -1.0f);
            this.mBottomDividerEndPadding = (int) obtainStyledAttributes.getDimension(1, -1.0f);
            this.mActionDividerHeight = (int) obtainStyledAttributes.getDimension(0, -1.0f);
            this.mSeekBarInlineWidth = (int) obtainStyledAttributes.getDimension(13, -1.0f);
            this.mProgressBarInlineWidth = (int) obtainStyledAttributes.getDimension(11, -1.0f);
            this.mProgressBarStartPadding = (int) obtainStyledAttributes.getDimension(12, -1.0f);
            this.mProgressBarEndPadding = (int) obtainStyledAttributes.getDimension(10, -1.0f);
            this.mTextActionPadding = (int) obtainStyledAttributes.getDimension(17, 10.0f);
            this.mIconSize = (int) obtainStyledAttributes.getDimension(8, -1.0f);
            this.mDisableRecyclerViewItemAnimator = obtainStyledAttributes.getBoolean(5, false);
            this.mImageSize = (int) obtainStyledAttributes.getDimension(9, (float) context.getResources().getDimensionPixelSize(R.dimen.abc_slice_small_image_size));
            this.mTintColor = getOptionalColor(obtainStyledAttributes, 18);
            this.mTitleColor = getOptionalColor(obtainStyledAttributes, 19);
            this.mSubtitleColor = getOptionalColor(obtainStyledAttributes, 16);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }
}
