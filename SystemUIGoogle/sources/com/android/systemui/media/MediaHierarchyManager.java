package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewOverlay;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.animation.UniqueObjectHostView;
import java.util.Objects;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaHierarchyManager.kt */
/* loaded from: classes.dex */
public final class MediaHierarchyManager {
    public static final Companion Companion = new Companion(null);
    private boolean animationPending;
    private float animationStartAlpha;
    private float animationStartCrossFadeProgress;
    private ValueAnimator animator;
    private final KeyguardBypassController bypassController;
    private boolean collapsingShadeFromQS;
    private final Context context;
    private int distanceForFullShadeTransition;
    private boolean dozeAnimationRunning;
    private float fullShadeTransitionProgress;
    private boolean fullyAwake;
    private boolean goingToSleep;
    private boolean isCrossFadeAnimatorRunning;
    private final KeyguardStateController keyguardStateController;
    private final MediaCarouselController mediaCarouselController;
    private final NotificationLockscreenUserManager notifLockscreenUserManager;
    private boolean qsExpanded;
    private float qsExpansion;
    private ViewGroupOverlay rootOverlay;
    private View rootView;
    private final StatusBarKeyguardViewManager statusBarKeyguardViewManager;
    private final SysuiStatusBarStateController statusBarStateController;
    private int statusbarState;
    private Rect currentBounds = new Rect();
    private Rect animationStartBounds = new Rect();
    private int crossFadeAnimationStartLocation = -1;
    private int crossFadeAnimationEndLocation = -1;
    private Rect targetBounds = new Rect();
    private final MediaHost[] mediaHosts = new MediaHost[3];
    private int previousLocation = -1;
    private int desiredLocation = -1;
    private int currentAttachmentLocation = -1;
    private final Runnable startAnimation = new Runnable(this) { // from class: com.android.systemui.media.MediaHierarchyManager$startAnimation$1
        final /* synthetic */ MediaHierarchyManager this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // java.lang.Runnable
        public final void run() {
            this.this$0.animator.start();
        }
    };
    private float animationCrossFadeProgress = 1.0f;
    private float carouselAlpha = 1.0f;

    public final float calculateAlphaFromCrossFade(float f, boolean z) {
        if (f <= 0.5f) {
            return 1.0f - (f / 0.5f);
        }
        if (z) {
            return 1.0f;
        }
        return (f - 0.5f) / 0.5f;
    }

    public MediaHierarchyManager(Context context, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardStateController keyguardStateController, KeyguardBypassController keyguardBypassController, MediaCarouselController mediaCarouselController, NotificationLockscreenUserManager notificationLockscreenUserManager, ConfigurationController configurationController, WakefulnessLifecycle wakefulnessLifecycle, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(keyguardBypassController, "bypassController");
        Intrinsics.checkNotNullParameter(mediaCarouselController, "mediaCarouselController");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "notifLockscreenUserManager");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle, "wakefulnessLifecycle");
        Intrinsics.checkNotNullParameter(statusBarKeyguardViewManager, "statusBarKeyguardViewManager");
        this.context = context;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.keyguardStateController = keyguardStateController;
        this.bypassController = keyguardBypassController;
        this.mediaCarouselController = mediaCarouselController;
        this.notifLockscreenUserManager = notificationLockscreenUserManager;
        this.statusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.statusbarState = sysuiStatusBarStateController.getState();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, ofFloat) { // from class: com.android.systemui.media.MediaHierarchyManager$animator$1$1
            final /* synthetic */ ValueAnimator $this_apply;
            final /* synthetic */ MediaHierarchyManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$this_apply = r2;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                float f;
                MediaHierarchyManager.access$updateTargetState(this.this$0);
                float animatedFraction = this.$this_apply.getAnimatedFraction();
                if (MediaHierarchyManager.access$isCrossFadeAnimatorRunning$p(this.this$0)) {
                    MediaHierarchyManager mediaHierarchyManager = this.this$0;
                    MediaHierarchyManager.access$setAnimationCrossFadeProgress$p(mediaHierarchyManager, MathUtils.lerp(MediaHierarchyManager.access$getAnimationStartCrossFadeProgress$p(mediaHierarchyManager), 1.0f, this.$this_apply.getAnimatedFraction()));
                    animatedFraction = MediaHierarchyManager.access$getAnimationCrossFadeProgress$p(this.this$0) < 0.5f ? 0.0f : 1.0f;
                    MediaHierarchyManager mediaHierarchyManager2 = this.this$0;
                    f = MediaHierarchyManager.access$calculateAlphaFromCrossFade(mediaHierarchyManager2, MediaHierarchyManager.access$getAnimationCrossFadeProgress$p(mediaHierarchyManager2), false);
                } else {
                    f = MathUtils.lerp(MediaHierarchyManager.access$getAnimationStartAlpha$p(this.this$0), 1.0f, this.$this_apply.getAnimatedFraction());
                }
                MediaHierarchyManager mediaHierarchyManager3 = this.this$0;
                MediaHierarchyManager.access$interpolateBounds(mediaHierarchyManager3, MediaHierarchyManager.access$getAnimationStartBounds$p(mediaHierarchyManager3), MediaHierarchyManager.access$getTargetBounds$p(this.this$0), animatedFraction, MediaHierarchyManager.access$getCurrentBounds$p(this.this$0));
                MediaHierarchyManager mediaHierarchyManager4 = this.this$0;
                MediaHierarchyManager.applyState$default(mediaHierarchyManager4, MediaHierarchyManager.access$getCurrentBounds$p(mediaHierarchyManager4), f, false, 4, null);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter(this) { // from class: com.android.systemui.media.MediaHierarchyManager$animator$1$2
            private boolean cancelled;
            final /* synthetic */ MediaHierarchyManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.cancelled = true;
                MediaHierarchyManager.access$setAnimationPending$p(this.this$0, false);
                View access$getRootView$p = MediaHierarchyManager.access$getRootView$p(this.this$0);
                if (access$getRootView$p != null) {
                    access$getRootView$p.removeCallbacks(MediaHierarchyManager.access$getStartAnimation$p(this.this$0));
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                MediaHierarchyManager.access$setCrossFadeAnimatorRunning$p(this.this$0, false);
                if (!this.cancelled) {
                    MediaHierarchyManager.access$applyTargetStateIfNotAnimating(this.this$0);
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                this.cancelled = false;
                MediaHierarchyManager.access$setAnimationPending$p(this.this$0, false);
            }
        });
        Unit unit = Unit.INSTANCE;
        this.animator = ofFloat;
        updateConfiguration();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) { // from class: com.android.systemui.media.MediaHierarchyManager.1
            final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onDensityOrFontScaleChanged() {
                this.this$0.updateConfiguration();
            }
        });
        sysuiStatusBarStateController.addCallback(new StatusBarStateController.StateListener(this) { // from class: com.android.systemui.media.MediaHierarchyManager.2
            final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onStatePreChange(int i, int i2) {
                this.this$0.statusbarState = i2;
                MediaHierarchyManager.updateDesiredLocation$default(this.this$0, false, false, 3, null);
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onStateChanged(int i) {
                this.this$0.updateTargetState();
                if (i == 2 && this.this$0.isLockScreenShadeVisibleToUser()) {
                    this.this$0.mediaCarouselController.logSmartspaceImpression(this.this$0.getQsExpanded());
                }
                this.this$0.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(this.this$0.isVisibleToUser());
            }

            /* JADX WARNING: Code restructure failed: missing block: B:11:0x0017, code lost:
                if ((r3 == 1.0f) == false) goto L_0x001b;
             */
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onDozeAmountChanged(float r3, float r4) {
                /*
                    r2 = this;
                    com.android.systemui.media.MediaHierarchyManager r2 = r2.this$0
                    r4 = 0
                    int r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
                    r0 = 1
                    r1 = 0
                    if (r4 != 0) goto L_0x000b
                    r4 = r0
                    goto L_0x000c
                L_0x000b:
                    r4 = r1
                L_0x000c:
                    if (r4 != 0) goto L_0x001a
                    r4 = 1065353216(0x3f800000, float:1.0)
                    int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
                    if (r3 != 0) goto L_0x0016
                    r3 = r0
                    goto L_0x0017
                L_0x0016:
                    r3 = r1
                L_0x0017:
                    if (r3 != 0) goto L_0x001a
                    goto L_0x001b
                L_0x001a:
                    r0 = r1
                L_0x001b:
                    com.android.systemui.media.MediaHierarchyManager.access$setDozeAnimationRunning(r2, r0)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaHierarchyManager.AnonymousClass2.onDozeAmountChanged(float, float):void");
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onDozingChanged(boolean z) {
                if (!z) {
                    this.this$0.setDozeAnimationRunning(false);
                    if (this.this$0.isLockScreenVisibleToUser()) {
                        this.this$0.mediaCarouselController.logSmartspaceImpression(this.this$0.getQsExpanded());
                    }
                } else {
                    MediaHierarchyManager.updateDesiredLocation$default(this.this$0, false, false, 3, null);
                    this.this$0.setQsExpanded(false);
                    this.this$0.closeGuts();
                }
                this.this$0.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(this.this$0.isVisibleToUser());
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onExpandedChanged(boolean z) {
                if (this.this$0.isHomeScreenShadeVisibleToUser()) {
                    this.this$0.mediaCarouselController.logSmartspaceImpression(this.this$0.getQsExpanded());
                }
                this.this$0.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(this.this$0.isVisibleToUser());
            }
        });
        wakefulnessLifecycle.addObserver(new WakefulnessLifecycle.Observer(this) { // from class: com.android.systemui.media.MediaHierarchyManager.3
            final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onFinishedGoingToSleep() {
                this.this$0.setGoingToSleep(false);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onStartedGoingToSleep() {
                this.this$0.setGoingToSleep(true);
                this.this$0.setFullyAwake(false);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onFinishedWakingUp() {
                this.this$0.setGoingToSleep(false);
                this.this$0.setFullyAwake(true);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onStartedWakingUp() {
                this.this$0.setGoingToSleep(false);
            }
        });
        mediaCarouselController.setUpdateUserVisibility(new Function0<Unit>(this) { // from class: com.android.systemui.media.MediaHierarchyManager.4
            final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                this.this$0.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(this.this$0.isVisibleToUser());
            }
        });
    }

    private final ViewGroup getMediaFrame() {
        return this.mediaCarouselController.getMediaFrame();
    }

    private final boolean getHasActiveMedia() {
        MediaHost mediaHost = this.mediaHosts[1];
        return Intrinsics.areEqual(mediaHost == null ? null : Boolean.valueOf(mediaHost.getVisible()), Boolean.TRUE);
    }

    public final void setQsExpansion(float f) {
        if (!(this.qsExpansion == f)) {
            this.qsExpansion = f;
            updateDesiredLocation$default(this, false, false, 3, null);
            if (getQSTransformationProgress() >= 0.0f) {
                updateTargetState();
                applyTargetStateIfNotAnimating();
            }
        }
    }

    public final boolean getQsExpanded() {
        return this.qsExpanded;
    }

    public final void setQsExpanded(boolean z) {
        if (this.qsExpanded != z) {
            this.qsExpanded = z;
            this.mediaCarouselController.getMediaCarouselScrollHandler().setQsExpanded(z);
        }
        if (z && (isLockScreenShadeVisibleToUser() || isHomeScreenShadeVisibleToUser())) {
            this.mediaCarouselController.logSmartspaceImpression(z);
        }
        this.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(isVisibleToUser());
    }

    private final void setFullShadeTransitionProgress(float f) {
        if (!(this.fullShadeTransitionProgress == f)) {
            this.fullShadeTransitionProgress = f;
            if (!this.bypassController.getBypassEnabled() && this.statusbarState == 1) {
                updateDesiredLocation$default(this, isCurrentlyFading(), false, 2, null);
                if (f >= 0.0f) {
                    updateTargetState();
                    setCarouselAlpha(calculateAlphaFromCrossFade(this.fullShadeTransitionProgress, true));
                    applyTargetStateIfNotAnimating();
                }
            }
        }
    }

    private final boolean isTransitioningToFullShade() {
        return !((this.fullShadeTransitionProgress > 0.0f ? 1 : (this.fullShadeTransitionProgress == 0.0f ? 0 : -1)) == 0) && !this.bypassController.getBypassEnabled() && this.statusbarState == 1;
    }

    public final void setTransitionToFullShadeAmount(float f) {
        setFullShadeTransitionProgress(MathUtils.saturate(f / ((float) this.distanceForFullShadeTransition)));
    }

    public final void setCollapsingShadeFromQS(boolean z) {
        if (this.collapsingShadeFromQS != z) {
            this.collapsingShadeFromQS = z;
            updateDesiredLocation$default(this, true, false, 2, null);
        }
    }

    private final boolean getBlockLocationChanges() {
        return this.goingToSleep || this.dozeAnimationRunning;
    }

    public final void setGoingToSleep(boolean z) {
        if (this.goingToSleep != z) {
            this.goingToSleep = z;
            if (!z) {
                updateDesiredLocation$default(this, false, false, 3, null);
            }
        }
    }

    public final void setFullyAwake(boolean z) {
        if (this.fullyAwake != z) {
            this.fullyAwake = z;
            if (z) {
                updateDesiredLocation$default(this, true, false, 2, null);
            }
        }
    }

    public final void setDozeAnimationRunning(boolean z) {
        if (this.dozeAnimationRunning != z) {
            this.dozeAnimationRunning = z;
            if (!z) {
                updateDesiredLocation$default(this, false, false, 3, null);
            }
        }
    }

    private final void setCarouselAlpha(float f) {
        if (!(this.carouselAlpha == f)) {
            this.carouselAlpha = f;
            CrossFadeHelper.fadeIn(getMediaFrame(), f);
        }
    }

    public final void updateConfiguration() {
        this.distanceForFullShadeTransition = this.context.getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_media_transition_distance);
    }

    public final UniqueObjectHostView register(MediaHost mediaHost) {
        Intrinsics.checkNotNullParameter(mediaHost, "mediaObject");
        UniqueObjectHostView createUniqueObjectHost = createUniqueObjectHost();
        mediaHost.setHostView(createUniqueObjectHost);
        mediaHost.addVisibilityChangeListener(new Function1<Boolean, Unit>(mediaHost, this) { // from class: com.android.systemui.media.MediaHierarchyManager$register$1
            final /* synthetic */ MediaHost $mediaObject;
            final /* synthetic */ MediaHierarchyManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.$mediaObject = r1;
                this.this$0 = r2;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Boolean bool) {
                invoke(bool.booleanValue());
                return Unit.INSTANCE;
            }

            public final void invoke(boolean z) {
                this.this$0.updateDesiredLocation(true, this.$mediaObject.getLocation() == 1);
            }
        });
        this.mediaHosts[mediaHost.getLocation()] = mediaHost;
        if (mediaHost.getLocation() == this.desiredLocation) {
            this.desiredLocation = -1;
        }
        if (mediaHost.getLocation() == this.currentAttachmentLocation) {
            this.currentAttachmentLocation = -1;
        }
        updateDesiredLocation$default(this, false, false, 3, null);
        return createUniqueObjectHost;
    }

    public final void closeGuts() {
        MediaCarouselController.closeGuts$default(this.mediaCarouselController, false, 1, null);
    }

    private final UniqueObjectHostView createUniqueObjectHost() {
        UniqueObjectHostView uniqueObjectHostView = new UniqueObjectHostView(this.context);
        uniqueObjectHostView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener(this, uniqueObjectHostView) { // from class: com.android.systemui.media.MediaHierarchyManager$createUniqueObjectHost$1
            final /* synthetic */ UniqueObjectHostView $viewHost;
            final /* synthetic */ MediaHierarchyManager this$0;

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view) {
            }

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$viewHost = r2;
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view) {
                if (this.this$0.rootOverlay == null) {
                    this.this$0.rootView = this.$viewHost.getViewRootImpl().getView();
                    MediaHierarchyManager mediaHierarchyManager = this.this$0;
                    View view2 = mediaHierarchyManager.rootView;
                    Intrinsics.checkNotNull(view2);
                    ViewOverlay overlay = view2.getOverlay();
                    Objects.requireNonNull(overlay, "null cannot be cast to non-null type android.view.ViewGroupOverlay");
                    mediaHierarchyManager.rootOverlay = (ViewGroupOverlay) overlay;
                }
                this.$viewHost.removeOnAttachStateChangeListener(this);
            }
        });
        return uniqueObjectHostView;
    }

    static /* synthetic */ void updateDesiredLocation$default(MediaHierarchyManager mediaHierarchyManager, boolean z, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        if ((i & 2) != 0) {
            z2 = false;
        }
        mediaHierarchyManager.updateDesiredLocation(z, z2);
    }

    public final void updateDesiredLocation(boolean z, boolean z2) {
        int i;
        int calculateLocation = calculateLocation();
        int i2 = this.desiredLocation;
        if (calculateLocation != i2 || z2) {
            boolean z3 = false;
            if (i2 >= 0 && calculateLocation != i2) {
                this.previousLocation = i2;
            } else if (z2) {
                boolean z4 = !this.bypassController.getBypassEnabled() && ((i = this.statusbarState) == 1 || i == 3);
                if (calculateLocation == 0 && this.previousLocation == 2 && !z4) {
                    this.previousLocation = 1;
                }
            }
            boolean z5 = this.desiredLocation == -1;
            this.desiredLocation = calculateLocation;
            boolean z6 = !z && shouldAnimateTransition(calculateLocation, this.previousLocation);
            Pair<Long, Long> animationParams = getAnimationParams(this.previousLocation, calculateLocation);
            long longValue = animationParams.component1().longValue();
            long longValue2 = animationParams.component2().longValue();
            MediaHost host = getHost(calculateLocation);
            if (calculateTransformationType() == 1) {
                z3 = true;
            }
            if (!z3 || isCurrentlyInGuidedTransformation() || !z6) {
                this.mediaCarouselController.onDesiredLocationChanged(calculateLocation, host, z6, longValue, longValue2);
            }
            performTransitionToNewLocation(z5, z6);
        }
    }

    private final void performTransitionToNewLocation(boolean z, boolean z2) {
        int i;
        View view;
        if (this.previousLocation < 0 || z) {
            cancelAnimationAndApplyDesiredState();
            return;
        }
        MediaHost host = getHost(this.desiredLocation);
        MediaHost host2 = getHost(this.previousLocation);
        if (host == null || host2 == null) {
            cancelAnimationAndApplyDesiredState();
            return;
        }
        updateTargetState();
        if (isCurrentlyInGuidedTransformation()) {
            applyTargetStateIfNotAnimating();
        } else if (z2) {
            boolean z3 = this.isCrossFadeAnimatorRunning;
            float f = this.animationCrossFadeProgress;
            this.animator.cancel();
            if (this.currentAttachmentLocation != this.previousLocation || !host2.getHostView().isAttachedToWindow()) {
                this.animationStartBounds.set(this.currentBounds);
            } else {
                this.animationStartBounds.set(host2.getCurrentBounds());
            }
            boolean z4 = calculateTransformationType() == 1;
            int i2 = this.previousLocation;
            if (!z3) {
                if (z4) {
                    f = (1.0f - this.carouselAlpha) / 2.0f;
                    i = i2;
                }
                f = 0.0f;
                i = i2;
            } else if (this.currentAttachmentLocation == this.crossFadeAnimationEndLocation) {
                if (z4) {
                    f = 1.0f - f;
                    i = i2;
                }
                f = 0.0f;
                i = i2;
            } else {
                i = this.crossFadeAnimationStartLocation;
                if (i == this.desiredLocation) {
                    f = 1.0f - f;
                } else {
                    z4 = true;
                }
            }
            this.isCrossFadeAnimatorRunning = z4;
            this.crossFadeAnimationStartLocation = i;
            int i3 = this.desiredLocation;
            this.crossFadeAnimationEndLocation = i3;
            this.animationStartAlpha = this.carouselAlpha;
            this.animationStartCrossFadeProgress = f;
            adjustAnimatorForTransition(i3, i2);
            if (!this.animationPending && (view = this.rootView) != null) {
                this.animationPending = true;
                view.postOnAnimation(this.startAnimation);
            }
        } else {
            cancelAnimationAndApplyDesiredState();
        }
    }

    private final boolean shouldAnimateTransition(int i, int i2) {
        if (isCurrentlyInGuidedTransformation()) {
            return false;
        }
        if (i2 == 2 && this.desiredLocation == 1 && this.statusbarState == 0) {
            return false;
        }
        if (i == 1 && i2 == 2 && (this.statusBarStateController.leaveOpenOnKeyguardHide() || this.statusbarState == 2)) {
            return true;
        }
        if (this.statusbarState == 1 && (i == 2 || i2 == 2)) {
            return false;
        }
        return MediaHierarchyManagerKt.isShownNotFaded(getMediaFrame()) || this.animator.isRunning() || this.animationPending;
    }

    private final void adjustAnimatorForTransition(int i, int i2) {
        Pair<Long, Long> animationParams = getAnimationParams(i2, i);
        long longValue = animationParams.component1().longValue();
        long longValue2 = animationParams.component2().longValue();
        ValueAnimator valueAnimator = this.animator;
        valueAnimator.setDuration(longValue);
        valueAnimator.setStartDelay(longValue2);
    }

    private final Pair<Long, Long> getAnimationParams(int i, int i2) {
        long j;
        long j2 = 0;
        if (i == 2 && i2 == 1) {
            if (this.statusbarState == 0 && this.keyguardStateController.isKeyguardFadingAway()) {
                j2 = this.keyguardStateController.getKeyguardFadingAwayDelay();
            }
            j = 224;
        } else {
            j = (i == 1 && i2 == 2) ? 464 : 200;
        }
        return TuplesKt.to(Long.valueOf(j), Long.valueOf(j2));
    }

    public final void applyTargetStateIfNotAnimating() {
        if (!this.animator.isRunning()) {
            applyState$default(this, this.targetBounds, this.carouselAlpha, false, 4, null);
        }
    }

    public final void updateTargetState() {
        if (!isCurrentlyInGuidedTransformation() || isCurrentlyFading()) {
            MediaHost host = getHost(this.desiredLocation);
            Rect currentBounds = host == null ? null : host.getCurrentBounds();
            if (currentBounds != null) {
                this.targetBounds.set(currentBounds);
                return;
            }
            return;
        }
        float transformationProgress = getTransformationProgress();
        MediaHost host2 = getHost(this.desiredLocation);
        Intrinsics.checkNotNull(host2);
        MediaHost host3 = getHost(this.previousLocation);
        Intrinsics.checkNotNull(host3);
        if (!host2.getVisible()) {
            host2 = host3;
        } else if (!host3.getVisible()) {
            host3 = host2;
        }
        this.targetBounds = interpolateBounds$default(this, host3.getCurrentBounds(), host2.getCurrentBounds(), transformationProgress, null, 8, null);
    }

    static /* synthetic */ Rect interpolateBounds$default(MediaHierarchyManager mediaHierarchyManager, Rect rect, Rect rect2, float f, Rect rect3, int i, Object obj) {
        if ((i & 8) != 0) {
            rect3 = null;
        }
        return mediaHierarchyManager.interpolateBounds(rect, rect2, f, rect3);
    }

    public final Rect interpolateBounds(Rect rect, Rect rect2, float f, Rect rect3) {
        int lerp = (int) MathUtils.lerp((float) rect.left, (float) rect2.left, f);
        int lerp2 = (int) MathUtils.lerp((float) rect.top, (float) rect2.top, f);
        int lerp3 = (int) MathUtils.lerp((float) rect.right, (float) rect2.right, f);
        int lerp4 = (int) MathUtils.lerp((float) rect.bottom, (float) rect2.bottom, f);
        if (rect3 == null) {
            rect3 = new Rect();
        }
        rect3.set(lerp, lerp2, lerp3, lerp4);
        return rect3;
    }

    private final boolean isCurrentlyInGuidedTransformation() {
        return getTransformationProgress() >= 0.0f;
    }

    public final int calculateTransformationType() {
        if (isTransitioningToFullShade()) {
            return 1;
        }
        int i = this.previousLocation;
        if ((i == 2 && this.desiredLocation == 0) || (i == 0 && this.desiredLocation == 2)) {
            return 1;
        }
        if (i == 2 && this.desiredLocation == 1) {
            return 1;
        }
        return 0;
    }

    private final float getTransformationProgress() {
        float qSTransformationProgress = getQSTransformationProgress();
        if (this.statusbarState != 1 && qSTransformationProgress >= 0.0f) {
            return qSTransformationProgress;
        }
        if (isTransitioningToFullShade()) {
            return this.fullShadeTransitionProgress;
        }
        return -1.0f;
    }

    private final float getQSTransformationProgress() {
        MediaHost host = getHost(this.desiredLocation);
        MediaHost host2 = getHost(this.previousLocation);
        if (!getHasActiveMedia()) {
            return -1.0f;
        }
        Integer num = null;
        Integer valueOf = host == null ? null : Integer.valueOf(host.getLocation());
        if (valueOf == null || valueOf.intValue() != 0) {
            return -1.0f;
        }
        if (host2 != null) {
            num = Integer.valueOf(host2.getLocation());
        }
        if (num == null || num.intValue() != 1) {
            return -1.0f;
        }
        if (host2.getVisible() || this.statusbarState != 1) {
            return this.qsExpansion;
        }
        return -1.0f;
    }

    private final MediaHost getHost(int i) {
        if (i < 0) {
            return null;
        }
        return this.mediaHosts[i];
    }

    private final void cancelAnimationAndApplyDesiredState() {
        this.animator.cancel();
        MediaHost host = getHost(this.desiredLocation);
        if (host != null) {
            applyState(host.getCurrentBounds(), 1.0f, true);
        }
    }

    public static /* synthetic */ void applyState$default(MediaHierarchyManager mediaHierarchyManager, Rect rect, float f, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        mediaHierarchyManager.applyState(rect, f, z);
    }

    private final void applyState(Rect rect, float f, boolean z) {
        int i;
        this.currentBounds.set(rect);
        float f2 = 1.0f;
        if (!isCurrentlyFading()) {
            f = 1.0f;
        }
        setCarouselAlpha(f);
        boolean z2 = !isCurrentlyInGuidedTransformation() || isCurrentlyFading();
        if (z2) {
            i = -1;
        } else {
            i = this.previousLocation;
        }
        if (!z2) {
            f2 = getTransformationProgress();
        }
        this.mediaCarouselController.setCurrentState(i, resolveLocationForFading(), f2, z);
        updateHostAttachment();
        if (this.currentAttachmentLocation == -1000) {
            ViewGroup mediaFrame = getMediaFrame();
            Rect rect2 = this.currentBounds;
            mediaFrame.setLeftTopRightBottom(rect2.left, rect2.top, rect2.right, rect2.bottom);
        }
    }

    private final void updateHostAttachment() {
        UniqueObjectHostView hostView;
        int resolveLocationForFading = resolveLocationForFading();
        boolean z = true;
        boolean z2 = !isCurrentlyFading();
        if (this.isCrossFadeAnimatorRunning) {
            MediaHost host = getHost(resolveLocationForFading);
            Boolean bool = null;
            if (Intrinsics.areEqual(host == null ? null : Boolean.valueOf(host.getVisible()), Boolean.TRUE)) {
                MediaHost host2 = getHost(resolveLocationForFading);
                if (!(host2 == null || (hostView = host2.getHostView()) == null)) {
                    bool = Boolean.valueOf(hostView.isShown());
                }
                if (Intrinsics.areEqual(bool, Boolean.FALSE) && resolveLocationForFading != this.desiredLocation) {
                    z2 = true;
                }
            }
        }
        if (!isTransitionRunning() || this.rootOverlay == null || !z2) {
            z = false;
        }
        if (z) {
            resolveLocationForFading = -1000;
        }
        if (this.currentAttachmentLocation != resolveLocationForFading) {
            this.currentAttachmentLocation = resolveLocationForFading;
            ViewGroup viewGroup = (ViewGroup) getMediaFrame().getParent();
            if (viewGroup != null) {
                viewGroup.removeView(getMediaFrame());
            }
            if (z) {
                ViewGroupOverlay viewGroupOverlay = this.rootOverlay;
                Intrinsics.checkNotNull(viewGroupOverlay);
                viewGroupOverlay.add(getMediaFrame());
            } else {
                MediaHost host3 = getHost(resolveLocationForFading);
                Intrinsics.checkNotNull(host3);
                UniqueObjectHostView hostView2 = host3.getHostView();
                hostView2.addView(getMediaFrame());
                int paddingLeft = hostView2.getPaddingLeft();
                int paddingTop = hostView2.getPaddingTop();
                getMediaFrame().setLeftTopRightBottom(paddingLeft, paddingTop, this.currentBounds.width() + paddingLeft, this.currentBounds.height() + paddingTop);
            }
            if (this.isCrossFadeAnimatorRunning) {
                MediaCarouselController.onDesiredLocationChanged$default(this.mediaCarouselController, resolveLocationForFading, getHost(resolveLocationForFading), false, 0, 0, 24, null);
            }
        }
    }

    private final int resolveLocationForFading() {
        if (!this.isCrossFadeAnimatorRunning) {
            return this.desiredLocation;
        }
        if (((double) this.animationCrossFadeProgress) > 0.5d || this.previousLocation == -1) {
            return this.crossFadeAnimationEndLocation;
        }
        return this.crossFadeAnimationStartLocation;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0015, code lost:
        if ((getTransformationProgress() == 1.0f) != false) goto L_0x0017;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean isTransitionRunning() {
        /*
            r4 = this;
            boolean r0 = r4.isCurrentlyInGuidedTransformation()
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0017
            float r0 = r4.getTransformationProgress()
            r3 = 1065353216(0x3f800000, float:1.0)
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 != 0) goto L_0x0014
            r0 = r2
            goto L_0x0015
        L_0x0014:
            r0 = r1
        L_0x0015:
            if (r0 == 0) goto L_0x0023
        L_0x0017:
            android.animation.ValueAnimator r0 = r4.animator
            boolean r0 = r0.isRunning()
            if (r0 != 0) goto L_0x0023
            boolean r4 = r4.animationPending
            if (r4 == 0) goto L_0x0024
        L_0x0023:
            r1 = r2
        L_0x0024:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaHierarchyManager.isTransitionRunning():boolean");
    }

    private final int calculateLocation() {
        int i;
        if (getBlockLocationChanges()) {
            return this.desiredLocation;
        }
        int i2 = 1;
        boolean z = !this.bypassController.getBypassEnabled() && ((i = this.statusbarState) == 1 || i == 3);
        boolean shouldShowLockscreenNotifications = this.notifLockscreenUserManager.shouldShowLockscreenNotifications();
        float f = this.qsExpansion;
        if ((f > 0.0f && !z) || ((f > 0.4f && z) || !getHasActiveMedia())) {
            i2 = 0;
        } else if ((!z || !isTransformingToFullShadeAndInQQS()) && z && shouldShowLockscreenNotifications) {
            i2 = 2;
        }
        if (i2 == 2) {
            MediaHost host = getHost(i2);
            if (!Intrinsics.areEqual(host == null ? null : Boolean.valueOf(host.getVisible()), Boolean.TRUE) && !this.statusBarStateController.isDozing()) {
                return 0;
            }
        }
        if (i2 == 2 && this.desiredLocation == 0 && this.collapsingShadeFromQS) {
            return 0;
        }
        if (i2 == 2 || this.desiredLocation != 2 || this.fullyAwake) {
            return i2;
        }
        return 2;
    }

    private final boolean isTransformingToFullShadeAndInQQS() {
        if (isTransitioningToFullShade() && this.fullShadeTransitionProgress > 0.5f) {
            return true;
        }
        return false;
    }

    private final boolean isCurrentlyFading() {
        if (isTransitioningToFullShade()) {
            return true;
        }
        return this.isCrossFadeAnimatorRunning;
    }

    public final boolean isVisibleToUser() {
        return isLockScreenVisibleToUser() || isLockScreenShadeVisibleToUser() || isHomeScreenShadeVisibleToUser();
    }

    public final boolean isLockScreenVisibleToUser() {
        if (this.statusBarStateController.isDozing() || this.statusBarKeyguardViewManager.isBouncerShowing() || this.statusBarStateController.getState() != 1 || !this.notifLockscreenUserManager.shouldShowLockscreenNotifications() || !this.statusBarStateController.isExpanded() || this.qsExpanded) {
            return false;
        }
        return true;
    }

    public final boolean isLockScreenShadeVisibleToUser() {
        if (!this.statusBarStateController.isDozing() && !this.statusBarKeyguardViewManager.isBouncerShowing()) {
            if (this.statusBarStateController.getState() == 2) {
                return true;
            }
            if (this.statusBarStateController.getState() == 1 && this.qsExpanded) {
                return true;
            }
        }
        return false;
    }

    public final boolean isHomeScreenShadeVisibleToUser() {
        return !this.statusBarStateController.isDozing() && this.statusBarStateController.getState() == 0 && this.statusBarStateController.isExpanded();
    }

    /* compiled from: MediaHierarchyManager.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
