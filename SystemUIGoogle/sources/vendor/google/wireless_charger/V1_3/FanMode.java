package vendor.google.wireless_charger.V1_3;
/* loaded from: classes2.dex */
public final class FanMode {
    public static final String toString(byte b) {
        if (b == 0) {
            return "BUILT_IN";
        }
        if (b == 1) {
            return "FIXED";
        }
        return "0x" + Integer.toHexString(Byte.toUnsignedInt(b));
    }
}
