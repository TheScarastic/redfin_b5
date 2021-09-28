package kotlinx.coroutines.android;

import android.os.Handler;
import android.os.Looper;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: HandlerDispatcher.kt */
/* loaded from: classes2.dex */
public final class HandlerContext extends HandlerDispatcher {
    private volatile HandlerContext _immediate;
    private final Handler handler;
    private final HandlerContext immediate;
    private final boolean invokeImmediately;
    private final String name;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    private HandlerContext(Handler handler, String str, boolean z) {
        super(null);
        HandlerContext handlerContext = null;
        this.handler = handler;
        this.name = str;
        this.invokeImmediately = z;
        this._immediate = z ? this : handlerContext;
        HandlerContext handlerContext2 = this._immediate;
        if (handlerContext2 == null) {
            handlerContext2 = new HandlerContext(handler, str, true);
            this._immediate = handlerContext2;
        }
        this.immediate = handlerContext2;
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public HandlerContext(Handler handler, String str) {
        this(handler, str, false);
        Intrinsics.checkParameterIsNotNull(handler, "handler");
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public boolean isDispatchNeeded(CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return !this.invokeImmediately || (Intrinsics.areEqual(Looper.myLooper(), this.handler.getLooper()) ^ true);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        this.handler.post(runnable);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher, java.lang.Object
    public String toString() {
        String str = this.name;
        if (str == null) {
            String handler = this.handler.toString();
            Intrinsics.checkExpressionValueIsNotNull(handler, "handler.toString()");
            return handler;
        } else if (!this.invokeImmediately) {
            return str;
        } else {
            return this.name + " [immediate]";
        }
    }

    public boolean equals(Object obj) {
        return (obj instanceof HandlerContext) && ((HandlerContext) obj).handler == this.handler;
    }

    public int hashCode() {
        return System.identityHashCode(this.handler);
    }
}
