package androidx.dynamicanimation.animation;
/* loaded from: classes.dex */
public abstract class FloatPropertyCompat<T> {
    final String mPropertyName;

    public abstract float getValue(T t);

    public abstract void setValue(T t, float f);

    public FloatPropertyCompat(String str) {
        this.mPropertyName = str;
    }
}
