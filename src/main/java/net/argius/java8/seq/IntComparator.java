package net.argius.java8.seq;

@FunctionalInterface
public interface IntComparator {

    IntComparator NATURAL = new IntComparator() {
        @Override
        public int compareTo(int a, int b) {
            return (a == b) ? 0 : (a < b) ? -1 : 1;
        }
    };

    IntComparator REVERSE = new IntComparator() {
        @Override
        public int compareTo(int a, int b) {
            return (a == b) ? 0 : (a > b) ? -1 : 1;
        }
    };

    int compareTo(int a, int b);

    default boolean gt(int a, int b) {
        return compareTo(a, b) > 0;
    }

    default boolean eq(int a, int b) {
        return compareTo(a, b) == 0;
    }

    default boolean lt(int a, int b) {
        return compareTo(a, b) < 0;
    }

}
