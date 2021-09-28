package kotlinx.coroutines.android;

import android.os.Handler;
import android.os.Looper;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class HandlerDispatcherKt {
    static {
        Object obj;
        try {
            Looper mainLooper = Looper.getMainLooper();
            Intrinsics.checkExpressionValueIsNotNull(mainLooper, "Looper.getMainLooper()");
            Handler asHandler = asHandler(mainLooper, true);
            Intrinsics.checkParameterIsNotNull(asHandler, "handler");
            obj = new HandlerContext(asHandler, "Main", false);
        } catch (Throwable th) {
            obj = ResultKt.createFailure(th);
        }
        if (obj instanceof Result.Failure) {
            obj = null;
        }
        HandlerDispatcher handlerDispatcher = (HandlerDispatcher) obj;
    }

    @NotNull
    public static final Handler asHandler(@NotNull Looper looper, boolean z) {
        Intrinsics.checkParameterIsNotNull(looper, "$this$asHandler");
        if (!z) {
            return new Handler(looper);
        }
        Object invoke = Handler.class.getDeclaredMethod("createAsync", Looper.class).invoke(null, looper);
        if (invoke != null) {
            return (Handler) invoke;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.os.Handler");
    }
}
