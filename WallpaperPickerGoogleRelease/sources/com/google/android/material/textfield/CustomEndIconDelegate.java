package com.google.android.material.textfield;

import com.google.android.material.internal.CheckableImageButton;
/* loaded from: classes.dex */
public class CustomEndIconDelegate extends EndIconDelegate {
    public CustomEndIconDelegate(TextInputLayout textInputLayout) {
        super(textInputLayout);
    }

    @Override // com.google.android.material.textfield.EndIconDelegate
    public void initialize() {
        this.textInputLayout.setEndIconOnClickListener(null);
        TextInputLayout textInputLayout = this.textInputLayout;
        textInputLayout.endIconOnLongClickListener = null;
        CheckableImageButton checkableImageButton = textInputLayout.endIconView;
        checkableImageButton.setOnLongClickListener(null);
        TextInputLayout.setIconClickable(checkableImageButton, null);
    }
}
