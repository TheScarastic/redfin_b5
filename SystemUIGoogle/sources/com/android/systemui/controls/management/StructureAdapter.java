package com.android.systemui.controls.management;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StructureAdapter.kt */
/* loaded from: classes.dex */
public final class StructureAdapter extends RecyclerView.Adapter<StructureHolder> {
    private final List<StructureContainer> models;

    public StructureAdapter(List<StructureContainer> list) {
        Intrinsics.checkNotNullParameter(list, "models");
        this.models = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public StructureHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.controls_structure_page, viewGroup, false);
        Intrinsics.checkNotNullExpressionValue(inflate, "layoutInflater.inflate(R.layout.controls_structure_page, parent, false)");
        return new StructureHolder(inflate);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.models.size();
    }

    public void onBindViewHolder(StructureHolder structureHolder, int i) {
        Intrinsics.checkNotNullParameter(structureHolder, "holder");
        structureHolder.bind(this.models.get(i).getModel());
    }

    /* compiled from: StructureAdapter.kt */
    /* loaded from: classes.dex */
    public static final class StructureHolder extends RecyclerView.ViewHolder {
        private final ControlAdapter controlAdapter = new ControlAdapter(this.itemView.getContext().getResources().getFloat(R$dimen.control_card_elevation));
        private final RecyclerView recyclerView;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public StructureHolder(View view) {
            super(view);
            Intrinsics.checkNotNullParameter(view, "view");
            View requireViewById = this.itemView.requireViewById(R$id.listAll);
            Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById<RecyclerView>(R.id.listAll)");
            this.recyclerView = (RecyclerView) requireViewById;
            setUpRecyclerView();
        }

        public final void bind(ControlsModel controlsModel) {
            Intrinsics.checkNotNullParameter(controlsModel, "model");
            this.controlAdapter.changeModel(controlsModel);
        }

        private final void setUpRecyclerView() {
            int dimensionPixelSize = this.itemView.getContext().getResources().getDimensionPixelSize(R$dimen.controls_card_margin);
            MarginItemDecorator marginItemDecorator = new MarginItemDecorator(dimensionPixelSize, dimensionPixelSize);
            RecyclerView recyclerView = this.recyclerView;
            recyclerView.setAdapter(this.controlAdapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this.recyclerView.getContext(), 2);
            gridLayoutManager.setSpanSizeLookup(this.controlAdapter.getSpanSizeLookup());
            Unit unit = Unit.INSTANCE;
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addItemDecoration(marginItemDecorator);
        }
    }
}
