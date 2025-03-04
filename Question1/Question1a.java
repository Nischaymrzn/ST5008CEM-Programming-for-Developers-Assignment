package Question1;

public class Question1a {

    // Function to calculate the minimum number of measurements required
    public static int minMeasurements(int k, int n) {
        // Create a 2D DP array with (k+1) rows and (n+1) columns
        int[][] dp = new int[k + 1][n + 1];

        // Base cases:
        // If we have 1 sample, we need n measurements
        for (int j = 1; j <= n; j++) {
            dp[1][j] = j;
        }

        // If we have 0 levels, we need 0 measurements
        for (int i = 1; i <= k; i++) {
            dp[i][0] = 0;
        }

        // Fill the DP table for k samples and n levels
        for (int i = 2; i <= k; i++) { // Start from 2 samples
            for (int j = 1; j <= n; j++) { // Iterate through all levels
                dp[i][j] = Integer.MAX_VALUE; // Initialize to infinity
                int low = 1, high = j;

                // Use binary search to find the optimal split point
                while (low <= high) {
                    int mid = (low + high) / 2;
                    int breaks = dp[i - 1][mid - 1]; // Case when material reacts
                    int doesNotBreak = dp[i][j - mid]; // Case when material does not react
                    int worstCase = 1 + Math.max(breaks, doesNotBreak);

                    dp[i][j] = Math.min(dp[i][j], worstCase);

                    // Adjust binary search range
                    if (breaks > doesNotBreak) {
                        high = mid - 1;
                    } else {
                        low = mid + 1;
                    }
                }
            }
        }

        // Return the result for k samples and n levels
        return dp[k][n];
    }

    // Main method for testing
    public static void main(String[] args) {
        // Example cases
        System.out.println(minMeasurements(1, 2)); // Output: 2
        System.out.println(minMeasurements(2, 6)); // Output: 3
        System.out.println(minMeasurements(3, 14)); // Output: 4
    }
}
