package kotlinx.coroutines.internal;

import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class SystemPropsKt {
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00a0  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00e3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final long systemProp(@org.jetbrains.annotations.NotNull java.lang.String r21, long r22, long r24, long r26) {
        /*
        // Method dump skipped, instructions count: 266
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.SystemPropsKt.systemProp(java.lang.String, long, long, long):long");
    }

    public static int systemProp$default(String str, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 4) != 0) {
            i2 = 1;
        }
        if ((i4 & 8) != 0) {
            i3 = Integer.MAX_VALUE;
        }
        return (int) systemProp(str, (long) i, (long) i2, (long) i3);
    }

    public static /* synthetic */ long systemProp$default(String str, long j, long j2, long j3, int i, Object obj) {
        if ((i & 4) != 0) {
            j2 = 1;
        }
        if ((i & 8) != 0) {
            j3 = RecyclerView.FOREVER_NS;
        }
        return systemProp(str, j, j2, j3);
    }

    @Nullable
    public static final String systemProp(@NotNull String str) {
        int i = SystemPropsKt__SystemPropsKt.AVAILABLE_PROCESSORS;
        try {
            return System.getProperty(str);
        } catch (SecurityException unused) {
            return null;
        }
    }
}
