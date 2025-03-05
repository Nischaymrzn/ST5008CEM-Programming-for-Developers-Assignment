package Question5;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Question5 extends JFrame {

    // Graph Data Structures
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private ArrayList<Edge> mstEdges;          // Edges in the computed MST
    private ArrayList<Edge> shortestPathEdges; // Edges in the computed shortest path

    // UI Components
    private GraphPanel graphPanel;
    private JLabel networkInfoLabel; // Shows overall network info
    private JLabel pathInfoLabel;    // Shows shortest path details
    private JComboBox<String> nodeTypeComboBox;
    
    // Interaction Modes
    private enum Mode { ADD_NODE, ADD_EDGE, COMPUTE_SP }
    private Mode currentMode = Mode.ADD_NODE;
    
    // For Edge Creation (ADD_EDGE mode)
    private Node selectedNodeForEdge = null;
    
    // For Shortest Path Computation (COMPUTE_SP mode)
    private Node selectedSPSource = null;
    private Node selectedSPDestination = null;

    public Question5() {
        super("Network Optimizer");
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        mstEdges = new ArrayList<>();
        shortestPathEdges = new ArrayList<>();
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLayout(new BorderLayout());

        // Graph drawing area
        graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);

        // Top control panel with improved styling
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8)); // Increased padding
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Add padding around panel
        
        // Mode selection radio buttons
        JRadioButton addNodeBtn = new JRadioButton("Add Node", true);
        JRadioButton addEdgeBtn = new JRadioButton("Add Edge");
        JRadioButton computeSPBtn = new JRadioButton("Compute Shortest Path");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(addNodeBtn);
        modeGroup.add(addEdgeBtn);
        modeGroup.add(computeSPBtn);
        
        // Add some spacing between radio buttons
        addNodeBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        addEdgeBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        computeSPBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        controlPanel.add(addNodeBtn);
        controlPanel.add(addEdgeBtn);
        controlPanel.add(computeSPBtn);
        
        // Node type selection with better spacing
        JLabel nodeTypeLabel = new JLabel("Node Type:");
        nodeTypeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        controlPanel.add(nodeTypeLabel);
        nodeTypeComboBox = new JComboBox<>(new String[] {"Client", "Server"});
        nodeTypeComboBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        controlPanel.add(nodeTypeComboBox);
        
        // Create a consistent green shade for buttons
        Color greenShade = new Color(134, 206, 134); // Green shade (~300)
        
        // MST button with green styling
        JButton mstButton = new JButton("Compute MST");
        mstButton.setBackground(greenShade);
        mstButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 180, 100), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mstButton.setFocusPainted(false);
        controlPanel.add(mstButton);
        
        // Clear Highlights button with green styling
        JButton clearHighlightsBtn = new JButton("Clear Highlights");
        clearHighlightsBtn.setBackground(greenShade);
        clearHighlightsBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 180, 100), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        clearHighlightsBtn.setFocusPainted(false);
        controlPanel.add(clearHighlightsBtn);
        
        // Clear All button with green styling
        JButton clearAllBtn = new JButton("Clear All");
        clearAllBtn.setBackground(greenShade);
        clearAllBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 180, 100), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        clearAllBtn.setFocusPainted(false);
        controlPanel.add(clearAllBtn);
        
        add(controlPanel, BorderLayout.NORTH);

        // Bottom information panel
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        networkInfoLabel = new JLabel("Network Info: ");
        pathInfoLabel = new JLabel("Shortest Path: ");
        infoPanel.add(networkInfoLabel);
        infoPanel.add(pathInfoLabel);
        add(infoPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        addNodeBtn.addActionListener(e -> {
            currentMode = Mode.ADD_NODE;
            resetSelections();
            graphPanel.repaint();
        });
        addEdgeBtn.addActionListener(e -> {
            currentMode = Mode.ADD_EDGE;
            resetSelections();
            graphPanel.repaint();
        });
        computeSPBtn.addActionListener(e -> {
            currentMode = Mode.COMPUTE_SP;
            resetSelections();
            graphPanel.repaint();
        });
        mstButton.addActionListener(e -> computeMST());
        clearHighlightsBtn.addActionListener(e -> {
            mstEdges.clear();
            shortestPathEdges.clear();
            resetSelections();
            updateNetworkInfo();
            updatePathInfo("Cleared Highlights");
            graphPanel.repaint();
        });
        clearAllBtn.addActionListener(e -> clearAll());

        updateNetworkInfo();
        updatePathInfo("No shortest path computed yet.");
    }
    
    // Reset selections for edge or SP computations.
    private void resetSelections() {
        selectedNodeForEdge = null;
        selectedSPSource = null;
        selectedSPDestination = null;
    }
    
    // Update network summary info.
    private void updateNetworkInfo() {
        double totalCost = 0;
        double totalLatency = 0;
        for (Edge edge : edges) {
            totalCost += edge.cost;
            totalLatency += (1.0 / edge.bandwidth);
        }
        networkInfoLabel.setText(String.format("Nodes: %d, Edges: %d | Total Cost: %.2f, Total Latency: %.2f",
                nodes.size(), edges.size(), totalCost, totalLatency));
    }
    
    // Update shortest path info.
    private void updatePathInfo(String info) {
        pathInfoLabel.setText(info);
    }
    
    // Compute the Minimum Spanning Tree (MST) using Kruskal's algorithm.
    private void computeMST() {
        if (nodes.isEmpty() || edges.isEmpty()) {
            updatePathInfo("Not enough nodes/edges for MST.");
            return;
        }
        mstEdges.clear();
        int n = nodes.size();
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
        
        ArrayList<Edge> sortedEdges = new ArrayList<>(edges);
        sortedEdges.sort(Comparator.comparingDouble(e -> e.cost));
        
        for (Edge edge : sortedEdges) {
            int u = edge.from.id;
            int v = edge.to.id;
            int rootU = find(parent, u);
            int rootV = find(parent, v);
            if (rootU != rootV) {
                mstEdges.add(edge);
                parent[rootU] = rootV;
            }
        }
        
        double mstCost = 0;
        for (Edge edge : mstEdges) {
            mstCost += edge.cost;
        }
        updatePathInfo(String.format("MST computed. Total MST Cost: %.2f", mstCost));
        shortestPathEdges.clear();
        graphPanel.repaint();
    }
    
    // Union-Find helper.
    private int find(int[] parent, int i) {
        if (parent[i] != i) {
            parent[i] = find(parent, parent[i]);
        }
        return parent[i];
    }
    
    // Compute the shortest path using Dijkstra's algorithm.
    // The weight for each edge is based on latency = 1 / bandwidth.
    // Also constructs and displays the path (e.g., "C0 -> S1 -> C2").
    private void computeShortestPath(Node source, Node dest) {
        if (source == null || dest == null) return;
        int n = nodes.size();
        double[] dist = new double[n];
        int[] prev = new int[n];
        Arrays.fill(dist, Double.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[source.id] = 0;
        
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>(Comparator.comparingDouble(nd -> nd.distance));
        queue.add(new NodeDistance(source.id, 0));
        
        while (!queue.isEmpty()) {
            NodeDistance nd = queue.poll();
            int u = nd.nodeId;
            if (nd.distance > dist[u]) continue;
            // Explore adjacent edges (graph is undirected)
            for (Edge edge : edges) {
                int v = -1;
                if (edge.from.id == u) {
                    v = edge.to.id;
                } else if (edge.to.id == u) {
                    v = edge.from.id;
                }
                if (v != -1) {
                    // Weight based on latency (1 / bandwidth)
                    double weight = 1.0 / edge.bandwidth;
                    if (dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                        prev[v] = u;
                        queue.add(new NodeDistance(v, dist[v]));
                    }
                }
            }
        }
        
        // Reconstruct the path from source to destination.
        ArrayList<Integer> path = new ArrayList<>();
        int curr = dest.id;
        while (curr != -1) {
            path.add(curr);
            curr = prev[curr];
        }
        Collections.reverse(path);
        
        if (path.isEmpty() || path.get(0) != source.id) {
            updatePathInfo("No path found between " + source.label + " and " + dest.label);
            return;
        }
        
        // Build a string for the path (e.g., "C0 -> S1 -> C2")
        StringBuilder pathStr = new StringBuilder("Shortest path (latency=1/bandwidth): ");
        for (int i = 0; i < path.size(); i++) {
            pathStr.append(nodes.get(path.get(i)).label);
            if (i < path.size() - 1) {
                pathStr.append(" -> ");
            }
        }
        
        // Identify and store edges along the computed path.
        shortestPathEdges.clear();
        for (int i = 0; i < path.size() - 1; i++) {
            int fromId = path.get(i);
            int toId = path.get(i + 1);
            for (Edge edge : edges) {
                if ((edge.from.id == fromId && edge.to.id == toId) ||
                    (edge.from.id == toId && edge.to.id == fromId)) {
                    shortestPathEdges.add(edge);
                    break;
                }
            }
        }
        
        updatePathInfo(String.format("%s | Total latency: %.2f", pathStr.toString(), dist[dest.id]));
        graphPanel.repaint();
    }
    
    // Clear all nodes, edges, and computed highlights.
    private void clearAll() {
        nodes.clear();
        edges.clear();
        mstEdges.clear();
        shortestPathEdges.clear();
        resetSelections();
        updateNetworkInfo();
        updatePathInfo("Cleared All");
        graphPanel.repaint();
    }
    
    // Inner Classes 
    class Node {
        int id;
        String label;
        int x, y;
        String type; // "Client" or "Server"
        static final int RADIUS = 30;
        
        public Node(int id, int x, int y, String type) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.type = type;
            this.label = type.charAt(0) + "" + id; // e.g., C0 for Client, S1 for Server
        }
        
        // Checks if a point (px, py) lies within this node's circle.
        public boolean containsPoint(int px, int py) {
            int dx = x - px;
            int dy = y - py;
            return dx * dx + dy * dy <= (RADIUS / 2) * (RADIUS / 2);
        }
    }
    
    class Edge {
        Node from;
        Node to;
        double cost;
        double bandwidth;
        public Edge(Node from, Node to, double cost, double bandwidth) {
            this.from = from;
            this.to = to;
            this.cost = cost;
            this.bandwidth = bandwidth;
        }
    }
    
    class NodeDistance {
        int nodeId;
        double distance;
        public NodeDistance(int nodeId, double distance) {
            this.nodeId = nodeId;
            this.distance = distance;
        }
    }
    
    // Custom panel to draw the network and handle mouse interactions.
    class GraphPanel extends JPanel implements MouseListener {
        public GraphPanel() {
            setBackground(Color.WHITE);
            addMouseListener(this);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            // Draw edges
            for (Edge edge : edges) {
                // If the edge is part of the MST, use a thicker red stroke.
                if (mstEdges.contains(edge)) {
                    g2d.setColor(Color.RED);
                    g2d.setStroke(new BasicStroke(3));
                } else if (shortestPathEdges.contains(edge)) {
                    g2d.setColor(Color.BLUE);
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(Color.GRAY);
                    g2d.setStroke(new BasicStroke(1));
                }
                g2d.drawLine(edge.from.x, edge.from.y, edge.to.x, edge.to.y);
                int midX = (edge.from.x + edge.to.x) / 2;
                int midY = (edge.from.y + edge.to.y) / 2;
                g2d.setColor(Color.BLACK);
                g2d.drawString("C:" + edge.cost + " B:" + edge.bandwidth, midX, midY);
            }
            
            // Draw nodes
            for (Node node : nodes) {
                if (node.type.equals("Server")) {
                    g2d.setColor(new Color(144,238,144)); // Light green for servers
                } else {
                    g2d.setColor(new Color(173,216,230)); // Light blue for clients
                }
                g2d.fillOval(node.x - Node.RADIUS/2, node.y - Node.RADIUS/2, Node.RADIUS, Node.RADIUS);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(node.x - Node.RADIUS/2, node.y - Node.RADIUS/2, Node.RADIUS, Node.RADIUS);
                g2d.drawString(node.label, node.x - 10, node.y - Node.RADIUS/2);
                
                // Highlight nodes selected for shortest path computation.
                if (currentMode == Mode.COMPUTE_SP && (node == selectedSPSource || node == selectedSPDestination)) {
                    g2d.setColor(Color.ORANGE);
                    g2d.drawOval(node.x - Node.RADIUS/2 - 3, node.y - Node.RADIUS/2 - 3, Node.RADIUS+6, Node.RADIUS+6);
                }
            }
            
            // Highlight selected node in ADD_EDGE mode.
            if (currentMode == Mode.ADD_EDGE && selectedNodeForEdge != null) {
                g2d.setColor(Color.MAGENTA);
                g2d.drawOval(selectedNodeForEdge.x - Node.RADIUS/2 - 3, selectedNodeForEdge.y - Node.RADIUS/2 - 3, Node.RADIUS+6, Node.RADIUS+6);
            }
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            
            if (currentMode == Mode.ADD_NODE) {
                String type = (String) nodeTypeComboBox.getSelectedItem();
                Node newNode = new Node(nodes.size(), x, y, type);
                nodes.add(newNode);
                updateNetworkInfo();
                repaint();
            } else if (currentMode == Mode.ADD_EDGE) {
                Node clicked = getNodeAt(x, y);
                if (clicked != null) {
                    if (selectedNodeForEdge == null) {
                        selectedNodeForEdge = clicked;
                        repaint();
                    } else if (selectedNodeForEdge != clicked) {
                        String input = JOptionPane.showInputDialog(Question5.this, "Enter cost and bandwidth (comma separated):", "Edge Details", JOptionPane.PLAIN_MESSAGE);
                        if (input != null && !input.trim().isEmpty()) {
                            try {
                                String[] parts = input.split(",");
                                double cost = Double.parseDouble(parts[0].trim());
                                double bandwidth = Double.parseDouble(parts[1].trim());
                                Edge newEdge = new Edge(selectedNodeForEdge, clicked, cost, bandwidth);
                                edges.add(newEdge);
                                updateNetworkInfo();
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(Question5.this, "Invalid input. Please enter cost and bandwidth as numbers separated by a comma.");
                            }
                        }
                        selectedNodeForEdge = null;
                        repaint();
                    }
                }
            } else if (currentMode == Mode.COMPUTE_SP) {
                Node clicked = getNodeAt(x, y);
                if (clicked != null) {
                    if (selectedSPSource == null) {
                        selectedSPSource = clicked;
                        updatePathInfo("Selected source: " + clicked.label);
                        repaint();
                    } else if (selectedSPDestination == null && clicked != selectedSPSource) {
                        selectedSPDestination = clicked;
                        updatePathInfo("Selected destination: " + clicked.label);
                        computeShortestPath(selectedSPSource, selectedSPDestination);
                        resetSelections();
                    } else {
                        updatePathInfo("Source and destination cannot be the same.");
                    }
                }
            }
        }
        
        private Node getNodeAt(int x, int y) {
            for (Node node : nodes) {
                if (node.containsPoint(x, y)) {
                    return node;
                }
            }
            return null;
        }
        
        @Override public void mousePressed(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Question5().setVisible(true));
    }
}

