package com.android.wm.shell.splitscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.TransitionInfo;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda2;
import com.android.wm.shell.transition.OneShotRemoteHandler;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class SplitScreenTransitions {
    private SurfaceControl.Transaction mFinishTransaction;
    private final Runnable mOnFinish;
    private final TransactionPool mTransactionPool;
    private final Transitions mTransitions;
    IBinder mPendingDismiss = null;
    IBinder mPendingEnter = null;
    private IBinder mAnimatingTransition = null;
    private OneShotRemoteHandler mRemoteHandler = null;
    private Transitions.TransitionFinishCallback mRemoteFinishCB = new Transitions.TransitionFinishCallback() { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda2
        @Override // com.android.wm.shell.transition.Transitions.TransitionFinishCallback
        public final void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
            SplitScreenTransitions.m586$r8$lambda$5r3nN0aRvuq8i_xsLICb1ldKQY(SplitScreenTransitions.this, windowContainerTransaction, windowContainerTransactionCallback);
        }
    };
    private final ArrayList<Animator> mAnimations = new ArrayList<>();
    private Transitions.TransitionFinishCallback mFinishCallback = null;

    public /* synthetic */ void lambda$new$0(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        if (windowContainerTransaction == null && windowContainerTransactionCallback == null) {
            onFinish();
            return;
        }
        throw new UnsupportedOperationException("finish transactions not supported yet.");
    }

    public SplitScreenTransitions(TransactionPool transactionPool, Transitions transitions, Runnable runnable) {
        this.mTransactionPool = transactionPool;
        this.mTransitions = transitions;
        this.mOnFinish = runnable;
    }

    public void playAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerToken windowContainerToken, WindowContainerToken windowContainerToken2) {
        this.mFinishCallback = transitionFinishCallback;
        this.mAnimatingTransition = iBinder;
        OneShotRemoteHandler oneShotRemoteHandler = this.mRemoteHandler;
        if (oneShotRemoteHandler != null) {
            oneShotRemoteHandler.startAnimation(iBinder, transitionInfo, transaction, this.mRemoteFinishCB);
            this.mRemoteHandler = null;
            return;
        }
        playInternalAnimation(iBinder, transitionInfo, transaction, windowContainerToken, windowContainerToken2);
    }

    private void playInternalAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, WindowContainerToken windowContainerToken, WindowContainerToken windowContainerToken2) {
        this.mFinishTransaction = this.mTransactionPool.acquire();
        for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            SurfaceControl leash = change.getLeash();
            int mode = ((TransitionInfo.Change) transitionInfo.getChanges().get(size)).getMode();
            if (mode == 6) {
                if (change.getParent() != null) {
                    TransitionInfo.Change change2 = transitionInfo.getChange(change.getParent());
                    transaction.show(change2.getLeash());
                    transaction.setAlpha(change2.getLeash(), 1.0f);
                    transaction.reparent(leash, transitionInfo.getRootLeash());
                    transaction.setLayer(leash, transitionInfo.getChanges().size() - size);
                    this.mFinishTransaction.reparent(leash, change2.getLeash());
                    this.mFinishTransaction.setPosition(leash, (float) change.getEndRelOffset().x, (float) change.getEndRelOffset().y);
                }
                Rect rect = new Rect(change.getStartAbsBounds());
                if (transitionInfo.getType() == 11) {
                    rect.offsetTo(change.getEndAbsBounds().left, change.getEndAbsBounds().top);
                }
                Rect rect2 = new Rect(change.getEndAbsBounds());
                rect.offset(-transitionInfo.getRootOffset().x, -transitionInfo.getRootOffset().y);
                rect2.offset(-transitionInfo.getRootOffset().x, -transitionInfo.getRootOffset().y);
                startExampleResizeAnimation(leash, rect, rect2);
            }
            if (change.getParent() == null) {
                if (iBinder == this.mPendingEnter && (windowContainerToken.equals(change.getContainer()) || windowContainerToken2.equals(change.getContainer()))) {
                    transaction.setWindowCrop(leash, change.getStartAbsBounds().width(), change.getStartAbsBounds().height());
                }
                boolean isOpeningType = Transitions.isOpeningType(transitionInfo.getType());
                if (isOpeningType && (mode == 1 || mode == 3)) {
                    startExampleAnimation(leash, true);
                } else if (!isOpeningType && (mode == 2 || mode == 4)) {
                    if (transitionInfo.getType() == 11) {
                        transaction.setAlpha(leash, 0.0f);
                    } else {
                        startExampleAnimation(leash, false);
                    }
                }
            }
        }
        transaction.apply();
        onFinish();
    }

    public IBinder startEnterTransition(int i, WindowContainerTransaction windowContainerTransaction, IRemoteTransition iRemoteTransition, Transitions.TransitionHandler transitionHandler) {
        if (iRemoteTransition != null) {
            this.mRemoteHandler = new OneShotRemoteHandler(this.mTransitions.getMainExecutor(), iRemoteTransition);
        }
        IBinder startTransition = this.mTransitions.startTransition(i, windowContainerTransaction, transitionHandler);
        this.mPendingEnter = startTransition;
        OneShotRemoteHandler oneShotRemoteHandler = this.mRemoteHandler;
        if (oneShotRemoteHandler != null) {
            oneShotRemoteHandler.setTransition(startTransition);
        }
        return startTransition;
    }

    public IBinder startSnapToDismiss(WindowContainerTransaction windowContainerTransaction, Transitions.TransitionHandler transitionHandler) {
        IBinder startTransition = this.mTransitions.startTransition(11, windowContainerTransaction, transitionHandler);
        this.mPendingDismiss = startTransition;
        return startTransition;
    }

    void onFinish() {
        if (this.mAnimations.isEmpty()) {
            this.mOnFinish.run();
            SurfaceControl.Transaction transaction = this.mFinishTransaction;
            if (transaction != null) {
                transaction.apply();
                this.mTransactionPool.release(this.mFinishTransaction);
                this.mFinishTransaction = null;
            }
            this.mFinishCallback.onTransitionFinished(null, null);
            this.mFinishCallback = null;
            IBinder iBinder = this.mAnimatingTransition;
            if (iBinder == this.mPendingEnter) {
                this.mPendingEnter = null;
            }
            if (iBinder == this.mPendingDismiss) {
                this.mPendingDismiss = null;
            }
            this.mAnimatingTransition = null;
        }
    }

    private void startExampleAnimation(SurfaceControl surfaceControl, boolean z) {
        float f = z ? 1.0f : 0.0f;
        float f2 = 1.0f - f;
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(f2, f);
        ofFloat.setDuration(500L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(acquire, surfaceControl, f2, f) { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda0
            public final /* synthetic */ SurfaceControl.Transaction f$0;
            public final /* synthetic */ SurfaceControl f$1;
            public final /* synthetic */ float f$2;
            public final /* synthetic */ float f$3;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SplitScreenTransitions.$r8$lambda$K7f0W5eW3rf4ETbr4KyTb7F86hI(this.f$0, this.f$1, this.f$2, this.f$3, valueAnimator);
            }
        });
        final SplitScreenTransitions$$ExternalSyntheticLambda5 splitScreenTransitions$$ExternalSyntheticLambda5 = new Runnable(acquire, surfaceControl, f, ofFloat) { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda5
            public final /* synthetic */ SurfaceControl.Transaction f$1;
            public final /* synthetic */ SurfaceControl f$2;
            public final /* synthetic */ float f$3;
            public final /* synthetic */ ValueAnimator f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SplitScreenTransitions.$r8$lambda$zBJz70Jb7fWfoaH7HWz3ujB1fA0(SplitScreenTransitions.this, this.f$1, this.f$2, this.f$3, this.f$4);
            }
        };
        ofFloat.addListener(new Animator.AnimatorListener() { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions.1
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                splitScreenTransitions$$ExternalSyntheticLambda5.run();
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                splitScreenTransitions$$ExternalSyntheticLambda5.run();
            }
        });
        this.mAnimations.add(ofFloat);
        this.mTransitions.getAnimExecutor().execute(new LegacySplitScreenTransitions$$ExternalSyntheticLambda2(ofFloat));
    }

    public static /* synthetic */ void lambda$startExampleAnimation$1(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, float f2, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        transaction.setAlpha(surfaceControl, (f * (1.0f - animatedFraction)) + (f2 * animatedFraction));
        transaction.apply();
    }

    public /* synthetic */ void lambda$startExampleAnimation$3(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, ValueAnimator valueAnimator) {
        transaction.setAlpha(surfaceControl, f);
        transaction.apply();
        this.mTransactionPool.release(transaction);
        this.mTransitions.getMainExecutor().execute(new Runnable(valueAnimator) { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda4
            public final /* synthetic */ ValueAnimator f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SplitScreenTransitions.$r8$lambda$ibiAD9R4rhpB8AGK85QVtnxAtcE(SplitScreenTransitions.this, this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$startExampleAnimation$2(ValueAnimator valueAnimator) {
        this.mAnimations.remove(valueAnimator);
        onFinish();
    }

    private void startExampleResizeAnimation(SurfaceControl surfaceControl, Rect rect, Rect rect2) {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(500L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(acquire, surfaceControl, rect, rect2) { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda1
            public final /* synthetic */ SurfaceControl.Transaction f$0;
            public final /* synthetic */ SurfaceControl f$1;
            public final /* synthetic */ Rect f$2;
            public final /* synthetic */ Rect f$3;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SplitScreenTransitions.m588$r8$lambda$s8OMv1JB1mmCAXK4762zGyZjtQ(this.f$0, this.f$1, this.f$2, this.f$3, valueAnimator);
            }
        });
        final SplitScreenTransitions$$ExternalSyntheticLambda6 splitScreenTransitions$$ExternalSyntheticLambda6 = new Runnable(acquire, surfaceControl, rect2, ofFloat) { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda6
            public final /* synthetic */ SurfaceControl.Transaction f$1;
            public final /* synthetic */ SurfaceControl f$2;
            public final /* synthetic */ Rect f$3;
            public final /* synthetic */ ValueAnimator f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SplitScreenTransitions.$r8$lambda$mx94UPIqB_sGNXHQe_UIDVN_YyI(SplitScreenTransitions.this, this.f$1, this.f$2, this.f$3, this.f$4);
            }
        };
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                splitScreenTransitions$$ExternalSyntheticLambda6.run();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                splitScreenTransitions$$ExternalSyntheticLambda6.run();
            }
        });
        this.mAnimations.add(ofFloat);
        this.mTransitions.getAnimExecutor().execute(new LegacySplitScreenTransitions$$ExternalSyntheticLambda2(ofFloat));
    }

    public static /* synthetic */ void lambda$startExampleResizeAnimation$4(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float f = 1.0f - animatedFraction;
        transaction.setWindowCrop(surfaceControl, (int) ((((float) rect.width()) * f) + (((float) rect2.width()) * animatedFraction)), (int) ((((float) rect.height()) * f) + (((float) rect2.height()) * animatedFraction)));
        transaction.setPosition(surfaceControl, (((float) rect.left) * f) + (((float) rect2.left) * animatedFraction), (((float) rect.top) * f) + (((float) rect2.top) * animatedFraction));
        transaction.apply();
    }

    public /* synthetic */ void lambda$startExampleResizeAnimation$6(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, ValueAnimator valueAnimator) {
        transaction.setWindowCrop(surfaceControl, 0, 0);
        transaction.setPosition(surfaceControl, (float) rect.left, (float) rect.top);
        transaction.apply();
        this.mTransactionPool.release(transaction);
        this.mTransitions.getMainExecutor().execute(new Runnable(valueAnimator) { // from class: com.android.wm.shell.splitscreen.SplitScreenTransitions$$ExternalSyntheticLambda3
            public final /* synthetic */ ValueAnimator f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SplitScreenTransitions.m587$r8$lambda$g7vtvqJ5Qux3k4B2RAB7gPJb9A(SplitScreenTransitions.this, this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$startExampleResizeAnimation$5(ValueAnimator valueAnimator) {
        this.mAnimations.remove(valueAnimator);
        onFinish();
    }
}
