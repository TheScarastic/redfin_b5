package com.android.systemui.power;
/* compiled from: BatteryStateSnapshot.kt */
/* loaded from: classes.dex */
public final class BatteryStateSnapshot {
    private final long averageTimeToDischargeMillis;
    private final int batteryLevel;
    private final int batteryStatus;
    private final int bucket;
    private final boolean isBasedOnUsage;
    private boolean isHybrid = false;
    private final boolean isLowWarningEnabled;
    private final boolean isPowerSaver;
    private final int lowLevelThreshold;
    private final long lowThresholdMillis;
    private final boolean plugged;
    private final int severeLevelThreshold;
    private final long severeThresholdMillis;
    private final long timeRemainingMillis;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BatteryStateSnapshot)) {
            return false;
        }
        BatteryStateSnapshot batteryStateSnapshot = (BatteryStateSnapshot) obj;
        return this.batteryLevel == batteryStateSnapshot.batteryLevel && this.isPowerSaver == batteryStateSnapshot.isPowerSaver && this.plugged == batteryStateSnapshot.plugged && this.bucket == batteryStateSnapshot.bucket && this.batteryStatus == batteryStateSnapshot.batteryStatus && this.severeLevelThreshold == batteryStateSnapshot.severeLevelThreshold && this.lowLevelThreshold == batteryStateSnapshot.lowLevelThreshold && this.timeRemainingMillis == batteryStateSnapshot.timeRemainingMillis && this.averageTimeToDischargeMillis == batteryStateSnapshot.averageTimeToDischargeMillis && this.severeThresholdMillis == batteryStateSnapshot.severeThresholdMillis && this.lowThresholdMillis == batteryStateSnapshot.lowThresholdMillis && this.isBasedOnUsage == batteryStateSnapshot.isBasedOnUsage && this.isLowWarningEnabled == batteryStateSnapshot.isLowWarningEnabled;
    }

    public int hashCode() {
        int hashCode = Integer.hashCode(this.batteryLevel) * 31;
        boolean z = this.isPowerSaver;
        int i = 1;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = z ? 1 : 0;
        int i5 = (hashCode + i2) * 31;
        boolean z2 = this.plugged;
        if (z2) {
            z2 = true;
        }
        int i6 = z2 ? 1 : 0;
        int i7 = z2 ? 1 : 0;
        int i8 = z2 ? 1 : 0;
        int hashCode2 = (((((((((((((((((i5 + i6) * 31) + Integer.hashCode(this.bucket)) * 31) + Integer.hashCode(this.batteryStatus)) * 31) + Integer.hashCode(this.severeLevelThreshold)) * 31) + Integer.hashCode(this.lowLevelThreshold)) * 31) + Long.hashCode(this.timeRemainingMillis)) * 31) + Long.hashCode(this.averageTimeToDischargeMillis)) * 31) + Long.hashCode(this.severeThresholdMillis)) * 31) + Long.hashCode(this.lowThresholdMillis)) * 31;
        boolean z3 = this.isBasedOnUsage;
        if (z3) {
            z3 = true;
        }
        int i9 = z3 ? 1 : 0;
        int i10 = z3 ? 1 : 0;
        int i11 = z3 ? 1 : 0;
        int i12 = (hashCode2 + i9) * 31;
        boolean z4 = this.isLowWarningEnabled;
        if (!z4) {
            i = z4 ? 1 : 0;
        }
        return i12 + i;
    }

    public String toString() {
        return "BatteryStateSnapshot(batteryLevel=" + this.batteryLevel + ", isPowerSaver=" + this.isPowerSaver + ", plugged=" + this.plugged + ", bucket=" + this.bucket + ", batteryStatus=" + this.batteryStatus + ", severeLevelThreshold=" + this.severeLevelThreshold + ", lowLevelThreshold=" + this.lowLevelThreshold + ", timeRemainingMillis=" + this.timeRemainingMillis + ", averageTimeToDischargeMillis=" + this.averageTimeToDischargeMillis + ", severeThresholdMillis=" + this.severeThresholdMillis + ", lowThresholdMillis=" + this.lowThresholdMillis + ", isBasedOnUsage=" + this.isBasedOnUsage + ", isLowWarningEnabled=" + this.isLowWarningEnabled + ')';
    }

    public BatteryStateSnapshot(int i, boolean z, boolean z2, int i2, int i3, int i4, int i5, long j, long j2, long j3, long j4, boolean z3, boolean z4) {
        this.batteryLevel = i;
        this.isPowerSaver = z;
        this.plugged = z2;
        this.bucket = i2;
        this.batteryStatus = i3;
        this.severeLevelThreshold = i4;
        this.lowLevelThreshold = i5;
        this.timeRemainingMillis = j;
        this.averageTimeToDischargeMillis = j2;
        this.severeThresholdMillis = j3;
        this.lowThresholdMillis = j4;
        this.isBasedOnUsage = z3;
        this.isLowWarningEnabled = z4;
    }

    public final int getBatteryLevel() {
        return this.batteryLevel;
    }

    public final boolean isPowerSaver() {
        return this.isPowerSaver;
    }

    public final boolean getPlugged() {
        return this.plugged;
    }

    public final int getBucket() {
        return this.bucket;
    }

    public final int getBatteryStatus() {
        return this.batteryStatus;
    }

    public final int getSevereLevelThreshold() {
        return this.severeLevelThreshold;
    }

    public final int getLowLevelThreshold() {
        return this.lowLevelThreshold;
    }

    public final long getTimeRemainingMillis() {
        return this.timeRemainingMillis;
    }

    public final long getAverageTimeToDischargeMillis() {
        return this.averageTimeToDischargeMillis;
    }

    public final long getSevereThresholdMillis() {
        return this.severeThresholdMillis;
    }

    public final long getLowThresholdMillis() {
        return this.lowThresholdMillis;
    }

    public final boolean isBasedOnUsage() {
        return this.isBasedOnUsage;
    }

    public final boolean isLowWarningEnabled() {
        return this.isLowWarningEnabled;
    }

    public final boolean isHybrid() {
        return this.isHybrid;
    }

    public BatteryStateSnapshot(int i, boolean z, boolean z2, int i2, int i3, int i4, int i5) {
        this(i, z, z2, i2, i3, i4, i5, -1, -1, -1, -1, false, true);
    }
}
