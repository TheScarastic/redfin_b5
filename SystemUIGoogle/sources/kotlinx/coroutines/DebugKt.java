package kotlinx.coroutines;

import java.util.concurrent.atomic.AtomicLong;
/* compiled from: Debug.kt */
/* loaded from: classes2.dex */
public final class DebugKt {
    private static final boolean ASSERTIONS_ENABLED = false;
    private static final AtomicLong COROUTINE_ID;
    private static final boolean DEBUG;
    private static final boolean RECOVER_STACK_TRACES;

    public static final boolean getASSERTIONS_ENABLED() {
        return ASSERTIONS_ENABLED;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0038, code lost:
        if (r0.equals("on") != false) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0041, code lost:
        if (r0.equals("") != false) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0043, code lost:
        r0 = true;
     */
    static {
        /*
            java.lang.String r0 = "kotlinx.coroutines.debug"
            java.lang.String r0 = kotlinx.coroutines.internal.SystemPropsKt.systemProp(r0)
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L_0x000b
            goto L_0x0027
        L_0x000b:
            int r3 = r0.hashCode()
            if (r3 == 0) goto L_0x003b
            r4 = 3551(0xddf, float:4.976E-42)
            if (r3 == r4) goto L_0x0032
            r4 = 109935(0x1ad6f, float:1.54052E-40)
            if (r3 == r4) goto L_0x0029
            r4 = 3005871(0x2dddaf, float:4.212122E-39)
            if (r3 != r4) goto L_0x005e
            java.lang.String r3 = "auto"
            boolean r3 = r0.equals(r3)
            if (r3 == 0) goto L_0x005e
        L_0x0027:
            r0 = r2
            goto L_0x0044
        L_0x0029:
            java.lang.String r3 = "off"
            boolean r3 = r0.equals(r3)
            if (r3 == 0) goto L_0x005e
            goto L_0x0027
        L_0x0032:
            java.lang.String r3 = "on"
            boolean r3 = r0.equals(r3)
            if (r3 == 0) goto L_0x005e
            goto L_0x0043
        L_0x003b:
            java.lang.String r3 = ""
            boolean r3 = r0.equals(r3)
            if (r3 == 0) goto L_0x005e
        L_0x0043:
            r0 = r1
        L_0x0044:
            kotlinx.coroutines.DebugKt.DEBUG = r0
            if (r0 == 0) goto L_0x0051
            java.lang.String r0 = "kotlinx.coroutines.stacktrace.recovery"
            boolean r0 = kotlinx.coroutines.internal.SystemPropsKt.systemProp(r0, r1)
            if (r0 == 0) goto L_0x0051
            goto L_0x0052
        L_0x0051:
            r1 = r2
        L_0x0052:
            kotlinx.coroutines.DebugKt.RECOVER_STACK_TRACES = r1
            java.util.concurrent.atomic.AtomicLong r0 = new java.util.concurrent.atomic.AtomicLong
            r1 = 0
            r0.<init>(r1)
            kotlinx.coroutines.DebugKt.COROUTINE_ID = r0
            return
        L_0x005e:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "System property 'kotlinx.coroutines.debug' has unrecognized value '"
            r1.append(r2)
            r1.append(r0)
            r0 = 39
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
            java.lang.String r0 = r0.toString()
            r1.<init>(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.DebugKt.<clinit>():void");
    }

    public static final boolean getDEBUG() {
        return DEBUG;
    }

    public static final boolean getRECOVER_STACK_TRACES() {
        return RECOVER_STACK_TRACES;
    }

    public static final AtomicLong getCOROUTINE_ID() {
        return COROUTINE_ID;
    }
}
