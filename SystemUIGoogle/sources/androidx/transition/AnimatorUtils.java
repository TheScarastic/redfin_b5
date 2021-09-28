package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import java.util.ArrayList;
/* loaded from: classes.dex */
class AnimatorUtils {

    /* loaded from: classes.dex */
    interface AnimatorPauseListenerCompat {
        @Override // android.animation.Animator.AnimatorPauseListener, androidx.transition.AnimatorUtils.AnimatorPauseListenerCompat
        void onAnimationPause(Animator animator);

        @Override // android.animation.Animator.AnimatorPauseListener, androidx.transition.AnimatorUtils.AnimatorPauseListenerCompat
        void onAnimationResume(Animator animator);
    }

    /* access modifiers changed from: package-private */
    public static void addPauseListener(Animator animator, AnimatorListenerAdapter animatorListenerAdapter) {
        if (Build.VERSION.SDK_INT >= 19) {
            animator.addPauseListener(animatorListenerAdapter);
        }
    }

    /* access modifiers changed from: package-private */
    public static void pause(Animator animator) {
        if (Build.VERSION.SDK_INT >= 19) {
            animator.pause();
            return;
        }
        ArrayList<Animator.AnimatorListener> listeners = animator.getListeners();
        if (listeners != null) {
            int size = listeners.size();
            for (int i = 0; i < size; i++) {
                Animator.AnimatorListener animatorListener = listeners.get(i);
                if (animatorListener instanceof AnimatorPauseListenerCompat) {
                    ((AnimatorPauseListenerCompat) animatorListener).onAnimationPause(animator);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public static void resume(Animator animator) {
        if (Build.VERSION.SDK_INT >= 19) {
            animator.resume();
            return;
        }
        ArrayList<Animator.AnimatorListener> listeners = animator.getListeners();
        if (listeners != null) {
            int size = listeners.size();
            for (int i = 0; i < size; i++) {
                Animator.AnimatorListener animatorListener = listeners.get(i);
                if (animatorListener instanceof AnimatorPauseListenerCompat) {
                    ((AnimatorPauseListenerCompat) animatorListener).onAnimationResume(animator);
                }
            }
        }
    }
}
