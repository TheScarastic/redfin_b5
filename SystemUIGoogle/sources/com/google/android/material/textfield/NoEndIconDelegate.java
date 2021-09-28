package com.google.android.material.textfield;
/* loaded from: classes2.dex */
class NoEndIconDelegate extends EndIconDelegate {
    /* access modifiers changed from: package-private */
    public NoEndIconDelegate(TextInputLayout textInputLayout) {
        super(textInputLayout);
    }

    /* access modifiers changed from: package-private */
    @Override // com.google.android.material.textfield.EndIconDelegate
    public void initialize() {
        this.textInputLayout.setEndIconOnClickListener(null);
        this.textInputLayout.setEndIconDrawable(null);
        this.textInputLayout.setEndIconContentDescription(null);
    }
}
