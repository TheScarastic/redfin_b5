package com.android.systemui.controls.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.service.controls.Control;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Space;
import android.widget.TextView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.management.ControlsEditingActivity;
import com.android.systemui.controls.management.ControlsFavoritingActivity;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity;
import com.android.systemui.globalactions.GlobalActionsPopupMenu;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlin.ranges.RangesKt___RangesKt;
/* compiled from: ControlsUiControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsUiControllerImpl implements ControlsUiController {
    public static final Companion Companion = new Companion(null);
    private static final ComponentName EMPTY_COMPONENT;
    private static final StructureInfo EMPTY_STRUCTURE;
    private Context activityContext;
    private final ActivityStarter activityStarter;
    private List<StructureInfo> allStructures;
    private final DelayableExecutor bgExecutor;
    private final Collator collator;
    private final Context context;
    private final ControlActionCoordinator controlActionCoordinator;
    private final Lazy<ControlsController> controlsController;
    private final Lazy<ControlsListingController> controlsListingController;
    private final ControlsMetricsLogger controlsMetricsLogger;
    private final CustomIconCache iconCache;
    private final KeyguardStateController keyguardStateController;
    private ControlsListingController.ControlsListingCallback listingCallback;
    private final Comparator<SelectionItem> localeComparator;
    private Runnable onDismiss;
    private ViewGroup parent;
    private ListPopupWindow popup;
    private final ContextThemeWrapper popupThemedContext;
    private boolean retainCache;
    private final ShadeController shadeController;
    private final SharedPreferences sharedPreferences;
    private final DelayableExecutor uiExecutor;
    private StructureInfo selectedStructure = EMPTY_STRUCTURE;
    private final Map<ControlKey, ControlWithState> controlsById = new LinkedHashMap();
    private final Map<ControlKey, ControlViewHolder> controlViewsById = new LinkedHashMap();
    private boolean hidden = true;
    private final Consumer<Boolean> onSeedingComplete = new Consumer<Boolean>(this) { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$onSeedingComplete$1
        final /* synthetic */ ControlsUiControllerImpl this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        public final void accept(Boolean bool) {
            Object obj;
            Intrinsics.checkNotNullExpressionValue(bool, "accepted");
            if (bool.booleanValue()) {
                ControlsUiControllerImpl controlsUiControllerImpl = this.this$0;
                Iterator<T> it = controlsUiControllerImpl.getControlsController().get().getFavorites().iterator();
                if (!it.hasNext()) {
                    obj = null;
                } else {
                    obj = it.next();
                    if (it.hasNext()) {
                        int size = ((StructureInfo) obj).getControls().size();
                        do {
                            Object next = it.next();
                            int size2 = ((StructureInfo) next).getControls().size();
                            if (size < size2) {
                                obj = next;
                                size = size2;
                            }
                        } while (it.hasNext());
                    }
                }
                StructureInfo structureInfo = (StructureInfo) obj;
                if (structureInfo == null) {
                    structureInfo = ControlsUiControllerImpl.EMPTY_STRUCTURE;
                }
                controlsUiControllerImpl.selectedStructure = structureInfo;
                ControlsUiControllerImpl controlsUiControllerImpl2 = this.this$0;
                controlsUiControllerImpl2.updatePreferences(controlsUiControllerImpl2.selectedStructure);
            }
            ControlsUiControllerImpl controlsUiControllerImpl3 = this.this$0;
            ViewGroup viewGroup = controlsUiControllerImpl3.parent;
            if (viewGroup != null) {
                controlsUiControllerImpl3.reload(viewGroup);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                throw null;
            }
        }
    };

    public ControlsUiControllerImpl(Lazy<ControlsController> lazy, Context context, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, Lazy<ControlsListingController> lazy2, SharedPreferences sharedPreferences, ControlActionCoordinator controlActionCoordinator, ActivityStarter activityStarter, ShadeController shadeController, CustomIconCache customIconCache, ControlsMetricsLogger controlsMetricsLogger, KeyguardStateController keyguardStateController) {
        Intrinsics.checkNotNullParameter(lazy, "controlsController");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(delayableExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(delayableExecutor2, "bgExecutor");
        Intrinsics.checkNotNullParameter(lazy2, "controlsListingController");
        Intrinsics.checkNotNullParameter(sharedPreferences, "sharedPreferences");
        Intrinsics.checkNotNullParameter(controlActionCoordinator, "controlActionCoordinator");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(shadeController, "shadeController");
        Intrinsics.checkNotNullParameter(customIconCache, "iconCache");
        Intrinsics.checkNotNullParameter(controlsMetricsLogger, "controlsMetricsLogger");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        this.controlsController = lazy;
        this.context = context;
        this.uiExecutor = delayableExecutor;
        this.bgExecutor = delayableExecutor2;
        this.controlsListingController = lazy2;
        this.sharedPreferences = sharedPreferences;
        this.controlActionCoordinator = controlActionCoordinator;
        this.activityStarter = activityStarter;
        this.shadeController = shadeController;
        this.iconCache = customIconCache;
        this.controlsMetricsLogger = controlsMetricsLogger;
        this.keyguardStateController = keyguardStateController;
        this.popupThemedContext = new ContextThemeWrapper(context, R$style.Control_ListPopupWindow);
        Collator instance = Collator.getInstance(context.getResources().getConfiguration().getLocales().get(0));
        this.collator = instance;
        Intrinsics.checkNotNullExpressionValue(instance, "collator");
        this.localeComparator = new Comparator<T>(instance) { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$special$$inlined$compareBy$1
            final /* synthetic */ Comparator $comparator;

            {
                this.$comparator = r1;
            }

            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return this.$comparator.compare(((SelectionItem) t).getTitle(), ((SelectionItem) t2).getTitle());
            }
        };
    }

    public final Lazy<ControlsController> getControlsController() {
        return this.controlsController;
    }

    public final DelayableExecutor getUiExecutor() {
        return this.uiExecutor;
    }

    public final DelayableExecutor getBgExecutor() {
        return this.bgExecutor;
    }

    public final Lazy<ControlsListingController> getControlsListingController() {
        return this.controlsListingController;
    }

    public final ControlActionCoordinator getControlActionCoordinator() {
        return this.controlActionCoordinator;
    }

    /* compiled from: ControlsUiControllerImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    static {
        ComponentName componentName = new ComponentName("", "");
        EMPTY_COMPONENT = componentName;
        EMPTY_STRUCTURE = new StructureInfo(componentName, "", new ArrayList());
    }

    private final ControlsListingController.ControlsListingCallback createCallback(Function1<? super List<SelectionItem>, Unit> function1) {
        return new ControlsListingController.ControlsListingCallback(this, function1) { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1
            final /* synthetic */ Function1<List<SelectionItem>, Unit> $onResult;
            final /* synthetic */ ControlsUiControllerImpl this$0;

            /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlin.jvm.functions.Function1<? super java.util.List<com.android.systemui.controls.ui.SelectionItem>, kotlin.Unit> */
            /* JADX WARN: Multi-variable type inference failed */
            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$onResult = r2;
            }

            @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
            public void onServicesUpdated(List<ControlsServiceInfo> list) {
                Intrinsics.checkNotNullParameter(list, "serviceInfos");
                ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10));
                for (ControlsServiceInfo controlsServiceInfo : list) {
                    int i = controlsServiceInfo.getServiceInfo().applicationInfo.uid;
                    CharSequence loadLabel = controlsServiceInfo.loadLabel();
                    Intrinsics.checkNotNullExpressionValue(loadLabel, "it.loadLabel()");
                    Drawable loadIcon = controlsServiceInfo.loadIcon();
                    Intrinsics.checkNotNullExpressionValue(loadIcon, "it.loadIcon()");
                    ComponentName componentName = controlsServiceInfo.componentName;
                    Intrinsics.checkNotNullExpressionValue(componentName, "it.componentName");
                    arrayList.add(new SelectionItem(loadLabel, "", loadIcon, componentName, i));
                }
                this.this$0.getUiExecutor().execute(
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x005c: INVOKE  
                      (wrap: com.android.systemui.util.concurrency.DelayableExecutor : 0x004f: INVOKE  (r10v3 com.android.systemui.util.concurrency.DelayableExecutor A[REMOVE]) = 
                      (wrap: com.android.systemui.controls.ui.ControlsUiControllerImpl : 0x004d: IGET  (r10v2 com.android.systemui.controls.ui.ControlsUiControllerImpl A[REMOVE]) = (r9v0 'this' com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1.this$0 com.android.systemui.controls.ui.ControlsUiControllerImpl)
                     type: VIRTUAL call: com.android.systemui.controls.ui.ControlsUiControllerImpl.getUiExecutor():com.android.systemui.util.concurrency.DelayableExecutor)
                      (wrap: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1$onServicesUpdated$1 : 0x0059: CONSTRUCTOR  (r1v3 com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1$onServicesUpdated$1 A[REMOVE]) = 
                      (wrap: com.android.systemui.controls.ui.ControlsUiControllerImpl : 0x0055: IGET  (r2v0 com.android.systemui.controls.ui.ControlsUiControllerImpl A[REMOVE]) = (r9v0 'this' com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1.this$0 com.android.systemui.controls.ui.ControlsUiControllerImpl)
                      (r0v1 'arrayList' java.util.ArrayList)
                      (wrap: kotlin.jvm.functions.Function1<java.util.List<com.android.systemui.controls.ui.SelectionItem>, kotlin.Unit> : 0x0057: IGET  (r9v1 kotlin.jvm.functions.Function1<java.util.List<com.android.systemui.controls.ui.SelectionItem>, kotlin.Unit> A[REMOVE]) = (r9v0 'this' com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1.$onResult kotlin.jvm.functions.Function1)
                     call: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1$onServicesUpdated$1.<init>(com.android.systemui.controls.ui.ControlsUiControllerImpl, java.util.List, kotlin.jvm.functions.Function1):void type: CONSTRUCTOR)
                     type: INTERFACE call: java.util.concurrent.Executor.execute(java.lang.Runnable):void in method: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1.onServicesUpdated(java.util.List<com.android.systemui.controls.ControlsServiceInfo>):void, file: classes.dex
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
                    Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1$onServicesUpdated$1, state: NOT_LOADED
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
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)
                    java.util.ArrayList r0 = new java.util.ArrayList
                    r1 = 10
                    int r1 = kotlin.collections.CollectionsKt.collectionSizeOrDefault(r10, r1)
                    r0.<init>(r1)
                    java.util.Iterator r10 = r10.iterator()
                L_0x0014:
                    boolean r1 = r10.hasNext()
                    if (r1 == 0) goto L_0x004d
                    java.lang.Object r1 = r10.next()
                    com.android.systemui.controls.ControlsServiceInfo r1 = (com.android.systemui.controls.ControlsServiceInfo) r1
                    android.content.pm.ServiceInfo r2 = r1.getServiceInfo()
                    android.content.pm.ApplicationInfo r2 = r2.applicationInfo
                    int r8 = r2.uid
                    com.android.systemui.controls.ui.SelectionItem r2 = new com.android.systemui.controls.ui.SelectionItem
                    java.lang.CharSequence r4 = r1.loadLabel()
                    java.lang.String r3 = "it.loadLabel()"
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r4, r3)
                    android.graphics.drawable.Drawable r6 = r1.loadIcon()
                    java.lang.String r3 = "it.loadIcon()"
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r6, r3)
                    android.content.ComponentName r7 = r1.componentName
                    java.lang.String r1 = "it.componentName"
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r7, r1)
                    java.lang.String r5 = ""
                    r3 = r2
                    r3.<init>(r4, r5, r6, r7, r8)
                    r0.add(r2)
                    goto L_0x0014
                L_0x004d:
                    com.android.systemui.controls.ui.ControlsUiControllerImpl r10 = r9.this$0
                    com.android.systemui.util.concurrency.DelayableExecutor r10 = r10.getUiExecutor()
                    com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1$onServicesUpdated$1 r1 = new com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1$onServicesUpdated$1
                    com.android.systemui.controls.ui.ControlsUiControllerImpl r2 = r9.this$0
                    kotlin.jvm.functions.Function1<java.util.List<com.android.systemui.controls.ui.SelectionItem>, kotlin.Unit> r9 = r9.$onResult
                    r1.<init>(r2, r0, r9)
                    r10.execute(r1)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1.onServicesUpdated(java.util.List):void");
            }
        };
    }

    /* JADX DEBUG: Multi-variable search result rejected for r5v21, resolved type: java.util.Map<com.android.systemui.controls.ui.ControlKey, com.android.systemui.controls.ui.ControlWithState> */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.systemui.controls.ui.ControlsUiController
    public void show(ViewGroup viewGroup, Runnable runnable, Context context) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        Intrinsics.checkNotNullParameter(runnable, "onDismiss");
        Intrinsics.checkNotNullParameter(context, "activityContext");
        Log.d("ControlsUiController", "show()");
        this.parent = viewGroup;
        this.onDismiss = runnable;
        this.activityContext = context;
        this.hidden = false;
        this.retainCache = false;
        this.controlActionCoordinator.setActivityContext(context);
        List<StructureInfo> favorites = this.controlsController.get().getFavorites();
        this.allStructures = favorites;
        if (favorites != null) {
            this.selectedStructure = getPreferredStructure(favorites);
            if (this.controlsController.get().addSeedingFavoritesCallback(this.onSeedingComplete)) {
                this.listingCallback = createCallback(new Function1<List<? extends SelectionItem>, Unit>(this) { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$show$1
                    /* Return type fixed from 'java.lang.Object' to match base method */
                    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                    @Override // kotlin.jvm.functions.Function1
                    public /* bridge */ /* synthetic */ Unit invoke(List<? extends SelectionItem> list) {
                        invoke((List<SelectionItem>) list);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(List<SelectionItem> list) {
                        Intrinsics.checkNotNullParameter(list, "p0");
                        ((ControlsUiControllerImpl) this.receiver).showSeedingView(list);
                    }
                });
            } else {
                if (this.selectedStructure.getControls().isEmpty()) {
                    List<StructureInfo> list = this.allStructures;
                    if (list == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("allStructures");
                        throw null;
                    } else if (list.size() <= 1) {
                        this.listingCallback = createCallback(new Function1<List<? extends SelectionItem>, Unit>(this) { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$show$2
                            /* Return type fixed from 'java.lang.Object' to match base method */
                            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                            @Override // kotlin.jvm.functions.Function1
                            public /* bridge */ /* synthetic */ Unit invoke(List<? extends SelectionItem> list2) {
                                invoke((List<SelectionItem>) list2);
                                return Unit.INSTANCE;
                            }

                            public final void invoke(List<SelectionItem> list2) {
                                Intrinsics.checkNotNullParameter(list2, "p0");
                                ((ControlsUiControllerImpl) this.receiver).showInitialSetupView(list2);
                            }
                        });
                    }
                }
                List<ControlInfo> controls = this.selectedStructure.getControls();
                ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(controls, 10));
                for (ControlInfo controlInfo : controls) {
                    arrayList.add(new ControlWithState(this.selectedStructure.getComponentName(), controlInfo, null));
                }
                Map<ControlKey, ControlWithState> map = this.controlsById;
                for (Object obj : arrayList) {
                    map.put(new ControlKey(this.selectedStructure.getComponentName(), ((ControlWithState) obj).getCi().getControlId()), obj);
                }
                this.listingCallback = createCallback(new Function1<List<? extends SelectionItem>, Unit>(this) { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$show$5
                    /* Return type fixed from 'java.lang.Object' to match base method */
                    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                    @Override // kotlin.jvm.functions.Function1
                    public /* bridge */ /* synthetic */ Unit invoke(List<? extends SelectionItem> list2) {
                        invoke((List<SelectionItem>) list2);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(List<SelectionItem> list2) {
                        Intrinsics.checkNotNullParameter(list2, "p0");
                        ((ControlsUiControllerImpl) this.receiver).showControlsView(list2);
                    }
                });
                this.controlsController.get().subscribeToFavorites(this.selectedStructure);
            }
            ControlsListingController controlsListingController = this.controlsListingController.get();
            ControlsListingController.ControlsListingCallback controlsListingCallback = this.listingCallback;
            if (controlsListingCallback != null) {
                controlsListingController.addCallback(controlsListingCallback);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("listingCallback");
                throw null;
            }
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("allStructures");
            throw null;
        }
    }

    /* access modifiers changed from: private */
    public final void reload(ViewGroup viewGroup) {
        if (!this.hidden) {
            ControlsListingController controlsListingController = this.controlsListingController.get();
            ControlsListingController.ControlsListingCallback controlsListingCallback = this.listingCallback;
            if (controlsListingCallback != null) {
                controlsListingController.removeCallback(controlsListingCallback);
                this.controlsController.get().unsubscribe();
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(viewGroup, "alpha", 1.0f, 0.0f);
                ofFloat.setInterpolator(new AccelerateInterpolator(1.0f));
                ofFloat.setDuration(200L);
                ofFloat.addListener(new AnimatorListenerAdapter(this, viewGroup) { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$reload$1
                    final /* synthetic */ ViewGroup $parent;
                    final /* synthetic */ ControlsUiControllerImpl this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.this$0 = r1;
                        this.$parent = r2;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        Intrinsics.checkNotNullParameter(animator, "animation");
                        this.this$0.controlViewsById.clear();
                        this.this$0.controlsById.clear();
                        ControlsUiControllerImpl controlsUiControllerImpl = this.this$0;
                        ViewGroup viewGroup2 = this.$parent;
                        Runnable runnable = controlsUiControllerImpl.onDismiss;
                        if (runnable != null) {
                            Context context = this.this$0.activityContext;
                            if (context != null) {
                                controlsUiControllerImpl.show(viewGroup2, runnable, context);
                                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.$parent, "alpha", 0.0f, 1.0f);
                                ofFloat2.setInterpolator(new DecelerateInterpolator(1.0f));
                                ofFloat2.setDuration(200L);
                                ofFloat2.start();
                                return;
                            }
                            Intrinsics.throwUninitializedPropertyAccessException("activityContext");
                            throw null;
                        }
                        Intrinsics.throwUninitializedPropertyAccessException("onDismiss");
                        throw null;
                    }
                });
                ofFloat.start();
                return;
            }
            Intrinsics.throwUninitializedPropertyAccessException("listingCallback");
            throw null;
        }
    }

    /* access modifiers changed from: private */
    public final void showSeedingView(List<SelectionItem> list) {
        LayoutInflater from = LayoutInflater.from(this.context);
        int i = R$layout.controls_no_favorites;
        ViewGroup viewGroup = this.parent;
        if (viewGroup != null) {
            from.inflate(i, viewGroup, true);
            ViewGroup viewGroup2 = this.parent;
            if (viewGroup2 != null) {
                ((TextView) viewGroup2.requireViewById(R$id.controls_subtitle)).setText(this.context.getResources().getString(R$string.controls_seeding_in_progress));
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                throw null;
            }
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
    }

    /* access modifiers changed from: private */
    public final void showInitialSetupView(List<SelectionItem> list) {
        startProviderSelectorActivity();
        Runnable runnable = this.onDismiss;
        if (runnable != null) {
            runnable.run();
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("onDismiss");
            throw null;
        }
    }

    /* access modifiers changed from: private */
    public final void startFavoritingActivity(StructureInfo structureInfo) {
        startTargetedActivity(structureInfo, ControlsFavoritingActivity.class);
    }

    /* access modifiers changed from: private */
    public final void startEditingActivity(StructureInfo structureInfo) {
        startTargetedActivity(structureInfo, ControlsEditingActivity.class);
    }

    private final void startTargetedActivity(StructureInfo structureInfo, Class<?> cls) {
        Context context = this.activityContext;
        if (context != null) {
            Intent intent = new Intent(context, cls);
            putIntentExtras(intent, structureInfo);
            startActivity(intent);
            this.retainCache = true;
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("activityContext");
        throw null;
    }

    private final void putIntentExtras(Intent intent, StructureInfo structureInfo) {
        intent.putExtra("extra_app_label", getControlsListingController().get().getAppLabel(structureInfo.getComponentName()));
        intent.putExtra("extra_structure", structureInfo.getStructure());
        intent.putExtra("android.intent.extra.COMPONENT_NAME", structureInfo.getComponentName());
    }

    private final void startProviderSelectorActivity() {
        Context context = this.activityContext;
        if (context != null) {
            Intent intent = new Intent(context, ControlsProviderSelectorActivity.class);
            intent.putExtra("back_should_exit", true);
            startActivity(intent);
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("activityContext");
        throw null;
    }

    private final SelectionItem findSelectionItem(StructureInfo structureInfo, List<SelectionItem> list) {
        Object obj;
        boolean z;
        Iterator<T> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            SelectionItem selectionItem = (SelectionItem) obj;
            if (!Intrinsics.areEqual(selectionItem.getComponentName(), structureInfo.getComponentName()) || !Intrinsics.areEqual(selectionItem.getStructure(), structureInfo.getStructure())) {
                z = false;
                continue;
            } else {
                z = true;
                continue;
            }
            if (z) {
                break;
            }
        }
        return (SelectionItem) obj;
    }

    private final void startActivity(Intent intent) {
        intent.putExtra("extra_animate", true);
        if (this.keyguardStateController.isUnlocked()) {
            Context context = this.activityContext;
            if (context == null) {
                Intrinsics.throwUninitializedPropertyAccessException("activityContext");
                throw null;
            } else if (context != null) {
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, new Pair[0]).toBundle());
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("activityContext");
                throw null;
            }
        } else {
            this.activityStarter.postStartActivityDismissingKeyguard(intent, 0);
        }
    }

    /* access modifiers changed from: private */
    public final void showControlsView(List<SelectionItem> list) {
        this.controlViewsById.clear();
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt___RangesKt.coerceAtLeast(MapsKt__MapsJVMKt.mapCapacity(CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10)), 16));
        for (Object obj : list) {
            linkedHashMap.put(((SelectionItem) obj).getComponentName(), obj);
        }
        ArrayList arrayList = new ArrayList();
        List<StructureInfo> list2 = this.allStructures;
        if (list2 != null) {
            for (StructureInfo structureInfo : list2) {
                SelectionItem selectionItem = (SelectionItem) linkedHashMap.get(structureInfo.getComponentName());
                SelectionItem copy$default = selectionItem == null ? null : SelectionItem.copy$default(selectionItem, null, structureInfo.getStructure(), null, null, 0, 29, null);
                if (copy$default != null) {
                    arrayList.add(copy$default);
                }
            }
            CollectionsKt__MutableCollectionsJVMKt.sortWith(arrayList, this.localeComparator);
            SelectionItem findSelectionItem = findSelectionItem(this.selectedStructure, arrayList);
            if (findSelectionItem == null) {
                findSelectionItem = list.get(0);
            }
            this.controlsMetricsLogger.refreshBegin(findSelectionItem.getUid(), !this.keyguardStateController.isUnlocked());
            createListView(findSelectionItem);
            createDropDown(arrayList, findSelectionItem);
            createMenu();
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("allStructures");
        throw null;
    }

    /* JADX WARN: Type inference failed for: r2v4, types: [android.widget.ArrayAdapter, T] */
    private final void createMenu() {
        String[] strArr = {this.context.getResources().getString(R$string.controls_menu_add), this.context.getResources().getString(R$string.controls_menu_edit)};
        Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
        ref$ObjectRef.element = new ArrayAdapter(this.context, R$layout.controls_more_item, strArr);
        ViewGroup viewGroup = this.parent;
        if (viewGroup != null) {
            ImageView imageView = (ImageView) viewGroup.requireViewById(R$id.controls_more);
            imageView.setOnClickListener(new View.OnClickListener(this, imageView, ref$ObjectRef) { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createMenu$1
                final /* synthetic */ Ref$ObjectRef<ArrayAdapter<String>> $adapter;
                final /* synthetic */ ImageView $anchor;
                final /* synthetic */ ControlsUiControllerImpl this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$anchor = r2;
                    this.$adapter = r3;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Intrinsics.checkNotNullParameter(view, "v");
                    ControlsUiControllerImpl controlsUiControllerImpl = this.this$0;
                    GlobalActionsPopupMenu globalActionsPopupMenu = new GlobalActionsPopupMenu(this.this$0.popupThemedContext, false);
                    ImageView imageView2 = this.$anchor;
                    Ref$ObjectRef<ArrayAdapter<String>> ref$ObjectRef2 = this.$adapter;
                    ControlsUiControllerImpl controlsUiControllerImpl2 = this.this$0;
                    globalActionsPopupMenu.setAnchorView(imageView2);
                    globalActionsPopupMenu.setAdapter(ref$ObjectRef2.element);
                    globalActionsPopupMenu.setOnItemClickListener(
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0029: INVOKE  
                          (r0v1 'globalActionsPopupMenu' com.android.systemui.globalactions.GlobalActionsPopupMenu)
                          (wrap: com.android.systemui.controls.ui.ControlsUiControllerImpl$createMenu$1$onClick$1$1 : 0x0026: CONSTRUCTOR  (r1v5 com.android.systemui.controls.ui.ControlsUiControllerImpl$createMenu$1$onClick$1$1 A[REMOVE]) = 
                          (r3v1 'controlsUiControllerImpl2' com.android.systemui.controls.ui.ControlsUiControllerImpl)
                          (r0v1 'globalActionsPopupMenu' com.android.systemui.globalactions.GlobalActionsPopupMenu)
                         call: com.android.systemui.controls.ui.ControlsUiControllerImpl$createMenu$1$onClick$1$1.<init>(com.android.systemui.controls.ui.ControlsUiControllerImpl, com.android.systemui.globalactions.GlobalActionsPopupMenu):void type: CONSTRUCTOR)
                         type: VIRTUAL call: android.widget.ListPopupWindow.setOnItemClickListener(android.widget.AdapterView$OnItemClickListener):void in method: com.android.systemui.controls.ui.ControlsUiControllerImpl$createMenu$1.onClick(android.view.View):void, file: classes.dex
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
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createMenu$1$onClick$1$1, state: NOT_LOADED
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
                        java.lang.String r0 = "v"
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
                        com.android.systemui.controls.ui.ControlsUiControllerImpl r4 = r3.this$0
                        com.android.systemui.globalactions.GlobalActionsPopupMenu r0 = new com.android.systemui.globalactions.GlobalActionsPopupMenu
                        com.android.systemui.controls.ui.ControlsUiControllerImpl r1 = r3.this$0
                        android.view.ContextThemeWrapper r1 = com.android.systemui.controls.ui.ControlsUiControllerImpl.access$getPopupThemedContext$p(r1)
                        r2 = 0
                        r0.<init>(r1, r2)
                        android.widget.ImageView r1 = r3.$anchor
                        kotlin.jvm.internal.Ref$ObjectRef<android.widget.ArrayAdapter<java.lang.String>> r2 = r3.$adapter
                        com.android.systemui.controls.ui.ControlsUiControllerImpl r3 = r3.this$0
                        r0.setAnchorView(r1)
                        T r1 = r2.element
                        android.widget.ListAdapter r1 = (android.widget.ListAdapter) r1
                        r0.setAdapter(r1)
                        com.android.systemui.controls.ui.ControlsUiControllerImpl$createMenu$1$onClick$1$1 r1 = new com.android.systemui.controls.ui.ControlsUiControllerImpl$createMenu$1$onClick$1$1
                        r1.<init>(r3, r0)
                        r0.setOnItemClickListener(r1)
                        r0.show()
                        kotlin.Unit r3 = kotlin.Unit.INSTANCE
                        com.android.systemui.controls.ui.ControlsUiControllerImpl.access$setPopup$p(r4, r0)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.ui.ControlsUiControllerImpl$createMenu$1.onClick(android.view.View):void");
                }
            });
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("parent");
        throw null;
    }

    private final void createListView(SelectionItem selectionItem) {
        LayoutInflater from = LayoutInflater.from(this.context);
        int i = R$layout.controls_with_favorites;
        ViewGroup viewGroup = this.parent;
        if (viewGroup != null) {
            from.inflate(i, viewGroup, true);
            ViewGroup viewGroup2 = this.parent;
            if (viewGroup2 != null) {
                ImageView imageView = (ImageView) viewGroup2.requireViewById(R$id.controls_close);
                imageView.setOnClickListener(new View.OnClickListener(this) { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createListView$1$1
                    final /* synthetic */ ControlsUiControllerImpl this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.this$0 = r1;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        Intrinsics.checkNotNullParameter(view, "$noName_0");
                        Runnable runnable = this.this$0.onDismiss;
                        if (runnable != null) {
                            runnable.run();
                        } else {
                            Intrinsics.throwUninitializedPropertyAccessException("onDismiss");
                            throw null;
                        }
                    }
                });
                imageView.setVisibility(0);
                int findMaxColumns = findMaxColumns();
                ViewGroup viewGroup3 = this.parent;
                if (viewGroup3 != null) {
                    View requireViewById = viewGroup3.requireViewById(R$id.global_actions_controls_list);
                    Objects.requireNonNull(requireViewById, "null cannot be cast to non-null type android.view.ViewGroup");
                    ViewGroup viewGroup4 = (ViewGroup) requireViewById;
                    Intrinsics.checkNotNullExpressionValue(from, "inflater");
                    ViewGroup createRow = createRow(from, viewGroup4);
                    for (ControlInfo controlInfo : this.selectedStructure.getControls()) {
                        ControlKey controlKey = new ControlKey(this.selectedStructure.getComponentName(), controlInfo.getControlId());
                        ControlWithState controlWithState = this.controlsById.get(controlKey);
                        if (controlWithState != null) {
                            if (createRow.getChildCount() == findMaxColumns) {
                                createRow = createRow(from, viewGroup4);
                            }
                            View inflate = from.inflate(R$layout.controls_base_item, createRow, false);
                            Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
                            ViewGroup viewGroup5 = (ViewGroup) inflate;
                            createRow.addView(viewGroup5);
                            if (createRow.getChildCount() == 1) {
                                ViewGroup.LayoutParams layoutParams = viewGroup5.getLayoutParams();
                                Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                                ((ViewGroup.MarginLayoutParams) layoutParams).setMarginStart(0);
                            }
                            ControlsController controlsController = getControlsController().get();
                            Intrinsics.checkNotNullExpressionValue(controlsController, "controlsController.get()");
                            ControlViewHolder controlViewHolder = new ControlViewHolder(viewGroup5, controlsController, getUiExecutor(), getBgExecutor(), getControlActionCoordinator(), this.controlsMetricsLogger, selectionItem.getUid());
                            controlViewHolder.bindData(controlWithState, false);
                            this.controlViewsById.put(controlKey, controlViewHolder);
                        }
                    }
                    int size = this.selectedStructure.getControls().size() % findMaxColumns;
                    int dimensionPixelSize = this.context.getResources().getDimensionPixelSize(R$dimen.control_spacing);
                    for (int i2 = size == 0 ? 0 : findMaxColumns - size; i2 > 0; i2--) {
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, 0, 1.0f);
                        layoutParams2.setMarginStart(dimensionPixelSize);
                        createRow.addView(new Space(this.context), layoutParams2);
                    }
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                throw null;
            }
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("parent");
        throw null;
    }

    private final int findMaxColumns() {
        int i;
        Resources resources = this.context.getResources();
        int integer = resources.getInteger(R$integer.controls_max_columns);
        int integer2 = resources.getInteger(R$integer.controls_max_columns_adjust_below_width_dp);
        TypedValue typedValue = new TypedValue();
        boolean z = true;
        resources.getValue(R$dimen.controls_max_columns_adjust_above_font_scale, typedValue, true);
        float f = typedValue.getFloat();
        Configuration configuration = resources.getConfiguration();
        if (configuration.orientation != 1) {
            z = false;
        }
        return (!z || (i = configuration.screenWidthDp) == 0 || i > integer2 || configuration.fontScale < f) ? integer : integer - 1;
    }

    @Override // com.android.systemui.controls.ui.ControlsUiController
    public StructureInfo getPreferredStructure(List<StructureInfo> list) {
        ComponentName componentName;
        boolean z;
        Intrinsics.checkNotNullParameter(list, "structures");
        if (list.isEmpty()) {
            return EMPTY_STRUCTURE;
        }
        Object obj = null;
        String string = this.sharedPreferences.getString("controls_component", null);
        if (string == null) {
            componentName = null;
        } else {
            componentName = ComponentName.unflattenFromString(string);
        }
        if (componentName == null) {
            componentName = EMPTY_COMPONENT;
        }
        String string2 = this.sharedPreferences.getString("controls_structure", "");
        Iterator<T> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            StructureInfo structureInfo = (StructureInfo) next;
            if (!Intrinsics.areEqual(componentName, structureInfo.getComponentName()) || !Intrinsics.areEqual(string2, structureInfo.getStructure())) {
                z = false;
                continue;
            } else {
                z = true;
                continue;
            }
            if (z) {
                obj = next;
                break;
            }
        }
        StructureInfo structureInfo2 = (StructureInfo) obj;
        return structureInfo2 == null ? list.get(0) : structureInfo2;
    }

    /* access modifiers changed from: private */
    public final void updatePreferences(StructureInfo structureInfo) {
        if (!Intrinsics.areEqual(structureInfo, EMPTY_STRUCTURE)) {
            this.sharedPreferences.edit().putString("controls_component", structureInfo.getComponentName().flattenToString()).putString("controls_structure", structureInfo.getStructure().toString()).commit();
        }
    }

    /* access modifiers changed from: private */
    public final void switchAppOrStructure(SelectionItem selectionItem) {
        boolean z;
        List<StructureInfo> list = this.allStructures;
        if (list != null) {
            for (StructureInfo structureInfo : list) {
                if (!Intrinsics.areEqual(structureInfo.getStructure(), selectionItem.getStructure()) || !Intrinsics.areEqual(structureInfo.getComponentName(), selectionItem.getComponentName())) {
                    z = false;
                    continue;
                } else {
                    z = true;
                    continue;
                }
                if (z) {
                    if (!Intrinsics.areEqual(structureInfo, this.selectedStructure)) {
                        this.selectedStructure = structureInfo;
                        updatePreferences(structureInfo);
                        ViewGroup viewGroup = this.parent;
                        if (viewGroup != null) {
                            reload(viewGroup);
                            return;
                        } else {
                            Intrinsics.throwUninitializedPropertyAccessException("parent");
                            throw null;
                        }
                    } else {
                        return;
                    }
                }
            }
            throw new NoSuchElementException("Collection contains no element matching the predicate.");
        }
        Intrinsics.throwUninitializedPropertyAccessException("allStructures");
        throw null;
    }

    public void closeDialogs(boolean z) {
        if (z) {
            ListPopupWindow listPopupWindow = this.popup;
            if (listPopupWindow != null) {
                listPopupWindow.dismissImmediate();
            }
        } else {
            ListPopupWindow listPopupWindow2 = this.popup;
            if (listPopupWindow2 != null) {
                listPopupWindow2.dismiss();
            }
        }
        this.popup = null;
        for (Map.Entry<ControlKey, ControlViewHolder> entry : this.controlViewsById.entrySet()) {
            entry.getValue().dismiss();
        }
        this.controlActionCoordinator.closeDialogs();
    }

    @Override // com.android.systemui.controls.ui.ControlsUiController
    public void hide() {
        this.hidden = true;
        closeDialogs(true);
        this.controlsController.get().unsubscribe();
        ViewGroup viewGroup = this.parent;
        if (viewGroup != null) {
            viewGroup.removeAllViews();
            this.controlsById.clear();
            this.controlViewsById.clear();
            ControlsListingController controlsListingController = this.controlsListingController.get();
            ControlsListingController.ControlsListingCallback controlsListingCallback = this.listingCallback;
            if (controlsListingCallback != null) {
                controlsListingController.removeCallback(controlsListingCallback);
                if (!this.retainCache) {
                    RenderInfo.Companion.clearCache();
                    return;
                }
                return;
            }
            Intrinsics.throwUninitializedPropertyAccessException("listingCallback");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("parent");
        throw null;
    }

    @Override // com.android.systemui.controls.ui.ControlsUiController
    public void onRefreshState(ComponentName componentName, List<Control> list) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(list, "controls");
        boolean z = !this.keyguardStateController.isUnlocked();
        for (Control control : list) {
            Map<ControlKey, ControlWithState> map = this.controlsById;
            String controlId = control.getControlId();
            Intrinsics.checkNotNullExpressionValue(controlId, "c.getControlId()");
            ControlWithState controlWithState = map.get(new ControlKey(componentName, controlId));
            if (controlWithState != null) {
                Log.d("ControlsUiController", Intrinsics.stringPlus("onRefreshState() for id: ", control.getControlId()));
                CustomIconCache customIconCache = this.iconCache;
                String controlId2 = control.getControlId();
                Intrinsics.checkNotNullExpressionValue(controlId2, "c.controlId");
                customIconCache.store(componentName, controlId2, control.getCustomIcon());
                ControlWithState controlWithState2 = new ControlWithState(componentName, controlWithState.getCi(), control);
                String controlId3 = control.getControlId();
                Intrinsics.checkNotNullExpressionValue(controlId3, "c.getControlId()");
                ControlKey controlKey = new ControlKey(componentName, controlId3);
                this.controlsById.put(controlKey, controlWithState2);
                ControlViewHolder controlViewHolder = this.controlViewsById.get(controlKey);
                if (controlViewHolder != null) {
                    getUiExecutor().execute(new Runnable(controlViewHolder, controlWithState2, z) { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$onRefreshState$1$1$1$1
                        final /* synthetic */ ControlWithState $cws;
                        final /* synthetic */ boolean $isLocked;
                        final /* synthetic */ ControlViewHolder $it;

                        /* access modifiers changed from: package-private */
                        {
                            this.$it = r1;
                            this.$cws = r2;
                            this.$isLocked = r3;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            this.$it.bindData(this.$cws, this.$isLocked);
                        }
                    });
                }
            }
        }
    }

    @Override // com.android.systemui.controls.ui.ControlsUiController
    public void onActionResponse(ComponentName componentName, String str, int i) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(str, "controlId");
        this.uiExecutor.execute(new Runnable(this, new ControlKey(componentName, str), i) { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$onActionResponse$1
            final /* synthetic */ ControlKey $key;
            final /* synthetic */ int $response;
            final /* synthetic */ ControlsUiControllerImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$key = r2;
                this.$response = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ControlViewHolder controlViewHolder = (ControlViewHolder) this.this$0.controlViewsById.get(this.$key);
                if (controlViewHolder != null) {
                    controlViewHolder.actionResponse(this.$response);
                }
            }
        });
    }

    private final ViewGroup createRow(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(R$layout.controls_row, viewGroup, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
        ViewGroup viewGroup2 = (ViewGroup) inflate;
        viewGroup.addView(viewGroup2);
        return viewGroup2;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.controls.ui.ItemAdapter, android.widget.ArrayAdapter, T] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void createDropDown(java.util.List<com.android.systemui.controls.ui.SelectionItem> r7, com.android.systemui.controls.ui.SelectionItem r8) {
        /*
            r6 = this;
            java.util.Iterator r0 = r7.iterator()
        L_0x0004:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x001e
            java.lang.Object r1 = r0.next()
            com.android.systemui.controls.ui.SelectionItem r1 = (com.android.systemui.controls.ui.SelectionItem) r1
            com.android.systemui.controls.ui.RenderInfo$Companion r2 = com.android.systemui.controls.ui.RenderInfo.Companion
            android.content.ComponentName r3 = r1.getComponentName()
            android.graphics.drawable.Drawable r1 = r1.getIcon()
            r2.registerComponentIcon(r3, r1)
            goto L_0x0004
        L_0x001e:
            kotlin.jvm.internal.Ref$ObjectRef r0 = new kotlin.jvm.internal.Ref$ObjectRef
            r0.<init>()
            com.android.systemui.controls.ui.ItemAdapter r1 = new com.android.systemui.controls.ui.ItemAdapter
            android.content.Context r2 = r6.context
            int r3 = com.android.systemui.R$layout.controls_spinner_item
            r1.<init>(r2, r3)
            r1.addAll(r7)
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            r0.element = r1
            android.view.ViewGroup r1 = r6.parent
            java.lang.String r2 = "parent"
            r3 = 0
            if (r1 == 0) goto L_0x008e
            int r4 = com.android.systemui.R$id.app_or_structure_spinner
            android.view.View r1 = r1.requireViewById(r4)
            android.widget.TextView r1 = (android.widget.TextView) r1
            java.lang.CharSequence r8 = r8.getTitle()
            r1.setText(r8)
            android.graphics.drawable.Drawable r8 = r1.getBackground()
            java.lang.String r4 = "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable"
            java.util.Objects.requireNonNull(r8, r4)
            android.graphics.drawable.LayerDrawable r8 = (android.graphics.drawable.LayerDrawable) r8
            r4 = 0
            android.graphics.drawable.Drawable r8 = r8.getDrawable(r4)
            android.content.Context r4 = r1.getContext()
            android.content.res.Resources r4 = r4.getResources()
            int r5 = com.android.systemui.R$color.control_spinner_dropdown
            int r4 = r4.getColor(r5, r3)
            r8.setTint(r4)
            int r7 = r7.size()
            r8 = 1
            if (r7 != r8) goto L_0x0075
            r1.setBackground(r3)
            return
        L_0x0075:
            android.view.ViewGroup r7 = r6.parent
            if (r7 == 0) goto L_0x008a
            int r8 = com.android.systemui.R$id.controls_header
            android.view.View r7 = r7.requireViewById(r8)
            android.view.ViewGroup r7 = (android.view.ViewGroup) r7
            com.android.systemui.controls.ui.ControlsUiControllerImpl$createDropDown$2 r8 = new com.android.systemui.controls.ui.ControlsUiControllerImpl$createDropDown$2
            r8.<init>(r6, r7, r0)
            r7.setOnClickListener(r8)
            return
        L_0x008a:
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r2)
            throw r3
        L_0x008e:
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.ui.ControlsUiControllerImpl.createDropDown(java.util.List, com.android.systemui.controls.ui.SelectionItem):void");
    }
}
