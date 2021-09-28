package com.android.volley.toolbox;

import android.content.Context;
import com.android.volley.toolbox.DiskBasedCache;
import java.io.File;
/* loaded from: classes.dex */
public class Volley$1 implements DiskBasedCache.FileSupplier {
    public File cacheDir = null;
    public final /* synthetic */ Context val$appContext;

    public Volley$1(Context context) {
        this.val$appContext = context;
    }

    public File get() {
        if (this.cacheDir == null) {
            this.cacheDir = new File(this.val$appContext.getCacheDir(), "volley");
        }
        return this.cacheDir;
    }
}
