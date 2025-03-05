package Question4;

import java.util.*;

public class Question4b {

    public int minRoads(int[] packages, int[][] roads) {
        int n = packages.length;
        // If there are no packages, no travel is needed.
        boolean hasPackage = false;
        for (int p : packages) {
            if (p == 1) {
                hasPackage = true;
                break;
            }
        }
        if (!hasPackage) return 0;
        
        // Build undirected graph (tree) as an adjacency list.
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] road : roads) {
            int u = road[0], v = road[1];
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
        
        // To help our greedy selection, choose a “central” node as root.
        int root = findCenter(n, adj);
        
        // Build a BFS tree from the chosen root.
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<>());
        }
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(root);
        visited[root] = true;
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            for (int nei : adj.get(cur)) {
                if (!visited[nei]) {
                    visited[nei] = true;
                    parent[nei] = cur;
                    children.get(cur).add(nei);
                    queue.offer(nei);
                }
            }
        }
        
        // Greedy: choose “stop” nodes in postorder so that every package is covered.
        // A stop covers its own node, its immediate neighbors, and their neighbors (distance 2).
        boolean[] covered = new boolean[n];
        boolean[] stop = new boolean[n];
        List<Integer> postOrder = new ArrayList<>();
        getPostOrder(root, children, postOrder);
        
        // Process nodes in postorder: if a package location is not covered, choose a stop.
        for (int node : postOrder) {
            if (packages[node] == 1 && !covered[node]) {
                // Try to cover as high as possible: choose parent's parent if available,
                // otherwise parent (if exists), otherwise the node itself.
                int p = parent[node];
                int gp = (p == -1 ? -1 : parent[p]);
                int stopNode = (gp != -1 ? gp : (p != -1 ? p : node));
                stop[stopNode] = true;
                // Mark all nodes within distance 2 (using the undirected graph) as covered.
                markCovered(stopNode, adj, covered);
            }
        }
        
        // Compute the minimal subtree spanning all stops in the BFS tree.
        // For each edge in the BFS tree that leads to a subtree containing a stop, we count that edge.
        int[] necessaryEdges = new int[1];
        computeSubtree(root, children, stop, necessaryEdges);
        // Since we must return to the start, each necessary edge is traversed twice.
        return necessaryEdges[0] * 2;
    }
    
    // Find a center of the tree using a “peeling” process.
    private int findCenter(int n, List<List<Integer>> adj) {
        int[] degree = new int[n];
        for (int i = 0; i < n; i++) {
            degree[i] = adj.get(i).size();
        }
        Queue<Integer> leaves = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] <= 1) {
                leaves.offer(i);
            }
        }
        int remaining = n;
        while (remaining > 2) {
            int size = leaves.size();
            remaining -= size;
            for (int i = 0; i < size; i++) {
                int leaf = leaves.poll();
                for (int nei : adj.get(leaf)) {
                    degree[nei]--;
                    if (degree[nei] == 1) {
                        leaves.offer(nei);
                    }
                }
            }
        }
        return leaves.poll();
    }
    
    // Standard DFS to obtain postorder traversal of the BFS tree.
    private void getPostOrder(int node, List<List<Integer>> children, List<Integer> order) {
        for (int child : children.get(node)) {
            getPostOrder(child, children, order);
        }
        order.add(node);
    }
    
    // From a given start node, mark every node within distance 2 as covered.
    private void markCovered(int start, List<List<Integer>> adj, boolean[] covered) {
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{start, 0});
        boolean[] visited = new boolean[covered.length];
        visited[start] = true;
        while (!q.isEmpty()) {
            int[] curr = q.poll();
            int node = curr[0], d = curr[1];
            if (d > 2) continue;
            covered[node] = true;
            for (int nei : adj.get(node)) {
                if (!visited[nei]) {
                    visited[nei] = true;
                    q.offer(new int[]{nei, d + 1});
                }
            }
        }
    }
    
    // DFS on the BFS tree that “prunes” branches not containing a stop.
    // For every child subtree that contains a stop, count that edge.
    private boolean computeSubtree(int node, List<List<Integer>> children, boolean[] stop, int[] count) {
        boolean hasStop = stop[node];
        for (int child : children.get(node)) {
            if (computeSubtree(child, children, stop, count)) {
                hasStop = true;
                count[0]++; // count the edge from node to this child
            }
        }
        return hasStop;
    }
    
    // For testing the sample
    public static void main(String[] args) {
        Question4b solver = new Question4b();
        int[] packages = {1, 0, 0, 0, 0, 1};
        int[][] roads = {
            {0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}
        };
        int result = solver.minRoads(packages, roads);
        System.out.println("Minimum roads traversed: " + result);
        // Expected output is 2.
    }
}
