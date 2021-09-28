package com.android.wm.shell.draganddrop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.SurfaceControl;
import android.view.View;
import android.view.WindowInsets;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.draganddrop.DragAndDropPolicy;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class DragLayout extends View {
    private int mDisplayMargin;
    private DropOutlineDrawable mDropOutline;
    private boolean mHasDropped;
    private boolean mIsShowing;
    private final DragAndDropPolicy mPolicy;
    private DragAndDropPolicy.Target mCurrentTarget = null;
    private Insets mInsets = Insets.NONE;

    public DragLayout(Context context, SplitScreenController splitScreenController) {
        super(context);
        this.mPolicy = new DragAndDropPolicy(context, splitScreenController);
        this.mDisplayMargin = context.getResources().getDimensionPixelSize(R.dimen.drop_layout_display_margin);
        DropOutlineDrawable dropOutlineDrawable = new DropOutlineDrawable(context);
        this.mDropOutline = dropOutlineDrawable;
        setBackground(dropOutlineDrawable);
        setWillNotDraw(false);
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mInsets = windowInsets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
        recomputeDropTargets();
        return super.onApplyWindowInsets(windowInsets);
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return drawable == this.mDropOutline || super.verifyDrawable(drawable);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mCurrentTarget != null) {
            this.mDropOutline.draw(canvas);
        }
    }

    public boolean hasDropped() {
        return this.mHasDropped;
    }

    public void prepare(DisplayLayout displayLayout, ClipData clipData) {
        this.mPolicy.start(displayLayout, clipData);
        this.mHasDropped = false;
        this.mCurrentTarget = null;
    }

    public void show() {
        this.mIsShowing = true;
        recomputeDropTargets();
    }

    private void recomputeDropTargets() {
        if (this.mIsShowing) {
            ArrayList<DragAndDropPolicy.Target> targets = this.mPolicy.getTargets(this.mInsets);
            for (int i = 0; i < targets.size(); i++) {
                DragAndDropPolicy.Target target = targets.get(i);
                if (ShellProtoLogCache.WM_SHELL_DRAG_AND_DROP_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, -710770147, 0, null, String.valueOf(target));
                }
                Rect rect = target.drawRegion;
                int i2 = this.mDisplayMargin;
                rect.inset(i2, i2);
            }
        }
    }

    public void update(DragEvent dragEvent) {
        DragAndDropPolicy.Target targetAtLocation = this.mPolicy.getTargetAtLocation((int) dragEvent.getX(), (int) dragEvent.getY());
        if (this.mCurrentTarget != targetAtLocation) {
            if (ShellProtoLogCache.WM_SHELL_DRAG_AND_DROP_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, 1481772149, 0, null, String.valueOf(targetAtLocation));
            }
            if (targetAtLocation == null) {
                this.mDropOutline.startVisibilityAnimation(false, Interpolators.LINEAR);
                Rect rect = new Rect(this.mCurrentTarget.drawRegion);
                int i = this.mDisplayMargin;
                rect.inset(i, i);
                this.mDropOutline.startBoundsAnimation(rect, Interpolators.FAST_OUT_LINEAR_IN);
            } else if (this.mCurrentTarget == null) {
                this.mDropOutline.startVisibilityAnimation(true, Interpolators.LINEAR);
                Rect rect2 = new Rect(targetAtLocation.drawRegion);
                int i2 = this.mDisplayMargin;
                rect2.inset(i2, i2);
                this.mDropOutline.setRegionBounds(rect2);
                this.mDropOutline.startBoundsAnimation(targetAtLocation.drawRegion, Interpolators.LINEAR_OUT_SLOW_IN);
            } else {
                this.mDropOutline.startBoundsAnimation(targetAtLocation.drawRegion, Interpolators.FAST_OUT_SLOW_IN);
            }
            this.mCurrentTarget = targetAtLocation;
        }
    }

    public void hide(DragEvent dragEvent, final Runnable runnable) {
        ObjectAnimator objectAnimator;
        this.mIsShowing = false;
        ObjectAnimator startVisibilityAnimation = this.mDropOutline.startVisibilityAnimation(false, Interpolators.LINEAR);
        if (this.mCurrentTarget != null) {
            Rect rect = new Rect(this.mCurrentTarget.drawRegion);
            int i = this.mDisplayMargin;
            rect.inset(i, i);
            objectAnimator = this.mDropOutline.startBoundsAnimation(rect, Interpolators.FAST_OUT_LINEAR_IN);
        } else {
            objectAnimator = null;
        }
        if (runnable != null) {
            if (objectAnimator != null) {
                startVisibilityAnimation = objectAnimator;
            }
            startVisibilityAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.draganddrop.DragLayout.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    runnable.run();
                }
            });
        }
        this.mCurrentTarget = null;
    }

    public boolean drop(DragEvent dragEvent, SurfaceControl surfaceControl, Runnable runnable) {
        DragAndDropPolicy.Target target = this.mCurrentTarget;
        boolean z = target != null;
        this.mHasDropped = true;
        this.mPolicy.handleDrop(target, dragEvent.getClipData());
        hide(dragEvent, runnable);
        return z;
    }
}
