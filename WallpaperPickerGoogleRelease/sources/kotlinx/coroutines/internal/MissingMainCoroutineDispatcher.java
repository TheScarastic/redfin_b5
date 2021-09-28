package kotlinx.coroutines.internal;

import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.MainCoroutineDispatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class MissingMainCoroutineDispatcher extends MainCoroutineDispatcher {
    public final Throwable cause;
    public final String errorHint;

    public MissingMainCoroutineDispatcher(@Nullable Throwable th, @Nullable String str) {
        this.cause = th;
        this.errorHint = str;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        missing();
        throw null;
    }

    @Override // kotlinx.coroutines.MainCoroutineDispatcher
    @NotNull
    public MainCoroutineDispatcher getImmediate() {
        return this;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public boolean isDispatchNeeded(@NotNull CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        missing();
        throw null;
    }

    public final Void missing() {
        String str;
        if (this.cause != null) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Module with the Main dispatcher had failed to initialize");
            String str2 = this.errorHint;
            if (str2 == null || (str = SupportMenuInflater$$ExternalSyntheticOutline0.m(". ", str2)) == null) {
                str = "";
            }
            m.append((Object) str);
            throw new IllegalStateException(m.toString(), this.cause);
        }
        throw new IllegalStateException("Module with the Main dispatcher is missing. Add dependency providing the Main dispatcher, e.g. 'kotlinx-coroutines-android'");
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher, java.lang.Object
    @NotNull
    public String toString() {
        String str;
        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Main[missing");
        if (this.cause != null) {
            StringBuilder m2 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m(", cause=");
            m2.append(this.cause);
            str = m2.toString();
        } else {
            str = "";
        }
        m.append(str);
        m.append(']');
        return m.toString();
    }

    public MissingMainCoroutineDispatcher(Throwable th, String str, int i) {
        this.cause = th;
        this.errorHint = null;
    }
}
