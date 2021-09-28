package kotlinx.coroutines.internal;
/* loaded from: classes.dex */
public class ArrayQueue<T> {
    public Object[] elements = new Object[16];
    public int head;
    public int tail;
}
