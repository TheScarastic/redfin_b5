package com.android.systemui.statusbar.events;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.statusbar.SuperStatusBarViewFactory;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.StatusBarWindowController;
import com.android.systemui.statusbar.phone.StatusBarWindowView;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SystemEventChipAnimationController.kt */
/* loaded from: classes.dex */
public final class SystemEventChipAnimationController {
    private View animationDotView;
    private FrameLayout animationWindowView;
    private final Context context;
    private View currentAnimatedView;
    private boolean initialized;
    private final StatusBarLocationPublisher locationPublisher;
    private final SuperStatusBarViewFactory statusBarViewFactory;
    private final StatusBarWindowController statusBarWindowController;
    private StatusBarWindowView statusBarWindowView;

    public SystemEventChipAnimationController(Context context, SuperStatusBarViewFactory superStatusBarViewFactory, StatusBarWindowController statusBarWindowController, StatusBarLocationPublisher statusBarLocationPublisher) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(superStatusBarViewFactory, "statusBarViewFactory");
        Intrinsics.checkNotNullParameter(statusBarWindowController, "statusBarWindowController");
        Intrinsics.checkNotNullParameter(statusBarLocationPublisher, "locationPublisher");
        this.context = context;
        this.statusBarViewFactory = superStatusBarViewFactory;
        this.statusBarWindowController = statusBarWindowController;
        this.locationPublisher = statusBarLocationPublisher;
    }

    public void onChipAnimationStart(Function1<? super Context, ? extends View> function1, int i) {
        Intrinsics.checkNotNullParameter(function1, "viewCreator");
        if (!this.initialized) {
            init();
        }
        if (i == 1) {
            View view = (View) function1.invoke(this.context);
            this.currentAnimatedView = view;
            FrameLayout frameLayout = this.animationWindowView;
            if (frameLayout != null) {
                frameLayout.addView(view, layoutParamsDefault());
                View view2 = this.currentAnimatedView;
                if (view2 != null) {
                    float width = (float) view2.getWidth();
                    if (view2.isLayoutRtl()) {
                        width = -width;
                    }
                    view2.setTranslationX(width);
                    view2.setAlpha(0.0f);
                    view2.setVisibility(0);
                    view2.setPadding(this.locationPublisher.getMarginLeft(), 0, this.locationPublisher.getMarginRight(), 0);
                    return;
                }
                return;
            }
            Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
            throw null;
        }
        View view3 = this.currentAnimatedView;
        if (view3 != null) {
            view3.setTranslationX(0.0f);
            view3.setAlpha(1.0f);
        }
    }

    public void onChipAnimationEnd(int i) {
        if (i == 1) {
            View view = this.currentAnimatedView;
            if (view != null) {
                view.setTranslationX(0.0f);
                view.setAlpha(1.0f);
                return;
            }
            return;
        }
        View view2 = this.currentAnimatedView;
        if (view2 != null) {
            view2.setVisibility(4);
        }
        FrameLayout frameLayout = this.animationWindowView;
        if (frameLayout != null) {
            frameLayout.removeView(this.currentAnimatedView);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
            throw null;
        }
    }

    public void onChipAnimationUpdate(ValueAnimator valueAnimator, int i) {
        Intrinsics.checkNotNullParameter(valueAnimator, "animator");
        View view = this.currentAnimatedView;
        if (view != null) {
            Object animatedValue = valueAnimator.getAnimatedValue();
            Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
            float floatValue = ((Float) animatedValue).floatValue();
            view.setAlpha(floatValue);
            float width = (((float) 1) - floatValue) * ((float) view.getWidth());
            if (view.isLayoutRtl()) {
                width = -width;
            }
            view.setTranslationX(width);
        }
    }

    private final void init() {
        this.initialized = true;
        StatusBarWindowView statusBarWindowView = this.statusBarViewFactory.getStatusBarWindowView();
        Intrinsics.checkNotNullExpressionValue(statusBarWindowView, "statusBarViewFactory.statusBarWindowView");
        this.statusBarWindowView = statusBarWindowView;
        View inflate = LayoutInflater.from(this.context).inflate(R$layout.system_event_animation_window, (ViewGroup) null);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.widget.FrameLayout");
        FrameLayout frameLayout = (FrameLayout) inflate;
        this.animationWindowView = frameLayout;
        View findViewById = frameLayout.findViewById(R$id.dot_view);
        Intrinsics.checkNotNullExpressionValue(findViewById, "animationWindowView.findViewById(R.id.dot_view)");
        this.animationDotView = findViewById;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 8388629;
        StatusBarWindowView statusBarWindowView2 = this.statusBarWindowView;
        if (statusBarWindowView2 != null) {
            FrameLayout frameLayout2 = this.animationWindowView;
            if (frameLayout2 != null) {
                statusBarWindowView2.addView(frameLayout2, layoutParams);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
                throw null;
            }
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("statusBarWindowView");
            throw null;
        }
    }

    private final int start() {
        FrameLayout frameLayout = this.animationWindowView;
        if (frameLayout != null) {
            return frameLayout.isLayoutRtl() ? right() : left();
        }
        Intrinsics.throwUninitializedPropertyAccessException("animationWindowView");
        throw null;
    }

    private final int right() {
        return this.locationPublisher.getMarginRight();
    }

    private final int left() {
        return this.locationPublisher.getMarginLeft();
    }

    private final FrameLayout.LayoutParams layoutParamsDefault() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 8388629;
        layoutParams.setMarginStart(start());
        return layoutParams;
    }
}
