package com.google.android.material.snackbar;

import com.google.android.material.behavior.SwipeDismissBehavior;
/* loaded from: classes.dex */
public class BaseTransientBottomBar$BehaviorDelegate {
    public BaseTransientBottomBar$BehaviorDelegate(SwipeDismissBehavior<?> swipeDismissBehavior) {
        swipeDismissBehavior.alphaStartSwipeDistance = SwipeDismissBehavior.clamp(0.0f, 0.1f, 1.0f);
        swipeDismissBehavior.alphaEndSwipeDistance = SwipeDismissBehavior.clamp(0.0f, 0.6f, 1.0f);
        swipeDismissBehavior.swipeDirection = 0;
    }
}
