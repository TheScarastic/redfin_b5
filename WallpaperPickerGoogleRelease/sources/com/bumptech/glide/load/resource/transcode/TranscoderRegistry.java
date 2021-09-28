package com.bumptech.glide.load.resource.transcode;

import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class TranscoderRegistry {
    public final List<Entry<?, ?>> transcoders = new ArrayList();

    /* loaded from: classes.dex */
    public static final class Entry<Z, R> {
        public final Class<Z> fromClass;
        public final Class<R> toClass;
        public final ResourceTranscoder<Z, R> transcoder;

        public Entry(Class<Z> cls, Class<R> cls2, ResourceTranscoder<Z, R> resourceTranscoder) {
            this.fromClass = cls;
            this.toClass = cls2;
            this.transcoder = resourceTranscoder;
        }

        public boolean handles(Class<?> cls, Class<?> cls2) {
            return this.fromClass.isAssignableFrom(cls) && cls2.isAssignableFrom(this.toClass);
        }
    }

    public synchronized <Z, R> List<Class<R>> getTranscodeClasses(Class<Z> cls, Class<R> cls2) {
        ArrayList arrayList = new ArrayList();
        if (cls2.isAssignableFrom(cls)) {
            arrayList.add(cls2);
            return arrayList;
        }
        for (Entry<?, ?> entry : this.transcoders) {
            if (entry.handles(cls, cls2)) {
                arrayList.add(cls2);
            }
        }
        return arrayList;
    }
}
