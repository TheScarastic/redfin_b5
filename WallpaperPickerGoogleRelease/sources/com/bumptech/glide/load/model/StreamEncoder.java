package com.bumptech.glide.load.model;

import android.util.Log;
import com.android.systemui.shared.system.QuickStepContract;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class StreamEncoder implements Encoder<InputStream> {
    public final ArrayPool byteArrayPool;

    public StreamEncoder(ArrayPool arrayPool) {
        this.byteArrayPool = arrayPool;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.io.File, com.bumptech.glide.load.Options] */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:28:0x0043 */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:30:0x0000 */
    /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: com.bumptech.glide.load.model.StreamEncoder */
    /* JADX DEBUG: Multi-variable search result rejected for r4v2, resolved type: com.bumptech.glide.load.model.StreamEncoder */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v1 */
    /* JADX WARN: Type inference failed for: r4v3, types: [com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool] */
    @Override // com.bumptech.glide.load.Encoder
    public boolean encode(InputStream inputStream, File file, Options options) {
        boolean z;
        byte[] bArr;
        InputStream inputStream2;
        FileOutputStream fileOutputStream;
        Throwable th;
        FileOutputStream fileOutputStream2;
        IOException e;
        try {
            inputStream2 = inputStream;
            bArr = (byte[]) this.byteArrayPool.get(QuickStepContract.SYSUI_STATE_ONE_HANDED_ACTIVE, byte[].class);
            z = false;
            fileOutputStream = null;
        } catch (IOException unused) {
        }
        try {
            try {
                fileOutputStream2 = new FileOutputStream(file);
                while (true) {
                    try {
                        int read = inputStream2.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        fileOutputStream2.write(bArr, 0, read);
                    } catch (IOException e2) {
                        e = e2;
                        fileOutputStream = fileOutputStream2;
                        if (Log.isLoggable("StreamEncoder", 3)) {
                            Log.d("StreamEncoder", "Failed to encode data onto the OutputStream", e);
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        this = this.byteArrayPool;
                        this.put(bArr);
                        return z;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileOutputStream2 != null) {
                            try {
                                fileOutputStream2.close();
                            } catch (IOException unused2) {
                            }
                        }
                        this.byteArrayPool.put(bArr);
                        throw th;
                    }
                }
                fileOutputStream2.close();
                z = true;
                fileOutputStream2.close();
            } catch (IOException e3) {
                e = e3;
            }
            this = this.byteArrayPool;
            this.put(bArr);
            return z;
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream2 = fileOutputStream;
        }
    }
}
