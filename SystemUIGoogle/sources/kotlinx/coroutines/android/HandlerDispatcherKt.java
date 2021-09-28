package kotlinx.coroutines.android;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.VisibleForTesting;
import java.lang.reflect.Constructor;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: HandlerDispatcher.kt */
/* loaded from: classes2.dex */
public final class HandlerDispatcherKt {
    public static final HandlerDispatcher Main;

    @VisibleForTesting
    public static final Handler asHandler(Looper looper, boolean z) {
        int i;
        Intrinsics.checkParameterIsNotNull(looper, "$this$asHandler");
        if (!z || (i = Build.VERSION.SDK_INT) < 16) {
            return new Handler(looper);
        }
        if (i >= 28) {
            Object invoke = Handler.class.getDeclaredMethod("createAsync", Looper.class).invoke(null, looper);
            if (invoke != null) {
                return (Handler) invoke;
            }
            throw new TypeCastException("null cannot be cast to non-null type android.os.Handler");
        }
        try {
            Constructor declaredConstructor = Handler.class.getDeclaredConstructor(Looper.class, Handler.Callback.class, Boolean.TYPE);
            Intrinsics.checkExpressionValueIsNotNull(declaredConstructor, "Handler::class.java.getD…:class.javaPrimitiveType)");
            Object newInstance = declaredConstructor.newInstance(looper, null, Boolean.TRUE);
            Intrinsics.checkExpressionValueIsNotNull(newInstance, "constructor.newInstance(this, null, true)");
            return (Handler) newInstance;
        } catch (NoSuchMethodException unused) {
            return new Handler(looper);
        }
    }

    static {
        Object obj;
        try {
            Result.Companion companion = Result.Companion;
            Looper mainLooper = Looper.getMainLooper();
            Intrinsics.checkExpressionValueIsNotNull(mainLooper, "Looper.getMainLooper()");
            obj = Result.m670constructorimpl(new HandlerContext(asHandler(mainLooper, true), "Main"));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.m670constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m674isFailureimpl(obj)) {
            obj = null;
        }
        Main = (HandlerDispatcher) obj;
    }
}
