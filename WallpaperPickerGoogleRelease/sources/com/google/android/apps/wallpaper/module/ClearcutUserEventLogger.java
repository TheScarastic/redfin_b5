package com.google.android.apps.wallpaper.module;

import android.content.Context;
import android.content.Intent;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import com.android.customization.model.grid.GridOption;
import com.android.customization.module.ThemesUserEventLogger;
import com.android.systemui.shared.R;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.LoggingOptInStatusProvider;
import com.android.wallpaper.module.NoOpUserEventLogger;
import com.android.wallpaper.module.WallpaperPreferences;
import com.android.wallpaper.picker.ImagePreviewFragment$3$$ExternalSyntheticLambda0;
import com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda1;
import com.android.wallpaper.util.WallpaperConnection$$ExternalSyntheticLambda1;
import com.google.android.gms.clearcut.ClearcutLogger;
import com.google.android.gms.clearcut.Counters;
import com.google.android.gms.common.util.zzh;
import com.google.wireless.android.apps.wallpaper.WallpaperLogProto$WallpaperEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;
/* loaded from: classes.dex */
public class ClearcutUserEventLogger extends NoOpUserEventLogger implements ThemesUserEventLogger {
    public static final boolean IS_VERBOSE = Log.isLoggable("UserEvent", 2);
    public static final ArrayMap<Class, SparseArray<String>> sNameCache = new ArrayMap<>();
    public ClearcutLogger mClearcutLogger;
    public Context mContext;
    public Counters mCounters;
    public LoggingOptInStatusProvider mLoggingOptInStatusProvider;
    public WallpaperPreferences mPreferences;

    public ClearcutUserEventLogger(Context context) {
        super(0);
        this.mContext = context;
        Injector injector = InjectorProvider.getInjector();
        this.mPreferences = injector.getPreferences(context);
        this.mClearcutLogger = new ClearcutLogger(context, "WALLPAPER_PICKER", null);
        this.mLoggingOptInStatusProvider = injector.getLoggingOptInStatusProvider(context);
        this.mCounters = new Counters(this.mClearcutLogger, "WALLPAPER_PICKER_COUNTERS", QuickStepContract.SYSUI_STATE_SEARCH_DISABLED, zzh.zza);
    }

    public final void log(WallpaperLogProto$WallpaperEvent wallpaperLogProto$WallpaperEvent) {
        SparseArray<String> sparseArray;
        int i;
        ClearcutLogger clearcutLogger = this.mClearcutLogger;
        byte[] byteArray = wallpaperLogProto$WallpaperEvent.toByteArray();
        Objects.requireNonNull(clearcutLogger);
        new ClearcutLogger.LogEventBuilder(byteArray, null).logAsync();
        if (IS_VERBOSE) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("type:");
            int number = wallpaperLogProto$WallpaperEvent.getType().getNumber();
            ArrayMap<Class, SparseArray<String>> arrayMap = sNameCache;
            synchronized (arrayMap) {
                sparseArray = arrayMap.get(WallpaperLogProto$WallpaperEvent.Type.class);
                if (sparseArray == null) {
                    sparseArray = new SparseArray<>();
                    Field[] declaredFields = WallpaperLogProto$WallpaperEvent.Type.class.getDeclaredFields();
                    for (Field field : declaredFields) {
                        if (field.getType() == Integer.TYPE && Modifier.isStatic(field.getModifiers())) {
                            try {
                                field.setAccessible(true);
                                sparseArray.put(field.getInt(null), field.getName());
                            } catch (IllegalAccessException unused) {
                            }
                        }
                    }
                    sNameCache.put(WallpaperLogProto$WallpaperEvent.Type.class, sparseArray);
                }
            }
            String str = sparseArray.get(number);
            if (str == null) {
                str = "UNKNOWN";
            }
            m.append(str);
            Log.d("UserEvent", m.toString());
            try {
                Field[] declaredFields2 = WallpaperLogProto$WallpaperEvent.class.getDeclaredFields();
                for (Field field2 : declaredFields2) {
                    Object obj = WallpaperLogProto$WallpaperEvent.class.getField(field2.getName()).get(wallpaperLogProto$WallpaperEvent);
                    if (!field2.getName().equals(WallpaperLogProto$WallpaperEvent.Type.class.getName()) && (obj instanceof Integer) && ((Integer) obj).intValue() != 0) {
                        Log.d("UserEvent", field2.getName() + ":" + ((Integer) obj).intValue());
                    }
                }
            } catch (IllegalAccessException | NoSuchFieldException unused2) {
            }
        }
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logActionClicked(String str, int i) {
        logEventWithCollectionId(i == R.string.build_case ? true : true ? 8 : 7, str);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logAppLaunched(Intent intent) {
    }

    public final void logBasicEvent(int i) {
        ClearcutUserEventLogger$$ExternalSyntheticLambda1 clearcutUserEventLogger$$ExternalSyntheticLambda1 = new ClearcutUserEventLogger$$ExternalSyntheticLambda1(this, i, 3);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda1.run();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logCategorySelected(String str) {
        logEventWithCollectionId(4, str);
    }

    @Override // com.android.customization.module.ThemesUserEventLogger
    public void logColorApplied(int i, int i2) {
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logCurrentWallpaperPreviewed() {
        logBasicEvent(6);
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logDailyRefreshTurnedOn() {
        logBasicEvent(3);
        ClearcutUserEventLogger$$ExternalSyntheticLambda0 clearcutUserEventLogger$$ExternalSyntheticLambda0 = new ClearcutUserEventLogger$$ExternalSyntheticLambda0(this, 1);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda0.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperMetadataRequestFailure(int i) {
        ClearcutUserEventLogger$$ExternalSyntheticLambda1 clearcutUserEventLogger$$ExternalSyntheticLambda1 = new ClearcutUserEventLogger$$ExternalSyntheticLambda1(this, i, 0);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda1.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperRotationHour(int i) {
        ClearcutUserEventLogger$$ExternalSyntheticLambda1 clearcutUserEventLogger$$ExternalSyntheticLambda1 = new ClearcutUserEventLogger$$ExternalSyntheticLambda1(this, i, 8);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda1.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperRotationStatus(int i) {
        ClearcutUserEventLogger$$ExternalSyntheticLambda1 clearcutUserEventLogger$$ExternalSyntheticLambda1 = new ClearcutUserEventLogger$$ExternalSyntheticLambda1(this, i, 2);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda1.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperSetNextWallpaperCrash(int i) {
        ClearcutUserEventLogger$$ExternalSyntheticLambda1 clearcutUserEventLogger$$ExternalSyntheticLambda1 = new ClearcutUserEventLogger$$ExternalSyntheticLambda1(this, i, 5);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda1.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logDailyWallpaperSetNextWallpaperResult(int i) {
        ClearcutUserEventLogger$$ExternalSyntheticLambda1 clearcutUserEventLogger$$ExternalSyntheticLambda1 = new ClearcutUserEventLogger$$ExternalSyntheticLambda1(this, i, 9);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda1.run();
    }

    public final void logEventWithCollectionId(int i, String str) {
        WallpaperConnection$$ExternalSyntheticLambda1 wallpaperConnection$$ExternalSyntheticLambda1 = new WallpaperConnection$$ExternalSyntheticLambda1(this, i, str);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        wallpaperConnection$$ExternalSyntheticLambda1.run();
    }

    @Override // com.android.customization.module.ThemesUserEventLogger
    public void logGridApplied(GridOption gridOption) {
        WallpaperConnection$$ExternalSyntheticLambda1 wallpaperConnection$$ExternalSyntheticLambda1 = new WallpaperConnection$$ExternalSyntheticLambda1(this, 14, gridOption);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        wallpaperConnection$$ExternalSyntheticLambda1.run();
    }

    @Override // com.android.customization.module.ThemesUserEventLogger
    public void logGridSelected(GridOption gridOption) {
        WallpaperConnection$$ExternalSyntheticLambda1 wallpaperConnection$$ExternalSyntheticLambda1 = new WallpaperConnection$$ExternalSyntheticLambda1(this, 13, gridOption);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        wallpaperConnection$$ExternalSyntheticLambda1.run();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logIndividualWallpaperSelected(String str) {
        logEventWithCollectionId(5, str);
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logNumDailyWallpaperRotationsInLastWeek() {
        ClearcutUserEventLogger$$ExternalSyntheticLambda0 clearcutUserEventLogger$$ExternalSyntheticLambda0 = new ClearcutUserEventLogger$$ExternalSyntheticLambda0(this, 0);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda0.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logNumDailyWallpaperRotationsPreviousDay() {
        ClearcutUserEventLogger$$ExternalSyntheticLambda0 clearcutUserEventLogger$$ExternalSyntheticLambda0 = new ClearcutUserEventLogger$$ExternalSyntheticLambda0(this, 4);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda0.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logNumDaysDailyRotationFailed(int i) {
        ClearcutUserEventLogger$$ExternalSyntheticLambda1 clearcutUserEventLogger$$ExternalSyntheticLambda1 = new ClearcutUserEventLogger$$ExternalSyntheticLambda1(this, i, 1);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda1.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logNumDaysDailyRotationNotAttempted(int i) {
        ClearcutUserEventLogger$$ExternalSyntheticLambda1 clearcutUserEventLogger$$ExternalSyntheticLambda1 = new ClearcutUserEventLogger$$ExternalSyntheticLambda1(this, i, 6);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda1.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logRefreshDailyWallpaperButtonClicked() {
        ImagePreviewFragment$3$$ExternalSyntheticLambda0 imagePreviewFragment$3$$ExternalSyntheticLambda0 = new ImagePreviewFragment$3$$ExternalSyntheticLambda0(this, "DailyWallpaper.RefreshButtonClicked");
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        imagePreviewFragment$3$$ExternalSyntheticLambda0.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logRestored() {
        ImagePreviewFragment$3$$ExternalSyntheticLambda0 imagePreviewFragment$3$$ExternalSyntheticLambda0 = new ImagePreviewFragment$3$$ExternalSyntheticLambda0(this, "BackupAndRestore.Restored");
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        imagePreviewFragment$3$$ExternalSyntheticLambda0.run();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logResumed(boolean z, boolean z2) {
        if (z) {
            if (z2) {
                logBasicEvent(17);
            } else {
                logBasicEvent(18);
            }
        } else if (z2) {
            logBasicEvent(15);
        } else {
            logBasicEvent(16);
        }
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logSnapshot() {
        ClearcutUserEventLogger$$ExternalSyntheticLambda0 clearcutUserEventLogger$$ExternalSyntheticLambda0 = new ClearcutUserEventLogger$$ExternalSyntheticLambda0(this, 3);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda0.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logStandalonePreviewImageUriHasReadPermission(boolean z) {
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        this.mCounters.getIntegerHistogram("StandalonePreview.ImageUriPermissionStatus").increment(!z ? 1 : 0);
        Counters counters = this.mCounters;
        counters.logAllAsync(counters.zzf);
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logStandalonePreviewLaunched() {
        ImagePreviewFragment$3$$ExternalSyntheticLambda0 imagePreviewFragment$3$$ExternalSyntheticLambda0 = new ImagePreviewFragment$3$$ExternalSyntheticLambda0(this, "StandalonePreview.Launched");
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        imagePreviewFragment$3$$ExternalSyntheticLambda0.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logStandalonePreviewStorageDialogApproved(boolean z) {
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        this.mCounters.getIntegerHistogram("StandalonePreview.StorageDialogResponse").increment(!z ? 1 : 0);
        Counters counters = this.mCounters;
        counters.logAllAsync(counters.zzf);
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logStopped() {
        logBasicEvent(19);
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logWallpaperPresentationMode() {
        ClearcutUserEventLogger$$ExternalSyntheticLambda0 clearcutUserEventLogger$$ExternalSyntheticLambda0 = new ClearcutUserEventLogger$$ExternalSyntheticLambda0(this, 2);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda0.run();
    }

    @Override // com.android.wallpaper.module.UserEventLogger
    public void logWallpaperSet(String str, String str2) {
        PreviewUtils$$ExternalSyntheticLambda1 previewUtils$$ExternalSyntheticLambda1 = new PreviewUtils$$ExternalSyntheticLambda1(this, str, str2);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        previewUtils$$ExternalSyntheticLambda1.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logWallpaperSetFailureReason(int i) {
        ClearcutUserEventLogger$$ExternalSyntheticLambda1 clearcutUserEventLogger$$ExternalSyntheticLambda1 = new ClearcutUserEventLogger$$ExternalSyntheticLambda1(this, i, 4);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda1.run();
    }

    @Override // com.android.wallpaper.module.NoOpUserEventLogger, com.android.wallpaper.module.UserEventLogger
    public void logWallpaperSetResult(int i) {
        ClearcutUserEventLogger$$ExternalSyntheticLambda1 clearcutUserEventLogger$$ExternalSyntheticLambda1 = new ClearcutUserEventLogger$$ExternalSyntheticLambda1(this, i, 7);
        Objects.requireNonNull((AlwaysLoggingOptInStatusProvider) this.mLoggingOptInStatusProvider);
        clearcutUserEventLogger$$ExternalSyntheticLambda1.run();
    }
}
