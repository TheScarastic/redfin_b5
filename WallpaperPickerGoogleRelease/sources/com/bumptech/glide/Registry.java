package com.bumptech.glide;

import androidx.core.util.Pools$Pool;
import androidx.core.util.Pools$SynchronizedPool;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.data.DataRewinderRegistry;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.ModelLoaderRegistry;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry;
import com.bumptech.glide.provider.EncoderRegistry;
import com.bumptech.glide.provider.ImageHeaderParserRegistry;
import com.bumptech.glide.provider.LoadPathCache;
import com.bumptech.glide.provider.ModelToResourceClassCache;
import com.bumptech.glide.provider.ResourceDecoderRegistry;
import com.bumptech.glide.provider.ResourceEncoderRegistry;
import com.bumptech.glide.util.pool.FactoryPools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class Registry {
    public final ResourceDecoderRegistry decoderRegistry;
    public final ModelLoaderRegistry modelLoaderRegistry;
    public final Pools$Pool<List<Throwable>> throwableListPool;
    public final ModelToResourceClassCache modelToResourceClassCache = new ModelToResourceClassCache();
    public final LoadPathCache loadPathCache = new LoadPathCache();
    public final EncoderRegistry encoderRegistry = new EncoderRegistry();
    public final ResourceEncoderRegistry resourceEncoderRegistry = new ResourceEncoderRegistry();
    public final DataRewinderRegistry dataRewinderRegistry = new DataRewinderRegistry();
    public final TranscoderRegistry transcoderRegistry = new TranscoderRegistry();
    public final ImageHeaderParserRegistry imageHeaderParserRegistry = new ImageHeaderParserRegistry();

    /* loaded from: classes.dex */
    public static class MissingComponentException extends RuntimeException {
        public MissingComponentException(String str) {
            super(str);
        }
    }

    /* loaded from: classes.dex */
    public static final class NoImageHeaderParserException extends MissingComponentException {
        public NoImageHeaderParserException() {
            super("Failed to find image header parser.");
        }
    }

    /* loaded from: classes.dex */
    public static class NoModelLoaderAvailableException extends MissingComponentException {
        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public NoModelLoaderAvailableException(java.lang.Object r3) {
            /*
                r2 = this;
                java.lang.String r3 = java.lang.String.valueOf(r3)
                int r0 = r3.length()
                int r0 = r0 + 43
                java.lang.String r1 = "Failed to find any ModelLoaders for model: "
                java.lang.String r3 = com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(r0, r1, r3)
                r2.<init>(r3)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.Registry.NoModelLoaderAvailableException.<init>(java.lang.Object):void");
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public NoModelLoaderAvailableException(java.lang.Class<?> r4, java.lang.Class<?> r5) {
            /*
                r3 = this;
                java.lang.String r4 = java.lang.String.valueOf(r4)
                java.lang.String r5 = java.lang.String.valueOf(r5)
                int r0 = r4.length()
                int r0 = r0 + 54
                int r1 = r5.length()
                int r1 = r1 + r0
                java.lang.String r0 = "Failed to find any ModelLoaders for model: "
                java.lang.String r2 = " and data: "
                java.lang.String r4 = com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1.m(r1, r0, r4, r2, r5)
                r3.<init>(r4)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.Registry.NoModelLoaderAvailableException.<init>(java.lang.Class, java.lang.Class):void");
        }
    }

    /* loaded from: classes.dex */
    public static class NoResultEncoderAvailableException extends MissingComponentException {
        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public NoResultEncoderAvailableException(java.lang.Class<?> r4) {
            /*
                r3 = this;
                java.lang.String r4 = java.lang.String.valueOf(r4)
                int r0 = r4.length()
                int r0 = r0 + 227
                java.lang.String r1 = "Failed to find result encoder for resource class: "
                java.lang.String r2 = ", you may need to consider registering a new Encoder for the requested type or DiskCacheStrategy.DATA/DiskCacheStrategy.NONE if caching your transformed resource is unnecessary."
                java.lang.String r4 = androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0.m(r0, r1, r4, r2)
                r3.<init>(r4)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.Registry.NoResultEncoderAvailableException.<init>(java.lang.Class):void");
        }
    }

    /* loaded from: classes.dex */
    public static class NoSourceEncoderAvailableException extends MissingComponentException {
        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public NoSourceEncoderAvailableException(java.lang.Class<?> r3) {
            /*
                r2 = this;
                java.lang.String r3 = java.lang.String.valueOf(r3)
                int r0 = r3.length()
                int r0 = r0 + 46
                java.lang.String r1 = "Failed to find source encoder for data class: "
                java.lang.String r3 = com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(r0, r1, r3)
                r2.<init>(r3)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.Registry.NoSourceEncoderAvailableException.<init>(java.lang.Class):void");
        }
    }

    public Registry() {
        FactoryPools.FactoryPool factoryPool = new FactoryPools.FactoryPool(new Pools$SynchronizedPool(20), new FactoryPools.Factory<List<?>>() { // from class: com.bumptech.glide.util.pool.FactoryPools.2
            /* Return type fixed from 'java.lang.Object' to match base method */
            @Override // com.bumptech.glide.util.pool.FactoryPools.Factory
            public List<?> create() {
                return new ArrayList();
            }
        }, new FactoryPools.Resetter<List<?>>() { // from class: com.bumptech.glide.util.pool.FactoryPools.3
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // com.bumptech.glide.util.pool.FactoryPools.Resetter
            public void reset(List<?> list) {
                list.clear();
            }
        });
        this.throwableListPool = factoryPool;
        this.modelLoaderRegistry = new ModelLoaderRegistry(factoryPool);
        ResourceDecoderRegistry resourceDecoderRegistry = new ResourceDecoderRegistry();
        this.decoderRegistry = resourceDecoderRegistry;
        List asList = Arrays.asList("Gif", "Bitmap", "BitmapDrawable");
        ArrayList arrayList = new ArrayList(asList.size());
        arrayList.addAll(asList);
        arrayList.add(0, "legacy_prepend_all");
        arrayList.add("legacy_append");
        synchronized (resourceDecoderRegistry) {
            ArrayList arrayList2 = new ArrayList(resourceDecoderRegistry.bucketPriorityList);
            resourceDecoderRegistry.bucketPriorityList.clear();
            resourceDecoderRegistry.bucketPriorityList.addAll(arrayList);
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (!arrayList.contains(str)) {
                    resourceDecoderRegistry.bucketPriorityList.add(str);
                }
            }
        }
    }

    public <Data> Registry append(Class<Data> cls, Encoder<Data> encoder) {
        EncoderRegistry encoderRegistry = this.encoderRegistry;
        synchronized (encoderRegistry) {
            encoderRegistry.encoders.add(new EncoderRegistry.Entry<>(cls, encoder));
        }
        return this;
    }

    public List<ImageHeaderParser> getImageHeaderParsers() {
        List<ImageHeaderParser> list;
        ImageHeaderParserRegistry imageHeaderParserRegistry = this.imageHeaderParserRegistry;
        synchronized (imageHeaderParserRegistry) {
            list = imageHeaderParserRegistry.parsers;
        }
        if (!list.isEmpty()) {
            return list;
        }
        throw new NoImageHeaderParserException();
    }

    public <Model> List<ModelLoader<Model, ?>> getModelLoaders(Model model) {
        List<ModelLoader<?, ?>> list;
        ModelLoaderRegistry modelLoaderRegistry = this.modelLoaderRegistry;
        Objects.requireNonNull(modelLoaderRegistry);
        Class<?> cls = model.getClass();
        synchronized (modelLoaderRegistry) {
            ModelLoaderRegistry.ModelLoaderCache.Entry<?> entry = modelLoaderRegistry.cache.cachedModelLoaders.get(cls);
            if (entry == null) {
                list = null;
            } else {
                list = entry.loaders;
            }
            if (list == null) {
                list = Collections.unmodifiableList(modelLoaderRegistry.multiModelLoaderFactory.build(cls));
                modelLoaderRegistry.cache.put(cls, list);
            }
        }
        int size = list.size();
        List<ModelLoader<Model, ?>> emptyList = Collections.emptyList();
        boolean z = true;
        for (int i = 0; i < size; i++) {
            ModelLoader<?, ?> modelLoader = list.get(i);
            if (modelLoader.handles(model)) {
                if (z) {
                    emptyList = new ArrayList<>(size - i);
                    z = false;
                }
                emptyList.add(modelLoader);
            }
        }
        if (!emptyList.isEmpty()) {
            return emptyList;
        }
        throw new NoModelLoaderAvailableException(model);
    }

    public Registry register(DataRewinder.Factory<?> factory) {
        DataRewinderRegistry dataRewinderRegistry = this.dataRewinderRegistry;
        synchronized (dataRewinderRegistry) {
            dataRewinderRegistry.rewinders.put(factory.getDataClass(), factory);
        }
        return this;
    }

    public <Data, TResource> Registry append(String str, Class<Data> cls, Class<TResource> cls2, ResourceDecoder<Data, TResource> resourceDecoder) {
        ResourceDecoderRegistry resourceDecoderRegistry = this.decoderRegistry;
        synchronized (resourceDecoderRegistry) {
            resourceDecoderRegistry.getOrAddEntryList(str).add(new ResourceDecoderRegistry.Entry<>(cls, cls2, resourceDecoder));
        }
        return this;
    }

    public <TResource, Transcode> Registry register(Class<TResource> cls, Class<Transcode> cls2, ResourceTranscoder<TResource, Transcode> resourceTranscoder) {
        TranscoderRegistry transcoderRegistry = this.transcoderRegistry;
        synchronized (transcoderRegistry) {
            transcoderRegistry.transcoders.add(new TranscoderRegistry.Entry<>(cls, cls2, resourceTranscoder));
        }
        return this;
    }

    public <TResource> Registry append(Class<TResource> cls, ResourceEncoder<TResource> resourceEncoder) {
        ResourceEncoderRegistry resourceEncoderRegistry = this.resourceEncoderRegistry;
        synchronized (resourceEncoderRegistry) {
            resourceEncoderRegistry.encoders.add(new ResourceEncoderRegistry.Entry<>(cls, resourceEncoder));
        }
        return this;
    }

    public <Model, Data> Registry append(Class<Model> cls, Class<Data> cls2, ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        ModelLoaderRegistry modelLoaderRegistry = this.modelLoaderRegistry;
        synchronized (modelLoaderRegistry) {
            MultiModelLoaderFactory multiModelLoaderFactory = modelLoaderRegistry.multiModelLoaderFactory;
            synchronized (multiModelLoaderFactory) {
                MultiModelLoaderFactory.Entry<?, ?> entry = new MultiModelLoaderFactory.Entry<>(cls, cls2, modelLoaderFactory);
                List<MultiModelLoaderFactory.Entry<?, ?>> list = multiModelLoaderFactory.entries;
                list.add(list.size(), entry);
            }
            modelLoaderRegistry.cache.cachedModelLoaders.clear();
        }
        return this;
    }
}
