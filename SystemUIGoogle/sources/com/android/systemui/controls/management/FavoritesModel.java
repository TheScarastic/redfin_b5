package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import android.util.Log;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.management.ControlsModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: FavoritesModel.kt */
/* loaded from: classes.dex */
public final class FavoritesModel implements ControlsModel {
    public static final Companion Companion = new Companion(null);
    private RecyclerView.Adapter<?> adapter;
    private final ComponentName componentName;
    private final CustomIconCache customIconCache;
    private int dividerPosition;
    private final List<ElementWrapper> elements;
    private final FavoritesModelCallback favoritesModelCallback;
    private final ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private boolean modified;
    private final ControlsModel.MoveHelper moveHelper = new ControlsModel.MoveHelper(this) { // from class: com.android.systemui.controls.management.FavoritesModel$moveHelper$1
        final /* synthetic */ FavoritesModel this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // com.android.systemui.controls.management.ControlsModel.MoveHelper
        public boolean canMoveBefore(int i) {
            return i > 0 && i < this.this$0.dividerPosition;
        }

        @Override // com.android.systemui.controls.management.ControlsModel.MoveHelper
        public boolean canMoveAfter(int i) {
            return i >= 0 && i < this.this$0.dividerPosition - 1;
        }

        @Override // com.android.systemui.controls.management.ControlsModel.MoveHelper
        public void moveBefore(int i) {
            if (!canMoveBefore(i)) {
                Log.w("FavoritesModel", "Cannot move position " + i + " before");
                return;
            }
            this.this$0.onMoveItem(i, i - 1);
        }

        @Override // com.android.systemui.controls.management.ControlsModel.MoveHelper
        public void moveAfter(int i) {
            if (!canMoveAfter(i)) {
                Log.w("FavoritesModel", "Cannot move position " + i + " after");
                return;
            }
            this.this$0.onMoveItem(i, i + 1);
        }
    };

    /* compiled from: FavoritesModel.kt */
    /* loaded from: classes.dex */
    public interface FavoritesModelCallback extends ControlsModel.ControlsModelCallback {
        void onNoneChanged(boolean z);
    }

    public FavoritesModel(CustomIconCache customIconCache, ComponentName componentName, List<ControlInfo> list, FavoritesModelCallback favoritesModelCallback) {
        Intrinsics.checkNotNullParameter(customIconCache, "customIconCache");
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(list, "favorites");
        Intrinsics.checkNotNullParameter(favoritesModelCallback, "favoritesModelCallback");
        this.customIconCache = customIconCache;
        this.componentName = componentName;
        this.favoritesModelCallback = favoritesModelCallback;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10));
        for (ControlInfo controlInfo : list) {
            arrayList.add(new ControlInfoWrapper(this.componentName, controlInfo, true, new Function2<ComponentName, String, Icon>(this.customIconCache) { // from class: com.android.systemui.controls.management.FavoritesModel$elements$1$1
                public final Icon invoke(ComponentName componentName2, String str) {
                    Intrinsics.checkNotNullParameter(componentName2, "p0");
                    Intrinsics.checkNotNullParameter(str, "p1");
                    return ((CustomIconCache) this.receiver).retrieve(componentName2, str);
                }
            }));
        }
        this.elements = CollectionsKt___CollectionsKt.plus(arrayList, new DividerWrapper(false, false, 3, null));
        this.dividerPosition = getElements().size() - 1;
        this.itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(this) { // from class: com.android.systemui.controls.management.FavoritesModel$itemTouchHelperCallback$1
            private final int MOVEMENT = 15;
            final /* synthetic */ FavoritesModel this$0;

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean isItemViewSwipeEnabled() {
                return false;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                Intrinsics.checkNotNullParameter(viewHolder, "viewHolder");
            }

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
                Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
                Intrinsics.checkNotNullParameter(viewHolder, "viewHolder");
                Intrinsics.checkNotNullParameter(viewHolder2, "target");
                this.this$0.onMoveItem(viewHolder.getBindingAdapterPosition(), viewHolder2.getBindingAdapterPosition());
                return true;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
                Intrinsics.checkNotNullParameter(viewHolder, "viewHolder");
                if (viewHolder.getBindingAdapterPosition() < this.this$0.dividerPosition) {
                    return ItemTouchHelper.Callback.makeMovementFlags(this.MOVEMENT, 0);
                }
                return ItemTouchHelper.Callback.makeMovementFlags(0, 0);
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
                Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
                Intrinsics.checkNotNullParameter(viewHolder, "current");
                Intrinsics.checkNotNullParameter(viewHolder2, "target");
                return viewHolder2.getBindingAdapterPosition() < this.this$0.dividerPosition;
            }
        };
    }

    /* compiled from: FavoritesModel.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    public ControlsModel.MoveHelper getMoveHelper() {
        return this.moveHelper;
    }

    public void attachAdapter(RecyclerView.Adapter<?> adapter) {
        Intrinsics.checkNotNullParameter(adapter, "adapter");
        this.adapter = adapter;
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    public List<ControlInfo> getFavorites() {
        List<ElementWrapper> list = CollectionsKt___CollectionsKt.take(getElements(), this.dividerPosition);
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10));
        for (ElementWrapper elementWrapper : list) {
            arrayList.add(((ControlInfoWrapper) elementWrapper).getControlInfo());
        }
        return arrayList;
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    public List<ElementWrapper> getElements() {
        return this.elements;
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    public void changeFavoriteStatus(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        Iterator<ElementWrapper> it = getElements().iterator();
        int i = 0;
        while (true) {
            if (!it.hasNext()) {
                i = -1;
                break;
            }
            ElementWrapper next = it.next();
            if ((next instanceof ControlInterface) && Intrinsics.areEqual(((ControlInterface) next).getControlId(), str)) {
                break;
            }
            i++;
        }
        if (i != -1) {
            int i2 = this.dividerPosition;
            if (i < i2 && z) {
                return;
            }
            if (i > i2 && !z) {
                return;
            }
            if (z) {
                onMoveItemInternal(i, i2);
            } else {
                onMoveItemInternal(i, getElements().size() - 1);
            }
        }
    }

    public void onMoveItem(int i, int i2) {
        onMoveItemInternal(i, i2);
    }

    private final void updateDividerNone(int i, boolean z) {
        ((DividerWrapper) getElements().get(i)).setShowNone(z);
        this.favoritesModelCallback.onNoneChanged(z);
    }

    private final void updateDividerShow(int i, boolean z) {
        ((DividerWrapper) getElements().get(i)).setShowDivider(z);
    }

    private final void onMoveItemInternal(int i, int i2) {
        RecyclerView.Adapter<?> adapter;
        int i3 = this.dividerPosition;
        if (i != i3) {
            boolean z = false;
            if ((i < i3 && i2 >= i3) || (i > i3 && i2 <= i3)) {
                if (i < i3 && i2 >= i3) {
                    ((ControlInfoWrapper) getElements().get(i)).setFavorite(false);
                } else if (i > i3 && i2 <= i3) {
                    ((ControlInfoWrapper) getElements().get(i)).setFavorite(true);
                }
                updateDivider(i, i2);
                z = true;
            }
            moveElement(i, i2);
            RecyclerView.Adapter<?> adapter2 = this.adapter;
            if (adapter2 != null) {
                adapter2.notifyItemMoved(i, i2);
            }
            if (z && (adapter = this.adapter) != null) {
                adapter.notifyItemChanged(i2, new Object());
            }
            if (!this.modified) {
                this.modified = true;
                this.favoritesModelCallback.onFirstChange();
            }
        }
    }

    private final void updateDivider(int i, int i2) {
        RecyclerView.Adapter<?> adapter;
        boolean z;
        int i3 = this.dividerPosition;
        boolean z2 = false;
        if (i < i3 && i2 >= i3) {
            int i4 = i3 - 1;
            this.dividerPosition = i4;
            if (i4 == 0) {
                updateDividerNone(i3, true);
                z2 = true;
            }
            if (this.dividerPosition == getElements().size() - 2) {
                updateDividerShow(i3, true);
                z2 = true;
            }
        } else if (i > i3 && i2 <= i3) {
            int i5 = i3 + 1;
            this.dividerPosition = i5;
            if (i5 == 1) {
                updateDividerNone(i3, false);
                z = true;
            } else {
                z = false;
            }
            if (this.dividerPosition == getElements().size() - 1) {
                updateDividerShow(i3, false);
                z2 = true;
            } else {
                z2 = z;
            }
        }
        if (z2 && (adapter = this.adapter) != null) {
            adapter.notifyItemChanged(i3);
        }
    }

    private final void moveElement(int i, int i2) {
        if (i >= i2) {
            int i3 = i2 + 1;
            if (i3 <= i) {
                while (true) {
                    int i4 = i - 1;
                    Collections.swap(getElements(), i, i - 1);
                    if (i != i3) {
                        i = i4;
                    } else {
                        return;
                    }
                }
            }
        } else if (i < i2) {
            while (true) {
                int i5 = i + 1;
                Collections.swap(getElements(), i, i5);
                if (i5 < i2) {
                    i = i5;
                } else {
                    return;
                }
            }
        }
    }

    public final ItemTouchHelper.SimpleCallback getItemTouchHelperCallback() {
        return this.itemTouchHelperCallback;
    }
}
