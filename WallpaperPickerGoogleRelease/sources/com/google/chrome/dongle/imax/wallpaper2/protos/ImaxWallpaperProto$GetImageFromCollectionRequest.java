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
public final class ImaxWallpaperProto$GetImageFromCollectionRequest extends GeneratedMessageLite<ImaxWallpaperProto$GetImageFromCollectionRequest, Builder> implements MessageLiteOrBuilder {
    public static final int COLLECTION_IDS_FIELD_NUMBER = 1;
    private static final ImaxWallpaperProto$GetImageFromCollectionRequest DEFAULT_INSTANCE;
    public static final int FILTERING_LABEL_FIELD_NUMBER = 5;
    public static final int LANGUAGE_FIELD_NUMBER = 3;
    private static volatile Parser<ImaxWallpaperProto$GetImageFromCollectionRequest> PARSER = null;
    public static final int REGION_FIELD_NUMBER = 4;
    public static final int RESUME_TOKEN_FIELD_NUMBER = 2;
    private int bitField0_;
    private Internal.ProtobufList<String> collectionIds_;
    private Internal.ProtobufList<String> filteringLabel_;
    private String resumeToken_ = "";
    private String language_ = "";
    private String region_ = "";

    /* loaded from: classes.dex */
    public static final class Builder extends GeneratedMessageLite.Builder<ImaxWallpaperProto$GetImageFromCollectionRequest, Builder> implements MessageLiteOrBuilder {
        public Builder() {
            super(ImaxWallpaperProto$GetImageFromCollectionRequest.DEFAULT_INSTANCE);
        }

        public Builder(ImaxWallpaperProto$1 imaxWallpaperProto$1) {
            super(ImaxWallpaperProto$GetImageFromCollectionRequest.DEFAULT_INSTANCE);
        }
    }

    static {
        ImaxWallpaperProto$GetImageFromCollectionRequest imaxWallpaperProto$GetImageFromCollectionRequest = new ImaxWallpaperProto$GetImageFromCollectionRequest();
        DEFAULT_INSTANCE = imaxWallpaperProto$GetImageFromCollectionRequest;
        GeneratedMessageLite.registerDefaultInstance(ImaxWallpaperProto$GetImageFromCollectionRequest.class, imaxWallpaperProto$GetImageFromCollectionRequest);
    }

    public ImaxWallpaperProto$GetImageFromCollectionRequest() {
        ProtobufArrayList<Object> protobufArrayList = ProtobufArrayList.EMPTY_LIST;
        this.collectionIds_ = protobufArrayList;
        this.filteringLabel_ = protobufArrayList;
    }

    public static void access$10000(ImaxWallpaperProto$GetImageFromCollectionRequest imaxWallpaperProto$GetImageFromCollectionRequest, String str) {
        Objects.requireNonNull(imaxWallpaperProto$GetImageFromCollectionRequest);
        Objects.requireNonNull(str);
        imaxWallpaperProto$GetImageFromCollectionRequest.bitField0_ |= 1;
        imaxWallpaperProto$GetImageFromCollectionRequest.resumeToken_ = str;
    }

    public static void access$10300(ImaxWallpaperProto$GetImageFromCollectionRequest imaxWallpaperProto$GetImageFromCollectionRequest, String str) {
        Objects.requireNonNull(imaxWallpaperProto$GetImageFromCollectionRequest);
        Objects.requireNonNull(str);
        imaxWallpaperProto$GetImageFromCollectionRequest.bitField0_ |= 2;
        imaxWallpaperProto$GetImageFromCollectionRequest.language_ = str;
    }

    public static void access$11100(ImaxWallpaperProto$GetImageFromCollectionRequest imaxWallpaperProto$GetImageFromCollectionRequest, Iterable iterable) {
        if (!imaxWallpaperProto$GetImageFromCollectionRequest.filteringLabel_.isModifiable()) {
            imaxWallpaperProto$GetImageFromCollectionRequest.filteringLabel_ = GeneratedMessageLite.mutableCopy(imaxWallpaperProto$GetImageFromCollectionRequest.filteringLabel_);
        }
        AbstractMessageLite.addAll(iterable, imaxWallpaperProto$GetImageFromCollectionRequest.filteringLabel_);
    }

    public static void access$9600(ImaxWallpaperProto$GetImageFromCollectionRequest imaxWallpaperProto$GetImageFromCollectionRequest, String str) {
        Objects.requireNonNull(imaxWallpaperProto$GetImageFromCollectionRequest);
        Objects.requireNonNull(str);
        if (!imaxWallpaperProto$GetImageFromCollectionRequest.collectionIds_.isModifiable()) {
            imaxWallpaperProto$GetImageFromCollectionRequest.collectionIds_ = GeneratedMessageLite.mutableCopy(imaxWallpaperProto$GetImageFromCollectionRequest.collectionIds_);
        }
        imaxWallpaperProto$GetImageFromCollectionRequest.collectionIds_.add(str);
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
                return new RawMessageInfo(DEFAULT_INSTANCE, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0002\u0000\u0001\u001a\u0002\b\u0000\u0003\b\u0001\u0004\b\u0002\u0005\u001a", new Object[]{"bitField0_", "collectionIds_", "resumeToken_", "language_", "region_", "filteringLabel_"});
            case 3:
                return new ImaxWallpaperProto$GetImageFromCollectionRequest();
            case 4:
                return new Builder(null);
            case 5:
                return DEFAULT_INSTANCE;
            case 6:
                Parser<ImaxWallpaperProto$GetImageFromCollectionRequest> parser = PARSER;
                if (parser == null) {
                    synchronized (ImaxWallpaperProto$GetImageFromCollectionRequest.class) {
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
