package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.applications.ServiceListing;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.settings.UserTracker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsListingControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsListingControllerImpl implements ControlsListingController {
    public static final Companion Companion = new Companion(null);
    private Set<ComponentName> availableComponents;
    private List<? extends ServiceInfo> availableServices;
    private final Executor backgroundExecutor;
    private final Set<ControlsListingController.ControlsListingCallback> callbacks;
    private final Context context;
    private int currentUserId;
    private ServiceListing serviceListing;
    private final Function1<Context, ServiceListing> serviceListingBuilder;
    private final ServiceListing.Callback serviceListingCallback;
    private AtomicInteger userChangeInProgress;

    /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: kotlin.jvm.functions.Function1<? super android.content.Context, ? extends com.android.settingslib.applications.ServiceListing> */
    /* JADX WARN: Multi-variable type inference failed */
    @VisibleForTesting
    public ControlsListingControllerImpl(Context context, Executor executor, Function1<? super Context, ? extends ServiceListing> function1, UserTracker userTracker) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(executor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(function1, "serviceListingBuilder");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        this.context = context;
        this.backgroundExecutor = executor;
        this.serviceListingBuilder = function1;
        this.serviceListing = (ServiceListing) function1.invoke(context);
        this.callbacks = new LinkedHashSet();
        this.availableComponents = SetsKt__SetsKt.emptySet();
        this.availableServices = CollectionsKt__CollectionsKt.emptyList();
        this.userChangeInProgress = new AtomicInteger(0);
        this.currentUserId = userTracker.getUserId();
        ControlsListingControllerImpl$serviceListingCallback$1 controlsListingControllerImpl$serviceListingCallback$1 = new ServiceListing.Callback(this) { // from class: com.android.systemui.controls.management.ControlsListingControllerImpl$serviceListingCallback$1
            final /* synthetic */ ControlsListingControllerImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.settingslib.applications.ServiceListing.Callback
            public final void onServicesReloaded(List<ServiceInfo> list) {
                Intrinsics.checkNotNullExpressionValue(list, "it");
                final List<ServiceInfo> list2 = CollectionsKt___CollectionsKt.toList(list);
                final LinkedHashSet linkedHashSet = new LinkedHashSet();
                for (ServiceInfo serviceInfo : list2) {
                    linkedHashSet.add(serviceInfo.getComponentName());
                }
                Executor executor2 = this.this$0.backgroundExecutor;
                final ControlsListingControllerImpl controlsListingControllerImpl = this.this$0;
                executor2.execute(new Runnable() { // from class: com.android.systemui.controls.management.ControlsListingControllerImpl$serviceListingCallback$1.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        if (controlsListingControllerImpl.userChangeInProgress.get() <= 0 && !linkedHashSet.equals(controlsListingControllerImpl.availableComponents)) {
                            Log.d("ControlsListingControllerImpl", Intrinsics.stringPlus("ServiceConfig reloaded, count: ", Integer.valueOf(linkedHashSet.size())));
                            controlsListingControllerImpl.availableComponents = linkedHashSet;
                            controlsListingControllerImpl.availableServices = list2;
                            List<ControlsServiceInfo> currentServices = controlsListingControllerImpl.getCurrentServices();
                            for (ControlsListingController.ControlsListingCallback controlsListingCallback : controlsListingControllerImpl.callbacks) {
                                controlsListingCallback.onServicesUpdated(currentServices);
                            }
                        }
                    }
                });
            }
        };
        this.serviceListingCallback = controlsListingControllerImpl$serviceListingCallback$1;
        Log.d("ControlsListingControllerImpl", "Initializing");
        this.serviceListing.addCallback(controlsListingControllerImpl$serviceListingCallback$1);
        this.serviceListing.setListening(true);
        this.serviceListing.reload();
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public ControlsListingControllerImpl(Context context, Executor executor, UserTracker userTracker) {
        this(context, executor, AnonymousClass1.INSTANCE, userTracker);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
    }

    /* compiled from: ControlsListingControllerImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.util.UserAwareController
    public int getCurrentUserId() {
        return this.currentUserId;
    }

    @Override // com.android.systemui.util.UserAwareController
    public void changeUser(UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(userHandle, "newUser");
        this.userChangeInProgress.incrementAndGet();
        this.serviceListing.setListening(false);
        this.backgroundExecutor.execute(new Runnable(this, userHandle) { // from class: com.android.systemui.controls.management.ControlsListingControllerImpl$changeUser$1
            final /* synthetic */ UserHandle $newUser;
            final /* synthetic */ ControlsListingControllerImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$newUser = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                if (ControlsListingControllerImpl.access$getUserChangeInProgress$p(this.this$0).decrementAndGet() == 0) {
                    ControlsListingControllerImpl.access$setCurrentUserId$p(this.this$0, this.$newUser.getIdentifier());
                    Context createContextAsUser = ControlsListingControllerImpl.access$getContext$p(this.this$0).createContextAsUser(this.$newUser, 0);
                    ControlsListingControllerImpl controlsListingControllerImpl = this.this$0;
                    Function1 access$getServiceListingBuilder$p = ControlsListingControllerImpl.access$getServiceListingBuilder$p(controlsListingControllerImpl);
                    Intrinsics.checkNotNullExpressionValue(createContextAsUser, "contextForUser");
                    ControlsListingControllerImpl.access$setServiceListing$p(controlsListingControllerImpl, (ServiceListing) access$getServiceListingBuilder$p.invoke(createContextAsUser));
                    ControlsListingControllerImpl.access$getServiceListing$p(this.this$0).addCallback(ControlsListingControllerImpl.access$getServiceListingCallback$p(this.this$0));
                    ControlsListingControllerImpl.access$getServiceListing$p(this.this$0).setListening(true);
                    ControlsListingControllerImpl.access$getServiceListing$p(this.this$0).reload();
                }
            }
        });
    }

    public void addCallback(ControlsListingController.ControlsListingCallback controlsListingCallback) {
        Intrinsics.checkNotNullParameter(controlsListingCallback, "listener");
        this.backgroundExecutor.execute(new Runnable(this, controlsListingCallback) { // from class: com.android.systemui.controls.management.ControlsListingControllerImpl$addCallback$1
            final /* synthetic */ ControlsListingController.ControlsListingCallback $listener;
            final /* synthetic */ ControlsListingControllerImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$listener = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                if (ControlsListingControllerImpl.access$getUserChangeInProgress$p(this.this$0).get() > 0) {
                    this.this$0.addCallback(this.$listener);
                    return;
                }
                List<ControlsServiceInfo> currentServices = this.this$0.getCurrentServices();
                Log.d("ControlsListingControllerImpl", Intrinsics.stringPlus("Subscribing callback, service count: ", Integer.valueOf(currentServices.size())));
                ControlsListingControllerImpl.access$getCallbacks$p(this.this$0).add(this.$listener);
                this.$listener.onServicesUpdated(currentServices);
            }
        });
    }

    public void removeCallback(ControlsListingController.ControlsListingCallback controlsListingCallback) {
        Intrinsics.checkNotNullParameter(controlsListingCallback, "listener");
        this.backgroundExecutor.execute(new Runnable(this, controlsListingCallback) { // from class: com.android.systemui.controls.management.ControlsListingControllerImpl$removeCallback$1
            final /* synthetic */ ControlsListingController.ControlsListingCallback $listener;
            final /* synthetic */ ControlsListingControllerImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$listener = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Log.d("ControlsListingControllerImpl", "Unsubscribing callback");
                this.this$0.callbacks.remove(this.$listener);
            }
        });
    }

    public List<ControlsServiceInfo> getCurrentServices() {
        List<? extends ServiceInfo> list = this.availableServices;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10));
        for (ServiceInfo serviceInfo : list) {
            arrayList.add(new ControlsServiceInfo(this.context, serviceInfo));
        }
        return arrayList;
    }

    @Override // com.android.systemui.controls.management.ControlsListingController
    public CharSequence getAppLabel(ComponentName componentName) {
        Object obj;
        Intrinsics.checkNotNullParameter(componentName, "name");
        Iterator<T> it = getCurrentServices().iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (Intrinsics.areEqual(((ControlsServiceInfo) obj).componentName, componentName)) {
                break;
            }
        }
        ControlsServiceInfo controlsServiceInfo = (ControlsServiceInfo) obj;
        if (controlsServiceInfo == null) {
            return null;
        }
        return controlsServiceInfo.loadLabel();
    }
}
