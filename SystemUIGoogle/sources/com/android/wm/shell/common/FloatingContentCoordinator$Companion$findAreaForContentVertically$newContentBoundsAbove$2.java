package com.android.wm.shell.common;

import android.graphics.Rect;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref$ObjectRef;
/* compiled from: FloatingContentCoordinator.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsAbove$2 extends Lambda implements Function0<Rect> {
    final /* synthetic */ Rect $contentRect;
    final /* synthetic */ Rect $newlyOverlappingRect;
    final /* synthetic */ Ref$ObjectRef<List<Rect>> $rectsToAvoidAbove;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsAbove$2(Rect rect, Ref$ObjectRef<List<Rect>> ref$ObjectRef, Rect rect2) {
        super(0);
        this.$contentRect = rect;
        this.$rectsToAvoidAbove = ref$ObjectRef;
        this.$newlyOverlappingRect = rect2;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Rect invoke() {
        return FloatingContentCoordinator.Companion.findAreaForContentAboveOrBelow(this.$contentRect, CollectionsKt___CollectionsKt.plus(this.$rectsToAvoidAbove.element, this.$newlyOverlappingRect), true);
    }
}
