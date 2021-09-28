package com.google.android.libraries.microvideo;

import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.impl.XMPMetaImpl;
import com.google.android.libraries.microvideo.MicrovideoXmpMetadata;
/* loaded from: classes.dex */
public final /* synthetic */ class MicrovideoXmpMetadata$$Lambda$6 implements MicrovideoXmpMetadata.ThrowableSupplier {
    public final /* synthetic */ int $r8$classId;
    public final XMPMeta arg$1;

    public MicrovideoXmpMetadata$$Lambda$6(XMPMeta xMPMeta, int i) {
        this.$r8$classId = i;
        if (i == 1) {
            this.arg$1 = xMPMeta;
        } else if (i != 2) {
            this.arg$1 = xMPMeta;
        } else {
            this.arg$1 = xMPMeta;
        }
    }

    @Override // com.google.android.libraries.microvideo.MicrovideoXmpMetadata.ThrowableSupplier
    public Object get() {
        switch (this.$r8$classId) {
            case 0:
                Integer propertyInteger = ((XMPMetaImpl) this.arg$1).getPropertyInteger("http://ns.google.com/photos/1.0/camera/", "MotionPhoto");
                if (propertyInteger == null || propertyInteger.intValue() <= 0) {
                    return null;
                }
                return 2;
            case 1:
                return ((XMPMetaImpl) this.arg$1).getPropertyInteger("http://ns.google.com/photos/1.0/camera/", "MicroVideoOffset");
            default:
                Integer propertyInteger2 = ((XMPMetaImpl) this.arg$1).getPropertyInteger("http://ns.google.com/photos/1.0/camera/", "MicroVideo");
                if (propertyInteger2 == null || propertyInteger2.intValue() <= 0) {
                    return null;
                }
                return 1;
        }
    }
}
