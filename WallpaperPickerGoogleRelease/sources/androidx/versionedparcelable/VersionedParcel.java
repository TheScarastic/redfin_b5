package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import androidx.collection.SimpleArrayMap;
import androidx.core.graphics.PathParser$$ExternalSyntheticOutline0;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class VersionedParcel {
    public final SimpleArrayMap<String, Class<?>> mParcelizerCache;
    public final SimpleArrayMap<String, Method> mReadCache;
    public final SimpleArrayMap<String, Method> mWriteCache;

    public VersionedParcel(SimpleArrayMap<String, Method> simpleArrayMap, SimpleArrayMap<String, Method> simpleArrayMap2, SimpleArrayMap<String, Class<?>> simpleArrayMap3) {
        this.mReadCache = simpleArrayMap;
        this.mWriteCache = simpleArrayMap2;
        this.mParcelizerCache = simpleArrayMap3;
    }

    public abstract void closeField();

    public abstract VersionedParcel createSubParcel();

    public final Class<?> findParcelClass(Class<?> cls) throws ClassNotFoundException {
        Class<?> orDefault = this.mParcelizerCache.getOrDefault(cls.getName(), null);
        if (orDefault != null) {
            return orDefault;
        }
        Class<?> cls2 = Class.forName(String.format("%s.%sParcelizer", cls.getPackage().getName(), cls.getSimpleName()), false, cls.getClassLoader());
        this.mParcelizerCache.put(cls.getName(), cls2);
        return cls2;
    }

    public final Method getReadMethod(String str) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        Method orDefault = this.mReadCache.getOrDefault(str, null);
        if (orDefault != null) {
            return orDefault;
        }
        Method declaredMethod = Class.forName(str, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", VersionedParcel.class);
        this.mReadCache.put(str, declaredMethod);
        return declaredMethod;
    }

    public final Method getWriteMethod(Class<?> cls) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        Method orDefault = this.mWriteCache.getOrDefault(cls.getName(), null);
        if (orDefault != null) {
            return orDefault;
        }
        Method declaredMethod = findParcelClass(cls).getDeclaredMethod("write", cls, VersionedParcel.class);
        this.mWriteCache.put(cls.getName(), declaredMethod);
        return declaredMethod;
    }

    public <T> T[] readArray(T[] tArr, int i) {
        Serializable serializable;
        if (!readField(i)) {
            return tArr;
        }
        int readInt = readInt();
        if (readInt < 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList(readInt);
        if (readInt != 0) {
            int readInt2 = readInt();
            if (readInt < 0) {
                return null;
            }
            if (readInt2 == 1) {
                while (readInt > 0) {
                    arrayList.add(readVersionedParcelable());
                    readInt--;
                }
            } else if (readInt2 == 2) {
                while (readInt > 0) {
                    arrayList.add(readParcelable());
                    readInt--;
                }
            } else if (readInt2 == 3) {
                while (readInt > 0) {
                    String readString = readString();
                    if (readString == null) {
                        serializable = null;
                    } else {
                        try {
                            serializable = (Serializable) new ObjectInputStream(this, new ByteArrayInputStream(readByteArray())) { // from class: androidx.versionedparcelable.VersionedParcel.1
                                @Override // java.io.ObjectInputStream
                                public Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
                                    Class<?> cls = Class.forName(objectStreamClass.getName(), false, AnonymousClass1.class.getClassLoader());
                                    if (cls != null) {
                                        return cls;
                                    }
                                    return super.resolveClass(objectStreamClass);
                                }
                            }.readObject();
                        } catch (IOException e) {
                            throw new RuntimeException(PathParser$$ExternalSyntheticOutline0.m("Unable to read Serializable object (name = ", readString, ")"), e);
                        } catch (ClassNotFoundException e2) {
                            throw new RuntimeException(PathParser$$ExternalSyntheticOutline0.m("Unable to read Serializable object (name = ", readString, ")"), e2);
                        }
                    }
                    arrayList.add(serializable);
                    readInt--;
                }
            } else if (readInt2 == 4) {
                while (readInt > 0) {
                    arrayList.add(readString());
                    readInt--;
                }
            } else if (readInt2 == 5) {
                while (readInt > 0) {
                    arrayList.add(readStrongBinder());
                    readInt--;
                }
            }
        }
        return (T[]) arrayList.toArray(tArr);
    }

    public abstract boolean readBoolean();

    public abstract Bundle readBundle();

    public abstract byte[] readByteArray();

    public abstract CharSequence readCharSequence();

    public abstract boolean readField(int i);

    public abstract int readInt();

    public int readInt(int i, int i2) {
        if (!readField(i2)) {
            return i;
        }
        return readInt();
    }

    public abstract long readLong();

    public abstract <T extends Parcelable> T readParcelable();

    public <T extends Parcelable> T readParcelable(T t, int i) {
        if (!readField(i)) {
            return t;
        }
        return (T) readParcelable();
    }

    public abstract String readString();

    public String readString(String str, int i) {
        if (!readField(i)) {
            return str;
        }
        return readString();
    }

    public abstract IBinder readStrongBinder();

    public <T extends VersionedParcelable> T readVersionedParcelable(T t, int i) {
        if (!readField(i)) {
            return t;
        }
        return (T) readVersionedParcelable();
    }

    public abstract void setOutputField(int i);

    public <T> void writeArray(T[] tArr, int i) {
        int i2;
        setOutputField(i);
        if (tArr == null) {
            writeInt(-1);
            return;
        }
        int length = tArr.length;
        writeInt(length);
        if (length > 0) {
            int i3 = 0;
            T t = tArr[0];
            if (t instanceof String) {
                i2 = 4;
            } else if (t instanceof Parcelable) {
                i2 = 2;
            } else if (t instanceof VersionedParcelable) {
                i2 = 1;
            } else if (t instanceof Serializable) {
                i2 = 3;
            } else if (t instanceof IBinder) {
                i2 = 5;
            } else if (t instanceof Integer) {
                i2 = 7;
            } else if (t instanceof Float) {
                i2 = 8;
            } else {
                throw new IllegalArgumentException(t.getClass().getName() + " cannot be VersionedParcelled");
            }
            writeInt(i2);
            if (i2 == 1) {
                while (i3 < length) {
                    writeVersionedParcelable((VersionedParcelable) tArr[i3]);
                    i3++;
                }
            } else if (i2 == 2) {
                while (i3 < length) {
                    writeParcelable((Parcelable) tArr[i3]);
                    i3++;
                }
            } else if (i2 == 3) {
                while (i3 < length) {
                    Serializable serializable = (Serializable) tArr[i3];
                    if (serializable == null) {
                        writeString(null);
                    } else {
                        String name = serializable.getClass().getName();
                        writeString(name);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        try {
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                            objectOutputStream.writeObject(serializable);
                            objectOutputStream.close();
                            writeByteArray(byteArrayOutputStream.toByteArray());
                        } catch (IOException e) {
                            throw new RuntimeException(PathParser$$ExternalSyntheticOutline0.m("VersionedParcelable encountered IOException writing serializable object (name = ", name, ")"), e);
                        }
                    }
                    i3++;
                }
            } else if (i2 == 4) {
                while (i3 < length) {
                    writeString((String) tArr[i3]);
                    i3++;
                }
            } else if (i2 == 5) {
                while (i3 < length) {
                    writeStrongBinder((IBinder) tArr[i3]);
                    i3++;
                }
            }
        }
    }

    public abstract void writeBoolean(boolean z);

    public abstract void writeBundle(Bundle bundle);

    public abstract void writeByteArray(byte[] bArr);

    public abstract void writeCharSequence(CharSequence charSequence);

    public abstract void writeInt(int i);

    public abstract void writeLong(long j);

    public abstract void writeParcelable(Parcelable parcelable);

    public abstract void writeString(String str);

    public abstract void writeStrongBinder(IBinder iBinder);

    public void writeVersionedParcelable(VersionedParcelable versionedParcelable) {
        if (versionedParcelable == null) {
            writeString(null);
            return;
        }
        try {
            writeString(findParcelClass(versionedParcelable.getClass()).getName());
            VersionedParcel createSubParcel = createSubParcel();
            try {
                getWriteMethod(versionedParcelable.getClass()).invoke(null, versionedParcelable, createSubParcel);
                createSubParcel.closeField();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            } catch (NoSuchMethodException e3) {
                throw new RuntimeException(e3);
            } catch (InvocationTargetException e4) {
                Throwable cause = e4.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                } else if (cause instanceof Error) {
                    throw ((Error) cause);
                } else {
                    throw new RuntimeException(e4);
                }
            }
        } catch (ClassNotFoundException e5) {
            throw new RuntimeException(versionedParcelable.getClass().getSimpleName() + " does not have a Parcelizer", e5);
        }
    }

    public <T extends VersionedParcelable> T readVersionedParcelable() {
        String readString = readString();
        if (readString == null) {
            return null;
        }
        try {
            return (T) ((VersionedParcelable) getReadMethod(readString).invoke(null, createSubParcel()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException(e3);
        } catch (InvocationTargetException e4) {
            Throwable cause = e4.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            } else if (cause instanceof Error) {
                throw ((Error) cause);
            } else {
                throw new RuntimeException(e4);
            }
        }
    }
}
