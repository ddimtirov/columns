package columns.model;

import ca.odell.glazedlists.*;

import java.util.List;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author ddimitrov
 * @since June 12, 2009
 */
public class Branch<T> {
    private final EventList<T> source;
    private final List<Slice<T>> slices = new CopyOnWriteArrayList<Slice<T>>();
    private final CompositeList<T> projectionSelections;

    public Branch(EventList<T> source) {
        this.source = source;
        projectionSelections = new CompositeList<T>(
                source.getPublisher(),
                source.getReadWriteLock()
        );
    }

    public Slice<T> createSlice(Projection<? super T> projection) {
        try {
            source.getReadWriteLock().writeLock().lock();

            Slice<T> slice = new Slice<T>(source, projectionSelections, projection);
            slices.add(slice);

            return slice;
        } finally {
            source.getReadWriteLock().writeLock().unlock();
        }
    }

    public void disposeSlice(Slice<T> slice) {
        slices.remove(slice);
        slice.dispose();
    }

    public EventList<T> getSelectedItems() {
        return projectionSelections;
    }

    public void dispose() {
        // TODO: 
    }
}

