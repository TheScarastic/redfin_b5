package com.google.android.systemui.elmyra;

import android.app.StatsManager;
import android.content.Context;
import android.util.Log;
import android.util.StatsEvent;
import com.android.internal.util.ConcurrentUtils;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.google.android.systemui.elmyra.proto.nano.ChassisProtos$Chassis;
import com.google.android.systemui.elmyra.proto.nano.ElmyraAtoms$ElmyraSnapshot;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$Snapshot;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
/* loaded from: classes2.dex */
public class WestworldLogger implements GestureSensor.Listener {
    private CountDownLatch mCountDownLatch;
    private GestureConfiguration mGestureConfiguration;
    private SnapshotController mSnapshotController;
    private final StatsManager.StatsPullAtomCallback mWestworldCallback = new StatsManager.StatsPullAtomCallback() { // from class: com.google.android.systemui.elmyra.WestworldLogger$$ExternalSyntheticLambda0
        public final int onPullAtom(int i, List list) {
            return WestworldLogger.m639$r8$lambda$pe0Ao3clsrFKJNNv54yuPouSIw(WestworldLogger.this, i, list);
        }
    };
    private ChassisProtos$Chassis mChassis = null;
    private SnapshotProtos$Snapshot mSnapshot = null;
    private Object mMutex = new Object();

    /* access modifiers changed from: private */
    public /* synthetic */ int lambda$new$0(int i, List list) {
        Log.d("Elmyra/Logger", "Receiving pull request from statsd.");
        return pull(i, list);
    }

    public WestworldLogger(Context context, GestureConfiguration gestureConfiguration, SnapshotController snapshotController) {
        this.mGestureConfiguration = gestureConfiguration;
        this.mSnapshotController = snapshotController;
        registerWithWestworld(context);
    }

    public void registerWithWestworld(Context context) {
        StatsManager statsManager = (StatsManager) context.getSystemService("stats");
        if (statsManager == null) {
            Log.d("Elmyra/Logger", "Failed to get StatsManager");
        }
        try {
            statsManager.setPullAtomCallback(150000, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mWestworldCallback);
        } catch (RuntimeException e) {
            Log.d("Elmyra/Logger", "Failed to register callback with StatsManager");
            e.printStackTrace();
        }
    }

    public void didReceiveChassis(ChassisProtos$Chassis chassisProtos$Chassis) {
        this.mChassis = chassisProtos$Chassis;
    }

    @Override // com.google.android.systemui.elmyra.sensors.GestureSensor.Listener
    public void onGestureProgress(GestureSensor gestureSensor, float f, int i) {
        SysUiStatsLog.write(176, (int) (f * 100.0f));
        SysUiStatsLog.write(174, i);
    }

    @Override // com.google.android.systemui.elmyra.sensors.GestureSensor.Listener
    public void onGestureDetected(GestureSensor gestureSensor, GestureSensor.DetectionProperties detectionProperties) {
        SysUiStatsLog.write(174, 3);
    }

    public void querySubmitted() {
        SysUiStatsLog.write(175, 2);
    }

    public int pull(int i, List<StatsEvent> list) {
        if (this.mSnapshotController == null) {
            Log.d("Elmyra/Logger", "Snapshot Controller is null, returning.");
            return 1;
        }
        synchronized (this.mMutex) {
            if (this.mCountDownLatch != null) {
                return 1;
            }
            this.mCountDownLatch = new CountDownLatch(1);
            this.mSnapshotController.onWestworldPull();
            try {
                long currentTimeMillis = System.currentTimeMillis();
                this.mCountDownLatch.await(50, TimeUnit.MILLISECONDS);
                Log.d("Elmyra/Logger", "Snapshot took " + Long.toString(System.currentTimeMillis() - currentTimeMillis) + " milliseconds.");
            } catch (IllegalMonitorStateException e) {
                Log.d("Elmyra/Logger", e.getMessage());
            } catch (InterruptedException e2) {
                Log.d("Elmyra/Logger", e2.getMessage());
            }
            synchronized (this.mMutex) {
                if (!(this.mSnapshot == null || this.mChassis == null)) {
                    ElmyraAtoms$ElmyraSnapshot elmyraAtoms$ElmyraSnapshot = new MessageNano() { // from class: com.google.android.systemui.elmyra.proto.nano.ElmyraAtoms$ElmyraSnapshot
                        public ChassisProtos$Chassis chassis;
                        public SnapshotProtos$Snapshot snapshot;

                        {
                            clear();
                        }

                        public ElmyraAtoms$ElmyraSnapshot clear() {
                            this.snapshot = null;
                            this.chassis = null;
                            this.cachedSize = -1;
                            return this;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                            SnapshotProtos$Snapshot snapshotProtos$Snapshot = this.snapshot;
                            if (snapshotProtos$Snapshot != null) {
                                codedOutputByteBufferNano.writeMessage(1, snapshotProtos$Snapshot);
                            }
                            ChassisProtos$Chassis chassisProtos$Chassis = this.chassis;
                            if (chassisProtos$Chassis != null) {
                                codedOutputByteBufferNano.writeMessage(2, chassisProtos$Chassis);
                            }
                            super.writeTo(codedOutputByteBufferNano);
                        }

                        /* access modifiers changed from: protected */
                        @Override // com.google.protobuf.nano.MessageNano
                        public int computeSerializedSize() {
                            int computeSerializedSize = super.computeSerializedSize();
                            SnapshotProtos$Snapshot snapshotProtos$Snapshot = this.snapshot;
                            if (snapshotProtos$Snapshot != null) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, snapshotProtos$Snapshot);
                            }
                            ChassisProtos$Chassis chassisProtos$Chassis = this.chassis;
                            return chassisProtos$Chassis != null ? computeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(2, chassisProtos$Chassis) : computeSerializedSize;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public ElmyraAtoms$ElmyraSnapshot mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                            while (true) {
                                int readTag = codedInputByteBufferNano.readTag();
                                if (readTag == 0) {
                                    return this;
                                }
                                if (readTag == 10) {
                                    if (this.snapshot == null) {
                                        this.snapshot = new SnapshotProtos$Snapshot();
                                    }
                                    codedInputByteBufferNano.readMessage(this.snapshot);
                                } else if (readTag == 18) {
                                    if (this.chassis == null) {
                                        this.chassis = new ChassisProtos$Chassis();
                                    }
                                    codedInputByteBufferNano.readMessage(this.chassis);
                                } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                                    return this;
                                }
                            }
                        }
                    };
                    float sensitivity = this.mGestureConfiguration.getSensitivity();
                    SnapshotProtos$Snapshot snapshotProtos$Snapshot = this.mSnapshot;
                    snapshotProtos$Snapshot.sensitivitySetting = sensitivity;
                    elmyraAtoms$ElmyraSnapshot.snapshot = snapshotProtos$Snapshot;
                    elmyraAtoms$ElmyraSnapshot.chassis = this.mChassis;
                    list.add(StatsEvent.newBuilder().setAtomId(i).writeByteArray(MessageNano.toByteArray(elmyraAtoms$ElmyraSnapshot.snapshot)).writeByteArray(MessageNano.toByteArray(elmyraAtoms$ElmyraSnapshot.chassis)).build());
                    this.mSnapshot = null;
                    synchronized (this.mMutex) {
                        this.mCountDownLatch = null;
                        this.mSnapshot = null;
                    }
                    return 0;
                }
                this.mCountDownLatch = null;
                return 1;
            }
        }
    }

    public void didReceiveSnapshot(SnapshotProtos$Snapshot snapshotProtos$Snapshot) {
        synchronized (this.mMutex) {
            this.mSnapshot = snapshotProtos$Snapshot;
            CountDownLatch countDownLatch = this.mCountDownLatch;
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
        }
    }
}
