/**
 * Stefano Forti - 481183
 */
package edu.stefano.deccen.utils;

/**
 * It represents a couple of nodes.
 *
 * @author Stefano
 */
public class Couple implements Comparable {

    private final long first;
    private final long second;

    public Couple(long a, long b) {
        first = a;
        second = b;
    }

    @Override
    public int compareTo(Object ip) {
        int cmp = compare(first, ((Couple) ip).first);
        if (cmp != 0) {
            return cmp;
        }
        return compare(second, ((Couple) ip).second);

    }

    public static int compare(long a, long b) {
        return (a < b ? -1 : (a == b ? 0 : 1));
    }

    /**
     * Checks if the target equals a. It is used by Closeness Centrality
     * algorithm.
     *
     * @param a
     * @return
     */
    public boolean contains(long a) {
        return a == second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        long a = ((Couple) o).first;
        long b = ((Couple) o).second;

        return a == first && b == second;

    }

    @Override
    public int hashCode() {
        int hash = 5;
        int result = 1;

        result = (int) (hash * result + hash * first + hash * second);

        return result;
    }

}
