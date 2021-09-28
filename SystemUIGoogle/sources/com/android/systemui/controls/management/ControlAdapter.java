package com.android.systemui.controls.management;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$drawable;
import com.android.systemui.R$layout;
import com.android.systemui.controls.ControlInterface;
import java.util.List;
import java.util.Objects;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class ControlAdapter extends RecyclerView.Adapter<Holder> {
    public static final Companion Companion = new Companion(null);
    private final float elevation;
    private ControlsModel model;
    private final GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup(this) { // from class: com.android.systemui.controls.management.ControlAdapter$spanSizeLookup$1
        final /* synthetic */ ControlAdapter this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public int getSpanSize(int i) {
            return this.this$0.getItemViewType(i) != 1 ? 2 : 1;
        }
    };

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [androidx.recyclerview.widget.RecyclerView$ViewHolder, int, java.util.List] */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(Holder holder, int i, List list) {
        onBindViewHolder(holder, i, (List<Object>) list);
    }

    public ControlAdapter(float f) {
        this.elevation = f;
    }

    /* compiled from: ControlAdapter.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return this.spanSizeLookup;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        if (i == 0) {
            View inflate = from.inflate(R$layout.controls_zone_header, viewGroup, false);
            Intrinsics.checkNotNullExpressionValue(inflate, "layoutInflater.inflate(R.layout.controls_zone_header, parent, false)");
            return new ZoneHolder(inflate);
        } else if (i == 1) {
            View inflate2 = from.inflate(R$layout.controls_base_item, viewGroup, false);
            ViewGroup.LayoutParams layoutParams = inflate2.getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.width = -1;
            marginLayoutParams.topMargin = 0;
            marginLayoutParams.bottomMargin = 0;
            marginLayoutParams.leftMargin = 0;
            marginLayoutParams.rightMargin = 0;
            inflate2.setElevation(this.elevation);
            inflate2.setBackground(viewGroup.getContext().getDrawable(R$drawable.control_background_ripple));
            Unit unit = Unit.INSTANCE;
            Intrinsics.checkNotNullExpressionValue(inflate2, "layoutInflater.inflate(R.layout.controls_base_item, parent, false).apply {\n                        (layoutParams as ViewGroup.MarginLayoutParams).apply {\n                            width = ViewGroup.LayoutParams.MATCH_PARENT\n                            // Reset margins as they will be set through the decoration\n                            topMargin = 0\n                            bottomMargin = 0\n                            leftMargin = 0\n                            rightMargin = 0\n                        }\n                        elevation = this@ControlAdapter.elevation\n                        background = parent.context.getDrawable(\n                                R.drawable.control_background_ripple)\n                    }");
            ControlsModel controlsModel = this.model;
            return new ControlHolder(inflate2, controlsModel == null ? null : controlsModel.getMoveHelper(), new Function2<String, Boolean, Unit>(this) { // from class: com.android.systemui.controls.management.ControlAdapter$onCreateViewHolder$2
                final /* synthetic */ ControlAdapter this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                /* Return type fixed from 'java.lang.Object' to match base method */
                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
                @Override // kotlin.jvm.functions.Function2
                public /* bridge */ /* synthetic */ Unit invoke(String str, Boolean bool) {
                    invoke(str, bool.booleanValue());
                    return Unit.INSTANCE;
                }

                public final void invoke(String str, boolean z) {
                    Intrinsics.checkNotNullParameter(str, "id");
                    ControlsModel access$getModel$p = ControlAdapter.access$getModel$p(this.this$0);
                    if (access$getModel$p != null) {
                        access$getModel$p.changeFavoriteStatus(str, z);
                    }
                }
            });
        } else if (i == 2) {
            View inflate3 = from.inflate(R$layout.controls_horizontal_divider_with_empty, viewGroup, false);
            Intrinsics.checkNotNullExpressionValue(inflate3, "layoutInflater.inflate(\n                        R.layout.controls_horizontal_divider_with_empty, parent, false)");
            return new DividerHolder(inflate3);
        } else {
            throw new IllegalStateException(Intrinsics.stringPlus("Wrong viewType: ", Integer.valueOf(i)));
        }
    }

    public final void changeModel(ControlsModel controlsModel) {
        Intrinsics.checkNotNullParameter(controlsModel, "model");
        this.model = controlsModel;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        ControlsModel controlsModel = this.model;
        List<ElementWrapper> elements = controlsModel == null ? null : controlsModel.getElements();
        if (elements == null) {
            return 0;
        }
        return elements.size();
    }

    public void onBindViewHolder(Holder holder, int i) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        ControlsModel controlsModel = this.model;
        if (controlsModel != null) {
            holder.bindData(controlsModel.getElements().get(i));
        }
    }

    public void onBindViewHolder(Holder holder, int i, List<Object> list) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        Intrinsics.checkNotNullParameter(list, "payloads");
        if (list.isEmpty()) {
            super.onBindViewHolder((ControlAdapter) holder, i, list);
            return;
        }
        ControlsModel controlsModel = this.model;
        if (controlsModel != null) {
            ElementWrapper elementWrapper = controlsModel.getElements().get(i);
            if (elementWrapper instanceof ControlInterface) {
                holder.updateFavorite(((ControlInterface) elementWrapper).getFavorite());
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        ControlsModel controlsModel = this.model;
        if (controlsModel != null) {
            ElementWrapper elementWrapper = controlsModel.getElements().get(i);
            if (elementWrapper instanceof ZoneNameWrapper) {
                return 0;
            }
            if ((elementWrapper instanceof ControlStatusWrapper) || (elementWrapper instanceof ControlInfoWrapper)) {
                return 1;
            }
            if (elementWrapper instanceof DividerWrapper) {
                return 2;
            }
            throw new NoWhenBranchMatchedException();
        }
        throw new IllegalStateException("Getting item type for null model");
    }
}
