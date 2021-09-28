package com.android.wm.shell.common;

import android.graphics.Rect;
import com.android.wm.shell.common.FloatingContentCoordinator;
import kotlin.Lazy;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: FloatingContentCoordinator.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class FloatingContentCoordinator$Companion$findAreaForContentVertically$positionBelowInBounds$2 extends Lambda implements Function0<Boolean> {
    final /* synthetic */ Rect $allowedBounds;
    final /* synthetic */ Lazy<Rect> $newContentBoundsBelow$delegate;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public FloatingContentCoordinator$Companion$findAreaForContentVertically$positionBelowInBounds$2(Rect rect, Lazy<Rect> lazy) {
        super(0);
        this.$allowedBounds = rect;
        this.$newContentBoundsBelow$delegate = lazy;
    }

    /* Return type fixed from 'boolean' to match base method */
    /* JADX WARN: Type inference failed for: r1v3, types: [boolean, java.lang.Boolean] */
    @Override // kotlin.jvm.functions.Function0
    public final Boolean invoke() {
        return this.$allowedBounds.contains(FloatingContentCoordinator.Companion.m523findAreaForContentVertically$lambda3(this.$newContentBoundsBelow$delegate));
    }
}
