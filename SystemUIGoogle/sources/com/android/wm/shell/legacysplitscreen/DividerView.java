package com.android.wm.shell.legacysplitscreen;

import android.animation.AnimationHandler;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.SurfaceControl;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.policy.DividerSnapAlgorithm;
import com.android.internal.policy.DockedDividerUtils;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.FlingAnimationUtils;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.common.split.DividerHandleView;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class DividerView extends FrameLayout implements View.OnTouchListener, ViewTreeObserver.OnComputeInternalInsetsListener {
    private boolean mAdjustedForIme;
    private View mBackground;
    private boolean mBackgroundLifted;
    private DividerCallbacks mCallback;
    private ValueAnimator mCurrentAnimator;
    private final Display mDefaultDisplay;
    private int mDividerInsets;
    int mDividerPositionX;
    int mDividerPositionY;
    private int mDividerSize;
    private int mDockSide;
    private final Rect mDockedInsetRect;
    private final Rect mDockedRect;
    private boolean mDockedStackMinimized;
    private final Rect mDockedTaskRect;
    private boolean mEntranceAnimationRunning;
    private boolean mExitAnimationRunning;
    private int mExitStartPosition;
    boolean mFirstLayout;
    private FlingAnimationUtils mFlingAnimationUtils;
    private DividerHandleView mHandle;
    private final View.AccessibilityDelegate mHandleDelegate;
    private boolean mHomeStackResizable;
    private DividerImeController mImeController;
    private boolean mIsInMinimizeInteraction;
    private final Rect mLastResizeRect;
    private int mLongPressEntraceAnimDuration;
    private MinimizedDockShadow mMinimizedShadow;
    private boolean mMoving;
    private final Rect mOtherInsetRect;
    private final Rect mOtherRect;
    private final Rect mOtherTaskRect;
    private boolean mRemoved;
    private final Runnable mResetBackgroundRunnable;
    private AnimationHandler mSfVsyncAnimationHandler;
    DividerSnapAlgorithm.SnapTarget mSnapTargetBeforeMinimized;
    private LegacySplitDisplayLayout mSplitLayout;
    private LegacySplitScreenController mSplitScreenController;
    private int mStartPosition;
    private int mStartX;
    private int mStartY;
    private DividerState mState;
    private boolean mSurfaceHidden;
    private LegacySplitScreenTaskListener mTiles;
    private final Matrix mTmpMatrix;
    private final Rect mTmpRect;
    private final float[] mTmpValues;
    private int mTouchElevation;
    private int mTouchSlop;
    private Runnable mUpdateEmbeddedMatrix;
    private VelocityTracker mVelocityTracker;
    private DividerWindowManager mWindowManager;
    private WindowManagerProxy mWindowManagerProxy;
    private static final PathInterpolator SLOWDOWN_INTERPOLATOR = new PathInterpolator(0.5f, 1.0f, 0.5f, 1.0f);
    private static final PathInterpolator DIM_INTERPOLATOR = new PathInterpolator(0.23f, 0.87f, 0.52f, -0.11f);
    private static final Interpolator IME_ADJUST_INTERPOLATOR = new PathInterpolator(0.2f, 0.0f, 0.1f, 1.0f);

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface DividerCallbacks {
        void onDraggingEnd();

        void onDraggingStart();
    }

    private static boolean dockSideBottomRight(int i) {
        return i == 4 || i == 3;
    }

    private static boolean dockSideTopLeft(int i) {
        return i == 2 || i == 1;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (getViewRootImpl() != null) {
            if (isHorizontalDivision()) {
                this.mTmpMatrix.setTranslate(0.0f, (float) (this.mDividerPositionY - this.mDividerInsets));
            } else {
                this.mTmpMatrix.setTranslate((float) (this.mDividerPositionX - this.mDividerInsets), 0.0f);
            }
            this.mTmpMatrix.getValues(this.mTmpValues);
            try {
                getViewRootImpl().getAccessibilityEmbeddedConnection().setScreenMatrix(this.mTmpValues);
            } catch (RemoteException unused) {
            }
        }
    }

    public DividerView(Context context) {
        this(context, null);
    }

    public DividerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DividerView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DividerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDockedRect = new Rect();
        this.mDockedTaskRect = new Rect();
        this.mOtherTaskRect = new Rect();
        this.mOtherRect = new Rect();
        this.mDockedInsetRect = new Rect();
        this.mOtherInsetRect = new Rect();
        this.mLastResizeRect = new Rect();
        this.mTmpRect = new Rect();
        this.mFirstLayout = true;
        this.mTmpMatrix = new Matrix();
        this.mTmpValues = new float[9];
        this.mSurfaceHidden = false;
        this.mHandleDelegate = new View.AccessibilityDelegate() { // from class: com.android.wm.shell.legacysplitscreen.DividerView.1
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                DividerSnapAlgorithm snapAlgorithm = DividerView.this.getSnapAlgorithm();
                if (DividerView.this.isHorizontalDivision()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_full, ((FrameLayout) DividerView.this).mContext.getString(R.string.accessibility_action_divider_top_full)));
                    if (snapAlgorithm.isFirstSplitTargetAvailable()) {
                        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_70, ((FrameLayout) DividerView.this).mContext.getString(R.string.accessibility_action_divider_top_70)));
                    }
                    if (snapAlgorithm.showMiddleSplitTargetForAccessibility()) {
                        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_50, ((FrameLayout) DividerView.this).mContext.getString(R.string.accessibility_action_divider_top_50)));
                    }
                    if (snapAlgorithm.isLastSplitTargetAvailable()) {
                        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_30, ((FrameLayout) DividerView.this).mContext.getString(R.string.accessibility_action_divider_top_30)));
                    }
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_rb_full, ((FrameLayout) DividerView.this).mContext.getString(R.string.accessibility_action_divider_bottom_full)));
                    return;
                }
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_full, ((FrameLayout) DividerView.this).mContext.getString(R.string.accessibility_action_divider_left_full)));
                if (snapAlgorithm.isFirstSplitTargetAvailable()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_70, ((FrameLayout) DividerView.this).mContext.getString(R.string.accessibility_action_divider_left_70)));
                }
                if (snapAlgorithm.showMiddleSplitTargetForAccessibility()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_50, ((FrameLayout) DividerView.this).mContext.getString(R.string.accessibility_action_divider_left_50)));
                }
                if (snapAlgorithm.isLastSplitTargetAvailable()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_tl_30, ((FrameLayout) DividerView.this).mContext.getString(R.string.accessibility_action_divider_left_30)));
                }
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_move_rb_full, ((FrameLayout) DividerView.this).mContext.getString(R.string.accessibility_action_divider_right_full)));
            }

            @Override // android.view.View.AccessibilityDelegate
            public boolean performAccessibilityAction(View view, int i3, Bundle bundle) {
                DividerSnapAlgorithm.SnapTarget dismissStartTarget;
                int currentPosition = DividerView.this.getCurrentPosition();
                DividerSnapAlgorithm snapAlgorithm = DividerView.this.mSplitLayout.getSnapAlgorithm();
                if (i3 == R.id.action_move_tl_full) {
                    dismissStartTarget = snapAlgorithm.getDismissEndTarget();
                } else if (i3 == R.id.action_move_tl_70) {
                    dismissStartTarget = snapAlgorithm.getLastSplitTarget();
                } else if (i3 == R.id.action_move_tl_50) {
                    dismissStartTarget = snapAlgorithm.getMiddleTarget();
                } else if (i3 == R.id.action_move_tl_30) {
                    dismissStartTarget = snapAlgorithm.getFirstSplitTarget();
                } else {
                    dismissStartTarget = i3 == R.id.action_move_rb_full ? snapAlgorithm.getDismissStartTarget() : null;
                }
                if (dismissStartTarget == null) {
                    return super.performAccessibilityAction(view, i3, bundle);
                }
                DividerView.this.startDragging(true, false);
                DividerView.this.stopDragging(currentPosition, dismissStartTarget, 250, Interpolators.FAST_OUT_SLOW_IN);
                return true;
            }
        };
        this.mResetBackgroundRunnable = new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.DividerView.2
            @Override // java.lang.Runnable
            public void run() {
                DividerView.this.resetBackground();
            }
        };
        this.mUpdateEmbeddedMatrix = new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.DividerView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DividerView.this.lambda$new$0();
            }
        };
        this.mDefaultDisplay = ((DisplayManager) ((FrameLayout) this).mContext.getSystemService("display")).getDisplay(0);
    }

    public void setAnimationHandler(AnimationHandler animationHandler) {
        this.mSfVsyncAnimationHandler = animationHandler;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mHandle = (DividerHandleView) findViewById(R.id.docked_divider_handle);
        this.mBackground = findViewById(R.id.docked_divider_background);
        this.mMinimizedShadow = (MinimizedDockShadow) findViewById(R.id.minimized_dock_shadow);
        this.mHandle.setOnTouchListener(this);
        int dimensionPixelSize = getResources().getDimensionPixelSize(17105198);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(17105197);
        this.mDividerInsets = dimensionPixelSize2;
        this.mDividerSize = dimensionPixelSize - (dimensionPixelSize2 * 2);
        this.mTouchElevation = getResources().getDimensionPixelSize(R.dimen.docked_stack_divider_lift_elevation);
        this.mLongPressEntraceAnimDuration = getResources().getInteger(R.integer.long_press_dock_anim_duration);
        this.mTouchSlop = ViewConfiguration.get(((FrameLayout) this).mContext).getScaledTouchSlop();
        this.mFlingAnimationUtils = new FlingAnimationUtils(getResources().getDisplayMetrics(), 0.3f);
        this.mHandle.setPointerIcon(PointerIcon.getSystemIcon(getContext(), getResources().getConfiguration().orientation == 2 ? 1014 : 1015));
        getViewTreeObserver().addOnComputeInternalInsetsListener(this);
        this.mHandle.setAccessibilityDelegate(this.mHandleDelegate);
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mDockSide != -1 && !this.mIsInMinimizeInteraction) {
            saveSnapTargetBeforeMinimized(this.mSnapTargetBeforeMinimized);
        }
        this.mFirstLayout = true;
    }

    /* access modifiers changed from: package-private */
    public void onDividerRemoved() {
        this.mRemoved = true;
        this.mCallback = null;
    }

    @Override // android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int right;
        super.onLayout(z, i, i2, i3, i4);
        int i6 = 0;
        if (this.mFirstLayout) {
            initializeSurfaceState();
            this.mFirstLayout = false;
        }
        int i7 = this.mDockSide;
        if (i7 == 2) {
            i5 = this.mBackground.getTop();
        } else {
            if (i7 == 1) {
                right = this.mBackground.getLeft();
            } else if (i7 == 3) {
                right = this.mBackground.getRight() - this.mMinimizedShadow.getWidth();
            } else {
                i5 = 0;
            }
            i6 = right;
            i5 = 0;
        }
        MinimizedDockShadow minimizedDockShadow = this.mMinimizedShadow;
        minimizedDockShadow.layout(i6, i5, minimizedDockShadow.getMeasuredWidth() + i6, this.mMinimizedShadow.getMeasuredHeight() + i5);
        if (z) {
            notifySplitScreenBoundsChanged();
        }
    }

    /* access modifiers changed from: package-private */
    public void injectDependencies(LegacySplitScreenController legacySplitScreenController, DividerWindowManager dividerWindowManager, DividerState dividerState, DividerCallbacks dividerCallbacks, LegacySplitScreenTaskListener legacySplitScreenTaskListener, LegacySplitDisplayLayout legacySplitDisplayLayout, DividerImeController dividerImeController, WindowManagerProxy windowManagerProxy) {
        this.mSplitScreenController = legacySplitScreenController;
        this.mWindowManager = dividerWindowManager;
        this.mState = dividerState;
        this.mCallback = dividerCallbacks;
        this.mTiles = legacySplitScreenTaskListener;
        this.mSplitLayout = legacySplitDisplayLayout;
        this.mImeController = dividerImeController;
        this.mWindowManagerProxy = windowManagerProxy;
        if (dividerState.mRatioPositionBeforeMinimized == 0.0f) {
            this.mSnapTargetBeforeMinimized = legacySplitDisplayLayout.getSnapAlgorithm().getMiddleTarget();
        } else {
            repositionSnapTargetBeforeMinimized();
        }
    }

    public Rect getNonMinimizedSplitScreenSecondaryBounds() {
        this.mOtherTaskRect.set(this.mSplitLayout.mSecondary);
        return this.mOtherTaskRect;
    }

    private boolean inSplitMode() {
        return getVisibility() == 0;
    }

    /* access modifiers changed from: package-private */
    public void setHidden(boolean z) {
        if (this.mSurfaceHidden != z) {
            this.mSurfaceHidden = z;
            post(new Runnable(z) { // from class: com.android.wm.shell.legacysplitscreen.DividerView$$ExternalSyntheticLambda2
                public final /* synthetic */ boolean f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    DividerView.this.lambda$setHidden$1(this.f$1);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setHidden$1(boolean z) {
        SurfaceControl windowSurfaceControl = getWindowSurfaceControl();
        if (windowSurfaceControl != null) {
            SurfaceControl.Transaction transaction = this.mTiles.getTransaction();
            if (z) {
                transaction.hide(windowSurfaceControl);
            } else {
                transaction.show(windowSurfaceControl);
            }
            this.mImeController.setDimsHidden(transaction, z);
            transaction.apply();
            this.mTiles.releaseTransaction(transaction);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isHidden() {
        return getVisibility() != 0 || this.mSurfaceHidden;
    }

    public boolean startDragging(boolean z, boolean z2) {
        cancelFlingAnimation();
        if (z2) {
            this.mHandle.setTouching(true, z);
        }
        this.mDockSide = this.mSplitLayout.getPrimarySplitSide();
        this.mWindowManagerProxy.setResizing(true);
        if (z2) {
            this.mWindowManager.setSlippery(false);
            liftBackground();
        }
        DividerCallbacks dividerCallbacks = this.mCallback;
        if (dividerCallbacks != null) {
            dividerCallbacks.onDraggingStart();
        }
        return inSplitMode();
    }

    public void stopDragging(int i, float f, boolean z, boolean z2) {
        this.mHandle.setTouching(false, true);
        fling(i, f, z, z2);
        this.mWindowManager.setSlippery(true);
        releaseBackground();
    }

    /* access modifiers changed from: private */
    public void stopDragging(int i, DividerSnapAlgorithm.SnapTarget snapTarget, long j, Interpolator interpolator) {
        stopDragging(i, snapTarget, j, 0, 0, interpolator);
    }

    private void stopDragging(int i, DividerSnapAlgorithm.SnapTarget snapTarget, long j, Interpolator interpolator, long j2) {
        stopDragging(i, snapTarget, j, 0, j2, interpolator);
    }

    private void stopDragging(int i, DividerSnapAlgorithm.SnapTarget snapTarget, long j, long j2, long j3, Interpolator interpolator) {
        this.mHandle.setTouching(false, true);
        flingTo(i, snapTarget, j, j2, j3, interpolator);
        this.mWindowManager.setSlippery(true);
        releaseBackground();
    }

    private void stopDragging() {
        this.mHandle.setTouching(false, true);
        this.mWindowManager.setSlippery(true);
        releaseBackground();
    }

    private void updateDockSide() {
        int primarySplitSide = this.mSplitLayout.getPrimarySplitSide();
        this.mDockSide = primarySplitSide;
        this.mMinimizedShadow.setDockSide(primarySplitSide);
    }

    public DividerSnapAlgorithm getSnapAlgorithm() {
        if (this.mDockedStackMinimized) {
            return this.mSplitLayout.getMinimizedSnapAlgorithm(this.mHomeStackResizable);
        }
        return this.mSplitLayout.getSnapAlgorithm();
    }

    public int getCurrentPosition() {
        return isHorizontalDivision() ? this.mDividerPositionY : this.mDividerPositionX;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0013, code lost:
        if (r6 != 3) goto L_0x00b2;
     */
    @Override // android.view.View.OnTouchListener
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouch(android.view.View r6, android.view.MotionEvent r7) {
        /*
            r5 = this;
            r5.convertToScreenCoordinates(r7)
            int r6 = r7.getAction()
            r6 = r6 & 255(0xff, float:3.57E-43)
            r0 = 0
            r1 = 1
            if (r6 == 0) goto L_0x00b3
            if (r6 == r1) goto L_0x0078
            r2 = 2
            if (r6 == r2) goto L_0x0017
            r2 = 3
            if (r6 == r2) goto L_0x0078
            goto L_0x00b2
        L_0x0017:
            android.view.VelocityTracker r6 = r5.mVelocityTracker
            r6.addMovement(r7)
            float r6 = r7.getX()
            int r6 = (int) r6
            float r7 = r7.getY()
            int r7 = (int) r7
            boolean r2 = r5.isHorizontalDivision()
            if (r2 == 0) goto L_0x0038
            int r2 = r5.mStartY
            int r2 = r7 - r2
            int r2 = java.lang.Math.abs(r2)
            int r3 = r5.mTouchSlop
            if (r2 > r3) goto L_0x004a
        L_0x0038:
            boolean r2 = r5.isHorizontalDivision()
            if (r2 != 0) goto L_0x004c
            int r2 = r5.mStartX
            int r2 = r6 - r2
            int r2 = java.lang.Math.abs(r2)
            int r3 = r5.mTouchSlop
            if (r2 <= r3) goto L_0x004c
        L_0x004a:
            r2 = r1
            goto L_0x004d
        L_0x004c:
            r2 = r0
        L_0x004d:
            boolean r3 = r5.mMoving
            if (r3 != 0) goto L_0x0059
            if (r2 == 0) goto L_0x0059
            r5.mStartX = r6
            r5.mStartY = r7
            r5.mMoving = r1
        L_0x0059:
            boolean r2 = r5.mMoving
            if (r2 == 0) goto L_0x00b2
            int r2 = r5.mDockSide
            r3 = -1
            if (r2 == r3) goto L_0x00b2
            com.android.internal.policy.DividerSnapAlgorithm r2 = r5.getSnapAlgorithm()
            int r3 = r5.mStartPosition
            r4 = 0
            com.android.internal.policy.DividerSnapAlgorithm$SnapTarget r0 = r2.calculateSnapTarget(r3, r4, r0)
            int r6 = r5.calculatePosition(r6, r7)
            int r7 = r5.mStartPosition
            r2 = 0
            r5.resizeStackSurfaces(r6, r7, r0, r2)
            goto L_0x00b2
        L_0x0078:
            boolean r6 = r5.mMoving
            if (r6 != 0) goto L_0x0080
            r5.stopDragging()
            goto L_0x00b2
        L_0x0080:
            float r6 = r7.getRawX()
            int r6 = (int) r6
            float r2 = r7.getRawY()
            int r2 = (int) r2
            android.view.VelocityTracker r3 = r5.mVelocityTracker
            r3.addMovement(r7)
            android.view.VelocityTracker r7 = r5.mVelocityTracker
            r3 = 1000(0x3e8, float:1.401E-42)
            r7.computeCurrentVelocity(r3)
            int r6 = r5.calculatePosition(r6, r2)
            boolean r7 = r5.isHorizontalDivision()
            if (r7 == 0) goto L_0x00a7
            android.view.VelocityTracker r7 = r5.mVelocityTracker
            float r7 = r7.getYVelocity()
            goto L_0x00ad
        L_0x00a7:
            android.view.VelocityTracker r7 = r5.mVelocityTracker
            float r7 = r7.getXVelocity()
        L_0x00ad:
            r5.stopDragging(r6, r7, r0, r1)
            r5.mMoving = r0
        L_0x00b2:
            return r1
        L_0x00b3:
            android.view.VelocityTracker r6 = android.view.VelocityTracker.obtain()
            r5.mVelocityTracker = r6
            r6.addMovement(r7)
            float r6 = r7.getX()
            int r6 = (int) r6
            r5.mStartX = r6
            float r6 = r7.getY()
            int r6 = (int) r6
            r5.mStartY = r6
            boolean r6 = r5.startDragging(r1, r1)
            if (r6 != 0) goto L_0x00d3
            r5.stopDragging()
        L_0x00d3:
            int r7 = r5.getCurrentPosition()
            r5.mStartPosition = r7
            r5.mMoving = r0
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.legacysplitscreen.DividerView.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    private void logResizeEvent(DividerSnapAlgorithm.SnapTarget snapTarget) {
        if (snapTarget == this.mSplitLayout.getSnapAlgorithm().getDismissStartTarget()) {
            MetricsLogger.action(((FrameLayout) this).mContext, 390, dockSideTopLeft(this.mDockSide) ? 1 : 0);
        } else if (snapTarget == this.mSplitLayout.getSnapAlgorithm().getDismissEndTarget()) {
            MetricsLogger.action(((FrameLayout) this).mContext, 390, dockSideBottomRight(this.mDockSide) ? 1 : 0);
        } else if (snapTarget == this.mSplitLayout.getSnapAlgorithm().getMiddleTarget()) {
            MetricsLogger.action(((FrameLayout) this).mContext, 389, 0);
        } else {
            int i = 1;
            if (snapTarget == this.mSplitLayout.getSnapAlgorithm().getFirstSplitTarget()) {
                Context context = ((FrameLayout) this).mContext;
                if (!dockSideTopLeft(this.mDockSide)) {
                    i = 2;
                }
                MetricsLogger.action(context, 389, i);
            } else if (snapTarget == this.mSplitLayout.getSnapAlgorithm().getLastSplitTarget()) {
                Context context2 = ((FrameLayout) this).mContext;
                if (dockSideTopLeft(this.mDockSide)) {
                    i = 2;
                }
                MetricsLogger.action(context2, 389, i);
            }
        }
    }

    private void convertToScreenCoordinates(MotionEvent motionEvent) {
        motionEvent.setLocation(motionEvent.getRawX(), motionEvent.getRawY());
    }

    private void fling(int i, float f, boolean z, boolean z2) {
        DividerSnapAlgorithm snapAlgorithm = getSnapAlgorithm();
        DividerSnapAlgorithm.SnapTarget calculateSnapTarget = snapAlgorithm.calculateSnapTarget(i, f);
        if (z && calculateSnapTarget == snapAlgorithm.getDismissStartTarget()) {
            calculateSnapTarget = snapAlgorithm.getFirstSplitTarget();
        }
        if (z2) {
            logResizeEvent(calculateSnapTarget);
        }
        ValueAnimator flingAnimator = getFlingAnimator(i, calculateSnapTarget, 0);
        this.mFlingAnimationUtils.apply(flingAnimator, (float) i, (float) calculateSnapTarget.position, f);
        flingAnimator.start();
    }

    private void flingTo(int i, DividerSnapAlgorithm.SnapTarget snapTarget, long j, long j2, long j3, Interpolator interpolator) {
        ValueAnimator flingAnimator = getFlingAnimator(i, snapTarget, j3);
        flingAnimator.setDuration(j);
        flingAnimator.setStartDelay(j2);
        flingAnimator.setInterpolator(interpolator);
        flingAnimator.start();
    }

    private ValueAnimator getFlingAnimator(int i, DividerSnapAlgorithm.SnapTarget snapTarget, final long j) {
        if (this.mCurrentAnimator != null) {
            cancelFlingAnimation();
            updateDockSide();
        }
        boolean z = snapTarget.flag == 0;
        ValueAnimator ofInt = ValueAnimator.ofInt(i, snapTarget.position);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(z, snapTarget) { // from class: com.android.wm.shell.legacysplitscreen.DividerView$$ExternalSyntheticLambda0
            public final /* synthetic */ boolean f$1;
            public final /* synthetic */ DividerSnapAlgorithm.SnapTarget f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DividerView.this.lambda$getFlingAnimator$2(this.f$1, this.f$2, valueAnimator);
            }
        });
        final DividerView$$ExternalSyntheticLambda3 dividerView$$ExternalSyntheticLambda3 = new Consumer(snapTarget) { // from class: com.android.wm.shell.legacysplitscreen.DividerView$$ExternalSyntheticLambda3
            public final /* synthetic */ DividerSnapAlgorithm.SnapTarget f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DividerView.this.lambda$getFlingAnimator$3(this.f$1, (Boolean) obj);
            }
        };
        ofInt.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.legacysplitscreen.DividerView.3
            private boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                long j2 = j;
                if (j2 == 0) {
                    j2 = 0;
                }
                if (j2 == 0) {
                    dividerView$$ExternalSyntheticLambda3.accept(Boolean.valueOf(this.mCancelled));
                    return;
                }
                DividerView.this.getHandler().postDelayed(new DividerView$3$$ExternalSyntheticLambda0(dividerView$$ExternalSyntheticLambda3, Boolean.valueOf(this.mCancelled)), j2);
            }
        });
        this.mCurrentAnimator = ofInt;
        ofInt.setAnimationHandler(this.mSfVsyncAnimationHandler);
        return ofInt;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getFlingAnimator$2(boolean z, DividerSnapAlgorithm.SnapTarget snapTarget, ValueAnimator valueAnimator) {
        int i;
        int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        if (!z || valueAnimator.getAnimatedFraction() != 1.0f) {
            i = snapTarget.taskPosition;
        } else {
            i = Integer.MAX_VALUE;
        }
        resizeStackSurfaces(intValue, i, snapTarget, null);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getFlingAnimator$3(DividerSnapAlgorithm.SnapTarget snapTarget, Boolean bool) {
        boolean z = this.mIsInMinimizeInteraction;
        if (!bool.booleanValue() && !this.mDockedStackMinimized && this.mIsInMinimizeInteraction) {
            this.mIsInMinimizeInteraction = false;
        }
        boolean commitSnapFlags = commitSnapFlags(snapTarget);
        this.mWindowManagerProxy.setResizing(false);
        updateDockSide();
        this.mCurrentAnimator = null;
        this.mEntranceAnimationRunning = false;
        this.mExitAnimationRunning = false;
        if (!commitSnapFlags && !z) {
            this.mWindowManagerProxy.applyResizeSplits(snapTarget.position, this.mSplitLayout);
        }
        DividerCallbacks dividerCallbacks = this.mCallback;
        if (dividerCallbacks != null) {
            dividerCallbacks.onDraggingEnd();
        }
        if (!this.mIsInMinimizeInteraction) {
            if (snapTarget.position < 0) {
                snapTarget = this.mSplitLayout.getSnapAlgorithm().getMiddleTarget();
            }
            DividerSnapAlgorithm snapAlgorithm = this.mSplitLayout.getSnapAlgorithm();
            if (!(snapTarget.position == snapAlgorithm.getDismissEndTarget().position || snapTarget.position == snapAlgorithm.getDismissStartTarget().position)) {
                saveSnapTargetBeforeMinimized(snapTarget);
            }
        }
        notifySplitScreenBoundsChanged();
    }

    private void notifySplitScreenBoundsChanged() {
        Rect rect;
        LegacySplitDisplayLayout legacySplitDisplayLayout = this.mSplitLayout;
        if (legacySplitDisplayLayout.mPrimary != null && (rect = legacySplitDisplayLayout.mSecondary) != null) {
            this.mOtherTaskRect.set(rect);
            this.mTmpRect.set(this.mHandle.getLeft(), this.mHandle.getTop(), this.mHandle.getRight(), this.mHandle.getBottom());
            if (isHorizontalDivision()) {
                this.mTmpRect.offsetTo(this.mHandle.getLeft(), this.mDividerPositionY);
            } else {
                this.mTmpRect.offsetTo(this.mDividerPositionX, this.mHandle.getTop());
            }
            this.mWindowManagerProxy.setTouchRegion(this.mTmpRect);
            this.mTmpRect.set(this.mSplitLayout.mDisplayLayout.stableInsets());
            int primarySplitSide = this.mSplitLayout.getPrimarySplitSide();
            if (primarySplitSide == 1) {
                this.mTmpRect.left = 0;
            } else if (primarySplitSide == 2) {
                this.mTmpRect.top = 0;
            } else if (primarySplitSide == 3) {
                this.mTmpRect.right = 0;
            }
            this.mSplitScreenController.notifyBoundsChanged(this.mOtherTaskRect, this.mTmpRect);
        }
    }

    private void cancelFlingAnimation() {
        ValueAnimator valueAnimator = this.mCurrentAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    private boolean commitSnapFlags(DividerSnapAlgorithm.SnapTarget snapTarget) {
        int i;
        int i2;
        int i3 = snapTarget.flag;
        if (i3 == 0) {
            return false;
        }
        this.mWindowManagerProxy.dismissOrMaximizeDocked(this.mTiles, this.mSplitLayout, i3 != 1 ? (i = this.mDockSide) == 3 || i == 4 : (i2 = this.mDockSide) == 1 || i2 == 2);
        SurfaceControl.Transaction transaction = this.mTiles.getTransaction();
        setResizeDimLayer(transaction, true, 0.0f);
        setResizeDimLayer(transaction, false, 0.0f);
        transaction.apply();
        this.mTiles.releaseTransaction(transaction);
        return true;
    }

    private void liftBackground() {
        if (!this.mBackgroundLifted) {
            if (isHorizontalDivision()) {
                this.mBackground.animate().scaleY(1.4f);
            } else {
                this.mBackground.animate().scaleX(1.4f);
            }
            ViewPropertyAnimator animate = this.mBackground.animate();
            Interpolator interpolator = Interpolators.TOUCH_RESPONSE;
            animate.setInterpolator(interpolator).setDuration(150).translationZ((float) this.mTouchElevation).start();
            this.mHandle.animate().setInterpolator(interpolator).setDuration(150).translationZ((float) this.mTouchElevation).start();
            this.mBackgroundLifted = true;
        }
    }

    private void releaseBackground() {
        if (this.mBackgroundLifted) {
            ViewPropertyAnimator animate = this.mBackground.animate();
            Interpolator interpolator = Interpolators.FAST_OUT_SLOW_IN;
            animate.setInterpolator(interpolator).setDuration(200).translationZ(0.0f).scaleX(1.0f).scaleY(1.0f).start();
            this.mHandle.animate().setInterpolator(interpolator).setDuration(200).translationZ(0.0f).start();
            this.mBackgroundLifted = false;
        }
    }

    private void initializeSurfaceState() {
        Rect rect;
        this.mSplitLayout.resizeSplits(this.mSplitLayout.getSnapAlgorithm().getMiddleTarget().position);
        SurfaceControl.Transaction transaction = this.mTiles.getTransaction();
        if (this.mDockedStackMinimized) {
            int i = this.mSplitLayout.getMinimizedSnapAlgorithm(this.mHomeStackResizable).getMiddleTarget().position;
            calculateBoundsForPosition(i, this.mDockSide, this.mDockedRect);
            calculateBoundsForPosition(i, DockedDividerUtils.invertDockSide(this.mDockSide), this.mOtherRect);
            this.mDividerPositionY = i;
            this.mDividerPositionX = i;
            Rect rect2 = this.mDockedRect;
            LegacySplitDisplayLayout legacySplitDisplayLayout = this.mSplitLayout;
            resizeSplitSurfaces(transaction, rect2, legacySplitDisplayLayout.mPrimary, this.mOtherRect, legacySplitDisplayLayout.mSecondary);
        } else {
            LegacySplitDisplayLayout legacySplitDisplayLayout2 = this.mSplitLayout;
            resizeSplitSurfaces(transaction, legacySplitDisplayLayout2.mPrimary, null, legacySplitDisplayLayout2.mSecondary, null);
        }
        setResizeDimLayer(transaction, true, 0.0f);
        setResizeDimLayer(transaction, false, 0.0f);
        transaction.apply();
        this.mTiles.releaseTransaction(transaction);
        if (isHorizontalDivision()) {
            rect = new Rect(0, this.mDividerInsets, this.mSplitLayout.mDisplayLayout.width(), this.mDividerInsets + this.mDividerSize);
        } else {
            int i2 = this.mDividerInsets;
            rect = new Rect(i2, 0, this.mDividerSize + i2, this.mSplitLayout.mDisplayLayout.height());
        }
        Region region = new Region(rect);
        region.union(new Rect(this.mHandle.getLeft(), this.mHandle.getTop(), this.mHandle.getRight(), this.mHandle.getBottom()));
        this.mWindowManager.setTouchRegion(region);
    }

    /* access modifiers changed from: package-private */
    public void setMinimizedDockStack(boolean z, boolean z2, SurfaceControl.Transaction transaction) {
        this.mHomeStackResizable = z2;
        updateDockSide();
        if (!z) {
            resetBackground();
        }
        this.mMinimizedShadow.setAlpha(z ? 1.0f : 0.0f);
        if (this.mDockedStackMinimized != z) {
            this.mDockedStackMinimized = z;
            if (this.mSplitLayout.mDisplayLayout.rotation() != this.mDefaultDisplay.getRotation()) {
                repositionSnapTargetBeforeMinimized();
            }
            if (this.mIsInMinimizeInteraction != z || this.mCurrentAnimator != null) {
                cancelFlingAnimation();
                if (z) {
                    requestLayout();
                    this.mIsInMinimizeInteraction = true;
                    resizeStackSurfaces(this.mSplitLayout.getMinimizedSnapAlgorithm(this.mHomeStackResizable).getMiddleTarget(), transaction);
                    return;
                }
                resizeStackSurfaces(this.mSnapTargetBeforeMinimized, transaction);
                this.mIsInMinimizeInteraction = false;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void enterSplitMode(boolean z) {
        setHidden(false);
        DividerSnapAlgorithm.SnapTarget middleTarget = this.mSplitLayout.getMinimizedSnapAlgorithm(z).getMiddleTarget();
        if (this.mDockedStackMinimized) {
            int i = middleTarget.position;
            this.mDividerPositionX = i;
            this.mDividerPositionY = i;
        }
    }

    private SurfaceControl getWindowSurfaceControl() {
        return this.mWindowManager.mSystemWindows.getViewSurface(this);
    }

    /* access modifiers changed from: package-private */
    public void exitSplitMode() {
        SurfaceControl windowSurfaceControl = getWindowSurfaceControl();
        if (windowSurfaceControl != null) {
            SurfaceControl.Transaction transaction = this.mTiles.getTransaction();
            transaction.hide(windowSurfaceControl);
            this.mImeController.setDimsHidden(transaction, true);
            transaction.apply();
            this.mTiles.releaseTransaction(transaction);
            this.mWindowManagerProxy.applyResizeSplits(this.mSplitLayout.getSnapAlgorithm().getMiddleTarget().position, this.mSplitLayout);
        }
    }

    /* access modifiers changed from: package-private */
    public void setMinimizedDockStack(boolean z, long j, boolean z2) {
        int i;
        DividerSnapAlgorithm.SnapTarget snapTarget;
        this.mHomeStackResizable = z2;
        updateDockSide();
        if (this.mDockedStackMinimized != z) {
            this.mIsInMinimizeInteraction = true;
            this.mDockedStackMinimized = z;
            if (z) {
                i = this.mSnapTargetBeforeMinimized.position;
            } else {
                i = getCurrentPosition();
            }
            if (z) {
                snapTarget = this.mSplitLayout.getMinimizedSnapAlgorithm(this.mHomeStackResizable).getMiddleTarget();
            } else {
                snapTarget = this.mSnapTargetBeforeMinimized;
            }
            stopDragging(i, snapTarget, j, Interpolators.FAST_OUT_SLOW_IN, 0);
            setAdjustedForIme(false, j);
        }
        if (!z) {
            this.mBackground.animate().withEndAction(this.mResetBackgroundRunnable);
        }
        this.mBackground.animate().setInterpolator(Interpolators.FAST_OUT_SLOW_IN).setDuration(j).start();
    }

    /* access modifiers changed from: package-private */
    public void finishAnimations() {
        ValueAnimator valueAnimator = this.mCurrentAnimator;
        if (valueAnimator != null) {
            valueAnimator.end();
        }
    }

    /* access modifiers changed from: package-private */
    public void setAdjustedForIme(boolean z, long j) {
        if (this.mAdjustedForIme != z) {
            updateDockSide();
            ViewPropertyAnimator animate = this.mHandle.animate();
            Interpolator interpolator = IME_ADJUST_INTERPOLATOR;
            float f = 1.0f;
            animate.setInterpolator(interpolator).setDuration(j).alpha(z ? 0.0f : 1.0f).start();
            if (this.mDockSide == 2) {
                this.mBackground.setPivotY(0.0f);
                ViewPropertyAnimator animate2 = this.mBackground.animate();
                if (z) {
                    f = 0.5f;
                }
                animate2.scaleY(f);
            }
            if (!z) {
                this.mBackground.animate().withEndAction(this.mResetBackgroundRunnable);
            }
            this.mBackground.animate().setInterpolator(interpolator).setDuration(j).start();
            this.mAdjustedForIme = z;
        }
    }

    private void saveSnapTargetBeforeMinimized(DividerSnapAlgorithm.SnapTarget snapTarget) {
        int i;
        this.mSnapTargetBeforeMinimized = snapTarget;
        DividerState dividerState = this.mState;
        float f = (float) snapTarget.position;
        if (isHorizontalDivision()) {
            i = this.mSplitLayout.mDisplayLayout.height();
        } else {
            i = this.mSplitLayout.mDisplayLayout.width();
        }
        dividerState.mRatioPositionBeforeMinimized = f / ((float) i);
    }

    /* access modifiers changed from: private */
    public void resetBackground() {
        View view = this.mBackground;
        view.setPivotX((float) (view.getWidth() / 2));
        View view2 = this.mBackground;
        view2.setPivotY((float) (view2.getHeight() / 2));
        this.mBackground.setScaleX(1.0f);
        this.mBackground.setScaleY(1.0f);
        this.mMinimizedShadow.setAlpha(0.0f);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    private void repositionSnapTargetBeforeMinimized() {
        int i;
        float f = this.mState.mRatioPositionBeforeMinimized;
        if (isHorizontalDivision()) {
            i = this.mSplitLayout.mDisplayLayout.height();
        } else {
            i = this.mSplitLayout.mDisplayLayout.width();
        }
        this.mSnapTargetBeforeMinimized = this.mSplitLayout.getSnapAlgorithm().calculateNonDismissingSnapTarget((int) (f * ((float) i)));
    }

    private int calculatePosition(int i, int i2) {
        return isHorizontalDivision() ? calculateYPosition(i2) : calculateXPosition(i);
    }

    public boolean isHorizontalDivision() {
        return getResources().getConfiguration().orientation == 1;
    }

    private int calculateXPosition(int i) {
        return (this.mStartPosition + i) - this.mStartX;
    }

    private int calculateYPosition(int i) {
        return (this.mStartPosition + i) - this.mStartY;
    }

    private void alignTopLeft(Rect rect, Rect rect2) {
        int width = rect2.width();
        int height = rect2.height();
        int i = rect.left;
        int i2 = rect.top;
        rect2.set(i, i2, width + i, height + i2);
    }

    private void alignBottomRight(Rect rect, Rect rect2) {
        int width = rect2.width();
        int height = rect2.height();
        int i = rect.right;
        int i2 = rect.bottom;
        rect2.set(i - width, i2 - height, i, i2);
    }

    private void calculateBoundsForPosition(int i, int i2, Rect rect) {
        DockedDividerUtils.calculateBoundsForPosition(i, i2, rect, this.mSplitLayout.mDisplayLayout.width(), this.mSplitLayout.mDisplayLayout.height(), this.mDividerSize);
    }

    private void resizeStackSurfaces(DividerSnapAlgorithm.SnapTarget snapTarget, SurfaceControl.Transaction transaction) {
        int i = snapTarget.position;
        resizeStackSurfaces(i, i, snapTarget, transaction);
    }

    /* access modifiers changed from: package-private */
    public void resizeSplitSurfaces(SurfaceControl.Transaction transaction, Rect rect, Rect rect2) {
        resizeSplitSurfaces(transaction, rect, null, rect2, null);
    }

    private void resizeSplitSurfaces(SurfaceControl.Transaction transaction, Rect rect, Rect rect2, Rect rect3, Rect rect4) {
        if (rect2 == null) {
            rect2 = rect;
        }
        if (rect4 == null) {
            rect4 = rect3;
        }
        this.mDividerPositionX = this.mSplitLayout.getPrimarySplitSide() == 3 ? rect3.right : rect.right;
        this.mDividerPositionY = rect.bottom;
        transaction.setPosition(this.mTiles.mPrimarySurface, (float) rect2.left, (float) rect2.top);
        Rect rect5 = new Rect(rect);
        rect5.offsetTo(-Math.min(rect2.left - rect.left, 0), -Math.min(rect2.top - rect.top, 0));
        transaction.setWindowCrop(this.mTiles.mPrimarySurface, rect5);
        transaction.setPosition(this.mTiles.mSecondarySurface, (float) rect4.left, (float) rect4.top);
        rect5.set(rect3);
        rect5.offsetTo(-(rect4.left - rect3.left), -(rect4.top - rect3.top));
        transaction.setWindowCrop(this.mTiles.mSecondarySurface, rect5);
        SurfaceControl windowSurfaceControl = getWindowSurfaceControl();
        if (windowSurfaceControl != null) {
            if (isHorizontalDivision()) {
                transaction.setPosition(windowSurfaceControl, 0.0f, (float) (this.mDividerPositionY - this.mDividerInsets));
            } else {
                transaction.setPosition(windowSurfaceControl, (float) (this.mDividerPositionX - this.mDividerInsets), 0.0f);
            }
        }
        if (getViewRootImpl() != null) {
            getHandler().removeCallbacks(this.mUpdateEmbeddedMatrix);
            getHandler().post(this.mUpdateEmbeddedMatrix);
        }
    }

    /* access modifiers changed from: package-private */
    public void setResizeDimLayer(SurfaceControl.Transaction transaction, boolean z, float f) {
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mTiles;
        SurfaceControl surfaceControl = z ? legacySplitScreenTaskListener.mPrimaryDim : legacySplitScreenTaskListener.mSecondaryDim;
        if (f <= 0.001f) {
            transaction.hide(surfaceControl);
            return;
        }
        transaction.setAlpha(surfaceControl, f);
        transaction.show(surfaceControl);
    }

    void resizeStackSurfaces(int i, int i2, DividerSnapAlgorithm.SnapTarget snapTarget, SurfaceControl.Transaction transaction) {
        if (!this.mRemoved) {
            calculateBoundsForPosition(i, this.mDockSide, this.mDockedRect);
            calculateBoundsForPosition(i, DockedDividerUtils.invertDockSide(this.mDockSide), this.mOtherRect);
            if (!this.mDockedRect.equals(this.mLastResizeRect) || this.mEntranceAnimationRunning) {
                if (this.mBackground.getZ() > 0.0f) {
                    this.mBackground.invalidate();
                }
                boolean z = transaction == null;
                SurfaceControl.Transaction transaction2 = z ? this.mTiles.getTransaction() : transaction;
                this.mLastResizeRect.set(this.mDockedRect);
                if (this.mIsInMinimizeInteraction) {
                    calculateBoundsForPosition(this.mSnapTargetBeforeMinimized.position, this.mDockSide, this.mDockedTaskRect);
                    calculateBoundsForPosition(this.mSnapTargetBeforeMinimized.position, DockedDividerUtils.invertDockSide(this.mDockSide), this.mOtherTaskRect);
                    if (this.mDockSide == 3) {
                        this.mDockedTaskRect.offset((Math.max(i, -this.mDividerSize) - this.mDockedTaskRect.left) + this.mDividerSize, 0);
                    }
                    resizeSplitSurfaces(transaction2, this.mDockedRect, this.mDockedTaskRect, this.mOtherRect, this.mOtherTaskRect);
                    if (z) {
                        transaction2.setFrameTimelineVsync(Choreographer.getSfInstance().getVsyncId());
                        transaction2.apply();
                        this.mTiles.releaseTransaction(transaction2);
                        return;
                    }
                    return;
                }
                if (this.mEntranceAnimationRunning && i2 != Integer.MAX_VALUE) {
                    calculateBoundsForPosition(i2, this.mDockSide, this.mDockedTaskRect);
                    if (this.mDockSide == 3) {
                        this.mDockedTaskRect.offset((Math.max(i, -this.mDividerSize) - this.mDockedTaskRect.left) + this.mDividerSize, 0);
                    }
                    calculateBoundsForPosition(i2, DockedDividerUtils.invertDockSide(this.mDockSide), this.mOtherTaskRect);
                    resizeSplitSurfaces(transaction2, this.mDockedRect, this.mDockedTaskRect, this.mOtherRect, this.mOtherTaskRect);
                } else if (this.mExitAnimationRunning && i2 != Integer.MAX_VALUE) {
                    calculateBoundsForPosition(i2, this.mDockSide, this.mDockedTaskRect);
                    this.mDockedInsetRect.set(this.mDockedTaskRect);
                    calculateBoundsForPosition(this.mExitStartPosition, DockedDividerUtils.invertDockSide(this.mDockSide), this.mOtherTaskRect);
                    this.mOtherInsetRect.set(this.mOtherTaskRect);
                    applyExitAnimationParallax(this.mOtherTaskRect, i);
                    if (this.mDockSide == 3) {
                        this.mDockedTaskRect.offset(this.mDividerSize + i, 0);
                    }
                    resizeSplitSurfaces(transaction2, this.mDockedRect, this.mDockedTaskRect, this.mOtherRect, this.mOtherTaskRect);
                } else if (i2 != Integer.MAX_VALUE) {
                    calculateBoundsForPosition(i, DockedDividerUtils.invertDockSide(this.mDockSide), this.mOtherRect);
                    int invertDockSide = DockedDividerUtils.invertDockSide(this.mDockSide);
                    int restrictDismissingTaskPosition = restrictDismissingTaskPosition(i2, this.mDockSide, snapTarget);
                    int restrictDismissingTaskPosition2 = restrictDismissingTaskPosition(i2, invertDockSide, snapTarget);
                    calculateBoundsForPosition(restrictDismissingTaskPosition, this.mDockSide, this.mDockedTaskRect);
                    calculateBoundsForPosition(restrictDismissingTaskPosition2, invertDockSide, this.mOtherTaskRect);
                    this.mTmpRect.set(0, 0, this.mSplitLayout.mDisplayLayout.width(), this.mSplitLayout.mDisplayLayout.height());
                    alignTopLeft(this.mDockedRect, this.mDockedTaskRect);
                    alignTopLeft(this.mOtherRect, this.mOtherTaskRect);
                    this.mDockedInsetRect.set(this.mDockedTaskRect);
                    this.mOtherInsetRect.set(this.mOtherTaskRect);
                    if (dockSideTopLeft(this.mDockSide)) {
                        alignTopLeft(this.mTmpRect, this.mDockedInsetRect);
                        alignBottomRight(this.mTmpRect, this.mOtherInsetRect);
                    } else {
                        alignBottomRight(this.mTmpRect, this.mDockedInsetRect);
                        alignTopLeft(this.mTmpRect, this.mOtherInsetRect);
                    }
                    applyDismissingParallax(this.mDockedTaskRect, this.mDockSide, snapTarget, i, restrictDismissingTaskPosition);
                    applyDismissingParallax(this.mOtherTaskRect, invertDockSide, snapTarget, i, restrictDismissingTaskPosition2);
                    resizeSplitSurfaces(transaction2, this.mDockedRect, this.mDockedTaskRect, this.mOtherRect, this.mOtherTaskRect);
                } else {
                    resizeSplitSurfaces(transaction2, this.mDockedRect, null, this.mOtherRect, null);
                }
                DividerSnapAlgorithm.SnapTarget closestDismissTarget = getSnapAlgorithm().getClosestDismissTarget(i);
                setResizeDimLayer(transaction2, isDismissTargetPrimary(closestDismissTarget), getDimFraction(i, closestDismissTarget));
                if (z) {
                    transaction2.apply();
                    this.mTiles.releaseTransaction(transaction2);
                }
            }
        }
    }

    private void applyExitAnimationParallax(Rect rect, int i) {
        int i2 = this.mDockSide;
        if (i2 == 2) {
            rect.offset(0, (int) (((float) (i - this.mExitStartPosition)) * 0.25f));
        } else if (i2 == 1) {
            rect.offset((int) (((float) (i - this.mExitStartPosition)) * 0.25f), 0);
        } else if (i2 == 3) {
            rect.offset((int) (((float) (this.mExitStartPosition - i)) * 0.25f), 0);
        }
    }

    private float getDimFraction(int i, DividerSnapAlgorithm.SnapTarget snapTarget) {
        if (this.mEntranceAnimationRunning) {
            return 0.0f;
        }
        return DIM_INTERPOLATOR.getInterpolation(Math.max(0.0f, Math.min(getSnapAlgorithm().calculateDismissingFraction(i), 1.0f)));
    }

    private int restrictDismissingTaskPosition(int i, int i2, DividerSnapAlgorithm.SnapTarget snapTarget) {
        if (snapTarget.flag != 1 || !dockSideTopLeft(i2)) {
            return (snapTarget.flag != 2 || !dockSideBottomRight(i2)) ? i : Math.min(this.mSplitLayout.getSnapAlgorithm().getLastSplitTarget().position, this.mStartPosition);
        }
        return Math.max(this.mSplitLayout.getSnapAlgorithm().getFirstSplitTarget().position, this.mStartPosition);
    }

    private void applyDismissingParallax(Rect rect, int i, DividerSnapAlgorithm.SnapTarget snapTarget, int i2, int i3) {
        DividerSnapAlgorithm.SnapTarget snapTarget2;
        float min = Math.min(1.0f, Math.max(0.0f, this.mSplitLayout.getSnapAlgorithm().calculateDismissingFraction(i2)));
        DividerSnapAlgorithm.SnapTarget snapTarget3 = null;
        if (i2 <= this.mSplitLayout.getSnapAlgorithm().getLastSplitTarget().position && dockSideTopLeft(i)) {
            snapTarget3 = this.mSplitLayout.getSnapAlgorithm().getDismissStartTarget();
            snapTarget2 = this.mSplitLayout.getSnapAlgorithm().getFirstSplitTarget();
        } else if (i2 < this.mSplitLayout.getSnapAlgorithm().getLastSplitTarget().position || !dockSideBottomRight(i)) {
            i3 = 0;
            snapTarget2 = null;
        } else {
            snapTarget3 = this.mSplitLayout.getSnapAlgorithm().getDismissEndTarget();
            DividerSnapAlgorithm.SnapTarget lastSplitTarget = this.mSplitLayout.getSnapAlgorithm().getLastSplitTarget();
            i3 = lastSplitTarget.position;
            snapTarget2 = lastSplitTarget;
        }
        if (snapTarget3 != null && min > 0.0f && isDismissing(snapTarget2, i2, i)) {
            int calculateParallaxDismissingFraction = (int) (((float) i3) + (calculateParallaxDismissingFraction(min, i) * ((float) (snapTarget3.position - snapTarget2.position))));
            int width = rect.width();
            int height = rect.height();
            if (i == 1) {
                rect.left = calculateParallaxDismissingFraction - width;
                rect.right = calculateParallaxDismissingFraction;
            } else if (i == 2) {
                rect.top = calculateParallaxDismissingFraction - height;
                rect.bottom = calculateParallaxDismissingFraction;
            } else if (i == 3) {
                int i4 = this.mDividerSize;
                rect.left = calculateParallaxDismissingFraction + i4;
                rect.right = calculateParallaxDismissingFraction + width + i4;
            } else if (i == 4) {
                int i5 = this.mDividerSize;
                rect.top = calculateParallaxDismissingFraction + i5;
                rect.bottom = calculateParallaxDismissingFraction + height + i5;
            }
        }
    }

    private static float calculateParallaxDismissingFraction(float f, int i) {
        float interpolation = SLOWDOWN_INTERPOLATOR.getInterpolation(f) / 3.5f;
        return i == 2 ? interpolation / 2.0f : interpolation;
    }

    private static boolean isDismissing(DividerSnapAlgorithm.SnapTarget snapTarget, int i, int i2) {
        return (i2 == 2 || i2 == 1) ? i < snapTarget.position : i > snapTarget.position;
    }

    private boolean isDismissTargetPrimary(DividerSnapAlgorithm.SnapTarget snapTarget) {
        if (snapTarget.flag == 1 && dockSideTopLeft(this.mDockSide)) {
            return true;
        }
        if (snapTarget.flag != 2 || !dockSideBottomRight(this.mDockSide)) {
            return false;
        }
        return true;
    }

    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.set(this.mHandle.getLeft(), this.mHandle.getTop(), this.mHandle.getRight(), this.mHandle.getBottom());
        internalInsetsInfo.touchableRegion.op(this.mBackground.getLeft(), this.mBackground.getTop(), this.mBackground.getRight(), this.mBackground.getBottom(), Region.Op.UNION);
    }

    /* access modifiers changed from: package-private */
    public void onUndockingTask() {
        DividerSnapAlgorithm.SnapTarget snapTarget;
        int primarySplitSide = this.mSplitLayout.getPrimarySplitSide();
        if (inSplitMode()) {
            startDragging(false, false);
            if (dockSideTopLeft(primarySplitSide)) {
                snapTarget = this.mSplitLayout.getSnapAlgorithm().getDismissEndTarget();
            } else {
                snapTarget = this.mSplitLayout.getSnapAlgorithm().getDismissStartTarget();
            }
            this.mExitAnimationRunning = true;
            int currentPosition = getCurrentPosition();
            this.mExitStartPosition = currentPosition;
            stopDragging(currentPosition, snapTarget, 336, 100, 0, Interpolators.FAST_OUT_SLOW_IN);
        }
    }
}
