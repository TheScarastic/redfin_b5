package com.bumptech.glide.load.engine;

import android.util.Log;
import androidx.core.util.Pools$Pool;
import androidx.preference.R$string$$ExternalSyntheticOutline0;
import com.bumptech.glide.Registry;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.DecodeJob;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class DecodePath<DataType, ResourceType, Transcode> {
    public final Class<DataType> dataClass;
    public final List<? extends ResourceDecoder<DataType, ResourceType>> decoders;
    public final String failureMessage;
    public final Pools$Pool<List<Throwable>> listPool;
    public final ResourceTranscoder<ResourceType, Transcode> transcoder;

    /* loaded from: classes.dex */
    public interface DecodeCallback<ResourceType> {
    }

    public DecodePath(Class<DataType> cls, Class<ResourceType> cls2, Class<Transcode> cls3, List<? extends ResourceDecoder<DataType, ResourceType>> list, ResourceTranscoder<ResourceType, Transcode> resourceTranscoder, Pools$Pool<List<Throwable>> pools$Pool) {
        this.dataClass = cls;
        this.decoders = list;
        this.transcoder = resourceTranscoder;
        this.listPool = pools$Pool;
        String simpleName = cls.getSimpleName();
        String simpleName2 = cls2.getSimpleName();
        String simpleName3 = cls3.getSimpleName();
        StringBuilder m = R$string$$ExternalSyntheticOutline0.m(simpleName3.length() + simpleName2.length() + simpleName.length() + 23, "Failed DecodePath{", simpleName, "->", simpleName2);
        m.append("->");
        m.append(simpleName3);
        m.append("}");
        this.failureMessage = m.toString();
    }

    /* JADX INFO: finally extract failed */
    public Resource<Transcode> decode(DataRewinder<DataType> dataRewinder, int i, int i2, Options options, DecodeCallback<ResourceType> decodeCallback) throws GlideException {
        Resource<ResourceType> resource;
        Transformation transformation;
        EncodeStrategy encodeStrategy;
        Key key;
        List<Throwable> acquire = this.listPool.acquire();
        Objects.requireNonNull(acquire, "Argument must not be null");
        try {
            Resource<ResourceType> decodeResourceWithList = decodeResourceWithList(dataRewinder, i, i2, options, acquire);
            this.listPool.release(acquire);
            DecodeJob.DecodeCallback decodeCallback2 = (DecodeJob.DecodeCallback) decodeCallback;
            DecodeJob decodeJob = DecodeJob.this;
            DataSource dataSource = decodeCallback2.dataSource;
            Objects.requireNonNull(decodeJob);
            Class<?> cls = decodeResourceWithList.get().getClass();
            ResourceEncoder resourceEncoder = null;
            if (dataSource != DataSource.RESOURCE_DISK_CACHE) {
                Transformation transformation2 = decodeJob.decodeHelper.getTransformation(cls);
                transformation = transformation2;
                resource = transformation2.transform(decodeJob.glideContext, decodeResourceWithList, decodeJob.width, decodeJob.height);
            } else {
                resource = decodeResourceWithList;
                transformation = null;
            }
            if (!decodeResourceWithList.equals(resource)) {
                decodeResourceWithList.recycle();
            }
            boolean z = false;
            if (decodeJob.decodeHelper.glideContext.registry.resourceEncoderRegistry.get(resource.getResourceClass()) != null) {
                ResourceEncoder resourceEncoder2 = decodeJob.decodeHelper.glideContext.registry.resourceEncoderRegistry.get(resource.getResourceClass());
                if (resourceEncoder2 != null) {
                    encodeStrategy = resourceEncoder2.getEncodeStrategy(decodeJob.options);
                    resourceEncoder = resourceEncoder2;
                } else {
                    throw new Registry.NoResultEncoderAvailableException(resource.getResourceClass());
                }
            } else {
                encodeStrategy = EncodeStrategy.NONE;
            }
            DecodeHelper<R> decodeHelper = decodeJob.decodeHelper;
            Key key2 = decodeJob.currentSourceKey;
            List<ModelLoader.LoadData<?>> loadData = decodeHelper.getLoadData();
            int size = loadData.size();
            int i3 = 0;
            while (true) {
                if (i3 >= size) {
                    break;
                } else if (loadData.get(i3).sourceKey.equals(key2)) {
                    z = true;
                    break;
                } else {
                    i3++;
                }
            }
            Resource<ResourceType> resource2 = resource;
            if (decodeJob.diskCacheStrategy.isResourceCacheable(!z, dataSource, encodeStrategy)) {
                if (resourceEncoder != null) {
                    int ordinal = encodeStrategy.ordinal();
                    if (ordinal == 0) {
                        key = new DataCacheKey(decodeJob.currentSourceKey, decodeJob.signature);
                    } else if (ordinal == 1) {
                        key = new ResourceCacheKey(decodeJob.decodeHelper.glideContext.arrayPool, decodeJob.currentSourceKey, decodeJob.signature, decodeJob.width, decodeJob.height, transformation, cls, decodeJob.options);
                    } else {
                        String valueOf = String.valueOf(encodeStrategy);
                        throw new IllegalArgumentException(Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(valueOf.length() + 18, "Unknown strategy: ", valueOf));
                    }
                    LockedResource<Z> obtain = LockedResource.obtain(resource);
                    DecodeJob.DeferredEncodeManager<?> deferredEncodeManager = decodeJob.deferredEncodeManager;
                    deferredEncodeManager.key = key;
                    deferredEncodeManager.encoder = resourceEncoder;
                    deferredEncodeManager.toEncode = obtain;
                    resource2 = obtain;
                } else {
                    throw new Registry.NoResultEncoderAvailableException(resource.get().getClass());
                }
            }
            return this.transcoder.transcode(resource2 == 1 ? 1 : 0, options);
        } catch (Throwable th) {
            this.listPool.release(acquire);
            throw th;
        }
    }

    public final Resource<ResourceType> decodeResourceWithList(DataRewinder<DataType> dataRewinder, int i, int i2, Options options, List<Throwable> list) throws GlideException {
        int size = this.decoders.size();
        Resource<ResourceType> resource = null;
        for (int i3 = 0; i3 < size; i3++) {
            ResourceDecoder resourceDecoder = (ResourceDecoder) this.decoders.get(i3);
            try {
                if (resourceDecoder.handles(dataRewinder.rewindAndGet(), options)) {
                    resource = resourceDecoder.decode(dataRewinder.rewindAndGet(), i, i2, options);
                }
            } catch (IOException | OutOfMemoryError | RuntimeException e) {
                if (Log.isLoggable("DecodePath", 2)) {
                    String valueOf = String.valueOf(resourceDecoder);
                    StringBuilder sb = new StringBuilder(valueOf.length() + 26);
                    sb.append("Failed to decode data for ");
                    sb.append(valueOf);
                    Log.v("DecodePath", sb.toString(), e);
                }
                list.add(e);
            }
            if (resource != null) {
                break;
            }
        }
        if (resource != null) {
            return resource;
        }
        throw new GlideException(this.failureMessage, new ArrayList(list));
    }

    public String toString() {
        String valueOf = String.valueOf(this.dataClass);
        String valueOf2 = String.valueOf(this.decoders);
        String valueOf3 = String.valueOf(this.transcoder);
        StringBuilder m = R$string$$ExternalSyntheticOutline0.m(valueOf3.length() + valueOf2.length() + valueOf.length() + 47, "DecodePath{ dataClass=", valueOf, ", decoders=", valueOf2);
        m.append(", transcoder=");
        m.append(valueOf3);
        m.append('}');
        return m.toString();
    }
}
