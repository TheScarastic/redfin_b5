package kotlin;
/* compiled from: Lazy.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class LazyKt__LazyKt extends LazyKt__LazyJVMKt {
    public static <T> Lazy<T> lazyOf(T t) {
        return new InitializedLazyImpl(t);
    }
}
