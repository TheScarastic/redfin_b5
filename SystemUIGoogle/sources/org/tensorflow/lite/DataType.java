package org.tensorflow.lite;
/* loaded from: classes2.dex */
public enum DataType {
    FLOAT32(1),
    INT32(2),
    UINT8(3),
    INT64(4),
    STRING(5),
    BOOL(6),
    INT8(9);
    
    private static final DataType[] values = values();
    private final int value;

    DataType(int i) {
        this.value = i;
    }

    /* renamed from: org.tensorflow.lite.DataType$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$tensorflow$lite$DataType;

        static {
            int[] iArr = new int[DataType.values().length];
            $SwitchMap$org$tensorflow$lite$DataType = iArr;
            try {
                iArr[DataType.FLOAT32.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.INT32.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.INT8.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.UINT8.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.INT64.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.BOOL.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.STRING.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public int byteSize() {
        switch (AnonymousClass1.$SwitchMap$org$tensorflow$lite$DataType[ordinal()]) {
            case 1:
            case 2:
                return 4;
            case 3:
            case 4:
                return 1;
            case 5:
                return 8;
            case 6:
            case 7:
                return -1;
            default:
                throw new IllegalArgumentException("DataType error: DataType " + this + " is not supported yet");
        }
    }

    /* access modifiers changed from: package-private */
    public static DataType fromC(int i) {
        DataType[] dataTypeArr = values;
        for (DataType dataType : dataTypeArr) {
            if (dataType.value == i) {
                return dataType;
            }
        }
        throw new IllegalArgumentException("DataType error: DataType " + i + " is not recognized in Java (version " + TensorFlowLite.runtimeVersion() + ")");
    }

    /* access modifiers changed from: package-private */
    public String toStringName() {
        switch (AnonymousClass1.$SwitchMap$org$tensorflow$lite$DataType[ordinal()]) {
            case 1:
                return "float";
            case 2:
                return "int";
            case 3:
            case 4:
                return "byte";
            case 5:
                return "long";
            case 6:
                return "bool";
            case 7:
                return "string";
            default:
                throw new IllegalArgumentException("DataType error: DataType " + this + " is not supported yet");
        }
    }
}
