package com.android.systemui.statusbar.notification.row;

import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class ExpandableOutlineView$$ExternalSyntheticLambda3 implements Function {
    public static final /* synthetic */ ExpandableOutlineView$$ExternalSyntheticLambda3 INSTANCE = new ExpandableOutlineView$$ExternalSyntheticLambda3();

    private /* synthetic */ ExpandableOutlineView$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return Float.valueOf(((ExpandableOutlineView) obj).getCurrentTopRoundness());
    }
}
