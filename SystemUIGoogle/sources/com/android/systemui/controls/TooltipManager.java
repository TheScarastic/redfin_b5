package com.android.systemui.controls;

import android.content.Context;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import com.android.systemui.Prefs;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.recents.TriangleShape;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TooltipManager.kt */
/* loaded from: classes.dex */
public final class TooltipManager {
    public static final Companion Companion = new Companion(null);
    private final View arrowView;
    private final boolean below;
    private final View dismissView;
    private final ViewGroup layout;
    private final int maxTimesShown;
    private final String preferenceName;
    private final Function1<Integer, Unit> preferenceStorer;
    private int shown;
    private final TextView textView;

    public TooltipManager(Context context, String str, int i, boolean z) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(str, "preferenceName");
        this.preferenceName = str;
        this.maxTimesShown = i;
        this.below = z;
        this.shown = Prefs.getInt(context, str, 0);
        View inflate = LayoutInflater.from(context).inflate(R$layout.controls_onboarding, (ViewGroup) null);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
        ViewGroup viewGroup = (ViewGroup) inflate;
        this.layout = viewGroup;
        this.preferenceStorer = new Function1<Integer, Unit>(context, this) { // from class: com.android.systemui.controls.TooltipManager$preferenceStorer$1
            final /* synthetic */ Context $context;
            final /* synthetic */ TooltipManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.$context = r1;
                this.this$0 = r2;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Integer num) {
                invoke(num.intValue());
                return Unit.INSTANCE;
            }

            public final void invoke(int i2) {
                Prefs.putInt(this.$context, TooltipManager.access$getPreferenceName$p(this.this$0), i2);
            }
        };
        viewGroup.setAlpha(0.0f);
        this.textView = (TextView) viewGroup.requireViewById(R$id.onboarding_text);
        View requireViewById = viewGroup.requireViewById(R$id.dismiss);
        requireViewById.setOnClickListener(new View.OnClickListener(this) { // from class: com.android.systemui.controls.TooltipManager$dismissView$1$1
            final /* synthetic */ TooltipManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.this$0.hide(true);
            }
        });
        Unit unit = Unit.INSTANCE;
        this.dismissView = requireViewById;
        View requireViewById2 = viewGroup.requireViewById(R$id.arrow);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16843829, typedValue, true);
        int color = context.getResources().getColor(typedValue.resourceId, context.getTheme());
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.recents_onboarding_toast_arrow_corner_radius);
        ViewGroup.LayoutParams layoutParams = requireViewById2.getLayoutParams();
        ShapeDrawable shapeDrawable = new ShapeDrawable(TriangleShape.create((float) layoutParams.width, (float) layoutParams.height, z));
        Paint paint = shapeDrawable.getPaint();
        paint.setColor(color);
        paint.setPathEffect(new CornerPathEffect((float) dimensionPixelSize));
        requireViewById2.setBackground(shapeDrawable);
        this.arrowView = requireViewById2;
        if (!z) {
            viewGroup.removeView(requireViewById2);
            viewGroup.addView(requireViewById2);
            ViewGroup.LayoutParams layoutParams2 = requireViewById2.getLayoutParams();
            Objects.requireNonNull(layoutParams2, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams2;
            marginLayoutParams.bottomMargin = marginLayoutParams.topMargin;
            marginLayoutParams.topMargin = 0;
        }
    }

    public /* synthetic */ TooltipManager(Context context, String str, int i, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, str, (i2 & 4) != 0 ? 2 : i, (i2 & 8) != 0 ? true : z);
    }

    /* compiled from: TooltipManager.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final ViewGroup getLayout() {
        return this.layout;
    }

    public final void show(int i, int i2, int i3) {
        if (shouldShow()) {
            this.textView.setText(i);
            int i4 = this.shown + 1;
            this.shown = i4;
            this.preferenceStorer.invoke(Integer.valueOf(i4));
            this.layout.post(new Runnable(this, i2, i3) { // from class: com.android.systemui.controls.TooltipManager$show$1
                final /* synthetic */ int $x;
                final /* synthetic */ int $y;
                final /* synthetic */ TooltipManager this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$x = r2;
                    this.$y = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    int[] iArr = new int[2];
                    this.this$0.getLayout().getLocationOnScreen(iArr);
                    boolean z = false;
                    this.this$0.getLayout().setTranslationX((float) ((this.$x - iArr[0]) - (this.this$0.getLayout().getWidth() / 2)));
                    this.this$0.getLayout().setTranslationY(((float) (this.$y - iArr[1])) - ((float) (!TooltipManager.access$getBelow$p(this.this$0) ? this.this$0.getLayout().getHeight() : 0)));
                    if (this.this$0.getLayout().getAlpha() == 0.0f) {
                        z = true;
                    }
                    if (z) {
                        this.this$0.getLayout().animate().alpha(1.0f).withLayer().setStartDelay(500).setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
                    }
                }
            });
        }
    }

    public final void hide(boolean z) {
        if (!(this.layout.getAlpha() == 0.0f)) {
            this.layout.post(new Runnable(z, this) { // from class: com.android.systemui.controls.TooltipManager$hide$1
                final /* synthetic */ boolean $animate;
                final /* synthetic */ TooltipManager this$0;

                /* access modifiers changed from: package-private */
                {
                    this.$animate = r1;
                    this.this$0 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    if (this.$animate) {
                        this.this$0.getLayout().animate().alpha(0.0f).withLayer().setStartDelay(0).setDuration(100).setInterpolator(new AccelerateInterpolator()).start();
                        return;
                    }
                    this.this$0.getLayout().animate().cancel();
                    this.this$0.getLayout().setAlpha(0.0f);
                }
            });
        }
    }

    private final boolean shouldShow() {
        return this.shown < this.maxTimesShown;
    }
}
