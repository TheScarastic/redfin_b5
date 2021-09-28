package com.google.android.systemui.columbus;

import android.app.IActivityManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ColumbusContentObserver.kt */
/* loaded from: classes2.dex */
public final class ColumbusContentObserver extends ContentObserver {
    private static final Companion Companion = new Companion(null);
    private final IActivityManager activityManagerService;
    private final Function1<Uri, Unit> callback;
    private final ContentResolverWrapper contentResolver;
    private final Uri settingsUri;
    private final ColumbusContentObserver$userSwitchCallback$1 userSwitchCallback;

    public /* synthetic */ ColumbusContentObserver(ContentResolverWrapper contentResolverWrapper, Uri uri, Function1 function1, IActivityManager iActivityManager, Handler handler, DefaultConstructorMarker defaultConstructorMarker) {
        this(contentResolverWrapper, uri, function1, iActivityManager, handler);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit> */
    /* JADX WARN: Multi-variable type inference failed */
    private ColumbusContentObserver(ContentResolverWrapper contentResolverWrapper, Uri uri, Function1<? super Uri, Unit> function1, IActivityManager iActivityManager, Handler handler) {
        super(handler);
        this.contentResolver = contentResolverWrapper;
        this.settingsUri = uri;
        this.callback = function1;
        this.activityManagerService = iActivityManager;
        this.userSwitchCallback = new ColumbusContentObserver$userSwitchCallback$1(this);
    }

    /* compiled from: ColumbusContentObserver.kt */
    /* loaded from: classes2.dex */
    public static final class Factory {
        private final IActivityManager activityManagerService;
        private final ContentResolverWrapper contentResolver;
        private final Handler handler;

        public Factory(ContentResolverWrapper contentResolverWrapper, IActivityManager iActivityManager, Handler handler) {
            Intrinsics.checkNotNullParameter(contentResolverWrapper, "contentResolver");
            Intrinsics.checkNotNullParameter(iActivityManager, "activityManagerService");
            Intrinsics.checkNotNullParameter(handler, "handler");
            this.contentResolver = contentResolverWrapper;
            this.activityManagerService = iActivityManager;
            this.handler = handler;
        }

        public final ColumbusContentObserver create(Uri uri, Function1<? super Uri, Unit> function1) {
            Intrinsics.checkNotNullParameter(uri, "settingsUri");
            Intrinsics.checkNotNullParameter(function1, "callback");
            return new ColumbusContentObserver(this.contentResolver, uri, function1, this.activityManagerService, this.handler, null);
        }
    }

    public final void activate() {
        updateContentObserver();
        try {
            this.activityManagerService.registerUserSwitchObserver(this.userSwitchCallback, "Columbus/ContentObserve");
        } catch (RemoteException e) {
            Log.e("Columbus/ContentObserve", "Failed to register user switch observer", e);
        }
    }

    public final void deactivate() {
        this.contentResolver.unregisterContentObserver(this);
        try {
            this.activityManagerService.unregisterUserSwitchObserver(this.userSwitchCallback);
        } catch (RemoteException e) {
            Log.e("Columbus/ContentObserve", "Failed to unregister user switch observer", e);
        }
    }

    /* access modifiers changed from: private */
    public final void updateContentObserver() {
        this.contentResolver.unregisterContentObserver(this);
        this.contentResolver.registerContentObserver(this.settingsUri, false, this, -2);
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean z, Uri uri) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        this.callback.invoke(uri);
    }

    /* compiled from: ColumbusContentObserver.kt */
    /* loaded from: classes2.dex */
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
