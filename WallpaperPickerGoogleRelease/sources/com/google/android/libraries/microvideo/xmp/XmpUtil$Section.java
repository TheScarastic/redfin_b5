package com.google.android.libraries.microvideo.xmp;

import com.google.common.base.Preconditions;
/* loaded from: classes.dex */
public class XmpUtil$Section {
    public final byte[] data;
    public final int length;
    public final int offset;

    public XmpUtil$Section(byte[] bArr, int i, int i2, int i3) {
        boolean z = true;
        Preconditions.checkArgument(i2 >= 0, "offset must be >= 0");
        Preconditions.checkArgument(i3 > 0, "length must be > 0");
        Preconditions.checkArgument(i3 > bArr.length ? false : z, "length exceeds data length");
        this.data = bArr;
        this.offset = i2;
        this.length = i3;
    }
}
