package com.android.systemui.theme;

import android.content.om.FabricatedOverlay;
import android.content.om.OverlayIdentifier;
import android.content.om.OverlayInfo;
import android.content.om.OverlayManager;
import android.content.om.OverlayManagerTransaction;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.google.android.collect.Lists;
import com.google.android.collect.Sets;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/* loaded from: classes2.dex */
public class ThemeOverlayApplier implements Dumpable {
    static final String ANDROID_PACKAGE = "android";
    static final String SETTINGS_PACKAGE = "com.android.settings";
    static final String SYSUI_PACKAGE = "com.android.systemui";
    private final Map<String, String> mCategoryToTargetPackage;
    private final Executor mExecutor;
    private final String mLauncherPackage;
    private final OverlayManager mOverlayManager;
    private final Map<String, Set<String>> mTargetPackageToCategories;
    private final String mThemePickerPackage;
    private static final boolean DEBUG = Log.isLoggable("ThemeOverlayApplier", 3);
    static final String OVERLAY_CATEGORY_ICON_LAUNCHER = "android.theme.customization.icon_pack.launcher";
    static final String OVERLAY_CATEGORY_SHAPE = "android.theme.customization.adaptive_icon_shape";
    static final String OVERLAY_CATEGORY_FONT = "android.theme.customization.font";
    static final String OVERLAY_CATEGORY_ICON_ANDROID = "android.theme.customization.icon_pack.android";
    static final String OVERLAY_CATEGORY_ICON_SYSUI = "android.theme.customization.icon_pack.systemui";
    static final String OVERLAY_CATEGORY_ICON_SETTINGS = "android.theme.customization.icon_pack.settings";
    static final String OVERLAY_CATEGORY_ICON_THEME_PICKER = "android.theme.customization.icon_pack.themepicker";
    static final List<String> THEME_CATEGORIES = Lists.newArrayList(new String[]{"android.theme.customization.system_palette", OVERLAY_CATEGORY_ICON_LAUNCHER, OVERLAY_CATEGORY_SHAPE, OVERLAY_CATEGORY_FONT, "android.theme.customization.accent_color", OVERLAY_CATEGORY_ICON_ANDROID, OVERLAY_CATEGORY_ICON_SYSUI, OVERLAY_CATEGORY_ICON_SETTINGS, OVERLAY_CATEGORY_ICON_THEME_PICKER});
    static final Set<String> SYSTEM_USER_CATEGORIES = Sets.newHashSet(new String[]{"android.theme.customization.system_palette", "android.theme.customization.accent_color", OVERLAY_CATEGORY_FONT, OVERLAY_CATEGORY_SHAPE, OVERLAY_CATEGORY_ICON_ANDROID, OVERLAY_CATEGORY_ICON_SYSUI});

    public ThemeOverlayApplier(OverlayManager overlayManager, Executor executor, String str, String str2, DumpManager dumpManager) {
        ArrayMap arrayMap = new ArrayMap();
        this.mTargetPackageToCategories = arrayMap;
        ArrayMap arrayMap2 = new ArrayMap();
        this.mCategoryToTargetPackage = arrayMap2;
        this.mOverlayManager = overlayManager;
        this.mExecutor = executor;
        this.mLauncherPackage = str;
        this.mThemePickerPackage = str2;
        arrayMap.put(ANDROID_PACKAGE, Sets.newHashSet(new String[]{"android.theme.customization.system_palette", "android.theme.customization.accent_color", OVERLAY_CATEGORY_FONT, OVERLAY_CATEGORY_SHAPE, OVERLAY_CATEGORY_ICON_ANDROID}));
        arrayMap.put(SYSUI_PACKAGE, Sets.newHashSet(new String[]{OVERLAY_CATEGORY_ICON_SYSUI}));
        arrayMap.put(SETTINGS_PACKAGE, Sets.newHashSet(new String[]{OVERLAY_CATEGORY_ICON_SETTINGS}));
        arrayMap.put(str, Sets.newHashSet(new String[]{OVERLAY_CATEGORY_ICON_LAUNCHER}));
        arrayMap.put(str2, Sets.newHashSet(new String[]{OVERLAY_CATEGORY_ICON_THEME_PICKER}));
        arrayMap2.put("android.theme.customization.accent_color", ANDROID_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_FONT, ANDROID_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_SHAPE, ANDROID_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_ANDROID, ANDROID_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_SYSUI, SYSUI_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_SETTINGS, SETTINGS_PACKAGE);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_LAUNCHER, str);
        arrayMap2.put(OVERLAY_CATEGORY_ICON_THEME_PICKER, str2);
        dumpManager.registerDumpable("ThemeOverlayApplier", this);
    }

    /* access modifiers changed from: package-private */
    public void applyCurrentUserOverlays(Map<String, OverlayIdentifier> map, FabricatedOverlay[] fabricatedOverlayArr, int i, Set<UserHandle> set) {
        this.mExecutor.execute(new Runnable(map, fabricatedOverlayArr, i, set) { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda0
            public final /* synthetic */ Map f$1;
            public final /* synthetic */ FabricatedOverlay[] f$2;
            public final /* synthetic */ int f$3;
            public final /* synthetic */ Set f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ThemeOverlayApplier.this.lambda$applyCurrentUserOverlays$7(this.f$1, this.f$2, this.f$3, this.f$4);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$applyCurrentUserOverlays$7(Map map, FabricatedOverlay[] fabricatedOverlayArr, int i, Set set) {
        HashSet hashSet = new HashSet(THEME_CATEGORIES);
        ArrayList arrayList = new ArrayList();
        ((Set) hashSet.stream().map(new Function() { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ThemeOverlayApplier.this.lambda$applyCurrentUserOverlays$0((String) obj);
            }
        }).collect(Collectors.toSet())).forEach(new Consumer(arrayList) { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda1
            public final /* synthetic */ List f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ThemeOverlayApplier.this.lambda$applyCurrentUserOverlays$1(this.f$1, (String) obj);
            }
        });
        List<Pair> list = (List) arrayList.stream().filter(new Predicate() { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ThemeOverlayApplier.this.lambda$applyCurrentUserOverlays$2((OverlayInfo) obj);
            }
        }).filter(new Predicate(hashSet) { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda6
            public final /* synthetic */ Set f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ThemeOverlayApplier.lambda$applyCurrentUserOverlays$3(this.f$0, (OverlayInfo) obj);
            }
        }).filter(new Predicate(map) { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda5
            public final /* synthetic */ Map f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ThemeOverlayApplier.lambda$applyCurrentUserOverlays$4(this.f$0, (OverlayInfo) obj);
            }
        }).filter(ThemeOverlayApplier$$ExternalSyntheticLambda7.INSTANCE).map(ThemeOverlayApplier$$ExternalSyntheticLambda3.INSTANCE).collect(Collectors.toList());
        OverlayManagerTransaction.Builder transactionBuilder = getTransactionBuilder();
        HashSet hashSet2 = new HashSet();
        if (fabricatedOverlayArr != null) {
            for (FabricatedOverlay fabricatedOverlay : fabricatedOverlayArr) {
                hashSet2.add(fabricatedOverlay.getIdentifier());
                transactionBuilder.registerFabricatedOverlay(fabricatedOverlay);
            }
        }
        for (Pair pair : list) {
            OverlayIdentifier overlayIdentifier = new OverlayIdentifier((String) pair.second);
            setEnabled(transactionBuilder, overlayIdentifier, (String) pair.first, i, set, false, hashSet2.contains(overlayIdentifier));
        }
        for (String str : THEME_CATEGORIES) {
            if (map.containsKey(str)) {
                OverlayIdentifier overlayIdentifier2 = (OverlayIdentifier) map.get(str);
                setEnabled(transactionBuilder, overlayIdentifier2, str, i, set, true, hashSet2.contains(overlayIdentifier2));
            }
        }
        try {
            this.mOverlayManager.commit(transactionBuilder.build());
        } catch (IllegalStateException | SecurityException e) {
            Log.e("ThemeOverlayApplier", "setEnabled failed", e);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$applyCurrentUserOverlays$0(String str) {
        return this.mCategoryToTargetPackage.get(str);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$applyCurrentUserOverlays$1(List list, String str) {
        list.addAll(this.mOverlayManager.getOverlayInfosForTarget(str, UserHandle.SYSTEM));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$applyCurrentUserOverlays$2(OverlayInfo overlayInfo) {
        return this.mTargetPackageToCategories.get(overlayInfo.targetPackageName).contains(overlayInfo.category);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$applyCurrentUserOverlays$3(Set set, OverlayInfo overlayInfo) {
        return set.contains(overlayInfo.category);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$applyCurrentUserOverlays$4(Map map, OverlayInfo overlayInfo) {
        return !map.containsValue(new OverlayIdentifier(overlayInfo.packageName));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Pair lambda$applyCurrentUserOverlays$6(OverlayInfo overlayInfo) {
        return new Pair(overlayInfo.category, overlayInfo.packageName);
    }

    protected OverlayManagerTransaction.Builder getTransactionBuilder() {
        return new OverlayManagerTransaction.Builder();
    }

    private void setEnabled(OverlayManagerTransaction.Builder builder, OverlayIdentifier overlayIdentifier, String str, int i, Set<UserHandle> set, boolean z, boolean z2) {
        if (DEBUG) {
            Log.d("ThemeOverlayApplier", "setEnabled: " + overlayIdentifier.getPackageName() + " category: " + str + ": " + z);
        }
        if (this.mOverlayManager.getOverlayInfo(overlayIdentifier, UserHandle.of(i)) != null || z2) {
            builder.setEnabled(overlayIdentifier, z, i);
            if (i != UserHandle.SYSTEM.getIdentifier() && SYSTEM_USER_CATEGORIES.contains(str)) {
                builder.setEnabled(overlayIdentifier, z, UserHandle.SYSTEM.getIdentifier());
            }
            OverlayInfo overlayInfo = this.mOverlayManager.getOverlayInfo(overlayIdentifier, UserHandle.SYSTEM);
            if (!(overlayInfo == null || overlayInfo.targetPackageName.equals(this.mLauncherPackage) || overlayInfo.targetPackageName.equals(this.mThemePickerPackage))) {
                for (UserHandle userHandle : set) {
                    builder.setEnabled(overlayIdentifier, z, userHandle.getIdentifier());
                }
                return;
            }
            return;
        }
        Log.i("ThemeOverlayApplier", "Won't enable " + overlayIdentifier + ", it doesn't exist for user" + i);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("mTargetPackageToCategories=" + this.mTargetPackageToCategories);
        printWriter.println("mCategoryToTargetPackage=" + this.mCategoryToTargetPackage);
    }
}
