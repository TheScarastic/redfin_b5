package com.google.android.systemui.statusbar;

import android.os.IBinder;
import com.android.systemui.statusbar.notification.people.Subscription;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt;
import java.math.BigInteger;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerServiceKt {
    private static final byte[][] AGSA_CERTS;
    private static final byte[] DEBUG_DIGEST_GMSCORE;
    private static final byte[] RELEASE_DIGEST_GMSCORE;

    public static final Subscription onDeath(IBinder iBinder, Function0<Unit> function0) {
        NotificationVoiceReplyManagerServiceKt$onDeath$recipient$1 notificationVoiceReplyManagerServiceKt$onDeath$recipient$1 = new IBinder.DeathRecipient(function0) { // from class: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$onDeath$recipient$1
            final /* synthetic */ Function0<Unit> $block;

            /* access modifiers changed from: package-private */
            {
                this.$block = r1;
            }

            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                this.$block.invoke();
            }
        };
        iBinder.linkToDeath(notificationVoiceReplyManagerServiceKt$onDeath$recipient$1, 0);
        return NotificationVoiceReplyManagerKt.Subscription(new Function0<Unit>(iBinder, notificationVoiceReplyManagerServiceKt$onDeath$recipient$1) { // from class: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt$onDeath$1
            final /* synthetic */ IBinder.DeathRecipient $recipient;
            final /* synthetic */ IBinder $this_onDeath;

            /* access modifiers changed from: package-private */
            {
                this.$this_onDeath = r1;
                this.$recipient = r2;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                this.$this_onDeath.unlinkToDeath(this.$recipient, 0);
            }
        });
    }

    public static final <T> Flow<T> startingWith(Flow<? extends T> flow, T t) {
        return FlowKt.flow(new NotificationVoiceReplyManagerServiceKt$startingWith$1$1(t, flow, null));
    }

    public static final <T> StateFlow<T> stateIn(Flow<? extends T> flow, CoroutineScope coroutineScope, T t) {
        MutableStateFlow MutableStateFlow = StateFlowKt.MutableStateFlow(t);
        Job unused = BuildersKt__Builders_commonKt.launch$default(coroutineScope, null, null, new NotificationVoiceReplyManagerServiceKt$stateIn$1(flow, MutableStateFlow, null), 3, null);
        return MutableStateFlow;
    }

    static {
        byte[] byteArray = new BigInteger("1975b2f17177bc89a5dff31f9e64a6cae281a53dc1d1d59b1d147fe1c82afa00", 16).toByteArray();
        DEBUG_DIGEST_GMSCORE = byteArray;
        byte[] byteArray2 = new BigInteger("f0fd6c5b410f25cb25c3b53346c8972fae30f8ee7411df910480ad6b2d60db83", 16).toByteArray();
        RELEASE_DIGEST_GMSCORE = byteArray2;
        AGSA_CERTS = new byte[][]{byteArray, byteArray2};
    }
}
