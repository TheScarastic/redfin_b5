package com.bumptech.glide.load.engine;

import androidx.collection.SimpleArrayMap;
import androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DecodeJob;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.UnitTransformation;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry;
import com.bumptech.glide.load.resource.transcode.UnitTranscoder;
import com.bumptech.glide.provider.LoadPathCache;
import com.bumptech.glide.provider.ResourceDecoderRegistry;
import com.bumptech.glide.util.MultiClassKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DecodeHelper<Transcode> {
    public DecodeJob.DiskCacheProvider diskCacheProvider;
    public DiskCacheStrategy diskCacheStrategy;
    public GlideContext glideContext;
    public int height;
    public boolean isCacheKeysSet;
    public boolean isLoadDataSet;
    public boolean isScaleOnlyOrNoTransform;
    public boolean isTransformationRequired;
    public Object model;
    public Options options;
    public Priority priority;
    public Class<?> resourceClass;
    public Key signature;
    public Class<Transcode> transcodeClass;
    public Map<Class<?>, Transformation<?>> transformations;
    public int width;
    public final List<ModelLoader.LoadData<?>> loadData = new ArrayList();
    public final List<Key> cacheKeys = new ArrayList();

    public List<Key> getCacheKeys() {
        if (!this.isCacheKeysSet) {
            this.isCacheKeysSet = true;
            this.cacheKeys.clear();
            List<ModelLoader.LoadData<?>> loadData = getLoadData();
            int size = loadData.size();
            for (int i = 0; i < size; i++) {
                ModelLoader.LoadData<?> loadData2 = loadData.get(i);
                if (!this.cacheKeys.contains(loadData2.sourceKey)) {
                    this.cacheKeys.add(loadData2.sourceKey);
                }
                for (int i2 = 0; i2 < loadData2.alternateKeys.size(); i2++) {
                    if (!this.cacheKeys.contains(loadData2.alternateKeys.get(i2))) {
                        this.cacheKeys.add(loadData2.alternateKeys.get(i2));
                    }
                }
            }
        }
        return this.cacheKeys;
    }

    public DiskCache getDiskCache() {
        return ((Engine.LazyDiskCacheProvider) this.diskCacheProvider).getDiskCache();
    }

    public List<ModelLoader.LoadData<?>> getLoadData() {
        if (!this.isLoadDataSet) {
            this.isLoadDataSet = true;
            this.loadData.clear();
            List modelLoaders = this.glideContext.registry.getModelLoaders(this.model);
            int size = modelLoaders.size();
            for (int i = 0; i < size; i++) {
                ModelLoader.LoadData<?> buildLoadData = ((ModelLoader) modelLoaders.get(i)).buildLoadData(this.model, this.width, this.height, this.options);
                if (buildLoadData != null) {
                    this.loadData.add(buildLoadData);
                }
            }
        }
        return this.loadData;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r18v0, resolved type: java.lang.Class<Data> */
    /* JADX WARN: Multi-variable type inference failed */
    public <Data> LoadPath<Data, ?, Transcode> getLoadPath(Class<Data> cls) {
        LoadPath<Data, ?, Transcode> loadPath;
        LoadPath<Data, ?, Transcode> loadPath2;
        Object obj;
        ArrayList arrayList;
        ResourceTranscoder resourceTranscoder;
        Registry registry = this.glideContext.registry;
        Class<?> cls2 = this.resourceClass;
        Class cls3 = (Class<Transcode>) this.transcodeClass;
        LoadPathCache loadPathCache = registry.loadPathCache;
        MultiClassKey andSet = loadPathCache.keyRef.getAndSet(null);
        if (andSet == null) {
            andSet = new MultiClassKey();
        }
        andSet.first = cls;
        andSet.second = cls2;
        andSet.third = cls3;
        synchronized (loadPathCache.cache) {
            loadPath = (LoadPath<Data, ?, Transcode>) loadPathCache.cache.getOrDefault(andSet, null);
        }
        loadPathCache.keyRef.set(andSet);
        Objects.requireNonNull(registry.loadPathCache);
        if (LoadPathCache.NO_PATHS_SIGNAL.equals(loadPath)) {
            return null;
        }
        if (loadPath != null) {
            return loadPath;
        }
        ArrayList arrayList2 = new ArrayList();
        Iterator it = ((ArrayList) registry.decoderRegistry.getResourceClasses(cls, cls2)).iterator();
        while (it.hasNext()) {
            Class<?> cls4 = (Class) it.next();
            Iterator it2 = ((ArrayList) registry.transcoderRegistry.getTranscodeClasses(cls4, cls3)).iterator();
            while (it2.hasNext()) {
                Class<?> cls5 = (Class) it2.next();
                ResourceDecoderRegistry resourceDecoderRegistry = registry.decoderRegistry;
                synchronized (resourceDecoderRegistry) {
                    arrayList = new ArrayList();
                    for (String str : resourceDecoderRegistry.bucketPriorityList) {
                        List<ResourceDecoderRegistry.Entry<?, ?>> list = resourceDecoderRegistry.decoders.get(str);
                        if (list != null) {
                            for (ResourceDecoderRegistry.Entry<?, ?> entry : list) {
                                if (entry.handles(cls, cls4)) {
                                    arrayList.add(entry.decoder);
                                }
                            }
                        }
                    }
                }
                TranscoderRegistry transcoderRegistry = registry.transcoderRegistry;
                synchronized (transcoderRegistry) {
                    if (cls5.isAssignableFrom(cls4)) {
                        resourceTranscoder = UnitTranscoder.UNIT_TRANSCODER;
                    } else {
                        for (TranscoderRegistry.Entry<?, ?> entry2 : transcoderRegistry.transcoders) {
                            if (entry2.handles(cls4, cls5)) {
                                resourceTranscoder = entry2.transcoder;
                            }
                        }
                        String valueOf = String.valueOf(cls4);
                        String valueOf2 = String.valueOf(cls5);
                        StringBuilder sb = new StringBuilder(valueOf.length() + 47 + valueOf2.length());
                        sb.append("No transcoder registered to transcode from ");
                        sb.append(valueOf);
                        sb.append(" to ");
                        sb.append(valueOf2);
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
                arrayList2.add(new DecodePath(cls, cls4, cls5, arrayList, resourceTranscoder, registry.throwableListPool));
            }
        }
        if (arrayList2.isEmpty()) {
            loadPath2 = null;
        } else {
            loadPath2 = new LoadPath<>(cls, cls2, cls3, arrayList2, registry.throwableListPool);
        }
        LoadPathCache loadPathCache2 = registry.loadPathCache;
        synchronized (loadPathCache2.cache) {
            SimpleArrayMap simpleArrayMap = loadPathCache2.cache;
            MultiClassKey multiClassKey = new MultiClassKey(cls, cls2, cls3);
            if (loadPath2 != null) {
                obj = loadPath2;
            } else {
                obj = LoadPathCache.NO_PATHS_SIGNAL;
            }
            simpleArrayMap.put(multiClassKey, obj);
        }
        return loadPath2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0025, code lost:
        r0 = (com.bumptech.glide.load.Encoder<X>) r2.encoder;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <X> com.bumptech.glide.load.Encoder<X> getSourceEncoder(X r5) throws com.bumptech.glide.Registry.NoSourceEncoderAvailableException {
        /*
            r4 = this;
            com.bumptech.glide.GlideContext r4 = r4.glideContext
            com.bumptech.glide.Registry r4 = r4.registry
            com.bumptech.glide.provider.EncoderRegistry r4 = r4.encoderRegistry
            java.lang.Class r0 = r5.getClass()
            monitor-enter(r4)
            java.util.List<com.bumptech.glide.provider.EncoderRegistry$Entry<?>> r1 = r4.encoders     // Catch: all -> 0x0038
            java.util.Iterator r1 = r1.iterator()     // Catch: all -> 0x0038
        L_0x0011:
            boolean r2 = r1.hasNext()     // Catch: all -> 0x0038
            if (r2 == 0) goto L_0x0029
            java.lang.Object r2 = r1.next()     // Catch: all -> 0x0038
            com.bumptech.glide.provider.EncoderRegistry$Entry r2 = (com.bumptech.glide.provider.EncoderRegistry.Entry) r2     // Catch: all -> 0x0038
            java.lang.Class<T> r3 = r2.dataClass     // Catch: all -> 0x0038
            boolean r3 = r3.isAssignableFrom(r0)     // Catch: all -> 0x0038
            if (r3 == 0) goto L_0x0011
            com.bumptech.glide.load.Encoder<T> r0 = r2.encoder     // Catch: all -> 0x0038
            monitor-exit(r4)
            goto L_0x002b
        L_0x0029:
            r0 = 0
            monitor-exit(r4)
        L_0x002b:
            if (r0 == 0) goto L_0x002e
            return r0
        L_0x002e:
            com.bumptech.glide.Registry$NoSourceEncoderAvailableException r4 = new com.bumptech.glide.Registry$NoSourceEncoderAvailableException
            java.lang.Class r5 = r5.getClass()
            r4.<init>(r5)
            throw r4
        L_0x0038:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.engine.DecodeHelper.getSourceEncoder(java.lang.Object):com.bumptech.glide.load.Encoder");
    }

    public <Z> Transformation<Z> getTransformation(Class<Z> cls) {
        Transformation<Z> transformation = (Transformation<Z>) this.transformations.get(cls);
        if (transformation == null) {
            Iterator<Map.Entry<Class<?>, Transformation<?>>> it = this.transformations.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry<Class<?>, Transformation<?>> next = it.next();
                if (next.getKey().isAssignableFrom(cls)) {
                    transformation = (Transformation<Z>) next.getValue();
                    break;
                }
            }
        }
        if (transformation != null) {
            return transformation;
        }
        if (!this.transformations.isEmpty() || !this.isTransformationRequired) {
            return (UnitTransformation) UnitTransformation.TRANSFORMATION;
        }
        String valueOf = String.valueOf(cls);
        throw new IllegalArgumentException(FakeDrag$$ExternalSyntheticOutline0.m(valueOf.length() + 115, "Missing transformation for ", valueOf, ". If you wish to ignore unknown resource types, use the optional transformation methods."));
    }

    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: java.lang.Class<?> */
    /* JADX WARN: Multi-variable type inference failed */
    public boolean hasLoadPath(Class<?> cls) {
        return getLoadPath(cls) != null;
    }
}
