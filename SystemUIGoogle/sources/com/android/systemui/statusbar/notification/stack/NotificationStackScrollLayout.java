package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import android.widget.ScrollView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.settingslib.Utils;
import com.android.systemui.Dumpable;
import com.android.systemui.ExpandHelper;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.EmptyShadeView;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.notification.ExpandAnimationParameters;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.FooterView;
import com.android.systemui.statusbar.notification.row.ForegroundServiceDungeonView;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController;
import com.android.systemui.statusbar.phone.HeadsUpTouchHelper;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.HeadsUpUtil;
import com.android.systemui.statusbar.policy.ScrollAdapter;
import com.android.systemui.util.Assert;
import com.android.systemui.util.leak.RotationUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class NotificationStackScrollLayout extends ViewGroup implements Dumpable {
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.debug.nssl", false);
    private static final boolean DEBUG_REMOVE_ANIMATION = SystemProperties.getBoolean("persist.debug.nssl.dismiss", false);
    private boolean mActivateNeedsAnimation;
    private final AmbientState mAmbientState;
    private boolean mAnimateBottomOnLayout;
    private boolean mAnimateNextBackgroundBottom;
    private boolean mAnimateNextBackgroundTop;
    private boolean mAnimateNextSectionBoundsChange;
    private boolean mAnimateNextTopPaddingChange;
    private boolean mAnimationRunning;
    private boolean mAnimationsEnabled;
    private final Paint mBackgroundPaint;
    private boolean mBackwardScrollable;
    private int mBottomMargin;
    private int mCachedBackgroundColor;
    private boolean mChangePositionInProgress;
    boolean mCheckForLeavebehind;
    private boolean mChildTransferInProgress;
    private boolean mChildrenUpdateRequested;
    protected boolean mClearAllEnabled;
    private int mCollapsedSize;
    private int mContentHeight;
    private boolean mContinuousBackgroundUpdate;
    private boolean mContinuousShadowUpdate;
    private NotificationStackScrollLayoutController mController;
    private int mCornerRadius;
    private Paint mDebugPaint;
    private float mDimAmount;
    private ValueAnimator mDimAnimator;
    private boolean mDimmedNeedsAnimation;
    private boolean mDisallowDismissInThisMotion;
    private boolean mDisallowScrollingInThisMotion;
    private DismissAllAnimationListener mDismissAllAnimationListener;
    private boolean mDismissAllInProgress;
    private DismissListener mDismissListener;
    private boolean mDismissRtl;
    private boolean mDontClampNextScroll;
    private boolean mDontReportNextOverScroll;
    private int mDownX;
    protected EmptyShadeView mEmptyShadeView;
    private boolean mEverythingNeedsAnimation;
    private ExpandHelper mExpandHelper;
    private ExpandableView mExpandedGroupView;
    private float mExpandedHeight;
    private boolean mExpandedInThisMotion;
    private boolean mExpandingNotification;
    private ExpandableNotificationRow mExpandingNotificationRow;
    private float mExtraTopInsetForFullShadeTransition;
    private final FeatureFlags mFeatureFlags;
    private ForegroundServiceDungeonView mFgsSectionView;
    private Runnable mFinishScrollingCallback;
    private boolean mFlingAfterUpEvent;
    private FooterDismissListener mFooterDismissListener;
    protected FooterView mFooterView;
    private boolean mForceNoOverlappingRendering;
    private View mForcedScroll;
    private boolean mForwardScrollable;
    private int mGapHeight;
    private boolean mGenerateChildOrderChangedEvent;
    private long mGoToFullShadeDelay;
    private boolean mGoToFullShadeNeedsAnimation;
    private GroupExpansionManager mGroupExpansionManager;
    private GroupMembershipManager mGroupMembershipManager;
    private boolean mHeadsUpAnimatingAway;
    private HeadsUpAppearanceController mHeadsUpAppearanceController;
    private int mHeadsUpInset;
    private boolean mHideSensitiveNeedsAnimation;
    private boolean mHighPriorityBeforeSpeedBump;
    private boolean mInHeadsUpPinnedMode;
    private float mInitialTouchX;
    private float mInitialTouchY;
    private int mIntrinsicContentHeight;
    private int mIntrinsicPadding;
    private boolean mIsBeingDragged;
    private boolean mIsClipped;
    private boolean mIsExpansionChanging;
    private KeyguardBypassEnabledProvider mKeyguardBypassEnabledProvider;
    private int mLastMotionY;
    private float mLastSentAppear;
    private float mLastSentExpandedHeight;
    private ExpandAnimationParameters mLaunchAnimationParams;
    private boolean mLaunchingNotification;
    private boolean mLaunchingNotificationNeedsToBeClipped;
    private NotificationLogger.OnChildLocationsChangedListener mListener;
    private int mMaxLayoutHeight;
    private float mMaxOverScroll;
    private int mMaxScrollAfterExpand;
    private int mMaxTopPadding;
    private int mMaximumVelocity;
    private int mMinInteractionHeight;
    private float mMinTopOverScrollToEscape;
    private int mMinimumPaddings;
    private int mMinimumVelocity;
    private boolean mNeedViewResizeAnimation;
    private boolean mNeedsAnimation;
    private NotificationActivityStarter mNotificationActivityStarter;
    private long mNumHeadsUp;
    private OnEmptySpaceClickListener mOnEmptySpaceClickListener;
    private ExpandableView.OnHeightChangedListener mOnHeightChangedListener;
    private Consumer<Boolean> mOnStackYChanged;
    private boolean mOnlyScrollingInThisMotion;
    private final ViewOutlineProvider mOutlineProvider;
    private float mOverScrolledBottomPixels;
    private float mOverScrolledTopPixels;
    private int mOverflingDistance;
    private OnOverscrollTopChangedListener mOverscrollTopChangedListener;
    private int mOwnScrollY;
    private int mPaddingBetweenElements;
    private boolean mPanelTracking;
    private boolean mPulsing;
    protected ViewGroup mQsContainer;
    private boolean mQsExpanded;
    private float mQsExpansionFraction;
    private int mQsScrollBoundaryPosition;
    private int mQsTilePadding;
    private NotificationRemoteInputManager mRemoteInputManager;
    private Rect mRequestedClipBounds;
    private int mRoundedRectClippingBottom;
    private int mRoundedRectClippingLeft;
    private int mRoundedRectClippingRight;
    private int mRoundedRectClippingTop;
    private final ScrollAdapter mScrollAdapter;
    private Consumer<Integer> mScrollListener;
    private boolean mScrollable;
    private boolean mScrolledToTopOnFirstDown;
    private OverScroller mScroller;
    protected boolean mScrollingEnabled;
    private NotificationSection[] mSections;
    private final NotificationSectionsManager mSectionsManager;
    private ShadeController mShadeController;
    private NotificationShelf mShelf;
    private final boolean mShouldDrawNotificationBackground;
    private boolean mShouldShowShelfOnly;
    private boolean mShouldUseSplitNotificationShade;
    private int mSidePaddings;
    private boolean mSkinnyNotifsInLandscape;
    private float mSlopMultiplier;
    private final StackScrollAlgorithm mStackScrollAlgorithm;
    private float mStackTranslation;
    private StatusBar mStatusBar;
    private int mStatusBarHeight;
    private int mStatusBarState;
    private NotificationSwipeHelper mSwipeHelper;
    private NotificationEntry mTopHeadsUpEntry;
    private int mTopPadding;
    private boolean mTopPaddingNeedsAnimation;
    private float mTopPaddingOverflow;
    private NotificationStackScrollLayoutController.TouchHandler mTouchHandler;
    private boolean mTouchIsClick;
    private int mTouchSlop;
    private boolean mTrackingHeadsUp;
    private final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    private VelocityTracker mVelocityTracker;
    private int mWaterfallTopInset;
    private boolean mWillExpand;
    private int mCurrentStackHeight = Integer.MAX_VALUE;
    private int mActivePointerId = -1;
    private int mBottomInset = 0;
    private HashSet<ExpandableView> mChildrenToAddAnimated = new HashSet<>();
    private ArrayList<View> mAddedHeadsUpChildren = new ArrayList<>();
    private ArrayList<ExpandableView> mChildrenToRemoveAnimated = new ArrayList<>();
    private ArrayList<ExpandableView> mChildrenChangingPositions = new ArrayList<>();
    private HashSet<View> mFromMoreCardAdditions = new HashSet<>();
    private ArrayList<AnimationEvent> mAnimationEvents = new ArrayList<>();
    private ArrayList<View> mSwipedOutViews = new ArrayList<>();
    private final StackStateAnimator mStateAnimator = new StackStateAnimator(this);
    private int mSpeedBumpIndex = -1;
    private boolean mSpeedBumpIndexDirty = true;
    private boolean mIsExpanded = true;
    private ViewTreeObserver.OnPreDrawListener mChildrenUpdater = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.1
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            NotificationStackScrollLayout.this.updateForcedScroll();
            NotificationStackScrollLayout.this.updateChildren();
            NotificationStackScrollLayout.this.mChildrenUpdateRequested = false;
            NotificationStackScrollLayout.this.getViewTreeObserver().removeOnPreDrawListener(this);
            return true;
        }
    };
    private int[] mTempInt2 = new int[2];
    private HashSet<Runnable> mAnimationFinishedRunnables = new HashSet<>();
    private HashSet<ExpandableView> mClearTransientViewsWhenFinished = new HashSet<>();
    private HashSet<Pair<ExpandableNotificationRow, Boolean>> mHeadsUpChangeAnimations = new HashSet<>();
    private final ArrayList<Pair<ExpandableNotificationRow, Boolean>> mTmpList = new ArrayList<>();
    private ViewTreeObserver.OnPreDrawListener mRunningAnimationUpdater = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.2
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            NotificationStackScrollLayout.this.onPreDrawDuringAnimation();
            return true;
        }
    };
    private ArrayList<ExpandableView> mTmpSortedChildren = new ArrayList<>();
    private final Animator.AnimatorListener mDimEndListener = new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.3
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            NotificationStackScrollLayout.this.mDimAnimator = null;
        }
    };
    private ValueAnimator.AnimatorUpdateListener mDimUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.4
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            NotificationStackScrollLayout.this.setDimAmount(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    };
    private ViewTreeObserver.OnPreDrawListener mShadowUpdater = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda3
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public final boolean onPreDraw() {
            return NotificationStackScrollLayout.this.lambda$new$0();
        }
    };
    private ViewTreeObserver.OnPreDrawListener mBackgroundUpdater = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda4
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public final boolean onPreDraw() {
            return NotificationStackScrollLayout.this.lambda$new$1();
        }
    };
    private Comparator<ExpandableView> mViewPositionComparator = NotificationStackScrollLayout$$ExternalSyntheticLambda10.INSTANCE;
    private float mInterpolatedHideAmount = 0.0f;
    private float mLinearHideAmount = 0.0f;
    private float mBackgroundXFactor = 1.0f;
    private int mMaxDisplayedNotifications = -1;
    private final Rect mClipRect = new Rect();
    private boolean mHeadsUpGoingAwayAnimationsAllowed = true;
    private Runnable mReflingAndAnimateScroll = new Runnable() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda6
        @Override // java.lang.Runnable
        public final void run() {
            NotificationStackScrollLayout.this.lambda$new$3();
        }
    };
    private final Rect mBackgroundAnimationRect = new Rect();
    private ArrayList<BiConsumer<Float, Float>> mExpandedHeightListeners = new ArrayList<>();
    private final Rect mTmpRect = new Rect();
    private Interpolator mHideXInterpolator = Interpolators.FAST_OUT_SLOW_IN;
    private final Path mRoundedClipPath = new Path();
    private final Path mLaunchedNotificationClipPath = new Path();
    private boolean mShouldUseRoundedRectClipping = false;
    private float[] mBgCornerRadii = new float[8];
    private boolean mAnimateStackYForContentHeightChange = false;
    private float[] mLaunchedNotificationRadii = new float[8];
    private boolean mDismissUsingRowTranslationX = true;
    private final ExpandableView.OnHeightChangedListener mOnChildHeightChangedListener = new ExpandableView.OnHeightChangedListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.6
        @Override // com.android.systemui.statusbar.notification.row.ExpandableView.OnHeightChangedListener
        public void onHeightChanged(ExpandableView expandableView, boolean z) {
            NotificationStackScrollLayout.this.onChildHeightChanged(expandableView, z);
        }

        @Override // com.android.systemui.statusbar.notification.row.ExpandableView.OnHeightChangedListener
        public void onReset(ExpandableView expandableView) {
            NotificationStackScrollLayout.this.onChildHeightReset(expandableView);
        }
    };
    private Runnable mReclamp = new Runnable() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.8
        @Override // java.lang.Runnable
        public void run() {
            NotificationStackScrollLayout.this.mScroller.startScroll(((ViewGroup) NotificationStackScrollLayout.this).mScrollX, NotificationStackScrollLayout.this.mOwnScrollY, 0, NotificationStackScrollLayout.this.getScrollRange() - NotificationStackScrollLayout.this.mOwnScrollY);
            NotificationStackScrollLayout.this.mDontReportNextOverScroll = true;
            NotificationStackScrollLayout.this.mDontClampNextScroll = true;
            NotificationStackScrollLayout.this.lambda$new$3();
        }
    };
    private final HeadsUpTouchHelper.Callback mHeadsUpCallback = new HeadsUpTouchHelper.Callback() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.9
        @Override // com.android.systemui.statusbar.phone.HeadsUpTouchHelper.Callback
        public ExpandableView getChildAtRawPosition(float f, float f2) {
            return NotificationStackScrollLayout.this.getChildAtRawPosition(f, f2);
        }

        @Override // com.android.systemui.statusbar.phone.HeadsUpTouchHelper.Callback
        public boolean isExpanded() {
            return NotificationStackScrollLayout.this.mIsExpanded;
        }

        @Override // com.android.systemui.statusbar.phone.HeadsUpTouchHelper.Callback
        public Context getContext() {
            return ((ViewGroup) NotificationStackScrollLayout.this).mContext;
        }
    };
    private ExpandHelper.Callback mExpandHelperCallback = new ExpandHelper.Callback() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.11
        @Override // com.android.systemui.ExpandHelper.Callback
        public ExpandableView getChildAtPosition(float f, float f2) {
            return NotificationStackScrollLayout.this.getChildAtPosition(f, f2);
        }

        @Override // com.android.systemui.ExpandHelper.Callback
        public ExpandableView getChildAtRawPosition(float f, float f2) {
            return NotificationStackScrollLayout.this.getChildAtRawPosition(f, f2);
        }

        @Override // com.android.systemui.ExpandHelper.Callback
        public boolean canChildBeExpanded(View view) {
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (expandableNotificationRow.isExpandable() && !expandableNotificationRow.areGutsExposed() && (NotificationStackScrollLayout.this.mIsExpanded || !expandableNotificationRow.isPinned())) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.android.systemui.ExpandHelper.Callback
        public void setUserExpandedChild(View view, boolean z) {
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (!z || !NotificationStackScrollLayout.this.onKeyguard()) {
                    expandableNotificationRow.setUserExpanded(z, true);
                    expandableNotificationRow.onExpandedByGesture(z);
                    return;
                }
                expandableNotificationRow.setUserLocked(false);
                NotificationStackScrollLayout.this.updateContentHeight();
                NotificationStackScrollLayout.this.notifyHeightChangeListener(expandableNotificationRow);
            }
        }

        @Override // com.android.systemui.ExpandHelper.Callback
        public void setExpansionCancelled(View view) {
            if (view instanceof ExpandableNotificationRow) {
                ((ExpandableNotificationRow) view).setGroupExpansionChanging(false);
            }
        }

        @Override // com.android.systemui.ExpandHelper.Callback
        public void setUserLockedChild(View view, boolean z) {
            if (view instanceof ExpandableNotificationRow) {
                ((ExpandableNotificationRow) view).setUserLocked(z);
            }
            NotificationStackScrollLayout.this.cancelLongPress();
            NotificationStackScrollLayout.this.requestDisallowInterceptTouchEvent(true);
        }

        @Override // com.android.systemui.ExpandHelper.Callback
        public void expansionStateChanged(boolean z) {
            NotificationStackScrollLayout.this.mExpandingNotification = z;
            if (!NotificationStackScrollLayout.this.mExpandedInThisMotion) {
                NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayout.this;
                notificationStackScrollLayout.mMaxScrollAfterExpand = notificationStackScrollLayout.mOwnScrollY;
                NotificationStackScrollLayout.this.mExpandedInThisMotion = true;
            }
        }

        @Override // com.android.systemui.ExpandHelper.Callback
        public int getMaxExpandHeight(ExpandableView expandableView) {
            return expandableView.getMaxContentHeight();
        }
    };
    private int mBgColor = Utils.getColorAttr(((ViewGroup) this).mContext, 16844002).getDefaultColor();

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface DismissAllAnimationListener {
        void onAnimationEnd(List<ExpandableNotificationRow> list, int i);
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface DismissListener {
        void onDismiss(int i);
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface FooterDismissListener {
        void onDismiss();
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface KeyguardBypassEnabledProvider {
        boolean getBypassEnabled();
    }

    /* loaded from: classes.dex */
    public interface OnEmptySpaceClickListener {
        void onEmptySpaceClicked(float f, float f2);
    }

    /* loaded from: classes.dex */
    public interface OnOverscrollTopChangedListener {
        void flingTopOverscroll(float f, boolean z);

        void onOverscrollTopChanged(float f, boolean z);
    }

    public ViewGroup getViewParentForNotification(NotificationEntry notificationEntry) {
        return this;
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0() {
        updateViewShadows();
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1() {
        updateBackground();
        return true;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ int lambda$new$2(ExpandableView expandableView, ExpandableView expandableView2) {
        float translationY = expandableView.getTranslationY() + ((float) expandableView.getActualHeight());
        float translationY2 = expandableView2.getTranslationY() + ((float) expandableView2.getActualHeight());
        if (translationY < translationY2) {
            return -1;
        }
        return translationY > translationY2 ? 1 : 0;
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationStackScrollLayout(Context context, AttributeSet attributeSet, NotificationSectionsManager notificationSectionsManager, GroupMembershipManager groupMembershipManager, GroupExpansionManager groupExpansionManager, AmbientState ambientState, FeatureFlags featureFlags, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        super(context, attributeSet, 0, 0);
        boolean z = false;
        Paint paint = new Paint();
        this.mBackgroundPaint = paint;
        AnonymousClass5 r3 = new ViewOutlineProvider() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.5
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                if (NotificationStackScrollLayout.this.mAmbientState.isHiddenAtAll()) {
                    outline.setRoundRect(NotificationStackScrollLayout.this.mBackgroundAnimationRect, MathUtils.lerp(((float) NotificationStackScrollLayout.this.mCornerRadius) / 2.0f, (float) NotificationStackScrollLayout.this.mCornerRadius, NotificationStackScrollLayout.this.mHideXInterpolator.getInterpolation((1.0f - NotificationStackScrollLayout.this.mLinearHideAmount) * NotificationStackScrollLayout.this.mBackgroundXFactor)));
                    outline.setAlpha(1.0f - NotificationStackScrollLayout.this.mAmbientState.getHideAmount());
                    return;
                }
                ViewOutlineProvider.BACKGROUND.getOutline(view, outline);
            }
        };
        this.mOutlineProvider = r3;
        AnonymousClass7 r1 = new ScrollAdapter() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.7
            @Override // com.android.systemui.statusbar.policy.ScrollAdapter
            public boolean isScrolledToTop() {
                return NotificationStackScrollLayout.this.mOwnScrollY == 0;
            }

            @Override // com.android.systemui.statusbar.policy.ScrollAdapter
            public boolean isScrolledToBottom() {
                return NotificationStackScrollLayout.this.mOwnScrollY >= NotificationStackScrollLayout.this.getScrollRange();
            }

            @Override // com.android.systemui.statusbar.policy.ScrollAdapter
            public View getHostView() {
                return NotificationStackScrollLayout.this;
            }
        };
        this.mScrollAdapter = r1;
        Resources resources = getResources();
        this.mSectionsManager = notificationSectionsManager;
        this.mFeatureFlags = featureFlags;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        updateSplitNotificationShade();
        notificationSectionsManager.initialize(this, LayoutInflater.from(context));
        this.mSections = notificationSectionsManager.createSectionsForBuckets();
        this.mAmbientState = ambientState;
        ExpandHelper expandHelper = new ExpandHelper(getContext(), this.mExpandHelperCallback, resources.getDimensionPixelSize(R$dimen.notification_min_height), resources.getDimensionPixelSize(R$dimen.notification_max_height));
        this.mExpandHelper = expandHelper;
        expandHelper.setEventSource(this);
        this.mExpandHelper.setScrollAdapter(r1);
        this.mStackScrollAlgorithm = createStackScrollAlgorithm(context);
        boolean z2 = resources.getBoolean(R$bool.config_drawNotificationBackground);
        this.mShouldDrawNotificationBackground = z2;
        setOutlineProvider(r3);
        setWillNotDraw(!((z2 || DEBUG) ? true : z));
        paint.setAntiAlias(true);
        if (DEBUG) {
            Paint paint2 = new Paint();
            this.mDebugPaint = paint2;
            paint2.setColor(-65536);
            this.mDebugPaint.setStrokeWidth(2.0f);
            this.mDebugPaint.setStyle(Paint.Style.STROKE);
            this.mDebugPaint.setTextSize(25.0f);
        }
        this.mClearAllEnabled = resources.getBoolean(R$bool.config_enableNotificationsClearAll);
        this.mGroupMembershipManager = groupMembershipManager;
        this.mGroupExpansionManager = groupExpansionManager;
    }

    /* access modifiers changed from: package-private */
    public void initializeForegroundServiceSection(ForegroundServiceDungeonView foregroundServiceDungeonView) {
        if (this.mFgsSectionView == null) {
            this.mFgsSectionView = foregroundServiceDungeonView;
            addView(foregroundServiceDungeonView, -1);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateDismissRtlSetting(boolean z) {
        this.mDismissRtl = z;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof ExpandableNotificationRow) {
                ((ExpandableNotificationRow) childAt).setDismissRtl(z);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setOverExpansion(float f) {
        this.mAmbientState.setOverExpansion(f);
        updateStackPosition();
        requestChildrenUpdate();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflateEmptyShadeView();
        inflateFooterView();
    }

    public float getWakeUpHeight() {
        int collapsedHeight;
        ExpandableView firstChildWithBackground = getFirstChildWithBackground();
        if (firstChildWithBackground == null) {
            return 0.0f;
        }
        if (this.mKeyguardBypassEnabledProvider.getBypassEnabled()) {
            collapsedHeight = firstChildWithBackground.getHeadsUpHeightWithoutHeader();
        } else {
            collapsedHeight = firstChildWithBackground.getCollapsedHeight();
        }
        return (float) collapsedHeight;
    }

    /* access modifiers changed from: package-private */
    public void reinflateViews() {
        inflateFooterView();
        inflateEmptyShadeView();
        updateFooter();
        this.mSectionsManager.reinflateViews(LayoutInflater.from(((ViewGroup) this).mContext));
    }

    @VisibleForTesting
    public void updateFooter() {
        if (this.mFooterView != null) {
            boolean z = true;
            boolean z2 = this.mClearAllEnabled && this.mController.hasActiveClearableNotifications(0);
            RemoteInputController controller = this.mRemoteInputManager.getController();
            boolean z3 = (z2 || getVisibleNotificationCount() > 0) && this.mStatusBarState != 1 && !this.mUnlockedScreenOffAnimationController.isScreenOffAnimationPlaying() && (controller == null || !controller.isRemoteInputActive());
            if (Settings.Secure.getIntForUser(((ViewGroup) this).mContext.getContentResolver(), "notification_history_enabled", 0, -2) != 1) {
                z = false;
            }
            updateFooterView(z3, z2, z);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean hasActiveClearableNotifications(int i) {
        return this.mController.hasActiveClearableNotifications(i);
    }

    /* access modifiers changed from: package-private */
    public void updateBgColor() {
        this.mBgColor = Utils.getColorAttr(((ViewGroup) this).mContext, 16844002).getDefaultColor();
        updateBackgroundDimming();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof ActivatableNotificationView) {
                ((ActivatableNotificationView) childAt).updateBackgroundColors();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onDraw(android.graphics.Canvas r10) {
        /*
            r9 = this;
            boolean r0 = r9.mShouldDrawNotificationBackground
            if (r0 == 0) goto L_0x002a
            com.android.systemui.statusbar.notification.stack.NotificationSection[] r0 = r9.mSections
            r1 = 0
            r0 = r0[r1]
            android.graphics.Rect r0 = r0.getCurrentBounds()
            int r0 = r0.top
            com.android.systemui.statusbar.notification.stack.NotificationSection[] r1 = r9.mSections
            int r2 = r1.length
            int r2 = r2 + -1
            r1 = r1[r2]
            android.graphics.Rect r1 = r1.getCurrentBounds()
            int r1 = r1.bottom
            if (r0 < r1) goto L_0x0026
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r9.mAmbientState
            boolean r0 = r0.isDozing()
            if (r0 == 0) goto L_0x002a
        L_0x0026:
            r9.drawBackground(r10)
            goto L_0x0035
        L_0x002a:
            boolean r0 = r9.mInHeadsUpPinnedMode
            if (r0 != 0) goto L_0x0032
            boolean r0 = r9.mHeadsUpAnimatingAway
            if (r0 == 0) goto L_0x0035
        L_0x0032:
            r9.drawHeadsUpBackground(r10)
        L_0x0035:
            boolean r0 = com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.DEBUG
            if (r0 == 0) goto L_0x00c1
            int r0 = r9.mTopPadding
            android.graphics.Paint r1 = r9.mDebugPaint
            r2 = -65536(0xffffffffffff0000, float:NaN)
            r1.setColor(r2)
            r4 = 0
            float r7 = (float) r0
            int r0 = r9.getWidth()
            float r6 = (float) r0
            android.graphics.Paint r8 = r9.mDebugPaint
            r3 = r10
            r5 = r7
            r3.drawLine(r4, r5, r6, r7, r8)
            int r0 = r9.getLayoutHeight()
            android.graphics.Paint r1 = r9.mDebugPaint
            r2 = -256(0xffffffffffffff00, float:NaN)
            r1.setColor(r2)
            float r7 = (float) r0
            int r0 = r9.getWidth()
            float r6 = (float) r0
            android.graphics.Paint r8 = r9.mDebugPaint
            r5 = r7
            r3.drawLine(r4, r5, r6, r7, r8)
            int r0 = r9.getHeight()
            int r1 = r9.getEmptyBottomMargin()
            int r0 = r0 - r1
            android.graphics.Paint r1 = r9.mDebugPaint
            r2 = -16711936(0xffffffffff00ff00, float:-1.7146522E38)
            r1.setColor(r2)
            float r7 = (float) r0
            int r0 = r9.getWidth()
            float r6 = (float) r0
            android.graphics.Paint r8 = r9.mDebugPaint
            r5 = r7
            r3.drawLine(r4, r5, r6, r7, r8)
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r9.mAmbientState
            float r0 = r0.getStackY()
            int r0 = (int) r0
            android.graphics.Paint r1 = r9.mDebugPaint
            r2 = -16711681(0xffffffffff00ffff, float:-1.714704E38)
            r1.setColor(r2)
            float r7 = (float) r0
            int r0 = r9.getWidth()
            float r6 = (float) r0
            android.graphics.Paint r8 = r9.mDebugPaint
            r5 = r7
            r3.drawLine(r4, r5, r6, r7, r8)
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r9.mAmbientState
            float r0 = r0.getStackY()
            com.android.systemui.statusbar.notification.stack.AmbientState r1 = r9.mAmbientState
            float r1 = r1.getStackHeight()
            float r0 = r0 + r1
            int r0 = (int) r0
            android.graphics.Paint r1 = r9.mDebugPaint
            r2 = -16776961(0xffffffffff0000ff, float:-1.7014636E38)
            r1.setColor(r2)
            float r7 = (float) r0
            int r0 = r9.getWidth()
            float r6 = (float) r0
            android.graphics.Paint r8 = r9.mDebugPaint
            r5 = r7
            r3.drawLine(r4, r5, r6, r7, r8)
        L_0x00c1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.onDraw(android.graphics.Canvas):void");
    }

    private void drawBackground(Canvas canvas) {
        boolean z;
        boolean z2;
        int i = this.mSidePaddings;
        int width = getWidth() - this.mSidePaddings;
        boolean z3 = false;
        int i2 = this.mSections[0].getCurrentBounds().top;
        NotificationSection[] notificationSectionArr = this.mSections;
        int i3 = notificationSectionArr[notificationSectionArr.length - 1].getCurrentBounds().bottom;
        int i4 = this.mTopPadding;
        float f = 1.0f - this.mInterpolatedHideAmount;
        float interpolation = this.mHideXInterpolator.getInterpolation((1.0f - this.mLinearHideAmount) * this.mBackgroundXFactor);
        float width2 = (float) (getWidth() / 2);
        int lerp = (int) MathUtils.lerp(width2, (float) i, interpolation);
        int lerp2 = (int) MathUtils.lerp(width2, (float) width, interpolation);
        float f2 = (float) i4;
        int lerp3 = (int) MathUtils.lerp(f2, (float) i2, f);
        this.mBackgroundAnimationRect.set(lerp, lerp3, lerp2, (int) MathUtils.lerp(f2, (float) i3, f));
        int i5 = lerp3 - i2;
        NotificationSection[] notificationSectionArr2 = this.mSections;
        int length = notificationSectionArr2.length;
        int i6 = 0;
        while (true) {
            if (i6 >= length) {
                z = false;
                break;
            } else if (notificationSectionArr2[i6].needsBackground()) {
                z = true;
                break;
            } else {
                i6++;
            }
        }
        if (!this.mKeyguardBypassEnabledProvider.getBypassEnabled() || !onKeyguard()) {
            if (!this.mAmbientState.isDozing() || z) {
                z3 = true;
            }
            z2 = z3;
        } else {
            z2 = isPulseExpanding();
        }
        if (z2) {
            drawBackgroundRects(canvas, lerp, lerp2, lerp3, i5);
        }
        updateClipping();
    }

    private void drawBackgroundRects(Canvas canvas, int i, int i2, int i3, int i4) {
        int i5 = i2;
        NotificationSection[] notificationSectionArr = this.mSections;
        int length = notificationSectionArr.length;
        int i6 = 1;
        int i7 = i;
        int i8 = i5;
        int i9 = this.mSections[0].getCurrentBounds().bottom + i4;
        int i10 = 0;
        boolean z = true;
        int i11 = i3;
        while (i10 < length) {
            NotificationSection notificationSection = notificationSectionArr[i10];
            if (notificationSection.needsBackground()) {
                int i12 = notificationSection.getCurrentBounds().top + i4;
                int min = Math.min(Math.max(i, notificationSection.getCurrentBounds().left), i5);
                int max = Math.max(Math.min(i5, notificationSection.getCurrentBounds().right), min);
                if (i12 - i9 > i6 || (!(i7 == min && i8 == max) && !z)) {
                    int i13 = this.mCornerRadius;
                    canvas.drawRoundRect((float) i7, (float) i11, (float) i8, (float) i9, (float) i13, (float) i13, this.mBackgroundPaint);
                    i11 = i12;
                }
                i9 = notificationSection.getCurrentBounds().bottom + i4;
                i8 = max;
                i7 = min;
                z = false;
            }
            i10++;
            i5 = i2;
            i6 = 1;
        }
        int i14 = this.mCornerRadius;
        canvas.drawRoundRect((float) i7, (float) i11, (float) i8, (float) i9, (float) i14, (float) i14, this.mBackgroundPaint);
    }

    private void drawHeadsUpBackground(Canvas canvas) {
        int i = this.mSidePaddings;
        int width = getWidth() - this.mSidePaddings;
        int childCount = getChildCount();
        float height = (float) getHeight();
        float f = 0.0f;
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if (childAt.getVisibility() != 8 && (childAt instanceof ExpandableNotificationRow)) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                if ((expandableNotificationRow.isPinned() || expandableNotificationRow.isHeadsUpAnimatingAway()) && expandableNotificationRow.getTranslation() < 0.0f && expandableNotificationRow.getProvider().shouldShowGutsOnSnapOpen()) {
                    float min = Math.min(height, expandableNotificationRow.getTranslationY());
                    f = Math.max(f, expandableNotificationRow.getTranslationY() + ((float) expandableNotificationRow.getActualHeight()));
                    height = min;
                }
            }
        }
        if (height < f) {
            int i3 = this.mCornerRadius;
            canvas.drawRoundRect((float) i, height, (float) width, f, (float) i3, (float) i3, this.mBackgroundPaint);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateBackgroundDimming() {
        int blendARGB;
        if (this.mShouldDrawNotificationBackground && this.mCachedBackgroundColor != (blendARGB = ColorUtils.blendARGB(this.mBgColor, -1, MathUtils.smoothStep(0.4f, 1.0f, this.mLinearHideAmount)))) {
            this.mCachedBackgroundColor = blendARGB;
            this.mBackgroundPaint.setColor(blendARGB);
            invalidate();
        }
    }

    private void reinitView() {
        initView(getContext(), this.mKeyguardBypassEnabledProvider, this.mSwipeHelper);
    }

    /* access modifiers changed from: package-private */
    public void initView(Context context, KeyguardBypassEnabledProvider keyguardBypassEnabledProvider, NotificationSwipeHelper notificationSwipeHelper) {
        this.mScroller = new OverScroller(getContext());
        this.mKeyguardBypassEnabledProvider = keyguardBypassEnabledProvider;
        this.mSwipeHelper = notificationSwipeHelper;
        setDescendantFocusability(262144);
        setClipChildren(false);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mSlopMultiplier = viewConfiguration.getScaledAmbiguousGestureMultiplier();
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mOverflingDistance = viewConfiguration.getScaledOverflingDistance();
        Resources resources = context.getResources();
        this.mCollapsedSize = resources.getDimensionPixelSize(R$dimen.notification_min_height);
        this.mGapHeight = resources.getDimensionPixelSize(R$dimen.notification_section_divider_height);
        this.mStackScrollAlgorithm.initView(context);
        this.mAmbientState.reload(context);
        this.mPaddingBetweenElements = Math.max(1, resources.getDimensionPixelSize(R$dimen.notification_divider_height));
        this.mMinTopOverScrollToEscape = (float) resources.getDimensionPixelSize(R$dimen.min_top_overscroll_to_qs);
        this.mStatusBarHeight = resources.getDimensionPixelSize(R$dimen.status_bar_height);
        this.mBottomMargin = resources.getDimensionPixelSize(R$dimen.notification_panel_margin_bottom);
        this.mMinimumPaddings = resources.getDimensionPixelSize(R$dimen.notification_side_paddings);
        this.mQsTilePadding = resources.getDimensionPixelOffset(R$dimen.qs_tile_margin_horizontal);
        this.mSkinnyNotifsInLandscape = resources.getBoolean(R$bool.config_skinnyNotifsInLandscape);
        this.mSidePaddings = this.mMinimumPaddings;
        this.mMinInteractionHeight = resources.getDimensionPixelSize(R$dimen.notification_min_interaction_height);
        this.mCornerRadius = resources.getDimensionPixelSize(R$dimen.notification_corner_radius);
        this.mHeadsUpInset = this.mStatusBarHeight + resources.getDimensionPixelSize(R$dimen.heads_up_status_bar_padding);
        this.mQsScrollBoundaryPosition = resources.getDimensionPixelSize(17105475);
    }

    void updateSidePadding(int i) {
        if (i == 0 || !this.mSkinnyNotifsInLandscape) {
            this.mSidePaddings = this.mMinimumPaddings;
        } else if (RotationUtils.getRotation(((ViewGroup) this).mContext) == 0) {
            this.mSidePaddings = this.mMinimumPaddings;
        } else {
            int i2 = this.mMinimumPaddings;
            int i3 = this.mQsTilePadding;
            this.mSidePaddings = i2 + (((i - (i2 * 2)) - (i3 * 3)) / 4) + i3;
        }
    }

    /* access modifiers changed from: package-private */
    public void updateCornerRadius() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.notification_corner_radius);
        if (this.mCornerRadius != dimensionPixelSize) {
            this.mCornerRadius = dimensionPixelSize;
            invalidate();
        }
    }

    /* access modifiers changed from: private */
    public void notifyHeightChangeListener(ExpandableView expandableView) {
        notifyHeightChangeListener(expandableView, false);
    }

    private void notifyHeightChangeListener(ExpandableView expandableView, boolean z) {
        ExpandableView.OnHeightChangedListener onHeightChangedListener = this.mOnHeightChangedListener;
        if (onHeightChangedListener != null) {
            onHeightChangedListener.onHeightChanged(expandableView, z);
        }
    }

    public boolean isPulseExpanding() {
        return this.mAmbientState.isPulseExpanding();
    }

    public int getSpeedBumpIndex() {
        if (this.mSpeedBumpIndexDirty) {
            this.mSpeedBumpIndexDirty = false;
            int childCount = getChildCount();
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (childAt.getVisibility() != 8 && (childAt instanceof ExpandableNotificationRow)) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                    i2++;
                    boolean z = true;
                    if (!this.mHighPriorityBeforeSpeedBump) {
                        z = true ^ expandableNotificationRow.getEntry().isAmbient();
                    } else if (expandableNotificationRow.getEntry().getBucket() >= 6) {
                        z = false;
                    }
                    if (z) {
                        i = i2;
                    }
                }
            }
            this.mSpeedBumpIndex = i;
        }
        return this.mSpeedBumpIndex;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i);
        updateSidePadding(size);
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size - (this.mSidePaddings * 2), View.MeasureSpec.getMode(i));
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 0);
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            measureChild(getChildAt(i3), makeMeasureSpec, makeMeasureSpec2);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float width = ((float) getWidth()) / 2.0f;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            float measuredWidth = ((float) childAt.getMeasuredWidth()) / 2.0f;
            childAt.layout((int) (width - measuredWidth), 0, (int) (measuredWidth + width), (int) ((float) childAt.getMeasuredHeight()));
        }
        setMaxLayoutHeight(getHeight());
        updateContentHeight();
        clampScrollPosition();
        requestChildrenUpdate();
        updateFirstAndLastBackgroundViews();
        updateAlgorithmLayoutMinHeight();
        updateOwnTranslationZ();
        this.mAnimateStackYForContentHeightChange = false;
    }

    private void requestAnimationOnViewResize(ExpandableNotificationRow expandableNotificationRow) {
        if (!this.mAnimationsEnabled) {
            return;
        }
        if (this.mIsExpanded || (expandableNotificationRow != null && expandableNotificationRow.isPinned())) {
            this.mNeedViewResizeAnimation = true;
            this.mNeedsAnimation = true;
        }
    }

    public void setChildLocationsChangedListener(NotificationLogger.OnChildLocationsChangedListener onChildLocationsChangedListener) {
        this.mListener = onChildLocationsChangedListener;
    }

    private void setMaxLayoutHeight(int i) {
        this.mMaxLayoutHeight = i;
        updateAlgorithmHeightAndPadding();
    }

    private void updateAlgorithmHeightAndPadding() {
        this.mAmbientState.setLayoutHeight(getLayoutHeight());
        updateAlgorithmLayoutMinHeight();
        this.mAmbientState.setTopPadding(this.mTopPadding);
    }

    private void updateAlgorithmLayoutMinHeight() {
        int i;
        AmbientState ambientState = this.mAmbientState;
        if (this.mQsExpanded || isHeadsUpTransition()) {
            i = getLayoutMinHeight();
        } else {
            i = 0;
        }
        ambientState.setLayoutMinHeight(i);
    }

    /* access modifiers changed from: private */
    public void updateChildren() {
        float f;
        updateScrollStateForAddedChildren();
        AmbientState ambientState = this.mAmbientState;
        if (this.mScroller.isFinished()) {
            f = 0.0f;
        } else {
            f = this.mScroller.getCurrVelocity();
        }
        ambientState.setCurrentScrollVelocity(f);
        this.mStackScrollAlgorithm.resetViewStates(this.mAmbientState, getSpeedBumpIndex());
        if (isCurrentlyAnimating() || this.mNeedsAnimation) {
            startAnimationToState();
        } else {
            applyCurrentState();
        }
    }

    /* access modifiers changed from: private */
    public void onPreDrawDuringAnimation() {
        this.mShelf.updateAppearance();
        if (!this.mNeedsAnimation && !this.mChildrenUpdateRequested) {
            updateBackground();
        }
    }

    private void updateScrollStateForAddedChildren() {
        if (!this.mChildrenToAddAnimated.isEmpty()) {
            for (int i = 0; i < getChildCount(); i++) {
                ExpandableView expandableView = (ExpandableView) getChildAt(i);
                if (this.mChildrenToAddAnimated.contains(expandableView)) {
                    int positionInLinearLayout = getPositionInLinearLayout(expandableView);
                    int intrinsicHeight = getIntrinsicHeight(expandableView) + this.mPaddingBetweenElements;
                    int i2 = this.mOwnScrollY;
                    if (positionInLinearLayout < i2) {
                        setOwnScrollY(i2 + intrinsicHeight);
                    }
                }
            }
            clampScrollPosition();
        }
    }

    /* access modifiers changed from: private */
    public void updateForcedScroll() {
        View view = this.mForcedScroll;
        if (view != null && (!view.hasFocus() || !this.mForcedScroll.isAttachedToWindow())) {
            this.mForcedScroll = null;
        }
        View view2 = this.mForcedScroll;
        if (view2 != null) {
            ExpandableView expandableView = (ExpandableView) view2;
            int positionInLinearLayout = getPositionInLinearLayout(expandableView);
            int targetScrollForView = targetScrollForView(expandableView, positionInLinearLayout);
            int intrinsicHeight = positionInLinearLayout + expandableView.getIntrinsicHeight();
            int max = Math.max(0, Math.min(targetScrollForView, getScrollRange()));
            int i = this.mOwnScrollY;
            if (i < max || intrinsicHeight < i) {
                setOwnScrollY(max);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void requestChildrenUpdate() {
        if (!this.mChildrenUpdateRequested) {
            getViewTreeObserver().addOnPreDrawListener(this.mChildrenUpdater);
            this.mChildrenUpdateRequested = true;
            invalidate();
        }
    }

    public int getVisibleNotificationCount() {
        int i = 0;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            if (childAt.getVisibility() != 8 && (childAt instanceof ExpandableNotificationRow)) {
                i++;
            }
        }
        return i;
    }

    private boolean isCurrentlyAnimating() {
        return this.mStateAnimator.isRunning();
    }

    private void clampScrollPosition() {
        int scrollRange = getScrollRange();
        if (scrollRange < this.mOwnScrollY) {
            boolean z = false;
            if (scrollRange < getScrollAmountToScrollBoundary() && this.mAnimateStackYForContentHeightChange) {
                z = true;
            }
            setOwnScrollY(scrollRange, z);
        }
    }

    public int getTopPadding() {
        return this.mTopPadding;
    }

    private void setTopPadding(int i, boolean z) {
        if (this.mTopPadding != i) {
            boolean z2 = z || this.mAnimateNextTopPaddingChange;
            this.mTopPadding = i;
            updateAlgorithmHeightAndPadding();
            updateContentHeight();
            if (z2 && this.mAnimationsEnabled && this.mIsExpanded) {
                this.mTopPaddingNeedsAnimation = true;
                this.mNeedsAnimation = true;
            }
            updateStackPosition();
            requestChildrenUpdate();
            notifyHeightChangeListener(null, z2);
            this.mAnimateNextTopPaddingChange = false;
        }
    }

    private void updateStackPosition() {
        updateStackPosition(false);
    }

    private void updateStackPosition(boolean z) {
        float overExpansion = ((((float) this.mTopPadding) + this.mExtraTopInsetForFullShadeTransition) + this.mAmbientState.getOverExpansion()) - getCurrentOverScrollAmount(false);
        float expansionFraction = this.mAmbientState.getExpansionFraction();
        this.mAmbientState.setStackY(MathUtils.lerp(0.0f, overExpansion, expansionFraction));
        Consumer<Boolean> consumer = this.mOnStackYChanged;
        if (consumer != null) {
            consumer.accept(Boolean.valueOf(z));
        }
        if (this.mQsExpansionFraction <= 0.0f) {
            float max = Math.max(0.0f, (float) ((getHeight() - getEmptyBottomMargin()) - this.mTopPadding));
            this.mAmbientState.setStackEndHeight(max);
            this.mAmbientState.setStackHeight(MathUtils.lerp(0.3f * max, max, expansionFraction));
        }
    }

    /* access modifiers changed from: package-private */
    public void setOnStackYChanged(Consumer<Boolean> consumer) {
        this.mOnStackYChanged = consumer;
    }

    public void setExpandedHeight(float f) {
        int i;
        float f2;
        this.mAmbientState.setExpansionFraction(MathUtils.saturate(f / ((float) (getHeight() - getEmptyBottomMargin()))));
        updateStackPosition();
        this.mExpandedHeight = f;
        float f3 = 0.0f;
        boolean z = true;
        setIsExpanded(f > 0.0f);
        float minExpansionHeight = (float) getMinExpansionHeight();
        if (f < minExpansionHeight) {
            Rect rect = this.mClipRect;
            rect.left = 0;
            rect.right = getWidth();
            Rect rect2 = this.mClipRect;
            rect2.top = 0;
            rect2.bottom = (int) f;
            setRequestedClipBounds(rect2);
            f = minExpansionHeight;
        } else {
            setRequestedClipBounds(null);
        }
        float appearEndPosition = getAppearEndPosition();
        float appearStartPosition = getAppearStartPosition();
        float f4 = 1.0f;
        if (f >= appearEndPosition) {
            z = false;
        }
        this.mAmbientState.setAppearing(z);
        if (z) {
            f4 = calculateAppearFraction(f);
            if (f4 >= 0.0f) {
                f2 = NotificationUtils.interpolate(getExpandTranslationStart(), 0.0f, f4);
            } else {
                f2 = (f - appearStartPosition) + getExpandTranslationStart();
            }
            i = (int) (f - f2);
            f3 = isHeadsUpTransition() ? MathUtils.lerp((float) (this.mHeadsUpInset - this.mTopPadding), 0.0f, f4) : f2;
        } else if (this.mShouldShowShelfOnly) {
            i = this.mTopPadding + this.mShelf.getIntrinsicHeight();
        } else {
            if (this.mQsExpanded) {
                int i2 = (this.mContentHeight - this.mTopPadding) + this.mIntrinsicPadding;
                int intrinsicHeight = this.mMaxTopPadding + this.mShelf.getIntrinsicHeight();
                if (i2 <= intrinsicHeight) {
                    i = intrinsicHeight;
                } else if (!this.mShouldUseSplitNotificationShade) {
                    f = NotificationUtils.interpolate((float) i2, (float) intrinsicHeight, this.mQsExpansionFraction);
                }
            }
            i = (int) f;
        }
        this.mAmbientState.setAppearFraction(f4);
        if (i != this.mCurrentStackHeight) {
            this.mCurrentStackHeight = i;
            updateAlgorithmHeightAndPadding();
            requestChildrenUpdate();
        }
        setStackTranslation(f3);
        notifyAppearChangedListeners();
    }

    private void notifyAppearChangedListeners() {
        float f;
        float f2;
        if (!this.mKeyguardBypassEnabledProvider.getBypassEnabled() || !onKeyguard()) {
            f2 = MathUtils.saturate(calculateAppearFraction(this.mExpandedHeight));
            f = this.mExpandedHeight;
        } else {
            f2 = calculateAppearFractionBypass();
            f = getPulseHeight();
        }
        if (!(f2 == this.mLastSentAppear && f == this.mLastSentExpandedHeight)) {
            this.mLastSentAppear = f2;
            this.mLastSentExpandedHeight = f;
            for (int i = 0; i < this.mExpandedHeightListeners.size(); i++) {
                this.mExpandedHeightListeners.get(i).accept(Float.valueOf(f), Float.valueOf(f2));
            }
        }
    }

    private void setRequestedClipBounds(Rect rect) {
        this.mRequestedClipBounds = rect;
        updateClipping();
    }

    public int getIntrinsicContentHeight() {
        return this.mIntrinsicContentHeight;
    }

    public void updateClipping() {
        boolean z = this.mRequestedClipBounds != null && !this.mInHeadsUpPinnedMode && !this.mHeadsUpAnimatingAway;
        if (this.mIsClipped != z) {
            this.mIsClipped = z;
        }
        if (this.mAmbientState.isHiddenAtAll()) {
            invalidateOutline();
            if (isFullyHidden()) {
                setClipBounds(null);
            }
        } else if (z) {
            setClipBounds(this.mRequestedClipBounds);
        } else {
            setClipBounds(null);
        }
        setClipToOutline(false);
    }

    private float getExpandTranslationStart() {
        return (float) (((-this.mTopPadding) + getMinExpansionHeight()) - this.mShelf.getIntrinsicHeight());
    }

    private float getAppearStartPosition() {
        int minExpansionHeight;
        if (isHeadsUpTransition()) {
            NotificationSection firstVisibleSection = getFirstVisibleSection();
            minExpansionHeight = this.mHeadsUpInset + (firstVisibleSection != null ? firstVisibleSection.getFirstVisibleChild().getPinnedHeadsUpHeight() : 0);
        } else {
            minExpansionHeight = getMinExpansionHeight();
        }
        return (float) minExpansionHeight;
    }

    private int getTopHeadsUpPinnedHeight() {
        NotificationEntry groupSummary;
        NotificationEntry notificationEntry = this.mTopHeadsUpEntry;
        if (notificationEntry == null) {
            return 0;
        }
        ExpandableNotificationRow row = notificationEntry.getRow();
        if (row.isChildInGroup() && (groupSummary = this.mGroupMembershipManager.getGroupSummary(row.getEntry())) != null) {
            row = groupSummary.getRow();
        }
        return row.getPinnedHeadsUpHeight();
    }

    private float getAppearEndPosition() {
        int i;
        int visibleNotificationCount = getVisibleNotificationCount();
        int i2 = 0;
        if (this.mEmptyShadeView.getVisibility() != 8 || visibleNotificationCount <= 0) {
            i2 = this.mEmptyShadeView.getHeight();
        } else {
            if (isHeadsUpTransition() || (this.mInHeadsUpPinnedMode && !this.mAmbientState.isDozing())) {
                if (this.mShelf.getVisibility() != 8 && visibleNotificationCount > 1) {
                    i2 = 0 + this.mShelf.getIntrinsicHeight() + this.mPaddingBetweenElements;
                }
                i = getTopHeadsUpPinnedHeight() + getPositionInLinearLayout(this.mAmbientState.getTrackedHeadsUpRow());
            } else if (this.mShelf.getVisibility() != 8) {
                i = this.mShelf.getIntrinsicHeight();
            }
            i2 += i;
        }
        return (float) (i2 + (onKeyguard() ? this.mTopPadding : this.mIntrinsicPadding));
    }

    private boolean isHeadsUpTransition() {
        return this.mAmbientState.getTrackedHeadsUpRow() != null;
    }

    public float calculateAppearFraction(float f) {
        float appearEndPosition = getAppearEndPosition();
        float appearStartPosition = getAppearStartPosition();
        return (f - appearStartPosition) / (appearEndPosition - appearStartPosition);
    }

    public float getStackTranslation() {
        return this.mStackTranslation;
    }

    private void setStackTranslation(float f) {
        if (f != this.mStackTranslation) {
            this.mStackTranslation = f;
            this.mAmbientState.setStackTranslation(f);
            requestChildrenUpdate();
        }
    }

    private int getLayoutHeight() {
        return Math.min(this.mMaxLayoutHeight, this.mCurrentStackHeight);
    }

    public void setQsContainer(ViewGroup viewGroup) {
        this.mQsContainer = viewGroup;
    }

    public static boolean isPinnedHeadsUp(View view) {
        if (!(view instanceof ExpandableNotificationRow)) {
            return false;
        }
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        if (!expandableNotificationRow.isHeadsUp() || !expandableNotificationRow.isPinned()) {
            return false;
        }
        return true;
    }

    private boolean isHeadsUp(View view) {
        if (view instanceof ExpandableNotificationRow) {
            return ((ExpandableNotificationRow) view).isHeadsUp();
        }
        return false;
    }

    /* access modifiers changed from: private */
    public ExpandableView getChildAtPosition(float f, float f2) {
        return getChildAtPosition(f, f2, true, true);
    }

    /* access modifiers changed from: package-private */
    public ExpandableView getChildAtPosition(float f, float f2, boolean z, boolean z2) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView.getVisibility() == 0 && (!z2 || !(expandableView instanceof StackScrollerDecorView))) {
                float translationY = expandableView.getTranslationY();
                float clipTopAmount = ((float) expandableView.getClipTopAmount()) + translationY;
                float actualHeight = (((float) expandableView.getActualHeight()) + translationY) - ((float) expandableView.getClipBottomAmount());
                int width = getWidth();
                if ((actualHeight - clipTopAmount >= ((float) this.mMinInteractionHeight) || !z) && f2 >= clipTopAmount && f2 <= actualHeight && f >= ((float) 0) && f <= ((float) width)) {
                    if (!(expandableView instanceof ExpandableNotificationRow)) {
                        return expandableView;
                    }
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
                    NotificationEntry entry = expandableNotificationRow.getEntry();
                    if (this.mIsExpanded || !expandableNotificationRow.isHeadsUp() || !expandableNotificationRow.isPinned() || this.mTopHeadsUpEntry.getRow() == expandableNotificationRow || this.mGroupMembershipManager.getGroupSummary(this.mTopHeadsUpEntry) == entry) {
                        return expandableNotificationRow.getViewAtPosition(f2 - translationY);
                    }
                }
            }
        }
        return null;
    }

    public ExpandableView getChildAtRawPosition(float f, float f2) {
        getLocationOnScreen(this.mTempInt2);
        int[] iArr = this.mTempInt2;
        return getChildAtPosition(f - ((float) iArr[0]), f2 - ((float) iArr[1]));
    }

    public void setScrollingEnabled(boolean z) {
        this.mScrollingEnabled = z;
    }

    public void lockScrollTo(View view) {
        if (this.mForcedScroll != view) {
            this.mForcedScroll = view;
            scrollTo(view);
        }
    }

    public boolean scrollTo(View view) {
        ExpandableView expandableView = (ExpandableView) view;
        int positionInLinearLayout = getPositionInLinearLayout(view);
        int targetScrollForView = targetScrollForView(expandableView, positionInLinearLayout);
        int intrinsicHeight = positionInLinearLayout + expandableView.getIntrinsicHeight();
        int i = this.mOwnScrollY;
        if (i >= targetScrollForView && intrinsicHeight >= i) {
            return false;
        }
        this.mScroller.startScroll(((ViewGroup) this).mScrollX, i, 0, targetScrollForView - i);
        this.mDontReportNextOverScroll = true;
        lambda$new$3();
        return true;
    }

    private int targetScrollForView(ExpandableView expandableView, int i) {
        return (((i + expandableView.getIntrinsicHeight()) + getImeInset()) - getHeight()) + ((isExpanded() || !isPinnedHeadsUp(expandableView)) ? getTopPadding() : this.mHeadsUpInset);
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mBottomInset = windowInsets.getSystemWindowInsetBottom();
        this.mWaterfallTopInset = 0;
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        if (displayCutout != null) {
            this.mWaterfallTopInset = displayCutout.getWaterfallInsets().top;
        }
        if (this.mOwnScrollY > getScrollRange()) {
            removeCallbacks(this.mReclamp);
            postDelayed(this.mReclamp, 50);
        } else {
            View view = this.mForcedScroll;
            if (view != null) {
                scrollTo(view);
            }
        }
        return windowInsets;
    }

    public void setExpandingEnabled(boolean z) {
        this.mExpandHelper.setEnabled(z);
    }

    private boolean isScrollingEnabled() {
        return this.mScrollingEnabled;
    }

    /* access modifiers changed from: package-private */
    public boolean onKeyguard() {
        return this.mStatusBarState == 1;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Resources resources = getResources();
        updateSplitNotificationShade();
        this.mStatusBarHeight = resources.getDimensionPixelOffset(R$dimen.status_bar_height);
        this.mSwipeHelper.setDensityScale(resources.getDisplayMetrics().density);
        this.mSwipeHelper.setPagingTouchSlop((float) ViewConfiguration.get(getContext()).getScaledPagingTouchSlop());
        reinitView();
    }

    public void dismissViewAnimated(View view, Runnable runnable, int i, long j) {
        this.mSwipeHelper.dismissChild(view, 0.0f, runnable, (long) i, true, j, true);
    }

    private void snapViewIfNeeded(NotificationEntry notificationEntry) {
        ExpandableNotificationRow row = notificationEntry.getRow();
        boolean z = this.mIsExpanded || isPinnedHeadsUp(row);
        if (row.getProvider() != null) {
            this.mSwipeHelper.snapChildIfNeeded(row, z, row.getProvider().isMenuVisible() ? row.getTranslation() : 0.0f);
        }
    }

    private float overScrollUp(int i, int i2) {
        int max = Math.max(i, 0);
        float currentOverScrollAmount = getCurrentOverScrollAmount(true);
        float f = currentOverScrollAmount - ((float) max);
        if (currentOverScrollAmount > 0.0f) {
            setOverScrollAmount(f, true, false);
        }
        float f2 = f < 0.0f ? -f : 0.0f;
        float f3 = ((float) this.mOwnScrollY) + f2;
        float f4 = (float) i2;
        if (f3 <= f4) {
            return f2;
        }
        if (!this.mExpandedInThisMotion) {
            setOverScrolledPixels((getCurrentOverScrolledPixels(false) + f3) - f4, false, false);
        }
        setOwnScrollY(i2);
        return 0.0f;
    }

    private float overScrollDown(int i) {
        int min = Math.min(i, 0);
        float currentOverScrollAmount = getCurrentOverScrollAmount(false);
        float f = ((float) min) + currentOverScrollAmount;
        if (currentOverScrollAmount > 0.0f) {
            setOverScrollAmount(f, false, false);
        }
        if (f >= 0.0f) {
            f = 0.0f;
        }
        float f2 = ((float) this.mOwnScrollY) + f;
        if (f2 >= 0.0f) {
            return f;
        }
        setOverScrolledPixels(getCurrentOverScrolledPixels(true) - f2, true, false);
        setOwnScrollY(0);
        return 0.0f;
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    public void setFinishScrollingCallback(Runnable runnable) {
        this.mFinishScrollingCallback = runnable;
    }

    /* access modifiers changed from: private */
    /* renamed from: animateScroll */
    public void lambda$new$3() {
        if (this.mScroller.computeScrollOffset()) {
            int i = this.mOwnScrollY;
            int currY = this.mScroller.getCurrY();
            if (i != currY) {
                int scrollRange = getScrollRange();
                if ((currY < 0 && i >= 0) || (currY > scrollRange && i <= scrollRange)) {
                    setMaxOverScrollFromCurrentVelocity();
                }
                if (this.mDontClampNextScroll) {
                    scrollRange = Math.max(scrollRange, i);
                }
                customOverScrollBy(currY - i, i, scrollRange, (int) this.mMaxOverScroll);
            }
            postOnAnimation(this.mReflingAndAnimateScroll);
            return;
        }
        this.mDontClampNextScroll = false;
        Runnable runnable = this.mFinishScrollingCallback;
        if (runnable != null) {
            runnable.run();
        }
    }

    private void setMaxOverScrollFromCurrentVelocity() {
        float currVelocity = this.mScroller.getCurrVelocity();
        if (currVelocity >= ((float) this.mMinimumVelocity)) {
            this.mMaxOverScroll = (Math.abs(currVelocity) / 1000.0f) * ((float) this.mOverflingDistance);
        }
    }

    private void customOverScrollBy(int i, int i2, int i3, int i4) {
        int i5 = i2 + i;
        int i6 = -i4;
        int i7 = i4 + i3;
        boolean z = true;
        if (i5 > i7) {
            i5 = i7;
        } else if (i5 < i6) {
            i5 = i6;
        } else {
            z = false;
        }
        onCustomOverScrolled(i5, z);
    }

    public void setOverScrolledPixels(float f, boolean z, boolean z2) {
        setOverScrollAmount(f * getRubberBandFactor(z), z, z2, true);
    }

    public void setOverScrollAmount(float f, boolean z, boolean z2) {
        setOverScrollAmount(f, z, z2, true);
    }

    public void setOverScrollAmount(float f, boolean z, boolean z2, boolean z3) {
        setOverScrollAmount(f, z, z2, z3, isRubberbanded(z));
    }

    public void setOverScrollAmount(float f, boolean z, boolean z2, boolean z3, boolean z4) {
        if (z3) {
            this.mStateAnimator.cancelOverScrollAnimators(z);
        }
        setOverScrollAmountInternal(f, z, z2, z4);
    }

    private void setOverScrollAmountInternal(float f, boolean z, boolean z2, boolean z3) {
        float max = Math.max(0.0f, f);
        if (z2) {
            this.mStateAnimator.animateOverScrollToAmount(max, z, z3);
            return;
        }
        setOverScrolledPixels(max / getRubberBandFactor(z), z);
        this.mAmbientState.setOverScrollAmount(max, z);
        if (z) {
            notifyOverscrollTopListener(max, z3);
        }
        updateStackPosition();
        requestChildrenUpdate();
    }

    private void notifyOverscrollTopListener(float f, boolean z) {
        this.mExpandHelper.onlyObserveMovements(f > 1.0f);
        if (this.mDontReportNextOverScroll) {
            this.mDontReportNextOverScroll = false;
            return;
        }
        OnOverscrollTopChangedListener onOverscrollTopChangedListener = this.mOverscrollTopChangedListener;
        if (onOverscrollTopChangedListener != null) {
            onOverscrollTopChangedListener.onOverscrollTopChanged(f, z);
        }
    }

    public void setOverscrollTopChangedListener(OnOverscrollTopChangedListener onOverscrollTopChangedListener) {
        this.mOverscrollTopChangedListener = onOverscrollTopChangedListener;
    }

    public float getCurrentOverScrollAmount(boolean z) {
        return this.mAmbientState.getOverScrollAmount(z);
    }

    public float getCurrentOverScrolledPixels(boolean z) {
        return z ? this.mOverScrolledTopPixels : this.mOverScrolledBottomPixels;
    }

    private void setOverScrolledPixels(float f, boolean z) {
        if (z) {
            this.mOverScrolledTopPixels = f;
        } else {
            this.mOverScrolledBottomPixels = f;
        }
    }

    private void onCustomOverScrolled(int i, boolean z) {
        if (!this.mScroller.isFinished()) {
            setOwnScrollY(i);
            if (z) {
                springBack();
                return;
            }
            float currentOverScrollAmount = getCurrentOverScrollAmount(true);
            int i2 = this.mOwnScrollY;
            if (i2 < 0) {
                notifyOverscrollTopListener((float) (-i2), isRubberbanded(true));
            } else {
                notifyOverscrollTopListener(currentOverScrollAmount, isRubberbanded(true));
            }
        } else {
            setOwnScrollY(i);
        }
    }

    private void springBack() {
        boolean z;
        float f;
        int scrollRange = getScrollRange();
        int i = this.mOwnScrollY;
        boolean z2 = i <= 0;
        boolean z3 = i >= scrollRange;
        if (z2 || z3) {
            if (z2) {
                f = (float) (-i);
                setOwnScrollY(0);
                this.mDontReportNextOverScroll = true;
                z = true;
            } else {
                setOwnScrollY(scrollRange);
                f = (float) (i - scrollRange);
                z = false;
            }
            setOverScrollAmount(f, z, false);
            setOverScrollAmount(0.0f, z, true);
            this.mScroller.forceFinished(true);
        }
    }

    /* access modifiers changed from: private */
    public int getScrollRange() {
        int i = this.mContentHeight;
        if (!isExpanded() && this.mInHeadsUpPinnedMode) {
            i = this.mHeadsUpInset + getTopHeadsUpPinnedHeight();
        }
        int max = Math.max(0, i - this.mMaxLayoutHeight);
        int imeInset = getImeInset();
        int min = max + Math.min(imeInset, Math.max(0, i - (getHeight() - imeInset)));
        return min > 0 ? Math.max(getScrollAmountToScrollBoundary(), min) : min;
    }

    private int getImeInset() {
        return Math.max(0, this.mBottomInset - (getRootView().getHeight() - getHeight()));
    }

    public ExpandableView getFirstChildNotGone() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (!(childAt.getVisibility() == 8 || childAt == this.mShelf)) {
                return (ExpandableView) childAt;
            }
        }
        return null;
    }

    private View getFirstChildBelowTranlsationY(float f, boolean z) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                float translationY = childAt.getTranslationY();
                if (translationY >= f) {
                    return childAt;
                }
                if (!z && (childAt instanceof ExpandableNotificationRow)) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                    if (expandableNotificationRow.isSummaryWithChildren() && expandableNotificationRow.areChildrenExpanded()) {
                        List<ExpandableNotificationRow> attachedChildren = expandableNotificationRow.getAttachedChildren();
                        for (int i2 = 0; i2 < attachedChildren.size(); i2++) {
                            ExpandableNotificationRow expandableNotificationRow2 = attachedChildren.get(i2);
                            if (expandableNotificationRow2.getTranslationY() + translationY >= f) {
                                return expandableNotificationRow2;
                            }
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public ExpandableView getLastChildNotGone() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            if (!(childAt.getVisibility() == 8 || childAt == this.mShelf)) {
                return (ExpandableView) childAt;
            }
        }
        return null;
    }

    public int getNotGoneChildCount() {
        int childCount = getChildCount();
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i2);
            if (!(expandableView.getVisibility() == 8 || expandableView.willBeGone() || expandableView == this.mShelf)) {
                i++;
            }
        }
        return i;
    }

    /* access modifiers changed from: private */
    public void updateContentHeight() {
        float f;
        int i = (int) (this.mAmbientState.isOnKeyguard() ? 0.0f : (float) this.mMinimumPaddings);
        int i2 = this.mMaxDisplayedNotifications;
        ExpandableView expandableView = null;
        int i3 = 0;
        boolean z = false;
        for (int i4 = 0; i4 < getChildCount(); i4++) {
            ExpandableView expandableView2 = (ExpandableView) getChildAt(i4);
            boolean z2 = true;
            boolean z3 = expandableView2 == this.mFooterView && onKeyguard();
            if (expandableView2.getVisibility() != 8 && !expandableView2.hasNoContentHeight() && !z3) {
                if (i2 != -1 && i3 >= i2) {
                    f = (float) this.mShelf.getIntrinsicHeight();
                } else {
                    f = (float) expandableView2.getIntrinsicHeight();
                    z2 = z;
                }
                if (i != 0) {
                    i += this.mPaddingBetweenElements;
                }
                i = (int) (((float) ((int) (((float) i) + calculateGapHeight(expandableView, expandableView2, i3)))) + f);
                if (f > 0.0f || !(expandableView2 instanceof MediaHeaderView)) {
                    i3++;
                }
                if (z2) {
                    break;
                }
                expandableView = expandableView2;
                z = z2;
            }
        }
        this.mIntrinsicContentHeight = i;
        this.mContentHeight = i + Math.max(this.mIntrinsicPadding, this.mTopPadding) + this.mBottomMargin;
        updateScrollability();
        clampScrollPosition();
        updateStackPosition();
        this.mAmbientState.setContentHeight(this.mContentHeight);
    }

    public float calculateGapHeight(ExpandableView expandableView, ExpandableView expandableView2, int i) {
        return this.mStackScrollAlgorithm.getGapHeightForChild(this.mSectionsManager, i, expandableView2, expandableView);
    }

    public boolean hasPulsingNotifications() {
        return this.mPulsing;
    }

    private void updateScrollability() {
        boolean z = !this.mQsExpanded && getScrollRange() > 0;
        if (z != this.mScrollable) {
            this.mScrollable = z;
            setFocusable(z);
            updateForwardAndBackwardScrollability();
        }
    }

    private void updateForwardAndBackwardScrollability() {
        boolean z = true;
        boolean z2 = this.mScrollable && !this.mScrollAdapter.isScrolledToBottom();
        boolean z3 = this.mScrollable && !this.mScrollAdapter.isScrolledToTop();
        if (z2 == this.mForwardScrollable && z3 == this.mBackwardScrollable) {
            z = false;
        }
        this.mForwardScrollable = z2;
        this.mBackwardScrollable = z3;
        if (z) {
            sendAccessibilityEvent(2048);
        }
    }

    private void updateBackground() {
        if (this.mShouldDrawNotificationBackground) {
            updateBackgroundBounds();
            if (didSectionBoundsChange()) {
                boolean z = this.mAnimateNextSectionBoundsChange || this.mAnimateNextBackgroundTop || this.mAnimateNextBackgroundBottom || areSectionBoundsAnimating();
                if (!isExpanded()) {
                    abortBackgroundAnimators();
                    z = false;
                }
                if (z) {
                    startBackgroundAnimation();
                } else {
                    for (NotificationSection notificationSection : this.mSections) {
                        notificationSection.resetCurrentBounds();
                    }
                    invalidate();
                }
            } else {
                abortBackgroundAnimators();
            }
            this.mAnimateNextBackgroundTop = false;
            this.mAnimateNextBackgroundBottom = false;
            this.mAnimateNextSectionBoundsChange = false;
        }
    }

    private void abortBackgroundAnimators() {
        for (NotificationSection notificationSection : this.mSections) {
            notificationSection.cancelAnimators();
        }
    }

    private boolean didSectionBoundsChange() {
        for (NotificationSection notificationSection : this.mSections) {
            if (notificationSection.didBoundsChange()) {
                return true;
            }
        }
        return false;
    }

    private boolean areSectionBoundsAnimating() {
        for (NotificationSection notificationSection : this.mSections) {
            if (notificationSection.areBoundsAnimating()) {
                return true;
            }
        }
        return false;
    }

    private void startBackgroundAnimation() {
        boolean z;
        boolean z2;
        NotificationSection firstVisibleSection = getFirstVisibleSection();
        NotificationSection lastVisibleSection = getLastVisibleSection();
        NotificationSection[] notificationSectionArr = this.mSections;
        for (NotificationSection notificationSection : notificationSectionArr) {
            if (notificationSection == firstVisibleSection) {
                z = this.mAnimateNextBackgroundTop;
            } else {
                z = this.mAnimateNextSectionBoundsChange;
            }
            if (notificationSection == lastVisibleSection) {
                z2 = this.mAnimateNextBackgroundBottom;
            } else {
                z2 = this.mAnimateNextSectionBoundsChange;
            }
            notificationSection.startBackgroundAnimation(z, z2);
        }
    }

    private void updateBackgroundBounds() {
        int i;
        int i2 = this.mSidePaddings;
        int width = getWidth() - this.mSidePaddings;
        NotificationSection[] notificationSectionArr = this.mSections;
        for (NotificationSection notificationSection : notificationSectionArr) {
            notificationSection.getBounds().left = i2;
            notificationSection.getBounds().right = width;
        }
        if (!this.mIsExpanded) {
            NotificationSection[] notificationSectionArr2 = this.mSections;
            for (NotificationSection notificationSection2 : notificationSectionArr2) {
                notificationSection2.getBounds().top = 0;
                notificationSection2.getBounds().bottom = 0;
            }
            return;
        }
        NotificationSection lastVisibleSection = getLastVisibleSection();
        boolean z = true;
        boolean z2 = this.mStatusBarState == 1;
        if (!z2) {
            i = (int) (((float) this.mTopPadding) + this.mStackTranslation);
        } else if (lastVisibleSection == null) {
            i = this.mTopPadding;
        } else {
            NotificationSection firstVisibleSection = getFirstVisibleSection();
            firstVisibleSection.updateBounds(0, 0, false);
            i = firstVisibleSection.getBounds().top;
        }
        if (this.mNumHeadsUp > 1 || (!this.mAmbientState.isDozing() && (!this.mKeyguardBypassEnabledProvider.getBypassEnabled() || !z2))) {
            z = false;
        }
        NotificationSection[] notificationSectionArr3 = this.mSections;
        int length = notificationSectionArr3.length;
        int i3 = 0;
        while (i3 < length) {
            NotificationSection notificationSection3 = notificationSectionArr3[i3];
            i = notificationSection3.updateBounds(i, notificationSection3 == lastVisibleSection ? (int) (ViewState.getFinalTranslationY(this.mShelf) + ((float) this.mShelf.getIntrinsicHeight())) : i, z);
            i3++;
            z = false;
        }
    }

    private NotificationSection getFirstVisibleSection() {
        NotificationSection[] notificationSectionArr = this.mSections;
        for (NotificationSection notificationSection : notificationSectionArr) {
            if (notificationSection.getFirstVisibleChild() != null) {
                return notificationSection;
            }
        }
        return null;
    }

    private NotificationSection getLastVisibleSection() {
        for (int length = this.mSections.length - 1; length >= 0; length--) {
            NotificationSection notificationSection = this.mSections[length];
            if (notificationSection.getLastVisibleChild() != null) {
                return notificationSection;
            }
        }
        return null;
    }

    private ExpandableView getLastChildWithBackground() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            ExpandableView expandableView = (ExpandableView) getChildAt(childCount);
            if (!(expandableView.getVisibility() == 8 || (expandableView instanceof StackScrollerDecorView) || expandableView == this.mShelf)) {
                return expandableView;
            }
        }
        return null;
    }

    private ExpandableView getFirstChildWithBackground() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (!(expandableView.getVisibility() == 8 || (expandableView instanceof StackScrollerDecorView) || expandableView == this.mShelf)) {
                return expandableView;
            }
        }
        return null;
    }

    private List<ExpandableView> getChildrenWithBackground() {
        ArrayList arrayList = new ArrayList();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (!(expandableView.getVisibility() == 8 || (expandableView instanceof StackScrollerDecorView) || expandableView == this.mShelf)) {
                arrayList.add(expandableView);
            }
        }
        return arrayList;
    }

    protected void fling(int i) {
        if (getChildCount() > 0) {
            float currentOverScrollAmount = getCurrentOverScrollAmount(true);
            int i2 = 0;
            float currentOverScrollAmount2 = getCurrentOverScrollAmount(false);
            if (i < 0 && currentOverScrollAmount > 0.0f) {
                setOwnScrollY(this.mOwnScrollY - ((int) currentOverScrollAmount));
                this.mDontReportNextOverScroll = true;
                setOverScrollAmount(0.0f, true, false);
                this.mMaxOverScroll = ((((float) Math.abs(i)) / 1000.0f) * getRubberBandFactor(true) * ((float) this.mOverflingDistance)) + currentOverScrollAmount;
            } else if (i <= 0 || currentOverScrollAmount2 <= 0.0f) {
                this.mMaxOverScroll = 0.0f;
            } else {
                setOwnScrollY((int) (((float) this.mOwnScrollY) + currentOverScrollAmount2));
                setOverScrollAmount(0.0f, false, false);
                this.mMaxOverScroll = ((((float) Math.abs(i)) / 1000.0f) * getRubberBandFactor(false) * ((float) this.mOverflingDistance)) + currentOverScrollAmount2;
            }
            int max = Math.max(0, getScrollRange());
            if (this.mExpandedInThisMotion) {
                max = Math.min(max, this.mMaxScrollAfterExpand);
            }
            OverScroller overScroller = this.mScroller;
            int i3 = ((ViewGroup) this).mScrollX;
            int i4 = this.mOwnScrollY;
            if (!this.mExpandedInThisMotion || i4 < 0) {
                i2 = 1073741823;
            }
            overScroller.fling(i3, i4, 1, i, 0, 0, 0, max, 0, i2);
            lambda$new$3();
        }
    }

    private boolean shouldOverScrollFling(int i) {
        float currentOverScrollAmount = getCurrentOverScrollAmount(true);
        if (this.mScrolledToTopOnFirstDown && !this.mExpandedInThisMotion) {
            if (i > this.mMinimumVelocity) {
                return true;
            }
            if (currentOverScrollAmount > this.mMinTopOverScrollToEscape && i > 0) {
                return true;
            }
        }
        return false;
    }

    public void updateTopPadding(float f, boolean z) {
        int i = (int) f;
        int layoutMinHeight = getLayoutMinHeight() + i;
        if (layoutMinHeight > getHeight()) {
            this.mTopPaddingOverflow = (float) (layoutMinHeight - getHeight());
        } else {
            this.mTopPaddingOverflow = 0.0f;
        }
        setTopPadding(i, z && !this.mKeyguardBypassEnabledProvider.getBypassEnabled());
        setExpandedHeight(this.mExpandedHeight);
    }

    public void setMaxTopPadding(int i) {
        this.mMaxTopPadding = i;
    }

    public int getLayoutMinHeight() {
        if (isHeadsUpTransition()) {
            ExpandableNotificationRow trackedHeadsUpRow = this.mAmbientState.getTrackedHeadsUpRow();
            if (!trackedHeadsUpRow.isAboveShelf()) {
                return getTopHeadsUpPinnedHeight();
            }
            return getTopHeadsUpPinnedHeight() + ((int) MathUtils.lerp(0.0f, (float) getPositionInLinearLayout(trackedHeadsUpRow), this.mAmbientState.getAppearFraction()));
        } else if (this.mShelf.getVisibility() == 8) {
            return 0;
        } else {
            return this.mShelf.getIntrinsicHeight();
        }
    }

    public float getTopPaddingOverflow() {
        return this.mTopPaddingOverflow;
    }

    private float getRubberBandFactor(boolean z) {
        if (!z) {
            return 0.35f;
        }
        if (this.mExpandedInThisMotion) {
            return 0.15f;
        }
        if (this.mIsExpansionChanging || this.mPanelTracking) {
            return 0.21f;
        }
        if (this.mScrolledToTopOnFirstDown) {
            return 1.0f;
        }
        return 0.35f;
    }

    private boolean isRubberbanded(boolean z) {
        return !z || this.mExpandedInThisMotion || this.mIsExpansionChanging || this.mPanelTracking || !this.mScrolledToTopOnFirstDown;
    }

    public void setChildTransferInProgress(boolean z) {
        Assert.isMainThread();
        this.mChildTransferInProgress = z;
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if (!this.mChildTransferInProgress) {
            onViewRemovedInternal((ExpandableView) view, this);
        }
    }

    public void cleanUpViewStateForEntry(NotificationEntry notificationEntry) {
        if (notificationEntry.getRow() == this.mSwipeHelper.getTranslatingParentView()) {
            this.mSwipeHelper.clearTranslatingParentView();
        }
    }

    private void onViewRemovedInternal(ExpandableView expandableView, ViewGroup viewGroup) {
        if (!this.mChangePositionInProgress) {
            expandableView.setOnHeightChangedListener(null);
            updateScrollStateForRemovedChild(expandableView);
            if (!generateRemoveAnimation(expandableView)) {
                this.mSwipedOutViews.remove(expandableView);
            } else if (!this.mSwipedOutViews.contains(expandableView) || !isFullySwipedOut(expandableView)) {
                viewGroup.addTransientView(expandableView, 0);
                expandableView.setTransientContainer(viewGroup);
            }
            updateAnimationState(false, expandableView);
            focusNextViewIfFocused(expandableView);
        }
    }

    public boolean isFullySwipedOut(ExpandableView expandableView) {
        return Math.abs(expandableView.getTranslation()) >= Math.abs(getTotalTranslationLength(expandableView));
    }

    private void focusNextViewIfFocused(View view) {
        float f;
        if (view instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            if (expandableNotificationRow.shouldRefocusOnDismiss()) {
                View childAfterViewWhenDismissed = expandableNotificationRow.getChildAfterViewWhenDismissed();
                if (childAfterViewWhenDismissed == null) {
                    View groupParentWhenDismissed = expandableNotificationRow.getGroupParentWhenDismissed();
                    if (groupParentWhenDismissed != null) {
                        f = groupParentWhenDismissed.getTranslationY();
                    } else {
                        f = view.getTranslationY();
                    }
                    childAfterViewWhenDismissed = getFirstChildBelowTranlsationY(f, true);
                }
                if (childAfterViewWhenDismissed != null) {
                    childAfterViewWhenDismissed.requestAccessibilityFocus();
                }
            }
        }
    }

    private boolean isChildInGroup(View view) {
        return (view instanceof ExpandableNotificationRow) && this.mGroupMembershipManager.isChildInGroup(((ExpandableNotificationRow) view).getEntry());
    }

    /* access modifiers changed from: package-private */
    public boolean generateRemoveAnimation(ExpandableView expandableView) {
        boolean z = DEBUG_REMOVE_ANIMATION;
        String str = "";
        if (z) {
            if (expandableView instanceof ExpandableNotificationRow) {
                str = ((ExpandableNotificationRow) expandableView).getEntry().getKey();
            }
            Log.d("StackScroller", "generateRemoveAnimation " + str);
        }
        if (removeRemovedChildFromHeadsUpChangeAnimations(expandableView)) {
            if (z) {
                Log.d("StackScroller", "removedBecauseOfHeadsUp " + str);
            }
            this.mAddedHeadsUpChildren.remove(expandableView);
            return false;
        } else if (isClickedHeadsUp(expandableView)) {
            this.mClearTransientViewsWhenFinished.add(expandableView);
            return true;
        } else {
            if (z) {
                StringBuilder sb = new StringBuilder();
                sb.append("generateRemove ");
                sb.append(str);
                sb.append("\nmIsExpanded ");
                sb.append(this.mIsExpanded);
                sb.append("\nmAnimationsEnabled ");
                sb.append(this.mAnimationsEnabled);
                sb.append("\n!invisible group ");
                sb.append(!isChildInInvisibleGroup(expandableView));
                Log.d("StackScroller", sb.toString());
            }
            if (this.mIsExpanded && this.mAnimationsEnabled && !isChildInInvisibleGroup(expandableView)) {
                if (!this.mChildrenToAddAnimated.contains(expandableView)) {
                    if (z) {
                        Log.d("StackScroller", "needsAnimation = true " + str);
                    }
                    this.mChildrenToRemoveAnimated.add(expandableView);
                    this.mNeedsAnimation = true;
                    return true;
                }
                this.mChildrenToAddAnimated.remove(expandableView);
                this.mFromMoreCardAdditions.remove(expandableView);
            }
            return false;
        }
    }

    private boolean isClickedHeadsUp(View view) {
        return HeadsUpUtil.isClickedHeadsUpNotification(view);
    }

    private boolean removeRemovedChildFromHeadsUpChangeAnimations(View view) {
        Iterator<Pair<ExpandableNotificationRow, Boolean>> it = this.mHeadsUpChangeAnimations.iterator();
        boolean z = false;
        while (it.hasNext()) {
            Pair<ExpandableNotificationRow, Boolean> next = it.next();
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) next.first;
            boolean booleanValue = ((Boolean) next.second).booleanValue();
            if (view == expandableNotificationRow) {
                this.mTmpList.add(next);
                z |= booleanValue;
            }
        }
        if (z) {
            this.mHeadsUpChangeAnimations.removeAll(this.mTmpList);
            ((ExpandableNotificationRow) view).setHeadsUpAnimatingAway(false);
        }
        this.mTmpList.clear();
        if (!z || !this.mAddedHeadsUpChildren.contains(view)) {
            return false;
        }
        return true;
    }

    private boolean isChildInInvisibleGroup(View view) {
        if (!(view instanceof ExpandableNotificationRow)) {
            return false;
        }
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        NotificationEntry groupSummary = this.mGroupMembershipManager.getGroupSummary(expandableNotificationRow.getEntry());
        if (groupSummary == null || groupSummary.getRow() == expandableNotificationRow || expandableNotificationRow.getVisibility() != 4) {
            return false;
        }
        return true;
    }

    private void updateScrollStateForRemovedChild(ExpandableView expandableView) {
        int positionInLinearLayout = getPositionInLinearLayout(expandableView);
        int intrinsicHeight = getIntrinsicHeight(expandableView) + this.mPaddingBetweenElements;
        int i = positionInLinearLayout + intrinsicHeight;
        int scrollAmountToScrollBoundary = getScrollAmountToScrollBoundary();
        this.mAnimateStackYForContentHeightChange = true;
        int i2 = this.mOwnScrollY;
        if (i <= i2 - scrollAmountToScrollBoundary) {
            setOwnScrollY(i2 - intrinsicHeight);
        } else if (positionInLinearLayout < i2 - scrollAmountToScrollBoundary) {
            setOwnScrollY(positionInLinearLayout + scrollAmountToScrollBoundary);
        }
    }

    private int getScrollAmountToScrollBoundary() {
        if (this.mShouldUseSplitNotificationShade) {
            return this.mSidePaddings;
        }
        return this.mTopPadding - this.mQsScrollBoundaryPosition;
    }

    private int getIntrinsicHeight(View view) {
        if (view instanceof ExpandableView) {
            return ((ExpandableView) view).getIntrinsicHeight();
        }
        return view.getHeight();
    }

    /* JADX DEBUG: Multi-variable search result rejected for r0v1, resolved type: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow */
    /* JADX DEBUG: Multi-variable search result rejected for r0v2, resolved type: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow */
    /* JADX DEBUG: Multi-variable search result rejected for r0v3, resolved type: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow */
    /* JADX WARN: Multi-variable type inference failed */
    public int getPositionInLinearLayout(View view) {
        ExpandableNotificationRow expandableNotificationRow;
        ExpandableNotificationRow expandableNotificationRow2 = null;
        if (isChildInGroup(view)) {
            expandableNotificationRow2 = (ExpandableNotificationRow) view;
            view = expandableNotificationRow2.getNotificationParent();
            expandableNotificationRow = view;
        } else {
            expandableNotificationRow = 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i2);
            boolean z = expandableView.getVisibility() != 8;
            if (z && !expandableView.hasNoContentHeight() && i != 0) {
                i += this.mPaddingBetweenElements;
            }
            if (expandableView == view) {
                return expandableNotificationRow != 0 ? i + expandableNotificationRow.getPositionOfChild(expandableNotificationRow2) : i;
            }
            if (z) {
                i += getIntrinsicHeight(expandableView);
            }
        }
        return 0;
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        super.onViewAdded(view);
        if (view instanceof ExpandableView) {
            onViewAddedInternal((ExpandableView) view);
        }
    }

    private void updateFirstAndLastBackgroundViews() {
        ExpandableView expandableView;
        NotificationSection firstVisibleSection = getFirstVisibleSection();
        NotificationSection lastVisibleSection = getLastVisibleSection();
        ExpandableView expandableView2 = null;
        if (firstVisibleSection == null) {
            expandableView = null;
        } else {
            expandableView = firstVisibleSection.getFirstVisibleChild();
        }
        if (lastVisibleSection != null) {
            expandableView2 = lastVisibleSection.getLastVisibleChild();
        }
        ExpandableView firstChildWithBackground = getFirstChildWithBackground();
        ExpandableView lastChildWithBackground = getLastChildWithBackground();
        boolean updateFirstAndLastViewsForAllSections = this.mSectionsManager.updateFirstAndLastViewsForAllSections(this.mSections, getChildrenWithBackground());
        if (!this.mAnimationsEnabled || !this.mIsExpanded) {
            this.mAnimateNextBackgroundTop = false;
            this.mAnimateNextBackgroundBottom = false;
            this.mAnimateNextSectionBoundsChange = false;
        } else {
            boolean z = true;
            this.mAnimateNextBackgroundTop = firstChildWithBackground != expandableView;
            if (lastChildWithBackground == expandableView2 && !this.mAnimateBottomOnLayout) {
                z = false;
            }
            this.mAnimateNextBackgroundBottom = z;
            this.mAnimateNextSectionBoundsChange = updateFirstAndLastViewsForAllSections;
        }
        this.mAmbientState.setLastVisibleBackgroundChild(lastChildWithBackground);
        this.mController.getNoticationRoundessManager().updateRoundedChildren(this.mSections);
        this.mAnimateBottomOnLayout = false;
        invalidate();
    }

    private void onViewAddedInternal(ExpandableView expandableView) {
        updateHideSensitiveForChild(expandableView);
        expandableView.setOnHeightChangedListener(this.mOnChildHeightChangedListener);
        generateAddAnimation(expandableView, false);
        updateAnimationState(expandableView);
        updateChronometerForChild(expandableView);
        if (expandableView instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
            expandableNotificationRow.setDismissRtl(this.mDismissRtl);
            expandableNotificationRow.setDismissUsingRowTranslationX(this.mDismissUsingRowTranslationX);
        }
    }

    private void updateHideSensitiveForChild(ExpandableView expandableView) {
        expandableView.setHideSensitiveForIntrinsicHeight(this.mAmbientState.isHideSensitive());
    }

    public void notifyGroupChildRemoved(ExpandableView expandableView, ViewGroup viewGroup) {
        onViewRemovedInternal(expandableView, viewGroup);
    }

    public void notifyGroupChildAdded(ExpandableView expandableView) {
        onViewAddedInternal(expandableView);
    }

    public void setAnimationsEnabled(boolean z) {
        this.mAnimationsEnabled = z;
        updateNotificationAnimationStates();
        if (!z) {
            this.mSwipedOutViews.clear();
            this.mChildrenToRemoveAnimated.clear();
            clearTemporaryViewsInGroup(this);
        }
    }

    private void updateNotificationAnimationStates() {
        boolean z = this.mAnimationsEnabled || hasPulsingNotifications();
        this.mShelf.setAnimationsEnabled(z);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            z &= this.mIsExpanded || isPinnedHeadsUp(childAt);
            updateAnimationState(z, childAt);
        }
    }

    void updateAnimationState(View view) {
        updateAnimationState((this.mAnimationsEnabled || hasPulsingNotifications()) && (this.mIsExpanded || isPinnedHeadsUp(view)), view);
    }

    /* access modifiers changed from: package-private */
    public void setExpandingNotification(ExpandableNotificationRow expandableNotificationRow) {
        ExpandableNotificationRow expandableNotificationRow2 = this.mExpandingNotificationRow;
        if (expandableNotificationRow2 != null && expandableNotificationRow == null) {
            expandableNotificationRow2.setExpandingClipPath(null);
            ExpandableNotificationRow notificationParent = this.mExpandingNotificationRow.getNotificationParent();
            if (notificationParent != null) {
                notificationParent.setExpandingClipPath(null);
            }
        }
        this.mExpandingNotificationRow = expandableNotificationRow;
        updateLaunchedNotificationClipPath();
        requestChildrenUpdate();
    }

    public boolean containsView(View view) {
        return view.getParent() == this;
    }

    public void applyExpandAnimationParams(ExpandAnimationParameters expandAnimationParameters) {
        this.mLaunchAnimationParams = expandAnimationParameters;
        setLaunchingNotification(expandAnimationParameters != null);
        updateLaunchedNotificationClipPath();
        requestChildrenUpdate();
    }

    private void updateAnimationState(boolean z, View view) {
        if (view instanceof ExpandableNotificationRow) {
            ((ExpandableNotificationRow) view).setIconAnimationRunning(z);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isAddOrRemoveAnimationPending() {
        return this.mNeedsAnimation && (!this.mChildrenToAddAnimated.isEmpty() || !this.mChildrenToRemoveAnimated.isEmpty());
    }

    public void generateAddAnimation(ExpandableView expandableView, boolean z) {
        if (this.mIsExpanded && this.mAnimationsEnabled && !this.mChangePositionInProgress && !isFullyHidden()) {
            this.mChildrenToAddAnimated.add(expandableView);
            if (z) {
                this.mFromMoreCardAdditions.add(expandableView);
            }
            this.mNeedsAnimation = true;
        }
        if (isHeadsUp(expandableView) && this.mAnimationsEnabled && !this.mChangePositionInProgress && !isFullyHidden()) {
            this.mAddedHeadsUpChildren.add(expandableView);
            this.mChildrenToAddAnimated.remove(expandableView);
        }
    }

    public void changeViewPosition(ExpandableView expandableView, int i) {
        Assert.isMainThread();
        if (!this.mChangePositionInProgress) {
            int indexOfChild = indexOfChild(expandableView);
            boolean z = false;
            if (indexOfChild == -1) {
                if ((expandableView instanceof ExpandableNotificationRow) && expandableView.getTransientContainer() != null) {
                    z = true;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Attempting to re-position ");
                sb.append(z ? "transient" : "");
                sb.append(" view {");
                sb.append(expandableView);
                sb.append("}");
                Log.e("StackScroller", sb.toString());
            } else if (expandableView != null && expandableView.getParent() == this && indexOfChild != i) {
                this.mChangePositionInProgress = true;
                expandableView.setChangingPosition(true);
                removeView(expandableView);
                addView(expandableView, i);
                expandableView.setChangingPosition(false);
                this.mChangePositionInProgress = false;
                if (this.mIsExpanded && this.mAnimationsEnabled && expandableView.getVisibility() != 8) {
                    this.mChildrenChangingPositions.add(expandableView);
                    this.mNeedsAnimation = true;
                }
            }
        } else {
            throw new IllegalStateException("Reentrant call to changeViewPosition");
        }
    }

    private void startAnimationToState() {
        if (this.mNeedsAnimation) {
            generateAllAnimationEvents();
            this.mNeedsAnimation = false;
        }
        if (!this.mAnimationEvents.isEmpty() || isCurrentlyAnimating()) {
            setAnimationRunning(true);
            this.mStateAnimator.startAnimationForEvents(this.mAnimationEvents, this.mGoToFullShadeDelay);
            this.mAnimationEvents.clear();
            updateBackground();
            updateViewShadows();
        } else {
            applyCurrentState();
        }
        this.mGoToFullShadeDelay = 0;
    }

    private void generateAllAnimationEvents() {
        generateHeadsUpAnimationEvents();
        generateChildRemovalEvents();
        generateChildAdditionEvents();
        generatePositionChangeEvents();
        generateTopPaddingEvent();
        generateActivateEvent();
        generateDimmedEvent();
        generateHideSensitiveEvent();
        generateGoToFullShadeEvent();
        generateViewResizeEvent();
        generateGroupExpansionEvent();
        generateAnimateEverythingEvent();
    }

    private void generateHeadsUpAnimationEvents() {
        Iterator<Pair<ExpandableNotificationRow, Boolean>> it = this.mHeadsUpChangeAnimations.iterator();
        while (it.hasNext()) {
            Pair<ExpandableNotificationRow, Boolean> next = it.next();
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) next.first;
            boolean booleanValue = ((Boolean) next.second).booleanValue();
            if (booleanValue == expandableNotificationRow.isHeadsUp()) {
                int i = 14;
                boolean z = true;
                boolean z2 = false;
                boolean z3 = expandableNotificationRow.isPinned() && !this.mIsExpanded;
                if (this.mIsExpanded && (!this.mKeyguardBypassEnabledProvider.getBypassEnabled() || !onKeyguard() || !this.mInHeadsUpPinnedMode)) {
                    z = false;
                }
                if (!z || booleanValue) {
                    ExpandableViewState viewState = expandableNotificationRow.getViewState();
                    if (viewState != null) {
                        if (booleanValue && (this.mAddedHeadsUpChildren.contains(expandableNotificationRow) || z3)) {
                            i = (z3 || shouldHunAppearFromBottom(viewState)) ? 11 : 0;
                            z2 = !z3;
                        }
                        AnimationEvent animationEvent = new AnimationEvent(expandableNotificationRow, i);
                        animationEvent.headsUpFromBottom = z2;
                        this.mAnimationEvents.add(animationEvent);
                    }
                } else {
                    i = expandableNotificationRow.wasJustClicked() ? 13 : 12;
                    if (expandableNotificationRow.isChildInGroup()) {
                        expandableNotificationRow.setHeadsUpAnimatingAway(false);
                    } else {
                        AnimationEvent animationEvent = new AnimationEvent(expandableNotificationRow, i);
                        animationEvent.headsUpFromBottom = z2;
                        this.mAnimationEvents.add(animationEvent);
                    }
                }
            }
        }
        this.mHeadsUpChangeAnimations.clear();
        this.mAddedHeadsUpChildren.clear();
    }

    private boolean shouldHunAppearFromBottom(ExpandableViewState expandableViewState) {
        return expandableViewState.yTranslation + ((float) expandableViewState.height) >= this.mAmbientState.getMaxHeadsUpTranslation();
    }

    private void generateGroupExpansionEvent() {
        if (this.mExpandedGroupView != null) {
            this.mAnimationEvents.add(new AnimationEvent(this.mExpandedGroupView, 10));
            this.mExpandedGroupView = null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x0011  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void generateViewResizeEvent() {
        /*
            r5 = this;
            boolean r0 = r5.mNeedViewResizeAnimation
            r1 = 0
            if (r0 == 0) goto L_0x0033
            java.util.ArrayList<com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$AnimationEvent> r0 = r5.mAnimationEvents
            java.util.Iterator r0 = r0.iterator()
        L_0x000b:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0023
            java.lang.Object r2 = r0.next()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$AnimationEvent r2 = (com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.AnimationEvent) r2
            int r2 = r2.animationType
            r3 = 13
            if (r2 == r3) goto L_0x0021
            r3 = 12
            if (r2 != r3) goto L_0x000b
        L_0x0021:
            r0 = 1
            goto L_0x0024
        L_0x0023:
            r0 = r1
        L_0x0024:
            if (r0 != 0) goto L_0x0033
            java.util.ArrayList<com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$AnimationEvent> r0 = r5.mAnimationEvents
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$AnimationEvent r2 = new com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$AnimationEvent
            r3 = 0
            r4 = 9
            r2.<init>(r3, r4)
            r0.add(r2)
        L_0x0033:
            r5.mNeedViewResizeAnimation = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.generateViewResizeEvent():void");
    }

    private void generateChildRemovalEvents() {
        boolean z;
        ViewGroup transientContainer;
        Iterator<ExpandableView> it = this.mChildrenToRemoveAnimated.iterator();
        while (it.hasNext()) {
            ExpandableView next = it.next();
            boolean contains = this.mSwipedOutViews.contains(next);
            float translationY = next.getTranslationY();
            boolean z2 = next instanceof ExpandableNotificationRow;
            boolean z3 = false;
            int i = 1;
            if (z2) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) next;
                if (!expandableNotificationRow.isRemoved() || !expandableNotificationRow.wasChildInGroupWhenRemoved()) {
                    z = true;
                } else {
                    translationY = expandableNotificationRow.getTranslationWhenRemoved();
                    z = false;
                }
                contains |= isFullySwipedOut(expandableNotificationRow);
            } else if (next instanceof MediaHeaderView) {
                contains = true;
                z = true;
            } else {
                z = true;
            }
            if (!contains) {
                Rect clipBounds = next.getClipBounds();
                if (clipBounds != null && clipBounds.height() == 0) {
                    z3 = true;
                }
                if (z3 && (transientContainer = next.getTransientContainer()) != null) {
                    transientContainer.removeTransientView(next);
                }
                contains = z3;
            }
            if (contains) {
                i = 2;
            }
            AnimationEvent animationEvent = new AnimationEvent(next, i);
            animationEvent.viewAfterChangingView = getFirstChildBelowTranlsationY(translationY, z);
            this.mAnimationEvents.add(animationEvent);
            this.mSwipedOutViews.remove(next);
            if (DEBUG_REMOVE_ANIMATION) {
                Log.d("StackScroller", "created Remove Event - SwipedOut: " + contains + " " + (z2 ? ((ExpandableNotificationRow) next).getEntry().getKey() : ""));
            }
        }
        this.mChildrenToRemoveAnimated.clear();
    }

    private void generatePositionChangeEvents() {
        AnimationEvent animationEvent;
        Iterator<ExpandableView> it = this.mChildrenChangingPositions.iterator();
        while (true) {
            Integer num = null;
            if (!it.hasNext()) {
                break;
            }
            ExpandableView next = it.next();
            if (next instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) next;
                if (expandableNotificationRow.getEntry().isMarkedForUserTriggeredMovement()) {
                    num = 500;
                    expandableNotificationRow.getEntry().markForUserTriggeredMovement(false);
                }
            }
            if (num == null) {
                animationEvent = new AnimationEvent(next, 6);
            } else {
                animationEvent = new AnimationEvent(next, 6, (long) num.intValue());
            }
            this.mAnimationEvents.add(animationEvent);
        }
        this.mChildrenChangingPositions.clear();
        if (this.mGenerateChildOrderChangedEvent) {
            this.mAnimationEvents.add(new AnimationEvent(null, 6));
            this.mGenerateChildOrderChangedEvent = false;
        }
    }

    private void generateChildAdditionEvents() {
        Iterator<ExpandableView> it = this.mChildrenToAddAnimated.iterator();
        while (it.hasNext()) {
            ExpandableView next = it.next();
            if (this.mFromMoreCardAdditions.contains(next)) {
                this.mAnimationEvents.add(new AnimationEvent(next, 0, 360));
            } else {
                this.mAnimationEvents.add(new AnimationEvent(next, 0));
            }
        }
        this.mChildrenToAddAnimated.clear();
        this.mFromMoreCardAdditions.clear();
    }

    private void generateTopPaddingEvent() {
        AnimationEvent animationEvent;
        if (this.mTopPaddingNeedsAnimation) {
            if (this.mAmbientState.isDozing()) {
                animationEvent = new AnimationEvent(null, 3, 550);
            } else {
                animationEvent = new AnimationEvent(null, 3);
            }
            this.mAnimationEvents.add(animationEvent);
        }
        this.mTopPaddingNeedsAnimation = false;
    }

    private void generateActivateEvent() {
        if (this.mActivateNeedsAnimation) {
            this.mAnimationEvents.add(new AnimationEvent(null, 4));
        }
        this.mActivateNeedsAnimation = false;
    }

    private void generateAnimateEverythingEvent() {
        if (this.mEverythingNeedsAnimation) {
            this.mAnimationEvents.add(new AnimationEvent(null, 15));
        }
        this.mEverythingNeedsAnimation = false;
    }

    private void generateDimmedEvent() {
        if (this.mDimmedNeedsAnimation) {
            this.mAnimationEvents.add(new AnimationEvent(null, 5));
        }
        this.mDimmedNeedsAnimation = false;
    }

    private void generateHideSensitiveEvent() {
        if (this.mHideSensitiveNeedsAnimation) {
            this.mAnimationEvents.add(new AnimationEvent(null, 8));
        }
        this.mHideSensitiveNeedsAnimation = false;
    }

    private void generateGoToFullShadeEvent() {
        if (this.mGoToFullShadeNeedsAnimation) {
            this.mAnimationEvents.add(new AnimationEvent(null, 7));
        }
        this.mGoToFullShadeNeedsAnimation = false;
    }

    protected StackScrollAlgorithm createStackScrollAlgorithm(Context context) {
        return new StackScrollAlgorithm(context, this);
    }

    public boolean isInContentBounds(float f) {
        return f < ((float) (getHeight() - getEmptyBottomMargin()));
    }

    private float getTouchSlop(MotionEvent motionEvent) {
        if (motionEvent.getClassification() == 1) {
            return ((float) this.mTouchSlop) * this.mSlopMultiplier;
        }
        return (float) this.mTouchSlop;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        NotificationStackScrollLayoutController.TouchHandler touchHandler = this.mTouchHandler;
        if (touchHandler == null || !touchHandler.onTouchEvent(motionEvent)) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void dispatchDownEventToScroller(MotionEvent motionEvent) {
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.setAction(0);
        onScrollTouch(obtain);
        obtain.recycle();
    }

    @Override // android.view.View
    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        int i = 0;
        if (!isScrollingEnabled() || !this.mIsExpanded || this.mSwipeHelper.isSwiping() || this.mExpandingNotification || this.mDisallowScrollingInThisMotion) {
            return false;
        }
        if ((motionEvent.getSource() & 2) != 0 && motionEvent.getAction() == 8 && !this.mIsBeingDragged) {
            float axisValue = motionEvent.getAxisValue(9);
            if (axisValue != 0.0f) {
                int scrollRange = getScrollRange();
                int i2 = this.mOwnScrollY;
                int verticalScrollFactor = i2 - ((int) (axisValue * getVerticalScrollFactor()));
                if (verticalScrollFactor >= 0) {
                    i = verticalScrollFactor > scrollRange ? scrollRange : verticalScrollFactor;
                }
                if (i != i2) {
                    setOwnScrollY(i);
                    return true;
                }
            }
        }
        return super.onGenericMotionEvent(motionEvent);
    }

    /* access modifiers changed from: package-private */
    public boolean onScrollTouch(MotionEvent motionEvent) {
        float f;
        if (!isScrollingEnabled()) {
            return false;
        }
        if (isInsideQsContainer(motionEvent) && !this.mIsBeingDragged) {
            return false;
        }
        this.mForcedScroll = null;
        initVelocityTrackerIfNotExists();
        this.mVelocityTracker.addMovement(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (motionEvent.findPointerIndex(this.mActivePointerId) != -1 || actionMasked == 0) {
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (findPointerIndex == -1) {
                            Log.e("StackScroller", "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent");
                        } else {
                            int y = (int) motionEvent.getY(findPointerIndex);
                            int i = this.mLastMotionY - y;
                            int abs = Math.abs(((int) motionEvent.getX(findPointerIndex)) - this.mDownX);
                            int abs2 = Math.abs(i);
                            float touchSlop = getTouchSlop(motionEvent);
                            if (!this.mIsBeingDragged && ((float) abs2) > touchSlop && abs2 > abs) {
                                setIsBeingDragged(true);
                                i = (int) (i > 0 ? ((float) i) - touchSlop : ((float) i) + touchSlop);
                            }
                            if (this.mIsBeingDragged) {
                                this.mLastMotionY = y;
                                int scrollRange = getScrollRange();
                                if (this.mExpandedInThisMotion) {
                                    scrollRange = Math.min(scrollRange, this.mMaxScrollAfterExpand);
                                }
                                if (i < 0) {
                                    f = overScrollDown(i);
                                } else {
                                    f = overScrollUp(i, scrollRange);
                                }
                                if (f != 0.0f) {
                                    customOverScrollBy((int) f, this.mOwnScrollY, scrollRange, getHeight() / 2);
                                    this.mController.checkSnoozeLeavebehind();
                                }
                            }
                        }
                    } else if (actionMasked != 3) {
                        if (actionMasked == 5) {
                            int actionIndex = motionEvent.getActionIndex();
                            this.mLastMotionY = (int) motionEvent.getY(actionIndex);
                            this.mDownX = (int) motionEvent.getX(actionIndex);
                            this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                        } else if (actionMasked == 6) {
                            onSecondaryPointerUp(motionEvent);
                            this.mLastMotionY = (int) motionEvent.getY(motionEvent.findPointerIndex(this.mActivePointerId));
                            this.mDownX = (int) motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId));
                        }
                    } else if (this.mIsBeingDragged && getChildCount() > 0) {
                        if (this.mScroller.springBack(((ViewGroup) this).mScrollX, this.mOwnScrollY, 0, 0, 0, getScrollRange())) {
                            lambda$new$3();
                        }
                        this.mActivePointerId = -1;
                        endDrag();
                    }
                } else if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    int yVelocity = (int) velocityTracker.getYVelocity(this.mActivePointerId);
                    if (shouldOverScrollFling(yVelocity)) {
                        onOverScrollFling(true, yVelocity);
                    } else if (getChildCount() > 0) {
                        if (Math.abs(yVelocity) > this.mMinimumVelocity) {
                            if (getCurrentOverScrollAmount(true) == 0.0f || yVelocity > 0) {
                                this.mFlingAfterUpEvent = true;
                                setFinishScrollingCallback(new Runnable() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda5
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        NotificationStackScrollLayout.this.lambda$onScrollTouch$4();
                                    }
                                });
                                fling(-yVelocity);
                            } else {
                                onOverScrollFling(false, yVelocity);
                            }
                        } else if (this.mScroller.springBack(((ViewGroup) this).mScrollX, this.mOwnScrollY, 0, 0, 0, getScrollRange())) {
                            lambda$new$3();
                        }
                    }
                    this.mActivePointerId = -1;
                    endDrag();
                }
            } else if (getChildCount() == 0 || !isInContentBounds(motionEvent)) {
                return false;
            } else {
                setIsBeingDragged(!this.mScroller.isFinished());
                if (!this.mScroller.isFinished()) {
                    this.mScroller.forceFinished(true);
                }
                this.mLastMotionY = (int) motionEvent.getY();
                this.mDownX = (int) motionEvent.getX();
                this.mActivePointerId = motionEvent.getPointerId(0);
            }
            return true;
        }
        Log.e("StackScroller", "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent " + MotionEvent.actionToString(motionEvent.getActionMasked()));
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onScrollTouch$4() {
        this.mFlingAfterUpEvent = false;
        InteractionJankMonitor.getInstance().end(2);
        setFinishScrollingCallback(null);
    }

    /* access modifiers changed from: package-private */
    public boolean isFlingAfterUpEvent() {
        return this.mFlingAfterUpEvent;
    }

    protected boolean isInsideQsContainer(MotionEvent motionEvent) {
        return motionEvent.getY() < ((float) this.mQsContainer.getBottom());
    }

    private void onOverScrollFling(boolean z, int i) {
        OnOverscrollTopChangedListener onOverscrollTopChangedListener = this.mOverscrollTopChangedListener;
        if (onOverscrollTopChangedListener != null) {
            onOverscrollTopChangedListener.flingTopOverscroll((float) i, z);
        }
        this.mDontReportNextOverScroll = true;
        setOverScrollAmount(0.0f, true, false);
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int action = (motionEvent.getAction() & 65280) >> 8;
        if (motionEvent.getPointerId(action) == this.mActivePointerId) {
            int i = action == 0 ? 1 : 0;
            this.mLastMotionY = (int) motionEvent.getY(i);
            this.mActivePointerId = motionEvent.getPointerId(i);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    private void endDrag() {
        setIsBeingDragged(false);
        recycleVelocityTracker();
        if (getCurrentOverScrollAmount(true) > 0.0f) {
            setOverScrollAmount(0.0f, true, true);
        }
        if (getCurrentOverScrollAmount(false) > 0.0f) {
            setOverScrollAmount(0.0f, false, true);
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        NotificationStackScrollLayoutController.TouchHandler touchHandler = this.mTouchHandler;
        if (touchHandler == null || !touchHandler.onInterceptTouchEvent(motionEvent)) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void handleEmptySpaceClick(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                float touchSlop = getTouchSlop(motionEvent);
                if (!this.mTouchIsClick) {
                    return;
                }
                if (Math.abs(motionEvent.getY() - this.mInitialTouchY) > touchSlop || Math.abs(motionEvent.getX() - this.mInitialTouchX) > touchSlop) {
                    this.mTouchIsClick = false;
                }
            }
        } else if (this.mStatusBarState != 1 && this.mTouchIsClick && isBelowLastNotification(this.mInitialTouchX, this.mInitialTouchY)) {
            this.mOnEmptySpaceClickListener.onEmptySpaceClicked(this.mInitialTouchX, this.mInitialTouchY);
        }
    }

    /* access modifiers changed from: package-private */
    public void initDownStates(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mExpandedInThisMotion = false;
            this.mOnlyScrollingInThisMotion = !this.mScroller.isFinished();
            this.mDisallowScrollingInThisMotion = false;
            this.mDisallowDismissInThisMotion = false;
            this.mTouchIsClick = true;
            this.mInitialTouchX = motionEvent.getX();
            this.mInitialTouchY = motionEvent.getY();
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z) {
        super.requestDisallowInterceptTouchEvent(z);
        if (z) {
            cancelLongPress();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean onInterceptTouchEventScroll(MotionEvent motionEvent) {
        if (!isScrollingEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        }
        int i = action & 255;
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    int i2 = this.mActivePointerId;
                    if (i2 != -1) {
                        int findPointerIndex = motionEvent.findPointerIndex(i2);
                        if (findPointerIndex == -1) {
                            Log.e("StackScroller", "Invalid pointerId=" + i2 + " in onInterceptTouchEvent");
                        } else {
                            int y = (int) motionEvent.getY(findPointerIndex);
                            int x = (int) motionEvent.getX(findPointerIndex);
                            int abs = Math.abs(y - this.mLastMotionY);
                            int abs2 = Math.abs(x - this.mDownX);
                            if (((float) abs) > getTouchSlop(motionEvent) && abs > abs2) {
                                setIsBeingDragged(true);
                                this.mLastMotionY = y;
                                this.mDownX = x;
                                initVelocityTrackerIfNotExists();
                                this.mVelocityTracker.addMovement(motionEvent);
                            }
                        }
                    }
                } else if (i != 3) {
                    if (i == 6) {
                        onSecondaryPointerUp(motionEvent);
                    }
                }
            }
            setIsBeingDragged(false);
            this.mActivePointerId = -1;
            recycleVelocityTracker();
            if (this.mScroller.springBack(((ViewGroup) this).mScrollX, this.mOwnScrollY, 0, 0, 0, getScrollRange())) {
                lambda$new$3();
            }
        } else {
            int y2 = (int) motionEvent.getY();
            this.mScrolledToTopOnFirstDown = this.mScrollAdapter.isScrolledToTop();
            if (getChildAtPosition(motionEvent.getX(), (float) y2, false, false) == null) {
                setIsBeingDragged(false);
                recycleVelocityTracker();
            } else {
                this.mLastMotionY = y2;
                this.mDownX = (int) motionEvent.getX();
                this.mActivePointerId = motionEvent.getPointerId(0);
                initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(motionEvent);
                setIsBeingDragged(!this.mScroller.isFinished());
            }
        }
        return this.mIsBeingDragged;
    }

    private boolean isInContentBounds(MotionEvent motionEvent) {
        return isInContentBounds(motionEvent.getY());
    }

    @VisibleForTesting
    void setIsBeingDragged(boolean z) {
        this.mIsBeingDragged = z;
        if (z) {
            requestDisallowInterceptTouchEvent(true);
            cancelLongPress();
            resetExposedMenuView(true, true);
        }
    }

    public void requestDisallowLongPress() {
        cancelLongPress();
    }

    public void requestDisallowDismiss() {
        this.mDisallowDismissInThisMotion = true;
    }

    @Override // android.view.View
    public void cancelLongPress() {
        this.mSwipeHelper.cancelLongPress();
    }

    public void setOnEmptySpaceClickListener(OnEmptySpaceClickListener onEmptySpaceClickListener) {
        this.mOnEmptySpaceClickListener = onEmptySpaceClickListener;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0021, code lost:
        if (r5 != 16908346) goto L_0x0059;
     */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x004d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean performAccessibilityActionInternal(int r5, android.os.Bundle r6) {
        /*
            r4 = this;
            boolean r6 = super.performAccessibilityActionInternal(r5, r6)
            r0 = 1
            if (r6 == 0) goto L_0x0008
            return r0
        L_0x0008:
            boolean r6 = r4.isEnabled()
            r1 = 0
            if (r6 != 0) goto L_0x0010
            return r1
        L_0x0010:
            r6 = -1
            r2 = 4096(0x1000, float:5.74E-42)
            if (r5 == r2) goto L_0x0024
            r2 = 8192(0x2000, float:1.14794E-41)
            if (r5 == r2) goto L_0x0025
            r2 = 16908344(0x1020038, float:2.3877386E-38)
            if (r5 == r2) goto L_0x0025
            r6 = 16908346(0x102003a, float:2.3877392E-38)
            if (r5 == r6) goto L_0x0024
            goto L_0x0059
        L_0x0024:
            r6 = r0
        L_0x0025:
            int r5 = r4.getHeight()
            int r2 = r4.mPaddingBottom
            int r5 = r5 - r2
            int r2 = r4.mTopPadding
            int r5 = r5 - r2
            int r2 = r4.mPaddingTop
            int r5 = r5 - r2
            com.android.systemui.statusbar.NotificationShelf r2 = r4.mShelf
            int r2 = r2.getIntrinsicHeight()
            int r5 = r5 - r2
            int r2 = r4.mOwnScrollY
            int r6 = r6 * r5
            int r2 = r2 + r6
            int r5 = r4.getScrollRange()
            int r5 = java.lang.Math.min(r2, r5)
            int r5 = java.lang.Math.max(r1, r5)
            int r6 = r4.mOwnScrollY
            if (r5 == r6) goto L_0x0059
            android.widget.OverScroller r2 = r4.mScroller
            int r3 = r4.mScrollX
            int r5 = r5 - r6
            r2.startScroll(r3, r6, r1, r5)
            r4.lambda$new$3()
            return r0
        L_0x0059:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.performAccessibilityActionInternal(int, android.os.Bundle):boolean");
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (!z) {
            cancelLongPress();
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void clearChildFocus(View view) {
        super.clearChildFocus(view);
        if (this.mForcedScroll == view) {
            this.mForcedScroll = null;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isScrolledToBottom() {
        return this.mScrollAdapter.isScrolledToBottom();
    }

    /* access modifiers changed from: package-private */
    public int getEmptyBottomMargin() {
        return Math.max(this.mMaxLayoutHeight - this.mContentHeight, 0);
    }

    /* access modifiers changed from: package-private */
    public void onExpansionStarted() {
        this.mIsExpansionChanging = true;
        this.mAmbientState.setExpansionChanging(true);
    }

    /* access modifiers changed from: package-private */
    public void onExpansionStopped() {
        this.mIsExpansionChanging = false;
        this.mAmbientState.setExpansionChanging(false);
        if (!this.mIsExpanded) {
            resetScrollPosition();
            this.mStatusBar.resetUserExpandedStates();
            clearTemporaryViews();
            clearUserLockedViews();
            if (this.mSwipeHelper.isSwiping()) {
                this.mSwipeHelper.resetSwipeState();
                updateContinuousShadowDrawing();
            }
        }
    }

    private void clearUserLockedViews() {
        for (int i = 0; i < getChildCount(); i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView instanceof ExpandableNotificationRow) {
                ((ExpandableNotificationRow) expandableView).setUserLocked(false);
            }
        }
    }

    private void clearTemporaryViews() {
        clearTemporaryViewsInGroup(this);
        for (int i = 0; i < getChildCount(); i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView instanceof ExpandableNotificationRow) {
                clearTemporaryViewsInGroup(((ExpandableNotificationRow) expandableView).getChildrenContainer());
            }
        }
    }

    private void clearTemporaryViewsInGroup(ViewGroup viewGroup) {
        while (viewGroup != null && viewGroup.getTransientViewCount() != 0) {
            viewGroup.removeTransientView(viewGroup.getTransientView(0));
        }
    }

    /* access modifiers changed from: package-private */
    public void onPanelTrackingStarted() {
        this.mPanelTracking = true;
        this.mAmbientState.setPanelTracking(true);
        resetExposedMenuView(true, true);
    }

    /* access modifiers changed from: package-private */
    public void onPanelTrackingStopped() {
        this.mPanelTracking = false;
        this.mAmbientState.setPanelTracking(false);
    }

    /* access modifiers changed from: package-private */
    public void resetScrollPosition() {
        this.mScroller.abortAnimation();
        setOwnScrollY(0);
    }

    private void setIsExpanded(boolean z) {
        boolean z2 = z != this.mIsExpanded;
        this.mIsExpanded = z;
        this.mStackScrollAlgorithm.setIsExpanded(z);
        this.mAmbientState.setShadeExpanded(z);
        this.mStateAnimator.setShadeExpanded(z);
        this.mSwipeHelper.setIsExpanded(z);
        if (z2) {
            this.mWillExpand = false;
            if (!this.mIsExpanded) {
                this.mGroupExpansionManager.collapseGroups();
                this.mExpandHelper.cancelImmediately();
            }
            updateNotificationAnimationStates();
            updateChronometers();
            requestChildrenUpdate();
            updateUseRoundedRectClipping();
        }
    }

    private void updateChronometers() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            updateChronometerForChild(getChildAt(i));
        }
    }

    void updateChronometerForChild(View view) {
        if (view instanceof ExpandableNotificationRow) {
            ((ExpandableNotificationRow) view).setChronometerRunning(this.mIsExpanded);
        }
    }

    /* access modifiers changed from: package-private */
    public void onChildHeightChanged(ExpandableView expandableView, boolean z) {
        boolean z2 = this.mAnimateStackYForContentHeightChange;
        if (z) {
            this.mAnimateStackYForContentHeightChange = true;
        }
        updateContentHeight();
        updateScrollPositionOnExpandInBottom(expandableView);
        clampScrollPosition();
        notifyHeightChangeListener(expandableView, z);
        ExpandableView expandableView2 = null;
        ExpandableNotificationRow expandableNotificationRow = expandableView instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) expandableView : null;
        NotificationSection firstVisibleSection = getFirstVisibleSection();
        if (firstVisibleSection != null) {
            expandableView2 = firstVisibleSection.getFirstVisibleChild();
        }
        if (expandableNotificationRow != null && (expandableNotificationRow == expandableView2 || expandableNotificationRow.getNotificationParent() == expandableView2)) {
            updateAlgorithmLayoutMinHeight();
        }
        if (z) {
            requestAnimationOnViewResize(expandableNotificationRow);
        }
        requestChildrenUpdate();
        this.mAnimateStackYForContentHeightChange = z2;
    }

    /* access modifiers changed from: package-private */
    public void onChildHeightReset(ExpandableView expandableView) {
        updateAnimationState(expandableView);
        updateChronometerForChild(expandableView);
    }

    private void updateScrollPositionOnExpandInBottom(ExpandableView expandableView) {
        ExpandableView expandableView2;
        if ((expandableView instanceof ExpandableNotificationRow) && !onKeyguard()) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
            if (expandableNotificationRow.isUserLocked() && expandableNotificationRow != getFirstChildNotGone() && !expandableNotificationRow.isSummaryWithChildren()) {
                float translationY = expandableNotificationRow.getTranslationY() + ((float) expandableNotificationRow.getActualHeight());
                if (expandableNotificationRow.isChildInGroup()) {
                    translationY += expandableNotificationRow.getNotificationParent().getTranslationY();
                }
                int i = this.mMaxLayoutHeight + ((int) this.mStackTranslation);
                NotificationSection lastVisibleSection = getLastVisibleSection();
                if (lastVisibleSection == null) {
                    expandableView2 = null;
                } else {
                    expandableView2 = lastVisibleSection.getLastVisibleChild();
                }
                if (!(expandableNotificationRow == expandableView2 || this.mShelf.getVisibility() == 8)) {
                    i -= this.mShelf.getIntrinsicHeight() + this.mPaddingBetweenElements;
                }
                float f = (float) i;
                if (translationY > f) {
                    setOwnScrollY((int) ((((float) this.mOwnScrollY) + translationY) - f));
                    this.mDisallowScrollingInThisMotion = true;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setOnHeightChangedListener(ExpandableView.OnHeightChangedListener onHeightChangedListener) {
        this.mOnHeightChangedListener = onHeightChangedListener;
    }

    /* access modifiers changed from: package-private */
    public void onChildAnimationFinished() {
        setAnimationRunning(false);
        requestChildrenUpdate();
        runAnimationFinishedRunnables();
        clearTransient();
        clearHeadsUpDisappearRunning();
    }

    private void clearHeadsUpDisappearRunning() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                expandableNotificationRow.setHeadsUpAnimatingAway(false);
                if (expandableNotificationRow.isSummaryWithChildren()) {
                    for (ExpandableNotificationRow expandableNotificationRow2 : expandableNotificationRow.getAttachedChildren()) {
                        expandableNotificationRow2.setHeadsUpAnimatingAway(false);
                    }
                }
            }
        }
    }

    private void clearTransient() {
        Iterator<ExpandableView> it = this.mClearTransientViewsWhenFinished.iterator();
        while (it.hasNext()) {
            StackStateAnimator.removeTransientView(it.next());
        }
        this.mClearTransientViewsWhenFinished.clear();
    }

    private void runAnimationFinishedRunnables() {
        Iterator<Runnable> it = this.mAnimationFinishedRunnables.iterator();
        while (it.hasNext()) {
            it.next().run();
        }
        this.mAnimationFinishedRunnables.clear();
    }

    /* access modifiers changed from: package-private */
    public void setDimmed(boolean z, boolean z2) {
        boolean onKeyguard = z & onKeyguard();
        this.mAmbientState.setDimmed(onKeyguard);
        if (!z2 || !this.mAnimationsEnabled) {
            setDimAmount(onKeyguard ? 1.0f : 0.0f);
        } else {
            this.mDimmedNeedsAnimation = true;
            this.mNeedsAnimation = true;
            animateDimmed(onKeyguard);
        }
        requestChildrenUpdate();
    }

    @VisibleForTesting
    boolean isDimmed() {
        return this.mAmbientState.isDimmed();
    }

    /* access modifiers changed from: private */
    public void setDimAmount(float f) {
        this.mDimAmount = f;
        updateBackgroundDimming();
    }

    private void animateDimmed(boolean z) {
        ValueAnimator valueAnimator = this.mDimAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        float f = z ? 1.0f : 0.0f;
        float f2 = this.mDimAmount;
        if (f != f2) {
            ValueAnimator ofFloat = TimeAnimator.ofFloat(f2, f);
            this.mDimAnimator = ofFloat;
            ofFloat.setDuration(220L);
            this.mDimAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            this.mDimAnimator.addListener(this.mDimEndListener);
            this.mDimAnimator.addUpdateListener(this.mDimUpdateListener);
            this.mDimAnimator.start();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateSensitiveness(boolean z, boolean z2) {
        if (z2 != this.mAmbientState.isHideSensitive()) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                ((ExpandableView) getChildAt(i)).setHideSensitiveForIntrinsicHeight(z2);
            }
            this.mAmbientState.setHideSensitive(z2);
            if (z && this.mAnimationsEnabled) {
                this.mHideSensitiveNeedsAnimation = true;
                this.mNeedsAnimation = true;
            }
            updateContentHeight();
            requestChildrenUpdate();
        }
    }

    /* access modifiers changed from: package-private */
    public void setActivatedChild(ActivatableNotificationView activatableNotificationView) {
        this.mAmbientState.setActivatedChild(activatableNotificationView);
        if (this.mAnimationsEnabled) {
            this.mActivateNeedsAnimation = true;
            this.mNeedsAnimation = true;
        }
        requestChildrenUpdate();
    }

    public ActivatableNotificationView getActivatedChild() {
        return this.mAmbientState.getActivatedChild();
    }

    private void applyCurrentState() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ((ExpandableView) getChildAt(i)).applyViewState();
        }
        NotificationLogger.OnChildLocationsChangedListener onChildLocationsChangedListener = this.mListener;
        if (onChildLocationsChangedListener != null) {
            onChildLocationsChangedListener.onChildLocationsChanged();
        }
        runAnimationFinishedRunnables();
        setAnimationRunning(false);
        updateBackground();
        updateViewShadows();
    }

    private void updateViewShadows() {
        float f;
        for (int i = 0; i < getChildCount(); i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView.getVisibility() != 8) {
                this.mTmpSortedChildren.add(expandableView);
            }
        }
        Collections.sort(this.mTmpSortedChildren, this.mViewPositionComparator);
        ExpandableView expandableView2 = null;
        int i2 = 0;
        while (i2 < this.mTmpSortedChildren.size()) {
            ExpandableView expandableView3 = this.mTmpSortedChildren.get(i2);
            float translationZ = expandableView3.getTranslationZ();
            if (expandableView2 == null) {
                f = translationZ;
            } else {
                f = expandableView2.getTranslationZ();
            }
            float f2 = f - translationZ;
            if (f2 <= 0.0f || f2 >= 0.1f) {
                expandableView3.setFakeShadowIntensity(0.0f, 0.0f, 0, 0);
            } else {
                expandableView3.setFakeShadowIntensity(f2 / 0.1f, expandableView2.getOutlineAlpha(), (int) (((expandableView2.getTranslationY() + ((float) expandableView2.getActualHeight())) - expandableView3.getTranslationY()) - ((float) expandableView2.getExtraBottomPadding())), (int) (((float) expandableView2.getOutlineTranslation()) + expandableView2.getTranslation()));
            }
            i2++;
            expandableView2 = expandableView3;
        }
        this.mTmpSortedChildren.clear();
    }

    /* access modifiers changed from: package-private */
    public void updateDecorViews() {
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(((ViewGroup) this).mContext, 16842806);
        this.mSectionsManager.setHeaderForegroundColor(colorAttrDefaultColor);
        this.mFooterView.setTextColor(colorAttrDefaultColor);
        this.mEmptyShadeView.setTextColor(colorAttrDefaultColor);
    }

    /* access modifiers changed from: package-private */
    public void goToFullShade(long j) {
        this.mGoToFullShadeNeedsAnimation = true;
        this.mGoToFullShadeDelay = j;
        this.mNeedsAnimation = true;
        requestChildrenUpdate();
    }

    public void cancelExpandHelper() {
        this.mExpandHelper.cancel();
    }

    /* access modifiers changed from: package-private */
    public void setIntrinsicPadding(int i) {
        this.mIntrinsicPadding = i;
        this.mAmbientState.setIntrinsicPadding(i);
    }

    /* access modifiers changed from: package-private */
    public int getIntrinsicPadding() {
        return this.mIntrinsicPadding;
    }

    public void setDozing(boolean z, boolean z2, PointF pointF) {
        if (this.mAmbientState.isDozing() != z) {
            this.mAmbientState.setDozing(z);
            requestChildrenUpdate();
            notifyHeightChangeListener(this.mShelf);
        }
    }

    /* access modifiers changed from: package-private */
    public void setHideAmount(float f, float f2) {
        this.mLinearHideAmount = f;
        this.mInterpolatedHideAmount = f2;
        boolean isFullyHidden = this.mAmbientState.isFullyHidden();
        boolean isHiddenAtAll = this.mAmbientState.isHiddenAtAll();
        this.mAmbientState.setHideAmount(f2);
        boolean isFullyHidden2 = this.mAmbientState.isFullyHidden();
        boolean isHiddenAtAll2 = this.mAmbientState.isHiddenAtAll();
        if (isFullyHidden2 != isFullyHidden) {
            updateVisibility();
        }
        if (!isHiddenAtAll && isHiddenAtAll2) {
            resetExposedMenuView(true, true);
        }
        if (!(isFullyHidden2 == isFullyHidden && isHiddenAtAll == isHiddenAtAll2)) {
            invalidateOutline();
        }
        updateAlgorithmHeightAndPadding();
        updateBackgroundDimming();
        requestChildrenUpdate();
        updateOwnTranslationZ();
    }

    private void updateOwnTranslationZ() {
        ExpandableView firstChildNotGone;
        setTranslationZ((!this.mKeyguardBypassEnabledProvider.getBypassEnabled() || !this.mAmbientState.isHiddenAtAll() || (firstChildNotGone = getFirstChildNotGone()) == null || !firstChildNotGone.showingPulsing()) ? 0.0f : firstChildNotGone.getTranslationZ());
    }

    private void updateVisibility() {
        int i = 0;
        if (!(!this.mAmbientState.isFullyHidden() || !onKeyguard())) {
            i = 4;
        }
        setVisibility(i);
    }

    /* access modifiers changed from: package-private */
    public void notifyHideAnimationStart(boolean z) {
        Interpolator interpolator;
        float f = this.mInterpolatedHideAmount;
        if (f == 0.0f || f == 1.0f) {
            this.mBackgroundXFactor = z ? 1.8f : 1.5f;
            if (z) {
                interpolator = Interpolators.FAST_OUT_SLOW_IN_REVERSE;
            } else {
                interpolator = Interpolators.FAST_OUT_SLOW_IN;
            }
            this.mHideXInterpolator = interpolator;
        }
    }

    void setFooterView(FooterView footerView) {
        int i;
        FooterView footerView2 = this.mFooterView;
        if (footerView2 != null) {
            i = indexOfChild(footerView2);
            removeView(this.mFooterView);
        } else {
            i = -1;
        }
        this.mFooterView = footerView;
        addView(footerView, i);
    }

    void setEmptyShadeView(EmptyShadeView emptyShadeView) {
        int i;
        EmptyShadeView emptyShadeView2 = this.mEmptyShadeView;
        if (emptyShadeView2 != null) {
            i = indexOfChild(emptyShadeView2);
            removeView(this.mEmptyShadeView);
        } else {
            i = -1;
        }
        this.mEmptyShadeView = emptyShadeView;
        addView(emptyShadeView, i);
    }

    /* access modifiers changed from: package-private */
    public void updateEmptyShadeView(boolean z, boolean z2) {
        this.mEmptyShadeView.setVisible(z, this.mIsExpanded && this.mAnimationsEnabled);
        int textResource = this.mEmptyShadeView.getTextResource();
        int i = z2 ? R$string.dnd_suppressing_shade_text : R$string.empty_shade_text;
        if (textResource != i) {
            this.mEmptyShadeView.setText(i);
        }
    }

    public void updateFooterView(boolean z, boolean z2, boolean z3) {
        FooterView footerView = this.mFooterView;
        if (footerView != null) {
            boolean z4 = this.mIsExpanded && this.mAnimationsEnabled;
            footerView.setVisible(z, z4);
            this.mFooterView.setSecondaryVisible(z2, z4);
            this.mFooterView.showHistory(z3);
        }
    }

    public void setDismissAllInProgress(boolean z) {
        this.mDismissAllInProgress = z;
        this.mAmbientState.setDismissAllInProgress(z);
        handleDismissAllClipping();
    }

    /* access modifiers changed from: package-private */
    public boolean getDismissAllInProgress() {
        return this.mDismissAllInProgress;
    }

    private void handleDismissAllClipping() {
        int childCount = getChildCount();
        boolean z = false;
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView.getVisibility() != 8) {
                if (!this.mDismissAllInProgress || !z) {
                    expandableView.setMinClipTopAmount(0);
                } else {
                    expandableView.setMinClipTopAmount(expandableView.getClipTopAmount());
                }
                z = canChildBeDismissed(expandableView);
            }
        }
    }

    public int getPaddingAfterMedia() {
        return this.mGapHeight + this.mPaddingBetweenElements;
    }

    public int getEmptyShadeViewHeight() {
        return this.mEmptyShadeView.getHeight();
    }

    public float getBottomMostNotificationBottom() {
        int childCount = getChildCount();
        float f = 0.0f;
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView.getVisibility() != 8) {
                float translationY = (expandableView.getTranslationY() + ((float) expandableView.getActualHeight())) - ((float) expandableView.getClipBottomAmount());
                if (translationY > f) {
                    f = translationY;
                }
            }
        }
        return f + getStackTranslation();
    }

    public void setStatusBar(StatusBar statusBar) {
        this.mStatusBar = statusBar;
    }

    /* access modifiers changed from: package-private */
    public void requestAnimateEverything() {
        if (this.mIsExpanded && this.mAnimationsEnabled) {
            this.mEverythingNeedsAnimation = true;
            this.mNeedsAnimation = true;
            requestChildrenUpdate();
        }
    }

    public boolean isBelowLastNotification(float f, float f2) {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            ExpandableView expandableView = (ExpandableView) getChildAt(childCount);
            if (expandableView.getVisibility() != 8) {
                float y = expandableView.getY();
                if (y > f2) {
                    return false;
                }
                boolean z = f2 > (((float) expandableView.getActualHeight()) + y) - ((float) expandableView.getClipBottomAmount());
                FooterView footerView = this.mFooterView;
                if (expandableView == footerView) {
                    if (!z && !footerView.isOnEmptySpace(f - footerView.getX(), f2 - y)) {
                        return false;
                    }
                } else if (expandableView == this.mEmptyShadeView) {
                    return true;
                } else {
                    if (!z) {
                        return false;
                    }
                }
            }
        }
        return f2 > ((float) this.mTopPadding) + this.mStackTranslation;
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEventInternal(accessibilityEvent);
        accessibilityEvent.setScrollable(this.mScrollable);
        accessibilityEvent.setMaxScrollX(((ViewGroup) this).mScrollX);
        accessibilityEvent.setScrollY(this.mOwnScrollY);
        accessibilityEvent.setMaxScrollY(getScrollRange());
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfoInternal(accessibilityNodeInfo);
        if (this.mScrollable) {
            accessibilityNodeInfo.setScrollable(true);
            if (this.mBackwardScrollable) {
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP);
            }
            if (this.mForwardScrollable) {
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN);
            }
        }
        accessibilityNodeInfo.setClassName(ScrollView.class.getName());
    }

    public void generateChildOrderChangedEvent() {
        if (this.mIsExpanded && this.mAnimationsEnabled) {
            this.mGenerateChildOrderChangedEvent = true;
            this.mNeedsAnimation = true;
            requestChildrenUpdate();
        }
    }

    public int getContainerChildCount() {
        return getChildCount();
    }

    public View getContainerChildAt(int i) {
        return getChildAt(i);
    }

    public void removeContainerView(View view) {
        Assert.isMainThread();
        removeView(view);
        if ((view instanceof ExpandableNotificationRow) && !this.mController.isShowingEmptyShadeView()) {
            this.mController.updateShowEmptyShadeView();
            updateFooter();
        }
        updateSpeedBumpIndex();
    }

    public void addContainerView(View view) {
        Assert.isMainThread();
        addView(view);
        if ((view instanceof ExpandableNotificationRow) && this.mController.isShowingEmptyShadeView()) {
            this.mController.updateShowEmptyShadeView();
            updateFooter();
        }
        updateSpeedBumpIndex();
    }

    public void addContainerViewAt(View view, int i) {
        Assert.isMainThread();
        addView(view, i);
        if ((view instanceof ExpandableNotificationRow) && this.mController.isShowingEmptyShadeView()) {
            this.mController.updateShowEmptyShadeView();
            updateFooter();
        }
        updateSpeedBumpIndex();
    }

    public void runAfterAnimationFinished(Runnable runnable) {
        this.mAnimationFinishedRunnables.add(runnable);
    }

    public void generateHeadsUpAnimation(NotificationEntry notificationEntry, boolean z) {
        generateHeadsUpAnimation(notificationEntry.getHeadsUpAnimationView(), z);
    }

    public void generateHeadsUpAnimation(ExpandableNotificationRow expandableNotificationRow, boolean z) {
        if (!this.mAnimationsEnabled) {
            return;
        }
        if (z || this.mHeadsUpGoingAwayAnimationsAllowed) {
            this.mHeadsUpChangeAnimations.add(new Pair<>(expandableNotificationRow, Boolean.valueOf(z)));
            this.mNeedsAnimation = true;
            if (!this.mIsExpanded && !this.mWillExpand && !z) {
                expandableNotificationRow.setHeadsUpAnimatingAway(true);
            }
            requestChildrenUpdate();
        }
    }

    public void setHeadsUpBoundaries(int i, int i2) {
        this.mAmbientState.setMaxHeadsUpTranslation((float) (i - i2));
        this.mStateAnimator.setHeadsUpAppearHeightBottom(i);
        requestChildrenUpdate();
    }

    public void setWillExpand(boolean z) {
        this.mWillExpand = z;
    }

    public void setTrackingHeadsUp(ExpandableNotificationRow expandableNotificationRow) {
        this.mAmbientState.setTrackedHeadsUpRow(expandableNotificationRow);
        this.mTrackingHeadsUp = expandableNotificationRow != null;
    }

    public void forceNoOverlappingRendering(boolean z) {
        this.mForceNoOverlappingRendering = z;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return !this.mForceNoOverlappingRendering && super.hasOverlappingRendering();
    }

    public void setAnimationRunning(boolean z) {
        if (z != this.mAnimationRunning) {
            if (z) {
                getViewTreeObserver().addOnPreDrawListener(this.mRunningAnimationUpdater);
            } else {
                getViewTreeObserver().removeOnPreDrawListener(this.mRunningAnimationUpdater);
            }
            this.mAnimationRunning = z;
            updateContinuousShadowDrawing();
        }
    }

    public boolean isExpanded() {
        return this.mIsExpanded;
    }

    public void setPulsing(boolean z, boolean z2) {
        if (this.mPulsing || z) {
            this.mPulsing = z;
            this.mAmbientState.setPulsing(z);
            this.mSwipeHelper.setPulsing(z);
            updateNotificationAnimationStates();
            updateAlgorithmHeightAndPadding();
            updateContentHeight();
            requestChildrenUpdate();
            notifyHeightChangeListener(null, z2);
        }
    }

    public void setQsExpanded(boolean z) {
        this.mQsExpanded = z;
        updateAlgorithmLayoutMinHeight();
        updateScrollability();
    }

    /* access modifiers changed from: package-private */
    public boolean isQsExpanded() {
        return this.mQsExpanded;
    }

    public void setQsExpansionFraction(float f) {
        this.mQsExpansionFraction = f;
        updateUseRoundedRectClipping();
        int i = this.mOwnScrollY;
        if (i > 0) {
            setOwnScrollY((int) MathUtils.lerp((float) i, 0.0f, this.mQsExpansionFraction));
        }
    }

    private void setOwnScrollY(int i) {
        setOwnScrollY(i, false);
    }

    private void setOwnScrollY(int i, boolean z) {
        int i2 = this.mOwnScrollY;
        if (i != i2) {
            int i3 = ((ViewGroup) this).mScrollX;
            onScrollChanged(i3, i, i3, i2);
            this.mOwnScrollY = i;
            this.mAmbientState.setScrollY(i);
            updateOnScrollChange();
            updateStackPosition(z);
        }
    }

    private void updateOnScrollChange() {
        Consumer<Integer> consumer = this.mScrollListener;
        if (consumer != null) {
            consumer.accept(Integer.valueOf(this.mOwnScrollY));
        }
        updateForwardAndBackwardScrollability();
        requestChildrenUpdate();
    }

    public void setShelfController(NotificationShelfController notificationShelfController) {
        int i;
        NotificationShelf notificationShelf = this.mShelf;
        if (notificationShelf != null) {
            i = indexOfChild(notificationShelf);
            removeView(this.mShelf);
        } else {
            i = -1;
        }
        NotificationShelf view = notificationShelfController.getView();
        this.mShelf = view;
        addView(view, i);
        this.mAmbientState.setShelf(this.mShelf);
        this.mStateAnimator.setShelf(this.mShelf);
        notificationShelfController.bind(this.mAmbientState, this.mController);
    }

    public void setMaxDisplayedNotifications(int i) {
        if (this.mMaxDisplayedNotifications != i) {
            this.mMaxDisplayedNotifications = i;
            updateContentHeight();
            notifyHeightChangeListener(this.mShelf);
        }
    }

    public void setShouldShowShelfOnly(boolean z) {
        this.mShouldShowShelfOnly = z;
        updateAlgorithmLayoutMinHeight();
    }

    public int getMinExpansionHeight() {
        int intrinsicHeight = this.mShelf.getIntrinsicHeight();
        int i = this.mWaterfallTopInset;
        return (intrinsicHeight - (((this.mShelf.getIntrinsicHeight() - this.mStatusBarHeight) + i) / 2)) + i;
    }

    public void setInHeadsUpPinnedMode(boolean z) {
        this.mInHeadsUpPinnedMode = z;
        updateClipping();
    }

    public void setHeadsUpAnimatingAway(boolean z) {
        this.mHeadsUpAnimatingAway = z;
        updateClipping();
    }

    @VisibleForTesting
    public void setStatusBarState(int i) {
        this.mStatusBarState = i;
        this.mAmbientState.setStatusBarState(i);
        updateSpeedBumpIndex();
        updateDismissBehavior();
    }

    /* access modifiers changed from: package-private */
    public void onStatePostChange(boolean z) {
        boolean onKeyguard = onKeyguard();
        this.mAmbientState.setActivatedChild(null);
        this.mAmbientState.setDimmed(onKeyguard);
        HeadsUpAppearanceController headsUpAppearanceController = this.mHeadsUpAppearanceController;
        if (headsUpAppearanceController != null) {
            headsUpAppearanceController.onStateChanged();
        }
        setDimmed(onKeyguard, z);
        setExpandingEnabled(!onKeyguard);
        ActivatableNotificationView activatedChild = getActivatedChild();
        setActivatedChild(null);
        if (activatedChild != null) {
            activatedChild.makeInactive(false);
        }
        updateFooter();
        requestChildrenUpdate();
        onUpdateRowStates();
        updateVisibility();
    }

    public void setExpandingVelocity(float f) {
        this.mAmbientState.setExpandingVelocity(f);
    }

    public float getOpeningHeight() {
        if (this.mEmptyShadeView.getVisibility() == 8) {
            return (float) getMinExpansionHeight();
        }
        return getAppearEndPosition();
    }

    public void setIsFullWidth(boolean z) {
        this.mAmbientState.setPanelFullWidth(z);
    }

    public void setUnlockHintRunning(boolean z) {
        this.mAmbientState.setUnlockHintRunning(z);
    }

    public void setQsCustomizerShowing(boolean z) {
        this.mAmbientState.setQsCustomizerShowing(z);
        requestChildrenUpdate();
    }

    public void setHeadsUpGoingAwayAnimationsAllowed(boolean z) {
        this.mHeadsUpGoingAwayAnimationsAllowed = z;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        String str2;
        Object[] objArr = new Object[10];
        objArr[0] = getClass().getSimpleName();
        String str3 = "T";
        objArr[1] = this.mPulsing ? str3 : "f";
        if (this.mAmbientState.isQsCustomizerShowing()) {
            str = str3;
        } else {
            str = "f";
        }
        objArr[2] = str;
        if (getVisibility() == 0) {
            str2 = "visible";
        } else {
            str2 = getVisibility() == 8 ? "gone" : "invisible";
        }
        objArr[3] = str2;
        objArr[4] = Float.valueOf(getAlpha());
        objArr[5] = Integer.valueOf(this.mAmbientState.getScrollY());
        objArr[6] = Integer.valueOf(this.mMaxTopPadding);
        if (!this.mShouldShowShelfOnly) {
            str3 = "f";
        }
        objArr[7] = str3;
        objArr[8] = Float.valueOf(this.mQsExpansionFraction);
        objArr[9] = Float.valueOf(this.mAmbientState.getHideAmount());
        printWriter.println(String.format("[%s: pulsing=%s qsCustomizerShowing=%s visibility=%s alpha=%f scrollY:%d maxTopPadding=%d showShelfOnly=%s qsExpandFraction=%f hideAmount=%f]", objArr));
        int childCount = getChildCount();
        printWriter.println("  Number of children: " + childCount);
        printWriter.println();
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            expandableView.dump(fileDescriptor, printWriter, strArr);
            if (!(expandableView instanceof ExpandableNotificationRow)) {
                printWriter.println("  " + expandableView.getClass().getSimpleName());
                ExpandableViewState viewState = expandableView.getViewState();
                if (viewState == null) {
                    printWriter.println("    no viewState!!!");
                } else {
                    printWriter.print("    ");
                    viewState.dump(fileDescriptor, printWriter, strArr);
                    printWriter.println();
                    printWriter.println();
                }
            }
        }
        int transientViewCount = getTransientViewCount();
        printWriter.println("  Transient Views: " + transientViewCount);
        for (int i2 = 0; i2 < transientViewCount; i2++) {
            ((ExpandableView) getTransientView(i2)).dump(fileDescriptor, printWriter, strArr);
        }
        View swipedView = this.mSwipeHelper.getSwipedView();
        printWriter.println("  Swiped view: " + swipedView);
        if (swipedView instanceof ExpandableView) {
            ((ExpandableView) swipedView).dump(fileDescriptor, printWriter, strArr);
        }
    }

    public boolean isFullyHidden() {
        return this.mAmbientState.isFullyHidden();
    }

    public void addOnExpandedHeightChangedListener(BiConsumer<Float, Float> biConsumer) {
        this.mExpandedHeightListeners.add(biConsumer);
    }

    public void removeOnExpandedHeightChangedListener(BiConsumer<Float, Float> biConsumer) {
        this.mExpandedHeightListeners.remove(biConsumer);
    }

    /* access modifiers changed from: package-private */
    public void setHeadsUpAppearanceController(HeadsUpAppearanceController headsUpAppearanceController) {
        this.mHeadsUpAppearanceController = headsUpAppearanceController;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004f, code lost:
        if (r11.mTmpRect.height() > 0) goto L_0x0053;
     */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0094 A[SYNTHETIC] */
    @com.android.internal.annotations.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void clearNotifications(int r12, boolean r13) {
        /*
            r11 = this;
            int r0 = r11.getChildCount()
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>(r0)
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>(r0)
            r3 = 0
            r4 = r3
        L_0x0010:
            if (r4 >= r0) goto L_0x0098
            android.view.View r5 = r11.getChildAt(r4)
            boolean r6 = r5 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            if (r6 == 0) goto L_0x0094
            r6 = r5
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r6 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r6
            android.graphics.Rect r7 = r11.mTmpRect
            boolean r7 = r5.getClipBounds(r7)
            boolean r8 = r11.includeChildInDismissAll(r6, r12)
            r9 = 1
            if (r8 == 0) goto L_0x0041
            r2.add(r6)
            int r8 = r5.getVisibility()
            if (r8 != 0) goto L_0x0052
            if (r7 == 0) goto L_0x003d
            android.graphics.Rect r7 = r11.mTmpRect
            int r7 = r7.height()
            if (r7 <= 0) goto L_0x0052
        L_0x003d:
            r1.add(r5)
            goto L_0x0053
        L_0x0041:
            int r5 = r5.getVisibility()
            if (r5 != 0) goto L_0x0052
            if (r7 == 0) goto L_0x0053
            android.graphics.Rect r5 = r11.mTmpRect
            int r5 = r5.height()
            if (r5 <= 0) goto L_0x0052
            goto L_0x0053
        L_0x0052:
            r9 = r3
        L_0x0053:
            java.util.List r5 = r6.getAttachedChildren()
            if (r5 == 0) goto L_0x0094
            java.util.Iterator r5 = r5.iterator()
        L_0x005d:
            boolean r7 = r5.hasNext()
            if (r7 == 0) goto L_0x0094
            java.lang.Object r7 = r5.next()
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r7 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r7
            boolean r8 = r11.includeChildInDismissAll(r6, r12)
            if (r8 == 0) goto L_0x005d
            r2.add(r7)
            if (r9 == 0) goto L_0x005d
            boolean r8 = r6.areChildrenExpanded()
            if (r8 == 0) goto L_0x005d
            android.graphics.Rect r8 = r11.mTmpRect
            boolean r8 = r7.getClipBounds(r8)
            int r10 = r7.getVisibility()
            if (r10 != 0) goto L_0x005d
            if (r8 == 0) goto L_0x0090
            android.graphics.Rect r8 = r11.mTmpRect
            int r8 = r8.height()
            if (r8 <= 0) goto L_0x005d
        L_0x0090:
            r1.add(r7)
            goto L_0x005d
        L_0x0094:
            int r4 = r4 + 1
            goto L_0x0010
        L_0x0098:
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$DismissListener r0 = r11.mDismissListener
            if (r0 == 0) goto L_0x009f
            r0.onDismiss(r12)
        L_0x009f:
            boolean r0 = r2.isEmpty()
            if (r0 == 0) goto L_0x00af
            if (r13 == 0) goto L_0x00ae
            com.android.systemui.statusbar.phone.ShadeController r11 = r11.mShadeController
            if (r11 == 0) goto L_0x00ae
            r11.animateCollapsePanels(r3)
        L_0x00ae:
            return
        L_0x00af:
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda8 r0 = new com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda8
            r0.<init>(r2, r12)
            r11.performDismissAllAnimations(r1, r13, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.clearNotifications(int, boolean):void");
    }

    private boolean includeChildInDismissAll(ExpandableNotificationRow expandableNotificationRow, int i) {
        return canChildBeDismissed(expandableNotificationRow) && matchesSelection(expandableNotificationRow, i);
    }

    private void performDismissAllAnimations(ArrayList<View> arrayList, boolean z, Runnable runnable) {
        NotificationStackScrollLayout$$ExternalSyntheticLambda9 notificationStackScrollLayout$$ExternalSyntheticLambda9 = new Runnable(z, runnable) { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda9
            public final /* synthetic */ boolean f$1;
            public final /* synthetic */ Runnable f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                NotificationStackScrollLayout.this.lambda$performDismissAllAnimations$7(this.f$1, this.f$2);
            }
        };
        if (arrayList.isEmpty()) {
            notificationStackScrollLayout$$ExternalSyntheticLambda9.run();
            return;
        }
        setDismissAllInProgress(true);
        int i = 140;
        int i2 = 180;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            View view = arrayList.get(size);
            NotificationStackScrollLayout$$ExternalSyntheticLambda9 notificationStackScrollLayout$$ExternalSyntheticLambda92 = null;
            if (size == 0) {
                notificationStackScrollLayout$$ExternalSyntheticLambda92 = notificationStackScrollLayout$$ExternalSyntheticLambda9;
            }
            dismissViewAnimated(view, notificationStackScrollLayout$$ExternalSyntheticLambda92, i2, 260);
            i = Math.max(50, i - 10);
            i2 += i;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$performDismissAllAnimations$7(boolean z, Runnable runnable) {
        if (z) {
            this.mShadeController.addPostCollapseAction(new Runnable(runnable) { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda7
                public final /* synthetic */ Runnable f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    NotificationStackScrollLayout.this.lambda$performDismissAllAnimations$6(this.f$1);
                }
            });
            this.mShadeController.animateCollapsePanels(0);
            return;
        }
        setDismissAllInProgress(false);
        runnable.run();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$performDismissAllAnimations$6(Runnable runnable) {
        setDismissAllInProgress(false);
        runnable.run();
    }

    public void setNotificationActivityStarter(NotificationActivityStarter notificationActivityStarter) {
        this.mNotificationActivityStarter = notificationActivityStarter;
    }

    @VisibleForTesting
    protected void inflateFooterView() {
        FooterView footerView = (FooterView) LayoutInflater.from(((ViewGroup) this).mContext).inflate(R$layout.status_bar_notification_footer, (ViewGroup) this, false);
        footerView.setDismissButtonClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NotificationStackScrollLayout.this.lambda$inflateFooterView$8(view);
            }
        });
        footerView.setManageButtonClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NotificationStackScrollLayout.this.lambda$inflateFooterView$9(view);
            }
        });
        setFooterView(footerView);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$inflateFooterView$8(View view) {
        FooterDismissListener footerDismissListener = this.mFooterDismissListener;
        if (footerDismissListener != null) {
            footerDismissListener.onDismiss();
        }
        clearNotifications(0, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$inflateFooterView$9(View view) {
        this.mNotificationActivityStarter.startHistoryIntent(view, this.mFooterView.isHistoryShown());
    }

    private void inflateEmptyShadeView() {
        EmptyShadeView emptyShadeView = (EmptyShadeView) LayoutInflater.from(((ViewGroup) this).mContext).inflate(R$layout.status_bar_no_notifications, (ViewGroup) this, false);
        emptyShadeView.setText(R$string.empty_shade_text);
        emptyShadeView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NotificationStackScrollLayout.this.lambda$inflateEmptyShadeView$10(view);
            }
        });
        setEmptyShadeView(emptyShadeView);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$inflateEmptyShadeView$10(View view) {
        Intent intent;
        boolean z = false;
        if (Settings.Secure.getIntForUser(((ViewGroup) this).mContext.getContentResolver(), "notification_history_enabled", 0, -2) == 1) {
            z = true;
        }
        if (z) {
            intent = new Intent("android.settings.NOTIFICATION_HISTORY");
        } else {
            intent = new Intent("android.settings.NOTIFICATION_SETTINGS");
        }
        this.mStatusBar.startActivity(intent, true, true, 536870912);
    }

    public void onUpdateRowStates() {
        ForegroundServiceDungeonView foregroundServiceDungeonView = this.mFgsSectionView;
        int i = 1;
        if (foregroundServiceDungeonView != null) {
            i = 2;
            changeViewPosition(foregroundServiceDungeonView, getChildCount() - 1);
        }
        int i2 = i + 1;
        changeViewPosition(this.mFooterView, getChildCount() - i);
        changeViewPosition(this.mEmptyShadeView, getChildCount() - i2);
        changeViewPosition(this.mShelf, getChildCount() - (i2 + 1));
    }

    public float setPulseHeight(float f) {
        float f2;
        this.mAmbientState.setPulseHeight(f);
        if (this.mKeyguardBypassEnabledProvider.getBypassEnabled()) {
            notifyAppearChangedListeners();
            f2 = Math.max(0.0f, f - ((float) getIntrinsicPadding()));
        } else {
            f2 = Math.max(0.0f, f - ((float) this.mAmbientState.getInnerHeight(true)));
        }
        requestChildrenUpdate();
        return f2;
    }

    public float getPulseHeight() {
        return this.mAmbientState.getPulseHeight();
    }

    public void setDozeAmount(float f) {
        this.mAmbientState.setDozeAmount(f);
        updateContinuousBackgroundDrawing();
        requestChildrenUpdate();
    }

    public boolean isFullyAwake() {
        return this.mAmbientState.isFullyAwake();
    }

    public void wakeUpFromPulse() {
        setPulseHeight(getWakeUpHeight());
        int childCount = getChildCount();
        float f = -1.0f;
        boolean z = true;
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView.getVisibility() != 8) {
                boolean z2 = expandableView == this.mShelf;
                if ((expandableView instanceof ExpandableNotificationRow) || z2) {
                    if (expandableView.getVisibility() != 0 || z2) {
                        if (!z) {
                            expandableView.setTranslationY(f);
                        }
                    } else if (z) {
                        f = (expandableView.getTranslationY() + ((float) expandableView.getActualHeight())) - ((float) this.mShelf.getIntrinsicHeight());
                        z = false;
                    }
                }
            }
        }
        this.mDimmedNeedsAnimation = true;
    }

    /* access modifiers changed from: package-private */
    public void setAnimateBottomOnLayout(boolean z) {
        this.mAnimateBottomOnLayout = z;
    }

    public void setOnPulseHeightChangedListener(Runnable runnable) {
        this.mAmbientState.setOnPulseHeightChangedListener(runnable);
    }

    public float calculateAppearFractionBypass() {
        return MathUtils.smoothStep(0.0f, (float) getIntrinsicPadding(), getPulseHeight());
    }

    public void setController(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        this.mController = notificationStackScrollLayoutController;
        notificationStackScrollLayoutController.getNoticationRoundessManager().setAnimatedChildren(this.mChildrenToAddAnimated);
    }

    public NotificationStackScrollLayoutController getController() {
        return this.mController;
    }

    /* access modifiers changed from: package-private */
    public void addSwipedOutView(View view) {
        this.mSwipedOutViews.add(view);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0026, code lost:
        if (r6.mSectionsManager.beginsSection(r7, r2) != false) goto L_0x0028;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onSwipeBegin(android.view.View r7) {
        /*
            r6 = this;
            boolean r0 = r7 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            int r0 = r6.indexOfChild(r7)
            if (r0 >= 0) goto L_0x000c
            return
        L_0x000c:
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager r1 = r6.mSectionsManager
            com.android.systemui.statusbar.notification.stack.NotificationSection[] r2 = r6.mSections
            java.util.List r3 = r6.getChildrenWithBackground()
            r1.updateFirstAndLastViewsForAllSections(r2, r3)
            r1 = 0
            if (r0 <= 0) goto L_0x0028
            int r2 = r0 + -1
            android.view.View r2 = r6.getChildAt(r2)
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager r3 = r6.mSectionsManager
            boolean r3 = r3.beginsSection(r7, r2)
            if (r3 == 0) goto L_0x0029
        L_0x0028:
            r2 = r1
        L_0x0029:
            int r3 = r6.getChildCount()
            r4 = 1
            if (r0 >= r3) goto L_0x003f
            int r0 = r0 + r4
            android.view.View r0 = r6.getChildAt(r0)
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager r3 = r6.mSectionsManager
            boolean r3 = r3.beginsSection(r0, r7)
            if (r3 == 0) goto L_0x003e
            goto L_0x003f
        L_0x003e:
            r1 = r0
        L_0x003f:
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r0 = r6.mController
            com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager r0 = r0.getNoticationRoundessManager()
            com.android.systemui.statusbar.notification.row.ExpandableView r2 = (com.android.systemui.statusbar.notification.row.ExpandableView) r2
            com.android.systemui.statusbar.notification.row.ExpandableView r7 = (com.android.systemui.statusbar.notification.row.ExpandableView) r7
            com.android.systemui.statusbar.notification.row.ExpandableView r1 = (com.android.systemui.statusbar.notification.row.ExpandableView) r1
            android.content.res.Resources r3 = r6.getResources()
            int r5 = com.android.systemui.R$bool.flag_notif_updates
            boolean r3 = r3.getBoolean(r5)
            r0.setViewsAffectedBySwipe(r2, r7, r1, r3)
            r6.updateFirstAndLastBackgroundViews()
            r6.requestDisallowInterceptTouchEvent(r4)
            r6.updateContinuousShadowDrawing()
            r6.updateContinuousBackgroundDrawing()
            r6.requestChildrenUpdate()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.onSwipeBegin(android.view.View):void");
    }

    /* access modifiers changed from: package-private */
    public void onSwipeEnd() {
        updateFirstAndLastBackgroundViews();
        this.mController.getNoticationRoundessManager().setViewsAffectedBySwipe(null, null, null, getResources().getBoolean(R$bool.flag_notif_updates));
        this.mShelf.updateAppearance();
    }

    /* access modifiers changed from: package-private */
    public void setTopHeadsUpEntry(NotificationEntry notificationEntry) {
        this.mTopHeadsUpEntry = notificationEntry;
    }

    /* access modifiers changed from: package-private */
    public void setNumHeadsUp(long j) {
        this.mNumHeadsUp = j;
        this.mAmbientState.setHasAlertEntries(j > 0);
    }

    public boolean getIsExpanded() {
        return this.mIsExpanded;
    }

    /* access modifiers changed from: package-private */
    public boolean getOnlyScrollingInThisMotion() {
        return this.mOnlyScrollingInThisMotion;
    }

    /* access modifiers changed from: package-private */
    public ExpandHelper getExpandHelper() {
        return this.mExpandHelper;
    }

    /* access modifiers changed from: package-private */
    public boolean isExpandingNotification() {
        return this.mExpandingNotification;
    }

    /* access modifiers changed from: package-private */
    public boolean getDisallowScrollingInThisMotion() {
        return this.mDisallowScrollingInThisMotion;
    }

    /* access modifiers changed from: package-private */
    public boolean isBeingDragged() {
        return this.mIsBeingDragged;
    }

    /* access modifiers changed from: package-private */
    public boolean getExpandedInThisMotion() {
        return this.mExpandedInThisMotion;
    }

    /* access modifiers changed from: package-private */
    public boolean getDisallowDismissInThisMotion() {
        return this.mDisallowDismissInThisMotion;
    }

    /* access modifiers changed from: package-private */
    public void setCheckForLeaveBehind(boolean z) {
        this.mCheckForLeavebehind = z;
    }

    /* access modifiers changed from: package-private */
    public void setTouchHandler(NotificationStackScrollLayoutController.TouchHandler touchHandler) {
        this.mTouchHandler = touchHandler;
    }

    /* access modifiers changed from: package-private */
    public boolean getCheckSnoozeLeaveBehind() {
        return this.mCheckForLeavebehind;
    }

    /* access modifiers changed from: package-private */
    public void setDismissListener(DismissListener dismissListener) {
        this.mDismissListener = dismissListener;
    }

    /* access modifiers changed from: package-private */
    public void setDismissAllAnimationListener(DismissAllAnimationListener dismissAllAnimationListener) {
        this.mDismissAllAnimationListener = dismissAllAnimationListener;
    }

    public void setHighPriorityBeforeSpeedBump(boolean z) {
        this.mHighPriorityBeforeSpeedBump = z;
    }

    /* access modifiers changed from: package-private */
    public void setFooterDismissListener(FooterDismissListener footerDismissListener) {
        this.mFooterDismissListener = footerDismissListener;
    }

    public void setRemoteInputManager(NotificationRemoteInputManager notificationRemoteInputManager) {
        this.mRemoteInputManager = notificationRemoteInputManager;
    }

    /* access modifiers changed from: package-private */
    public void setShadeController(ShadeController shadeController) {
        this.mShadeController = shadeController;
    }

    public void setExtraTopInsetForFullShadeTransition(float f) {
        this.mExtraTopInsetForFullShadeTransition = f;
        updateStackPosition();
        requestChildrenUpdate();
    }

    public void setOnScrollListener(Consumer<Integer> consumer) {
        this.mScrollListener = consumer;
    }

    public void setRoundedClippingBounds(int i, int i2, int i3, int i4, int i5, int i6) {
        if (this.mRoundedRectClippingLeft == i && this.mRoundedRectClippingRight == i3 && this.mRoundedRectClippingBottom == i4 && this.mRoundedRectClippingTop == i2) {
            float[] fArr = this.mBgCornerRadii;
            if (fArr[0] == ((float) i5) && fArr[5] == ((float) i6)) {
                return;
            }
        }
        this.mRoundedRectClippingLeft = i;
        this.mRoundedRectClippingTop = i2;
        this.mRoundedRectClippingBottom = i4;
        this.mRoundedRectClippingRight = i3;
        float[] fArr2 = this.mBgCornerRadii;
        float f = (float) i5;
        fArr2[0] = f;
        fArr2[1] = f;
        fArr2[2] = f;
        fArr2[3] = f;
        float f2 = (float) i6;
        fArr2[4] = f2;
        fArr2[5] = f2;
        fArr2[6] = f2;
        fArr2[7] = f2;
        this.mRoundedClipPath.reset();
        this.mRoundedClipPath.addRoundRect((float) i, (float) i2, (float) i3, (float) i4, this.mBgCornerRadii, Path.Direction.CW);
        if (this.mShouldUseRoundedRectClipping) {
            invalidate();
        }
    }

    private void updateSplitNotificationShade() {
        boolean shouldUseSplitNotificationShade = com.android.systemui.util.Utils.shouldUseSplitNotificationShade(this.mFeatureFlags, getResources());
        if (shouldUseSplitNotificationShade != this.mShouldUseSplitNotificationShade) {
            this.mShouldUseSplitNotificationShade = shouldUseSplitNotificationShade;
            updateDismissBehavior();
            updateUseRoundedRectClipping();
        }
    }

    private void updateDismissBehavior() {
        boolean z = true;
        if (this.mShouldUseSplitNotificationShade && this.mStatusBarState == 1) {
            z = false;
        }
        if (this.mDismissUsingRowTranslationX != z) {
            this.mDismissUsingRowTranslationX = z;
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                if (childAt instanceof ExpandableNotificationRow) {
                    ((ExpandableNotificationRow) childAt).setDismissUsingRowTranslationX(z);
                }
            }
        }
    }

    private void setLaunchingNotification(boolean z) {
        if (z != this.mLaunchingNotification) {
            this.mLaunchingNotification = z;
            ExpandAnimationParameters expandAnimationParameters = this.mLaunchAnimationParams;
            boolean z2 = expandAnimationParameters != null && (expandAnimationParameters.getStartRoundedTopClipping() > 0 || this.mLaunchAnimationParams.getParentStartRoundedTopClipping() > 0);
            this.mLaunchingNotificationNeedsToBeClipped = z2;
            if (!z2 || !this.mLaunchingNotification) {
                this.mLaunchedNotificationClipPath.reset();
            }
            invalidate();
        }
    }

    private void updateUseRoundedRectClipping() {
        boolean z = false;
        boolean z2 = this.mQsExpansionFraction < 0.5f || this.mShouldUseSplitNotificationShade;
        if (this.mIsExpanded && z2) {
            z = true;
        }
        if (z != this.mShouldUseRoundedRectClipping) {
            this.mShouldUseRoundedRectClipping = z;
            invalidate();
        }
    }

    private void updateLaunchedNotificationClipPath() {
        if (this.mLaunchingNotificationNeedsToBeClipped && this.mLaunchingNotification && this.mExpandingNotificationRow != null) {
            int min = Math.min(this.mLaunchAnimationParams.getLeft(), this.mRoundedRectClippingLeft);
            int max = Math.max(this.mLaunchAnimationParams.getRight(), this.mRoundedRectClippingRight);
            int max2 = Math.max(this.mLaunchAnimationParams.getBottom(), this.mRoundedRectClippingBottom);
            float interpolation = Interpolators.FAST_OUT_SLOW_IN.getInterpolation(this.mLaunchAnimationParams.getProgress(0, 100));
            float topCornerRadius = this.mLaunchAnimationParams.getTopCornerRadius();
            float bottomCornerRadius = this.mLaunchAnimationParams.getBottomCornerRadius();
            float[] fArr = this.mLaunchedNotificationRadii;
            fArr[0] = topCornerRadius;
            fArr[1] = topCornerRadius;
            fArr[2] = topCornerRadius;
            fArr[3] = topCornerRadius;
            fArr[4] = bottomCornerRadius;
            fArr[5] = bottomCornerRadius;
            fArr[6] = bottomCornerRadius;
            fArr[7] = bottomCornerRadius;
            this.mLaunchedNotificationClipPath.reset();
            this.mLaunchedNotificationClipPath.addRoundRect((float) min, (float) ((int) Math.min(MathUtils.lerp((float) this.mRoundedRectClippingTop, (float) this.mLaunchAnimationParams.getTop(), interpolation), (float) this.mRoundedRectClippingTop)), (float) max, (float) max2, this.mLaunchedNotificationRadii, Path.Direction.CW);
            ExpandableNotificationRow expandableNotificationRow = this.mExpandingNotificationRow;
            if (expandableNotificationRow.getNotificationParent() != null) {
                expandableNotificationRow = expandableNotificationRow.getNotificationParent();
            }
            this.mLaunchedNotificationClipPath.offset(((float) (-expandableNotificationRow.getLeft())) - expandableNotificationRow.getTranslationX(), ((float) (-expandableNotificationRow.getTop())) - expandableNotificationRow.getTranslationY());
            expandableNotificationRow.setExpandingClipPath(this.mLaunchedNotificationClipPath);
            if (this.mShouldUseRoundedRectClipping) {
                invalidate();
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.mShouldUseRoundedRectClipping && !this.mLaunchingNotification) {
            canvas.clipPath(this.mRoundedClipPath);
        }
        super.dispatchDraw(canvas);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        if (!this.mShouldUseRoundedRectClipping || !this.mLaunchingNotification) {
            return super.drawChild(canvas, view, j);
        }
        canvas.save();
        ExpandableView expandableView = (ExpandableView) view;
        Path path = (expandableView.isExpandAnimationRunning() || expandableView.hasExpandingChild()) ? null : this.mRoundedClipPath;
        if (path != null) {
            canvas.clipPath(path);
        }
        boolean drawChild = super.drawChild(canvas, view, j);
        canvas.restore();
        return drawChild;
    }

    public float getTotalTranslationLength(View view) {
        if (!this.mDismissUsingRowTranslationX) {
            return (float) view.getMeasuredWidth();
        }
        float measuredWidth = (float) getMeasuredWidth();
        return measuredWidth - ((measuredWidth - ((float) view.getMeasuredWidth())) / 2.0f);
    }

    public int getTopClippingStartLocation() {
        if (this.mIsExpanded) {
            return this.mQsScrollBoundaryPosition;
        }
        return 0;
    }

    public void animateNextTopPaddingChange() {
        this.mAnimateNextTopPaddingChange = true;
    }

    private void updateSpeedBumpIndex() {
        this.mSpeedBumpIndexDirty = true;
    }

    public void updateSectionBoundaries(String str) {
        this.mSectionsManager.updateSectionBoundaries(str);
    }

    void updateContinuousBackgroundDrawing() {
        boolean z = !this.mAmbientState.isFullyAwake() && this.mSwipeHelper.isSwiping();
        if (z != this.mContinuousBackgroundUpdate) {
            this.mContinuousBackgroundUpdate = z;
            if (z) {
                getViewTreeObserver().addOnPreDrawListener(this.mBackgroundUpdater);
            } else {
                getViewTreeObserver().removeOnPreDrawListener(this.mBackgroundUpdater);
            }
        }
    }

    void updateContinuousShadowDrawing() {
        boolean z = this.mAnimationRunning || this.mSwipeHelper.isSwiping();
        if (z != this.mContinuousShadowUpdate) {
            if (z) {
                getViewTreeObserver().addOnPreDrawListener(this.mShadowUpdater);
            } else {
                getViewTreeObserver().removeOnPreDrawListener(this.mShadowUpdater);
            }
            this.mContinuousShadowUpdate = z;
        }
    }

    private void resetExposedMenuView(boolean z, boolean z2) {
        this.mSwipeHelper.resetExposedMenuView(z, z2);
    }

    /* access modifiers changed from: package-private */
    public static boolean matchesSelection(ExpandableNotificationRow expandableNotificationRow, int i) {
        if (i == 0) {
            return true;
        }
        if (i == 1) {
            return expandableNotificationRow.getEntry().getBucket() < 6;
        }
        if (i == 2) {
            return expandableNotificationRow.getEntry().getBucket() == 6;
        }
        throw new IllegalArgumentException("Unknown selection: " + i);
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class AnimationEvent {
        static AnimationFilter[] FILTERS = {new AnimationFilter().animateAlpha().animateHeight().animateTopInset().animateY().animateZ().hasDelays(), new AnimationFilter().animateAlpha().animateHeight().animateTopInset().animateY().animateZ().hasDelays(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateZ().hasDelays(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateDimmed().animateZ(), new AnimationFilter().animateZ(), new AnimationFilter().animateDimmed(), new AnimationFilter().animateAlpha().animateHeight().animateTopInset().animateY().animateZ(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateDimmed().animateZ().hasDelays(), new AnimationFilter().animateHideSensitive(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateZ(), new AnimationFilter().animateAlpha().animateHeight().animateTopInset().animateY().animateZ(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateZ(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateZ().hasDelays(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateZ().hasDelays(), new AnimationFilter().animateHeight().animateTopInset().animateY().animateZ(), new AnimationFilter().animateAlpha().animateDimmed().animateHideSensitive().animateHeight().animateTopInset().animateY().animateZ()};
        static int[] LENGTHS = {464, 464, 360, 360, 220, 220, 360, 448, 360, 360, 360, 400, 400, 400, 360, 360};
        final int animationType;
        final long eventStartTime;
        final AnimationFilter filter;
        boolean headsUpFromBottom;
        final long length;
        final ExpandableView mChangingView;
        View viewAfterChangingView;

        AnimationEvent(ExpandableView expandableView, int i) {
            this(expandableView, i, (long) LENGTHS[i]);
        }

        AnimationEvent(ExpandableView expandableView, int i, long j) {
            this(expandableView, i, j, FILTERS[i]);
        }

        AnimationEvent(ExpandableView expandableView, int i, long j, AnimationFilter animationFilter) {
            this.eventStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mChangingView = expandableView;
            this.animationType = i;
            this.length = j;
            this.filter = animationFilter;
        }

        /* access modifiers changed from: package-private */
        public static long combineLength(ArrayList<AnimationEvent> arrayList) {
            int size = arrayList.size();
            long j = 0;
            for (int i = 0; i < size; i++) {
                AnimationEvent animationEvent = arrayList.get(i);
                j = Math.max(j, animationEvent.length);
                if (animationEvent.animationType == 7) {
                    return animationEvent.length;
                }
            }
            return j;
        }
    }

    /* access modifiers changed from: package-private */
    public static boolean canChildBeDismissed(View view) {
        if (view instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            if (expandableNotificationRow.isBlockingHelperShowingAndTranslationFinished()) {
                return true;
            }
            if (expandableNotificationRow.areGutsExposed() || !expandableNotificationRow.getEntry().hasFinishedInitialization()) {
                return false;
            }
            return expandableNotificationRow.canViewBeDismissed();
        } else if (view instanceof PeopleHubView) {
            return ((PeopleHubView) view).getCanSwipe();
        } else {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public void onEntryUpdated(NotificationEntry notificationEntry) {
        if (notificationEntry.rowExists() && !notificationEntry.getSbn().isClearable()) {
            snapViewIfNeeded(notificationEntry);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onDismissAllAnimationsEnd */
    public void lambda$clearNotifications$5(List<ExpandableNotificationRow> list, int i) {
        DismissAllAnimationListener dismissAllAnimationListener = this.mDismissAllAnimationListener;
        if (dismissAllAnimationListener != null) {
            dismissAllAnimationListener.onAnimationEnd(list, i);
        }
    }

    /* access modifiers changed from: package-private */
    public void resetCheckSnoozeLeavebehind() {
        setCheckForLeaveBehind(true);
    }

    public HeadsUpTouchHelper.Callback getHeadsUpCallback() {
        return this.mHeadsUpCallback;
    }

    /* access modifiers changed from: package-private */
    public void onGroupExpandChanged(final ExpandableNotificationRow expandableNotificationRow, boolean z) {
        boolean z2 = this.mAnimationsEnabled && (this.mIsExpanded || expandableNotificationRow.isPinned());
        if (z2) {
            this.mExpandedGroupView = expandableNotificationRow;
            this.mNeedsAnimation = true;
        }
        expandableNotificationRow.setChildrenExpanded(z, z2);
        onChildHeightChanged(expandableNotificationRow, false);
        runAfterAnimationFinished(new Runnable() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.10
            @Override // java.lang.Runnable
            public void run() {
                expandableNotificationRow.onFinishedExpansionChange();
            }
        });
    }

    public ExpandHelper.Callback getExpandHelperCallback() {
        return this.mExpandHelperCallback;
    }
}
