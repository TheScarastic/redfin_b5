package com.android.keyguard;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import java.util.List;
/* loaded from: classes.dex */
public class KeyguardPINView extends KeyguardPinBasedInputView {
    private final AppearAnimationUtils mAppearAnimationUtils;
    private ViewGroup mContainer;
    private final DisappearAnimationUtils mDisappearAnimationUtils;
    private final DisappearAnimationUtils mDisappearAnimationUtilsLocked;
    private int mDisappearYTranslation;
    private ViewGroup mRow0;
    private ViewGroup mRow1;
    private ViewGroup mRow2;
    private ViewGroup mRow3;
    private View[][] mViews;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public KeyguardPINView(Context context) {
        this(context, null);
    }

    public KeyguardPINView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAppearAnimationUtils = new AppearAnimationUtils(context);
        this.mDisappearAnimationUtils = new DisappearAnimationUtils(context, 125, 0.6f, 0.45f, AnimationUtils.loadInterpolator(((LinearLayout) this).mContext, 17563663));
        this.mDisappearAnimationUtilsLocked = new DisappearAnimationUtils(context, 187, 0.6f, 0.45f, AnimationUtils.loadInterpolator(((LinearLayout) this).mContext, 17563663));
        this.mDisappearYTranslation = getResources().getDimensionPixelSize(R$dimen.disappear_y_translation);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        updateMargins();
    }

    /* access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public int getPasswordTextViewId() {
        return R$id.pinEntry;
    }

    private void updateMargins() {
        int dimensionPixelSize = ((LinearLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.num_pad_row_margin_bottom);
        for (ViewGroup viewGroup : List.of(this.mRow1, this.mRow2, this.mRow3)) {
            ((LinearLayout.LayoutParams) viewGroup.getLayoutParams()).setMargins(0, 0, 0, dimensionPixelSize);
        }
        ((LinearLayout.LayoutParams) this.mRow0.getLayoutParams()).setMargins(0, 0, 0, ((LinearLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.num_pad_entry_row_margin_bottom));
        if (this.mEcaView != null) {
            ((LinearLayout.LayoutParams) this.mEcaView.getLayoutParams()).setMargins(0, ((LinearLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.keyguard_eca_top_margin), 0, ((LinearLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.keyguard_eca_bottom_margin));
        }
        View findViewById = findViewById(R$id.pinEntry);
        ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
        layoutParams.height = ((LinearLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.keyguard_password_height);
        findViewById.setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardPinBasedInputView, com.android.keyguard.KeyguardAbsKeyInputView, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mContainer = (ViewGroup) findViewById(R$id.pin_container);
        this.mRow0 = (ViewGroup) findViewById(R$id.row0);
        this.mRow1 = (ViewGroup) findViewById(R$id.row1);
        this.mRow2 = (ViewGroup) findViewById(R$id.row2);
        this.mRow3 = (ViewGroup) findViewById(R$id.row3);
        this.mViews = new View[][]{new View[]{this.mRow0, null, null}, new View[]{findViewById(R$id.key1), findViewById(R$id.key2), findViewById(R$id.key3)}, new View[]{findViewById(R$id.key4), findViewById(R$id.key5), findViewById(R$id.key6)}, new View[]{findViewById(R$id.key7), findViewById(R$id.key8), findViewById(R$id.key9)}, new View[]{findViewById(R$id.delete_button), findViewById(R$id.key0), findViewById(R$id.key_enter)}, new View[]{null, this.mEcaView, null}};
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public int getWrongPasswordStringId() {
        return R$string.kg_wrong_pin;
    }

    @Override // com.android.keyguard.KeyguardInputView
    public void startAppearAnimation() {
        enableClipping(false);
        setAlpha(1.0f);
        setTranslationY(this.mAppearAnimationUtils.getStartTranslation());
        AppearAnimationUtils.startTranslationYAnimation(this, 0, 500, 0.0f, this.mAppearAnimationUtils.getInterpolator(), getAnimationListener(19));
        this.mAppearAnimationUtils.startAnimation2d(this.mViews, new Runnable() { // from class: com.android.keyguard.KeyguardPINView.1
            @Override // java.lang.Runnable
            public void run() {
                KeyguardPINView.this.enableClipping(true);
            }
        });
    }

    public boolean startDisappearAnimation(boolean z, Runnable runnable) {
        DisappearAnimationUtils disappearAnimationUtils;
        enableClipping(false);
        setTranslationY(0.0f);
        AppearAnimationUtils.startTranslationYAnimation(this, 0, 280, (float) this.mDisappearYTranslation, this.mDisappearAnimationUtils.getInterpolator(), getAnimationListener(22));
        if (z) {
            disappearAnimationUtils = this.mDisappearAnimationUtilsLocked;
        } else {
            disappearAnimationUtils = this.mDisappearAnimationUtils;
        }
        disappearAnimationUtils.startAnimation2d(this.mViews, new Runnable(runnable) { // from class: com.android.keyguard.KeyguardPINView$$ExternalSyntheticLambda0
            public final /* synthetic */ Runnable f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                KeyguardPINView.$r8$lambda$CIVa4P7dGPecjDvjYiZaOW_N_No(KeyguardPINView.this, this.f$1);
            }
        });
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startDisappearAnimation$0(Runnable runnable) {
        enableClipping(true);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* access modifiers changed from: private */
    public void enableClipping(boolean z) {
        this.mContainer.setClipToPadding(z);
        this.mContainer.setClipChildren(z);
        this.mRow1.setClipToPadding(z);
        this.mRow2.setClipToPadding(z);
        this.mRow3.setClipToPadding(z);
        setClipChildren(z);
    }
}
