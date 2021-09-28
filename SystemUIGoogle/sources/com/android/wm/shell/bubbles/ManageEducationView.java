package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.util.ContrastColorUtil;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ManageEducationView.kt */
/* loaded from: classes2.dex */
public final class ManageEducationView extends LinearLayout {
    private boolean isHiding;
    private final String TAG = "Bubbles";
    private final long ANIMATE_DURATION = 200;
    private final long ANIMATE_DURATION_SHORT = 40;
    private final Lazy manageView$delegate = LazyKt__LazyJVMKt.lazy(new Function0<View>(this) { // from class: com.android.wm.shell.bubbles.ManageEducationView$manageView$2
        final /* synthetic */ ManageEducationView this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // kotlin.jvm.functions.Function0
        public final View invoke() {
            return this.this$0.findViewById(R.id.manage_education_view);
        }
    });
    private final Lazy manageButton$delegate = LazyKt__LazyJVMKt.lazy(new Function0<Button>(this) { // from class: com.android.wm.shell.bubbles.ManageEducationView$manageButton$2
        final /* synthetic */ ManageEducationView this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // kotlin.jvm.functions.Function0
        public final Button invoke() {
            return (Button) this.this$0.findViewById(R.id.manage);
        }
    });
    private final Lazy gotItButton$delegate = LazyKt__LazyJVMKt.lazy(new Function0<Button>(this) { // from class: com.android.wm.shell.bubbles.ManageEducationView$gotItButton$2
        final /* synthetic */ ManageEducationView this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // kotlin.jvm.functions.Function0
        public final Button invoke() {
            return (Button) this.this$0.findViewById(R.id.got_it);
        }
    });
    private final Lazy titleTextView$delegate = LazyKt__LazyJVMKt.lazy(new Function0<TextView>(this) { // from class: com.android.wm.shell.bubbles.ManageEducationView$titleTextView$2
        final /* synthetic */ ManageEducationView this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // kotlin.jvm.functions.Function0
        public final TextView invoke() {
            return (TextView) this.this$0.findViewById(R.id.user_education_title);
        }
    });
    private final Lazy descTextView$delegate = LazyKt__LazyJVMKt.lazy(new Function0<TextView>(this) { // from class: com.android.wm.shell.bubbles.ManageEducationView$descTextView$2
        final /* synthetic */ ManageEducationView this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // kotlin.jvm.functions.Function0
        public final TextView invoke() {
            return (TextView) this.this$0.findViewById(R.id.user_education_description);
        }
    });

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ManageEducationView(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        LayoutInflater.from(context).inflate(R.layout.bubbles_manage_button_education, this);
        setVisibility(8);
        setElevation((float) getResources().getDimensionPixelSize(R.dimen.bubble_elevation));
        setLayoutDirection(3);
    }

    /* access modifiers changed from: private */
    public final View getManageView() {
        return (View) this.manageView$delegate.getValue();
    }

    /* access modifiers changed from: private */
    public final Button getManageButton() {
        return (Button) this.manageButton$delegate.getValue();
    }

    /* access modifiers changed from: private */
    public final Button getGotItButton() {
        return (Button) this.gotItButton$delegate.getValue();
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
        View manageView = getManageView();
        if (getResources().getConfiguration().getLayoutDirection() == 1) {
            i = R.drawable.bubble_stack_user_education_bg_rtl;
        } else {
            i = R.drawable.bubble_stack_user_education_bg;
        }
        manageView.setBackgroundResource(i);
    }

    public final void show(BubbleExpandedView bubbleExpandedView, Rect rect) {
        Intrinsics.checkNotNullParameter(bubbleExpandedView, "expandedView");
        Intrinsics.checkNotNullParameter(rect, "rect");
        if (getVisibility() != 0) {
            setAlpha(0.0f);
            setVisibility(0);
            post(new Runnable(bubbleExpandedView, rect, this) { // from class: com.android.wm.shell.bubbles.ManageEducationView$show$1
                final /* synthetic */ BubbleExpandedView $expandedView;
                final /* synthetic */ Rect $rect;
                final /* synthetic */ ManageEducationView this$0;

                /* access modifiers changed from: package-private */
                {
                    this.$expandedView = r1;
                    this.$rect = r2;
                    this.this$0 = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    this.$expandedView.getManageButtonBoundsOnScreen(this.$rect);
                    Button access$getManageButton = ManageEducationView.access$getManageButton(this.this$0);
                    final BubbleExpandedView bubbleExpandedView2 = this.$expandedView;
                    final ManageEducationView manageEducationView = this.this$0;
                    access$getManageButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.bubbles.ManageEducationView$show$1.1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            bubbleExpandedView2.findViewById(R.id.settings_button).performClick();
                            manageEducationView.hide(true);
                        }
                    });
                    Button access$getGotItButton = ManageEducationView.access$getGotItButton(this.this$0);
                    final ManageEducationView manageEducationView2 = this.this$0;
                    access$getGotItButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.bubbles.ManageEducationView$show$1.2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            manageEducationView2.hide(true);
                        }
                    });
                    final ManageEducationView manageEducationView3 = this.this$0;
                    manageEducationView3.setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.bubbles.ManageEducationView$show$1.3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            manageEducationView3.hide(true);
                        }
                    });
                    View access$getManageView = ManageEducationView.access$getManageView(this.this$0);
                    Rect rect2 = this.$rect;
                    ManageEducationView manageEducationView4 = this.this$0;
                    access$getManageView.setTranslationX(0.0f);
                    access$getManageView.setTranslationY((float) ((rect2.top - ManageEducationView.access$getManageView(manageEducationView4).getHeight()) + access$getManageView.getResources().getDimensionPixelSize(R.dimen.bubbles_manage_education_top_inset)));
                    this.this$0.bringToFront();
                    this.this$0.animate().setDuration(ManageEducationView.access$getANIMATE_DURATION$p(this.this$0)).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f);
                }
            });
            setShouldShow(false);
        }
    }

    public final void hide(boolean z) {
        if (getVisibility() == 0 && !this.isHiding) {
            animate().withStartAction(new Runnable(this) { // from class: com.android.wm.shell.bubbles.ManageEducationView$hide$1
                final /* synthetic */ ManageEducationView this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    ManageEducationView.access$setHiding$p(this.this$0, true);
                }
            }).alpha(0.0f).setDuration(z ? this.ANIMATE_DURATION_SHORT : this.ANIMATE_DURATION).withEndAction(new Runnable(this) { // from class: com.android.wm.shell.bubbles.ManageEducationView$hide$2
                final /* synthetic */ ManageEducationView this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    ManageEducationView.access$setHiding$p(this.this$0, false);
                    this.this$0.setVisibility(8);
                }
            });
        }
    }

    private final void setShouldShow(boolean z) {
        getContext().getSharedPreferences(getContext().getPackageName(), 0).edit().putBoolean("HasSeenBubblesManageOnboarding", !z).apply();
    }
}
