package com.android.systemui.assist.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.metrics.LogMaker;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.Dependency;
import com.android.systemui.R$layout;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.assist.AssistantSessionEvent;
import java.util.Locale;
/* loaded from: classes.dex */
public class DefaultUiController implements AssistManager.UiController {
    private static final boolean VERBOSE;
    protected final AssistLogger mAssistLogger;
    protected InvocationLightsView mInvocationLightsView;
    private final WindowManager.LayoutParams mLayoutParams;
    protected final FrameLayout mRoot;
    private final WindowManager mWindowManager;
    private final PathInterpolator mProgressInterpolator = new PathInterpolator(0.83f, 0.0f, 0.84f, 1.0f);
    private boolean mAttached = false;
    private boolean mInvocationInProgress = false;
    private float mLastInvocationProgress = 0.0f;
    private ValueAnimator mInvocationAnimator = new ValueAnimator();

    static {
        String str = Build.TYPE;
        Locale locale = Locale.ROOT;
        VERBOSE = str.toLowerCase(locale).contains("debug") || str.toLowerCase(locale).equals("eng");
    }

    public DefaultUiController(Context context, AssistLogger assistLogger) {
        this.mAssistLogger = assistLogger;
        FrameLayout frameLayout = new FrameLayout(context);
        this.mRoot = frameLayout;
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -2, 0, 0, 2024, 808, -3);
        this.mLayoutParams = layoutParams;
        layoutParams.privateFlags = 64;
        layoutParams.gravity = 80;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTitle("Assist");
        InvocationLightsView invocationLightsView = (InvocationLightsView) LayoutInflater.from(context).inflate(R$layout.invocation_lights, (ViewGroup) frameLayout, false);
        this.mInvocationLightsView = invocationLightsView;
        frameLayout.addView(invocationLightsView);
    }

    @Override // com.android.systemui.assist.AssistManager.UiController
    public void onInvocationProgress(int i, float f) {
        boolean z = this.mInvocationInProgress;
        if (f == 1.0f) {
            animateInvocationCompletion(i, 0.0f);
        } else if (f == 0.0f) {
            hide();
        } else {
            if (!z) {
                attach();
                this.mInvocationInProgress = true;
            }
            setProgressInternal(i, f);
        }
        this.mLastInvocationProgress = f;
        logInvocationProgressMetrics(i, f, z);
    }

    @Override // com.android.systemui.assist.AssistManager.UiController
    public void onGestureCompletion(float f) {
        animateInvocationCompletion(1, f);
        logInvocationProgressMetrics(1, 1.0f, this.mInvocationInProgress);
    }

    @Override // com.android.systemui.assist.AssistManager.UiController
    public void hide() {
        detach();
        if (this.mInvocationAnimator.isRunning()) {
            this.mInvocationAnimator.cancel();
        }
        this.mInvocationLightsView.hide();
        this.mInvocationInProgress = false;
    }

    protected void logInvocationProgressMetrics(int i, float f, boolean z) {
        if (f == 1.0f && VERBOSE) {
            Log.v("DefaultUiController", "Invocation complete: type=" + i);
        }
        if (!z && f > 0.0f) {
            if (VERBOSE) {
                Log.v("DefaultUiController", "Invocation started: type=" + i);
            }
            this.mAssistLogger.reportAssistantInvocationEventFromLegacy(i, false, null, null);
            MetricsLogger.action(new LogMaker(1716).setType(4).setSubtype(((AssistManager) Dependency.get(AssistManager.class)).toLoggingSubType(i)));
        }
        ValueAnimator valueAnimator = this.mInvocationAnimator;
        if ((valueAnimator == null || !valueAnimator.isRunning()) && z && f == 0.0f) {
            if (VERBOSE) {
                Log.v("DefaultUiController", "Invocation cancelled: type=" + i);
            }
            this.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_INVOCATION_CANCELLED);
            MetricsLogger.action(new LogMaker(1716).setType(5).setSubtype(1));
        }
    }

    private void attach() {
        if (!this.mAttached) {
            this.mWindowManager.addView(this.mRoot, this.mLayoutParams);
            this.mAttached = true;
        }
    }

    private void detach() {
        if (this.mAttached) {
            this.mWindowManager.removeViewImmediate(this.mRoot);
            this.mAttached = false;
        }
    }

    private void setProgressInternal(int i, float f) {
        this.mInvocationLightsView.onInvocationProgress(this.mProgressInterpolator.getInterpolation(f));
    }

    private void animateInvocationCompletion(int i, float f) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mLastInvocationProgress, 1.0f);
        this.mInvocationAnimator = ofFloat;
        ofFloat.setStartDelay(1);
        this.mInvocationAnimator.setDuration(200L);
        this.mInvocationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(i) { // from class: com.android.systemui.assist.ui.DefaultUiController$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DefaultUiController.$r8$lambda$sk0ku4pQsD5FH9JgWmx0chQFSxc(DefaultUiController.this, this.f$1, valueAnimator);
            }
        });
        this.mInvocationAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.assist.ui.DefaultUiController.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                DefaultUiController.this.mInvocationInProgress = false;
                DefaultUiController.this.mLastInvocationProgress = 0.0f;
                DefaultUiController.this.hide();
            }
        });
        this.mInvocationAnimator.start();
    }

    public /* synthetic */ void lambda$animateInvocationCompletion$0(int i, ValueAnimator valueAnimator) {
        setProgressInternal(i, ((Float) valueAnimator.getAnimatedValue()).floatValue());
    }
}
