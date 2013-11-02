package columns.view;

import columns.model.Slice;

import java.util.EventObject;

/**
 * @author ddimitrov
 * @since Jul 26, 2009
 */

public class SliceEvent<T> extends EventObject {
    private static final long serialVersionUID = 0x20100923;

    private final Slice<T> slice;

    public SliceEvent(ProjectionView<T> source, Slice<T> slice) {
        super(source);
        this.slice = slice;
    }

    public Slice<T> getSlice() {
        return slice;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public ProjectionView<T> getSource() {
        return (ProjectionView<T>) super.getSource();
    }
}
