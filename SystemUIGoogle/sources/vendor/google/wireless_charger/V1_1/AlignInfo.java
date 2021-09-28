package vendor.google.wireless_charger.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class AlignInfo {
    public byte alignState = 0;
    public byte alignPct = 0;
    public int alignX = 0;
    public int alignY = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != AlignInfo.class) {
            return false;
        }
        AlignInfo alignInfo = (AlignInfo) obj;
        return this.alignState == alignInfo.alignState && this.alignPct == alignInfo.alignPct && this.alignX == alignInfo.alignX && this.alignY == alignInfo.alignY;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.alignState))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.alignPct))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.alignX))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.alignY))));
    }

    public final String toString() {
        return "{.alignState = " + AlignState.toString(this.alignState) + ", .alignPct = " + ((int) this.alignPct) + ", .alignX = " + this.alignX + ", .alignY = " + this.alignY + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(12), 0);
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.alignState = hwBlob.getInt8(0 + j);
        this.alignPct = hwBlob.getInt8(1 + j);
        this.alignX = hwBlob.getInt32(4 + j);
        this.alignY = hwBlob.getInt32(j + 8);
    }
}
