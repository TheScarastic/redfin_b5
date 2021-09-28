package com.android.wm.shell.common;

import android.graphics.Rect;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.Pair;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
/* compiled from: FloatingContentCoordinator.kt */
/* loaded from: classes2.dex */
public final class FloatingContentCoordinator {
    public static final Companion Companion = new Companion(null);
    private final Map<FloatingContent, Rect> allContentBounds = new HashMap();
    private boolean currentlyResolvingConflicts;

    /* compiled from: FloatingContentCoordinator.kt */
    /* loaded from: classes2.dex */
    public interface FloatingContent {
        Rect getAllowedFloatingBoundsRegion();

        Rect getFloatingBoundsOnScreen();

        void moveToBounds(Rect rect);

        default Rect calculateNewBoundsOnOverlap(Rect rect, List<Rect> list) {
            Intrinsics.checkNotNullParameter(rect, "overlappingContentBounds");
            Intrinsics.checkNotNullParameter(list, "otherContentBounds");
            return FloatingContentCoordinator.Companion.findAreaForContentVertically(getFloatingBoundsOnScreen(), rect, list, getAllowedFloatingBoundsRegion());
        }
    }

    public final void onContentAdded(FloatingContent floatingContent) {
        Intrinsics.checkNotNullParameter(floatingContent, "newContent");
        updateContentBounds();
        this.allContentBounds.put(floatingContent, floatingContent.getFloatingBoundsOnScreen());
        maybeMoveConflictingContent(floatingContent);
    }

    public final void onContentMoved(FloatingContent floatingContent) {
        Intrinsics.checkNotNullParameter(floatingContent, "content");
        if (!this.currentlyResolvingConflicts) {
            if (!this.allContentBounds.containsKey(floatingContent)) {
                Log.wtf("FloatingCoordinator", "Received onContentMoved call before onContentAdded! This should never happen.");
                return;
            }
            updateContentBounds();
            maybeMoveConflictingContent(floatingContent);
        }
    }

    public final void onContentRemoved(FloatingContent floatingContent) {
        Intrinsics.checkNotNullParameter(floatingContent, "removedContent");
        this.allContentBounds.remove(floatingContent);
    }

    private final void maybeMoveConflictingContent(FloatingContent floatingContent) {
        this.currentlyResolvingConflicts = true;
        Rect rect = this.allContentBounds.get(floatingContent);
        Intrinsics.checkNotNull(rect);
        Map<FloatingContent, Rect> map = this.allContentBounds;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterator<Map.Entry<FloatingContent, Rect>> it = map.entrySet().iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<FloatingContent, Rect> next = it.next();
            Rect value = next.getValue();
            if (!Intrinsics.areEqual(next.getKey(), floatingContent) && Rect.intersects(rect, value)) {
                z = true;
            }
            if (z) {
                linkedHashMap.put(next.getKey(), next.getValue());
            }
        }
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            FloatingContent floatingContent2 = (FloatingContent) entry.getKey();
            Rect calculateNewBoundsOnOverlap = floatingContent2.calculateNewBoundsOnOverlap(rect, CollectionsKt___CollectionsKt.minus(CollectionsKt___CollectionsKt.minus(this.allContentBounds.values(), (Rect) entry.getValue()), rect));
            if (!calculateNewBoundsOnOverlap.isEmpty()) {
                floatingContent2.moveToBounds(calculateNewBoundsOnOverlap);
                this.allContentBounds.put(floatingContent2, floatingContent2.getFloatingBoundsOnScreen());
            }
        }
        this.currentlyResolvingConflicts = false;
    }

    private final void updateContentBounds() {
        for (FloatingContent floatingContent : this.allContentBounds.keySet()) {
            this.allContentBounds.put(floatingContent, floatingContent.getFloatingBoundsOnScreen());
        }
    }

    /* compiled from: FloatingContentCoordinator.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX WARN: Type inference failed for: r3v2, types: [T, java.lang.Object] */
        /* JADX WARN: Type inference failed for: r0v4, types: [T, java.lang.Object] */
        public final Rect findAreaForContentVertically(Rect rect, Rect rect2, Collection<Rect> collection, Rect rect3) {
            Intrinsics.checkNotNullParameter(rect, "contentRect");
            Intrinsics.checkNotNullParameter(rect2, "newlyOverlappingRect");
            Intrinsics.checkNotNullParameter(collection, "exclusionRects");
            Intrinsics.checkNotNullParameter(rect3, "allowedBounds");
            boolean z = true;
            boolean z2 = rect2.centerY() < rect.centerY();
            ArrayList arrayList = new ArrayList();
            for (Object obj : collection) {
                if (FloatingContentCoordinator.Companion.rectsIntersectVertically((Rect) obj, rect)) {
                    arrayList.add(obj);
                }
            }
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            for (Object obj2 : arrayList) {
                if (((Rect) obj2).top < rect.top) {
                    arrayList2.add(obj2);
                } else {
                    arrayList3.add(obj2);
                }
            }
            Pair pair = new Pair(arrayList2, arrayList3);
            Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            ref$ObjectRef.element = pair.component1();
            Ref$ObjectRef ref$ObjectRef2 = new Ref$ObjectRef();
            ref$ObjectRef2.element = pair.component2();
            Lazy lazy = LazyKt__LazyJVMKt.lazy(new FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsAbove$2(rect, ref$ObjectRef, rect2));
            Lazy lazy2 = LazyKt__LazyJVMKt.lazy(new FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsBelow$2(rect, ref$ObjectRef2, rect2));
            Lazy lazy3 = LazyKt__LazyJVMKt.lazy(new FloatingContentCoordinator$Companion$findAreaForContentVertically$positionAboveInBounds$2(rect3, lazy));
            Lazy lazy4 = LazyKt__LazyJVMKt.lazy(new FloatingContentCoordinator$Companion$findAreaForContentVertically$positionBelowInBounds$2(rect3, lazy2));
            if ((!z2 || !m525findAreaForContentVertically$lambda5(lazy4)) && (z2 || m524findAreaForContentVertically$lambda4(lazy3))) {
                z = false;
            }
            Rect r7 = z ? m523findAreaForContentVertically$lambda3(lazy2) : m522findAreaForContentVertically$lambda2(lazy);
            return rect3.contains(r7) ? r7 : new Rect();
        }

        /* access modifiers changed from: private */
        /* renamed from: findAreaForContentVertically$lambda-2  reason: not valid java name */
        public static final Rect m522findAreaForContentVertically$lambda2(Lazy<Rect> lazy) {
            return lazy.getValue();
        }

        /* access modifiers changed from: private */
        /* renamed from: findAreaForContentVertically$lambda-3  reason: not valid java name */
        public static final Rect m523findAreaForContentVertically$lambda3(Lazy<Rect> lazy) {
            return lazy.getValue();
        }

        /* renamed from: findAreaForContentVertically$lambda-4  reason: not valid java name */
        private static final boolean m524findAreaForContentVertically$lambda4(Lazy<Boolean> lazy) {
            return lazy.getValue().booleanValue();
        }

        /* renamed from: findAreaForContentVertically$lambda-5  reason: not valid java name */
        private static final boolean m525findAreaForContentVertically$lambda5(Lazy<Boolean> lazy) {
            return lazy.getValue().booleanValue();
        }

        private final boolean rectsIntersectVertically(Rect rect, Rect rect2) {
            int i;
            int i2 = rect.left;
            int i3 = rect2.left;
            return (i2 >= i3 && i2 <= rect2.right) || ((i = rect.right) <= rect2.right && i >= i3);
        }

        public final Rect findAreaForContentAboveOrBelow(Rect rect, Collection<Rect> collection, boolean z) {
            Intrinsics.checkNotNullParameter(rect, "contentRect");
            Intrinsics.checkNotNullParameter(collection, "exclusionRects");
            List<Rect> list = CollectionsKt___CollectionsKt.sortedWith(collection, new FloatingContentCoordinator$Companion$findAreaForContentAboveOrBelow$$inlined$sortedBy$1(z));
            Rect rect2 = new Rect(rect);
            for (Rect rect3 : list) {
                if (!Rect.intersects(rect2, rect3)) {
                    break;
                }
                rect2.offsetTo(rect2.left, rect3.top + (z ? -rect.height() : rect3.height()));
            }
            return rect2;
        }
    }
}
