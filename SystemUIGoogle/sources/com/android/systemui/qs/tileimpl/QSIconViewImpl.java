package com.android.systemui.qs.tileimpl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.settingslib.Utils;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.AlphaControlledSignalTileView;
import java.util.Objects;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class QSIconViewImpl extends QSIconView {
    protected final View mIcon;
    protected int mIconSizePx;
    private QSTile.Icon mLastIcon;
    private int mTint;
    private boolean mAnimationEnabled = true;
    private int mState = -1;

    protected int getIconMeasureMode() {
        return 1073741824;
    }

    public QSIconViewImpl(Context context) {
        super(context);
        this.mIconSizePx = context.getResources().getDimensionPixelSize(R$dimen.qs_icon_size);
        View createIcon = createIcon();
        this.mIcon = createIcon;
        addView(createIcon);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mIconSizePx = getContext().getResources().getDimensionPixelSize(R$dimen.qs_icon_size);
    }

    @Override // com.android.systemui.plugins.qs.QSIconView
    public void disableAnimation() {
        this.mAnimationEnabled = false;
    }

    @Override // com.android.systemui.plugins.qs.QSIconView
    public View getIconView() {
        return this.mIcon;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        this.mIcon.measure(View.MeasureSpec.makeMeasureSpec(size, getIconMeasureMode()), exactly(this.mIconSizePx));
        setMeasuredDimension(size, this.mIcon.getMeasuredHeight());
    }

    @Override // android.view.View, java.lang.Object
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append('[');
        sb.append("state=" + this.mState);
        sb.append(", tint=" + this.mTint);
        if (this.mLastIcon != null) {
            sb.append(", lastIcon=" + this.mLastIcon.toString());
        }
        sb.append("]");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        layout(this.mIcon, (getMeasuredWidth() - this.mIcon.getMeasuredWidth()) / 2, 0);
    }

    @Override // com.android.systemui.plugins.qs.QSIconView
    public void setIcon(QSTile.State state, boolean z) {
        setIcon((ImageView) this.mIcon, state, z);
    }

    /* access modifiers changed from: protected */
    /* renamed from: updateIcon */
    public void lambda$setIcon$0(ImageView imageView, QSTile.State state, boolean z) {
        Drawable drawable;
        Supplier<QSTile.Icon> supplier = state.iconSupplier;
        QSTile.Icon icon = supplier != null ? supplier.get() : state.icon;
        int i = R$id.qs_icon_tag;
        if (!Objects.equals(icon, imageView.getTag(i)) || !Objects.equals(state.slash, imageView.getTag(R$id.qs_slash_tag))) {
            boolean z2 = z && shouldAnimate(imageView);
            this.mLastIcon = icon;
            if (icon != null) {
                drawable = z2 ? icon.getDrawable(((ViewGroup) this).mContext) : icon.getInvisibleDrawable(((ViewGroup) this).mContext);
            } else {
                drawable = null;
            }
            int padding = icon != null ? icon.getPadding() : 0;
            if (drawable != null) {
                drawable.setAutoMirrored(false);
                drawable.setLayoutDirection(getLayoutDirection());
            }
            if (imageView instanceof SlashImageView) {
                SlashImageView slashImageView = (SlashImageView) imageView;
                slashImageView.setAnimationEnabled(z2);
                slashImageView.setState(null, drawable);
            } else {
                imageView.setImageDrawable(drawable);
            }
            imageView.setTag(i, icon);
            imageView.setTag(R$id.qs_slash_tag, state.slash);
            imageView.setPadding(0, padding, 0, padding);
            if (drawable instanceof Animatable2) {
                final Animatable2 animatable2 = (Animatable2) drawable;
                animatable2.start();
                if (state.isTransient) {
                    animatable2.registerAnimationCallback(new Animatable2.AnimationCallback() { // from class: com.android.systemui.qs.tileimpl.QSIconViewImpl.1
                        @Override // android.graphics.drawable.Animatable2.AnimationCallback
                        public void onAnimationEnd(Drawable drawable2) {
                            animatable2.start();
                        }
                    });
                }
            }
        }
    }

    private boolean shouldAnimate(ImageView imageView) {
        return this.mAnimationEnabled && imageView.isShown() && imageView.getDrawable() != null;
    }

    /* access modifiers changed from: protected */
    public void setIcon(ImageView imageView, QSTile.State state, boolean z) {
        if (state.disabledByPolicy) {
            imageView.setColorFilter(getContext().getColor(R$color.qs_tile_disabled_color));
        } else {
            imageView.clearColorFilter();
        }
        int i = state.state;
        if (i != this.mState) {
            int color = getColor(i);
            this.mState = state.state;
            if (this.mTint == 0 || !z || !shouldAnimate(imageView)) {
                if (imageView instanceof AlphaControlledSignalTileView.AlphaControlledSlashImageView) {
                    ((AlphaControlledSignalTileView.AlphaControlledSlashImageView) imageView).setFinalImageTintList(ColorStateList.valueOf(color));
                } else {
                    setTint(imageView, color);
                }
                this.mTint = color;
                lambda$setIcon$0(imageView, state, z);
                return;
            }
            animateGrayScale(this.mTint, color, imageView, new Runnable(imageView, state, z) { // from class: com.android.systemui.qs.tileimpl.QSIconViewImpl$$ExternalSyntheticLambda1
                public final /* synthetic */ ImageView f$1;
                public final /* synthetic */ QSTile.State f$2;
                public final /* synthetic */ boolean f$3;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    QSIconViewImpl.this.lambda$setIcon$0(this.f$1, this.f$2, this.f$3);
                }
            });
            this.mTint = color;
            return;
        }
        lambda$setIcon$0(imageView, state, z);
    }

    protected int getColor(int i) {
        return getIconColorForState(getContext(), i);
    }

    private void animateGrayScale(int i, int i2, ImageView imageView, final Runnable runnable) {
        if (imageView instanceof AlphaControlledSignalTileView.AlphaControlledSlashImageView) {
            ((AlphaControlledSignalTileView.AlphaControlledSlashImageView) imageView).setFinalImageTintList(ColorStateList.valueOf(i2));
        }
        if (!this.mAnimationEnabled || !ValueAnimator.areAnimatorsEnabled()) {
            setTint(imageView, i2);
            runnable.run();
            return;
        }
        float red = (float) Color.red(i);
        float red2 = (float) Color.red(i2);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(350L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener((float) Color.alpha(i), (float) Color.alpha(i2), red, red2, imageView) { // from class: com.android.systemui.qs.tileimpl.QSIconViewImpl$$ExternalSyntheticLambda0
            public final /* synthetic */ float f$0;
            public final /* synthetic */ float f$1;
            public final /* synthetic */ float f$2;
            public final /* synthetic */ float f$3;
            public final /* synthetic */ ImageView f$4;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                QSIconViewImpl.lambda$animateGrayScale$1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, valueAnimator);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.tileimpl.QSIconViewImpl.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                runnable.run();
            }
        });
        ofFloat.start();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$animateGrayScale$1(float f, float f2, float f3, float f4, ImageView imageView, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        int i = (int) (f3 + ((f4 - f3) * animatedFraction));
        setTint(imageView, Color.argb((int) (f + ((f2 - f) * animatedFraction)), i, i, i));
    }

    public static void setTint(ImageView imageView, int i) {
        imageView.setImageTintList(ColorStateList.valueOf(i));
    }

    protected View createIcon() {
        SlashImageView slashImageView = new SlashImageView(((ViewGroup) this).mContext);
        slashImageView.setId(16908294);
        slashImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return slashImageView;
    }

    protected final int exactly(int i) {
        return View.MeasureSpec.makeMeasureSpec(i, 1073741824);
    }

    protected final void layout(View view, int i, int i2) {
        view.layout(i, i2, view.getMeasuredWidth() + i, view.getMeasuredHeight() + i2);
    }

    public static int getIconColorForState(Context context, int i) {
        if (i == 0) {
            return Utils.applyAlpha(0.3f, Utils.getColorAttrDefaultColor(context, 16842806));
        }
        if (i == 1) {
            return Utils.getColorAttrDefaultColor(context, 16842806);
        }
        if (i == 2) {
            return Utils.getColorAttrDefaultColor(context, 16842809);
        }
        Log.e("QSIconView", "Invalid state " + i);
        return 0;
    }
}
