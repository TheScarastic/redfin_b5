package com.android.wm.shell.common.magnetictarget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MagnetizedObject.kt */
/* loaded from: classes2.dex */
public abstract class MagnetizedObject<T> {
    public static final Companion Companion = new Companion(null);
    private static boolean hapticSettingObserverInitialized;
    private static boolean systemHapticsEnabled;
    private final PhysicsAnimator<T> animator;
    private final Context context;
    private PhysicsAnimator.SpringConfig flungIntoTargetSpringConfig;
    public MagnetListener magnetListener;
    private boolean movedBeyondSlop;
    private PhysicsAnimator.EndListener<T> physicsAnimatorEndListener;
    private PhysicsAnimator.UpdateListener<T> physicsAnimatorUpdateListener;
    private PhysicsAnimator.SpringConfig springConfig;
    private MagneticTarget targetObjectIsStuckTo;
    private int touchSlop;
    private final T underlyingObject;
    private final VelocityTracker velocityTracker;
    private final Vibrator vibrator;
    private final FloatPropertyCompat<? super T> xProperty;
    private final FloatPropertyCompat<? super T> yProperty;
    private final int[] objectLocationOnScreen = new int[2];
    private final ArrayList<MagneticTarget> associatedTargets = new ArrayList<>();
    private PointF touchDown = new PointF();
    private Function5<? super MagneticTarget, ? super Float, ? super Float, ? super Boolean, ? super Function0<Unit>, Unit> animateStuckToTarget = new Function5<MagneticTarget, Float, Float, Boolean, Function0<? extends Unit>, Unit>(this) { // from class: com.android.wm.shell.common.magnetictarget.MagnetizedObject$animateStuckToTarget$1
        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object] */
        @Override // kotlin.jvm.functions.Function5
        public /* bridge */ /* synthetic */ Unit invoke(MagnetizedObject.MagneticTarget magneticTarget, Float f, Float f2, Boolean bool, Function0<? extends Unit> function0) {
            invoke(magneticTarget, f.floatValue(), f2.floatValue(), bool.booleanValue(), (Function0<Unit>) function0);
            return Unit.INSTANCE;
        }

        public final void invoke(MagnetizedObject.MagneticTarget magneticTarget, float f, float f2, boolean z, Function0<Unit> function0) {
            Intrinsics.checkNotNullParameter(magneticTarget, "p0");
            ((MagnetizedObject) this.receiver).animateStuckToTargetInternal(magneticTarget, f, f2, z, function0);
        }
    };
    private boolean flingToTargetEnabled = true;
    private float flingToTargetWidthPercent = 3.0f;
    private float flingToTargetMinVelocity = 4000.0f;
    private float flingUnstuckFromTargetMinVelocity = 4000.0f;
    private float stickToTargetMaxXVelocity = 2000.0f;
    private boolean hapticsEnabled = true;

    /* compiled from: MagnetizedObject.kt */
    /* loaded from: classes2.dex */
    public interface MagnetListener {
        void onReleasedInTarget(MagneticTarget magneticTarget);

        void onStuckToTarget(MagneticTarget magneticTarget);

        void onUnstuckFromTarget(MagneticTarget magneticTarget, float f, float f2, boolean z);
    }

    public abstract float getHeight(T t);

    public abstract void getLocationOnScreen(T t, int[] iArr);

    public abstract float getWidth(T t);

    public MagnetizedObject(Context context, T t, FloatPropertyCompat<? super T> floatPropertyCompat, FloatPropertyCompat<? super T> floatPropertyCompat2) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(t, "underlyingObject");
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "xProperty");
        Intrinsics.checkNotNullParameter(floatPropertyCompat2, "yProperty");
        this.context = context;
        this.underlyingObject = t;
        this.xProperty = floatPropertyCompat;
        this.yProperty = floatPropertyCompat2;
        this.animator = PhysicsAnimator.Companion.getInstance(t);
        VelocityTracker obtain = VelocityTracker.obtain();
        Intrinsics.checkNotNullExpressionValue(obtain, "obtain()");
        this.velocityTracker = obtain;
        Object systemService = context.getSystemService("vibrator");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.os.Vibrator");
        this.vibrator = (Vibrator) systemService;
        PhysicsAnimator.SpringConfig springConfig = new PhysicsAnimator.SpringConfig(1500.0f, 1.0f);
        this.springConfig = springConfig;
        this.flungIntoTargetSpringConfig = springConfig;
        Companion.initHapticSettingObserver(context);
    }

    public final T getUnderlyingObject() {
        return this.underlyingObject;
    }

    public final boolean getObjectStuckToTarget() {
        return this.targetObjectIsStuckTo != null;
    }

    public final MagnetListener getMagnetListener() {
        MagnetListener magnetListener = this.magnetListener;
        if (magnetListener != null) {
            return magnetListener;
        }
        Intrinsics.throwUninitializedPropertyAccessException("magnetListener");
        throw null;
    }

    public final void setMagnetListener(MagnetListener magnetListener) {
        Intrinsics.checkNotNullParameter(magnetListener, "<set-?>");
        this.magnetListener = magnetListener;
    }

    public final void setAnimateStuckToTarget(Function5<? super MagneticTarget, ? super Float, ? super Float, ? super Boolean, ? super Function0<Unit>, Unit> function5) {
        Intrinsics.checkNotNullParameter(function5, "<set-?>");
        this.animateStuckToTarget = function5;
    }

    public final float getFlingToTargetWidthPercent() {
        return this.flingToTargetWidthPercent;
    }

    public final void setFlingToTargetWidthPercent(float f) {
        this.flingToTargetWidthPercent = f;
    }

    public final float getFlingToTargetMinVelocity() {
        return this.flingToTargetMinVelocity;
    }

    public final void setFlingToTargetMinVelocity(float f) {
        this.flingToTargetMinVelocity = f;
    }

    public final float getStickToTargetMaxXVelocity() {
        return this.stickToTargetMaxXVelocity;
    }

    public final void setStickToTargetMaxXVelocity(float f) {
        this.stickToTargetMaxXVelocity = f;
    }

    public final void setHapticsEnabled(boolean z) {
        this.hapticsEnabled = z;
    }

    public final void addTarget(MagneticTarget magneticTarget) {
        Intrinsics.checkNotNullParameter(magneticTarget, "target");
        this.associatedTargets.add(magneticTarget);
        magneticTarget.updateLocationOnScreen();
    }

    public final MagneticTarget addTarget(View view, int i) {
        Intrinsics.checkNotNullParameter(view, "target");
        MagneticTarget magneticTarget = new MagneticTarget(view, i);
        addTarget(magneticTarget);
        return magneticTarget;
    }

    public final boolean maybeConsumeMotionEvent(MotionEvent motionEvent) {
        T t;
        boolean z;
        Intrinsics.checkNotNullParameter(motionEvent, "ev");
        if (this.associatedTargets.size() == 0) {
            return false;
        }
        MagneticTarget magneticTarget = null;
        if (motionEvent.getAction() == 0) {
            updateTargetViews$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
            this.velocityTracker.clear();
            this.targetObjectIsStuckTo = null;
            this.touchDown.set(motionEvent.getRawX(), motionEvent.getRawY());
            this.movedBeyondSlop = false;
        }
        addMovement(motionEvent);
        if (!this.movedBeyondSlop) {
            if (((float) Math.hypot((double) (motionEvent.getRawX() - this.touchDown.x), (double) (motionEvent.getRawY() - this.touchDown.y))) <= ((float) this.touchSlop)) {
                return false;
            }
            this.movedBeyondSlop = true;
        }
        Iterator<T> it = this.associatedTargets.iterator();
        while (true) {
            if (!it.hasNext()) {
                t = null;
                break;
            }
            t = it.next();
            MagneticTarget magneticTarget2 = (MagneticTarget) t;
            if (((float) Math.hypot((double) (motionEvent.getRawX() - magneticTarget2.getCenterOnScreen().x), (double) (motionEvent.getRawY() - magneticTarget2.getCenterOnScreen().y))) < ((float) magneticTarget2.getMagneticFieldRadiusPx())) {
                z = true;
                continue;
            } else {
                z = false;
                continue;
            }
            if (z) {
                break;
            }
        }
        MagneticTarget magneticTarget3 = (MagneticTarget) t;
        boolean z2 = !getObjectStuckToTarget() && magneticTarget3 != null;
        boolean z3 = getObjectStuckToTarget() && magneticTarget3 != null && !Intrinsics.areEqual(this.targetObjectIsStuckTo, magneticTarget3);
        if (z2 || z3) {
            this.velocityTracker.computeCurrentVelocity(1000);
            float xVelocity = this.velocityTracker.getXVelocity();
            float yVelocity = this.velocityTracker.getYVelocity();
            if (z2 && Math.abs(xVelocity) > this.stickToTargetMaxXVelocity) {
                return false;
            }
            this.targetObjectIsStuckTo = magneticTarget3;
            cancelAnimations$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
            MagnetListener magnetListener = getMagnetListener();
            Intrinsics.checkNotNull(magneticTarget3);
            magnetListener.onStuckToTarget(magneticTarget3);
            this.animateStuckToTarget.invoke(magneticTarget3, Float.valueOf(xVelocity), Float.valueOf(yVelocity), Boolean.FALSE, null);
            vibrateIfEnabled(5);
        } else if (magneticTarget3 == null && getObjectStuckToTarget()) {
            this.velocityTracker.computeCurrentVelocity(1000);
            cancelAnimations$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
            MagnetListener magnetListener2 = getMagnetListener();
            MagneticTarget magneticTarget4 = this.targetObjectIsStuckTo;
            Intrinsics.checkNotNull(magneticTarget4);
            magnetListener2.onUnstuckFromTarget(magneticTarget4, this.velocityTracker.getXVelocity(), this.velocityTracker.getYVelocity(), false);
            this.targetObjectIsStuckTo = null;
            vibrateIfEnabled(2);
        }
        if (motionEvent.getAction() != 1) {
            return getObjectStuckToTarget();
        }
        this.velocityTracker.computeCurrentVelocity(1000);
        float xVelocity2 = this.velocityTracker.getXVelocity();
        float yVelocity2 = this.velocityTracker.getYVelocity();
        cancelAnimations$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
        if (getObjectStuckToTarget()) {
            if ((-yVelocity2) > this.flingUnstuckFromTargetMinVelocity) {
                MagnetListener magnetListener3 = getMagnetListener();
                MagneticTarget magneticTarget5 = this.targetObjectIsStuckTo;
                Intrinsics.checkNotNull(magneticTarget5);
                magnetListener3.onUnstuckFromTarget(magneticTarget5, xVelocity2, yVelocity2, true);
            } else {
                MagnetListener magnetListener4 = getMagnetListener();
                MagneticTarget magneticTarget6 = this.targetObjectIsStuckTo;
                Intrinsics.checkNotNull(magneticTarget6);
                magnetListener4.onReleasedInTarget(magneticTarget6);
                vibrateIfEnabled(5);
            }
            this.targetObjectIsStuckTo = null;
            return true;
        }
        Iterator<T> it2 = this.associatedTargets.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            T next = it2.next();
            if (isForcefulFlingTowardsTarget((MagneticTarget) next, motionEvent.getRawX(), motionEvent.getRawY(), xVelocity2, yVelocity2)) {
                magneticTarget = next;
                break;
            }
        }
        MagneticTarget magneticTarget7 = magneticTarget;
        if (magneticTarget7 == null) {
            return false;
        }
        getMagnetListener().onStuckToTarget(magneticTarget7);
        this.targetObjectIsStuckTo = magneticTarget7;
        this.animateStuckToTarget.invoke(magneticTarget7, Float.valueOf(xVelocity2), Float.valueOf(yVelocity2), Boolean.TRUE, new Function0<Unit>(this, magneticTarget7) { // from class: com.android.wm.shell.common.magnetictarget.MagnetizedObject$maybeConsumeMotionEvent$1
            final /* synthetic */ MagnetizedObject.MagneticTarget $flungToTarget;
            final /* synthetic */ MagnetizedObject<T> this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$flungToTarget = r2;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                this.this$0.getMagnetListener().onReleasedInTarget(this.$flungToTarget);
                ((MagnetizedObject) this.this$0).targetObjectIsStuckTo = null;
                this.this$0.vibrateIfEnabled(5);
            }
        });
        return true;
    }

    @SuppressLint({"MissingPermission"})
    public final void vibrateIfEnabled(int i) {
        if (this.hapticsEnabled && systemHapticsEnabled) {
            this.vibrator.vibrate(VibrationEffect.createPredefined(i));
        }
    }

    private final void addMovement(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        motionEvent.offsetLocation(rawX, rawY);
        this.velocityTracker.addMovement(motionEvent);
        motionEvent.offsetLocation(-rawX, -rawY);
    }

    /* JADX DEBUG: Type inference failed for r5v0. Raw type applied. Possible types: T, ? super T */
    /* JADX DEBUG: Type inference failed for r1v14. Raw type applied. Possible types: T, ? super T */
    public final void animateStuckToTargetInternal(MagneticTarget magneticTarget, float f, float f2, boolean z, Function0<Unit> function0) {
        magneticTarget.updateLocationOnScreen();
        getLocationOnScreen(this.underlyingObject, this.objectLocationOnScreen);
        float width = (magneticTarget.getCenterOnScreen().x - (getWidth(this.underlyingObject) / 2.0f)) - ((float) this.objectLocationOnScreen[0]);
        float height = (magneticTarget.getCenterOnScreen().y - (getHeight(this.underlyingObject) / 2.0f)) - ((float) this.objectLocationOnScreen[1]);
        PhysicsAnimator.SpringConfig springConfig = z ? this.flungIntoTargetSpringConfig : this.springConfig;
        cancelAnimations$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
        PhysicsAnimator<T> physicsAnimator = this.animator;
        FloatPropertyCompat<? super T> floatPropertyCompat = this.xProperty;
        PhysicsAnimator<T> spring = physicsAnimator.spring(floatPropertyCompat, floatPropertyCompat.getValue((T) this.underlyingObject) + width, f, springConfig);
        FloatPropertyCompat<? super T> floatPropertyCompat2 = this.yProperty;
        spring.spring(floatPropertyCompat2, floatPropertyCompat2.getValue((T) this.underlyingObject) + height, f2, springConfig);
        PhysicsAnimator.UpdateListener<T> updateListener = this.physicsAnimatorUpdateListener;
        if (updateListener != null) {
            PhysicsAnimator<T> physicsAnimator2 = this.animator;
            Intrinsics.checkNotNull(updateListener);
            physicsAnimator2.addUpdateListener(updateListener);
        }
        PhysicsAnimator.EndListener<T> endListener = this.physicsAnimatorEndListener;
        if (endListener != null) {
            PhysicsAnimator<T> physicsAnimator3 = this.animator;
            Intrinsics.checkNotNull(endListener);
            physicsAnimator3.addEndListener(endListener);
        }
        if (function0 != null) {
            this.animator.withEndActions(function0);
        }
        this.animator.start();
    }

    private final boolean isForcefulFlingTowardsTarget(MagneticTarget magneticTarget, float f, float f2, float f3, float f4) {
        if (!this.flingToTargetEnabled) {
            return false;
        }
        if (!(f2 >= magneticTarget.getCenterOnScreen().y ? f4 < this.flingToTargetMinVelocity : f4 > this.flingToTargetMinVelocity)) {
            return false;
        }
        if (!(f3 == 0.0f)) {
            float f5 = f4 / f3;
            f = (magneticTarget.getCenterOnScreen().y - (f2 - (f * f5))) / f5;
        }
        float width = (((float) magneticTarget.getTargetView().getWidth()) * this.flingToTargetWidthPercent) / ((float) 2);
        if (f <= magneticTarget.getCenterOnScreen().x - width || f >= magneticTarget.getCenterOnScreen().x + width) {
            return false;
        }
        return true;
    }

    public final void cancelAnimations$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() {
        this.animator.cancel(this.xProperty, this.yProperty);
    }

    public final void updateTargetViews$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() {
        for (MagneticTarget magneticTarget : this.associatedTargets) {
            magneticTarget.updateLocationOnScreen();
        }
        if (this.associatedTargets.size() > 0) {
            this.touchSlop = ViewConfiguration.get(this.associatedTargets.get(0).getTargetView().getContext()).getScaledTouchSlop();
        }
    }

    /* compiled from: MagnetizedObject.kt */
    /* loaded from: classes2.dex */
    public static final class MagneticTarget {
        private int magneticFieldRadiusPx;
        private final View targetView;
        private final PointF centerOnScreen = new PointF();
        private final int[] tempLoc = new int[2];

        public MagneticTarget(View view, int i) {
            Intrinsics.checkNotNullParameter(view, "targetView");
            this.targetView = view;
            this.magneticFieldRadiusPx = i;
        }

        public final View getTargetView() {
            return this.targetView;
        }

        public final int getMagneticFieldRadiusPx() {
            return this.magneticFieldRadiusPx;
        }

        public final void setMagneticFieldRadiusPx(int i) {
            this.magneticFieldRadiusPx = i;
        }

        public final PointF getCenterOnScreen() {
            return this.centerOnScreen;
        }

        public final void updateLocationOnScreen() {
            this.targetView.post(new MagnetizedObject$MagneticTarget$updateLocationOnScreen$1(this));
        }
    }

    /* compiled from: MagnetizedObject.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final void initHapticSettingObserver(Context context) {
            if (!MagnetizedObject.hapticSettingObserverInitialized) {
                MagnetizedObject$Companion$initHapticSettingObserver$hapticSettingObserver$1 magnetizedObject$Companion$initHapticSettingObserver$hapticSettingObserver$1 = new MagnetizedObject$Companion$initHapticSettingObserver$hapticSettingObserver$1(context, Handler.getMain());
                context.getContentResolver().registerContentObserver(Settings.System.getUriFor("haptic_feedback_enabled"), true, magnetizedObject$Companion$initHapticSettingObserver$hapticSettingObserver$1);
                magnetizedObject$Companion$initHapticSettingObserver$hapticSettingObserver$1.onChange(false);
                MagnetizedObject.hapticSettingObserverInitialized = true;
            }
        }
    }
}
