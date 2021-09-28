package com.android.systemui.controls.controller;

import android.service.controls.IControlsActionCallback;
import android.service.controls.IControlsProvider;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.service.controls.actions.ControlAction;
import android.service.controls.actions.ControlActionWrapper;
import android.util.Log;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ServiceWrapper.kt */
/* loaded from: classes.dex */
public final class ServiceWrapper {
    public static final Companion Companion = new Companion(null);
    private final IControlsProvider service;

    /* compiled from: ServiceWrapper.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public ServiceWrapper(IControlsProvider iControlsProvider) {
        Intrinsics.checkNotNullParameter(iControlsProvider, "service");
        this.service = iControlsProvider;
    }

    public final IControlsProvider getService() {
        return this.service;
    }

    public final boolean load(IControlsSubscriber iControlsSubscriber) {
        Intrinsics.checkNotNullParameter(iControlsSubscriber, "subscriber");
        try {
            getService().load(iControlsSubscriber);
            return true;
        } catch (Exception e) {
            Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean loadSuggested(IControlsSubscriber iControlsSubscriber) {
        Intrinsics.checkNotNullParameter(iControlsSubscriber, "subscriber");
        try {
            getService().loadSuggested(iControlsSubscriber);
            return true;
        } catch (Exception e) {
            Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean subscribe(List<String> list, IControlsSubscriber iControlsSubscriber) {
        Intrinsics.checkNotNullParameter(list, "controlIds");
        Intrinsics.checkNotNullParameter(iControlsSubscriber, "subscriber");
        try {
            getService().subscribe(list, iControlsSubscriber);
            return true;
        } catch (Exception e) {
            Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean request(IControlsSubscription iControlsSubscription, long j) {
        Intrinsics.checkNotNullParameter(iControlsSubscription, "subscription");
        try {
            iControlsSubscription.request(j);
            return true;
        } catch (Exception e) {
            Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean cancel(IControlsSubscription iControlsSubscription) {
        Intrinsics.checkNotNullParameter(iControlsSubscription, "subscription");
        try {
            iControlsSubscription.cancel();
            return true;
        } catch (Exception e) {
            Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            return false;
        }
    }

    public final boolean action(String str, ControlAction controlAction, IControlsActionCallback iControlsActionCallback) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        Intrinsics.checkNotNullParameter(controlAction, "action");
        Intrinsics.checkNotNullParameter(iControlsActionCallback, "cb");
        try {
            getService().action(str, new ControlActionWrapper(controlAction), iControlsActionCallback);
            return true;
        } catch (Exception e) {
            Log.e("ServiceWrapper", "Caught exception from ControlsProviderService", e);
            return false;
        }
    }
}
