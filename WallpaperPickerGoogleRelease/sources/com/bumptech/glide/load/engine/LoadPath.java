package com.bumptech.glide.load.engine;

import androidx.core.util.Pools$Pool;
import androidx.preference.R$string$$ExternalSyntheticOutline0;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.DecodePath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class LoadPath<Data, ResourceType, Transcode> {
    public final List<? extends DecodePath<Data, ResourceType, Transcode>> decodePaths;
    public final String failureMessage;
    public final Pools$Pool<List<Throwable>> listPool;

    public LoadPath(Class<Data> cls, Class<ResourceType> cls2, Class<Transcode> cls3, List<DecodePath<Data, ResourceType, Transcode>> list, Pools$Pool<List<Throwable>> pools$Pool) {
        this.listPool = pools$Pool;
        if (!list.isEmpty()) {
            this.decodePaths = list;
            String simpleName = cls.getSimpleName();
            String simpleName2 = cls2.getSimpleName();
            String simpleName3 = cls3.getSimpleName();
            StringBuilder m = R$string$$ExternalSyntheticOutline0.m(simpleName3.length() + simpleName2.length() + simpleName.length() + 21, "Failed LoadPath{", simpleName, "->", simpleName2);
            m.append("->");
            m.append(simpleName3);
            m.append("}");
            this.failureMessage = m.toString();
            return;
        }
        throw new IllegalArgumentException("Must not be empty.");
    }

    public Resource<Transcode> load(DataRewinder<Data> dataRewinder, Options options, int i, int i2, DecodePath.DecodeCallback<ResourceType> decodeCallback) throws GlideException {
        List<Throwable> acquire = this.listPool.acquire();
        Objects.requireNonNull(acquire, "Argument must not be null");
        try {
            int size = this.decodePaths.size();
            Resource<Transcode> resource = null;
            for (int i3 = 0; i3 < size; i3++) {
                try {
                    resource = ((DecodePath) this.decodePaths.get(i3)).decode(dataRewinder, i, i2, options, decodeCallback);
                } catch (GlideException e) {
                    acquire.add(e);
                }
                if (resource != null) {
                    break;
                }
            }
            if (resource != null) {
                return resource;
            }
            throw new GlideException(this.failureMessage, new ArrayList(acquire));
        } finally {
            this.listPool.release(acquire);
        }
    }

    public String toString() {
        String arrays = Arrays.toString(this.decodePaths.toArray());
        StringBuilder sb = new StringBuilder(XMPPathFactory$$ExternalSyntheticOutline0.m(arrays, 22));
        sb.append("LoadPath{decodePaths=");
        sb.append(arrays);
        sb.append('}');
        return sb.toString();
    }
}
