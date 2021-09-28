package com.adobe.xmp.options;
/* loaded from: classes.dex */
public final class ParseOptions extends Options {
    public ParseOptions() {
        setOption(24, true);
    }

    public boolean getFixControlChars() {
        return getOption(8);
    }

    @Override // com.adobe.xmp.options.Options
    public int getValidOptions() {
        return 61;
    }
}
