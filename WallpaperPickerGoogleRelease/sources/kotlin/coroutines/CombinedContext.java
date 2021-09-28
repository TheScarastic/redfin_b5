package kotlin.coroutines;

import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.support.v4.app.FragmentTabHost$SavedState$$ExternalSyntheticOutline0;
import java.io.Serializable;
import java.util.Objects;
import kotlin.Unit;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$IntRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class CombinedContext implements CoroutineContext, Serializable {
    private final CoroutineContext.Element element;
    private final CoroutineContext left;

    /* loaded from: classes.dex */
    public static final class Serialized implements Serializable {
        private static final long serialVersionUID = 0;
        @NotNull
        private final CoroutineContext[] elements;

        public Serialized(@NotNull CoroutineContext[] coroutineContextArr) {
            this.elements = coroutineContextArr;
        }

        private final Object readResolve() {
            CoroutineContext[] coroutineContextArr = this.elements;
            CoroutineContext coroutineContext = EmptyCoroutineContext.INSTANCE;
            for (CoroutineContext coroutineContext2 : coroutineContextArr) {
                coroutineContext = coroutineContext.plus(coroutineContext2);
            }
            return coroutineContext;
        }
    }

    public CombinedContext(@NotNull CoroutineContext coroutineContext, @NotNull CoroutineContext.Element element) {
        Intrinsics.checkNotNullParameter(coroutineContext, "left");
        Intrinsics.checkNotNullParameter(element, "element");
        this.left = coroutineContext;
        this.element = element;
    }

    private final Object writeReplace() {
        int size = size();
        CoroutineContext[] coroutineContextArr = new CoroutineContext[size];
        Ref$IntRef ref$IntRef = new Ref$IntRef();
        boolean z = false;
        ref$IntRef.element = 0;
        fold(Unit.INSTANCE, new Function2<Unit, CoroutineContext.Element, Unit>(coroutineContextArr, ref$IntRef) { // from class: kotlin.coroutines.CombinedContext$writeReplace$1
            public final /* synthetic */ CoroutineContext[] $elements;
            public final /* synthetic */ Ref$IntRef $index;

            {
                this.$elements = r1;
                this.$index = r2;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
            @Override // kotlin.jvm.functions.Function2
            public Unit invoke(Unit unit, CoroutineContext.Element element) {
                CoroutineContext.Element element2 = element;
                Intrinsics.checkNotNullParameter(unit, "<anonymous parameter 0>");
                Intrinsics.checkNotNullParameter(element2, "element");
                CoroutineContext[] coroutineContextArr2 = this.$elements;
                Ref$IntRef ref$IntRef2 = this.$index;
                int i = ref$IntRef2.element;
                ref$IntRef2.element = i + 1;
                coroutineContextArr2[i] = element2;
                return Unit.INSTANCE;
            }
        });
        if (ref$IntRef.element == size) {
            z = true;
        }
        if (z) {
            return new Serialized(coroutineContextArr);
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    @Override // java.lang.Object
    public boolean equals(@Nullable Object obj) {
        boolean z;
        if (this != obj) {
            if (!(obj instanceof CombinedContext)) {
                return false;
            }
            CombinedContext combinedContext = (CombinedContext) obj;
            if (combinedContext.size() != size()) {
                return false;
            }
            Objects.requireNonNull(combinedContext);
            while (true) {
                CoroutineContext.Element element = this.element;
                if (Intrinsics.areEqual(combinedContext.get(element.getKey()), element)) {
                    CoroutineContext coroutineContext = this.left;
                    if (!(coroutineContext instanceof CombinedContext)) {
                        Objects.requireNonNull(coroutineContext, "null cannot be cast to non-null type kotlin.coroutines.CoroutineContext.Element");
                        CoroutineContext.Element element2 = (CoroutineContext.Element) coroutineContext;
                        z = Intrinsics.areEqual(combinedContext.get(element2.getKey()), element2);
                        break;
                    }
                    this = (CombinedContext) coroutineContext;
                } else {
                    z = false;
                    break;
                }
            }
            if (!z) {
                return false;
            }
        }
        return true;
    }

    @Override // kotlin.coroutines.CoroutineContext
    public <R> R fold(R r, @NotNull Function2<? super R, ? super CoroutineContext.Element, ? extends R> function2) {
        Intrinsics.checkNotNullParameter(function2, "operation");
        return (R) function2.invoke((Object) this.left.fold(r, function2), this.element);
    }

    @Override // kotlin.coroutines.CoroutineContext
    @Nullable
    public <E extends CoroutineContext.Element> E get(@NotNull CoroutineContext.Key<E> key) {
        Intrinsics.checkNotNullParameter(key, "key");
        while (true) {
            E e = (E) this.element.get(key);
            if (e != null) {
                return e;
            }
            CoroutineContext coroutineContext = this.left;
            if (!(coroutineContext instanceof CombinedContext)) {
                return (E) coroutineContext.get(key);
            }
            this = (CombinedContext) coroutineContext;
        }
    }

    @Override // java.lang.Object
    public int hashCode() {
        return this.element.hashCode() + this.left.hashCode();
    }

    @Override // kotlin.coroutines.CoroutineContext
    @NotNull
    public CoroutineContext minusKey(@NotNull CoroutineContext.Key<?> key) {
        Intrinsics.checkNotNullParameter(key, "key");
        if (this.element.get(key) != null) {
            return this.left;
        }
        CoroutineContext minusKey = this.left.minusKey(key);
        if (minusKey == this.left) {
            return this;
        }
        if (minusKey == EmptyCoroutineContext.INSTANCE) {
            return this.element;
        }
        return new CombinedContext(minusKey, this.element);
    }

    @Override // kotlin.coroutines.CoroutineContext
    @NotNull
    public CoroutineContext plus(@NotNull CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter(coroutineContext, "context");
        return coroutineContext == EmptyCoroutineContext.INSTANCE ? this : (CoroutineContext) coroutineContext.fold(this, CoroutineContext$plus$1.INSTANCE);
    }

    public final int size() {
        int i = 2;
        while (true) {
            CoroutineContext coroutineContext = this.left;
            if (!(coroutineContext instanceof CombinedContext)) {
                coroutineContext = null;
            }
            this = (CombinedContext) coroutineContext;
            if (this == null) {
                return i;
            }
            i++;
        }
    }

    @Override // java.lang.Object
    @NotNull
    public String toString() {
        return FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("["), (String) fold("", CombinedContext$toString$1.INSTANCE), "]");
    }
}
