package com.bumptech.glide.load.engine.bitmap_recycle;

import java.util.Objects;
/* loaded from: classes.dex */
public class SizeStrategy$KeyPool extends BaseKeyPool<SizeStrategy$Key> {
    /* Return type fixed from 'com.bumptech.glide.load.engine.bitmap_recycle.Poolable' to match base method */
    @Override // com.bumptech.glide.load.engine.bitmap_recycle.BaseKeyPool
    public SizeStrategy$Key create() {
        return new Poolable(this) { // from class: com.bumptech.glide.load.engine.bitmap_recycle.SizeStrategy$Key
            public final SizeStrategy$KeyPool pool;

            {
                this.pool = r1;
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof SizeStrategy$Key)) {
                    return false;
                }
                Objects.requireNonNull((SizeStrategy$Key) obj);
                return true;
            }

            public int hashCode() {
                return 0;
            }

            @Override // com.bumptech.glide.load.engine.bitmap_recycle.Poolable
            public void offer() {
                this.pool.offer(this);
            }

            public String toString() {
                return "[0]";
            }
        };
    }
}
