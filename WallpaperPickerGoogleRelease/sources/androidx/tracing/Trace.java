package androidx.tracing;

import android.annotation.SuppressLint;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: classes.dex */
public final class Trace {
    public static Method sIsTagEnabledMethod;
    public static long sTraceTagApp;

    @SuppressLint({"NewApi"})
    public static boolean isEnabled() {
        try {
            if (sIsTagEnabledMethod == null) {
                return android.os.Trace.isEnabled();
            }
        } catch (NoClassDefFoundError | NoSuchMethodError unused) {
        }
        try {
            if (sIsTagEnabledMethod == null) {
                sTraceTagApp = android.os.Trace.class.getField("TRACE_TAG_APP").getLong(null);
                sIsTagEnabledMethod = android.os.Trace.class.getMethod("isTagEnabled", Long.TYPE);
            }
            return ((Boolean) sIsTagEnabledMethod.invoke(null, Long.valueOf(sTraceTagApp))).booleanValue();
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                throw new RuntimeException(cause);
            }
            Log.v("Trace", "Unable to call isTagEnabled via reflection", e);
            return false;
        }
    }
}
