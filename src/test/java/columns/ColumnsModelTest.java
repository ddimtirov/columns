package columns;

import columns.model.Slice;
import columns.model.Branch;
import columns.model.Projection;
import columns.comparators.ArrayIndexComparator;
import columns.comparators.ArrayIndexSummarizer;

import java.io.*;
import java.util.*;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.EventList;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

/**
 * @author ddimitrov
 * @since June 11, 2009
 */
@Test(description = "Demonstrate how to use the abstract model")
public class ColumnsModelTest {
    private static final int GROWING_DATASETS_COUNT = 1;
    private ArffDataset dataset;

    @Test(description = "Load Data")
    void loadData() throws IOException {
        InputStream stream = ColumnsModelTest.class.getClassLoader().getResourceAsStream("UCI/autos.arff");
        ArffDataset dataset = ArffDataset.loadFromStream(stream);
        stream.close();

        assert dataset.size()>0 : "No data items!";
        this.dataset = dataset;
    }

    @Test(description = "No Slice -> No Selection", dependsOnMethods = "loadData", dataProvider = "growingDatasets")
    void noProjection(int datasetSize) {
        EventList<String[]> eventData = GlazedLists.eventList(createDataset(datasetSize));
        Branch<String[]> branch = new Branch<String[]>(eventData);
        EventList<String[]> selectedItems = branch.getSelectedItems();
        assert selectedItems.isEmpty() : "No slice - no selection";

        Slice<? extends Serializable> slice = branch.createSlice(
                new Projection<String[]>(new ArrayIndexComparator<String>(2), new ArrayIndexSummarizer<String>(2))
        );
        assert selectedItems.isEmpty() : "No selection criteria - no selection";

        slice.setSelectionCriteria(Arrays.asList(0, 1, 2));
        assert !selectedItems.isEmpty() : "Selection failed!";
        
        Branch<String[]> subBranch = new Branch<String[]>(selectedItems);
        Slice<? extends Serializable> subSlice = branch.createSlice(
                new Projection<String[]>(new ArrayIndexComparator<String>(2), new ArrayIndexSummarizer<String>(2))
        );


        subBranch.dispose();
        slice.dispose();
        branch.dispose();
    }

    @Test(description = "No Slice -> Set Selection", dependsOnMethods = "loadData", dataProvider = "growingDatasets")
    void noProjectionX(int datasetSize) {
        EventList<String[]> eventData = GlazedLists.eventList(createDataset(datasetSize));
        Branch<String[]> rootBranch = new Branch<String[]>(eventData);
        EventList<String[]> rootSelectedItems = rootBranch.getSelectedItems();
        assert rootSelectedItems.isEmpty() : "No slice - no selection";

        Slice<? extends Serializable> rootSlice = rootBranch.createSlice(
                new Projection<String[]>(new ArrayIndexComparator<String>(0), new ArrayIndexSummarizer<String>(0))
        );
        assert rootSelectedItems.isEmpty() : "No selection criteria - no selection";

        rootSlice.setSelectionCriteria(Arrays.asList(0, 1, 2));
        assert !rootSelectedItems.isEmpty() : "Selection failed!";

        Branch<? extends String[]> childBranch = new Branch<String[]>(rootSelectedItems);
        Slice<? extends Serializable> childSlice = childBranch.createSlice(
            new Projection<String[]>(new ArrayIndexComparator<String>(1), new ArrayIndexSummarizer<String>(1))
        );
        EventList<?> childSelectedItems = childBranch.getSelectedItems();
        assert childSelectedItems.isEmpty();

        childSlice.setSelectionCriteria(Arrays.asList(0));
        assert !childSelectedItems.isEmpty();

        List<?> oldChildSelectedItems = new ArrayList<Object>(childSelectedItems);
        rootSlice.setSelectionCriteria(Arrays.asList(1));
        assert !oldChildSelectedItems.equals(new ArrayList<Object>(childSelectedItems));

        rootSlice.dispose();
        rootBranch.dispose();

        // TODO: change grouper
        // TODO: change sorter
        // TODO: selection transformer (based on summary)
        // TODO: pinning
        // TODO: multi-createSlice
        // TODO: path support
        // TODO: detached branches (levels deep)
        // TODO: branch merges
    }

    @DataProvider
    Object[][] growingDatasets() {
        Object[][] args = new Object[GROWING_DATASETS_COUNT][];
        for (int i = 0; i < args.length; i++) args[i] = new Object[] { (i+1) * 1000 } ;
        return args;
    }
    private List<String[]> createDataset(int items) {
        if (items>=dataset.size()) {
            Reporter.log("Insufficient data volume in dataset. (requested" + items + ", available " + dataset.size() + ")", 1);
            items=dataset.size()-1;
        }

        String[][] selected = new String[items][];
        System.arraycopy(dataset.getData(), 0, selected, 0, selected.length);
        return Arrays.asList(selected);
    }

}
