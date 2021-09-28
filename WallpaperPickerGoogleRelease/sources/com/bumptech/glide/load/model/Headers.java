package com.bumptech.glide.load.model;

import com.bumptech.glide.load.model.LazyHeaders;
import java.util.Map;
/* loaded from: classes.dex */
public interface Headers {
    public static final Headers DEFAULT = new LazyHeaders(new LazyHeaders.Builder().headers);

    Map<String, String> getHeaders();
}
