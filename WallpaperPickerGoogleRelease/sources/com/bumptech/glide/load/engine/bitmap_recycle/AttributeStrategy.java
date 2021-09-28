package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentTabHost$SavedState$$ExternalSyntheticOutline0;
import com.bumptech.glide.gifdecoder.GifHeaderParser$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public class AttributeStrategy implements LruPoolStrategy {

    /* loaded from: classes.dex */
    public static class Key implements Poolable {
        public Bitmap.Config config;
        public int height;
        public final KeyPool pool;
        public int width;

        public Key(KeyPool keyPool) {
            this.pool = keyPool;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            Key key = (Key) obj;
            if (this.width == key.width && this.height == key.height && this.config == key.config) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int i = ((this.width * 31) + this.height) * 31;
            Bitmap.Config config = this.config;
            return i + (config != null ? config.hashCode() : 0);
        }

        @Override // com.bumptech.glide.load.engine.bitmap_recycle.Poolable
        public void offer() {
            this.pool.offer(this);
        }

        public String toString() {
            return AttributeStrategy.getBitmapString(this.width, this.height, this.config);
        }
    }

    /* loaded from: classes.dex */
    public static class KeyPool extends BaseKeyPool<Key> {
        /* Return type fixed from 'com.bumptech.glide.load.engine.bitmap_recycle.Poolable' to match base method */
        @Override // com.bumptech.glide.load.engine.bitmap_recycle.BaseKeyPool
        public Key create() {
            return new Key(this);
        }
    }

    public static String getBitmapString(int i, int i2, Bitmap.Config config) {
        String valueOf = String.valueOf(config);
        return FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(GifHeaderParser$$ExternalSyntheticOutline0.m(valueOf.length() + 27, "[", i, "x", i2), "], ", valueOf);
    }
}
