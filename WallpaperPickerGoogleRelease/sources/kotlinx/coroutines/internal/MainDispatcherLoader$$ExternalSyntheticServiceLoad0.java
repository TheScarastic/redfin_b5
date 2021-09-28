package kotlinx.coroutines.internal;

import java.util.Arrays;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import kotlinx.coroutines.android.AndroidDispatcherFactory;
/* loaded from: classes.dex */
public final /* synthetic */ class MainDispatcherLoader$$ExternalSyntheticServiceLoad0 {
    public static /* synthetic */ Iterator m() {
        try {
            return Arrays.asList(new AndroidDispatcherFactory()).iterator();
        } catch (Throwable th) {
            throw new ServiceConfigurationError(th.getMessage(), th);
        }
    }
}
