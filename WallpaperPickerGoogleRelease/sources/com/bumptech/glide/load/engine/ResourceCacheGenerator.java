package com.bumptech.glide.load.engine;

import com.bumptech.glide.Registry;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DataFetcherGenerator;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderRegistry;
import com.bumptech.glide.provider.ModelToResourceClassCache;
import com.bumptech.glide.util.MultiClassKey;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class ResourceCacheGenerator implements DataFetcherGenerator, DataFetcher.DataCallback<Object> {
    public File cacheFile;
    public final DataFetcherGenerator.FetcherReadyCallback cb;
    public ResourceCacheKey currentKey;
    public final DecodeHelper<?> helper;
    public volatile ModelLoader.LoadData<?> loadData;
    public int modelLoaderIndex;
    public List<ModelLoader<File, ?>> modelLoaders;
    public int resourceClassIndex = -1;
    public int sourceIdIndex;
    public Key sourceKey;

    public ResourceCacheGenerator(DecodeHelper<?> decodeHelper, DataFetcherGenerator.FetcherReadyCallback fetcherReadyCallback) {
        this.helper = decodeHelper;
        this.cb = fetcherReadyCallback;
    }

    @Override // com.bumptech.glide.load.engine.DataFetcherGenerator
    public void cancel() {
        ModelLoader.LoadData<?> loadData = this.loadData;
        if (loadData != null) {
            loadData.fetcher.cancel();
        }
    }

    @Override // com.bumptech.glide.load.data.DataFetcher.DataCallback
    public void onDataReady(Object obj) {
        this.cb.onDataFetcherReady(this.sourceKey, obj, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE, this.currentKey);
    }

    @Override // com.bumptech.glide.load.data.DataFetcher.DataCallback
    public void onLoadFailed(Exception exc) {
        this.cb.onDataFetcherFailed(this.currentKey, exc, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE);
    }

    @Override // com.bumptech.glide.load.engine.DataFetcherGenerator
    public boolean startNext() {
        List<Class<?>> orDefault;
        List<Class<?>> dataClasses;
        List<Key> cacheKeys = this.helper.getCacheKeys();
        if (cacheKeys.isEmpty()) {
            return false;
        }
        DecodeHelper<?> decodeHelper = this.helper;
        Registry registry = decodeHelper.glideContext.registry;
        Class<?> cls = decodeHelper.model.getClass();
        Class<?> cls2 = decodeHelper.resourceClass;
        Class<?> cls3 = decodeHelper.transcodeClass;
        ModelToResourceClassCache modelToResourceClassCache = registry.modelToResourceClassCache;
        MultiClassKey andSet = modelToResourceClassCache.resourceClassKeyRef.getAndSet(null);
        if (andSet == null) {
            andSet = new MultiClassKey(cls, cls2, cls3);
        } else {
            andSet.first = cls;
            andSet.second = cls2;
            andSet.third = cls3;
        }
        synchronized (modelToResourceClassCache.registeredResourceClassCache) {
            orDefault = modelToResourceClassCache.registeredResourceClassCache.getOrDefault(andSet, null);
        }
        modelToResourceClassCache.resourceClassKeyRef.set(andSet);
        ArrayList arrayList = orDefault;
        if (orDefault == null) {
            ArrayList arrayList2 = new ArrayList();
            ModelLoaderRegistry modelLoaderRegistry = registry.modelLoaderRegistry;
            synchronized (modelLoaderRegistry) {
                dataClasses = modelLoaderRegistry.multiModelLoaderFactory.getDataClasses(cls);
            }
            Iterator it = ((ArrayList) dataClasses).iterator();
            while (it.hasNext()) {
                Iterator it2 = ((ArrayList) registry.decoderRegistry.getResourceClasses((Class) it.next(), cls2)).iterator();
                while (it2.hasNext()) {
                    Class cls4 = (Class) it2.next();
                    if (!((ArrayList) registry.transcoderRegistry.getTranscodeClasses(cls4, cls3)).isEmpty() && !arrayList2.contains(cls4)) {
                        arrayList2.add(cls4);
                    }
                }
            }
            ModelToResourceClassCache modelToResourceClassCache2 = registry.modelToResourceClassCache;
            List<Class<?>> unmodifiableList = Collections.unmodifiableList(arrayList2);
            synchronized (modelToResourceClassCache2.registeredResourceClassCache) {
                modelToResourceClassCache2.registeredResourceClassCache.put(new MultiClassKey(cls, cls2, cls3), unmodifiableList);
            }
            arrayList = arrayList2;
        }
        if (!arrayList.isEmpty()) {
            while (true) {
                List<ModelLoader<File, ?>> list = this.modelLoaders;
                if (list != null) {
                    if (this.modelLoaderIndex < list.size()) {
                        this.loadData = null;
                        boolean z = false;
                        while (!z) {
                            if (!(this.modelLoaderIndex < this.modelLoaders.size())) {
                                break;
                            }
                            List<ModelLoader<File, ?>> list2 = this.modelLoaders;
                            int i = this.modelLoaderIndex;
                            this.modelLoaderIndex = i + 1;
                            File file = this.cacheFile;
                            DecodeHelper<?> decodeHelper2 = this.helper;
                            this.loadData = list2.get(i).buildLoadData(file, decodeHelper2.width, decodeHelper2.height, decodeHelper2.options);
                            if (this.loadData != null && this.helper.hasLoadPath(this.loadData.fetcher.getDataClass())) {
                                this.loadData.fetcher.loadData(this.helper.priority, this);
                                z = true;
                            }
                        }
                        return z;
                    }
                }
                int i2 = this.resourceClassIndex + 1;
                this.resourceClassIndex = i2;
                if (i2 >= arrayList.size()) {
                    int i3 = this.sourceIdIndex + 1;
                    this.sourceIdIndex = i3;
                    if (i3 >= cacheKeys.size()) {
                        return false;
                    }
                    this.resourceClassIndex = 0;
                }
                Key key = cacheKeys.get(this.sourceIdIndex);
                Class<?> cls5 = arrayList.get(this.resourceClassIndex);
                Transformation<Z> transformation = this.helper.getTransformation(cls5);
                DecodeHelper<?> decodeHelper3 = this.helper;
                this.currentKey = new ResourceCacheKey(decodeHelper3.glideContext.arrayPool, key, decodeHelper3.signature, decodeHelper3.width, decodeHelper3.height, transformation, cls5, decodeHelper3.options);
                File file2 = decodeHelper3.getDiskCache().get(this.currentKey);
                this.cacheFile = file2;
                if (file2 != null) {
                    this.sourceKey = key;
                    this.modelLoaders = this.helper.glideContext.registry.getModelLoaders(file2);
                    this.modelLoaderIndex = 0;
                }
            }
        } else if (File.class.equals(this.helper.transcodeClass)) {
            return false;
        } else {
            String valueOf = String.valueOf(this.helper.model.getClass());
            String valueOf2 = String.valueOf(this.helper.transcodeClass);
            throw new IllegalStateException(Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1.m(valueOf2.length() + valueOf.length() + 38, "Failed to find any load path from ", valueOf, " to ", valueOf2));
        }
    }
}
