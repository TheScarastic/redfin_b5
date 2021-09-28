package kotlinx.coroutines.internal;

import java.util.concurrent.ScheduledThreadPoolExecutor;
/* loaded from: classes.dex */
public final class ConcurrentKt {
    public static final /* synthetic */ int $r8$clinit = 0;

    static {
        try {
            ScheduledThreadPoolExecutor.class.getMethod("setRemoveOnCancelPolicy", Boolean.TYPE);
        } catch (Throwable unused) {
        }
    }
}
