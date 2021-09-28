package com.google.protobuf;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.util.logging.Logger;
import sun.misc.Unsafe;
/* loaded from: classes.dex */
public final class UnsafeUtil {
    public static final long BYTE_ARRAY_BASE_OFFSET;
    public static final boolean HAS_UNSAFE_ARRAY_OPERATIONS;
    public static final boolean HAS_UNSAFE_BYTEBUFFER_OPERATIONS;
    public static final MemoryAccessor MEMORY_ACCESSOR;
    public static final Unsafe UNSAFE;
    public static final Logger logger = Logger.getLogger(UnsafeUtil.class.getName());

    /* loaded from: classes.dex */
    public static final class JvmMemoryAccessor extends MemoryAccessor {
        public JvmMemoryAccessor(Unsafe unsafe) {
            super(unsafe);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class MemoryAccessor {
        public Unsafe unsafe;

        public MemoryAccessor(Unsafe unsafe) {
            this.unsafe = unsafe;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x0237  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0107 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    static {
        /*
        // Method dump skipped, instructions count: 583
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.UnsafeUtil.<clinit>():void");
    }

    public static <T> T allocateInstance(Class<T> cls) {
        try {
            return (T) UNSAFE.allocateInstance(cls);
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static int arrayBaseOffset(Class<?> cls) {
        if (HAS_UNSAFE_ARRAY_OPERATIONS) {
            return MEMORY_ACCESSOR.unsafe.arrayBaseOffset(cls);
        }
        return -1;
    }

    public static int arrayIndexScale(Class<?> cls) {
        if (HAS_UNSAFE_ARRAY_OPERATIONS) {
            return MEMORY_ACCESSOR.unsafe.arrayIndexScale(cls);
        }
        return -1;
    }

    public static Field bufferAddressField() {
        Field field;
        try {
            field = Buffer.class.getDeclaredField("address");
        } catch (Throwable unused) {
            field = null;
        }
        if (field == null || field.getType() != Long.TYPE) {
            return null;
        }
        return field;
    }

    public static boolean getBoolean(Object obj, long j) {
        return ((JvmMemoryAccessor) MEMORY_ACCESSOR).unsafe.getBoolean(obj, j);
    }

    public static byte getByte(byte[] bArr, long j) {
        MemoryAccessor memoryAccessor = MEMORY_ACCESSOR;
        return ((JvmMemoryAccessor) memoryAccessor).unsafe.getByte(bArr, BYTE_ARRAY_BASE_OFFSET + j);
    }

    public static double getDouble(Object obj, long j) {
        return ((JvmMemoryAccessor) MEMORY_ACCESSOR).unsafe.getDouble(obj, j);
    }

    public static float getFloat(Object obj, long j) {
        return ((JvmMemoryAccessor) MEMORY_ACCESSOR).unsafe.getFloat(obj, j);
    }

    public static int getInt(Object obj, long j) {
        return MEMORY_ACCESSOR.unsafe.getInt(obj, j);
    }

    public static long getLong(Object obj, long j) {
        return MEMORY_ACCESSOR.unsafe.getLong(obj, j);
    }

    public static Object getObject(Object obj, long j) {
        return MEMORY_ACCESSOR.unsafe.getObject(obj, j);
    }

    public static void putBoolean(Object obj, long j, boolean z) {
        ((JvmMemoryAccessor) MEMORY_ACCESSOR).unsafe.putBoolean(obj, j, z);
    }

    public static void putByte(byte[] bArr, long j, byte b) {
        MemoryAccessor memoryAccessor = MEMORY_ACCESSOR;
        ((JvmMemoryAccessor) memoryAccessor).unsafe.putByte(bArr, BYTE_ARRAY_BASE_OFFSET + j, b);
    }

    public static void putDouble(Object obj, long j, double d) {
        ((JvmMemoryAccessor) MEMORY_ACCESSOR).unsafe.putDouble(obj, j, d);
    }

    public static void putFloat(Object obj, long j, float f) {
        ((JvmMemoryAccessor) MEMORY_ACCESSOR).unsafe.putFloat(obj, j, f);
    }

    public static void putInt(Object obj, long j, int i) {
        MEMORY_ACCESSOR.unsafe.putInt(obj, j, i);
    }

    public static void putLong(Object obj, long j, long j2) {
        MEMORY_ACCESSOR.unsafe.putLong(obj, j, j2);
    }

    public static void putObject(Object obj, long j, Object obj2) {
        MEMORY_ACCESSOR.unsafe.putObject(obj, j, obj2);
    }
}
