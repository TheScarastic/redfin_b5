package com.bumptech.glide.util.pool;
/* loaded from: classes.dex */
public abstract class StateVerifier {

    /* loaded from: classes.dex */
    public static class DefaultStateVerifier extends StateVerifier {
        public volatile boolean isReleased;

        public DefaultStateVerifier() {
            super(null);
        }

        @Override // com.bumptech.glide.util.pool.StateVerifier
        public void throwIfRecycled() {
            if (this.isReleased) {
                throw new IllegalStateException("Already released");
            }
        }
    }

    public StateVerifier(AnonymousClass1 r1) {
    }

    public abstract void throwIfRecycled();
}
