package columns.comparators;

import columns.model.Summarizer;

import java.util.Comparator;
import java.util.List;
import java.util.Collection;
import java.util.HashSet;

/**
* @author ddimitrov
* @since June 12, 2009
*/
public class ArrayIndexComparator<T extends Comparable<T>> implements Comparator<T[]> {
    int index;

    public ArrayIndexComparator(int index) {
        this.index = index;
    }

    public int compare(T[] o1, T[] o2) {
        return o1[index].compareTo(o2[index]);
    }
}
