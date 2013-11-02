package columns.view;

import java.util.EventListener;

/**
 * @author ddimitrov
 * @since Dec 13, 2009
 */
public interface ProjectionListener<T> extends EventListener {
    void selectionChanged(SliceEvent<? extends T> sliceEvent);
}
