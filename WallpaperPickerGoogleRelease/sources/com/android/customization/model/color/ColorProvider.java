package com.android.customization.model.color;

import android.app.WallpaperColors;
import android.content.res.ColorStateList;
import androidx.core.graphics.ColorUtils;
import com.android.customization.model.ResourcesApkProvider;
import com.android.customization.model.color.ColorSeedOption;
import com.android.internal.graphics.cam.Cam;
import com.android.wallpaper.compat.WallpaperManagerCompatV16;
import com.android.wallpaper.module.InjectorProvider;
import com.google.material.monet.ColorScheme;
import com.google.material.monet.Shades;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.EmptyList;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class ColorProvider extends ResourcesApkProvider implements ColorOptionsProvider {
    @Nullable
    public List<? extends ColorOption> colorBundles;
    @Nullable
    public WallpaperColors homeWallpaperColors;
    @Nullable
    public WallpaperColors lockWallpaperColors;
    @NotNull
    public final CoroutineScope scope;

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:14:0x0067 */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX DEBUG: Type inference failed for r2v3. Raw type applied. Possible types: java.util.concurrent.atomic.AtomicReference<java.lang.Object>, java.util.concurrent.atomic.AtomicReference */
    /* JADX WARN: Type inference failed for: r10v2, types: [kotlinx.coroutines.CoroutineScope] */
    /* JADX WARN: Type inference failed for: r10v4, types: [kotlinx.coroutines.CoroutineScope] */
    /* JADX WARN: Type inference failed for: r10v8, types: [androidx.lifecycle.LifecycleCoroutineScopeImpl] */
    /* JADX WARN: Type inference failed for: r10v9, types: [java.lang.Object, androidx.lifecycle.LifecycleCoroutineScopeImpl, kotlinx.coroutines.CoroutineScope] */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ColorProvider(@org.jetbrains.annotations.NotNull android.content.Context r9, @org.jetbrains.annotations.NotNull java.lang.String r10) {
        /*
            r8 = this;
            java.lang.String r0 = "stubPackageName"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)
            r8.<init>(r9, r10)
            com.android.customization.model.color.ColorUtils.isMonetEnabled(r9)
            boolean r10 = r9 instanceof androidx.lifecycle.LifecycleOwner
            r0 = 1
            r1 = 0
            if (r10 == 0) goto L_0x0057
            androidx.lifecycle.LifecycleOwner r9 = (androidx.lifecycle.LifecycleOwner) r9
            androidx.lifecycle.Lifecycle r9 = r9.getLifecycle()
            java.lang.String r10 = "lifecycle"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r9, r10)
        L_0x001c:
            java.util.concurrent.atomic.AtomicReference<java.lang.Object> r10 = r9.mInternalScopeRef
            java.lang.Object r10 = r10.get()
            androidx.lifecycle.LifecycleCoroutineScopeImpl r10 = (androidx.lifecycle.LifecycleCoroutineScopeImpl) r10
            if (r10 == 0) goto L_0x0027
            goto L_0x0067
        L_0x0027:
            androidx.lifecycle.LifecycleCoroutineScopeImpl r10 = new androidx.lifecycle.LifecycleCoroutineScopeImpl
            kotlinx.coroutines.CompletableJob r2 = kotlinx.coroutines.JobKt.SupervisorJob$default(r1, r0)
            kotlinx.coroutines.CoroutineDispatcher r3 = kotlinx.coroutines.Dispatchers.Default
            kotlinx.coroutines.MainCoroutineDispatcher r3 = kotlinx.coroutines.internal.MainDispatcherLoader.dispatcher
            kotlinx.coroutines.MainCoroutineDispatcher r4 = r3.getImmediate()
            kotlinx.coroutines.JobSupport r2 = (kotlinx.coroutines.JobSupport) r2
            kotlin.coroutines.CoroutineContext r2 = r2.plus(r4)
            r10.<init>(r9, r2)
            java.util.concurrent.atomic.AtomicReference<java.lang.Object> r2 = r9.mInternalScopeRef
            boolean r2 = r2.compareAndSet(r1, r10)
            if (r2 == 0) goto L_0x001c
            kotlinx.coroutines.MainCoroutineDispatcher r3 = r3.getImmediate()
            r4 = 0
            androidx.lifecycle.LifecycleCoroutineScopeImpl$register$1 r5 = new androidx.lifecycle.LifecycleCoroutineScopeImpl$register$1
            r5.<init>(r10, r1)
            r6 = 2
            r7 = 0
            r2 = r10
            kotlinx.coroutines.BuildersKt.launch$default(r2, r3, r4, r5, r6, r7)
            goto L_0x0067
        L_0x0057:
            kotlinx.coroutines.Dispatchers r9 = kotlinx.coroutines.Dispatchers.INSTANCE
            kotlinx.coroutines.CoroutineDispatcher r9 = kotlinx.coroutines.Dispatchers.Default
            kotlinx.coroutines.CompletableJob r10 = kotlinx.coroutines.JobKt.SupervisorJob$default(r1, r0)
            kotlin.coroutines.CoroutineContext r9 = r9.plus(r10)
            kotlinx.coroutines.CoroutineScope r10 = kotlinx.coroutines.CoroutineScopeKt.CoroutineScope(r9)
        L_0x0067:
            r8.scope = r10
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.customization.model.color.ColorProvider.<init>(android.content.Context, java.lang.String):void");
    }

    public static final int access$getItemColorFromStub(ColorProvider colorProvider, String str, String str2) {
        return colorProvider.mStubApkResources.getColor(colorProvider.mStubApkResources.getIdentifier(String.format("%s%s", str, str2), "color", colorProvider.mStubPackageName), null);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r13v1, resolved type: java.util.ArrayList */
    /* JADX WARN: Multi-variable type inference failed */
    public static final void access$loadSeedColors(ColorProvider colorProvider, WallpaperColors wallpaperColors, WallpaperColors wallpaperColors2) {
        EmptyList emptyList;
        Objects.requireNonNull(colorProvider);
        if (wallpaperColors != null) {
            ArrayList arrayList = new ArrayList();
            int i = wallpaperColors2 == null ? 4 : 2;
            if (wallpaperColors2 != null) {
                WallpaperManagerCompatV16 wallpaperManagerCompat = InjectorProvider.getInjector().getWallpaperManagerCompat(colorProvider.mContext);
                boolean z = true;
                if (wallpaperManagerCompat.getWallpaperId(2) <= wallpaperManagerCompat.getWallpaperId(1)) {
                    z = false;
                }
                colorProvider.buildColorSeeds(z ? wallpaperColors2 : wallpaperColors, i, z ? "lock_wallpaper" : "home_wallpaper", true, arrayList);
                colorProvider.buildColorSeeds(z ? wallpaperColors : wallpaperColors2, i, z ? "home_wallpaper" : "lock_wallpaper", false, arrayList);
            } else {
                colorProvider.buildColorSeeds(wallpaperColors, i, "home_wallpaper", true, arrayList);
            }
            List<? extends ColorOption> list = colorProvider.colorBundles;
            if (list == null) {
                emptyList = null;
            } else {
                ArrayList arrayList2 = new ArrayList();
                for (Object obj : list) {
                    if (!(((ColorOption) obj) instanceof ColorSeedOption)) {
                        arrayList2.add(obj);
                    }
                }
                emptyList = arrayList2;
            }
            if (emptyList == null) {
                emptyList = EmptyList.INSTANCE;
            }
            arrayList.addAll(emptyList);
            colorProvider.colorBundles = arrayList;
        }
    }

    public final void buildBundle(int i, int i2, boolean z, String str, List<ColorOption> list) {
        String str2;
        HashMap hashMap = new HashMap();
        int i3 = -14979341;
        Cam fromInt = Cam.fromInt(i == 0 ? -14979341 : i);
        float hue = fromInt.getHue();
        float chroma = fromInt.getChroma();
        float f = 48.0f;
        if (chroma < 48.0f) {
            chroma = 48.0f;
        }
        List<Integer> list2 = ArraysKt___ArraysKt.toList(Shades.of(hue, chroma));
        List<Integer> list3 = ArraysKt___ArraysKt.toList(Shades.of(hue, 16.0f));
        List<Integer> list4 = ArraysKt___ArraysKt.toList(Shades.of(hue + 60.0f, 32.0f));
        ArraysKt___ArraysKt.toList(Shades.of(hue, 4.0f));
        ArraysKt___ArraysKt.toList(Shades.of(hue, 8.0f));
        if (i != 0) {
            i3 = i;
        }
        Cam fromInt2 = Cam.fromInt(i3);
        float hue2 = fromInt2.getHue();
        float chroma2 = fromInt2.getChroma();
        if (chroma2 >= 48.0f) {
            f = chroma2;
        }
        List<Integer> list5 = ArraysKt___ArraysKt.toList(Shades.of(hue2, f));
        List<Integer> list6 = ArraysKt___ArraysKt.toList(Shades.of(hue2, 16.0f));
        List<Integer> list7 = ArraysKt___ArraysKt.toList(Shades.of(60.0f + hue2, 32.0f));
        ArraysKt___ArraysKt.toList(Shades.of(hue2, 4.0f));
        ArraysKt___ArraysKt.toList(Shades.of(hue2, 8.0f));
        int[] iArr = {ColorUtils.setAlphaComponent(list2.get(2).intValue(), 255), ColorUtils.setAlphaComponent(list2.get(2).intValue(), 255), ColorStateList.valueOf(list4.get(6).intValue()).withLStar(85.0f).getColors()[0], ColorStateList.valueOf(list3.get(6).intValue()).withLStar(95.0f).getColors()[0]};
        int[] iArr2 = {ColorUtils.setAlphaComponent(list5.get(2).intValue(), 255), ColorUtils.setAlphaComponent(list5.get(2).intValue(), 255), ColorStateList.valueOf(list7.get(6).intValue()).withLStar(85.0f).getColors()[0], ColorStateList.valueOf(list6.get(6).intValue()).withLStar(95.0f).getColors()[0]};
        String str3 = "";
        if (z) {
            str2 = str3;
        } else {
            str2 = ColorUtils.toColorString(i);
        }
        hashMap.put("android.theme.customization.system_palette", str2);
        if (!z) {
            str3 = ColorUtils.toColorString(i);
        }
        hashMap.put("android.theme.customization.accent_color", str3);
        list.add(new ColorSeedOption(null, hashMap, z, str, i2 + 1, new ColorSeedOption.PreviewInfo(iArr, iArr2, null)));
    }

    public final void buildColorSeeds(WallpaperColors wallpaperColors, int i, String str, boolean z, List<ColorOption> list) {
        List<Object> list2;
        List<Number> list3;
        List<Integer> seedColors = ColorScheme.Companion.getSeedColors(wallpaperColors);
        buildBundle(((Number) CollectionsKt___CollectionsKt.first(seedColors)).intValue(), 0, z, str, list);
        int size = seedColors.size() - 1;
        if (size <= 0) {
            list2 = EmptyList.INSTANCE;
        } else if (size != 1) {
            ArrayList arrayList = new ArrayList(size);
            if (seedColors instanceof RandomAccess) {
                int size2 = seedColors.size();
                for (int i2 = 1; i2 < size2; i2++) {
                    arrayList.add(seedColors.get(i2));
                }
            } else {
                ListIterator<Integer> listIterator = seedColors.listIterator(1);
                while (listIterator.hasNext()) {
                    arrayList.add(listIterator.next());
                }
            }
            list2 = arrayList;
        } else if (!seedColors.isEmpty()) {
            list2 = CollectionsKt__CollectionsKt.listOf(seedColors.get(seedColors.size() - 1));
        } else {
            throw new NoSuchElementException("List is empty.");
        }
        int i3 = i - 1;
        Intrinsics.checkNotNullParameter(list2, "$this$take");
        int i4 = 0;
        if (i3 >= 0) {
            if (i3 == 0) {
                list3 = EmptyList.INSTANCE;
            } else if (i3 >= list2.size()) {
                list3 = CollectionsKt___CollectionsKt.toList(list2);
            } else if (i3 == 1) {
                list3 = CollectionsKt__CollectionsKt.listOf(CollectionsKt___CollectionsKt.first(list2));
            } else {
                ArrayList arrayList2 = new ArrayList(i3);
                int i5 = 0;
                for (Object obj : list2) {
                    arrayList2.add(obj);
                    i5++;
                    if (i5 == i3) {
                        break;
                    }
                }
                list3 = CollectionsKt__CollectionsKt.optimizeReadOnlyList(arrayList2);
            }
            for (Number number : list3) {
                i4++;
                buildBundle(number.intValue(), i4, false, str, list);
            }
            return;
        }
        throw new IllegalArgumentException(("Requested element count " + i3 + " is less than zero.").toString());
    }
}
