package com.google.android.systemui.assist.uihints;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import com.android.systemui.Dependency;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.policy.ConfigurationController;
/* loaded from: classes2.dex */
public class PromptView extends TextView implements ConfigurationController.ConfigurationListener {
    private final DecelerateInterpolator mDecelerateInterpolator;
    private boolean mEnabled;
    private String mHandleString;
    private boolean mHasDarkBackground;
    private int mLastInvocationType;
    private final float mRiseDistance;
    private String mSqueezeString;
    private final int mTextColorDark;
    private final int mTextColorLight;

    public PromptView(Context context) {
        this(context, null);
    }

    public PromptView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PromptView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public PromptView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDecelerateInterpolator = new DecelerateInterpolator(2.0f);
        this.mHasDarkBackground = false;
        this.mEnabled = false;
        this.mLastInvocationType = 0;
        this.mTextColorDark = getContext().getColor(R$color.transcription_text_dark);
        this.mTextColorLight = getContext().getColor(R$color.transcription_text_light);
        this.mRiseDistance = getResources().getDimension(R$dimen.assist_prompt_rise_distance);
        this.mHandleString = getResources().getString(R$string.handle_invocation_prompt);
        this.mSqueezeString = getResources().getString(R$string.squeeze_invocation_prompt);
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).addCallback(this);
        setHasDarkBackground(!this.mHasDarkBackground);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onConfigChanged(Configuration configuration) {
        this.mHandleString = getResources().getString(R$string.handle_invocation_prompt);
        this.mSqueezeString = getResources().getString(R$string.squeeze_invocation_prompt);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onDensityOrFontScaleChanged() {
        setTextSize(0, ((TextView) this).mContext.getResources().getDimension(R$dimen.transcription_text_size));
        updateViewHeight();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        updateViewHeight();
    }

    public void setHasDarkBackground(boolean z) {
        if (z != this.mHasDarkBackground) {
            setTextColor(z ? this.mTextColorDark : this.mTextColorLight);
            this.mHasDarkBackground = z;
        }
    }

    public void enable() {
        this.mEnabled = true;
    }

    public void disable() {
        this.mEnabled = false;
        setVisibility(8);
    }

    public void onInvocationProgress(int i, float f) {
        if (f <= 1.0f) {
            if (f == 0.0f) {
                setVisibility(8);
                setAlpha(0.0f);
                setTranslationY(0.0f);
                this.mLastInvocationType = 0;
            } else if (this.mEnabled) {
                if (i != 1) {
                    if (i != 2) {
                        this.mLastInvocationType = 0;
                        setText("");
                    } else if (this.mLastInvocationType != i) {
                        this.mLastInvocationType = i;
                        setText(this.mSqueezeString);
                        announceForAccessibility(this.mSqueezeString);
                    }
                } else if (this.mLastInvocationType != i) {
                    this.mLastInvocationType = i;
                    setText(this.mHandleString);
                    announceForAccessibility(this.mHandleString);
                }
                setVisibility(0);
                setTranslationYProgress(f);
                setAlphaProgress(i, f);
            }
        }
    }

    private void setTranslationYProgress(float f) {
        setTranslationY((-this.mRiseDistance) * f);
    }

    private void setAlphaProgress(int i, float f) {
        if (i != 2 && f > 0.8f) {
            setAlpha(0.0f);
        } else if (f > 0.32000002f) {
            setAlpha(1.0f);
        } else {
            setAlpha(this.mDecelerateInterpolator.getInterpolation(f / 0.32000002f));
        }
    }

    private void updateViewHeight() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = (int) (getResources().getDimension(R$dimen.assist_prompt_start_height) + this.mRiseDistance + ((TextView) this).mContext.getResources().getDimension(R$dimen.transcription_text_size));
        }
        requestLayout();
    }
}
