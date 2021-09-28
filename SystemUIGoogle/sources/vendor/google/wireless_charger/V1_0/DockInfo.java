package vendor.google.wireless_charger.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class DockInfo {
    public String manufacturer = new String();
    public String model = new String();
    public String serial = new String();
    public int maxFwSize = 0;
    public boolean isGetInfoSupported = false;
    public FirmwareVersion version = new FirmwareVersion();
    public byte orientation = 0;
    public byte type = 0;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != DockInfo.class) {
            return false;
        }
        DockInfo dockInfo = (DockInfo) obj;
        return HidlSupport.deepEquals(this.manufacturer, dockInfo.manufacturer) && HidlSupport.deepEquals(this.model, dockInfo.model) && HidlSupport.deepEquals(this.serial, dockInfo.serial) && this.maxFwSize == dockInfo.maxFwSize && this.isGetInfoSupported == dockInfo.isGetInfoSupported && HidlSupport.deepEquals(this.version, dockInfo.version) && this.orientation == dockInfo.orientation && this.type == dockInfo.type;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.manufacturer)), Integer.valueOf(HidlSupport.deepHashCode(this.model)), Integer.valueOf(HidlSupport.deepHashCode(this.serial)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.maxFwSize))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isGetInfoSupported))), Integer.valueOf(HidlSupport.deepHashCode(this.version)), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.orientation))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.type))));
    }

    public final String toString() {
        return "{.manufacturer = " + this.manufacturer + ", .model = " + this.model + ", .serial = " + this.serial + ", .maxFwSize = " + this.maxFwSize + ", .isGetInfoSupported = " + this.isGetInfoSupported + ", .version = " + this.version + ", .orientation = " + Orientation.toString(this.orientation) + ", .type = " + DockType.toString(this.type) + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(88), 0);
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        long j2 = j + 0;
        String string = hwBlob.getString(j2);
        this.manufacturer = string;
        hwParcel.readEmbeddedBuffer((long) (string.getBytes().length + 1), hwBlob.handle(), j2 + 0, false);
        long j3 = j + 16;
        String string2 = hwBlob.getString(j3);
        this.model = string2;
        hwParcel.readEmbeddedBuffer((long) (string2.getBytes().length + 1), hwBlob.handle(), j3 + 0, false);
        long j4 = j + 32;
        String string3 = hwBlob.getString(j4);
        this.serial = string3;
        hwParcel.readEmbeddedBuffer((long) (string3.getBytes().length + 1), hwBlob.handle(), j4 + 0, false);
        this.maxFwSize = hwBlob.getInt32(j + 48);
        this.isGetInfoSupported = hwBlob.getBool(j + 52);
        this.version.readEmbeddedFromParcel(hwParcel, hwBlob, j + 56);
        this.orientation = hwBlob.getInt8(j + 80);
        this.type = hwBlob.getInt8(j + 81);
    }
}
