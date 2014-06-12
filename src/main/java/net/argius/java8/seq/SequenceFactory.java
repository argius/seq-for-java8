package net.argius.java8.seq;

import java.util.*;

final class SequenceFactory {

    static final Sequence<Object> EMPTY = createWithoutCopy();

    private SequenceFactory() {
        //
    }

    @SafeVarargs
    static <T> Sequence<T> createWithoutCopy(T... a) {
        return new SequenceImpl<>(a);
    }

    static <T> Sequence<T> createWithoutCopy(Collection<T> collection) {
        SequenceImpl<T> seq = new SequenceImpl<>(collection);
        return seq;
    }

    @SafeVarargs
    static <T> Sequence<T> createWithCopy(T... a) {
        return new SequenceImpl<>(Arrays.copyOf(a, a.length));
    }

    static <T> Sequence<T> createWithCopy(Collection<T> collection) {
        Collection<T> copy = new ArrayList<>(collection);
        return new SequenceImpl<>(copy);
    }

}
