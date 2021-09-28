package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.content.res.AppCompatResources;
import com.android.systemui.shared.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.textfield.TextInputLayout;
/* loaded from: classes.dex */
public class ClearTextEndIconDelegate extends EndIconDelegate {
    public AnimatorSet iconInAnim;
    public ValueAnimator iconOutAnim;
    public final TextWatcher clearTextEndIconTextWatcher = new TextWatcher() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.1
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001a, code lost:
            if ((r4.length() > 0) != false) goto L_0x001e;
         */
        @Override // android.text.TextWatcher
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void afterTextChanged(android.text.Editable r4) {
            /*
                r3 = this;
                com.google.android.material.textfield.ClearTextEndIconDelegate r3 = com.google.android.material.textfield.ClearTextEndIconDelegate.this
                com.google.android.material.textfield.TextInputLayout r0 = r3.textInputLayout
                java.lang.CharSequence r1 = r0.suffixText
                if (r1 == 0) goto L_0x0009
                return
            L_0x0009:
                boolean r0 = r0.hasFocus()
                r1 = 1
                r2 = 0
                if (r0 == 0) goto L_0x001d
                int r4 = r4.length()
                if (r4 <= 0) goto L_0x0019
                r4 = r1
                goto L_0x001a
            L_0x0019:
                r4 = r2
            L_0x001a:
                if (r4 == 0) goto L_0x001d
                goto L_0x001e
            L_0x001d:
                r1 = r2
            L_0x001e:
                r3.animateIcon(r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.textfield.ClearTextEndIconDelegate.AnonymousClass1.afterTextChanged(android.text.Editable):void");
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }
    };
    public final View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.2
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z) {
            boolean z2 = true;
            boolean z3 = !TextUtils.isEmpty(((EditText) view).getText());
            ClearTextEndIconDelegate clearTextEndIconDelegate = ClearTextEndIconDelegate.this;
            if (!z3 || !z) {
                z2 = false;
            }
            clearTextEndIconDelegate.animateIcon(z2);
        }
    };
    public final TextInputLayout.OnEditTextAttachedListener clearTextOnEditTextAttachedListener = new TextInputLayout.OnEditTextAttachedListener() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.3
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0017, code lost:
            if ((r0.getText().length() > 0) != false) goto L_0x001b;
         */
        @Override // com.google.android.material.textfield.TextInputLayout.OnEditTextAttachedListener
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onEditTextAttached(com.google.android.material.textfield.TextInputLayout r5) {
            /*
                r4 = this;
                android.widget.EditText r0 = r5.editText
                boolean r1 = r0.hasFocus()
                r2 = 1
                r3 = 0
                if (r1 == 0) goto L_0x001a
                android.text.Editable r1 = r0.getText()
                int r1 = r1.length()
                if (r1 <= 0) goto L_0x0016
                r1 = r2
                goto L_0x0017
            L_0x0016:
                r1 = r3
            L_0x0017:
                if (r1 == 0) goto L_0x001a
                goto L_0x001b
            L_0x001a:
                r2 = r3
            L_0x001b:
                r5.setEndIconVisible(r2)
                r5.setEndIconCheckable(r3)
                com.google.android.material.textfield.ClearTextEndIconDelegate r5 = com.google.android.material.textfield.ClearTextEndIconDelegate.this
                android.view.View$OnFocusChangeListener r5 = r5.onFocusChangeListener
                r0.setOnFocusChangeListener(r5)
                com.google.android.material.textfield.ClearTextEndIconDelegate r5 = com.google.android.material.textfield.ClearTextEndIconDelegate.this
                android.text.TextWatcher r5 = r5.clearTextEndIconTextWatcher
                r0.removeTextChangedListener(r5)
                com.google.android.material.textfield.ClearTextEndIconDelegate r4 = com.google.android.material.textfield.ClearTextEndIconDelegate.this
                android.text.TextWatcher r4 = r4.clearTextEndIconTextWatcher
                r0.addTextChangedListener(r4)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.textfield.ClearTextEndIconDelegate.AnonymousClass3.onEditTextAttached(com.google.android.material.textfield.TextInputLayout):void");
        }
    };
    public final TextInputLayout.OnEndIconChangedListener endIconChangedListener = new TextInputLayout.OnEndIconChangedListener() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.4
        @Override // com.google.android.material.textfield.TextInputLayout.OnEndIconChangedListener
        public void onEndIconChanged(TextInputLayout textInputLayout, int i) {
            final EditText editText = textInputLayout.editText;
            if (editText != null && i == 2) {
                editText.post(new Runnable() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        editText.removeTextChangedListener(ClearTextEndIconDelegate.this.clearTextEndIconTextWatcher);
                    }
                });
                if (editText.getOnFocusChangeListener() == ClearTextEndIconDelegate.this.onFocusChangeListener) {
                    editText.setOnFocusChangeListener(null);
                }
            }
        }
    };

    public ClearTextEndIconDelegate(TextInputLayout textInputLayout) {
        super(textInputLayout);
    }

    public final void animateIcon(boolean z) {
        boolean z2 = this.textInputLayout.isEndIconVisible() == z;
        if (z && !this.iconInAnim.isRunning()) {
            this.iconOutAnim.cancel();
            this.iconInAnim.start();
            if (z2) {
                this.iconInAnim.end();
            }
        } else if (!z) {
            this.iconInAnim.cancel();
            this.iconOutAnim.start();
            if (z2) {
                this.iconOutAnim.end();
            }
        }
    }

    @Override // com.google.android.material.textfield.EndIconDelegate
    public void initialize() {
        this.textInputLayout.setEndIconDrawable(AppCompatResources.getDrawable(this.context, R.drawable.mtrl_ic_cancel));
        TextInputLayout textInputLayout = this.textInputLayout;
        textInputLayout.setEndIconContentDescription(textInputLayout.getResources().getText(R.string.clear_text_end_icon_content_description));
        TextInputLayout textInputLayout2 = this.textInputLayout;
        AnonymousClass5 r1 = new View.OnClickListener() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Editable text = ClearTextEndIconDelegate.this.textInputLayout.editText.getText();
                if (text != null) {
                    text.clear();
                }
                ClearTextEndIconDelegate.this.textInputLayout.refreshEndIconDrawableState();
            }
        };
        CheckableImageButton checkableImageButton = textInputLayout2.endIconView;
        View.OnLongClickListener onLongClickListener = textInputLayout2.endIconOnLongClickListener;
        checkableImageButton.setOnClickListener(r1);
        TextInputLayout.setIconClickable(checkableImageButton, onLongClickListener);
        this.textInputLayout.addOnEditTextAttachedListener(this.clearTextOnEditTextAttachedListener);
        this.textInputLayout.endIconChangedListeners.add(this.endIconChangedListener);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.8f, 1.0f);
        ofFloat.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
        ofFloat.setDuration(150L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.9
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                ClearTextEndIconDelegate.this.endIconView.setScaleX(floatValue);
                ClearTextEndIconDelegate.this.endIconView.setScaleY(floatValue);
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        TimeInterpolator timeInterpolator = AnimationUtils.LINEAR_INTERPOLATOR;
        ofFloat2.setInterpolator(timeInterpolator);
        ofFloat2.setDuration(100L);
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ClearTextEndIconDelegate.this.endIconView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        this.iconInAnim = animatorSet;
        animatorSet.playTogether(ofFloat, ofFloat2);
        this.iconInAnim.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.6
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ClearTextEndIconDelegate.this.textInputLayout.setEndIconVisible(true);
            }
        });
        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat3.setInterpolator(timeInterpolator);
        ofFloat3.setDuration(100L);
        ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ClearTextEndIconDelegate.this.endIconView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        this.iconOutAnim = ofFloat3;
        ofFloat3.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.textfield.ClearTextEndIconDelegate.7
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ClearTextEndIconDelegate.this.textInputLayout.setEndIconVisible(false);
            }
        });
    }

    @Override // com.google.android.material.textfield.EndIconDelegate
    public void onSuffixVisibilityChanged(boolean z) {
        if (this.textInputLayout.suffixText != null) {
            animateIcon(z);
        }
    }
}
