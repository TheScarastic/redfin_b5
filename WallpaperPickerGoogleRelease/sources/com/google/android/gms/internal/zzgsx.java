package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Objects;
/* loaded from: classes.dex */
public final class zzgsx extends zzgrt<zzgsx> implements Cloneable {
    public zzgsx() {
        this.zzay = null;
        this.zzaz = -1;
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final Object clone() throws CloneNotSupportedException {
        try {
            return (zzgsx) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final int computeSerializedSize() {
        super.computeSerializedSize();
        return 0;
    }

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgsx)) {
            return false;
        }
        zzgsx zzgsx = (zzgsx) obj;
        Objects.requireNonNull(zzgsx);
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null && !zzgrv.zzb()) {
            return this.zzay.equals(zzgsx.zzay);
        }
        zzgrv zzgrv2 = zzgsx.zzay;
        return zzgrv2 == null || zzgrv2.zzb();
    }

    @Override // java.lang.Object
    public final int hashCode() {
        zzgrv zzgrv = this.zzay;
        return -1637121971 + ((zzgrv == null || zzgrv.zzb()) ? 0 : this.zzay.hashCode());
    }

    @Override // com.google.android.gms.internal.zzgrt, com.google.android.gms.internal.zzgrz
    public final void writeTo(zzgrr zzgrr) throws IOException {
        super.writeTo(zzgrr);
    }
}
