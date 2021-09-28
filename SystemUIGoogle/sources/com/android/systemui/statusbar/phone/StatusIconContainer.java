package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.android.keyguard.AlphaOptimizedLinearLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.StatusIconDisplayable;
import com.android.systemui.statusbar.notification.stack.AnimationFilter;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.ViewState;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class StatusIconContainer extends AlphaOptimizedLinearLayout {
    private int mDotPadding;
    private int mIconDotFrameWidth;
    private int mIconSpacing;
    private ArrayList<String> mIgnoredSlots;
    private ArrayList<StatusIconState> mLayoutStates;
    private ArrayList<View> mMeasureViews;
    private boolean mNeedsUnderflow;
    private boolean mShouldRestrictIcons;
    private int mStaticDotDiameter;
    private int mUnderflowStart;
    private int mUnderflowWidth;
    private static final AnimationProperties ADD_ICON_PROPERTIES = new AnimationProperties() { // from class: com.android.systemui.statusbar.phone.StatusIconContainer.1
        private AnimationFilter mAnimationFilter = new AnimationFilter().animateAlpha();

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(200).setDelay(50);
    private static final AnimationProperties X_ANIMATION_PROPERTIES = new AnimationProperties() { // from class: com.android.systemui.statusbar.phone.StatusIconContainer.2
        private AnimationFilter mAnimationFilter = new AnimationFilter().animateX();

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(200);
    private static final AnimationProperties ANIMATE_ALL_PROPERTIES = new AnimationProperties() { // from class: com.android.systemui.statusbar.phone.StatusIconContainer.3
        private AnimationFilter mAnimationFilter = new AnimationFilter().animateX().animateY().animateAlpha().animateScale();

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(200);

    public StatusIconContainer(Context context) {
        this(context, null);
    }

    public StatusIconContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mUnderflowStart = 0;
        this.mShouldRestrictIcons = true;
        this.mLayoutStates = new ArrayList<>();
        this.mMeasureViews = new ArrayList<>();
        this.mIgnoredSlots = new ArrayList<>();
        initDimens();
        setWillNotDraw(true);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setShouldRestrictIcons(boolean z) {
        this.mShouldRestrictIcons = z;
    }

    public boolean isRestrictingIcons() {
        return this.mShouldRestrictIcons;
    }

    private void initDimens() {
        this.mIconDotFrameWidth = getResources().getDimensionPixelSize(17105528);
        this.mDotPadding = getResources().getDimensionPixelSize(R$dimen.overflow_icon_dot_padding);
        this.mIconSpacing = getResources().getDimensionPixelSize(R$dimen.status_bar_system_icon_spacing);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.overflow_dot_radius) * 2;
        this.mStaticDotDiameter = dimensionPixelSize;
        this.mUnderflowWidth = this.mIconDotFrameWidth + ((dimensionPixelSize + this.mDotPadding) * 0);
    }

    @Override // android.widget.LinearLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float height = ((float) getHeight()) / 2.0f;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();
            int i6 = (int) (height - (((float) measuredHeight) / 2.0f));
            childAt.layout(0, i6, measuredWidth, measuredHeight + i6);
        }
        resetViewStates();
        calculateIconTranslations();
        applyIconStates();
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        int i4;
        this.mMeasureViews.clear();
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            StatusIconDisplayable statusIconDisplayable = (StatusIconDisplayable) getChildAt(i5);
            if (statusIconDisplayable.isIconVisible() && !statusIconDisplayable.isIconBlocked() && !this.mIgnoredSlots.contains(statusIconDisplayable.getSlot())) {
                this.mMeasureViews.add((View) statusIconDisplayable);
            }
        }
        int size2 = this.mMeasureViews.size();
        int i6 = size2 <= 7 ? 7 : 6;
        int i7 = ((LinearLayout) this).mPaddingLeft + ((LinearLayout) this).mPaddingRight;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 0);
        this.mNeedsUnderflow = this.mShouldRestrictIcons && size2 > 7;
        boolean z = true;
        for (int i8 = 0; i8 < size2; i8++) {
            View view = this.mMeasureViews.get((size2 - i8) - 1);
            measureChild(view, makeMeasureSpec, i2);
            if (i8 == size2 - 1) {
                i3 = 0;
            } else {
                i3 = this.mIconSpacing;
            }
            if (!this.mShouldRestrictIcons) {
                i4 = getViewTotalMeasuredWidth(view);
            } else if (i8 >= i6 || !z) {
                if (z) {
                    i7 += this.mUnderflowWidth;
                    z = false;
                }
            } else {
                i4 = getViewTotalMeasuredWidth(view);
            }
            i7 += i4 + i3;
        }
        if (mode == 1073741824) {
            if (!this.mNeedsUnderflow && i7 > size) {
                this.mNeedsUnderflow = true;
            }
            setMeasuredDimension(size, View.MeasureSpec.getSize(i2));
            return;
        }
        if (mode != Integer.MIN_VALUE || i7 <= size) {
            size = i7;
        } else {
            this.mNeedsUnderflow = true;
        }
        setMeasuredDimension(size, View.MeasureSpec.getSize(i2));
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        super.onViewAdded(view);
        StatusIconState statusIconState = new StatusIconState();
        statusIconState.justAdded = true;
        view.setTag(R$id.status_bar_view_state_tag, statusIconState);
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        view.setTag(R$id.status_bar_view_state_tag, null);
    }

    public void addIgnoredSlot(String str) {
        addIgnoredSlotInternal(str);
        requestLayout();
    }

    public void addIgnoredSlots(List<String> list) {
        for (String str : list) {
            addIgnoredSlotInternal(str);
        }
        requestLayout();
    }

    private void addIgnoredSlotInternal(String str) {
        if (!this.mIgnoredSlots.contains(str)) {
            this.mIgnoredSlots.add(str);
        }
    }

    public void removeIgnoredSlot(String str) {
        this.mIgnoredSlots.remove(str);
        requestLayout();
    }

    public void removeIgnoredSlots(List<String> list) {
        for (String str : list) {
            this.mIgnoredSlots.remove(str);
        }
        requestLayout();
    }

    private void calculateIconTranslations() {
        int i;
        this.mLayoutStates.clear();
        float width = (float) getWidth();
        float paddingEnd = width - ((float) getPaddingEnd());
        float paddingStart = (float) getPaddingStart();
        int childCount = getChildCount();
        int i2 = childCount - 1;
        while (true) {
            if (i2 < 0) {
                break;
            }
            View childAt = getChildAt(i2);
            StatusIconDisplayable statusIconDisplayable = (StatusIconDisplayable) childAt;
            StatusIconState viewStateFromChild = getViewStateFromChild(childAt);
            if (!statusIconDisplayable.isIconVisible() || statusIconDisplayable.isIconBlocked() || this.mIgnoredSlots.contains(statusIconDisplayable.getSlot())) {
                viewStateFromChild.visibleState = 2;
            } else {
                float viewTotalWidth = paddingEnd - ((float) getViewTotalWidth(childAt));
                viewStateFromChild.visibleState = 0;
                viewStateFromChild.xTranslation = viewTotalWidth;
                this.mLayoutStates.add(0, viewStateFromChild);
                paddingEnd = viewTotalWidth - ((float) this.mIconSpacing);
            }
            i2--;
        }
        int size = this.mLayoutStates.size();
        int i3 = 7;
        if (size > 7) {
            i3 = 6;
        }
        this.mUnderflowStart = 0;
        int i4 = size - 1;
        int i5 = 0;
        while (true) {
            if (i4 < 0) {
                i4 = -1;
                break;
            }
            StatusIconState statusIconState = this.mLayoutStates.get(i4);
            if ((this.mNeedsUnderflow && statusIconState.xTranslation < ((float) this.mUnderflowWidth) + paddingStart) || (this.mShouldRestrictIcons && i5 >= i3)) {
                break;
            }
            this.mUnderflowStart = (int) Math.max(paddingStart, (statusIconState.xTranslation - ((float) this.mUnderflowWidth)) - ((float) this.mIconSpacing));
            i5++;
            i4--;
        }
        if (i4 != -1) {
            int i6 = this.mStaticDotDiameter + this.mDotPadding;
            int i7 = (this.mUnderflowStart + this.mUnderflowWidth) - this.mIconDotFrameWidth;
            int i8 = 0;
            while (i4 >= 0) {
                StatusIconState statusIconState2 = this.mLayoutStates.get(i4);
                if (i8 < 1) {
                    statusIconState2.xTranslation = (float) i7;
                    statusIconState2.visibleState = 1;
                    i7 -= i6;
                    i8++;
                } else {
                    statusIconState2.visibleState = 2;
                }
                i4--;
            }
        }
        if (isLayoutRtl()) {
            for (i = 0; i < childCount; i++) {
                View childAt2 = getChildAt(i);
                StatusIconState viewStateFromChild2 = getViewStateFromChild(childAt2);
                viewStateFromChild2.xTranslation = (width - viewStateFromChild2.xTranslation) - ((float) childAt2.getWidth());
            }
        }
    }

    private void applyIconStates() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            StatusIconState viewStateFromChild = getViewStateFromChild(childAt);
            if (viewStateFromChild != null) {
                viewStateFromChild.applyToView(childAt);
            }
        }
    }

    private void resetViewStates() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            StatusIconState viewStateFromChild = getViewStateFromChild(childAt);
            if (viewStateFromChild != null) {
                viewStateFromChild.initFrom(childAt);
                viewStateFromChild.alpha = 1.0f;
                viewStateFromChild.hidden = false;
            }
        }
    }

    private static StatusIconState getViewStateFromChild(View view) {
        return (StatusIconState) view.getTag(R$id.status_bar_view_state_tag);
    }

    private static int getViewTotalMeasuredWidth(View view) {
        return view.getMeasuredWidth() + view.getPaddingStart() + view.getPaddingEnd();
    }

    private static int getViewTotalWidth(View view) {
        return view.getWidth() + view.getPaddingStart() + view.getPaddingEnd();
    }

    /* loaded from: classes.dex */
    public static class StatusIconState extends ViewState {
        public int visibleState = 0;
        public boolean justAdded = true;
        float distanceToViewEnd = -1.0f;

        @Override // com.android.systemui.statusbar.notification.stack.ViewState
        public void applyToView(View view) {
            float width = (view.getParent() instanceof View ? (float) ((View) view.getParent()).getWidth() : 0.0f) - this.xTranslation;
            if (view instanceof StatusIconDisplayable) {
                StatusIconDisplayable statusIconDisplayable = (StatusIconDisplayable) view;
                AnimationProperties animationProperties = null;
                boolean z = true;
                if (this.justAdded || (statusIconDisplayable.getVisibleState() == 2 && this.visibleState == 0)) {
                    super.applyToView(view);
                    view.setAlpha(0.0f);
                    statusIconDisplayable.setVisibleState(2);
                    animationProperties = StatusIconContainer.ADD_ICON_PROPERTIES;
                } else {
                    int visibleState = statusIconDisplayable.getVisibleState();
                    int i = this.visibleState;
                    if (visibleState != i) {
                        if (statusIconDisplayable.getVisibleState() == 0 && this.visibleState == 2) {
                            z = false;
                        } else {
                            animationProperties = StatusIconContainer.ANIMATE_ALL_PROPERTIES;
                        }
                    } else if (!(i == 2 || this.distanceToViewEnd == width)) {
                        animationProperties = StatusIconContainer.X_ANIMATION_PROPERTIES;
                    }
                }
                statusIconDisplayable.setVisibleState(this.visibleState, z);
                if (animationProperties != null) {
                    animateTo(view, animationProperties);
                } else {
                    super.applyToView(view);
                }
                this.justAdded = false;
                this.distanceToViewEnd = width;
            }
        }
    }
}
