package com.google.protobuf;
/* JADX WARN: Init of enum INT64 can be incorrect */
/* JADX WARN: Init of enum UINT64 can be incorrect */
/* JADX WARN: Init of enum INT32 can be incorrect */
/* JADX WARN: Init of enum FIXED64 can be incorrect */
/* JADX WARN: Init of enum FIXED32 can be incorrect */
/* JADX WARN: Init of enum GROUP can be incorrect */
/* JADX WARN: Init of enum MESSAGE can be incorrect */
/* JADX WARN: Init of enum UINT32 can be incorrect */
/* JADX WARN: Init of enum SFIXED32 can be incorrect */
/* JADX WARN: Init of enum SFIXED64 can be incorrect */
/* JADX WARN: Init of enum SINT32 can be incorrect */
/* JADX WARN: Init of enum SINT64 can be incorrect */
/* loaded from: classes.dex */
public enum WireFormat$FieldType {
    DOUBLE(WireFormat$JavaType.DOUBLE, 1),
    FLOAT(WireFormat$JavaType.FLOAT, 5),
    INT64(r5, 0),
    UINT64(r5, 0),
    INT32(r11, 0),
    FIXED64(r5, 1),
    FIXED32(r11, 5),
    BOOL(WireFormat$JavaType.BOOLEAN, 0),
    STRING(WireFormat$JavaType.STRING, 2) {
    },
    GROUP(r13, 3) {
    },
    MESSAGE(r13, 2) {
    },
    BYTES(WireFormat$JavaType.BYTE_STRING, 2) {
    },
    UINT32(r11, 0),
    ENUM(WireFormat$JavaType.ENUM, 0),
    SFIXED32(r11, 5),
    SFIXED64(r5, 1),
    SINT32(r11, 0),
    SINT64(r5, 0);
    
    private final WireFormat$JavaType javaType;
    private final int wireType;

    static {
        WireFormat$JavaType wireFormat$JavaType = WireFormat$JavaType.LONG;
        WireFormat$JavaType wireFormat$JavaType2 = WireFormat$JavaType.INT;
        WireFormat$JavaType wireFormat$JavaType3 = WireFormat$JavaType.MESSAGE;
    }

    WireFormat$FieldType(WireFormat$JavaType wireFormat$JavaType, int i) {
        this.javaType = wireFormat$JavaType;
        this.wireType = i;
    }

    public WireFormat$JavaType getJavaType() {
        return this.javaType;
    }

    WireFormat$FieldType(WireFormat$JavaType wireFormat$JavaType, int i, WireFormat$1 wireFormat$1) {
        this.javaType = wireFormat$JavaType;
        this.wireType = i;
    }
}
