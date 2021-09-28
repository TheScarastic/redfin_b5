package com.google.android.systemui.autorotate;

import android.app.StatsManager;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.util.StatsEvent;
import com.android.internal.util.ConcurrentUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.google.android.systemui.autorotate.proto.nano.AutorotateProto$DeviceRotatedSensorData;
import com.google.android.systemui.autorotate.proto.nano.AutorotateProto$DeviceRotatedSensorHeader;
import com.google.android.systemui.autorotate.proto.nano.AutorotateProto$DeviceRotatedSensorSample;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class DataLogger {
    private Queue<Pair<SensorData[], Integer>> mDataQueue = new LinkedList();
    private long mLastPullTimeNanos = 0;
    private StatsManager mStatsManager;

    private static int convertSensorId(int i) {
        if (i == 4) {
            return 2;
        }
        return i;
    }

    private static int rotationToLogEnum(int i) {
        if (i == 0) {
            return 1;
        }
        if (i == 1) {
            return 2;
        }
        if (i != 2) {
            return i != 3 ? 0 : 4;
        }
        return 3;
    }

    /* access modifiers changed from: package-private */
    public DataLogger(StatsManager statsManager) {
        this.mStatsManager = statsManager;
    }

    /* access modifiers changed from: package-private */
    public void pushDeviceRotatedEvent(long j, int i, int i2) {
        FrameworkStatsLog.write(333, j, rotationToLogEnum(i), i2);
    }

    /* access modifiers changed from: package-private */
    public void setDeviceRotatedData(SensorData[] sensorDataArr, int i) {
        if (sensorDataArr != null && sensorDataArr.length != 0 && sensorDataArr[0] != null) {
            if (SystemClock.elapsedRealtimeNanos() - this.mLastPullTimeNanos > 5000000000L) {
                clearData();
            }
            this.mDataQueue.add(Pair.create(sensorDataArr, Integer.valueOf(i)));
        }
    }

    /* access modifiers changed from: package-private */
    public void clearData() {
        this.mDataQueue.clear();
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class StatsPullAtomCallbackImpl implements StatsManager.StatsPullAtomCallback {
        StatsPullAtomCallbackImpl() {
        }

        public int onPullAtom(int i, List<StatsEvent> list) {
            if (i == 10097) {
                return DataLogger.this.pullSensorData(list);
            }
            throw new UnsupportedOperationException("Unknown tagId: " + i);
        }
    }

    /* access modifiers changed from: package-private */
    public void registerPullAtomCallback() {
        if (this.mStatsManager != null) {
            this.mStatsManager.setPullAtomCallback(10097, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, new StatsPullAtomCallbackImpl());
            Log.d("Autorotate-DataLogger", "Registered to statsd for pull");
        }
    }

    /* access modifiers changed from: package-private */
    public void unregisterPullAtomCallback() {
        StatsManager statsManager = this.mStatsManager;
        if (statsManager != null) {
            statsManager.clearPullAtomCallback(10097);
        }
    }

    /* access modifiers changed from: private */
    public int pullSensorData(List<StatsEvent> list) {
        Log.d("Autorotate-DataLogger", "Received pull request from statsd.");
        this.mLastPullTimeNanos = SystemClock.elapsedRealtimeNanos();
        Pair<SensorData[], Integer> poll = this.mDataQueue.poll();
        SensorData[] sensorDataArr = (SensorData[]) poll.first;
        int intValue = ((Integer) poll.second).intValue();
        if (!(sensorDataArr == null || sensorDataArr.length == 0 || sensorDataArr[0] == null)) {
            AutorotateProto$DeviceRotatedSensorData autorotateProto$DeviceRotatedSensorData = new MessageNano() { // from class: com.google.android.systemui.autorotate.proto.nano.AutorotateProto$DeviceRotatedSensorData
                public AutorotateProto$DeviceRotatedSensorHeader header;
                public AutorotateProto$DeviceRotatedSensorSample[] sample;

                {
                    clear();
                }

                public AutorotateProto$DeviceRotatedSensorData clear() {
                    this.header = null;
                    this.sample = AutorotateProto$DeviceRotatedSensorSample.emptyArray();
                    this.cachedSize = -1;
                    return this;
                }

                @Override // com.google.protobuf.nano.MessageNano
                public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                    AutorotateProto$DeviceRotatedSensorHeader autorotateProto$DeviceRotatedSensorHeader = this.header;
                    if (autorotateProto$DeviceRotatedSensorHeader != null) {
                        codedOutputByteBufferNano.writeMessage(1, autorotateProto$DeviceRotatedSensorHeader);
                    }
                    AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr = this.sample;
                    if (autorotateProto$DeviceRotatedSensorSampleArr != null && autorotateProto$DeviceRotatedSensorSampleArr.length > 0) {
                        int i = 0;
                        while (true) {
                            AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr2 = this.sample;
                            if (i >= autorotateProto$DeviceRotatedSensorSampleArr2.length) {
                                break;
                            }
                            AutorotateProto$DeviceRotatedSensorSample autorotateProto$DeviceRotatedSensorSample = autorotateProto$DeviceRotatedSensorSampleArr2[i];
                            if (autorotateProto$DeviceRotatedSensorSample != null) {
                                codedOutputByteBufferNano.writeMessage(2, autorotateProto$DeviceRotatedSensorSample);
                            }
                            i++;
                        }
                    }
                    super.writeTo(codedOutputByteBufferNano);
                }

                /* access modifiers changed from: protected */
                @Override // com.google.protobuf.nano.MessageNano
                public int computeSerializedSize() {
                    int computeSerializedSize = super.computeSerializedSize();
                    AutorotateProto$DeviceRotatedSensorHeader autorotateProto$DeviceRotatedSensorHeader = this.header;
                    if (autorotateProto$DeviceRotatedSensorHeader != null) {
                        computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, autorotateProto$DeviceRotatedSensorHeader);
                    }
                    AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr = this.sample;
                    if (autorotateProto$DeviceRotatedSensorSampleArr != null && autorotateProto$DeviceRotatedSensorSampleArr.length > 0) {
                        int i = 0;
                        while (true) {
                            AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr2 = this.sample;
                            if (i >= autorotateProto$DeviceRotatedSensorSampleArr2.length) {
                                break;
                            }
                            AutorotateProto$DeviceRotatedSensorSample autorotateProto$DeviceRotatedSensorSample = autorotateProto$DeviceRotatedSensorSampleArr2[i];
                            if (autorotateProto$DeviceRotatedSensorSample != null) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(2, autorotateProto$DeviceRotatedSensorSample);
                            }
                            i++;
                        }
                    }
                    return computeSerializedSize;
                }

                @Override // com.google.protobuf.nano.MessageNano
                public AutorotateProto$DeviceRotatedSensorData mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                    while (true) {
                        int readTag = codedInputByteBufferNano.readTag();
                        if (readTag == 0) {
                            return this;
                        }
                        if (readTag == 10) {
                            if (this.header == null) {
                                this.header = new AutorotateProto$DeviceRotatedSensorHeader();
                            }
                            codedInputByteBufferNano.readMessage(this.header);
                        } else if (readTag == 18) {
                            int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                            AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr = this.sample;
                            int length = autorotateProto$DeviceRotatedSensorSampleArr == null ? 0 : autorotateProto$DeviceRotatedSensorSampleArr.length;
                            int i = repeatedFieldArrayLength + length;
                            AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr2 = new AutorotateProto$DeviceRotatedSensorSample[i];
                            if (length != 0) {
                                System.arraycopy(autorotateProto$DeviceRotatedSensorSampleArr, 0, autorotateProto$DeviceRotatedSensorSampleArr2, 0, length);
                            }
                            while (length < i - 1) {
                                autorotateProto$DeviceRotatedSensorSampleArr2[length] = new AutorotateProto$DeviceRotatedSensorSample();
                                codedInputByteBufferNano.readMessage(autorotateProto$DeviceRotatedSensorSampleArr2[length]);
                                codedInputByteBufferNano.readTag();
                                length++;
                            }
                            autorotateProto$DeviceRotatedSensorSampleArr2[length] = new AutorotateProto$DeviceRotatedSensorSample();
                            codedInputByteBufferNano.readMessage(autorotateProto$DeviceRotatedSensorSampleArr2[length]);
                            this.sample = autorotateProto$DeviceRotatedSensorSampleArr2;
                        } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                            return this;
                        }
                    }
                }
            };
            AutorotateProto$DeviceRotatedSensorHeader autorotateProto$DeviceRotatedSensorHeader = new AutorotateProto$DeviceRotatedSensorHeader();
            autorotateProto$DeviceRotatedSensorHeader.timestampBase = sensorDataArr[0].mTimestampMillis;
            autorotateProto$DeviceRotatedSensorData.header = autorotateProto$DeviceRotatedSensorHeader;
            AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr = new AutorotateProto$DeviceRotatedSensorSample[sensorDataArr.length];
            for (int i = 0; i < sensorDataArr.length; i++) {
                AutorotateProto$DeviceRotatedSensorSample autorotateProto$DeviceRotatedSensorSample = new AutorotateProto$DeviceRotatedSensorSample();
                autorotateProto$DeviceRotatedSensorSample.timestampOffset = (int) (sensorDataArr[i].mTimestampMillis - autorotateProto$DeviceRotatedSensorHeader.timestampBase);
                autorotateProto$DeviceRotatedSensorSample.sensorType = convertSensorId(sensorDataArr[i].mSensorIdentifier);
                autorotateProto$DeviceRotatedSensorSample.xValue = sensorDataArr[i].mValueX;
                autorotateProto$DeviceRotatedSensorSample.yValue = sensorDataArr[i].mValueY;
                autorotateProto$DeviceRotatedSensorSample.zValue = sensorDataArr[i].mValueZ;
                autorotateProto$DeviceRotatedSensorSampleArr[i] = autorotateProto$DeviceRotatedSensorSample;
            }
            autorotateProto$DeviceRotatedSensorData.sample = autorotateProto$DeviceRotatedSensorSampleArr;
            list.add(FrameworkStatsLog.buildStatsEvent(10097, MessageNano.toByteArray(autorotateProto$DeviceRotatedSensorData), rotationToLogEnum(intValue)));
        }
        return 0;
    }
}
