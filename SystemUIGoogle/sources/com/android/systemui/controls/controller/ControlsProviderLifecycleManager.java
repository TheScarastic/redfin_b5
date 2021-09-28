package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.UserHandle;
import android.service.controls.IControlsActionCallback;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.service.controls.actions.ControlAction;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsProviderLifecycleManager.kt */
/* loaded from: classes.dex */
public final class ControlsProviderLifecycleManager implements IBinder.DeathRecipient {
    private final IControlsActionCallback.Stub actionCallbackService;
    private int bindTryCount;
    private final ComponentName componentName;
    private final Context context;
    private final DelayableExecutor executor;
    private final Intent intent;
    private Runnable onLoadCanceller;
    private boolean requiresBound;
    private final UserHandle user;
    private ServiceWrapper wrapper;
    public static final Companion Companion = new Companion(null);
    private static final int BIND_FLAGS = 67109121;
    private final IBinder token = new Binder();
    @GuardedBy({"queuedServiceMethods"})
    private final Set<ServiceMethod> queuedServiceMethods = new ArraySet();
    private final String TAG = ControlsProviderLifecycleManager.class.getSimpleName();
    private final ControlsProviderLifecycleManager$serviceConnection$1 serviceConnection = new ControlsProviderLifecycleManager$serviceConnection$1(this);

    public ControlsProviderLifecycleManager(Context context, DelayableExecutor delayableExecutor, IControlsActionCallback.Stub stub, UserHandle userHandle, ComponentName componentName) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(delayableExecutor, "executor");
        Intrinsics.checkNotNullParameter(stub, "actionCallbackService");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        this.context = context;
        this.executor = delayableExecutor;
        this.actionCallbackService = stub;
        this.user = userHandle;
        this.componentName = componentName;
        Intent intent = new Intent();
        intent.setComponent(getComponentName());
        Bundle bundle = new Bundle();
        bundle.putBinder("CALLBACK_TOKEN", getToken());
        Unit unit = Unit.INSTANCE;
        intent.putExtra("CALLBACK_BUNDLE", bundle);
        this.intent = intent;
    }

    public final UserHandle getUser() {
        return this.user;
    }

    public final ComponentName getComponentName() {
        return this.componentName;
    }

    public final IBinder getToken() {
        return this.token;
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* access modifiers changed from: private */
    public final void bindService(boolean z) {
        this.executor.execute(new Runnable(this, z) { // from class: com.android.systemui.controls.controller.ControlsProviderLifecycleManager$bindService$1
            final /* synthetic */ boolean $bind;
            final /* synthetic */ ControlsProviderLifecycleManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$bind = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.requiresBound = this.$bind;
                if (!this.$bind) {
                    Log.d(this.this$0.TAG, Intrinsics.stringPlus("Unbinding service ", this.this$0.intent));
                    this.this$0.bindTryCount = 0;
                    if (this.this$0.wrapper != null) {
                        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.this$0;
                        controlsProviderLifecycleManager.context.unbindService(controlsProviderLifecycleManager.serviceConnection);
                    }
                    this.this$0.wrapper = null;
                } else if (this.this$0.bindTryCount != 5) {
                    Log.d(this.this$0.TAG, Intrinsics.stringPlus("Binding service ", this.this$0.intent));
                    ControlsProviderLifecycleManager controlsProviderLifecycleManager2 = this.this$0;
                    controlsProviderLifecycleManager2.bindTryCount = controlsProviderLifecycleManager2.bindTryCount + 1;
                    try {
                        this.this$0.context.bindServiceAsUser(this.this$0.intent, this.this$0.serviceConnection, ControlsProviderLifecycleManager.BIND_FLAGS, this.this$0.getUser());
                    } catch (SecurityException e) {
                        Log.e(this.this$0.TAG, "Failed to bind to service", e);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public final void handlePendingServiceMethods() {
        ArraySet<ServiceMethod> arraySet;
        synchronized (this.queuedServiceMethods) {
            arraySet = new ArraySet(this.queuedServiceMethods);
            this.queuedServiceMethods.clear();
        }
        for (ServiceMethod serviceMethod : arraySet) {
            serviceMethod.run();
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        if (this.wrapper != null) {
            this.wrapper = null;
            if (this.requiresBound) {
                Log.d(this.TAG, "binderDied");
            }
        }
    }

    /* access modifiers changed from: private */
    public final void queueServiceMethod(ServiceMethod serviceMethod) {
        synchronized (this.queuedServiceMethods) {
            this.queuedServiceMethods.add(serviceMethod);
        }
    }

    private final void invokeOrQueue(ServiceMethod serviceMethod) {
        Unit unit;
        if (this.wrapper == null) {
            unit = null;
        } else {
            serviceMethod.run();
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            queueServiceMethod(serviceMethod);
            bindService(true);
        }
    }

    public final void maybeBindAndLoad(IControlsSubscriber.Stub stub) {
        Intrinsics.checkNotNullParameter(stub, "subscriber");
        this.onLoadCanceller = this.executor.executeDelayed(new Runnable(this, stub) { // from class: com.android.systemui.controls.controller.ControlsProviderLifecycleManager$maybeBindAndLoad$1
            final /* synthetic */ IControlsSubscriber.Stub $subscriber;
            final /* synthetic */ ControlsProviderLifecycleManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$subscriber = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Log.d(this.this$0.TAG, Intrinsics.stringPlus("Timeout waiting onLoad for ", this.this$0.getComponentName()));
                this.$subscriber.onError(this.this$0.getToken(), "Timeout waiting onLoad");
                this.this$0.unbindService();
            }
        }, 20, TimeUnit.SECONDS);
        invokeOrQueue(new Load(this, stub));
    }

    public final void maybeBindAndLoadSuggested(IControlsSubscriber.Stub stub) {
        Intrinsics.checkNotNullParameter(stub, "subscriber");
        this.onLoadCanceller = this.executor.executeDelayed(new Runnable(this, stub) { // from class: com.android.systemui.controls.controller.ControlsProviderLifecycleManager$maybeBindAndLoadSuggested$1
            final /* synthetic */ IControlsSubscriber.Stub $subscriber;
            final /* synthetic */ ControlsProviderLifecycleManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$subscriber = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Log.d(this.this$0.TAG, Intrinsics.stringPlus("Timeout waiting onLoadSuggested for ", this.this$0.getComponentName()));
                this.$subscriber.onError(this.this$0.getToken(), "Timeout waiting onLoadSuggested");
                this.this$0.unbindService();
            }
        }, 20, TimeUnit.SECONDS);
        invokeOrQueue(new Suggest(this, stub));
    }

    public final void cancelLoadTimeout() {
        Runnable runnable = this.onLoadCanceller;
        if (runnable != null) {
            runnable.run();
        }
        this.onLoadCanceller = null;
    }

    public final void maybeBindAndSubscribe(List<String> list, IControlsSubscriber iControlsSubscriber) {
        Intrinsics.checkNotNullParameter(list, "controlIds");
        Intrinsics.checkNotNullParameter(iControlsSubscriber, "subscriber");
        invokeOrQueue(new Subscribe(this, list, iControlsSubscriber));
    }

    public final void maybeBindAndSendAction(String str, ControlAction controlAction) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        Intrinsics.checkNotNullParameter(controlAction, "action");
        invokeOrQueue(new Action(this, str, controlAction));
    }

    public final void startSubscription(IControlsSubscription iControlsSubscription, long j) {
        Intrinsics.checkNotNullParameter(iControlsSubscription, "subscription");
        Log.d(this.TAG, Intrinsics.stringPlus("startSubscription: ", iControlsSubscription));
        ServiceWrapper serviceWrapper = this.wrapper;
        if (serviceWrapper != null) {
            serviceWrapper.request(iControlsSubscription, j);
        }
    }

    public final void cancelSubscription(IControlsSubscription iControlsSubscription) {
        Intrinsics.checkNotNullParameter(iControlsSubscription, "subscription");
        Log.d(this.TAG, Intrinsics.stringPlus("cancelSubscription: ", iControlsSubscription));
        ServiceWrapper serviceWrapper = this.wrapper;
        if (serviceWrapper != null) {
            serviceWrapper.cancel(iControlsSubscription);
        }
    }

    public final void unbindService() {
        Runnable runnable = this.onLoadCanceller;
        if (runnable != null) {
            runnable.run();
        }
        this.onLoadCanceller = null;
        bindService(false);
    }

    @Override // java.lang.Object
    public String toString() {
        String str = "ControlsProviderLifecycleManager(" + Intrinsics.stringPlus("component=", getComponentName()) + Intrinsics.stringPlus(", user=", getUser()) + ")";
        Intrinsics.checkNotNullExpressionValue(str, "StringBuilder(\"ControlsProviderLifecycleManager(\").apply {\n            append(\"component=$componentName\")\n            append(\", user=$user\")\n            append(\")\")\n        }.toString()");
        return str;
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public abstract class ServiceMethod {
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        public abstract boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core();

        /* JADX WARN: Incorrect args count in method signature: ()V */
        public ServiceMethod(ControlsProviderLifecycleManager controlsProviderLifecycleManager) {
            Intrinsics.checkNotNullParameter(controlsProviderLifecycleManager, "this$0");
            this.this$0 = controlsProviderLifecycleManager;
        }

        public final void run() {
            if (!callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
                this.this$0.queueServiceMethod(this);
                this.this$0.binderDied();
            }
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public final class Load extends ServiceMethod {
        private final IControlsSubscriber.Stub subscriber;
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public Load(ControlsProviderLifecycleManager controlsProviderLifecycleManager, IControlsSubscriber.Stub stub) {
            super(controlsProviderLifecycleManager);
            Intrinsics.checkNotNullParameter(controlsProviderLifecycleManager, "this$0");
            Intrinsics.checkNotNullParameter(stub, "subscriber");
            this.this$0 = controlsProviderLifecycleManager;
            this.subscriber = stub;
        }

        @Override // com.android.systemui.controls.controller.ControlsProviderLifecycleManager.ServiceMethod
        public boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            Log.d(this.this$0.TAG, Intrinsics.stringPlus("load ", this.this$0.getComponentName()));
            ServiceWrapper serviceWrapper = this.this$0.wrapper;
            if (serviceWrapper == null) {
                return false;
            }
            return serviceWrapper.load(this.subscriber);
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public final class Suggest extends ServiceMethod {
        private final IControlsSubscriber.Stub subscriber;
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public Suggest(ControlsProviderLifecycleManager controlsProviderLifecycleManager, IControlsSubscriber.Stub stub) {
            super(controlsProviderLifecycleManager);
            Intrinsics.checkNotNullParameter(controlsProviderLifecycleManager, "this$0");
            Intrinsics.checkNotNullParameter(stub, "subscriber");
            this.this$0 = controlsProviderLifecycleManager;
            this.subscriber = stub;
        }

        @Override // com.android.systemui.controls.controller.ControlsProviderLifecycleManager.ServiceMethod
        public boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            Log.d(this.this$0.TAG, Intrinsics.stringPlus("suggest ", this.this$0.getComponentName()));
            ServiceWrapper serviceWrapper = this.this$0.wrapper;
            if (serviceWrapper == null) {
                return false;
            }
            return serviceWrapper.loadSuggested(this.subscriber);
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public final class Subscribe extends ServiceMethod {
        private final List<String> list;
        private final IControlsSubscriber subscriber;
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public Subscribe(ControlsProviderLifecycleManager controlsProviderLifecycleManager, List<String> list, IControlsSubscriber iControlsSubscriber) {
            super(controlsProviderLifecycleManager);
            Intrinsics.checkNotNullParameter(controlsProviderLifecycleManager, "this$0");
            Intrinsics.checkNotNullParameter(list, "list");
            Intrinsics.checkNotNullParameter(iControlsSubscriber, "subscriber");
            this.this$0 = controlsProviderLifecycleManager;
            this.list = list;
            this.subscriber = iControlsSubscriber;
        }

        @Override // com.android.systemui.controls.controller.ControlsProviderLifecycleManager.ServiceMethod
        public boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            String str = this.this$0.TAG;
            Log.d(str, "subscribe " + this.this$0.getComponentName() + " - " + this.list);
            ServiceWrapper serviceWrapper = this.this$0.wrapper;
            if (serviceWrapper == null) {
                return false;
            }
            return serviceWrapper.subscribe(this.list, this.subscriber);
        }
    }

    /* compiled from: ControlsProviderLifecycleManager.kt */
    /* loaded from: classes.dex */
    public final class Action extends ServiceMethod {
        private final ControlAction action;
        private final String id;
        final /* synthetic */ ControlsProviderLifecycleManager this$0;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public Action(ControlsProviderLifecycleManager controlsProviderLifecycleManager, String str, ControlAction controlAction) {
            super(controlsProviderLifecycleManager);
            Intrinsics.checkNotNullParameter(controlsProviderLifecycleManager, "this$0");
            Intrinsics.checkNotNullParameter(str, "id");
            Intrinsics.checkNotNullParameter(controlAction, "action");
            this.this$0 = controlsProviderLifecycleManager;
            this.id = str;
            this.action = controlAction;
        }

        @Override // com.android.systemui.controls.controller.ControlsProviderLifecycleManager.ServiceMethod
        public boolean callWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            String str = this.this$0.TAG;
            Log.d(str, "onAction " + this.this$0.getComponentName() + " - " + this.id);
            ServiceWrapper serviceWrapper = this.this$0.wrapper;
            if (serviceWrapper == null) {
                return false;
            }
            return serviceWrapper.action(this.id, this.action, this.this$0.actionCallbackService);
        }
    }
}
