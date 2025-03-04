import java.util.PriorityQueue;

public class Question1b {

    // Function to find the k-th lowest combined return
    public static int findKthLowest(int[] returns1, int[] returns2, int k) {
        // Create a min-heap to store the combined returns and their indices
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        // Step 1: Add all combinations of the first element in returns1 with the first k elements in returns2
        for (int j = 0; j < returns2.length && j < k; j++) {
            int sum = returns1[0] + returns2[j]; // Calculate the sum
            minHeap.offer(new int[]{sum, 0, j}); // Add the sum and indices to the heap
        }

        // Step 2: Extract the smallest element from the heap k times
        int count = 0; // Counter to track how many elements have been extracted
        while (count < k - 1) { // Stop extracting after k-1 times
            int[] current = minHeap.poll(); // Remove the smallest element from the heap
            int i = current[1]; // Index in returns1
            int j = current[2]; // Index in returns2

            // If there are more elements in returns1 to pair with returns2[j]
            if (i + 1 < returns1.length) {
                int newSum = returns1[i + 1] + returns2[j]; // Calculate the new sum
                minHeap.offer(new int[]{newSum, i + 1, j}); // Add the new sum to the heap
            }

            count++; // Increment the counter
        }

        // Step 3: The k-th smallest combined return is now at the top of the heap
        return minHeap.poll()[0];
    }

    public static void main(String[] args) {
        // Example 1
        int[] returns1 = {2, 5};
        int[] returns2 = {3, 4};
        int k = 2;
        System.out.println("Kth Lowest Return: " + findKthLowest(returns1, returns2, k)); // Output: 8

        // Example 2
        returns1 = new int[]{-4, -2, 0, 3};
        returns2 = new int[]{2, 4};
        k = 6;
        System.out.println("Kth Lowest Return: " + findKthLowest(returns1, returns2, k)); // Output: 0
    }
}
