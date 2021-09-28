package kotlinx.coroutines;

import java.util.Arrays;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import kotlinx.coroutines.android.AndroidExceptionPreHandler;
/* loaded from: classes2.dex */
public final /* synthetic */ class CoroutineExceptionHandlerImplKt$$ExternalSyntheticServiceLoad0 {
    public static /* synthetic */ Iterator m() {
        try {
            return Arrays.asList(new AndroidExceptionPreHandler()).iterator();
        } catch (Throwable th) {
            throw new ServiceConfigurationError(th.getMessage(), th);
        }
    }
}
