package net.argius.java8.seq;

import java.util.*;
import java.util.stream.*;

final class IntSequenceFactory {

    static final IntSequence EMPTY = createWithoutCopy();

    private IntSequenceFactory() {
        //
    }

    @SafeVarargs
    static IntSequence createWithCopy(int... a) {
        return new IntSequenceImpl(Arrays.copyOf(a, a.length));
    }

    // static <E> IntSequence createWithCopy(Collection<E> collection) {
    // Collection<E> copy = new ArrayList<>(collection);
    // return new IntSequenceImpl(copy);
    // }

    @SafeVarargs
    static IntSequence createWithoutCopy(int... a) {
        return new IntSequenceImpl(a);
    }

    static IntSequence createWithoutCopy(IntStream stream) {
        return new IntSequenceImpl(stream.toArray());
    }

}
