package com.google.android.material.textfield;
/* loaded from: classes2.dex */
class CustomEndIconDelegate extends EndIconDelegate {
    /* access modifiers changed from: package-private */
    public CustomEndIconDelegate(TextInputLayout textInputLayout) {
        super(textInputLayout);
    }

    /* access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.EndIconDelegate
    public void initialize() {
        this.textInputLayout.setEndIconOnClickListener(null);
        this.textInputLayout.setEndIconOnLongClickListener(null);
    }
}
