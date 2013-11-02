package columns.view;

import columns.model.Branch;
import columns.model.Projection;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;

/**
 * @author ddimitrov
 * @since Jul 4, 2009
 */
public class DrillDownColumnsView<T> extends JComponent implements ProjectionListener {
    private static final long serialVersionUID = 0x20100920;

    private final List<Branch<T>> branches = new ArrayList<Branch<T>>();
    private final List<ProjectionView<? extends T>> views = new ArrayList<ProjectionView<? extends T>>();
    private Component FILLER = Box.createGlue();
    private static final int COLUMN_WIDTH = 200;
    private @Deprecated Projection<T>[] p;

    public DrillDownColumnsView(Branch<T> branch) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        branches.add(branch);
        add(FILLER);
    }

    public int drill(int branchIdx, Projection<T>... projections) {
        Branch<T> branch = branches.get(branchIdx);
        p = projections;

        ProjectionView<T> view = new ProjectionView<T>(
                branch,
                branchIdx % projections.length,
                projections
        );
        view.addProjectionListener(this);
        view.setPreferredSize(new Dimension(COLUMN_WIDTH, 0));
        view.setMaximumSize(new Dimension(COLUMN_WIDTH, Integer.MAX_VALUE));
        view.setMinimumSize(new Dimension(COLUMN_WIDTH, 0));

        views.add(view);
        remove(FILLER);
        add(view);
        add(FILLER);
        revalidate();
        doLayout();

        Container parent = getParent();
        if (parent instanceof JViewport) {
            JViewport scrollPane = (JViewport) parent;
            scrollPane.revalidate();
            scrollPane.scrollRectToVisible(view.getBounds());
        }

        return views.indexOf(view);
    }

    public void selectionChanged(SliceEvent evt) {
        int idx = views.indexOf(evt.getSource());
        if (idx == branches.size()-1) {
            Branch<T> selected = branches.get(idx);
            Branch<T> child = new Branch<T>(selected.getSelectedItems());
            branches.add(child);
            drill(idx + 1, p);
        }
    }
}
