package kotlin.io;

import java.io.Closeable;
import kotlin.ExceptionsKt;
/* compiled from: Closeable.kt */
/* loaded from: classes2.dex */
public final class CloseableKt {
    public static final void closeFinally(Closeable closeable, Throwable th) {
        if (closeable != null) {
            if (th == null) {
                closeable.close();
                return;
            }
            try {
                closeable.close();
            } catch (Throwable th2) {
                ExceptionsKt.addSuppressed(th, th2);
            }
        }
    }
}
