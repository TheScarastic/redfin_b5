package com.android.wm.shell.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.ArrayMap;
import android.view.Choreographer;
import android.view.SurfaceControl;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.internal.policy.AttributeCache;
import com.android.internal.policy.TransitionAnimation;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda2;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class DefaultTransitionHandler implements Transitions.TransitionHandler {
    private final ShellExecutor mAnimExecutor;
    private final ShellExecutor mMainExecutor;
    private final TransactionPool mTransactionPool;
    private final TransitionAnimation mTransitionAnimation;
    private final ArrayMap<IBinder, ArrayList<Animator>> mAnimations = new ArrayMap<>();
    private final Rect mInsets = new Rect(0, 0, 0, 0);
    private float mTransitionAnimationScaleSetting = 1.0f;

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        return null;
    }

    /* access modifiers changed from: package-private */
    public DefaultTransitionHandler(TransactionPool transactionPool, Context context, ShellExecutor shellExecutor, ShellExecutor shellExecutor2) {
        this.mTransactionPool = transactionPool;
        this.mMainExecutor = shellExecutor;
        this.mAnimExecutor = shellExecutor2;
        this.mTransitionAnimation = new TransitionAnimation(context, false, "ShellTransitions");
        AttributeCache.init(context);
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, Transitions.TransitionFinishCallback transitionFinishCallback) {
        Animation loadAnimation;
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -146110597, 0, "start default transition animation, info = %s", String.valueOf(transitionInfo));
        }
        if (!this.mAnimations.containsKey(iBinder)) {
            ArrayList<Animator> arrayList = new ArrayList<>();
            this.mAnimations.put(iBinder, arrayList);
            DefaultTransitionHandler$$ExternalSyntheticLambda2 defaultTransitionHandler$$ExternalSyntheticLambda2 = new Runnable(arrayList, iBinder, transitionFinishCallback) { // from class: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda2
                public final /* synthetic */ ArrayList f$1;
                public final /* synthetic */ IBinder f$2;
                public final /* synthetic */ Transitions.TransitionFinishCallback f$3;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    DefaultTransitionHandler.m606$r8$lambda$42XeKNKaf8yBeFAl_GmLtqm558(DefaultTransitionHandler.this, this.f$1, this.f$2, this.f$3);
                }
            };
            for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
                TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
                if (change.getMode() == 6) {
                    transaction.setPosition(change.getLeash(), (float) (change.getEndAbsBounds().left - change.getEndRelOffset().x), (float) (change.getEndAbsBounds().top - change.getEndRelOffset().y));
                    if (change.getTaskInfo() != null) {
                        transaction.setWindowCrop(change.getLeash(), change.getEndAbsBounds().width(), change.getEndAbsBounds().height());
                    }
                }
                if (TransitionInfo.isIndependent(change, transitionInfo) && (loadAnimation = loadAnimation(transitionInfo.getType(), transitionInfo.getFlags(), change)) != null) {
                    startAnimInternal(arrayList, loadAnimation, change.getLeash(), defaultTransitionHandler$$ExternalSyntheticLambda2);
                }
            }
            transaction.apply();
            defaultTransitionHandler$$ExternalSyntheticLambda2.run();
            return true;
        }
        throw new IllegalStateException("Got a duplicate startAnimation call for " + iBinder);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$0(ArrayList arrayList, IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback) {
        if (arrayList.isEmpty()) {
            this.mAnimations.remove(iBinder);
            transitionFinishCallback.onTransitionFinished(null, null);
        }
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public void setAnimScaleSetting(float f) {
        this.mTransitionAnimationScaleSetting = f;
    }

    private Animation loadAnimation(int i, int i2, TransitionInfo.Change change) {
        boolean isOpeningType = Transitions.isOpeningType(i);
        int mode = change.getMode();
        int flags = change.getFlags();
        Animation animation = null;
        if (i == 5) {
            animation = this.mTransitionAnimation.createRelaunchAnimation(change.getStartAbsBounds(), this.mInsets, change.getEndAbsBounds());
        } else {
            boolean z = false;
            if (i == 7) {
                TransitionAnimation transitionAnimation = this.mTransitionAnimation;
                if ((flags & 1) != 0) {
                    z = true;
                }
                animation = transitionAnimation.loadKeyguardExitAnimation(i2, z);
            } else if (i == 9) {
                animation = this.mTransitionAnimation.loadKeyguardUnoccludeAnimation();
            } else if (mode != 1 || !isOpeningType) {
                if (mode != 3 || !isOpeningType) {
                    if (mode != 2 || isOpeningType) {
                        if (mode == 4 && !isOpeningType) {
                            animation = (flags & 16) != 0 ? this.mTransitionAnimation.loadVoiceActivityExitAnimation(false) : this.mTransitionAnimation.loadDefaultAnimationAttr(15);
                        } else if (mode == 6) {
                            animation = new AlphaAnimation(1.0f, 1.0f);
                            animation.setDuration(336);
                        }
                    } else if ((flags & 16) != 0) {
                        animation = this.mTransitionAnimation.loadVoiceActivityExitAnimation(false);
                    } else if (change.getTaskInfo() != null) {
                        animation = this.mTransitionAnimation.loadDefaultAnimationAttr(11);
                    } else {
                        animation = this.mTransitionAnimation.loadDefaultAnimationRes((4 & flags) == 0 ? 17432590 : 17432593);
                    }
                } else if ((flags & 8) != 0) {
                    return null;
                } else {
                    animation = (flags & 16) != 0 ? this.mTransitionAnimation.loadVoiceActivityOpenAnimation(true) : this.mTransitionAnimation.loadDefaultAnimationAttr(12);
                }
            } else if ((flags & 8) != 0) {
                return null;
            } else {
                if ((flags & 16) != 0) {
                    animation = this.mTransitionAnimation.loadVoiceActivityOpenAnimation(true);
                } else if (change.getTaskInfo() != null) {
                    animation = this.mTransitionAnimation.loadDefaultAnimationAttr(8);
                } else {
                    animation = this.mTransitionAnimation.loadDefaultAnimationRes((4 & flags) == 0 ? 17432591 : 17432594);
                }
            }
        }
        if (animation != null) {
            Rect startAbsBounds = change.getStartAbsBounds();
            Rect endAbsBounds = change.getEndAbsBounds();
            animation.restrictDuration(3000);
            animation.initialize(endAbsBounds.width(), endAbsBounds.height(), startAbsBounds.width(), startAbsBounds.height());
            animation.scaleCurrentDuration(this.mTransitionAnimationScaleSetting);
        }
        return animation;
    }

    private void startAnimInternal(ArrayList<Animator> arrayList, Animation animation, SurfaceControl surfaceControl, Runnable runnable) {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        Transformation transformation = new Transformation();
        float[] fArr = new float[9];
        ofFloat.overrideDurationScale(1.0f);
        ofFloat.setDuration(animation.computeDurationHint());
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(ofFloat, acquire, surfaceControl, animation, transformation, fArr) { // from class: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda0
            public final /* synthetic */ ValueAnimator f$0;
            public final /* synthetic */ SurfaceControl.Transaction f$1;
            public final /* synthetic */ SurfaceControl f$2;
            public final /* synthetic */ Animation f$3;
            public final /* synthetic */ Transformation f$4;
            public final /* synthetic */ float[] f$5;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DefaultTransitionHandler.$r8$lambda$LcEA68FEy6IXYeeukiPl3fAus5o(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, valueAnimator);
            }
        });
        final DefaultTransitionHandler$$ExternalSyntheticLambda1 defaultTransitionHandler$$ExternalSyntheticLambda1 = new Runnable(ofFloat, acquire, surfaceControl, animation, transformation, fArr, arrayList, runnable) { // from class: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda1
            public final /* synthetic */ ValueAnimator f$1;
            public final /* synthetic */ SurfaceControl.Transaction f$2;
            public final /* synthetic */ SurfaceControl f$3;
            public final /* synthetic */ Animation f$4;
            public final /* synthetic */ Transformation f$5;
            public final /* synthetic */ float[] f$6;
            public final /* synthetic */ ArrayList f$7;
            public final /* synthetic */ Runnable f$8;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
                this.f$6 = r7;
                this.f$7 = r8;
                this.f$8 = r9;
            }

            @Override // java.lang.Runnable
            public final void run() {
                DefaultTransitionHandler.$r8$lambda$8HQf9g19RRo5X1pEa0UlVszX2_E(DefaultTransitionHandler.this, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8);
            }
        };
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.transition.DefaultTransitionHandler.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                defaultTransitionHandler$$ExternalSyntheticLambda1.run();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                defaultTransitionHandler$$ExternalSyntheticLambda1.run();
            }
        });
        arrayList.add(ofFloat);
        this.mAnimExecutor.execute(new LegacySplitScreenTransitions$$ExternalSyntheticLambda2(ofFloat));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$startAnimInternal$1(ValueAnimator valueAnimator, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Animation animation, Transformation transformation, float[] fArr, ValueAnimator valueAnimator2) {
        applyTransformation(Math.min(valueAnimator.getDuration(), valueAnimator.getCurrentPlayTime()), transaction, surfaceControl, animation, transformation, fArr);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimInternal$3(ValueAnimator valueAnimator, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Animation animation, Transformation transformation, float[] fArr, ArrayList arrayList, Runnable runnable) {
        applyTransformation(valueAnimator.getDuration(), transaction, surfaceControl, animation, transformation, fArr);
        this.mTransactionPool.release(transaction);
        this.mMainExecutor.execute(new Runnable(arrayList, valueAnimator, runnable) { // from class: com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda3
            public final /* synthetic */ ArrayList f$0;
            public final /* synthetic */ ValueAnimator f$1;
            public final /* synthetic */ Runnable f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                DefaultTransitionHandler.$r8$lambda$hbnbo3kcmXWANUSgiRavnHDFX0E(this.f$0, this.f$1, this.f$2);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$startAnimInternal$2(ArrayList arrayList, ValueAnimator valueAnimator, Runnable runnable) {
        arrayList.remove(valueAnimator);
        runnable.run();
    }

    private static void applyTransformation(long j, SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Animation animation, Transformation transformation, float[] fArr) {
        animation.getTransformation(j, transformation);
        transaction.setMatrix(surfaceControl, transformation.getMatrix(), fArr);
        transaction.setAlpha(surfaceControl, transformation.getAlpha());
        transaction.setFrameTimelineVsync(Choreographer.getInstance().getVsyncId());
        transaction.apply();
    }
}
