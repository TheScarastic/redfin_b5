package com.android.systemui.qs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.os.UserManager;
import android.service.quicksettings.Tile;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dumpable;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.qs.QSFactory;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.external.CustomTileStatePersister;
import com.android.systemui.qs.external.TileLifecycleManager;
import com.android.systemui.qs.external.TileServiceKey;
import com.android.systemui.qs.external.TileServices;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.settings.SecureSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.inject.Provider;
/* loaded from: classes.dex */
public class QSTileHost implements QSHost, TunerService.Tunable, PluginListener<QSFactory>, Dumpable {
    private static final boolean DEBUG = Log.isLoggable("QSTileHost", 3);
    private AutoTileManager mAutoTiles;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final Context mContext;
    private int mCurrentUser;
    private final CustomTileStatePersister mCustomTileStatePersister;
    private final DumpManager mDumpManager;
    private final StatusBarIconController mIconController;
    private final PluginManager mPluginManager;
    private final QSLogger mQSLogger;
    private final ArrayList<QSFactory> mQsFactories;
    private SecureSettings mSecureSettings;
    private final TileServices mServices;
    private final Optional<StatusBar> mStatusBarOptional;
    private final TunerService mTunerService;
    private final UiEventLogger mUiEventLogger;
    private Context mUserContext;
    private UserTracker mUserTracker;
    private final LinkedHashMap<String, QSTile> mTiles = new LinkedHashMap<>();
    protected final ArrayList<String> mTileSpecs = new ArrayList<>();
    private final List<QSHost.Callback> mCallbacks = new ArrayList();
    private final InstanceIdSequence mInstanceIdSequence = new InstanceIdSequence(1048576);

    @Override // com.android.systemui.qs.QSHost
    public void warn(String str, Throwable th) {
    }

    public QSTileHost(Context context, StatusBarIconController statusBarIconController, QSFactory qSFactory, Handler handler, Looper looper, PluginManager pluginManager, TunerService tunerService, Provider<AutoTileManager> provider, DumpManager dumpManager, BroadcastDispatcher broadcastDispatcher, Optional<StatusBar> optional, QSLogger qSLogger, UiEventLogger uiEventLogger, UserTracker userTracker, SecureSettings secureSettings, CustomTileStatePersister customTileStatePersister) {
        ArrayList<QSFactory> arrayList = new ArrayList<>();
        this.mQsFactories = arrayList;
        this.mIconController = statusBarIconController;
        this.mContext = context;
        this.mUserContext = context;
        this.mTunerService = tunerService;
        this.mPluginManager = pluginManager;
        this.mDumpManager = dumpManager;
        this.mQSLogger = qSLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mServices = new TileServices(this, looper, broadcastDispatcher, userTracker);
        this.mStatusBarOptional = optional;
        arrayList.add(qSFactory);
        pluginManager.addPluginListener((PluginListener) this, QSFactory.class, true);
        dumpManager.registerDumpable("QSTileHost", this);
        this.mUserTracker = userTracker;
        this.mSecureSettings = secureSettings;
        this.mCustomTileStatePersister = customTileStatePersister;
        handler.post(new Runnable(tunerService, provider) { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda0
            public final /* synthetic */ TunerService f$1;
            public final /* synthetic */ Provider f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                QSTileHost.this.lambda$new$0(this.f$1, this.f$2);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(TunerService tunerService, Provider provider) {
        tunerService.addTunable(this, "sysui_qs_tiles");
        this.mAutoTiles = (AutoTileManager) provider.get();
    }

    public StatusBarIconController getIconController() {
        return this.mIconController;
    }

    @Override // com.android.systemui.qs.QSHost
    public InstanceId getNewInstanceId() {
        return this.mInstanceIdSequence.newInstanceId();
    }

    public void onPluginConnected(QSFactory qSFactory, Context context) {
        this.mQsFactories.add(0, qSFactory);
        String value = this.mTunerService.getValue("sysui_qs_tiles");
        onTuningChanged("sysui_qs_tiles", "");
        onTuningChanged("sysui_qs_tiles", value);
    }

    public void onPluginDisconnected(QSFactory qSFactory) {
        this.mQsFactories.remove(qSFactory);
        String value = this.mTunerService.getValue("sysui_qs_tiles");
        onTuningChanged("sysui_qs_tiles", "");
        onTuningChanged("sysui_qs_tiles", value);
    }

    @Override // com.android.systemui.qs.QSHost
    public UiEventLogger getUiEventLogger() {
        return this.mUiEventLogger;
    }

    public void addCallback(QSHost.Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(QSHost.Callback callback) {
        this.mCallbacks.remove(callback);
    }

    public Collection<QSTile> getTiles() {
        return this.mTiles.values();
    }

    @Override // com.android.systemui.qs.QSHost
    public void collapsePanels() {
        this.mStatusBarOptional.ifPresent(QSTileHost$$ExternalSyntheticLambda3.INSTANCE);
    }

    public void forceCollapsePanels() {
        this.mStatusBarOptional.ifPresent(QSTileHost$$ExternalSyntheticLambda4.INSTANCE);
    }

    @Override // com.android.systemui.qs.QSHost
    public void openPanels() {
        this.mStatusBarOptional.ifPresent(QSTileHost$$ExternalSyntheticLambda5.INSTANCE);
    }

    @Override // com.android.systemui.qs.QSHost
    public Context getContext() {
        return this.mContext;
    }

    @Override // com.android.systemui.qs.QSHost
    public Context getUserContext() {
        return this.mUserContext;
    }

    @Override // com.android.systemui.qs.QSHost
    public int getUserId() {
        return this.mCurrentUser;
    }

    @Override // com.android.systemui.qs.QSHost
    public TileServices getTileServices() {
        return this.mServices;
    }

    @Override // com.android.systemui.qs.QSHost
    public int indexOf(String str) {
        return this.mTileSpecs.indexOf(str);
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        boolean z;
        if ("sysui_qs_tiles".equals(str)) {
            Log.d("QSTileHost", "Recreating tiles");
            if (str2 == null && UserManager.isDeviceInDemoMode(this.mContext)) {
                str2 = this.mContext.getResources().getString(R$string.quick_settings_tiles_retail_mode);
            }
            List<String> loadTileSpecs = loadTileSpecs(this.mContext, str2);
            int userId = this.mUserTracker.getUserId();
            if (userId != this.mCurrentUser) {
                this.mUserContext = this.mUserTracker.getUserContext();
                AutoTileManager autoTileManager = this.mAutoTiles;
                if (autoTileManager != null) {
                    autoTileManager.lambda$changeUser$0(UserHandle.of(userId));
                }
            }
            if (!loadTileSpecs.equals(this.mTileSpecs) || userId != this.mCurrentUser) {
                this.mTiles.entrySet().stream().filter(new Predicate(loadTileSpecs) { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda8
                    public final /* synthetic */ List f$0;

                    {
                        this.f$0 = r1;
                    }

                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return QSTileHost.lambda$onTuningChanged$2(this.f$0, (Map.Entry) obj);
                    }
                }).forEach(new Consumer() { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        QSTileHost.this.lambda$onTuningChanged$3((Map.Entry) obj);
                    }
                });
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                for (String str3 : loadTileSpecs) {
                    QSTile qSTile = this.mTiles.get(str3);
                    if (qSTile == null || (((z = qSTile instanceof CustomTile)) && ((CustomTile) qSTile).getUser() != userId)) {
                        if (qSTile != null) {
                            qSTile.destroy();
                            Log.d("QSTileHost", "Destroying tile for wrong user: " + str3);
                            this.mQSLogger.logTileDestroyed(str3, "Tile for wrong user");
                        }
                        Log.d("QSTileHost", "Creating tile: " + str3);
                        try {
                            QSTile createTile = createTile(str3);
                            if (createTile != null) {
                                createTile.setTileSpec(str3);
                                if (createTile.isAvailable()) {
                                    linkedHashMap.put(str3, createTile);
                                    this.mQSLogger.logTileAdded(str3);
                                } else {
                                    createTile.destroy();
                                    Log.d("QSTileHost", "Destroying not available tile: " + str3);
                                    this.mQSLogger.logTileDestroyed(str3, "Tile not available");
                                }
                            }
                        } catch (Throwable th) {
                            Log.w("QSTileHost", "Error creating tile for spec: " + str3, th);
                        }
                    } else if (qSTile.isAvailable()) {
                        if (DEBUG) {
                            Log.d("QSTileHost", "Adding " + qSTile);
                        }
                        qSTile.removeCallbacks();
                        if (!z && this.mCurrentUser != userId) {
                            qSTile.userSwitch(userId);
                        }
                        linkedHashMap.put(str3, qSTile);
                        this.mQSLogger.logTileAdded(str3);
                    } else {
                        qSTile.destroy();
                        Log.d("QSTileHost", "Destroying not available tile: " + str3);
                        this.mQSLogger.logTileDestroyed(str3, "Tile not available");
                    }
                }
                this.mCurrentUser = userId;
                ArrayList arrayList = new ArrayList(this.mTileSpecs);
                this.mTileSpecs.clear();
                this.mTileSpecs.addAll(loadTileSpecs);
                this.mTiles.clear();
                this.mTiles.putAll(linkedHashMap);
                if (!linkedHashMap.isEmpty() || loadTileSpecs.isEmpty()) {
                    for (int i = 0; i < this.mCallbacks.size(); i++) {
                        this.mCallbacks.get(i).onTilesChanged();
                    }
                    return;
                }
                Log.d("QSTileHost", "No valid tiles on tuning changed. Setting to default.");
                changeTiles(arrayList, loadTileSpecs(this.mContext, ""));
            }
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onTuningChanged$2(List list, Map.Entry entry) {
        return !list.contains(entry.getKey());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTuningChanged$3(Map.Entry entry) {
        Log.d("QSTileHost", "Destroying tile: " + ((String) entry.getKey()));
        this.mQSLogger.logTileDestroyed((String) entry.getKey(), "Tile removed");
        ((QSTile) entry.getValue()).destroy();
    }

    @Override // com.android.systemui.qs.QSHost
    public void removeTile(String str) {
        changeTileSpecs(new Predicate(str) { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda6
            public final /* synthetic */ String f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((List) obj).remove(this.f$0);
            }
        });
    }

    @Override // com.android.systemui.qs.QSHost
    public void unmarkTileAsAutoAdded(String str) {
        AutoTileManager autoTileManager = this.mAutoTiles;
        if (autoTileManager != null) {
            autoTileManager.unmarkTileAsAutoAdded(str);
        }
    }

    public void addTile(String str) {
        addTile(str, -1);
    }

    public void addTile(String str, int i) {
        changeTileSpecs(new Predicate(str, i) { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda7
            public final /* synthetic */ String f$0;
            public final /* synthetic */ int f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return QSTileHost.lambda$addTile$5(this.f$0, this.f$1, (List) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$addTile$5(String str, int i, List list) {
        if (list.contains(str)) {
            return false;
        }
        int size = list.size();
        if (i == -1 || i >= size) {
            list.add(str);
            return true;
        }
        list.add(i, str);
        return true;
    }

    void saveTilesToSettings(List<String> list) {
        this.mSecureSettings.putStringForUser("sysui_qs_tiles", TextUtils.join(",", list), null, false, this.mCurrentUser, true);
    }

    private void changeTileSpecs(Predicate<List<String>> predicate) {
        List<String> loadTileSpecs = loadTileSpecs(this.mContext, this.mSecureSettings.getStringForUser("sysui_qs_tiles", this.mCurrentUser));
        if (predicate.test(loadTileSpecs)) {
            saveTilesToSettings(loadTileSpecs);
        }
    }

    public void addTile(ComponentName componentName) {
        addTile(componentName, false);
    }

    public void addTile(ComponentName componentName, boolean z) {
        String spec = CustomTile.toSpec(componentName);
        if (!this.mTileSpecs.contains(spec)) {
            ArrayList arrayList = new ArrayList(this.mTileSpecs);
            if (z) {
                arrayList.add(spec);
            } else {
                arrayList.add(0, spec);
            }
            changeTiles(this.mTileSpecs, arrayList);
        }
    }

    public void removeTile(ComponentName componentName) {
        ArrayList arrayList = new ArrayList(this.mTileSpecs);
        arrayList.remove(CustomTile.toSpec(componentName));
        changeTiles(this.mTileSpecs, arrayList);
    }

    public void changeTiles(List<String> list, List<String> list2) {
        ArrayList arrayList = new ArrayList(list);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            String str = (String) arrayList.get(i);
            if (str.startsWith("custom(") && !list2.contains(str)) {
                ComponentName componentFromSpec = CustomTile.getComponentFromSpec(str);
                TileLifecycleManager tileLifecycleManager = new TileLifecycleManager(new Handler(), this.mContext, this.mServices, new Tile(), new Intent().setComponent(componentFromSpec), new UserHandle(this.mCurrentUser), this.mBroadcastDispatcher);
                tileLifecycleManager.onStopListening();
                tileLifecycleManager.onTileRemoved();
                this.mCustomTileStatePersister.removeState(new TileServiceKey(componentFromSpec, this.mCurrentUser));
                TileLifecycleManager.setTileAdded(this.mContext, componentFromSpec, false);
                tileLifecycleManager.flushMessagesAndUnbind();
            }
        }
        if (DEBUG) {
            Log.d("QSTileHost", "saveCurrentTiles " + list2);
        }
        saveTilesToSettings(list2);
    }

    public QSTile createTile(String str) {
        for (int i = 0; i < this.mQsFactories.size(); i++) {
            QSTile createTile = this.mQsFactories.get(i).createTile(str);
            if (createTile != null) {
                return createTile;
            }
        }
        return null;
    }

    public QSTileView createTileView(Context context, QSTile qSTile, boolean z) {
        for (int i = 0; i < this.mQsFactories.size(); i++) {
            QSTileView createTileView = this.mQsFactories.get(i).createTileView(context, qSTile, z);
            if (createTileView != null) {
                return createTileView;
            }
        }
        throw new RuntimeException("Default factory didn't create view for " + qSTile.getTileSpec());
    }

    protected static List<String> loadTileSpecs(Context context, String str) {
        Resources resources = context.getResources();
        if (TextUtils.isEmpty(str)) {
            str = resources.getString(R$string.quick_settings_tiles);
            if (DEBUG) {
                Log.d("QSTileHost", "Loaded tile specs from config: " + str);
            }
        } else if (DEBUG) {
            Log.d("QSTileHost", "Loaded tile specs from setting: " + str);
        }
        ArrayList arrayList = new ArrayList();
        ArraySet arraySet = new ArraySet();
        boolean z = false;
        for (String str2 : str.split(",")) {
            String trim = str2.trim();
            if (!trim.isEmpty()) {
                if (trim.equals("default")) {
                    if (!z) {
                        for (String str3 : getDefaultSpecs(context)) {
                            if (!arraySet.contains(str3)) {
                                arrayList.add(str3);
                                arraySet.add(str3);
                            }
                        }
                        z = true;
                    }
                } else if (!arraySet.contains(trim)) {
                    arrayList.add(trim);
                    arraySet.add(trim);
                }
            }
        }
        return arrayList;
    }

    public static List<String> getDefaultSpecs(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(Arrays.asList(context.getResources().getString(R$string.quick_settings_tiles_default).split(",")));
        if (Build.IS_DEBUGGABLE) {
            arrayList.add("dbg:mem");
        }
        return arrayList;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("QSTileHost:");
        this.mTiles.values().stream().filter(QSTileHost$$ExternalSyntheticLambda9.INSTANCE).forEach(new Consumer(fileDescriptor, printWriter, strArr) { // from class: com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda2
            public final /* synthetic */ FileDescriptor f$0;
            public final /* synthetic */ PrintWriter f$1;
            public final /* synthetic */ String[] f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                QSTileHost.lambda$dump$7(this.f$0, this.f$1, this.f$2, (QSTile) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$dump$6(QSTile qSTile) {
        return qSTile instanceof Dumpable;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$7(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, QSTile qSTile) {
        ((Dumpable) qSTile).dump(fileDescriptor, printWriter, strArr);
    }
}
