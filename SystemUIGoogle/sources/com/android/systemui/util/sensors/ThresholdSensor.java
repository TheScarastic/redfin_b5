package com.android.systemui.util.sensors;
/* loaded from: classes2.dex */
public interface ThresholdSensor {

    /* loaded from: classes2.dex */
    public interface Listener {
        void onThresholdCrossed(ThresholdSensorEvent thresholdSensorEvent);
    }

    boolean isLoaded();

    void pause();

    void register(Listener listener);

    void resume();

    void setDelay(int i);

    void setTag(String str);

    /* loaded from: classes2.dex */
    public static class ThresholdSensorEvent {
        private final boolean mBelow;
        private final long mTimestampNs;

        public ThresholdSensorEvent(boolean z, long j) {
            this.mBelow = z;
            this.mTimestampNs = j;
        }

        public boolean getBelow() {
            return this.mBelow;
        }

        public long getTimestampNs() {
            return this.mTimestampNs;
        }

        public String toString() {
            return String.format(null, "{near=%s, timestamp_ns=%d}", Boolean.valueOf(this.mBelow), Long.valueOf(this.mTimestampNs));
        }
    }
}
