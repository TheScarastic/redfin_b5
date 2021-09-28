package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.FrameLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.DismissCircleView;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DismissView.kt */
/* loaded from: classes2.dex */
public final class DismissView extends FrameLayout {
    private final PhysicsAnimator<DismissCircleView> animator;
    private DismissCircleView circle;
    private boolean isShowing;
    private final PhysicsAnimator.SpringConfig spring = new PhysicsAnimator.SpringConfig(200.0f, 0.75f);
    private final int DISMISS_SCRIM_FADE_MS = 200;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public DismissView(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        DismissCircleView dismissCircleView = new DismissCircleView(context);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.dismiss_circle_size);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize);
        layoutParams.gravity = 81;
        dismissCircleView.setLayoutParams(layoutParams);
        Resources resources = dismissCircleView.getResources();
        int i = R.dimen.floating_dismiss_gradient_height;
        dismissCircleView.setTranslationY((float) resources.getDimensionPixelSize(i));
        Unit unit = Unit.INSTANCE;
        this.circle = dismissCircleView;
        this.animator = PhysicsAnimator.Companion.getInstance(dismissCircleView);
        setLayoutParams(new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(i), 80));
        setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.floating_dismiss_bottom_margin));
        setClipToPadding(false);
        setClipChildren(false);
        setVisibility(4);
        setBackgroundResource(R.drawable.floating_dismiss_gradient_transition);
        addView(this.circle);
    }

    public final DismissCircleView getCircle() {
        return this.circle;
    }

    public final boolean isShowing() {
        return this.isShowing;
    }

    public final void show() {
        if (!this.isShowing) {
            this.isShowing = true;
            setVisibility(0);
            Drawable background = getBackground();
            Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
            ((TransitionDrawable) background).startTransition(this.DISMISS_SCRIM_FADE_MS);
            this.animator.cancel();
            PhysicsAnimator<DismissCircleView> physicsAnimator = this.animator;
            DynamicAnimation.ViewProperty viewProperty = DynamicAnimation.TRANSLATION_Y;
            Intrinsics.checkNotNullExpressionValue(viewProperty, "TRANSLATION_Y");
            physicsAnimator.spring(viewProperty, 0.0f, this.spring).start();
        }
    }

    public final void hide() {
        if (this.isShowing) {
            this.isShowing = false;
            Drawable background = getBackground();
            Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
            ((TransitionDrawable) background).reverseTransition(this.DISMISS_SCRIM_FADE_MS);
            PhysicsAnimator<DismissCircleView> physicsAnimator = this.animator;
            DynamicAnimation.ViewProperty viewProperty = DynamicAnimation.TRANSLATION_Y;
            Intrinsics.checkNotNullExpressionValue(viewProperty, "TRANSLATION_Y");
            physicsAnimator.spring(viewProperty, (float) getHeight(), this.spring).withEndActions(new Function0<Unit>(this) { // from class: com.android.wm.shell.bubbles.DismissView$hide$1
                final /* synthetic */ DismissView this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // kotlin.jvm.functions.Function0
                public final void invoke() {
                    this.this$0.setVisibility(4);
                }
            }).start();
        }
    }

    public final void updateResources() {
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R.dimen.dismiss_circle_size);
        this.circle.getLayoutParams().width = dimensionPixelSize;
        this.circle.getLayoutParams().height = dimensionPixelSize;
        this.circle.requestLayout();
    }
}
