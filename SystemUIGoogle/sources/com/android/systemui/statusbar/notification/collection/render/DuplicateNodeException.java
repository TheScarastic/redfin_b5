package com.android.systemui.statusbar.notification.collection.render;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: ShadeViewDiffer.kt */
/* loaded from: classes.dex */
final class DuplicateNodeException extends RuntimeException {
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public DuplicateNodeException(String str) {
        super(str);
        Intrinsics.checkNotNullParameter(str, "message");
    }
}
