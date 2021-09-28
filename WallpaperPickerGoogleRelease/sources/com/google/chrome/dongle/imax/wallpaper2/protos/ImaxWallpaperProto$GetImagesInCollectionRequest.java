package com.google.chrome.dongle.imax.wallpaper2.protos;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.Internal;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtobufArrayList;
import com.google.protobuf.RawMessageInfo;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ImaxWallpaperProto$GetImagesInCollectionRequest extends GeneratedMessageLite<ImaxWallpaperProto$GetImagesInCollectionRequest, Builder> implements MessageLiteOrBuilder {
    public static final int COLLECTION_ID_FIELD_NUMBER = 1;
    private static final ImaxWallpaperProto$GetImagesInCollectionRequest DEFAULT_INSTANCE;
    public static final int FILTERING_LABEL_FIELD_NUMBER = 4;
    public static final int LANGUAGE_FIELD_NUMBER = 2;
    private static volatile Parser<ImaxWallpaperProto$GetImagesInCollectionRequest> PARSER = null;
    public static final int REGION_FIELD_NUMBER = 3;
    private int bitField0_;
    private String collectionId_ = "";
    private String language_ = "";
    private String region_ = "";
    private Internal.ProtobufList<String> filteringLabel_ = ProtobufArrayList.EMPTY_LIST;

    /* loaded from: classes.dex */
    public static final class Builder extends GeneratedMessageLite.Builder<ImaxWallpaperProto$GetImagesInCollectionRequest, Builder> implements MessageLiteOrBuilder {
        public Builder() {
            super(ImaxWallpaperProto$GetImagesInCollectionRequest.DEFAULT_INSTANCE);
        }

        public Builder(ImaxWallpaperProto$1 imaxWallpaperProto$1) {
            super(ImaxWallpaperProto$GetImagesInCollectionRequest.DEFAULT_INSTANCE);
        }
    }

    static {
        ImaxWallpaperProto$GetImagesInCollectionRequest imaxWallpaperProto$GetImagesInCollectionRequest = new ImaxWallpaperProto$GetImagesInCollectionRequest();
        DEFAULT_INSTANCE = imaxWallpaperProto$GetImagesInCollectionRequest;
        GeneratedMessageLite.registerDefaultInstance(ImaxWallpaperProto$GetImagesInCollectionRequest.class, imaxWallpaperProto$GetImagesInCollectionRequest);
    }

    public static void access$6800(ImaxWallpaperProto$GetImagesInCollectionRequest imaxWallpaperProto$GetImagesInCollectionRequest, String str) {
        Objects.requireNonNull(imaxWallpaperProto$GetImagesInCollectionRequest);
        Objects.requireNonNull(str);
        imaxWallpaperProto$GetImagesInCollectionRequest.bitField0_ |= 1;
        imaxWallpaperProto$GetImagesInCollectionRequest.collectionId_ = str;
    }

    public static void access$7100(ImaxWallpaperProto$GetImagesInCollectionRequest imaxWallpaperProto$GetImagesInCollectionRequest, String str) {
        Objects.requireNonNull(imaxWallpaperProto$GetImagesInCollectionRequest);
        Objects.requireNonNull(str);
        imaxWallpaperProto$GetImagesInCollectionRequest.bitField0_ |= 2;
        imaxWallpaperProto$GetImagesInCollectionRequest.language_ = str;
    }

    public static void access$7900(ImaxWallpaperProto$GetImagesInCollectionRequest imaxWallpaperProto$GetImagesInCollectionRequest, Iterable iterable) {
        if (!imaxWallpaperProto$GetImagesInCollectionRequest.filteringLabel_.isModifiable()) {
            imaxWallpaperProto$GetImagesInCollectionRequest.filteringLabel_ = GeneratedMessageLite.mutableCopy(imaxWallpaperProto$GetImagesInCollectionRequest.filteringLabel_);
        }
        AbstractMessageLite.addAll(iterable, imaxWallpaperProto$GetImagesInCollectionRequest.filteringLabel_);
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.createBuilder();
    }

    @Override // com.google.protobuf.GeneratedMessageLite
    public final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke, Object obj, Object obj2) {
        switch (methodToInvoke.ordinal()) {
            case 0:
                return (byte) 1;
            case 1:
                return null;
            case 2:
                return new RawMessageInfo(DEFAULT_INSTANCE, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0001\u0000\u0001\b\u0000\u0002\b\u0001\u0003\b\u0002\u0004\u001a", new Object[]{"bitField0_", "collectionId_", "language_", "region_", "filteringLabel_"});
            case 3:
                return new ImaxWallpaperProto$GetImagesInCollectionRequest();
            case 4:
                return new Builder(null);
            case 5:
                return DEFAULT_INSTANCE;
            case 6:
                Parser<ImaxWallpaperProto$GetImagesInCollectionRequest> parser = PARSER;
                if (parser == null) {
                    synchronized (ImaxWallpaperProto$GetImagesInCollectionRequest.class) {
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
}
