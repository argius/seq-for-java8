package net.argius.java8.seq;

import java.util.*;

final class SequenceFactory {

    static final Sequence<Object> EMPTY = createWithoutCopy();

    private SequenceFactory() {
        //
    }

    @SafeVarargs
    static <E> Sequence<E> createWithoutCopy(E... a) {
        return new SequenceImpl<>(a);
    }

    static <E> Sequence<E> createWithoutCopy(Collection<E> collection) {
        SequenceImpl<E> seq = new SequenceImpl<>(collection);
        return seq;
    }

    @SafeVarargs
    static <E> Sequence<E> createWithCopy(E... a) {
        return new SequenceImpl<>(Arrays.copyOf(a, a.length));
    }

    static <E> Sequence<E> createWithCopy(Collection<E> collection) {
        Collection<E> copy = new ArrayList<>(collection);
        return new SequenceImpl<>(copy);
    }

}
