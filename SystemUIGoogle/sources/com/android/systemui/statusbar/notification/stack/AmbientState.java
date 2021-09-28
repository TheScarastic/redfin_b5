package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import android.util.MathUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
/* loaded from: classes.dex */
public class AmbientState {
    private ActivatableNotificationView mActivatedChild;
    private float mAppearFraction;
    private boolean mAppearing;
    private int mBaseZHeight;
    private final StackScrollAlgorithm.BypassController mBypassController;
    private int mContentHeight;
    private float mCurrentScrollVelocity;
    private boolean mDimmed;
    private boolean mDismissAllInProgress;
    private boolean mDozing;
    private float mExpandingVelocity;
    private boolean mExpansionChanging;
    private float mExpansionFraction;
    private boolean mHasAlertEntries;
    private float mHideAmount;
    private boolean mHideSensitive;
    private int mIntrinsicPadding;
    private boolean mIsShadeOpening;
    private ExpandableView mLastVisibleBackgroundChild;
    private int mLayoutHeight;
    private int mLayoutMinHeight;
    private float mMaxHeadsUpTranslation;
    private Runnable mOnPulseHeightChangedListener;
    private float mOverExpansion;
    private float mOverScrollBottomAmount;
    private float mOverScrollTopAmount;
    private boolean mPanelFullWidth;
    private boolean mPanelTracking;
    private boolean mPulsing;
    private boolean mQsCustomizerShowing;
    private int mScrollY;
    private final StackScrollAlgorithm.SectionProvider mSectionProvider;
    private boolean mShadeExpanded;
    private NotificationShelf mShelf;
    private float mStackEndHeight;
    private float mStackTranslation;
    private int mStatusBarState;
    private int mTopPadding;
    private ExpandableNotificationRow mTrackedHeadsUpRow;
    private boolean mUnlockHintRunning;
    private int mZDistanceBetweenElements;
    private float mPulseHeight = 100000.0f;
    private float mDozeAmount = 0.0f;
    private float mStackY = 0.0f;
    private float mStackHeight = 0.0f;

    private static int getBaseHeight(int i) {
        return 0;
    }

    public float getStackEndHeight() {
        return this.mStackEndHeight;
    }

    public void setStackEndHeight(float f) {
        this.mStackEndHeight = f;
    }

    public void setStackY(float f) {
        this.mStackY = f;
    }

    public float getStackY() {
        return this.mStackY;
    }

    public void setExpansionFraction(float f) {
        this.mExpansionFraction = f;
    }

    public float getExpansionFraction() {
        return this.mExpansionFraction;
    }

    public void setStackHeight(float f) {
        this.mStackHeight = f;
    }

    public float getStackHeight() {
        return this.mStackHeight;
    }

    public AmbientState(Context context, StackScrollAlgorithm.SectionProvider sectionProvider, StackScrollAlgorithm.BypassController bypassController) {
        this.mSectionProvider = sectionProvider;
        this.mBypassController = bypassController;
        reload(context);
    }

    public void reload(Context context) {
        int zDistanceBetweenElements = getZDistanceBetweenElements(context);
        this.mZDistanceBetweenElements = zDistanceBetweenElements;
        this.mBaseZHeight = getBaseHeight(zDistanceBetweenElements);
    }

    public void setIsShadeOpening(boolean z) {
        this.mIsShadeOpening = z;
    }

    public boolean isShadeOpening() {
        return this.mIsShadeOpening;
    }

    /* access modifiers changed from: package-private */
    public void setOverExpansion(float f) {
        this.mOverExpansion = f;
    }

    /* access modifiers changed from: package-private */
    public float getOverExpansion() {
        return this.mOverExpansion;
    }

    private static int getZDistanceBetweenElements(Context context) {
        return Math.max(1, context.getResources().getDimensionPixelSize(R$dimen.z_distance_between_notifications));
    }

    public static int getNotificationLaunchHeight(Context context) {
        return getZDistanceBetweenElements(context) * 4;
    }

    public int getBaseZHeight() {
        return this.mBaseZHeight;
    }

    public int getZDistanceBetweenElements() {
        return this.mZDistanceBetweenElements;
    }

    public int getScrollY() {
        return this.mScrollY;
    }

    public void setScrollY(int i) {
        this.mScrollY = Math.max(i, 0);
    }

    public void setDimmed(boolean z) {
        this.mDimmed = z;
    }

    public void setDozing(boolean z) {
        this.mDozing = z;
    }

    public void setHideAmount(float f) {
        if (f == 1.0f && this.mHideAmount != f) {
            setPulseHeight(100000.0f);
        }
        this.mHideAmount = f;
    }

    public float getHideAmount() {
        return this.mHideAmount;
    }

    public void setHideSensitive(boolean z) {
        this.mHideSensitive = z;
    }

    public void setActivatedChild(ActivatableNotificationView activatableNotificationView) {
        this.mActivatedChild = activatableNotificationView;
    }

    public boolean isDimmed() {
        return this.mDimmed && (!isPulseExpanding() || this.mDozeAmount != 1.0f);
    }

    public boolean isDozing() {
        return this.mDozing;
    }

    public boolean isHideSensitive() {
        return this.mHideSensitive;
    }

    public ActivatableNotificationView getActivatedChild() {
        return this.mActivatedChild;
    }

    public void setOverScrollAmount(float f, boolean z) {
        if (z) {
            this.mOverScrollTopAmount = f;
        } else {
            this.mOverScrollBottomAmount = f;
        }
    }

    public boolean isBypassEnabled() {
        return this.mBypassController.isBypassEnabled();
    }

    public float getOverScrollAmount(boolean z) {
        return z ? this.mOverScrollTopAmount : this.mOverScrollBottomAmount;
    }

    public StackScrollAlgorithm.SectionProvider getSectionProvider() {
        return this.mSectionProvider;
    }

    public float getStackTranslation() {
        return this.mStackTranslation;
    }

    public void setStackTranslation(float f) {
        this.mStackTranslation = f;
    }

    public void setLayoutHeight(int i) {
        this.mLayoutHeight = i;
    }

    public float getTopPadding() {
        return (float) this.mTopPadding;
    }

    public void setTopPadding(int i) {
        this.mTopPadding = i;
    }

    public int getInnerHeight() {
        return getInnerHeight(false);
    }

    public int getInnerHeight(boolean z) {
        if (this.mDozeAmount == 1.0f && !isPulseExpanding()) {
            return this.mShelf.getHeight();
        }
        int max = Math.max(this.mLayoutMinHeight, Math.min(this.mLayoutHeight, this.mContentHeight) - this.mTopPadding);
        if (z) {
            return max;
        }
        float f = (float) max;
        return (int) MathUtils.lerp(f, Math.min(this.mPulseHeight, f), this.mDozeAmount);
    }

    public boolean isPulseExpanding() {
        return (this.mPulseHeight == 100000.0f || this.mDozeAmount == 0.0f || this.mHideAmount == 1.0f) ? false : true;
    }

    public boolean isShadeExpanded() {
        return this.mShadeExpanded;
    }

    public void setShadeExpanded(boolean z) {
        this.mShadeExpanded = z;
    }

    public void setMaxHeadsUpTranslation(float f) {
        this.mMaxHeadsUpTranslation = f;
    }

    public float getMaxHeadsUpTranslation() {
        return this.mMaxHeadsUpTranslation;
    }

    public void setDismissAllInProgress(boolean z) {
        this.mDismissAllInProgress = z;
    }

    public void setLayoutMinHeight(int i) {
        this.mLayoutMinHeight = i;
    }

    public void setShelf(NotificationShelf notificationShelf) {
        this.mShelf = notificationShelf;
    }

    public NotificationShelf getShelf() {
        return this.mShelf;
    }

    public void setContentHeight(int i) {
        this.mContentHeight = i;
    }

    public void setLastVisibleBackgroundChild(ExpandableView expandableView) {
        this.mLastVisibleBackgroundChild = expandableView;
    }

    public ExpandableView getLastVisibleBackgroundChild() {
        return this.mLastVisibleBackgroundChild;
    }

    public void setCurrentScrollVelocity(float f) {
        this.mCurrentScrollVelocity = f;
    }

    public float getCurrentScrollVelocity() {
        return this.mCurrentScrollVelocity;
    }

    public boolean isOnKeyguard() {
        return this.mStatusBarState == 1;
    }

    public void setStatusBarState(int i) {
        this.mStatusBarState = i;
    }

    public void setExpandingVelocity(float f) {
        this.mExpandingVelocity = f;
    }

    public void setExpansionChanging(boolean z) {
        this.mExpansionChanging = z;
    }

    public boolean isExpansionChanging() {
        return this.mExpansionChanging;
    }

    public float getExpandingVelocity() {
        return this.mExpandingVelocity;
    }

    public void setPanelTracking(boolean z) {
        this.mPanelTracking = z;
    }

    public void setPulsing(boolean z) {
        this.mPulsing = z;
    }

    public boolean isPulsing() {
        return this.mPulsing;
    }

    public boolean isPulsing(NotificationEntry notificationEntry) {
        return this.mPulsing && notificationEntry.isAlerting();
    }

    public boolean isPanelTracking() {
        return this.mPanelTracking;
    }

    public void setPanelFullWidth(boolean z) {
        this.mPanelFullWidth = z;
    }

    public void setUnlockHintRunning(boolean z) {
        this.mUnlockHintRunning = z;
    }

    public boolean isUnlockHintRunning() {
        return this.mUnlockHintRunning;
    }

    public boolean isQsCustomizerShowing() {
        return this.mQsCustomizerShowing;
    }

    public void setQsCustomizerShowing(boolean z) {
        this.mQsCustomizerShowing = z;
    }

    public void setIntrinsicPadding(int i) {
        this.mIntrinsicPadding = i;
    }

    public boolean isDozingAndNotPulsing(ExpandableView expandableView) {
        if (expandableView instanceof ExpandableNotificationRow) {
            return isDozingAndNotPulsing((ExpandableNotificationRow) expandableView);
        }
        return false;
    }

    public boolean isDozingAndNotPulsing(ExpandableNotificationRow expandableNotificationRow) {
        return isDozing() && !isPulsing(expandableNotificationRow.getEntry());
    }

    public boolean isFullyHidden() {
        return this.mHideAmount == 1.0f;
    }

    public boolean isHiddenAtAll() {
        return this.mHideAmount != 0.0f;
    }

    public void setAppearing(boolean z) {
        this.mAppearing = z;
    }

    public void setPulseHeight(float f) {
        if (f != this.mPulseHeight) {
            this.mPulseHeight = f;
            Runnable runnable = this.mOnPulseHeightChangedListener;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public float getPulseHeight() {
        float f = this.mPulseHeight;
        if (f == 100000.0f) {
            return 0.0f;
        }
        return f;
    }

    public void setDozeAmount(float f) {
        if (f != this.mDozeAmount) {
            this.mDozeAmount = f;
            if (f == 0.0f || f == 1.0f) {
                setPulseHeight(100000.0f);
            }
        }
    }

    public boolean isFullyAwake() {
        return this.mDozeAmount == 0.0f;
    }

    public void setOnPulseHeightChangedListener(Runnable runnable) {
        this.mOnPulseHeightChangedListener = runnable;
    }

    public void setTrackedHeadsUpRow(ExpandableNotificationRow expandableNotificationRow) {
        this.mTrackedHeadsUpRow = expandableNotificationRow;
    }

    public ExpandableNotificationRow getTrackedHeadsUpRow() {
        ExpandableNotificationRow expandableNotificationRow = this.mTrackedHeadsUpRow;
        if (expandableNotificationRow == null || !expandableNotificationRow.isAboveShelf()) {
            return null;
        }
        return this.mTrackedHeadsUpRow;
    }

    public void setAppearFraction(float f) {
        this.mAppearFraction = f;
    }

    public float getAppearFraction() {
        return this.mAppearFraction;
    }

    public void setHasAlertEntries(boolean z) {
        this.mHasAlertEntries = z;
    }
}
