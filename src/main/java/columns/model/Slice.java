package columns.model;

import ca.odell.glazedlists.*;

import java.util.*;

/**
 * @author ddimitrov
 * @since June 12, 2009
 */
public class Slice<T> {
    private final GroupingList<T> groupedInput;
    private final Projection<? super T> projection;

    private ListSelection<List<T>> selection;
    private Collection<?> selectionCriteria = new ArrayList<Object>();
    private SelectionPin selectionPin;
    private SortedList<T> sortedInput;
    private int sorterIdx;

    /**
     * Make sure that input and selectionAggregator share the same lock and publisher and
     * that you acquire them for writing before you invoke this constructor.
     */
    Slice(EventList<T> input, CompositeList<T> selectionAggregator, Projection<? super T> projection) {
        this.projection = projection;
        selectionPin = SelectionPins.nukeOnChange();
        sortedInput = new SortedList<T>(input, projection.getAllSorters().get(sorterIdx));
        groupedInput = new GroupingList<T>(sortedInput, projection.getGrouper());
        selection = new ListSelection<List<T>>(groupedInput);
        selectionAggregator.addMemberList(new CollectionList<List<T>,T>(
                selection.getSelected(),
                GlazedLists.<T>listCollectionListModel()
        ));
    }

    public void dispose() {
        groupedInput.dispose();
        selection.dispose();
    }

    public void setSorterIdx(int sorterIdx) {
        this.sorterIdx = sorterIdx;
    }

    public GroupingList<T> getGroups() {
        return groupedInput;
    }

    public Projection<? super T> getProjection() {
        return projection;
    }

    public void setSelectionCriteria(Collection<?> selectionCriteria) {
        this.selectionCriteria = selectionCriteria;
        int[] indexes = selectionPin.transformCriteriaToIndexes(selectionCriteria);

        try {
            groupedInput.getReadWriteLock().writeLock().lock();
            selection.setSelection(indexes);
        } catch (IndexOutOfBoundsException e) {
            selection.setSelection(new int[0]);
        } finally {
            groupedInput.getReadWriteLock().writeLock().unlock();            
        }
    }

    public void setSelectionPin(SelectionPin selectionPin) {
        this.selectionPin = selectionPin;
    }

    public Collection<?> getSelectionCriteria() {
        return selectionCriteria;
    }

    public SelectionPin getSelectionPin() {
        return selectionPin;
    }
}
