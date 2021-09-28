package androidx.appcompat.widget;

import android.content.Context;
import android.content.ContextWrapper;
/* loaded from: classes.dex */
public class TintContextWrapper extends ContextWrapper {
    public static final Object CACHE_LOCK = new Object();

    public static Context wrap(Context context) {
        if (!(context instanceof TintContextWrapper) && !(context.getResources() instanceof TintResources)) {
            context.getResources();
            int i = VectorEnabledTintResources.$r8$clinit;
        }
        return context;
    }
}
