package com.google.android.systemui.statusbar.phone;

import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyClient;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class StatusBarGoogle$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ StatusBarGoogle$$ExternalSyntheticLambda0 INSTANCE = new StatusBarGoogle$$ExternalSyntheticLambda0();

    private /* synthetic */ StatusBarGoogle$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((NotificationVoiceReplyClient) obj).startClient();
    }
}
