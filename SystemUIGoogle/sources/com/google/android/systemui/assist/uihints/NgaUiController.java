package com.google.android.systemui.assist.uihints;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.metrics.LogMaker;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.util.MathUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.view.animation.PathInterpolator;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.DejankUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.assist.AssistantSessionEvent;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.google.android.systemui.assist.uihints.AssistantPresenceHandler;
import com.google.android.systemui.assist.uihints.TimeoutManager;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import com.google.android.systemui.assist.uihints.edgelights.mode.FullListening;
import com.google.android.systemui.assist.uihints.edgelights.mode.Gone;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
/* loaded from: classes2.dex */
public class NgaUiController implements AssistManager.UiController, ViewTreeObserver.OnComputeInternalInsetsListener, StatusBarStateController.StateListener {
    private static final boolean VERBOSE;
    private static final PathInterpolator mProgressInterpolator;
    private final AssistLogger mAssistLogger;
    private final Lazy<AssistManager> mAssistManager;
    private final AssistantPresenceHandler mAssistantPresenceHandler;
    private final AssistantWarmer mAssistantWarmer;
    private final ColorChangeHandler mColorChangeHandler;
    private final Context mContext;
    private final EdgeLightsController mEdgeLightsController;
    private final FlingVelocityWrapper mFlingVelocity;
    private final GlowController mGlowController;
    private final IconController mIconController;
    private ValueAnimator mInvocationAnimator;
    private AssistantInvocationLightsView mInvocationLightsView;
    private final LightnessProvider mLightnessProvider;
    private final NavBarFader mNavBarFader;
    private Runnable mPendingEdgeLightsModeChange;
    private PromptView mPromptView;
    private final ScrimController mScrimController;
    private final TimeoutManager mTimeoutManager;
    private final TouchInsideHandler mTouchInsideHandler;
    private final TranscriptionController mTranscriptionController;
    private final OverlayUiHost mUiHost;
    private PowerManager.WakeLock mWakeLock;
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private boolean mHasDarkBackground = false;
    private boolean mIsMonitoringColor = false;
    private boolean mInvocationInProgress = false;
    private boolean mShowingAssistUi = false;
    private boolean mShouldKeepWakeLock = false;
    private long mLastInvocationStartTime = 0;
    private float mLastInvocationProgress = 0.0f;
    private long mColorMonitoringStart = 0;

    static {
        String str = Build.TYPE;
        Locale locale = Locale.ROOT;
        VERBOSE = str.toLowerCase(locale).contains("debug") || str.toLowerCase(locale).equals("eng");
        mProgressInterpolator = new PathInterpolator(0.83f, 0.0f, 0.84f, 1.0f);
    }

    public NgaUiController(Context context, TimeoutManager timeoutManager, AssistantPresenceHandler assistantPresenceHandler, TouchInsideHandler touchInsideHandler, ColorChangeHandler colorChangeHandler, OverlayUiHost overlayUiHost, EdgeLightsController edgeLightsController, GlowController glowController, ScrimController scrimController, TranscriptionController transcriptionController, IconController iconController, LightnessProvider lightnessProvider, StatusBarStateController statusBarStateController, Lazy<AssistManager> lazy, FlingVelocityWrapper flingVelocityWrapper, AssistantWarmer assistantWarmer, NavBarFader navBarFader, AssistLogger assistLogger) {
        this.mContext = context;
        this.mAssistLogger = assistLogger;
        this.mColorChangeHandler = colorChangeHandler;
        colorChangeHandler.onColorChange(false);
        this.mTimeoutManager = timeoutManager;
        this.mAssistantPresenceHandler = assistantPresenceHandler;
        this.mTouchInsideHandler = touchInsideHandler;
        this.mUiHost = overlayUiHost;
        this.mEdgeLightsController = edgeLightsController;
        this.mGlowController = glowController;
        this.mScrimController = scrimController;
        this.mTranscriptionController = transcriptionController;
        this.mIconController = iconController;
        this.mLightnessProvider = lightnessProvider;
        this.mAssistManager = lazy;
        this.mFlingVelocity = flingVelocityWrapper;
        this.mAssistantWarmer = assistantWarmer;
        this.mNavBarFader = navBarFader;
        lightnessProvider.setListener(new LightnessListener() { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda2
            @Override // com.google.android.systemui.assist.uihints.LightnessListener
            public final void onLightnessUpdate(float f) {
                NgaUiController.this.lambda$new$0(f);
            }
        });
        assistantPresenceHandler.registerSysUiIsNgaUiChangeListener(new AssistantPresenceHandler.SysUiIsNgaUiChangeListener() { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda1
            @Override // com.google.android.systemui.assist.uihints.AssistantPresenceHandler.SysUiIsNgaUiChangeListener
            public final void onSysUiIsNgaUiChanged(boolean z) {
                NgaUiController.this.lambda$new$1(z);
            }
        });
        touchInsideHandler.setFallback(new NgaUiController$$ExternalSyntheticLambda6(this));
        edgeLightsController.setModeChangeThrottler(new EdgeLightsController.ModeChangeThrottler() { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda5
            @Override // com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController.ModeChangeThrottler
            public final void runWhenReady(String str, Runnable runnable) {
                NgaUiController.this.lambda$new$2(str, runnable);
            }
        });
        this.mWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(805306378, "Assist (NGA)");
        NgaUiController$$ExternalSyntheticLambda4 ngaUiController$$ExternalSyntheticLambda4 = new VisibilityListener() { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda4
            @Override // com.google.android.systemui.assist.uihints.VisibilityListener
            public final void onVisibilityChanged(int i) {
                NgaUiController.this.lambda$new$3(i);
            }
        };
        glowController.setVisibilityListener(ngaUiController$$ExternalSyntheticLambda4);
        scrimController.setVisibilityListener(ngaUiController$$ExternalSyntheticLambda4);
        ViewGroup parent = overlayUiHost.getParent();
        AssistantInvocationLightsView assistantInvocationLightsView = (AssistantInvocationLightsView) parent.findViewById(R$id.invocation_lights);
        this.mInvocationLightsView = assistantInvocationLightsView;
        assistantInvocationLightsView.setGoogleAssistant(true);
        edgeLightsController.addListener(glowController);
        edgeLightsController.addListener(scrimController);
        transcriptionController.setListener(scrimController);
        this.mPromptView = (PromptView) parent.findViewById(R$id.prompt);
        dispatchHasDarkBackground();
        statusBarStateController.addCallback(this);
        refresh();
        timeoutManager.setTimeoutCallback(new TimeoutManager.TimeoutCallback(new Runnable() { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                NgaUiController.this.lambda$new$4();
            }
        }) { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda3
            public final /* synthetic */ Runnable f$0;

            {
                this.f$0 = r1;
            }

            @Override // com.google.android.systemui.assist.uihints.TimeoutManager.TimeoutCallback
            public final void onTimeout() {
                this.f$0.run();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(float f) {
        boolean z = VERBOSE;
        if (this.mColorMonitoringStart > 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime() - this.mColorMonitoringStart;
            if (z) {
                Log.d("NgaUiController", "Got lightness update (" + f + ") after " + elapsedRealtime + " ms");
            }
            this.mColorMonitoringStart = 0;
        }
        boolean z2 = true;
        this.mIconController.setHasAccurateLuma(true);
        this.mGlowController.setMedianLightness(f);
        this.mScrimController.setHasMedianLightness(f);
        this.mTranscriptionController.setHasAccurateBackground(true);
        if (f > 0.4f) {
            z2 = false;
        }
        setHasDarkBackground(z2);
        refresh();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(boolean z) {
        if (!z) {
            hide();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(String str, Runnable runnable) {
        ValueAnimator valueAnimator = this.mInvocationAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            this.mPendingEdgeLightsModeChange = runnable;
        } else if (this.mShowingAssistUi || !"FULL_LISTENING".equals(str)) {
            this.mPendingEdgeLightsModeChange = null;
            runnable.run();
        } else {
            this.mInvocationInProgress = true;
            onInvocationProgress(0, 1.0f);
            this.mPendingEdgeLightsModeChange = runnable;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(int i) {
        refresh();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4() {
        if (this.mShowingAssistUi) {
            Log.e("NgaUiController", "Timed out");
            this.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_TIMEOUT_DISMISS);
            closeNgaUi();
            MetricsLogger.action(new LogMaker(1716).setType(5).setSubtype(4));
        }
    }

    /* access modifiers changed from: package-private */
    public void onUiMessageReceived() {
        refresh();
    }

    private void refresh() {
        updateShowingAssistUi();
        updateShowingNavBar();
    }

    private void setHasDarkBackground(boolean z) {
        String str = "dark";
        if (this.mHasDarkBackground != z) {
            this.mHasDarkBackground = z;
            this.mColorChangeHandler.onColorChange(z);
            if (VERBOSE) {
                StringBuilder sb = new StringBuilder();
                sb.append("switching to ");
                if (!this.mHasDarkBackground) {
                    str = "light";
                }
                sb.append(str);
                Log.v("NgaUiController", sb.toString());
            }
            dispatchHasDarkBackground();
        } else if (VERBOSE) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("not switching; already ");
            if (!z) {
                str = "light";
            }
            sb2.append(str);
            Log.v("NgaUiController", sb2.toString());
        }
    }

    private void dispatchHasDarkBackground() {
        this.mTranscriptionController.setHasDarkBackground(this.mHasDarkBackground);
        this.mIconController.setHasDarkBackground(this.mHasDarkBackground);
        this.mPromptView.setHasDarkBackground(this.mHasDarkBackground);
    }

    /* access modifiers changed from: private */
    public void closeNgaUi() {
        this.mAssistManager.get().hideAssist();
        hide();
    }

    @Override // com.android.systemui.assist.AssistManager.UiController
    public void hide() {
        ValueAnimator valueAnimator = this.mInvocationAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            this.mInvocationAnimator.cancel();
        }
        this.mInvocationInProgress = false;
        this.mTranscriptionController.onClear(false);
        this.mEdgeLightsController.setGone();
        this.mPendingEdgeLightsModeChange = null;
        this.mPromptView.disable();
        this.mIconController.onHideKeyboard();
        this.mIconController.onHideZerostate();
        refresh();
    }

    private void setColorMonitoringState(boolean z) {
        if (this.mIsMonitoringColor != z) {
            if (!z || !this.mScrimController.isVisible() || this.mScrimController.getSurfaceControllerHandle() != null) {
                this.mIsMonitoringColor = z;
                if (z) {
                    int rotatedHeight = (DisplayUtils.getRotatedHeight(this.mContext) - ((int) this.mContext.getResources().getDimension(R$dimen.transcription_space_bottom_margin))) - DisplayUtils.convertSpToPx(20.0f, this.mContext);
                    Rect rect = new Rect(0, rotatedHeight - DisplayUtils.convertDpToPx(160.0f, this.mContext), DisplayUtils.getRotatedWidth(this.mContext), rotatedHeight);
                    this.mColorMonitoringStart = SystemClock.elapsedRealtime();
                    this.mLightnessProvider.enableColorMonitoring(true, rect, this.mScrimController.getSurfaceControllerHandle());
                    return;
                }
                this.mLightnessProvider.enableColorMonitoring(false, null, null);
                this.mIconController.setHasAccurateLuma(false);
                this.mScrimController.onLightnessInvalidated();
                this.mTranscriptionController.setHasAccurateBackground(false);
            }
        }
    }

    private void updateShowingAssistUi() {
        boolean z = false;
        boolean z2 = !(this.mEdgeLightsController.getMode() instanceof Gone) || this.mGlowController.isVisible() || this.mScrimController.isVisible() || this.mInvocationInProgress;
        if (z2 || this.mIconController.isVisible() || this.mIconController.isRequested()) {
            z = true;
        }
        setColorMonitoringState(z);
        if (this.mShowingAssistUi != z) {
            this.mShowingAssistUi = z;
            this.mUiHost.setAssistState(z, this.mEdgeLightsController.getMode() instanceof FullListening);
            if (z) {
                this.mUiHost.getParent().getViewTreeObserver().addOnComputeInternalInsetsListener(this);
            } else {
                this.mUiHost.getParent().getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
                ValueAnimator valueAnimator = this.mInvocationAnimator;
                if (valueAnimator != null && valueAnimator.isStarted()) {
                    this.mInvocationAnimator.cancel();
                }
            }
        }
        if (this.mShouldKeepWakeLock != z2) {
            this.mShouldKeepWakeLock = z2;
            if (z2) {
                this.mWakeLock.acquire();
            } else {
                this.mWakeLock.release();
            }
        }
    }

    private void updateShowingNavBar() {
        this.mNavBarFader.onVisibleRequest(!this.mInvocationInProgress && (this.mEdgeLightsController.getMode() instanceof Gone));
    }

    private float getAnimationProgress(int i, float f) {
        return i == 2 ? f * 0.95f : mProgressInterpolator.getInterpolation(f * 0.8f);
    }

    @Override // com.android.systemui.assist.AssistManager.UiController
    public void onInvocationProgress(int i, float f) {
        ValueAnimator valueAnimator = this.mInvocationAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            Log.w("NgaUiController", "Already animating; ignoring invocation progress");
        } else if (!this.mEdgeLightsController.getMode().preventsInvocations()) {
            boolean z = this.mInvocationInProgress;
            int i2 = (f > 1.0f ? 1 : (f == 1.0f ? 0 : -1));
            if (i2 < 0) {
                this.mLastInvocationProgress = f;
                if (!z && f > 0.0f) {
                    this.mLastInvocationStartTime = SystemClock.uptimeMillis();
                }
                boolean z2 = f > 0.0f && i2 < 0;
                this.mInvocationInProgress = z2;
                if (!z2) {
                    this.mPromptView.disable();
                } else if (f < 0.9f && SystemClock.uptimeMillis() - this.mLastInvocationStartTime > 200) {
                    this.mPromptView.enable();
                }
                setProgress(i, getAnimationProgress(i, f));
            } else {
                ValueAnimator valueAnimator2 = this.mInvocationAnimator;
                if (valueAnimator2 == null || !valueAnimator2.isStarted()) {
                    this.mFlingVelocity.setVelocity(0.0f);
                    completeInvocation(i);
                }
            }
            this.mAssistantWarmer.onInvocationProgress(f);
            logInvocationProgressMetrics(i, f, z);
        } else if (VERBOSE) {
            Log.v("NgaUiController", "ignoring invocation; mode is " + this.mEdgeLightsController.getMode().getClass().getSimpleName());
        }
    }

    private void logInvocationProgressMetrics(int i, float f, boolean z) {
        if (f == 1.0f && VERBOSE) {
            Log.v("NgaUiController", "Invocation complete: type=" + i);
        }
        if (!z && f > 0.0f) {
            if (VERBOSE) {
                Log.v("NgaUiController", "Invocation started: type=" + i);
            }
            this.mAssistLogger.reportAssistantInvocationEventFromLegacy(i, false, null, null);
            MetricsLogger.action(new LogMaker(1716).setType(4).setSubtype(this.mAssistManager.get().toLoggingSubType(i)));
        }
        ValueAnimator valueAnimator = this.mInvocationAnimator;
        if ((valueAnimator == null || !valueAnimator.isRunning()) && z && f == 0.0f) {
            if (VERBOSE) {
                Log.v("NgaUiController", "Invocation cancelled: type=" + i);
            }
            this.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_INVOCATION_CANCELLED);
            MetricsLogger.action(new LogMaker(1716).setType(5).setSubtype(1));
        }
    }

    @Override // com.android.systemui.assist.AssistManager.UiController
    public void onGestureCompletion(float f) {
        if (!this.mEdgeLightsController.getMode().preventsInvocations()) {
            this.mFlingVelocity.setVelocity(f);
            completeInvocation(1);
            logInvocationProgressMetrics(1, 1.0f, this.mInvocationInProgress);
        } else if (VERBOSE) {
            Log.v("NgaUiController", "ignoring invocation; mode is " + this.mEdgeLightsController.getMode().getClass().getSimpleName());
        }
    }

    private void setProgress(int i, float f) {
        this.mInvocationLightsView.onInvocationProgress(f);
        this.mGlowController.setInvocationProgress(f);
        this.mScrimController.setInvocationProgress(f);
        this.mPromptView.onInvocationProgress(i, f);
        refresh();
    }

    private void completeInvocation(int i) {
        if (!this.mAssistantPresenceHandler.isSysUiNgaUi()) {
            setProgress(i, 0.0f);
            resetInvocationProgress();
            return;
        }
        this.mTouchInsideHandler.maybeSetGuarded();
        this.mTimeoutManager.resetTimeout();
        this.mPromptView.disable();
        ValueAnimator valueAnimator = this.mInvocationAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            this.mInvocationAnimator.cancel();
        }
        float velocity = this.mFlingVelocity.getVelocity();
        float f = 3.0f;
        if (velocity != 0.0f) {
            f = MathUtils.constrain((-velocity) / 1.45f, 3.0f, 12.0f);
        }
        OvershootInterpolator overshootInterpolator = new OvershootInterpolator(f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(approximateInverse(Float.valueOf(getAnimationProgress(i, this.mLastInvocationProgress)), new Function(overshootInterpolator) { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda13
            public final /* synthetic */ OvershootInterpolator f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return NgaUiController.lambda$completeInvocation$5(this.f$0, (Float) obj);
            }
        }), 1.0f);
        ofFloat.setDuration(600L);
        ofFloat.setStartDelay(1);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(i, overshootInterpolator) { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$1;
            public final /* synthetic */ OvershootInterpolator f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                NgaUiController.this.lambda$completeInvocation$6(this.f$1, this.f$2, valueAnimator2);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.uihints.NgaUiController.1
            private boolean mCancelled = false;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                this.mCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (!this.mCancelled) {
                    if (NgaUiController.this.mPendingEdgeLightsModeChange == null) {
                        NgaUiController.this.mEdgeLightsController.setFullListening();
                    } else {
                        NgaUiController.this.mPendingEdgeLightsModeChange.run();
                        NgaUiController.this.mPendingEdgeLightsModeChange = null;
                    }
                }
                NgaUiController.this.mUiHandler.post(new NgaUiController$1$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onAnimationEnd$0() {
                NgaUiController.this.resetInvocationProgress();
            }
        });
        this.mInvocationAnimator = ofFloat;
        ofFloat.start();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Float lambda$completeInvocation$5(OvershootInterpolator overshootInterpolator, Float f) {
        return Float.valueOf(Math.min(1.0f, overshootInterpolator.getInterpolation(f.floatValue())));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$completeInvocation$6(int i, OvershootInterpolator overshootInterpolator, ValueAnimator valueAnimator) {
        setProgress(i, overshootInterpolator.getInterpolation(((Float) valueAnimator.getAnimatedValue()).floatValue()));
    }

    /* access modifiers changed from: private */
    public void resetInvocationProgress() {
        this.mInvocationInProgress = false;
        this.mInvocationLightsView.hide();
        this.mLastInvocationProgress = 0.0f;
        this.mScrimController.setInvocationProgress(0.0f);
        refresh();
    }

    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        internalInsetsInfo.setTouchableInsets(3);
        Region region = new Region();
        this.mIconController.getTouchActionRegion().ifPresent(new Consumer(region) { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda10
            public final /* synthetic */ Region f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NgaUiController.lambda$onComputeInternalInsets$7(this.f$0, (Region) obj);
            }
        });
        Region region2 = new Region();
        EdgeLightsView.Mode mode = this.mEdgeLightsController.getMode();
        if (!((mode instanceof FullListening) && ((FullListening) mode).isFakeForHalfListening())) {
            this.mGlowController.getTouchInsideRegion().ifPresent(new Consumer(region2) { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda12
                public final /* synthetic */ Region f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    NgaUiController.lambda$onComputeInternalInsets$8(this.f$0, (Region) obj);
                }
            });
        }
        this.mScrimController.getTouchInsideRegion().ifPresent(new Consumer(region2) { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda9
            public final /* synthetic */ Region f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NgaUiController.lambda$onComputeInternalInsets$9(this.f$0, (Region) obj);
            }
        });
        NgaUiController$$ExternalSyntheticLambda11 ngaUiController$$ExternalSyntheticLambda11 = new Consumer(region2) { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda11
            public final /* synthetic */ Region f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NgaUiController.lambda$onComputeInternalInsets$10(this.f$0, (Region) obj);
            }
        };
        this.mTranscriptionController.getTouchInsideRegion().ifPresent(ngaUiController$$ExternalSyntheticLambda11);
        this.mTranscriptionController.getTouchActionRegion().ifPresent(ngaUiController$$ExternalSyntheticLambda11);
        region.op(region2, Region.Op.UNION);
        internalInsetsInfo.touchableRegion.set(region);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onComputeInternalInsets$7(Region region, Region region2) {
        region.op(region2, Region.Op.UNION);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onComputeInternalInsets$8(Region region, Region region2) {
        region.op(region2, Region.Op.UNION);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onComputeInternalInsets$9(Region region, Region region2) {
        region.op(region2, Region.Op.UNION);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onComputeInternalInsets$10(Region region, Region region2) {
        if (region.isEmpty()) {
            region.op(region2, Region.Op.UNION);
        } else if (region.quickReject(region2)) {
            Rect bounds = region.getBounds();
            bounds.top = region2.getBounds().top;
            region.set(bounds);
        } else {
            region.op(region2, Region.Op.UNION);
        }
    }

    private float approximateInverse(Float f, Function<Float, Float> function) {
        ArrayList arrayList = new ArrayList((int) 200.0f);
        for (float f2 = 0.0f; f2 < 1.0f; f2 += 0.005f) {
            arrayList.add(function.apply(Float.valueOf(f2)));
        }
        int binarySearch = Collections.binarySearch(arrayList, f);
        if (binarySearch < 0) {
            binarySearch = (binarySearch + 1) * -1;
        }
        return ((float) binarySearch) * 0.005f;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    /* renamed from: onDozingChanged */
    public void lambda$onDozingChanged$11(boolean z) {
        if (Looper.myLooper() != this.mUiHandler.getLooper()) {
            this.mUiHandler.post(new Runnable(z) { // from class: com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda8
                public final /* synthetic */ boolean f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    NgaUiController.this.lambda$onDozingChanged$11(this.f$1);
                }
            });
            return;
        }
        this.mScrimController.setIsDozing(z);
        if (z && this.mShowingAssistUi) {
            DejankUtils.whitelistIpcs(new NgaUiController$$ExternalSyntheticLambda6(this));
        }
    }
}
