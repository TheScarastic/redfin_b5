package com.google.android.systemui.columbus.sensors;

import android.hardware.location.ContextHubClient;
import android.hardware.location.ContextHubClientCallback;
import android.hardware.location.NanoAppMessage;
import android.util.Log;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$GestureDetected;
import com.google.android.systemui.columbus.proto.nano.ColumbusProto$NanoappEvents;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CHREGestureSensor.kt */
/* loaded from: classes2.dex */
public final class CHREGestureSensor$contextHubClientCallback$1 extends ContextHubClientCallback {
    final /* synthetic */ CHREGestureSensor this$0;

    /* access modifiers changed from: package-private */
    public CHREGestureSensor$contextHubClientCallback$1(CHREGestureSensor cHREGestureSensor) {
        this.this$0 = cHREGestureSensor;
    }

    public void onMessageFromNanoApp(ContextHubClient contextHubClient, NanoAppMessage nanoAppMessage) {
        Intrinsics.checkNotNullParameter(contextHubClient, "client");
        Intrinsics.checkNotNullParameter(nanoAppMessage, "message");
        if (nanoAppMessage.getNanoAppId() == 5147455389092024345L) {
            try {
                int messageType = nanoAppMessage.getMessageType();
                if (messageType == 300) {
                    CHREGestureSensor cHREGestureSensor = this.this$0;
                    ColumbusProto$GestureDetected parseFrom = ColumbusProto$GestureDetected.parseFrom(nanoAppMessage.getMessageBody());
                    Intrinsics.checkNotNullExpressionValue(parseFrom, "parseFrom(message.messageBody)");
                    cHREGestureSensor.handleGestureDetection(parseFrom);
                } else if (messageType != 500) {
                    Log.e("Columbus/GestureSensor", Intrinsics.stringPlus("Unknown message type: ", Integer.valueOf(nanoAppMessage.getMessageType())));
                } else {
                    CHREGestureSensor cHREGestureSensor2 = this.this$0;
                    ColumbusProto$NanoappEvents parseFrom2 = ColumbusProto$NanoappEvents.parseFrom(nanoAppMessage.getMessageBody());
                    Intrinsics.checkNotNullExpressionValue(parseFrom2, "parseFrom(message.messageBody)");
                    cHREGestureSensor2.handleNanoappEvents(parseFrom2);
                }
            } catch (InvalidProtocolBufferNanoException e) {
                while (true) {
                    Log.e("Columbus/GestureSensor", "Invalid protocol buffer", e);
                    return;
                }
            }
        }
    }

    public void onHubReset(ContextHubClient contextHubClient) {
        Intrinsics.checkNotNullParameter(contextHubClient, "client");
        Log.d("Columbus/GestureSensor", Intrinsics.stringPlus("HubReset: ", Integer.valueOf(contextHubClient.getAttachedHub().getId())));
    }

    public void onNanoAppAborted(ContextHubClient contextHubClient, long j, int i) {
        Intrinsics.checkNotNullParameter(contextHubClient, "client");
        if (j == 5147455389092024345L) {
            Log.e("Columbus/GestureSensor", Intrinsics.stringPlus("Nanoapp aborted, code: ", Integer.valueOf(i)));
        }
    }

    public void onNanoAppLoaded(ContextHubClient contextHubClient, long j) {
        if (j == 5147455389092024345L && this.this$0.isListening()) {
            Log.d("Columbus/GestureSensor", "Nanoapp loaded");
            this.this$0.updateScreenState();
            this.this$0.startRecognizer();
        }
    }
}
