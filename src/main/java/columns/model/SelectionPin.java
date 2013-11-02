package columns.model;

import java.util.Collection;

/**
 * <p>Selection pins are strategies determining how to handle the selection
 * criteria when the data contents or the data type changes. Certain strategies
 * would anchor the selection to a specific reference point, while others might
 * try to calculate it based on rules (e.g. select all prime numbers).</p>
 *
 * <p>Each selection pin should be able to handle selection criteria of any type.</p>
 *
 * @author ddimitrov
 * @since June 12, 2009
 */
public interface SelectionPin {
    int[] transformCriteriaToIndexes(Collection<?> selectionCriteria);
}
