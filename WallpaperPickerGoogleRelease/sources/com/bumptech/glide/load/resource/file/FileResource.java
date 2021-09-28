package com.bumptech.glide.load.resource.file;

import com.bumptech.glide.load.engine.Resource;
import java.io.File;
import java.util.Objects;
/* loaded from: classes.dex */
public class FileResource implements Resource {
    public final T data;

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: java.io.File */
    /* JADX WARN: Multi-variable type inference failed */
    public FileResource(File file) {
        Objects.requireNonNull(file, "Argument must not be null");
        this.data = file;
    }

    @Override // com.bumptech.glide.load.engine.Resource
    public final Object get() {
        return this.data;
    }

    @Override // com.bumptech.glide.load.engine.Resource
    public Class getResourceClass() {
        return this.data.getClass();
    }

    @Override // com.bumptech.glide.load.engine.Resource
    public final /* bridge */ /* synthetic */ int getSize() {
        return 1;
    }

    @Override // com.bumptech.glide.load.engine.Resource
    public /* bridge */ /* synthetic */ void recycle() {
    }
}
