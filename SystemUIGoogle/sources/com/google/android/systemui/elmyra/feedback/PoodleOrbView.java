package com.google.android.systemui.elmyra.feedback;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class PoodleOrbView extends FrameLayout implements Animator.AnimatorListener, FeedbackEffect {
    private View mBackground;
    private View mBlue;
    private int mFeedbackHeight;
    private View mGreen;
    private View mRed;
    private View mYellow;
    private int mState = 0;
    private ArrayList<ValueAnimator> mAnimations = new ArrayList<>();

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
    }

    public PoodleOrbView(Context context) {
        super(context);
    }

    public PoodleOrbView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PoodleOrbView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public PoodleOrbView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mBackground = findViewById(R$id.elmyra_feedback_background);
        this.mBlue = findViewById(R$id.elmyra_feedback_blue);
        this.mGreen = findViewById(R$id.elmyra_feedback_green);
        this.mRed = findViewById(R$id.elmyra_feedback_red);
        this.mYellow = findViewById(R$id.elmyra_feedback_yellow);
        this.mFeedbackHeight = getResources().getDimensionPixelSize(R$dimen.opa_elmyra_orb_height);
        this.mBackground.setScaleX(0.0f);
        this.mBackground.setScaleY(0.0f);
        View view = this.mBlue;
        view.setTranslationY(view.getTranslationY() + ((float) this.mFeedbackHeight));
        View view2 = this.mGreen;
        view2.setTranslationY(view2.getTranslationY() + ((float) this.mFeedbackHeight));
        View view3 = this.mRed;
        view3.setTranslationY(view3.getTranslationY() + ((float) this.mFeedbackHeight));
        View view4 = this.mYellow;
        view4.setTranslationY(view4.getTranslationY() + ((float) this.mFeedbackHeight));
        this.mAnimations.addAll(Arrays.asList(createBackgroundAnimator(this.mBackground)));
        this.mAnimations.get(0).addListener(this);
        Path path = new Path();
        path.moveTo(this.mBlue.getTranslationX(), this.mBlue.getTranslationY() - ((float) this.mFeedbackHeight));
        path.cubicTo(px(-32.5f), px(-27.5f), px(15.0f), px(-33.75f), px(-2.5f), px(-20.0f));
        this.mAnimations.addAll(Arrays.asList(createDotAnimator(this.mBlue, 0.0f, path)));
        Path path2 = new Path();
        path2.moveTo(this.mRed.getTranslationX(), this.mRed.getTranslationY() - ((float) this.mFeedbackHeight));
        path2.cubicTo(px(-25.0f), px(-17.5f), px(-20.0f), px(-27.5f), px(2.5f), px(-20.0f));
        this.mAnimations.addAll(Arrays.asList(createDotAnimator(this.mRed, 0.05f, path2)));
        Path path3 = new Path();
        path3.moveTo(this.mYellow.getTranslationX(), this.mYellow.getTranslationY() - ((float) this.mFeedbackHeight));
        path3.cubicTo(px(21.25f), px(-33.75f), px(15.0f), px(-27.5f), px(0.0f), px(-20.0f));
        this.mAnimations.addAll(Arrays.asList(createDotAnimator(this.mYellow, 0.1f, path3)));
        Path path4 = new Path();
        path4.moveTo(this.mGreen.getTranslationX(), this.mGreen.getTranslationY() - ((float) this.mFeedbackHeight));
        path4.cubicTo(px(-27.5f), px(-20.0f), px(35.0f), px(-30.0f), px(0.0f), px(-20.0f));
        this.mAnimations.addAll(Arrays.asList(createDotAnimator(this.mGreen, 0.2f, path4)));
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onProgress(float f, int i) {
        if (this.mState != 3) {
            float f2 = (0.75f * f) + 0.0f;
            Iterator<ValueAnimator> it = this.mAnimations.iterator();
            while (it.hasNext()) {
                ValueAnimator next = it.next();
                next.cancel();
                next.setCurrentFraction(f2);
            }
            if (f == 0.0f) {
                this.mState = 0;
            } else if (f == 1.0f) {
                this.mState = 2;
            } else {
                this.mState = 1;
            }
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onRelease() {
        int i = this.mState;
        if (i == 2 || i == 1) {
            Iterator<ValueAnimator> it = this.mAnimations.iterator();
            while (it.hasNext()) {
                it.next().reverse();
            }
            this.mState = 0;
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        Iterator<ValueAnimator> it = this.mAnimations.iterator();
        while (it.hasNext()) {
            it.next().start();
        }
        this.mState = 3;
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        this.mState = 0;
        onProgress(0.0f, 0);
    }

    private float px(float f) {
        return TypedValue.applyDimension(1, f, getResources().getDisplayMetrics());
    }

    private ObjectAnimator[] createBackgroundAnimator(View view) {
        Keyframe[] keyframeArr = {Keyframe.ofFloat(0.0f, 0.0f), Keyframe.ofFloat(0.375f, 1.2f), Keyframe.ofFloat(0.75f, 1.2f), Keyframe.ofFloat(0.95f, 0.2f), Keyframe.ofFloat(1.0f, 0.0f)};
        keyframeArr[1].setInterpolator(new OvershootInterpolator());
        ObjectAnimator[] objectAnimatorArr = {ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofKeyframe(View.SCALE_X, keyframeArr)), ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofKeyframe(View.SCALE_Y, keyframeArr)), ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofKeyframe(View.TRANSLATION_Y, Keyframe.ofFloat(0.0f, view.getTranslationY()), Keyframe.ofFloat(0.375f, px(27.5f)), Keyframe.ofFloat(0.75f, px(27.5f)), Keyframe.ofFloat(0.95f, px(21.75f))))};
        for (int i = 0; i < 3; i++) {
            objectAnimatorArr[i].setDuration(1000L);
        }
        return objectAnimatorArr;
    }

    private Keyframe[][] approximatePath(Path path, float f, float f2) {
        float[] approximate = path.approximate(0.5f);
        Keyframe[] keyframeArr = new Keyframe[approximate.length / 3];
        Keyframe[] keyframeArr2 = new Keyframe[approximate.length / 3];
        int i = 0;
        int i2 = 0;
        while (i < approximate.length) {
            int i3 = i + 1;
            float f3 = ((f2 - f) * approximate[i]) + f;
            int i4 = i3 + 1;
            keyframeArr[i2] = Keyframe.ofFloat(f3, approximate[i3]);
            keyframeArr2[i2] = Keyframe.ofFloat(f3, approximate[i4]);
            i2++;
            i = i4 + 1;
        }
        return new Keyframe[][]{keyframeArr, keyframeArr2};
    }

    private ObjectAnimator[] createDotAnimator(View view, float f, Path path) {
        Keyframe[] keyframeArr = {Keyframe.ofFloat(0.0f, view.getScaleX()), Keyframe.ofFloat(0.75f, view.getScaleX()), Keyframe.ofFloat(0.95f, 0.3f), Keyframe.ofFloat(1.0f, 0.0f)};
        Keyframe[] keyframeArr2 = {Keyframe.ofFloat(0.0f, 1.0f), Keyframe.ofFloat(0.75f, 1.0f), Keyframe.ofFloat(0.95f, 0.25f), Keyframe.ofFloat(1.0f, 0.0f)};
        Keyframe[][] approximatePath = approximatePath(path, 0.75f, 1.0f);
        Keyframe[] keyframeArr3 = new Keyframe[approximatePath[0].length + 2];
        keyframeArr3[0] = Keyframe.ofFloat(0.0f, view.getTranslationX());
        keyframeArr3[1] = Keyframe.ofFloat(0.75f, view.getTranslationX());
        System.arraycopy(approximatePath[0], 0, keyframeArr3, 2, approximatePath[0].length);
        Keyframe[] keyframeArr4 = new Keyframe[approximatePath[1].length + 3];
        keyframeArr4[0] = Keyframe.ofFloat(0.0f, view.getTranslationY());
        keyframeArr4[1] = Keyframe.ofFloat(f, view.getTranslationY());
        keyframeArr4[2] = Keyframe.ofFloat(0.75f, view.getTranslationY() - ((float) this.mFeedbackHeight));
        System.arraycopy(approximatePath[1], 0, keyframeArr4, 3, approximatePath[1].length);
        keyframeArr4[2].setInterpolator(new OvershootInterpolator());
        ObjectAnimator[] objectAnimatorArr = {ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofKeyframe(View.SCALE_X, keyframeArr)), ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofKeyframe(View.SCALE_Y, keyframeArr)), ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X, keyframeArr3)), ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofKeyframe(View.TRANSLATION_Y, keyframeArr4)), ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofKeyframe(View.ALPHA, keyframeArr2))};
        for (int i = 0; i < 5; i++) {
            objectAnimatorArr[i].setDuration(1000L);
        }
        return objectAnimatorArr;
    }
}
