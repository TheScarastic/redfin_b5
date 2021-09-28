package com.google.android.material.snackbar;

import android.view.MotionEvent;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.behavior.SwipeDismissBehavior;
import java.util.Objects;
/* loaded from: classes.dex */
public class BaseTransientBottomBar$Behavior extends SwipeDismissBehavior<View> {
    public final BaseTransientBottomBar$BehaviorDelegate delegate = new BaseTransientBottomBar$BehaviorDelegate(this);

    @Override // com.google.android.material.behavior.SwipeDismissBehavior
    public boolean canSwipeDismissView(View view) {
        Objects.requireNonNull(this.delegate);
        return view instanceof BaseTransientBottomBar$SnackbarBaseLayout;
    }

    @Override // com.google.android.material.behavior.SwipeDismissBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
        Objects.requireNonNull(this.delegate);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked == 1 || actionMasked == 3) {
                if (SnackbarManager.snackbarManager == null) {
                    SnackbarManager.snackbarManager = new SnackbarManager();
                }
                synchronized (SnackbarManager.snackbarManager.lock) {
                }
            }
        } else if (coordinatorLayout.isPointInChildBounds(view, (int) motionEvent.getX(), (int) motionEvent.getY())) {
            if (SnackbarManager.snackbarManager == null) {
                SnackbarManager.snackbarManager = new SnackbarManager();
            }
            synchronized (SnackbarManager.snackbarManager.lock) {
            }
        }
        return super.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
    }
}
