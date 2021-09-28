package com.adobe.xmp.impl;

import java.io.PushbackReader;
import java.io.Reader;
/* loaded from: classes.dex */
public class FixASCIIControlsReader extends PushbackReader {
    public int state = 0;
    public int control = 0;
    public int digits = 0;

    public FixASCIIControlsReader(Reader reader) {
        super(reader, 8);
    }

    /* JADX WARNING: Removed duplicated region for block: B:76:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x00eb A[SYNTHETIC] */
    @Override // java.io.PushbackReader, java.io.FilterReader, java.io.Reader
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int read(char[] r17, int r18, int r19) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 282
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adobe.xmp.impl.FixASCIIControlsReader.read(char[], int, int):int");
    }
}
