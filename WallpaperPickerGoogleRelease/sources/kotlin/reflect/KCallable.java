package kotlin.reflect;

import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public interface KCallable<R> extends KAnnotatedElement {
    R call(@NotNull Object... objArr);

    R callBy(@NotNull Map<?, ? extends Object> map);

    @NotNull
    List<?> getParameters();

    @NotNull
    KType getReturnType();

    @NotNull
    List<?> getTypeParameters();

    @Nullable
    KVisibility getVisibility();

    boolean isAbstract();

    boolean isFinal();

    boolean isOpen();

    boolean isSuspend();
}
