package kotlin.reflect;
/* compiled from: KCallable.kt */
/* loaded from: classes2.dex */
public interface KCallable<R> {
    R call(Object... objArr);

    String getName();
}
