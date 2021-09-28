package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.cache.DiskCache;
/* loaded from: classes.dex */
public class DataCacheWriter<DataType> implements DiskCache.Writer {
    public final DataType data;
    public final Encoder<DataType> encoder;
    public final Options options;

    public DataCacheWriter(Encoder<DataType> encoder, DataType datatype, Options options) {
        this.encoder = encoder;
        this.data = datatype;
        this.options = options;
    }
}
