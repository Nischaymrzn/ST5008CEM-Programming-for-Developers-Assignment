package Question3;

import java.util.*;

public class Question3a {

    // Union-Find (Disjoint Set Union) with path compression and union by rank.
    static class UnionFind {
        int[] parent, rank;
        
        // Constructor to initialize Union-Find data structure.
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            // Initially, each element is its own parent (each node is a separate set).
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        
        // The find method with path compression.
        // It recursively finds the root of x and compresses the path.
        public int find(int x) {
            if (parent[x] != x) {
                // Recursively find the root and set the parent of x directly to the root.
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        // Union method combines the sets that contain x and y.
        // It returns false if x and y are already in the same set.
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            // If both elements have the same root, they are already connected.
            if (rootX == rootY) return false;
            
            // Union by rank: attach the tree with lower rank to the tree with higher rank.
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                // If both trees have the same rank, choose one as the new root and increment its rank.
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }
    
    // Class representing an edge (connection) in the graph.
    // Each edge connects two devices (or a device and the super device) and has an associated cost.
    static class Connection {
        int cost;
        int device1;
        int device2;
        
        // Constructor to initialize an edge with cost and endpoints.
        public Connection(int cost, int device1, int device2) {
            this.cost = cost;
            this.device1 = device1;
            this.device2 = device2;
        }
    }
    
    // Method to calculate the minimum total cost to connect all devices.
    public int minimumCost(int n, int[] modules, int[][] connections) {
        // List to store all potential edges for the MST.
        List<Connection> edges = new ArrayList<>();
        
        // Create edges from each device (0-indexed) to a virtual "super device" (node n).
        // The cost of each edge is the module installation cost for that device.
        for (int i = 0; i < n; i++) {
            edges.add(new Connection(modules[i], i, n));
        }
        
        // Add the provided connections between devices.
        // Note: The given device indices are 1-based, so we convert them to 0-based.
        for (int[] conn : connections) {
            // conn[2] is the cost, and conn[0]-1 and conn[1]-1 convert to 0-indexed.
            edges.add(new Connection(conn[2], conn[0] - 1, conn[1] - 1));
        }
        
        // Sort all edges by cost in ascending order for Kruskal's algorithm.
        edges.sort(Comparator.comparingInt(e -> e.cost));
        
        // Initialize Union-Find for n devices plus the virtual super device.
        UnionFind uf = new UnionFind(n + 1);
        int totalCost = 0;
        int edgesUsed = 0;
        
        // Apply Kruskal's algorithm to build the Minimum Spanning Tree (MST).
        // Loop through all sorted edges and add them if they connect two separate components.
        for (Connection edge : edges) {
            if (uf.union(edge.device1, edge.device2)) {
                // If the edge was added successfully (connecting two separate trees),
                // update the total cost and increment the count of used edges.
                totalCost += edge.cost;
                edgesUsed++;
                // Since there are n+1 nodes, a valid MST will include exactly n edges.
                if (edgesUsed == n) {
                    break;
                }
            }
        }
        
        // Return the minimum total cost calculated for connecting all devices.
        return totalCost;
    }
    
    public static void main(String[] args) {
        Question3a solution = new Question3a();
        
        // Example test case.
        // n: number of devices.
        int n = 3;
        // modules: installation cost for each device's module.
        int[] modules = {1, 2, 2};
        // connections: Each connection array has [device1, device2, cost].
        // Device indices are 1-based in the input.
        int[][] connections = { {1, 2, 1}, {2, 3, 1} };
        // Expected output is 3, which is the minimum cost to connect all devices.
        System.out.println(solution.minimumCost(n, modules, connections));  // Expected output: 3
    }
}
