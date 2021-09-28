package com.bumptech.glide.load.model;

import androidx.core.util.Pools$Pool;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class ModelLoaderRegistry {
    public final ModelLoaderCache cache = new ModelLoaderCache();
    public final MultiModelLoaderFactory multiModelLoaderFactory;

    /* loaded from: classes.dex */
    public static class ModelLoaderCache {
        public final Map<Class<?>, Entry<?>> cachedModelLoaders = new HashMap();

        /* loaded from: classes.dex */
        public static class Entry<Model> {
            public final List<ModelLoader<Model, ?>> loaders;

            public Entry(List<ModelLoader<Model, ?>> list) {
                this.loaders = list;
            }
        }

        public <Model> void put(Class<Model> cls, List<ModelLoader<Model, ?>> list) {
            if (this.cachedModelLoaders.put(cls, new Entry<>(list)) != null) {
                String valueOf = String.valueOf(cls);
                throw new IllegalStateException(Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(valueOf.length() + 34, "Already cached loaders for model: ", valueOf));
            }
        }
    }

    public ModelLoaderRegistry(Pools$Pool<List<Throwable>> pools$Pool) {
        MultiModelLoaderFactory multiModelLoaderFactory = new MultiModelLoaderFactory(pools$Pool, MultiModelLoaderFactory.DEFAULT_FACTORY);
        this.multiModelLoaderFactory = multiModelLoaderFactory;
    }
}
