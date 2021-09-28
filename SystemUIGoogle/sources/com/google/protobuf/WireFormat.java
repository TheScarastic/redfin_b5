package com.google.protobuf;
/* loaded from: classes2.dex */
public final class WireFormat {
    static final int MESSAGE_SET_ITEM_TAG = makeTag(1, 3);
    static final int MESSAGE_SET_ITEM_END_TAG = makeTag(1, 4);
    static final int MESSAGE_SET_TYPE_ID_TAG = makeTag(2, 0);
    static final int MESSAGE_SET_MESSAGE_TAG = makeTag(3, 2);

    public static int getTagFieldNumber(int i) {
        return i >>> 3;
    }

    public static int getTagWireType(int i) {
        return i & 7;
    }

    /* access modifiers changed from: package-private */
    public static int makeTag(int i, int i2) {
        return (i << 3) | i2;
    }

    /* loaded from: classes2.dex */
    public enum JavaType {
        INT(0),
        LONG(0L),
        FLOAT(Float.valueOf(0.0f)),
        DOUBLE(Double.valueOf(0.0d)),
        BOOLEAN(Boolean.FALSE),
        STRING(""),
        BYTE_STRING(ByteString.EMPTY),
        ENUM(null),
        MESSAGE(null);
        
        private final Object defaultDefault;

        JavaType(Object obj) {
            this.defaultDefault = obj;
        }
    }

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
    /* loaded from: classes2.dex */
    public enum FieldType {
        DOUBLE(JavaType.DOUBLE, 1),
        FLOAT(JavaType.FLOAT, 5),
        INT64(r5, 0),
        UINT64(r5, 0),
        INT32(r11, 0),
        FIXED64(r5, 1),
        FIXED32(r11, 5),
        BOOL(JavaType.BOOLEAN, 0),
        STRING(JavaType.STRING, 2) {
        },
        GROUP(r13, 3) {
        },
        MESSAGE(r13, 2) {
        },
        BYTES(JavaType.BYTE_STRING, 2) {
        },
        UINT32(r11, 0),
        ENUM(JavaType.ENUM, 0),
        SFIXED32(r11, 5),
        SFIXED64(r5, 1),
        SINT32(r11, 0),
        SINT64(r5, 0);
        
        private final JavaType javaType;
        private final int wireType;

        static {
            JavaType javaType = JavaType.LONG;
            JavaType javaType2 = JavaType.INT;
            JavaType javaType3 = JavaType.MESSAGE;
        }

        FieldType(JavaType javaType, int i) {
            this.javaType = javaType;
            this.wireType = i;
        }

        public JavaType getJavaType() {
            return this.javaType;
        }
    }
}
