package com.android.systemui.navigationbar.buttons;

import android.content.Context;
import android.view.View;
import com.android.systemui.navigationbar.RotationButton;
import com.android.systemui.navigationbar.RotationButtonController;
import com.android.systemui.navigationbar.buttons.ContextualButton;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class RotationContextButton extends ContextualButton implements RotationButton {
    private RotationButtonController mRotationButtonController;

    public RotationContextButton(int i, Context context, int i2) {
        super(i, context, i2);
    }

    @Override // com.android.systemui.navigationbar.RotationButton
    public void setRotationButtonController(RotationButtonController rotationButtonController) {
        this.mRotationButtonController = rotationButtonController;
    }

    @Override // com.android.systemui.navigationbar.RotationButton
    public void setVisibilityChangedCallback(final Consumer<Boolean> consumer) {
        setListener(new ContextualButton.ContextButtonListener() { // from class: com.android.systemui.navigationbar.buttons.RotationContextButton.1
            @Override // com.android.systemui.navigationbar.buttons.ContextualButton.ContextButtonListener
            public void onVisibilityChanged(ContextualButton contextualButton, boolean z) {
                Consumer consumer2 = consumer;
                if (consumer2 != null) {
                    consumer2.accept(Boolean.valueOf(z));
                }
            }
        });
    }

    @Override // com.android.systemui.navigationbar.buttons.ContextualButton, com.android.systemui.navigationbar.buttons.ButtonDispatcher
    public void setVisibility(int i) {
        super.setVisibility(i);
        KeyButtonDrawable imageDrawable = getImageDrawable();
        if (i == 0 && imageDrawable != null) {
            imageDrawable.resetAnimation();
            imageDrawable.startAnimation();
        }
    }

    @Override // com.android.systemui.navigationbar.buttons.ContextualButton
    protected KeyButtonDrawable getNewDrawable(int i, int i2) {
        return KeyButtonDrawable.create(this.mRotationButtonController.getContext(), i, i2, this.mRotationButtonController.getIconResId(), false, null);
    }

    @Override // com.android.systemui.navigationbar.RotationButton
    public boolean acceptRotationProposal() {
        View currentView = getCurrentView();
        return currentView != null && currentView.isAttachedToWindow();
    }
}
