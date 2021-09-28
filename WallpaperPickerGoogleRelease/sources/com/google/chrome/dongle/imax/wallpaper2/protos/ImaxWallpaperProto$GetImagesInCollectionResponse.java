package com.google.chrome.dongle.imax.wallpaper2.protos;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.Internal;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtobufArrayList;
import com.google.protobuf.RawMessageInfo;
import java.util.List;
/* loaded from: classes.dex */
public final class ImaxWallpaperProto$GetImagesInCollectionResponse extends GeneratedMessageLite<ImaxWallpaperProto$GetImagesInCollectionResponse, Builder> implements MessageLiteOrBuilder {
    private static final ImaxWallpaperProto$GetImagesInCollectionResponse DEFAULT_INSTANCE;
    public static final int IMAGES_FIELD_NUMBER = 1;
    private static volatile Parser<ImaxWallpaperProto$GetImagesInCollectionResponse> PARSER;
    private Internal.ProtobufList<ImaxWallpaperProto$Image> images_ = ProtobufArrayList.EMPTY_LIST;

    /* loaded from: classes.dex */
    public static final class Builder extends GeneratedMessageLite.Builder<ImaxWallpaperProto$GetImagesInCollectionResponse, Builder> implements MessageLiteOrBuilder {
        public Builder(ImaxWallpaperProto$1 imaxWallpaperProto$1) {
            super(ImaxWallpaperProto$GetImagesInCollectionResponse.DEFAULT_INSTANCE);
        }
    }

    static {
        ImaxWallpaperProto$GetImagesInCollectionResponse imaxWallpaperProto$GetImagesInCollectionResponse = new ImaxWallpaperProto$GetImagesInCollectionResponse();
        DEFAULT_INSTANCE = imaxWallpaperProto$GetImagesInCollectionResponse;
        GeneratedMessageLite.registerDefaultInstance(ImaxWallpaperProto$GetImagesInCollectionResponse.class, imaxWallpaperProto$GetImagesInCollectionResponse);
    }

    public static Parser<ImaxWallpaperProto$GetImagesInCollectionResponse> parser() {
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
                return new RawMessageInfo(DEFAULT_INSTANCE, "\u0001\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u001b", new Object[]{"images_", ImaxWallpaperProto$Image.class});
            case 3:
                return new ImaxWallpaperProto$GetImagesInCollectionResponse();
            case 4:
                return new Builder(null);
            case 5:
                return DEFAULT_INSTANCE;
            case 6:
                Parser<ImaxWallpaperProto$GetImagesInCollectionResponse> parser = PARSER;
                if (parser == null) {
                    synchronized (ImaxWallpaperProto$GetImagesInCollectionResponse.class) {
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

    public List<ImaxWallpaperProto$Image> getImagesList() {
        return this.images_;
    }
}
