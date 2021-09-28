package com.google.android.systemui.elmyra.sensors;

import android.content.Context;
import android.hardware.location.ContextHubClient;
import android.hardware.location.ContextHubClientCallback;
import android.hardware.location.ContextHubInfo;
import android.hardware.location.ContextHubManager;
import android.hardware.location.NanoAppMessage;
import android.util.Log;
import android.util.TypedValue;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.google.android.systemui.elmyra.SnapshotConfiguration;
import com.google.android.systemui.elmyra.SnapshotController;
import com.google.android.systemui.elmyra.proto.nano.ChassisProtos$Chassis;
import com.google.android.systemui.elmyra.proto.nano.ContextHubMessages$GestureDetected;
import com.google.android.systemui.elmyra.proto.nano.ContextHubMessages$GestureProgress;
import com.google.android.systemui.elmyra.proto.nano.ContextHubMessages$RecognizerStart;
import com.google.android.systemui.elmyra.proto.nano.ContextHubMessages$SensitivityUpdate;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$Snapshot;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$SnapshotHeader;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
/* loaded from: classes2.dex */
public class CHREGestureSensor implements Dumpable, GestureSensor {
    private final Context mContext;
    private ContextHubClient mContextHubClient;
    private final ContextHubClientCallback mContextHubClientCallback = new ContextHubClientCallback() { // from class: com.google.android.systemui.elmyra.sensors.CHREGestureSensor.1
        public void onMessageFromNanoApp(ContextHubClient contextHubClient, NanoAppMessage nanoAppMessage) {
            if (nanoAppMessage.getNanoAppId() == 5147455389092024334L) {
                try {
                    int messageType = nanoAppMessage.getMessageType();
                    if (messageType != 1) {
                        switch (messageType) {
                            case 300:
                                CHREGestureSensor.this.mController.onGestureProgress(ContextHubMessages$GestureProgress.parseFrom(nanoAppMessage.getMessageBody()).progress);
                                return;
                            case 301:
                                ContextHubMessages$GestureDetected parseFrom = ContextHubMessages$GestureDetected.parseFrom(nanoAppMessage.getMessageBody());
                                CHREGestureSensor.this.mController.onGestureDetected(new GestureSensor.DetectionProperties(parseFrom.hapticConsumed, parseFrom.hostSuspended));
                                return;
                            case 302:
                                SnapshotProtos$Snapshot parseFrom2 = SnapshotProtos$Snapshot.parseFrom(nanoAppMessage.getMessageBody());
                                parseFrom2.sensitivitySetting = CHREGestureSensor.this.mGestureConfiguration.getSensitivity();
                                CHREGestureSensor.this.mController.onSnapshotReceived(parseFrom2);
                                return;
                            case 303:
                                CHREGestureSensor.this.mController.storeChassisConfiguration(ChassisProtos$Chassis.parseFrom(nanoAppMessage.getMessageBody()));
                                return;
                            case 304:
                            case 305:
                                return;
                            default:
                                Log.e("Elmyra/GestureSensor", "Unknown message type: " + nanoAppMessage.getMessageType());
                                return;
                        }
                    } else if (CHREGestureSensor.this.mIsListening) {
                        CHREGestureSensor.this.startRecognizer();
                    }
                } catch (InvalidProtocolBufferNanoException e) {
                    while (true) {
                        Log.e("Elmyra/GestureSensor", "Invalid protocol buffer", e);
                        return;
                    }
                }
            }
        }

        public void onHubReset(ContextHubClient contextHubClient) {
            Log.d("Elmyra/GestureSensor", "HubReset: " + contextHubClient.getAttachedHub().getId());
        }

        public void onNanoAppAborted(ContextHubClient contextHubClient, long j, int i) {
            if (j == 5147455389092024334L) {
                Log.e("Elmyra/GestureSensor", "Nanoapp aborted, code: " + i);
            }
        }
    };
    private int mContextHubRetryCount;
    private final AssistGestureController mController;
    private final GestureConfiguration mGestureConfiguration;
    private boolean mIsListening;
    private final float mProgressDetectThreshold;

    public CHREGestureSensor(Context context, GestureConfiguration gestureConfiguration, SnapshotConfiguration snapshotConfiguration) {
        this.mContext = context;
        TypedValue typedValue = new TypedValue();
        context.getResources().getValue(R$dimen.elmyra_progress_detect_threshold, typedValue, true);
        this.mProgressDetectThreshold = typedValue.getFloat();
        AssistGestureController assistGestureController = new AssistGestureController(context, this, gestureConfiguration, snapshotConfiguration);
        this.mController = assistGestureController;
        assistGestureController.setSnapshotListener(new SnapshotController.Listener() { // from class: com.google.android.systemui.elmyra.sensors.CHREGestureSensor$$ExternalSyntheticLambda0
            @Override // com.google.android.systemui.elmyra.SnapshotController.Listener
            public final void onSnapshotRequested(SnapshotProtos$SnapshotHeader snapshotProtos$SnapshotHeader) {
                CHREGestureSensor.this.lambda$new$0(snapshotProtos$SnapshotHeader);
            }
        });
        this.mGestureConfiguration = gestureConfiguration;
        gestureConfiguration.setListener(new GestureConfiguration.Listener() { // from class: com.google.android.systemui.elmyra.sensors.CHREGestureSensor$$ExternalSyntheticLambda1
            @Override // com.google.android.systemui.elmyra.sensors.config.GestureConfiguration.Listener
            public final void onGestureConfigurationChanged(GestureConfiguration gestureConfiguration2) {
                CHREGestureSensor.this.updateSensitivity(gestureConfiguration2);
            }
        });
        initializeContextHubClientIfNull();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(SnapshotProtos$SnapshotHeader snapshotProtos$SnapshotHeader) {
        sendMessageToNanoApp(203, MessageNano.toByteArray(snapshotProtos$SnapshotHeader));
    }

    @Override // com.google.android.systemui.elmyra.sensors.Sensor
    public void startListening() {
        this.mIsListening = true;
        startRecognizer();
    }

    @Override // com.google.android.systemui.elmyra.sensors.Sensor
    public boolean isListening() {
        return this.mIsListening;
    }

    @Override // com.google.android.systemui.elmyra.sensors.Sensor
    public void stopListening() {
        sendMessageToNanoApp(201, new byte[0]);
        this.mIsListening = false;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(CHREGestureSensor.class.getSimpleName() + " state:");
        printWriter.println("  mIsListening: " + this.mIsListening);
        if (this.mContextHubClient == null) {
            printWriter.println("  mContextHubClient is null. Likely no context hubs were found");
        }
        printWriter.println("  mContextHubRetryCount: " + this.mContextHubRetryCount);
        this.mController.dump(fileDescriptor, printWriter, strArr);
    }

    @Override // com.google.android.systemui.elmyra.sensors.GestureSensor
    public void setGestureListener(GestureSensor.Listener listener) {
        this.mController.setGestureListener(listener);
    }

    private void initializeContextHubClientIfNull() {
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.context_hub") && this.mContextHubClient == null) {
            ContextHubManager contextHubManager = (ContextHubManager) this.mContext.getSystemService("contexthub");
            List contextHubs = contextHubManager.getContextHubs();
            if (contextHubs.size() == 0) {
                Log.e("Elmyra/GestureSensor", "No context hubs found");
                return;
            }
            this.mContextHubClient = contextHubManager.createClient((ContextHubInfo) contextHubs.get(0), this.mContextHubClientCallback);
            this.mContextHubRetryCount++;
        }
    }

    /* access modifiers changed from: private */
    public void updateSensitivity(GestureConfiguration gestureConfiguration) {
        ContextHubMessages$SensitivityUpdate contextHubMessages$SensitivityUpdate = new MessageNano() { // from class: com.google.android.systemui.elmyra.proto.nano.ContextHubMessages$SensitivityUpdate
            public float sensitivity;

            {
                clear();
            }

            public ContextHubMessages$SensitivityUpdate clear() {
                this.sensitivity = 0.0f;
                this.cachedSize = -1;
                return this;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                if (Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f)) {
                    codedOutputByteBufferNano.writeFloat(1, this.sensitivity);
                }
                super.writeTo(codedOutputByteBufferNano);
            }

            /* access modifiers changed from: protected */
            @Override // com.google.protobuf.nano.MessageNano
            public int computeSerializedSize() {
                int computeSerializedSize = super.computeSerializedSize();
                return Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f) ? computeSerializedSize + CodedOutputByteBufferNano.computeFloatSize(1, this.sensitivity) : computeSerializedSize;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public ContextHubMessages$SensitivityUpdate mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    int readTag = codedInputByteBufferNano.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 13) {
                        this.sensitivity = codedInputByteBufferNano.readFloat();
                    } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                        return this;
                    }
                }
            }
        };
        contextHubMessages$SensitivityUpdate.sensitivity = gestureConfiguration.getSensitivity();
        sendMessageToNanoApp(202, MessageNano.toByteArray(contextHubMessages$SensitivityUpdate));
    }

    /* access modifiers changed from: private */
    public void startRecognizer() {
        ContextHubMessages$RecognizerStart contextHubMessages$RecognizerStart = new MessageNano() { // from class: com.google.android.systemui.elmyra.proto.nano.ContextHubMessages$RecognizerStart
            public float progressReportThreshold;
            public float sensitivity;

            {
                clear();
            }

            public ContextHubMessages$RecognizerStart clear() {
                this.progressReportThreshold = 0.0f;
                this.sensitivity = 0.0f;
                this.cachedSize = -1;
                return this;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                if (Float.floatToIntBits(this.progressReportThreshold) != Float.floatToIntBits(0.0f)) {
                    codedOutputByteBufferNano.writeFloat(1, this.progressReportThreshold);
                }
                if (Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f)) {
                    codedOutputByteBufferNano.writeFloat(2, this.sensitivity);
                }
                super.writeTo(codedOutputByteBufferNano);
            }

            /* access modifiers changed from: protected */
            @Override // com.google.protobuf.nano.MessageNano
            public int computeSerializedSize() {
                int computeSerializedSize = super.computeSerializedSize();
                if (Float.floatToIntBits(this.progressReportThreshold) != Float.floatToIntBits(0.0f)) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeFloatSize(1, this.progressReportThreshold);
                }
                return Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f) ? computeSerializedSize + CodedOutputByteBufferNano.computeFloatSize(2, this.sensitivity) : computeSerializedSize;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public ContextHubMessages$RecognizerStart mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    int readTag = codedInputByteBufferNano.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 13) {
                        this.progressReportThreshold = codedInputByteBufferNano.readFloat();
                    } else if (readTag == 21) {
                        this.sensitivity = codedInputByteBufferNano.readFloat();
                    } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                        return this;
                    }
                }
            }
        };
        contextHubMessages$RecognizerStart.progressReportThreshold = this.mProgressDetectThreshold;
        contextHubMessages$RecognizerStart.sensitivity = this.mGestureConfiguration.getSensitivity();
        sendMessageToNanoApp(200, MessageNano.toByteArray(contextHubMessages$RecognizerStart));
        if (this.mController.getChassisConfiguration() == null) {
            sendMessageToNanoApp(204, new byte[0]);
        }
    }

    private void sendMessageToNanoApp(int i, byte[] bArr) {
        initializeContextHubClientIfNull();
        if (this.mContextHubClient == null) {
            Log.e("Elmyra/GestureSensor", "ContextHubClient null");
            return;
        }
        int sendMessageToNanoApp = this.mContextHubClient.sendMessageToNanoApp(NanoAppMessage.createMessageToNanoApp(5147455389092024334L, i, bArr));
        if (sendMessageToNanoApp != 0) {
            Log.e("Elmyra/GestureSensor", String.format("Unable to send message %d to nanoapp, error code %d", Integer.valueOf(i), Integer.valueOf(sendMessageToNanoApp)));
        }
    }
}
