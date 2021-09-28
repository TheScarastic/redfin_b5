package androidx.lifecycle;

import android.annotation.SuppressLint;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.internal.FastSafeIterableMap;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.core.graphics.PathParser$$ExternalSyntheticOutline0;
import androidx.lifecycle.Lifecycle;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class LifecycleRegistry extends Lifecycle {
    public final boolean mEnforceMainThread;
    public final WeakReference<LifecycleOwner> mLifecycleOwner;
    public FastSafeIterableMap<LifecycleObserver, ObserverWithState> mObserverMap = new FastSafeIterableMap<>();
    public int mAddingObserverCounter = 0;
    public boolean mHandlingEvent = false;
    public boolean mNewEventOccurred = false;
    public ArrayList<Lifecycle.State> mParentStates = new ArrayList<>();
    public Lifecycle.State mState = Lifecycle.State.INITIALIZED;

    /* loaded from: classes.dex */
    public static class ObserverWithState {
        public LifecycleEventObserver mLifecycleObserver;
        public Lifecycle.State mState;

        public ObserverWithState(LifecycleObserver lifecycleObserver, Lifecycle.State state) {
            LifecycleEventObserver lifecycleEventObserver;
            Map<Class<?>, Integer> map = Lifecycling.sCallbackCache;
            boolean z = lifecycleObserver instanceof LifecycleEventObserver;
            boolean z2 = lifecycleObserver instanceof FullLifecycleObserver;
            if (z && z2) {
                lifecycleEventObserver = new FullLifecycleObserverAdapter((FullLifecycleObserver) lifecycleObserver, (LifecycleEventObserver) lifecycleObserver);
            } else if (z2) {
                lifecycleEventObserver = new FullLifecycleObserverAdapter((FullLifecycleObserver) lifecycleObserver, null);
            } else if (z) {
                lifecycleEventObserver = (LifecycleEventObserver) lifecycleObserver;
            } else {
                Class<?> cls = lifecycleObserver.getClass();
                if (Lifecycling.getObserverConstructorType(cls) == 2) {
                    List list = (List) ((HashMap) Lifecycling.sClassToAdapters).get(cls);
                    if (list.size() == 1) {
                        lifecycleEventObserver = new SingleGeneratedAdapterObserver(Lifecycling.createGeneratedAdapter((Constructor) list.get(0), lifecycleObserver));
                    } else {
                        GeneratedAdapter[] generatedAdapterArr = new GeneratedAdapter[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            generatedAdapterArr[i] = Lifecycling.createGeneratedAdapter((Constructor) list.get(i), lifecycleObserver);
                        }
                        lifecycleEventObserver = new CompositeGeneratedAdaptersObserver(generatedAdapterArr);
                    }
                } else {
                    lifecycleEventObserver = new ReflectiveGenericLifecycleObserver(lifecycleObserver);
                }
            }
            this.mLifecycleObserver = lifecycleEventObserver;
            this.mState = state;
        }

        public void dispatchEvent(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            Lifecycle.State targetState = event.getTargetState();
            this.mState = LifecycleRegistry.min(this.mState, targetState);
            this.mLifecycleObserver.onStateChanged(lifecycleOwner, event);
            this.mState = targetState;
        }
    }

    public LifecycleRegistry(LifecycleOwner lifecycleOwner, boolean z) {
        this.mLifecycleOwner = new WeakReference<>(lifecycleOwner);
        this.mEnforceMainThread = z;
    }

    public static LifecycleRegistry createUnsafe(LifecycleOwner lifecycleOwner) {
        return new LifecycleRegistry(lifecycleOwner, false);
    }

    public static Lifecycle.State min(Lifecycle.State state, Lifecycle.State state2) {
        return (state2 == null || state2.compareTo(state) >= 0) ? state : state2;
    }

    @Override // androidx.lifecycle.Lifecycle
    public void addObserver(LifecycleObserver lifecycleObserver) {
        LifecycleOwner lifecycleOwner;
        enforceMainThreadIfNeeded("addObserver");
        Lifecycle.State state = this.mState;
        Lifecycle.State state2 = Lifecycle.State.DESTROYED;
        if (state != state2) {
            state2 = Lifecycle.State.INITIALIZED;
        }
        ObserverWithState observerWithState = new ObserverWithState(lifecycleObserver, state2);
        if (this.mObserverMap.putIfAbsent(lifecycleObserver, observerWithState) == null && (lifecycleOwner = this.mLifecycleOwner.get()) != null) {
            boolean z = this.mAddingObserverCounter != 0 || this.mHandlingEvent;
            Lifecycle.State calculateTargetState = calculateTargetState(lifecycleObserver);
            this.mAddingObserverCounter++;
            while (observerWithState.mState.compareTo(calculateTargetState) < 0 && this.mObserverMap.mHashMap.containsKey(lifecycleObserver)) {
                this.mParentStates.add(observerWithState.mState);
                Lifecycle.Event upFrom = Lifecycle.Event.upFrom(observerWithState.mState);
                if (upFrom != null) {
                    observerWithState.dispatchEvent(lifecycleOwner, upFrom);
                    popParentState();
                    calculateTargetState = calculateTargetState(lifecycleObserver);
                } else {
                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("no event up from ");
                    m.append(observerWithState.mState);
                    throw new IllegalStateException(m.toString());
                }
            }
            if (!z) {
                sync();
            }
            this.mAddingObserverCounter--;
        }
    }

    public final Lifecycle.State calculateTargetState(LifecycleObserver lifecycleObserver) {
        FastSafeIterableMap<LifecycleObserver, ObserverWithState> fastSafeIterableMap = this.mObserverMap;
        Lifecycle.State state = null;
        SafeIterableMap.Entry<LifecycleObserver, ObserverWithState> entry = fastSafeIterableMap.mHashMap.containsKey(lifecycleObserver) ? fastSafeIterableMap.mHashMap.get(lifecycleObserver).mPrevious : null;
        Lifecycle.State state2 = entry != null ? entry.mValue.mState : null;
        if (!this.mParentStates.isEmpty()) {
            ArrayList<Lifecycle.State> arrayList = this.mParentStates;
            state = arrayList.get(arrayList.size() - 1);
        }
        return min(min(this.mState, state2), state);
    }

    @SuppressLint({"RestrictedApi"})
    public final void enforceMainThreadIfNeeded(String str) {
        if (this.mEnforceMainThread && !ArchTaskExecutor.getInstance().isMainThread()) {
            throw new IllegalStateException(PathParser$$ExternalSyntheticOutline0.m("Method ", str, " must be called on the main thread"));
        }
    }

    public void handleLifecycleEvent(Lifecycle.Event event) {
        enforceMainThreadIfNeeded("handleLifecycleEvent");
        moveToState(event.getTargetState());
    }

    public final void moveToState(Lifecycle.State state) {
        if (this.mState != state) {
            this.mState = state;
            if (this.mHandlingEvent || this.mAddingObserverCounter != 0) {
                this.mNewEventOccurred = true;
                return;
            }
            this.mHandlingEvent = true;
            sync();
            this.mHandlingEvent = false;
        }
    }

    public final void popParentState() {
        ArrayList<Lifecycle.State> arrayList = this.mParentStates;
        arrayList.remove(arrayList.size() - 1);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r4v4, resolved type: androidx.arch.core.internal.FastSafeIterableMap<androidx.lifecycle.LifecycleObserver, androidx.lifecycle.LifecycleRegistry$ObserverWithState> */
    /* JADX DEBUG: Multi-variable search result rejected for r4v13, resolved type: androidx.arch.core.internal.FastSafeIterableMap<androidx.lifecycle.LifecycleObserver, androidx.lifecycle.LifecycleRegistry$ObserverWithState> */
    /* JADX WARN: Multi-variable type inference failed */
    public final void sync() {
        Lifecycle.Event event;
        Lifecycle.State state;
        LifecycleOwner lifecycleOwner = this.mLifecycleOwner.get();
        if (lifecycleOwner != null) {
            while (true) {
                FastSafeIterableMap<LifecycleObserver, ObserverWithState> fastSafeIterableMap = this.mObserverMap;
                boolean z = true;
                if (!(fastSafeIterableMap.mSize == 0 || (fastSafeIterableMap.mStart.mValue.mState == (state = fastSafeIterableMap.mEnd.mValue.mState) && this.mState == state))) {
                    z = false;
                }
                if (!z) {
                    this.mNewEventOccurred = false;
                    if (this.mState.compareTo(fastSafeIterableMap.mStart.mValue.mState) < 0) {
                        FastSafeIterableMap<LifecycleObserver, ObserverWithState> fastSafeIterableMap2 = this.mObserverMap;
                        SafeIterableMap.DescendingIterator descendingIterator = new SafeIterableMap.DescendingIterator(fastSafeIterableMap2.mEnd, fastSafeIterableMap2.mStart);
                        fastSafeIterableMap2.mIterators.put(descendingIterator, Boolean.FALSE);
                        while (descendingIterator.hasNext() && !this.mNewEventOccurred) {
                            Map.Entry entry = (Map.Entry) descendingIterator.next();
                            ObserverWithState observerWithState = (ObserverWithState) entry.getValue();
                            while (observerWithState.mState.compareTo(this.mState) > 0 && !this.mNewEventOccurred && this.mObserverMap.contains(entry.getKey())) {
                                int ordinal = observerWithState.mState.ordinal();
                                if (ordinal == 2) {
                                    event = Lifecycle.Event.ON_DESTROY;
                                } else if (ordinal == 3) {
                                    event = Lifecycle.Event.ON_STOP;
                                } else if (ordinal != 4) {
                                    event = null;
                                } else {
                                    event = Lifecycle.Event.ON_PAUSE;
                                }
                                if (event != null) {
                                    this.mParentStates.add(event.getTargetState());
                                    observerWithState.dispatchEvent(lifecycleOwner, event);
                                    popParentState();
                                } else {
                                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("no event down from ");
                                    m.append(observerWithState.mState);
                                    throw new IllegalStateException(m.toString());
                                }
                            }
                        }
                    }
                    SafeIterableMap.Entry<LifecycleObserver, ObserverWithState> entry2 = this.mObserverMap.mEnd;
                    if (!this.mNewEventOccurred && entry2 != null && this.mState.compareTo(entry2.mValue.mState) > 0) {
                        SafeIterableMap<LifecycleObserver, ObserverWithState>.IteratorWithAdditions iteratorWithAdditions = this.mObserverMap.iteratorWithAdditions();
                        while (iteratorWithAdditions.hasNext() && !this.mNewEventOccurred) {
                            Map.Entry entry3 = (Map.Entry) iteratorWithAdditions.next();
                            ObserverWithState observerWithState2 = (ObserverWithState) entry3.getValue();
                            while (observerWithState2.mState.compareTo(this.mState) < 0 && !this.mNewEventOccurred && this.mObserverMap.contains(entry3.getKey())) {
                                this.mParentStates.add(observerWithState2.mState);
                                Lifecycle.Event upFrom = Lifecycle.Event.upFrom(observerWithState2.mState);
                                if (upFrom != null) {
                                    observerWithState2.dispatchEvent(lifecycleOwner, upFrom);
                                    popParentState();
                                } else {
                                    StringBuilder m2 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("no event up from ");
                                    m2.append(observerWithState2.mState);
                                    throw new IllegalStateException(m2.toString());
                                }
                            }
                        }
                    }
                } else {
                    this.mNewEventOccurred = false;
                    return;
                }
            }
        } else {
            throw new IllegalStateException("LifecycleOwner of this LifecycleRegistry is alreadygarbage collected. It is too late to change lifecycle state.");
        }
    }
}
