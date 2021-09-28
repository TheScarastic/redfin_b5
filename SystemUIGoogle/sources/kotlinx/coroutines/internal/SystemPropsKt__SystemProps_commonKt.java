package kotlinx.coroutines.internal;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringNumberConversionsKt;
/* compiled from: SystemProps.common.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final /* synthetic */ class SystemPropsKt__SystemProps_commonKt {
    public static final boolean systemProp(String str, boolean z) {
        Intrinsics.checkParameterIsNotNull(str, "propertyName");
        String systemProp = SystemPropsKt.systemProp(str);
        return systemProp != null ? Boolean.parseBoolean(systemProp) : z;
    }

    public static /* synthetic */ int systemProp$default(String str, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 4) != 0) {
            i2 = 1;
        }
        if ((i4 & 8) != 0) {
            i3 = Integer.MAX_VALUE;
        }
        return SystemPropsKt.systemProp(str, i, i2, i3);
    }

    public static final int systemProp(String str, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(str, "propertyName");
        return (int) SystemPropsKt.systemProp(str, (long) i, (long) i2, (long) i3);
    }

    public static /* synthetic */ long systemProp$default(String str, long j, long j2, long j3, int i, Object obj) {
        if ((i & 4) != 0) {
            j2 = 1;
        }
        if ((i & 8) != 0) {
            j3 = Long.MAX_VALUE;
        }
        return SystemPropsKt.systemProp(str, j, j2, j3);
    }

    public static final long systemProp(String str, long j, long j2, long j3) {
        Intrinsics.checkParameterIsNotNull(str, "propertyName");
        String systemProp = SystemPropsKt.systemProp(str);
        if (systemProp == null) {
            return j;
        }
        Long l = StringsKt__StringNumberConversionsKt.toLongOrNull(systemProp);
        if (l != null) {
            long longValue = l.longValue();
            if (j2 <= longValue && j3 >= longValue) {
                return longValue;
            }
            throw new IllegalStateException(("System property '" + str + "' should be in range " + j2 + ".." + j3 + ", but is '" + longValue + '\'').toString());
        }
        throw new IllegalStateException(("System property '" + str + "' has unrecognized value '" + systemProp + '\'').toString());
    }
}
