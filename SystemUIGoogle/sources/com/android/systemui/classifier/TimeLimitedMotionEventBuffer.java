package com.android.systemui.classifier;

import android.view.MotionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
/* loaded from: classes.dex */
public class TimeLimitedMotionEventBuffer implements List<MotionEvent> {
    private final long mMaxAgeMs;
    private final LinkedList<MotionEvent> mMotionEvents = new LinkedList<>();

    public TimeLimitedMotionEventBuffer(long j) {
        this.mMaxAgeMs = j;
    }

    private void ejectOldEvents() {
        if (!this.mMotionEvents.isEmpty()) {
            ListIterator<MotionEvent> listIterator = listIterator();
            long eventTime = this.mMotionEvents.getLast().getEventTime();
            while (listIterator.hasNext()) {
                MotionEvent next = listIterator.next();
                if (eventTime - next.getEventTime() > this.mMaxAgeMs) {
                    listIterator.remove();
                    next.recycle();
                }
            }
        }
    }

    public void add(int i, MotionEvent motionEvent) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public MotionEvent remove(int i) {
        return this.mMotionEvents.remove(i);
    }

    @Override // java.util.List
    public int indexOf(Object obj) {
        return this.mMotionEvents.indexOf(obj);
    }

    @Override // java.util.List
    public int lastIndexOf(Object obj) {
        return this.mMotionEvents.lastIndexOf(obj);
    }

    @Override // java.util.List, java.util.Collection
    public int size() {
        return this.mMotionEvents.size();
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        return this.mMotionEvents.isEmpty();
    }

    @Override // java.util.List, java.util.Collection
    public boolean contains(Object obj) {
        return this.mMotionEvents.contains(obj);
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Iterator<MotionEvent> iterator() {
        return this.mMotionEvents.iterator();
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray() {
        return this.mMotionEvents.toArray();
    }

    @Override // java.util.List, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) this.mMotionEvents.toArray(tArr);
    }

    public boolean add(MotionEvent motionEvent) {
        boolean add = this.mMotionEvents.add(motionEvent);
        ejectOldEvents();
        return add;
    }

    @Override // java.util.List, java.util.Collection
    public boolean remove(Object obj) {
        return this.mMotionEvents.remove(obj);
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        return this.mMotionEvents.containsAll(collection);
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends MotionEvent> collection) {
        boolean addAll = this.mMotionEvents.addAll(collection);
        ejectOldEvents();
        return addAll;
    }

    @Override // java.util.List
    public boolean addAll(int i, Collection<? extends MotionEvent> collection) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        return this.mMotionEvents.removeAll(collection);
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        return this.mMotionEvents.retainAll(collection);
    }

    @Override // java.util.List, java.util.Collection
    public void clear() {
        this.mMotionEvents.clear();
    }

    @Override // java.util.List, java.util.Collection, java.lang.Object
    public boolean equals(Object obj) {
        return this.mMotionEvents.equals(obj);
    }

    @Override // java.util.List, java.util.Collection, java.lang.Object
    public int hashCode() {
        return this.mMotionEvents.hashCode();
    }

    @Override // java.util.List
    public MotionEvent get(int i) {
        return this.mMotionEvents.get(i);
    }

    public MotionEvent set(int i, MotionEvent motionEvent) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public ListIterator<MotionEvent> listIterator() {
        return new Iter(0);
    }

    @Override // java.util.List
    public ListIterator<MotionEvent> listIterator(int i) {
        return new Iter(i);
    }

    @Override // java.util.List
    public List<MotionEvent> subList(int i, int i2) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class Iter implements ListIterator<MotionEvent> {
        private final ListIterator<MotionEvent> mIterator;

        Iter(int i) {
            this.mIterator = TimeLimitedMotionEventBuffer.this.mMotionEvents.listIterator(i);
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.mIterator.hasNext();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public MotionEvent next() {
            return this.mIterator.next();
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.mIterator.hasPrevious();
        }

        @Override // java.util.ListIterator
        public MotionEvent previous() {
            return this.mIterator.previous();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.mIterator.nextIndex();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.mIterator.previousIndex();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            this.mIterator.remove();
        }

        public void set(MotionEvent motionEvent) {
            throw new UnsupportedOperationException();
        }

        public void add(MotionEvent motionEvent) {
            throw new UnsupportedOperationException();
        }
    }
}
