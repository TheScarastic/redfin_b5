package com.google.chrome.dongle.imax.wallpaper2.protos;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.Internal;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtobufArrayList;
import com.google.protobuf.RawMessageInfo;
/* loaded from: classes.dex */
public final class ImaxWallpaperProto$Collection extends GeneratedMessageLite<ImaxWallpaperProto$Collection, Builder> implements MessageLiteOrBuilder {
    public static final int COLLECTION_ID_FIELD_NUMBER = 1;
    public static final int COLLECTION_NAME_FIELD_NUMBER = 2;
    private static final ImaxWallpaperProto$Collection DEFAULT_INSTANCE;
    private static volatile Parser<ImaxWallpaperProto$Collection> PARSER = null;
    public static final int PREVIEW_FIELD_NUMBER = 3;
    private int bitField0_;
    private String collectionId_ = "";
    private String collectionName_ = "";
    private Internal.ProtobufList<ImaxWallpaperProto$Image> preview_ = ProtobufArrayList.EMPTY_LIST;

    /* loaded from: classes.dex */
    public static final class Builder extends GeneratedMessageLite.Builder<ImaxWallpaperProto$Collection, Builder> implements MessageLiteOrBuilder {
        public Builder(ImaxWallpaperProto$1 imaxWallpaperProto$1) {
            super(ImaxWallpaperProto$Collection.DEFAULT_INSTANCE);
        }
    }

    static {
        ImaxWallpaperProto$Collection imaxWallpaperProto$Collection = new ImaxWallpaperProto$Collection();
        DEFAULT_INSTANCE = imaxWallpaperProto$Collection;
        GeneratedMessageLite.registerDefaultInstance(ImaxWallpaperProto$Collection.class, imaxWallpaperProto$Collection);
    }

    @Override // com.google.protobuf.GeneratedMessageLite
    public final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
        switch (methodToInvoke.ordinal()) {
            case 0:
                return (byte) 1;
            case 1:
                return null;
            case 2:
                return new RawMessageInfo(DEFAULT_INSTANCE, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0001\u0000\u0001\b\u0000\u0002\b\u0001\u0003\u001b", new Object[]{"bitField0_", "collectionId_", "collectionName_", "preview_", ImaxWallpaperProto$Image.class});
            case 3:
                return new ImaxWallpaperProto$Collection();
            case 4:
                return new Builder(null);
            case 5:
                return DEFAULT_INSTANCE;
            case 6:
                Parser<ImaxWallpaperProto$Collection> parser = PARSER;
                if (parser == null) {
                    synchronized (ImaxWallpaperProto$Collection.class) {
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

    public String getCollectionId() {
        return this.collectionId_;
    }

    public String getCollectionName() {
        return this.collectionName_;
    }

    public ImaxWallpaperProto$Image getPreview(int i) {
        return this.preview_.get(i);
    }
}
