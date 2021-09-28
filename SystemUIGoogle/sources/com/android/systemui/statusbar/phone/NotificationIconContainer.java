package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Icon;
import android.util.AttributeSet;
import android.util.Property;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.collection.ArrayMap;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.AlphaOptimizedFrameLayout;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.stack.AnimationFilter;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.ViewState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class NotificationIconContainer extends AlphaOptimizedFrameLayout {
    private boolean mChangingViewPositions;
    private boolean mDisallowNextAnimation;
    private int mDotPadding;
    private boolean mDozing;
    private IconState mFirstVisibleIconState;
    private int mIconSize;
    private boolean mInNotificationIconShelf;
    private StatusBarIconView mIsolatedIcon;
    private View mIsolatedIconForAnimation;
    private Rect mIsolatedIconLocation;
    private IconState mLastVisibleIconState;
    private int mNumDots;
    private boolean mOnLockScreen;
    private int mOverflowWidth;
    private ArrayMap<String, ArrayList<StatusBarIcon>> mReplacingIcons;
    private int mStaticDotDiameter;
    private int mStaticDotRadius;
    private int mThemedTextColorPrimary;
    private float mVisualOverflowStart;
    private static final AnimationProperties DOT_ANIMATION_PROPERTIES = new AnimationProperties() { // from class: com.android.systemui.statusbar.phone.NotificationIconContainer.1
        private AnimationFilter mAnimationFilter = new AnimationFilter().animateX();

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(200);
    private static final AnimationProperties ICON_ANIMATION_PROPERTIES = new AnimationProperties() { // from class: com.android.systemui.statusbar.phone.NotificationIconContainer.2
        private AnimationFilter mAnimationFilter = new AnimationFilter().animateX().animateY().animateAlpha().animateScale();

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(100);
    private static final AnimationProperties sTempProperties = new AnimationProperties() { // from class: com.android.systemui.statusbar.phone.NotificationIconContainer.3
        private AnimationFilter mAnimationFilter = new AnimationFilter();

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    };
    private static final AnimationProperties ADD_ICON_PROPERTIES = new AnimationProperties() { // from class: com.android.systemui.statusbar.phone.NotificationIconContainer.4
        private AnimationFilter mAnimationFilter = new AnimationFilter().animateAlpha();

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(200).setDelay(50);
    private static final AnimationProperties UNISOLATION_PROPERTY_OTHERS = new AnimationProperties() { // from class: com.android.systemui.statusbar.phone.NotificationIconContainer.5
        private AnimationFilter mAnimationFilter = new AnimationFilter().animateAlpha();

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(110);
    private static final AnimationProperties UNISOLATION_PROPERTY = new AnimationProperties() { // from class: com.android.systemui.statusbar.phone.NotificationIconContainer.6
        private AnimationFilter mAnimationFilter = new AnimationFilter().animateX();

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }.setDuration(110);
    private boolean mIsStaticLayout = true;
    private final HashMap<View, IconState> mIconStates = new HashMap<>();
    private int mActualLayoutWidth = Integer.MIN_VALUE;
    private float mActualPaddingEnd = -2.14748365E9f;
    private float mActualPaddingStart = -2.14748365E9f;
    private int mAddAnimationStartIndex = -1;
    private int mCannedAnimationStartIndex = -1;
    private int mSpeedBumpIndex = -1;
    private boolean mAnimationsEnabled = true;
    private int[] mAbsolutePosition = new int[2];

    public NotificationIconContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initDimens();
        setWillNotDraw(true);
    }

    private void initDimens() {
        this.mDotPadding = getResources().getDimensionPixelSize(R$dimen.overflow_icon_dot_padding);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.overflow_dot_radius);
        this.mStaticDotRadius = dimensionPixelSize;
        this.mStaticDotDiameter = dimensionPixelSize * 2;
        this.mThemedTextColorPrimary = Utils.getColorAttr(new ContextThemeWrapper(getContext(), 16974563), 16842806).getDefaultColor();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(-65536);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(getActualPaddingStart(), 0.0f, getLayoutEnd(), (float) getHeight(), paint);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        initDimens();
    }

    @Override // android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float height = ((float) getHeight()) / 2.0f;
        this.mIconSize = 0;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();
            int i6 = (int) (height - (((float) measuredHeight) / 2.0f));
            childAt.layout(0, i6, measuredWidth, measuredHeight + i6);
            if (i5 == 0) {
                setIconSize(childAt.getWidth());
            }
        }
        getLocationOnScreen(this.mAbsolutePosition);
        if (this.mIsStaticLayout) {
            updateState();
        }
    }

    private void setIconSize(int i) {
        this.mIconSize = i;
        this.mOverflowWidth = i + ((this.mStaticDotDiameter + this.mDotPadding) * 0);
    }

    private void updateState() {
        resetViewStates();
        calculateIconTranslations();
        applyIconStates();
    }

    public void applyIconStates() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            IconState iconState = this.mIconStates.get(childAt);
            if (iconState != null) {
                iconState.applyToView(childAt);
            }
        }
        this.mAddAnimationStartIndex = -1;
        this.mCannedAnimationStartIndex = -1;
        this.mDisallowNextAnimation = false;
        this.mIsolatedIconForAnimation = null;
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        super.onViewAdded(view);
        boolean isReplacingIcon = isReplacingIcon(view);
        if (!this.mChangingViewPositions) {
            IconState iconState = new IconState(view);
            if (isReplacingIcon) {
                iconState.justAdded = false;
                iconState.justReplaced = true;
            }
            this.mIconStates.put(view, iconState);
        }
        int indexOfChild = indexOfChild(view);
        if (indexOfChild < getChildCount() - 1 && !isReplacingIcon && this.mIconStates.get(getChildAt(indexOfChild + 1)).iconAppearAmount > 0.0f) {
            int i = this.mAddAnimationStartIndex;
            if (i < 0) {
                this.mAddAnimationStartIndex = indexOfChild;
            } else {
                this.mAddAnimationStartIndex = Math.min(i, indexOfChild);
            }
        }
        if (view instanceof StatusBarIconView) {
            ((StatusBarIconView) view).setDozing(this.mDozing, false, 0);
        }
    }

    private boolean isReplacingIcon(View view) {
        if (this.mReplacingIcons == null || !(view instanceof StatusBarIconView)) {
            return false;
        }
        StatusBarIconView statusBarIconView = (StatusBarIconView) view;
        Icon sourceIcon = statusBarIconView.getSourceIcon();
        ArrayList<StatusBarIcon> arrayList = this.mReplacingIcons.get(statusBarIconView.getNotification().getGroupKey());
        if (arrayList == null || !sourceIcon.sameAs(arrayList.get(0).icon)) {
            return false;
        }
        return true;
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if (view instanceof StatusBarIconView) {
            boolean isReplacingIcon = isReplacingIcon(view);
            StatusBarIconView statusBarIconView = (StatusBarIconView) view;
            if (areAnimationsEnabled(statusBarIconView) && statusBarIconView.getVisibleState() != 2 && view.getVisibility() == 0 && isReplacingIcon) {
                int findFirstViewIndexAfter = findFirstViewIndexAfter(statusBarIconView.getTranslationX());
                int i = this.mAddAnimationStartIndex;
                if (i < 0) {
                    this.mAddAnimationStartIndex = findFirstViewIndexAfter;
                } else {
                    this.mAddAnimationStartIndex = Math.min(i, findFirstViewIndexAfter);
                }
            }
            if (!this.mChangingViewPositions) {
                this.mIconStates.remove(view);
                if (areAnimationsEnabled(statusBarIconView) && !isReplacingIcon) {
                    boolean z = false;
                    addTransientView(statusBarIconView, 0);
                    if (view == this.mIsolatedIcon) {
                        z = true;
                    }
                    statusBarIconView.setVisibleState(2, true, new Runnable(statusBarIconView) { // from class: com.android.systemui.statusbar.phone.NotificationIconContainer$$ExternalSyntheticLambda0
                        public final /* synthetic */ StatusBarIconView f$1;

                        {
                            this.f$1 = r2;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationIconContainer.this.lambda$onViewRemoved$0(this.f$1);
                        }
                    }, z ? 110 : 0);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewRemoved$0(StatusBarIconView statusBarIconView) {
        removeTransientView(statusBarIconView);
    }

    public boolean hasMaxNumDot() {
        return this.mNumDots >= 1;
    }

    /* access modifiers changed from: private */
    public boolean areAnimationsEnabled(StatusBarIconView statusBarIconView) {
        return this.mAnimationsEnabled || statusBarIconView == this.mIsolatedIcon;
    }

    private int findFirstViewIndexAfter(float f) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i).getTranslationX() > f) {
                return i;
            }
        }
        return getChildCount();
    }

    public void resetViewStates() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            IconState iconState = this.mIconStates.get(childAt);
            iconState.initFrom(childAt);
            StatusBarIconView statusBarIconView = this.mIsolatedIcon;
            iconState.alpha = (statusBarIconView == null || childAt == statusBarIconView) ? 1.0f : 0.0f;
            iconState.hidden = false;
        }
    }

    public void calculateIconTranslations() {
        int i;
        IconState iconState;
        float f;
        float actualPaddingStart = getActualPaddingStart();
        int childCount = getChildCount();
        if (this.mOnLockScreen) {
            i = 5;
        } else {
            i = this.mIsStaticLayout ? 4 : childCount;
        }
        float layoutEnd = getLayoutEnd();
        float maxOverflowStart = getMaxOverflowStart();
        float f2 = 0.0f;
        this.mVisualOverflowStart = 0.0f;
        this.mFirstVisibleIconState = null;
        int i2 = -1;
        int i3 = 0;
        while (true) {
            boolean z = true;
            if (i3 >= childCount) {
                break;
            }
            View childAt = getChildAt(i3);
            IconState iconState2 = this.mIconStates.get(childAt);
            float f3 = iconState2.iconAppearAmount;
            if (f3 == 1.0f) {
                iconState2.xTranslation = actualPaddingStart;
            }
            if (this.mFirstVisibleIconState == null) {
                this.mFirstVisibleIconState = iconState2;
            }
            int i4 = this.mSpeedBumpIndex;
            boolean z2 = (i4 != -1 && i3 >= i4 && f3 > f2) || i3 >= i;
            boolean z3 = i3 == childCount + -1;
            float iconScaleIncreased = (!this.mOnLockScreen || !(childAt instanceof StatusBarIconView)) ? 1.0f : ((StatusBarIconView) childAt).getIconScaleIncreased();
            iconState2.visibleState = iconState2.hidden ? 2 : 0;
            if (z3) {
                f = layoutEnd - ((float) this.mIconSize);
            } else {
                f = maxOverflowStart - ((float) this.mIconSize);
            }
            if (actualPaddingStart <= f) {
                z = false;
            }
            if (i2 == -1 && (z2 || z)) {
                i2 = (!z3 || z2) ? i3 : i3 - 1;
                float f4 = layoutEnd - ((float) this.mOverflowWidth);
                this.mVisualOverflowStart = f4;
                if (z2 || this.mIsStaticLayout) {
                    this.mVisualOverflowStart = Math.min(actualPaddingStart, f4);
                }
            }
            actualPaddingStart += iconState2.iconAppearAmount * ((float) childAt.getWidth()) * iconScaleIncreased;
            i3++;
            f2 = 0.0f;
        }
        this.mNumDots = 0;
        if (i2 != -1) {
            float f5 = this.mVisualOverflowStart;
            while (i2 < childCount) {
                IconState iconState3 = this.mIconStates.get(getChildAt(i2));
                int i5 = this.mStaticDotDiameter + this.mDotPadding;
                iconState3.xTranslation = f5;
                int i6 = this.mNumDots;
                if (i6 < 1) {
                    if (i6 != 0 || iconState3.iconAppearAmount >= 0.8f) {
                        iconState3.visibleState = 1;
                        this.mNumDots = i6 + 1;
                    } else {
                        iconState3.visibleState = 0;
                    }
                    if (this.mNumDots == 1) {
                        i5 *= 1;
                    }
                    f5 += ((float) i5) * iconState3.iconAppearAmount;
                    this.mLastVisibleIconState = iconState3;
                } else {
                    iconState3.visibleState = 2;
                }
                i2++;
            }
        } else if (childCount > 0) {
            this.mLastVisibleIconState = this.mIconStates.get(getChildAt(childCount - 1));
            this.mFirstVisibleIconState = this.mIconStates.get(getChildAt(0));
        }
        if (isLayoutRtl()) {
            for (int i7 = 0; i7 < childCount; i7++) {
                View childAt2 = getChildAt(i7);
                IconState iconState4 = this.mIconStates.get(childAt2);
                iconState4.xTranslation = (((float) getWidth()) - iconState4.xTranslation) - ((float) childAt2.getWidth());
            }
        }
        StatusBarIconView statusBarIconView = this.mIsolatedIcon;
        if (!(statusBarIconView == null || (iconState = this.mIconStates.get(statusBarIconView)) == null)) {
            iconState.xTranslation = ((float) (this.mIsolatedIconLocation.left - this.mAbsolutePosition[0])) - (((1.0f - this.mIsolatedIcon.getIconScale()) * ((float) this.mIsolatedIcon.getWidth())) / 2.0f);
            iconState.visibleState = 0;
        }
    }

    private float getLayoutEnd() {
        return ((float) getActualWidth()) - getActualPaddingEnd();
    }

    private float getActualPaddingEnd() {
        float f = this.mActualPaddingEnd;
        return f == -2.14748365E9f ? (float) getPaddingEnd() : f;
    }

    public float getActualPaddingStart() {
        float f = this.mActualPaddingStart;
        return f == -2.14748365E9f ? (float) getPaddingStart() : f;
    }

    public void setIsStaticLayout(boolean z) {
        this.mIsStaticLayout = z;
    }

    public int getActualWidth() {
        int i = this.mActualLayoutWidth;
        return i == Integer.MIN_VALUE ? getWidth() : i;
    }

    private float getMaxOverflowStart() {
        return getLayoutEnd() - ((float) this.mOverflowWidth);
    }

    public void setChangingViewPositions(boolean z) {
        this.mChangingViewPositions = z;
    }

    public void setDozing(boolean z, boolean z2, long j) {
        this.mDozing = z;
        this.mDisallowNextAnimation |= !z2;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof StatusBarIconView) {
                ((StatusBarIconView) childAt).setDozing(z, z2, j);
            }
        }
    }

    public IconState getIconState(StatusBarIconView statusBarIconView) {
        return this.mIconStates.get(statusBarIconView);
    }

    public void setSpeedBumpIndex(int i) {
        this.mSpeedBumpIndex = i;
    }

    public void setAnimationsEnabled(boolean z) {
        if (!z && this.mAnimationsEnabled) {
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                IconState iconState = this.mIconStates.get(childAt);
                if (iconState != null) {
                    iconState.cancelAnimations(childAt);
                    iconState.applyToView(childAt);
                }
            }
        }
        this.mAnimationsEnabled = z;
    }

    public void setReplacingIcons(ArrayMap<String, ArrayList<StatusBarIcon>> arrayMap) {
        this.mReplacingIcons = arrayMap;
    }

    public void showIconIsolated(StatusBarIconView statusBarIconView, boolean z) {
        if (z) {
            this.mIsolatedIconForAnimation = statusBarIconView != null ? statusBarIconView : this.mIsolatedIcon;
        }
        this.mIsolatedIcon = statusBarIconView;
        updateState();
    }

    public void setIsolatedIconLocation(Rect rect, boolean z) {
        this.mIsolatedIconLocation = rect;
        if (z) {
            updateState();
        }
    }

    public void setOnLockScreen(boolean z) {
        this.mOnLockScreen = z;
    }

    public void setInNotificationIconShelf(boolean z) {
        this.mInNotificationIconShelf = z;
    }

    /* loaded from: classes.dex */
    public class IconState extends ViewState {
        private boolean justReplaced;
        private final View mView;
        public boolean needsCannedAnimation;
        public boolean noAnimations;
        public int visibleState;
        public float iconAppearAmount = 1.0f;
        public float clampedAppearAmount = 1.0f;
        public boolean justAdded = true;
        public int iconColor = 0;
        private final Consumer<Property> mCannedAnimationEndListener = new NotificationIconContainer$IconState$$ExternalSyntheticLambda0(this);

        public IconState(View view) {
            this.mView = view;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(Property property) {
            if (property == View.TRANSLATION_Y && this.iconAppearAmount == 0.0f && this.mView.getVisibility() == 0) {
                this.mView.setVisibility(4);
            }
        }

        @Override // com.android.systemui.statusbar.notification.stack.ViewState
        public void applyToView(View view) {
            boolean z;
            AnimationProperties animationProperties;
            int i;
            AnimationProperties animationProperties2;
            Interpolator interpolator;
            if (view instanceof StatusBarIconView) {
                StatusBarIconView statusBarIconView = (StatusBarIconView) view;
                boolean z2 = true;
                boolean z3 = NotificationIconContainer.this.areAnimationsEnabled(statusBarIconView) && !NotificationIconContainer.this.mDisallowNextAnimation && !this.noAnimations && !((this.visibleState == 2 && statusBarIconView.getVisibleState() == 1) || (this.visibleState == 1 && statusBarIconView.getVisibleState() == 2));
                if (z3) {
                    if (this.justAdded || this.justReplaced) {
                        super.applyToView(statusBarIconView);
                        if (this.justAdded && this.iconAppearAmount != 0.0f) {
                            statusBarIconView.setAlpha(0.0f);
                            statusBarIconView.setVisibleState(2, false);
                            animationProperties = NotificationIconContainer.ADD_ICON_PROPERTIES;
                            z = true;
                        }
                        z = false;
                        animationProperties = null;
                    } else {
                        if (this.visibleState != statusBarIconView.getVisibleState()) {
                            animationProperties = NotificationIconContainer.DOT_ANIMATION_PROPERTIES;
                            z = true;
                        }
                        z = false;
                        animationProperties = null;
                    }
                    if (!z && NotificationIconContainer.this.mAddAnimationStartIndex >= 0 && NotificationIconContainer.this.indexOfChild(view) >= NotificationIconContainer.this.mAddAnimationStartIndex && !(statusBarIconView.getVisibleState() == 2 && this.visibleState == 2)) {
                        animationProperties = NotificationIconContainer.DOT_ANIMATION_PROPERTIES;
                        z = true;
                    }
                    long j = 100;
                    if (this.needsCannedAnimation) {
                        AnimationFilter animationFilter = NotificationIconContainer.sTempProperties.getAnimationFilter();
                        animationFilter.reset();
                        animationFilter.combineFilter(NotificationIconContainer.ICON_ANIMATION_PROPERTIES.getAnimationFilter());
                        NotificationIconContainer.sTempProperties.resetCustomInterpolators();
                        NotificationIconContainer.sTempProperties.combineCustomInterpolators(NotificationIconContainer.ICON_ANIMATION_PROPERTIES);
                        if (statusBarIconView.showsConversation()) {
                            interpolator = Interpolators.ICON_OVERSHOT_LESS;
                        } else {
                            interpolator = Interpolators.ICON_OVERSHOT;
                        }
                        NotificationIconContainer.sTempProperties.setCustomInterpolator(View.TRANSLATION_Y, interpolator);
                        NotificationIconContainer.sTempProperties.setAnimationEndAction(this.mCannedAnimationEndListener);
                        if (animationProperties != null) {
                            animationFilter.combineFilter(animationProperties.getAnimationFilter());
                            NotificationIconContainer.sTempProperties.combineCustomInterpolators(animationProperties);
                        }
                        animationProperties = NotificationIconContainer.sTempProperties;
                        animationProperties.setDuration(100);
                        NotificationIconContainer notificationIconContainer = NotificationIconContainer.this;
                        notificationIconContainer.mCannedAnimationStartIndex = notificationIconContainer.indexOfChild(view);
                        z = true;
                    }
                    if (!z && NotificationIconContainer.this.mCannedAnimationStartIndex >= 0 && NotificationIconContainer.this.indexOfChild(view) > NotificationIconContainer.this.mCannedAnimationStartIndex && !(statusBarIconView.getVisibleState() == 2 && this.visibleState == 2)) {
                        AnimationFilter animationFilter2 = NotificationIconContainer.sTempProperties.getAnimationFilter();
                        animationFilter2.reset();
                        animationFilter2.animateX();
                        NotificationIconContainer.sTempProperties.resetCustomInterpolators();
                        animationProperties = NotificationIconContainer.sTempProperties;
                        animationProperties.setDuration(100);
                        z = true;
                    }
                    if (NotificationIconContainer.this.mIsolatedIconForAnimation != null) {
                        if (view == NotificationIconContainer.this.mIsolatedIconForAnimation) {
                            animationProperties2 = NotificationIconContainer.UNISOLATION_PROPERTY;
                            if (NotificationIconContainer.this.mIsolatedIcon == null) {
                                j = 0;
                            }
                            animationProperties2.setDelay(j);
                        } else {
                            animationProperties2 = NotificationIconContainer.UNISOLATION_PROPERTY_OTHERS;
                            if (NotificationIconContainer.this.mIsolatedIcon != null) {
                                j = 0;
                            }
                            animationProperties2.setDelay(j);
                        }
                        animationProperties = animationProperties2;
                        z = true;
                    }
                } else {
                    z = false;
                    animationProperties = null;
                }
                statusBarIconView.setVisibleState(this.visibleState, z3);
                if (NotificationIconContainer.this.mInNotificationIconShelf) {
                    i = NotificationIconContainer.this.mThemedTextColorPrimary;
                } else {
                    i = this.iconColor;
                }
                statusBarIconView.setIconColor(i, this.needsCannedAnimation && z3);
                if (z) {
                    animateTo(statusBarIconView, animationProperties);
                } else {
                    super.applyToView(view);
                }
                NotificationIconContainer.sTempProperties.setAnimationEndAction(null);
                if (this.iconAppearAmount != 1.0f) {
                    z2 = false;
                }
                statusBarIconView.setIsInShelf(z2);
            }
            this.justAdded = false;
            this.justReplaced = false;
            this.needsCannedAnimation = false;
        }

        @Override // com.android.systemui.statusbar.notification.stack.ViewState
        public void initFrom(View view) {
            super.initFrom(view);
            if (view instanceof StatusBarIconView) {
                this.iconColor = ((StatusBarIconView) view).getStaticDrawableColor();
            }
        }
    }
}
