package columns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author ddimitrov
 * @since Feb 20, 2010
 */
public class ArffDataset {
    private final String name;
    private final String meta;
    private final Map<String, String> attribute2type;
    private final Collection<String[]> data;

    public ArffDataset(String name, String meta, Map<String, String> attribute2type, Collection<String[]> data) {
        this.name = name;
        this.meta = meta;
        this.attribute2type = Collections.unmodifiableMap(attribute2type);
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String getMeta() {
        return meta;
    }

    public String[] getAttributes() {
        Set<String> attrNames = attribute2type.keySet();
        return attrNames.toArray(new String[attrNames.size()]);
    }

    public String[][] getData() { // TODO: specify which attributes and rows
        return data.toArray(new String[data.size()][]);
    }

    public int size() {
        return data.size();
    }

    public static ArffDataset loadFromStream(InputStream input) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(input));

        String name = "<unnamed>";
        StringBuilder meta = new StringBuilder();
        Map<String, String> attribute2type = new LinkedHashMap<String, String>();
        while (true) {
            String line = in.readLine();
            if (line==null) return null;
            if (line.startsWith("@data")) break;

            if (line.startsWith("%")) {
                meta.append(line.substring(1)).append('\n');
            } else if (line.startsWith("@relation")) {
                name = line.replaceAll("^@relation", "").trim();
            } else if (line.startsWith("@attribute")) {
                String[] strings = line.split(" ", 3);
                attribute2type.put(strings[1].replace("'", ""), strings[2].trim());
            }
        }

        Collection<String[]> data = new ArrayList<String[]>();
        while (true) {
            String line = in.readLine();
            if (line==null) break;
            if (line.trim().isEmpty() || line.startsWith("%")) continue;

            String[] parsed = line.split(",(?![^\\S])");
            for (int i = 0; i < parsed.length; i++) parsed[i] = parsed[i].intern();
            data.add(parsed);
        }

        return new ArffDataset(name, meta.toString(), attribute2type, data);
    }
}
