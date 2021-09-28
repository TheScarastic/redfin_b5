package com.google.android.systemui.columbus;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.systemui.columbus.ColumbusServiceProxy;
import com.google.android.systemui.columbus.IColumbusService;
import com.google.android.systemui.columbus.IColumbusServiceListener;
import java.util.List;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ColumbusServiceProxy.kt */
/* loaded from: classes2.dex */
public final class ColumbusServiceProxy$binder$1 extends IColumbusService.Stub {
    final /* synthetic */ ColumbusServiceProxy this$0;

    /* access modifiers changed from: package-private */
    public ColumbusServiceProxy$binder$1(ColumbusServiceProxy columbusServiceProxy) {
        this.this$0 = columbusServiceProxy;
    }

    @Override // com.google.android.systemui.columbus.IColumbusService
    public void registerGestureListener(IBinder iBinder, IBinder iBinder2) {
        ColumbusServiceProxy.access$checkPermission(this.this$0);
        int size = ColumbusServiceProxy.access$getColumbusServiceListeners$p(this.this$0).size() - 1;
        if (size >= 0) {
            while (true) {
                int i = size - 1;
                IColumbusServiceListener listener = ((ColumbusServiceProxy.ColumbusServiceListener) ColumbusServiceProxy.access$getColumbusServiceListeners$p(this.this$0).get(size)).getListener();
                if (listener == null) {
                    ColumbusServiceProxy.access$getColumbusServiceListeners$p(this.this$0).remove(size);
                } else {
                    try {
                        listener.setListener(iBinder, iBinder2);
                    } catch (RemoteException e) {
                        Log.e("Columbus/ColumbusProxy", "Cannot set listener", e);
                        ColumbusServiceProxy.access$getColumbusServiceListeners$p(this.this$0).remove(size);
                    }
                }
                if (i >= 0) {
                    size = i;
                } else {
                    return;
                }
            }
        }
    }

    @Override // com.google.android.systemui.columbus.IColumbusService
    public void registerServiceListener(IBinder iBinder, IBinder iBinder2) {
        ColumbusServiceProxy.access$checkPermission(this.this$0);
        if (iBinder == null) {
            Log.e("Columbus/ColumbusProxy", "Binder token must not be null");
        } else if (iBinder2 == null) {
            boolean unused = CollectionsKt__MutableCollectionsKt.removeAll((List) ColumbusServiceProxy.access$getColumbusServiceListeners$p(this.this$0), (Function1) new Function1<ColumbusServiceProxy.ColumbusServiceListener, Boolean>(iBinder) { // from class: com.google.android.systemui.columbus.ColumbusServiceProxy$binder$1$registerServiceListener$1
                final /* synthetic */ IBinder $token;

                /* access modifiers changed from: package-private */
                {
                    this.$token = r1;
                }

                /* Return type fixed from 'java.lang.Object' to match base method */
                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Boolean invoke(ColumbusServiceProxy.ColumbusServiceListener columbusServiceListener) {
                    return Boolean.valueOf(invoke(columbusServiceListener));
                }

                public final boolean invoke(ColumbusServiceProxy.ColumbusServiceListener columbusServiceListener) {
                    Intrinsics.checkNotNullParameter(columbusServiceListener, "it");
                    if (!Intrinsics.areEqual(this.$token, columbusServiceListener.getToken())) {
                        return false;
                    }
                    columbusServiceListener.unlinkToDeath();
                    return true;
                }
            });
        } else {
            ColumbusServiceProxy.access$getColumbusServiceListeners$p(this.this$0).add(new ColumbusServiceProxy.ColumbusServiceListener(iBinder, IColumbusServiceListener.Stub.asInterface(iBinder2)));
        }
    }
}
