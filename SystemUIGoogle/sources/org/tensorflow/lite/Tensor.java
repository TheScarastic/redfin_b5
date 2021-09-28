package org.tensorflow.lite;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Arrays;
/* loaded from: classes2.dex */
public final class Tensor {
    private final DataType dtype;
    private long nativeHandle;
    private final QuantizationParams quantizationParamsCopy;
    private int[] shapeCopy;
    private final int[] shapeSignatureCopy;

    private static native ByteBuffer buffer(long j);

    private static native long create(long j, int i);

    private static native void delete(long j);

    private static native int dtype(long j);

    private static native boolean hasDelegateBufferHandle(long j);

    private static native String name(long j);

    private static native int numBytes(long j);

    private static native float quantizationScale(long j);

    private static native int quantizationZeroPoint(long j);

    private static native void readMultiDimensionalArray(long j, Object obj);

    private static native int[] shape(long j);

    private static native int[] shapeSignature(long j);

    private static native void writeDirectBuffer(long j, Buffer buffer);

    private static native void writeMultiDimensionalArray(long j, Object obj);

    private static native void writeScalar(long j, Object obj);

    /* access modifiers changed from: package-private */
    public static Tensor fromIndex(long j, int i) {
        return new Tensor(create(j, i));
    }

    /* loaded from: classes2.dex */
    public static class QuantizationParams {
        private final float scale;
        private final int zeroPoint;

        public QuantizationParams(float f, int i) {
            this.scale = f;
            this.zeroPoint = i;
        }
    }

    /* access modifiers changed from: package-private */
    public void close() {
        delete(this.nativeHandle);
        this.nativeHandle = 0;
    }

    public int numBytes() {
        return numBytes(this.nativeHandle);
    }

    public String name() {
        return name(this.nativeHandle);
    }

    /* access modifiers changed from: package-private */
    public void setTo(Object obj) {
        if (obj != null) {
            throwIfTypeIsIncompatible(obj);
            throwIfSrcShapeIsIncompatible(obj);
            if (isBuffer(obj)) {
                setTo((Buffer) obj);
            } else if (obj.getClass().isArray()) {
                writeMultiDimensionalArray(this.nativeHandle, obj);
            } else {
                writeScalar(this.nativeHandle, obj);
            }
        } else if (!hasDelegateBufferHandle(this.nativeHandle)) {
            throw new IllegalArgumentException("Null inputs are allowed only if the Tensor is bound to a buffer handle.");
        }
    }

    private void setTo(Buffer buffer) {
        if (buffer instanceof ByteBuffer) {
            ByteBuffer byteBuffer = (ByteBuffer) buffer;
            if (!byteBuffer.isDirect() || byteBuffer.order() != ByteOrder.nativeOrder()) {
                buffer().put(byteBuffer);
            } else {
                writeDirectBuffer(this.nativeHandle, buffer);
            }
        } else if (buffer instanceof LongBuffer) {
            LongBuffer longBuffer = (LongBuffer) buffer;
            if (!longBuffer.isDirect() || longBuffer.order() != ByteOrder.nativeOrder()) {
                buffer().asLongBuffer().put(longBuffer);
            } else {
                writeDirectBuffer(this.nativeHandle, buffer);
            }
        } else if (buffer instanceof FloatBuffer) {
            FloatBuffer floatBuffer = (FloatBuffer) buffer;
            if (!floatBuffer.isDirect() || floatBuffer.order() != ByteOrder.nativeOrder()) {
                buffer().asFloatBuffer().put(floatBuffer);
            } else {
                writeDirectBuffer(this.nativeHandle, buffer);
            }
        } else if (buffer instanceof IntBuffer) {
            IntBuffer intBuffer = (IntBuffer) buffer;
            if (!intBuffer.isDirect() || intBuffer.order() != ByteOrder.nativeOrder()) {
                buffer().asIntBuffer().put(intBuffer);
            } else {
                writeDirectBuffer(this.nativeHandle, buffer);
            }
        } else {
            throw new IllegalArgumentException("Unexpected input buffer type: " + buffer);
        }
    }

    /* access modifiers changed from: package-private */
    public Object copyTo(Object obj) {
        if (obj != null) {
            throwIfTypeIsIncompatible(obj);
            throwIfDstShapeIsIncompatible(obj);
            if (isBuffer(obj)) {
                copyTo((Buffer) obj);
            } else {
                readMultiDimensionalArray(this.nativeHandle, obj);
            }
            return obj;
        } else if (hasDelegateBufferHandle(this.nativeHandle)) {
            return obj;
        } else {
            throw new IllegalArgumentException("Null outputs are allowed only if the Tensor is bound to a buffer handle.");
        }
    }

    private void copyTo(Buffer buffer) {
        if (buffer instanceof ByteBuffer) {
            ((ByteBuffer) buffer).put(buffer());
        } else if (buffer instanceof FloatBuffer) {
            ((FloatBuffer) buffer).put(buffer().asFloatBuffer());
        } else if (buffer instanceof LongBuffer) {
            ((LongBuffer) buffer).put(buffer().asLongBuffer());
        } else if (buffer instanceof IntBuffer) {
            ((IntBuffer) buffer).put(buffer().asIntBuffer());
        } else {
            throw new IllegalArgumentException("Unexpected output buffer type: " + buffer);
        }
    }

    /* access modifiers changed from: package-private */
    public int[] getInputShapeIfDifferent(Object obj) {
        if (obj == null || isBuffer(obj)) {
            return null;
        }
        throwIfTypeIsIncompatible(obj);
        int[] computeShapeOf = computeShapeOf(obj);
        if (Arrays.equals(this.shapeCopy, computeShapeOf)) {
            return null;
        }
        return computeShapeOf;
    }

    /* access modifiers changed from: package-private */
    public void refreshShape() {
        this.shapeCopy = shape(this.nativeHandle);
    }

    DataType dataTypeOf(Object obj) {
        if (obj != null) {
            Class<?> cls = obj.getClass();
            if (cls.isArray()) {
                while (cls.isArray()) {
                    cls = cls.getComponentType();
                }
                if (Float.TYPE.equals(cls)) {
                    return DataType.FLOAT32;
                }
                if (Integer.TYPE.equals(cls)) {
                    return DataType.INT32;
                }
                if (Byte.TYPE.equals(cls)) {
                    DataType dataType = this.dtype;
                    DataType dataType2 = DataType.STRING;
                    if (dataType == dataType2) {
                        return dataType2;
                    }
                    return DataType.UINT8;
                } else if (Long.TYPE.equals(cls)) {
                    return DataType.INT64;
                } else {
                    if (Boolean.TYPE.equals(cls)) {
                        return DataType.BOOL;
                    }
                    if (String.class.equals(cls)) {
                        return DataType.STRING;
                    }
                }
            } else if (Float.class.equals(cls) || (obj instanceof FloatBuffer)) {
                return DataType.FLOAT32;
            } else {
                if (Integer.class.equals(cls) || (obj instanceof IntBuffer)) {
                    return DataType.INT32;
                }
                if (Byte.class.equals(cls)) {
                    return DataType.UINT8;
                }
                if (Long.class.equals(cls) || (obj instanceof LongBuffer)) {
                    return DataType.INT64;
                }
                if (Boolean.class.equals(cls)) {
                    return DataType.BOOL;
                }
                if (String.class.equals(cls)) {
                    return DataType.STRING;
                }
            }
        }
        throw new IllegalArgumentException("DataType error: cannot resolve DataType of " + obj.getClass().getName());
    }

    int[] computeShapeOf(Object obj) {
        int computeNumDimensions = computeNumDimensions(obj);
        if (this.dtype == DataType.STRING) {
            Class<?> cls = obj.getClass();
            if (cls.isArray()) {
                while (cls.isArray()) {
                    cls = cls.getComponentType();
                }
                if (Byte.TYPE.equals(cls)) {
                    computeNumDimensions--;
                }
            }
        }
        int[] iArr = new int[computeNumDimensions];
        fillShape(obj, 0, iArr);
        return iArr;
    }

    static int computeNumDimensions(Object obj) {
        if (obj == null || !obj.getClass().isArray()) {
            return 0;
        }
        if (Array.getLength(obj) != 0) {
            return computeNumDimensions(Array.get(obj, 0)) + 1;
        }
        throw new IllegalArgumentException("Array lengths cannot be 0.");
    }

    static void fillShape(Object obj, int i, int[] iArr) {
        if (!(iArr == null || i == iArr.length)) {
            int length = Array.getLength(obj);
            if (iArr[i] == 0) {
                iArr[i] = length;
            } else if (iArr[i] != length) {
                throw new IllegalArgumentException(String.format("Mismatched lengths (%d and %d) in dimension %d", Integer.valueOf(iArr[i]), Integer.valueOf(length), Integer.valueOf(i)));
            }
            for (int i2 = 0; i2 < length; i2++) {
                fillShape(Array.get(obj, i2), i + 1, iArr);
            }
        }
    }

    private void throwIfTypeIsIncompatible(Object obj) {
        DataType dataTypeOf;
        if (!isByteBuffer(obj) && (dataTypeOf = dataTypeOf(obj)) != this.dtype && !dataTypeOf.toStringName().equals(this.dtype.toStringName())) {
            throw new IllegalArgumentException(String.format("Cannot convert between a TensorFlowLite tensor with type %s and a Java object of type %s (which is compatible with the TensorFlowLite type %s).", this.dtype, obj.getClass().getName(), dataTypeOf));
        }
    }

    private void throwIfSrcShapeIsIncompatible(Object obj) {
        if (isBuffer(obj)) {
            Buffer buffer = (Buffer) obj;
            int numBytes = numBytes();
            int capacity = isByteBuffer(obj) ? buffer.capacity() : buffer.capacity() * this.dtype.byteSize();
            if (numBytes != capacity) {
                throw new IllegalArgumentException(String.format("Cannot copy to a TensorFlowLite tensor (%s) with %d bytes from a Java Buffer with %d bytes.", name(), Integer.valueOf(numBytes), Integer.valueOf(capacity)));
            }
            return;
        }
        int[] computeShapeOf = computeShapeOf(obj);
        if (!Arrays.equals(computeShapeOf, this.shapeCopy)) {
            throw new IllegalArgumentException(String.format("Cannot copy to a TensorFlowLite tensor (%s) with shape %s from a Java object with shape %s.", name(), Arrays.toString(this.shapeCopy), Arrays.toString(computeShapeOf)));
        }
    }

    private void throwIfDstShapeIsIncompatible(Object obj) {
        if (isBuffer(obj)) {
            Buffer buffer = (Buffer) obj;
            int numBytes = numBytes();
            int capacity = isByteBuffer(obj) ? buffer.capacity() : buffer.capacity() * this.dtype.byteSize();
            if (numBytes > capacity) {
                throw new IllegalArgumentException(String.format("Cannot copy from a TensorFlowLite tensor (%s) with %d bytes to a Java Buffer with %d bytes.", name(), Integer.valueOf(numBytes), Integer.valueOf(capacity)));
            }
            return;
        }
        int[] computeShapeOf = computeShapeOf(obj);
        if (!Arrays.equals(computeShapeOf, this.shapeCopy)) {
            throw new IllegalArgumentException(String.format("Cannot copy from a TensorFlowLite tensor (%s) with shape %s to a Java object with shape %s.", name(), Arrays.toString(this.shapeCopy), Arrays.toString(computeShapeOf)));
        }
    }

    private static boolean isBuffer(Object obj) {
        return obj instanceof Buffer;
    }

    private static boolean isByteBuffer(Object obj) {
        return obj instanceof ByteBuffer;
    }

    private Tensor(long j) {
        this.nativeHandle = j;
        this.dtype = DataType.fromC(dtype(j));
        this.shapeCopy = shape(j);
        this.shapeSignatureCopy = shapeSignature(j);
        this.quantizationParamsCopy = new QuantizationParams(quantizationScale(j), quantizationZeroPoint(j));
    }

    private ByteBuffer buffer() {
        return buffer(this.nativeHandle).order(ByteOrder.nativeOrder());
    }
}
