package com.adobe.xmp.options;

import com.adobe.xmp.XMPException;
/* loaded from: classes.dex */
public final class AliasOptions extends Options {
    public AliasOptions() {
    }

    @Override // com.adobe.xmp.options.Options
    public int getValidOptions() {
        return 7680;
    }

    public AliasOptions(int i) throws XMPException {
        super(i);
    }
}
