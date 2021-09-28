package com.google.android.systemui.elmyra.feedback;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.IRotationWatcher;
import android.view.IWindowManager;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
import java.util.List;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class SquishyViewController implements FeedbackEffect {
    private static final Interpolator SQUISH_TRANSLATION_MAP = new PathInterpolator(0.4f, 0.0f, 0.6f, 1.0f);
    private AnimatorSet mAnimatorSet;
    private final Context mContext;
    private float mLastPressure;
    private float mPressure;
    private final IRotationWatcher.Stub mRotationWatcher;
    private int mScreenRotation;
    private final IWindowManager mWindowManager;
    private final List<View> mLeftViews = new ArrayList();
    private final List<View> mRightViews = new ArrayList();
    private final float mSquishTranslationMax = px(8.0f);

    public SquishyViewController(Context context) {
        AnonymousClass1 r0 = new IRotationWatcher.Stub() { // from class: com.google.android.systemui.elmyra.feedback.SquishyViewController.1
            public void onRotationChanged(int i) {
                SquishyViewController.this.mScreenRotation = i;
            }
        };
        this.mRotationWatcher = r0;
        this.mContext = context;
        IWindowManager asInterface = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
        this.mWindowManager = asInterface;
        try {
            this.mScreenRotation = asInterface.watchRotation(r0, context.getDisplay().getDisplayId());
        } catch (RemoteException e) {
            Log.e("SquishyViewController", "Couldn't get screen rotation or set watcher", e);
            this.mScreenRotation = 0;
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onProgress(float f, int i) {
        float min = Math.min(f, 1.0f) / 1.0f;
        if (min != 0.0f) {
            this.mPressure = (1.0f * min) + (this.mLastPressure * 0.0f);
        } else {
            this.mPressure = min;
        }
        AnimatorSet animatorSet = this.mAnimatorSet;
        if (animatorSet == null || !animatorSet.isRunning()) {
            if (min - this.mLastPressure < -0.1f) {
                AnimatorSet createSpringbackAnimatorSets = createSpringbackAnimatorSets();
                this.mAnimatorSet = createSpringbackAnimatorSets;
                createSpringbackAnimatorSets.start();
            } else {
                translateViews(this.mSquishTranslationMax * SQUISH_TRANSLATION_MAP.getInterpolation(this.mPressure));
            }
        }
        this.mLastPressure = this.mPressure;
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onRelease() {
        onProgress(0.0f, 0);
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        onProgress(0.0f, 0);
    }

    private AnimatorSet createSpringbackAnimatorSets() {
        AnimatorSet animatorSet = new AnimatorSet();
        for (int i = 0; i < this.mLeftViews.size(); i++) {
            animatorSet.play(createSpringbackAnimatorSet(this.mLeftViews.get(i)));
        }
        for (int i2 = 0; i2 < this.mRightViews.size(); i2++) {
            animatorSet.play(createSpringbackAnimatorSet(this.mRightViews.get(i2)));
        }
        return animatorSet;
    }

    private AnimatorSet createSpringbackAnimatorSet(View view) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, view.getTranslationX(), 0.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.getTranslationY(), 0.0f);
        ofFloat.setDuration(250L);
        ofFloat2.setDuration(250L);
        float max = Math.max(Math.abs(view.getTranslationX()) / 8.0f, Math.abs(view.getTranslationY()) / 8.0f) * 3.1f;
        ofFloat.setInterpolator(new SpringInterpolator(0.31f, max));
        ofFloat2.setInterpolator(new SpringInterpolator(0.31f, max));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofFloat2);
        animatorSet.setStartDelay(50);
        return animatorSet;
    }

    private void translateViews(float f) {
        for (int i = 0; i < this.mLeftViews.size(); i++) {
            setViewTranslation(this.mLeftViews.get(i), f);
        }
        for (int i2 = 0; i2 < this.mRightViews.size(); i2++) {
            setViewTranslation(this.mRightViews.get(i2), -f);
        }
    }

    private void setViewTranslation(View view, float f) {
        if (view.isAttachedToWindow()) {
            if (view.getLayoutDirection() == 1) {
                f *= -1.0f;
            }
            int i = this.mScreenRotation;
            if (i != 0) {
                if (i == 1) {
                    view.setTranslationX(0.0f);
                    view.setTranslationY(-f);
                    return;
                } else if (i != 2) {
                    if (i == 3) {
                        view.setTranslationX(0.0f);
                        view.setTranslationY(f);
                        return;
                    }
                    return;
                }
            }
            view.setTranslationX(f);
            view.setTranslationY(0.0f);
        }
    }

    private float px(float f) {
        return TypedValue.applyDimension(1, f, this.mContext.getResources().getDisplayMetrics());
    }

    public void addLeftView(View view) {
        this.mLeftViews.add(view);
    }

    public void addRightView(View view) {
        this.mRightViews.add(view);
    }

    public boolean isAttachedToWindow() {
        for (int i = 0; i < this.mLeftViews.size(); i++) {
            if (!this.mLeftViews.get(i).isAttachedToWindow()) {
                return false;
            }
        }
        for (int i2 = 0; i2 < this.mRightViews.size(); i2++) {
            if (!this.mRightViews.get(i2).isAttachedToWindow()) {
                return false;
            }
        }
        return true;
    }

    public void clearViews() {
        translateViews(0.0f);
        this.mLeftViews.clear();
        this.mRightViews.clear();
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class SpringInterpolator implements Interpolator {
        private float mBounce;
        private float mMass;

        SpringInterpolator(float f, float f2) {
            this.mMass = f;
            this.mBounce = f2;
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            return (float) ((-(Math.exp((double) (-(f / this.mMass))) * Math.cos((double) (f * this.mBounce)))) + 1.0d);
        }
    }
}
