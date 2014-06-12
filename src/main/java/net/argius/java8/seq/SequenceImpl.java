package net.argius.java8.seq;

import java.util.*;
import java.util.function.*;

final class SequenceImpl<T> implements Sequence<T> {

    final int size;
    final private T[] values;

    @SafeVarargs
    SequenceImpl(T... a) {
        this.size = a.length;
        this.values = a;
    }

    SequenceImpl(Collection<T> a) {
        this.size = a.size();
        @SuppressWarnings("unchecked")
        final T[] array = (T[])a.toArray();
        this.values = array;
    }

    // accessors

    @Override
    public T at(int index) {
        return values[index];
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorImpl();
    }

    // maps

    @Override
    public <R> Sequence<R> map(Function<? super T, ? extends R> mapper) {
        @SuppressWarnings("unchecked")
        R[] a = (R[])new Object[size];
        for (int i = 0, n = size; i < n; i++)
            a[i] = mapper.apply(values[i]);
        return new SequenceImpl<>(a);
    }

    @Override
    public int size() {
        return size;
    }

    // filters

    @Override
    public Sequence<T> filter(Predicate<? super T> predicate) {
        @SuppressWarnings("unchecked")
        T[] a = (T[])new Object[size];
        int p = 0;
        for (int i = 0, n = size; i < n; i++)
            if (predicate.test(values[i]))
                a[p++] = values[i];
        return new SequenceImpl<>(Arrays.copyOf(a, p));
    }

    // converters

    @Override
    public Set<T> toSet() {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, values);
        return set;
    }

    @Override
    public String[] toStringArray() {
        String[] a = new String[size];
        for (int i = 0; i < size; i++)
            a[i] = Objects.toString(values[i], null);
        return a;
    }

    @Override
    public <R> Map<T, R> toMapWithKey(Function<? super T, ? extends R> mapper) {
        Map<T, R> m = new HashMap<>();
        for (int i = 0; i < size; i++)
            m.put(values[i], mapper.apply(values[i]));
        return m;
    }

    @Override
    public <R> Map<R, T> toMapWithValue(Function<? super T, ? extends R> mapper) {
        Map<R, T> m = new HashMap<>();
        for (int i = 0; i < size; i++)
            m.put(mapper.apply(values[i]), values[i]);
        return m;
    }

    @Override
    public T[] toArray() {
        return Arrays.copyOf(values, size);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + size;
        result = prime * result + Arrays.hashCode(values);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SequenceImpl<?> other = (SequenceImpl<?>)obj;
        if (size != other.size)
            return false;
        if (!Arrays.equals(values, other.values))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    final class IteratorImpl implements Iterator<T> {

        private int p;

        IteratorImpl() {
            this.p = -1;
        }

        @Override
        public boolean hasNext() {
            if (p + 1 < size) {
                ++p;
                return true;
            }
            else
                return false;
        }

        @Override
        public T next() {
            return at(p);
        }

    }

}
