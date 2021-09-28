package android.hardware.google.pixel.vendor;

import com.google.protobuf.Internal;
/* loaded from: classes.dex */
public enum PixelAtoms$DoubleTapNanoappEventReported$Type implements Internal.EnumLite {
    UNKNOWN(0),
    GATE_START(1),
    GATE_STOP(2),
    HIGH_IMU_ODR_START(3),
    HIGH_IMU_ODR_STOP(4),
    ML_PREDICTION_START(5),
    ML_PREDICTION_STOP(6),
    SINGLE_TAP(7),
    DOUBLE_TAP(8);
    
    private static final Internal.EnumLiteMap<PixelAtoms$DoubleTapNanoappEventReported$Type> internalValueMap = new Internal.EnumLiteMap<PixelAtoms$DoubleTapNanoappEventReported$Type>() { // from class: android.hardware.google.pixel.vendor.PixelAtoms$DoubleTapNanoappEventReported$Type.1
        @Override // com.google.protobuf.Internal.EnumLiteMap
        public PixelAtoms$DoubleTapNanoappEventReported$Type findValueByNumber(int i) {
            return PixelAtoms$DoubleTapNanoappEventReported$Type.forNumber(i);
        }
    };
    private final int value;

    PixelAtoms$DoubleTapNanoappEventReported$Type(int i) {
        this.value = i;
    }

    public static PixelAtoms$DoubleTapNanoappEventReported$Type forNumber(int i) {
        switch (i) {
            case 0:
                return UNKNOWN;
            case 1:
                return GATE_START;
            case 2:
                return GATE_STOP;
            case 3:
                return HIGH_IMU_ODR_START;
            case 4:
                return HIGH_IMU_ODR_STOP;
            case 5:
                return ML_PREDICTION_START;
            case 6:
                return ML_PREDICTION_STOP;
            case 7:
                return SINGLE_TAP;
            case 8:
                return DOUBLE_TAP;
            default:
                return null;
        }
    }

    public final int getNumber() {
        return this.value;
    }
}
