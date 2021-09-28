package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.List;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public interface MotionStrategy {
    AnimatorSet createAnimator();

    int getDefaultMotionSpecResource();

    List<Animator.AnimatorListener> getListeners();

    void onAnimationCancel();

    void onAnimationEnd();

    void onAnimationStart(Animator animator);

    void onChange(ExtendedFloatingActionButton.OnChangedCallback onChangedCallback);

    void performNow();

    void setMotionSpec(MotionSpec motionSpec);

    boolean shouldCancel();
}
