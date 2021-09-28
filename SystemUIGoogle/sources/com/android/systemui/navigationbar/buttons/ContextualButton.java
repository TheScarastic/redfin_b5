package com.android.systemui.navigationbar.buttons;

import android.content.Context;
/* loaded from: classes.dex */
public class ContextualButton extends ButtonDispatcher {
    private ContextualButtonGroup mGroup;
    protected final int mIconResId;
    protected final Context mLightContext;
    private ContextButtonListener mListener;

    /* loaded from: classes.dex */
    public interface ContextButtonListener {
        void onVisibilityChanged(ContextualButton contextualButton, boolean z);
    }

    public ContextualButton(int i, Context context, int i2) {
        super(i);
        this.mLightContext = context;
        this.mIconResId = i2;
    }

    public void updateIcon(int i, int i2) {
        if (this.mIconResId != 0) {
            KeyButtonDrawable imageDrawable = getImageDrawable();
            KeyButtonDrawable newDrawable = getNewDrawable(i, i2);
            if (imageDrawable != null) {
                newDrawable.setDarkIntensity(imageDrawable.getDarkIntensity());
            }
            setImageDrawable(newDrawable);
        }
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonDispatcher
    public void setVisibility(int i) {
        super.setVisibility(i);
        KeyButtonDrawable imageDrawable = getImageDrawable();
        if (!(i == 0 || imageDrawable == null || !imageDrawable.canAnimate())) {
            imageDrawable.clearAnimationCallbacks();
            imageDrawable.resetAnimation();
        }
        ContextButtonListener contextButtonListener = this.mListener;
        if (contextButtonListener != null) {
            contextButtonListener.onVisibilityChanged(this, i == 0);
        }
    }

    public void setListener(ContextButtonListener contextButtonListener) {
        this.mListener = contextButtonListener;
    }

    public boolean show() {
        ContextualButtonGroup contextualButtonGroup = this.mGroup;
        if (contextualButtonGroup != null) {
            return contextualButtonGroup.setButtonVisibility(getId(), true) == 0;
        }
        setVisibility(0);
        return true;
    }

    public boolean hide() {
        ContextualButtonGroup contextualButtonGroup = this.mGroup;
        if (contextualButtonGroup == null) {
            setVisibility(4);
            return false;
        } else if (contextualButtonGroup.setButtonVisibility(getId(), false) != 0) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public void attachToGroup(ContextualButtonGroup contextualButtonGroup) {
        this.mGroup = contextualButtonGroup;
    }

    protected KeyButtonDrawable getNewDrawable(int i, int i2) {
        return KeyButtonDrawable.create(this.mLightContext, i, i2, this.mIconResId, false, null);
    }
}
