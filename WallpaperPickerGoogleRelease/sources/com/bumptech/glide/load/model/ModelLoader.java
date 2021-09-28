package com.bumptech.glide.load.model;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public interface ModelLoader<Model, Data> {

    /* loaded from: classes.dex */
    public static class LoadData<Data> {
        public final List<Key> alternateKeys;
        public final DataFetcher<Data> fetcher;
        public final Key sourceKey;

        public LoadData(Key key, DataFetcher<Data> dataFetcher) {
            List<Key> emptyList = Collections.emptyList();
            Objects.requireNonNull(key, "Argument must not be null");
            this.sourceKey = key;
            Objects.requireNonNull(emptyList, "Argument must not be null");
            this.alternateKeys = emptyList;
            Objects.requireNonNull(dataFetcher, "Argument must not be null");
            this.fetcher = dataFetcher;
        }
    }

    LoadData<Data> buildLoadData(Model model, int i, int i2, Options options);

    boolean handles(Model model);
}
