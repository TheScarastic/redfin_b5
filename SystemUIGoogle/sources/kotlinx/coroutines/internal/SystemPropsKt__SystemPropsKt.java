package kotlinx.coroutines.internal;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: SystemProps.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final /* synthetic */ class SystemPropsKt__SystemPropsKt {
    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    public static final int getAVAILABLE_PROCESSORS() {
        return AVAILABLE_PROCESSORS;
    }

    public static final String systemProp(String str) {
        Intrinsics.checkParameterIsNotNull(str, "propertyName");
        try {
            return System.getProperty(str);
        } catch (SecurityException unused) {
            return null;
        }
    }
}
