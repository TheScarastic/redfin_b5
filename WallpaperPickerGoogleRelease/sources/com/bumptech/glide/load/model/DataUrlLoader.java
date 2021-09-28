package com.bumptech.glide.load.model;

import android.util.Base64;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DataUrlLoader<Model, Data> implements ModelLoader<Model, Data> {
    public final DataDecoder<Data> dataDecoder;

    /* loaded from: classes.dex */
    public interface DataDecoder<Data> {
    }

    /* loaded from: classes.dex */
    public static final class DataUriFetcher<Data> implements DataFetcher<Data> {
        public Data data;
        public final String dataUri;
        public final DataDecoder<Data> reader;

        public DataUriFetcher(String str, DataDecoder<Data> dataDecoder) {
            this.dataUri = str;
            this.reader = dataDecoder;
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void cancel() {
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void cleanup() {
            try {
                DataDecoder<Data> dataDecoder = this.reader;
                Data data = this.data;
                Objects.requireNonNull((StreamFactory.AnonymousClass1) dataDecoder);
                ((InputStream) data).close();
            } catch (IOException unused) {
            }
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public Class<Data> getDataClass() {
            Objects.requireNonNull((StreamFactory.AnonymousClass1) this.reader);
            return InputStream.class;
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        /* JADX WARN: Type inference failed for: r2v3, types: [java.lang.Object, Data] */
        /* JADX WARNING: Unknown variable types count: 1 */
        @Override // com.bumptech.glide.load.data.DataFetcher
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void loadData(com.bumptech.glide.Priority r2, com.bumptech.glide.load.data.DataFetcher.DataCallback<? super Data> r3) {
            /*
                r1 = this;
                com.bumptech.glide.load.model.DataUrlLoader$DataDecoder<Data> r2 = r1.reader     // Catch: IllegalArgumentException -> 0x0010
                java.lang.String r0 = r1.dataUri     // Catch: IllegalArgumentException -> 0x0010
                com.bumptech.glide.load.model.DataUrlLoader$StreamFactory$1 r2 = (com.bumptech.glide.load.model.DataUrlLoader.StreamFactory.AnonymousClass1) r2     // Catch: IllegalArgumentException -> 0x0010
                java.lang.Object r2 = r2.decode(r0)     // Catch: IllegalArgumentException -> 0x0010
                r1.data = r2     // Catch: IllegalArgumentException -> 0x0010
                r3.onDataReady(r2)     // Catch: IllegalArgumentException -> 0x0010
                goto L_0x0014
            L_0x0010:
                r1 = move-exception
                r3.onLoadFailed(r1)
            L_0x0014:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.model.DataUrlLoader.DataUriFetcher.loadData(com.bumptech.glide.Priority, com.bumptech.glide.load.data.DataFetcher$DataCallback):void");
        }
    }

    /* loaded from: classes.dex */
    public static final class StreamFactory<Model> implements ModelLoaderFactory<Model, InputStream> {
        public final DataDecoder<InputStream> opener = new DataDecoder<InputStream>() { // from class: com.bumptech.glide.load.model.DataUrlLoader.StreamFactory.1
            public Object decode(String str) throws IllegalArgumentException {
                if (str.startsWith("data:image")) {
                    int indexOf = str.indexOf(44);
                    if (indexOf == -1) {
                        throw new IllegalArgumentException("Missing comma in data URL.");
                    } else if (str.substring(0, indexOf).endsWith(";base64")) {
                        return new ByteArrayInputStream(Base64.decode(str.substring(indexOf + 1), 0));
                    } else {
                        throw new IllegalArgumentException("Not a base64 image data URL.");
                    }
                } else {
                    throw new IllegalArgumentException("Not a valid image data URL.");
                }
            }
        };

        @Override // com.bumptech.glide.load.model.ModelLoaderFactory
        public ModelLoader<Model, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new DataUrlLoader(this.opener);
        }
    }

    public DataUrlLoader(DataDecoder<Data> dataDecoder) {
        this.dataDecoder = dataDecoder;
    }

    @Override // com.bumptech.glide.load.model.ModelLoader
    public ModelLoader.LoadData<Data> buildLoadData(Model model, int i, int i2, Options options) {
        return new ModelLoader.LoadData<>(new ObjectKey(model), new DataUriFetcher(model.toString(), this.dataDecoder));
    }

    @Override // com.bumptech.glide.load.model.ModelLoader
    public boolean handles(Model model) {
        return model.toString().startsWith("data:image");
    }
}
