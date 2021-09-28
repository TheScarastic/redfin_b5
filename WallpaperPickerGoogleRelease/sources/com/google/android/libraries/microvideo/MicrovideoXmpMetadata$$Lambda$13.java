package com.google.android.libraries.microvideo;

import com.google.android.libraries.microvideo.MicrovideoXmpMetadata;
/* loaded from: classes.dex */
public final /* synthetic */ class MicrovideoXmpMetadata$$Lambda$13 implements MicrovideoXmpMetadata.ThrowableSupplier {
    public static final MicrovideoXmpMetadata.ThrowableSupplier $instance = new MicrovideoXmpMetadata$$Lambda$13();

    @Override // com.google.android.libraries.microvideo.MicrovideoXmpMetadata.ThrowableSupplier
    public Object get() {
        MicrovideoXmpMetadata.requiredValueMissing("payload length");
        throw null;
    }
}
