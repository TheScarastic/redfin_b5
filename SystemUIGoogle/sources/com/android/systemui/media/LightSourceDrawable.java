package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.animation.Interpolator;
import androidx.annotation.Keep;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.R$styleable;
import com.android.systemui.animation.Interpolators;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.xmlpull.v1.XmlPullParser;
/* compiled from: LightSourceDrawable.kt */
@Keep
/* loaded from: classes.dex */
public final class LightSourceDrawable extends Drawable {
    private boolean active;
    private boolean pressed;
    private Animator rippleAnimation;
    private int[] themeAttrs;
    private final RippleData rippleData = new RippleData(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Paint paint = new Paint();
    private int highlightColor = -1;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        Intrinsics.checkNotNullParameter(outline, "outline");
    }

    public boolean hasFocusStateSpecified() {
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isProjected() {
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return true;
    }

    public final int getHighlightColor() {
        return this.highlightColor;
    }

    public final void setHighlightColor(int i) {
        if (this.highlightColor != i) {
            this.highlightColor = i;
            invalidateSelf();
        }
    }

    private final void setActive(boolean z) {
        if (z != this.active) {
            this.active = z;
            if (z) {
                Animator animator = this.rippleAnimation;
                if (animator != null) {
                    animator.cancel();
                }
                this.rippleData.setAlpha(1.0f);
                this.rippleData.setProgress(0.05f);
            } else {
                Animator animator2 = this.rippleAnimation;
                if (animator2 != null) {
                    animator2.cancel();
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.rippleData.getAlpha(), 0.0f);
                ofFloat.setDuration(200L);
                ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.media.LightSourceDrawable$active$1$1
                    final /* synthetic */ LightSourceDrawable this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.this$0 = r1;
                    }

                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        RippleData access$getRippleData$p = LightSourceDrawable.access$getRippleData$p(this.this$0);
                        Object animatedValue = valueAnimator.getAnimatedValue();
                        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                        access$getRippleData$p.setAlpha(((Float) animatedValue).floatValue());
                        this.this$0.invalidateSelf();
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter(this) { // from class: com.android.systemui.media.LightSourceDrawable$active$1$2
                    private boolean cancelled;
                    final /* synthetic */ LightSourceDrawable this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.this$0 = r1;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator3) {
                        this.cancelled = true;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator3) {
                        if (!this.cancelled) {
                            LightSourceDrawable.access$getRippleData$p(this.this$0).setProgress(0.0f);
                            LightSourceDrawable.access$getRippleData$p(this.this$0).setAlpha(0.0f);
                            LightSourceDrawable.access$setRippleAnimation$p(this.this$0, null);
                            this.this$0.invalidateSelf();
                        }
                    }
                });
                ofFloat.start();
                Unit unit = Unit.INSTANCE;
                this.rippleAnimation = ofFloat;
            }
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        float lerp = MathUtils.lerp(this.rippleData.getMinSize(), this.rippleData.getMaxSize(), this.rippleData.getProgress());
        this.paint.setShader(new RadialGradient(this.rippleData.getX(), this.rippleData.getY(), lerp, new int[]{ColorUtils.setAlphaComponent(this.highlightColor, (int) (this.rippleData.getAlpha() * ((float) 255))), 0}, LightSourceDrawableKt.GRADIENT_STOPS, Shader.TileMode.CLAMP));
        canvas.drawCircle(this.rippleData.getX(), this.rippleData.getY(), lerp, this.paint);
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        Intrinsics.checkNotNullParameter(resources, "r");
        Intrinsics.checkNotNullParameter(xmlPullParser, "parser");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
        TypedArray obtainAttributes = Drawable.obtainAttributes(resources, theme, attributeSet, R$styleable.IlluminationDrawable);
        this.themeAttrs = obtainAttributes.extractThemeAttrs();
        Intrinsics.checkNotNullExpressionValue(obtainAttributes, "a");
        updateStateFromTypedArray(obtainAttributes);
        obtainAttributes.recycle();
    }

    private final void updateStateFromTypedArray(TypedArray typedArray) {
        int i = R$styleable.IlluminationDrawable_rippleMinSize;
        if (typedArray.hasValue(i)) {
            this.rippleData.setMinSize(typedArray.getDimension(i, 0.0f));
        }
        int i2 = R$styleable.IlluminationDrawable_rippleMaxSize;
        if (typedArray.hasValue(i2)) {
            this.rippleData.setMaxSize(typedArray.getDimension(i2, 0.0f));
        }
        int i3 = R$styleable.IlluminationDrawable_highlight;
        if (typedArray.hasValue(i3)) {
            this.rippleData.setHighlight(((float) typedArray.getInteger(i3, 0)) / 100.0f);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0008, code lost:
        if (r0.length <= 0) goto L_0x000a;
     */
    @Override // android.graphics.drawable.Drawable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean canApplyTheme() {
        /*
            r1 = this;
            int[] r0 = r1.themeAttrs
            if (r0 == 0) goto L_0x000a
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            int r0 = r0.length
            if (r0 > 0) goto L_0x0010
        L_0x000a:
            boolean r1 = super.canApplyTheme()
            if (r1 == 0) goto L_0x0012
        L_0x0010:
            r1 = 1
            goto L_0x0013
        L_0x0012:
            r1 = 0
        L_0x0013:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.LightSourceDrawable.canApplyTheme():boolean");
    }

    @Override // android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme theme) {
        Intrinsics.checkNotNullParameter(theme, "t");
        super.applyTheme(theme);
        int[] iArr = this.themeAttrs;
        if (iArr != null) {
            TypedArray resolveAttributes = theme.resolveAttributes(iArr, R$styleable.IlluminationDrawable);
            Intrinsics.checkNotNullExpressionValue(resolveAttributes, "a");
            updateStateFromTypedArray(resolveAttributes);
            resolveAttributes.recycle();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        throw new UnsupportedOperationException("Color filters are not supported");
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        if (i != this.paint.getAlpha()) {
            this.paint.setAlpha(i);
            invalidateSelf();
        }
    }

    private final void illuminate() {
        this.rippleData.setAlpha(1.0f);
        invalidateSelf();
        Animator animator = this.rippleAnimation;
        if (animator != null) {
            animator.cancel();
        }
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat.setStartDelay(133);
        ofFloat.setDuration(800 - ofFloat.getStartDelay());
        Interpolator interpolator = Interpolators.LINEAR_OUT_SLOW_IN;
        ofFloat.setInterpolator(interpolator);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.media.LightSourceDrawable$illuminate$1$1$1
            final /* synthetic */ LightSourceDrawable this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                RippleData access$getRippleData$p = LightSourceDrawable.access$getRippleData$p(this.this$0);
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                access$getRippleData$p.setAlpha(((Float) animatedValue).floatValue());
                this.this$0.invalidateSelf();
            }
        });
        Unit unit = Unit.INSTANCE;
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.rippleData.getProgress(), 1.0f);
        ofFloat2.setDuration(800L);
        ofFloat2.setInterpolator(interpolator);
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.media.LightSourceDrawable$illuminate$1$2$1
            final /* synthetic */ LightSourceDrawable this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                RippleData rippleData = this.this$0.rippleData;
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                rippleData.setProgress(((Float) animatedValue).floatValue());
                this.this$0.invalidateSelf();
            }
        });
        animatorSet.playTogether(ofFloat, ofFloat2);
        animatorSet.addListener(new AnimatorListenerAdapter(this) { // from class: com.android.systemui.media.LightSourceDrawable$illuminate$1$3
            final /* synthetic */ LightSourceDrawable this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator2) {
                this.this$0.rippleData.setProgress(0.0f);
                this.this$0.rippleAnimation = null;
                this.this$0.invalidateSelf();
            }
        });
        animatorSet.start();
        this.rippleAnimation = animatorSet;
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspot(float f, float f2) {
        this.rippleData.setX(f);
        this.rippleData.setY(f2);
        if (this.active) {
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public Rect getDirtyBounds() {
        float lerp = MathUtils.lerp(this.rippleData.getMinSize(), this.rippleData.getMaxSize(), this.rippleData.getProgress());
        Rect rect = new Rect((int) (this.rippleData.getX() - lerp), (int) (this.rippleData.getY() - lerp), (int) (this.rippleData.getX() + lerp), (int) (this.rippleData.getY() + lerp));
        rect.union(super.getDirtyBounds());
        return rect;
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        boolean onStateChange = super.onStateChange(iArr);
        if (iArr == null) {
            return onStateChange;
        }
        boolean z = this.pressed;
        boolean z2 = false;
        this.pressed = false;
        int length = iArr.length;
        int i = 0;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        while (i < length) {
            int i2 = iArr[i];
            i++;
            switch (i2) {
                case 16842908:
                    z4 = true;
                    break;
                case 16842910:
                    z3 = true;
                    break;
                case 16842919:
                    this.pressed = true;
                    break;
                case 16843623:
                    z5 = true;
                    break;
            }
        }
        if (z3 && (this.pressed || z4 || z5)) {
            z2 = true;
        }
        setActive(z2);
        if (z && !this.pressed) {
            illuminate();
        }
        return onStateChange;
    }
}
