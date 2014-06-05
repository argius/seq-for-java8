package net.argius.java8.seq;

import java.util.*;
import java.util.stream.*;

final class IntSequenceImpl implements IntSequence {

    final int size;
    final int[] values;

    IntSequenceImpl(int... a) {
        this.size = a.length;
        this.values = a;
    }

    @Override
    public int at(int index) {
        return values[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public OptionalInt max() {
        final int n = size;
        if (n == 0)
            return OptionalInt.empty();
        if (n == 1)
            return OptionalInt.of(values[0]);
        int max = values[0];
        for (int i = 1; i < n; i++)
            if (values[i] > max)
                max = values[i];
        return OptionalInt.of(max);
    }

    @Override
    public OptionalInt min() {
        final int n = size;
        if (n == 0)
            return OptionalInt.empty();
        if (n == 1)
            return OptionalInt.of(values[0]);
        int min = values[0];
        for (int i = 1; i < n; i++)
            if (values[i] < min)
                min = values[i];
        return OptionalInt.of(min);
    }

    @Override
    public int sum() {
        final int n = size;
        if (n == 0)
            return 0;
        if (n == 1)
            return values[0];
        int sum = values[0];
        for (int i = 1; i < n; i++)
            sum += values[i];
        return sum;
    }

    @Override
    public IntSequence sortWith(int fromIndex, int toIndex, IntComparator cmp) {
        int[] a = Arrays.copyOf(values, size);
        sortWith0(a, fromIndex, toIndex, cmp);
        return new IntSequenceImpl(a);
    }

    static void sortWith0(int[] a, int fromIndex, int toIndex, IntComparator cmp) {
        final int length = toIndex - fromIndex + 1;
        if (length < 2)
            return;
        if (length == 2) {
            if (cmp.gt(a[fromIndex], a[toIndex])) {
                int x = a[fromIndex];
                a[fromIndex] = a[toIndex];
                a[toIndex] = x;
            }
            return;
        }
        // FIXME bad performance
        final int pivot = a[fromIndex];
        int p1 = 0;
        int p2 = 0;
        int[] a1 = new int[length];
        int[] a2 = new int[length];
        for (int i = fromIndex + 1; i <= toIndex; i++) {
            final int v = a[i];
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
    public IntStream stream() {
        return IntStream.of(values);
    }

    @Override
    public int[] toArray() {
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
        IntSequenceImpl other = (IntSequenceImpl) obj;
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
