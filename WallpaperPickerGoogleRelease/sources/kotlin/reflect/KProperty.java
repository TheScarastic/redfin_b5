package kotlin.reflect;
/* loaded from: classes.dex */
public interface KProperty<V> extends KCallable<V> {

    /* loaded from: classes.dex */
    public interface Getter<V> extends KCallable {
    }

    boolean isConst();

    boolean isLateinit();
}
