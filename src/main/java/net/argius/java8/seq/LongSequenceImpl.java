package net.argius.java8.seq;

import java.util.*;

final class LongSequenceImpl implements LongSequence {

    final int size;
    final long[] values;

    LongSequenceImpl(long... a) {
        this.size = a.length;
        this.values = a;
    }

    static LongSequence seq(long... a) {
        return new LongSequenceImpl(a);
    }

    @Override
    public long at(int index) {
        return values[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public OptionalLong max() {
        if (size == 0)
            return OptionalLong.empty();
        if (size == 1)
            return OptionalLong.of(values[0]);
        long max = values[0];
        for (int i = 1, n = values.length; i < n; i++)
            if (values[i] > max)
                max = values[i];
        return OptionalLong.of(max);
    }

    @Override
    public OptionalLong min() {
        if (size == 0)
            return OptionalLong.empty();
        if (size == 1)
            return OptionalLong.of(values[0]);
        long min = values[0];
        for (int i = 1, n = values.length; i < n; i++)
            if (values[i] < min)
                min = values[i];
        return OptionalLong.of(min);
    }

    @Override
    public long sum() {
        if (size == 0)
            return 0;
        if (size == 1)
            return values[0];
        long sum = values[0];
        for (int i = 1, n = values.length; i < n; i++)
            sum += values[i];
        return sum;
    }

    @Override
    public LongSequence sortWith(int fromIndex, int toIndex, LongComparator cmp) {
        long[] a = Arrays.copyOf(values, size);
        sortWith0(a, fromIndex, toIndex, cmp);
        return new LongSequenceImpl(a);
    }

    static void sortWith0(long[] a, int fromIndex, int toIndex, LongComparator cmp) {
        final int length = toIndex - fromIndex + 1;
        if (length < 2)
            return;
        if (length == 2) {
            if (cmp.gt(a[fromIndex], a[toIndex])) {
                long x = a[fromIndex];
                a[fromIndex] = a[toIndex];
                a[toIndex] = x;
            }
            return;
        }
        // FIXME bad performance
        final long pivot = a[fromIndex];
        int p1 = 0;
        int p2 = 0;
        long[] a1 = new long[length];
        long[] a2 = new long[length];
        for (int i = fromIndex + 1; i <= toIndex; i++) {
            final long v = a[i];
            if (cmp.lt(v, pivot))
                a1[p1++] = v;
            else
                a2[p2++] = v;
        }
        int p = fromIndex;
        for (int i = 0; i < p1; i++)
            a[p++] = a1[i];
        a[p++] = pivot;
        for (int i = 0; i < p2; i++)
            a[p++] = a2[i];
        if (p1 > 0)
            sortWith0(a, fromIndex, fromIndex + p1 - 1, cmp);
        if (p2 > 0)
            sortWith0(a, fromIndex + p1 + 1, toIndex, cmp);
    }

    @Override
    public long[] toArray() {
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
        LongSequenceImpl other = (LongSequenceImpl)obj;
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

}
