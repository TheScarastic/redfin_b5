package androidx.slice.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import androidx.core.content.ContextCompat;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.widget.SliceView;
import com.android.systemui.shared.R;
/* loaded from: classes.dex */
public class SliceActionView extends FrameLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static final int[] CHECKED_STATE_SET = {16842912};
    public View mActionView;
    public EventInfo mEventInfo;
    public int mIconSize;
    public int mImageSize;
    public SliceActionLoadingListener mLoadingListener;
    public SliceView.OnSliceActionListener mObserver;
    public ProgressBar mProgressView;
    public SliceActionImpl mSliceAction;
    public int mTextActionPadding;

    /* loaded from: classes.dex */
    public static class ImageToggle extends ImageView implements Checkable, View.OnClickListener {
        public boolean mIsChecked;
        public View.OnClickListener mListener;

        public ImageToggle(Context context) {
            super(context);
            super.setOnClickListener(this);
        }

        @Override // android.widget.Checkable
        public boolean isChecked() {
            return this.mIsChecked;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            toggle();
        }

        @Override // android.widget.ImageView, android.view.View
        public int[] onCreateDrawableState(int i) {
            int[] onCreateDrawableState = super.onCreateDrawableState(i + 1);
            if (this.mIsChecked) {
                ImageView.mergeDrawableStates(onCreateDrawableState, SliceActionView.CHECKED_STATE_SET);
            }
            return onCreateDrawableState;
        }

        @Override // android.widget.Checkable
        public void setChecked(boolean z) {
            if (this.mIsChecked != z) {
                this.mIsChecked = z;
                refreshDrawableState();
                View.OnClickListener onClickListener = this.mListener;
                if (onClickListener != null) {
                    onClickListener.onClick(this);
                }
            }
        }

        @Override // android.view.View
        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.mListener = onClickListener;
        }

        @Override // android.widget.Checkable
        public void toggle() {
            setChecked(!this.mIsChecked);
        }
    }

    /* loaded from: classes.dex */
    public interface SliceActionLoadingListener {
    }

    public SliceActionView(Context context, RowStyle rowStyle) {
        super(context);
        Resources resources = getContext().getResources();
        this.mIconSize = resources.getDimensionPixelSize(R.dimen.abc_slice_icon_size);
        this.mImageSize = resources.getDimensionPixelSize(R.dimen.abc_slice_small_image_size);
        this.mTextActionPadding = 0;
        if (rowStyle != null) {
            this.mIconSize = rowStyle.mIconSize;
            this.mImageSize = rowStyle.mImageSize;
            this.mTextActionPadding = rowStyle.mTextActionPadding;
        }
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (this.mSliceAction != null && this.mActionView != null) {
            sendActionInternal();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mSliceAction != null && this.mActionView != null) {
            sendActionInternal();
        }
    }

    public final void sendActionInternal() {
        EventInfo eventInfo;
        SliceActionImpl sliceActionImpl = this.mSliceAction;
        if (sliceActionImpl != null && sliceActionImpl.mActionItem != null) {
            Intent intent = null;
            try {
                if (sliceActionImpl.isToggle()) {
                    boolean isChecked = ((Checkable) this.mActionView).isChecked();
                    intent = new Intent().addFlags(268435456).putExtra("android.app.slice.extra.TOGGLE_STATE", isChecked);
                    EventInfo eventInfo2 = this.mEventInfo;
                    if (eventInfo2 != null) {
                        eventInfo2.state = isChecked ? 1 : 0;
                    }
                }
                if (this.mSliceAction.mActionItem.fireActionInternal(getContext(), intent)) {
                    setLoading(true);
                    SliceActionLoadingListener sliceActionLoadingListener = this.mLoadingListener;
                    if (sliceActionLoadingListener != null) {
                        EventInfo eventInfo3 = this.mEventInfo;
                        ((SliceAdapter) sliceActionLoadingListener).onSliceActionLoading(this.mSliceAction.mSliceItem, eventInfo3 != null ? eventInfo3.rowIndex : -1);
                    }
                }
                SliceView.OnSliceActionListener onSliceActionListener = this.mObserver;
                if (onSliceActionListener != null && (eventInfo = this.mEventInfo) != null) {
                    onSliceActionListener.onSliceAction(eventInfo, this.mSliceAction.mSliceItem);
                }
            } catch (PendingIntent.CanceledException e) {
                View view = this.mActionView;
                if (view instanceof Checkable) {
                    view.setSelected(!((Checkable) view).isChecked());
                }
                Log.e("SliceActionView", "PendingIntent for slice cannot be sent", e);
            }
        }
    }

    public void setAction(SliceActionImpl sliceActionImpl, EventInfo eventInfo, SliceView.OnSliceActionListener onSliceActionListener, int i, SliceActionLoadingListener sliceActionLoadingListener) {
        View view = this.mActionView;
        if (view != null) {
            removeView(view);
            this.mActionView = null;
        }
        View view2 = this.mProgressView;
        if (view2 != null) {
            removeView(view2);
            this.mProgressView = null;
        }
        this.mSliceAction = sliceActionImpl;
        this.mEventInfo = eventInfo;
        this.mObserver = onSliceActionListener;
        this.mActionView = null;
        this.mLoadingListener = sliceActionLoadingListener;
        int i2 = 0;
        if (sliceActionImpl.mActionType == 2 && sliceActionImpl.mIcon == null) {
            Switch r9 = (Switch) LayoutInflater.from(getContext()).inflate(R.layout.abc_slice_switch, (ViewGroup) this, false);
            r9.setChecked(sliceActionImpl.mIsChecked);
            r9.setOnCheckedChangeListener(this);
            r9.setMinimumHeight(this.mImageSize);
            r9.setMinimumWidth(this.mImageSize);
            addView(r9);
            if (i != -1) {
                int colorAttr = SliceViewUtil.getColorAttr(getContext(), 16842800);
                int[] iArr = CHECKED_STATE_SET;
                int[] iArr2 = FrameLayout.EMPTY_STATE_SET;
                ColorStateList colorStateList = new ColorStateList(new int[][]{iArr, iArr2}, new int[]{i, colorAttr});
                Drawable trackDrawable = r9.getTrackDrawable();
                trackDrawable.setTintList(colorStateList);
                r9.setTrackDrawable(trackDrawable);
                int colorAttr2 = SliceViewUtil.getColorAttr(getContext(), R.attr.colorSwitchThumbNormal);
                if (colorAttr2 == 0) {
                    Context context = getContext();
                    Object obj = ContextCompat.sLock;
                    colorAttr2 = context.getColor(R.color.switch_thumb_normal_material_light);
                }
                ColorStateList colorStateList2 = new ColorStateList(new int[][]{iArr, iArr2}, new int[]{i, colorAttr2});
                Drawable thumbDrawable = r9.getThumbDrawable();
                thumbDrawable.setTintList(colorStateList2);
                r9.setThumbDrawable(thumbDrawable);
            }
            this.mActionView = r9;
        } else if (sliceActionImpl.mImageMode == 6) {
            Button button = new Button(getContext());
            this.mActionView = button;
            button.setText(sliceActionImpl.mTitle);
            addView(this.mActionView);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mActionView.getLayoutParams();
            layoutParams.width = -2;
            layoutParams.height = -2;
            this.mActionView.setLayoutParams(layoutParams);
            int i3 = this.mTextActionPadding;
            this.mActionView.setPadding(i3, i3, i3, i3);
            this.mActionView.setOnClickListener(this);
        } else if (sliceActionImpl.mIcon != null) {
            if (sliceActionImpl.isToggle()) {
                ImageToggle imageToggle = new ImageToggle(getContext());
                imageToggle.setChecked(sliceActionImpl.mIsChecked);
                this.mActionView = imageToggle;
            } else {
                this.mActionView = new ImageView(getContext());
            }
            addView(this.mActionView);
            Drawable loadDrawable = this.mSliceAction.mIcon.loadDrawable(getContext());
            ((ImageView) this.mActionView).setImageDrawable(loadDrawable);
            if (!(i == -1 || this.mSliceAction.mImageMode != 0 || loadDrawable == null)) {
                loadDrawable.setTint(i);
            }
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mActionView.getLayoutParams();
            int i4 = this.mImageSize;
            layoutParams2.width = i4;
            layoutParams2.height = i4;
            this.mActionView.setLayoutParams(layoutParams2);
            if (sliceActionImpl.mImageMode == 0) {
                int i5 = this.mImageSize;
                i2 = (i5 == -1 ? this.mIconSize : i5 - this.mIconSize) / 2;
            }
            this.mActionView.setPadding(i2, i2, i2, i2);
            this.mActionView.setBackground(SliceViewUtil.getDrawable(getContext(), 16843868));
            this.mActionView.setOnClickListener(this);
        }
        View view3 = this.mActionView;
        if (view3 != null) {
            CharSequence charSequence = sliceActionImpl.mContentDescription;
            if (charSequence == null) {
                charSequence = sliceActionImpl.mTitle;
            }
            view3.setContentDescription(charSequence);
        }
    }

    public void setLoading(boolean z) {
        int i = 0;
        if (z) {
            if (this.mProgressView == null) {
                ProgressBar progressBar = (ProgressBar) LayoutInflater.from(getContext()).inflate(R.layout.abc_slice_progress_view, (ViewGroup) this, false);
                this.mProgressView = progressBar;
                addView(progressBar);
            }
            SliceViewUtil.tintIndeterminateProgressBar(getContext(), this.mProgressView);
        }
        this.mActionView.setVisibility(z ? 8 : 0);
        ProgressBar progressBar2 = this.mProgressView;
        if (!z) {
            i = 8;
        }
        progressBar2.setVisibility(i);
    }
}
