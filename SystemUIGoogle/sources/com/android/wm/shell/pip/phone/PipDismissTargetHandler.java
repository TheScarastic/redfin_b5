package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.TransitionDrawable;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.DismissCircleView;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import com.android.wm.shell.pip.PipUiEventLogger;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function5;
/* loaded from: classes2.dex */
public class PipDismissTargetHandler implements ViewTreeObserver.OnPreDrawListener {
    private final Context mContext;
    private int mDismissAreaHeight;
    private boolean mEnableDismissDragToEdge;
    private boolean mHasDismissTargetSurface;
    private MagnetizedObject.MagneticTarget mMagneticTarget;
    private PhysicsAnimator<View> mMagneticTargetAnimator;
    private MagnetizedObject<Rect> mMagnetizedPip;
    private final ShellExecutor mMainExecutor;
    private final PipMotionHelper mMotionHelper;
    private final PipUiEventLogger mPipUiEventLogger;
    private int mTargetSize;
    private DismissCircleView mTargetView;
    private ViewGroup mTargetViewContainer;
    private SurfaceControl mTaskLeash;
    private final WindowManager mWindowManager;
    private final PhysicsAnimator.SpringConfig mTargetSpringConfig = new PhysicsAnimator.SpringConfig(200.0f, 0.75f);
    private float mMagneticFieldRadiusPercent = 1.0f;

    public PipDismissTargetHandler(Context context, PipUiEventLogger pipUiEventLogger, PipMotionHelper pipMotionHelper, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mPipUiEventLogger = pipUiEventLogger;
        this.mMotionHelper = pipMotionHelper;
        this.mMainExecutor = shellExecutor;
        this.mWindowManager = (WindowManager) context.getSystemService("window");
    }

    public void init() {
        Resources resources = this.mContext.getResources();
        this.mEnableDismissDragToEdge = resources.getBoolean(R.bool.config_pipEnableDismissDragToEdge);
        this.mDismissAreaHeight = resources.getDimensionPixelSize(R.dimen.floating_dismiss_gradient_height);
        this.mTargetView = new DismissCircleView(this.mContext);
        FrameLayout frameLayout = new FrameLayout(this.mContext);
        this.mTargetViewContainer = frameLayout;
        frameLayout.setBackgroundDrawable(this.mContext.getDrawable(R.drawable.floating_dismiss_gradient_transition));
        this.mTargetViewContainer.setClipChildren(false);
        this.mTargetViewContainer.addView(this.mTargetView);
        MagnetizedObject<Rect> magnetizedPip = this.mMotionHelper.getMagnetizedPip();
        this.mMagnetizedPip = magnetizedPip;
        this.mMagneticTarget = magnetizedPip.addTarget(this.mTargetView, 0);
        updateMagneticTargetSize();
        this.mMagnetizedPip.setAnimateStuckToTarget(new Function5() { // from class: com.android.wm.shell.pip.phone.PipDismissTargetHandler$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function5
            public final Object invoke(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                return PipDismissTargetHandler.this.lambda$init$0((MagnetizedObject.MagneticTarget) obj, (Float) obj2, (Float) obj3, (Boolean) obj4, (Function0) obj5);
            }
        });
        this.mMagnetizedPip.setMagnetListener(new MagnetizedObject.MagnetListener() { // from class: com.android.wm.shell.pip.phone.PipDismissTargetHandler.1
            @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject.MagnetListener
            public void onStuckToTarget(MagnetizedObject.MagneticTarget magneticTarget) {
                if (PipDismissTargetHandler.this.mEnableDismissDragToEdge) {
                    PipDismissTargetHandler.this.showDismissTargetMaybe();
                }
            }

            @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject.MagnetListener
            public void onUnstuckFromTarget(MagnetizedObject.MagneticTarget magneticTarget, float f, float f2, boolean z) {
                if (z) {
                    PipDismissTargetHandler.this.mMotionHelper.flingToSnapTarget(f, f2, null);
                    PipDismissTargetHandler.this.hideDismissTargetMaybe();
                    return;
                }
                PipDismissTargetHandler.this.mMotionHelper.setSpringingToTouch(true);
            }

            @Override // com.android.wm.shell.common.magnetictarget.MagnetizedObject.MagnetListener
            public void onReleasedInTarget(MagnetizedObject.MagneticTarget magneticTarget) {
                PipDismissTargetHandler.this.mMainExecutor.executeDelayed(new PipDismissTargetHandler$1$$ExternalSyntheticLambda0(this), 0);
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onReleasedInTarget$0() {
                PipDismissTargetHandler.this.mMotionHelper.notifyDismissalPending();
                PipDismissTargetHandler.this.mMotionHelper.animateDismiss();
                PipDismissTargetHandler.this.hideDismissTargetMaybe();
                PipDismissTargetHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_DRAG_TO_REMOVE);
            }
        });
        this.mMagneticTargetAnimator = PhysicsAnimator.getInstance(this.mTargetView);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$init$0(MagnetizedObject.MagneticTarget magneticTarget, Float f, Float f2, Boolean bool, Function0 function0) {
        if (this.mEnableDismissDragToEdge) {
            this.mMotionHelper.animateIntoDismissTarget(magneticTarget, f.floatValue(), f2.floatValue(), bool.booleanValue(), function0);
        }
        return Unit.INSTANCE;
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        this.mTargetViewContainer.getViewTreeObserver().removeOnPreDrawListener(this);
        this.mHasDismissTargetSurface = true;
        updateDismissTargetLayer();
        return true;
    }

    public boolean maybeConsumeMotionEvent(MotionEvent motionEvent) {
        return this.mMagnetizedPip.maybeConsumeMotionEvent(motionEvent);
    }

    public void updateMagneticTargetSize() {
        if (this.mTargetView != null) {
            Resources resources = this.mContext.getResources();
            this.mTargetSize = resources.getDimensionPixelSize(R.dimen.dismiss_circle_size);
            this.mDismissAreaHeight = resources.getDimensionPixelSize(R.dimen.floating_dismiss_gradient_height);
            int i = this.mTargetSize;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(i, i);
            layoutParams.gravity = 81;
            layoutParams.bottomMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.floating_dismiss_bottom_margin);
            this.mTargetView.setLayoutParams(layoutParams);
            setMagneticFieldRadiusPercent(this.mMagneticFieldRadiusPercent);
        }
    }

    public void setMagneticFieldRadiusPercent(float f) {
        this.mMagneticFieldRadiusPercent = f;
        this.mMagneticTarget.setMagneticFieldRadiusPx((int) (f * ((float) this.mTargetSize) * 1.25f));
    }

    public void setTaskLeash(SurfaceControl surfaceControl) {
        this.mTaskLeash = surfaceControl;
    }

    private void updateDismissTargetLayer() {
        if (this.mHasDismissTargetSurface && this.mTaskLeash != null) {
            SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
            transaction.setRelativeLayer(this.mTargetViewContainer.getViewRootImpl().getSurfaceControl(), this.mTaskLeash, -1);
            transaction.apply();
        }
    }

    public void createOrUpdateDismissTarget() {
        if (!this.mTargetViewContainer.isAttachedToWindow()) {
            this.mMagneticTargetAnimator.cancel();
            this.mTargetViewContainer.setVisibility(4);
            this.mTargetViewContainer.getViewTreeObserver().removeOnPreDrawListener(this);
            this.mHasDismissTargetSurface = false;
            try {
                this.mWindowManager.addView(this.mTargetViewContainer, getDismissTargetLayoutParams());
            } catch (IllegalStateException unused) {
                this.mWindowManager.updateViewLayout(this.mTargetViewContainer, getDismissTargetLayoutParams());
            }
        } else {
            this.mWindowManager.updateViewLayout(this.mTargetViewContainer, getDismissTargetLayoutParams());
        }
    }

    private WindowManager.LayoutParams getDismissTargetLayoutParams() {
        Point point = new Point();
        this.mWindowManager.getDefaultDisplay().getRealSize(point);
        int i = this.mDismissAreaHeight;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, i, 0, point.y - i, 2024, 280, -3);
        layoutParams.setTitle("pip-dismiss-overlay");
        layoutParams.privateFlags |= 16;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.setFitInsetsTypes(0);
        return layoutParams;
    }

    public void showDismissTargetMaybe() {
        if (this.mEnableDismissDragToEdge) {
            createOrUpdateDismissTarget();
            if (this.mTargetViewContainer.getVisibility() != 0) {
                this.mTargetView.setTranslationY((float) this.mTargetViewContainer.getHeight());
                this.mTargetViewContainer.setVisibility(0);
                this.mTargetViewContainer.getViewTreeObserver().addOnPreDrawListener(this);
                this.mMagneticTargetAnimator.cancel();
                this.mMagneticTargetAnimator.spring(DynamicAnimation.TRANSLATION_Y, 0.0f, this.mTargetSpringConfig).start();
                ((TransitionDrawable) this.mTargetViewContainer.getBackground()).startTransition(200);
            }
        }
    }

    public void hideDismissTargetMaybe() {
        if (this.mEnableDismissDragToEdge) {
            this.mMagneticTargetAnimator.spring(DynamicAnimation.TRANSLATION_Y, (float) this.mTargetViewContainer.getHeight(), this.mTargetSpringConfig).withEndActions(new Runnable() { // from class: com.android.wm.shell.pip.phone.PipDismissTargetHandler$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PipDismissTargetHandler.this.lambda$hideDismissTargetMaybe$1();
                }
            }).start();
            ((TransitionDrawable) this.mTargetViewContainer.getBackground()).reverseTransition(200);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$hideDismissTargetMaybe$1() {
        this.mTargetViewContainer.setVisibility(8);
    }

    public void cleanUpDismissTarget() {
        if (this.mTargetViewContainer.isAttachedToWindow()) {
            this.mWindowManager.removeViewImmediate(this.mTargetViewContainer);
        }
    }
}
