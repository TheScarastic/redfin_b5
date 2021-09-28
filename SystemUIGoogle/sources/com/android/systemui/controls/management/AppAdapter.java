package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AppAdapter.kt */
/* loaded from: classes.dex */
public final class AppAdapter extends RecyclerView.Adapter<Holder> {
    private final AppAdapter$callback$1 callback;
    private final FavoritesRenderer favoritesRenderer;
    private final LayoutInflater layoutInflater;
    private List<ControlsServiceInfo> listOfServices = CollectionsKt__CollectionsKt.emptyList();
    private final Function1<ComponentName, Unit> onAppSelected;
    private final Resources resources;

    /* JADX DEBUG: Multi-variable search result rejected for r7v0, resolved type: kotlin.jvm.functions.Function1<? super android.content.ComponentName, kotlin.Unit> */
    /* JADX WARN: Multi-variable type inference failed */
    public AppAdapter(Executor executor, Executor executor2, Lifecycle lifecycle, ControlsListingController controlsListingController, LayoutInflater layoutInflater, Function1<? super ComponentName, Unit> function1, FavoritesRenderer favoritesRenderer, Resources resources) {
        Intrinsics.checkNotNullParameter(executor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(executor2, "uiExecutor");
        Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
        Intrinsics.checkNotNullParameter(controlsListingController, "controlsListingController");
        Intrinsics.checkNotNullParameter(layoutInflater, "layoutInflater");
        Intrinsics.checkNotNullParameter(function1, "onAppSelected");
        Intrinsics.checkNotNullParameter(favoritesRenderer, "favoritesRenderer");
        Intrinsics.checkNotNullParameter(resources, "resources");
        this.layoutInflater = layoutInflater;
        this.onAppSelected = function1;
        this.favoritesRenderer = favoritesRenderer;
        this.resources = resources;
        AppAdapter$callback$1 appAdapter$callback$1 = new ControlsListingController.ControlsListingCallback(executor, this, executor2) { // from class: com.android.systemui.controls.management.AppAdapter$callback$1
            final /* synthetic */ Executor $backgroundExecutor;
            final /* synthetic */ Executor $uiExecutor;
            final /* synthetic */ AppAdapter this$0;

            /* access modifiers changed from: package-private */
            {
                this.$backgroundExecutor = r1;
                this.this$0 = r2;
                this.$uiExecutor = r3;
            }

            @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
            public void onServicesUpdated(List<ControlsServiceInfo> list) {
                Intrinsics.checkNotNullParameter(list, "serviceInfos");
                this.$backgroundExecutor.execute(
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0010: INVOKE  
                      (wrap: java.util.concurrent.Executor : 0x0005: IGET  (r0v1 java.util.concurrent.Executor A[REMOVE]) = (r3v0 'this' com.android.systemui.controls.management.AppAdapter$callback$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.management.AppAdapter$callback$1.$backgroundExecutor java.util.concurrent.Executor)
                      (wrap: com.android.systemui.controls.management.AppAdapter$callback$1$onServicesUpdated$1 : 0x000d: CONSTRUCTOR  (r1v0 com.android.systemui.controls.management.AppAdapter$callback$1$onServicesUpdated$1 A[REMOVE]) = 
                      (wrap: com.android.systemui.controls.management.AppAdapter : 0x0009: IGET  (r2v0 com.android.systemui.controls.management.AppAdapter A[REMOVE]) = (r3v0 'this' com.android.systemui.controls.management.AppAdapter$callback$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.management.AppAdapter$callback$1.this$0 com.android.systemui.controls.management.AppAdapter)
                      (r4v0 'list' java.util.List<com.android.systemui.controls.ControlsServiceInfo>)
                      (wrap: java.util.concurrent.Executor : 0x000b: IGET  (r3v1 java.util.concurrent.Executor A[REMOVE]) = (r3v0 'this' com.android.systemui.controls.management.AppAdapter$callback$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.management.AppAdapter$callback$1.$uiExecutor java.util.concurrent.Executor)
                     call: com.android.systemui.controls.management.AppAdapter$callback$1$onServicesUpdated$1.<init>(com.android.systemui.controls.management.AppAdapter, java.util.List, java.util.concurrent.Executor):void type: CONSTRUCTOR)
                     type: INTERFACE call: java.util.concurrent.Executor.execute(java.lang.Runnable):void in method: com.android.systemui.controls.management.AppAdapter$callback$1.onServicesUpdated(java.util.List<com.android.systemui.controls.ControlsServiceInfo>):void, file: classes.dex
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:261)
                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:254)
                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:349)
                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:302)
                    	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:271)
                    	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                    	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                    	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                    Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.systemui.controls.management.AppAdapter$callback$1$onServicesUpdated$1, state: NOT_LOADED
                    	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                    	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                    	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:976)
                    	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:801)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                    	... 15 more
                    */
                /*
                    this = this;
                    java.lang.String r0 = "serviceInfos"
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
                    java.util.concurrent.Executor r0 = r3.$backgroundExecutor
                    com.android.systemui.controls.management.AppAdapter$callback$1$onServicesUpdated$1 r1 = new com.android.systemui.controls.management.AppAdapter$callback$1$onServicesUpdated$1
                    com.android.systemui.controls.management.AppAdapter r2 = r3.this$0
                    java.util.concurrent.Executor r3 = r3.$uiExecutor
                    r1.<init>(r2, r4, r3)
                    r0.execute(r1)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.management.AppAdapter$callback$1.onServicesUpdated(java.util.List):void");
            }
        };
        this.callback = appAdapter$callback$1;
        controlsListingController.observe(lifecycle, (Lifecycle) appAdapter$callback$1);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        View inflate = this.layoutInflater.inflate(R$layout.controls_app_item, viewGroup, false);
        Intrinsics.checkNotNullExpressionValue(inflate, "layoutInflater.inflate(R.layout.controls_app_item, parent, false)");
        return new Holder(inflate, this.favoritesRenderer);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.listOfServices.size();
    }

    public void onBindViewHolder(Holder holder, int i) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        holder.bindData(this.listOfServices.get(i));
        holder.itemView.setOnClickListener(new View.OnClickListener(this, i) { // from class: com.android.systemui.controls.management.AppAdapter$onBindViewHolder$1
            final /* synthetic */ int $index;
            final /* synthetic */ AppAdapter this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$index = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AppAdapter.access$getOnAppSelected$p(this.this$0).invoke(ComponentName.unflattenFromString(((ControlsServiceInfo) AppAdapter.access$getListOfServices$p(this.this$0).get(this.$index)).getKey()));
            }
        });
    }

    /* compiled from: AppAdapter.kt */
    /* loaded from: classes.dex */
    public static final class Holder extends RecyclerView.ViewHolder {
        private final FavoritesRenderer favRenderer;
        private final TextView favorites;
        private final ImageView icon;
        private final TextView title;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public Holder(View view, FavoritesRenderer favoritesRenderer) {
            super(view);
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(favoritesRenderer, "favRenderer");
            this.favRenderer = favoritesRenderer;
            View requireViewById = this.itemView.requireViewById(16908294);
            Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById(com.android.internal.R.id.icon)");
            this.icon = (ImageView) requireViewById;
            View requireViewById2 = this.itemView.requireViewById(16908310);
            Intrinsics.checkNotNullExpressionValue(requireViewById2, "itemView.requireViewById(com.android.internal.R.id.title)");
            this.title = (TextView) requireViewById2;
            View requireViewById3 = this.itemView.requireViewById(R$id.favorites);
            Intrinsics.checkNotNullExpressionValue(requireViewById3, "itemView.requireViewById(R.id.favorites)");
            this.favorites = (TextView) requireViewById3;
        }

        public final void bindData(ControlsServiceInfo controlsServiceInfo) {
            Intrinsics.checkNotNullParameter(controlsServiceInfo, "data");
            this.icon.setImageDrawable(controlsServiceInfo.loadIcon());
            this.title.setText(controlsServiceInfo.loadLabel());
            FavoritesRenderer favoritesRenderer = this.favRenderer;
            ComponentName componentName = controlsServiceInfo.componentName;
            Intrinsics.checkNotNullExpressionValue(componentName, "data.componentName");
            String renderFavoritesForComponent = favoritesRenderer.renderFavoritesForComponent(componentName);
            this.favorites.setText(renderFavoritesForComponent);
            this.favorites.setVisibility(renderFavoritesForComponent == null ? 8 : 0);
        }
    }
}
