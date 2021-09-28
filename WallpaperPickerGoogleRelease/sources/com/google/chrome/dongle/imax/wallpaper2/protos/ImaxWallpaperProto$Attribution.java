package com.google.chrome.dongle.imax.wallpaper2.protos;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.RawMessageInfo;
/* loaded from: classes.dex */
public final class ImaxWallpaperProto$Attribution extends GeneratedMessageLite<ImaxWallpaperProto$Attribution, Builder> implements MessageLiteOrBuilder {
    private static final ImaxWallpaperProto$Attribution DEFAULT_INSTANCE;
    private static volatile Parser<ImaxWallpaperProto$Attribution> PARSER = null;
    public static final int TEXT_FIELD_NUMBER = 1;
    private int bitField0_;
    private String text_ = "";

    /* loaded from: classes.dex */
    public static final class Builder extends GeneratedMessageLite.Builder<ImaxWallpaperProto$Attribution, Builder> implements MessageLiteOrBuilder {
        public Builder(ImaxWallpaperProto$1 imaxWallpaperProto$1) {
            super(ImaxWallpaperProto$Attribution.DEFAULT_INSTANCE);
        }
    }

    static {
        ImaxWallpaperProto$Attribution imaxWallpaperProto$Attribution = new ImaxWallpaperProto$Attribution();
        DEFAULT_INSTANCE = imaxWallpaperProto$Attribution;
        GeneratedMessageLite.registerDefaultInstance(ImaxWallpaperProto$Attribution.class, imaxWallpaperProto$Attribution);
    }

    @Override // com.google.protobuf.GeneratedMessageLite
    public final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
        switch (methodToInvoke.ordinal()) {
            case 0:
                return (byte) 1;
            case 1:
                return null;
            case 2:
                return new RawMessageInfo(DEFAULT_INSTANCE, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0000\u0000\u0000\u0001\b\u0000", new Object[]{"bitField0_", "text_"});
            case 3:
                return new ImaxWallpaperProto$Attribution();
            case 4:
                return new Builder(null);
            case 5:
                return DEFAULT_INSTANCE;
            case 6:
                Parser<ImaxWallpaperProto$Attribution> parser = PARSER;
                if (parser == null) {
                    synchronized (ImaxWallpaperProto$Attribution.class) {
                        parser = PARSER;
                        if (parser == null) {
                            parser = new GeneratedMessageLite.DefaultInstanceBasedParser<>(DEFAULT_INSTANCE);
                            PARSER = parser;
                        }
                    }
                }
                return parser;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public String getText() {
        return this.text_;
    }
}
