package com.android.keyguard;

import android.view.ViewGroup;
import com.android.systemui.util.ViewController;
/* loaded from: classes.dex */
public class KeyguardRootViewController extends ViewController<ViewGroup> {
    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
    }

    public KeyguardRootViewController(ViewGroup viewGroup) {
        super(viewGroup);
    }

    public ViewGroup getView() {
        return (ViewGroup) this.mView;
    }
}
