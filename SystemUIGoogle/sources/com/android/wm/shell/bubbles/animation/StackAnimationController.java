package com.android.wm.shell.bubbles.animation;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.bubbles.BadgedImageView;
import com.android.wm.shell.bubbles.BubblePositioner;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import com.google.android.collect.Sets;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.IntSupplier;
/* loaded from: classes2.dex */
public class StackAnimationController extends PhysicsAnimationLayout.PhysicsAnimationController {
    private IntSupplier mBubbleCountSupplier;
    private int mBubbleOffscreen;
    private int mBubblePaddingTop;
    private int mBubbleSize;
    private int mElevation;
    private FloatingContentCoordinator mFloatingContentCoordinator;
    private MagnetizedObject<StackAnimationController> mMagnetizedStack;
    private int mMaxBubbles;
    private Runnable mOnBubbleAnimatedOutAction;
    private Runnable mOnStackAnimationFinished;
    private BubblePositioner mPositioner;
    private float mStackOffset;
    private float mSwapAnimationOffset;
    private final PhysicsAnimator.SpringConfig mAnimateOutSpringConfig = new PhysicsAnimator.SpringConfig(700.0f, 1.0f);
    private PointF mStackPosition = new PointF(-1.0f, -1.0f);
    private Rect mAnimatingToBounds = new Rect();
    private boolean mStackMovedToStartPosition = false;
    private float mImeHeight = 0.0f;
    private float mPreImeY = -1.4E-45f;
    private HashMap<DynamicAnimation.ViewProperty, DynamicAnimation> mStackPositionAnimations = new HashMap<>();
    private boolean mIsMovingFromFlinging = false;
    private boolean mFirstBubbleSpringingToTouch = false;
    private boolean mSpringToTouchOnNextMotionEvent = false;
    private final FloatingContentCoordinator.FloatingContent mStackFloatingContent = new FloatingContentCoordinator.FloatingContent() { // from class: com.android.wm.shell.bubbles.animation.StackAnimationController.1
        private final Rect mFloatingBoundsOnScreen = new Rect();

        @Override // com.android.wm.shell.common.FloatingContentCoordinator.FloatingContent
        public void moveToBounds(Rect rect) {
            StackAnimationController.this.springStack((float) rect.left, (float) rect.top, 700.0f);
        }

        @Override // com.android.wm.shell.common.FloatingContentCoordinator.FloatingContent
        public Rect getAllowedFloatingBoundsRegion() {
            Rect floatingBoundsOnScreen = getFloatingBoundsOnScreen();
            Rect rect = new Rect();
            StackAnimationController.this.getAllowableStackPositionRegion().roundOut(rect);
            rect.right += floatingBoundsOnScreen.width();
            rect.bottom += floatingBoundsOnScreen.height();
            return rect;
        }

        @Override // com.android.wm.shell.common.FloatingContentCoordinator.FloatingContent
        public Rect getFloatingBoundsOnScreen() {
            if (!StackAnimationController.this.mAnimatingToBounds.isEmpty()) {
                return StackAnimationController.this.mAnimatingToBounds;
            }
            if (StackAnimationController.this.mLayout.getChildCount() > 0) {
                this.mFloatingBoundsOnScreen.set((int) StackAnimationController.this.mStackPosition.x, (int) StackAnimationController.this.mStackPosition.y, ((int) StackAnimationController.this.mStackPosition.x) + StackAnimationController.this.mBubbleSize, ((int) StackAnimationController.this.mStackPosition.y) + StackAnimationController.this.mBubbleSize + StackAnimationController.this.mBubblePaddingTop);
            } else {
                this.mFloatingBoundsOnScreen.setEmpty();
            }
            return this.mFloatingBoundsOnScreen;
        }
    };

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    void onChildReordered(View view, int i, int i2) {
    }

    public StackAnimationController(FloatingContentCoordinator floatingContentCoordinator, IntSupplier intSupplier, Runnable runnable, Runnable runnable2, BubblePositioner bubblePositioner) {
        this.mFloatingContentCoordinator = floatingContentCoordinator;
        this.mBubbleCountSupplier = intSupplier;
        this.mOnBubbleAnimatedOutAction = runnable;
        this.mOnStackAnimationFinished = runnable2;
        this.mPositioner = bubblePositioner;
    }

    public void moveFirstBubbleWithStackFollowing(float f, float f2) {
        this.mAnimatingToBounds.setEmpty();
        this.mPreImeY = -1.4E-45f;
        moveFirstBubbleWithStackFollowing(DynamicAnimation.TRANSLATION_X, f);
        moveFirstBubbleWithStackFollowing(DynamicAnimation.TRANSLATION_Y, f2);
        this.mIsMovingFromFlinging = false;
    }

    public PointF getStackPosition() {
        return this.mStackPosition;
    }

    public boolean isStackOnLeftSide() {
        if (this.mLayout == null || !isStackPositionSet() || this.mStackPosition.x + ((float) (this.mBubbleSize / 2)) < ((float) (this.mLayout.getWidth() / 2))) {
            return true;
        }
        return false;
    }

    public void springStack(float f, float f2, float f3) {
        notifyFloatingCoordinatorStackAnimatingTo(f, f2);
        springFirstBubbleWithStackFollowing(DynamicAnimation.TRANSLATION_X, new SpringForce().setStiffness(f3).setDampingRatio(0.85f), 0.0f, f, new Runnable[0]);
        springFirstBubbleWithStackFollowing(DynamicAnimation.TRANSLATION_Y, new SpringForce().setStiffness(f3).setDampingRatio(0.85f), 0.0f, f2, new Runnable[0]);
    }

    public void springStackAfterFling(float f, float f2) {
        springStack(f, f2, 700.0f);
    }

    public float flingStackThenSpringToEdge(float f, float f2, float f3) {
        float f4;
        boolean z = !(((f - ((float) (this.mBubbleSize / 2))) > ((float) (this.mLayout.getWidth() / 2)) ? 1 : ((f - ((float) (this.mBubbleSize / 2))) == ((float) (this.mLayout.getWidth() / 2)) ? 0 : -1)) < 0) ? f2 < -750.0f : f2 < 750.0f;
        RectF allowableStackPositionRegion = getAllowableStackPositionRegion();
        float f5 = z ? allowableStackPositionRegion.left : allowableStackPositionRegion.right;
        PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
        if (!(physicsAnimationLayout == null || physicsAnimationLayout.getChildCount() == 0)) {
            ContentResolver contentResolver = this.mLayout.getContext().getContentResolver();
            float f6 = Settings.Secure.getFloat(contentResolver, "bubble_stiffness", 700.0f);
            float f7 = Settings.Secure.getFloat(contentResolver, "bubble_damping", 0.85f);
            float f8 = Settings.Secure.getFloat(contentResolver, "bubble_friction", 1.9f);
            float f9 = (f5 - f) * 4.2f * f8;
            notifyFloatingCoordinatorStackAnimatingTo(f5, PhysicsAnimator.estimateFlingEndValue(this.mStackPosition.y, f3, new PhysicsAnimator.FlingConfig(f8, allowableStackPositionRegion.top, allowableStackPositionRegion.bottom)));
            if (z) {
                f4 = Math.min(f9, f2);
            } else {
                f4 = Math.max(f9, f2);
            }
            flingThenSpringFirstBubbleWithStackFollowing(DynamicAnimation.TRANSLATION_X, f4, f8, new SpringForce().setStiffness(f6).setDampingRatio(f7), Float.valueOf(f5));
            flingThenSpringFirstBubbleWithStackFollowing(DynamicAnimation.TRANSLATION_Y, f3, f8, new SpringForce().setStiffness(f6).setDampingRatio(f7), null);
            this.mFirstBubbleSpringingToTouch = false;
            this.mIsMovingFromFlinging = true;
        }
        return f5;
    }

    public PointF getStackPositionAlongNearestHorizontalEdge() {
        if (this.mPositioner.showingInTaskbar()) {
            return this.mPositioner.getRestingPosition();
        }
        PointF stackPosition = getStackPosition();
        boolean isFirstChildXLeftOfCenter = this.mLayout.isFirstChildXLeftOfCenter(stackPosition.x);
        RectF allowableStackPositionRegion = getAllowableStackPositionRegion();
        stackPosition.x = isFirstChildXLeftOfCenter ? allowableStackPositionRegion.left : allowableStackPositionRegion.right;
        return stackPosition;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("StackAnimationController state:");
        printWriter.print("  isActive:             ");
        printWriter.println(isActiveController());
        printWriter.print("  restingStackPos:      ");
        printWriter.println(this.mPositioner.getRestingPosition().toString());
        printWriter.print("  currentStackPos:      ");
        printWriter.println(this.mStackPosition.toString());
        printWriter.print("  isMovingFromFlinging: ");
        printWriter.println(this.mIsMovingFromFlinging);
        printWriter.print("  withinDismiss:        ");
        printWriter.println(isStackStuckToTarget());
        printWriter.print("  firstBubbleSpringing: ");
        printWriter.println(this.mFirstBubbleSpringingToTouch);
    }

    protected void flingThenSpringFirstBubbleWithStackFollowing(DynamicAnimation.ViewProperty viewProperty, float f, float f2, SpringForce springForce, Float f3) {
        float f4;
        float f5;
        if (isActiveController()) {
            Log.d("Bubbs.StackCtrl", String.format("Flinging %s.", PhysicsAnimationLayout.getReadablePropertyName(viewProperty)));
            StackPositionProperty stackPositionProperty = new StackPositionProperty(viewProperty);
            float value = stackPositionProperty.getValue(this);
            RectF allowableStackPositionRegion = getAllowableStackPositionRegion();
            DynamicAnimation.ViewProperty viewProperty2 = DynamicAnimation.TRANSLATION_X;
            if (viewProperty.equals(viewProperty2)) {
                f4 = allowableStackPositionRegion.left;
            } else {
                f4 = allowableStackPositionRegion.top;
            }
            if (viewProperty.equals(viewProperty2)) {
                f5 = allowableStackPositionRegion.right;
            } else {
                f5 = allowableStackPositionRegion.bottom;
            }
            FlingAnimation flingAnimation = new FlingAnimation(this, stackPositionProperty);
            flingAnimation.setFriction(f2).setStartVelocity(f).setMinValue(Math.min(value, f4)).setMaxValue(Math.max(value, f5)).addEndListener(new DynamicAnimation.OnAnimationEndListener(viewProperty, springForce, f3, f4, f5) { // from class: com.android.wm.shell.bubbles.animation.StackAnimationController$$ExternalSyntheticLambda0
                public final /* synthetic */ DynamicAnimation.ViewProperty f$1;
                public final /* synthetic */ SpringForce f$2;
                public final /* synthetic */ Float f$3;
                public final /* synthetic */ float f$4;
                public final /* synthetic */ float f$5;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                    this.f$5 = r6;
                }

                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f6, float f7) {
                    StackAnimationController.this.lambda$flingThenSpringFirstBubbleWithStackFollowing$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, dynamicAnimation, z, f6, f7);
                }
            });
            cancelStackPositionAnimation(viewProperty);
            this.mStackPositionAnimations.put(viewProperty, flingAnimation);
            flingAnimation.start();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$flingThenSpringFirstBubbleWithStackFollowing$0(DynamicAnimation.ViewProperty viewProperty, SpringForce springForce, Float f, float f2, float f3, DynamicAnimation dynamicAnimation, boolean z, float f4, float f5) {
        float f6;
        if (!z) {
            this.mPositioner.setRestingPosition(this.mStackPosition);
            if (f != null) {
                f6 = f.floatValue();
            } else {
                f6 = Math.max(f2, Math.min(f3, f4));
            }
            springFirstBubbleWithStackFollowing(viewProperty, springForce, f5, f6, new Runnable[0]);
        }
    }

    public void cancelStackPositionAnimations() {
        DynamicAnimation.ViewProperty viewProperty = DynamicAnimation.TRANSLATION_X;
        cancelStackPositionAnimation(viewProperty);
        DynamicAnimation.ViewProperty viewProperty2 = DynamicAnimation.TRANSLATION_Y;
        cancelStackPositionAnimation(viewProperty2);
        removeEndActionForProperty(viewProperty);
        removeEndActionForProperty(viewProperty2);
    }

    public void setImeHeight(int i) {
        this.mImeHeight = (float) i;
    }

    public float getImeHeight() {
        return this.mImeHeight;
    }

    public float animateForImeVisibility(boolean z) {
        float f = getAllowableStackPositionRegion().bottom;
        if (z) {
            float f2 = this.mStackPosition.y;
            if (f2 > f && this.mPreImeY == -1.4E-45f) {
                this.mPreImeY = f2;
            }
            f = -1.4E-45f;
        } else {
            f = this.mPreImeY;
            if (f != -1.4E-45f) {
                this.mPreImeY = -1.4E-45f;
            }
            f = -1.4E-45f;
        }
        int i = (f > -1.4E-45f ? 1 : (f == -1.4E-45f ? 0 : -1));
        if (i != 0) {
            DynamicAnimation.ViewProperty viewProperty = DynamicAnimation.TRANSLATION_Y;
            springFirstBubbleWithStackFollowing(viewProperty, getSpringForce(viewProperty, null).setStiffness(200.0f), 0.0f, f, new Runnable[0]);
            notifyFloatingCoordinatorStackAnimatingTo(this.mStackPosition.x, f);
        }
        return i != 0 ? f : this.mStackPosition.y;
    }

    private void notifyFloatingCoordinatorStackAnimatingTo(float f, float f2) {
        Rect floatingBoundsOnScreen = this.mStackFloatingContent.getFloatingBoundsOnScreen();
        floatingBoundsOnScreen.offsetTo((int) f, (int) f2);
        this.mAnimatingToBounds = floatingBoundsOnScreen;
        this.mFloatingContentCoordinator.onContentMoved(this.mStackFloatingContent);
    }

    public RectF getAllowableStackPositionRegion() {
        RectF rectF = new RectF(this.mPositioner.getAvailableRect());
        float f = rectF.left;
        int i = this.mBubbleOffscreen;
        rectF.left = f - ((float) i);
        float f2 = rectF.top;
        int i2 = this.mBubblePaddingTop;
        rectF.top = f2 + ((float) i2);
        float f3 = rectF.right;
        int i3 = this.mBubbleSize;
        rectF.right = f3 + ((float) (i - i3));
        float f4 = rectF.bottom;
        float f5 = (float) (i3 + i2);
        float f6 = this.mImeHeight;
        rectF.bottom = f4 - (f5 + (f6 != -1.4E-45f ? f6 + ((float) i2) : 0.0f));
        return rectF;
    }

    public void moveStackFromTouch(float f, float f2) {
        if (this.mSpringToTouchOnNextMotionEvent) {
            springStack(f, f2, 12000.0f);
            this.mSpringToTouchOnNextMotionEvent = false;
            this.mFirstBubbleSpringingToTouch = true;
        } else if (this.mFirstBubbleSpringingToTouch) {
            SpringAnimation springAnimation = (SpringAnimation) this.mStackPositionAnimations.get(DynamicAnimation.TRANSLATION_X);
            SpringAnimation springAnimation2 = (SpringAnimation) this.mStackPositionAnimations.get(DynamicAnimation.TRANSLATION_Y);
            if (springAnimation.isRunning() || springAnimation2.isRunning()) {
                springAnimation.animateToFinalPosition(f);
                springAnimation2.animateToFinalPosition(f2);
            } else {
                this.mFirstBubbleSpringingToTouch = false;
            }
        }
        if (!this.mFirstBubbleSpringingToTouch && !isStackStuckToTarget()) {
            moveFirstBubbleWithStackFollowing(f, f2);
        }
    }

    public void onUnstuckFromTarget() {
        this.mSpringToTouchOnNextMotionEvent = true;
    }

    public void animateStackDismissal(float f, Runnable runnable) {
        animationsForChildrenFromIndex(0, new PhysicsAnimationLayout.PhysicsAnimationController.ChildAnimationConfigurator(f) { // from class: com.android.wm.shell.bubbles.animation.StackAnimationController$$ExternalSyntheticLambda2
            public final /* synthetic */ float f$1;

            {
                this.f$1 = r2;
            }

            @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController.ChildAnimationConfigurator
            public final void configureAnimationForChildAtIndex(int i, PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator) {
                StackAnimationController.this.lambda$animateStackDismissal$1(this.f$1, i, physicsPropertyAnimator);
            }
        }).startAll(runnable);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateStackDismissal$1(float f, int i, PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator) {
        physicsPropertyAnimator.scaleX(0.0f, new Runnable[0]).scaleY(0.0f, new Runnable[0]).alpha(0.0f, new Runnable[0]).translationY(this.mLayout.getChildAt(i).getTranslationY() + f, new Runnable[0]).withStiffness(10000.0f);
    }

    protected void springFirstBubbleWithStackFollowing(DynamicAnimation.ViewProperty viewProperty, SpringForce springForce, float f, float f2, Runnable... runnableArr) {
        if (this.mLayout.getChildCount() != 0 && isActiveController()) {
            Log.d("Bubbs.StackCtrl", String.format("Springing %s to final position %f.", PhysicsAnimationLayout.getReadablePropertyName(viewProperty), Float.valueOf(f2)));
            SpringAnimation startVelocity = new SpringAnimation(this, new StackPositionProperty(viewProperty)).setSpring(springForce).addEndListener(new DynamicAnimation.OnAnimationEndListener(this.mSpringToTouchOnNextMotionEvent, runnableArr) { // from class: com.android.wm.shell.bubbles.animation.StackAnimationController$$ExternalSyntheticLambda1
                public final /* synthetic */ boolean f$1;
                public final /* synthetic */ Runnable[] f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f3, float f4) {
                    StackAnimationController.this.lambda$springFirstBubbleWithStackFollowing$2(this.f$1, this.f$2, dynamicAnimation, z, f3, f4);
                }
            }).setStartVelocity(f);
            cancelStackPositionAnimation(viewProperty);
            this.mStackPositionAnimations.put(viewProperty, startVelocity);
            startVelocity.animateToFinalPosition(f2);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$springFirstBubbleWithStackFollowing$2(boolean z, Runnable[] runnableArr, DynamicAnimation dynamicAnimation, boolean z2, float f, float f2) {
        if (!z) {
            this.mPositioner.setRestingPosition(this.mStackPosition);
        }
        Runnable runnable = this.mOnStackAnimationFinished;
        if (runnable != null) {
            runnable.run();
        }
        if (runnableArr != null) {
            for (Runnable runnable2 : runnableArr) {
                runnable2.run();
            }
        }
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    Set<DynamicAnimation.ViewProperty> getAnimatedProperties() {
        return Sets.newHashSet(new DynamicAnimation.ViewProperty[]{DynamicAnimation.TRANSLATION_X, DynamicAnimation.TRANSLATION_Y, DynamicAnimation.ALPHA, DynamicAnimation.SCALE_X, DynamicAnimation.SCALE_Y});
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    int getNextAnimationInChain(DynamicAnimation.ViewProperty viewProperty, int i) {
        if (viewProperty.equals(DynamicAnimation.TRANSLATION_X) || viewProperty.equals(DynamicAnimation.TRANSLATION_Y)) {
            return i + 1;
        }
        return -1;
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    float getOffsetForChainedPropertyAnimation(DynamicAnimation.ViewProperty viewProperty, int i) {
        if (!viewProperty.equals(DynamicAnimation.TRANSLATION_Y) || isStackStuckToTarget() || i > 1) {
            return 0.0f;
        }
        return this.mStackOffset;
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    SpringForce getSpringForce(DynamicAnimation.ViewProperty viewProperty, View view) {
        return new SpringForce().setDampingRatio(Settings.Secure.getFloat(this.mLayout.getContext().getContentResolver(), "bubble_damping", 0.9f)).setStiffness(800.0f);
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    void onChildAdded(View view, int i) {
        if (!isStackStuckToTarget()) {
            if (getBubbleCount() == 1) {
                moveStackToStartPosition();
            } else if (isStackPositionSet() && this.mLayout.indexOfChild(view) == 0) {
                animateInBubble(view, i);
            }
        }
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    void onChildRemoved(View view, int i, Runnable runnable) {
        PhysicsAnimator.getInstance(view).spring(DynamicAnimation.ALPHA, 0.0f).spring(DynamicAnimation.SCALE_X, 0.0f, this.mAnimateOutSpringConfig).spring(DynamicAnimation.SCALE_Y, 0.0f, this.mAnimateOutSpringConfig).withEndActions(runnable, this.mOnBubbleAnimatedOutAction).start();
        if (getBubbleCount() > 0) {
            animationForChildAtIndex(0).translationX(this.mStackPosition.x, new Runnable[0]).start(new Runnable[0]);
            return;
        }
        BubblePositioner bubblePositioner = this.mPositioner;
        bubblePositioner.setRestingPosition(bubblePositioner.getRestingPosition());
        this.mFloatingContentCoordinator.onContentRemoved(this.mStackFloatingContent);
    }

    public void animateReorder(List<View> list, Runnable runnable) {
        StackAnimationController$$ExternalSyntheticLambda7 stackAnimationController$$ExternalSyntheticLambda7 = new Runnable(list) { // from class: com.android.wm.shell.bubbles.animation.StackAnimationController$$ExternalSyntheticLambda7
            public final /* synthetic */ List f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                StackAnimationController.this.lambda$animateReorder$3(this.f$1);
            }
        };
        for (int i = 0; i < list.size(); i++) {
            View view = list.get(i);
            animateSwap(view, this.mLayout.indexOfChild(view), i, stackAnimationController$$ExternalSyntheticLambda7, runnable);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateReorder$3(List list) {
        for (int i = 0; i < list.size(); i++) {
            updateBadgesAndZOrder((View) list.get(i), i);
        }
    }

    private void animateSwap(View view, int i, int i2, Runnable runnable, Runnable runnable2) {
        if (i2 == i) {
            updateBadgesAndZOrder(view, i2);
            if (i2 == 0) {
                animateInBubble(view, i2);
            } else {
                moveToFinalIndex(view, i2, runnable2);
            }
        } else if (i2 == 0) {
            animateToFrontThenUpdateIcons(view, runnable, runnable2);
        } else {
            moveToFinalIndex(view, i2, runnable2);
        }
    }

    private void animateToFrontThenUpdateIcons(View view, Runnable runnable, Runnable runnable2) {
        view.setTag(R.id.reorder_animator_tag, view.animate().translationY(getStackPosition().y - this.mSwapAnimationOffset).setDuration(300).withEndAction(new Runnable(runnable, view, runnable2) { // from class: com.android.wm.shell.bubbles.animation.StackAnimationController$$ExternalSyntheticLambda6
            public final /* synthetic */ Runnable f$1;
            public final /* synthetic */ View f$2;
            public final /* synthetic */ Runnable f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                StackAnimationController.this.lambda$animateToFrontThenUpdateIcons$4(this.f$1, this.f$2, this.f$3);
            }
        }));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateToFrontThenUpdateIcons$4(Runnable runnable, View view, Runnable runnable2) {
        runnable.run();
        moveToFinalIndex(view, 0, runnable2);
    }

    private void moveToFinalIndex(View view, int i, Runnable runnable) {
        view.setTag(R.id.reorder_animator_tag, view.animate().translationY(getStackPosition().y + (((float) Math.min(i, 1)) * this.mStackOffset)).setDuration(300).withEndAction(new Runnable(view, runnable) { // from class: com.android.wm.shell.bubbles.animation.StackAnimationController$$ExternalSyntheticLambda4
            public final /* synthetic */ View f$0;
            public final /* synthetic */ Runnable f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                StackAnimationController.lambda$moveToFinalIndex$5(this.f$0, this.f$1);
            }
        }));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$moveToFinalIndex$5(View view, Runnable runnable) {
        view.setTag(R.id.reorder_animator_tag, null);
        runnable.run();
    }

    private void updateBadgesAndZOrder(View view, int i) {
        view.setZ(i < 2 ? (float) ((this.mMaxBubbles * this.mElevation) - i) : 0.0f);
        BadgedImageView badgedImageView = (BadgedImageView) view;
        if (i == 0) {
            badgedImageView.showDotAndBadge(!isStackOnLeftSide());
        } else {
            badgedImageView.hideDotAndBadge(!isStackOnLeftSide());
        }
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController
    void onActiveControllerForLayout(PhysicsAnimationLayout physicsAnimationLayout) {
        Resources resources = physicsAnimationLayout.getResources();
        this.mStackOffset = (float) resources.getDimensionPixelSize(R.dimen.bubble_stack_offset);
        this.mSwapAnimationOffset = (float) resources.getDimensionPixelSize(R.dimen.bubble_swap_animation_offset);
        this.mMaxBubbles = resources.getInteger(R.integer.bubbles_max_rendered);
        this.mElevation = resources.getDimensionPixelSize(R.dimen.bubble_elevation);
        this.mBubbleSize = this.mPositioner.getBubbleSize();
        this.mBubblePaddingTop = resources.getDimensionPixelSize(R.dimen.bubble_padding_top);
        this.mBubbleOffscreen = resources.getDimensionPixelSize(R.dimen.bubble_stack_offscreen);
    }

    public void updateResources() {
        PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
        if (physicsAnimationLayout != null) {
            this.mBubblePaddingTop = physicsAnimationLayout.getContext().getResources().getDimensionPixelSize(R.dimen.bubble_padding_top);
        }
    }

    private boolean isStackStuckToTarget() {
        MagnetizedObject<StackAnimationController> magnetizedObject = this.mMagnetizedStack;
        return magnetizedObject != null && magnetizedObject.getObjectStuckToTarget();
    }

    private void moveStackToStartPosition() {
        this.mLayout.setVisibility(4);
        this.mLayout.post(new Runnable() { // from class: com.android.wm.shell.bubbles.animation.StackAnimationController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                StackAnimationController.this.lambda$moveStackToStartPosition$6();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$moveStackToStartPosition$6() {
        setStackPosition(this.mPositioner.getRestingPosition());
        this.mStackMovedToStartPosition = true;
        this.mLayout.setVisibility(0);
        if (this.mLayout.getChildCount() > 0) {
            this.mFloatingContentCoordinator.onContentAdded(this.mStackFloatingContent);
            animateInBubble(this.mLayout.getChildAt(0), 0);
        }
    }

    /* access modifiers changed from: private */
    public void moveFirstBubbleWithStackFollowing(DynamicAnimation.ViewProperty viewProperty, float f) {
        if (viewProperty.equals(DynamicAnimation.TRANSLATION_X)) {
            this.mStackPosition.x = f;
        } else if (viewProperty.equals(DynamicAnimation.TRANSLATION_Y)) {
            this.mStackPosition.y = f;
        }
        if (this.mLayout.getChildCount() > 0) {
            viewProperty.setValue(this.mLayout.getChildAt(0), f);
            if (this.mLayout.getChildCount() > 1) {
                animationForChildAtIndex(1).property(viewProperty, f + getOffsetForChainedPropertyAnimation(viewProperty, 0), new Runnable[0]).start(new Runnable[0]);
            }
        }
    }

    public void setStackPosition(PointF pointF) {
        Log.d("Bubbs.StackCtrl", String.format("Setting position to (%f, %f).", Float.valueOf(pointF.x), Float.valueOf(pointF.y)));
        this.mStackPosition.set(pointF.x, pointF.y);
        this.mPositioner.setRestingPosition(this.mStackPosition);
        if (isActiveController()) {
            PhysicsAnimationLayout physicsAnimationLayout = this.mLayout;
            DynamicAnimation.ViewProperty viewProperty = DynamicAnimation.TRANSLATION_X;
            DynamicAnimation.ViewProperty viewProperty2 = DynamicAnimation.TRANSLATION_Y;
            physicsAnimationLayout.cancelAllAnimationsOfProperties(viewProperty, viewProperty2);
            cancelStackPositionAnimations();
            float offsetForChainedPropertyAnimation = getOffsetForChainedPropertyAnimation(viewProperty, 0);
            float offsetForChainedPropertyAnimation2 = getOffsetForChainedPropertyAnimation(viewProperty2, 0);
            for (int i = 0; i < this.mLayout.getChildCount(); i++) {
                float min = (float) Math.min(i, 1);
                this.mLayout.getChildAt(i).setTranslationX(pointF.x + (min * offsetForChainedPropertyAnimation));
                this.mLayout.getChildAt(i).setTranslationY(pointF.y + (min * offsetForChainedPropertyAnimation2));
            }
        }
    }

    public void setStackPosition(BubbleStackView.RelativeStackPosition relativeStackPosition) {
        setStackPosition(relativeStackPosition.getAbsolutePositionInRegion(getAllowableStackPositionRegion()));
    }

    private boolean isStackPositionSet() {
        return this.mStackMovedToStartPosition;
    }

    private void animateInBubble(View view, int i) {
        if (isActiveController()) {
            float offsetForChainedPropertyAnimation = getOffsetForChainedPropertyAnimation(DynamicAnimation.TRANSLATION_Y, 0);
            PointF pointF = this.mStackPosition;
            float f = pointF.y + (offsetForChainedPropertyAnimation * ((float) i));
            float f2 = pointF.x;
            if (this.mPositioner.showBubblesVertically()) {
                view.setTranslationY(f);
                view.setTranslationX(isStackOnLeftSide() ? f2 - 100.0f : f2 + 100.0f);
            } else {
                view.setTranslationX(this.mStackPosition.x);
                view.setTranslationY(100.0f + f);
            }
            view.setScaleX(0.5f);
            view.setScaleY(0.5f);
            view.setAlpha(0.0f);
            ViewPropertyAnimator withEndAction = view.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(300).withEndAction(new Runnable(view) { // from class: com.android.wm.shell.bubbles.animation.StackAnimationController$$ExternalSyntheticLambda3
                public final /* synthetic */ View f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    StackAnimationController.lambda$animateInBubble$7(this.f$0);
                }
            });
            view.setTag(R.id.reorder_animator_tag, withEndAction);
            if (this.mPositioner.showBubblesVertically()) {
                withEndAction.translationX(f2);
            } else {
                withEndAction.translationY(f);
            }
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$animateInBubble$7(View view) {
        view.setTag(R.id.reorder_animator_tag, null);
    }

    private void cancelStackPositionAnimation(DynamicAnimation.ViewProperty viewProperty) {
        if (this.mStackPositionAnimations.containsKey(viewProperty)) {
            this.mStackPositionAnimations.get(viewProperty).cancel();
        }
    }

    public MagnetizedObject<StackAnimationController> getMagnetizedStack(MagnetizedObject.MagneticTarget magneticTarget) {
        if (this.mMagnetizedStack == null) {
            AnonymousClass2 r0 = new MagnetizedObject<StackAnimationController>(this.mLayout.getContext(), this, new StackPositionProperty(DynamicAnimation.TRANSLATION_X), new StackPositionProperty(DynamicAnimation.TRANSLATION_Y)) { // from class: com.android.wm.shell.bubbles.animation.StackAnimationController.2
                public float getWidth(StackAnimationController stackAnimationController) {
                    return (float) StackAnimationController.this.mBubbleSize;
                }

                public float getHeight(StackAnimationController stackAnimationController) {
                    return (float) StackAnimationController.this.mBubbleSize;
                }

                public void getLocationOnScreen(StackAnimationController stackAnimationController, int[] iArr) {
                    iArr[0] = (int) StackAnimationController.this.mStackPosition.x;
                    iArr[1] = (int) StackAnimationController.this.mStackPosition.y;
                }
            };
            this.mMagnetizedStack = r0;
            r0.addTarget(magneticTarget);
            this.mMagnetizedStack.setHapticsEnabled(true);
            this.mMagnetizedStack.setFlingToTargetMinVelocity(4000.0f);
        }
        ContentResolver contentResolver = this.mLayout.getContext().getContentResolver();
        float f = Settings.Secure.getFloat(contentResolver, "bubble_dismiss_fling_min_velocity", this.mMagnetizedStack.getFlingToTargetMinVelocity());
        float f2 = Settings.Secure.getFloat(contentResolver, "bubble_dismiss_stick_max_velocity", this.mMagnetizedStack.getStickToTargetMaxXVelocity());
        float f3 = Settings.Secure.getFloat(contentResolver, "bubble_dismiss_target_width_percent", this.mMagnetizedStack.getFlingToTargetWidthPercent());
        this.mMagnetizedStack.setFlingToTargetMinVelocity(f);
        this.mMagnetizedStack.setStickToTargetMaxXVelocity(f2);
        this.mMagnetizedStack.setFlingToTargetWidthPercent(f3);
        return this.mMagnetizedStack;
    }

    private int getBubbleCount() {
        return this.mBubbleCountSupplier.getAsInt();
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class StackPositionProperty extends FloatPropertyCompat<StackAnimationController> {
        private final DynamicAnimation.ViewProperty mProperty;

        private StackPositionProperty(DynamicAnimation.ViewProperty viewProperty) {
            super(viewProperty.toString());
            this.mProperty = viewProperty;
        }

        public float getValue(StackAnimationController stackAnimationController) {
            if (StackAnimationController.this.mLayout.getChildCount() > 0) {
                return this.mProperty.getValue(StackAnimationController.this.mLayout.getChildAt(0));
            }
            return 0.0f;
        }

        public void setValue(StackAnimationController stackAnimationController, float f) {
            StackAnimationController.this.moveFirstBubbleWithStackFollowing(this.mProperty, f);
        }
    }
}
