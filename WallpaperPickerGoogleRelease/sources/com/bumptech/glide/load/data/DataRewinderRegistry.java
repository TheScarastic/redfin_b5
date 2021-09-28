package com.bumptech.glide.load.data;

import com.bumptech.glide.load.data.DataRewinder;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class DataRewinderRegistry {
    public static final DataRewinder.Factory<?> DEFAULT_FACTORY = new DataRewinder.Factory<Object>() { // from class: com.bumptech.glide.load.data.DataRewinderRegistry.1
        @Override // com.bumptech.glide.load.data.DataRewinder.Factory
        public DataRewinder<Object> build(Object obj) {
            return new DefaultRewinder(obj);
        }

        @Override // com.bumptech.glide.load.data.DataRewinder.Factory
        public Class<Object> getDataClass() {
            throw new UnsupportedOperationException("Not implemented");
        }
    };
    public final Map<Class<?>, DataRewinder.Factory<?>> rewinders = new HashMap();

    /* loaded from: classes.dex */
    public static final class DefaultRewinder implements DataRewinder<Object> {
        public final Object data;

        public DefaultRewinder(Object obj) {
            this.data = obj;
        }

        @Override // com.bumptech.glide.load.data.DataRewinder
        public void cleanup() {
        }

        @Override // com.bumptech.glide.load.data.DataRewinder
        public Object rewindAndGet() {
            return this.data;
        }
    }
}
