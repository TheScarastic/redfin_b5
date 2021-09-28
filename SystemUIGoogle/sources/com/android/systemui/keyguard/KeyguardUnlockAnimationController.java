package com.android.systemui.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import androidx.core.math.MathUtils;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.shared.system.smartspace.ISmartspaceCallback;
import com.android.systemui.shared.system.smartspace.SmartspaceTransitionController;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: KeyguardUnlockAnimationController.kt */
/* loaded from: classes.dex */
public final class KeyguardUnlockAnimationController implements KeyguardStateController.Callback {
    private boolean attemptedSmartSpaceTransitionForThisSwipe;
    private final FeatureFlags featureFlags;
    private final KeyguardStateController keyguardStateController;
    private final KeyguardViewController keyguardViewController;
    private final Lazy<KeyguardViewMediator> keyguardViewMediator;
    private float roundedCornerRadius;
    private final SmartspaceTransitionController smartspaceTransitionController;
    private final ValueAnimator surfaceBehindEntryAnimator;
    private long surfaceBehindRemoteAnimationStartTime;
    private RemoteAnimationTarget surfaceBehindRemoteAnimationTarget;
    private SyncRtSurfaceTransactionApplier surfaceTransactionApplier;
    private boolean unlockingWithSmartSpaceTransition;
    private float surfaceBehindAlpha = 1.0f;
    private ValueAnimator surfaceBehindAlphaAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
    private final Matrix surfaceBehindMatrix = new Matrix();

    public KeyguardUnlockAnimationController(Context context, KeyguardStateController keyguardStateController, Lazy<KeyguardViewMediator> lazy, KeyguardViewController keyguardViewController, SmartspaceTransitionController smartspaceTransitionController, FeatureFlags featureFlags) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(lazy, "keyguardViewMediator");
        Intrinsics.checkNotNullParameter(keyguardViewController, "keyguardViewController");
        Intrinsics.checkNotNullParameter(smartspaceTransitionController, "smartspaceTransitionController");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        this.keyguardStateController = keyguardStateController;
        this.keyguardViewMediator = lazy;
        this.keyguardViewController = keyguardViewController;
        this.smartspaceTransitionController = smartspaceTransitionController;
        this.featureFlags = featureFlags;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.surfaceBehindEntryAnimator = ofFloat;
        this.surfaceBehindAlphaAnimator.setDuration(150L);
        this.surfaceBehindAlphaAnimator.setInterpolator(Interpolators.ALPHA_IN);
        this.surfaceBehindAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.keyguard.KeyguardUnlockAnimationController.1
            final /* synthetic */ KeyguardUnlockAnimationController this$0;

            {
                this.this$0 = r1;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                Intrinsics.checkNotNullParameter(valueAnimator, "valueAnimator");
                KeyguardUnlockAnimationController keyguardUnlockAnimationController = this.this$0;
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                keyguardUnlockAnimationController.surfaceBehindAlpha = ((Float) animatedValue).floatValue();
                this.this$0.updateSurfaceBehindAppearAmount();
            }
        });
        this.surfaceBehindAlphaAnimator.addListener(new AnimatorListenerAdapter(this) { // from class: com.android.systemui.keyguard.KeyguardUnlockAnimationController.2
            final /* synthetic */ KeyguardUnlockAnimationController this$0;

            {
                this.this$0 = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                Intrinsics.checkNotNullParameter(animator, "animation");
                if (this.this$0.surfaceBehindAlpha == 0.0f) {
                    ((KeyguardViewMediator) this.this$0.keyguardViewMediator.get()).finishSurfaceBehindRemoteAnimation(false);
                }
            }
        });
        ofFloat.setDuration(450L);
        ofFloat.setInterpolator(Interpolators.DECELERATE_QUINT);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.keyguard.KeyguardUnlockAnimationController.3
            final /* synthetic */ KeyguardUnlockAnimationController this$0;

            {
                this.this$0 = r1;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                Intrinsics.checkNotNullParameter(valueAnimator, "valueAnimator");
                KeyguardUnlockAnimationController keyguardUnlockAnimationController = this.this$0;
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                keyguardUnlockAnimationController.surfaceBehindAlpha = ((Float) animatedValue).floatValue();
                KeyguardUnlockAnimationController keyguardUnlockAnimationController2 = this.this$0;
                Object animatedValue2 = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue2, "null cannot be cast to non-null type kotlin.Float");
                keyguardUnlockAnimationController2.setSurfaceBehindAppearAmount(((Float) animatedValue2).floatValue());
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter(this) { // from class: com.android.systemui.keyguard.KeyguardUnlockAnimationController.4
            final /* synthetic */ KeyguardUnlockAnimationController this$0;

            {
                this.this$0 = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                Intrinsics.checkNotNullParameter(animator, "animation");
                ((KeyguardViewMediator) this.this$0.keyguardViewMediator.get()).onKeyguardExitRemoteAnimationFinished(false);
            }
        });
        keyguardStateController.addCallback(this);
        this.roundedCornerRadius = (float) context.getResources().getDimensionPixelSize(17105496);
    }

    public final void notifyStartKeyguardExitAnimation(RemoteAnimationTarget remoteAnimationTarget, long j, boolean z) {
        Intrinsics.checkNotNullParameter(remoteAnimationTarget, "target");
        if (this.surfaceTransactionApplier == null) {
            this.surfaceTransactionApplier = new SyncRtSurfaceTransactionApplier(this.keyguardViewController.getViewRootImpl().getView());
        }
        this.surfaceBehindRemoteAnimationTarget = remoteAnimationTarget;
        this.surfaceBehindRemoteAnimationStartTime = j;
        if (!z) {
            this.keyguardViewController.hide(j, 350);
            this.surfaceBehindEntryAnimator.start();
        }
    }

    public final void notifyFinishedKeyguardExitAnimation() {
        this.surfaceBehindRemoteAnimationTarget = null;
    }

    public final void hideKeyguardViewAfterRemoteAnimation() {
        this.keyguardViewController.hide(this.surfaceBehindRemoteAnimationStartTime, 350);
    }

    public final boolean isUnlockingWithSmartSpaceTransition() {
        return this.unlockingWithSmartSpaceTransition;
    }

    public final void updateLockscreenSmartSpacePosition() {
        this.smartspaceTransitionController.setProgressToDestinationBounds(this.keyguardStateController.getDismissAmount() / 0.3f);
    }

    /* access modifiers changed from: private */
    public final void setSurfaceBehindAppearAmount(float f) {
        RemoteAnimationTarget remoteAnimationTarget = this.surfaceBehindRemoteAnimationTarget;
        if (remoteAnimationTarget != null) {
            Intrinsics.checkNotNull(remoteAnimationTarget);
            int height = remoteAnimationTarget.screenSpaceBounds.height();
            float clamp = (MathUtils.clamp(f, 0.0f, 1.0f) * 0.050000012f) + 0.95f;
            Matrix matrix = this.surfaceBehindMatrix;
            RemoteAnimationTarget remoteAnimationTarget2 = this.surfaceBehindRemoteAnimationTarget;
            Intrinsics.checkNotNull(remoteAnimationTarget2);
            float f2 = (float) height;
            matrix.setScale(clamp, clamp, ((float) remoteAnimationTarget2.screenSpaceBounds.width()) / 2.0f, 0.66f * f2);
            this.surfaceBehindMatrix.postTranslate(0.0f, f2 * 0.05f * (1.0f - f));
            if (!this.keyguardStateController.isSnappingKeyguardBackAfterSwipe()) {
                f = this.surfaceBehindAlpha;
            }
            RemoteAnimationTarget remoteAnimationTarget3 = this.surfaceBehindRemoteAnimationTarget;
            Intrinsics.checkNotNull(remoteAnimationTarget3);
            SyncRtSurfaceTransactionApplier.SurfaceParams build = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget3.leash).withMatrix(this.surfaceBehindMatrix).withCornerRadius(this.roundedCornerRadius).withAlpha(f).build();
            SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier = this.surfaceTransactionApplier;
            Intrinsics.checkNotNull(syncRtSurfaceTransactionApplier);
            syncRtSurfaceTransactionApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build});
        }
    }

    /* access modifiers changed from: private */
    public final void updateSurfaceBehindAppearAmount() {
        if (this.surfaceBehindRemoteAnimationTarget != null) {
            if (this.keyguardStateController.isFlingingToDismissKeyguard()) {
                setSurfaceBehindAppearAmount(this.keyguardStateController.getDismissAmount());
            } else if (this.keyguardStateController.isDismissingFromSwipe() || this.keyguardStateController.isSnappingKeyguardBackAfterSwipe()) {
                setSurfaceBehindAppearAmount((this.keyguardStateController.getDismissAmount() - 0.1f) / 0.20000002f);
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public void onKeyguardDismissAmountChanged() {
        if (KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation) {
            if (this.keyguardViewController.isShowing()) {
                updateKeyguardViewMediatorIfThresholdsReached();
                if (this.keyguardViewMediator.get().requestedShowSurfaceBehindKeyguard() || this.keyguardViewMediator.get().isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe()) {
                    updateSurfaceBehindAppearAmount();
                }
            }
            updateSmartSpaceTransition();
        }
    }

    private final void updateKeyguardViewMediatorIfThresholdsReached() {
        if (this.featureFlags.isNewKeyguardSwipeAnimationEnabled()) {
            float dismissAmount = this.keyguardStateController.getDismissAmount();
            boolean z = dismissAmount >= 1.0f || (this.keyguardStateController.isDismissingFromSwipe() && !this.keyguardStateController.isFlingingToDismissKeyguardDuringSwipeGesture() && dismissAmount >= 0.3f);
            if (dismissAmount >= 0.1f && !this.keyguardViewMediator.get().requestedShowSurfaceBehindKeyguard()) {
                this.keyguardViewMediator.get().showSurfaceBehindKeyguard();
                fadeInSurfaceBehind();
            } else if (dismissAmount < 0.1f && this.keyguardViewMediator.get().requestedShowSurfaceBehindKeyguard()) {
                this.keyguardViewMediator.get().hideSurfaceBehindKeyguard();
                fadeOutSurfaceBehind();
            } else if (this.keyguardViewMediator.get().isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe() && z) {
                this.keyguardViewMediator.get().onKeyguardExitRemoteAnimationFinished(false);
            }
        }
    }

    private final void updateSmartSpaceTransition() {
        if (this.featureFlags.isSmartSpaceSharedElementTransitionEnabled()) {
            float dismissAmount = this.keyguardStateController.getDismissAmount();
            boolean z = true;
            if (!this.attemptedSmartSpaceTransitionForThisSwipe && dismissAmount > 0.0f && dismissAmount < 1.0f && this.keyguardViewController.isShowing()) {
                this.attemptedSmartSpaceTransitionForThisSwipe = true;
                this.smartspaceTransitionController.prepareForUnlockTransition();
                if (this.keyguardStateController.canPerformSmartSpaceTransition()) {
                    this.unlockingWithSmartSpaceTransition = true;
                    ISmartspaceCallback launcherSmartspace = this.smartspaceTransitionController.getLauncherSmartspace();
                    if (launcherSmartspace != null) {
                        launcherSmartspace.setVisibility(4);
                    }
                }
            } else if (this.attemptedSmartSpaceTransitionForThisSwipe) {
                if (!(dismissAmount == 0.0f)) {
                    if (dismissAmount != 1.0f) {
                        z = false;
                    }
                    if (!z) {
                        return;
                    }
                }
                this.attemptedSmartSpaceTransitionForThisSwipe = false;
                this.unlockingWithSmartSpaceTransition = false;
                ISmartspaceCallback launcherSmartspace2 = this.smartspaceTransitionController.getLauncherSmartspace();
                if (launcherSmartspace2 != null) {
                    launcherSmartspace2.setVisibility(0);
                }
            }
        }
    }

    private final void fadeInSurfaceBehind() {
        this.surfaceBehindAlphaAnimator.cancel();
        this.surfaceBehindAlphaAnimator.start();
    }

    private final void fadeOutSurfaceBehind() {
        this.surfaceBehindAlphaAnimator.cancel();
        this.surfaceBehindAlphaAnimator.reverse();
    }
}
