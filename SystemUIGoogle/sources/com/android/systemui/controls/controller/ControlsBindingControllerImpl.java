package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.os.UserHandle;
import android.service.controls.Control;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.service.controls.actions.ControlAction;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.controls.controller.ControlsBindingController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsBindingControllerImpl.kt */
@VisibleForTesting
/* loaded from: classes.dex */
public class ControlsBindingControllerImpl implements ControlsBindingController {
    public static final Companion Companion = new Companion(null);
    private static final ControlsBindingControllerImpl$Companion$emptyCallback$1 emptyCallback = new ControlsBindingControllerImpl$Companion$emptyCallback$1();
    private final ControlsBindingControllerImpl$actionCallbackService$1 actionCallbackService = new ControlsBindingControllerImpl$actionCallbackService$1(this);
    private final DelayableExecutor backgroundExecutor;
    private final Context context;
    private ControlsProviderLifecycleManager currentProvider;
    private UserHandle currentUser;
    private final Lazy<ControlsController> lazyController;
    private LoadSubscriber loadSubscriber;
    private StatefulControlSubscriber statefulControlSubscriber;

    public ControlsBindingControllerImpl(Context context, DelayableExecutor delayableExecutor, Lazy<ControlsController> lazy, UserTracker userTracker) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(delayableExecutor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(lazy, "lazyController");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        this.context = context;
        this.backgroundExecutor = delayableExecutor;
        this.lazyController = lazy;
        this.currentUser = userTracker.getUserHandle();
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @VisibleForTesting
    public ControlsProviderLifecycleManager createProviderManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "component");
        return new ControlsProviderLifecycleManager(this.context, this.backgroundExecutor, this.actionCallbackService, this.currentUser, componentName);
    }

    private final ControlsProviderLifecycleManager retrieveLifecycleManager(ComponentName componentName) {
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.currentProvider;
        if (controlsProviderLifecycleManager != null) {
            if (!Intrinsics.areEqual(controlsProviderLifecycleManager == null ? null : controlsProviderLifecycleManager.getComponentName(), componentName)) {
                unbind();
            }
        }
        ControlsProviderLifecycleManager controlsProviderLifecycleManager2 = this.currentProvider;
        if (controlsProviderLifecycleManager2 == null) {
            controlsProviderLifecycleManager2 = createProviderManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core(componentName);
        }
        this.currentProvider = controlsProviderLifecycleManager2;
        return controlsProviderLifecycleManager2;
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public Runnable bindAndLoad(ComponentName componentName, ControlsBindingController.LoadCallback loadCallback) {
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(loadCallback, "callback");
        LoadSubscriber loadSubscriber = this.loadSubscriber;
        if (loadSubscriber != null) {
            loadSubscriber.loadCancel();
        }
        LoadSubscriber loadSubscriber2 = new LoadSubscriber(this, loadCallback, 100000);
        this.loadSubscriber = loadSubscriber2;
        retrieveLifecycleManager(componentName).maybeBindAndLoad(loadSubscriber2);
        return loadSubscriber2.loadCancel();
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public void bindAndLoadSuggested(ComponentName componentName, ControlsBindingController.LoadCallback loadCallback) {
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(loadCallback, "callback");
        LoadSubscriber loadSubscriber = this.loadSubscriber;
        if (loadSubscriber != null) {
            loadSubscriber.loadCancel();
        }
        LoadSubscriber loadSubscriber2 = new LoadSubscriber(this, loadCallback, 36);
        this.loadSubscriber = loadSubscriber2;
        retrieveLifecycleManager(componentName).maybeBindAndLoadSuggested(loadSubscriber2);
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public void subscribe(StructureInfo structureInfo) {
        Intrinsics.checkNotNullParameter(structureInfo, "structureInfo");
        unsubscribe();
        ControlsProviderLifecycleManager retrieveLifecycleManager = retrieveLifecycleManager(structureInfo.getComponentName());
        ControlsController controlsController = this.lazyController.get();
        Intrinsics.checkNotNullExpressionValue(controlsController, "lazyController.get()");
        StatefulControlSubscriber statefulControlSubscriber = new StatefulControlSubscriber(controlsController, retrieveLifecycleManager, this.backgroundExecutor, 100000);
        this.statefulControlSubscriber = statefulControlSubscriber;
        List<ControlInfo> controls = structureInfo.getControls();
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(controls, 10));
        for (ControlInfo controlInfo : controls) {
            arrayList.add(controlInfo.getControlId());
        }
        retrieveLifecycleManager.maybeBindAndSubscribe(arrayList, statefulControlSubscriber);
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public void unsubscribe() {
        StatefulControlSubscriber statefulControlSubscriber = this.statefulControlSubscriber;
        if (statefulControlSubscriber != null) {
            statefulControlSubscriber.cancel();
        }
        this.statefulControlSubscriber = null;
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public void action(ComponentName componentName, ControlInfo controlInfo, ControlAction controlAction) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
        Intrinsics.checkNotNullParameter(controlAction, "action");
        if (this.statefulControlSubscriber == null) {
            Log.w("ControlsBindingControllerImpl", "No actions can occur outside of an active subscription. Ignoring.");
        } else {
            retrieveLifecycleManager(componentName).maybeBindAndSendAction(controlInfo.getControlId(), controlAction);
        }
    }

    @Override // com.android.systemui.util.UserAwareController
    public void changeUser(UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(userHandle, "newUser");
        if (!Intrinsics.areEqual(userHandle, this.currentUser)) {
            unbind();
            this.currentUser = userHandle;
        }
    }

    /* access modifiers changed from: private */
    public final void unbind() {
        unsubscribe();
        LoadSubscriber loadSubscriber = this.loadSubscriber;
        if (loadSubscriber != null) {
            loadSubscriber.loadCancel();
        }
        this.loadSubscriber = null;
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.currentProvider;
        if (controlsProviderLifecycleManager != null) {
            controlsProviderLifecycleManager.unbindService();
        }
        this.currentProvider = null;
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public void onComponentRemoved(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        this.backgroundExecutor.execute(new Runnable(this, componentName) { // from class: com.android.systemui.controls.controller.ControlsBindingControllerImpl$onComponentRemoved$1
            final /* synthetic */ ComponentName $componentName;
            final /* synthetic */ ControlsBindingControllerImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$componentName = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ControlsProviderLifecycleManager access$getCurrentProvider$p = ControlsBindingControllerImpl.access$getCurrentProvider$p(this.this$0);
                if (access$getCurrentProvider$p != null) {
                    ComponentName componentName2 = this.$componentName;
                    ControlsBindingControllerImpl controlsBindingControllerImpl = this.this$0;
                    if (Intrinsics.areEqual(access$getCurrentProvider$p.getComponentName(), componentName2)) {
                        ControlsBindingControllerImpl.access$unbind(controlsBindingControllerImpl);
                    }
                }
            }
        });
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("  ControlsBindingController:\n");
        sb.append("    currentUser=" + this.currentUser + '\n');
        sb.append(Intrinsics.stringPlus("    StatefulControlSubscriber=", this.statefulControlSubscriber));
        sb.append("    Providers=" + this.currentProvider + '\n');
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "StringBuilder(\"  ControlsBindingController:\\n\").apply {\n            append(\"    currentUser=$currentUser\\n\")\n            append(\"    StatefulControlSubscriber=$statefulControlSubscriber\")\n            append(\"    Providers=$currentProvider\\n\")\n        }.toString()");
        return sb2;
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    private abstract class CallbackRunnable implements Runnable {
        private final ControlsProviderLifecycleManager provider;
        final /* synthetic */ ControlsBindingControllerImpl this$0;
        private final IBinder token;

        public abstract void doRun();

        public CallbackRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder) {
            Intrinsics.checkNotNullParameter(controlsBindingControllerImpl, "this$0");
            Intrinsics.checkNotNullParameter(iBinder, "token");
            this.this$0 = controlsBindingControllerImpl;
            this.token = iBinder;
            this.provider = controlsBindingControllerImpl.currentProvider;
        }

        protected final ControlsProviderLifecycleManager getProvider() {
            return this.provider;
        }

        @Override // java.lang.Runnable
        public void run() {
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.provider;
            if (controlsProviderLifecycleManager == null) {
                Log.e("ControlsBindingControllerImpl", "No current provider set");
            } else if (!Intrinsics.areEqual(controlsProviderLifecycleManager.getUser(), this.this$0.currentUser)) {
                Log.e("ControlsBindingControllerImpl", "User " + this.provider.getUser() + " is not current user");
            } else if (!Intrinsics.areEqual(this.token, this.provider.getToken())) {
                Log.e("ControlsBindingControllerImpl", "Provider for token:" + this.token + " does not exist anymore");
            } else {
                doRun();
            }
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    private final class OnLoadRunnable extends CallbackRunnable {
        private final ControlsBindingController.LoadCallback callback;
        private final List<Control> list;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public OnLoadRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, List<Control> list, ControlsBindingController.LoadCallback loadCallback) {
            super(controlsBindingControllerImpl, iBinder);
            Intrinsics.checkNotNullParameter(controlsBindingControllerImpl, "this$0");
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(list, "list");
            Intrinsics.checkNotNullParameter(loadCallback, "callback");
            this.this$0 = controlsBindingControllerImpl;
            this.list = list;
            this.callback = loadCallback;
        }

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public void doRun() {
            Log.d("ControlsBindingControllerImpl", "LoadSubscription: Complete and loading controls");
            this.callback.accept(this.list);
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class OnCancelAndLoadRunnable extends CallbackRunnable {
        private final ControlsBindingController.LoadCallback callback;
        private final List<Control> list;
        private final IControlsSubscription subscription;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public OnCancelAndLoadRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, List<Control> list, IControlsSubscription iControlsSubscription, ControlsBindingController.LoadCallback loadCallback) {
            super(controlsBindingControllerImpl, iBinder);
            Intrinsics.checkNotNullParameter(controlsBindingControllerImpl, "this$0");
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(list, "list");
            Intrinsics.checkNotNullParameter(iControlsSubscription, "subscription");
            Intrinsics.checkNotNullParameter(loadCallback, "callback");
            this.this$0 = controlsBindingControllerImpl;
            this.list = list;
            this.subscription = iControlsSubscription;
            this.callback = loadCallback;
        }

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public void doRun() {
            Log.d("ControlsBindingControllerImpl", "LoadSubscription: Canceling and loading controls");
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider != null) {
                provider.cancelSubscription(this.subscription);
            }
            this.callback.accept(this.list);
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    private final class OnSubscribeRunnable extends CallbackRunnable {
        private final long requestLimit;
        private final IControlsSubscription subscription;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public OnSubscribeRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, IControlsSubscription iControlsSubscription, long j) {
            super(controlsBindingControllerImpl, iBinder);
            Intrinsics.checkNotNullParameter(controlsBindingControllerImpl, "this$0");
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(iControlsSubscription, "subscription");
            this.this$0 = controlsBindingControllerImpl;
            this.subscription = iControlsSubscription;
            this.requestLimit = j;
        }

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public void doRun() {
            Log.d("ControlsBindingControllerImpl", "LoadSubscription: Starting subscription");
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider != null) {
                provider.startSubscription(this.subscription, this.requestLimit);
            }
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class OnActionResponseRunnable extends CallbackRunnable {
        private final String controlId;
        private final int response;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public OnActionResponseRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, String str, int i) {
            super(controlsBindingControllerImpl, iBinder);
            Intrinsics.checkNotNullParameter(controlsBindingControllerImpl, "this$0");
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(str, "controlId");
            this.this$0 = controlsBindingControllerImpl;
            this.controlId = str;
            this.response = i;
        }

        public final String getControlId() {
            return this.controlId;
        }

        public final int getResponse() {
            return this.response;
        }

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public void doRun() {
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider != null) {
                ((ControlsController) this.this$0.lazyController.get()).onActionResponse(provider.getComponentName(), getControlId(), getResponse());
            }
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    private final class OnLoadErrorRunnable extends CallbackRunnable {
        private final ControlsBindingController.LoadCallback callback;
        private final String error;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public OnLoadErrorRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, String str, ControlsBindingController.LoadCallback loadCallback) {
            super(controlsBindingControllerImpl, iBinder);
            Intrinsics.checkNotNullParameter(controlsBindingControllerImpl, "this$0");
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(str, "error");
            Intrinsics.checkNotNullParameter(loadCallback, "callback");
            this.this$0 = controlsBindingControllerImpl;
            this.error = str;
            this.callback = loadCallback;
        }

        public final String getError() {
            return this.error;
        }

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public void doRun() {
            this.callback.error(this.error);
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider != null) {
                Log.e("ControlsBindingControllerImpl", "onError receive from '" + provider.getComponentName() + "': " + getError());
            }
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class LoadSubscriber extends IControlsSubscriber.Stub {
        private Function0<Unit> _loadCancelInternal;
        private ControlsBindingController.LoadCallback callback;
        private final long requestLimit;
        private IControlsSubscription subscription;
        final /* synthetic */ ControlsBindingControllerImpl this$0;
        private final ArrayList<Control> loadedControls = new ArrayList<>();
        private AtomicBoolean isTerminated = new AtomicBoolean(false);

        public LoadSubscriber(ControlsBindingControllerImpl controlsBindingControllerImpl, ControlsBindingController.LoadCallback loadCallback, long j) {
            Intrinsics.checkNotNullParameter(controlsBindingControllerImpl, "this$0");
            Intrinsics.checkNotNullParameter(loadCallback, "callback");
            this.this$0 = controlsBindingControllerImpl;
            this.callback = loadCallback;
            this.requestLimit = j;
        }

        public final ControlsBindingController.LoadCallback getCallback() {
            return this.callback;
        }

        public final long getRequestLimit() {
            return this.requestLimit;
        }

        public final ArrayList<Control> getLoadedControls() {
            return this.loadedControls;
        }

        public final Runnable loadCancel() {
            return new ControlsBindingControllerImpl$LoadSubscriber$loadCancel$1(this);
        }

        public void onSubscribe(IBinder iBinder, IControlsSubscription iControlsSubscription) {
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(iControlsSubscription, "subs");
            this.subscription = iControlsSubscription;
            this._loadCancelInternal = new ControlsBindingControllerImpl$LoadSubscriber$onSubscribe$1(this.this$0, this);
            this.this$0.backgroundExecutor.execute(new OnSubscribeRunnable(this.this$0, iBinder, iControlsSubscription, this.requestLimit));
        }

        public void onNext(IBinder iBinder, Control control) {
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(control, "c");
            this.this$0.backgroundExecutor.execute(new ControlsBindingControllerImpl$LoadSubscriber$onNext$1(this, control, this.this$0, iBinder));
        }

        public void onError(IBinder iBinder, String str) {
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(str, "s");
            maybeTerminateAndRun(new OnLoadErrorRunnable(this.this$0, iBinder, str, this.callback));
        }

        public void onComplete(IBinder iBinder) {
            Intrinsics.checkNotNullParameter(iBinder, "token");
            maybeTerminateAndRun(new OnLoadRunnable(this.this$0, iBinder, this.loadedControls, this.callback));
        }

        /* access modifiers changed from: private */
        public final void maybeTerminateAndRun(Runnable runnable) {
            if (!this.isTerminated.get()) {
                this._loadCancelInternal = ControlsBindingControllerImpl$LoadSubscriber$maybeTerminateAndRun$1.INSTANCE;
                this.callback = ControlsBindingControllerImpl.emptyCallback;
                ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.this$0.currentProvider;
                if (controlsProviderLifecycleManager != null) {
                    controlsProviderLifecycleManager.cancelLoadTimeout();
                }
                this.this$0.backgroundExecutor.execute(new ControlsBindingControllerImpl$LoadSubscriber$maybeTerminateAndRun$2(this, runnable));
            }
        }
    }
}
