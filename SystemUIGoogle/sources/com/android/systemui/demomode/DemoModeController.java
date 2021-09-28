package com.android.systemui.demomode;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.UserHandle;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.settings.GlobalSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DemoModeController.kt */
/* loaded from: classes.dex */
public final class DemoModeController implements CallbackController<DemoMode>, Dumpable {
    private final DemoModeController$broadcastReceiver$1 broadcastReceiver;
    private final Context context;
    private final DumpManager dumpManager;
    private final GlobalSettings globalSettings;
    private boolean initialized;
    private boolean isInDemoMode;
    private final Map<String, List<DemoMode>> receiverMap;
    private final List<DemoMode> receivers = new ArrayList();
    private final DemoModeController$tracker$1 tracker;

    public DemoModeController(Context context, DumpManager dumpManager, GlobalSettings globalSettings) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(globalSettings, "globalSettings");
        this.context = context;
        this.dumpManager = dumpManager;
        this.globalSettings = globalSettings;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        List<String> list = DemoMode.COMMANDS;
        Intrinsics.checkNotNullExpressionValue(list, "COMMANDS");
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10));
        for (String str : list) {
            Intrinsics.checkNotNullExpressionValue(str, "command");
            arrayList.add((List) linkedHashMap.put(str, new ArrayList()));
        }
        this.receiverMap = linkedHashMap;
        this.tracker = new DemoModeController$tracker$1(this, this.context);
        this.broadcastReceiver = new DemoModeController$broadcastReceiver$1(this);
    }

    public final boolean isInDemoMode() {
        return this.isInDemoMode;
    }

    public final boolean isAvailable() {
        return this.tracker.isDemoModeAvailable();
    }

    public final void initialize() {
        if (!this.initialized) {
            this.initialized = true;
            this.dumpManager.registerDumpable("DemoModeController", this);
            this.tracker.startTracking();
            this.isInDemoMode = this.tracker.isInDemoMode();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.android.systemui.demo");
            this.context.registerReceiverAsUser(this.broadcastReceiver, UserHandle.ALL, intentFilter, "android.permission.DUMP", null);
            return;
        }
        throw new IllegalStateException("Already initialized");
    }

    public void addCallback(DemoMode demoMode) {
        Intrinsics.checkNotNullParameter(demoMode, "listener");
        List<String> demoCommands = demoMode.demoCommands();
        Intrinsics.checkNotNullExpressionValue(demoCommands, "commands");
        for (String str : demoCommands) {
            if (this.receiverMap.containsKey(str)) {
                List<DemoMode> list = this.receiverMap.get(str);
                Intrinsics.checkNotNull(list);
                list.add(demoMode);
            } else {
                throw new IllegalStateException("Command (" + ((Object) str) + ") not recognized. See DemoMode.java for valid commands");
            }
        }
        synchronized (this) {
            this.receivers.add(demoMode);
        }
        if (this.isInDemoMode) {
            demoMode.onDemoModeStarted();
        }
    }

    public void removeCallback(DemoMode demoMode) {
        Intrinsics.checkNotNullParameter(demoMode, "listener");
        synchronized (this) {
            List<String> demoCommands = demoMode.demoCommands();
            Intrinsics.checkNotNullExpressionValue(demoCommands, "listener.demoCommands()");
            for (String str : demoCommands) {
                List<DemoMode> list = this.receiverMap.get(str);
                Intrinsics.checkNotNull(list);
                list.remove(demoMode);
            }
            this.receivers.remove(demoMode);
        }
    }

    /* access modifiers changed from: private */
    public final void setIsDemoModeAllowed(boolean z) {
        if (this.isInDemoMode && !z) {
            requestFinishDemoMode();
        }
    }

    /* access modifiers changed from: private */
    public final void enterDemoMode() {
        List<DemoModeCommandReceiver> list;
        this.isInDemoMode = true;
        Assert.isMainThread();
        synchronized (this) {
            list = CollectionsKt___CollectionsKt.toList(this.receivers);
            Unit unit = Unit.INSTANCE;
        }
        for (DemoModeCommandReceiver demoModeCommandReceiver : list) {
            demoModeCommandReceiver.onDemoModeStarted();
        }
    }

    /* access modifiers changed from: private */
    public final void exitDemoMode() {
        List<DemoModeCommandReceiver> list;
        this.isInDemoMode = false;
        Assert.isMainThread();
        synchronized (this) {
            list = CollectionsKt___CollectionsKt.toList(this.receivers);
            Unit unit = Unit.INSTANCE;
        }
        for (DemoModeCommandReceiver demoModeCommandReceiver : list) {
            demoModeCommandReceiver.onDemoModeFinished();
        }
    }

    public final void dispatchDemoCommand(String str, Bundle bundle) {
        Intrinsics.checkNotNullParameter(str, "command");
        Intrinsics.checkNotNullParameter(bundle, "args");
        Assert.isMainThread();
        if (isAvailable()) {
            if (Intrinsics.areEqual(str, "enter")) {
                enterDemoMode();
            } else if (Intrinsics.areEqual(str, "exit")) {
                exitDemoMode();
            } else if (!this.isInDemoMode) {
                enterDemoMode();
            }
            List<DemoMode> list = this.receiverMap.get(str);
            Intrinsics.checkNotNull(list);
            for (DemoMode demoMode : list) {
                demoMode.dispatchDemoCommand(str, bundle);
            }
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        List<DemoModeCommandReceiver> list;
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("DemoModeController state -");
        printWriter.println(Intrinsics.stringPlus("  isInDemoMode=", Boolean.valueOf(this.isInDemoMode)));
        printWriter.println(Intrinsics.stringPlus("  isDemoModeAllowed=", Boolean.valueOf(isAvailable())));
        printWriter.print("  receivers=[");
        synchronized (this) {
            list = CollectionsKt___CollectionsKt.toList(this.receivers);
            Unit unit = Unit.INSTANCE;
        }
        for (DemoModeCommandReceiver demoModeCommandReceiver : list) {
            printWriter.print(Intrinsics.stringPlus(" ", demoModeCommandReceiver.getClass().getSimpleName()));
        }
        printWriter.println(" ]");
        printWriter.println("  receiverMap= [");
        for (String str : this.receiverMap.keySet()) {
            printWriter.print("    " + str + " : [");
            List<DemoMode> list2 = this.receiverMap.get(str);
            Intrinsics.checkNotNull(list2);
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(list2, 10));
            for (DemoMode demoMode : list2) {
                arrayList.add(demoMode.getClass().getSimpleName());
            }
            printWriter.println(Intrinsics.stringPlus(CollectionsKt___CollectionsKt.joinToString$default(arrayList, ",", null, null, 0, null, null, 62, null), " ]"));
        }
    }

    public final void requestSetDemoModeAllowed(boolean z) {
        setGlobal("sysui_demo_allowed", z ? 1 : 0);
    }

    public final void requestStartDemoMode() {
        setGlobal("sysui_tuner_demo_on", 1);
    }

    public final void requestFinishDemoMode() {
        setGlobal("sysui_tuner_demo_on", 0);
    }

    private final void setGlobal(String str, int i) {
        this.globalSettings.putInt(str, i);
    }
}
