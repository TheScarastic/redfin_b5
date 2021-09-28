package org.tensorflow.lite;
/* loaded from: classes2.dex */
public final class TensorFlowLite {
    private static final Throwable LOAD_LIBRARY_EXCEPTION;
    private static volatile boolean isInit = false;

    private static native String nativeRuntimeVersion();

    static {
        UnsatisfiedLinkError e;
        try {
            System.loadLibrary("tensorflowlite_jni");
            e = null;
        } catch (UnsatisfiedLinkError e2) {
            e = e2;
        }
        LOAD_LIBRARY_EXCEPTION = e;
    }

    public static String runtimeVersion() {
        init();
        return nativeRuntimeVersion();
    }

    public static void init() {
        if (!isInit) {
            try {
                nativeRuntimeVersion();
                isInit = true;
            } catch (UnsatisfiedLinkError e) {
                e = e;
                Object obj = LOAD_LIBRARY_EXCEPTION;
                if (obj != null) {
                    e = obj;
                }
                throw new UnsatisfiedLinkError("Failed to load native TensorFlow Lite methods. Check that the correct native libraries are present, and, if using a custom native library, have been properly loaded via System.loadLibrary():\n  " + e);
            }
        }
    }
}
