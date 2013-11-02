package columns.model;

import java.util.Collection;

/**
 * @author ddimitrov
 * @since June 12, 2009
 */
public class SelectionPins {
    private SelectionPins() { }

    public static SelectionPin nukeOnChange() {
        return new NukeOnChange();
    }

    private static class NukeOnChange implements SelectionPin {
        public int[] transformCriteriaToIndexes(Collection<?> selectionCriteria) {
            int i=0;
            int[] ints = new int[selectionCriteria.size()];
            for (Object o : selectionCriteria) {
                ints[i++] = ((Number) o).intValue();
            }
            return ints;
        }
    }
}
