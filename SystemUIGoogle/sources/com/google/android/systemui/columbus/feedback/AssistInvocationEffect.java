package com.google.android.systemui.columbus.feedback;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import com.android.systemui.assist.AssistManager;
import com.google.android.systemui.assist.AssistManagerGoogle;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AssistInvocationEffect.kt */
/* loaded from: classes2.dex */
public final class AssistInvocationEffect implements FeedbackEffect {
    public static final Companion Companion = new Companion(null);
    private Animator animation;
    private final AssistInvocationEffect$animatorListener$1 animatorListener;
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener;
    private final AssistManagerGoogle assistManager;
    private float progress;

    public AssistInvocationEffect(AssistManager assistManager) {
        Intrinsics.checkNotNullParameter(assistManager, "assistManager");
        this.assistManager = assistManager instanceof AssistManagerGoogle ? (AssistManagerGoogle) assistManager : null;
        this.animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.google.android.systemui.columbus.feedback.AssistInvocationEffect$animatorUpdateListener$1
            final /* synthetic */ AssistInvocationEffect this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (valueAnimator != null) {
                    AssistInvocationEffect assistInvocationEffect = this.this$0;
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                    assistInvocationEffect.progress = ((Float) animatedValue).floatValue();
                    assistInvocationEffect.updateAssistManager();
                }
            }
        };
        this.animatorListener = new AssistInvocationEffect$animatorListener$1(this);
    }

    @Override // com.google.android.systemui.columbus.feedback.FeedbackEffect
    public void onGestureDetected(int i, GestureSensor.DetectionProperties detectionProperties) {
        if (i == 0) {
            clear();
        } else if (i == 1) {
            onTrigger();
        }
    }

    private final void onTrigger() {
        clear();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.progress, 1.0f);
        ofFloat.setDuration(200L);
        ofFloat.setInterpolator(new DecelerateInterpolator());
        ofFloat.addUpdateListener(this.animatorUpdateListener);
        ofFloat.addListener(this.animatorListener);
        this.animation = ofFloat;
        ofFloat.start();
    }

    private final void clear() {
        Animator animator;
        Animator animator2 = this.animation;
        if (Intrinsics.areEqual(animator2 == null ? null : Boolean.valueOf(animator2.isRunning()), Boolean.TRUE) && (animator = this.animation) != null) {
            animator.cancel();
        }
        this.animation = null;
    }

    public final void updateAssistManager() {
        AssistManagerGoogle assistManagerGoogle = this.assistManager;
        if (assistManagerGoogle != null) {
            assistManagerGoogle.onInvocationProgress(2, this.progress);
        }
    }

    /* compiled from: AssistInvocationEffect.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
