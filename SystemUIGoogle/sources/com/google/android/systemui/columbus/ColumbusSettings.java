package com.google.android.systemui.columbus;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import com.google.android.systemui.columbus.ColumbusContentObserver;
import com.google.android.systemui.columbus.ColumbusSettings;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ColumbusSettings.kt */
/* loaded from: classes2.dex */
public class ColumbusSettings {
    private static final Uri COLUMBUS_ACTION_URI;
    private static final Uri COLUMBUS_AP_SENSOR_URI;
    private static final Uri COLUMBUS_ENABLED_URI;
    private static final Uri COLUMBUS_LAUNCH_APP_SHORTCUT_URI;
    private static final Uri COLUMBUS_LAUNCH_APP_URI;
    private static final Uri COLUMBUS_LOW_SENSITIVITY_URI;
    private static final Uri COLUMBUS_SILENCE_ALERTS_URI;
    public static final Companion Companion = new Companion(null);
    private static final Set<Uri> MONITORED_URIS;
    private final Set<ColumbusContentObserver> contentObservers;
    private final ContentResolver contentResolver;
    private boolean registered;
    private final Set<ColumbusSettingsChangeListener> listeners = new LinkedHashSet();
    private final Function1<Uri, Unit> callback = new Function1<Uri, Unit>(this) { // from class: com.google.android.systemui.columbus.ColumbusSettings$callback$1
        final /* synthetic */ ColumbusSettings this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Uri uri) {
            invoke(uri);
            return Unit.INSTANCE;
        }

        public final void invoke(Uri uri) {
            Intrinsics.checkNotNullParameter(uri, "uri");
            if (Intrinsics.areEqual(uri, ColumbusSettings.COLUMBUS_ENABLED_URI)) {
                boolean isColumbusEnabled = this.this$0.isColumbusEnabled();
                for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener : this.this$0.listeners) {
                    columbusSettingsChangeListener.onColumbusEnabledChange(isColumbusEnabled);
                }
            } else if (Intrinsics.areEqual(uri, ColumbusSettings.COLUMBUS_AP_SENSOR_URI)) {
                boolean useApSensor = this.this$0.useApSensor();
                for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener2 : this.this$0.listeners) {
                    columbusSettingsChangeListener2.onUseApSensorChange(useApSensor);
                }
            } else if (Intrinsics.areEqual(uri, ColumbusSettings.COLUMBUS_ACTION_URI)) {
                String selectedAction = this.this$0.selectedAction();
                for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener3 : this.this$0.listeners) {
                    columbusSettingsChangeListener3.onSelectedActionChange(selectedAction);
                }
            } else if (Intrinsics.areEqual(uri, ColumbusSettings.COLUMBUS_LAUNCH_APP_URI)) {
                String selectedApp = this.this$0.selectedApp();
                for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener4 : this.this$0.listeners) {
                    columbusSettingsChangeListener4.onSelectedAppChange(selectedApp);
                }
            } else if (Intrinsics.areEqual(uri, ColumbusSettings.COLUMBUS_LAUNCH_APP_SHORTCUT_URI)) {
                String selectedAppShortcut = this.this$0.selectedAppShortcut();
                for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener5 : this.this$0.listeners) {
                    columbusSettingsChangeListener5.onSelectedAppShortcutChange(selectedAppShortcut);
                }
            } else if (Intrinsics.areEqual(uri, ColumbusSettings.COLUMBUS_LOW_SENSITIVITY_URI)) {
                boolean useLowSensitivity = this.this$0.useLowSensitivity();
                for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener6 : this.this$0.listeners) {
                    columbusSettingsChangeListener6.onLowSensitivityChange(useLowSensitivity);
                }
            } else if (Intrinsics.areEqual(uri, ColumbusSettings.COLUMBUS_SILENCE_ALERTS_URI)) {
                boolean silenceAlertsEnabled = this.this$0.silenceAlertsEnabled();
                for (ColumbusSettings.ColumbusSettingsChangeListener columbusSettingsChangeListener7 : this.this$0.listeners) {
                    columbusSettingsChangeListener7.onAlertSilenceEnabledChange(silenceAlertsEnabled);
                }
            } else {
                Log.w("Columbus/Settings", Intrinsics.stringPlus("Unknown setting change: ", uri));
            }
        }
    };

    /* compiled from: ColumbusSettings.kt */
    /* loaded from: classes2.dex */
    public interface ColumbusSettingsChangeListener {

        /* compiled from: ColumbusSettings.kt */
        /* loaded from: classes2.dex */
        public static final class DefaultImpls {
            public static void onAlertSilenceEnabledChange(ColumbusSettingsChangeListener columbusSettingsChangeListener, boolean z) {
                Intrinsics.checkNotNullParameter(columbusSettingsChangeListener, "this");
            }

            public static void onColumbusEnabledChange(ColumbusSettingsChangeListener columbusSettingsChangeListener, boolean z) {
                Intrinsics.checkNotNullParameter(columbusSettingsChangeListener, "this");
            }

            public static void onLowSensitivityChange(ColumbusSettingsChangeListener columbusSettingsChangeListener, boolean z) {
                Intrinsics.checkNotNullParameter(columbusSettingsChangeListener, "this");
            }

            public static void onSelectedActionChange(ColumbusSettingsChangeListener columbusSettingsChangeListener, String str) {
                Intrinsics.checkNotNullParameter(columbusSettingsChangeListener, "this");
                Intrinsics.checkNotNullParameter(str, "selectedAction");
            }

            public static void onSelectedAppChange(ColumbusSettingsChangeListener columbusSettingsChangeListener, String str) {
                Intrinsics.checkNotNullParameter(columbusSettingsChangeListener, "this");
                Intrinsics.checkNotNullParameter(str, "selectedApp");
            }

            public static void onSelectedAppShortcutChange(ColumbusSettingsChangeListener columbusSettingsChangeListener, String str) {
                Intrinsics.checkNotNullParameter(columbusSettingsChangeListener, "this");
                Intrinsics.checkNotNullParameter(str, "selectedShortcut");
            }

            public static void onUseApSensorChange(ColumbusSettingsChangeListener columbusSettingsChangeListener, boolean z) {
                Intrinsics.checkNotNullParameter(columbusSettingsChangeListener, "this");
            }
        }

        void onAlertSilenceEnabledChange(boolean z);

        void onColumbusEnabledChange(boolean z);

        void onLowSensitivityChange(boolean z);

        void onSelectedActionChange(String str);

        void onSelectedAppChange(String str);

        void onSelectedAppShortcutChange(String str);

        void onUseApSensorChange(boolean z);
    }

    public ColumbusSettings(Context context, ColumbusContentObserver.Factory factory) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(factory, "contentObserverFactory");
        this.contentResolver = context.getContentResolver();
        Set<Uri> set = MONITORED_URIS;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(set, 10));
        for (Uri uri : set) {
            Intrinsics.checkNotNullExpressionValue(uri, "it");
            arrayList.add(factory.create(uri, this.callback));
        }
        this.contentObservers = CollectionsKt___CollectionsKt.toSet(arrayList);
    }

    public void registerColumbusSettingsChangeListener(ColumbusSettingsChangeListener columbusSettingsChangeListener) {
        Intrinsics.checkNotNullParameter(columbusSettingsChangeListener, "listener");
        this.listeners.add(columbusSettingsChangeListener);
        if (!this.registered && (!this.listeners.isEmpty())) {
            this.registered = true;
            for (ColumbusContentObserver columbusContentObserver : this.contentObservers) {
                columbusContentObserver.activate();
            }
        }
    }

    public void unregisterColumbusSettingsChangeListener(ColumbusSettingsChangeListener columbusSettingsChangeListener) {
        Intrinsics.checkNotNullParameter(columbusSettingsChangeListener, "listener");
        this.listeners.remove(columbusSettingsChangeListener);
        if (this.registered && this.listeners.isEmpty()) {
            this.registered = false;
            for (ColumbusContentObserver columbusContentObserver : this.contentObservers) {
                columbusContentObserver.deactivate();
            }
        }
    }

    public boolean isColumbusEnabled() {
        if (Settings.Secure.getIntForUser(this.contentResolver, "columbus_enabled", 0, ActivityManager.getCurrentUser()) != 0) {
            return true;
        }
        return false;
    }

    public boolean useApSensor() {
        if (Settings.Secure.getIntForUser(this.contentResolver, "columbus_ap_sensor", 0, ActivityManager.getCurrentUser()) != 0) {
            return true;
        }
        return false;
    }

    public String selectedAction() {
        String stringForUser = Settings.Secure.getStringForUser(this.contentResolver, "columbus_action", ActivityManager.getCurrentUser());
        return stringForUser == null ? "" : stringForUser;
    }

    public String selectedApp() {
        String stringForUser = Settings.Secure.getStringForUser(this.contentResolver, "columbus_launch_app", ActivityManager.getCurrentUser());
        return stringForUser == null ? "" : stringForUser;
    }

    public String selectedAppShortcut() {
        String stringForUser = Settings.Secure.getStringForUser(this.contentResolver, "columbus_launch_app_shortcut", ActivityManager.getCurrentUser());
        return stringForUser == null ? "" : stringForUser;
    }

    public boolean useLowSensitivity() {
        if (Settings.Secure.getIntForUser(this.contentResolver, "columbus_low_sensitivity", 0, ActivityManager.getCurrentUser()) != 0) {
            return true;
        }
        return false;
    }

    public boolean silenceAlertsEnabled() {
        if (Settings.Secure.getIntForUser(this.contentResolver, "columbus_silence_alerts", 1, ActivityManager.getCurrentUser()) != 0) {
            return true;
        }
        return false;
    }

    /* compiled from: ColumbusSettings.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    static {
        Uri uriFor = Settings.Secure.getUriFor("columbus_enabled");
        COLUMBUS_ENABLED_URI = uriFor;
        Uri uriFor2 = Settings.Secure.getUriFor("columbus_ap_sensor");
        COLUMBUS_AP_SENSOR_URI = uriFor2;
        Uri uriFor3 = Settings.Secure.getUriFor("columbus_action");
        COLUMBUS_ACTION_URI = uriFor3;
        Uri uriFor4 = Settings.Secure.getUriFor("columbus_launch_app");
        COLUMBUS_LAUNCH_APP_URI = uriFor4;
        Uri uriFor5 = Settings.Secure.getUriFor("columbus_launch_app_shortcut");
        COLUMBUS_LAUNCH_APP_SHORTCUT_URI = uriFor5;
        Uri uriFor6 = Settings.Secure.getUriFor("columbus_low_sensitivity");
        COLUMBUS_LOW_SENSITIVITY_URI = uriFor6;
        Uri uriFor7 = Settings.Secure.getUriFor("columbus_silence_alerts");
        COLUMBUS_SILENCE_ALERTS_URI = uriFor7;
        MONITORED_URIS = SetsKt__SetsKt.setOf((Object[]) new Uri[]{uriFor, uriFor2, uriFor3, uriFor4, uriFor5, uriFor6, uriFor7});
    }
}
