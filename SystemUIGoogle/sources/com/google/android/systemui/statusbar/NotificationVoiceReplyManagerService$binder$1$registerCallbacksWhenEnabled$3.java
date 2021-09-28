package com.google.android.systemui.statusbar;

import kotlin.Pair;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$3 extends Lambda implements Function2<Pair<? extends Integer, ? extends Integer>, Pair<? extends Integer, ? extends Integer>, Boolean> {
    public static final NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$3 INSTANCE = new NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$3();

    NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$3() {
        super(2);
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Boolean invoke(Pair<? extends Integer, ? extends Integer> pair, Pair<? extends Integer, ? extends Integer> pair2) {
        return Boolean.valueOf(invoke((Pair<Integer, Integer>) pair, (Pair<Integer, Integer>) pair2));
    }

    public final boolean invoke(Pair<Integer, Integer> pair, Pair<Integer, Integer> pair2) {
        Intrinsics.checkNotNullParameter(pair, "$dstr$_u24__u24$old");
        Intrinsics.checkNotNullParameter(pair2, "$dstr$_u24__u24$new");
        return (pair.component2().intValue() != 0) == (pair2.component2().intValue() != 0);
    }
}
