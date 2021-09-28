package com.android.systemui.controls.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.controls.management.ControlsAnimations;
import com.android.systemui.util.LifecycleActivity;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsActivity.kt */
/* loaded from: classes.dex */
public final class ControlsActivity extends LifecycleActivity {
    private ViewGroup parent;
    private final ControlsUiController uiController;

    public ControlsActivity(ControlsUiController controlsUiController) {
        Intrinsics.checkNotNullParameter(controlsUiController, "uiController");
        this.uiController = controlsUiController;
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.controls_fullscreen);
        Lifecycle lifecycle = getLifecycle();
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        int i = R$id.control_detail_root;
        View requireViewById = requireViewById(i);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGroup>(R.id.control_detail_root)");
        Window window = getWindow();
        Intrinsics.checkNotNullExpressionValue(window, "window");
        Intent intent = getIntent();
        Intrinsics.checkNotNullExpressionValue(intent, "intent");
        lifecycle.addObserver(controlsAnimations.observerForAnimations((ViewGroup) requireViewById, window, intent));
        ((ViewGroup) requireViewById(i)).setOnApplyWindowInsetsListener(ControlsActivity$onCreate$1$1.INSTANCE);
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        View requireViewById = requireViewById(R$id.global_actions_controls);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGroup>(R.id.global_actions_controls)");
        ViewGroup viewGroup = (ViewGroup) requireViewById;
        this.parent = viewGroup;
        if (viewGroup != null) {
            viewGroup.setAlpha(0.0f);
            ControlsUiController controlsUiController = this.uiController;
            ViewGroup viewGroup2 = this.parent;
            if (viewGroup2 != null) {
                controlsUiController.show(viewGroup2, new Runnable(this) { // from class: com.android.systemui.controls.ui.ControlsActivity$onResume$1
                    final /* synthetic */ ControlsActivity this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.this$0 = r1;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        this.this$0.finish();
                    }
                }, this);
                ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
                ViewGroup viewGroup3 = this.parent;
                if (viewGroup3 != null) {
                    controlsAnimations.enterAnimation(viewGroup3).start();
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("parent");
                    throw null;
                }
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                throw null;
            }
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        finish();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.uiController.hide();
    }
}
