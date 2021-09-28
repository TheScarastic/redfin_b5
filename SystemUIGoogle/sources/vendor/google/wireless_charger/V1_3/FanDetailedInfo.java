package vendor.google.wireless_charger.V1_3;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class FanDetailedInfo {
    public byte fanMode = 0;
    public short currentRpm = 0;
    public short minimumRpm = 0;
    public short maximumRpm = 0;
    public byte type = 0;
    public byte count = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != FanDetailedInfo.class) {
            return false;
        }
        FanDetailedInfo fanDetailedInfo = (FanDetailedInfo) obj;
        return this.fanMode == fanDetailedInfo.fanMode && this.currentRpm == fanDetailedInfo.currentRpm && this.minimumRpm == fanDetailedInfo.minimumRpm && this.maximumRpm == fanDetailedInfo.maximumRpm && this.type == fanDetailedInfo.type && this.count == fanDetailedInfo.count;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.fanMode))), Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(this.currentRpm))), Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(this.minimumRpm))), Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(this.maximumRpm))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.type))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.count))));
    }

    public final String toString() {
        return "{.fanMode = " + FanMode.toString(this.fanMode) + ", .currentRpm = " + ((int) this.currentRpm) + ", .minimumRpm = " + ((int) this.minimumRpm) + ", .maximumRpm = " + ((int) this.maximumRpm) + ", .type = " + ((int) this.type) + ", .count = " + ((int) this.count) + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(10), 0);
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.fanMode = hwBlob.getInt8(0 + j);
        this.currentRpm = hwBlob.getInt16(2 + j);
        this.minimumRpm = hwBlob.getInt16(4 + j);
        this.maximumRpm = hwBlob.getInt16(6 + j);
        this.type = hwBlob.getInt8(8 + j);
        this.count = hwBlob.getInt8(j + 9);
    }
}
