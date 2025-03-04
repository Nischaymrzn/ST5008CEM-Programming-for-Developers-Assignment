import java.util.*;

public class Question3a {

    // Union-Find (Disjoint Set Union) with path compression and union by rank.
    static class UnionFind {
        int[] parent, rank;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return false;
            
            // Union by rank.
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }
    
    // Class representing an edge (connection) in the graph.
    static class Connection {
        int cost;
        int device1;
        int device2;
        
        public Connection(int cost, int device1, int device2) {
            this.cost = cost;
            this.device1 = device1;
            this.device2 = device2;
        }
    }
    
    // Returns the minimum total cost to connect all devices.
    public int minimumCost(int n, int[] modules, int[][] connections) {
        List<Connection> edges = new ArrayList<>();
        
        // Create edges from each device (0-indexed) to a virtual "super device" (node n)
        // with cost equal to the module installation cost.
        for (int i = 0; i < n; i++) {
            edges.add(new Connection(modules[i], i, n));
        }
        
        // Add the provided connections.
        // Note: The given device indices are 1-based; converting them to 0-based.
        for (int[] conn : connections) {
            edges.add(new Connection(conn[2], conn[0] - 1, conn[1] - 1));
        }
        
        // Sort all edges by cost in ascending order.
        edges.sort(Comparator.comparingInt(e -> e.cost));
        
        // Initialize Union-Find for n devices plus the virtual super device.
        UnionFind uf = new UnionFind(n + 1);
        int totalCost = 0;
        int edgesUsed = 0;
        
        // Apply Kruskal's algorithm.
        for (Connection edge : edges) {
            if (uf.union(edge.device1, edge.device2)) {
                totalCost += edge.cost;
                edgesUsed++;
                // Since there are n+1 nodes, an MST will have exactly n edges.
                if (edgesUsed == n) {
                    break;
                }
            }
        }
        
        return totalCost;
    }
    
    public static void main(String[] args) {
        Question3a solution = new Question3a();
        
        // Example test case.
        int n = 3;
        int[] modules = {1, 2, 2};
        int[][] connections = { {1, 2, 1}, {2, 3, 1} };
        System.out.println(solution.minimumCost(n, modules, connections));  // Expected output: 3
    }
}
