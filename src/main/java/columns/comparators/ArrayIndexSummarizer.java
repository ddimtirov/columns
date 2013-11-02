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
public class ArrayIndexSummarizer<T extends Comparable<T>> implements Summarizer<T[]> {
    int index;

    public ArrayIndexSummarizer(int index) {
        this.index = index;
    }

    public Object summarize(Collection<T[]> group) {
        Collection<T> summary = new HashSet<T>();
        for (T[] item : group) summary.add(item[index]);

        if (summary.isEmpty()) {
            throw new IllegalArgumentException("Group contains no items: " + summary);
        } else if (summary.size() > 1) {
            throw new IllegalArgumentException("Group contains heterogenous items: " + summary);
        }
        return summary.iterator().next();
    }
}