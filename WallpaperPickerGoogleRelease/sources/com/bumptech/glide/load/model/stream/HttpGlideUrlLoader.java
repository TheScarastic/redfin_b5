package com.bumptech.glide.load.model.stream;

import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.HttpUrlFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;
import java.util.Objects;
/* loaded from: classes.dex */
public class HttpGlideUrlLoader implements ModelLoader<GlideUrl, InputStream> {
    public static final Option<Integer> TIMEOUT = Option.memory("com.bumptech.glide.load.model.stream.HttpGlideUrlLoader.Timeout", 2500);
    public final ModelCache<GlideUrl, GlideUrl> modelCache;

    /* loaded from: classes.dex */
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        public final ModelCache<GlideUrl, GlideUrl> modelCache = new ModelCache<>(500);

        @Override // com.bumptech.glide.load.model.ModelLoaderFactory
        public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new HttpGlideUrlLoader(this.modelCache);
        }
    }

    public HttpGlideUrlLoader(ModelCache<GlideUrl, GlideUrl> modelCache) {
        this.modelCache = modelCache;
    }

    /* Return type fixed from 'com.bumptech.glide.load.model.ModelLoader$LoadData' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, int, int, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.model.ModelLoader
    public ModelLoader.LoadData<InputStream> buildLoadData(GlideUrl glideUrl, int i, int i2, Options options) {
        GlideUrl glideUrl2 = glideUrl;
        ModelCache<GlideUrl, GlideUrl> modelCache = this.modelCache;
        if (modelCache != null) {
            ModelCache.ModelKey<GlideUrl> modelKey = ModelCache.ModelKey.get(glideUrl2, 0, 0);
            GlideUrl glideUrl3 = modelCache.cache.get(modelKey);
            modelKey.release();
            GlideUrl glideUrl4 = glideUrl3;
            if (glideUrl4 == null) {
                ModelCache<GlideUrl, GlideUrl> modelCache2 = this.modelCache;
                Objects.requireNonNull(modelCache2);
                modelCache2.cache.put(ModelCache.ModelKey.get(glideUrl2, 0, 0), glideUrl2);
            } else {
                glideUrl2 = glideUrl4;
            }
        }
        return new ModelLoader.LoadData<>(glideUrl2, new HttpUrlFetcher(glideUrl2, ((Integer) options.get(TIMEOUT)).intValue(), HttpUrlFetcher.DEFAULT_CONNECTION_FACTORY));
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // com.bumptech.glide.load.model.ModelLoader
    public /* bridge */ /* synthetic */ boolean handles(GlideUrl glideUrl) {
        return true;
    }
}
