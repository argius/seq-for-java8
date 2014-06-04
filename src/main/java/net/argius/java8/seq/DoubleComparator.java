package net.argius.java8.seq;

@FunctionalInterface
public interface DoubleComparator {

    DoubleComparator NATURAL = new DoubleComparator() {
        @Override
        public int compareTo(double a, double b) {
            return (a == b) ? 0 : (a < b) ? -1 : 1;
        }
    };

    DoubleComparator REVERSE = new DoubleComparator() {
        @Override
        public int compareTo(double a, double b) {
            return (a == b) ? 0 : (a > b) ? -1 : 1;
        }
    };

    int compareTo(double a, double b);

    default boolean gt(double a, double b) {
        return compareTo(a, b) > 0;
    }

    default boolean eq(double a, double b) {
        return compareTo(a, b) == 0;
    }

    default boolean lt(double a, double b) {
        return compareTo(a, b) < 0;
    }

}
