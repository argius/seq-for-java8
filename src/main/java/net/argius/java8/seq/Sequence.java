package net.argius.java8.seq;

import static net.argius.java8.seq.SequenceFactory.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface Sequence<E> extends Iterable<E> {

    @SafeVarargs
    static <E> Sequence<E> of(E... a) {
        return createWithCopy(a);
    }

    static <E> Sequence<E> of(Collection<E> collection) {
        return createWithCopy(collection);
    }

    static <E> Sequence<E> of(Stream<E> stream) {
        return createWithoutCopy(stream.collect(Collectors.toList()));
    }

    @SafeVarargs
    static <E> Sequence<E> seq(E... a) {
        return createWithCopy(a);
    }

    static <E> Sequence<E> seq(Collection<E> collection) {
        return createWithCopy(collection);
    }

    static <E> Sequence<E> seq(Stream<E> stream) {
        return createWithoutCopy(stream.collect(Collectors.toList()));
    }

    /**
     * Returns an empty Sequence.
     * 
     * @param <E>
     *            generic type of Sequence
     * @return an empty Sequence
     */
    static <E> Sequence<E> empty() {
        @SuppressWarnings("unchecked")
        Sequence<E> seq = (Sequence<E>)SequenceFactory.EMPTY;
        return seq;
    }

    int size();

    default E at(int index) {
        return toArray()[index];
    }

    Sequence<E> filter(Predicate<? super E> predicate);

    default Optional<E> head() {
        return (size() == 0) ? Optional.empty() : Optional.of(at(0));
    }

    default Sequence<E> tail() {
        return (size() > 1) ? subSequence(1, Integer.MAX_VALUE) : empty();
    }

    default Sequence<E> take(int count) {
        return (count == 0) ? empty() : subSequence(0, count - 1);
    }

    default Sequence<E> drop(int count) {
        final int n = size();
        return (count >= n) ? empty() : subSequence(count, n);
    }

    default Sequence<E> subSequence(int from, int to) {
        final int n = size() - 1;
        final int to0 = (to < n) ? to : n;
        return seq(toList().subList(from, to0 + 1));
    }

    default <R> Sequence<R> map(Function<? super E, ? extends R> mapper) {
        final int n = size();
        @SuppressWarnings("unchecked")
        R[] a = (R[])new Object[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.apply(at(i));
        return createWithoutCopy(a);
    }

    default IntSequence mapToInt(ToIntFunction<? super E> mapper) {
        final int n = size();
        int[] a = new int[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsInt(at(i));
        return IntSequenceFactory.createWithoutCopy(a);
    }

    default LongSequence mapToLong(ToLongFunction<? super E> mapper) {
        final int n = size();
        long[] a = new long[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsLong(at(i));
        return LongSequenceFactory.createWithoutCopy(a);
    }

    default DoubleSequence mapToDouble(ToDoubleFunction<? super E> mapper) {
        final int n = size();
        double[] a = new double[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsDouble(at(i));
        return DoubleSequenceFactory.createWithoutCopy(a);
    }

    default Optional<E> reduce(BinaryOperator<E> op) {
        final int n = size();
        if (n == 0)
            return Optional.empty();
        E[] a = toArray();
        E result = a[0];
        for (int i = 1; i < n; i++)
            result = op.apply(result, a[i]);
        return Optional.of(result);
    }

    default E reduce(E identity, BinaryOperator<E> op) {
        final int n = size();
        if (n == 0)
            return identity;
        E[] a = toArray();
        E result = identity;
        for (int i = 0; i < n; i++)
            result = op.apply(result, a[i]);
        return result;
    }

    default E fold(E value, BiFunction<E, E, E> f) {
        switch (size()) {
        case 0:
            return value;
        case 1:
            return f.apply(value, at(0));
        default:
            return f.apply(value, tail().fold(at(0), f));
        }
    }

    default Sequence<E> sort() {
        E[] values = toArray();
        Arrays.sort(values);
        return seq(values);
    }

    default Sequence<E> sortWith(Comparator<E> cmp) {
        E[] values = toArray();
        Arrays.sort(values, cmp);
        return seq(values);
    }

    default Sequence<E> reverse() {
        E[] a = toArray();
        final int size = a.length;
        final int n = a.length / 2;
        for (int i = 0, j = size - 1; i < n; i++, j--) {
            E x = a[j];
            a[j] = a[i];
            a[i] = x;
        }
        return createWithoutCopy(a);
    }

    default Sequence<E> distinct() {
        return createWithoutCopy(new LinkedHashSet<>(toList()));
    }

    @SuppressWarnings("unchecked")
    default Sequence<E> concat(Sequence<? extends E> first, Sequence<? extends E>... rest) {
        // XXX varargs
        final int selfLength = size();
        final int firstLength = first.size();
        int newLength = selfLength;
        newLength += firstLength;
        for (Sequence<? extends E> o : rest)
            newLength += o.size();
        E[] a = Arrays.copyOf(toArray(), newLength);
        int p = selfLength;
        System.arraycopy(first.toArray(), 0, a, p, firstLength);
        p += firstLength;
        for (Sequence<? extends E> o : rest) {
            final int length = o.size();
            System.arraycopy(o.toArray(), 0, a, p, length);
            p += length;
        }
        return createWithoutCopy(a);
    }

    E[] toArray();

    default E[] toArray(IntFunction<E[]> generator) {
        final int n = size();
        E[] a = generator.apply(n);
        for (int i = 0; i < n; i++)
            a[i] = at(i);
        return a;
    }

    /**
     * Returns a String array converted from this sequence.
     * 
     * This method declares for the other methods which requires a String array,
     * such as String.join.
     * 
     * @return a String array converted from this sequence
     */
    default String[] toStringArray() {
        final int n = size();
        String[] a = new String[n];
        for (int i = 0; i < n; i++)
            a[i] = Objects.toString(at(i), null);
        return a;
    }

    default List<E> toList() {
        return Arrays.asList(toArray());
    }

    default Set<E> toSet() {
        Set<E> set = new HashSet<>();
        Collections.addAll(set, toArray());
        return set;
    }

    default <R> Map<E, R> toMapWithKey(Function<? super E, ? extends R> mapper) {
        Map<E, R> m = new HashMap<>();
        for (E k : toArray())
            m.put(k, mapper.apply(k));
        return m;
    }

    default <R> Map<R, E> toMapWithValue(Function<? super E, ? extends R> mapper) {
        Map<R, E> m = new HashMap<>();
        for (E k : toArray())
            m.put(mapper.apply(k), k);
        return m;
    }

    default Stream<E> stream() {
        return toList().stream();
    }

}
