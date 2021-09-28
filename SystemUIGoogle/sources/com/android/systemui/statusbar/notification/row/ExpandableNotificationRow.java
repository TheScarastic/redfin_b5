package com.android.systemui.statusbar.notification.row;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.Log;
import android.util.MathUtils;
import android.util.Pair;
import android.util.Property;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.ContrastColorUtil;
import com.android.internal.widget.CallLayout;
import com.android.settingslib.Utils;
import com.android.systemui.Dependency;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.AboveShelfChangedListener;
import com.android.systemui.statusbar.notification.ExpandAnimationParameters;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.ExpandableViewState;
import com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.SwipeableView;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.InflatedSmartReplyState;
import com.android.systemui.wmshell.BubblesManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ExpandableNotificationRow extends ActivatableNotificationView implements PluginListener<NotificationMenuRowPlugin>, SwipeableView {
    private static final long RECENTLY_ALERTED_THRESHOLD_MS = TimeUnit.SECONDS.toMillis(30);
    private static final Property<ExpandableNotificationRow, Float> TRANSLATE_CONTENT = new FloatProperty<ExpandableNotificationRow>("translate") { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.2
        public void setValue(ExpandableNotificationRow expandableNotificationRow, float f) {
            expandableNotificationRow.setTranslation(f);
        }

        public Float get(ExpandableNotificationRow expandableNotificationRow) {
            return Float.valueOf(expandableNotificationRow.getTranslation());
        }
    };
    private boolean mAboveShelf;
    private AboveShelfChangedListener mAboveShelfChangedListener;
    private String mAppName;
    private float mBottomRoundnessDuringExpandAnimation;
    private Optional<BubblesManager> mBubblesManagerOptional;
    private KeyguardBypassController mBypassController;
    private View mChildAfterViewWhenDismissed;
    private boolean mChildIsExpanding;
    private NotificationChildrenContainer mChildrenContainer;
    private ViewStub mChildrenContainerStub;
    private boolean mChildrenExpanded;
    private boolean mEnableNonGroupedNotificationExpand;
    private NotificationEntry mEntry;
    private boolean mExpandAnimationRunning;
    private boolean mExpandable;
    private boolean mExpandedWhenPinned;
    private Path mExpandingClipPath;
    private OnExpansionChangedListener mExpansionChangedListener;
    private FalsingCollector mFalsingCollector;
    private FalsingManager mFalsingManager;
    private boolean mGroupExpansionChanging;
    private GroupExpansionManager mGroupExpansionManager;
    private GroupMembershipManager mGroupMembershipManager;
    private View mGroupParentWhenDismissed;
    private NotificationGuts mGuts;
    private ViewStub mGutsStub;
    private boolean mHasUserChangedExpansion;
    private Consumer<Boolean> mHeadsUpAnimatingAwayListener;
    private HeadsUpManager mHeadsUpManager;
    private boolean mHeadsupDisappearRunning;
    private boolean mHideSensitiveForIntrinsicHeight;
    private boolean mIconAnimationRunning;
    private int mIconTransformContentShift;
    private NotificationInlineImageResolver mImageResolver;
    private int mIncreasedPaddingBetweenElements;
    private boolean mIsBlockingHelperShowing;
    private boolean mIsHeadsUp;
    private boolean mIsLowPriority;
    private boolean mIsPinned;
    private boolean mIsSnoozed;
    private boolean mIsSummaryWithChildren;
    private boolean mIsSystemChildExpanded;
    private boolean mIsSystemExpanded;
    private boolean mJustClicked;
    private boolean mKeepInParent;
    private LayoutListener mLayoutListener;
    private NotificationContentView[] mLayouts;
    private ExpansionLogger mLogger;
    private String mLoggingKey;
    private LongPressListener mLongPressListener;
    private int mMaxExpandedHeight;
    private int mMaxHeadsUpHeight;
    private int mMaxHeadsUpHeightBeforeN;
    private int mMaxHeadsUpHeightBeforeP;
    private int mMaxHeadsUpHeightBeforeS;
    private int mMaxHeadsUpHeightIncreased;
    private int mMaxSmallHeight;
    private int mMaxSmallHeightBeforeN;
    private int mMaxSmallHeightBeforeP;
    private int mMaxSmallHeightBeforeS;
    private int mMaxSmallHeightLarge;
    private int mMaxSmallHeightMedia;
    private NotificationMediaManager mMediaManager;
    private NotificationMenuRowPlugin mMenuRow;
    private boolean mMustStayOnScreen;
    private boolean mNeedsRedaction;
    private int mNotificationColor;
    private NotificationGutsManager mNotificationGutsManager;
    private int mNotificationLaunchHeight;
    private ExpandableNotificationRow mNotificationParent;
    private View.OnClickListener mOnClickListener;
    private OnExpandClickListener mOnExpandClickListener;
    private View.OnClickListener mOnFeedbackClickListener;
    private Runnable mOnIntrinsicHeightReachedRunnable;
    private boolean mOnKeyguard;
    private OnUserInteractionCallback mOnUserInteractionCallback;
    private PeopleNotificationIdentifier mPeopleNotificationIdentifier;
    private NotificationContentView mPrivateLayout;
    private NotificationContentView mPublicLayout;
    private boolean mRemoved;
    private RowContentBindStage mRowContentBindStage;
    private BooleanSupplier mSecureStateProvider;
    private boolean mSensitive;
    private boolean mSensitiveHiddenInGeneral;
    private boolean mShowGroupBackgroundWhenExpanded;
    private boolean mShowNoBackground;
    private boolean mShowingPublic;
    private boolean mShowingPublicInitialized;
    private StatusBarStateController mStatusBarStateController;
    private float mTopRoundnessDuringExpandAnimation;
    private Animator mTranslateAnim;
    private ArrayList<View> mTranslateableViews;
    private float mTranslationWhenRemoved;
    private boolean mUpdateBackgroundOnUpdate;
    private boolean mUseIncreasedCollapsedHeight;
    private boolean mUseIncreasedHeadsUpHeight;
    private boolean mUserExpanded;
    private boolean mUserLocked;
    private boolean mWasChildInGroupWhenRemoved;
    private boolean mNotificationTranslationFinished = false;
    private float mHeaderVisibleAmount = 1.0f;
    private boolean mLastChronometerRunning = true;
    private View.OnClickListener mExpandClickListener = new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            boolean z;
            if (!ExpandableNotificationRow.this.shouldShowPublic() && ((!ExpandableNotificationRow.this.mIsLowPriority || ExpandableNotificationRow.this.isExpanded()) && ExpandableNotificationRow.this.mGroupMembershipManager.isGroupSummary(ExpandableNotificationRow.this.mEntry))) {
                ExpandableNotificationRow.this.mGroupExpansionChanging = true;
                boolean isGroupExpanded = ExpandableNotificationRow.this.mGroupExpansionManager.isGroupExpanded(ExpandableNotificationRow.this.mEntry);
                boolean z2 = ExpandableNotificationRow.this.mGroupExpansionManager.toggleGroupExpansion(ExpandableNotificationRow.this.mEntry);
                ExpandableNotificationRow.this.mOnExpandClickListener.onExpandClicked(ExpandableNotificationRow.this.mEntry, view, z2);
                MetricsLogger.action(((FrameLayout) ExpandableNotificationRow.this).mContext, 408, z2);
                ExpandableNotificationRow.this.onExpansionChanged(true, isGroupExpanded);
            } else if (ExpandableNotificationRow.this.mEnableNonGroupedNotificationExpand) {
                if (view.isAccessibilityFocused()) {
                    ExpandableNotificationRow.this.mPrivateLayout.setFocusOnVisibilityChange();
                }
                if (ExpandableNotificationRow.this.isPinned()) {
                    z = !ExpandableNotificationRow.this.mExpandedWhenPinned;
                    ExpandableNotificationRow.this.mExpandedWhenPinned = z;
                    if (ExpandableNotificationRow.this.mExpansionChangedListener != null) {
                        ExpandableNotificationRow.this.mExpansionChangedListener.onExpansionChanged(z);
                    }
                } else {
                    z = !ExpandableNotificationRow.this.isExpanded();
                    ExpandableNotificationRow.this.setUserExpanded(z);
                }
                ExpandableNotificationRow.this.notifyHeightChanged(true);
                ExpandableNotificationRow.this.mOnExpandClickListener.onExpandClicked(ExpandableNotificationRow.this.mEntry, view, z);
                MetricsLogger.action(((FrameLayout) ExpandableNotificationRow.this).mContext, 407, z);
            }
        }
    };
    private SystemNotificationAsyncTask mSystemNotificationAsyncTask = new SystemNotificationAsyncTask();
    private final Runnable mExpireRecentlyAlertedFlag = new Runnable() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$$ExternalSyntheticLambda6
        @Override // java.lang.Runnable
        public final void run() {
            ExpandableNotificationRow.this.lambda$new$2();
        }
    };

    /* loaded from: classes.dex */
    public interface CoordinateOnClickListener {
        boolean onClick(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem);
    }

    /* loaded from: classes.dex */
    public interface ExpansionLogger {
        void logNotificationExpansion(String str, boolean z, boolean z2);
    }

    /* loaded from: classes.dex */
    public interface LayoutListener {
        void onLayout();
    }

    /* loaded from: classes.dex */
    public interface LongPressListener {
        boolean onLongPress(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem);
    }

    /* loaded from: classes.dex */
    public interface OnExpandClickListener {
        void onExpandClicked(NotificationEntry notificationEntry, View view, boolean z);
    }

    /* loaded from: classes.dex */
    public interface OnExpansionChangedListener {
        void onExpansionChanged(boolean z);
    }

    /* access modifiers changed from: private */
    public static Boolean isSystemNotification(Context context, StatusBarNotification statusBarNotification) {
        PackageManager packageManagerForUser = StatusBar.getPackageManagerForUser(context, statusBarNotification.getUser().getIdentifier());
        try {
            return Boolean.valueOf(Utils.isSystemPackage(context.getResources(), packageManagerForUser, packageManagerForUser.getPackageInfo(statusBarNotification.getPackageName(), 64)));
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("ExpandableNotifRow", "cacheIsSystemNotification: Could not find package info");
            return null;
        }
    }

    public NotificationContentView[] getLayouts() {
        NotificationContentView[] notificationContentViewArr = this.mLayouts;
        return (NotificationContentView[]) Arrays.copyOf(notificationContentViewArr, notificationContentViewArr.length);
    }

    public boolean isPinnedAndExpanded() {
        if (!isPinned()) {
            return false;
        }
        return this.mExpandedWhenPinned;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean isGroupExpansionChanging() {
        if (isChildInGroup()) {
            return this.mNotificationParent.isGroupExpansionChanging();
        }
        return this.mGroupExpansionChanging;
    }

    public void setGroupExpansionChanging(boolean z) {
        this.mGroupExpansionChanging = z;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void setActualHeightAnimating(boolean z) {
        NotificationContentView notificationContentView = this.mPrivateLayout;
        if (notificationContentView != null) {
            notificationContentView.setContentHeightAnimating(z);
        }
    }

    public NotificationContentView getPrivateLayout() {
        return this.mPrivateLayout;
    }

    public NotificationContentView getPublicLayout() {
        return this.mPublicLayout;
    }

    public void setIconAnimationRunning(boolean z) {
        for (NotificationContentView notificationContentView : this.mLayouts) {
            setIconAnimationRunning(z, notificationContentView);
        }
        if (this.mIsSummaryWithChildren) {
            NotificationViewWrapper notificationViewWrapper = this.mChildrenContainer.getNotificationViewWrapper();
            if (notificationViewWrapper != null) {
                setIconAnimationRunningForChild(z, notificationViewWrapper.getIcon());
            }
            NotificationViewWrapper lowPriorityViewWrapper = this.mChildrenContainer.getLowPriorityViewWrapper();
            if (lowPriorityViewWrapper != null) {
                setIconAnimationRunningForChild(z, lowPriorityViewWrapper.getIcon());
            }
            List<ExpandableNotificationRow> attachedChildren = this.mChildrenContainer.getAttachedChildren();
            for (int i = 0; i < attachedChildren.size(); i++) {
                attachedChildren.get(i).setIconAnimationRunning(z);
            }
        }
        this.mIconAnimationRunning = z;
    }

    private void setIconAnimationRunning(boolean z, NotificationContentView notificationContentView) {
        if (notificationContentView != null) {
            View contractedChild = notificationContentView.getContractedChild();
            View expandedChild = notificationContentView.getExpandedChild();
            View headsUpChild = notificationContentView.getHeadsUpChild();
            setIconAnimationRunningForChild(z, contractedChild);
            setIconAnimationRunningForChild(z, expandedChild);
            setIconAnimationRunningForChild(z, headsUpChild);
        }
    }

    private void setIconAnimationRunningForChild(boolean z, View view) {
        if (view != null) {
            setIconRunning((ImageView) view.findViewById(16908294), z);
            setIconRunning((ImageView) view.findViewById(16909380), z);
        }
    }

    private void setIconRunning(ImageView imageView, boolean z) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
                if (z) {
                    animationDrawable.start();
                } else {
                    animationDrawable.stop();
                }
            } else if (drawable instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                if (z) {
                    animatedVectorDrawable.start();
                } else {
                    animatedVectorDrawable.stop();
                }
            }
        }
    }

    private void cacheIsSystemNotification() {
        NotificationEntry notificationEntry = this.mEntry;
        if (notificationEntry != null && notificationEntry.mIsSystemNotification == null && this.mSystemNotificationAsyncTask.getStatus() == AsyncTask.Status.PENDING) {
            this.mSystemNotificationAsyncTask.execute(new Void[0]);
        }
    }

    public boolean getIsNonblockable() {
        NotificationEntry notificationEntry;
        Boolean bool;
        NotificationEntry notificationEntry2 = this.mEntry;
        if (notificationEntry2 != null && notificationEntry2.mIsSystemNotification == null) {
            this.mSystemNotificationAsyncTask.cancel(true);
            NotificationEntry notificationEntry3 = this.mEntry;
            notificationEntry3.mIsSystemNotification = isSystemNotification(((FrameLayout) this).mContext, notificationEntry3.getSbn());
        }
        boolean z = this.mEntry.getChannel().isImportanceLockedByOEM() || this.mEntry.getChannel().isImportanceLockedByCriticalDeviceFunction();
        if (z || (notificationEntry = this.mEntry) == null || (bool = notificationEntry.mIsSystemNotification) == null || !bool.booleanValue() || this.mEntry.getChannel() == null || this.mEntry.getChannel().isBlockable()) {
            return z;
        }
        return true;
    }

    private boolean isConversation() {
        return this.mPeopleNotificationIdentifier.getPeopleNotificationType(this.mEntry) != 0;
    }

    public void onNotificationUpdated() {
        for (NotificationContentView notificationContentView : this.mLayouts) {
            notificationContentView.onNotificationUpdated(this.mEntry);
        }
        this.mShowingPublicInitialized = false;
        updateNotificationColor();
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin != null) {
            notificationMenuRowPlugin.onNotificationUpdated(this.mEntry.getSbn());
            this.mMenuRow.setAppName(this.mAppName);
        }
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.recreateNotificationHeader(this.mExpandClickListener, isConversation());
            this.mChildrenContainer.onNotificationUpdated();
        }
        if (this.mIconAnimationRunning) {
            setIconAnimationRunning(true);
        }
        if (this.mLastChronometerRunning) {
            setChronometerRunning(true);
        }
        ExpandableNotificationRow expandableNotificationRow = this.mNotificationParent;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.updateChildrenAppearance();
        }
        onAttachedChildrenCountChanged();
        this.mPublicLayout.updateExpandButtons(true);
        updateLimits();
        updateShelfIconColor();
        updateRippleAllowed();
        if (this.mUpdateBackgroundOnUpdate) {
            this.mUpdateBackgroundOnUpdate = false;
            updateBackgroundColors();
        }
    }

    public void onNotificationRankingUpdated() {
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin != null) {
            notificationMenuRowPlugin.onNotificationUpdated(this.mEntry.getSbn());
        }
    }

    public void updateBubbleButton() {
        for (NotificationContentView notificationContentView : this.mLayouts) {
            notificationContentView.updateBubbleButton(this.mEntry);
        }
    }

    @VisibleForTesting
    void updateShelfIconColor() {
        StatusBarIconView shelfIcon = this.mEntry.getIcons().getShelfIcon();
        int i = 0;
        if (!Boolean.TRUE.equals(shelfIcon.getTag(R$id.icon_is_pre_L)) || NotificationUtils.isGrayscale(shelfIcon, ContrastColorUtil.getInstance(((FrameLayout) this).mContext))) {
            i = getOriginalIconColor();
        }
        shelfIcon.setStaticDrawableColor(i);
    }

    public int getOriginalIconColor() {
        if (this.mIsSummaryWithChildren && !shouldShowPublic()) {
            return this.mChildrenContainer.getVisibleWrapper().getOriginalIconColor();
        }
        int originalIconColor = getShowingLayout().getOriginalIconColor();
        boolean z = true;
        if (originalIconColor != 1) {
            return originalIconColor;
        }
        NotificationEntry notificationEntry = this.mEntry;
        Context context = ((FrameLayout) this).mContext;
        if (!this.mIsLowPriority || isExpanded()) {
            z = false;
        }
        return notificationEntry.getContrastedColor(context, z, getBackgroundColorWithoutTint());
    }

    public void setAboveShelfChangedListener(AboveShelfChangedListener aboveShelfChangedListener) {
        this.mAboveShelfChangedListener = aboveShelfChangedListener;
    }

    public void setSecureStateProvider(BooleanSupplier booleanSupplier) {
        this.mSecureStateProvider = booleanSupplier;
    }

    private void updateLimits() {
        for (NotificationContentView notificationContentView : this.mLayouts) {
            updateLimitsForView(notificationContentView);
        }
    }

    private void updateLimitsForView(NotificationContentView notificationContentView) {
        int i;
        int i2;
        View contractedChild = notificationContentView.getContractedChild();
        boolean z = true;
        boolean z2 = (contractedChild == null || contractedChild.getId() == 16909496) ? false : true;
        int i3 = this.mEntry.targetSdk;
        boolean z3 = i3 < 24;
        boolean z4 = i3 < 28;
        boolean z5 = i3 < 31;
        boolean z6 = contractedChild instanceof CallLayout;
        if (!z2 || !z5 || this.mIsSummaryWithChildren) {
            if (z6) {
                i = this.mMaxExpandedHeight;
            } else if (!this.mUseIncreasedCollapsedHeight || notificationContentView != this.mPrivateLayout) {
                i = this.mMaxSmallHeight;
            } else {
                i = this.mMaxSmallHeightLarge;
            }
        } else if (z3) {
            i = this.mMaxSmallHeightBeforeN;
        } else if (z4) {
            i = this.mMaxSmallHeightBeforeP;
        } else {
            i = this.mMaxSmallHeightBeforeS;
        }
        if (notificationContentView.getHeadsUpChild() == null || notificationContentView.getHeadsUpChild().getId() == 16909496) {
            z = false;
        }
        if (!z || !z5) {
            if (!this.mUseIncreasedHeadsUpHeight || notificationContentView != this.mPrivateLayout) {
                i2 = this.mMaxHeadsUpHeight;
            } else {
                i2 = this.mMaxHeadsUpHeightIncreased;
            }
        } else if (z3) {
            i2 = this.mMaxHeadsUpHeightBeforeN;
        } else if (z4) {
            i2 = this.mMaxHeadsUpHeightBeforeP;
        } else {
            i2 = this.mMaxHeadsUpHeightBeforeS;
        }
        NotificationViewWrapper visibleWrapper = notificationContentView.getVisibleWrapper(2);
        if (visibleWrapper != null) {
            i2 = Math.max(i2, visibleWrapper.getMinLayoutHeight());
        }
        notificationContentView.setHeights(i, i2, this.mMaxExpandedHeight);
    }

    public NotificationEntry getEntry() {
        return this.mEntry;
    }

    public boolean isHeadsUp() {
        return this.mIsHeadsUp;
    }

    public void setHeadsUp(boolean z) {
        boolean isAboveShelf = isAboveShelf();
        int intrinsicHeight = getIntrinsicHeight();
        this.mIsHeadsUp = z;
        this.mPrivateLayout.setHeadsUp(z);
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.updateGroupOverflow();
        }
        if (intrinsicHeight != getIntrinsicHeight()) {
            notifyHeightChanged(false);
        }
        if (z) {
            this.mMustStayOnScreen = true;
            setAboveShelf(true);
        } else if (isAboveShelf() != isAboveShelf) {
            this.mAboveShelfChangedListener.onAboveShelfStateChanged(!isAboveShelf);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean showingPulsing() {
        return isHeadsUpState() && (isDozing() || (this.mOnKeyguard && isBypassEnabled()));
    }

    public boolean isHeadsUpState() {
        return this.mIsHeadsUp || this.mHeadsupDisappearRunning;
    }

    public void setRemoteInputController(RemoteInputController remoteInputController) {
        this.mPrivateLayout.setRemoteInputController(remoteInputController);
    }

    /* access modifiers changed from: package-private */
    public String getAppName() {
        return this.mAppName;
    }

    public void addChildNotification(ExpandableNotificationRow expandableNotificationRow) {
        addChildNotification(expandableNotificationRow, -1);
    }

    public void setHeaderVisibleAmount(float f) {
        if (this.mHeaderVisibleAmount != f) {
            this.mHeaderVisibleAmount = f;
            for (NotificationContentView notificationContentView : this.mLayouts) {
                notificationContentView.setHeaderVisibleAmount(f);
            }
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            if (notificationChildrenContainer != null) {
                notificationChildrenContainer.setHeaderVisibleAmount(f);
            }
            notifyHeightChanged(false);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public float getHeaderVisibleAmount() {
        return this.mHeaderVisibleAmount;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void setHeadsUpIsVisible() {
        super.setHeadsUpIsVisible();
        this.mMustStayOnScreen = false;
    }

    public void setUntruncatedChildCount(int i) {
        if (this.mChildrenContainer == null) {
            this.mChildrenContainerStub.inflate();
        }
        this.mChildrenContainer.setUntruncatedChildCount(i);
    }

    public void addChildNotification(ExpandableNotificationRow expandableNotificationRow, int i) {
        if (this.mChildrenContainer == null) {
            this.mChildrenContainerStub.inflate();
        }
        this.mChildrenContainer.addNotification(expandableNotificationRow, i);
        onAttachedChildrenCountChanged();
        expandableNotificationRow.setIsChildInGroup(true, this);
    }

    public void removeChildNotification(ExpandableNotificationRow expandableNotificationRow) {
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.removeNotification(expandableNotificationRow);
        }
        onAttachedChildrenCountChanged();
        expandableNotificationRow.setIsChildInGroup(false, null);
        expandableNotificationRow.setBottomRoundness(0.0f, false);
    }

    public ExpandableNotificationRow getChildNotificationAt(int i) {
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer == null || notificationChildrenContainer.getAttachedChildren().size() <= i) {
            return null;
        }
        return this.mChildrenContainer.getAttachedChildren().get(i);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean isChildInGroup() {
        return this.mNotificationParent != null;
    }

    public ExpandableNotificationRow getNotificationParent() {
        return this.mNotificationParent;
    }

    public void setIsChildInGroup(boolean z, ExpandableNotificationRow expandableNotificationRow) {
        ExpandableNotificationRow expandableNotificationRow2;
        if (this.mExpandAnimationRunning && !z && (expandableNotificationRow2 = this.mNotificationParent) != null) {
            expandableNotificationRow2.setChildIsExpanding(false);
            this.mNotificationParent.setExpandingClipPath(null);
            this.mNotificationParent.setExtraWidthForClipping(0.0f);
            this.mNotificationParent.setMinimumHeightForClipping(0);
        }
        if (!z) {
            expandableNotificationRow = null;
        }
        this.mNotificationParent = expandableNotificationRow;
        this.mPrivateLayout.setIsChildInGroup(z);
        updateBackgroundForGroupState();
        updateClickAndFocus();
        if (this.mNotificationParent != null) {
            setOverrideTintColor(0, 0.0f);
            this.mNotificationParent.updateBackgroundForGroupState();
        }
        updateBackgroundClipping();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0) {
            this.mFalsingManager.isFalseTap(2);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0 || !isChildInGroup() || isGroupExpanded()) {
            return super.onTouchEvent(motionEvent);
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    protected boolean handleSlideBack() {
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin == null || !notificationMenuRowPlugin.isMenuVisible()) {
            return false;
        }
        animateResetTranslation();
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean isSummaryWithChildren() {
        return this.mIsSummaryWithChildren;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean areChildrenExpanded() {
        return this.mChildrenExpanded;
    }

    public List<ExpandableNotificationRow> getAttachedChildren() {
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer == null) {
            return null;
        }
        return notificationChildrenContainer.getAttachedChildren();
    }

    public boolean applyChildOrder(List<ExpandableNotificationRow> list, VisualStabilityManager visualStabilityManager, VisualStabilityManager.Callback callback) {
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        return notificationChildrenContainer != null && notificationChildrenContainer.applyChildOrder(list, visualStabilityManager, callback);
    }

    public void updateChildrenStates(AmbientState ambientState) {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.updateState(getViewState(), ambientState);
        }
    }

    public void applyChildrenState() {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.applyState();
        }
    }

    public void prepareExpansionChanged() {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.prepareExpansionChanged();
        }
    }

    public void startChildAnimation(AnimationProperties animationProperties) {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.startAnimationToState(animationProperties);
        }
    }

    public ExpandableNotificationRow getViewAtPosition(float f) {
        ExpandableNotificationRow viewAtPosition;
        return (!this.mIsSummaryWithChildren || !this.mChildrenExpanded || (viewAtPosition = this.mChildrenContainer.getViewAtPosition(f)) == null) ? this : viewAtPosition;
    }

    public NotificationGuts getGuts() {
        return this.mGuts;
    }

    public void setPinned(boolean z) {
        int intrinsicHeight = getIntrinsicHeight();
        boolean isAboveShelf = isAboveShelf();
        this.mIsPinned = z;
        if (intrinsicHeight != getIntrinsicHeight()) {
            notifyHeightChanged(false);
        }
        if (z) {
            setIconAnimationRunning(true);
            this.mExpandedWhenPinned = false;
        } else if (this.mExpandedWhenPinned) {
            setUserExpanded(true);
        }
        setChronometerRunning(this.mLastChronometerRunning);
        if (isAboveShelf() != isAboveShelf) {
            this.mAboveShelfChangedListener.onAboveShelfStateChanged(!isAboveShelf);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean isPinned() {
        return this.mIsPinned;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public int getPinnedHeadsUpHeight() {
        return getPinnedHeadsUpHeight(true);
    }

    private int getPinnedHeadsUpHeight(boolean z) {
        if (this.mIsSummaryWithChildren) {
            return this.mChildrenContainer.getIntrinsicHeight();
        }
        if (this.mExpandedWhenPinned) {
            return Math.max(getMaxExpandHeight(), getHeadsUpHeight());
        }
        if (z) {
            return Math.max(getCollapsedHeight(), getHeadsUpHeight());
        }
        return getHeadsUpHeight();
    }

    public void setJustClicked(boolean z) {
        this.mJustClicked = z;
    }

    public boolean wasJustClicked() {
        return this.mJustClicked;
    }

    public void setChronometerRunning(boolean z) {
        this.mLastChronometerRunning = z;
        setChronometerRunning(z, this.mPrivateLayout);
        setChronometerRunning(z, this.mPublicLayout);
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            List<ExpandableNotificationRow> attachedChildren = notificationChildrenContainer.getAttachedChildren();
            for (int i = 0; i < attachedChildren.size(); i++) {
                attachedChildren.get(i).setChronometerRunning(z);
            }
        }
    }

    private void setChronometerRunning(boolean z, NotificationContentView notificationContentView) {
        if (notificationContentView != null) {
            boolean z2 = z || isPinned();
            View contractedChild = notificationContentView.getContractedChild();
            View expandedChild = notificationContentView.getExpandedChild();
            View headsUpChild = notificationContentView.getHeadsUpChild();
            setChronometerRunningForChild(z2, contractedChild);
            setChronometerRunningForChild(z2, expandedChild);
            setChronometerRunningForChild(z2, headsUpChild);
        }
    }

    private void setChronometerRunningForChild(boolean z, View view) {
        if (view != null) {
            View findViewById = view.findViewById(16908847);
            if (findViewById instanceof Chronometer) {
                ((Chronometer) findViewById).setStarted(z);
            }
        }
    }

    public NotificationViewWrapper getNotificationViewWrapper() {
        if (this.mIsSummaryWithChildren) {
            return this.mChildrenContainer.getNotificationViewWrapper();
        }
        return this.mPrivateLayout.getNotificationViewWrapper();
    }

    public NotificationViewWrapper getVisibleNotificationViewWrapper() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout().getVisibleWrapper();
        }
        return this.mChildrenContainer.getVisibleWrapper();
    }

    public void setLongPressListener(LongPressListener longPressListener) {
        this.mLongPressListener = longPressListener;
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        this.mOnClickListener = onClickListener;
        updateClickAndFocus();
    }

    public View.OnClickListener getBubbleClickListener() {
        return new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ExpandableNotificationRow.this.lambda$getBubbleClickListener$0(view);
            }
        };
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getBubbleClickListener$0(View view) {
        if (this.mBubblesManagerOptional.isPresent()) {
            NotificationEntry notificationEntry = this.mEntry;
            this.mBubblesManagerOptional.get().onUserChangedBubble(notificationEntry, !notificationEntry.isBubble());
        }
        this.mHeadsUpManager.removeNotification(this.mEntry.getKey(), true);
    }

    public View.OnClickListener getSnoozeClickListener(NotificationMenuRowPlugin.MenuItem menuItem) {
        return new View.OnClickListener(menuItem) { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$$ExternalSyntheticLambda1
            public final /* synthetic */ NotificationMenuRowPlugin.MenuItem f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ExpandableNotificationRow.this.lambda$getSnoozeClickListener$1(this.f$1, view);
            }
        };
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getSnoozeClickListener$1(NotificationMenuRowPlugin.MenuItem menuItem, View view) {
        this.mNotificationGutsManager.closeAndSaveGuts(true, false, false, -1, -1, false);
        this.mNotificationGutsManager.openGuts(this, 0, 0, menuItem);
        this.mIsSnoozed = true;
    }

    private void updateClickAndFocus() {
        boolean z = false;
        boolean z2 = !isChildInGroup() || isGroupExpanded();
        if (this.mOnClickListener != null && z2) {
            z = true;
        }
        if (isFocusable() != z2) {
            setFocusable(z2);
        }
        if (isClickable() != z) {
            setClickable(z);
        }
    }

    public void setGutsView(NotificationMenuRowPlugin.MenuItem menuItem) {
        if (getGuts() != null && (menuItem.getGutsView() instanceof NotificationGuts.GutsContent)) {
            getGuts().setGutsContent((NotificationGuts.GutsContent) menuItem.getGutsView());
        }
    }

    public void onPluginConnected(NotificationMenuRowPlugin notificationMenuRowPlugin, Context context) {
        NotificationMenuRowPlugin notificationMenuRowPlugin2 = this.mMenuRow;
        boolean z = (notificationMenuRowPlugin2 == null || notificationMenuRowPlugin2.getMenuView() == null) ? false : true;
        if (z) {
            removeView(this.mMenuRow.getMenuView());
        }
        if (notificationMenuRowPlugin != null) {
            this.mMenuRow = notificationMenuRowPlugin;
            if (notificationMenuRowPlugin.shouldUseDefaultMenuItems()) {
                ArrayList<NotificationMenuRowPlugin.MenuItem> arrayList = new ArrayList<>();
                arrayList.add(NotificationMenuRow.createConversationItem(((FrameLayout) this).mContext));
                arrayList.add(NotificationMenuRow.createPartialConversationItem(((FrameLayout) this).mContext));
                arrayList.add(NotificationMenuRow.createInfoItem(((FrameLayout) this).mContext));
                arrayList.add(NotificationMenuRow.createSnoozeItem(((FrameLayout) this).mContext));
                this.mMenuRow.setMenuItems(arrayList);
            }
            if (z) {
                createMenu();
            }
        }
    }

    public void onPluginDisconnected(NotificationMenuRowPlugin notificationMenuRowPlugin) {
        boolean z = this.mMenuRow.getMenuView() != null;
        this.mMenuRow = new NotificationMenuRow(((FrameLayout) this).mContext, this.mPeopleNotificationIdentifier);
        if (z) {
            createMenu();
        }
    }

    @Override // com.android.systemui.statusbar.notification.stack.SwipeableView
    public boolean hasFinishedInitialization() {
        return getEntry().hasFinishedInitialization();
    }

    @Override // com.android.systemui.statusbar.notification.stack.SwipeableView
    public NotificationMenuRowPlugin createMenu() {
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin == null) {
            return null;
        }
        if (notificationMenuRowPlugin.getMenuView() == null) {
            this.mMenuRow.createMenu(this, this.mEntry.getSbn());
            this.mMenuRow.setAppName(this.mAppName);
            addView(this.mMenuRow.getMenuView(), 0, new FrameLayout.LayoutParams(-1, -1));
        }
        return this.mMenuRow;
    }

    public NotificationMenuRowPlugin getProvider() {
        return this.mMenuRow;
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public void onDensityOrFontScaleChanged() {
        super.onDensityOrFontScaleChanged();
        initDimens();
        initBackground();
        reInflateViews();
    }

    private void reInflateViews() {
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.reInflateViews(this.mExpandClickListener, this.mEntry.getSbn());
        }
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts != null) {
            int indexOfChild = indexOfChild(notificationGuts);
            removeView(notificationGuts);
            NotificationGuts notificationGuts2 = (NotificationGuts) LayoutInflater.from(((FrameLayout) this).mContext).inflate(R$layout.notification_guts, (ViewGroup) this, false);
            this.mGuts = notificationGuts2;
            notificationGuts2.setVisibility(notificationGuts.isExposed() ? 0 : 8);
            addView(this.mGuts, indexOfChild);
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        View menuView = notificationMenuRowPlugin == null ? null : notificationMenuRowPlugin.getMenuView();
        if (menuView != null) {
            int indexOfChild2 = indexOfChild(menuView);
            removeView(menuView);
            this.mMenuRow.createMenu(this, this.mEntry.getSbn());
            this.mMenuRow.setAppName(this.mAppName);
            addView(this.mMenuRow.getMenuView(), indexOfChild2);
        }
        NotificationContentView[] notificationContentViewArr = this.mLayouts;
        for (NotificationContentView notificationContentView : notificationContentViewArr) {
            notificationContentView.initView();
            notificationContentView.reInflateViews();
        }
        this.mEntry.getSbn().clearPackageContext();
        this.mRowContentBindStage.getStageParams(this.mEntry).setNeedsReinflation(true);
        this.mRowContentBindStage.requestRebind(this.mEntry, null);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (!(notificationMenuRowPlugin == null || notificationMenuRowPlugin.getMenuView() == null)) {
            this.mMenuRow.onConfigurationChanged();
        }
        NotificationInlineImageResolver notificationInlineImageResolver = this.mImageResolver;
        if (notificationInlineImageResolver != null) {
            notificationInlineImageResolver.updateMaxImageSizes();
        }
    }

    public void onUiModeChanged() {
        this.mUpdateBackgroundOnUpdate = true;
        reInflateViews();
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            for (ExpandableNotificationRow expandableNotificationRow : notificationChildrenContainer.getAttachedChildren()) {
                expandableNotificationRow.onUiModeChanged();
            }
        }
    }

    public void setContentBackground(int i, boolean z, NotificationContentView notificationContentView) {
        if (getShowingLayout() == notificationContentView) {
            setTintColor(i, z);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    protected void setBackgroundTintColor(int i) {
        super.setBackgroundTintColor(i);
        NotificationContentView showingLayout = getShowingLayout();
        if (showingLayout != null) {
            showingLayout.setBackgroundTintColor(i);
        }
    }

    public void closeRemoteInput() {
        for (NotificationContentView notificationContentView : this.mLayouts) {
            notificationContentView.closeRemoteInput();
        }
    }

    public void setSingleLineWidthIndention(int i) {
        this.mPrivateLayout.setSingleLineWidthIndention(i);
    }

    public int getNotificationColor() {
        return this.mNotificationColor;
    }

    public void updateNotificationColor() {
        this.mNotificationColor = ContrastColorUtil.resolveContrastColor(((FrameLayout) this).mContext, this.mEntry.getSbn().getNotification().color, getBackgroundColorWithoutTint(), (getResources().getConfiguration().uiMode & 48) == 32);
    }

    public HybridNotificationView getSingleLineView() {
        return this.mPrivateLayout.getSingleLineView();
    }

    public boolean isOnKeyguard() {
        return this.mOnKeyguard;
    }

    public void removeAllChildren() {
        ArrayList arrayList = new ArrayList(this.mChildrenContainer.getAttachedChildren());
        for (int i = 0; i < arrayList.size(); i++) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) arrayList.get(i);
            if (!expandableNotificationRow.keepInParent()) {
                this.mChildrenContainer.removeNotification(expandableNotificationRow);
                expandableNotificationRow.setIsChildInGroup(false, null);
            }
        }
        onAttachedChildrenCountChanged();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    public void dismiss(boolean z) {
        List<ExpandableNotificationRow> attachedChildren;
        int indexOf;
        super.dismiss(z);
        setLongPressListener(null);
        this.mGroupParentWhenDismissed = this.mNotificationParent;
        this.mChildAfterViewWhenDismissed = null;
        this.mEntry.getIcons().getStatusBarIcon().setDismissed();
        if (isChildInGroup() && (indexOf = (attachedChildren = this.mNotificationParent.getAttachedChildren()).indexOf(this)) != -1 && indexOf < attachedChildren.size() - 1) {
            this.mChildAfterViewWhenDismissed = attachedChildren.get(indexOf + 1);
        }
    }

    public boolean keepInParent() {
        return this.mKeepInParent;
    }

    public void setKeepInParent(boolean z) {
        this.mKeepInParent = z;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, com.android.systemui.statusbar.notification.stack.SwipeableView
    public boolean isRemoved() {
        return this.mRemoved;
    }

    public void setRemoved() {
        this.mRemoved = true;
        this.mTranslationWhenRemoved = getTranslationY();
        this.mWasChildInGroupWhenRemoved = isChildInGroup();
        if (isChildInGroup()) {
            this.mTranslationWhenRemoved += getNotificationParent().getTranslationY();
        }
        for (NotificationContentView notificationContentView : this.mLayouts) {
            notificationContentView.setRemoved();
        }
    }

    public boolean wasChildInGroupWhenRemoved() {
        return this.mWasChildInGroupWhenRemoved;
    }

    public float getTranslationWhenRemoved() {
        return this.mTranslationWhenRemoved;
    }

    public NotificationChildrenContainer getChildrenContainer() {
        return this.mChildrenContainer;
    }

    public void setHeadsUpAnimatingAway(boolean z) {
        Consumer<Boolean> consumer;
        boolean isAboveShelf = isAboveShelf();
        boolean z2 = true;
        if (z == this.mHeadsupDisappearRunning) {
            z2 = false;
        }
        this.mHeadsupDisappearRunning = z;
        this.mPrivateLayout.setHeadsUpAnimatingAway(z);
        if (z2 && (consumer = this.mHeadsUpAnimatingAwayListener) != null) {
            consumer.accept(Boolean.valueOf(z));
        }
        if (isAboveShelf() != isAboveShelf) {
            this.mAboveShelfChangedListener.onAboveShelfStateChanged(!isAboveShelf);
        }
    }

    public void setHeadsUpAnimatingAwayListener(Consumer<Boolean> consumer) {
        this.mHeadsUpAnimatingAwayListener = consumer;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean isHeadsUpAnimatingAway() {
        return this.mHeadsupDisappearRunning;
    }

    public View getChildAfterViewWhenDismissed() {
        return this.mChildAfterViewWhenDismissed;
    }

    public View getGroupParentWhenDismissed() {
        return this.mGroupParentWhenDismissed;
    }

    public void performDismiss(boolean z) {
        OnUserInteractionCallback onUserInteractionCallback;
        ((MetricsLogger) Dependency.get(MetricsLogger.class)).count("notification_dismissed", 1);
        dismiss(z);
        if (this.mEntry.isClearable() && (onUserInteractionCallback = this.mOnUserInteractionCallback) != null) {
            NotificationEntry notificationEntry = this.mEntry;
            onUserInteractionCallback.onDismiss(notificationEntry, 2, onUserInteractionCallback.getGroupSummaryToDismiss(notificationEntry));
        }
    }

    public boolean isBlockingHelperShowing() {
        return this.mIsBlockingHelperShowing;
    }

    public boolean isBlockingHelperShowingAndTranslationFinished() {
        return this.mIsBlockingHelperShowing && this.mNotificationTranslationFinished;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public View getShelfTransformationTarget() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout().getShelfTransformationTarget();
        }
        return this.mChildrenContainer.getVisibleWrapper().getShelfTransformationTarget();
    }

    public boolean isShowingIcon() {
        if (!areGutsExposed() && getShelfTransformationTarget() != null) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    protected void updateContentTransformation() {
        if (!this.mExpandAnimationRunning) {
            super.updateContentTransformation();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    protected void applyContentTransformation(float f, float f2) {
        super.applyContentTransformation(f, f2);
        if (!this.mIsLastChild) {
            f = 1.0f;
        }
        NotificationContentView[] notificationContentViewArr = this.mLayouts;
        for (NotificationContentView notificationContentView : notificationContentViewArr) {
            notificationContentView.setAlpha(f);
            notificationContentView.setTranslationY(f2);
        }
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.setAlpha(f);
            this.mChildrenContainer.setTranslationY(f2);
        }
    }

    public void setIsLowPriority(boolean z) {
        this.mIsLowPriority = z;
        this.mPrivateLayout.setIsLowPriority(z);
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.setIsLowPriority(z);
        }
    }

    public boolean isLowPriority() {
        return this.mIsLowPriority;
    }

    public void setUsesIncreasedCollapsedHeight(boolean z) {
        this.mUseIncreasedCollapsedHeight = z;
    }

    public void setUsesIncreasedHeadsUpHeight(boolean z) {
        this.mUseIncreasedHeadsUpHeight = z;
    }

    public void setNeedsRedaction(boolean z) {
        if (this.mNeedsRedaction != z) {
            this.mNeedsRedaction = z;
            if (!isRemoved()) {
                RowContentBindParams stageParams = this.mRowContentBindStage.getStageParams(this.mEntry);
                if (z) {
                    stageParams.requireContentViews(8);
                } else {
                    stageParams.markContentViewsFreeable(8);
                }
                this.mRowContentBindStage.requestRebind(this.mEntry, null);
            }
        }
    }

    public ExpandableNotificationRow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mImageResolver = new NotificationInlineImageResolver(context, new NotificationInlineImageCache());
        initDimens();
    }

    public void initialize(NotificationEntry notificationEntry, String str, String str2, ExpansionLogger expansionLogger, KeyguardBypassController keyguardBypassController, GroupMembershipManager groupMembershipManager, GroupExpansionManager groupExpansionManager, HeadsUpManager headsUpManager, RowContentBindStage rowContentBindStage, OnExpandClickListener onExpandClickListener, NotificationMediaManager notificationMediaManager, CoordinateOnClickListener coordinateOnClickListener, FalsingManager falsingManager, FalsingCollector falsingCollector, StatusBarStateController statusBarStateController, PeopleNotificationIdentifier peopleNotificationIdentifier, OnUserInteractionCallback onUserInteractionCallback, Optional<BubblesManager> optional, NotificationGutsManager notificationGutsManager) {
        this.mEntry = notificationEntry;
        this.mAppName = str;
        if (this.mMenuRow == null) {
            this.mMenuRow = new NotificationMenuRow(((FrameLayout) this).mContext, peopleNotificationIdentifier);
        }
        if (this.mMenuRow.getMenuView() != null) {
            this.mMenuRow.setAppName(this.mAppName);
        }
        this.mLogger = expansionLogger;
        this.mLoggingKey = str2;
        this.mBypassController = keyguardBypassController;
        this.mGroupMembershipManager = groupMembershipManager;
        this.mGroupExpansionManager = groupExpansionManager;
        this.mPrivateLayout.setGroupMembershipManager(groupMembershipManager);
        this.mHeadsUpManager = headsUpManager;
        this.mRowContentBindStage = rowContentBindStage;
        this.mOnExpandClickListener = onExpandClickListener;
        this.mMediaManager = notificationMediaManager;
        setOnFeedbackClickListener(coordinateOnClickListener);
        this.mFalsingManager = falsingManager;
        this.mFalsingCollector = falsingCollector;
        this.mStatusBarStateController = statusBarStateController;
        this.mPeopleNotificationIdentifier = peopleNotificationIdentifier;
        for (NotificationContentView notificationContentView : this.mLayouts) {
            notificationContentView.setPeopleNotificationIdentifier(this.mPeopleNotificationIdentifier);
        }
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mBubblesManagerOptional = optional;
        this.mNotificationGutsManager = notificationGutsManager;
        cacheIsSystemNotification();
    }

    private void initDimens() {
        this.mMaxSmallHeightBeforeN = NotificationUtils.getFontScaledHeight(((FrameLayout) this).mContext, R$dimen.notification_min_height_legacy);
        this.mMaxSmallHeightBeforeP = NotificationUtils.getFontScaledHeight(((FrameLayout) this).mContext, R$dimen.notification_min_height_before_p);
        this.mMaxSmallHeightBeforeS = NotificationUtils.getFontScaledHeight(((FrameLayout) this).mContext, R$dimen.notification_min_height_before_s);
        this.mMaxSmallHeight = NotificationUtils.getFontScaledHeight(((FrameLayout) this).mContext, R$dimen.notification_min_height);
        this.mMaxSmallHeightLarge = NotificationUtils.getFontScaledHeight(((FrameLayout) this).mContext, R$dimen.notification_min_height_increased);
        this.mMaxSmallHeightMedia = NotificationUtils.getFontScaledHeight(((FrameLayout) this).mContext, R$dimen.notification_min_height_media);
        this.mMaxExpandedHeight = NotificationUtils.getFontScaledHeight(((FrameLayout) this).mContext, R$dimen.notification_max_height);
        this.mMaxHeadsUpHeightBeforeN = NotificationUtils.getFontScaledHeight(((FrameLayout) this).mContext, R$dimen.notification_max_heads_up_height_legacy);
        this.mMaxHeadsUpHeightBeforeP = NotificationUtils.getFontScaledHeight(((FrameLayout) this).mContext, R$dimen.notification_max_heads_up_height_before_p);
        this.mMaxHeadsUpHeightBeforeS = NotificationUtils.getFontScaledHeight(((FrameLayout) this).mContext, R$dimen.notification_max_heads_up_height_before_s);
        this.mMaxHeadsUpHeight = NotificationUtils.getFontScaledHeight(((FrameLayout) this).mContext, R$dimen.notification_max_heads_up_height);
        this.mMaxHeadsUpHeightIncreased = NotificationUtils.getFontScaledHeight(((FrameLayout) this).mContext, R$dimen.notification_max_heads_up_height_increased);
        Resources resources = getResources();
        this.mEnableNonGroupedNotificationExpand = resources.getBoolean(R$bool.config_enableNonGroupedNotificationExpand);
        this.mShowGroupBackgroundWhenExpanded = resources.getBoolean(R$bool.config_showGroupNotificationBgWhenExpanded);
    }

    /* access modifiers changed from: package-private */
    public NotificationInlineImageResolver getImageResolver() {
        return this.mImageResolver;
    }

    public void reset() {
        this.mShowingPublicInitialized = false;
        unDismiss();
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin == null || !notificationMenuRowPlugin.isMenuVisible()) {
            resetTranslation();
        }
        onHeightReset();
        requestLayout();
    }

    public void showFeedbackIcon(boolean z, Pair<Integer, Integer> pair) {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.showFeedbackIcon(z, pair);
        }
        this.mPrivateLayout.showFeedbackIcon(z, pair);
        this.mPublicLayout.showFeedbackIcon(z, pair);
    }

    public void setLastAudiblyAlertedMs(long j) {
        long currentTimeMillis = System.currentTimeMillis() - j;
        long j2 = RECENTLY_ALERTED_THRESHOLD_MS;
        boolean z = currentTimeMillis < j2;
        applyAudiblyAlertedRecently(z);
        removeCallbacks(this.mExpireRecentlyAlertedFlag);
        if (z) {
            postDelayed(this.mExpireRecentlyAlertedFlag, j2 - currentTimeMillis);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        applyAudiblyAlertedRecently(false);
    }

    private void applyAudiblyAlertedRecently(boolean z) {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.setRecentlyAudiblyAlerted(z);
        }
        this.mPrivateLayout.setRecentlyAudiblyAlerted(z);
        this.mPublicLayout.setRecentlyAudiblyAlerted(z);
    }

    public View.OnClickListener getFeedbackOnClickListener() {
        return this.mOnFeedbackClickListener;
    }

    void setOnFeedbackClickListener(CoordinateOnClickListener coordinateOnClickListener) {
        this.mOnFeedbackClickListener = new View.OnClickListener(coordinateOnClickListener) { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$$ExternalSyntheticLambda2
            public final /* synthetic */ ExpandableNotificationRow.CoordinateOnClickListener f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ExpandableNotificationRow.this.lambda$setOnFeedbackClickListener$3(this.f$1, view);
            }
        };
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setOnFeedbackClickListener$3(CoordinateOnClickListener coordinateOnClickListener, View view) {
        NotificationMenuRowPlugin.MenuItem feedbackMenuItem;
        createMenu();
        NotificationMenuRowPlugin provider = getProvider();
        if (provider != null && (feedbackMenuItem = provider.getFeedbackMenuItem(((FrameLayout) this).mContext)) != null) {
            coordinateOnClickListener.onClick(this, view.getWidth() / 2, view.getHeight() / 2, feedbackMenuItem);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mPublicLayout = (NotificationContentView) findViewById(R$id.expandedPublic);
        NotificationContentView notificationContentView = (NotificationContentView) findViewById(R$id.expanded);
        this.mPrivateLayout = notificationContentView;
        NotificationContentView[] notificationContentViewArr = {notificationContentView, this.mPublicLayout};
        this.mLayouts = notificationContentViewArr;
        for (NotificationContentView notificationContentView2 : notificationContentViewArr) {
            notificationContentView2.setExpandClickListener(this.mExpandClickListener);
            notificationContentView2.setContainingNotification(this);
        }
        ViewStub viewStub = (ViewStub) findViewById(R$id.notification_guts_stub);
        this.mGutsStub = viewStub;
        viewStub.setOnInflateListener(new ViewStub.OnInflateListener() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$$ExternalSyntheticLambda3
            @Override // android.view.ViewStub.OnInflateListener
            public final void onInflate(ViewStub viewStub2, View view) {
                ExpandableNotificationRow.this.lambda$onFinishInflate$4(viewStub2, view);
            }
        });
        ViewStub viewStub2 = (ViewStub) findViewById(R$id.child_container_stub);
        this.mChildrenContainerStub = viewStub2;
        viewStub2.setOnInflateListener(new ViewStub.OnInflateListener() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$$ExternalSyntheticLambda4
            @Override // android.view.ViewStub.OnInflateListener
            public final void onInflate(ViewStub viewStub3, View view) {
                ExpandableNotificationRow.this.lambda$onFinishInflate$5(viewStub3, view);
            }
        });
        this.mTranslateableViews = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            this.mTranslateableViews.add(getChildAt(i));
        }
        this.mTranslateableViews.remove(this.mChildrenContainerStub);
        this.mTranslateableViews.remove(this.mGutsStub);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$4(ViewStub viewStub, View view) {
        NotificationGuts notificationGuts = (NotificationGuts) view;
        this.mGuts = notificationGuts;
        notificationGuts.setClipTopAmount(getClipTopAmount());
        this.mGuts.setActualHeight(getActualHeight());
        this.mGutsStub = null;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFinishInflate$5(ViewStub viewStub, View view) {
        NotificationChildrenContainer notificationChildrenContainer = (NotificationChildrenContainer) view;
        this.mChildrenContainer = notificationChildrenContainer;
        notificationChildrenContainer.setIsLowPriority(this.mIsLowPriority);
        this.mChildrenContainer.setContainingNotification(this);
        this.mChildrenContainer.onNotificationUpdated();
        this.mTranslateableViews.add(this.mChildrenContainer);
    }

    private void doLongClickCallback() {
        doLongClickCallback(getWidth() / 2, getHeight() / 2);
    }

    public void doLongClickCallback(int i, int i2) {
        createMenu();
        NotificationMenuRowPlugin provider = getProvider();
        doLongClickCallback(i, i2, provider != null ? provider.getLongpressMenuItem(((FrameLayout) this).mContext) : null);
    }

    public void doSmartActionClick(int i, int i2, int i3) {
        createMenu();
        NotificationMenuRowPlugin provider = getProvider();
        NotificationMenuRowPlugin.MenuItem longpressMenuItem = provider != null ? provider.getLongpressMenuItem(((FrameLayout) this).mContext) : null;
        if (11 == i3 && (longpressMenuItem.getGutsView() instanceof NotificationConversationInfo)) {
            ((NotificationConversationInfo) longpressMenuItem.getGutsView()).setSelectedAction(2);
        }
        doLongClickCallback(i, i2, longpressMenuItem);
    }

    private void doLongClickCallback(int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        LongPressListener longPressListener = this.mLongPressListener;
        if (longPressListener != null && menuItem != null) {
            longPressListener.onLongPress(this, i, i2, menuItem);
        }
    }

    @Override // android.view.KeyEvent.Callback, android.view.View
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (!KeyEvent.isConfirmKey(i)) {
            return super.onKeyDown(i, keyEvent);
        }
        keyEvent.startTracking();
        return true;
    }

    @Override // android.view.KeyEvent.Callback, android.view.View
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (!KeyEvent.isConfirmKey(i)) {
            return super.onKeyUp(i, keyEvent);
        }
        if (keyEvent.isCanceled()) {
            return true;
        }
        performClick();
        return true;
    }

    @Override // android.view.KeyEvent.Callback, android.view.View
    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        if (!KeyEvent.isConfirmKey(i)) {
            return false;
        }
        doLongClickCallback();
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.stack.SwipeableView
    public void resetTranslation() {
        Animator animator = this.mTranslateAnim;
        if (animator != null) {
            animator.cancel();
        }
        if (this.mDismissUsingRowTranslationX) {
            setTranslationX(0.0f);
        } else if (this.mTranslateableViews != null) {
            for (int i = 0; i < this.mTranslateableViews.size(); i++) {
                this.mTranslateableViews.get(i).setTranslationX(0.0f);
            }
            invalidateOutline();
            getEntry().getIcons().getShelfIcon().setScrollX(0);
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin != null) {
            notificationMenuRowPlugin.resetMenu();
        }
    }

    /* access modifiers changed from: package-private */
    public void onGutsOpened() {
        resetTranslation();
        updateContentAccessibilityImportanceForGuts(false);
    }

    /* access modifiers changed from: package-private */
    public void onGutsClosed() {
        updateContentAccessibilityImportanceForGuts(true);
        this.mIsSnoozed = false;
    }

    private void updateContentAccessibilityImportanceForGuts(boolean z) {
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            updateChildAccessibilityImportance(notificationChildrenContainer, z);
        }
        NotificationContentView[] notificationContentViewArr = this.mLayouts;
        if (notificationContentViewArr != null) {
            for (NotificationContentView notificationContentView : notificationContentViewArr) {
                updateChildAccessibilityImportance(notificationContentView, z);
            }
        }
        if (z) {
            requestAccessibilityFocus();
        }
    }

    private void updateChildAccessibilityImportance(View view, boolean z) {
        view.setImportantForAccessibility(z ? 0 : 4);
    }

    public CharSequence getActiveRemoteInputText() {
        return this.mPrivateLayout.getActiveRemoteInputText();
    }

    public void animateResetTranslation() {
        Animator animator = this.mTranslateAnim;
        if (animator != null) {
            animator.cancel();
        }
        Animator translateViewAnimator = getTranslateViewAnimator(0.0f, null);
        this.mTranslateAnim = translateViewAnimator;
        if (translateViewAnimator != null) {
            translateViewAnimator.start();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public void setDismissUsingRowTranslationX(boolean z) {
        if (z != this.mDismissUsingRowTranslationX) {
            float translation = getTranslation();
            int i = (translation > 0.0f ? 1 : (translation == 0.0f ? 0 : -1));
            if (i != 0) {
                setTranslation(0.0f);
            }
            super.setDismissUsingRowTranslationX(z);
            if (i != 0) {
                setTranslation(translation);
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, com.android.systemui.statusbar.notification.stack.SwipeableView
    public void setTranslation(float f) {
        invalidate();
        if (isBlockingHelperShowingAndTranslationFinished()) {
            this.mGuts.setTranslationX(f);
            return;
        }
        if (this.mDismissUsingRowTranslationX) {
            setTranslationX(f);
        } else if (this.mTranslateableViews != null) {
            for (int i = 0; i < this.mTranslateableViews.size(); i++) {
                if (this.mTranslateableViews.get(i) != null) {
                    this.mTranslateableViews.get(i).setTranslationX(f);
                }
            }
            invalidateOutline();
            getEntry().getIcons().getShelfIcon().setScrollX((int) (-f));
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (!(notificationMenuRowPlugin == null || notificationMenuRowPlugin.getMenuView() == null)) {
            this.mMenuRow.onParentTranslationUpdate(f);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, com.android.systemui.statusbar.notification.stack.SwipeableView
    public float getTranslation() {
        if (this.mDismissUsingRowTranslationX) {
            return getTranslationX();
        }
        if (isBlockingHelperShowingAndCanTranslate()) {
            return this.mGuts.getTranslationX();
        }
        ArrayList<View> arrayList = this.mTranslateableViews;
        if (arrayList == null || arrayList.size() <= 0) {
            return 0.0f;
        }
        return this.mTranslateableViews.get(0).getTranslationX();
    }

    private boolean isBlockingHelperShowingAndCanTranslate() {
        return areGutsExposed() && this.mIsBlockingHelperShowing && this.mNotificationTranslationFinished;
    }

    public Animator getTranslateViewAnimator(final float f, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        Animator animator = this.mTranslateAnim;
        if (animator != null) {
            animator.cancel();
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, TRANSLATE_CONTENT, f);
        if (animatorUpdateListener != null) {
            ofFloat.addUpdateListener(animatorUpdateListener);
        }
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.3
            boolean cancelled = false;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator2) {
                this.cancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator2) {
                if (ExpandableNotificationRow.this.mIsBlockingHelperShowing) {
                    ExpandableNotificationRow.this.mNotificationTranslationFinished = true;
                }
                if (!this.cancelled && f == 0.0f) {
                    if (ExpandableNotificationRow.this.mMenuRow != null) {
                        ExpandableNotificationRow.this.mMenuRow.resetMenu();
                    }
                    ExpandableNotificationRow.this.mTranslateAnim = null;
                }
            }
        });
        this.mTranslateAnim = ofFloat;
        return ofFloat;
    }

    /* access modifiers changed from: package-private */
    public void ensureGutsInflated() {
        if (this.mGuts == null) {
            this.mGutsStub.inflate();
        }
    }

    private void updateChildrenVisibility() {
        NotificationGuts notificationGuts;
        int i = 0;
        boolean z = this.mExpandAnimationRunning && (notificationGuts = this.mGuts) != null && notificationGuts.isExposed();
        this.mPrivateLayout.setVisibility((this.mShowingPublic || this.mIsSummaryWithChildren || z) ? 4 : 0);
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            if (this.mShowingPublic || !this.mIsSummaryWithChildren || z) {
                i = 4;
            }
            notificationChildrenContainer.setVisibility(i);
        }
        updateLimits();
    }

    public boolean onRequestSendAccessibilityEventInternal(View view, AccessibilityEvent accessibilityEvent) {
        if (!super.onRequestSendAccessibilityEventInternal(view, accessibilityEvent)) {
            return false;
        }
        AccessibilityEvent obtain = AccessibilityEvent.obtain();
        onInitializeAccessibilityEvent(obtain);
        dispatchPopulateAccessibilityEvent(obtain);
        accessibilityEvent.appendRecord(obtain);
        return true;
    }

    public void applyExpandAnimationParams(ExpandAnimationParameters expandAnimationParameters) {
        int i;
        if (expandAnimationParameters != null) {
            if (expandAnimationParameters.getVisible()) {
                Interpolator interpolator = Interpolators.FAST_OUT_SLOW_IN;
                float lerp = MathUtils.lerp(expandAnimationParameters.getStartTranslationZ(), (float) this.mNotificationLaunchHeight, interpolator.getInterpolation(expandAnimationParameters.getProgress(0, 50)));
                setTranslationZ(lerp);
                float width = (float) (expandAnimationParameters.getWidth() - getWidth());
                setExtraWidthForClipping(width);
                if (expandAnimationParameters.getStartRoundedTopClipping() > 0) {
                    float interpolation = interpolator.getInterpolation(expandAnimationParameters.getProgress(0, 100));
                    float startNotificationTop = expandAnimationParameters.getStartNotificationTop();
                    i = (int) Math.min(MathUtils.lerp(startNotificationTop, (float) expandAnimationParameters.getTop(), interpolation), startNotificationTop);
                } else {
                    i = expandAnimationParameters.getTop();
                }
                int bottom = expandAnimationParameters.getBottom() - i;
                setActualHeight(bottom);
                int startClipTopAmount = expandAnimationParameters.getStartClipTopAmount();
                int lerp2 = (int) MathUtils.lerp((float) startClipTopAmount, 0.0f, expandAnimationParameters.getProgress());
                ExpandableNotificationRow expandableNotificationRow = this.mNotificationParent;
                if (expandableNotificationRow != null) {
                    float translationY = expandableNotificationRow.getTranslationY();
                    i = (int) (((float) i) - translationY);
                    this.mNotificationParent.setTranslationZ(lerp);
                    this.mNotificationParent.setClipTopAmount(Math.min(expandAnimationParameters.getParentStartClipTopAmount(), lerp2 + i));
                    this.mNotificationParent.setExtraWidthForClipping(width);
                    this.mNotificationParent.setMinimumHeightForClipping((int) (Math.max((float) expandAnimationParameters.getBottom(), (((float) this.mNotificationParent.getActualHeight()) + translationY) - ((float) this.mNotificationParent.getClipBottomAmount())) - Math.min((float) expandAnimationParameters.getTop(), translationY)));
                } else if (startClipTopAmount != 0) {
                    setClipTopAmount(lerp2);
                }
                setTranslationY((float) i);
                this.mTopRoundnessDuringExpandAnimation = expandAnimationParameters.getTopCornerRadius() / this.mOutlineRadius;
                this.mBottomRoundnessDuringExpandAnimation = expandAnimationParameters.getBottomCornerRadius() / this.mOutlineRadius;
                invalidateOutline();
                this.mBackgroundNormal.setExpandAnimationSize(expandAnimationParameters.getWidth(), bottom);
            } else if (getVisibility() == 0) {
                setVisibility(4);
            }
        }
    }

    public void setExpandAnimationRunning(boolean z) {
        View view;
        if (this.mIsSummaryWithChildren) {
            view = this.mChildrenContainer;
        } else {
            view = getShowingLayout();
        }
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts != null && notificationGuts.isExposed()) {
            view = this.mGuts;
        }
        if (z) {
            setAboveShelf(true);
            this.mExpandAnimationRunning = true;
            getViewState().cancelAnimations(this);
            this.mNotificationLaunchHeight = AmbientState.getNotificationLaunchHeight(getContext());
        } else {
            this.mExpandAnimationRunning = false;
            setAboveShelf(isAboveShelf());
            setVisibility(0);
            NotificationGuts notificationGuts2 = this.mGuts;
            if (notificationGuts2 != null) {
                notificationGuts2.setAlpha(1.0f);
            }
            if (view != null) {
                view.setAlpha(1.0f);
            }
            setExtraWidthForClipping(0.0f);
            ExpandableNotificationRow expandableNotificationRow = this.mNotificationParent;
            if (expandableNotificationRow != null) {
                expandableNotificationRow.setExtraWidthForClipping(0.0f);
                this.mNotificationParent.setMinimumHeightForClipping(0);
            }
        }
        ExpandableNotificationRow expandableNotificationRow2 = this.mNotificationParent;
        if (expandableNotificationRow2 != null) {
            expandableNotificationRow2.setChildIsExpanding(this.mExpandAnimationRunning);
        }
        updateChildrenVisibility();
        updateClipping();
        this.mBackgroundNormal.setExpandAnimationRunning(z);
    }

    private void setChildIsExpanding(boolean z) {
        this.mChildIsExpanding = z;
        updateClipping();
        invalidate();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean hasExpandingChild() {
        return this.mChildIsExpanding;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public StatusBarIconView getShelfIcon() {
        return getEntry().getIcons().getShelfIcon();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    protected boolean shouldClipToActualHeight() {
        return super.shouldClipToActualHeight() && !this.mExpandAnimationRunning;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean isExpandAnimationRunning() {
        return this.mExpandAnimationRunning;
    }

    @Override // android.view.View
    public boolean isSoundEffectsEnabled() {
        BooleanSupplier booleanSupplier;
        StatusBarStateController statusBarStateController = this.mStatusBarStateController;
        return !(statusBarStateController != null && statusBarStateController.isDozing() && (booleanSupplier = this.mSecureStateProvider) != null && !booleanSupplier.getAsBoolean()) && super.isSoundEffectsEnabled();
    }

    public boolean isExpandable() {
        if (this.mIsSummaryWithChildren && !shouldShowPublic()) {
            return !this.mChildrenExpanded;
        }
        if (!this.mEnableNonGroupedNotificationExpand || !this.mExpandable) {
            return false;
        }
        return true;
    }

    public void setExpandable(boolean z) {
        this.mExpandable = z;
        this.mPrivateLayout.updateExpandButtons(isExpandable());
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void setClipToActualHeight(boolean z) {
        boolean z2 = false;
        super.setClipToActualHeight(z || isUserLocked());
        NotificationContentView showingLayout = getShowingLayout();
        if (z || isUserLocked()) {
            z2 = true;
        }
        showingLayout.setClipToActualHeight(z2);
    }

    public boolean hasUserChangedExpansion() {
        return this.mHasUserChangedExpansion;
    }

    public boolean isUserExpanded() {
        return this.mUserExpanded;
    }

    public void setUserExpanded(boolean z) {
        setUserExpanded(z, false);
    }

    public void setUserExpanded(boolean z, boolean z2) {
        this.mFalsingCollector.setNotificationExpanded();
        if (this.mIsSummaryWithChildren && !shouldShowPublic() && z2 && !this.mChildrenContainer.showingAsLowPriority()) {
            boolean isGroupExpanded = this.mGroupExpansionManager.isGroupExpanded(this.mEntry);
            this.mGroupExpansionManager.setGroupExpanded(this.mEntry, z);
            onExpansionChanged(true, isGroupExpanded);
        } else if (!z || this.mExpandable) {
            boolean isExpanded = isExpanded();
            this.mHasUserChangedExpansion = true;
            this.mUserExpanded = z;
            onExpansionChanged(true, isExpanded);
            if (!isExpanded && isExpanded() && getActualHeight() != getIntrinsicHeight()) {
                notifyHeightChanged(true);
            }
        }
    }

    public void resetUserExpansion() {
        boolean isExpanded = isExpanded();
        this.mHasUserChangedExpansion = false;
        this.mUserExpanded = false;
        if (isExpanded != isExpanded()) {
            if (this.mIsSummaryWithChildren) {
                this.mChildrenContainer.onExpansionChanged();
            }
            notifyHeightChanged(false);
        }
        updateShelfIconColor();
    }

    public boolean isUserLocked() {
        return this.mUserLocked;
    }

    public void setUserLocked(boolean z) {
        this.mUserLocked = z;
        this.mPrivateLayout.setUserExpanding(z);
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.setUserLocked(z);
            if (!this.mIsSummaryWithChildren) {
                return;
            }
            if (z || !isGroupExpanded()) {
                updateBackgroundForGroupState();
            }
        }
    }

    public boolean isSystemExpanded() {
        return this.mIsSystemExpanded;
    }

    public void setSystemExpanded(boolean z) {
        if (z != this.mIsSystemExpanded) {
            boolean isExpanded = isExpanded();
            this.mIsSystemExpanded = z;
            notifyHeightChanged(false);
            onExpansionChanged(false, isExpanded);
            if (this.mIsSummaryWithChildren) {
                this.mChildrenContainer.updateGroupOverflow();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setOnKeyguard(boolean z) {
        if (z != this.mOnKeyguard) {
            boolean isAboveShelf = isAboveShelf();
            boolean isExpanded = isExpanded();
            this.mOnKeyguard = z;
            onExpansionChanged(false, isExpanded);
            if (isExpanded != isExpanded()) {
                if (this.mIsSummaryWithChildren) {
                    this.mChildrenContainer.updateGroupOverflow();
                }
                notifyHeightChanged(false);
            }
            if (isAboveShelf() != isAboveShelf) {
                this.mAboveShelfChangedListener.onAboveShelfStateChanged(!isAboveShelf);
            }
        }
        updateRippleAllowed();
    }

    private void updateRippleAllowed() {
        setRippleAllowed(isOnKeyguard() || this.mEntry.getSbn().getNotification().contentIntent == null);
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    public void onTap() {
        if (this.mEntry.getSbn().getNotification().contentIntent != null) {
            setRippleAllowed(false);
        }
    }

    @Override // android.view.View
    public boolean performClick() {
        updateRippleAllowed();
        return super.performClick();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public int getIntrinsicHeight() {
        if (isUserLocked()) {
            return getActualHeight();
        }
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts != null && notificationGuts.isExposed()) {
            return this.mGuts.getIntrinsicHeight();
        }
        if (isChildInGroup() && !isGroupExpanded()) {
            return this.mPrivateLayout.getMinHeight();
        }
        if (this.mSensitive && this.mHideSensitiveForIntrinsicHeight) {
            return getMinHeight();
        }
        if (this.mIsSummaryWithChildren) {
            return this.mChildrenContainer.getIntrinsicHeight();
        }
        if (!canShowHeadsUp() || !isHeadsUpState()) {
            if (isExpanded()) {
                return getMaxExpandHeight();
            }
            return getCollapsedHeight();
        } else if (isPinned() || this.mHeadsupDisappearRunning) {
            return getPinnedHeadsUpHeight(true);
        } else {
            if (isExpanded()) {
                return Math.max(getMaxExpandHeight(), getHeadsUpHeight());
            }
            return Math.max(getCollapsedHeight(), getHeadsUpHeight());
        }
    }

    public boolean canShowHeadsUp() {
        return !this.mOnKeyguard || isDozing() || isBypassEnabled();
    }

    private boolean isBypassEnabled() {
        KeyguardBypassController keyguardBypassController = this.mBypassController;
        return keyguardBypassController == null || keyguardBypassController.getBypassEnabled();
    }

    private boolean isDozing() {
        StatusBarStateController statusBarStateController = this.mStatusBarStateController;
        return statusBarStateController != null && statusBarStateController.isDozing();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean isGroupExpanded() {
        return this.mGroupExpansionManager.isGroupExpanded(this.mEntry);
    }

    private void onAttachedChildrenCountChanged() {
        NotificationViewWrapper notificationViewWrapper;
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        boolean z = notificationChildrenContainer != null && notificationChildrenContainer.getNotificationChildCount() > 0;
        this.mIsSummaryWithChildren = z;
        if (z && ((notificationViewWrapper = this.mChildrenContainer.getNotificationViewWrapper()) == null || notificationViewWrapper.getNotificationHeader() == null)) {
            this.mChildrenContainer.recreateNotificationHeader(this.mExpandClickListener, isConversation());
        }
        getShowingLayout().updateBackgroundColor(false);
        this.mPrivateLayout.updateExpandButtons(isExpandable());
        updateChildrenAppearance();
        updateChildrenVisibility();
        applyChildrenRoundness();
    }

    /* access modifiers changed from: protected */
    public void expandNotification() {
        this.mExpandClickListener.onClick(this);
    }

    public ArraySet<NotificationChannel> getUniqueChannels() {
        ArraySet<NotificationChannel> arraySet = new ArraySet<>();
        arraySet.add(this.mEntry.getChannel());
        if (this.mIsSummaryWithChildren) {
            List<ExpandableNotificationRow> attachedChildren = getAttachedChildren();
            int size = attachedChildren.size();
            for (int i = 0; i < size; i++) {
                ExpandableNotificationRow expandableNotificationRow = attachedChildren.get(i);
                NotificationChannel channel = expandableNotificationRow.getEntry().getChannel();
                StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
                if (sbn.getUser().equals(this.mEntry.getSbn().getUser()) && sbn.getPackageName().equals(this.mEntry.getSbn().getPackageName())) {
                    arraySet.add(channel);
                }
            }
        }
        return arraySet;
    }

    public void updateChildrenAppearance() {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.updateChildrenAppearance();
        }
    }

    public boolean isExpanded() {
        return isExpanded(false);
    }

    public boolean isExpanded(boolean z) {
        return (!this.mOnKeyguard || z) && ((!hasUserChangedExpansion() && (isSystemExpanded() || isSystemChildExpanded())) || isUserExpanded());
    }

    private boolean isSystemChildExpanded() {
        return this.mIsSystemChildExpanded;
    }

    public void setSystemChildExpanded(boolean z) {
        this.mIsSystemChildExpanded = z;
    }

    public void setLayoutListener(LayoutListener layoutListener) {
        this.mLayoutListener = layoutListener;
    }

    public void removeListener() {
        this.mLayoutListener = null;
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableView, android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int intrinsicHeight = getIntrinsicHeight();
        super.onLayout(z, i, i2, i3, i4);
        if (intrinsicHeight != getIntrinsicHeight() && (intrinsicHeight != 0 || getActualHeight() > 0)) {
            notifyHeightChanged(true);
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (!(notificationMenuRowPlugin == null || notificationMenuRowPlugin.getMenuView() == null)) {
            this.mMenuRow.onParentHeightUpdate();
        }
        updateContentShiftHeight();
        LayoutListener layoutListener = this.mLayoutListener;
        if (layoutListener != null) {
            layoutListener.onLayout();
        }
    }

    private void updateContentShiftHeight() {
        View view;
        NotificationViewWrapper visibleNotificationViewWrapper = getVisibleNotificationViewWrapper();
        if (visibleNotificationViewWrapper == null) {
            view = null;
        } else {
            view = visibleNotificationViewWrapper.getIcon();
        }
        if (view != null) {
            this.mIconTransformContentShift = getRelativeTopPadding(view) + view.getHeight();
        } else {
            this.mIconTransformContentShift = this.mContentShift;
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    protected float getContentTransformationShift() {
        return (float) this.mIconTransformContentShift;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void notifyHeightChanged(boolean z) {
        super.notifyHeightChanged(z);
        getShowingLayout().requestSelectLayout(z || isUserLocked());
    }

    public void setSensitive(boolean z, boolean z2) {
        this.mSensitive = z;
        this.mSensitiveHiddenInGeneral = z2;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void setHideSensitiveForIntrinsicHeight(boolean z) {
        this.mHideSensitiveForIntrinsicHeight = z;
        if (this.mIsSummaryWithChildren) {
            List<ExpandableNotificationRow> attachedChildren = this.mChildrenContainer.getAttachedChildren();
            for (int i = 0; i < attachedChildren.size(); i++) {
                attachedChildren.get(i).setHideSensitiveForIntrinsicHeight(z);
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void setHideSensitive(boolean z, boolean z2, long j, long j2) {
        if (getVisibility() != 8) {
            boolean z3 = this.mShowingPublic;
            int i = 0;
            boolean z4 = this.mSensitive && z;
            this.mShowingPublic = z4;
            if ((!this.mShowingPublicInitialized || z4 != z3) && this.mPublicLayout.getChildCount() != 0) {
                if (!z2) {
                    this.mPublicLayout.animate().cancel();
                    this.mPrivateLayout.animate().cancel();
                    NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
                    if (notificationChildrenContainer != null) {
                        notificationChildrenContainer.animate().cancel();
                        this.mChildrenContainer.setAlpha(1.0f);
                    }
                    this.mPublicLayout.setAlpha(1.0f);
                    this.mPrivateLayout.setAlpha(1.0f);
                    NotificationContentView notificationContentView = this.mPublicLayout;
                    if (!this.mShowingPublic) {
                        i = 4;
                    }
                    notificationContentView.setVisibility(i);
                    updateChildrenVisibility();
                } else {
                    animateShowingPublic(j, j2, this.mShowingPublic);
                }
                getShowingLayout().updateBackgroundColor(z2);
                this.mPrivateLayout.updateExpandButtons(isExpandable());
                updateShelfIconColor();
                this.mShowingPublicInitialized = true;
            }
        }
    }

    private void animateShowingPublic(long j, long j2, boolean z) {
        View[] viewArr = this.mIsSummaryWithChildren ? new View[]{this.mChildrenContainer} : new View[]{this.mPrivateLayout};
        View[] viewArr2 = {this.mPublicLayout};
        View[] viewArr3 = z ? viewArr : viewArr2;
        if (z) {
            viewArr = viewArr2;
        }
        for (View view : viewArr3) {
            view.setVisibility(0);
            view.animate().cancel();
            view.animate().alpha(0.0f).setStartDelay(j).setDuration(j2).withEndAction(new Runnable(view) { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$$ExternalSyntheticLambda5
                public final /* synthetic */ View f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.setVisibility(4);
                }
            });
        }
        for (View view2 : viewArr) {
            view2.setVisibility(0);
            view2.setAlpha(0.0f);
            view2.animate().cancel();
            view2.animate().alpha(1.0f).setStartDelay(j).setDuration(j2);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean mustStayOnScreen() {
        return this.mIsHeadsUp && this.mMustStayOnScreen;
    }

    public boolean canViewBeDismissed() {
        return this.mEntry.isClearable() && (!shouldShowPublic() || !this.mSensitiveHiddenInGeneral);
    }

    /* access modifiers changed from: private */
    public boolean shouldShowPublic() {
        return this.mSensitive && this.mHideSensitiveForIntrinsicHeight;
    }

    public void makeActionsVisibile() {
        setUserExpanded(true, true);
        if (isChildInGroup()) {
            this.mGroupExpansionManager.setGroupExpanded(this.mEntry, true);
        }
        notifyHeightChanged(false);
    }

    public void setChildrenExpanded(boolean z, boolean z2) {
        this.mChildrenExpanded = z;
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.setChildrenExpanded(z);
        }
        updateBackgroundForGroupState();
        updateClickAndFocus();
    }

    public int getMaxExpandHeight() {
        return this.mPrivateLayout.getExpandHeight();
    }

    private int getHeadsUpHeight() {
        return getShowingLayout().getHeadsUpHeight(false);
    }

    public boolean areGutsExposed() {
        NotificationGuts notificationGuts = this.mGuts;
        return notificationGuts != null && notificationGuts.isExposed();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean isContentExpandable() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout().isContentExpandable();
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    protected View getContentView() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout();
        }
        return this.mChildrenContainer;
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableView
    public long performRemoveAnimation(final long j, final long j2, final float f, final boolean z, final float f2, final Runnable runnable, final AnimatorListenerAdapter animatorListenerAdapter) {
        Animator translateViewAnimator;
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin == null || !notificationMenuRowPlugin.isMenuVisible() || (translateViewAnimator = getTranslateViewAnimator(0.0f, null)) == null) {
            return super.performRemoveAnimation(j, j2, f, z, f2, runnable, animatorListenerAdapter);
        }
        translateViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.4
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ExpandableNotificationRow.super.performRemoveAnimation(j, j2, f, z, f2, runnable, animatorListenerAdapter);
            }
        });
        translateViewAnimator.start();
        return translateViewAnimator.getDuration();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    protected void onAppearAnimationFinished(boolean z) {
        super.onAppearAnimationFinished(z);
        if (z) {
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            if (notificationChildrenContainer != null) {
                notificationChildrenContainer.setAlpha(1.0f);
                this.mChildrenContainer.setLayerType(0, null);
            }
            NotificationContentView[] notificationContentViewArr = this.mLayouts;
            for (NotificationContentView notificationContentView : notificationContentViewArr) {
                notificationContentView.setAlpha(1.0f);
                notificationContentView.setLayerType(0, null);
            }
            return;
        }
        setHeadsUpAnimatingAway(false);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public int getExtraBottomPadding() {
        if (!this.mIsSummaryWithChildren || !isGroupExpanded()) {
            return 0;
        }
        return this.mIncreasedPaddingBetweenElements;
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableOutlineView, com.android.systemui.statusbar.notification.row.ExpandableView
    public void setActualHeight(int i, boolean z) {
        ViewGroup viewGroup;
        boolean z2 = i != getActualHeight();
        super.setActualHeight(i, z);
        if (z2 && isRemoved() && (viewGroup = (ViewGroup) getParent()) != null) {
            viewGroup.invalidate();
        }
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts == null || !notificationGuts.isExposed()) {
            int max = Math.max(getMinHeight(), i);
            for (NotificationContentView notificationContentView : this.mLayouts) {
                notificationContentView.setContentHeight(max);
            }
            if (this.mIsSummaryWithChildren) {
                this.mChildrenContainer.setActualHeight(i);
            }
            NotificationGuts notificationGuts2 = this.mGuts;
            if (notificationGuts2 != null) {
                notificationGuts2.setActualHeight(i);
            }
            NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
            if (!(notificationMenuRowPlugin == null || notificationMenuRowPlugin.getMenuView() == null)) {
                this.mMenuRow.onParentHeightUpdate();
            }
            handleIntrinsicHeightReached();
            return;
        }
        this.mGuts.setActualHeight(i);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public int getMaxContentHeight() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout().getMaxHeight();
        }
        return this.mChildrenContainer.getMaxContentHeight();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public int getMinHeight(boolean z) {
        NotificationGuts notificationGuts;
        if (!z && (notificationGuts = this.mGuts) != null && notificationGuts.isExposed()) {
            return this.mGuts.getIntrinsicHeight();
        }
        if (!z && canShowHeadsUp() && this.mIsHeadsUp && this.mHeadsUpManager.isTrackingHeadsUp()) {
            return getPinnedHeadsUpHeight(false);
        }
        if (this.mIsSummaryWithChildren && !isGroupExpanded() && !shouldShowPublic()) {
            return this.mChildrenContainer.getMinHeight();
        }
        if (z || !canShowHeadsUp() || !this.mIsHeadsUp) {
            return getShowingLayout().getMinHeight();
        }
        return getHeadsUpHeight();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public int getCollapsedHeight() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getMinHeight();
        }
        return this.mChildrenContainer.getCollapsedHeight();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableView
    public int getHeadsUpHeightWithoutHeader() {
        if (!canShowHeadsUp() || !this.mIsHeadsUp) {
            return getCollapsedHeight();
        }
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout().getHeadsUpHeight(true);
        }
        return this.mChildrenContainer.getCollapsedHeightWithoutHeader();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableOutlineView, com.android.systemui.statusbar.notification.row.ExpandableView
    public void setClipTopAmount(int i) {
        super.setClipTopAmount(i);
        for (NotificationContentView notificationContentView : this.mLayouts) {
            notificationContentView.setClipTopAmount(i);
        }
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts != null) {
            notificationGuts.setClipTopAmount(i);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableOutlineView, com.android.systemui.statusbar.notification.row.ExpandableView
    public void setClipBottomAmount(int i) {
        if (!this.mExpandAnimationRunning) {
            if (i != this.mClipBottomAmount) {
                super.setClipBottomAmount(i);
                for (NotificationContentView notificationContentView : this.mLayouts) {
                    notificationContentView.setClipBottomAmount(i);
                }
                NotificationGuts notificationGuts = this.mGuts;
                if (notificationGuts != null) {
                    notificationGuts.setClipBottomAmount(i);
                }
            }
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            if (!(notificationChildrenContainer == null || this.mChildIsExpanding)) {
                notificationChildrenContainer.setClipBottomAmount(i);
            }
        }
    }

    public NotificationContentView getShowingLayout() {
        return shouldShowPublic() ? this.mPublicLayout : this.mPrivateLayout;
    }

    public View getExpandedContentView() {
        return getPrivateLayout().getExpandedChild();
    }

    public void setLegacy(boolean z) {
        for (NotificationContentView notificationContentView : this.mLayouts) {
            notificationContentView.setLegacy(z);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    protected void updateBackgroundTint() {
        super.updateBackgroundTint();
        updateBackgroundForGroupState();
        if (this.mIsSummaryWithChildren) {
            List<ExpandableNotificationRow> attachedChildren = this.mChildrenContainer.getAttachedChildren();
            for (int i = 0; i < attachedChildren.size(); i++) {
                attachedChildren.get(i).updateBackgroundForGroupState();
            }
        }
    }

    public void onFinishedExpansionChange() {
        this.mGroupExpansionChanging = false;
        updateBackgroundForGroupState();
    }

    /* JADX DEBUG: Multi-variable search result rejected for r0v4, resolved type: boolean */
    /* JADX WARN: Multi-variable type inference failed */
    public void updateBackgroundForGroupState() {
        boolean z = true;
        int i = 0;
        if (this.mIsSummaryWithChildren) {
            if (this.mShowGroupBackgroundWhenExpanded || !isGroupExpanded() || isGroupExpansionChanging() || isUserLocked()) {
                z = false;
            }
            this.mShowNoBackground = z;
            this.mChildrenContainer.updateHeaderForExpansion(z);
            List<ExpandableNotificationRow> attachedChildren = this.mChildrenContainer.getAttachedChildren();
            while (i < attachedChildren.size()) {
                attachedChildren.get(i).updateBackgroundForGroupState();
                i++;
            }
        } else if (isChildInGroup()) {
            int backgroundColorForExpansionState = getShowingLayout().getBackgroundColorForExpansionState();
            if (isGroupExpanded() || ((this.mNotificationParent.isGroupExpansionChanging() || this.mNotificationParent.isUserLocked()) && backgroundColorForExpansionState != 0)) {
                i = 1;
            }
            this.mShowNoBackground = i ^ 1;
        } else {
            this.mShowNoBackground = false;
        }
        updateOutline();
        updateBackground();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    protected boolean hideBackground() {
        return this.mShowNoBackground || super.hideBackground();
    }

    public int getPositionOfChild(ExpandableNotificationRow expandableNotificationRow) {
        if (this.mIsSummaryWithChildren) {
            return this.mChildrenContainer.getPositionInLinearLayout(expandableNotificationRow);
        }
        return 0;
    }

    public void onExpandedByGesture(boolean z) {
        MetricsLogger.action(((FrameLayout) this).mContext, this.mGroupMembershipManager.isGroupSummary(this.mEntry) ? 410 : 409, z);
    }

    /* access modifiers changed from: private */
    public void onExpansionChanged(boolean z, boolean z2) {
        boolean isExpanded = isExpanded();
        if (this.mIsSummaryWithChildren && (!this.mIsLowPriority || z2)) {
            isExpanded = this.mGroupExpansionManager.isGroupExpanded(this.mEntry);
        }
        if (isExpanded != z2) {
            updateShelfIconColor();
            ExpansionLogger expansionLogger = this.mLogger;
            if (expansionLogger != null) {
                expansionLogger.logNotificationExpansion(this.mLoggingKey, z, isExpanded);
            }
            if (this.mIsSummaryWithChildren) {
                this.mChildrenContainer.onExpansionChanged();
            }
            OnExpansionChangedListener onExpansionChangedListener = this.mExpansionChangedListener;
            if (onExpansionChangedListener != null) {
                onExpansionChangedListener.onExpansionChanged(isExpanded);
            }
        }
    }

    public void setOnExpansionChangedListener(OnExpansionChangedListener onExpansionChangedListener) {
        this.mExpansionChangedListener = onExpansionChangedListener;
    }

    public void performOnIntrinsicHeightReached(Runnable runnable) {
        this.mOnIntrinsicHeightReachedRunnable = runnable;
        handleIntrinsicHeightReached();
    }

    private void handleIntrinsicHeightReached() {
        if (this.mOnIntrinsicHeightReachedRunnable != null && getActualHeight() == getIntrinsicHeight()) {
            this.mOnIntrinsicHeightReachedRunnable.run();
            this.mOnIntrinsicHeightReachedRunnable = null;
        }
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfoInternal(accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_LONG_CLICK);
        if (canViewBeDismissed() && !this.mIsSnoozed) {
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_DISMISS);
        }
        boolean shouldShowPublic = shouldShowPublic();
        boolean z = false;
        if (!shouldShowPublic) {
            if (this.mIsSummaryWithChildren) {
                shouldShowPublic = true;
                if (!this.mIsLowPriority || isExpanded()) {
                    z = isGroupExpanded();
                }
            } else {
                shouldShowPublic = this.mPrivateLayout.isContentExpandable();
                z = isExpanded();
            }
        }
        if (shouldShowPublic && !this.mIsSnoozed) {
            if (z) {
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_COLLAPSE);
            } else {
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
            }
        }
        NotificationMenuRowPlugin provider = getProvider();
        if (provider != null && provider.getSnoozeMenuItem(getContext()) != null) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.action_snooze, getContext().getResources().getString(R$string.notification_menu_snooze_action)));
        }
    }

    public boolean performAccessibilityActionInternal(int i, Bundle bundle) {
        NotificationMenuRowPlugin provider;
        if (super.performAccessibilityActionInternal(i, bundle)) {
            return true;
        }
        if (i == 32) {
            doLongClickCallback();
            return true;
        } else if (i == 262144 || i == 524288) {
            this.mExpandClickListener.onClick(this);
            return true;
        } else if (i == 1048576) {
            performDismiss(true);
            return true;
        } else if (i != R$id.action_snooze || (provider = getProvider()) == null) {
            return false;
        } else {
            NotificationMenuRowPlugin.MenuItem snoozeMenuItem = provider.getSnoozeMenuItem(getContext());
            if (snoozeMenuItem != null) {
                doLongClickCallback(getWidth() / 2, getHeight() / 2, snoozeMenuItem);
            }
            return true;
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public ExpandableViewState createExpandableViewState() {
        return new NotificationViewState();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean isAboveShelf() {
        return canShowHeadsUp() && (this.mIsPinned || this.mHeadsupDisappearRunning || ((this.mIsHeadsUp && this.mAboveShelf) || this.mExpandAnimationRunning || this.mChildIsExpanding));
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    protected boolean childNeedsClipping(View view) {
        if (view instanceof NotificationContentView) {
            NotificationContentView notificationContentView = (NotificationContentView) view;
            if (isClippingNeeded()) {
                return true;
            }
            if (!hasNoRounding()) {
                boolean z = false;
                boolean z2 = getCurrentTopRoundness() != 0.0f;
                if (getCurrentBottomRoundness() != 0.0f) {
                    z = true;
                }
                if (notificationContentView.shouldClipToRounding(z2, z)) {
                    return true;
                }
            }
        } else if (view == this.mChildrenContainer) {
            if (isClippingNeeded() || !hasNoRounding()) {
                return true;
            }
        } else if (view instanceof NotificationGuts) {
            return !hasNoRounding();
        }
        return super.childNeedsClipping(view);
    }

    public void setExpandingClipPath(Path path) {
        this.mExpandingClipPath = path;
        invalidate();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, android.view.View, android.view.ViewGroup
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        Path path = this.mExpandingClipPath;
        if (path != null && (this.mExpandAnimationRunning || this.mChildIsExpanding)) {
            canvas.clipPath(path);
        }
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    protected void applyRoundness() {
        super.applyRoundness();
        applyChildrenRoundness();
    }

    private void applyChildrenRoundness() {
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.setCurrentBottomRoundness(getCurrentBottomRoundness());
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public Path getCustomClipPath(View view) {
        if (view instanceof NotificationGuts) {
            return getClipPath(true);
        }
        return super.getCustomClipPath(view);
    }

    private boolean hasNoRounding() {
        return getCurrentBottomRoundness() == 0.0f && getCurrentTopRoundness() == 0.0f;
    }

    public boolean isMediaRow() {
        return (getExpandedContentView() == null || getExpandedContentView().findViewById(16909165) == null) ? false : true;
    }

    public boolean isTopLevelChild() {
        return getParent() instanceof NotificationStackScrollLayout;
    }

    public boolean isGroupNotFullyVisible() {
        return getClipTopAmount() > 0 || getTranslationY() < 0.0f;
    }

    public void setAboveShelf(boolean z) {
        boolean isAboveShelf = isAboveShelf();
        this.mAboveShelf = z;
        if (isAboveShelf() != isAboveShelf) {
            this.mAboveShelfChangedListener.onAboveShelfStateChanged(!isAboveShelf);
        }
    }

    public void setDismissRtl(boolean z) {
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin != null) {
            notificationMenuRowPlugin.setDismissRtl(z);
        }
    }

    /* loaded from: classes.dex */
    private static class NotificationViewState extends ExpandableViewState {
        private NotificationViewState() {
        }

        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public void applyToView(View view) {
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (!expandableNotificationRow.isExpandAnimationRunning()) {
                    handleFixedTranslationZ(expandableNotificationRow);
                    super.applyToView(view);
                    expandableNotificationRow.applyChildrenState();
                }
            }
        }

        private void handleFixedTranslationZ(ExpandableNotificationRow expandableNotificationRow) {
            if (expandableNotificationRow.hasExpandingChild()) {
                this.zTranslation = expandableNotificationRow.getTranslationZ();
                this.clipTopAmount = expandableNotificationRow.getClipTopAmount();
            }
        }

        @Override // com.android.systemui.statusbar.notification.stack.ViewState
        protected void onYTranslationAnimationFinished(View view) {
            super.onYTranslationAnimationFinished(view);
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (expandableNotificationRow.isHeadsUpAnimatingAway()) {
                    expandableNotificationRow.setHeadsUpAnimatingAway(false);
                }
            }
        }

        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public void animateTo(View view, AnimationProperties animationProperties) {
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (!expandableNotificationRow.isExpandAnimationRunning()) {
                    handleFixedTranslationZ(expandableNotificationRow);
                    super.animateTo(view, animationProperties);
                    expandableNotificationRow.startChildAnimation(animationProperties);
                }
            }
        }
    }

    public InflatedSmartReplyState getExistingSmartReplyState() {
        return this.mPrivateLayout.getCurrentSmartReplyState();
    }

    @VisibleForTesting
    protected void setChildrenContainer(NotificationChildrenContainer notificationChildrenContainer) {
        this.mChildrenContainer = notificationChildrenContainer;
    }

    @VisibleForTesting
    protected void setPrivateLayout(NotificationContentView notificationContentView) {
        this.mPrivateLayout = notificationContentView;
    }

    @VisibleForTesting
    protected void setPublicLayout(NotificationContentView notificationContentView) {
        this.mPublicLayout = notificationContentView;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(fileDescriptor, printWriter, strArr);
        printWriter.println("  Notification: " + this.mEntry.getKey());
        printWriter.print("    visibility: " + getVisibility());
        printWriter.print(", alpha: " + getAlpha());
        printWriter.print(", translation: " + getTranslation());
        printWriter.print(", removed: " + isRemoved());
        printWriter.print(", expandAnimationRunning: " + this.mExpandAnimationRunning);
        NotificationContentView showingLayout = getShowingLayout();
        StringBuilder sb = new StringBuilder();
        sb.append(", privateShowing: ");
        sb.append(showingLayout == this.mPrivateLayout);
        printWriter.print(sb.toString());
        printWriter.println();
        showingLayout.dump(fileDescriptor, printWriter, strArr);
        printWriter.print("    ");
        if (getViewState() != null) {
            getViewState().dump(fileDescriptor, printWriter, strArr);
        } else {
            printWriter.print("no viewState!!!");
        }
        printWriter.println();
        printWriter.println();
        if (this.mIsSummaryWithChildren) {
            printWriter.print("  ChildrenContainer");
            printWriter.print(" visibility: " + this.mChildrenContainer.getVisibility());
            printWriter.print(", alpha: " + this.mChildrenContainer.getAlpha());
            printWriter.print(", translationY: " + this.mChildrenContainer.getTranslationY());
            printWriter.println();
            List<ExpandableNotificationRow> attachedChildren = getAttachedChildren();
            printWriter.println("  Children: " + attachedChildren.size());
            printWriter.println("  {");
            for (ExpandableNotificationRow expandableNotificationRow : attachedChildren) {
                expandableNotificationRow.dump(fileDescriptor, printWriter, strArr);
            }
            printWriter.println("  }");
            printWriter.println();
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SystemNotificationAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private SystemNotificationAsyncTask() {
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(Void... voidArr) {
            return ExpandableNotificationRow.isSystemNotification(((FrameLayout) ExpandableNotificationRow.this).mContext, ExpandableNotificationRow.this.mEntry.getSbn());
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean bool) {
            if (ExpandableNotificationRow.this.mEntry != null) {
                ExpandableNotificationRow.this.mEntry.mIsSystemNotification = bool;
            }
        }
    }
}
