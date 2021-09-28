package com.android.systemui.statusbar.notification.row;

import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public final /* synthetic */ class ExpandableOutlineView$$ExternalSyntheticLambda0 implements BiConsumer {
    public static final /* synthetic */ ExpandableOutlineView$$ExternalSyntheticLambda0 INSTANCE = new ExpandableOutlineView$$ExternalSyntheticLambda0();

    private /* synthetic */ ExpandableOutlineView$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        ((ExpandableOutlineView) obj).setTopRoundnessInternal(((Float) obj2).floatValue());
    }
}
