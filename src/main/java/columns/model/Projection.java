package columns.model;

import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;

/**
 * @author ddimitrov
 * @since Jul 14, 2009
 */

public class Projection<T> {
    private final Comparator<T> grouper;
    private final Summarizer<T> summarizer;
    private final List<Comparator<T>> sorters;

    public Projection(Comparator<T> grouper, Summarizer<T> summarizer, Comparator<T>... sorters) {
        this.grouper = grouper;
        this.summarizer = summarizer;
        if (sorters.length==0) {
            this.sorters = Collections.singletonList(grouper);
        } else {
            this.sorters = Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(sorters, sorters.length)));
        }
    }

    public Comparator<T> getGrouper() {
        return grouper;
    }

    public Summarizer<T> getSummarizer() {
        return summarizer;
    }

    public List<Comparator<T>> getAllSorters() {
        return sorters;
    }
}
