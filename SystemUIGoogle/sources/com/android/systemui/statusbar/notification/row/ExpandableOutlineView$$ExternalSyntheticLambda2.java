package com.android.systemui.statusbar.notification.row;

import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class ExpandableOutlineView$$ExternalSyntheticLambda2 implements Function {
    public static final /* synthetic */ ExpandableOutlineView$$ExternalSyntheticLambda2 INSTANCE = new ExpandableOutlineView$$ExternalSyntheticLambda2();

    private /* synthetic */ ExpandableOutlineView$$ExternalSyntheticLambda2() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return Float.valueOf(((ExpandableOutlineView) obj).getCurrentBottomRoundness());
    }
}
