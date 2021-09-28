package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DataFetcherGenerator;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.File;
import java.util.List;
/* loaded from: classes.dex */
public class DataCacheGenerator implements DataFetcherGenerator, DataFetcher.DataCallback<Object> {
    public File cacheFile;
    public final List<Key> cacheKeys;
    public final DataFetcherGenerator.FetcherReadyCallback cb;
    public final DecodeHelper<?> helper;
    public volatile ModelLoader.LoadData<?> loadData;
    public int modelLoaderIndex;
    public List<ModelLoader<File, ?>> modelLoaders;
    public int sourceIdIndex = -1;
    public Key sourceKey;

    public DataCacheGenerator(DecodeHelper<?> decodeHelper, DataFetcherGenerator.FetcherReadyCallback fetcherReadyCallback) {
        List<Key> cacheKeys = decodeHelper.getCacheKeys();
        this.cacheKeys = cacheKeys;
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
        this.cb.onDataFetcherReady(this.sourceKey, obj, this.loadData.fetcher, DataSource.DATA_DISK_CACHE, this.sourceKey);
    }

    @Override // com.bumptech.glide.load.data.DataFetcher.DataCallback
    public void onLoadFailed(Exception exc) {
        this.cb.onDataFetcherFailed(this.sourceKey, exc, this.loadData.fetcher, DataSource.DATA_DISK_CACHE);
    }

    @Override // com.bumptech.glide.load.engine.DataFetcherGenerator
    public boolean startNext() {
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
                        DecodeHelper<?> decodeHelper = this.helper;
                        this.loadData = list2.get(i).buildLoadData(file, decodeHelper.width, decodeHelper.height, decodeHelper.options);
                        if (this.loadData != null && this.helper.hasLoadPath(this.loadData.fetcher.getDataClass())) {
                            this.loadData.fetcher.loadData(this.helper.priority, this);
                            z = true;
                        }
                    }
                    return z;
                }
            }
            int i2 = this.sourceIdIndex + 1;
            this.sourceIdIndex = i2;
            if (i2 >= this.cacheKeys.size()) {
                return false;
            }
            Key key = this.cacheKeys.get(this.sourceIdIndex);
            DecodeHelper<?> decodeHelper2 = this.helper;
            File file2 = decodeHelper2.getDiskCache().get(new DataCacheKey(key, decodeHelper2.signature));
            this.cacheFile = file2;
            if (file2 != null) {
                this.sourceKey = key;
                this.modelLoaders = this.helper.glideContext.registry.getModelLoaders(file2);
                this.modelLoaderIndex = 0;
            }
        }
    }

    public DataCacheGenerator(List<Key> list, DecodeHelper<?> decodeHelper, DataFetcherGenerator.FetcherReadyCallback fetcherReadyCallback) {
        this.cacheKeys = list;
        this.helper = decodeHelper;
        this.cb = fetcherReadyCallback;
    }
}
