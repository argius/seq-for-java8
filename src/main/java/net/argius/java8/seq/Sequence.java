package net.argius.java8.seq;

import static net.argius.java8.seq.SequenceFactory.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface Sequence<T> extends Iterable<T> {

    @SafeVarargs
    static <T> Sequence<T> of(T... a) {
        return createWithCopy(a);
    }

    static <T> Sequence<T> of(Collection<T> collection) {
        return createWithCopy(collection);
    }

    static <T> Sequence<T> of(Stream<T> stream) {
        return createWithoutCopy(stream.collect(Collectors.toList()));
    }

    @SafeVarargs
    static <T> Sequence<T> seq(T... a) {
        return createWithCopy(a);
    }

    static <T> Sequence<T> seq(Collection<T> collection) {
        return createWithCopy(collection);
    }

    static <T> Sequence<T> seq(Stream<T> stream) {
        return createWithoutCopy(stream.collect(Collectors.toList()));
    }

    static <T> Sequence<T> empty() {
        @SuppressWarnings("unchecked")
        Sequence<T> seq = (Sequence<T>)SequenceFactory.EMPTY;
        return seq;
    }

    int size();

    T at(int index);

    Sequence<T> filter(Predicate<? super T> predicate);

    default Optional<T> head() {
        return (size() == 0) ? Optional.empty() : Optional.of(at(0));
    }

    default Sequence<T> tail() {
        return (size() > 1) ? subSequence(1, Integer.MAX_VALUE) : empty();
    }

    default Sequence<T> take(int count) {
        return (count == 0) ? empty() : subSequence(0, count - 1);
    }

    default Sequence<T> takeWhile(Predicate<T> pred) {
        final int index = indexWhere(pred.negate());
        return (index > 0) ? subSequence(0, index - 1) : empty();
    }

    default Sequence<T> drop(int count) {
        final int n = size();
        return (count >= n) ? empty() : subSequence(count, n);
    }

    default Sequence<T> subSequence(int from, int to) {
        final int n = size() - 1;
        final int to0 = (to < n) ? to : n;
        return seq(toList().subList(from, to0 + 1));
    }

    default Optional<T> find(Predicate<T> pred) {
        return find(pred, 0);
    }

    default Optional<T> find(Predicate<T> pred, int start) {
        final int n = size();
        for (int i = start; i < n; i++) {
            T value = at(i);
            if (pred.test(value))
                return Optional.of(value);
        }
        return Optional.empty();
    }

    default boolean contains(T o) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (Objects.equals(at(i), o))
                return true;
        return false;
    }

    default boolean exists(Predicate<T> pred) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (pred.test(at(i)))
                return true;
        return false;
    }

    default int indexOf(T o) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (Objects.equals(at(i), o))
                return i;
        return -1;
    }

    default int indexWhere(Predicate<T> pred) {
        final int n = size();
        for (int i = 0; i < n; i++)
            if (pred.test(at(i)))
                return i;
        return -1;
    }

    default <R> Sequence<R> map(Function<? super T, ? extends R> mapper) {
        final int n = size();
        @SuppressWarnings("unchecked")
        R[] a = (R[])new Object[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.apply(at(i));
        return createWithoutCopy(a);
    }

    default IntSequence mapToInt(ToIntFunction<? super T> mapper) {
        final int n = size();
        int[] a = new int[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsInt(at(i));
        return IntSequenceFactory.createWithoutCopy(a);
    }

    default LongSequence mapToLong(ToLongFunction<? super T> mapper) {
        final int n = size();
        long[] a = new long[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsLong(at(i));
        return LongSequenceFactory.createWithoutCopy(a);
    }

    default DoubleSequence mapToDouble(ToDoubleFunction<? super T> mapper) {
        final int n = size();
        double[] a = new double[n];
        for (int i = 0; i < n; i++)
            a[i] = mapper.applyAsDouble(at(i));
        return DoubleSequenceFactory.createWithoutCopy(a);
    }

    default Optional<T> reduce(BinaryOperator<T> op) {
        final int n = size();
        if (n == 0)
            return Optional.empty();
        T result = at(0);
        for (int i = 1; i < n; i++)
            result = op.apply(result, at(i));
        return Optional.of(result);
    }

    default T reduce(T identity, BinaryOperator<T> op) {
        final int n = size();
        if (n == 0)
            return identity;
        T result = identity;
        for (int i = 0; i < n; i++)
            result = op.apply(result, at(i));
        return result;
    }

    default T fold(T value, BiFunction<T, T, T> f) {
        switch (size()) {
        case 0:
            return value;
        case 1:
            return f.apply(value, at(0));
        default:
            return f.apply(value, tail().fold(at(0), f));
        }
    }

    default Sequence<T> sort() {
        T[] values = toArray();
        Arrays.sort(values);
        return seq(values);
    }

    default Sequence<T> sortWith(Comparator<T> cmp) {
        T[] values = toArray();
        Arrays.sort(values, cmp);
        return seq(values);
    }

    default Sequence<T> reverse() {
        T[] a = toArray();
        final int size = a.length;
        final int n = a.length / 2;
        for (int i = 0, j = size - 1; i < n; i++, j--) {
            T x = a[j];
            a[j] = a[i];
            a[i] = x;
        }
        return createWithoutCopy(a);
    }

    default Sequence<T> distinct() {
        return createWithoutCopy(new LinkedHashSet<>(toList()));
    }

    @SuppressWarnings("unchecked")
    default Sequence<T> concat(Sequence<? extends T> first, Sequence<? extends T>... rest) {
        // XXX varargs
        final int selfLength = size();
        final int firstLength = first.size();
        int newLength = selfLength;
        newLength += firstLength;
        for (Sequence<? extends T> o : rest)
            newLength += o.size();
        // XXX copy twice
        T[] a = Arrays.copyOf(toArray(), newLength);
        int p = selfLength;
        System.arraycopy(first.toArray(), 0, a, p, firstLength);
        p += firstLength;
        for (Sequence<? extends T> o : rest) {
            final int length = o.size();
            System.arraycopy(o.toArray(), 0, a, p, length);
            p += length;
        }
        return createWithoutCopy(a);
    }

    T[] toArray();

    default T[] toArray(IntFunction<T[]> generator) {
        final int n = size();
        T[] a = generator.apply(n);
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

    default List<T> toList() {
        return Arrays.asList(toArray());
    }

    default Set<T> toSet() {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, toArray());
        return set;
    }

    default <R> Map<T, R> toMapWithKey(Function<? super T, ? extends R> mapper) {
        Map<T, R> m = new HashMap<>();
        for (T k : this)
            m.put(k, mapper.apply(k));
        return m;
    }

    default <R> Map<R, T> toMapWithValue(Function<? super T, ? extends R> mapper) {
        Map<R, T> m = new HashMap<>();
        for (T k : this)
            m.put(mapper.apply(k), k);
        return m;
    }

    default Stream<T> stream() {
        return toList().stream();
    }

}
