package com.google.common.io;

import com.android.systemui.shared.system.QuickStepContract;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ByteStreams {
    public static final /* synthetic */ int $r8$clinit = 0;

    static {
        new OutputStream() { // from class: com.google.common.io.ByteStreams.1
            @Override // java.lang.Object
            public String toString() {
                return "ByteStreams.nullOutputStream()";
            }

            @Override // java.io.OutputStream
            public void write(int i) {
            }

            @Override // java.io.OutputStream
            public void write(byte[] bArr) {
                Objects.requireNonNull(bArr);
            }

            @Override // java.io.OutputStream
            public void write(byte[] bArr, int i, int i2) {
                Objects.requireNonNull(bArr);
            }
        };
    }

    public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        Objects.requireNonNull(inputStream);
        Objects.requireNonNull(outputStream);
        byte[] bArr = new byte[QuickStepContract.SYSUI_STATE_ASSIST_GESTURE_CONSTRAINED];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }
}
