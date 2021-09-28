package com.bumptech.glide.load.model;

import androidx.core.util.Pools$Pool;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public class MultiModelLoaderFactory {
    public static final Factory DEFAULT_FACTORY = new Factory();
    public static final ModelLoader<Object, Object> EMPTY_MODEL_LOADER = new EmptyModelLoader();
    public final Factory factory;
    public final Pools$Pool<List<Throwable>> throwableListPool;
    public final List<Entry<?, ?>> entries = new ArrayList();
    public final Set<Entry<?, ?>> alreadyUsedEntries = new HashSet();

    /* loaded from: classes.dex */
    public static class EmptyModelLoader implements ModelLoader<Object, Object> {
        @Override // com.bumptech.glide.load.model.ModelLoader
        public ModelLoader.LoadData<Object> buildLoadData(Object obj, int i, int i2, Options options) {
            return null;
        }

        @Override // com.bumptech.glide.load.model.ModelLoader
        public boolean handles(Object obj) {
            return false;
        }
    }

    /* loaded from: classes.dex */
    public static class Entry<Model, Data> {
        public final Class<Data> dataClass;
        public final ModelLoaderFactory<? extends Model, ? extends Data> factory;
        public final Class<Model> modelClass;

        public Entry(Class<Model> cls, Class<Data> cls2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
            this.modelClass = cls;
            this.dataClass = cls2;
            this.factory = modelLoaderFactory;
        }
    }

    /* loaded from: classes.dex */
    public static class Factory {
    }

    public MultiModelLoaderFactory(Pools$Pool<List<Throwable>> pools$Pool, Factory factory) {
        this.throwableListPool = pools$Pool;
        this.factory = factory;
    }

    public synchronized <Model> List<ModelLoader<Model, ?>> build(Class<Model> cls) {
        ArrayList arrayList;
        try {
            arrayList = new ArrayList();
            for (Entry<?, ?> entry : this.entries) {
                if (!this.alreadyUsedEntries.contains(entry) && entry.modelClass.isAssignableFrom(cls)) {
                    this.alreadyUsedEntries.add(entry);
                    ModelLoader<? extends Object, ? extends Object> build = entry.factory.build(this);
                    Objects.requireNonNull(build, "Argument must not be null");
                    arrayList.add(build);
                    this.alreadyUsedEntries.remove(entry);
                }
            }
        } catch (Throwable th) {
            try {
                this.alreadyUsedEntries.clear();
                throw th;
            } catch (Throwable th2) {
                throw th2;
            }
        }
        return arrayList;
    }

    public synchronized List<Class<?>> getDataClasses(Class<?> cls) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (Entry<?, ?> entry : this.entries) {
            if (!arrayList.contains(entry.dataClass) && entry.modelClass.isAssignableFrom(cls)) {
                arrayList.add(entry.dataClass);
            }
        }
        return arrayList;
    }

    public synchronized <Model, Data> ModelLoader<Model, Data> build(Class<Model> cls, Class<Data> cls2) {
        try {
            ArrayList arrayList = new ArrayList();
            boolean z = false;
            for (Entry<?, ?> entry : this.entries) {
                if (this.alreadyUsedEntries.contains(entry)) {
                    z = true;
                } else if (entry.modelClass.isAssignableFrom(cls) && entry.dataClass.isAssignableFrom(cls2)) {
                    this.alreadyUsedEntries.add(entry);
                    arrayList.add(build(entry));
                    this.alreadyUsedEntries.remove(entry);
                }
            }
            if (arrayList.size() > 1) {
                Factory factory = this.factory;
                Pools$Pool<List<Throwable>> pools$Pool = this.throwableListPool;
                Objects.requireNonNull(factory);
                return new MultiModelLoader(arrayList, pools$Pool);
            } else if (arrayList.size() == 1) {
                return (ModelLoader) arrayList.get(0);
            } else if (z) {
                return (ModelLoader<Model, Data>) EMPTY_MODEL_LOADER;
            } else {
                throw new Registry.NoModelLoaderAvailableException(cls, cls2);
            }
        } catch (Throwable th) {
            try {
                this.alreadyUsedEntries.clear();
                throw th;
            } catch (Throwable th2) {
                throw th2;
            }
        }
    }

    public final <Model, Data> ModelLoader<Model, Data> build(Entry<?, ?> entry) {
        ModelLoader<Model, Data> modelLoader = (ModelLoader<Model, Data>) entry.factory.build(this);
        Objects.requireNonNull(modelLoader, "Argument must not be null");
        return modelLoader;
    }
}
