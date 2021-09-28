package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.WallpaperManager;
import android.os.SystemClock;
import android.os.Trace;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.MathUtils;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewRootImpl;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.PanelExpansionListener;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationShadeDepthController.kt */
/* loaded from: classes.dex */
public final class NotificationShadeDepthController implements PanelExpansionListener, Dumpable {
    public static final Companion Companion = new Companion(null);
    private final BiometricUnlockController biometricUnlockController;
    private View blurRoot;
    private final BlurUtils blurUtils;
    private boolean blursDisabledForAppLaunch;
    private boolean brightnessMirrorVisible;
    private final Choreographer choreographer;
    private final DozeParameters dozeParameters;
    private boolean isBlurred;
    private boolean isOpen;
    private Animator keyguardAnimator;
    private final NotificationShadeDepthController$keyguardStateCallback$1 keyguardStateCallback;
    private final KeyguardStateController keyguardStateController;
    private int lastAppliedBlur;
    private Animator notificationAnimator;
    private final NotificationShadeWindowController notificationShadeWindowController;
    private int prevShadeDirection;
    private float prevShadeVelocity;
    private boolean prevTracking;
    private float qsPanelExpansion;
    public View root;
    private boolean scrimsVisible;
    private float shadeExpansion;
    private final NotificationShadeDepthController$statusBarStateCallback$1 statusBarStateCallback;
    private final StatusBarStateController statusBarStateController;
    private float transitionToFullShadeProgress;
    private boolean updateScheduled;
    private int wakeAndUnlockBlurRadius;
    private final WallpaperManager wallpaperManager;
    private boolean isClosed = true;
    private List<DepthListener> listeners = new ArrayList();
    private long prevTimestamp = -1;
    private DepthAnimation shadeSpring = new DepthAnimation(this);
    private DepthAnimation shadeAnimation = new DepthAnimation(this);
    private DepthAnimation brightnessMirrorSpring = new DepthAnimation(this);
    private final Choreographer.FrameCallback updateBlurCallback = new Choreographer.FrameCallback(this) { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$updateBlurCallback$1
        final /* synthetic */ NotificationShadeDepthController this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        /* JADX DEBUG: Failed to insert an additional move for type inference into block B:37:0x00f4 */
        /* JADX DEBUG: Multi-variable search result rejected for r6v5, resolved type: java.lang.String */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r6v1, types: [java.lang.String] */
        /* JADX WARN: Type inference failed for: r6v4, types: [java.util.Iterator] */
        @Override // android.view.Choreographer.FrameCallback
        public final void doFrame(long j) {
            String str = "DepthController";
            boolean z = false;
            this.this$0.updateScheduled = false;
            float max = (float) Math.max(Math.max(Math.max((int) ((((float) this.this$0.getShadeSpring().getRadius()) * 0.4f) + (((float) MathUtils.constrain(this.this$0.getShadeAnimation().getRadius(), this.this$0.blurUtils.getMinBlurRadius(), this.this$0.blurUtils.getMaxBlurRadius())) * 0.6f)), this.this$0.blurUtils.blurRadiusOfRatio(this.this$0.getQsPanelExpansion() * this.this$0.shadeExpansion)), this.this$0.blurUtils.blurRadiusOfRatio(this.this$0.getTransitionToFullShadeProgress())), this.this$0.wakeAndUnlockBlurRadius);
            if (this.this$0.getBlursDisabledForAppLaunch()) {
                max = 0.0f;
            }
            int i = (int) max;
            if (this.this$0.scrimsVisible) {
                i = 0;
            }
            float ratioOfBlurRadius = this.this$0.blurUtils.ratioOfBlurRadius(i);
            if (!this.this$0.blurUtils.supportsBlursOnWindows()) {
                i = 0;
            }
            int ratio = (int) (((float) i) * (1.0f - this.this$0.getBrightnessMirrorSpring().getRatio()));
            if ((this.this$0.scrimsVisible) && !this.this$0.getBlursDisabledForAppLaunch()) {
                z = true;
            }
            Trace.traceCounter(4096, "shade_blur_radius", ratio);
            BlurUtils blurUtils = this.this$0.blurUtils;
            View view = this.this$0.blurRoot;
            ViewRootImpl viewRootImpl = view == null ? null : view.getViewRootImpl();
            if (viewRootImpl == null) {
                viewRootImpl = this.this$0.getRoot().getViewRootImpl();
            }
            blurUtils.applyBlur(viewRootImpl, ratio, z);
            this.this$0.lastAppliedBlur = ratio;
            try {
                if (!this.this$0.getRoot().isAttachedToWindow() || this.this$0.getRoot().getWindowToken() == null) {
                    Log.i(str, Intrinsics.stringPlus("Won't set zoom. Window not attached ", this.this$0.getRoot()));
                } else {
                    this.this$0.wallpaperManager.setWallpaperZoomOut(this.this$0.getRoot().getWindowToken(), ratioOfBlurRadius);
                }
            } catch (IllegalArgumentException e) {
                Log.w(str, Intrinsics.stringPlus("Can't set zoom. Window is gone: ", this.this$0.getRoot().getWindowToken()), e);
            }
            str = this.this$0.listeners.iterator();
            while (str.hasNext()) {
                NotificationShadeDepthController.DepthListener depthListener = (NotificationShadeDepthController.DepthListener) str.next();
                depthListener.onWallpaperZoomOutChanged(ratioOfBlurRadius);
                depthListener.onBlurRadiusChanged(ratio);
            }
            this.this$0.notificationShadeWindowController.setBackgroundBlurRadius(ratio);
        }
    };

    /* compiled from: NotificationShadeDepthController.kt */
    /* loaded from: classes.dex */
    public interface DepthListener {
        default void onBlurRadiusChanged(int i) {
        }

        void onWallpaperZoomOutChanged(float f);
    }

    public static /* synthetic */ void getBrightnessMirrorSpring$annotations() {
    }

    public static /* synthetic */ void getShadeSpring$annotations() {
    }

    public static /* synthetic */ void getUpdateBlurCallback$annotations() {
    }

    public NotificationShadeDepthController(StatusBarStateController statusBarStateController, BlurUtils blurUtils, BiometricUnlockController biometricUnlockController, KeyguardStateController keyguardStateController, Choreographer choreographer, WallpaperManager wallpaperManager, NotificationShadeWindowController notificationShadeWindowController, DozeParameters dozeParameters, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(blurUtils, "blurUtils");
        Intrinsics.checkNotNullParameter(biometricUnlockController, "biometricUnlockController");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(choreographer, "choreographer");
        Intrinsics.checkNotNullParameter(wallpaperManager, "wallpaperManager");
        Intrinsics.checkNotNullParameter(notificationShadeWindowController, "notificationShadeWindowController");
        Intrinsics.checkNotNullParameter(dozeParameters, "dozeParameters");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.statusBarStateController = statusBarStateController;
        this.blurUtils = blurUtils;
        this.biometricUnlockController = biometricUnlockController;
        this.keyguardStateController = keyguardStateController;
        this.choreographer = choreographer;
        this.wallpaperManager = wallpaperManager;
        this.notificationShadeWindowController = notificationShadeWindowController;
        this.dozeParameters = dozeParameters;
        NotificationShadeDepthController$keyguardStateCallback$1 notificationShadeDepthController$keyguardStateCallback$1 = new KeyguardStateController.Callback(this) { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1
            final /* synthetic */ NotificationShadeDepthController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public void onKeyguardFadingAwayChanged() {
                if (this.this$0.keyguardStateController.isKeyguardFadingAway() && this.this$0.biometricUnlockController.getMode() == 1) {
                    Animator animator = this.this$0.keyguardAnimator;
                    if (animator != null) {
                        animator.cancel();
                    }
                    NotificationShadeDepthController notificationShadeDepthController = this.this$0;
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
                    NotificationShadeDepthController notificationShadeDepthController2 = this.this$0;
                    ofFloat.setDuration(notificationShadeDepthController2.dozeParameters.getWallpaperFadeOutDuration());
                    ofFloat.setStartDelay(notificationShadeDepthController2.keyguardStateController.getKeyguardFadingAwayDelay());
                    ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                    ofFloat.addUpdateListener(
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0054: INVOKE  
                          (r1v3 'ofFloat' android.animation.ValueAnimator)
                          (wrap: com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$1 : 0x0051: CONSTRUCTOR  (r2v5 com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$1 A[REMOVE]) = (r4v1 'notificationShadeDepthController2' com.android.systemui.statusbar.NotificationShadeDepthController) call: com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$1.<init>(com.android.systemui.statusbar.NotificationShadeDepthController):void type: CONSTRUCTOR)
                         type: VIRTUAL call: android.animation.ValueAnimator.addUpdateListener(android.animation.ValueAnimator$AnimatorUpdateListener):void in method: com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1.onKeyguardFadingAwayChanged():void, file: classes.dex
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                        	at jadx.core.dex.regions.Region.generate(Region.java:35)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:137)
                        	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:137)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                        	at jadx.core.dex.regions.Region.generate(Region.java:35)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                        	at jadx.core.dex.regions.Region.generate(Region.java:35)
                        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:261)
                        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:254)
                        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:349)
                        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:302)
                        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:271)
                        	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                        	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                        	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$1, state: NOT_LOADED
                        	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:976)
                        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:801)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                        	... 23 more
                        */
                    /*
                        this = this;
                        com.android.systemui.statusbar.NotificationShadeDepthController r0 = r4.this$0
                        com.android.systemui.statusbar.policy.KeyguardStateController r0 = com.android.systemui.statusbar.NotificationShadeDepthController.access$getKeyguardStateController$p(r0)
                        boolean r0 = r0.isKeyguardFadingAway()
                        if (r0 == 0) goto L_0x0067
                        com.android.systemui.statusbar.NotificationShadeDepthController r0 = r4.this$0
                        com.android.systemui.statusbar.phone.BiometricUnlockController r0 = com.android.systemui.statusbar.NotificationShadeDepthController.access$getBiometricUnlockController$p(r0)
                        int r0 = r0.getMode()
                        r1 = 1
                        if (r0 == r1) goto L_0x001a
                        goto L_0x0067
                    L_0x001a:
                        com.android.systemui.statusbar.NotificationShadeDepthController r0 = r4.this$0
                        android.animation.Animator r0 = com.android.systemui.statusbar.NotificationShadeDepthController.access$getKeyguardAnimator$p(r0)
                        if (r0 != 0) goto L_0x0023
                        goto L_0x0026
                    L_0x0023:
                        r0.cancel()
                    L_0x0026:
                        com.android.systemui.statusbar.NotificationShadeDepthController r0 = r4.this$0
                        r1 = 2
                        float[] r1 = new float[r1]
                        r1 = {x0068: FILL_ARRAY_DATA  , data: [1065353216, 0} // fill-array
                        android.animation.ValueAnimator r1 = android.animation.ValueAnimator.ofFloat(r1)
                        com.android.systemui.statusbar.NotificationShadeDepthController r4 = r4.this$0
                        com.android.systemui.statusbar.phone.DozeParameters r2 = com.android.systemui.statusbar.NotificationShadeDepthController.access$getDozeParameters$p(r4)
                        long r2 = r2.getWallpaperFadeOutDuration()
                        r1.setDuration(r2)
                        com.android.systemui.statusbar.policy.KeyguardStateController r2 = com.android.systemui.statusbar.NotificationShadeDepthController.access$getKeyguardStateController$p(r4)
                        long r2 = r2.getKeyguardFadingAwayDelay()
                        r1.setStartDelay(r2)
                        android.view.animation.Interpolator r2 = com.android.systemui.animation.Interpolators.FAST_OUT_SLOW_IN
                        r1.setInterpolator(r2)
                        com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$1 r2 = new com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$1
                        r2.<init>(r4)
                        r1.addUpdateListener(r2)
                        com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$2 r2 = new com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$2
                        r2.<init>(r4)
                        r1.addListener(r2)
                        r1.start()
                        kotlin.Unit r4 = kotlin.Unit.INSTANCE
                        com.android.systemui.statusbar.NotificationShadeDepthController.access$setKeyguardAnimator$p(r0, r1)
                    L_0x0067:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1.onKeyguardFadingAwayChanged():void");
                }

                @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
                public void onKeyguardShowingChanged() {
                    if (this.this$0.keyguardStateController.isShowing()) {
                        Animator animator = this.this$0.keyguardAnimator;
                        if (animator != null) {
                            animator.cancel();
                        }
                        Animator animator2 = this.this$0.notificationAnimator;
                        if (animator2 != null) {
                            animator2.cancel();
                        }
                    }
                }
            };
            this.keyguardStateCallback = notificationShadeDepthController$keyguardStateCallback$1;
            NotificationShadeDepthController$statusBarStateCallback$1 notificationShadeDepthController$statusBarStateCallback$1 = new StatusBarStateController.StateListener(this) { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$statusBarStateCallback$1
                final /* synthetic */ NotificationShadeDepthController this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
                public void onStateChanged(int i) {
                    NotificationShadeDepthController notificationShadeDepthController = this.this$0;
                    notificationShadeDepthController.updateShadeAnimationBlur(notificationShadeDepthController.shadeExpansion, this.this$0.prevTracking, this.this$0.prevShadeVelocity, this.this$0.prevShadeDirection);
                    this.this$0.updateShadeBlur();
                }

                @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
                public void onDozingChanged(boolean z) {
                    if (z) {
                        this.this$0.getShadeSpring().finishIfRunning();
                        this.this$0.getShadeAnimation().finishIfRunning();
                        this.this$0.getBrightnessMirrorSpring().finishIfRunning();
                    }
                }

                @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
                public void onDozeAmountChanged(float f, float f2) {
                    NotificationShadeDepthController notificationShadeDepthController = this.this$0;
                    notificationShadeDepthController.setWakeAndUnlockBlurRadius(notificationShadeDepthController.blurUtils.blurRadiusOfRatio(f2));
                    NotificationShadeDepthController.scheduleUpdate$default(this.this$0, null, 1, null);
                }
            };
            this.statusBarStateCallback = notificationShadeDepthController$statusBarStateCallback$1;
            String name = NotificationShadeDepthController.class.getName();
            Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
            dumpManager.registerDumpable(name, this);
            keyguardStateController.addCallback(notificationShadeDepthController$keyguardStateCallback$1);
            statusBarStateController.addCallback(notificationShadeDepthController$statusBarStateCallback$1);
            notificationShadeWindowController.setScrimsVisibilityListener(new Consumer<Integer>(this) { // from class: com.android.systemui.statusbar.NotificationShadeDepthController.1
                final /* synthetic */ NotificationShadeDepthController this$0;

                {
                    this.this$0 = r1;
                }

                public final void accept(Integer num) {
                    this.this$0.setScrimsVisible(num != null && num.intValue() == 2);
                }
            });
            this.shadeAnimation.setStiffness(200.0f);
            this.shadeAnimation.setDampingRatio(1.0f);
        }

        /* compiled from: NotificationShadeDepthController.kt */
        /* loaded from: classes.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }
        }

        public final View getRoot() {
            View view = this.root;
            if (view != null) {
                return view;
            }
            Intrinsics.throwUninitializedPropertyAccessException("root");
            throw null;
        }

        public final void setRoot(View view) {
            Intrinsics.checkNotNullParameter(view, "<set-?>");
            this.root = view;
        }

        public final DepthAnimation getShadeSpring() {
            return this.shadeSpring;
        }

        public final DepthAnimation getShadeAnimation() {
            return this.shadeAnimation;
        }

        public final DepthAnimation getBrightnessMirrorSpring() {
            return this.brightnessMirrorSpring;
        }

        public final void setBrightnessMirrorVisible(boolean z) {
            this.brightnessMirrorVisible = z;
            DepthAnimation.animateTo$default(this.brightnessMirrorSpring, z ? this.blurUtils.blurRadiusOfRatio(1.0f) : 0, null, 2, null);
        }

        public final float getQsPanelExpansion() {
            return this.qsPanelExpansion;
        }

        public final void setQsPanelExpansion(float f) {
            if (!(this.qsPanelExpansion == f)) {
                this.qsPanelExpansion = f;
                scheduleUpdate$default(this, null, 1, null);
            }
        }

        public final float getTransitionToFullShadeProgress() {
            return this.transitionToFullShadeProgress;
        }

        public final void setTransitionToFullShadeProgress(float f) {
            if (!(this.transitionToFullShadeProgress == f)) {
                this.transitionToFullShadeProgress = f;
                scheduleUpdate$default(this, null, 1, null);
            }
        }

        public final boolean getBlursDisabledForAppLaunch() {
            return this.blursDisabledForAppLaunch;
        }

        public final void setBlursDisabledForAppLaunch(boolean z) {
            if (this.blursDisabledForAppLaunch != z) {
                this.blursDisabledForAppLaunch = z;
                scheduleUpdate$default(this, null, 1, null);
                if (!(this.shadeSpring.getRadius() == 0 && this.shadeAnimation.getRadius() == 0) && z) {
                    DepthAnimation.animateTo$default(this.shadeSpring, 0, null, 2, null);
                    this.shadeSpring.finishIfRunning();
                    DepthAnimation.animateTo$default(this.shadeAnimation, 0, null, 2, null);
                    this.shadeAnimation.finishIfRunning();
                }
            }
        }

        /* access modifiers changed from: private */
        public final void setScrimsVisible(boolean z) {
            if (this.scrimsVisible != z) {
                this.scrimsVisible = z;
                scheduleUpdate$default(this, null, 1, null);
            }
        }

        /* access modifiers changed from: private */
        public final void setWakeAndUnlockBlurRadius(int i) {
            if (this.wakeAndUnlockBlurRadius != i) {
                this.wakeAndUnlockBlurRadius = i;
                scheduleUpdate$default(this, null, 1, null);
            }
        }

        public final void addListener(DepthListener depthListener) {
            Intrinsics.checkNotNullParameter(depthListener, "listener");
            this.listeners.add(depthListener);
        }

        public final void removeListener(DepthListener depthListener) {
            Intrinsics.checkNotNullParameter(depthListener, "listener");
            this.listeners.remove(depthListener);
        }

        @Override // com.android.systemui.statusbar.phone.PanelExpansionListener
        public void onPanelExpansionChanged(float f, boolean z) {
            long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
            if (!(this.shadeExpansion == f) || this.prevTracking != z) {
                long j = this.prevTimestamp;
                float f2 = 1.0f;
                if (j < 0) {
                    this.prevTimestamp = elapsedRealtimeNanos;
                } else {
                    f2 = MathUtils.constrain((float) (((double) (elapsedRealtimeNanos - j)) / 1.0E9d), 1.0E-5f, 1.0f);
                }
                float f3 = f - this.shadeExpansion;
                int signum = (int) Math.signum(f3);
                float constrain = MathUtils.constrain((f3 * 100.0f) / f2, -3000.0f, 3000.0f);
                updateShadeAnimationBlur(f, z, constrain, signum);
                this.prevShadeDirection = signum;
                this.prevShadeVelocity = constrain;
                this.shadeExpansion = f;
                this.prevTracking = z;
                this.prevTimestamp = elapsedRealtimeNanos;
                updateShadeBlur();
                return;
            }
            this.prevTimestamp = elapsedRealtimeNanos;
        }

        /* access modifiers changed from: private */
        public final void updateShadeAnimationBlur(float f, boolean z, float f2, int i) {
            if (!shouldApplyShadeBlur()) {
                animateBlur(false, 0.0f);
                this.isClosed = true;
                this.isOpen = false;
            } else if (f > 0.0f) {
                if (this.isClosed) {
                    animateBlur(true, f2);
                    this.isClosed = false;
                }
                if (z && !this.isBlurred) {
                    animateBlur(true, 0.0f);
                }
                if (!z && i < 0 && this.isBlurred) {
                    animateBlur(false, f2);
                }
                if (!(f == 1.0f)) {
                    this.isOpen = false;
                } else if (!this.isOpen) {
                    this.isOpen = true;
                    if (!this.isBlurred) {
                        animateBlur(true, f2);
                    }
                }
            } else if (!this.isClosed) {
                this.isClosed = true;
                if (this.isBlurred) {
                    animateBlur(false, f2);
                }
            }
        }

        private final void animateBlur(boolean z, float f) {
            this.isBlurred = z;
            float f2 = (!z || !shouldApplyShadeBlur()) ? 0.0f : 1.0f;
            this.shadeAnimation.setStartVelocity(f);
            DepthAnimation.animateTo$default(this.shadeAnimation, this.blurUtils.blurRadiusOfRatio(f2), null, 2, null);
        }

        /* access modifiers changed from: private */
        public final void updateShadeBlur() {
            DepthAnimation.animateTo$default(this.shadeSpring, shouldApplyShadeBlur() ? this.blurUtils.blurRadiusOfRatio(this.shadeExpansion) : 0, null, 2, null);
        }

        /* access modifiers changed from: package-private */
        public static /* synthetic */ void scheduleUpdate$default(NotificationShadeDepthController notificationShadeDepthController, View view, int i, Object obj) {
            if ((i & 1) != 0) {
                view = null;
            }
            notificationShadeDepthController.scheduleUpdate(view);
        }

        /* access modifiers changed from: private */
        public final void scheduleUpdate(View view) {
            if (!this.updateScheduled) {
                this.updateScheduled = true;
                this.blurRoot = view;
                this.choreographer.postFrameCallback(this.updateBlurCallback);
            }
        }

        private final boolean shouldApplyShadeBlur() {
            int state = this.statusBarStateController.getState();
            return (state == 0 || state == 2) && !this.keyguardStateController.isKeyguardFadingAway();
        }

        @Override // com.android.systemui.Dumpable
        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(strArr, "args");
            IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
            indentingPrintWriter.println("StatusBarWindowBlurController:");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println(Intrinsics.stringPlus("shadeRadius: ", Integer.valueOf(getShadeSpring().getRadius())));
            indentingPrintWriter.println(Intrinsics.stringPlus("shadeAnimation: ", Integer.valueOf(getShadeAnimation().getRadius())));
            indentingPrintWriter.println(Intrinsics.stringPlus("brightnessMirrorRadius: ", Integer.valueOf(getBrightnessMirrorSpring().getRadius())));
            indentingPrintWriter.println(Intrinsics.stringPlus("wakeAndUnlockBlur: ", Integer.valueOf(this.wakeAndUnlockBlurRadius)));
            indentingPrintWriter.println(Intrinsics.stringPlus("blursDisabledForAppLaunch: ", Boolean.valueOf(getBlursDisabledForAppLaunch())));
            indentingPrintWriter.println(Intrinsics.stringPlus("qsPanelExpansion: ", Float.valueOf(getQsPanelExpansion())));
            indentingPrintWriter.println(Intrinsics.stringPlus("transitionToFullShadeProgress: ", Float.valueOf(getTransitionToFullShadeProgress())));
            indentingPrintWriter.println(Intrinsics.stringPlus("lastAppliedBlur: ", Integer.valueOf(this.lastAppliedBlur)));
        }

        /* compiled from: NotificationShadeDepthController.kt */
        /* loaded from: classes.dex */
        public final class DepthAnimation {
            private int pendingRadius = -1;
            private int radius;
            private SpringAnimation springAnimation;
            final /* synthetic */ NotificationShadeDepthController this$0;
            private View view;

            /* JADX WARN: Incorrect args count in method signature: ()V */
            public DepthAnimation(NotificationShadeDepthController notificationShadeDepthController) {
                Intrinsics.checkNotNullParameter(notificationShadeDepthController, "this$0");
                this.this$0 = notificationShadeDepthController;
                SpringAnimation springAnimation = new SpringAnimation(this, new NotificationShadeDepthController$DepthAnimation$springAnimation$1(this, notificationShadeDepthController));
                this.springAnimation = springAnimation;
                springAnimation.setSpring(new SpringForce(0.0f));
                this.springAnimation.getSpring().setDampingRatio(1.0f);
                this.springAnimation.getSpring().setStiffness(10000.0f);
                this.springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener(this) { // from class: com.android.systemui.statusbar.NotificationShadeDepthController.DepthAnimation.1
                    final /* synthetic */ DepthAnimation this$0;

                    {
                        this.this$0 = r1;
                    }

                    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                        this.this$0.pendingRadius = -1;
                    }
                });
            }

            public final int getRadius() {
                return this.radius;
            }

            public final void setRadius(int i) {
                this.radius = i;
            }

            public final float getRatio() {
                return this.this$0.blurUtils.ratioOfBlurRadius(this.radius);
            }

            public static /* synthetic */ void animateTo$default(DepthAnimation depthAnimation, int i, View view, int i2, Object obj) {
                if ((i2 & 2) != 0) {
                    view = null;
                }
                depthAnimation.animateTo(i, view);
            }

            public final void animateTo(int i, View view) {
                if (this.pendingRadius != i || !Intrinsics.areEqual(this.view, view)) {
                    this.view = view;
                    this.pendingRadius = i;
                    this.springAnimation.animateToFinalPosition((float) i);
                }
            }

            public final void finishIfRunning() {
                if (this.springAnimation.isRunning()) {
                    this.springAnimation.skipToEnd();
                }
            }

            public final void setStiffness(float f) {
                this.springAnimation.getSpring().setStiffness(f);
            }

            public final void setDampingRatio(float f) {
                this.springAnimation.getSpring().setDampingRatio(f);
            }

            public final void setStartVelocity(float f) {
                this.springAnimation.setStartVelocity(f);
            }
        }
    }
