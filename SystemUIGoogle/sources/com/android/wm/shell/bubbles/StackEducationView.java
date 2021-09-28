package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.util.ContrastColorUtil;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StackEducationView.kt */
/* loaded from: classes2.dex */
public final class StackEducationView extends LinearLayout {
    private boolean isHiding;
    private final String TAG = "Bubbles";
    private final long ANIMATE_DURATION = 200;
    private final long ANIMATE_DURATION_SHORT = 40;
    private final Lazy view$delegate = LazyKt__LazyJVMKt.lazy(new Function0<View>(this) { // from class: com.android.wm.shell.bubbles.StackEducationView$view$2
        final /* synthetic */ StackEducationView this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // kotlin.jvm.functions.Function0
        public final View invoke() {
            return this.this$0.findViewById(R.id.stack_education_layout);
        }
    });
    private final Lazy titleTextView$delegate = LazyKt__LazyJVMKt.lazy(new Function0<TextView>(this) { // from class: com.android.wm.shell.bubbles.StackEducationView$titleTextView$2
        final /* synthetic */ StackEducationView this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // kotlin.jvm.functions.Function0
        public final TextView invoke() {
            return (TextView) this.this$0.findViewById(R.id.stack_education_title);
        }
    });
    private final Lazy descTextView$delegate = LazyKt__LazyJVMKt.lazy(new Function0<TextView>(this) { // from class: com.android.wm.shell.bubbles.StackEducationView$descTextView$2
        final /* synthetic */ StackEducationView this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // kotlin.jvm.functions.Function0
        public final TextView invoke() {
            return (TextView) this.this$0.findViewById(R.id.stack_education_description);
        }
    });

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public StackEducationView(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        LayoutInflater.from(context).inflate(R.layout.bubble_stack_user_education, this);
        setVisibility(8);
        setElevation((float) getResources().getDimensionPixelSize(R.dimen.bubble_elevation));
        setLayoutDirection(3);
    }

    /* access modifiers changed from: private */
    public final View getView() {
        return (View) this.view$delegate.getValue();
    }

    private final TextView getTitleTextView() {
        return (TextView) this.titleTextView$delegate.getValue();
    }

    private final TextView getDescTextView() {
        return (TextView) this.descTextView$delegate.getValue();
    }

    @Override // android.view.View
    public void setLayoutDirection(int i) {
        super.setLayoutDirection(i);
        setDrawableDirection();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        setLayoutDirection(getResources().getConfiguration().getLayoutDirection());
        setTextColor();
    }

    private final void setTextColor() {
        TypedArray obtainStyledAttributes = ((LinearLayout) this).mContext.obtainStyledAttributes(new int[]{16843829, 16842809});
        int color = obtainStyledAttributes.getColor(0, -16777216);
        int color2 = obtainStyledAttributes.getColor(1, -1);
        obtainStyledAttributes.recycle();
        int ensureTextContrast = ContrastColorUtil.ensureTextContrast(color2, color, true);
        getTitleTextView().setTextColor(ensureTextContrast);
        getDescTextView().setTextColor(ensureTextContrast);
    }

    private final void setDrawableDirection() {
        int i;
        View view = getView();
        if (getResources().getConfiguration().getLayoutDirection() == 0) {
            i = R.drawable.bubble_stack_user_education_bg;
        } else {
            i = R.drawable.bubble_stack_user_education_bg_rtl;
        }
        view.setBackgroundResource(i);
    }

    public final boolean show(PointF pointF) {
        Intrinsics.checkNotNullParameter(pointF, "stackPosition");
        if (getVisibility() == 0) {
            return false;
        }
        setAlpha(0.0f);
        setVisibility(0);
        post(new Runnable(this, pointF) { // from class: com.android.wm.shell.bubbles.StackEducationView$show$1
            final /* synthetic */ PointF $stackPosition;
            final /* synthetic */ StackEducationView this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$stackPosition = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                View access$getView = StackEducationView.access$getView(this.this$0);
                access$getView.setTranslationY((this.$stackPosition.y + ((float) (access$getView.getContext().getResources().getDimensionPixelSize(R.dimen.bubble_size) / 2))) - ((float) (access$getView.getHeight() / 2)));
                this.this$0.animate().setDuration(StackEducationView.access$getANIMATE_DURATION$p(this.this$0)).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f);
            }
        });
        setShouldShow(false);
        return true;
    }

    public final void hide(boolean z) {
        if (getVisibility() == 0 && !this.isHiding) {
            animate().alpha(0.0f).setDuration(z ? this.ANIMATE_DURATION_SHORT : this.ANIMATE_DURATION).withEndAction(new Runnable(this) { // from class: com.android.wm.shell.bubbles.StackEducationView$hide$1
                final /* synthetic */ StackEducationView this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    this.this$0.setVisibility(8);
                }
            });
        }
    }

    private final void setShouldShow(boolean z) {
        getContext().getSharedPreferences(getContext().getPackageName(), 0).edit().putBoolean("HasSeenBubblesOnboarding", !z).apply();
    }
}
