package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.ExpandableViewState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.notification.stack.ViewState;
import com.android.systemui.statusbar.phone.NotificationIconContainer;
/* loaded from: classes.dex */
public class NotificationShelf extends ActivatableNotificationView implements View.OnLayoutChangeListener, StatusBarStateController.StateListener {
    private AmbientState mAmbientState;
    private NotificationIconContainer mCollapsedIcons;
    private NotificationShelfController mController;
    private float mCornerAnimationDistance;
    private float mFirstElementRoundness;
    private boolean mHasItemsInStableShelf;
    private boolean mHideBackground;
    private NotificationStackScrollLayoutController mHostLayoutController;
    private boolean mInteractive;
    private int mNotGoneIndex;
    private int mPaddingBetweenElements;
    private int mScrollFastThreshold;
    private NotificationIconContainer mShelfIcons;
    private boolean mShowNotificationShelf;
    private int mStatusBarHeight;
    private int mStatusBarState;
    private static final int TAG_CONTINUOUS_CLIPPING = R$id.continuous_clipping_tag;
    private static final Interpolator ICON_ALPHA_INTERPOLATOR = new PathInterpolator(0.6f, 0.0f, 0.6f, 0.0f);
    private int[] mTmp = new int[2];
    private boolean mAnimationsEnabled = true;
    private Rect mClipRect = new Rect();
    private int mIndexOfFirstViewInShelf = -1;

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean hasNoContentHeight() {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean needsClippingToShelf() {
        return false;
    }

    public NotificationShelf(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, android.view.View
    @VisibleForTesting
    public void onFinishInflate() {
        super.onFinishInflate();
        NotificationIconContainer notificationIconContainer = (NotificationIconContainer) findViewById(R$id.content);
        this.mShelfIcons = notificationIconContainer;
        notificationIconContainer.setClipChildren(false);
        this.mShelfIcons.setClipToPadding(false);
        setClipToActualHeight(false);
        setClipChildren(false);
        setClipToPadding(false);
        this.mShelfIcons.setIsStaticLayout(false);
        setBottomRoundness(1.0f, false);
        setTopRoundness(1.0f, false);
        setFirstInSection(true);
        initDimens();
    }

    public void bind(AmbientState ambientState, NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        this.mAmbientState = ambientState;
        this.mHostLayoutController = notificationStackScrollLayoutController;
    }

    private void initDimens() {
        Resources resources = getResources();
        this.mStatusBarHeight = resources.getDimensionPixelOffset(R$dimen.status_bar_height);
        this.mPaddingBetweenElements = resources.getDimensionPixelSize(R$dimen.notification_divider_height);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = resources.getDimensionPixelOffset(R$dimen.notification_shelf_height);
        setLayoutParams(layoutParams);
        int dimensionPixelOffset = resources.getDimensionPixelOffset(R$dimen.shelf_icon_container_padding);
        this.mShelfIcons.setPadding(dimensionPixelOffset, 0, dimensionPixelOffset, 0);
        this.mScrollFastThreshold = resources.getDimensionPixelOffset(R$dimen.scroll_fast_threshold);
        this.mShowNotificationShelf = resources.getBoolean(R$bool.config_showNotificationShelf);
        this.mCornerAnimationDistance = (float) resources.getDimensionPixelSize(R$dimen.notification_corner_animation_distance);
        this.mShelfIcons.setInNotificationIconShelf(true);
        if (!this.mShowNotificationShelf) {
            setVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        initDimens();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    protected View getContentView() {
        return this.mShelfIcons;
    }

    public NotificationIconContainer getShelfIcons() {
        return this.mShelfIcons;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public ExpandableViewState createExpandableViewState() {
        return new ShelfState();
    }

    public void updateState(StackScrollAlgorithm.StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        ExpandableView lastVisibleBackgroundChild = ambientState.getLastVisibleBackgroundChild();
        ShelfState shelfState = (ShelfState) getViewState();
        boolean z = false;
        if (!this.mShowNotificationShelf || lastVisibleBackgroundChild == null) {
            shelfState.hidden = true;
            shelfState.location = 64;
            shelfState.hasItemsInStableShelf = false;
            return;
        }
        ExpandableViewState viewState = lastVisibleBackgroundChild.getViewState();
        shelfState.copyFrom(viewState);
        shelfState.height = getIntrinsicHeight();
        shelfState.zTranslation = (float) ambientState.getBaseZHeight();
        shelfState.clipTopAmount = 0;
        if (!ambientState.isExpansionChanging() || ambientState.isOnKeyguard()) {
            shelfState.alpha = 1.0f - ambientState.getHideAmount();
        } else {
            shelfState.alpha = Interpolators.getNotificationScrimAlpha(ambientState.getExpansionFraction(), true);
        }
        shelfState.belowSpeedBump = this.mHostLayoutController.getSpeedBumpIndex() == 0;
        shelfState.hideSensitive = false;
        shelfState.xTranslation = getTranslationX();
        shelfState.hasItemsInStableShelf = viewState.inShelf;
        shelfState.firstViewInShelf = stackScrollAlgorithmState.firstViewInShelf;
        int i = this.mNotGoneIndex;
        if (i != -1) {
            shelfState.notGoneIndex = Math.min(shelfState.notGoneIndex, i);
        }
        if (!this.mAmbientState.isShadeExpanded() || this.mAmbientState.isQsCustomizerShowing() || stackScrollAlgorithmState.firstViewInShelf == null) {
            z = true;
        }
        shelfState.hidden = z;
        int indexOf = stackScrollAlgorithmState.visibleChildren.indexOf(stackScrollAlgorithmState.firstViewInShelf);
        if (this.mAmbientState.isExpansionChanging() && stackScrollAlgorithmState.firstViewInShelf != null && indexOf > 0 && stackScrollAlgorithmState.visibleChildren.get(indexOf - 1).getViewState().hidden) {
            shelfState.hidden = true;
        }
        shelfState.yTranslation = (ambientState.getStackY() + ambientState.getStackHeight()) - ((float) shelfState.height);
    }

    /* JADX WARNING: Removed duplicated region for block: B:64:0x0150  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x016c A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0173  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateAppearance() {
        /*
        // Method dump skipped, instructions count: 611
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationShelf.updateAppearance():void");
    }

    private void updateCornerRoundnessOnScroll(ActivatableNotificationView activatableNotificationView, float f, float f2) {
        boolean z = true;
        boolean z2 = !this.mAmbientState.isOnKeyguard() && !this.mAmbientState.isShadeExpanded() && (activatableNotificationView instanceof ExpandableNotificationRow) && ((ExpandableNotificationRow) activatableNotificationView).isHeadsUp();
        boolean z3 = this.mAmbientState.isShadeExpanded() && activatableNotificationView == this.mAmbientState.getTrackedHeadsUpRow();
        if (f >= f2 || this.mHostLayoutController.isViewAffectedBySwipe(activatableNotificationView) || z2 || z3 || activatableNotificationView.isAboveShelf() || this.mAmbientState.isPulsing() || this.mAmbientState.isDozing()) {
            z = false;
        }
        if (z) {
            float dimension = getResources().getDimension(R$dimen.notification_corner_radius_small) / getResources().getDimension(R$dimen.notification_corner_radius);
            float actualHeight = ((float) activatableNotificationView.getActualHeight()) + f;
            float expansionFraction = this.mCornerAnimationDistance * this.mAmbientState.getExpansionFraction();
            float f3 = f2 - expansionFraction;
            float f4 = 1.0f;
            if (actualHeight >= f3) {
                float saturate = MathUtils.saturate((actualHeight - f3) / expansionFraction);
                if (activatableNotificationView.isLastInSection()) {
                    saturate = 1.0f;
                }
                activatableNotificationView.setBottomRoundness(saturate, false);
            } else if (actualHeight < f3) {
                activatableNotificationView.setBottomRoundness(activatableNotificationView.isLastInSection() ? 1.0f : dimension, false);
            }
            if (f >= f3) {
                float saturate2 = MathUtils.saturate((f - f3) / expansionFraction);
                if (!activatableNotificationView.isFirstInSection()) {
                    f4 = saturate2;
                }
                activatableNotificationView.setTopRoundness(f4, false);
            } else if (f < f3) {
                if (activatableNotificationView.isFirstInSection()) {
                    dimension = 1.0f;
                }
                activatableNotificationView.setTopRoundness(dimension, false);
            }
        }
    }

    private void clipTransientViews() {
        for (int i = 0; i < this.mHostLayoutController.getTransientViewCount(); i++) {
            View transientView = this.mHostLayoutController.getTransientView(i);
            if (transientView instanceof ExpandableView) {
                updateNotificationClipHeight((ExpandableView) transientView, getTranslationY(), -1);
            }
        }
    }

    private void setFirstElementRoundness(float f) {
        if (this.mFirstElementRoundness != f) {
            this.mFirstElementRoundness = f;
        }
    }

    /* access modifiers changed from: private */
    public void updateIconClipAmount(ExpandableNotificationRow expandableNotificationRow) {
        float translationY = expandableNotificationRow.getTranslationY();
        if (getClipTopAmount() != 0) {
            translationY = Math.max(translationY, getTranslationY() + ((float) getClipTopAmount()));
        }
        StatusBarIconView shelfIcon = expandableNotificationRow.getEntry().getIcons().getShelfIcon();
        float translationY2 = getTranslationY() + ((float) shelfIcon.getTop()) + shelfIcon.getTranslationY();
        if (translationY2 >= translationY || this.mAmbientState.isFullyHidden()) {
            shelfIcon.setClipBounds(null);
            return;
        }
        int i = (int) (translationY - translationY2);
        shelfIcon.setClipBounds(new Rect(0, i, shelfIcon.getWidth(), Math.max(i, shelfIcon.getHeight())));
    }

    private void updateContinuousClipping(final ExpandableNotificationRow expandableNotificationRow) {
        final StatusBarIconView shelfIcon = expandableNotificationRow.getEntry().getIcons().getShelfIcon();
        boolean z = true;
        boolean z2 = ViewState.isAnimatingY(shelfIcon) && !this.mAmbientState.isDozing();
        int i = TAG_CONTINUOUS_CLIPPING;
        if (shelfIcon.getTag(i) == null) {
            z = false;
        }
        if (z2 && !z) {
            final ViewTreeObserver viewTreeObserver = shelfIcon.getViewTreeObserver();
            final AnonymousClass1 r2 = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.NotificationShelf.1
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    if (!ViewState.isAnimatingY(shelfIcon)) {
                        if (viewTreeObserver.isAlive()) {
                            viewTreeObserver.removeOnPreDrawListener(this);
                        }
                        shelfIcon.setTag(NotificationShelf.TAG_CONTINUOUS_CLIPPING, null);
                        return true;
                    }
                    NotificationShelf.this.updateIconClipAmount(expandableNotificationRow);
                    return true;
                }
            };
            viewTreeObserver.addOnPreDrawListener(r2);
            shelfIcon.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.statusbar.NotificationShelf.2
                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view) {
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view) {
                    if (view == shelfIcon) {
                        if (viewTreeObserver.isAlive()) {
                            viewTreeObserver.removeOnPreDrawListener(r2);
                        }
                        shelfIcon.setTag(NotificationShelf.TAG_CONTINUOUS_CLIPPING, null);
                    }
                }
            });
            shelfIcon.setTag(i, r2);
        }
    }

    private int updateNotificationClipHeight(ExpandableView expandableView, float f, int i) {
        float translationY = expandableView.getTranslationY() + ((float) expandableView.getActualHeight());
        boolean z = true;
        boolean z2 = (expandableView.isPinned() || expandableView.isHeadsUpAnimatingAway()) && !this.mAmbientState.isDozingAndNotPulsing(expandableView);
        if (!this.mAmbientState.isPulseExpanding()) {
            z = expandableView.showingPulsing();
        } else if (i != 0) {
            z = false;
        }
        if (translationY <= f || z || (!this.mAmbientState.isShadeExpanded() && z2)) {
            expandableView.setClipBottomAmount(0);
        } else {
            int i2 = (int) (translationY - f);
            if (z2) {
                i2 = Math.min(expandableView.getIntrinsicHeight() - expandableView.getCollapsedHeight(), i2);
            }
            expandableView.setClipBottomAmount(i2);
        }
        if (z) {
            return (int) (translationY - getTranslationY());
        }
        return 0;
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableView
    public void setFakeShadowIntensity(float f, float f2, int i, int i2) {
        if (!this.mHasItemsInStableShelf) {
            f = 0.0f;
        }
        super.setFakeShadowIntensity(f, f2, i, i2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x005a, code lost:
        if (r12 >= r1) goto L_0x005c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private float updateShelfTransformation(int r12, com.android.systemui.statusbar.notification.row.ExpandableView r13, boolean r14, boolean r15, boolean r16) {
        /*
            r11 = this;
            r0 = r11
            float r1 = r13.getTranslationY()
            int r2 = r13.getActualHeight()
            int r3 = r0.mPaddingBetweenElements
            int r2 = r2 + r3
            r3 = r13
            float r4 = r11.calculateIconTransformationStart(r13)
            float r5 = (float) r2
            float r5 = r5 + r1
            float r5 = r5 - r4
            int r6 = r11.getIntrinsicHeight()
            float r6 = (float) r6
            float r5 = java.lang.Math.min(r5, r6)
            if (r16 == 0) goto L_0x003a
            int r6 = r13.getMinHeight()
            int r7 = r11.getIntrinsicHeight()
            int r6 = r6 - r7
            int r2 = java.lang.Math.min(r2, r6)
            int r6 = r13.getMinHeight()
            int r7 = r11.getIntrinsicHeight()
            int r6 = r6 - r7
            float r6 = (float) r6
            float r5 = java.lang.Math.min(r5, r6)
        L_0x003a:
            float r2 = (float) r2
            float r6 = r1 + r2
            float r7 = r11.getTranslationY()
            com.android.systemui.statusbar.notification.stack.AmbientState r8 = r0.mAmbientState
            boolean r8 = r8.isExpansionChanging()
            r9 = 0
            r10 = 1065353216(0x3f800000, float:1.0)
            if (r8 == 0) goto L_0x005f
            com.android.systemui.statusbar.notification.stack.AmbientState r8 = r0.mAmbientState
            boolean r8 = r8.isOnKeyguard()
            if (r8 != 0) goto L_0x005f
            int r1 = r0.mIndexOfFirstViewInShelf
            r2 = -1
            if (r1 == r2) goto L_0x00a3
            r2 = r12
            if (r2 < r1) goto L_0x00a3
        L_0x005c:
            r2 = r10
            r9 = r2
            goto L_0x00a4
        L_0x005f:
            int r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1))
            if (r6 < 0) goto L_0x00a3
            com.android.systemui.statusbar.notification.stack.AmbientState r6 = r0.mAmbientState
            boolean r6 = r6.isUnlockHintRunning()
            if (r6 == 0) goto L_0x0071
            boolean r6 = r13.isInShelf()
            if (r6 == 0) goto L_0x00a3
        L_0x0071:
            com.android.systemui.statusbar.notification.stack.AmbientState r6 = r0.mAmbientState
            boolean r6 = r6.isShadeExpanded()
            if (r6 != 0) goto L_0x0085
            boolean r6 = r13.isPinned()
            if (r6 != 0) goto L_0x00a3
            boolean r6 = r13.isHeadsUpAnimatingAway()
            if (r6 != 0) goto L_0x00a3
        L_0x0085:
            int r6 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r6 >= 0) goto L_0x005c
            float r6 = r7 - r1
            float r2 = r6 / r2
            float r2 = java.lang.Math.min(r10, r2)
            float r2 = r10 - r2
            if (r16 == 0) goto L_0x0098
            float r4 = r4 - r1
            float r6 = r6 / r4
            goto L_0x009b
        L_0x0098:
            float r7 = r7 - r4
            float r6 = r7 / r5
        L_0x009b:
            float r1 = android.util.MathUtils.constrain(r6, r9, r10)
            float r10 = r10 - r1
            r9 = r2
            r2 = r10
            goto L_0x00a4
        L_0x00a3:
            r2 = r9
        L_0x00a4:
            r0 = r11
            r1 = r13
            r3 = r14
            r4 = r15
            r5 = r16
            r0.updateIconPositioning(r1, r2, r3, r4, r5)
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationShelf.updateShelfTransformation(int, com.android.systemui.statusbar.notification.row.ExpandableView, boolean, boolean, boolean):float");
    }

    private float calculateIconTransformationStart(ExpandableView expandableView) {
        View shelfTransformationTarget = expandableView.getShelfTransformationTarget();
        if (shelfTransformationTarget == null) {
            return expandableView.getTranslationY();
        }
        return (expandableView.getTranslationY() + ((float) expandableView.getRelativeTopPadding(shelfTransformationTarget))) - ((float) expandableView.getShelfIcon().getTop());
    }

    private void updateIconPositioning(ExpandableView expandableView, float f, boolean z, boolean z2, boolean z3) {
        StatusBarIconView shelfIcon = expandableView.getShelfIcon();
        NotificationIconContainer.IconState iconState = getIconState(shelfIcon);
        if (iconState != null) {
            boolean z4 = false;
            float f2 = (f > 0.5f ? 1 : (f == 0.5f ? 0 : -1)) > 0 || isTargetClipped(expandableView) ? 1.0f : 0.0f;
            if (f == f2) {
                iconState.noAnimations = (z || z2) && !z3;
            }
            if (!z3 && (z || (z2 && !ViewState.isAnimatingY(shelfIcon)))) {
                iconState.cancelAnimations(shelfIcon);
                iconState.noAnimations = true;
            }
            if (!this.mAmbientState.isHiddenAtAll() || expandableView.isInShelf()) {
                if (iconState.clampedAppearAmount != f2) {
                    z4 = true;
                }
                iconState.needsCannedAnimation = z4;
            } else {
                f = this.mAmbientState.isFullyHidden() ? 1.0f : 0.0f;
            }
            iconState.clampedAppearAmount = f2;
            setIconTransformationAmount(expandableView, f);
        }
    }

    private boolean isTargetClipped(ExpandableView expandableView) {
        View shelfTransformationTarget = expandableView.getShelfTransformationTarget();
        if (shelfTransformationTarget != null && expandableView.getTranslationY() + expandableView.getContentTranslation() + ((float) expandableView.getRelativeTopPadding(shelfTransformationTarget)) + ((float) shelfTransformationTarget.getHeight()) >= getTranslationY() - ((float) this.mPaddingBetweenElements)) {
            return true;
        }
        return false;
    }

    private void setIconTransformationAmount(ExpandableView expandableView, float f) {
        ExpandableNotificationRow expandableNotificationRow;
        StatusBarIconView shelfIcon;
        NotificationIconContainer.IconState iconState;
        boolean z = expandableView instanceof ExpandableNotificationRow;
        if (z && (iconState = getIconState((shelfIcon = (expandableNotificationRow = (ExpandableNotificationRow) expandableView).getShelfIcon()))) != null) {
            iconState.alpha = ICON_ALPHA_INTERPOLATOR.getInterpolation(f);
            boolean z2 = true;
            boolean z3 = (expandableNotificationRow.isDrawingAppearAnimation() && !expandableNotificationRow.isInShelf()) || (z && expandableNotificationRow.isLowPriority() && this.mShelfIcons.hasMaxNumDot()) || ((f == 0.0f && !iconState.isAnimating(shelfIcon)) || expandableNotificationRow.isAboveShelf() || expandableNotificationRow.showingPulsing() || expandableNotificationRow.getTranslationZ() > ((float) this.mAmbientState.getBaseZHeight()));
            iconState.hidden = z3;
            if (z3) {
                f = 0.0f;
            }
            iconState.iconAppearAmount = f;
            iconState.xTranslation = this.mShelfIcons.getActualPaddingStart();
            if (!expandableNotificationRow.isInShelf() || expandableNotificationRow.isTransformingIntoShelf()) {
                z2 = false;
            }
            if (z2) {
                iconState.iconAppearAmount = 1.0f;
                iconState.alpha = 1.0f;
                iconState.hidden = false;
            }
            int contrastedStaticDrawableColor = shelfIcon.getContrastedStaticDrawableColor(getBackgroundColorWithoutTint());
            if (expandableNotificationRow.isShowingIcon() && contrastedStaticDrawableColor != 0) {
                contrastedStaticDrawableColor = NotificationUtils.interpolateColors(expandableNotificationRow.getOriginalIconColor(), contrastedStaticDrawableColor, iconState.iconAppearAmount);
            }
            iconState.iconColor = contrastedStaticDrawableColor;
        }
    }

    private NotificationIconContainer.IconState getIconState(StatusBarIconView statusBarIconView) {
        return this.mShelfIcons.getIconState(statusBarIconView);
    }

    private void setHideBackground(boolean z) {
        if (this.mHideBackground != z) {
            this.mHideBackground = z;
            updateOutline();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    protected boolean needsOutline() {
        return !this.mHideBackground && super.needsOutline();
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableView, android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateRelativeOffset();
        int i5 = getResources().getDisplayMetrics().heightPixels;
        this.mClipRect.set(0, -i5, getWidth(), i5);
        this.mShelfIcons.setClipBounds(this.mClipRect);
    }

    private void updateRelativeOffset() {
        this.mCollapsedIcons.getLocationOnScreen(this.mTmp);
        getLocationOnScreen(this.mTmp);
    }

    public int getNotGoneIndex() {
        return this.mNotGoneIndex;
    }

    /* access modifiers changed from: private */
    public void setHasItemsInStableShelf(boolean z) {
        if (this.mHasItemsInStableShelf != z) {
            this.mHasItemsInStableShelf = z;
            updateInteractiveness();
        }
    }

    public void setCollapsedIcons(NotificationIconContainer notificationIconContainer) {
        this.mCollapsedIcons = notificationIconContainer;
        notificationIconContainer.addOnLayoutChangeListener(this);
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStateChanged(int i) {
        this.mStatusBarState = i;
        updateInteractiveness();
    }

    private void updateInteractiveness() {
        int i = 1;
        boolean z = this.mStatusBarState == 1 && this.mHasItemsInStableShelf;
        this.mInteractive = z;
        setClickable(z);
        setFocusable(this.mInteractive);
        if (!this.mInteractive) {
            i = 4;
        }
        setImportantForAccessibility(i);
    }

    public void setAnimationsEnabled(boolean z) {
        this.mAnimationsEnabled = z;
        if (!z) {
            this.mShelfIcons.setAnimationsEnabled(false);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.mInteractive) {
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, getContext().getString(R$string.accessibility_overflow_action)));
        }
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        updateRelativeOffset();
    }

    public void setController(NotificationShelfController notificationShelfController) {
        this.mController = notificationShelfController;
    }

    public void setIndexOfFirstViewInShelf(ExpandableView expandableView) {
        this.mIndexOfFirstViewInShelf = this.mHostLayoutController.indexOfChild(expandableView);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ShelfState extends ExpandableViewState {
        private ExpandableView firstViewInShelf;
        private boolean hasItemsInStableShelf;

        private ShelfState() {
        }

        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public void applyToView(View view) {
            if (NotificationShelf.this.mShowNotificationShelf) {
                super.applyToView(view);
                NotificationShelf.this.setIndexOfFirstViewInShelf(this.firstViewInShelf);
                NotificationShelf.this.updateAppearance();
                NotificationShelf.this.setHasItemsInStableShelf(this.hasItemsInStableShelf);
                NotificationShelf.this.mShelfIcons.setAnimationsEnabled(NotificationShelf.this.mAnimationsEnabled);
            }
        }

        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public void animateTo(View view, AnimationProperties animationProperties) {
            if (NotificationShelf.this.mShowNotificationShelf) {
                super.animateTo(view, animationProperties);
                NotificationShelf.this.setIndexOfFirstViewInShelf(this.firstViewInShelf);
                NotificationShelf.this.updateAppearance();
                NotificationShelf.this.setHasItemsInStableShelf(this.hasItemsInStableShelf);
                NotificationShelf.this.mShelfIcons.setAnimationsEnabled(NotificationShelf.this.mAnimationsEnabled);
            }
        }
    }
}
