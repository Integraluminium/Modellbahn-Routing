package de.dhbw.modellbahn.plugin;

import org.yaml.snakeyaml.Yaml;

import java.util.*;

/**
 * A track configuration parser that parses the input data and converts it to a string representation.
 * The input data must be in YAML format and contain the components for the graph.
 * The components must contain the following information depending on there type:
 * <ul>
 *     <li>switches:
 *         <ul>
 *             <li>straight: the name of the straight connection</li>
 *             <li>diverging: the name of the diverging connection</li>
 *             <li>root: the name of the root connection</li>
 *         </ul>
 *     </li>
 *     <li>endpoints:
 *         <ul>
 *             <li>cn1: the name of the cn1 connection</li>
 *         </ul>
 *     </li>
 * </ul>
 * The track configuration parser creates a directed graph with the given input data.
 * The directed graph contains the nodes with their available neighbours.
 * The nodes are represented by their name and the neighbours are represented by their name and the tether.
 * The tether is either "straight" or "diverging" for switches and "cn1" for endpoints.
 * <p>
 * Example:<br>
 * <code>
 * As->Bw<br>
 * Aw->Cs<br>
 * Aw->Zs<br>
 * Bs->Aw<br>
 * Bw->Cs<br>
 * </code>
 */
public class TrackConfigParser2 {
    private final Map<String, Map<String, Set<String>>> componentsMap;
    private final Map<String, List<String>> componentsOverview;

    /**
     * Constructs a new track configuration parser with the given input data.
     * The input data must not be null or blank.
     *
     * @param inputData the input data to parse
     * @throws NullPointerException     if the input data is null
     * @throws IllegalArgumentException if the input data is blank
     */
    public TrackConfigParser2(String inputData) {
        Objects.requireNonNull(inputData, "Input data must not be null");
        if (inputData.isBlank()) {
            throw new IllegalArgumentException("Input data must not be blank");
        }

        this.componentsMap = new HashMap<>();
        this.componentsOverview = new HashMap<>();

        parseTrackConfig(inputData);
    }

    /**
     * Converts the parsed track configuration to a string representation.
     * The string representation contains the directed nodes with their neighbours.
     * <p>
     * Example:<br>
     * <code>
     * As->Bw<br>
     * Aw->Cs<br>
     * Aw->Zs<br>
     * Bs->Aw<br>
     * Bw->Cs<br>
     * </code>
     *
     * @return the string representation of the parsed track configuration
     */
    public String convertToString() {
        var nodes = getDirectedNodesWithNeighbours();
        List<Map.Entry<String, List<String>>> sortedNodes = new ArrayList<>(nodes.entrySet());
        sortedNodes.sort(Map.Entry.comparingByKey());
        StringBuilder stringBuilder = new StringBuilder();

        for (var entry : sortedNodes) {
            String nodeName = entry.getKey();
            for (String value : entry.getValue()) {
                stringBuilder
                        .append(nodeName)
                        .append("->").append(value)
                        .append("\n");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Returns an unmodifiable map of the directed nodes with their available neighbours.
     * The key of the map is the node name and the value is a list of the neighbour node names.
     * <p>
     * Example:<br>
     * <code>
     * As-> [Bw]<br>
     * Aw-> [Cs, Zs]<br>
     * Bs-> [Aw]<br>
     * </code>
     *
     * @return an unmodifiable map of the directed nodes with their neighbours
     */
    public Map<String, List<String>> getDirectedNodesWithNeighbours() {
        Map<String, List<String>> directedNodesWithNeighbours = new HashMap<>();

        List<Map.Entry<String, Map<String, Set<String>>>> components = new ArrayList<>(componentsMap.entrySet());
        for (Map.Entry<String, Map<String, Set<String>>> entry : components) {
            String nodeName = entry.getKey();
            for (Map.Entry<String, Set<String>> innerEntry : entry.getValue().entrySet()) {
                var start = nodeName + innerEntry.getKey();
                directedNodesWithNeighbours.put(start, List.copyOf(innerEntry.getValue()));
            }
        }
        return directedNodesWithNeighbours;
    }

    /**
     * Returns an unmodifiable map of the components overview.
     * The map groups in components like "switches" and "endpoints".
     * The value of each key is a list of the component names.
     *
     * @return an unmodifiable map of the components overview
     */
    public Map<String, List<String>> getComponentsOverview() {
        return Collections.unmodifiableMap(componentsOverview);
    }

    /**
     * Parses the track configuration from the given input data.
     * The input data must not be null.
     *
     * @param inputData the input data to parse
     * @throws NullPointerException if the input data is null
     */
    private void parseTrackConfig(String inputData) {
        // Parse the YAML input data
        Yaml yaml = new Yaml();
        Map<String, Map<String, Object>> data = yaml.load(inputData);

        initializeComponentsOverview(data);
        processSwitches(data);
        processEndpoints(data);
    }

    /**
     * Initializes the components overview map with the given data.
     * The data must not be null.
     *
     * @param data the data to initialize the components overview map
     * @throws NullPointerException if the data is null
     */
    private void initializeComponentsOverview(Map<String, Map<String, Object>> data) {
        componentsOverview.put("switches", new ArrayList<>(data.get("switches").keySet()));
        componentsOverview.put("endpoints", new ArrayList<>(data.get("endpoints").keySet()));
    }

    /**
     * Processes the component <b>switches</b> in the given data.
     * The data must not be null.
     *
     * @param data the data to process the switches
     * @throws NullPointerException if the data is null
     */
    private void processSwitches(Map<String, Map<String, Object>> data) {
        for (String switchName : data.get("switches").keySet()) {
            createNodeIfNotExist(switchName);

            @SuppressWarnings("unchecked")  // Have not found another way to solve this problem
            Map<String, Object> switchData = (Map<String, Object>) data.get("switches").get(switchName);

            String straight = (String) switchData.get("straight");
            String diverging = (String) switchData.get("diverging");
            String root = (String) switchData.get("root");

            // Create links for straight, diverging, and root connections
            createLinking(switchName, 2, straight);
            createLinking(switchName, 2, diverging);
            createLinking(switchName, 1, root);
        }
    }

    /**
     * Processes the component <b>endpoints</b> in the given data.
     * The data must not be null.
     *
     * @param data the data to process the endpoints
     * @throws NullPointerException if the data is null
     */
    private void processEndpoints(Map<String, Map<String, Object>> data) {
        for (String endpointName : data.get("endpoints").keySet()) {
            createNodeIfNotExist(endpointName);

            @SuppressWarnings("unchecked")  // Have not found another way to solve this problem
            Map<String, Object> endpointData = (Map<String, Object>) data.get("endpoints").get(endpointName);

            String cn1 = (String) endpointData.get("cn1");

            // Create link for cn1 connection
            createLinking(endpointName, 2, cn1);
        }
    }

    /**
     * Creates a linking between two nodes.
     * The first node is the node with the given name and the second node is the connected node.
     * The index is used to determine the letter of the node.
     * The connected node information contains the connected node name and the tether.
     *
     * @param nodeName                 the name of the node
     * @param index                    the index to determine the letter of the node
     * @param connectedNodeInformation the connected node information
     */
    private void createLinking(String nodeName, int index, String connectedNodeInformation) {
        String[] letters = {"s", "w", "s"};
        Map<String, Set<String>> node = componentsMap.get(nodeName);

        String letter = letters[index];
        String inverse = letters[index - 1];

        String[] parts = connectedNodeInformation.split("\\.");
        String connectedNodeName = parts[0];
        String connectedNodeTether = parts[1];
        createNodeIfNotExist(connectedNodeName);
        Map<String, Set<String>> connectedNode = componentsMap.get(connectedNodeName);

        String connectedLetter;
        String connectedInverse;

        if (componentsOverview.get("switches").contains(connectedNodeName)) {
            int number = connectedNodeTether.equals("root") ? 1 : 2;
            connectedLetter = letters[number];
            connectedInverse = letters[number - 1];
        } else if (componentsOverview.get("endpoints").contains(connectedNodeName)) {
            connectedLetter = "s";
            connectedInverse = "w";
        } else {
            throw new IllegalArgumentException("Invalid connected node");
        }

        node.get(inverse).add(connectedNodeName + connectedLetter);
        connectedNode.get(connectedInverse).add(nodeName + letter);
    }

    private void createNodeIfNotExist(String nodeName) {
        componentsMap.putIfAbsent(nodeName, new HashMap<>() {{
            put("s", new HashSet<>());
            put("w", new HashSet<>());
        }});
    }
}
