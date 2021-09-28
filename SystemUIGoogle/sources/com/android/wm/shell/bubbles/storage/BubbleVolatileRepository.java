package com.android.wm.shell.bubbles.storage;

import android.content.pm.LauncherApps;
import android.os.UserHandle;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.bubbles.ShortcutKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BubbleVolatileRepository.kt */
/* loaded from: classes2.dex */
public final class BubbleVolatileRepository {
    private final LauncherApps launcherApps;
    private SparseArray<List<BubbleEntity>> entitiesByUser = new SparseArray<>();
    private int capacity = 16;

    @VisibleForTesting
    public static /* synthetic */ void getCapacity$annotations() {
    }

    public BubbleVolatileRepository(LauncherApps launcherApps) {
        Intrinsics.checkNotNullParameter(launcherApps, "launcherApps");
        this.launcherApps = launcherApps;
    }

    public final synchronized SparseArray<List<BubbleEntity>> getBubbles() {
        SparseArray<List<BubbleEntity>> sparseArray;
        sparseArray = new SparseArray<>();
        int i = 0;
        int size = this.entitiesByUser.size();
        if (size > 0) {
            while (true) {
                int i2 = i + 1;
                int keyAt = this.entitiesByUser.keyAt(i);
                List<BubbleEntity> valueAt = this.entitiesByUser.valueAt(i);
                Intrinsics.checkNotNullExpressionValue(valueAt, "v");
                sparseArray.put(keyAt, CollectionsKt___CollectionsKt.toList(valueAt));
                if (i2 >= size) {
                    break;
                }
                i = i2;
            }
        }
        return sparseArray;
    }

    public final synchronized List<BubbleEntity> getEntities(int i) {
        List<BubbleEntity> list;
        list = this.entitiesByUser.get(i);
        if (list == null) {
            list = new ArrayList<>();
            this.entitiesByUser.put(i, list);
        } else {
            Intrinsics.checkNotNullExpressionValue(list, "entities");
        }
        return list;
    }

    public final synchronized void addBubbles(int i, List<BubbleEntity> list) {
        Intrinsics.checkNotNullParameter(list, "bubbles");
        if (!list.isEmpty()) {
            List<BubbleEntity> entities = getEntities(i);
            List list2 = CollectionsKt___CollectionsKt.takeLast(list, this.capacity);
            List<BubbleEntity> arrayList = new ArrayList<>();
            for (Object obj : list2) {
                if (!entities.removeIf(new Predicate<BubbleEntity>((BubbleEntity) obj) { // from class: com.android.wm.shell.bubbles.storage.BubbleVolatileRepository$addBubbles$uniqueBubbles$1$1
                    final /* synthetic */ BubbleEntity $b;

                    /* access modifiers changed from: package-private */
                    {
                        this.$b = r1;
                    }

                    public final boolean test(BubbleEntity bubbleEntity) {
                        Intrinsics.checkNotNullParameter(bubbleEntity, "e");
                        return Intrinsics.areEqual(this.$b.getKey(), bubbleEntity.getKey());
                    }
                })) {
                    arrayList.add(obj);
                }
            }
            int size = (entities.size() + list2.size()) - this.capacity;
            if (size > 0) {
                uncache(CollectionsKt___CollectionsKt.take(entities, size));
                entities = CollectionsKt___CollectionsKt.toMutableList((Collection) CollectionsKt___CollectionsKt.drop(entities, size));
            }
            entities.addAll(list2);
            this.entitiesByUser.put(i, entities);
            cache(arrayList);
        }
    }

    public final synchronized void removeBubbles(int i, List<BubbleEntity> list) {
        Intrinsics.checkNotNullParameter(list, "bubbles");
        List<BubbleEntity> arrayList = new ArrayList<>();
        for (Object obj : list) {
            if (getEntities(i).removeIf(new Predicate<BubbleEntity>((BubbleEntity) obj) { // from class: com.android.wm.shell.bubbles.storage.BubbleVolatileRepository$removeBubbles$1$1
                final /* synthetic */ BubbleEntity $b;

                /* access modifiers changed from: package-private */
                {
                    this.$b = r1;
                }

                public final boolean test(BubbleEntity bubbleEntity) {
                    Intrinsics.checkNotNullParameter(bubbleEntity, "e");
                    return Intrinsics.areEqual(this.$b.getKey(), bubbleEntity.getKey());
                }
            })) {
                arrayList.add(obj);
            }
        }
        uncache(arrayList);
    }

    private final void cache(List<BubbleEntity> list) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj : list) {
            BubbleEntity bubbleEntity = (BubbleEntity) obj;
            ShortcutKey shortcutKey = new ShortcutKey(bubbleEntity.getUserId(), bubbleEntity.getPackageName());
            Object obj2 = linkedHashMap.get(shortcutKey);
            if (obj2 == null) {
                obj2 = new ArrayList();
                linkedHashMap.put(shortcutKey, obj2);
            }
            ((List) obj2).add(obj);
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            ShortcutKey shortcutKey2 = (ShortcutKey) entry.getKey();
            List<BubbleEntity> list2 = (List) entry.getValue();
            LauncherApps launcherApps = this.launcherApps;
            String pkg = shortcutKey2.getPkg();
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(list2, 10));
            for (BubbleEntity bubbleEntity2 : list2) {
                arrayList.add(bubbleEntity2.getShortcutId());
            }
            launcherApps.cacheShortcuts(pkg, arrayList, UserHandle.of(shortcutKey2.getUserId()), 1);
        }
    }

    private final void uncache(List<BubbleEntity> list) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj : list) {
            BubbleEntity bubbleEntity = (BubbleEntity) obj;
            ShortcutKey shortcutKey = new ShortcutKey(bubbleEntity.getUserId(), bubbleEntity.getPackageName());
            Object obj2 = linkedHashMap.get(shortcutKey);
            if (obj2 == null) {
                obj2 = new ArrayList();
                linkedHashMap.put(shortcutKey, obj2);
            }
            ((List) obj2).add(obj);
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            ShortcutKey shortcutKey2 = (ShortcutKey) entry.getKey();
            List<BubbleEntity> list2 = (List) entry.getValue();
            LauncherApps launcherApps = this.launcherApps;
            String pkg = shortcutKey2.getPkg();
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(list2, 10));
            for (BubbleEntity bubbleEntity2 : list2) {
                arrayList.add(bubbleEntity2.getShortcutId());
            }
            launcherApps.uncacheShortcuts(pkg, arrayList, UserHandle.of(shortcutKey2.getUserId()), 1);
        }
    }
}
