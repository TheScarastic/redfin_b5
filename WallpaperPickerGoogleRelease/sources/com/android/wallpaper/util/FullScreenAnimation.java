package com.android.wallpaper.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.cardview.R$attr;
import androidx.cardview.widget.CardView;
import androidx.cardview.widget.CardViewApi21Impl;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyGraph$$ExternalSyntheticOutline0;
import com.android.systemui.shared.R;
import com.android.wallpaper.picker.TouchForwardingLayout;
import com.google.android.material.appbar.AppBarLayout;
import java.util.Objects;
/* loaded from: classes.dex */
public class FullScreenAnimation {
    public float mBottomActionBarTranslation;
    public int mCurrentTextColor;
    public float mDefaultRadius;
    public float mFullScreenButtonsTranslation;
    public FullScreenStatusListener mFullScreenStatusListener;
    public int mNavigationBarHeight;
    public float mOffsetY;
    public float mScale;
    public int mStatusBarHeight;
    public final TouchForwardingLayout mTouchForwardingLayout;
    public final View mView;
    public int mWorkspaceHeight;
    public final SurfaceView mWorkspaceSurface;
    public int mWorkspaceWidth;
    public boolean mIsFullScreen = false;
    public boolean mScaleIsSet = false;
    public boolean mWorkspaceVisibility = true;
    public boolean mIsHomeSelected = true;
    public int mFullScreenTextColor = 1;

    /* loaded from: classes.dex */
    public interface FullScreenStatusListener {
        void onFullScreenStatusChange(boolean z);
    }

    public FullScreenAnimation(View view) {
        this.mView = view;
        this.mTouchForwardingLayout = (TouchForwardingLayout) view.findViewById(R.id.touch_forwarding_layout);
        this.mWorkspaceSurface = (SurfaceView) view.findViewById(R.id.workspace_surface);
        this.mCurrentTextColor = R$attr.getColorAttr(view.getContext(), 16842806);
    }

    public final void animateColor(boolean z) {
        int i;
        int i2;
        TextView textView = (TextView) this.mView.findViewById(R.id.custom_toolbar_title);
        if (!z || (i2 = this.mFullScreenTextColor) == 1) {
            i = R$attr.getColorAttr(this.mView.getContext(), 16842806);
        } else if (i2 == 2) {
            i = this.mView.getContext().getColor(17170444);
        } else {
            i = this.mView.getContext().getColor(17170443);
        }
        if (i != this.mCurrentTextColor) {
            ValueAnimator ofArgb = ValueAnimator.ofArgb(this.mCurrentTextColor, i);
            ofArgb.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(textView, (ImageButton) ((Toolbar) this.mView.findViewById(R.id.toolbar)).getNavigationView()) { // from class: com.android.wallpaper.util.FullScreenAnimation$$ExternalSyntheticLambda0
                public final /* synthetic */ TextView f$0;
                public final /* synthetic */ ImageButton f$1;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    TextView textView2 = this.f$0;
                    ImageButton imageButton = this.f$1;
                    int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    textView2.setTextColor(intValue);
                    if (imageButton != null) {
                        imageButton.setColorFilter(intValue);
                    }
                }
            });
            ofArgb.start();
            this.mCurrentTextColor = i;
        }
    }

    public void ensureToolbarIsCorrectlyColored() {
        ((TextView) this.mView.findViewById(R.id.custom_toolbar_title)).setTextColor(this.mCurrentTextColor);
        ImageButton imageButton = (ImageButton) ((Toolbar) this.mView.findViewById(R.id.toolbar)).getNavigationView();
        if (imageButton != null) {
            imageButton.setColorFilter(this.mCurrentTextColor);
        }
    }

    public void ensureToolbarIsCorrectlyLocated() {
        AppBarLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(0, this.mStatusBarHeight, 0, 0);
        this.mView.findViewById(R.id.toolbar).setLayoutParams(layoutParams);
    }

    public final void setViewMargins(int i, float f, float f2, boolean z) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, z ? -2 : -1);
        layoutParams.setMargins(0, Math.round(f), 0, Math.round(f2));
        if (z) {
            layoutParams.gravity = 80;
        }
        this.mView.findViewById(i).setLayoutParams(layoutParams);
    }

    public void startAnimation(final boolean z) {
        float f;
        float f2;
        if (z != this.mIsFullScreen) {
            if (!this.mScaleIsSet) {
                int[] iArr = new int[2];
                this.mTouchForwardingLayout.getLocationInWindow(iArr);
                Point screenSize = ScreenSizeCalculator.getInstance().getScreenSize(this.mView.getDisplay());
                int i = screenSize.x;
                int i2 = screenSize.y;
                this.mOffsetY = (float) ((((double) i2) / 2.0d) - ((((double) this.mTouchForwardingLayout.getHeight()) / 2.0d) + ((double) iArr[1])));
                this.mScale = Math.max(((float) i) / ((float) this.mTouchForwardingLayout.getWidth()), ((float) i2) / ((float) this.mTouchForwardingLayout.getHeight()));
                this.mDefaultRadius = ((CardViewApi21Impl) CardView.IMPL).getRadius(((CardView) this.mWorkspaceSurface.getParent()).mCardViewDelegate);
                this.mWorkspaceSurface.setEnableSurfaceClipping(true);
                this.mWorkspaceWidth = this.mWorkspaceSurface.getWidth();
                this.mWorkspaceHeight = this.mWorkspaceSurface.getHeight();
                this.mBottomActionBarTranslation = this.mView.getResources().getDimension(R.dimen.separated_tabs_height) + this.mView.getResources().getDimension(R.dimen.bottom_actions_height) + ((float) this.mNavigationBarHeight);
                this.mFullScreenButtonsTranslation = -(this.mView.getResources().getDimension(R.dimen.separated_tabs_height) + this.mView.getResources().getDimension(R.dimen.fullscreen_preview_button_margin_bottom) + ((float) this.mNavigationBarHeight));
                this.mScaleIsSet = true;
            }
            float f3 = 0.0f;
            float f4 = z ? this.mDefaultRadius : 0.0f;
            if (z) {
                f = 0.0f;
            } else {
                f = this.mDefaultRadius;
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(f4, f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wallpaper.util.FullScreenAnimation$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ((CardView) FullScreenAnimation.this.mWorkspaceSurface.getParent()).setRadius(((Float) valueAnimator.getAnimatedValue()).floatValue());
                }
            });
            float f5 = z ? 0.0f : 0.2f;
            float f6 = z ? 0.2f : 0.0f;
            if (z) {
                f2 = 0.0f;
            } else {
                f2 = this.mFullScreenButtonsTranslation / this.mScale;
            }
            float f7 = z ? this.mFullScreenButtonsTranslation / this.mScale : 0.0f;
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(f5, f6, f2, f7) { // from class: com.android.wallpaper.util.FullScreenAnimation$$ExternalSyntheticLambda2
                public final /* synthetic */ float f$1;
                public final /* synthetic */ float f$2;
                public final /* synthetic */ float f$3;
                public final /* synthetic */ float f$4;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FullScreenAnimation fullScreenAnimation = FullScreenAnimation.this;
                    float f8 = this.f$1;
                    float f9 = this.f$2;
                    float f10 = this.f$3;
                    float f11 = this.f$4;
                    Objects.requireNonNull(fullScreenAnimation);
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    float m = DependencyGraph$$ExternalSyntheticOutline0.m(f9, f8, floatValue, f8);
                    float m2 = DependencyGraph$$ExternalSyntheticOutline0.m(f11, f10, floatValue, f10);
                    fullScreenAnimation.mWorkspaceSurface.setClipBounds(new Rect(0, Math.round(((float) fullScreenAnimation.mWorkspaceHeight) * m), fullScreenAnimation.mWorkspaceWidth, Math.round(m2) + fullScreenAnimation.mWorkspaceHeight));
                }
            });
            float f8 = z ? this.mScale : 1.0f;
            float f9 = z ? this.mOffsetY : 0.0f;
            float f10 = z ? this.mBottomActionBarTranslation : 0.0f;
            if (z) {
                f3 = this.mFullScreenButtonsTranslation;
            }
            View findViewById = this.mView.findViewById(R.id.screen_preview_layout);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(findViewById, "scaleX", f8), ObjectAnimator.ofFloat(findViewById, "scaleY", f8), ObjectAnimator.ofFloat(findViewById, "translationY", f9), ObjectAnimator.ofFloat(this.mView.findViewById(R.id.bottom_actionbar), "translationY", f10), ObjectAnimator.ofFloat(this.mView.findViewById(R.id.separated_tabs_container), "translationY", f10), ObjectAnimator.ofFloat(this.mView.findViewById(R.id.fullscreen_buttons_container), "translationY", f3), ofFloat, ofFloat2);
            animatorSet.addListener(new Animator.AnimatorListener() { // from class: com.android.wallpaper.util.FullScreenAnimation.1
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    FullScreenStatusListener fullScreenStatusListener = FullScreenAnimation.this.mFullScreenStatusListener;
                    if (fullScreenStatusListener != null) {
                        fullScreenStatusListener.onFullScreenStatusChange(z);
                    }
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                }
            });
            animatorSet.start();
            animateColor(z);
            this.mWorkspaceVisibility = true;
            if (z) {
                ((Button) this.mView.findViewById(R.id.hide_ui_preview_button)).setText(R.string.hide_ui_preview_text);
            }
            this.mView.findViewById(R.id.lock_screen_preview_container).setVisibility(0);
            if (this.mIsHomeSelected) {
                this.mView.findViewById(R.id.lock_screen_preview_container).setVisibility(4);
            }
            this.mIsFullScreen = z;
        }
    }
}
