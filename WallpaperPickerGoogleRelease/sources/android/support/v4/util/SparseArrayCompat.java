package android.support.v4.util;
/* loaded from: classes.dex */
public class SparseArrayCompat<E> implements Cloneable {
    public static final Object DELETED = new Object();
    public int[] mKeys;
    public int mSize;
    public Object[] mValues;

    public SparseArrayCompat() {
        int i;
        int i2 = 4;
        while (true) {
            i = 40;
            if (i2 >= 32) {
                break;
            }
            int i3 = (1 << i2) - 12;
            if (40 <= i3) {
                i = i3;
                break;
            }
            i2++;
        }
        int i4 = i / 4;
        this.mKeys = new int[i4];
        this.mValues = new Object[i4];
        this.mSize = 0;
    }

    @Override // java.lang.Object
    public Object clone() throws CloneNotSupportedException {
        SparseArrayCompat sparseArrayCompat = null;
        try {
            SparseArrayCompat sparseArrayCompat2 = (SparseArrayCompat) super.clone();
            try {
                sparseArrayCompat2.mKeys = (int[]) this.mKeys.clone();
                sparseArrayCompat2.mValues = (Object[]) this.mValues.clone();
                return sparseArrayCompat2;
            } catch (CloneNotSupportedException unused) {
                sparseArrayCompat = sparseArrayCompat2;
                return sparseArrayCompat;
            }
        } catch (CloneNotSupportedException unused2) {
        }
    }

    @Override // java.lang.Object
    public String toString() {
        int i = this.mSize;
        if (i <= 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(i * 28);
        sb.append('{');
        for (int i2 = 0; i2 < this.mSize; i2++) {
            if (i2 > 0) {
                sb.append(", ");
            }
            sb.append(this.mKeys[i2]);
            sb.append('=');
            Object obj = this.mValues[i2];
            if (obj != this) {
                sb.append(obj);
            } else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
