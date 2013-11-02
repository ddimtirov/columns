package columns.view;

import columns.model.Slice;
import columns.model.Summarizer;
import columns.model.Branch;
import columns.model.Projection;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;

import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.swing.EventListModel;

/**
 * @author ddimitrov
 * @since Jul 4, 2009
 */
public class ProjectionView<T> extends JComponent {
    private static final long serialVersionUID = 0x20100921;

    private final Branch<T> branch;
    private Slice<T> slice;
    private JList summariesList;
    private Projection<T> cachedProjection;


    public ProjectionView(Branch<T> branch, int selectedProjection, Projection<T>... projections) {
        this.branch = branch;
        summariesList = new JList();

        JComboBox projectionsComboBox = new JComboBox();
        projectionsComboBox.addItemListener(new ProjectionUpdater<T>());
        projectionsComboBox.setModel(new DefaultComboBoxModel(projections));
        if (selectedProjection==projectionsComboBox.getSelectedIndex()) {
            slice(projections[selectedProjection]);
        } else {
            projectionsComboBox.setSelectedIndex(selectedProjection);
        }

        JScrollPane scrollPane = new JScrollPane(summariesList,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JViewport columnHeader = new JViewport();
        columnHeader.setView(projectionsComboBox);
        scrollPane.setColumnHeader(columnHeader);
        this.setLayout(new BorderLayout());
        this.add(scrollPane);
    }


    public void addProjectionListener(ProjectionListener l) {
        listenerList.add(ProjectionListener.class, l);
    }

    public void removeProjectionListener(ProjectionListener l) {
        listenerList.remove(ProjectionListener.class, l);
    }

    private void slice(Projection<T> projection) {
        if (slice!=null) branch.disposeSlice(slice);
        cachedProjection = projection;
        slice = branch.createSlice(cachedProjection);

        FunctionList.Function<java.util.List<T>, String> reduce = new SummaryText<T>(
                slice.getProjection().getSummarizer()
        );
        FunctionList<java.util.List<T>, String> summarizedGroups = new FunctionList<java.util.List<T>, String>(
                slice.getGroups(), reduce
        );
        summariesList.setModel(new EventListModel<String>(summarizedGroups));

        summariesList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                Collection<Integer> selection = new ArrayList<Integer>();
                for (int i : summariesList.getSelectedIndices()) selection.add(i);
                slice.setSelectionCriteria(selection);
                fireSliceSelectionChanged(new SliceEvent<T>(ProjectionView.this, slice));
            }
        });

    }

    private void fireSliceSelectionChanged(SliceEvent<T> sliceEvent) {
        @SuppressWarnings({"unchecked"})
        ProjectionListener<T>[] listeners = (ProjectionListener<T>[]) listenerList.getListeners(ProjectionListener.class);

        for (ProjectionListener<T> listener : listeners) {
            listener.selectionChanged(sliceEvent);
        }
    }

    private static class SummaryText<T> implements FunctionList.Function<java.util.List<T>, String> {
        private final Summarizer<? super T> summarizer;

        public SummaryText(Summarizer<? super T> summarizer) {
            this.summarizer = summarizer;
        }

        public String evaluate(java.util.List<T> group) {
            return String.format("%d - %s", group.size(), summarizer.summarize((Collection) group));
        }
    }

    private class ProjectionUpdater<T> implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            Object item = e.getItem();
            if (item instanceof Projection && item != cachedProjection) {
                slice((Projection) item);
            }
        }
    }
}
