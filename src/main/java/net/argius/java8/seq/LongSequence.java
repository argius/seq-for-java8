package net.argius.java8.seq;

import static net.argius.java8.seq.LongSequenceFactory.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface LongSequence {

    @SafeVarargs
    static LongSequence of(long... a) {
        return createWithCopy(a);
    }

    static LongSequence of(Collection<Long> collection) {
        // TODO boxed?
        return Sequence.of(collection).mapToLong(Long::longValue);
    }

    static LongSequence of(LongStream stream) {
        return createWithoutCopy(stream.toArray());
    }

    @SafeVarargs
    static LongSequence seq(long... a) {
        return createWithCopy(a);
    }

    static LongSequence seq(Collection<Long> collection) {
        return Sequence.of(collection).mapToLong(Long::intValue);
    }

    static LongSequence seq(LongStream stream) {
        return createWithoutCopy(stream.toArray());
    }

    static LongSequence empty() {
        return LongSequenceFactory.EMPTY;
    }

    static LongSequence generate(int size, LongSupplier generator) {
        long[] a = new long[size];
        for (int i = 0; i < size; i++)
            a[i] = generator.getAsLong();
        return createWithoutCopy(a);
    }

    static LongSequence random(int size, long min, long max) {
        // if you need SecureRandom, use generate(int, LongSupplier)
        final long distance = max - min + 1;
        Random r = new Random(System.currentTimeMillis());
        long[] a = new long[size];
        for (int i = 0; i < size; i++)
            a[i] = min + (int)(r.nextDouble() * distance);
        return createWithoutCopy(a);
    }

    int size();

    long at(int index);

    default LongSequence filter(LongPredicate pred) {
        final int n = size();
        int p = 0;
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            long x = at(i);
            if (pred.test(x))
                a[p++] = x;
        }
        return createWithoutCopy(Arrays.copyOf(a, p));
    }

    default OptionalLong head() {
        return (size() == 0) ? OptionalLong.empty() : OptionalLong.of(at(0));
    }

    default LongSequence tail() {
        return subSequence(1, Integer.MAX_VALUE);
    }

    default LongSequence take(int count) {
        return (count == 0) ? empty() : subSequence(0, count - 1);
    }

    default LongSequence takeWhile(LongPredicate pred) {
        final int index = indexWhere(pred.negate());
        return (index > 0) ? subSequence(0, index - 1) : empty();
    }

    default LongSequence drop(int count) {
        final int n = size();
        return (count >= n) ? empty() : subSequence(count, n);
    }

    default LongSequence subSequence(int from, int to) {
        final int n = size() - 1;
        final int to0 = (to < n) ? to : n;
        return createWithoutCopy(Arrays.copyOfRange(toArray(), from, to0 + 1));
    }

    default OptionalLong find(LongPredicate pred) {
        return find(pred, 0);
    }

    default OptionalLong find(LongPredicate pred, int start) {
        final int n = size();
        for (int i = start; i < n; i++) {
            final long value = at(i);
            if (pred.test(value))
                return OptionalLong.of(value);
        }
        return OptionalLong.empty();
    }

    default boolean contains(long value) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (at(i) == value)
                return true;
        return false;
    }

    default boolean exists(LongPredicate pred) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (pred.test(at(i)))
                return true;
        return false;
    }

    default int indexOf(long value) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (at(i) == value)
                return i;
        return -1;
    }

    default int indexWhere(LongPredicate pred) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (pred.test(at(i)))
                return i;
        return -1;
    }

    OptionalLong max();

    OptionalLong min();

    default LongSequence map(LongUnaryOperator mapper) {
        final int n = size();
        long[] a = new long[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsLong(at(i));
        return createWithoutCopy(a);
    }

    default <R> Sequence<R> mapToObj(LongFunction<R> mapper) {
        final int n = size();
        List<R> a = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            a.add(mapper.apply(at(i)));
        return Sequence.of(a);
    }

    default IntSequence mapToInt(LongToIntFunction mapper) {
        final int n = size();
        int[] a = new int[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsInt(at(i));
        return IntSequence.of(a);
    }

    default DoubleSequence mapToDouble(LongToDoubleFunction mapper) {
        final int n = size();
        double[] a = new double[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsDouble(at(i));
        return DoubleSequence.of(a);
    }

    default OptionalLong reduce(LongBinaryOperator op) {
        final int n = size();
        if (n == 0)
            return OptionalLong.empty();
        long result = at(0);
        for (int i = 1; i < n; i++)
            result = op.applyAsLong(result, at(i));
        return OptionalLong.of(result);
    }

    default long reduce(long identity, LongBinaryOperator op) {
        final int n = size();
        if (n == 0)
            return identity;
        long result = identity;
        for (int i = 0; i < n; i++)
            result = op.applyAsLong(result, at(i));
        return result;
    }

    default long fold(long value, LongBinaryOperator f) {
        switch (size()) {
        case 0:
            return value;
        case 1:
            return f.applyAsLong(value, at(0));
        default:
            return f.applyAsLong(value, tail().fold(at(0), f));
        }
    }

    default void forEach(LongConsumer action) {
        final int n = size();
        for (int i = 0; i < n; i++)
            action.accept(at(i));
    }

    long sum();

    long product();

    default double average() {
        return sum() * 1d / size();
    }

    default LongSequence sort() {
        long[] a = toArray();
        Arrays.sort(a);
        return createWithoutCopy(a);
    }

    default LongSequence sortWith(LongComparator cmp) {
        return sortWith(0, size() - 1, cmp);
    }

    LongSequence sortWith(int fromIndex, int toIndex, LongComparator cmp);

    default LongSequence reverse() {
        long[] a = toArray();
        final int size = a.length;
        final int n = a.length / 2;
        for (int i = 0, j = size - 1; i < n; i++, j--) {
            long x = a[j];
            a[j] = a[i];
            a[i] = x;
        }
        return createWithoutCopy(a);
    }

    default LongSequence distinct() {
        return createWithoutCopy(stream().distinct());
    }

    default LongSequence concat(LongSequence first, LongSequence... rest) {
        final int selfLength = size();
        final int firstLength = first.size();
        int newLength = selfLength;
        newLength += firstLength;
        for (LongSequence o : rest)
            newLength += o.size();
        // XXX copy twice
        long[] a = Arrays.copyOf(toArray(), newLength);
        int p = selfLength;
        System.arraycopy(first.toArray(), 0, a, p, firstLength);
        p += firstLength;
        for (LongSequence o : rest) {
            final int length = o.size();
            System.arraycopy(o.toArray(), 0, a, p, length);
            p += length;
        }
        return createWithoutCopy(a);
    }

    long[] toArray();

    default LongStream stream() {
        return LongStream.of(toArray());
    }

}
