package columns.comparators;

import columns.model.Projection;

import java.util.Comparator;

/**
 * @author ddimitrov
 * @since Jul 14, 2009
 */

@SuppressWarnings({"unchecked"})
public class ArrayIndexProjection<T extends Comparable<T>> extends Projection<T> {
    private final String name;

    public ArrayIndexProjection(int keyIdx, int... sortingIndices) {
        this(null, keyIdx, sortingIndices);
    }
    public ArrayIndexProjection(String name, int keyIdx, int... sortingIndices) {
        super(new ArrayIndexComparator(keyIdx), new ArrayIndexSummarizer(keyIdx), toComparators(sortingIndices));
        this.name = name;
    }

    private static Comparator[] toComparators(int[] indices) {
        final Comparator[] comparators = new Comparator[indices.length];
        for (int i = 0; i < comparators.length; i++) {
            comparators[i] = new ArrayIndexComparator(indices[i]);
        }
        return comparators;
    }

    @Override
    public String toString() {
        return name==null ? super.toString() : name;
    }
}
