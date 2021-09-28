package com.android.systemui.statusbar.notification.row;

import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public final /* synthetic */ class ExpandableOutlineView$$ExternalSyntheticLambda1 implements BiConsumer {
    public static final /* synthetic */ ExpandableOutlineView$$ExternalSyntheticLambda1 INSTANCE = new ExpandableOutlineView$$ExternalSyntheticLambda1();

    private /* synthetic */ ExpandableOutlineView$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        ((ExpandableOutlineView) obj).setBottomRoundnessInternal(((Float) obj2).floatValue());
    }
}
