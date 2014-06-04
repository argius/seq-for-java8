package net.argius.java8.seq;

@FunctionalInterface
public interface LongComparator {

    LongComparator NATURAL = new LongComparator() {
        @Override
        public int compareTo(long a, long b) {
            return (a == b) ? 0 : (a < b) ? -1 : 1;
        }
    };

    LongComparator REVERSE = new LongComparator() {
        @Override
        public int compareTo(long a, long b) {
            return (a == b) ? 0 : (a > b) ? -1 : 1;
        }
    };

    int compareTo(long a, long b);

    default boolean gt(long a, long b) {
        return compareTo(a, b) > 0;
    }

    default boolean eq(long a, long b) {
        return compareTo(a, b) == 0;
    }

    default boolean lt(long a, long b) {
        return compareTo(a, b) < 0;
    }

}
