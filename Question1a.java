// Question1
// a)
// You have a material with n temperature levels. You know that there exists a critical temperature f where
// 0 <= f <= n such that the material will react or change its properties at temperatures higher than f but
// remain unchanged at or below f.
// Rules:
//  You can measure the material's properties at any temperature level once.
//  If the material reacts or changes its properties, you can no longer use it for further measurements.
//  If the material remains unchanged, you can reuse it for further measurements.
// Goal:
// Determine the minimum number of measurements required to find the critical temperature.
// Input:
//  k: The number of identical samples of the material.
//  n: The number of temperature levels.
// Output:
//  The minimum number of measurements required to find the critical temperature.
// Example 1:
// Input: k = 1, n = 2
// Output: 2
// Explanation:
// Check the material at temperature 1. If its property changes, we know that f = 0.
// Otherwise, raise temperature to 2 and check if property changes. If its property changes, we know that f =
// 1.If its property changes at temperature, then we know f = 2.
// Hence, we need at minimum 2 moves to determine with certainty what the value of f is.
// Example 2:
// Input: k = 2, n = 6
// Output: 3
// Example 3:
// Input: k = 3, n = 14
// Output: 4

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
