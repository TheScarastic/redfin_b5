package com.android.systemui.controls.management;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.util.LifecycleActivity;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsProviderSelectorActivity.kt */
/* loaded from: classes.dex */
public final class ControlsProviderSelectorActivity extends LifecycleActivity {
    public static final Companion Companion = new Companion(null);
    private final Executor backExecutor;
    private boolean backShouldExit;
    private final BroadcastDispatcher broadcastDispatcher;
    private final ControlsController controlsController;
    private final ControlsProviderSelectorActivity$currentUserTracker$1 currentUserTracker;
    private final Executor executor;
    private final ControlsListingController listingController;
    private RecyclerView recyclerView;
    private final ControlsUiController uiController;

    public ControlsProviderSelectorActivity(Executor executor, Executor executor2, ControlsListingController controlsListingController, ControlsController controlsController, BroadcastDispatcher broadcastDispatcher, ControlsUiController controlsUiController) {
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(executor2, "backExecutor");
        Intrinsics.checkNotNullParameter(controlsListingController, "listingController");
        Intrinsics.checkNotNullParameter(controlsController, "controlsController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(controlsUiController, "uiController");
        this.executor = executor;
        this.backExecutor = executor2;
        this.listingController = controlsListingController;
        this.controlsController = controlsController;
        this.broadcastDispatcher = broadcastDispatcher;
        this.uiController = controlsUiController;
        this.currentUserTracker = new ControlsProviderSelectorActivity$currentUserTracker$1(this, broadcastDispatcher);
    }

    /* compiled from: ControlsProviderSelectorActivity.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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
        viewStub.setLayoutResource(R$layout.controls_management_apps);
        viewStub.inflate();
        View requireViewById2 = requireViewById(R$id.list);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById(R.id.list)");
        RecyclerView recyclerView = (RecyclerView) requireViewById2;
        this.recyclerView = recyclerView;
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            TextView textView = (TextView) requireViewById(R$id.title);
            textView.setText(textView.getResources().getText(R$string.controls_providers_title));
            Button button = (Button) requireViewById(R$id.other_apps);
            button.setVisibility(0);
            button.setText(17039360);
            button.setOnClickListener(new View.OnClickListener(this) { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$onCreate$3$1
                final /* synthetic */ ControlsProviderSelectorActivity this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    this.this$0.onBackPressed();
                }
            });
            requireViewById(R$id.done).setVisibility(8);
            this.backShouldExit = getIntent().getBooleanExtra("back_should_exit", false);
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
        throw null;
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (!this.backShouldExit) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(getApplicationContext(), ControlsActivity.class));
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, new Pair[0]).toBundle());
        }
        animateExitAndFinish();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        this.currentUserTracker.startTracking();
        RecyclerView recyclerView = this.recyclerView;
        if (recyclerView != null) {
            recyclerView.setAlpha(0.0f);
            RecyclerView recyclerView2 = this.recyclerView;
            if (recyclerView2 != null) {
                Executor executor = this.backExecutor;
                Executor executor2 = this.executor;
                Lifecycle lifecycle = getLifecycle();
                ControlsListingController controlsListingController = this.listingController;
                LayoutInflater from = LayoutInflater.from(this);
                Intrinsics.checkNotNullExpressionValue(from, "from(this)");
                ControlsProviderSelectorActivity$onStart$1 controlsProviderSelectorActivity$onStart$1 = new Function1<ComponentName, Unit>(this) { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$onStart$1
                    /* Return type fixed from 'java.lang.Object' to match base method */
                    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                    @Override // kotlin.jvm.functions.Function1
                    public /* bridge */ /* synthetic */ Unit invoke(ComponentName componentName) {
                        invoke(componentName);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(ComponentName componentName) {
                        ((ControlsProviderSelectorActivity) this.receiver).launchFavoritingActivity(componentName);
                    }
                };
                Resources resources = getResources();
                Intrinsics.checkNotNullExpressionValue(resources, "resources");
                FavoritesRenderer favoritesRenderer = new FavoritesRenderer(resources, new Function1<ComponentName, Integer>(this.controlsController) { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$onStart$2
                    public final int invoke(ComponentName componentName) {
                        Intrinsics.checkNotNullParameter(componentName, "p0");
                        return ((ControlsController) this.receiver).countFavoritesForComponent(componentName);
                    }

                    /* Return type fixed from 'java.lang.Object' to match base method */
                    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                    @Override // kotlin.jvm.functions.Function1
                    public /* bridge */ /* synthetic */ Integer invoke(ComponentName componentName) {
                        return Integer.valueOf(invoke(componentName));
                    }
                });
                Resources resources2 = getResources();
                Intrinsics.checkNotNullExpressionValue(resources2, "resources");
                AppAdapter appAdapter = new AppAdapter(executor, executor2, lifecycle, controlsListingController, from, controlsProviderSelectorActivity$onStart$1, favoritesRenderer, resources2);
                appAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(this) { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$onStart$3$1
                    private boolean hasAnimated;
                    final /* synthetic */ ControlsProviderSelectorActivity this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.this$0 = r1;
                    }

                    @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
                    public void onChanged() {
                        if (!this.hasAnimated) {
                            this.hasAnimated = true;
                            ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
                            RecyclerView access$getRecyclerView$p = ControlsProviderSelectorActivity.access$getRecyclerView$p(this.this$0);
                            if (access$getRecyclerView$p != null) {
                                controlsAnimations.enterAnimation(access$getRecyclerView$p).start();
                            } else {
                                Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
                                throw null;
                            }
                        }
                    }
                });
                Unit unit = Unit.INSTANCE;
                recyclerView2.setAdapter(appAdapter);
                return;
            }
            Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
        throw null;
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        this.currentUserTracker.stopTracking();
    }

    public final void launchFavoritingActivity(ComponentName componentName) {
        this.executor.execute(new Runnable(componentName, this) { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$launchFavoritingActivity$1
            final /* synthetic */ ComponentName $component;
            final /* synthetic */ ControlsProviderSelectorActivity this$0;

            /* access modifiers changed from: package-private */
            {
                this.$component = r1;
                this.this$0 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ComponentName componentName2 = this.$component;
                if (componentName2 != null) {
                    ControlsProviderSelectorActivity controlsProviderSelectorActivity = this.this$0;
                    Intent intent = new Intent(controlsProviderSelectorActivity.getApplicationContext(), ControlsFavoritingActivity.class);
                    intent.putExtra("extra_app_label", ControlsProviderSelectorActivity.access$getListingController$p(controlsProviderSelectorActivity).getAppLabel(componentName2));
                    intent.putExtra("android.intent.extra.COMPONENT_NAME", componentName2);
                    intent.putExtra("extra_from_provider_selector", true);
                    controlsProviderSelectorActivity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(controlsProviderSelectorActivity, new Pair[0]).toBundle());
                    ControlsProviderSelectorActivity.access$animateExitAndFinish(controlsProviderSelectorActivity);
                }
            }
        });
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onDestroy() {
        this.currentUserTracker.stopTracking();
        super.onDestroy();
    }

    public final void animateExitAndFinish() {
        ViewGroup viewGroup = (ViewGroup) requireViewById(R$id.controls_management_root);
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(viewGroup, "rootView");
        ControlsAnimations.exitAnimation(viewGroup, new Runnable(this) { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$animateExitAndFinish$1
            final /* synthetic */ ControlsProviderSelectorActivity this$0;

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
}
