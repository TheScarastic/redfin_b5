package vendor.google.wireless_charger.V1_1;
/* loaded from: classes2.dex */
public final class AlignState {
    public static final String toString(byte b) {
        if (b == 0) {
            return "CHECKING";
        }
        if (b == 1) {
            return "MOVE2CENTER";
        }
        if (b == 2) {
            return "OK";
        }
        if (b == 3) {
            return "ERROR";
        }
        return "0x" + Integer.toHexString(Byte.toUnsignedInt(b));
    }
}
