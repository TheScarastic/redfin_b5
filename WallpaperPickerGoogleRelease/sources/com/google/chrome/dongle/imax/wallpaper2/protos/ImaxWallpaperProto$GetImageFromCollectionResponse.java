package com.google.chrome.dongle.imax.wallpaper2.protos;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.RawMessageInfo;
/* loaded from: classes.dex */
public final class ImaxWallpaperProto$GetImageFromCollectionResponse extends GeneratedMessageLite<ImaxWallpaperProto$GetImageFromCollectionResponse, Builder> implements MessageLiteOrBuilder {
    private static final ImaxWallpaperProto$GetImageFromCollectionResponse DEFAULT_INSTANCE;
    public static final int IMAGE_FIELD_NUMBER = 1;
    private static volatile Parser<ImaxWallpaperProto$GetImageFromCollectionResponse> PARSER = null;
    public static final int RESUME_TOKEN_FIELD_NUMBER = 2;
    private int bitField0_;
    private ImaxWallpaperProto$Image image_;
    private String resumeToken_ = "";

    /* loaded from: classes.dex */
    public static final class Builder extends GeneratedMessageLite.Builder<ImaxWallpaperProto$GetImageFromCollectionResponse, Builder> implements MessageLiteOrBuilder {
        public Builder(ImaxWallpaperProto$1 imaxWallpaperProto$1) {
            super(ImaxWallpaperProto$GetImageFromCollectionResponse.DEFAULT_INSTANCE);
        }
    }

    static {
        ImaxWallpaperProto$GetImageFromCollectionResponse imaxWallpaperProto$GetImageFromCollectionResponse = new ImaxWallpaperProto$GetImageFromCollectionResponse();
        DEFAULT_INSTANCE = imaxWallpaperProto$GetImageFromCollectionResponse;
        GeneratedMessageLite.registerDefaultInstance(ImaxWallpaperProto$GetImageFromCollectionResponse.class, imaxWallpaperProto$GetImageFromCollectionResponse);
    }

    public static Parser<ImaxWallpaperProto$GetImageFromCollectionResponse> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }

    @Override // com.google.protobuf.GeneratedMessageLite
    public final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
        switch (methodToInvoke.ordinal()) {
            case 0:
                return (byte) 1;
            case 1:
                return null;
            case 2:
                return new RawMessageInfo(DEFAULT_INSTANCE, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001\t\u0000\u0002\b\u0001", new Object[]{"bitField0_", "image_", "resumeToken_"});
            case 3:
                return new ImaxWallpaperProto$GetImageFromCollectionResponse();
            case 4:
                return new Builder(null);
            case 5:
                return DEFAULT_INSTANCE;
            case 6:
                Parser<ImaxWallpaperProto$GetImageFromCollectionResponse> parser = PARSER;
                if (parser == null) {
                    synchronized (ImaxWallpaperProto$GetImageFromCollectionResponse.class) {
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

    public ImaxWallpaperProto$Image getImage() {
        ImaxWallpaperProto$Image imaxWallpaperProto$Image = this.image_;
        return imaxWallpaperProto$Image == null ? ImaxWallpaperProto$Image.getDefaultInstance() : imaxWallpaperProto$Image;
    }

    public String getResumeToken() {
        return this.resumeToken_;
    }
}
