package columns.view;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.awt.*;

import ca.odell.glazedlists.BasicEventList;
import columns.ArffDataset;
import columns.model.Branch;
import columns.comparators.ArrayIndexProjection;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;

/**
 * @author ddimitrov
 * @since Jul 4, 2009
 */

public class CsvColumnsDemo extends SwingWorker<EventList<String[]>, Integer> {
    private volatile JFrame frame;
    private DrillDownColumnsView<String[]> drillDownColumnsView;
    private static final int DATASET_SIZE = 100000;

    protected EventList<String[]> doInBackground() throws Exception {
        InputStream stream = CsvColumnsDemo.class.getClassLoader().getResourceAsStream("UCI/autos.arff");
        ArffDataset data = ArffDataset.loadFromStream(stream);
        stream.close();

        assert data.size()>0 : "No data items!";
        return GlazedLists.eventList(Arrays.asList(data.getData()));
    }


    @Override
    protected void process(List<Integer> chunks) {
        String text = String.format("Loaded %d of %d", chunks.get(chunks.size()-1), DATASET_SIZE);
        JLabel label;
        if (frame==null) {
            label = new JLabel(text, JLabel.CENTER);
            label.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

            frame = new JFrame("Loading Data");
            frame.getContentPane().add(label);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            showAndCenterFrame();
        } else {
            label = (JLabel) frame.getContentPane().getComponents()[0];
        }
        label.setText(text);
        frame.pack();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected void done() {
        try {
            EventList<String[]> eventData = get();
            Branch<String[]> branch = new Branch<String[]>(eventData);
            drillDownColumnsView = new DrillDownColumnsView<String[]>(branch);
            drillDownColumnsView.drill(0,
                    new ArrayIndexProjection("make", 1),
                    new ArrayIndexProjection("body-style", 5),
                    new ArrayIndexProjection("engine-type", 13),
                    new ArrayIndexProjection("num-of-cylinders", 14),
                    new ArrayIndexProjection("fuel-system", 16),
                    new ArrayIndexProjection("fuel-type", 2),
                    new ArrayIndexProjection("aspiration", 3),
                    new ArrayIndexProjection("num-of-doors", 4),
                    new ArrayIndexProjection("drive-wheels", 6),
                    new ArrayIndexProjection("engine-location", 7),
                    new ArrayIndexProjection("symboling", 25)

//                    new ArrayIndexProjection("horsepower", 20),
//                    new ArrayIndexProjection("peak-rpm", 21),
//                    new ArrayIndexProjection("city-mpg", 22),
//                    new ArrayIndexProjection("highway-mpg", 23),
//                    new ArrayIndexProjection("price", 24),
//                    new ArrayIndexProjection("bore", 17),
//                    new ArrayIndexProjection("stroke", 18),
//                    new ArrayIndexProjection("compression-ratio", 19),
//                    new ArrayIndexProjection("engine-size", 15),
//                    new ArrayIndexProjection("wheel-base", 8),
//                    new ArrayIndexProjection("length", 9),
//                    new ArrayIndexProjection("width", 10),
//                    new ArrayIndexProjection("height", 11),
//                    new ArrayIndexProjection("curb-weight", 12),
//                    new ArrayIndexProjection("normalized-losses", 0)
            );

        } catch (InterruptedException e) {
            System.err.println("Interrupted while loading data");
        } catch (ExecutionException e) {
            JOptionPane.showMessageDialog(frame, e.toString(), "Error while loading data", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            if (frame!=null) frame.dispose();
        }

        if (drillDownColumnsView ==null) return;
        drillDownColumnsView.setBorder(new MatteBorder(5,5,5,5, Color.RED));
        frame = new JFrame("Columns Demo");
        JScrollPane scrollPane = new JScrollPane(
                drillDownColumnsView,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);
        frame.getContentPane().add(scrollPane);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 500));
        frame.pack();
        showAndCenterFrame();
    }


    private void showAndCenterFrame() {
        Rectangle screenBounds = GraphicsEnvironment
                                .getLocalGraphicsEnvironment()
                                .getDefaultScreenDevice()
                                .getDefaultConfiguration()
                                .getBounds();

        frame.setLocation(
                screenBounds.x + (screenBounds.width - frame.getWidth()) / 2,
                screenBounds.y + (screenBounds.height - frame.getHeight()) / 2
        );
        frame.setVisible(true);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, ClassNotFoundException, UnsupportedLookAndFeelException, IllegalAccessException, InstantiationException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        CsvColumnsDemo demo = new CsvColumnsDemo();
        demo.execute();
        EventList<String[]> data = demo.get();
        System.out.printf("Started with %d entries\n", data.size());
    }
}
