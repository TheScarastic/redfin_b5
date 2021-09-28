package com.bumptech.glide.load.engine.cache;

import androidx.core.util.Pools$Pool;
import androidx.core.util.Pools$SynchronizedPool;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
/* loaded from: classes.dex */
public class SafeKeyGenerator {
    public final LruCache<Key, String> loadIdToSafeHash = new LruCache<>(1000);
    public final Pools$Pool<PoolableDigestContainer> digestPool = new FactoryPools.FactoryPool(new Pools$SynchronizedPool(10), new FactoryPools.Factory<PoolableDigestContainer>() { // from class: com.bumptech.glide.load.engine.cache.SafeKeyGenerator.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // com.bumptech.glide.util.pool.FactoryPools.Factory
        public PoolableDigestContainer create() {
            try {
                return new PoolableDigestContainer(MessageDigest.getInstance("SHA-256"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }, FactoryPools.EMPTY_RESETTER);

    /* loaded from: classes.dex */
    public static final class PoolableDigestContainer implements FactoryPools.Poolable {
        public final MessageDigest messageDigest;
        public final StateVerifier stateVerifier = new StateVerifier.DefaultStateVerifier();

        public PoolableDigestContainer(MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
        }

        @Override // com.bumptech.glide.util.pool.FactoryPools.Poolable
        public StateVerifier getVerifier() {
            return this.stateVerifier;
        }
    }

    public String getSafeKey(Key key) {
        String str;
        synchronized (this.loadIdToSafeHash) {
            str = this.loadIdToSafeHash.get(key);
        }
        if (str == null) {
            PoolableDigestContainer acquire = this.digestPool.acquire();
            Objects.requireNonNull(acquire, "Argument must not be null");
            try {
                key.updateDiskCacheKey(acquire.messageDigest);
                byte[] digest = acquire.messageDigest.digest();
                char[] cArr = Util.SHA_256_CHARS;
                synchronized (cArr) {
                    for (int i = 0; i < digest.length; i++) {
                        int i2 = digest[i] & 255;
                        int i3 = i * 2;
                        char[] cArr2 = Util.HEX_CHAR_ARRAY;
                        cArr[i3] = cArr2[i2 >>> 4];
                        cArr[i3 + 1] = cArr2[i2 & 15];
                    }
                    str = new String(cArr);
                }
            } finally {
                this.digestPool.release(acquire);
            }
        }
        synchronized (this.loadIdToSafeHash) {
            this.loadIdToSafeHash.put(key, str);
        }
        return str;
    }
}
