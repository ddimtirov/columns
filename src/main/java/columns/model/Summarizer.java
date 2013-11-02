package columns.model;

import java.util.Collection;

/**
 * @author ddimitrov
 * @since Jul 4, 2009
 */

public interface Summarizer<T> {
    Object summarize(Collection<T> group);
}
