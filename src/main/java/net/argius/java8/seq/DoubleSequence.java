package net.argius.java8.seq;

import static net.argius.java8.seq.DoubleSequenceFactory.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface DoubleSequence {

@SafeVarargs    static DoubleSequence of(double... a) {
        return createWithCopy(a);
    }

    static DoubleSequence of(Collection<Double> collection) {
        return Sequence.of(collection).mapToDouble(Double::doubleValue);
    }

    static DoubleSequence of(DoubleStream stream) {
        return createWithoutCopy(stream.toArray());
    }

@SafeVarargs    static DoubleSequence seq(double... a) {
        return createWithCopy(a);
    }

    static DoubleSequence seq(Collection<Double> collection) {
        return Sequence.of(collection).mapToDouble(Double::doubleValue);
    }

    static DoubleSequence seq(DoubleStream stream) {
        return createWithoutCopy(stream.toArray());
    }

    static DoubleSequence empty() {
        return DoubleSequenceFactory.EMPTY;
    }

    int size();

    double at(int index);

    default DoubleSequence filter(DoublePredicate predicate) {
        final int n = size();
        int p = 0;
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            double x = at(i);
            if (predicate.test(x))
                a[p++] = x;
        }
        return createWithoutCopy(Arrays.copyOf(a, p));
    }

    default OptionalDouble head() {
        return (size() == 0) ? OptionalDouble.empty() : OptionalDouble.of(at(0));
    }

    default DoubleSequence tail() {
        return subSequence(1, Integer.MAX_VALUE);
    }

    default DoubleSequence subSequence(int from, int to) {
        final int n = size() - 1;
        final int to0 = (to < n) ? to : n;
        return createWithoutCopy(Arrays.copyOfRange(toArray(), from, to0 + 1));
    }

    OptionalDouble max();

    OptionalDouble min();

    default DoubleSequence map(DoubleUnaryOperator mapper) {
        final int n = size();
        double[] values = toArray();
        double[] a = new double[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsDouble(values[i]);
        return createWithoutCopy(a);
    }

    default <R> Sequence<R> mapToObj(DoubleFunction<R> mapper) {
        final int n = size();
        List<R> a = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            a.add(mapper.apply(at(i)));
        return Sequence.of(a);
    }

    default double reduce(double identity, DoubleBinaryOperator op) {
        // TODO check
        double result = identity;
        for (double element : toArray())
            result = op.applyAsDouble(result, element);
        return result;
    }

    default double fold(double value, DoubleBinaryOperator f) {
        switch (size()) {
        case 0:
            return value;
        case 1:
            return f.applyAsDouble(value, at(0));
        default:
            return f.applyAsDouble(value, tail().fold(at(0), f));
        }
    }

    default void forEach(DoubleConsumer action) {
        final int n = size();
        double[] values = toArray();
        for (int i = 0; i < n; i++)
            action.accept(values[i]);
    }

    double sum();

    default double average() {
        return sum() / size();
    }

    default DoubleSequence sort() {
        double[] a = toArray();
        Arrays.sort(a);
        return createWithoutCopy(a);
    }

    default DoubleSequence sortWith(DoubleComparator cmp) {
        return sortWith(0, size() - 1, cmp);
    }

    DoubleSequence sortWith(int fromIndex, int toIndex, DoubleComparator cmp);

    default DoubleSequence reverse() {
        double[] a = toArray();
        final int size = a.length;
        final int n = a.length / 2;
        for (int i = 0, j = size - 1; i < n; i++, j--) {
            double x = a[j];
            a[j] = a[i];
            a[i] = x;
        }
        return createWithoutCopy(a);
    }

    default DoubleSequence distinct() {
        return createWithoutCopy(stream().distinct());
    }

    default DoubleSequence concat(DoubleSequence first, DoubleSequence... rest) {
        final int selfLength = size();
        final int firstLength = first.size();
        int newLength = selfLength;
        newLength += firstLength;
        for (DoubleSequence o : rest)
            newLength += o.size();
        double[] a = Arrays.copyOf(toArray(), newLength);
        int p = selfLength;
        System.arraycopy(first.toArray(), 0, a, p, firstLength);
        p += firstLength;
        for (DoubleSequence o : rest) {
            final int length = o.size();
            System.arraycopy(o.toArray(), 0, a, p, length);
            p += length;
        }
        return createWithoutCopy(a);
    }

    double[] toArray();

    DoubleStream stream();

}
