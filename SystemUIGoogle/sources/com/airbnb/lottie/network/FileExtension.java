package com.airbnb.lottie.network;
/* loaded from: classes.dex */
public enum FileExtension {
    JSON(".json"),
    ZIP(".zip");
    
    public final String extension;

    FileExtension(String str) {
        this.extension = str;
    }

    public String tempExtension() {
        return ".temp" + this.extension;
    }

    @Override // java.lang.Enum, java.lang.Object
    public String toString() {
        return this.extension;
    }
}
