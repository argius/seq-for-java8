package net.argius.java8.seq;

import static net.argius.java8.seq.IntSequenceFactory.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * IntSequence is the variation of Sequence, that is primitive int version.
 */
public interface IntSequence {

    // factories

    static IntSequence of(int... a) {
        return createWithCopy(a);
    }

    static IntSequence of(Collection<Integer> collection) {
        return Sequence.of(collection).mapToInt(Integer::intValue);
    }

    static IntSequence of(IntStream stream) {
        return createWithoutCopy(stream.toArray());
    }

    static IntSequence seq(int... a) {
        return createWithCopy(a);
    }

    static IntSequence seq(Collection<Integer> collection) {
        return Sequence.of(collection).mapToInt(Integer::intValue);
    }

    static IntSequence seq(IntStream stream) {
        return createWithoutCopy(stream.toArray());
    }

    static IntSequence range(int start, int end) {
        IntUnaryOperator generator = x -> x + 1;
        return range(start, end, generator);
    }

    static IntSequence range(int start, int end, IntUnaryOperator adder) {
        int length = end - start + 1;
        // TODO range check
        // assert length >= 0;
        if (length <= 0)
            return empty();
        if (length == 1)
            return seq(start);
        int[] a = new int[length];
        a[0] = start;
        int x = start;
        int newLength = 1;
        for (int i = 1;; i++) {
            x = adder.applyAsInt(x);
            if (x > end)
                break;
            a[i] = x;
            ++newLength;
        }
        return createWithoutCopy(Arrays.copyOf(a, newLength));
    }

    static IntSequence empty() {
        return IntSequenceFactory.EMPTY;
    }

    // accessors

    int size();

    int at(int index);

    default IntSequence subSequence(int from, int to) {
        final int n = size() - 1;
        final int to0 = (to < n) ? to : n;
        return createWithoutCopy(Arrays.copyOfRange(toArray(), from, to0 + 1));
    }

    // filters

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

    OptionalInt max();

    OptionalInt min();

    // maps

    default IntSequence map(IntUnaryOperator mapper) {
        final int n = size();
        int[] values = toArray();
        int[] a = new int[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsInt(values[i]);
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

    // reduces

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

    default int reduce(int identity, IntBinaryOperator op) {
        // TODO check
        int result = identity;
        for (int element : toArray())
            result = op.applyAsInt(result, element);
        return result;
    }

    default IntSequence distinct() {
        return createWithoutCopy(IntStream.of(toArray()).distinct());
    }

    int sum();

    default double average() {
        return sum() * 1d / size();
    }

    // sorts

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

    // mergers

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

    // converters

    default IntStream stream() {
        return IntStream.of(toArray());
    }

    int[] toArray();

    // terminators

    default void forEach(IntConsumer action) {
        final int n = size();
        int[] values = toArray();
        for (int i = 0; i < n; i++)
            action.accept(values[i]);
    }

}
