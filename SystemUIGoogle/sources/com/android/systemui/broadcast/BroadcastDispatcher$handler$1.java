package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BroadcastDispatcher.kt */
/* loaded from: classes.dex */
public final class BroadcastDispatcher$handler$1 extends Handler {
    final /* synthetic */ BroadcastDispatcher this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public BroadcastDispatcher$handler$1(BroadcastDispatcher broadcastDispatcher, Looper looper) {
        super(looper);
        this.this$0 = broadcastDispatcher;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i;
        Intrinsics.checkNotNullParameter(message, "msg");
        int i2 = message.what;
        if (i2 == 0) {
            Object obj = message.obj;
            Objects.requireNonNull(obj, "null cannot be cast to non-null type com.android.systemui.broadcast.ReceiverData");
            ReceiverData receiverData = (ReceiverData) obj;
            if (receiverData.getUser().getIdentifier() == -2) {
                i = this.this$0.userTracker.getUserId();
            } else {
                i = receiverData.getUser().getIdentifier();
            }
            if (i >= -1) {
                UserBroadcastDispatcher userBroadcastDispatcher = (UserBroadcastDispatcher) this.this$0.receiversByUser.get(i, this.this$0.createUBRForUser(i));
                this.this$0.receiversByUser.put(i, userBroadcastDispatcher);
                userBroadcastDispatcher.registerReceiver(receiverData);
                return;
            }
            throw new IllegalStateException("Attempting to register receiver for invalid user {" + i + '}');
        } else if (i2 == 1) {
            int i3 = 0;
            int size = this.this$0.receiversByUser.size();
            if (size > 0) {
                while (true) {
                    int i4 = i3 + 1;
                    Object obj2 = message.obj;
                    Objects.requireNonNull(obj2, "null cannot be cast to non-null type android.content.BroadcastReceiver");
                    ((UserBroadcastDispatcher) this.this$0.receiversByUser.valueAt(i3)).unregisterReceiver((BroadcastReceiver) obj2);
                    if (i4 < size) {
                        i3 = i4;
                    } else {
                        return;
                    }
                }
            }
        } else if (i2 != 2) {
            super.handleMessage(message);
        } else {
            UserBroadcastDispatcher userBroadcastDispatcher2 = (UserBroadcastDispatcher) this.this$0.receiversByUser.get(message.arg1);
            if (userBroadcastDispatcher2 != null) {
                Object obj3 = message.obj;
                Objects.requireNonNull(obj3, "null cannot be cast to non-null type android.content.BroadcastReceiver");
                userBroadcastDispatcher2.unregisterReceiver((BroadcastReceiver) obj3);
            }
        }
    }
}
