package com.android.systemui.accessibility.floatingmenu;

import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class AccessibilityFloatingMenuView$1$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ AccessibilityFloatingMenuView.AnonymousClass1 f$0;

    public /* synthetic */ AccessibilityFloatingMenuView$1$$ExternalSyntheticLambda0(AccessibilityFloatingMenuView.AnonymousClass1 r1) {
        this.f$0 = r1;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$onAnimationEnd$0((AccessibilityFloatingMenuView.OnDragEndListener) obj);
    }
}
