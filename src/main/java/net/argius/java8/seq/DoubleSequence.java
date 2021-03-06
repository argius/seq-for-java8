package net.argius.java8.seq;

import static net.argius.java8.seq.DoubleSequenceFactory.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface DoubleSequence {

    @SafeVarargs
    static DoubleSequence of(double... a) {
        return createWithCopy(a);
    }

    static DoubleSequence of(Collection<Double> collection) {
        return Sequence.of(collection).mapToDouble(Double::doubleValue);
    }

    static DoubleSequence of(DoubleStream stream) {
        return createWithoutCopy(stream.toArray());
    }

    @SafeVarargs
    static DoubleSequence seq(double... a) {
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

    static DoubleSequence generate(int size, DoubleSupplier generator) {
        double[] a = new double[size];
        for (int i = 0; i < size; i++)
            a[i] = generator.getAsDouble();
        return createWithoutCopy(a);
    }

    static DoubleSequence random(int size, double min, double max) {
        // if you need SecureRandom, use generate(int, DoubleSupplier)
        final double distance = max - min + 0.000000000d;
        Random r = new Random(System.currentTimeMillis());
        double[] a = new double[size];
        for (int i = 0; i < size; i++)
            a[i] = min + (int)(r.nextDouble() * distance);
        return createWithoutCopy(a);
    }

    int size();

    double at(int index);

    default DoubleSequence filter(DoublePredicate pred) {
        final int n = size();
        int p = 0;
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            double x = at(i);
            if (pred.test(x))
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

    default DoubleSequence take(int count) {
        return (count == 0) ? empty() : subSequence(0, count - 1);
    }

    default DoubleSequence takeWhile(DoublePredicate pred) {
        final int index = indexWhere(pred.negate());
        return (index > 0) ? subSequence(0, index - 1) : empty();
    }

    default DoubleSequence drop(int count) {
        final int n = size();
        return (count >= n) ? empty() : subSequence(count, n);
    }

    default DoubleSequence subSequence(int from, int to) {
        final int n = size() - 1;
        final int to0 = (to < n) ? to : n;
        return createWithoutCopy(Arrays.copyOfRange(toArray(), from, to0 + 1));
    }

    default OptionalDouble find(DoublePredicate pred) {
        return find(pred, 0);
    }

    default OptionalDouble find(DoublePredicate pred, int start) {
        final int n = size();
        for (int i = start; i < n; i++) {
            final double value = at(i);
            if (pred.test(value))
                return OptionalDouble.of(value);
        }
        return OptionalDouble.empty();
    }

    default boolean exists(DoublePredicate pred) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (pred.test(at(i)))
                return true;
        return false;
    }

    default int indexWhere(DoublePredicate pred) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (pred.test(at(i)))
                return i;
        return -1;
    }

    OptionalDouble max();

    OptionalDouble min();

    default DoubleSequence map(DoubleUnaryOperator mapper) {
        final int n = size();
        double[] a = new double[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsDouble(at(i));
        return createWithoutCopy(a);
    }

    default <R> Sequence<R> mapToObj(DoubleFunction<R> mapper) {
        final int n = size();
        List<R> a = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            a.add(mapper.apply(at(i)));
        return Sequence.of(a);
    }

    default IntSequence mapToInt(DoubleToIntFunction mapper) {
        final int n = size();
        int[] a = new int[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsInt(at(i));
        return IntSequence.of(a);
    }

    default LongSequence mapToLong(DoubleToLongFunction mapper) {
        final int n = size();
        long[] a = new long[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsLong(at(i));
        return LongSequence.of(a);
    }

    default OptionalDouble reduce(DoubleBinaryOperator op) {
        final int n = size();
        if (n == 0)
            return OptionalDouble.empty();
        double result = at(0);
        for (int i = 1; i < n; i++)
            result = op.applyAsDouble(result, at(i));
        return OptionalDouble.of(result);
    }

    default double reduce(double identity, DoubleBinaryOperator op) {
        final int n = size();
        if (n == 0)
            return identity;
        double result = identity;
        for (int i = 0; i < n; i++)
            result = op.applyAsDouble(result, at(i));
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
        for (int i = 0; i < n; i++)
            action.accept(at(i));
    }

    double sum();

    double product();

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
        // XXX copy twice
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
