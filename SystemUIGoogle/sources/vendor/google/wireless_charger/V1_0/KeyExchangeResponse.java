package vendor.google.wireless_charger.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class KeyExchangeResponse {
    public byte dockId = 0;
    public ArrayList<Byte> dockPublicKey = new ArrayList<>();

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != KeyExchangeResponse.class) {
            return false;
        }
        KeyExchangeResponse keyExchangeResponse = (KeyExchangeResponse) obj;
        return this.dockId == keyExchangeResponse.dockId && HidlSupport.deepEquals(this.dockPublicKey, keyExchangeResponse.dockPublicKey);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.dockId))), Integer.valueOf(HidlSupport.deepHashCode(this.dockPublicKey)));
    }

    public final String toString() {
        return "{.dockId = " + ((int) this.dockId) + ", .dockPublicKey = " + this.dockPublicKey + "}";
    }

    public final void readFromParcel(HwParcel hwParcel) {
        readEmbeddedFromParcel(hwParcel, hwParcel.readBuffer(24), 0);
    }

    public final void readEmbeddedFromParcel(HwParcel hwParcel, HwBlob hwBlob, long j) {
        this.dockId = hwBlob.getInt8(j + 0);
        long j2 = j + 8;
        int int32 = hwBlob.getInt32(8 + j2);
        HwBlob readEmbeddedBuffer = hwParcel.readEmbeddedBuffer((long) (int32 * 1), hwBlob.handle(), j2 + 0, true);
        this.dockPublicKey.clear();
        for (int i = 0; i < int32; i++) {
            this.dockPublicKey.add(Byte.valueOf(readEmbeddedBuffer.getInt8((long) (i * 1))));
        }
    }
}
