package com.android.systemui.privacy;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.permission.PermGroupUsage;
import android.permission.PermissionManager;
import android.util.Log;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PrivacyDialogController.kt */
/* loaded from: classes.dex */
public final class PrivacyDialogController {
    public static final Companion Companion = new Companion(null);
    private final ActivityStarter activityStarter;
    private final AppOpsController appOpsController;
    private final Executor backgroundExecutor;
    private Dialog dialog;
    private final DialogProvider dialogProvider;
    private final KeyguardStateController keyguardStateController;
    private final PrivacyDialogController$onDialogDismissed$1 onDialogDismissed;
    private final PackageManager packageManager;
    private final PermissionManager permissionManager;
    private final PrivacyItemController privacyItemController;
    private final PrivacyLogger privacyLogger;
    private final Executor uiExecutor;
    private final UserTracker userTracker;

    /* compiled from: PrivacyDialogController.kt */
    /* loaded from: classes.dex */
    public interface DialogProvider {
        PrivacyDialog makeDialog(Context context, List<PrivacyDialog.PrivacyElement> list, Function2<? super String, ? super Integer, Unit> function2);
    }

    public PrivacyDialogController(PermissionManager permissionManager, PackageManager packageManager, PrivacyItemController privacyItemController, UserTracker userTracker, ActivityStarter activityStarter, Executor executor, Executor executor2, PrivacyLogger privacyLogger, KeyguardStateController keyguardStateController, AppOpsController appOpsController, DialogProvider dialogProvider) {
        Intrinsics.checkNotNullParameter(permissionManager, "permissionManager");
        Intrinsics.checkNotNullParameter(packageManager, "packageManager");
        Intrinsics.checkNotNullParameter(privacyItemController, "privacyItemController");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(executor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(executor2, "uiExecutor");
        Intrinsics.checkNotNullParameter(privacyLogger, "privacyLogger");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(appOpsController, "appOpsController");
        Intrinsics.checkNotNullParameter(dialogProvider, "dialogProvider");
        this.permissionManager = permissionManager;
        this.packageManager = packageManager;
        this.privacyItemController = privacyItemController;
        this.userTracker = userTracker;
        this.activityStarter = activityStarter;
        this.backgroundExecutor = executor;
        this.uiExecutor = executor2;
        this.privacyLogger = privacyLogger;
        this.keyguardStateController = keyguardStateController;
        this.appOpsController = appOpsController;
        this.dialogProvider = dialogProvider;
        this.onDialogDismissed = new PrivacyDialogController$onDialogDismissed$1(this);
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public PrivacyDialogController(PermissionManager permissionManager, PackageManager packageManager, PrivacyItemController privacyItemController, UserTracker userTracker, ActivityStarter activityStarter, Executor executor, Executor executor2, PrivacyLogger privacyLogger, KeyguardStateController keyguardStateController, AppOpsController appOpsController) {
        this(permissionManager, packageManager, privacyItemController, userTracker, activityStarter, executor, executor2, privacyLogger, keyguardStateController, appOpsController, PrivacyDialogControllerKt.defaultDialogProvider);
        Intrinsics.checkNotNullParameter(permissionManager, "permissionManager");
        Intrinsics.checkNotNullParameter(packageManager, "packageManager");
        Intrinsics.checkNotNullParameter(privacyItemController, "privacyItemController");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(executor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(executor2, "uiExecutor");
        Intrinsics.checkNotNullParameter(privacyLogger, "privacyLogger");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(appOpsController, "appOpsController");
    }

    /* compiled from: PrivacyDialogController.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* access modifiers changed from: private */
    public final void startActivity(String str, int i) {
        Dialog dialog;
        Intent intent = new Intent("android.intent.action.MANAGE_APP_PERMISSIONS");
        intent.putExtra("android.intent.extra.PACKAGE_NAME", str);
        intent.putExtra("android.intent.extra.USER", UserHandle.of(i));
        this.privacyLogger.logStartSettingsActivityFromDialog(str, i);
        if (!this.keyguardStateController.isUnlocked() && (dialog = this.dialog) != null) {
            dialog.hide();
        }
        this.activityStarter.startActivity(intent, true, (ActivityStarter.Callback) new ActivityStarter.Callback(this) { // from class: com.android.systemui.privacy.PrivacyDialogController$startActivity$1
            final /* synthetic */ PrivacyDialogController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.plugins.ActivityStarter.Callback
            public final void onActivityStarted(int i2) {
                if (ActivityManager.isStartResultSuccessful(i2)) {
                    this.this$0.dismissDialog();
                    return;
                }
                Dialog dialog2 = this.this$0.dialog;
                if (dialog2 != null) {
                    dialog2.show();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public final List<PermGroupUsage> permGroupUsage() {
        List<PermGroupUsage> indicatorAppOpUsageData = this.permissionManager.getIndicatorAppOpUsageData(this.appOpsController.isMicMuted());
        Intrinsics.checkNotNullExpressionValue(indicatorAppOpUsageData, "permissionManager.getIndicatorAppOpUsageData(appOpsController.isMicMuted)");
        return indicatorAppOpUsageData;
    }

    public final void showDialog(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        dismissDialog();
        this.backgroundExecutor.execute(new Runnable(this, context) { // from class: com.android.systemui.privacy.PrivacyDialogController$showDialog$1
            final /* synthetic */ Context $context;
            final /* synthetic */ PrivacyDialogController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$context = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                PrivacyDialog.PrivacyElement privacyElement;
                Object obj;
                CharSequence charSequence;
                boolean z;
                List<PermGroupUsage> list = this.this$0.permGroupUsage();
                List<UserInfo> userProfiles = this.this$0.userTracker.getUserProfiles();
                this.this$0.privacyLogger.logUnfilteredPermGroupUsage(list);
                PrivacyDialogController privacyDialogController = this.this$0;
                final ArrayList arrayList = new ArrayList();
                for (PermGroupUsage permGroupUsage : list) {
                    String permGroupName = permGroupUsage.getPermGroupName();
                    Intrinsics.checkNotNullExpressionValue(permGroupName, "it.permGroupName");
                    PrivacyType privacyType = privacyDialogController.filterType(privacyDialogController.permGroupToPrivacyType(permGroupName));
                    Iterator<T> it = userProfiles.iterator();
                    while (true) {
                        privacyElement = null;
                        if (!it.hasNext()) {
                            obj = null;
                            break;
                        }
                        obj = it.next();
                        if (((UserInfo) obj).id == UserHandle.getUserId(permGroupUsage.getUid())) {
                            z = true;
                            continue;
                        } else {
                            z = false;
                            continue;
                        }
                        if (z) {
                            break;
                        }
                    }
                    UserInfo userInfo = (UserInfo) obj;
                    if (!(userInfo == null || privacyType == null)) {
                        if (permGroupUsage.isPhoneCall()) {
                            charSequence = "";
                        } else {
                            String packageName = permGroupUsage.getPackageName();
                            Intrinsics.checkNotNullExpressionValue(packageName, "it.packageName");
                            charSequence = privacyDialogController.getLabelForPackage(packageName, permGroupUsage.getUid());
                        }
                        String packageName2 = permGroupUsage.getPackageName();
                        Intrinsics.checkNotNullExpressionValue(packageName2, "it.packageName");
                        privacyElement = new PrivacyDialog.PrivacyElement(privacyType, packageName2, UserHandle.getUserId(permGroupUsage.getUid()), charSequence, permGroupUsage.getAttribution(), permGroupUsage.getLastAccess(), permGroupUsage.isActive(), userInfo.isManagedProfile(), permGroupUsage.isPhoneCall());
                    }
                    if (privacyElement != null) {
                        arrayList.add(privacyElement);
                    }
                }
                Executor executor = this.this$0.uiExecutor;
                final PrivacyDialogController privacyDialogController2 = this.this$0;
                final Context context2 = this.$context;
                executor.execute(new Runnable() { // from class: com.android.systemui.privacy.PrivacyDialogController$showDialog$1.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        List<PrivacyDialog.PrivacyElement> list2 = privacyDialogController2.filterAndSelect(arrayList);
                        if (!list2.isEmpty()) {
                            PrivacyDialog makeDialog = privacyDialogController2.dialogProvider.makeDialog(context2, list2, new PrivacyDialogController$showDialog$1$1$d$1(privacyDialogController2));
                            makeDialog.setShowForAllUsers(true);
                            makeDialog.addOnDismissListener(privacyDialogController2.onDialogDismissed);
                            makeDialog.show();
                            privacyDialogController2.privacyLogger.logShowDialogContents(list2);
                            privacyDialogController2.dialog = makeDialog;
                            return;
                        }
                        Log.w("PrivacyDialogController", "Trying to show empty dialog");
                    }
                });
            }
        });
    }

    public final void dismissDialog() {
        Dialog dialog = this.dialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /* access modifiers changed from: private */
    public final CharSequence getLabelForPackage(String str, int i) {
        try {
            CharSequence loadLabel = this.packageManager.getApplicationInfoAsUser(str, 0, UserHandle.getUserId(i)).loadLabel(this.packageManager);
            Intrinsics.checkNotNullExpressionValue(loadLabel, "{\n            packageManager\n                    .getApplicationInfoAsUser(packageName, 0, UserHandle.getUserId(uid))\n                    .loadLabel(packageManager)\n        }");
            return loadLabel;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.w("PrivacyDialogController", Intrinsics.stringPlus("Label not found for: ", str));
            return str;
        }
    }

    /* access modifiers changed from: private */
    public final PrivacyType permGroupToPrivacyType(String str) {
        int hashCode = str.hashCode();
        if (hashCode != -1140935117) {
            if (hashCode != 828638019) {
                if (hashCode == 1581272376 && str.equals("android.permission-group.MICROPHONE")) {
                    return PrivacyType.TYPE_MICROPHONE;
                }
            } else if (str.equals("android.permission-group.LOCATION")) {
                return PrivacyType.TYPE_LOCATION;
            }
        } else if (str.equals("android.permission-group.CAMERA")) {
            return PrivacyType.TYPE_CAMERA;
        }
        return null;
    }

    /* access modifiers changed from: private */
    public final PrivacyType filterType(PrivacyType privacyType) {
        if (privacyType == null) {
            return null;
        }
        if ((!(privacyType == PrivacyType.TYPE_CAMERA || privacyType == PrivacyType.TYPE_MICROPHONE) || !this.privacyItemController.getMicCameraAvailable()) && (privacyType != PrivacyType.TYPE_LOCATION || !this.privacyItemController.getLocationAvailable())) {
            privacyType = null;
        }
        return privacyType;
    }

    /* access modifiers changed from: private */
    public final List<PrivacyDialog.PrivacyElement> filterAndSelect(List<PrivacyDialog.PrivacyElement> list) {
        List list2;
        Object obj;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj2 : list) {
            PrivacyType type = ((PrivacyDialog.PrivacyElement) obj2).getType();
            Object obj3 = linkedHashMap.get(type);
            if (obj3 == null) {
                obj3 = new ArrayList();
                linkedHashMap.put(type, obj3);
            }
            ((List) obj3).add(obj2);
        }
        SortedMap sortedMap = MapsKt__MapsJVMKt.toSortedMap(linkedHashMap);
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : sortedMap.entrySet()) {
            List list3 = (List) entry.getValue();
            Intrinsics.checkNotNullExpressionValue(list3, "elements");
            ArrayList arrayList2 = new ArrayList();
            for (Object obj4 : list3) {
                if (((PrivacyDialog.PrivacyElement) obj4).getActive()) {
                    arrayList2.add(obj4);
                }
            }
            if (!arrayList2.isEmpty()) {
                list2 = CollectionsKt___CollectionsKt.sortedWith(arrayList2, new Comparator<T>() { // from class: com.android.systemui.privacy.PrivacyDialogController$filterAndSelect$lambda-6$$inlined$sortedByDescending$1
                    @Override // java.util.Comparator
                    public final int compare(T t, T t2) {
                        int compareValues;
                        compareValues = ComparisonsKt__ComparisonsKt.compareValues(Long.valueOf(((PrivacyDialog.PrivacyElement) t2).getLastActiveTimestamp()), Long.valueOf(((PrivacyDialog.PrivacyElement) t).getLastActiveTimestamp()));
                        return compareValues;
                    }
                });
            } else {
                Iterator it = list3.iterator();
                if (!it.hasNext()) {
                    obj = null;
                } else {
                    obj = it.next();
                    if (it.hasNext()) {
                        long lastActiveTimestamp = ((PrivacyDialog.PrivacyElement) obj).getLastActiveTimestamp();
                        do {
                            Object next = it.next();
                            long lastActiveTimestamp2 = ((PrivacyDialog.PrivacyElement) next).getLastActiveTimestamp();
                            if (lastActiveTimestamp < lastActiveTimestamp2) {
                                obj = next;
                                lastActiveTimestamp = lastActiveTimestamp2;
                            }
                        } while (it.hasNext());
                    }
                }
                PrivacyDialog.PrivacyElement privacyElement = (PrivacyDialog.PrivacyElement) obj;
                if (privacyElement == null) {
                    list2 = null;
                } else {
                    list2 = CollectionsKt__CollectionsJVMKt.listOf(privacyElement);
                }
                if (list2 == null) {
                    list2 = CollectionsKt__CollectionsKt.emptyList();
                }
            }
            boolean unused = CollectionsKt__MutableCollectionsKt.addAll(arrayList, list2);
        }
        return arrayList;
    }
}
