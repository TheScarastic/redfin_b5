package com.google.android.systemui.statusbar.notification.voicereplies;

import android.view.View;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyManager.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerKt$getReplyButton$1 extends Lambda implements Function1<View, Boolean> {
    public static final NotificationVoiceReplyManagerKt$getReplyButton$1 INSTANCE = new NotificationVoiceReplyManagerKt$getReplyButton$1();

    NotificationVoiceReplyManagerKt$getReplyButton$1() {
        super(1);
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Boolean invoke(View view) {
        return Boolean.valueOf(invoke(view));
    }

    public final boolean invoke(View view) {
        return view.getId() == 16908697;
    }
}
