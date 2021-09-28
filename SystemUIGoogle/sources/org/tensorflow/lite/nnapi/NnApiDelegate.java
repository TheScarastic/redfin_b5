package org.tensorflow.lite.nnapi;

import org.tensorflow.lite.Delegate;
import org.tensorflow.lite.TensorFlowLite;
/* loaded from: classes2.dex */
public class NnApiDelegate implements Delegate, AutoCloseable {
    private long delegateHandle;

    /* loaded from: classes2.dex */
    public static final class Options {
        private int executionPreference = -1;
        private String acceleratorName = null;
        private String cacheDir = null;
        private String modelToken = null;
        private Integer maxDelegatedPartitions = null;
        private Boolean useNnapiCpu = null;
        private Boolean allowFp16 = null;
    }

    private static native long createDelegate(int i, String str, String str2, String str3, int i2, boolean z, boolean z2, boolean z3);

    private static native void deleteDelegate(long j);

    public NnApiDelegate(Options options) {
        TensorFlowLite.init();
        int i = options.executionPreference;
        String str = options.acceleratorName;
        String str2 = options.cacheDir;
        String str3 = options.modelToken;
        int intValue = options.maxDelegatedPartitions != null ? options.maxDelegatedPartitions.intValue() : -1;
        boolean z = true;
        boolean z2 = false;
        boolean z3 = options.useNnapiCpu != null;
        if (options.useNnapiCpu != null && options.useNnapiCpu.booleanValue()) {
            z = false;
        }
        this.delegateHandle = createDelegate(i, str, str2, str3, intValue, z3, z, options.allowFp16 != null ? options.allowFp16.booleanValue() : z2);
    }

    public NnApiDelegate() {
        this(new Options());
    }

    @Override // org.tensorflow.lite.Delegate
    public long getNativeHandle() {
        return this.delegateHandle;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        long j = this.delegateHandle;
        if (j != 0) {
            deleteDelegate(j);
            this.delegateHandle = 0;
        }
    }
}
