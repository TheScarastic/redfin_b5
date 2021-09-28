package com.bumptech.glide.load.resource.file;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.File;
import java.io.IOException;
/* loaded from: classes.dex */
public class FileDecoder implements ResourceDecoder<File, File> {
    /* Return type fixed from 'com.bumptech.glide.load.engine.Resource' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, int, int, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.ResourceDecoder
    public Resource<File> decode(File file, int i, int i2, Options options) throws IOException {
        return new FileResource(file);
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.ResourceDecoder
    public /* bridge */ /* synthetic */ boolean handles(File file, Options options) throws IOException {
        return true;
    }
}
