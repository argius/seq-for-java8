package net.argius.java8.seq;

import java.util.*;
import java.util.stream.*;

final class DoubleSequenceFactory {

    static final DoubleSequence EMPTY = createWithoutCopy();

    private DoubleSequenceFactory() {
        //
    }

    @SafeVarargs
    static DoubleSequence createWithCopy(double... a) {
        return new DoubleSequenceImpl(Arrays.copyOf(a, a.length));
    }

    @SafeVarargs
    static DoubleSequence createWithoutCopy(double... a) {
        return new DoubleSequenceImpl(a);
    }

    static DoubleSequence createWithoutCopy(DoubleStream stream) {
        return new DoubleSequenceImpl(stream.toArray());
    }

}
