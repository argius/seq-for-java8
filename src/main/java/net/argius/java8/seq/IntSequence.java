package net.argius.java8.seq;

import static net.argius.java8.seq.IntSequenceFactory.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface IntSequence {

    @SafeVarargs
    static IntSequence of(int... a) {
        return createWithCopy(a);
    }

    static IntSequence of(Collection<Integer> collection) {
        return Sequence.of(collection).mapToInt(Integer::intValue);
    }

    static IntSequence of(IntStream stream) {
        return createWithoutCopy(stream.toArray());
    }

    @SafeVarargs
    static IntSequence seq(int... a) {
        return createWithCopy(a);
    }

    static IntSequence seq(Collection<Integer> collection) {
        return Sequence.of(collection).mapToInt(Integer::intValue);
    }

    static IntSequence seq(IntStream stream) {
        return createWithoutCopy(stream.toArray());
    }

    static IntSequence empty() {
        return IntSequenceFactory.EMPTY;
    }

    static IntSequence generate(int size, IntSupplier generator) {
        int[] a = new int[size];
        for (int i = 0; i < size; i++)
            a[i] = generator.getAsInt();
        return createWithoutCopy(a);
    }

    static IntSequence random(int size, int min, int max) {
        // if you need SecureRandom, use generate(int, IntSupplier)
        final int distance = max - min + 1;
        Random r = new Random(System.currentTimeMillis());
        int[] a = new int[size];
        for (int i = 0; i < size; i++)
            a[i] = min + (int)(r.nextDouble() * distance);
        return createWithoutCopy(a);
    }

    static IntSequence range(int start, int end) {
        final int length = end - start + 1;
        if (length <= 0)
            throw new IllegalArgumentException(String.format("illegal range: %d to %d", start, end));
        int[] a = new int[length];
        for (int i = 0; i < length; i++)
            a[i] = start + i;
        return createWithoutCopy(a);
    }

    static IntSequence range(int start, int end, int step) {
        final int length;
        if (step == 0)
            length = -1;
        else if (step < 0)
            length = (start - end) / (-step) + 1;
        else
            length = (end - start) / step + 1;
        if (length <= 0)
            throw new IllegalArgumentException(String.format("illegal range: %d to %d step %d", start, end, step));
        int[] a = new int[length];
        for (int i = 0; i < length; i++)
            a[i] = start + i * step;
        return createWithoutCopy(a);
    }

    int size();

    int at(int index);

    default IntSequence filter(IntPredicate predicate) {
        final int n = size();
        int p = 0;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            int x = at(i);
            if (predicate.test(x))
                a[p++] = x;
        }
        return createWithoutCopy(Arrays.copyOf(a, p));
    }

    default OptionalInt head() {
        return (size() == 0) ? OptionalInt.empty() : OptionalInt.of(at(0));
    }

    default IntSequence tail() {
        return subSequence(1, Integer.MAX_VALUE);
    }

    default IntSequence take(int count) {
        return (count == 0) ? empty() : subSequence(0, count - 1);
    }

    default IntSequence drop(int count) {
        final int n = size();
        return (count >= n) ? empty() : subSequence(count, n);
    }

    default IntSequence subSequence(int from, int to) {
        final int n = size() - 1;
        final int to0 = (to < n) ? to : n;
        return createWithoutCopy(Arrays.copyOfRange(toArray(), from, to0 + 1));
    }

    default OptionalInt find(IntPredicate pred) {
        return find(pred, 0);
    }

    default OptionalInt find(IntPredicate pred, int start) {
        final int n = size();
        for (int i = start; i < n; i++) {
            final int value = at(i);
            if (pred.test(value))
                return OptionalInt.of(value);
        }
        return OptionalInt.empty();
    }

    default boolean contains(int value) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (at(i) == value)
                return true;
        return false;
    }

    default boolean exists(IntPredicate pred) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (pred.test(at(i)))
                return true;
        return false;
    }

    default int indexOf(int value) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (at(i) == value)
                return i;
        return -1;
    }

    default int indexWhere(IntPredicate pred) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (pred.test(at(i)))
                return i;
        return -1;
    }

    OptionalInt max();

    OptionalInt min();

    default IntSequence map(IntUnaryOperator mapper) {
        final int n = size();
        int[] a = new int[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsInt(at(i));
        return createWithoutCopy(a);
    }

    default <R> Sequence<R> mapToObj(IntFunction<R> mapper) {
        final int n = size();
        List<R> a = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            a.add(mapper.apply(at(i)));
        // TODO factory
        return Sequence.of(a);
    }

    default LongSequence mapToLong(IntToLongFunction mapper) {
        final int n = size();
        long[] a = new long[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsLong(at(i));
        return LongSequence.of(a);
    }

    default DoubleSequence mapToDouble(IntToDoubleFunction mapper) {
        final int n = size();
        double[] a = new double[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsDouble(at(i));
        return DoubleSequence.of(a);
    }

    default OptionalInt reduce(IntBinaryOperator op) {
        final int n = size();
        if (n == 0)
            return OptionalInt.empty();
        int result = at(0);
        for (int i = 1; i < n; i++)
            result = op.applyAsInt(result, at(i));
        return OptionalInt.of(result);
    }

    default int reduce(int identity, IntBinaryOperator op) {
        final int n = size();
        if (n == 0)
            return identity;
        int result = identity;
        for (int i = 0; i < n; i++)
            result = op.applyAsInt(result, at(i));
        return result;
    }

    default int fold(int value, IntBinaryOperator f) {
        switch (size()) {
        case 0:
            return value;
        case 1:
            return f.applyAsInt(value, at(0));
        default:
            return f.applyAsInt(value, tail().fold(at(0), f));
        }
    }

    default void forEach(IntConsumer action) {
        final int n = size();
        for (int i = 0; i < n; i++)
            action.accept(at(i));
    }

    int sum();

    int product();

    default double average() {
        return sum() * 1d / size();
    }

    default IntSequence sort() {
        int[] a = toArray();
        Arrays.sort(a);
        return createWithoutCopy(a);
    }

    default IntSequence sortWith(IntComparator cmp) {
        return sortWith(0, size() - 1, cmp);
    }

    IntSequence sortWith(int fromIndex, int toIndex, IntComparator cmp);

    default IntSequence reverse() {
        int[] a = toArray();
        final int size = a.length;
        final int n = a.length / 2;
        for (int i = 0, j = size - 1; i < n; i++, j--) {
            int x = a[j];
            a[j] = a[i];
            a[i] = x;
        }
        return createWithoutCopy(a);
    }

    default IntSequence distinct() {
        return createWithoutCopy(IntStream.of(toArray()).distinct());
    }

    default IntSequence concat(IntSequence first, IntSequence... rest) {
        final int selfLength = size();
        final int firstLength = first.size();
        int newLength = selfLength;
        newLength += firstLength;
        for (IntSequence o : rest)
            newLength += o.size();
        int[] a = Arrays.copyOf(toArray(), newLength);
        int p = selfLength;
        System.arraycopy(first.toArray(), 0, a, p, firstLength);
        p += firstLength;
        for (IntSequence o : rest) {
            final int length = o.size();
            System.arraycopy(o.toArray(), 0, a, p, length);
            p += length;
        }
        return createWithoutCopy(a);
    }

    int[] toArray();

    default IntStream stream() {
        return IntStream.of(toArray());
    }

}
