package com.google.common.primitives;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Comparator;
import sun.misc.Unsafe;
/* loaded from: classes.dex */
public final class UnsignedBytes {

    /* loaded from: classes.dex */
    public static class LexicographicalComparatorHolder {
        public static final String UNSAFE_COMPARATOR_NAME;

        /* loaded from: classes.dex */
        public enum PureJavaComparator implements Comparator<byte[]> {
            INSTANCE;

            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
            @Override // java.util.Comparator
            public int compare(byte[] bArr, byte[] bArr2) {
                byte[] bArr3 = bArr;
                byte[] bArr4 = bArr2;
                int min = Math.min(bArr3.length, bArr4.length);
                for (int i = 0; i < min; i++) {
                    int i2 = (bArr3[i] & 255) - (bArr4[i] & 255);
                    if (i2 != 0) {
                        return i2;
                    }
                }
                return bArr3.length - bArr4.length;
            }

            @Override // java.lang.Enum, java.lang.Object
            public String toString() {
                return "UnsignedBytes.lexicographicalComparator() (pure Java version)";
            }
        }

        /* loaded from: classes.dex */
        public enum UnsafeComparator implements Comparator<byte[]> {
            /* Fake field, exist only in values array */
            INSTANCE;
            
            public static final boolean BIG_ENDIAN = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
            public static final int BYTE_ARRAY_BASE_OFFSET;
            public static final Unsafe theUnsafe;

            static {
                Unsafe unsafe;
                try {
                    try {
                        unsafe = Unsafe.getUnsafe();
                    } catch (SecurityException unused) {
                        unsafe = (Unsafe) AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>() { // from class: com.google.common.primitives.UnsignedBytes.LexicographicalComparatorHolder.UnsafeComparator.1
                            /* Return type fixed from 'java.lang.Object' to match base method */
                            @Override // java.security.PrivilegedExceptionAction
                            public Unsafe run() throws Exception {
                                Field[] declaredFields = Unsafe.class.getDeclaredFields();
                                for (Field field : declaredFields) {
                                    field.setAccessible(true);
                                    Object obj = field.get(null);
                                    if (Unsafe.class.isInstance(obj)) {
                                        return (Unsafe) Unsafe.class.cast(obj);
                                    }
                                }
                                throw new NoSuchFieldError("the Unsafe");
                            }
                        });
                    }
                    theUnsafe = unsafe;
                    int arrayBaseOffset = unsafe.arrayBaseOffset(byte[].class);
                    BYTE_ARRAY_BASE_OFFSET = arrayBaseOffset;
                    if (!"64".equals(System.getProperty("sun.arch.data.model")) || arrayBaseOffset % 8 != 0 || unsafe.arrayIndexScale(byte[].class) != 1) {
                        throw new Error();
                    }
                } catch (PrivilegedActionException e) {
                    throw new RuntimeException("Could not initialize intrinsics", e.getCause());
                }
            }

            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
            @Override // java.util.Comparator
            public int compare(byte[] bArr, byte[] bArr2) {
                byte[] bArr3 = bArr;
                byte[] bArr4 = bArr2;
                int min = Math.min(bArr3.length, bArr4.length);
                int i = min & -8;
                int i2 = 0;
                while (i2 < i) {
                    Unsafe unsafe = theUnsafe;
                    long j = ((long) BYTE_ARRAY_BASE_OFFSET) + ((long) i2);
                    long j2 = unsafe.getLong(bArr3, j);
                    long j3 = unsafe.getLong(bArr4, j);
                    if (j2 == j3) {
                        i2 += 8;
                    } else if (BIG_ENDIAN) {
                        int i3 = ((j2 ^ Long.MIN_VALUE) > (Long.MIN_VALUE ^ j3) ? 1 : ((j2 ^ Long.MIN_VALUE) == (Long.MIN_VALUE ^ j3) ? 0 : -1));
                        if (i3 < 0) {
                            return -1;
                        }
                        if (i3 > 0) {
                            return 1;
                        }
                        return 0;
                    } else {
                        int numberOfTrailingZeros = Long.numberOfTrailingZeros(j2 ^ j3) & -8;
                        return ((int) ((j2 >>> numberOfTrailingZeros) & 255)) - ((int) (255 & (j3 >>> numberOfTrailingZeros)));
                    }
                }
                while (i2 < min) {
                    int i4 = (bArr3[i2] & 255) - (bArr4[i2] & 255);
                    if (i4 != 0) {
                        return i4;
                    }
                    i2++;
                }
                return bArr3.length - bArr4.length;
            }

            @Override // java.lang.Enum, java.lang.Object
            public String toString() {
                return "UnsignedBytes.lexicographicalComparator() (sun.misc.Unsafe version)";
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for r0v5, resolved type: java.lang.Object[] */
        /* JADX WARN: Multi-variable type inference failed */
        static {
            String concat = LexicographicalComparatorHolder.class.getName().concat("$UnsafeComparator");
            UNSAFE_COMPARATOR_NAME = concat;
            try {
                Comparator comparator = (Comparator) Class.forName(concat).getEnumConstants()[0];
            } catch (Throwable unused) {
                UnsignedBytes.lexicographicalComparatorJavaImpl();
            }
        }
    }

    public static Comparator<byte[]> lexicographicalComparatorJavaImpl() {
        return LexicographicalComparatorHolder.PureJavaComparator.INSTANCE;
    }
}
