package com.google.chrome.dongle.imax.wallpaper2.protos;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.Internal;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtobufArrayList;
import com.google.protobuf.RawMessageInfo;
import java.util.List;
/* loaded from: classes.dex */
public final class ImaxWallpaperProto$GetCollectionsResponse extends GeneratedMessageLite<ImaxWallpaperProto$GetCollectionsResponse, Builder> implements MessageLiteOrBuilder {
    public static final int COLLECTIONS_FIELD_NUMBER = 1;
    private static final ImaxWallpaperProto$GetCollectionsResponse DEFAULT_INSTANCE;
    private static volatile Parser<ImaxWallpaperProto$GetCollectionsResponse> PARSER;
    private Internal.ProtobufList<ImaxWallpaperProto$Collection> collections_ = ProtobufArrayList.EMPTY_LIST;

    /* loaded from: classes.dex */
    public static final class Builder extends GeneratedMessageLite.Builder<ImaxWallpaperProto$GetCollectionsResponse, Builder> implements MessageLiteOrBuilder {
        public Builder(ImaxWallpaperProto$1 imaxWallpaperProto$1) {
            super(ImaxWallpaperProto$GetCollectionsResponse.DEFAULT_INSTANCE);
        }
    }

    static {
        ImaxWallpaperProto$GetCollectionsResponse imaxWallpaperProto$GetCollectionsResponse = new ImaxWallpaperProto$GetCollectionsResponse();
        DEFAULT_INSTANCE = imaxWallpaperProto$GetCollectionsResponse;
        GeneratedMessageLite.registerDefaultInstance(ImaxWallpaperProto$GetCollectionsResponse.class, imaxWallpaperProto$GetCollectionsResponse);
    }

    public static Parser<ImaxWallpaperProto$GetCollectionsResponse> parser() {
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
                return new RawMessageInfo(DEFAULT_INSTANCE, "\u0001\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u001b", new Object[]{"collections_", ImaxWallpaperProto$Collection.class});
            case 3:
                return new ImaxWallpaperProto$GetCollectionsResponse();
            case 4:
                return new Builder(null);
            case 5:
                return DEFAULT_INSTANCE;
            case 6:
                Parser<ImaxWallpaperProto$GetCollectionsResponse> parser = PARSER;
                if (parser == null) {
                    synchronized (ImaxWallpaperProto$GetCollectionsResponse.class) {
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

    public List<ImaxWallpaperProto$Collection> getCollectionsList() {
        return this.collections_;
    }
}
