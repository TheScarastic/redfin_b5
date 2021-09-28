package com.android.systemui.keyguard;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
/* loaded from: classes.dex */
public class KeyguardIndication {
    private final Drawable mBackground;
    private final Drawable mIcon;
    private final CharSequence mMessage;
    private final Long mMinVisibilityMillis;
    private final View.OnClickListener mOnClickListener;
    private final ColorStateList mTextColor;

    private KeyguardIndication(CharSequence charSequence, ColorStateList colorStateList, Drawable drawable, View.OnClickListener onClickListener, Drawable drawable2, Long l) {
        this.mMessage = charSequence;
        this.mTextColor = colorStateList;
        this.mIcon = drawable;
        this.mOnClickListener = onClickListener;
        this.mBackground = drawable2;
        this.mMinVisibilityMillis = l;
    }

    public CharSequence getMessage() {
        return this.mMessage;
    }

    public ColorStateList getTextColor() {
        return this.mTextColor;
    }

    public Drawable getIcon() {
        return this.mIcon;
    }

    public View.OnClickListener getClickListener() {
        return this.mOnClickListener;
    }

    public Long getMinVisibilityMillis() {
        return this.mMinVisibilityMillis;
    }

    public String toString() {
        String str = "KeyguardIndication{";
        if (!TextUtils.isEmpty(this.mMessage)) {
            str = str + "mMessage=" + ((Object) this.mMessage);
        }
        if (this.mIcon != null) {
            str = str + " mIcon=" + this.mIcon;
        }
        if (this.mOnClickListener != null) {
            str = str + " mOnClickListener=" + this.mOnClickListener;
        }
        if (this.mBackground != null) {
            str = str + " mBackground=" + this.mBackground;
        }
        if (this.mMinVisibilityMillis != null) {
            str = str + " mMinVisibilityMillis=" + this.mMinVisibilityMillis;
        }
        return str + "}";
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private Drawable mBackground;
        private Drawable mIcon;
        private CharSequence mMessage;
        private Long mMinVisibilityMillis;
        private View.OnClickListener mOnClickListener;
        private ColorStateList mTextColor;

        public Builder setMessage(CharSequence charSequence) {
            this.mMessage = charSequence;
            return this;
        }

        public Builder setTextColor(ColorStateList colorStateList) {
            this.mTextColor = colorStateList;
            return this;
        }

        public Builder setIcon(Drawable drawable) {
            this.mIcon = drawable;
            return this;
        }

        public Builder setClickListener(View.OnClickListener onClickListener) {
            this.mOnClickListener = onClickListener;
            return this;
        }

        public Builder setBackground(Drawable drawable) {
            this.mBackground = drawable;
            return this;
        }

        public Builder setMinVisibilityMillis(Long l) {
            this.mMinVisibilityMillis = l;
            return this;
        }

        public KeyguardIndication build() {
            if (!TextUtils.isEmpty(this.mMessage) || this.mIcon != null) {
                ColorStateList colorStateList = this.mTextColor;
                if (colorStateList != null) {
                    return new KeyguardIndication(this.mMessage, colorStateList, this.mIcon, this.mOnClickListener, this.mBackground, this.mMinVisibilityMillis);
                }
                throw new IllegalStateException("text color must be set");
            }
            throw new IllegalStateException("message or icon must be set");
        }
    }
}
