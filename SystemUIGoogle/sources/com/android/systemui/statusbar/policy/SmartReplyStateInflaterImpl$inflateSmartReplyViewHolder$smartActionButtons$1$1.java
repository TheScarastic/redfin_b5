package com.android.systemui.statusbar.policy;

import android.app.Notification;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
final class SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1 extends Lambda implements Function1<Notification.Action, Boolean> {
    public static final SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1 INSTANCE = new SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1();

    SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1() {
        super(1);
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Boolean invoke(Notification.Action action) {
        return Boolean.valueOf(invoke(action));
    }

    public final boolean invoke(Notification.Action action) {
        return action.actionIntent != null;
    }
}
