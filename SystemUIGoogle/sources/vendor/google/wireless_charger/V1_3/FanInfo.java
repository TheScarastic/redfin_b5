package vendor.google.wireless_charger.V1_3;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class FanInfo {
    public byte fanMode = 0;
    public short currentRpm = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != FanInfo.class) {
            return false;
        }
        FanInfo fanInfo = (FanInfo) obj;
        return this.fanMode == fanInfo.fanMode && this.currentRpm == fanInfo.currentRpm;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.fanMode))), Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(this.currentRpm))));
    }

    public final String toString() {
        return "{.fanMode = " + FanMode.toString(this.fanMode) + ", .currentRpm = " + ((int) this.currentRpm) + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(4), 0);
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.fanMode = hwBlob.getInt8(0 + j);
        this.currentRpm = hwBlob.getInt16(j + 2);
    }
}
