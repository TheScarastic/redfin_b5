package androidx.constraintlayout.solver;

import android.util.SparseArray;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;
import androidx.core.util.Pools$Pool;
import androidx.core.util.Pools$SimplePool;
import java.util.ArrayList;
import java.util.HashSet;
/* loaded from: classes.dex */
public class Cache {
    public Pools$SimplePool arrayRowPool;
    public Pools$SimplePool goalVariablePool;
    public Object mIndexedVariables;
    public Pools$SimplePool solverVariablePool;

    /* JADX WARN: Type inference failed for: r2v1, types: [androidx.core.util.Pools$SimplePool, androidx.constraintlayout.solver.Pools$SimplePool] */
    /* JADX WARN: Type inference failed for: r2v2, types: [androidx.constraintlayout.solver.Pools$SimplePool, androidx.collection.SimpleArrayMap] */
    /* JADX WARN: Type inference failed for: r2v3, types: [androidx.constraintlayout.solver.Pools$SimplePool, java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r2v5, types: [androidx.constraintlayout.solver.Pools$SimplePool, androidx.collection.ArrayMap] */
    /* JADX WARN: Type inference failed for: r2v6, types: [android.util.SparseArray, androidx.constraintlayout.solver.Pools$SimplePool] */
    /* JADX WARN: Type inference failed for: r2v7, types: [androidx.collection.LongSparseArray, androidx.constraintlayout.solver.Pools$SimplePool] */
    public Cache(int i) {
        if (i == 1) {
            this.arrayRowPool = new Pools$SimplePool(10);
            this.solverVariablePool = new SimpleArrayMap();
            this.goalVariablePool = new ArrayList();
            this.mIndexedVariables = new HashSet();
        } else if (i != 2) {
            this.arrayRowPool = new Pools$SimplePool(256);
            this.solverVariablePool = new Pools$SimplePool(256);
            this.mIndexedVariables = new SolverVariable[32];
            this.goalVariablePool = new Pools$SimplePool(64);
        } else {
            this.arrayRowPool = new ArrayMap();
            this.solverVariablePool = new SparseArray();
            this.goalVariablePool = new LongSparseArray();
            this.mIndexedVariables = new ArrayMap();
        }
    }

    public void addNode(T t) {
        if (!(((SimpleArrayMap) this.solverVariablePool).indexOfKey(t) >= 0)) {
            ((SimpleArrayMap) this.solverVariablePool).put(t, null);
        }
    }

    public void dfs(T t, ArrayList<T> arrayList, HashSet<T> hashSet) {
        if (!arrayList.contains(t)) {
            if (!hashSet.contains(t)) {
                hashSet.add(t);
                ArrayList arrayList2 = (ArrayList) ((SimpleArrayMap) this.solverVariablePool).getOrDefault(t, null);
                if (arrayList2 != null) {
                    int size = arrayList2.size();
                    for (int i = 0; i < size; i++) {
                        dfs(arrayList2.get(i), arrayList, hashSet);
                    }
                }
                hashSet.remove(t);
                arrayList.add(t);
                return;
            }
            throw new RuntimeException("This graph contains cyclic dependencies");
        }
    }

    public ArrayList<T> getEmptyList() {
        ArrayList<T> arrayList = (ArrayList) ((Pools$Pool) this.arrayRowPool).acquire();
        return arrayList == 0 ? new ArrayList<>() : arrayList;
    }

    public void poolList(ArrayList<T> arrayList) {
        arrayList.clear();
        ((Pools$Pool) this.arrayRowPool).release(arrayList);
    }
}
