package com.android.systemui.controls.controller;

import android.os.IBinder;
import android.service.controls.Control;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.util.Log;
import com.android.systemui.util.concurrency.DelayableExecutor;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StatefulControlSubscriber.kt */
/* loaded from: classes.dex */
public final class StatefulControlSubscriber extends IControlsSubscriber.Stub {
    public static final Companion Companion = new Companion(null);
    private final DelayableExecutor bgExecutor;
    private final ControlsController controller;
    private final ControlsProviderLifecycleManager provider;
    private final long requestLimit;
    private IControlsSubscription subscription;
    private boolean subscriptionOpen;

    public StatefulControlSubscriber(ControlsController controlsController, ControlsProviderLifecycleManager controlsProviderLifecycleManager, DelayableExecutor delayableExecutor, long j) {
        Intrinsics.checkNotNullParameter(controlsController, "controller");
        Intrinsics.checkNotNullParameter(controlsProviderLifecycleManager, "provider");
        Intrinsics.checkNotNullParameter(delayableExecutor, "bgExecutor");
        this.controller = controlsController;
        this.provider = controlsProviderLifecycleManager;
        this.bgExecutor = delayableExecutor;
        this.requestLimit = j;
    }

    /* compiled from: StatefulControlSubscriber.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final void run(IBinder iBinder, Function0<Unit> function0) {
        if (Intrinsics.areEqual(this.provider.getToken(), iBinder)) {
            this.bgExecutor.execute(new Runnable(function0) { // from class: com.android.systemui.controls.controller.StatefulControlSubscriber$run$1
                final /* synthetic */ Function0<Unit> $f;

                /* access modifiers changed from: package-private */
                {
                    this.$f = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    this.$f.invoke();
                }
            });
        }
    }

    public void onSubscribe(IBinder iBinder, IControlsSubscription iControlsSubscription) {
        Intrinsics.checkNotNullParameter(iBinder, "token");
        Intrinsics.checkNotNullParameter(iControlsSubscription, "subs");
        run(iBinder, new Function0<Unit>(this, iControlsSubscription) { // from class: com.android.systemui.controls.controller.StatefulControlSubscriber$onSubscribe$1
            final /* synthetic */ IControlsSubscription $subs;
            final /* synthetic */ StatefulControlSubscriber this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$subs = r2;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                this.this$0.subscriptionOpen = true;
                this.this$0.subscription = this.$subs;
                this.this$0.provider.startSubscription(this.$subs, this.this$0.requestLimit);
            }
        });
    }

    public void onNext(IBinder iBinder, Control control) {
        Intrinsics.checkNotNullParameter(iBinder, "token");
        Intrinsics.checkNotNullParameter(control, "control");
        run(iBinder, new Function0<Unit>(this, iBinder, control) { // from class: com.android.systemui.controls.controller.StatefulControlSubscriber$onNext$1
            final /* synthetic */ Control $control;
            final /* synthetic */ IBinder $token;
            final /* synthetic */ StatefulControlSubscriber this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$token = r2;
                this.$control = r3;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                if (!(this.this$0.subscriptionOpen)) {
                    Log.w("StatefulControlSubscriber", Intrinsics.stringPlus("Refresh outside of window for token:", this.$token));
                } else {
                    this.this$0.controller.refreshStatus(this.this$0.provider.getComponentName(), this.$control);
                }
            }
        });
    }

    public void onError(IBinder iBinder, String str) {
        Intrinsics.checkNotNullParameter(iBinder, "token");
        Intrinsics.checkNotNullParameter(str, "error");
        run(iBinder, new Function0<Unit>(this, str) { // from class: com.android.systemui.controls.controller.StatefulControlSubscriber$onError$1
            final /* synthetic */ String $error;
            final /* synthetic */ StatefulControlSubscriber this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$error = r2;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                if (this.this$0.subscriptionOpen) {
                    this.this$0.subscriptionOpen = false;
                    Log.e("StatefulControlSubscriber", "onError receive from '" + this.this$0.provider.getComponentName() + "': " + this.$error);
                }
            }
        });
    }

    public void onComplete(IBinder iBinder) {
        Intrinsics.checkNotNullParameter(iBinder, "token");
        run(iBinder, new Function0<Unit>(this) { // from class: com.android.systemui.controls.controller.StatefulControlSubscriber$onComplete$1
            final /* synthetic */ StatefulControlSubscriber this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                if (this.this$0.subscriptionOpen) {
                    this.this$0.subscriptionOpen = false;
                    Log.i("StatefulControlSubscriber", "onComplete receive from '" + this.this$0.provider.getComponentName() + '\'');
                }
            }
        });
    }

    public final void cancel() {
        if (this.subscriptionOpen) {
            this.bgExecutor.execute(new Runnable(this) { // from class: com.android.systemui.controls.controller.StatefulControlSubscriber$cancel$1
                final /* synthetic */ StatefulControlSubscriber this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    if (this.this$0.subscriptionOpen) {
                        this.this$0.subscriptionOpen = false;
                        IControlsSubscription iControlsSubscription = this.this$0.subscription;
                        if (iControlsSubscription != null) {
                            this.this$0.provider.cancelSubscription(iControlsSubscription);
                        }
                        this.this$0.subscription = null;
                    }
                }
            });
        }
    }
}
