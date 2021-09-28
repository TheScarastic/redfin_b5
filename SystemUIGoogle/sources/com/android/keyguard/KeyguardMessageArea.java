package com.android.keyguard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.R$id;
import com.android.systemui.R$style;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class KeyguardMessageArea extends TextView {
    private static final Object ANNOUNCE_TOKEN = new Object();
    private boolean mAltBouncerShowing;
    private boolean mBouncerVisible;
    private ColorStateList mDefaultColorState;
    private CharSequence mMessage;
    private ColorStateList mNextMessageColorState = ColorStateList.valueOf(-1);
    private final Handler mHandler = new Handler(Looper.myLooper());

    public KeyguardMessageArea(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayerType(2, null);
        onThemeChanged();
    }

    public void setNextMessageColor(ColorStateList colorStateList) {
        this.mNextMessageColorState = colorStateList;
    }

    /* access modifiers changed from: package-private */
    public void onThemeChanged() {
        TypedArray obtainStyledAttributes = ((TextView) this).mContext.obtainStyledAttributes(new int[]{16842806});
        ColorStateList valueOf = ColorStateList.valueOf(obtainStyledAttributes.getColor(0, -65536));
        obtainStyledAttributes.recycle();
        this.mDefaultColorState = valueOf;
        update();
    }

    /* access modifiers changed from: package-private */
    public void reloadColor() {
        this.mDefaultColorState = Utils.getColorAttr(getContext(), 16842806);
        update();
    }

    /* access modifiers changed from: package-private */
    public void onDensityOrFontScaleChanged() {
        TypedArray obtainStyledAttributes = ((TextView) this).mContext.obtainStyledAttributes(R$style.Keyguard_TextView, new int[]{16842901});
        setTextSize(0, (float) obtainStyledAttributes.getDimensionPixelSize(0, 0));
        obtainStyledAttributes.recycle();
    }

    public void setMessage(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            securityMessageChanged(charSequence);
        } else {
            clearMessage();
        }
    }

    public void setMessage(int i) {
        setMessage(i != 0 ? getContext().getResources().getText(i) : null);
    }

    public static KeyguardMessageArea findSecurityMessageDisplay(View view) {
        int i = R$id.keyguard_message_area;
        KeyguardMessageArea keyguardMessageArea = (KeyguardMessageArea) view.findViewById(i);
        if (keyguardMessageArea == null) {
            keyguardMessageArea = (KeyguardMessageArea) view.getRootView().findViewById(i);
        }
        if (keyguardMessageArea != null) {
            return keyguardMessageArea;
        }
        throw new RuntimeException("Can't find keyguard_message_area in " + view.getClass());
    }

    private void securityMessageChanged(CharSequence charSequence) {
        this.mMessage = charSequence;
        update();
        Handler handler = this.mHandler;
        Object obj = ANNOUNCE_TOKEN;
        handler.removeCallbacksAndMessages(obj);
        this.mHandler.postAtTime(new AnnounceRunnable(this, getText()), obj, SystemClock.uptimeMillis() + 250);
    }

    private void clearMessage() {
        this.mMessage = null;
        update();
    }

    /* access modifiers changed from: package-private */
    public void update() {
        CharSequence charSequence = this.mMessage;
        setVisibility((TextUtils.isEmpty(charSequence) || (!this.mBouncerVisible && !this.mAltBouncerShowing)) ? 4 : 0);
        setText(charSequence);
        ColorStateList colorStateList = this.mDefaultColorState;
        if (this.mNextMessageColorState.getDefaultColor() != -1) {
            colorStateList = this.mNextMessageColorState;
            this.mNextMessageColorState = ColorStateList.valueOf(-1);
        }
        setTextColor(colorStateList);
    }

    public void setBouncerVisible(boolean z) {
        this.mBouncerVisible = z;
    }

    /* access modifiers changed from: package-private */
    public void setAltBouncerShowing(boolean z) {
        if (this.mAltBouncerShowing != z) {
            this.mAltBouncerShowing = z;
            update();
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AnnounceRunnable implements Runnable {
        private final WeakReference<View> mHost;
        private final CharSequence mTextToAnnounce;

        AnnounceRunnable(View view, CharSequence charSequence) {
            this.mHost = new WeakReference<>(view);
            this.mTextToAnnounce = charSequence;
        }

        @Override // java.lang.Runnable
        public void run() {
            View view = this.mHost.get();
            if (view != null) {
                view.announceForAccessibility(this.mTextToAnnounce);
            }
        }
    }
}
