package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Looper;
import android.util.ArrayMap;
import android.view.Choreographer;
import androidx.dynamicanimation.animation.AnimationHandler;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.FloatProperties;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.phone.PipAppOpsListener;
import java.util.function.Consumer;
import java.util.function.Supplier;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* loaded from: classes2.dex */
public class PipMotionHelper implements PipAppOpsListener.Callback, FloatingContentCoordinator.FloatingContent {
    private final Context mContext;
    private PhysicsAnimator.FlingConfig mFlingConfigX;
    private PhysicsAnimator.FlingConfig mFlingConfigY;
    private FloatingContentCoordinator mFloatingContentCoordinator;
    private MagnetizedObject<Rect> mMagnetizedPip;
    private PhonePipMenuController mMenuController;
    private PipBoundsState mPipBoundsState;
    private final PipTaskOrganizer mPipTaskOrganizer;
    private final PipTransitionController.PipTransitionCallback mPipTransitionCallback;
    private Runnable mPostPipTransitionCallback;
    private PipSnapAlgorithm mSnapAlgorithm;
    private PhysicsAnimator.FlingConfig mStashConfigX;
    private PhysicsAnimator<Rect> mTemporaryBoundsPhysicsAnimator;
    private final Rect mFloatingAllowedArea = new Rect();
    private ThreadLocal<AnimationHandler> mSfAnimationHandlerThreadLocal = ThreadLocal.withInitial(new Supplier() { // from class: com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda5
        @Override // java.util.function.Supplier
        public final Object get() {
            return PipMotionHelper.this.lambda$new$0();
        }
    });
    private final PhysicsAnimator.SpringConfig mSpringConfig = new PhysicsAnimator.SpringConfig(700.0f, 1.0f);
    private final PhysicsAnimator.SpringConfig mAnimateToDismissSpringConfig = new PhysicsAnimator.SpringConfig(1500.0f, 1.0f);
    private final PhysicsAnimator.SpringConfig mCatchUpSpringConfig = new PhysicsAnimator.SpringConfig(5000.0f, 1.0f);
    private final PhysicsAnimator.SpringConfig mConflictResolutionSpringConfig = new PhysicsAnimator.SpringConfig(200.0f, 1.0f);
    private final Consumer<Rect> mUpdateBoundsCallback = new Consumer() { // from class: com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda3
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            PipMotionHelper.this.lambda$new$1((Rect) obj);
        }
    };
    private boolean mSpringingToTouch = false;
    private boolean mDismissalPending = false;
    private final PhysicsAnimator.UpdateListener<Rect> mResizePipUpdateListener = new PhysicsAnimator.UpdateListener() { // from class: com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda0
        @Override // com.android.wm.shell.animation.PhysicsAnimator.UpdateListener
        public final void onAnimationUpdateForProperty(Object obj, ArrayMap arrayMap) {
            PipMotionHelper.this.lambda$new$2((Rect) obj, arrayMap);
        }
    };

    /* access modifiers changed from: private */
    public /* synthetic */ AnimationHandler lambda$new$0() {
        final Looper myLooper = Looper.myLooper();
        return new AnimationHandler(new AnimationHandler.FrameCallbackScheduler() { // from class: com.android.wm.shell.pip.phone.PipMotionHelper.1
            @Override // androidx.dynamicanimation.animation.AnimationHandler.FrameCallbackScheduler
            public void postFrameCallback(Runnable runnable) {
                Choreographer.getSfInstance().postFrameCallback(new PipMotionHelper$1$$ExternalSyntheticLambda0(runnable));
            }

            @Override // androidx.dynamicanimation.animation.AnimationHandler.FrameCallbackScheduler
            public boolean isCurrentThread() {
                return Looper.myLooper() == myLooper;
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Rect rect) {
        if (!this.mPipBoundsState.getBounds().equals(rect)) {
            this.mMenuController.updateMenuLayout(rect);
            this.mPipBoundsState.setBounds(rect);
        }
    }

    public PipMotionHelper(Context context, PipBoundsState pipBoundsState, PipTaskOrganizer pipTaskOrganizer, PhonePipMenuController phonePipMenuController, PipSnapAlgorithm pipSnapAlgorithm, PipTransitionController pipTransitionController, FloatingContentCoordinator floatingContentCoordinator) {
        AnonymousClass2 r0 = new PipTransitionController.PipTransitionCallback() { // from class: com.android.wm.shell.pip.phone.PipMotionHelper.2
            @Override // com.android.wm.shell.pip.PipTransitionController.PipTransitionCallback
            public void onPipTransitionCanceled(int i) {
            }

            @Override // com.android.wm.shell.pip.PipTransitionController.PipTransitionCallback
            public void onPipTransitionStarted(int i, Rect rect) {
            }

            @Override // com.android.wm.shell.pip.PipTransitionController.PipTransitionCallback
            public void onPipTransitionFinished(int i) {
                if (PipMotionHelper.this.mPostPipTransitionCallback != null) {
                    PipMotionHelper.this.mPostPipTransitionCallback.run();
                    PipMotionHelper.this.mPostPipTransitionCallback = null;
                }
            }
        };
        this.mPipTransitionCallback = r0;
        this.mContext = context;
        this.mPipTaskOrganizer = pipTaskOrganizer;
        this.mPipBoundsState = pipBoundsState;
        this.mMenuController = phonePipMenuController;
        this.mSnapAlgorithm = pipSnapAlgorithm;
        this.mFloatingContentCoordinator = floatingContentCoordinator;
        pipTransitionController.registerPipTransitionCallback(r0);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(Rect rect, ArrayMap arrayMap) {
        if (this.mPipBoundsState.getMotionBoundsState().isInMotion()) {
            this.mPipTaskOrganizer.scheduleUserResizePip(getBounds(), this.mPipBoundsState.getMotionBoundsState().getBoundsInMotion(), null);
        }
    }

    public void init() {
        PhysicsAnimator<Rect> instance = PhysicsAnimator.getInstance(this.mPipBoundsState.getMotionBoundsState().getBoundsInMotion());
        this.mTemporaryBoundsPhysicsAnimator = instance;
        instance.setCustomAnimationHandler(this.mSfAnimationHandlerThreadLocal.get());
    }

    @Override // com.android.wm.shell.common.FloatingContentCoordinator.FloatingContent
    public Rect getFloatingBoundsOnScreen() {
        return !this.mPipBoundsState.getMotionBoundsState().getAnimatingToBounds().isEmpty() ? this.mPipBoundsState.getMotionBoundsState().getAnimatingToBounds() : getBounds();
    }

    @Override // com.android.wm.shell.common.FloatingContentCoordinator.FloatingContent
    public Rect getAllowedFloatingBoundsRegion() {
        return this.mFloatingAllowedArea;
    }

    @Override // com.android.wm.shell.common.FloatingContentCoordinator.FloatingContent
    public void moveToBounds(Rect rect) {
        animateToBounds(rect, this.mConflictResolutionSpringConfig);
    }

    /* access modifiers changed from: package-private */
    public void synchronizePinnedStackBounds() {
        cancelPhysicsAnimation();
        this.mPipBoundsState.getMotionBoundsState().onAllAnimationsEnded();
        if (this.mPipTaskOrganizer.isInPip()) {
            this.mFloatingContentCoordinator.onContentMoved(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void movePip(Rect rect) {
        movePip(rect, false);
    }

    /* access modifiers changed from: package-private */
    public void movePip(Rect rect, boolean z) {
        if (!z) {
            this.mFloatingContentCoordinator.onContentMoved(this);
        }
        if (!this.mSpringingToTouch) {
            cancelPhysicsAnimation();
            if (!z) {
                resizePipUnchecked(rect);
                this.mPipBoundsState.setBounds(rect);
                return;
            }
            this.mPipBoundsState.getMotionBoundsState().setBoundsInMotion(rect);
            this.mPipTaskOrganizer.scheduleUserResizePip(getBounds(), rect, new Consumer() { // from class: com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PipMotionHelper.this.lambda$movePip$3((Rect) obj);
                }
            });
            return;
        }
        this.mTemporaryBoundsPhysicsAnimator.spring(FloatProperties.RECT_WIDTH, (float) getBounds().width(), this.mCatchUpSpringConfig).spring(FloatProperties.RECT_HEIGHT, (float) getBounds().height(), this.mCatchUpSpringConfig).spring(FloatProperties.RECT_X, (float) rect.left, this.mCatchUpSpringConfig).spring(FloatProperties.RECT_Y, (float) rect.top, this.mCatchUpSpringConfig);
        startBoundsAnimator((float) rect.left, (float) rect.top);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$movePip$3(Rect rect) {
        this.mMenuController.updateMenuLayout(rect);
    }

    /* access modifiers changed from: package-private */
    public void animateIntoDismissTarget(MagnetizedObject.MagneticTarget magneticTarget, float f, float f2, boolean z, Function0<Unit> function0) {
        PointF centerOnScreen = magneticTarget.getCenterOnScreen();
        float dimensionPixelSize = ((float) this.mContext.getResources().getDimensionPixelSize(R.dimen.dismiss_circle_size)) * 0.85f;
        float width = dimensionPixelSize / (((float) getBounds().width()) / ((float) getBounds().height()));
        float f3 = centerOnScreen.x - (dimensionPixelSize / 2.0f);
        float f4 = centerOnScreen.y - (width / 2.0f);
        if (!this.mPipBoundsState.getMotionBoundsState().isInMotion()) {
            this.mPipBoundsState.getMotionBoundsState().setBoundsInMotion(getBounds());
        }
        this.mTemporaryBoundsPhysicsAnimator.spring(FloatProperties.RECT_X, f3, f, this.mAnimateToDismissSpringConfig).spring(FloatProperties.RECT_Y, f4, f2, this.mAnimateToDismissSpringConfig).spring(FloatProperties.RECT_WIDTH, dimensionPixelSize, this.mAnimateToDismissSpringConfig).spring(FloatProperties.RECT_HEIGHT, width, this.mAnimateToDismissSpringConfig).withEndActions(function0);
        startBoundsAnimator(f3, f4);
    }

    /* access modifiers changed from: package-private */
    public void setSpringingToTouch(boolean z) {
        this.mSpringingToTouch = z;
    }

    /* access modifiers changed from: package-private */
    public void expandLeavePip() {
        expandLeavePip(false);
    }

    /* access modifiers changed from: package-private */
    public void expandLeavePip(boolean z) {
        cancelPhysicsAnimation();
        int i = 0;
        this.mMenuController.hideMenu(0, false);
        PipTaskOrganizer pipTaskOrganizer = this.mPipTaskOrganizer;
        if (!z) {
            i = 300;
        }
        pipTaskOrganizer.exitPip(i);
    }

    @Override // com.android.wm.shell.pip.phone.PipAppOpsListener.Callback
    public void dismissPip() {
        cancelPhysicsAnimation();
        this.mMenuController.hideMenu(2, false);
        this.mPipTaskOrganizer.removePip();
    }

    /* access modifiers changed from: package-private */
    public void onMovementBoundsChanged() {
        rebuildFlingConfigs();
        this.mFloatingAllowedArea.set(this.mPipBoundsState.getMovementBounds());
        this.mFloatingAllowedArea.right += getBounds().width();
        this.mFloatingAllowedArea.bottom += getBounds().height();
    }

    private Rect getBounds() {
        return this.mPipBoundsState.getBounds();
    }

    /* access modifiers changed from: package-private */
    public void flingToSnapTarget(float f, float f2, Runnable runnable) {
        movetoTarget(f, f2, runnable, false);
    }

    /* access modifiers changed from: package-private */
    public void stashToEdge(float f, float f2, Runnable runnable) {
        if (this.mPipBoundsState.getStashedState() == 0) {
            f2 = 0.0f;
        }
        movetoTarget(f, f2, runnable, true);
    }

    private void movetoTarget(float f, float f2, Runnable runnable, boolean z) {
        int i;
        int i2;
        this.mSpringingToTouch = false;
        this.mTemporaryBoundsPhysicsAnimator.spring(FloatProperties.RECT_WIDTH, (float) getBounds().width(), this.mSpringConfig).spring(FloatProperties.RECT_HEIGHT, (float) getBounds().height(), this.mSpringConfig).flingThenSpring(FloatProperties.RECT_X, f, z ? this.mStashConfigX : this.mFlingConfigX, this.mSpringConfig, true).flingThenSpring(FloatProperties.RECT_Y, f2, this.mFlingConfigY, this.mSpringConfig);
        Rect stableInsets = this.mPipBoundsState.getDisplayLayout().stableInsets();
        if (z) {
            i = (this.mPipBoundsState.getStashOffset() - this.mPipBoundsState.getBounds().width()) + stableInsets.left;
        } else {
            i = this.mPipBoundsState.getMovementBounds().left;
        }
        float f3 = (float) i;
        if (z) {
            i2 = (this.mPipBoundsState.getDisplayBounds().right - this.mPipBoundsState.getStashOffset()) - stableInsets.right;
        } else {
            i2 = this.mPipBoundsState.getMovementBounds().right;
        }
        f3 = (float) i2;
        if (f >= 0.0f) {
        }
        startBoundsAnimator(f3, PhysicsAnimator.estimateFlingEndValue((float) this.mPipBoundsState.getMotionBoundsState().getBoundsInMotion().top, f2, this.mFlingConfigY), runnable);
    }

    void animateToBounds(Rect rect, PhysicsAnimator.SpringConfig springConfig) {
        if (!this.mTemporaryBoundsPhysicsAnimator.isRunning()) {
            this.mPipBoundsState.getMotionBoundsState().setBoundsInMotion(getBounds());
        }
        this.mTemporaryBoundsPhysicsAnimator.spring(FloatProperties.RECT_X, (float) rect.left, springConfig).spring(FloatProperties.RECT_Y, (float) rect.top, springConfig);
        startBoundsAnimator((float) rect.left, (float) rect.top);
    }

    /* access modifiers changed from: package-private */
    public void animateDismiss() {
        this.mTemporaryBoundsPhysicsAnimator.spring(FloatProperties.RECT_Y, (float) (this.mPipBoundsState.getMovementBounds().bottom + (getBounds().height() * 2)), 0.0f, this.mSpringConfig).withEndActions(new Runnable() { // from class: com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PipMotionHelper.this.dismissPip();
            }
        });
        startBoundsAnimator((float) getBounds().left, (float) (getBounds().bottom + getBounds().height()));
        this.mDismissalPending = false;
    }

    /* access modifiers changed from: package-private */
    public float animateToExpandedState(Rect rect, Rect rect2, Rect rect3, Runnable runnable) {
        float snapFraction = this.mSnapAlgorithm.getSnapFraction(new Rect(getBounds()), rect2);
        this.mSnapAlgorithm.applySnapFraction(rect, rect3, snapFraction);
        this.mPostPipTransitionCallback = runnable;
        resizeAndAnimatePipUnchecked(rect, 250);
        return snapFraction;
    }

    /* access modifiers changed from: package-private */
    public void animateToUnexpandedState(Rect rect, float f, Rect rect2, Rect rect3, boolean z) {
        if (f < 0.0f) {
            f = this.mSnapAlgorithm.getSnapFraction(new Rect(getBounds()), rect3, this.mPipBoundsState.getStashedState());
        }
        this.mSnapAlgorithm.applySnapFraction(rect, rect2, f, this.mPipBoundsState.getStashedState(), this.mPipBoundsState.getStashOffset(), this.mPipBoundsState.getDisplayBounds(), this.mPipBoundsState.getDisplayLayout().stableInsets());
        if (z) {
            movePip(rect);
        } else {
            resizeAndAnimatePipUnchecked(rect, 250);
        }
    }

    /* access modifiers changed from: package-private */
    public void animateToStashedClosestEdge() {
        int i;
        Rect rect = new Rect();
        Rect stableInsets = this.mPipBoundsState.getDisplayLayout().stableInsets();
        int i2 = this.mPipBoundsState.getBounds().left == this.mPipBoundsState.getMovementBounds().left ? 1 : 2;
        if (i2 == 1) {
            i = (this.mPipBoundsState.getStashOffset() - this.mPipBoundsState.getBounds().width()) + stableInsets.left;
        } else {
            i = (this.mPipBoundsState.getDisplayBounds().right - this.mPipBoundsState.getStashOffset()) - stableInsets.right;
        }
        float f = (float) i;
        rect.set((int) f, this.mPipBoundsState.getBounds().top, (int) (f + ((float) this.mPipBoundsState.getBounds().width())), this.mPipBoundsState.getBounds().bottom);
        resizeAndAnimatePipUnchecked(rect, 250);
        this.mPipBoundsState.setStashed(i2);
    }

    /* access modifiers changed from: package-private */
    public void animateToUnStashedBounds(Rect rect) {
        resizeAndAnimatePipUnchecked(rect, 250);
    }

    /* access modifiers changed from: package-private */
    public void animateToOffset(Rect rect, int i) {
        cancelPhysicsAnimation();
        this.mPipTaskOrganizer.scheduleOffsetPip(rect, i, 300, this.mUpdateBoundsCallback);
    }

    private void cancelPhysicsAnimation() {
        this.mTemporaryBoundsPhysicsAnimator.cancel();
        this.mPipBoundsState.getMotionBoundsState().onPhysicsAnimationEnded();
        this.mSpringingToTouch = false;
    }

    private void rebuildFlingConfigs() {
        this.mFlingConfigX = new PhysicsAnimator.FlingConfig(1.9f, (float) this.mPipBoundsState.getMovementBounds().left, (float) this.mPipBoundsState.getMovementBounds().right);
        this.mFlingConfigY = new PhysicsAnimator.FlingConfig(1.9f, (float) this.mPipBoundsState.getMovementBounds().top, (float) this.mPipBoundsState.getMovementBounds().bottom);
        Rect stableInsets = this.mPipBoundsState.getDisplayLayout().stableInsets();
        this.mStashConfigX = new PhysicsAnimator.FlingConfig(1.9f, (float) ((this.mPipBoundsState.getStashOffset() - this.mPipBoundsState.getBounds().width()) + stableInsets.left), (float) ((this.mPipBoundsState.getDisplayBounds().right - this.mPipBoundsState.getStashOffset()) - stableInsets.right));
    }

    private void startBoundsAnimator(float f, float f2) {
        startBoundsAnimator(f, f2, null);
    }

    private void startBoundsAnimator(float f, float f2, Runnable runnable) {
        if (!this.mSpringingToTouch) {
            cancelPhysicsAnimation();
        }
        int i = (int) f;
        int i2 = (int) f2;
        setAnimatingToBounds(new Rect(i, i2, getBounds().width() + i, getBounds().height() + i2));
        if (!this.mTemporaryBoundsPhysicsAnimator.isRunning()) {
            if (runnable != null) {
                this.mTemporaryBoundsPhysicsAnimator.addUpdateListener(this.mResizePipUpdateListener).withEndActions(new Runnable() { // from class: com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        PipMotionHelper.this.onBoundsPhysicsAnimationEnd();
                    }
                }, runnable);
            } else {
                this.mTemporaryBoundsPhysicsAnimator.addUpdateListener(this.mResizePipUpdateListener).withEndActions(new Runnable() { // from class: com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        PipMotionHelper.this.onBoundsPhysicsAnimationEnd();
                    }
                });
            }
        }
        this.mTemporaryBoundsPhysicsAnimator.start();
    }

    /* access modifiers changed from: package-private */
    public void notifyDismissalPending() {
        this.mDismissalPending = true;
    }

    /* access modifiers changed from: private */
    public void onBoundsPhysicsAnimationEnd() {
        if (!this.mDismissalPending && !this.mSpringingToTouch && !this.mMagnetizedPip.getObjectStuckToTarget()) {
            PipBoundsState pipBoundsState = this.mPipBoundsState;
            pipBoundsState.setBounds(pipBoundsState.getMotionBoundsState().getBoundsInMotion());
            this.mPipBoundsState.getMotionBoundsState().onAllAnimationsEnded();
            if (!this.mDismissalPending) {
                this.mPipTaskOrganizer.scheduleFinishResizePip(getBounds());
            }
        }
        this.mPipBoundsState.getMotionBoundsState().onPhysicsAnimationEnded();
        this.mSpringingToTouch = false;
        this.mDismissalPending = false;
    }

    private void setAnimatingToBounds(Rect rect) {
        this.mPipBoundsState.getMotionBoundsState().setAnimatingToBounds(rect);
        this.mFloatingContentCoordinator.onContentMoved(this);
    }

    private void resizePipUnchecked(Rect rect) {
        if (!rect.equals(getBounds())) {
            this.mPipTaskOrganizer.scheduleResizePip(rect, this.mUpdateBoundsCallback);
        }
    }

    private void resizeAndAnimatePipUnchecked(Rect rect, int i) {
        this.mPipTaskOrganizer.scheduleAnimateResizePip(rect, i, 8, null);
        setAnimatingToBounds(rect);
    }

    /* access modifiers changed from: package-private */
    public MagnetizedObject<Rect> getMagnetizedPip() {
        if (this.mMagnetizedPip == null) {
            this.mMagnetizedPip = new MagnetizedObject<Rect>(this.mContext, this.mPipBoundsState.getMotionBoundsState().getBoundsInMotion(), FloatProperties.RECT_X, FloatProperties.RECT_Y) { // from class: com.android.wm.shell.pip.phone.PipMotionHelper.3
                public float getWidth(Rect rect) {
                    return (float) rect.width();
                }

                public float getHeight(Rect rect) {
                    return (float) rect.height();
                }

                public void getLocationOnScreen(Rect rect, int[] iArr) {
                    iArr[0] = rect.left;
                    iArr[1] = rect.top;
                }
            };
        }
        return this.mMagnetizedPip;
    }
}
