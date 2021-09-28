package com.android.systemui.controls.management;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.viewpager2.widget.ViewPager2;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.systemui.Prefs;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.TooltipManager;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.util.LifecycleActivity;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsFavoritingActivity.kt */
/* loaded from: classes.dex */
public final class ControlsFavoritingActivity extends LifecycleActivity {
    public static final Companion Companion = new Companion(null);
    private CharSequence appName;
    private final BroadcastDispatcher broadcastDispatcher;
    private Runnable cancelLoadRunnable;
    private Comparator<StructureContainer> comparator;
    private ComponentName component;
    private final ControlsControllerImpl controller;
    private final ControlsFavoritingActivity$currentUserTracker$1 currentUserTracker;
    private View doneButton;
    private final Executor executor;
    private boolean fromProviderSelector;
    private boolean isPagerLoaded;
    private final ControlsListingController listingController;
    private TooltipManager mTooltipManager;
    private View otherAppsButton;
    private ManagementPageIndicator pageIndicator;
    private TextView statusText;
    private CharSequence structureExtra;
    private ViewPager2 structurePager;
    private TextView subtitleView;
    private TextView titleView;
    private final ControlsUiController uiController;
    private List<StructureContainer> listOfStructures = CollectionsKt__CollectionsKt.emptyList();
    private final ControlsFavoritingActivity$listingCallback$1 listingCallback = new ControlsFavoritingActivity$listingCallback$1(this);
    private final ControlsFavoritingActivity$controlsModelCallback$1 controlsModelCallback = new ControlsFavoritingActivity$controlsModelCallback$1(this);

    public ControlsFavoritingActivity(Executor executor, ControlsControllerImpl controlsControllerImpl, ControlsListingController controlsListingController, BroadcastDispatcher broadcastDispatcher, ControlsUiController controlsUiController) {
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "controller");
        Intrinsics.checkNotNullParameter(controlsListingController, "listingController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(controlsUiController, "uiController");
        this.executor = executor;
        this.controller = controlsControllerImpl;
        this.listingController = controlsListingController;
        this.broadcastDispatcher = broadcastDispatcher;
        this.uiController = controlsUiController;
        this.currentUserTracker = new ControlsFavoritingActivity$currentUserTracker$1(this, broadcastDispatcher);
    }

    /* compiled from: ControlsFavoritingActivity.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (!this.fromProviderSelector) {
            openControlsOrigin();
        }
        animateExitAndFinish();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Collator instance = Collator.getInstance(getResources().getConfiguration().getLocales().get(0));
        Intrinsics.checkNotNullExpressionValue(instance, "collator");
        this.comparator = new Comparator<T>(instance) { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$onCreate$$inlined$compareBy$1
            final /* synthetic */ Comparator $comparator;

            {
                this.$comparator = r1;
            }

            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return this.$comparator.compare(((StructureContainer) t).getStructureName(), ((StructureContainer) t2).getStructureName());
            }
        };
        this.appName = getIntent().getCharSequenceExtra("extra_app_label");
        this.structureExtra = getIntent().getCharSequenceExtra("extra_structure");
        this.component = (ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME");
        this.fromProviderSelector = getIntent().getBooleanExtra("extra_from_provider_selector", false);
        bindViews();
    }

    private final void loadControls() {
        ComponentName componentName = this.component;
        if (componentName != null) {
            TextView textView = this.statusText;
            if (textView != null) {
                textView.setText(getResources().getText(17040505));
                this.controller.loadForComponent(componentName, new Consumer<ControlsController.LoadData>(this, getResources().getText(R$string.controls_favorite_other_zone_header)) { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$1
                    final /* synthetic */ CharSequence $emptyZoneString;
                    final /* synthetic */ ControlsFavoritingActivity this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.this$0 = r1;
                        this.$emptyZoneString = r2;
                    }

                    public final void accept(ControlsController.LoadData loadData) {
                        Intrinsics.checkNotNullParameter(loadData, "data");
                        List<ControlStatus> allControls = loadData.getAllControls();
                        List<String> favoritesIds = loadData.getFavoritesIds();
                        final boolean errorOnLoad = loadData.getErrorOnLoad();
                        LinkedHashMap linkedHashMap = new LinkedHashMap();
                        for (Object obj : allControls) {
                            CharSequence structure = ((ControlStatus) obj).getControl().getStructure();
                            if (structure == null) {
                                structure = "";
                            }
                            Object obj2 = linkedHashMap.get(structure);
                            if (obj2 == null) {
                                obj2 = new ArrayList();
                                linkedHashMap.put(structure, obj2);
                            }
                            ((List) obj2).add(obj);
                        }
                        ControlsFavoritingActivity controlsFavoritingActivity = this.this$0;
                        CharSequence charSequence = this.$emptyZoneString;
                        ArrayList arrayList = new ArrayList(linkedHashMap.size());
                        for (Map.Entry entry : linkedHashMap.entrySet()) {
                            Intrinsics.checkNotNullExpressionValue(charSequence, "emptyZoneString");
                            arrayList.add(new StructureContainer((CharSequence) entry.getKey(), new AllModel((List) entry.getValue(), favoritesIds, charSequence, controlsFavoritingActivity.controlsModelCallback)));
                        }
                        Comparator comparator = this.this$0.comparator;
                        if (comparator != null) {
                            controlsFavoritingActivity.listOfStructures = CollectionsKt___CollectionsKt.sortedWith(arrayList, comparator);
                            List list = this.this$0.listOfStructures;
                            ControlsFavoritingActivity controlsFavoritingActivity2 = this.this$0;
                            Iterator it = list.iterator();
                            final int i = 0;
                            while (true) {
                                if (!it.hasNext()) {
                                    i = -1;
                                    break;
                                } else if (Intrinsics.areEqual(((StructureContainer) it.next()).getStructureName(), controlsFavoritingActivity2.structureExtra)) {
                                    break;
                                } else {
                                    i++;
                                }
                            }
                            if (i == -1) {
                                i = 0;
                            }
                            if (this.this$0.getIntent().getBooleanExtra("extra_single_structure", false)) {
                                ControlsFavoritingActivity controlsFavoritingActivity3 = this.this$0;
                                controlsFavoritingActivity3.listOfStructures = CollectionsKt__CollectionsJVMKt.listOf(controlsFavoritingActivity3.listOfStructures.get(i));
                            }
                            Executor executor = this.this$0.executor;
                            final ControlsFavoritingActivity controlsFavoritingActivity4 = this.this$0;
                            executor.execute(new Runnable() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$1.2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ViewPager2 viewPager2 = controlsFavoritingActivity4.structurePager;
                                    if (viewPager2 != null) {
                                        viewPager2.setAdapter(new StructureAdapter(controlsFavoritingActivity4.listOfStructures));
                                        ViewPager2 viewPager22 = controlsFavoritingActivity4.structurePager;
                                        if (viewPager22 != null) {
                                            viewPager22.setCurrentItem(i);
                                            int i2 = 0;
                                            if (errorOnLoad) {
                                                TextView textView2 = controlsFavoritingActivity4.statusText;
                                                if (textView2 != null) {
                                                    Resources resources = controlsFavoritingActivity4.getResources();
                                                    int i3 = R$string.controls_favorite_load_error;
                                                    Object[] objArr = new Object[1];
                                                    Object obj3 = controlsFavoritingActivity4.appName;
                                                    if (obj3 == null) {
                                                        obj3 = "";
                                                    }
                                                    objArr[0] = obj3;
                                                    textView2.setText(resources.getString(i3, objArr));
                                                    TextView textView3 = controlsFavoritingActivity4.subtitleView;
                                                    if (textView3 != null) {
                                                        textView3.setVisibility(8);
                                                    } else {
                                                        Intrinsics.throwUninitializedPropertyAccessException("subtitleView");
                                                        throw null;
                                                    }
                                                } else {
                                                    Intrinsics.throwUninitializedPropertyAccessException("statusText");
                                                    throw null;
                                                }
                                            } else if (controlsFavoritingActivity4.listOfStructures.isEmpty()) {
                                                TextView textView4 = controlsFavoritingActivity4.statusText;
                                                if (textView4 != null) {
                                                    textView4.setText(controlsFavoritingActivity4.getResources().getString(R$string.controls_favorite_load_none));
                                                    TextView textView5 = controlsFavoritingActivity4.subtitleView;
                                                    if (textView5 != null) {
                                                        textView5.setVisibility(8);
                                                    } else {
                                                        Intrinsics.throwUninitializedPropertyAccessException("subtitleView");
                                                        throw null;
                                                    }
                                                } else {
                                                    Intrinsics.throwUninitializedPropertyAccessException("statusText");
                                                    throw null;
                                                }
                                            } else {
                                                TextView textView6 = controlsFavoritingActivity4.statusText;
                                                if (textView6 != null) {
                                                    textView6.setVisibility(8);
                                                    ManagementPageIndicator managementPageIndicator = controlsFavoritingActivity4.pageIndicator;
                                                    if (managementPageIndicator != null) {
                                                        managementPageIndicator.setNumPages(controlsFavoritingActivity4.listOfStructures.size());
                                                        ManagementPageIndicator managementPageIndicator2 = controlsFavoritingActivity4.pageIndicator;
                                                        if (managementPageIndicator2 != null) {
                                                            managementPageIndicator2.setLocation(0.0f);
                                                            ManagementPageIndicator managementPageIndicator3 = controlsFavoritingActivity4.pageIndicator;
                                                            if (managementPageIndicator3 != null) {
                                                                if (controlsFavoritingActivity4.listOfStructures.size() <= 1) {
                                                                    i2 = 4;
                                                                }
                                                                managementPageIndicator3.setVisibility(i2);
                                                                ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
                                                                ManagementPageIndicator managementPageIndicator4 = controlsFavoritingActivity4.pageIndicator;
                                                                if (managementPageIndicator4 != null) {
                                                                    Animator enterAnimation = controlsAnimations.enterAnimation(managementPageIndicator4);
                                                                    enterAnimation.addListener(new ControlsFavoritingActivity$loadControls$1$1$2$1$1(controlsFavoritingActivity4));
                                                                    enterAnimation.start();
                                                                    ViewPager2 viewPager23 = controlsFavoritingActivity4.structurePager;
                                                                    if (viewPager23 != null) {
                                                                        controlsAnimations.enterAnimation(viewPager23).start();
                                                                    } else {
                                                                        Intrinsics.throwUninitializedPropertyAccessException("structurePager");
                                                                        throw null;
                                                                    }
                                                                } else {
                                                                    Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                                                                    throw null;
                                                                }
                                                            } else {
                                                                Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                                                                throw null;
                                                            }
                                                        } else {
                                                            Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                                                            throw null;
                                                        }
                                                    } else {
                                                        Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                                                        throw null;
                                                    }
                                                } else {
                                                    Intrinsics.throwUninitializedPropertyAccessException("statusText");
                                                    throw null;
                                                }
                                            }
                                        } else {
                                            Intrinsics.throwUninitializedPropertyAccessException("structurePager");
                                            throw null;
                                        }
                                    } else {
                                        Intrinsics.throwUninitializedPropertyAccessException("structurePager");
                                        throw null;
                                    }
                                }
                            });
                            return;
                        }
                        Intrinsics.throwUninitializedPropertyAccessException("comparator");
                        throw null;
                    }
                }, new Consumer<Runnable>(this) { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$2
                    final /* synthetic */ ControlsFavoritingActivity this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.this$0 = r1;
                    }

                    public final void accept(Runnable runnable) {
                        Intrinsics.checkNotNullParameter(runnable, "runnable");
                        this.this$0.cancelLoadRunnable = runnable;
                    }
                });
                return;
            }
            Intrinsics.throwUninitializedPropertyAccessException("statusText");
            throw null;
        }
    }

    private final void setUpPager() {
        ViewPager2 viewPager2 = this.structurePager;
        if (viewPager2 != null) {
            viewPager2.setAlpha(0.0f);
            ManagementPageIndicator managementPageIndicator = this.pageIndicator;
            if (managementPageIndicator != null) {
                managementPageIndicator.setAlpha(0.0f);
                ViewPager2 viewPager22 = this.structurePager;
                if (viewPager22 != null) {
                    viewPager22.setAdapter(new StructureAdapter(CollectionsKt__CollectionsKt.emptyList()));
                    viewPager22.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(this) { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$setUpPager$1$1
                        final /* synthetic */ ControlsFavoritingActivity this$0;

                        /* access modifiers changed from: package-private */
                        {
                            this.this$0 = r1;
                        }

                        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
                        public void onPageSelected(int i) {
                            super.onPageSelected(i);
                            CharSequence structureName = ((StructureContainer) this.this$0.listOfStructures.get(i)).getStructureName();
                            if (TextUtils.isEmpty(structureName)) {
                                structureName = this.this$0.appName;
                            }
                            TextView textView = this.this$0.titleView;
                            if (textView != null) {
                                textView.setText(structureName);
                                TextView textView2 = this.this$0.titleView;
                                if (textView2 != null) {
                                    textView2.requestFocus();
                                } else {
                                    Intrinsics.throwUninitializedPropertyAccessException("titleView");
                                    throw null;
                                }
                            } else {
                                Intrinsics.throwUninitializedPropertyAccessException("titleView");
                                throw null;
                            }
                        }

                        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
                        public void onPageScrolled(int i, float f, int i2) {
                            super.onPageScrolled(i, f, i2);
                            ManagementPageIndicator managementPageIndicator2 = this.this$0.pageIndicator;
                            if (managementPageIndicator2 != null) {
                                managementPageIndicator2.setLocation(((float) i) + f);
                            } else {
                                Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                                throw null;
                            }
                        }
                    });
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException("structurePager");
                throw null;
            }
            Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("structurePager");
        throw null;
    }

    private final void bindViews() {
        setContentView(R$layout.controls_management);
        Lifecycle lifecycle = getLifecycle();
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        View requireViewById = requireViewById(R$id.controls_management_root);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGroup>(R.id.controls_management_root)");
        Window window = getWindow();
        Intrinsics.checkNotNullExpressionValue(window, "window");
        Intent intent = getIntent();
        Intrinsics.checkNotNullExpressionValue(intent, "intent");
        lifecycle.addObserver(controlsAnimations.observerForAnimations((ViewGroup) requireViewById, window, intent));
        ViewStub viewStub = (ViewStub) requireViewById(R$id.stub);
        viewStub.setLayoutResource(R$layout.controls_management_favorites);
        viewStub.inflate();
        View requireViewById2 = requireViewById(R$id.status_message);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById(R.id.status_message)");
        this.statusText = (TextView) requireViewById2;
        if (shouldShowTooltip()) {
            TextView textView = this.statusText;
            if (textView != null) {
                Context context = textView.getContext();
                Intrinsics.checkNotNullExpressionValue(context, "statusText.context");
                TooltipManager tooltipManager = new TooltipManager(context, "ControlsStructureSwipeTooltipCount", 2, false, 8, null);
                this.mTooltipManager = tooltipManager;
                addContentView(tooltipManager.getLayout(), new FrameLayout.LayoutParams(-2, -2, 51));
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("statusText");
                throw null;
            }
        }
        View requireViewById3 = requireViewById(R$id.structure_page_indicator);
        ManagementPageIndicator managementPageIndicator = (ManagementPageIndicator) requireViewById3;
        managementPageIndicator.setVisibilityListener(new Function1<Integer, Unit>(this) { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$bindViews$2$1
            final /* synthetic */ ControlsFavoritingActivity this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Integer num) {
                invoke(num.intValue());
                return Unit.INSTANCE;
            }

            public final void invoke(int i) {
                TooltipManager tooltipManager2;
                if (i != 0 && (tooltipManager2 = this.this$0.mTooltipManager) != null) {
                    tooltipManager2.hide(true);
                }
            }
        });
        Unit unit = Unit.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "requireViewById<ManagementPageIndicator>(\n            R.id.structure_page_indicator).apply {\n            visibilityListener = {\n                if (it != View.VISIBLE) {\n                    mTooltipManager?.hide(true)\n                }\n            }\n        }");
        this.pageIndicator = managementPageIndicator;
        CharSequence charSequence = this.structureExtra;
        if (charSequence == null && (charSequence = this.appName) == null) {
            charSequence = getResources().getText(R$string.controls_favorite_default_title);
        }
        View requireViewById4 = requireViewById(R$id.title);
        TextView textView2 = (TextView) requireViewById4;
        textView2.setText(charSequence);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "requireViewById<TextView>(R.id.title).apply {\n            text = title\n        }");
        this.titleView = textView2;
        View requireViewById5 = requireViewById(R$id.subtitle);
        TextView textView3 = (TextView) requireViewById5;
        textView3.setText(textView3.getResources().getText(R$string.controls_favorite_subtitle));
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "requireViewById<TextView>(R.id.subtitle).apply {\n            text = resources.getText(R.string.controls_favorite_subtitle)\n        }");
        this.subtitleView = textView3;
        View requireViewById6 = requireViewById(R$id.structure_pager);
        Intrinsics.checkNotNullExpressionValue(requireViewById6, "requireViewById<ViewPager2>(R.id.structure_pager)");
        ViewPager2 viewPager2 = (ViewPager2) requireViewById6;
        this.structurePager = viewPager2;
        if (viewPager2 != null) {
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(this) { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$bindViews$5
                final /* synthetic */ ControlsFavoritingActivity this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
                public void onPageSelected(int i) {
                    super.onPageSelected(i);
                    TooltipManager tooltipManager2 = this.this$0.mTooltipManager;
                    if (tooltipManager2 != null) {
                        tooltipManager2.hide(true);
                    }
                }
            });
            bindButtons();
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("structurePager");
        throw null;
    }

    /* access modifiers changed from: private */
    public final void animateExitAndFinish() {
        ViewGroup viewGroup = (ViewGroup) requireViewById(R$id.controls_management_root);
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(viewGroup, "rootView");
        ControlsAnimations.exitAnimation(viewGroup, new Runnable(this) { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$animateExitAndFinish$1
            final /* synthetic */ ControlsFavoritingActivity this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // java.lang.Runnable
            public void run() {
                this.this$0.finish();
            }
        }).start();
    }

    private final void bindButtons() {
        View requireViewById = requireViewById(R$id.other_apps);
        Button button = (Button) requireViewById;
        button.setOnClickListener(new View.OnClickListener(this, button) { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$bindButtons$1$1
            final /* synthetic */ Button $this_apply;
            final /* synthetic */ ControlsFavoritingActivity this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$this_apply = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                View view2 = this.this$0.doneButton;
                if (view2 != null) {
                    if (view2.isEnabled()) {
                        Toast.makeText(this.this$0.getApplicationContext(), R$string.controls_favorite_toast_no_changes, 0).show();
                    }
                    this.this$0.startActivity(new Intent(this.$this_apply.getContext(), ControlsProviderSelectorActivity.class), ActivityOptions.makeSceneTransitionAnimation(this.this$0, new Pair[0]).toBundle());
                    this.this$0.animateExitAndFinish();
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException("doneButton");
                throw null;
            }
        });
        Unit unit = Unit.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<Button>(R.id.other_apps).apply {\n            setOnClickListener {\n                if (doneButton.isEnabled) {\n                    // The user has made changes\n                    Toast.makeText(\n                            applicationContext,\n                            R.string.controls_favorite_toast_no_changes,\n                            Toast.LENGTH_SHORT\n                            ).show()\n                }\n                startActivity(\n                    Intent(context, ControlsProviderSelectorActivity::class.java),\n                    ActivityOptions\n                        .makeSceneTransitionAnimation(this@ControlsFavoritingActivity).toBundle()\n                )\n                animateExitAndFinish()\n            }\n        }");
        this.otherAppsButton = requireViewById;
        View requireViewById2 = requireViewById(R$id.done);
        Button button2 = (Button) requireViewById2;
        button2.setEnabled(false);
        button2.setOnClickListener(new View.OnClickListener(this) { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$bindButtons$2$1
            final /* synthetic */ ControlsFavoritingActivity this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                if (this.this$0.component != null) {
                    List<StructureContainer> list = this.this$0.listOfStructures;
                    ControlsFavoritingActivity controlsFavoritingActivity = this.this$0;
                    for (StructureContainer structureContainer : list) {
                        List<ControlInfo> favorites = structureContainer.getModel().getFavorites();
                        ControlsControllerImpl controlsControllerImpl = controlsFavoritingActivity.controller;
                        ComponentName componentName = controlsFavoritingActivity.component;
                        Intrinsics.checkNotNull(componentName);
                        controlsControllerImpl.replaceFavoritesForStructure(new StructureInfo(componentName, structureContainer.getStructureName(), favorites));
                    }
                    this.this$0.animateExitAndFinish();
                    this.this$0.openControlsOrigin();
                }
            }
        });
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById<Button>(R.id.done).apply {\n            isEnabled = false\n            setOnClickListener {\n                if (component == null) return@setOnClickListener\n                listOfStructures.forEach {\n                    val favoritesForStorage = it.model.favorites\n                    controller.replaceFavoritesForStructure(\n                        StructureInfo(component!!, it.structureName, favoritesForStorage)\n                    )\n                }\n                animateExitAndFinish()\n                openControlsOrigin()\n            }\n        }");
        this.doneButton = requireViewById2;
    }

    /* access modifiers changed from: private */
    public final void openControlsOrigin() {
        startActivity(new Intent(getApplicationContext(), ControlsActivity.class), ActivityOptions.makeSceneTransitionAnimation(this, new Pair[0]).toBundle());
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        TooltipManager tooltipManager = this.mTooltipManager;
        if (tooltipManager != null) {
            tooltipManager.hide(false);
        }
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        this.listingController.addCallback(this.listingCallback);
        this.currentUserTracker.startTracking();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (!this.isPagerLoaded) {
            setUpPager();
            loadControls();
            this.isPagerLoaded = true;
        }
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        this.listingController.removeCallback(this.listingCallback);
        this.currentUserTracker.stopTracking();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        Intrinsics.checkNotNullParameter(configuration, "newConfig");
        super.onConfigurationChanged(configuration);
        TooltipManager tooltipManager = this.mTooltipManager;
        if (tooltipManager != null) {
            tooltipManager.hide(false);
        }
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    protected void onDestroy() {
        Runnable runnable = this.cancelLoadRunnable;
        if (runnable != null) {
            runnable.run();
        }
        super.onDestroy();
    }

    private final boolean shouldShowTooltip() {
        return Prefs.getInt(getApplicationContext(), "ControlsStructureSwipeTooltipCount", 0) < 2;
    }
}
