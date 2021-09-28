package com.bumptech.glide.load.model;

import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import java.util.ArrayDeque;
import java.util.Queue;
/* loaded from: classes.dex */
public class ModelCache<A, B> {
    public final LruCache<ModelKey<A>, B> cache;

    /* loaded from: classes.dex */
    public static final class ModelKey<A> {
        public static final Queue<ModelKey<?>> KEY_QUEUE = new ArrayDeque(0);
        public int height;
        public A model;
        public int width;

        static {
            char[] cArr = Util.HEX_CHAR_ARRAY;
        }

        private ModelKey() {
        }

        public static <A> ModelKey<A> get(A a, int i, int i2) {
            ModelKey<A> modelKey;
            Queue<ModelKey<?>> queue = KEY_QUEUE;
            synchronized (queue) {
                modelKey = (ModelKey) ((ArrayDeque) queue).poll();
            }
            if (modelKey == null) {
                modelKey = new ModelKey<>();
            }
            modelKey.model = a;
            modelKey.width = i;
            modelKey.height = i2;
            return modelKey;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ModelKey)) {
                return false;
            }
            ModelKey modelKey = (ModelKey) obj;
            if (this.width == modelKey.width && this.height == modelKey.height && this.model.equals(modelKey.model)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.model.hashCode() + (((this.height * 31) + this.width) * 31);
        }

        public void release() {
            Queue<ModelKey<?>> queue = KEY_QUEUE;
            synchronized (queue) {
                ((ArrayDeque) queue).offer(this);
            }
        }
    }

    public ModelCache(long j) {
        this.cache = new LruCache<ModelKey<A>, B>(j) { // from class: com.bumptech.glide.load.model.ModelCache.1
            @Override // com.bumptech.glide.util.LruCache
            public void onItemEvicted(Object obj, Object obj2) {
                ((ModelKey) obj).release();
            }
        };
    }
}
