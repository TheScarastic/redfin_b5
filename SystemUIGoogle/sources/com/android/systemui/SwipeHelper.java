package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.RectF;
import android.os.Handler;
import android.util.ArrayMap;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.wm.shell.animation.FlingAnimationUtils;
/* loaded from: classes.dex */
public class SwipeHelper implements Gefingerpoken {
    private final Callback mCallback;
    private boolean mCanCurrViewBeDimissed;
    private float mDensityScale;
    private boolean mDisableHwLayers;
    private final boolean mFadeDependingOnAmountSwiped;
    private final FalsingManager mFalsingManager;
    private final int mFalsingThreshold;
    private final FlingAnimationUtils mFlingAnimationUtils;
    private float mInitialTouchPos;
    private boolean mIsSwiping;
    private boolean mLongPressSent;
    private boolean mMenuRowIntercepting;
    private float mPagingTouchSlop;
    private float mPerpendicularInitialTouchPos;
    private final float mSlopMultiplier;
    private boolean mSnappingChild;
    private final int mSwipeDirection;
    private boolean mTouchAboveFalsingThreshold;
    private View mTouchedView;
    private float mMinSwipeProgress = 0.0f;
    private float mMaxSwipeProgress = 1.0f;
    private float mTranslation = 0.0f;
    private final float[] mDownLocation = new float[2];
    private final Runnable mPerformLongPress = new Runnable() { // from class: com.android.systemui.SwipeHelper.1
        private final int[] mViewOffset = new int[2];

        @Override // java.lang.Runnable
        public void run() {
            if (SwipeHelper.this.mTouchedView != null && !SwipeHelper.this.mLongPressSent) {
                SwipeHelper.this.mLongPressSent = true;
                if (SwipeHelper.this.mTouchedView instanceof ExpandableNotificationRow) {
                    SwipeHelper.this.mTouchedView.getLocationOnScreen(this.mViewOffset);
                    int i = ((int) SwipeHelper.this.mDownLocation[0]) - this.mViewOffset[0];
                    int i2 = ((int) SwipeHelper.this.mDownLocation[1]) - this.mViewOffset[1];
                    SwipeHelper.this.mTouchedView.sendAccessibilityEvent(2);
                    ((ExpandableNotificationRow) SwipeHelper.this.mTouchedView).doLongClickCallback(i, i2);
                }
            }
        }
    };
    private final ArrayMap<View, Animator> mDismissPendingMap = new ArrayMap<>();
    protected final Handler mHandler = new Handler();
    private final VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    private final long mLongPressTimeout = (long) (((float) ViewConfiguration.getLongPressTimeout()) * 1.5f);

    protected long getMaxEscapeAnimDuration() {
        return 400;
    }

    protected float getTotalTranslationLength(View view) {
        throw null;
    }

    protected float getTranslation(View view) {
        throw null;
    }

    protected float getUnscaledEscapeVelocity() {
        return 500.0f;
    }

    protected boolean handleUpEvent(MotionEvent motionEvent, View view, float f, float f2) {
        throw null;
    }

    public void onDownUpdate(View view, MotionEvent motionEvent) {
        throw null;
    }

    protected void onMoveUpdate(View view, MotionEvent motionEvent, float f, float f2) {
        throw null;
    }

    protected void prepareDismissAnimation(View view, Animator animator) {
    }

    protected void prepareSnapBackAnimation(View view, Animator animator) {
    }

    protected void setTranslation(View view, float f) {
        throw null;
    }

    public SwipeHelper(int i, Callback callback, Resources resources, ViewConfiguration viewConfiguration, FalsingManager falsingManager) {
        this.mCallback = callback;
        this.mSwipeDirection = i;
        this.mPagingTouchSlop = (float) viewConfiguration.getScaledPagingTouchSlop();
        this.mSlopMultiplier = viewConfiguration.getScaledAmbiguousGestureMultiplier();
        this.mDensityScale = resources.getDisplayMetrics().density;
        this.mFalsingThreshold = resources.getDimensionPixelSize(R$dimen.swipe_helper_falsing_threshold);
        this.mFadeDependingOnAmountSwiped = resources.getBoolean(R$bool.config_fadeDependingOnAmountSwiped);
        this.mFalsingManager = falsingManager;
        this.mFlingAnimationUtils = new FlingAnimationUtils(resources.getDisplayMetrics(), ((float) getMaxEscapeAnimDuration()) / 1000.0f);
    }

    public void setDensityScale(float f) {
        this.mDensityScale = f;
    }

    public void setPagingTouchSlop(float f) {
        this.mPagingTouchSlop = f;
    }

    private float getPos(MotionEvent motionEvent) {
        return this.mSwipeDirection == 0 ? motionEvent.getX() : motionEvent.getY();
    }

    private float getPerpendicularPos(MotionEvent motionEvent) {
        return this.mSwipeDirection == 0 ? motionEvent.getY() : motionEvent.getX();
    }

    private float getVelocity(VelocityTracker velocityTracker) {
        if (this.mSwipeDirection == 0) {
            return velocityTracker.getXVelocity();
        }
        return velocityTracker.getYVelocity();
    }

    protected ObjectAnimator createTranslationAnimation(View view, float f) {
        return ObjectAnimator.ofFloat(view, this.mSwipeDirection == 0 ? View.TRANSLATION_X : View.TRANSLATION_Y, f);
    }

    /* access modifiers changed from: protected */
    public Animator getViewTranslationAnimator(View view, float f, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        ObjectAnimator createTranslationAnimation = createTranslationAnimation(view, f);
        if (animatorUpdateListener != null) {
            createTranslationAnimation.addUpdateListener(animatorUpdateListener);
        }
        return createTranslationAnimation;
    }

    protected float getSize(View view) {
        return (float) (this.mSwipeDirection == 0 ? view.getMeasuredWidth() : view.getMeasuredHeight());
    }

    private float getSwipeProgressForOffset(View view, float f) {
        return Math.min(Math.max(this.mMinSwipeProgress, Math.abs(f / getSize(view))), this.mMaxSwipeProgress);
    }

    private float getSwipeAlpha(float f) {
        if (this.mFadeDependingOnAmountSwiped) {
            return Math.max(1.0f - f, 0.0f);
        }
        return 1.0f - Math.max(0.0f, Math.min(1.0f, f / 0.5f));
    }

    /* access modifiers changed from: private */
    public void updateSwipeProgressFromOffset(View view, boolean z) {
        updateSwipeProgressFromOffset(view, z, getTranslation(view));
    }

    private void updateSwipeProgressFromOffset(View view, boolean z, float f) {
        float swipeProgressForOffset = getSwipeProgressForOffset(view, f);
        if (!this.mCallback.updateSwipeProgress(view, z, swipeProgressForOffset) && z) {
            if (!this.mDisableHwLayers) {
                if (swipeProgressForOffset == 0.0f || swipeProgressForOffset == 1.0f) {
                    view.setLayerType(0, null);
                } else {
                    view.setLayerType(2, null);
                }
            }
            view.setAlpha(getSwipeAlpha(swipeProgressForOffset));
        }
        invalidateGlobalRegion(view);
    }

    public static void invalidateGlobalRegion(View view) {
        invalidateGlobalRegion(view, new RectF((float) view.getLeft(), (float) view.getTop(), (float) view.getRight(), (float) view.getBottom()));
    }

    public static void invalidateGlobalRegion(View view, RectF rectF) {
        while (view.getParent() != null && (view.getParent() instanceof View)) {
            view = (View) view.getParent();
            view.getMatrix().mapRect(rectF);
            view.invalidate((int) Math.floor((double) rectF.left), (int) Math.floor((double) rectF.top), (int) Math.ceil((double) rectF.right), (int) Math.ceil((double) rectF.bottom));
        }
    }

    public void cancelLongPress() {
        this.mHandler.removeCallbacks(this.mPerformLongPress);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0024, code lost:
        if (r0 != 3) goto L_0x011a;
     */
    @Override // com.android.systemui.Gefingerpoken
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onInterceptTouchEvent(android.view.MotionEvent r8) {
        /*
        // Method dump skipped, instructions count: 297
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.SwipeHelper.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    public void dismissChild(View view, float f, boolean z) {
        dismissChild(view, f, null, 0, z, 0, false);
    }

    public void dismissChild(final View view, float f, final Runnable runnable, long j, boolean z, long j2, boolean z2) {
        float f2;
        long j3;
        final boolean canChildBeDismissed = this.mCallback.canChildBeDismissed(view);
        boolean z3 = false;
        boolean z4 = view.getLayoutDirection() == 1;
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        boolean z5 = i == 0 && (getTranslation(view) == 0.0f || z2) && this.mSwipeDirection == 1;
        boolean z6 = i == 0 && (getTranslation(view) == 0.0f || z2) && z4;
        if ((Math.abs(f) > getEscapeVelocity() && f < 0.0f) || (getTranslation(view) < 0.0f && !z2)) {
            z3 = true;
        }
        if (z3 || z6 || z5) {
            f2 = -getTotalTranslationLength(view);
        } else {
            f2 = getTotalTranslationLength(view);
        }
        if (j2 == 0) {
            j3 = i != 0 ? Math.min(400L, (long) ((int) ((Math.abs(f2 - getTranslation(view)) * 1000.0f) / Math.abs(f)))) : 200;
        } else {
            j3 = j2;
        }
        if (!this.mDisableHwLayers) {
            view.setLayerType(2, null);
        }
        Animator viewTranslationAnimator = getViewTranslationAnimator(view, f2, new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.SwipeHelper.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                SwipeHelper.this.onTranslationUpdate(view, ((Float) valueAnimator.getAnimatedValue()).floatValue(), canChildBeDismissed);
            }
        });
        if (viewTranslationAnimator != null) {
            if (z) {
                viewTranslationAnimator.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
                viewTranslationAnimator.setDuration(j3);
            } else {
                this.mFlingAnimationUtils.applyDismissing(viewTranslationAnimator, getTranslation(view), f2, f, getSize(view));
            }
            if (j > 0) {
                viewTranslationAnimator.setStartDelay(j);
            }
            viewTranslationAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.SwipeHelper.3
                private boolean mCancelled;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.mCancelled = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SwipeHelper.this.updateSwipeProgressFromOffset(view, canChildBeDismissed);
                    SwipeHelper.this.mDismissPendingMap.remove(view);
                    View view2 = view;
                    boolean isRemoved = view2 instanceof ExpandableNotificationRow ? ((ExpandableNotificationRow) view2).isRemoved() : false;
                    if (!this.mCancelled || isRemoved) {
                        SwipeHelper.this.mCallback.onChildDismissed(view);
                        SwipeHelper.this.resetSwipeState();
                    }
                    Runnable runnable2 = runnable;
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                    if (!SwipeHelper.this.mDisableHwLayers) {
                        view.setLayerType(0, null);
                    }
                }
            });
            prepareDismissAnimation(view, viewTranslationAnimator);
            this.mDismissPendingMap.put(view, viewTranslationAnimator);
            viewTranslationAnimator.start();
        }
    }

    public void snapChild(final View view, float f, float f2) {
        final boolean canChildBeDismissed = this.mCallback.canChildBeDismissed(view);
        Animator viewTranslationAnimator = getViewTranslationAnimator(view, f, new ValueAnimator.AnimatorUpdateListener(view, canChildBeDismissed) { // from class: com.android.systemui.SwipeHelper$$ExternalSyntheticLambda0
            public final /* synthetic */ View f$1;
            public final /* synthetic */ boolean f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SwipeHelper.$r8$lambda$oZNJKeeaVNOiKb5ASGdHpRK1AoI(SwipeHelper.this, this.f$1, this.f$2, valueAnimator);
            }
        });
        if (viewTranslationAnimator != null) {
            viewTranslationAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.SwipeHelper.4
                boolean wasCancelled = false;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.wasCancelled = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SwipeHelper.this.mSnappingChild = false;
                    if (!this.wasCancelled) {
                        SwipeHelper.this.updateSwipeProgressFromOffset(view, canChildBeDismissed);
                        SwipeHelper.this.resetSwipeState();
                    }
                }
            });
            prepareSnapBackAnimation(view, viewTranslationAnimator);
            this.mSnappingChild = true;
            this.mFlingAnimationUtils.apply(viewTranslationAnimator, getTranslation(view), f, f2, Math.abs(f - getTranslation(view)));
            viewTranslationAnimator.start();
            this.mCallback.onChildSnappedBack(view, f);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$snapChild$0(View view, boolean z, ValueAnimator valueAnimator) {
        onTranslationUpdate(view, ((Float) valueAnimator.getAnimatedValue()).floatValue(), z);
    }

    public void onTranslationUpdate(View view, float f, boolean z) {
        updateSwipeProgressFromOffset(view, z, f);
    }

    private void snapChildInstantly(View view) {
        boolean canChildBeDismissed = this.mCallback.canChildBeDismissed(view);
        setTranslation(view, 0.0f);
        updateSwipeProgressFromOffset(view, canChildBeDismissed);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0025, code lost:
        if (getTranslation(r5) != 0.0f) goto L_0x001d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void snapChildIfNeeded(android.view.View r5, boolean r6, float r7) {
        /*
            r4 = this;
            boolean r0 = r4.mIsSwiping
            if (r0 == 0) goto L_0x0008
            android.view.View r0 = r4.mTouchedView
            if (r0 == r5) goto L_0x000c
        L_0x0008:
            boolean r0 = r4.mSnappingChild
            if (r0 == 0) goto L_0x000d
        L_0x000c:
            return
        L_0x000d:
            r0 = 0
            android.util.ArrayMap<android.view.View, android.animation.Animator> r1 = r4.mDismissPendingMap
            java.lang.Object r1 = r1.get(r5)
            android.animation.Animator r1 = (android.animation.Animator) r1
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x001f
            r1.cancel()
        L_0x001d:
            r0 = r2
            goto L_0x0028
        L_0x001f:
            float r1 = r4.getTranslation(r5)
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 == 0) goto L_0x0028
            goto L_0x001d
        L_0x0028:
            if (r0 == 0) goto L_0x0033
            if (r6 == 0) goto L_0x0030
            r4.snapChild(r5, r7, r3)
            goto L_0x0033
        L_0x0030:
            r4.snapChildInstantly(r5)
        L_0x0033:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.SwipeHelper.snapChildIfNeeded(android.view.View, boolean, float):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003e, code lost:
        if (r0 != 4) goto L_0x00fe;
     */
    @Override // com.android.systemui.Gefingerpoken
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r11) {
        /*
        // Method dump skipped, instructions count: 255
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.SwipeHelper.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private int getFalsingThreshold() {
        return (int) (((float) this.mFalsingThreshold) * this.mCallback.getFalsingThresholdFactor());
    }

    private float getMaxVelocity() {
        return this.mDensityScale * 4000.0f;
    }

    /* access modifiers changed from: protected */
    public float getEscapeVelocity() {
        return getUnscaledEscapeVelocity() * this.mDensityScale;
    }

    /* access modifiers changed from: protected */
    public boolean swipedFarEnough() {
        return Math.abs(getTranslation(this.mTouchedView)) > getSize(this.mTouchedView) * 0.6f;
    }

    public boolean isDismissGesture(MotionEvent motionEvent) {
        float translation = getTranslation(this.mTouchedView);
        if (motionEvent.getActionMasked() != 1 || this.mFalsingManager.isUnlockingDisabled() || isFalseGesture()) {
            return false;
        }
        if (!swipedFastEnough() && !swipedFarEnough()) {
            return false;
        }
        return this.mCallback.canChildBeDismissedInDirection(this.mTouchedView, (translation > 0.0f ? 1 : (translation == 0.0f ? 0 : -1)) > 0);
    }

    public boolean isFalseGesture() {
        boolean isAntiFalsingNeeded = this.mCallback.isAntiFalsingNeeded();
        if (this.mFalsingManager.isClassifierEnabled()) {
            if (!isAntiFalsingNeeded || !this.mFalsingManager.isFalseTouch(1)) {
                return false;
            }
        } else if (!isAntiFalsingNeeded || this.mTouchAboveFalsingThreshold) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean swipedFastEnough() {
        float velocity = getVelocity(this.mVelocityTracker);
        float translation = getTranslation(this.mTouchedView);
        if (Math.abs(velocity) > getEscapeVelocity()) {
            if ((velocity > 0.0f) == (translation > 0.0f)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSwiping() {
        return this.mIsSwiping;
    }

    public View getSwipedView() {
        if (this.mIsSwiping) {
            return this.mTouchedView;
        }
        return null;
    }

    public void resetSwipeState() {
        this.mTouchedView = null;
        this.mIsSwiping = false;
    }

    /* loaded from: classes.dex */
    public interface Callback {
        boolean canChildBeDismissed(View view);

        default boolean canChildBeDragged(View view) {
            return true;
        }

        View getChildAtPosition(MotionEvent motionEvent);

        default int getConstrainSwipeStartPosition() {
            return 0;
        }

        float getFalsingThresholdFactor();

        boolean isAntiFalsingNeeded();

        void onBeginDrag(View view);

        void onChildDismissed(View view);

        void onChildSnappedBack(View view, float f);

        void onDragCancelled(View view);

        boolean updateSwipeProgress(View view, boolean z, float f);

        default boolean canChildBeDismissedInDirection(View view, boolean z) {
            return canChildBeDismissed(view);
        }
    }
}
