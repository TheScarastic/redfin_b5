package com.google.common.collect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
/* loaded from: classes.dex */
public final class ArrayListMultimap<K, V> {
    private static final long serialVersionUID = 0;
    public transient int expectedValuesPerKey = 3;

    public ArrayListMultimap() {
        super(new CompactHashMap(12));
        CollectPreconditions.checkNonnegative(3, "expectedValuesPerKey");
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.expectedValuesPerKey = 3;
        int readInt = objectInputStream.readInt();
        setMap(new CompactHashMap());
        for (int i = 0; i < readInt; i++) {
            Collection collection = get(objectInputStream.readObject());
            int readInt2 = objectInputStream.readInt();
            for (int i2 = 0; i2 < readInt2; i2++) {
                collection.add(objectInputStream.readObject());
            }
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(asMap().size());
        for (Map.Entry<K, Collection<V>> entry : asMap().entrySet()) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeInt(entry.getValue().size());
            for (V v : entry.getValue()) {
                objectOutputStream.writeObject(v);
            }
        }
    }

    public Collection createCollection() {
        return new ArrayList(this.expectedValuesPerKey);
    }
}
