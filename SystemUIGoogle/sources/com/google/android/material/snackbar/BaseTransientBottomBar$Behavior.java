package com.google.android.material.snackbar;

import android.view.MotionEvent;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.behavior.SwipeDismissBehavior;
/* loaded from: classes2.dex */
public class BaseTransientBottomBar$Behavior extends SwipeDismissBehavior<View> {
    private final BaseTransientBottomBar$BehaviorDelegate delegate = new BaseTransientBottomBar$BehaviorDelegate(this);

    @Override // com.google.android.material.behavior.SwipeDismissBehavior
    public boolean canSwipeDismissView(View view) {
        return this.delegate.canSwipeDismissView(view);
    }

    @Override // com.google.android.material.behavior.SwipeDismissBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
        this.delegate.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
        return super.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
    }
}
