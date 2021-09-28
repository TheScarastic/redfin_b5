package com.android.systemui.log;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.constraintlayout.widget.R$styleable;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LogcatEchoTrackerDebug.kt */
/* loaded from: classes.dex */
public final class LogcatEchoTrackerDebug implements LogcatEchoTracker {
    public static final Factory Factory = new Factory(null);
    private final Map<String, LogLevel> cachedBufferLevels;
    private final Map<String, LogLevel> cachedTagLevels;
    private final ContentResolver contentResolver;

    public /* synthetic */ LogcatEchoTrackerDebug(ContentResolver contentResolver, DefaultConstructorMarker defaultConstructorMarker) {
        this(contentResolver);
    }

    public static final LogcatEchoTrackerDebug create(ContentResolver contentResolver, Looper looper) {
        return Factory.create(contentResolver, looper);
    }

    private LogcatEchoTrackerDebug(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
        this.cachedBufferLevels = new LinkedHashMap();
        this.cachedTagLevels = new LinkedHashMap();
    }

    /* compiled from: LogcatEchoTrackerDebug.kt */
    /* loaded from: classes.dex */
    public static final class Factory {
        public /* synthetic */ Factory(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Factory() {
        }

        public final LogcatEchoTrackerDebug create(ContentResolver contentResolver, Looper looper) {
            Intrinsics.checkNotNullParameter(contentResolver, "contentResolver");
            Intrinsics.checkNotNullParameter(looper, "mainLooper");
            LogcatEchoTrackerDebug logcatEchoTrackerDebug = new LogcatEchoTrackerDebug(contentResolver, null);
            logcatEchoTrackerDebug.attach(looper);
            return logcatEchoTrackerDebug;
        }
    }

    public final void attach(Looper looper) {
        this.contentResolver.registerContentObserver(Settings.Global.getUriFor("systemui/buffer"), true, new ContentObserver(this, new Handler(looper)) { // from class: com.android.systemui.log.LogcatEchoTrackerDebug$attach$1
            final /* synthetic */ LogcatEchoTrackerDebug this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                Intrinsics.checkNotNullParameter(uri, "uri");
                super.onChange(z, uri);
                LogcatEchoTrackerDebug.access$getCachedBufferLevels$p(this.this$0).clear();
            }
        });
        this.contentResolver.registerContentObserver(Settings.Global.getUriFor("systemui/tag"), true, new ContentObserver(this, new Handler(looper)) { // from class: com.android.systemui.log.LogcatEchoTrackerDebug$attach$2
            final /* synthetic */ LogcatEchoTrackerDebug this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                Intrinsics.checkNotNullParameter(uri, "uri");
                super.onChange(z, uri);
                LogcatEchoTrackerDebug.access$getCachedTagLevels$p(this.this$0).clear();
            }
        });
    }

    @Override // com.android.systemui.log.LogcatEchoTracker
    public synchronized boolean isBufferLoggable(String str, LogLevel logLevel) {
        Intrinsics.checkNotNullParameter(str, "bufferName");
        Intrinsics.checkNotNullParameter(logLevel, "level");
        return logLevel.ordinal() >= getLogLevel(str, "systemui/buffer", this.cachedBufferLevels).ordinal();
    }

    @Override // com.android.systemui.log.LogcatEchoTracker
    public synchronized boolean isTagLoggable(String str, LogLevel logLevel) {
        Intrinsics.checkNotNullParameter(str, "tagName");
        Intrinsics.checkNotNullParameter(logLevel, "level");
        return logLevel.compareTo(getLogLevel(str, "systemui/tag", this.cachedTagLevels)) >= 0;
    }

    private final LogLevel getLogLevel(String str, String str2, Map<String, LogLevel> map) {
        LogLevel logLevel = map.get(str);
        if (logLevel != null) {
            return logLevel;
        }
        LogLevel readSetting = readSetting(str2 + '/' + str);
        map.put(str, readSetting);
        return readSetting;
    }

    private final LogLevel readSetting(String str) {
        try {
            return parseProp(Settings.Global.getString(this.contentResolver, str));
        } catch (Settings.SettingNotFoundException unused) {
            return LogcatEchoTrackerDebugKt.DEFAULT_LEVEL;
        }
    }

    private final LogLevel parseProp(String str) {
        String str2;
        if (str == null) {
            str2 = null;
        } else {
            str2 = str.toLowerCase();
            Intrinsics.checkNotNullExpressionValue(str2, "(this as java.lang.String).toLowerCase()");
        }
        if (str2 != null) {
            switch (str2.hashCode()) {
                case -1408208058:
                    if (str2.equals("assert")) {
                        return LogLevel.WTF;
                    }
                    break;
                case R$styleable.Constraint_layout_goneMarginLeft:
                    if (str2.equals("d")) {
                        return LogLevel.DEBUG;
                    }
                    break;
                case R$styleable.Constraint_layout_goneMarginRight:
                    if (str2.equals("e")) {
                        return LogLevel.ERROR;
                    }
                    break;
                case R$styleable.Constraint_pathMotionArc:
                    if (str2.equals("i")) {
                        return LogLevel.INFO;
                    }
                    break;
                case androidx.appcompat.R$styleable.AppCompatTheme_windowActionBarOverlay:
                    if (str2.equals("v")) {
                        return LogLevel.VERBOSE;
                    }
                    break;
                case androidx.appcompat.R$styleable.AppCompatTheme_windowActionModeOverlay:
                    if (str2.equals("w")) {
                        return LogLevel.WARNING;
                    }
                    break;
                case 118057:
                    if (str2.equals("wtf")) {
                        return LogLevel.WTF;
                    }
                    break;
                case 3237038:
                    if (str2.equals("info")) {
                        return LogLevel.INFO;
                    }
                    break;
                case 3641990:
                    if (str2.equals("warn")) {
                        return LogLevel.WARNING;
                    }
                    break;
                case 95458899:
                    if (str2.equals("debug")) {
                        return LogLevel.DEBUG;
                    }
                    break;
                case 96784904:
                    if (str2.equals("error")) {
                        return LogLevel.ERROR;
                    }
                    break;
                case 351107458:
                    if (str2.equals("verbose")) {
                        return LogLevel.VERBOSE;
                    }
                    break;
                case 1124446108:
                    if (str2.equals("warning")) {
                        return LogLevel.WARNING;
                    }
                    break;
            }
        }
        return LogcatEchoTrackerDebugKt.DEFAULT_LEVEL;
    }
}
