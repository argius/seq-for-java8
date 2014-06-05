package net.argius.java8.seq;

import java.util.*;
import java.util.stream.*;

final class LongSequenceFactory {

    static final LongSequence EMPTY = createWithoutCopy();

    private LongSequenceFactory() {
        //
    }

    @SafeVarargs
    static LongSequence createWithCopy(long... a) {
        return new LongSequenceImpl(Arrays.copyOf(a, a.length));
    }

    @SafeVarargs
    static LongSequence createWithoutCopy(long... a) {
        return new LongSequenceImpl(a);
    }

    static LongSequence createWithoutCopy(LongStream stream) {
        return new LongSequenceImpl(stream.toArray());
    }

}
