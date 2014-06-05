package net.argius.java8.seq;

import java.util.*;
import java.util.stream.*;

final class DoubleSequenceImpl implements DoubleSequence {

    final int size;
    final double[] values;

    DoubleSequenceImpl(double... a) {
        this.size = a.length;
        this.values = a;
    }

    @Override
    public double at(int index) {
        return values[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public OptionalDouble max() {
        final int n = size;
        if (n == 0)
            return OptionalDouble.empty();
        if (n == 1)
            return OptionalDouble.of(values[0]);
        double max = values[0];
        for (int i = 1; i < n; i++)
            if (values[i] > max)
                max = values[i];
        return OptionalDouble.of(max);
    }

    @Override
    public OptionalDouble min() {
        final int n = size;
        if (n == 0)
            return OptionalDouble.empty();
        if (n == 1)
            return OptionalDouble.of(values[0]);
        double min = values[0];
        for (int i = 1; i < n; i++)
            if (values[i] < min)
                min = values[i];
        return OptionalDouble.of(min);
    }

    @Override
    public double sum() {
        final int n = size;
        if (n == 0)
            return 0;
        if (n == 1)
            return values[0];
        double sum = values[0];
        for (int i = 1; i < n; i++)
            sum += values[i];
        return sum;
    }

    @Override
    public double product() {
        final int n = size;
        if (n == 0)
            return 0;
        if (n == 1)
            return values[0];
        double product = values[0];
        for (int i = 1; i < n; i++)
            product *= values[i];
        return product;
    }

    @Override
    public DoubleSequence sortWith(int fromIndex, int toIndex, DoubleComparator cmp) {
        double[] a = Arrays.copyOf(values, size);
        sortWith0(a, fromIndex, toIndex, cmp);
        return new DoubleSequenceImpl(a);
    }

    static void sortWith0(double[] a, int fromIndex, int toIndex, DoubleComparator cmp) {
        final int length = toIndex - fromIndex + 1;
        if (length < 2)
            return;
        if (length == 2) {
            if (cmp.gt(a[fromIndex], a[toIndex])) {
                double x = a[fromIndex];
                a[fromIndex] = a[toIndex];
                a[toIndex] = x;
            }
            return;
        }
        // FIXME bad performance
        final double pivot = a[fromIndex];
        int p1 = 0;
        int p2 = 0;
        double[] a1 = new double[length];
        double[] a2 = new double[length];
        for (int i = fromIndex + 1; i <= toIndex; i++) {
            final double v = a[i];
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
    public DoubleStream stream() {
        return DoubleStream.of(values);
    }

    @Override
    public double[] toArray() {
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
        DoubleSequenceImpl other = (DoubleSequenceImpl)obj;
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
