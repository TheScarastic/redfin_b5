package com.android.wm.shell.onehanded;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.view.SurfaceControl;
import android.view.animation.BaseInterpolator;
import android.window.WindowContainerToken;
import com.android.wm.shell.onehanded.OneHandedSurfaceTransactionHelper;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes2.dex */
public class OneHandedAnimationController {
    private final HashMap<WindowContainerToken, OneHandedTransitionAnimator> mAnimatorMap = new HashMap<>();
    private final OneHandedInterpolator mInterpolator = new OneHandedInterpolator();
    private final OneHandedSurfaceTransactionHelper mSurfaceTransactionHelper;

    public OneHandedAnimationController(Context context) {
        this.mSurfaceTransactionHelper = new OneHandedSurfaceTransactionHelper(context);
    }

    /* access modifiers changed from: package-private */
    public OneHandedTransitionAnimator getAnimator(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, float f, float f2, Rect rect) {
        OneHandedTransitionAnimator oneHandedTransitionAnimator = this.mAnimatorMap.get(windowContainerToken);
        if (oneHandedTransitionAnimator == null) {
            this.mAnimatorMap.put(windowContainerToken, setupOneHandedTransitionAnimator(OneHandedTransitionAnimator.ofYOffset(windowContainerToken, surfaceControl, f, f2, rect)));
        } else if (oneHandedTransitionAnimator.isRunning()) {
            oneHandedTransitionAnimator.updateEndValue(f2);
        } else {
            oneHandedTransitionAnimator.cancel();
            this.mAnimatorMap.put(windowContainerToken, setupOneHandedTransitionAnimator(OneHandedTransitionAnimator.ofYOffset(windowContainerToken, surfaceControl, f, f2, rect)));
        }
        return this.mAnimatorMap.get(windowContainerToken);
    }

    /* access modifiers changed from: package-private */
    public HashMap<WindowContainerToken, OneHandedTransitionAnimator> getAnimatorMap() {
        return this.mAnimatorMap;
    }

    /* access modifiers changed from: package-private */
    public boolean isAnimatorsConsumed() {
        return this.mAnimatorMap.isEmpty();
    }

    /* access modifiers changed from: package-private */
    public void removeAnimator(WindowContainerToken windowContainerToken) {
        OneHandedTransitionAnimator remove = this.mAnimatorMap.remove(windowContainerToken);
        if (remove != null && remove.isRunning()) {
            remove.cancel();
        }
    }

    OneHandedTransitionAnimator setupOneHandedTransitionAnimator(OneHandedTransitionAnimator oneHandedTransitionAnimator) {
        oneHandedTransitionAnimator.setSurfaceTransactionHelper(this.mSurfaceTransactionHelper);
        oneHandedTransitionAnimator.setInterpolator(this.mInterpolator);
        oneHandedTransitionAnimator.setFloatValues(0.0f, 1.0f);
        return oneHandedTransitionAnimator;
    }

    /* loaded from: classes2.dex */
    public static abstract class OneHandedTransitionAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        private float mCurrentValue;
        private float mEndValue;
        private final SurfaceControl mLeash;
        private final List<OneHandedAnimationCallback> mOneHandedAnimationCallbacks;
        private float mStartValue;
        private OneHandedSurfaceTransactionHelper.SurfaceControlTransactionFactory mSurfaceControlTransactionFactory;
        private OneHandedSurfaceTransactionHelper mSurfaceTransactionHelper;
        private final WindowContainerToken mToken;
        private int mTransitionDirection;

        abstract void applySurfaceControlTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, float f);

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        void onEndTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction) {
        }

        private OneHandedTransitionAnimator(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, float f, float f2) {
            this.mOneHandedAnimationCallbacks = new ArrayList();
            this.mLeash = surfaceControl;
            this.mToken = windowContainerToken;
            this.mStartValue = f;
            this.mEndValue = f2;
            addListener(this);
            addUpdateListener(this);
            this.mSurfaceControlTransactionFactory = OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda0.INSTANCE;
            this.mTransitionDirection = 0;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.mCurrentValue = this.mStartValue;
            this.mOneHandedAnimationCallbacks.forEach(new OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationStart$0(OneHandedAnimationCallback oneHandedAnimationCallback) {
            oneHandedAnimationCallback.onOneHandedAnimationStart(this);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.mCurrentValue = this.mEndValue;
            SurfaceControl.Transaction newSurfaceControlTransaction = newSurfaceControlTransaction();
            onEndTransaction(this.mLeash, newSurfaceControlTransaction);
            this.mOneHandedAnimationCallbacks.forEach(new OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda4(this, newSurfaceControlTransaction));
            this.mOneHandedAnimationCallbacks.clear();
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$1(SurfaceControl.Transaction transaction, OneHandedAnimationCallback oneHandedAnimationCallback) {
            oneHandedAnimationCallback.onOneHandedAnimationEnd(transaction, this);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.mCurrentValue = this.mEndValue;
            this.mOneHandedAnimationCallbacks.forEach(new OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda2(this));
            this.mOneHandedAnimationCallbacks.clear();
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationCancel$2(OneHandedAnimationCallback oneHandedAnimationCallback) {
            oneHandedAnimationCallback.onOneHandedAnimationCancel(this);
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            SurfaceControl.Transaction newSurfaceControlTransaction = newSurfaceControlTransaction();
            this.mOneHandedAnimationCallbacks.forEach(new OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda3(this, newSurfaceControlTransaction));
            applySurfaceControlTransaction(this.mLeash, newSurfaceControlTransaction, valueAnimator.getAnimatedFraction());
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationUpdate$3(SurfaceControl.Transaction transaction, OneHandedAnimationCallback oneHandedAnimationCallback) {
            oneHandedAnimationCallback.onAnimationUpdate(transaction, 0.0f, this.mCurrentValue);
        }

        OneHandedSurfaceTransactionHelper getSurfaceTransactionHelper() {
            return this.mSurfaceTransactionHelper;
        }

        void setSurfaceTransactionHelper(OneHandedSurfaceTransactionHelper oneHandedSurfaceTransactionHelper) {
            this.mSurfaceTransactionHelper = oneHandedSurfaceTransactionHelper;
        }

        /* access modifiers changed from: package-private */
        public OneHandedTransitionAnimator addOneHandedAnimationCallback(OneHandedAnimationCallback oneHandedAnimationCallback) {
            if (oneHandedAnimationCallback != null) {
                this.mOneHandedAnimationCallbacks.add(oneHandedAnimationCallback);
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public WindowContainerToken getToken() {
            return this.mToken;
        }

        /* access modifiers changed from: package-private */
        public float getDestinationOffset() {
            return this.mEndValue - this.mStartValue;
        }

        /* access modifiers changed from: package-private */
        public int getTransitionDirection() {
            return this.mTransitionDirection;
        }

        /* access modifiers changed from: package-private */
        public OneHandedTransitionAnimator setTransitionDirection(int i) {
            this.mTransitionDirection = i;
            return this;
        }

        float getStartValue() {
            return this.mStartValue;
        }

        float getEndValue() {
            return this.mEndValue;
        }

        void setCurrentValue(float f) {
            this.mCurrentValue = f;
        }

        void updateEndValue(float f) {
            this.mEndValue = f;
        }

        SurfaceControl.Transaction newSurfaceControlTransaction() {
            return this.mSurfaceControlTransactionFactory.getTransaction();
        }

        static OneHandedTransitionAnimator ofYOffset(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, float f, float f2, Rect rect) {
            return new OneHandedTransitionAnimator(windowContainerToken, surfaceControl, f, f2, rect) { // from class: com.android.wm.shell.onehanded.OneHandedAnimationController.OneHandedTransitionAnimator.1
                private final Rect mTmpRect;
                final /* synthetic */ Rect val$displayBounds;

                private float getCastedFractionValue(float f3, float f4, float f5) {
                    return (f3 * (1.0f - f5)) + (f4 * f5) + 0.5f;
                }

                {
                    this.val$displayBounds = r11;
                    this.mTmpRect = new Rect(r11);
                }

                @Override // com.android.wm.shell.onehanded.OneHandedAnimationController.OneHandedTransitionAnimator
                void applySurfaceControlTransaction(SurfaceControl surfaceControl2, SurfaceControl.Transaction transaction, float f3) {
                    float castedFractionValue = getCastedFractionValue(getStartValue(), getEndValue(), f3);
                    Rect rect2 = this.mTmpRect;
                    int i = rect2.left;
                    int round = rect2.top + Math.round(castedFractionValue);
                    Rect rect3 = this.mTmpRect;
                    rect2.set(i, round, rect3.right, rect3.bottom + Math.round(castedFractionValue));
                    setCurrentValue(castedFractionValue);
                    getSurfaceTransactionHelper().crop(transaction, surfaceControl2, this.mTmpRect).round(transaction, surfaceControl2).translate(transaction, surfaceControl2, castedFractionValue);
                    transaction.apply();
                }
            };
        }
    }

    /* loaded from: classes2.dex */
    public class OneHandedInterpolator extends BaseInterpolator {
        public OneHandedInterpolator() {
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            return (float) ((Math.pow(2.0d, (double) (-10.0f * f)) * Math.sin((((double) ((f - 4.0f) / 4.0f)) * 6.283185307179586d) / 4.0d)) + 1.0d);
        }
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.println("OneHandedAnimationControllerstates: ");
        printWriter.print("  mAnimatorMap=");
        printWriter.println(this.mAnimatorMap);
        OneHandedSurfaceTransactionHelper oneHandedSurfaceTransactionHelper = this.mSurfaceTransactionHelper;
        if (oneHandedSurfaceTransactionHelper != null) {
            oneHandedSurfaceTransactionHelper.dump(printWriter);
        }
    }
}
