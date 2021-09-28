package com.google.wireless.android.apps.wallpaper;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.RawMessageInfo;
import java.util.Objects;
/* loaded from: classes.dex */
public final class WallpaperPickerSnapshotProto$WallpaperPickerSnapshot extends GeneratedMessageLite<WallpaperPickerSnapshotProto$WallpaperPickerSnapshot, Builder> implements MessageLiteOrBuilder {
    public static final int APP_LAUNCH_COUNT_FIELD_NUMBER = 3;
    private static final WallpaperPickerSnapshotProto$WallpaperPickerSnapshot DEFAULT_INSTANCE;
    public static final int FIRST_LAUNCH_DATE_SINCE_SETUP_FIELD_NUMBER = 1;
    public static final int FIRST_WALLPAPER_APPLY_DATE_SINCE_SETUP_FIELD_NUMBER = 2;
    public static final int HOME_WALLPAPER_CATEGORY_FIELD_NUMBER = 4;
    public static final int HOME_WALLPAPER_ID_FIELD_NUMBER = 5;
    public static final int LOCK_SCREEN_WALLPAPER_CATEGORY_FIELD_NUMBER = 6;
    public static final int LOCK_SCREEN_WALLPAPER_ID_FIELD_NUMBER = 7;
    private static volatile Parser<WallpaperPickerSnapshotProto$WallpaperPickerSnapshot> PARSER;
    private int appLaunchCount_;
    private int bitField0_;
    private int firstLaunchDateSinceSetup_;
    private int firstWallpaperApplyDateSinceSetup_;
    private String homeWallpaperCategory_ = "";
    private String homeWallpaperId_ = "";
    private String lockScreenWallpaperCategory_ = "";
    private String lockScreenWallpaperId_ = "";

    /* loaded from: classes.dex */
    public static final class Builder extends GeneratedMessageLite.Builder<WallpaperPickerSnapshotProto$WallpaperPickerSnapshot, Builder> implements MessageLiteOrBuilder {
        public Builder() {
            super(WallpaperPickerSnapshotProto$WallpaperPickerSnapshot.DEFAULT_INSTANCE);
        }

        public Builder(WallpaperPickerSnapshotProto$1 wallpaperPickerSnapshotProto$1) {
            super(WallpaperPickerSnapshotProto$WallpaperPickerSnapshot.DEFAULT_INSTANCE);
        }
    }

    static {
        WallpaperPickerSnapshotProto$WallpaperPickerSnapshot wallpaperPickerSnapshotProto$WallpaperPickerSnapshot = new WallpaperPickerSnapshotProto$WallpaperPickerSnapshot();
        DEFAULT_INSTANCE = wallpaperPickerSnapshotProto$WallpaperPickerSnapshot;
        GeneratedMessageLite.registerDefaultInstance(WallpaperPickerSnapshotProto$WallpaperPickerSnapshot.class, wallpaperPickerSnapshotProto$WallpaperPickerSnapshot);
    }

    public static void access$100(WallpaperPickerSnapshotProto$WallpaperPickerSnapshot wallpaperPickerSnapshotProto$WallpaperPickerSnapshot, int i) {
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.bitField0_ |= 1;
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.firstLaunchDateSinceSetup_ = i;
    }

    public static void access$1000(WallpaperPickerSnapshotProto$WallpaperPickerSnapshot wallpaperPickerSnapshotProto$WallpaperPickerSnapshot, String str) {
        Objects.requireNonNull(wallpaperPickerSnapshotProto$WallpaperPickerSnapshot);
        Objects.requireNonNull(str);
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.bitField0_ |= 16;
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.homeWallpaperId_ = str;
    }

    public static void access$1300(WallpaperPickerSnapshotProto$WallpaperPickerSnapshot wallpaperPickerSnapshotProto$WallpaperPickerSnapshot, String str) {
        Objects.requireNonNull(wallpaperPickerSnapshotProto$WallpaperPickerSnapshot);
        Objects.requireNonNull(str);
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.bitField0_ |= 32;
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.lockScreenWallpaperCategory_ = str;
    }

    public static void access$1600(WallpaperPickerSnapshotProto$WallpaperPickerSnapshot wallpaperPickerSnapshotProto$WallpaperPickerSnapshot, String str) {
        Objects.requireNonNull(wallpaperPickerSnapshotProto$WallpaperPickerSnapshot);
        Objects.requireNonNull(str);
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.bitField0_ |= 64;
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.lockScreenWallpaperId_ = str;
    }

    public static void access$300(WallpaperPickerSnapshotProto$WallpaperPickerSnapshot wallpaperPickerSnapshotProto$WallpaperPickerSnapshot, int i) {
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.bitField0_ |= 2;
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.firstWallpaperApplyDateSinceSetup_ = i;
    }

    public static void access$500(WallpaperPickerSnapshotProto$WallpaperPickerSnapshot wallpaperPickerSnapshotProto$WallpaperPickerSnapshot, int i) {
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.bitField0_ |= 4;
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.appLaunchCount_ = i;
    }

    public static void access$700(WallpaperPickerSnapshotProto$WallpaperPickerSnapshot wallpaperPickerSnapshotProto$WallpaperPickerSnapshot, String str) {
        Objects.requireNonNull(wallpaperPickerSnapshotProto$WallpaperPickerSnapshot);
        Objects.requireNonNull(str);
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.bitField0_ |= 8;
        wallpaperPickerSnapshotProto$WallpaperPickerSnapshot.homeWallpaperCategory_ = str;
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
                return new RawMessageInfo(DEFAULT_INSTANCE, "\u0001\u0007\u0000\u0001\u0001\u0007\u0007\u0000\u0000\u0000\u0001\u0004\u0000\u0002\u0004\u0001\u0003\u0004\u0002\u0004\b\u0003\u0005\b\u0004\u0006\b\u0005\u0007\b\u0006", new Object[]{"bitField0_", "firstLaunchDateSinceSetup_", "firstWallpaperApplyDateSinceSetup_", "appLaunchCount_", "homeWallpaperCategory_", "homeWallpaperId_", "lockScreenWallpaperCategory_", "lockScreenWallpaperId_"});
            case 3:
                return new WallpaperPickerSnapshotProto$WallpaperPickerSnapshot();
            case 4:
                return new Builder(null);
            case 5:
                return DEFAULT_INSTANCE;
            case 6:
                Parser<WallpaperPickerSnapshotProto$WallpaperPickerSnapshot> parser = PARSER;
                if (parser == null) {
                    synchronized (WallpaperPickerSnapshotProto$WallpaperPickerSnapshot.class) {
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
