package com.android.wm.shell.legacysplitscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class LegacySplitScreenTransitions implements Transitions.TransitionHandler {
    private SurfaceControl.Transaction mFinishTransaction;
    private final LegacySplitScreenTaskListener mListener;
    private final LegacySplitScreenController mSplitScreen;
    private final TransactionPool mTransactionPool;
    private final Transitions mTransitions;
    private IBinder mPendingDismiss = null;
    private boolean mDismissFromSnap = false;
    private IBinder mPendingEnter = null;
    private IBinder mAnimatingTransition = null;
    private final ArrayList<Animator> mAnimations = new ArrayList<>();
    private Transitions.TransitionFinishCallback mFinishCallback = null;

    /* access modifiers changed from: package-private */
    public LegacySplitScreenTransitions(TransactionPool transactionPool, Transitions transitions, LegacySplitScreenController legacySplitScreenController, LegacySplitScreenTaskListener legacySplitScreenTaskListener) {
        this.mTransactionPool = transactionPool;
        this.mTransitions = transitions;
        this.mSplitScreen = legacySplitScreenController;
        this.mListener = legacySplitScreenTaskListener;
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        ActivityManager.RunningTaskInfo triggerTask = transitionRequestInfo.getTriggerTask();
        int type = transitionRequestInfo.getType();
        if (this.mSplitScreen.isDividerVisible()) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            if (triggerTask == null) {
                return windowContainerTransaction;
            }
            if (!(((type == 2 || type == 4) && triggerTask.parentTaskId == this.mListener.mPrimary.taskId) || ((type == 1 || type == 3) && !triggerTask.supportsMultiWindow))) {
                return windowContainerTransaction;
            }
            WindowManagerProxy.buildDismissSplit(windowContainerTransaction, this.mListener, this.mSplitScreen.getSplitLayout(), true);
            if (type == 1 || type == 3) {
                windowContainerTransaction.reorder(triggerTask.token, true);
            }
            this.mPendingDismiss = iBinder;
            return windowContainerTransaction;
        } else if (triggerTask == null || ((type != 1 && type != 3) || triggerTask.configuration.windowConfiguration.getWindowingMode() != 3)) {
            return null;
        } else {
            WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
            this.mSplitScreen.prepareEnterSplitTransition(windowContainerTransaction2);
            this.mPendingEnter = iBinder;
            return windowContainerTransaction2;
        }
    }

    private void startExampleAnimation(SurfaceControl surfaceControl, boolean z) {
        float f = z ? 1.0f : 0.0f;
        float f2 = 1.0f - f;
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(f2, f);
        ofFloat.setDuration(500L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(acquire, surfaceControl, f2, f) { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda0
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
                LegacySplitScreenTransitions.lambda$startExampleAnimation$0(this.f$0, this.f$1, this.f$2, this.f$3, valueAnimator);
            }
        });
        final LegacySplitScreenTransitions$$ExternalSyntheticLambda5 legacySplitScreenTransitions$$ExternalSyntheticLambda5 = new Runnable(acquire, surfaceControl, f, ofFloat) { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda5
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
                LegacySplitScreenTransitions.this.lambda$startExampleAnimation$2(this.f$1, this.f$2, this.f$3, this.f$4);
            }
        };
        ofFloat.addListener(new Animator.AnimatorListener() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions.1
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                legacySplitScreenTransitions$$ExternalSyntheticLambda5.run();
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                legacySplitScreenTransitions$$ExternalSyntheticLambda5.run();
            }
        });
        this.mAnimations.add(ofFloat);
        this.mTransitions.getAnimExecutor().execute(new LegacySplitScreenTransitions$$ExternalSyntheticLambda2(ofFloat));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$startExampleAnimation$0(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, float f2, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        transaction.setAlpha(surfaceControl, (f * (1.0f - animatedFraction)) + (f2 * animatedFraction));
        transaction.apply();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleAnimation$2(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f, ValueAnimator valueAnimator) {
        transaction.setAlpha(surfaceControl, f);
        transaction.apply();
        this.mTransactionPool.release(transaction);
        this.mTransitions.getMainExecutor().execute(new Runnable(valueAnimator) { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda4
            public final /* synthetic */ ValueAnimator f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                LegacySplitScreenTransitions.this.lambda$startExampleAnimation$1(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleAnimation$1(ValueAnimator valueAnimator) {
        this.mAnimations.remove(valueAnimator);
        onFinish();
    }

    private void startExampleResizeAnimation(SurfaceControl surfaceControl, Rect rect, Rect rect2) {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(500L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(acquire, surfaceControl, rect, rect2) { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda1
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
                LegacySplitScreenTransitions.lambda$startExampleResizeAnimation$3(this.f$0, this.f$1, this.f$2, this.f$3, valueAnimator);
            }
        });
        final LegacySplitScreenTransitions$$ExternalSyntheticLambda6 legacySplitScreenTransitions$$ExternalSyntheticLambda6 = new Runnable(acquire, surfaceControl, rect2, ofFloat) { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda6
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
                LegacySplitScreenTransitions.this.lambda$startExampleResizeAnimation$5(this.f$1, this.f$2, this.f$3, this.f$4);
            }
        };
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                legacySplitScreenTransitions$$ExternalSyntheticLambda6.run();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                legacySplitScreenTransitions$$ExternalSyntheticLambda6.run();
            }
        });
        this.mAnimations.add(ofFloat);
        this.mTransitions.getAnimExecutor().execute(new LegacySplitScreenTransitions$$ExternalSyntheticLambda2(ofFloat));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$startExampleResizeAnimation$3(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float f = 1.0f - animatedFraction;
        transaction.setWindowCrop(surfaceControl, (int) ((((float) rect.width()) * f) + (((float) rect2.width()) * animatedFraction)), (int) ((((float) rect.height()) * f) + (((float) rect2.height()) * animatedFraction)));
        transaction.setPosition(surfaceControl, (((float) rect.left) * f) + (((float) rect2.left) * animatedFraction), (((float) rect.top) * f) + (((float) rect2.top) * animatedFraction));
        transaction.apply();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleResizeAnimation$5(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, ValueAnimator valueAnimator) {
        transaction.setWindowCrop(surfaceControl, 0, 0);
        transaction.setPosition(surfaceControl, (float) rect.left, (float) rect.top);
        transaction.apply();
        this.mTransactionPool.release(transaction);
        this.mTransitions.getMainExecutor().execute(new Runnable(valueAnimator) { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda3
            public final /* synthetic */ ValueAnimator f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                LegacySplitScreenTransitions.this.lambda$startExampleResizeAnimation$4(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startExampleResizeAnimation$4(ValueAnimator valueAnimator) {
        this.mAnimations.remove(valueAnimator);
        onFinish();
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:103:0x01d3 */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:106:0x021a */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r16v0, types: [com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions] */
    /* JADX WARN: Type inference failed for: r7v0 */
    /* JADX WARN: Type inference failed for: r7v1, types: [int] */
    /* JADX WARN: Type inference failed for: r7v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.android.wm.shell.legacysplitscreen.LegacySplitScreenController] */
    /* JADX WARN: Type inference failed for: r7v3 */
    /* JADX WARN: Type inference failed for: r9v7, types: [android.graphics.Rect] */
    /* JADX WARN: Type inference failed for: r7v49 */
    /* JADX WARNING: Unknown variable types count: 2 */
    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean startAnimation(android.os.IBinder r17, android.window.TransitionInfo r18, android.view.SurfaceControl.Transaction r19, com.android.wm.shell.transition.Transitions.TransitionFinishCallback r20) {
        /*
        // Method dump skipped, instructions count: 550
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions.startAnimation(android.os.IBinder, android.window.TransitionInfo, android.view.SurfaceControl$Transaction, com.android.wm.shell.transition.Transitions$TransitionFinishCallback):boolean");
    }

    /* access modifiers changed from: package-private */
    public void dismissSplit(LegacySplitScreenTaskListener legacySplitScreenTaskListener, LegacySplitDisplayLayout legacySplitDisplayLayout, boolean z, boolean z2) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        WindowManagerProxy.buildDismissSplit(windowContainerTransaction, legacySplitScreenTaskListener, legacySplitDisplayLayout, z);
        this.mTransitions.getMainExecutor().execute(new Runnable(z2, windowContainerTransaction) { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda7
            public final /* synthetic */ boolean f$1;
            public final /* synthetic */ WindowContainerTransaction f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                LegacySplitScreenTransitions.this.lambda$dismissSplit$6(this.f$1, this.f$2);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissSplit$6(boolean z, WindowContainerTransaction windowContainerTransaction) {
        this.mDismissFromSnap = z;
        this.mPendingDismiss = this.mTransitions.startTransition(20, windowContainerTransaction, this);
    }

    private void onFinish() {
        if (this.mAnimations.isEmpty()) {
            this.mFinishTransaction.apply();
            this.mTransactionPool.release(this.mFinishTransaction);
            this.mFinishTransaction = null;
            this.mFinishCallback.onTransitionFinished(null, null);
            this.mFinishCallback = null;
            IBinder iBinder = this.mAnimatingTransition;
            if (iBinder == this.mPendingEnter) {
                this.mPendingEnter = null;
            }
            if (iBinder == this.mPendingDismiss) {
                this.mSplitScreen.onDismissSplit();
                this.mPendingDismiss = null;
            }
            this.mDismissFromSnap = false;
            this.mAnimatingTransition = null;
        }
    }
}
